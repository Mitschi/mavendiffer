package at.aau.diff.maven;

import java.util.ArrayList;
import java.util.List;

import at.aau.diff.common.Change;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

public class GumTreeMavenBuildChangeExtractor {

	private static final String COMPLEX_SEPARATOR = "/::/::/";
	private static List<ITree> dontProcessChildrenList = new ArrayList<ITree>();

	public static MavenBuildChange getBuildChange(Action action) {
//		if(action.getNode().getType()==10 || action.getNode().getType()==15) {  
			String tagName = getTagName(action.getNode());
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
					// i guess we can mostly ignore move actions as the do not have
					// an impact on the maven build (order is neglected in the
					// pom.xml)
				}
			}
//		}

		return null;
	}

	public static String getTagName(ITree node) {
		if(node.getType()==10 || node.getType() == 11) {
			if(node.getLabel()!= null && !"".equals(node.getLabel())) {
				return node.getLabel();
			}
			List<ITree> children = node.getChildren();
			for (ITree child : children) {
				if(child.getType()==11) {
					return child.getLabel();
				}
			}
		}
		if(node.getType()==15) {
			List<ITree> children = node.getParent().getChildren();
			for (ITree child : children) {
				if(child.getType()==11) {
					return child.getLabel();
				}
			}
		}
		return null;
	}

	private static MavenBuildChange handleInsertAction(Insert action, String tagName) {
		if ("name".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "dependencies") 
				&& !hasParent(action.getNode(), "repositories") && !hasParent(action.getNode(), "profiles") && !hasParent(action.getNode(), "licenses") 
				&& !hasParent(action.getNode(), "organization") && !hasParent(action.getNode(), "developers") && !hasParent(action.getNode(), "contributors") 
				&& !hasParent(action.getNode(), "mailingLists")	&& !hasParent(action.getNode(), "distributionManagement") && action.getNode().getType()==10) {
			return new MavenBuildChange("PROJECT_NAME_INSERT","",action.getNode().getChild(1).getLabel());
		}
		if("developer".equals(tagName) && hasParent(action.getNode(), "developers")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEVELOPER_INSERT","",getDeveloperId(action.getNode()));
		}
		if("contributor".equals(tagName) && hasParent(action.getNode(), "contributors")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("CONTRIBUTOR_INSERT","",getContributorId(action.getNode()));
		}
		
		if("version".equals(tagName) && hasParent(action.getNode(), "plugin")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_VERSION_INSERT","",action.getNode().getChild(1).getLabel());
		}
		if("plugin".equals(tagName) && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_INSERT","",getPluginId(action.getNode()));
		}
		if("configuration".equals(tagName)  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT",getPluginId(action.getNode().getParent()),getPluginId(action.getNode().getParent()));
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "configuration")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "configuration");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
//			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","\""+tag+"\"::\""+value+"\""); // TODO we cannot do this, because if there are more insertions, we only get the first one as an update!
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","");
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "reportSet")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "reportSet");
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		if("snapshotRepository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_INSERT","",getRepositoryId(action.getNode()));
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "distributionManagement") && !hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_REPOSITORY_INSERT","",getRepositoryId(action.getNode()));
		}
		if("site".equals(tagName) && hasParent(action.getNode(), "distributionManagement") && !hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SITE_INSERT","",getSiteId(action.getNode()));
		}
		if(hasParent(action.getNode(), "distributionManagement")&& action.getNode().getType()==10 && !hasParent(action.getNode(), "repositories")&& !hasParent(action.getNode(), "reportSets") && !hasParent(action.getNode(), "site")) {
			addParentNodeToIgnoreList(action.getNode(), "repository");
			return new MavenBuildChange("REPOSITORY_UPDATE");
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("REPOSITORY_INSERT","",getRepositoryId(action.getNode()));
		}
		if(hasParent(action.getNode(), "repositories")&& action.getNode().getType()==10&& !hasParent(action.getNode(), "reportSets") && !hasParent(action.getNode(), "site")) {
			addParentNodeToIgnoreList(action.getNode(), "repository");
			return new MavenBuildChange("REPOSITORY_UPDATE");
		}
		if("pluginRepository".equals(tagName) && hasParent(action.getNode(), "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_REPOSITORY_INSERT","",getRepositoryId(action.getNode()));
		}
		if(hasParent(action.getNode(), "pluginRepositories")&& action.getNode().getType()==10) {
			addParentNodeToIgnoreList(action.getNode(), "pluginRepository");
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE");
		}
		
		if("profile".equals(tagName) && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_INSERT","",getProfileId(action.getNode()));
		}
		if("activation".equals(tagName) && hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_UPDATE","","");
		}
		if("module".equals(tagName) && hasParent(action.getNode(), "modules") && action.getNode().getType()==10) {
			return new MavenBuildChange("MODULE_INSERT","",action.getNode().getChild(1).getLabel());
		}
		
		if("connection".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_CONNECTION_INSERT","",action.getNode().getChild(1).getLabel());
		}
		if("developerConnection".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_DEVCONNECTION_INSERT","",action.getNode().getChild(1).getLabel());
		}
		if("url".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_URL_INSERT","",action.getNode().getChild(1).getLabel());
		}
		
		if(hasParent(action.getNode(), "properties") && action.getNode().getType()==10) {
			String tag =action.getNode().getChild(0).getLabel();
			String value="";
			if(action.getNode() !=null && action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1 && action.getNode().getChild(1).getLabel()!=null) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("GENERAL_PROPERTY_INSERT","",""+tag+"/"+value);
		}
		
		if("actifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("dependency".equals(tagName) && hasParent(action.getNode(), "plugin")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_INSERT","",getDependencyId(action.getNode()));
		}
		
		if("actifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getParent()),getDependencyId(action.getParent()));
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getParent()),getDependencyId(action.getParent()));
		}
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getParent()),getDependencyId(action.getParent()));
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getParent()),getDependencyId(action.getParent()));
		}
		if("dependency".equals(tagName) && !hasParent(action.getNode(), "profile")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEPENDENCY_INSERT", "",getDependencyId(action.getNode()));
		}
		
		if("resource".equals(tagName) && hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("RESOURCE_INSERT","",getResourceId(action.getNode()));
		}
		if( hasParent(action.getNode(), "resource") && hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			addParentNodeToIgnoreList(action.getNode(), "resource");
			return new MavenBuildChange("RESOURCE_UPDATE");
		}
		if("testResource".equals(tagName) && hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_RESOURCE_INSERT","",getResourceId(action.getNode()));
		}
		if( hasParent(action.getNode(), "testResource") && hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			addParentNodeToIgnoreList(action.getNode(), "testResource");
			return new MavenBuildChange("TEST_RESOURCE_UPDATE");
		}
		
		if("license".equals(tagName) && hasParent(action.getNode(), "licenses")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("LICENSE_INSERT","",getLicenseId(action.getNode()));
		}
		if(hasParent(action.getNode(), "license") && hasParent(action.getNode(), "licenses")) {
			addParentNodeToIgnoreList(action.getNode(), "license");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("LICENSE_UPDATE","","\""+tag+"\"::\""+value+"\"");
		}
		
		
		if(action.getNode().getType()==10 /*&& hasParent(action.getNode(), "project")*/ && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")) {
			return new MavenBuildChange("PARENT_UPDATE",getDependencyId(action.getParent()),getDependencyId(action.getParent()));
		}
		return null;
	}

	private static MavenBuildChange handleDeleteAction(Delete action, String tagName) {
		if ("name".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "dependencies") 
				&& !hasParent(action.getNode(), "repositories") && !hasParent(action.getNode(), "profiles") && !hasParent(action.getNode(), "licenses") 
				&& !hasParent(action.getNode(), "organization") && !hasParent(action.getNode(), "developers") && !hasParent(action.getNode(), "contributors") 
				&& !hasParent(action.getNode(), "mailingLists")	&& !hasParent(action.getNode(), "distributionManagement") && action.getNode().getType()==10) {
			return new MavenBuildChange("PROJECT_NAME_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		
		if("developer".equals(tagName) && hasParent(action.getNode(), "developers")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEVELOPER_DELETE",getDeveloperId(action.getNode()),"");
		}
		if("contributor".equals(tagName) && hasParent(action.getNode(), "contributors")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("CONTRIBUTOR_DELETE",getContributorId(action.getNode()),"");
		}
		
		if("version".equals(tagName) && hasParent(action.getNode(), "plugin")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_VERSION_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		if("plugin".equals(tagName) && hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_DELETE",getPluginId(action.getNode()),"");
		}
		if("configuration".equals(tagName)  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_CONFIGURATION_DELETE",getPluginId(action.getNode().getParent()),getPluginId(action.getNode().getParent()));
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "configuration")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "configuration");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","\""+tag+"\"::\""+value+"\"","");
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "reportSet")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "reportSet");
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		//=====
		if("snapshotRepository".equals(tagName) && hasParent(action.getNode(), "distributionManagement")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_DELETE",getRepositoryId(action.getNode()),"");
		}
		if("repository".equals(tagName) && hasParent(action.getNode(), "distributionManagement") && !hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_REPOSITORY_DELETE",getRepositoryId(action.getNode()),"");
		}
		if("site".equals(tagName) && hasParent(action.getNode(), "distributionManagement") && !hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DIST_SITE_DELETE",getSiteId(action.getNode()),"");
		}
		if(hasParent(action.getNode(), "distributionManagement")&& action.getNode().getType()==10 && !hasParent(action.getNode(), "repositories") && !hasParent(action.getNode(), "reportSets") && !hasParent(action.getNode(), "site")) {
			addParentNodeToIgnoreList(action.getNode(), "repository");
			return new MavenBuildChange("REPOSITORY_UPDATE");
		}
		//=====
		if("repository".equals(tagName) && hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("REPOSITORY_DELETE",getRepositoryId(action.getNode()),"");
		}
		if(hasParent(action.getNode(), "repositories")&& action.getNode().getType()==10 && !hasParent(action.getNode(), "reportSets") && !hasParent(action.getNode(), "site")) {
			addParentNodeToIgnoreList(action.getNode(), "repository");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("REPOSITORY_UPDATE","\""+tag+"\"::\""+value+"\"","");
		}
		if("pluginRepository".equals(tagName) && hasParent(action.getNode(), "pluginRepositories")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PLUGIN_REPOSITORY_DELETE",getRepositoryId(action.getNode()),"");
		}
		if(hasParent(action.getNode(), "pluginRepositories")&& action.getNode().getType()==10) {
			addParentNodeToIgnoreList(action.getNode(), "pluginRepository");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE","\""+tag+"\"::\""+value+"\"","");
		}
		
		if("profile".equals(tagName) && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_DELETE",getProfileId(action.getNode()),"");
		}
		if("activation".equals(tagName) && hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("PROFILE_UPDATE","","");
		}
		
		if("module".equals(tagName) && hasParent(action.getNode(), "modules") && action.getNode().getType()==10) {
			return new MavenBuildChange("MODULE_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		
		if("connection".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_CONNECTION_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		if("developerConnection".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_DEVCONNECTION_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		if("url".equals(tagName) && hasParent(action.getNode(), "scm") && action.getNode().getType()==10) {
			return new MavenBuildChange("SCM_URL_DELETE",action.getNode().getChild(1).getLabel(),"");
		}
		
		if(hasParent(action.getNode(), "properties") && action.getNode().getType()==10) {
			String tag =action.getNode().getChild(0).getLabel();
			String value="";
			if(action.getNode() !=null && action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1 && action.getNode().getChild(1).getLabel()!=null) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("GENERAL_PROPERTY_DELETE",""+tag+"/"+value,"");
		}
		
		if("artifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("dependency".equals(tagName) && hasParent(action.getNode(), "plugin")) {
			dontProcessChildrenList.add(action.getNode().getParent());
			return new MavenBuildChange("PLUGIN_DEPENDENCY_DELETE",getDependencyId(action.getNode()),"");
		}
		
		if("artifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if(action.getNode().getType()==10 && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		if("dependency".equals(tagName) && !hasParent(action.getNode(), "plugin")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("DEPENDENCY_DELETE",getDependencyId(action.getNode()),"");
		}
		
		if("license".equals(tagName) && hasParent(action.getNode(), "licenses")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("LICENSE_DELETE",getLicenseId(action.getNode()),"");
		}
		if(hasParent(action.getNode(), "license") && hasParent(action.getNode(), "licenses")) {
			addParentNodeToIgnoreList(action.getNode(), "license");
			String tag="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>0) {
				tag=action.getNode().getChild(0).getLabel();
			}
			String value="UKN";
			if(action!=null && action.getNode()!=null &&  action.getNode().getChildren()!=null && action.getNode().getChildren().size()>1) {
				value=action.getNode().getChild(1).getLabel();
			}
			return new MavenBuildChange("LICENSE_UPDATE","\""+tag+"\"::\""+value+"\"","");
		}
		
		if("resource".equals(tagName) && hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("RESOURCE_DELETE",getResourceId(action.getNode()),"");
		}
		if( hasParent(action.getNode(), "resource") && hasParent(action.getNode(), "resources") && hasParent(action.getNode(), "build")) {
			addParentNodeToIgnoreList(action.getNode(), "resource");
			return new MavenBuildChange("RESOURCE_UPDATE");
		}
		if("testResource".equals(tagName) && hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("TEST_RESOURCE_DELETE",getResourceId(action.getNode()),"");
		}
		if( hasParent(action.getNode(), "testResource") && hasParent(action.getNode(), "testResources") && hasParent(action.getNode(), "build")) {
			addParentNodeToIgnoreList(action.getNode(), "testResource");
			return new MavenBuildChange("TEST_RESOURCE_UPDATE");
		}
		
		
		
		if(action.getNode().getType()==10 /*&& hasParent(action.getNode(), "project")*/ && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")) {
			return new MavenBuildChange("PARENT_UPDATE",getDependencyId(action.getNode().getParent()),getDependencyId(action.getNode().getParent()));
		}
		return null;
	}

	private static MavenBuildChange handleUpdateAction(Update action, String tagName) {
		if(hasParent(action.getNode(), "developer") && hasParent(action.getNode(), "developers")) {
			addParentNodeToIgnoreList(action.getNode(), "developer");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DEVELOPER_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if(hasParent(action.getNode(), "contributor") && hasParent(action.getNode(), "contributors")) {
			addParentNodeToIgnoreList(action.getNode(), "contributor");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("CONTRIBUTOR_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if("version".equals(tagName) && hasParent(action.getNode(), "plugin")&& !hasParent(action.getNode(), "dependency")) {
			return new MavenBuildChange("PLUGIN_VERSION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		if(hasParent(action.getNode(), "repository") && hasParent(action.getNode(), "distributionManagement")) {
			addParentNodeToIgnoreList(action.getNode(), "repository");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if(hasParent(action.getNode(), "snapshotRepository") && hasParent(action.getNode(), "distributionManagement")) {
			addParentNodeToIgnoreList(action.getNode(), "snapshotRepository");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if(hasParent(action.getNode(), "site") && hasParent(action.getNode(), "distributionManagement") && !hasParent(action.getNode(), "repositories")) {
			dontProcessChildrenList.add(action.getNode());
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("DIST_SITE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if(hasParent(action.getNode(), "repositories")&& !hasParent(action.getNode(), "reportSets") && !hasParent(action.getNode(), "site")) {
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if(hasParent(action.getNode(), "pluginRepositories")) {
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("PLUGIN_REPOSITORY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if("module".equals(tagName) && hasParent(action.getNode(), "modules")) {
			return new MavenBuildChange("MODULE_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		if("connection".equals(tagName) && hasParent(action.getNode(), "scm")) {
			return new MavenBuildChange("SCM_CONNECTION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if("developerConnection".equals(tagName) && hasParent(action.getNode(), "scm")) {
			return new MavenBuildChange("SCM_DEVCONNECTION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if("url".equals(tagName) && hasParent(action.getNode(), "scm")) {
			return new MavenBuildChange("SCM_URL_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		if(action.getNode().getType()==15 && hasParent(action.getNode(), "properties")) {
			dontProcessChildrenList.add(action.getNode());
			return new MavenBuildChange("GENERAL_PROPERTY_UPDATE",action.getNode().getParent().getChild(0).getLabel()+"/"+action.getNode().getLabel(),action.getNode().getParent().getChild(0).getLabel()+"/"+action.getValue());
		}
		
		if("artifactId".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PROJECT_ARTIFACTID_UPDATE",oldId,newId);
		}
		if("groupId".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency") 
				&& ! hasParent(action.getNode(), "plugin")) {
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PROJECT_GROUPID_UPDATE",oldId,newId);
		}
		if("version".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			return new MavenBuildChange("PROJECT_VERSION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if("packaging".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			return new MavenBuildChange("PROJECT_PACKAGING_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		//TODO others than arti group version like below
		
		if("artifactId".equals(tagName) /* && hasParent(action.getNode(), "project") */ && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			return new MavenBuildChange("PARENT_ARTIFACTID_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if("groupId".equals(tagName) /* && hasParent(action.getNode(), "project") */ && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			return new MavenBuildChange("PARENT_GROUPID_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if("version".equals(tagName) /* && hasParent(action.getNode(), "project") */ && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")
				&& ! hasParent(action.getNode(), "plugin")) {
			return new MavenBuildChange("PARENT_VERSION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		if(hasParent(action.getNode(), "project") && hasParent(action.getNode(), "parent")&& !hasParent(action.getNode(), "dependency")) {
			return new MavenBuildChange("PARENT_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		
		if ("name".equals(tagName) /* && hasParent(action.getNode(), "project") */ && !hasParent(action.getNode(), "dependencies") 
				&& !hasParent(action.getNode(), "repositories") && !hasParent(action.getNode(), "profiles") && !hasParent(action.getNode(), "licenses") 
				&& !hasParent(action.getNode(), "organization") && !hasParent(action.getNode(), "developers") && !hasParent(action.getNode(), "contributors") 
				&& !hasParent(action.getNode(), "mailingLists")	&& !hasParent(action.getNode(), "distributionManagement")) {
			return new MavenBuildChange("PROJECT_NAME_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",oldId,newId);//workaround as you cannot retrieve the new dependency as a whole
		}
		if("artifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE",oldId,newId);
		}
		if("version".equals(tagName) && hasParent(action.getNode(), "dependency") && hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_UPDATE",action.getNode().getLabel(),action.getValue());
		}
		
		if((action.getNode().getType()==10 || action.getNode().getType()==15) && hasParent(action.getNode(), "configuration")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins") && !hasParent(action.getNode(), "site")) {
			addParentNodeToIgnoreList(action.getNode(), "plugin"); //TODO verify: maybe better only add the "configuration" tag as starting point for ignoring children
			return new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE");
		}
		if((action.getNode().getType()==10 || action.getNode().getType()==15) && hasParent(action.getNode(), "reportSet")  && hasParent(action.getNode(), "plugin")&& hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "reportSet");
			return new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","","");
		}
		//================
		if("groupId".equals(tagName) && hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "plugin");
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("PLUGIN_UPDATE",oldId,newId);
		}
		if("artifactId".equals(tagName) && hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "plugin");
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("PLUGIN_UPDATE",oldId,newId);
		}
		if(hasParent(action.getNode(), "plugin") && hasParent(action.getNode(), "plugins")) {
			addParentNodeToIgnoreList(action.getNode(), "plugin");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("PLUGIN_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		//================
		if(hasParent(action.getNode(), "profile") && hasParent(action.getNode(), "profiles")) {
			addParentNodeToIgnoreList(action.getNode(), "profile");
			return new MavenBuildChange("PROFILE_UPDATE","","");
		} // to test
		
		if("scope".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_UPDATE",getDependencyId(action.getNode().getParent().getParent()),getDependencyId(action.getNode().getParent().getParent()));
		}
		if("groupId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=action.getValue()+"/"+split[1]+"/"+split[2];
			return new MavenBuildChange("DEPENDENCY_UPDATE",oldId,newId);//workaround as you cannot retrieve the new dependency as a whole
		}
		if("artifactId".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			String oldId=getDependencyId(action.getNode().getParent().getParent());
			String[] split = oldId.split("/");
			String newId=split[0]+"/"+action.getValue()+"/"+split[2];
			return new MavenBuildChange("DEPENDENCY_UPDATE",oldId,newId);//workaround as you cannot retrieve the new dependency as a whole
		}
		if("version".equals(tagName) && hasParent(action.getNode(), "dependency") && !hasParent(action.getNode(), "plugin")) {
			addParentNodeToIgnoreList(action.getNode(), "dependency");
			return new MavenBuildChange("DEPENDENCY_VERSION_UPDATE",""+action.getNode().getLabel(),""+action.getValue());
		}
		
		if(hasParent(action.getNode(), "license") && hasParent(action.getNode(), "licenses")) {
			addParentNodeToIgnoreList(action.getNode(), "license");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
			return new MavenBuildChange("LICENSE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
		}
		
		if(hasParent(action.getNode(), "resource") && hasParent(action.getNode(), "resources")) {
			addParentNodeToIgnoreList(action.getNode(), "resource");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
//			return new MavenBuildChange("RESOURCE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
			return new MavenBuildChange("RESOURCE_UPDATE");
		}
		if(hasParent(action.getNode(), "testResource") && hasParent(action.getNode(), "testResources")) {
			addParentNodeToIgnoreList(action.getNode(), "testResource");
			String tag="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>0) {
				tag=action.getNode().getParent().getChild(0).getLabel();
			}
			String oldValue="UKN";
			if(action!=null && action.getNode()!=null && action.getNode().getParent()!=null && action.getNode().getParent().getChildren()!=null && action.getNode().getParent().getChildren().size()>1) {
				oldValue=action.getNode().getParent().getChild(1).getLabel();
			}
			String newValue=action.getValue();
//			return new MavenBuildChange("TEST_RESOURCE_UPDATE","\""+tag+"\"::\""+oldValue+"\"","\""+tag+"\"::\""+newValue+"\"");
			return new MavenBuildChange("TEST_RESOURCE_UPDATE");
		}
		
		
		return null;
	}

	private static void addParentNodeToIgnoreList(ITree node,String whichParent) {
		ITree tmpNode = node.getParent();
		while (tmpNode != null) {
			if((tmpNode.getType()==11 && whichParent.equals(tmpNode.getLabel())) || (tmpNode.getType()==10 && getFirstChildNode(tmpNode,11)!=null && whichParent.equals(getFirstChildNode(tmpNode,11).getLabel()))) {
				dontProcessChildrenList.add(tmpNode);
				return;
			}
			tmpNode = tmpNode.getParent();
		}
	}

	private static ITree getFirstChildNode(ITree tmpNode, int type) {
		if(tmpNode.getChildren().size()>0) {
			List<ITree> children = tmpNode.getChildren();
			for (ITree child : children) {
				if(child.getType()== type) {
					return child;
				}
			}
		} 
		return null;
	}

	private static ITree getParentNode(ITree node, String parentLabel) {
		ITree tmpNode = node;
		while (tmpNode != null) {
			if (parentLabel.equals(tmpNode.getLabel())) {
				return tmpNode;
			}
			if(tmpNode.getChildren().size()>0 && parentLabel.equals(tmpNode.getChild(0).getLabel())) {
				return tmpNode;
			}
			tmpNode = tmpNode.getParent();
		}
		return null;
	}

	private static boolean hasParent(ITree node, String parentLabel) {
		ITree tmpNode = node.getParent();
		while (tmpNode != null) {
			if (parentLabel.equals(tmpNode.getLabel())) {
				return true;
			}
			if(tmpNode.getChildren().size()>0 && parentLabel.equals(tmpNode.getChild(0).getLabel())) {
				return true;
			}
			tmpNode = tmpNode.getParent();
		}
		return false;
	}

//	private static boolean firstChildHasLabel(ITree tmpNode, String parentLabel) {
//		ITree child = tmpNode.getChild(0);
//		return child !=null && parentLabel.equals(child.getLabel());
//	}

	private static boolean parentIsInDontProcessChildrenList(ITree node) {
		ITree tmpNode = node;
		while (tmpNode != null) {
			if (dontProcessChildrenList.contains(tmpNode)) {
//			if (containsCheckWithHashes(dontProcessChildrenList,tmpNode)) { //try to use this 
				return true;
			}
			tmpNode = tmpNode.getParent();
		}
		return false;
	}

	private static boolean containsCheckWithHashes(List<ITree> dontProcessChildrenList2, ITree tmpNode) {
		for (ITree iTree : dontProcessChildrenList2) {
			if(iTree.hashCode()==tmpNode.hashCode() || iTree.getHash() == tmpNode.getHash()) {
				return true;
			}
		}
		return false;
	}

	public static void resetDontProcessChildrenList() {
		dontProcessChildrenList = new ArrayList<ITree>();
	}

	public static List<Change> extract(List<Action> actions) {
//		System.out.println("ACTIONSCOUNT: "+actions.size());
		List<Change> changes = new ArrayList<Change>();
		resetDontProcessChildrenList();
		for (Action action : actions) {
			Change buildChange = getBuildChange(action);
			if (buildChange != null) {
//				 System.out.println(buildChange.getChangeTyp()+" "+buildChange.getChangeSubject());
				changes.add(buildChange);
			}
		}
		return changes;
	}
	
	//======= EXTRACT IDs
	
	private static String getResourceId(ITree node) {
		List<ITree> children = node.getChildren();
		for(ITree child : children) {
			if(child.getChildren()!= null && child.getChildren().size()>1 && "directory".equals(child.getChild(0).getLabel())) {
				return child.getChild(1).getLabel();
			}
		}
		
		return "";
	}
	private static String getDependencyId(ITree node) {
		String groupId="UKN";
		String artifactId="UKN";
		String version="UKN";
		
		if(getChildWithName(node,"groupId")!=null && getChildWithName(node,"groupId").getChild(1)!=null && getChildWithName(node,"groupId").getChild(1).getLabel()!=null) {
			groupId=getChildWithName(node,"groupId").getChild(1).getLabel();
		}
		if(getChildWithName(node,"artifactId")!=null && getChildWithName(node,"artifactId").getChild(1)!=null && getChildWithName(node,"artifactId").getChild(1).getLabel()!=null) {
			artifactId=getChildWithName(node,"artifactId").getChild(1).getLabel();
		}
		if(getChildWithName(node,"version")!=null && getChildWithName(node,"version").getChild(1)!=null && getChildWithName(node,"version").getChild(1).getLabel()!=null) {
			version=getChildWithName(node,"version").getChild(1).getLabel();
		}
		
		return groupId+"/"+artifactId+"/"+version;
	}
	private static String getSiteId(ITree node) {
		String id="UKN";
		String url="UKN";
		
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(1)!=null && getChildWithName(node,"id").getChild(1).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(1).getLabel();
		}
		if(getChildWithName(node,"url")!=null && getChildWithName(node,"url").getChild(1)!=null && getChildWithName(node,"url").getChild(1).getLabel()!=null) {
			url=getChildWithName(node,"url").getChild(1).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+url;
	}
	private static String getPluginId(ITree node) {
		String groupId="UKN";
		String artifactId="UKN";
		String version="UKN";
		
		if(getChildWithName(node,"groupId")!=null && getChildWithName(node,"groupId").getChild(1)!=null && getChildWithName(node,"groupId").getChild(1).getLabel()!=null) {
			groupId=getChildWithName(node,"groupId").getChild(1).getLabel();
		}
		if(getChildWithName(node,"artifactId")!=null && getChildWithName(node,"artifactId").getChild(1)!=null && getChildWithName(node,"artifactId").getChild(1).getLabel()!=null) {
			artifactId=getChildWithName(node,"artifactId").getChild(1).getLabel();
		}
		if(getChildWithName(node,"version")!=null && getChildWithName(node,"version").getChild(1)!=null && getChildWithName(node,"version").getChild(1).getLabel()!=null) {
			version=getChildWithName(node,"version").getChild(1).getLabel();
		}
		
		return groupId+"/"+artifactId+"/"+version;
	}
	private static String getProfileId(ITree node) {
		String id="UKN";
		if(node!=null && node.getChildren()!=null && node.getChildren().size() > 1 && node.getChild(1).getChildren() != null && node.getChild(1).getChildren().size() > 1) {
			id=node.getChild(1).getChild(1).getLabel();
		}
		return id;
	}
	private static String getContributorId(ITree node) {
		String name="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(1)!=null && getChildWithName(node,"name").getChild(1).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(1).getLabel();
		}
		
		return name;
	}
	private static String getDeveloperId(ITree node) {
		String id="UKN";
		String name="UKN";
		String email="UKN";
		
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(1)!=null && getChildWithName(node,"id").getChild(1).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(1).getLabel();
		}
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(1)!=null && getChildWithName(node,"name").getChild(1).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(1).getLabel();
		}
		if(getChildWithName(node,"email")!=null && getChildWithName(node,"email").getChild(1)!=null && getChildWithName(node,"email").getChild(1).getLabel()!=null) {
			email=getChildWithName(node,"email").getChild(1).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+name+COMPLEX_SEPARATOR+email;
	}
	
	private static String getLicenseId(ITree node) {
		String name="UKN";
		String url="UKN";
		String comments="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(1)!=null && getChildWithName(node,"name").getChild(1).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(1).getLabel();
		}
		if(getChildWithName(node,"url")!=null && getChildWithName(node,"url").getChild(1)!=null && getChildWithName(node,"url").getChild(1).getLabel()!=null) {
			url=getChildWithName(node,"url").getChild(1).getLabel();
		}
		if(getChildWithName(node,"comments")!=null && getChildWithName(node,"comments").getChild(1)!=null && getChildWithName(node,"comments").getChild(1).getLabel()!=null) {
			comments=getChildWithName(node,"comments").getChild(1).getLabel();
		}
		
		return name+COMPLEX_SEPARATOR+url+COMPLEX_SEPARATOR+comments;
	}
	private static String getRepositoryId(ITree node) {
		String name="UKN";
		String url="UKN";
		String id="UKN";
		
		if(getChildWithName(node,"name")!=null && getChildWithName(node,"name").getChild(1)!=null && getChildWithName(node,"name").getChild(1).getLabel()!=null) {
			name=getChildWithName(node,"name").getChild(1).getLabel();
		}
		if(getChildWithName(node,"url")!=null && getChildWithName(node,"url").getChild(1)!=null && getChildWithName(node,"url").getChild(1).getLabel()!=null) {
			url=getChildWithName(node,"url").getChild(1).getLabel();
		}
		if(getChildWithName(node,"id")!=null && getChildWithName(node,"id").getChild(1)!=null && getChildWithName(node,"id").getChild(1).getLabel()!=null) {
			id=getChildWithName(node,"id").getChild(1).getLabel();
		}
		
		return id+COMPLEX_SEPARATOR+name+COMPLEX_SEPARATOR+url;
	}

	private static ITree getChildWithName(ITree node, String name) {
		List<ITree> children = node.getChildren();
		
		for (ITree child : children) {
			child.getChildrenLabels();
			if(child.getChildren()!=null && child.getChildren().size()>0 && name.equals(child.getChild(0).getLabel())) {
				return child;
			}
		}
		return null;
	}
}
