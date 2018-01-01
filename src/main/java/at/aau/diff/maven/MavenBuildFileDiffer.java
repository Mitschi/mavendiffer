package at.aau.diff.maven;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

//import com.github.gumtreediff.gen.sax.MavenPomTreeGeneratorDOM;
import com.github.gumtreediff.gen.sax.PositionalMavenPomTreeGeneratorDOM;
import com.github.gumtreediff.matchers.*;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import at.aau.diff.common.Change;
import at.aau.diff.common.Differ;
import at.aau.diff.maven.extractor.GumTreeMavenBuildChangeExtractorSax;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.tree.ITree;

public class MavenBuildFileDiffer implements Differ {

    @Override
    public List<Change> extractChanges(File oldTmpFile, File newTmpFile) throws Exception {
        String s1= FileUtils.readFileToString(oldTmpFile);
        String s2= FileUtils.readFileToString(newTmpFile);

        List<Change> changes = extractChanges(stringToBaos(s1), stringToBaos(s2));

        return changes;
    }

    @Override
	public List<Change> extractChanges(ByteArrayOutputStream file1, ByteArrayOutputStream file2) throws UnsupportedOperationException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		/*
		 * double gumtree.match.xy.sim 0.5: similarity value for XyDiff.
int gumtree.match.gt.minh 2: minimum matching height for GumTree top-down phase.
double gumtree.match.bu.sim 0.3: minimum similarity threshold for matching in GumTree bottom-up phase.
double gumtree.match.bu.size 1000: maximum subtree size to apply the optimal algorithm in GumTree bottum-up phase.
		 */
//		System.setProperty("gumtree.match.xy.sim", "1.0");
//		System.setProperty("gumtree.match.gt.minh", "2");
//		System.setProperty("gumtree.match.bu.sim", "0.65");
//		System.setProperty("gumtree.match.bu.size", "1000");
		
		Run.initGenerators();
		
//        ITree src = new MavenPomTreeGeneratorDOM().generateFromString(new String(file1.toByteArray())).getRoot();
//		ITree dst = new MavenPomTreeGeneratorDOM().generateFromString(new String(file2.toByteArray())).getRoot();
		ITree src = new PositionalMavenPomTreeGeneratorDOM().generateFromString(new String(file1.toByteArray())).getRoot();
		ITree dst = new PositionalMavenPomTreeGeneratorDOM().generateFromString(new String(file2.toByteArray())).getRoot();

		MappingStore mappingStore = new MappingStore();
        Matcher m= new MyMatchers.PomMatcher(src, dst, mappingStore);

		m.match();
//		System.out.println("Done, total mappings found: "+m.getMappings().asSet().size());
		ActionGenerator g = new ActionGenerator(src, dst, m.getMappings());
		g.generate();
        List<Action> actions = g.getActions(); // return the actions
//        for(Action a : actions) {
//            System.out.println(a.getNode().getPos()+" " +a.getNode().getLength());
////            if(a instanceof Insert) {
////                System.out.println(((Insert) a).getPosition());
////            } else if(a instanceof Delete) {
////                System.out.println(a.getNode().getPos());
////            } else if(a instanceof Update) {
////
////            } else {
////                System.out.println("STRANGE!!!");
////            }
//        }
//		System.out.println("Nr of actions: "+actions.size());
		//maybe we need to delete the wrapper tag changes before this step (because they are
		//sorted before the single change tags, and hence, might trigger a change. then they are
		//added to the ignorechildren list, and the correct changes cannot be retrieved.
		actions = sortParentChild(actions);
		
		List<Change> changes = GumTreeMavenBuildChangeExtractorSax.extract(actions,g, mappingStore);

        for (Change change : changes) {
            MavenBuildChange bc = (MavenBuildChange) change;
            System.out.println("======== BC: "+bc.getName()+" ========");
            System.out.println(" SrcStartLine:SrcStartOffset  "+bc.getSrcPositionInfo().getStartLineNumber()+":"+bc.getSrcPositionInfo().getStartLineOffset());
            System.out.println(" SrcEndLine:SrcEndOffset  "+bc.getSrcPositionInfo().getEndLineNumber()+":"+bc.getSrcPositionInfo().getEndLineOffset());
            System.out.println(" ====== AND ======");
            System.out.println(" DstStartLine:DstStartOffset  "+bc.getDstPositionInfo().getStartLineNumber()+":"+bc.getDstPositionInfo().getStartLineOffset());
            System.out.println(" DstEndLine:DstEndOffset  "+bc.getDstPositionInfo().getEndLineNumber()+":"+bc.getDstPositionInfo().getEndLineOffset());
        }


        return changes;
	}

    private List<Action> orderProperNew(List<Action> actions) {
		List<Action> firstLevel = new ArrayList<Action>();
		List<Action> secondLevel = new ArrayList<Action>();
		List<Action> thirdLevel = new ArrayList<Action>();
		List<Action> fourthLevel = new ArrayList<Action>();
		List<Action> others = new ArrayList<Action>();
		List<String> firstLevelTags = Arrays.asList(new String[]{"profile","plugin"});
		List<String> secondLevelTags = Arrays.asList(new String[]{"dependency","repository","snapshotRepository","pluginRepository"});
		List<String> thirdLevelTags = Arrays.asList(new String[]{"configuration","license","resource","testResource","developer","contributor"});
		
//	    public static final int CDATA_ID = 3;
//	    public static final int DOCUMENT_ID = 0;
//	    public static final int ATTR_ID = 2;
//	    public static final int ELT_ID = 1;
//	    public static final int VALUE_ID = 4;
	    
		//sort known types
		for (Action action : actions) {
			if(action.getNode().getType()==1) {
				if(firstLevelTags.contains(action.getNode().getLabel())) {
					firstLevel.add(action);
				} else if(secondLevelTags.contains(action.getNode().getLabel())) {
					secondLevel.add(action);
				} else if(thirdLevelTags.contains(action.getNode().getLabel())) {
					thirdLevel.add(action);
				} else {
					fourthLevel.add(action);
				}
			} else {
				others.add(action);
			}
		}
		
		//for each level sort according to a parent child relation -> parent must be first
		firstLevel = sortParentChild(firstLevel);
		firstLevel = sortParentChild(secondLevel);
		firstLevel = sortParentChild(thirdLevel);
		firstLevel = sortParentChild(fourthLevel);
		firstLevel = sortParentChild(others);
		
		List<Action> actionsNew = new ArrayList<Action>();
		actionsNew.addAll(firstLevel);
		actionsNew.addAll(secondLevel);
		actionsNew.addAll(thirdLevel);
		actionsNew.addAll(fourthLevel);
		actionsNew.addAll(others);
		
		return actionsNew;
	}
	
	private List<Action> sortParentChild(List<Action> level) {
		Collections.sort(level, new Comparator<Action>() {
	        @Override
	        public int compare(Action a1, Action a2)
	        {
//	        	System.out.println(a2.getNode());
//	        	System.out.println(a1.getNode());
//	        	System.out.println("============================");
	        	if(isParent(a2.getNode(),a1.getNode())) {
	        		return -1;
	        	}
	        	if(isParent(a1.getNode(),a2.getNode())) {
	        		return 1;
	        	}
	        	return 0;
	        }

			private boolean isParent(ITree child, ITree parent) {
				ITree tmp = child;
				while(tmp!=null) {
					if(tmp.equals(parent)) {
						return true;
					}
					tmp=tmp.getParent();
				}
				return false;
			}
	    });
		return level;
	}

	private List<Action> orderProper(List<Action> actions) {
		List<Action> firstLevel = new ArrayList<Action>();
		List<Action> secondLevel = new ArrayList<Action>();
		List<Action> thirdLevel = new ArrayList<Action>();
		List<Action> fourthLevel = new ArrayList<Action>();
		List<Action> others = new ArrayList<Action>();
		List<String> firstLevelTags = Arrays.asList(new String[]{"profile","plugin"});
		List<String> secondLevelTags = Arrays.asList(new String[]{"dependency","repository","snapshotRepository","pluginRepository"});
		List<String> thirdLevelTags = Arrays.asList(new String[]{"configuration","license","resource","testResource"});
		
		for (Action action : actions) {
			if(action.getNode().getType()==10) {
				if(firstLevelTags.contains(action.getNode().getChild(0).getLabel())) {
					firstLevel.add(action);
				} else if(secondLevelTags.contains(action.getNode().getChild(0).getLabel())) {
					secondLevel.add(action);
				} else if(thirdLevelTags.contains(action.getNode().getChild(0).getLabel())) {
					thirdLevel.add(action);
				} else {
					fourthLevel.add(action);
				}
			} else
			{
				others.add(action);
			}
		}
		
		List<Action> actionsNew = new ArrayList<Action>();
		actionsNew.addAll(firstLevel);
		actionsNew.addAll(secondLevel);
		actionsNew.addAll(thirdLevel);
		actionsNew.addAll(fourthLevel);
		actionsNew.addAll(others);
		
		return actionsNew;
	}

	public ByteArrayOutputStream preprocessPomFile(ByteArrayOutputStream pom) throws IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		String content = new String(pom.toByteArray());

		content=sortIfPossible(content);
		content = removeComments(content);
		content = removeCustomAttribute(content, "combine.children");
		content = removeCustomAttribute(content, "combine.self");

        ByteArrayOutputStream baos = stringToBaos(content);

		return baos;
	}

	private ByteArrayOutputStream stringToBaos(String content) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        pw.write(content);
        pw.flush();
        pw.close();

        return baos;
    }

	private String sortIfPossible(String content) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document parse1 = builder.parse(new ByteArrayInputStream(content.getBytes()));
		
		//sort modules
		content=sortSubtagsOfTag(content, parse1,"modules",new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.getTextContent().compareTo(o2.getTextContent());
			}
		});
		
		//sort properties
		content=sortSubtagsOfTag(content, parse1,"properties",new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.getNodeName().compareTo(o2.getNodeName());
			}
		});
		
		//sort dependencies
		content=sortSubtagsOfTag(content, parse1,"dependencies",new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				String groupIdO1 = getChildValue(o1,"groupId");
				String groupIdO2 = getChildValue(o2,"groupId");
				if(groupIdO1.compareTo(groupIdO2)==0) {
					//check artifactId
					String artifactIdO1 = getChildValue(o1,"artifactId");
					String artifactIdO2 = getChildValue(o2,"artifactId");
					return artifactIdO1.compareTo(artifactIdO2);
				}
				return groupIdO1.compareTo(groupIdO2);
			}
		});
		
		return content;
	}
	
	private String getChildValue(Node node, String childTagName) {
		NodeList childNodes = node.getChildNodes();
		
		for(int i=0;i<childNodes.getLength();i++) {
			Node childNode = childNodes.item(i);
			if(childTagName.equals(childNode.getNodeName())) {
				return childNode.getTextContent();
			}
		}
		return null;
	}

	private String sortSubtagsOfTag(String content, Document parse1, String tagName, Comparator<Node> comparator) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		NodeList modulesTag = parse1.getElementsByTagName(tagName);
		if(modulesTag.getLength()>0) { // only if subtags are given
			Node modulesNode = modulesTag.item(0);
			NodeList modulesChildNodes = modulesNode.getChildNodes();
			
			List<Node> nodeListModules = new ArrayList<Node>();
			for(int i=0;i<modulesChildNodes.getLength();i++) {
				Node moduleNode = modulesChildNodes.item(i);
				if(moduleNode.getNodeType()==1) {
					nodeListModules.add(moduleNode);
				}
			}
			for(int i=modulesChildNodes.getLength()-1;i>=0;i--) {
				Node moduleNode = modulesChildNodes.item(i);
				modulesNode.removeChild(moduleNode);
			}
			
			Collections.sort(nodeListModules,comparator);
			
			for(Node n :nodeListModules) {
				modulesNode.appendChild(n);
			}
			return printToString(parse1);
		}
		return content;
	}
	
	public String printToString(Document document) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
		LSSerializer writer = impl.createLSSerializer();
		writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
		LSOutput output = impl.createLSOutput();
//		output.setByteStream(System.out);
		StringWriter sw = new StringWriter();
		output.setCharacterStream(sw);
		writer.write(document, output);
		return sw.toString();
    }

	private String removeComments(String content) {
		String helper = content;
		while (helper.contains("<!--")) {
			int propertiesStart = helper.indexOf("<!--");
			int propertiesEnd = helper.indexOf("-->", propertiesStart) + ("-->").length();
			if (propertiesStart != -1 && propertiesEnd != -1) {
				helper = helper.substring(0, propertiesStart) + helper.substring(propertiesEnd, helper.length());
			}
		}
		return helper;
	}

	private String removeCustomAttribute(String content, String attributeName) {
		String helper = content;
		while (helper.contains(attributeName)) {
			int propertiesStart = helper.indexOf(attributeName);
			int propertiesStartX = helper.indexOf("\"", propertiesStart);
			int propertiesEnd = helper.indexOf("\"", propertiesStartX + 1);
//			String test = helper.substring(propertiesStart, propertiesEnd);
			if (propertiesStart != -1 && propertiesEnd != -1) {
				helper = helper.substring(0, propertiesStart) + helper.substring(propertiesEnd + 1, helper.length());
			}
		}
		return helper;
	}
}
