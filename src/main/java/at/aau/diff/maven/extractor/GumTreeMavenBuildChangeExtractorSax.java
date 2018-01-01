package at.aau.diff.maven.extractor;

import java.util.ArrayList;
import java.util.List;

import at.aau.diff.common.Change;
import at.aau.diff.maven.MavenBuildChange;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.ITree;

//cat GumTreeMavenBuildChangeExtractorSax.java | grep "return new MavenBuildChange" | cut -d'"' -f2 | uniq | sort | uniq 
public class GumTreeMavenBuildChangeExtractorSax {

	private static final String COMPLEX_SEPARATOR = "/::/::/";
	
	protected static List<ITree> dontProcessChildrenList = new ArrayList<ITree>();

	public static List<Change> extract(List<Action> actions, ActionGenerator actionGenerator, MappingStore mappingStore) {
//		System.out.println("ACTIONSCOUNT: "+actions.size());
		List<Change> changes = new ArrayList<Change>();
		resetDontProcessChildrenList();
		for (Action action : actions) {
//			System.out.println(action);
			MavenBuildChange buildChange = getBuildChange(action);


			if (buildChange != null) {
                buildChange.setAction(action);
                addLocationInformation(action,buildChange,actionGenerator,mappingStore);
//				 System.out.println(buildChange.getChangeTyp()+" "+buildChange.getChangeSubject());
				changes.add(buildChange);
			}
		}
		return changes;
	}

    private static void addLocationInformation(Action action, MavenBuildChange buildChange, ActionGenerator actionGenerator, MappingStore mappingStore) {
	    try {
            if (action instanceof Insert) {
                Insert i = (Insert) action;
                ITree positionNode = i.getNode();
                while (positionNode.getParent() != null && (positionNode.getPos() == 0 || positionNode.getPos() == -1)) {
                    positionNode = positionNode.getParent();
                }
                buildChange.getDstPositionInfo().setStartLineNumber(positionNode.getPos() / 10000);
                buildChange.getDstPositionInfo().setEndLineNumber(positionNode.getPos() % 10000);
                buildChange.getDstPositionInfo().setStartLineOffset(positionNode.getLength() / 10000);
                buildChange.getDstPositionInfo().setEndLineOffset(positionNode.getLength() % 10000);
                buildChange.setChangeType("INSERT");
            }
            if (action instanceof Update) {
                //TODO create the TWO position nodes here
                Update u = (Update) action;
                ITree srcPositionNode = u.getNode();
                buildChange.getSrcPositionInfo().setStartLineNumber(srcPositionNode.getPos() / 10000);
                buildChange.getSrcPositionInfo().setEndLineNumber(srcPositionNode.getPos() % 10000);
                buildChange.getSrcPositionInfo().setStartLineOffset(srcPositionNode.getLength() / 10000);
                buildChange.getSrcPositionInfo().setEndLineOffset(srcPositionNode.getLength() % 10000);
                //TODO we need to get this info
                ITree dstPositionNode = mappingStore.getDst(u.getNode());
                //mappingStore.hasDst(u.getNode())

                buildChange.getDstPositionInfo().setStartLineNumber(dstPositionNode.getPos() / 10000);
                buildChange.getDstPositionInfo().setEndLineNumber(dstPositionNode.getPos() % 10000);
                buildChange.getDstPositionInfo().setStartLineOffset(dstPositionNode.getLength() / 10000);
                buildChange.getDstPositionInfo().setEndLineOffset(dstPositionNode.getLength() % 10000);
                buildChange.setChangeType("UPDATE");
            }
            if (action instanceof Delete) {
                Delete d = (Delete) action;
                ITree positionNode = d.getNode();
                while (positionNode.getParent() != null && (positionNode.getPos() == 0 || positionNode.getPos() == -1)) {
                    positionNode = positionNode.getParent();
                }
                buildChange.getSrcPositionInfo().setStartLineNumber(positionNode.getPos() / 10000);
                buildChange.getSrcPositionInfo().setEndLineNumber(positionNode.getPos() % 10000);
                buildChange.getSrcPositionInfo().setStartLineOffset(positionNode.getLength() / 10000);
                buildChange.getSrcPositionInfo().setEndLineOffset(positionNode.getLength() % 10000);
                buildChange.setChangeType("DELETE");
            }
            if (action instanceof Move) {
                //ignore
            }
        }catch(Exception e) {
	        //currently: if we cannot infer the positions, do nothing to not break the build change extraction
        }
    }

    public static MavenBuildChange getBuildChange(Action action) {
//		if(action.getNode().getType()==10 || action.getNode().getType()==15) {  
			String tagName = getTagName(action.getNode());
//			System.out.println(tagName);
			boolean parentIsInDontProcessChildrenList = parentIsInDontProcessChildrenList(action.getNode());
			if (!parentIsInDontProcessChildrenList) {
				if (action instanceof Insert) {
					return handleInsertAction((Insert) action,tagName);
				}
				if (action instanceof Update) {
					return handleUpdateAction((Update) action,tagName);
				}
				if (action instanceof Delete) {
					return handleDeleteAction((Delete) action,tagName);
				}
				if (action instanceof Move) {
					// we can ignore move actions as the do not have
					// an impact on the maven build (order is neglected in the
					// pom.xml)
				}
			}
//		}

		return null;
	}

	private static MavenBuildChange handleInsertAction(Insert action, String tagName) {
		ITree node = action.getNode();
		
		if("resource".equals(tagName) &&hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("RESOURCE_INSERT","",getChildWithName(node, "directory").getChild(0).getLabel());
		}
		if("testResource".equals(tagName) &&hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_RESOURCE_INSERT","",getChildWithName(node, "directory").getChild(0).getLabel());
		}
		
		if("sourceDirectory".equals(tagName)  && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("SOURCE_DIRECTORY_INSERT","",action.getNode().getChild(0).getLabel());
		}
		if("testSourceDirectory".equals(tagName) && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_SOURCE_DIRECTORY_INSERT","",action.getNode().getChild(0).getLabel());
		}
		
		if("version".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && !hasParent(action.getNode(), "dependency")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_VERSION_INSERT","",node.getChild(0).getLabel());
		}
		
		if("version".equals(tagName) &&!hasParent(action.getNode(), "plugin") && !hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& !hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEPENDENCY_VERSION_INSERT",node.getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&!hasParent(action.getNode(), "plugin") && !hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("MANAGED_DEPENDENCY_VERSION_INSERT",node.getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& !hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_INSERT",node.getChild(0).getLabel(),"");
		}
		
		if("configuration".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT",getPluginId(getParentByName(node, "plugin")),getPluginId(getParentByName(node, "plugin")));
		}
		
		if("plugin".equals(tagName) && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_INSERT","",getPluginId(node));
		}
		
		if("reportSet".equals(tagName) && hasParent(action.getNode(), "reportSets")&& hasParent(action.getNode(), "reporting")) {
			dontProcessChildrenList.add(getParentByName(node, "reportSets"));
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		
		if("profile".equals(tagName) && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_INSERT","",getProfileId(node));
		}
		
		if("developer".equals(tagName) && hasParent(action.getNode(), "developers")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEVELOPER_INSERT","",getDeveloperId(node));
		}
		
		if("contributor".equals(tagName) && hasParent(action.getNode(), "contributors")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("CONTRIBUTOR_INSERT","",getContributorId(node));
		}

		if("site".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SITE_INSERT","",getSiteId(node));
		}
		if("snapshotRepository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_INSERT","",getRepositoryId(node));
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_REPOSITORY_INSERT","",getRepositoryId(node));
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("REPOSITORY_INSERT","",getRepositoryId(node));
		}
		if("pluginRepository".equals(tagName) && hasParent(action.getNode(), "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_REPOSITORY_INSERT","",getRepositoryId(node));
		}
		
		if("module".equals(tagName) && hasParent(node, "modules")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("MODULE_INSERT","",node.getChild(0).getLabel());
		}
		
		if("connection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_CONNECTION_INSERT","",node.getChild(0).getLabel());
		}
		if("developerConnection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_DEVCONNECTION_INSERT","",node.getChild(0).getLabel());
		}
		if("url".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_URL_INSERT","",node.getChild(0).getLabel());
		}
		
		if("name".equals(tagName)  &&  !hasParent(node, "license") ) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PROJECT_NAME_INSERT","",node.getChild(0).getLabel());
		}
		
		if("packaging".equals(tagName)  &&  !hasParent(node, "license") ) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PROJECT_PACKAGING_INSERT","",node.getChild(0).getLabel());
		}
		
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && !hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("DEPENDENCY_INSERT","",getDependencyId(node));
		}
		
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && !hasParent(node, "plugin") && hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("MANAGED_DEPENDENCY_INSERT","",getDependencyId(node));
		}
		
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PLUGIN_DEPENDENCY_INSERT","",getDependencyId(node));
		}
		
		if("license".equals(tagName)  && hasParent(node, "licenses")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("LICENSE_INSERT","",getLicenseId(node));
		}
		
		if(hasParent(node, "resource") && hasParent(node, "resources") && hasParent(node, "build") && !hasParent(node, "configuration")) {
			dontProcessChildrenList.add(getParentByName(node, "resource"));
			return new MavenBuildChange("RESOURCE_UPDATE","","");
		}
		if(hasParent(node, "testResource") && hasParent(node, "testResources") && hasParent(node, "build")&& !hasParent(node, "configuration")) {
			dontProcessChildrenList.add(getParentByName(node, "testResource"));
			return new MavenBuildChange("TEST_RESOURCE_UPDATE","","");
		}
		
		if( !hasParent(node, "profile")  && hasParent(node, "dependency")&& hasParent(node, "plugin") ) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		
		if(action.getNode().getType()==1 && hasParent(action.getNode(), "configuration") &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(getParentByName(node, "configuration"));
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","");
		}
		
		if(hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(getParentByName(node, "profile"));
			return new MavenBuildChange("PROFILE_UPDATE","","");
		}
		
		if(hasParent(action.getNode(), "reportSet") && hasParent(action.getNode(), "reportSets")&& hasParent(action.getNode(), "reporting")) {
			dontProcessChildrenList.add(getParentByName(node, "reportSets"));
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		
		if(hasParent(node, "repository") && hasParent(node, "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				value=action.getNode().getChild(0).getLabel();
			}
			return new MavenBuildChange("REPOSITORY_UPDATE","","\""+tagName+"\"::\""+value+"\"");
		}
		if(hasParent(node, "pluginRepository") && hasParent(node, "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				value=action.getNode().getChild(0).getLabel();
			}
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE","","\""+tagName+"\"::\""+value+"\"");
		}
		
		if(hasParent(node, "dependencies") && hasParent(node, "dependency") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		if(hasParent(node, "dependencies") && hasParent(node, "dependency") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			return new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		
		if(hasParent(node, "plugin") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "plugin"));
			return new MavenBuildChange("PLUGIN_UPDATE",getDependencyId(getParentByName(node, "plugin")),getDependencyId(getParentByName(node, "plugin")));
		}
		
		if(hasParent(node, "license") && hasParent(node, "licenses")) {
			dontProcessChildrenList.add(getParentByName(node, "license"));
			return new MavenBuildChange("LICENSE_UPDATE","","\""+tagName+"\"::\""+node.getChild(0).getLabel()+"\"");
		}
		
		if(hasParent(node, "parent")) {
			dontProcessChildrenList.add(getParentByName(node, "parent"));
			String oldId=getDependencyId(getParentByName(node, "parent"));
			return new MavenBuildChange("PARENT_UPDATE",oldId,oldId);
		}
		
		if(hasParent(node, "properties")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("GENERAL_PROPERTY_INSERT","",node.getLabel()+"/"+node.getChild(0).getLabel());
		}
		
		return null;
	}
	
	private static MavenBuildChange handleDeleteAction(Delete action, String tagName) {
		ITree node = action.getNode();
		
		if("resource".equals(tagName) &&hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("RESOURCE_DELETE",getChildWithName(node, "directory").getChild(0).getLabel(),"");
		}
		if("testResource".equals(tagName) &&hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_RESOURCE_DELETE",getChildWithName(node, "directory").getChild(0).getLabel(),"");
		}
		
		if("sourceDirectory".equals(tagName)  && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("SOURCE_DIRECTORY_DELETE",action.getNode().getChild(0).getLabel(),"");
		}
		if("testSourceDirectory".equals(tagName) && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_SOURCE_DIRECTORY_DELETE",action.getNode().getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && !hasParent(action.getNode(), "dependency")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_VERSION_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&!hasParent(action.getNode(), "plugin") && !hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& !hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEPENDENCY_VERSION_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&!hasParent(action.getNode(), "plugin") && !hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("MANAGED_DEPENDENCY_VERSION_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("version".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")
				&&!hasParent(action.getNode(), "parent") && hasParent(action.getNode(), "dependency")&& !hasParent(action.getNode(), "dependencyManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("configuration".equals(tagName) &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_CONFIGURATION_DELETE",getPluginId(getParentByName(node, "plugin")),getPluginId(getParentByName(node, "plugin")));
		}
		
		if("plugin".equals(tagName) && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DELETE",getPluginId(node),"");
		}
		
		if("reportSet".equals(tagName) && hasParent(action.getNode(), "reportSets")&& hasParent(action.getNode(), "reporting")) {
			dontProcessChildrenList.add(getParentByName(node, "reportSets"));
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		
		if("profile".equals(tagName) && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_DELETE",getProfileId(node),"");
		}
		
		if("developer".equals(tagName) && hasParent(action.getNode(), "developers")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEVELOPER_DELETE",getDeveloperId(node),"");
		}
		
		if("contributor".equals(tagName) && hasParent(action.getNode(), "contributors")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("CONTRIBUTOR_DELETE",getContributorId(node),"");
		}
		
		if("site".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SITE_DELETE",getSiteId(node),"");
		}
		if("snapshotRepository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_DELETE",getRepositoryId(node),"");
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_REPOSITORY_DELETE",getRepositoryId(node),"");
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("REPOSITORY_DELETE",getRepositoryId(node),"");
		}
		if("pluginRepository".equals(tagName) && hasParent(action.getNode(), "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_REPOSITORY_DELETE",getRepositoryId(node),"");
		}
		
		if("module".equals(tagName) && hasParent(node, "modules")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("MODULE_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("connection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_CONNECTION_DELETE",node.getChild(0).getLabel(),"");
		}
		if("developerConnection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_DEVCONNECTION_DELETE",node.getChild(0).getLabel(),"");
		}
		if("url".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_URL_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("name".equals(tagName)  && !hasParent(node, "license") ) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PROJECT_NAME_DELETE",node.getChild(0).getLabel(),"");
		}
		
		if("packaging".equals(tagName)  &&  !hasParent(node, "license") ) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PROJECT_PACKAGING_DELETE","",node.getChild(0).getLabel());
		}
		
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("DEPENDENCY_DELETE",getDependencyId(node),"");
		}
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("MANAGED_DEPENDENCY_DELETE",getDependencyId(node),"");
		}
		
		if("dependency".equals(tagName)  && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("PLUGIN_DEPENDENCY_DELETE",getDependencyId(node),"");
		}
		
		if("license".equals(tagName)  && hasParent(node, "licenses")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("LICENSE_DELETE",getLicenseId(node),"");
		}
		
		if(hasParent(node, "resource") && hasParent(node, "resources") && hasParent(node, "build") && !hasParent(node, "configuration")) {
			dontProcessChildrenList.add(getParentByName(node, "resource"));
			return new MavenBuildChange("RESOURCE_UPDATE","","");
		}
		if(hasParent(node, "testResource") && hasParent(node, "testResources") && hasParent(node, "build") && !hasParent(node, "configuration")) {
			dontProcessChildrenList.add(getParentByName(node, "testResource"));
			return new MavenBuildChange("TEST_RESOURCE_UPDATE","","");
		}
		
		if(!hasParent(node, "profile")  && hasParent(node, "dependency")&& hasParent(node, "plugin")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		
		if(action.getNode().getType()==1 && hasParent(action.getNode(), "configuration") &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(getParentByName(node, "configuration"));
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","");
		}
		
		if(hasParent(action.getNode(), "reportSet") && hasParent(action.getNode(), "reportSets")&& hasParent(action.getNode(), "reporting")) {
			dontProcessChildrenList.add(getParentByName(node, "reportSets"));
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		
		if(hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(getParentByName(node, "profile"));
			return new MavenBuildChange("PROFILE_UPDATE","","");
		}
		
		if(hasParent(node, "repository") && hasParent(node, "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				value=action.getNode().getChild(0).getLabel();
			}
			return new MavenBuildChange("REPOSITORY_UPDATE","\""+tagName+"\"::\""+value+"\"","");
		}
		if(hasParent(node, "pluginRepository") && hasParent(node, "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				value=action.getNode().getChild(0).getLabel();
			}
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE","\""+tagName+"\"::\""+value+"\"","");
		}
		
		if(hasParent(node, "dependencies") && hasParent(node, "dependency") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		if(hasParent(node, "dependencies") && hasParent(node, "dependency") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")  && !hasParent(node, "plugin") && hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			return new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE",getDependencyId(getParentByName(node, "dependency")),getDependencyId(getParentByName(node, "dependency")));
		}
		
		if(hasParent(node, "plugin") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "plugin"));
			return new MavenBuildChange("PLUGIN_UPDATE",getDependencyId(getParentByName(node, "plugin")),getDependencyId(getParentByName(node, "plugin")));
		}
		
		if(hasParent(node, "license") && hasParent(node, "licenses")) {
			dontProcessChildrenList.add(getParentByName(node, "license"));
			return new MavenBuildChange("LICENSE_UPDATE","\""+tagName+"\"::\""+node.getChild(0).getLabel()+"\"","");
		}
		
		if(hasParent(node, "parent")) {
			dontProcessChildrenList.add(getParentByName(node,"parent"));
			String oldId=getDependencyId(getParentByName(node,"parent"));
			return new MavenBuildChange("PARENT_UPDATE",oldId,oldId);
		}
		
		if(hasParent(node, "properties") && node.getType()==1) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("GENERAL_PROPERTY_DELETE",node.getLabel()+"/"+node.getChild(0).getLabel(),"");
		}
		
		return null;
	}
	
	private static MavenBuildChange handleUpdateAction(Update action, String tagName) {
		ITree node = action.getNode();
		
		if("module".equals(tagName) && hasParent(node, "modules")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("MODULE_UPDATE",node.getLabel(),action.getValue());
		}
		
		if("connection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_CONNECTION_UPDATE",node.getLabel(),action.getValue());
		}
		if("developerConnection".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_DEVCONNECTION_UPDATE",node.getLabel(),action.getValue());
		}
		if("url".equals(tagName) && hasParent(node, "scm")) {
			dontProcessChildrenList.add(node);
			return new MavenBuildChange("SCM_URL_UPDATE",node.getLabel(),action.getValue());
		}
		
		if("packaging".equals(tagName) &&  !hasParent(node, "parent") && !hasParent(node, "dependency")) {
			return new MavenBuildChange("PROJECT_PACKAGING_UPDATE",node.getLabel(),action.getValue());
		}
		if("groupId".equals(tagName) && !hasParent(node, "parent") && !hasParent(node, "dependency")&& !hasParent(node, "plugin")) {
			String oldId=getDependencyId(node.getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PROJECT_GROUPID_UPDATE",oldId,newId);
		}
		if("artifactId".equals(tagName)  && !hasParent(node, "parent") && !hasParent(node, "dependency")&& !hasParent(node, "plugin")) {
			String oldId=getDependencyId(node.getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PROJECT_ARTIFACTID_UPDATE",oldId,newId);
		}
		if("version".equals(tagName) && !hasParent(node, "plugin")
				&& !hasParent(node, "parent")  && !hasParent(node, "dependency") && !hasParent(node, "plugin")) {
			return new MavenBuildChange("PROJECT_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		//======
		//"plain" dependencies - no dep mgmt
		if("artifactId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && !hasParent(node, "plugin")&& !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("DEPENDENCY_UPDATE",oldId,newId);
		}
		if("groupId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& !hasParent(node, "plugin")&& !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("DEPENDENCY_UPDATE",oldId,newId);
		}
		if("scope".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& !hasParent(node, "plugin")&& !hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			return new MavenBuildChange("DEPENDENCY_UPDATE",oldId,oldId);
		}
		
		if("version".equals(tagName)  
				&& !hasParent(node, "profile")  && hasParent(node, "dependency")&& !hasParent(node, "plugin")&& !hasParent(node, "dependencyManagement")) {
			return new MavenBuildChange("DEPENDENCY_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		//=======
		//======
		//"plain" dependencies - in dep mgmt
		if("artifactId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && !hasParent(node, "plugin")&& hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE",oldId,newId);
		}
		if("groupId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& !hasParent(node, "plugin")&& hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE",oldId,newId);
		}
		if("scope".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& !hasParent(node, "plugin")&& hasParent(node, "dependencyManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			return new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE",oldId,oldId);
		}
		
		if("version".equals(tagName)  
				&& !hasParent(node, "profile")  && hasParent(node, "dependency")&& !hasParent(node, "plugin")&& hasParent(node, "dependencyManagement")) {
			return new MavenBuildChange("MANAGED_DEPENDENCY_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		//=======
		//======
		//"plugin"dependencies
		if("artifactId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license") && hasParent(node, "plugin")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",oldId,newId);
		}
		if("groupId".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& hasParent(node, "plugin")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",oldId,newId);
		}
		if("scope".equals(node.getParent().getLabel()) && hasParent(node, "dependencies") 
				&& !hasParent(node, "profile") && !hasParent(node, "license")&& hasParent(node, "plugin")) {
			dontProcessChildrenList.add(getParentByName(node, "dependency"));
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",oldId,oldId);
		}
		
		if("version".equals(tagName)  
				&& !hasParent(node, "profile")  && hasParent(node, "dependency")&& hasParent(node, "plugin")) {
			return new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		//=======
		if("groupId".equals(tagName) && hasParent(node, "plugin") && !hasParent(node, "dependency")) {
			String oldId=getPluginId(getParentByName(node, "plugin"));
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PLUGIN_UPDATE",oldId,newId);
		}
		if("artifactId".equals(tagName) && hasParent(node, "plugin") && !hasParent(node, "dependency")) {
			String oldId=getPluginId(getParentByName(node, "plugin"));
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PLUGIN_UPDATE",oldId,newId);
		}
		if("version".equals(tagName) && hasParent(node, "plugin") 
				&& !hasParent(node, "parent")  && !hasParent(node, "dependency")) {
			return new MavenBuildChange("PLUGIN_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		//=======
		if("groupId".equals(tagName) &&  hasParent(node, "parent")) {
			return new MavenBuildChange("PARENT_GROUPID_UPDATE",node.getLabel(),action.getValue());
		}
		if("artifactId".equals(tagName) &&  hasParent(node, "parent")) {
			return new MavenBuildChange("PARENT_ARTIFACTID_UPDATE",node.getLabel(),action.getValue());
		}
		if("version".equals(tagName) &&  hasParent(node, "parent")) {
			return new MavenBuildChange("PARENT_VERSION_UPDATE",node.getLabel(),action.getValue());
		}
		
		if("name".equals(tagName) && hasParent(node, "license") ) {
			dontProcessChildrenList.add(getParentByName(node, "license"));
			String tag="UKN";
			if(node.getParent()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("LICENSE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if("url".equals(tagName) && hasParent(node, "license")  ) {
			dontProcessChildrenList.add(getParentByName(node, "license"));
			String tag="UKN";
			if(node.getParent()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("LICENSE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if("name".equals(tagName) && !hasParent(node, "license")  
				&& !hasParent(node, "developer")&& !hasParent(node, "contributor")
				&& !hasParent(node, "distributionManagement") && !hasParent(node, "build")) {
			return new MavenBuildChange("PROJECT_NAME_UPDATE",node.getLabel(),action.getValue());
		}
		
		if("packaging".equals(tagName) && !hasParent(node, "license")  
				&& !hasParent(node, "developer")&& !hasParent(node, "contributor")
				&& !hasParent(node, "distributionManagement") && !hasParent(node, "build")) {
			return new MavenBuildChange("PROJECT_PACKAGING_UPDATE",node.getLabel(),action.getValue());
		}
		
		if(hasParent(node, "resource") && hasParent(node, "resources") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "resource"));
			return new MavenBuildChange("RESOURCE_UPDATE","","");
		}
		if(hasParent(node, "testResource") && hasParent(node, "testResources") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "testResource"));
			return new MavenBuildChange("TEST_RESOURCE_UPDATE","","");
		}
		
		if(hasParent(node, "sourceDirectory") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "sourceDirectory"));
			return new MavenBuildChange("SOURCE_DIRECTORY_UPDATE",node.getLabel(),action.getValue());
		}
		if(hasParent(node, "testSourceDirectory") && hasParent(node, "build")) {
			dontProcessChildrenList.add(getParentByName(node, "testSourceDirectory"));
			return new MavenBuildChange("TEST_SOURCE_DIRECTORY_UPDATE",node.getLabel(),action.getValue());
		}
		
		if(hasParent(node, "reportSet") && hasParent(action.getNode(), "reportSets")&& hasParent(action.getNode(), "reporting")) {
			dontProcessChildrenList.add(getParentByName(node, "reportSets"));
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		
		if(hasParent(action.getNode(), "site") && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "site"));
			String tag="UKN";
			if(action!=null && node!=null && node.getParent()!=null && node.getParent().getLabel()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(action!=null && node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_SITE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(action.getNode(), "repository") && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "repository"));
			String tag="UKN";
			if(action!=null && node!=null && node.getParent()!=null && node.getParent().getLabel()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(action!=null && node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(action.getNode(), "snapshotRepository") && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(getParentByName(node, "snapshotRepository"));
			String tag="UKN";
			if(action!=null && node!=null && node.getParent()!=null && node.getParent().getLabel()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(action!=null && node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(getParentByName(node, "profile"));
			return new MavenBuildChange("PROFILE_UPDATE","","");
		}
			
		
		if(hasParent(action.getNode(), "developer") && hasParent(action.getNode(), "developers")) {
			dontProcessChildrenList.add(node);
			String tag="UKN";
			if(action!=null && node!=null && node.getParent()!=null && node.getParent().getLabel()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(action!=null && node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DEVELOPER_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(action.getNode(), "contributor") && hasParent(action.getNode(), "contributors")) {
			dontProcessChildrenList.add(node);
			String tag="UKN";
			if(action!=null && node!=null && node.getParent()!=null && node.getParent().getLabel()!=null) {
				tag=node.getParent().getLabel();
			}
			String oldValue="UKN";
			if(action!=null && node!=null) {
				oldValue=node.getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("CONTRIBUTOR_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(node, "repository") && hasParent(node, "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("REPOSITORY_UPDATE","\""+tagName+"\"::\""+node.getLabel()+"\"","\""+tagName+"\"::\""+action.getValue()+"\"");
		}
		
		if(hasParent(action.getNode(), "configuration") &&hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(getParentByName(node, "configuration"));
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","");
		}
		
		if(hasParent(node, "pluginRepository") && hasParent(node, "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE","\""+tagName+"\"::\""+node.getLabel()+"\"","\""+tagName+"\"::\""+action.getValue()+"\"");
		}
		
		if(hasParent(node, "parent")) {
			dontProcessChildrenList.add(getParentByName(node, "parent"));
			//TODO is this really correct?? i mean, why chnage the artifact path? where do we know that?
			String oldId=getDependencyId(node.getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PARENT_UPDATE",oldId,newId);
		}
		
		if(hasParent(node, "properties")) {
			return new MavenBuildChange("GENERAL_PROPERTY_UPDATE",node.getParent().getLabel()+"/"+node.getLabel(),node.getParent().getLabel()+"/"+action.getValue());
		}
		return null;
	}
	
	public static String getTagName(ITree node) {
//		System.out.println(node.getType());
		if(node.getType()==1) {
			return node.getLabel();
		}
		if(node.getType()==3) {
			return node.getParent().getLabel();
		}
		return "NO_TAG_NAME";
	}

	public static ITree getChildWithName(ITree node, String name) {
		List<ITree> children = node.getChildren();
		
		for (ITree child : children) {
			if(name.equals(child.getLabel())) {
				return child;
			}
		}
		return null;
	}
	public static ITree getParentByName(ITree node, String name) {
		ITree tmp = node;
		while(tmp.getParent()!=null) {
			if(name.equals(tmp.getLabel())) {
				return tmp;
			}
			tmp=tmp.getParent();
		}
		return null;
	}
	public static boolean hasParent(ITree node, String parentLabel) {
		ITree tmpNode = node.getParent();
		while (tmpNode != null) {
			if (parentLabel.equals(tmpNode.getLabel())) {
				return true;
			}
//			if(tmpNode.getChildren().size()>0 && parentLabel.equals(tmpNode.getChild(0).getLabel())) {
//				return true;
//			}
			tmpNode = tmpNode.getParent();
		}
		return false;
	}


	private static String getDependencyId(ITree node) {
		String groupId="UKN";
		String artifactId="UKN";
		String version="UKN";
		
		if(getChildWithName(node,"groupId")!=null && getChildWithName(node,"groupId").getChild(0)!=null && getChildWithName(node,"groupId").getChild(0).getLabel()!=null) {
			groupId=getChildWithName(node,"groupId").getChild(0).getLabel();
		}
		if(getChildWithName(node,"artifactId")!=null && getChildWithName(node,"artifactId").getChild(0)!=null && getChildWithName(node,"artifactId").getChild(0).getLabel()!=null) {
			artifactId=getChildWithName(node,"artifactId").getChild(0).getLabel();
		}
		if(getChildWithName(node,"version")!=null && getChildWithName(node,"version").getChild(0)!=null && getChildWithName(node,"version").getChild(0).getLabel()!=null) {
			version=getChildWithName(node,"version").getChild(0).getLabel();
		}
		
		return groupId+"/"+artifactId+"/"+version;
	}
	
	private static void resetDontProcessChildrenList() {
		dontProcessChildrenList = new ArrayList<ITree>();
	}
	
	private static boolean parentIsInDontProcessChildrenList(ITree node) {
		ITree tmpNode = node;
		while (tmpNode != null) {
			if (dontProcessChildrenList.contains(tmpNode)) {
				return true;
			}
			tmpNode = tmpNode.getParent();
		}
		return false;
	}
	
	private static String getLicenseId(ITree node) {
		String name="UKN";
		String url="UKN";
		String comments="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(0)!=null) {
			name=getChildWithName(node,"name").getChild(0).getLabel();
		}
		if(getChildWithName(node,"url")!=null&& getChildWithName(node,"url").getChild(0)!=null) {
			url=getChildWithName(node,"url").getChild(0).getLabel();
		}
		if(getChildWithName(node,"comments")!=null && getChildWithName(node,"comments").getChild(0)!=null) {
			comments=getChildWithName(node,"comments").getChild(0).getLabel();
		}
		
		return name+COMPLEX_SEPARATOR+url+COMPLEX_SEPARATOR+comments;
	}
	private static String getRepositoryId(ITree node) {
		String name="UKN";
		String url="UKN";
		String id="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(0)!=null && getChildWithName(node,"name").getChild(0).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(0).getLabel();
		}
		if(getChildWithName(node,"url")!=null && getChildWithName(node,"url").getChild(0)!=null && getChildWithName(node,"url").getChild(0).getLabel()!=null) {
			url=getChildWithName(node,"url").getChild(0).getLabel();
		}
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(0)!=null && getChildWithName(node,"id").getChild(0).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(0).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+name+COMPLEX_SEPARATOR+url;
	}
	private static String getContributorId(ITree node) {
		String name="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(0)!=null && getChildWithName(node,"name").getChild(0).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(0).getLabel();
		}
		
		return name;
	}
	private static String getDeveloperId(ITree node) {
		String id="UKN";
		String name="UKN";
		String email="UKN";
		
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(0)!=null && getChildWithName(node,"id").getChild(0).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(0).getLabel();
		}
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(0)!=null && getChildWithName(node,"name").getChild(0).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(0).getLabel();
		}
		if(getChildWithName(node,"email")!=null && getChildWithName(node,"email").getChild(0)!=null && getChildWithName(node,"email").getChild(0).getLabel()!=null) {
			email=getChildWithName(node,"email").getChild(0).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+name+COMPLEX_SEPARATOR+email;
	}
	private static String getProfileId(ITree node) {
		String id="UKN";
		ITree idt = getChildWithName(node, "id");
		if(idt!=null && idt.getChild(0) !=null) {
			id=idt.getChild(0).getLabel();
		}
		return id;
	}
	private static String getPluginId(ITree node) {
		String groupId="UKN";
		String artifactId="UKN";
		String version="UKN";
		
		if(getChildWithName(node,"groupId")!=null && getChildWithName(node,"groupId").getChild(0)!=null && getChildWithName(node,"groupId").getChild(0).getLabel()!=null) {
			groupId=getChildWithName(node,"groupId").getChild(0).getLabel();
		}
		if(getChildWithName(node,"artifactId")!=null && getChildWithName(node,"artifactId").getChild(0)!=null && getChildWithName(node,"artifactId").getChild(0).getLabel()!=null) {
			artifactId=getChildWithName(node,"artifactId").getChild(0).getLabel();
		}
		if(getChildWithName(node,"version")!=null && getChildWithName(node,"version").getChild(0)!=null && getChildWithName(node,"version").getChild(0).getLabel()!=null) {
			version=getChildWithName(node,"version").getChild(0).getLabel();
		}
		
		return groupId+"/"+artifactId+"/"+version;
	}
	private static String getSiteId(ITree node) {
		String id="UKN";
		String url="UKN";
		
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(0)!=null && getChildWithName(node,"id").getChild(0).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(0).getLabel();
		}
		if(getChildWithName(node,"url")!=null && getChildWithName(node,"url").getChild(0)!=null && getChildWithName(node,"url").getChild(0).getLabel()!=null) {
			url=getChildWithName(node,"url").getChild(0).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+url;
	}
}
