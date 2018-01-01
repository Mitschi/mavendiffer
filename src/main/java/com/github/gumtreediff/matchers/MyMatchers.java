package com.github.gumtreediff.matchers;

import com.github.gumtreediff.tree.ITree;

public class MyMatchers {
	@Register(id = "pommatcher", defaultMatcher = true)
    public static class PomMatcher extends CompositeMatcher {

		public void match() {
	        for (Matcher matcher : matchers) {
	            matcher.match();
//	            System.out.println("Matches: "+matcher.mappings.asSet().size());
	        }
	    }
		
        public PomMatcher(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
//            		new TestMatcher(src, dst, store)
//            		new PomGreedySubtreeMatcher(src, dst, store),
//                    new PomGreedyBottomUpMatcher(src, dst, store)
//            		new TagMatcher(src, dst, store)


                    new MyGreedySubtreeMatcher(src, dst, store),
                    new MyBottomUpMatcher(src,dst,store)
            });
        }
    }
}