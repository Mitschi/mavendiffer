package com.github.gumtreediff.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simmetrics.metrics.Levenshtein;

import com.github.gumtreediff.tree.ITree;

public class TestMatcher extends Matcher {

		private static final double SIM_THRESHOLD = 0.3d;
		Map<String,List<ITree>> srcTreesOfTag=new HashMap<String, List<ITree>>();
		Map<String,List<ITree>> dstTreesOfTag=new HashMap<String, List<ITree>>();
		private Levenshtein levenshtein = new Levenshtein();
	
	    public TestMatcher(ITree src, ITree dst, MappingStore store) {
	        super(src, dst, store);
	    }

	    public void match() {
	    	List<String> allowedTags = Arrays.asList(new String[]{"dependency"});
	    	//iterate over all src and/or dst and make mappings according to the ids etc
	    	System.out.println("TestMatcher");
	    	ITree projectSrc = getChildByName(src, "project");
	    	ITree projectDst = getChildByName(dst, "project");
	    	System.out.println(projectSrc.getLabel());
	    	System.out.println(projectDst.getLabel());
	    	mappings.link(projectSrc, projectDst);
//	    	String[] firstLevel=new String[]{"dependencies",""};
	    	walkToFindTags(projectSrc,this.srcTreesOfTag);
	    	walkToFindTags(projectDst,this.dstTreesOfTag);
	    	
	    	//merge keys
	    	Set<String> mergedTags = new HashSet<String>();
	    	mergedTags.addAll(srcTreesOfTag.keySet());
	    	mergedTags.addAll(dstTreesOfTag.keySet());
	    	
	    	for (String tag : mergedTags) {
	    		if(allowedTags.contains(tag)) {
	    			//for each tag that occurred either in src or dst, try to map 
	    			linkPerTag(srcTreesOfTag.get(tag),dstTreesOfTag.get(tag));
	    		}
			}
	    	System.out.println("DONE, found number of mappings: "+mappings.asSet().size());
	    }

	    private void linkPerTag(List<ITree> src, List<ITree> dst) {
	    	List<ITree> alreadyMatched = new ArrayList<ITree>();
	    	if(src !=null) {
				for (ITree t : src) {
					if(!alreadyMatched.contains(t)) {
						double bestThreshold=0.0d;
						ITree bestMatch=null;
						//TODO if possible: find 100% match (tagname, id, maven triplet...)
						//find best not 100% match
						if(dst!=null) { //might happen that dst does not provide the tag 
							for(ITree cand : dst) {
								if(!alreadyMatched.contains(cand)) {
									double sim = similarity(t,cand);
									System.out.println("Comparing: ("+sim+")");
									System.out.print("  ");
									System.out.println(t.toShortString()+" "+t.getLabel()+" ("+getMavenTripleId(t)+")");
									System.out.print("  ");
									System.out.println(cand.toShortString()+" "+cand.getLabel()+" ("+getMavenTripleId(cand)+")");
									if(sim > SIM_THRESHOLD && sim > bestThreshold) {
										bestThreshold=sim;
										bestMatch=cand;
									}
								}
							}
							if(bestMatch!=null && bestThreshold > SIM_THRESHOLD) {
								mappings.link(t, bestMatch);
								alreadyMatched.add(t);
								alreadyMatched.add(bestMatch);
							}
						}
					}
				}
	    	}
		}

		private double similarity(ITree t, ITree cand) {
			if(t.getLabel() != null && cand.getLabel() !=null && t.getLabel().equals(cand.getLabel())) {
//			double sim=jaccardSimilarity(t, cand);
			double sim=0.0d;
			if("dependency".equals(t.getLabel())) {
				String tTriple=getMavenTripleId(t);
				String candTriple=getMavenTripleId(cand);
				sim=levenshtein.compare(tTriple, candTriple);
			}
			return sim;
			}
			else {
				return 0.0;
			}
		}

	private String getMavenTripleId(ITree node) {
		String groupId = "UKN";
		String artifactId = "UKN";
		String version = "UKN";
		
		if(getChildByName(node, "groupId") !=null && getChildByName(node, "groupId").getChildren()!=null && getChildByName(node, "groupId").getChildren().size()>0) {
			groupId = getChildByName(node, "groupId").getChild(0).getLabel();
		}
		if(getChildByName(node, "artifactId") !=null && getChildByName(node, "artifactId").getChildren()!=null && getChildByName(node, "artifactId").getChildren().size()>0) {
			artifactId = getChildByName(node, "artifactId").getChild(0).getLabel();
		}
		if(getChildByName(node, "version") !=null && getChildByName(node, "version").getChildren()!=null && getChildByName(node, "version").getChildren().size()>0) {
			version = getChildByName(node, "version").getChild(0).getLabel();
		}
		
		return groupId+"/"+artifactId+"/"+version;
	}

		private void walkToFindTags(ITree start,Map<String,List<ITree>> map) {
			List<ITree> children = start.getChildren();
			for (ITree child : children) {
				if(child.getType()==1) { //it's a tag
					if(!map.containsKey(child.getLabel())) {
						map.put(child.getLabel(), new ArrayList<ITree>());
					}
					map.get(child.getLabel()).add(child);
					if(child.getChildren().size()>0) {
						walkToFindTags(child, map);
					}
				}
			}
		}

		private static ITree getParentByName(ITree node, String name) {
			ITree tmp = node;
			while(tmp.getParent()!=null) {
				if(name.equals(tmp.getLabel())) {
					return tmp;
				}
				tmp=tmp.getParent();
			}
			return null;
		}

		private static ITree getChildByName(ITree node, String name) {
			List<ITree> children = node.getChildren();
			
			for (ITree child : children) {
				if(name.equals(child.getLabel())) {
					return child;
				}
			}
			return null;
		}
}