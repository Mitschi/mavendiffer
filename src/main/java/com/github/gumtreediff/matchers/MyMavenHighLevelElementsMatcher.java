package com.github.gumtreediff.matchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.aau.diff.maven.GumTreeMavenBuildChangeExtractor;

import com.github.gumtreediff.matchers.heuristic.gt.SubtreeMatcher;
import com.github.gumtreediff.tree.ITree;

public class MyMavenHighLevelElementsMatcher extends SubtreeMatcher {
	
	 private static int MIN_HEIGHT = Integer.parseInt(System.getProperty("gumtree.match.gt.minh", "2"));

	public MyMavenHighLevelElementsMatcher(ITree src, ITree dst, MappingStore store) {
		super(src, dst, store);
	}
	private void popLarger(PriorityTreeList srcs, PriorityTreeList dsts) {
        if (srcs.peekHeight() > dsts.peekHeight())
            srcs.open();
        else
            dsts.open();
    }
	@Override
	public void match() {
		mapHighLevelTag(src,dst,"dependencies");
//		mapHighLevelTag(src,dst,"parent");
//		mapHighLevelTag(src,dst,"build");
//		mapHighLevelTag(src,dst,"properties");
//		mapHighLevelTag(src,dst,"profiles");
		//TODO check for missing
	}
	private ITree getRootTag(ITree tree) {
		if(src.getChildren()!= null && src.getChildren().size()>3) {
			return src.getChild(3);
		}
		return null;
	}

	private void mapHighLevelTag(ITree src, ITree dst, String tagName) {
		ITree projectTagSrc = getRootTag(src);
		ITree projectTagDst = getRootTag(dst);
		
		List<ITree> firstLevelTagsSrc = projectTagSrc.getChildren();
		List<ITree> firstLevelTagsDst = projectTagDst.getChildren();
		for (ITree tSrc : firstLevelTagsSrc) {
			if (tSrc.getType() == 10) {
				// this is a child tag of <project/>, try to match it with the
				// equivalent of the other file
				String srcTagName=tSrc.getChild(0).getLabel();
				//check on Dst side
				for (ITree tDst : firstLevelTagsDst) {
					if (tDst.getType() == 10) {
						String dstTagName=tDst.getChild(0).getLabel();
						if(srcTagName.equals(dstTagName)) {
							addMapping(tSrc, tDst);
						}
					}
				}
			}
		}
	}
//	@Override
//	public void match() {
//        MultiMappingStore multiMappings = new MultiMappingStore();
//
//        PriorityTreeList srcs = new PriorityTreeList(src);
//        PriorityTreeList dsts = new PriorityTreeList(dst);
//
//        while (srcs.peekHeight() != -1 && dsts.peekHeight() != -1) {
//            while (srcs.peekHeight() != dsts.peekHeight())
//                popLarger(srcs, dsts);
//
//            List<ITree> hSrcs = srcs.pop();
//            List<ITree> hDsts = dsts.pop();
//
//            boolean[] srcMarks = new boolean[hSrcs.size()];
//            boolean[] dstMarks = new boolean[hDsts.size()];
//
//            for (int i = 0; i < hSrcs.size(); i++) {
//                for (int j = 0; j < hDsts.size(); j++) {
//                    ITree src = hSrcs.get(i);
//                    ITree dst = hDsts.get(j);
//
//                    if (src.isClone(dst)) {	
//                        multiMappings.link(src, dst);
//                        srcMarks[i] = true;
//                        dstMarks[j] = true;
//                    }
//                }
//            }
//
//            for (int i = 0; i < srcMarks.length; i++)
//                if (srcMarks[i] == false)
//                    srcs.open(hSrcs.get(i));
//            for (int j = 0; j < dstMarks.length; j++)
//                if (dstMarks[j] == false)
//                    dsts.open(hDsts.get(j));
//            srcs.updateHeight();
//            dsts.updateHeight();
//        }
//        
//        filterMappings(multiMappings);
//    }
	protected void addMapping(ITree src, ITree dst) {
		String tagNameSrc = GumTreeMavenBuildChangeExtractor.getTagName(src);
		String tagNameDst = GumTreeMavenBuildChangeExtractor.getTagName(dst);
			if(tagNameSrc!=null && tagNameSrc.equals(tagNameDst)) { //only map same tags
				System.out.println("Mapping "+tagNameSrc+" with "+tagNameDst);
		        src.setMatched(true);
		        dst.setMatched(true);
		        mappings.link(src, dst);
			}
    }
	public void filterMappings(MultiMappingStore multiMappings) {
        // Select unique mappings first and extract ambiguous mappings.
        List<Mapping> ambiguousList = new LinkedList<>();
        Set<ITree> ignored = new HashSet<>();
        for (ITree src: multiMappings.getSrcs()) {
            if (multiMappings.isSrcUnique(src))
                addFullMapping(src, multiMappings.getDst(src).iterator().next());
            else if (!ignored.contains(src)) {
                Set<ITree> adsts = multiMappings.getDst(src);
                Set<ITree> asrcs = multiMappings.getSrc(multiMappings.getDst(src).iterator().next());
                for (ITree asrc : asrcs)
                    for (ITree adst: adsts)
                        ambiguousList.add(new Mapping(asrc, adst));
                ignored.addAll(asrcs);
            }
        }

        // Rank the mappings by score.
        Set<ITree> srcIgnored = new HashSet<>();
        Set<ITree> dstIgnored = new HashSet<>();
        Collections.sort(ambiguousList, new MappingComparator(ambiguousList));

        // Select the best ambiguous mappings
        while (ambiguousList.size() > 0) {
            Mapping ambiguous = ambiguousList.remove(0);
            if (!(srcIgnored.contains(ambiguous.getFirst()) || dstIgnored.contains(ambiguous.getSecond()))) {
                addFullMapping(ambiguous.getFirst(), ambiguous.getSecond());
                srcIgnored.add(ambiguous.getFirst());
                dstIgnored.add(ambiguous.getSecond());
            }
        }
    }

    private class MappingComparator implements Comparator<Mapping> {

        private Map<Mapping, Double> simMap = new HashMap<>();

        public MappingComparator(List<Mapping> mappings) {
            for (Mapping mapping: mappings)
                simMap.put(mapping, sim(mapping.getFirst(), mapping.getSecond()));
        }

        public int compare(Mapping m1, Mapping m2) {
            return Double.compare(simMap.get(m2), simMap.get(m1));
        }

        private Map<ITree, List<ITree>> srcDescendants = new HashMap<>();

        private Map<ITree, Set<ITree>> dstDescendants = new HashMap<>();

        protected int numberOfCommonDescendants(ITree src, ITree dst) {
            if (!srcDescendants.containsKey(src))
                srcDescendants.put(src, src.getDescendants());
            if (!dstDescendants.containsKey(dst))
                dstDescendants.put(dst, new HashSet<>(dst.getDescendants()));

            int common = 0;

            for (ITree t: srcDescendants.get(src)) {
                ITree m = mappings.getDst(t);
                if (m != null && dstDescendants.get(dst).contains(m))
                    common++;
            }

            return common;
        }

//        protected double sim(ITree src, ITree dst) {
//            double jaccard = jaccardSimilarity(src.getParent(), dst.getParent());
//            System.out.println("JACCARD: "+jaccard);
//            int posSrc = (src.isRoot()) ? 0 : src.getParent().getChildPosition(src);
//            int posDst = (dst.isRoot()) ? 0 : dst.getParent().getChildPosition(dst);
//            int maxSrcPos =  (src.isRoot()) ? 1 : src.getParent().getChildren().size();
//            int maxDstPos =  (dst.isRoot()) ? 1 : dst.getParent().getChildren().size();
//            int maxPosDiff = Math.max(maxSrcPos, maxDstPos);
//            double pos = 1D - ((double) Math.abs(posSrc - posDst) / (double) maxPosDiff);
//            double po = 1D - ((double) Math.abs(src.getId() - dst.getId())
//                    / (double) MyMavenHighLevelElementsMatcher.this.getMaxTreeSize());
//            return 100 * jaccard + 10 * pos + po;
//        }
//
//        protected double jaccardSimilarity(ITree src, ITree dst) {
//            double num = (double) numberOfCommonDescendants(src, dst);
//            double den = (double) srcDescendants.get(src).size() + (double) dstDescendants.get(dst).size() - num;
//            return num / den;
//        }

        
        
    }
    private static class PriorityTreeList {

        private List<ITree>[] trees;

        private int maxHeight;

        private int currentIdx;

        @SuppressWarnings("unchecked")
        public PriorityTreeList(ITree tree) {
            int listSize = tree.getHeight() - MIN_HEIGHT + 1;
            if (listSize < 0)
                listSize = 0;
            if (listSize == 0)
                currentIdx = -1;
            trees = (List<ITree>[]) new ArrayList[listSize];
            maxHeight = tree.getHeight();
            addTree(tree);
        }

        private int idx(ITree tree) {
            return idx(tree.getHeight());
        }

        private int idx(int height) {
            return maxHeight - height;
        }

        private int height(int idx) {
            return maxHeight - idx;
        }

        private void addTree(ITree tree) {
            if (tree.getHeight() >= MIN_HEIGHT) {
                int idx = idx(tree);
                if (trees[idx] == null) trees[idx] = new ArrayList<>();
                trees[idx].add(tree);
            }
        }

        public List<ITree> open() {
            List<ITree> pop = pop();
            if (pop != null) {
                for (ITree tree: pop) open(tree);
                updateHeight();
                return pop;
            } else return null;
        }

        public List<ITree> pop() {
            if (currentIdx == -1)
                return null;
            else {
                List<ITree> pop = trees[currentIdx];
                trees[currentIdx] = null;
                return pop;
            }
        }

        public void open(ITree tree) {
            for (ITree c: tree.getChildren()) addTree(c);
        }

        public int peekHeight() {
            return (currentIdx == -1) ? -1 : height(currentIdx);
        }

        public void updateHeight() {
            currentIdx = -1;
            for (int i = 0; i < trees.length; i++) {
                if (trees[i] != null) {
                    currentIdx = i;
                    break;
                }
            }
        }

    }
}

//DEPENDENCY_DELETE
//DEPENDENCY_DELETE
//PROJECT_PACKAGING_UPDATE
//PROJECT_NAME_UPDATE
//GENERAL_PROPERTY_UPDATE