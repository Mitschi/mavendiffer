package com.github.gumtreediff.matchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.simmetrics.metrics.Levenshtein;

import at.aau.diff.maven.extractor.GumTreeMavenBuildChangeExtractorSax;

import com.github.gumtreediff.matchers.optimal.zs.ZsMatcher;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeMap;
import com.github.gumtreediff.tree.TreeUtils;

public class MyBottomUpMatcher extends Matcher {
	
	private static final double SIM_THRESHOLD = Double.parseDouble(System.getProperty("gumtree.match.bu.sim", "0.3"));
//	private static final double SIM_THRESHOLD = 0.8;

    private static final int SIZE_THRESHOLD = Integer.parseInt(System.getProperty("gumtree.match.bu.size", "1000"));

    private TreeMap srcIds;

    private TreeMap dstIds;
	public MyBottomUpMatcher(ITree src, ITree dst, MappingStore store) {
		super(src, dst, store);
	}
	protected void addMapping(ITree src, ITree dst) {
		String tagNameSrc = getTagName(src);
		String tagNameDst = getTagName(dst);
		if(("groupId".equals(tagNameSrc) && "groupId".equals(tagNameDst)) || ("artifactId".equals(tagNameSrc) && "artifactId".equals(tagNameDst))) {
//			System.out.println("NOW");
//			System.out.println(tagNameSrc+" // "+src.getChild(0).getLabel());
//			System.out.println(tagNameDst+" // "+dst.getChild(0).getLabel());
		}
		if((tagNameSrc==null && tagNameDst==null) || (tagNameSrc!=null && tagNameSrc.equals(tagNameDst))) { //only map same tags
			if(isValidMapping(src,dst)) {
		        src.setMatched(true);
		        dst.setMatched(true);
		        mappings.link(src, dst);
//		        System.out.println("MAPPED: "+ tagNameSrc +" // "+tagNameDst);
			}
		}
    }
	protected void addFullMapping(ITree src, ITree dst) {
//		System.out.println("ADD FULL MAPPING");
        super.addFullMapping(src, dst);
    }
	private boolean isValidMapping(ITree src, ITree dst) {
		String tagNameSrc = getTagName(src);
		String tagNameDst = getTagName(dst);
		
		if("pluginRepository".equals(tagNameSrc) ||"repository".equals(tagNameSrc) ||"profile".equals(tagNameSrc) ) {
			String idSrc = getTagValue(src,"id");
			String idDst = getTagValue(dst,"id");
//			return idSrc!=null && idSrc.equals(idDst); 
			Levenshtein l = new Levenshtein();
			return idSrc!=null && l.compare(idSrc, idDst)>0.65;
		}
		if("dependency".equals(tagNameSrc) || "plugin".equals(tagNameSrc)) {
			String groupIdSrc = getTagValue(src,"groupId");
			String artifactIdSrc = getTagValue(src, "artifactId");
			String groupIdDst = getTagValue(dst,"groupId");
			String artifactIdDst = getTagValue(dst, "artifactId");
			
			
//			return (groupIdSrc!=null && groupIdSrc.equals(groupIdDst)) && (artifactIdSrc!=null && artifactIdSrc.equals(artifactIdDst));
			
//			return (groupIdSrc!=null && l.compare(groupIdSrc, groupIdDst)>0.65 && (artifactIdSrc!=null && l.compare(artifactIdSrc, artifactIdDst) > 0.65));
//			return isMatching(groupIdSrc,groupIdDst) && isMatching(artifactIdSrc,artifactIdDst);
			return isMatchingHeuristic(groupIdSrc,groupIdDst,artifactIdSrc,artifactIdDst);
		}
		//TODO other tags
		
		return true;
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
	
	private boolean hasSameContext(ITree src, ITree dst) {
		System.out.println("Checking:");
		System.out.println(src);
		System.out.println("and:");
		System.out.println(dst);
		
		ITree srcTmp=src;
		ITree dstTmp=dst;
		
		while (srcTmp != null) {
			if(dstTmp!=null) {
				if(!srcTmp.equals(dstTmp)) {
					System.out.println("FALSE");
					return false;
				}
			}
			
			srcTmp = src.getParent();
			dstTmp = dst.getParent();
		}
		System.out.println("TRUE");
		return true;
	}
	private static String getTagName(ITree node) {
//		System.out.println(node.getType());
		if(node.getType()==1) {
			return node.getLabel();
		}
		if(node.getType()==3) {
			return node.getParent().getLabel();
		}
		return null;
	}
	private boolean isMatchingHeuristic(String groupIdSrc, String groupIdDst, String artifactIdSrc, String artifactIdDst) {
		if(groupIdSrc!=null &&  artifactIdSrc!=null) {
			Levenshtein l = new Levenshtein();
			if(groupIdSrc.equals(groupIdDst) && artifactIdSrc.equals(artifactIdDst)) { //both equal -> ok
//				System.out.println(l.compare(artifactIdSrc, artifactIdDst)+" for: "+artifactIdSrc + " and "+ artifactIdDst);
//				System.out.println(l.compare(groupIdSrc, groupIdDst)+" for: "+groupIdSrc + " and "+ groupIdDst);
				return true;
			}
			if((groupIdSrc.contains(groupIdDst) && l.compare(artifactIdSrc, artifactIdDst)>0.65)||
			   (artifactIdSrc.contains(artifactIdSrc) && l.compare(groupIdSrc, groupIdDst)>0.65)) { //-> one contains the other and the other one is similar
//				System.out.println(l.compare(artifactIdSrc, artifactIdDst)+" for: "+artifactIdSrc + " and "+ artifactIdDst);
//				System.out.println(l.compare(groupIdSrc, groupIdDst)+" for: "+groupIdSrc + " and "+ groupIdDst);
				return true;
			}
			if((groupIdDst.contains(groupIdSrc) && l.compare(artifactIdSrc, artifactIdDst)>0.65)||
			   (artifactIdSrc.contains(artifactIdSrc) && l.compare(groupIdSrc, groupIdDst)>0.65)) { //-> one contains the other and the other one is similar
//				System.out.println(l.compare(artifactIdSrc, artifactIdDst)+" for: "+artifactIdSrc + " and "+ artifactIdDst);
//				System.out.println(l.compare(groupIdSrc, groupIdDst)+" for: "+groupIdSrc + " and "+ groupIdDst);
				return true;
			}
			
//			System.out.println(l.compare(artifactIdSrc, artifactIdDst)+" for: "+artifactIdSrc + " and "+ artifactIdDst);
//			System.out.println(l.compare(groupIdSrc, groupIdDst)+" for: "+groupIdSrc + " and "+ groupIdDst);
			return (l.compare(artifactIdSrc, artifactIdDst)>0.65 && l.compare(groupIdSrc, groupIdDst)>0.65) || ((l.compare(artifactIdSrc, artifactIdDst) + l.compare(groupIdSrc, groupIdDst))/2>0.65);
		}
		return false;
	}
	private boolean isMatching(String src, String dst) {
		if(src!=null ) {
			if(src.equals(dst)) {
				return true;
			}
			if(src.contains(dst)) {
				return true;
			}
			if(dst != null && dst.contains(src)) {
				return true;
			}
			Levenshtein l = new Levenshtein();
			System.out.println(l.compare(src, dst));
			return l.compare(src, dst)>0.65;
		}
		return false;
	}
	private String getTagValue(ITree tree,String tag) {
		ITree childWithName = GumTreeMavenBuildChangeExtractorSax.getChildWithName(tree, tag);
		if(childWithName!=null && childWithName.getChildren()!=null && childWithName.getChildren().size()>0) {
			return childWithName.getChild(0).getLabel();
		}
		return "";
//		List<ITree> children = tree.getChildren();
//		for (ITree child : children) {
//			if(child.getChildren()!=null && child.getChildren().size()>1) {
//				if(tag.equals(child.getChild(0).getLabel())) {
//					return child.getChild(1).getLabel();
//				}
//			}
//		}
//		return "";
	}
	public void match() {
        srcIds = new TreeMap(src);
        dstIds = new TreeMap(dst);

        for (ITree t: src.postOrder())  {
            if (t.isRoot()) {
                addMapping(t, this.dst);
                lastChanceMatch(t, this.dst);
                break;
            } else if (!(t.isMatched() || t.isLeaf())) {
                List<ITree> candidates = getDstCandidates(t);
                ITree best = null;
                double max = -1D;

                for (ITree cand: candidates) {
                    double sim = jaccardSimilarity(t, cand);
                    if (sim > max && sim >= SIM_THRESHOLD) {
                        max = sim;
                        best = cand;
                    }
                }

                if (best != null) {
                    lastChanceMatch(t, best);
                    addMapping(t, best);
                }
            }
        }
        clean();
    }

    private List<ITree> getDstCandidates(ITree src) {
        List<ITree> seeds = new ArrayList<>();
        for (ITree c: src.getDescendants()) {
            ITree m = mappings.getDst(c);
            if (m != null) seeds.add(m);
        }
        List<ITree> candidates = new ArrayList<>();
        Set<ITree> visited = new HashSet<>();
        for (ITree seed: seeds) {
            while (seed.getParent() != null) {
                ITree parent = seed.getParent();
                if (visited.contains(parent))
                    break;
                visited.add(parent);
                if (parent.getType() == src.getType() && !parent.isMatched() && !parent.isRoot())
                    candidates.add(parent);
                seed = parent;
            }
        }

        return candidates;
    }

    //FIXME checks if it is better or not to remove the already found mappings.
    private void lastChanceMatch(ITree src, ITree dst) {
        ITree cSrc = src.deepCopy();
        ITree cDst = dst.deepCopy();
        TreeUtils.removeMatched(cSrc);
        TreeUtils.removeMatched(cDst);

        if (cSrc.getSize() < SIZE_THRESHOLD || cDst.getSize() < SIZE_THRESHOLD) {
            Matcher m = new ZsMatcher(cSrc, cDst, new MappingStore());
            m.match();
            for (Mapping candidate: m.getMappings()) {
                ITree left = srcIds.getTree(candidate.getFirst().getId());
                ITree right = dstIds.getTree(candidate.getSecond().getId());

                if (left.getId() == src.getId() || right.getId() == dst.getId()) {
                    //System.err.println("Trying to map already mapped source node.");
                    continue;
                } else if (!left.isMatchable(right)) {
                    //System.err.println("Trying to map not compatible nodes.");
                    continue;
                } else if (left.getParent().getType() != right.getParent().getType()) {
                    //System.err.println("Trying to map nodes with incompatible parents");
                    continue;
                } else addMapping(left, right);
            }
        }

        for (ITree t : src.getTrees())
            t.setMatched(true);
        for (ITree t : dst.getTrees())
            t.setMatched(true);
    }
}
