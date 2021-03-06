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

import com.github.gumtreediff.matchers.heuristic.gt.SubtreeMatcher;
import com.github.gumtreediff.tree.ITree;

public class PomGreedySubtreeMatcher extends SubtreeMatcher {

    public static int MIN_HEIGHT = Integer.parseInt(System.getProperty("gumtree.match.gt.minh", "2"));
    
    public PomGreedySubtreeMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }
    
    public void match() {
        MultiMappingStore multiMappings = new MultiMappingStore();

        PriorityTreeList srcs = new PriorityTreeList(src);
        PriorityTreeList dsts = new PriorityTreeList(dst);

        while (srcs.peekHeight() != -1 && dsts.peekHeight() != -1) {
            while (srcs.peekHeight() != dsts.peekHeight())
                popLarger(srcs, dsts);

            List<ITree> hSrcs = srcs.pop();
            List<ITree> hDsts = dsts.pop();

            boolean[] srcMarks = new boolean[hSrcs.size()];
            boolean[] dstMarks = new boolean[hDsts.size()];

            for (int i = 0; i < hSrcs.size(); i++) {
                for (int j = 0; j < hDsts.size(); j++) {
                    ITree src = hSrcs.get(i);
                    ITree dst = hDsts.get(j);

                    if (src.isClone(dst)) {
//                    	System.out.println("SubtreeMatcher - Linking: "+src.getLabel() + " || "+dst.getLabel());
                        multiMappings.link(src, dst);
                        srcMarks[i] = true;
                        dstMarks[j] = true;
                    }
                }
            }

            for (int i = 0; i < srcMarks.length; i++)
                if (srcMarks[i] == false)
                    srcs.open(hSrcs.get(i));
            for (int j = 0; j < dstMarks.length; j++)
                if (dstMarks[j] == false)
                    dsts.open(hDsts.get(j));
            srcs.updateHeight();
            dsts.updateHeight();
        }

        filterMappings(multiMappings);
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
    
    private void popLarger(PriorityTreeList srcs, PriorityTreeList dsts) {
        if (srcs.peekHeight() > dsts.peekHeight())
            srcs.open();
        else
            dsts.open();
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

        protected double sim(ITree src, ITree dst) {
            double jaccard = jaccardSimilarity(src.getParent(), dst.getParent());
            int posSrc = (src.isRoot()) ? 0 : src.getParent().getChildPosition(src);
            int posDst = (dst.isRoot()) ? 0 : dst.getParent().getChildPosition(dst);
            int maxSrcPos =  (src.isRoot()) ? 1 : src.getParent().getChildren().size();
            int maxDstPos =  (dst.isRoot()) ? 1 : dst.getParent().getChildren().size();
            int maxPosDiff = Math.max(maxSrcPos, maxDstPos);
            double pos = 1D - ((double) Math.abs(posSrc - posDst) / (double) maxPosDiff);
            double po = 1D - ((double) Math.abs(src.getId() - dst.getId())
                    / (double) PomGreedySubtreeMatcher.this.getMaxTreeSize());
            return 100 * jaccard + 10 * pos + po;
        }

        protected double jaccardSimilarity(ITree src, ITree dst) {
            double num = (double) numberOfCommonDescendants(src, dst);
            double den = (double) srcDescendants.get(src).size() + (double) dstDescendants.get(dst).size() - num;
            return num / den;
        }
        
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
