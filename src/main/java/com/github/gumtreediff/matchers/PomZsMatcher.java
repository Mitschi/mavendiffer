/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.matchers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simmetrics.StringMetrics;
import org.simmetrics.metrics.Levenshtein;

import com.github.gumtreediff.tree.ITree;

public class PomZsMatcher extends Matcher {

    private static final float THRESHOLD_FOR_MATCHING = 1.00f;
	private ZsTree src;
    private ZsTree dst;

    private double[][] treeDist;
    private double[][] forestDist;

    private static ITree getFirstLeaf(ITree t) {
        ITree current = t;
        while (!current.isLeaf())
            current = current.getChild(0);

        return current;
    }

    public PomZsMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
        this.src = new ZsTree(src);
        this.dst = new ZsTree(dst);
    }

    private double[][] computeTreeDist() {

        treeDist = new double[src.nodeCount + 1][dst.nodeCount + 1];
        forestDist = new double[src.nodeCount + 1][dst.nodeCount + 1];

        for (int i = 1; i < src.kr.length; i++) {
            for (int j = 1; j < dst.kr.length; j++) {
                forestDist(src.kr[i], dst.kr[j]);

            }
        }

        return treeDist;
    }

    private void forestDist(int i, int j) {
        forestDist[src.lld(i) - 1][dst.lld(j) - 1] = 0;
        for (int di = src.lld(i); di <= i; di++) {
            double costDel =  getDeletionCost(src.tree(di));
            forestDist[di][dst.lld(j) - 1] = forestDist[di - 1][dst.lld(j) - 1] + costDel;
            for (int dj = dst.lld(j); dj <= j; dj++) {
                double costIns = getInsertionCost(dst.tree(dj));
                forestDist[src.lld(i) - 1][dj] = forestDist[src.lld(i) - 1][dj - 1] + costIns;

                if ((src.lld(di) == src.lld(i) && (dst.lld(dj) == dst.lld(j)))) {
                    double costUpd = getUpdateCost(src.tree(di), dst.tree(dj));
                    forestDist[di][dj] = Math.min(Math.min(forestDist[di - 1][dj] + costDel,
                                    forestDist[di][dj - 1] + costIns),
                            forestDist[di - 1][dj - 1] + costUpd);
                    treeDist[di][dj] = forestDist[di][dj];
                } else {
                    forestDist[di][dj] = Math.min(Math.min(forestDist[di - 1][dj] + costDel,
                                    forestDist[di][dj - 1] + costIns),
                            forestDist[src.lld(di) - 1][dst.lld(dj) - 1]
                                    + treeDist[di][dj]);
                }
            }
        }
    }

    @Override
    public void match() {
        computeTreeDist();

        boolean rootNodePair = true;

        LinkedList<int[]> treePairs = new LinkedList<int[]>();

        // push the pair of trees (ted1,ted2) to stack
        treePairs.push(new int[] { src.nodeCount, dst.nodeCount });

        while (!treePairs.isEmpty()) {
            int[] treePair = treePairs.pop();

            int lastRow = treePair[0];
            int lastCol = treePair[1];

            // compute forest distance matrix
            if (!rootNodePair)
                forestDist(lastRow, lastCol);

            rootNodePair = false;

            // compute mapping for current forest distance matrix
            int firstRow = src.lld(lastRow) - 1;
            int firstCol = dst.lld(lastCol) - 1;

            int row = lastRow;
            int col = lastCol;

            while ((row > firstRow) || (col > firstCol)) {
                if ((row > firstRow)
                        && (forestDist[row - 1][col] + 1D == forestDist[row][col])) {
                    // node with postorderID row is deleted from ted1
                    row--;
                } else if ((col > firstCol)
                        && (forestDist[row][col - 1] + 1D == forestDist[row][col])) {
                    // node with postorderID col is inserted into ted2
                    col--;
                } else {
                    // node with postorderID row in ted1 is renamed to node col
                    // in ted2
                    if ((src.lld(row) - 1 == src.lld(lastRow) - 1) && (dst.lld(col) - 1 == dst.lld(lastCol) - 1)) {
                        // if both subforests are trees, map nodes
                        ITree tSrc = src.tree(row);
                        ITree tDst = dst.tree(col);
                        if (tSrc.getType() == tDst.getType())
                        {
//                        	if(isCloseEnough(tSrc,tDst)) {
                        		addMapping(tSrc, tDst);
//                        	}
                        }
                        else 
                            throw new RuntimeException("Should not map incompatible nodes.");
                        
                        row--;
                        col--;
                    } else {
                        // pop subtree pair
                        treePairs.push(new int[] { row, col });
                        // continue with forest to the left of the popped
                        // subtree pair

                        row = src.lld(row) - 1;
                        col = dst.lld(col) - 1;
                    }
                }
            }
        }
    }
    
    private double myLevenshtein(ITree tSrc, ITree tDst) {
    	String idSrc = getIdentifier(tSrc);
		String idDst = getIdentifier(tDst);
		
		float dist = 1.0f;
		
		if(idSrc!=null && idDst!=null) {
			dist = new Levenshtein().compare(idSrc, idDst);
		}
		
		return dist;
    }

    private boolean isCloseEnough(ITree tSrc, ITree tDst) {
		String idSrc = getIdentifier(tSrc);
		String idDst = getIdentifier(tDst);
		
		float dist = 1.0f;
		
		if(idSrc!=null && idDst!=null) {
			dist = new Levenshtein().compare(idSrc, idDst);
		} else {
			return true; 
		}
		
		if(dist< THRESHOLD_FOR_MATCHING) {
			return true;
		}
		
		return false;
	}

	private String getIdentifier(ITree t) {
		String identifier=null;
		ITree id = getChildWithName(t, "id");
		if(id!=null) {
			identifier=id.getChild(0).getLabel();
		} else {
			ITree groupId = getChildWithName(t, "groupId");
			ITree artifactId = getChildWithName(t, "artifactId");
			
			if(groupId!=null && artifactId !=null) {
				identifier=groupId.getChild(0).getLabel()+"/"+artifactId.getChild(0).getLabel();
			}
		}
		
		return identifier;
	}

	private static ITree getChildWithName(ITree node, String name) {
		List<ITree> children = node.getChildren();
		
		for (ITree child : children) {
			if(name.equals(child.getLabel())) {
				return child;
			}
		}
		return null;
	}
	
	private double getDeletionCost(ITree n) {
        return 1D;
    }

    private double getInsertionCost(ITree n) {
        return 1D;
    }

    private double getUpdateCost(ITree n1, ITree n2) {
        if (n1.getType() == n2.getType())
            if ("".equals(n1.getLabel()) || "".equals(n2.getLabel()))
                return 1D;
            else
                return 1D - StringMetrics.qGramsDistance().compare(n1.getLabel(), n2.getLabel());
//                return 1D - myLevenshtein(n1,n2);
        else
            return Double.MAX_VALUE;
    }

    private final class ZsTree {

        private int start; // internal array position of leafmost leaf descendant of the root node

        private int nodeCount; // number of nodes

        private int leafCount;

        private int[] llds; // llds[i] stores the postorder-ID of the
        // left-most leaf descendant of the i-th node in postorder
        private ITree[] labels; // labels[i] is the tree of the i-th node in postorder

        private int[] kr;

        private ZsTree(ITree t) {
            this.start = 0;
            this.nodeCount = t.getSize();
            this.leafCount = 0;
            this.llds = new int[start + nodeCount];
            this.labels = new ITree[start + nodeCount];

            int idx = 1;
            Map<ITree,Integer> tmpData = new HashMap<>();
            for (ITree n: t.postOrder()) {
                tmpData.put(n, idx);
                this.setITree(idx, n);
                this.setLld(idx,  tmpData.get(PomZsMatcher.getFirstLeaf(n)));
                if (n.isLeaf())
                    leafCount++;
                idx++;
            }

            setKeyRoots();
        }

        public void setITree(int i, ITree tree) {
            labels[i + start - 1] = tree;
            if (nodeCount < i)
                nodeCount = i;
        }

        public void setLld(int i, int lld) {
            llds[i + start - 1] = lld + start - 1;
            if (nodeCount < i)
                nodeCount = i;
        }

        public boolean isLeaf(int i) {
            return this.lld(i) == i;
        }

        public int lld(int i) {
            return llds[i + start - 1] - start + 1;
        }

        public ITree tree(int i) {
            return labels[i + start - 1];
        }

        public void setKeyRoots() {
            kr = new int[leafCount + 1];
            boolean[] visited = new boolean[nodeCount + 1];
            Arrays.fill(visited, false);
            int k = kr.length - 1;
            for (int i = nodeCount; i >= 1; i--) {
                if (!visited[lld(i)]) {
                    kr[k] = i;
                    visited[lld(i)] = true;
                    k--;
                }
            }
        }
    }
}
