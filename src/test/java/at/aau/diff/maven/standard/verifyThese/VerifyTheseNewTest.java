package at.aau.diff.maven.standard.verifyThese;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.aau.diff.common.Change;
import at.aau.diff.common.Differ;
import at.aau.diff.maven.MavenBuildChange;
import at.aau.diff.maven.MavenBuildFileDiffer;

@RunWith(Parameterized.class)
public class VerifyTheseNewTest {
	//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/verifyTheseNew/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public VerifyTheseNewTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
		this.oldTmpFile=oldTmpFile;
		this.newTmpFile=newTmpFile;
		this.expectedChanges=expectedChanges;
	}
	
	@Test
	public void test() throws Exception {
		Differ buildDiffer = new MavenBuildFileDiffer();
		
		List<Change> actualChanges = buildDiffer.extractChanges(oldTmpFile, newTmpFile);
//		System.out.println("ACTUAL: "+actualChanges.size());
		assertContainsSameChanges(expectedChanges,actualChanges);
	}

	private void assertContainsSameChanges(List<Change> expectedChanges, List<Change> actualChanges) {
//		System.out.println("expected: "+expectedChanges.size()+" actual:"+actualChanges.size());
		for (Change change : actualChanges) {
			System.out.println(((MavenBuildChange)change).getName()+" || "+((MavenBuildChange)change).getOldValue()+" || " +((MavenBuildChange)change).getNewValue());
		}
		assertEquals(expectedChanges.size(),actualChanges.size());
		for (Change expectedChange : expectedChanges) {
			assertContainsChange(actualChanges,expectedChange);
		}
	}

	private void assertContainsChange(List<Change> actualChanges, Change expectedChange) {
		assertTrue(actualChanges.contains(expectedChange));
	}

	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> retVal = new ArrayList<Object[]>();
		ArrayList<Object[]> tmp = new ArrayList<Object[]>();
//		Project: hibernate-search
//		File: hibernate-search/pom.xml
//		Old Commit: 1c341e933ed7c868fc3ad7cb9bb3e2c8a3a71901
//		New Commit: 1faf48c947e1694c6eef562d5c9750a33b3ec81f

        File oldTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_1c341e933ed7c868fc3ad7cb9bb3e2c8a3a71901.xml");
		File newTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_1faf48c947e1694c6eef562d5c9750a33b3ec81f.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
//		expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPO_INSERT"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
//		Project: karaf
//		File: pom.xml
//		Old Commit: a93165a243d9fbb9806d4b14d183aa88dc6a7a9b
//		New Commit: 7aefe9f045573c3549b88f9a10366a03123252ea
//		oldTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_0fabd40fc7dcf06ad485319e564b15f988f0d325.xml");
//		newTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_4ab65408cd757a0fadd7f98427d3a661931b3761.xml");
//		expectedChanges=new ArrayList<Change>();
//		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
//		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
//		retVal.add(o);
		
//		Project: jenkins
//		File: war/pom.xml
//		Old Commit: 19f34b54d6cd69f837552a21d8adb41f3b5fba91
//		New Commit: 64d27e9c4c73cd5cdd3e81c226bb8da23a42db6f
		
//		Project: wicket
//		File: archetypes/quickstart/src/main/resources/archetype-resources/pom.xml
//		Old Commit: 7a2259e0feaa8eb90ae5168d19d1ceff3b05ad71
//		New Commit: 89d60802385d2d65ed152614b6cae5d92058a84c
		
//		Project: wicket
//		File: pom.xml
//		Old Commit: 8409f06e2c5ab801e32bb9970ffe9434b0dff1d0
//		New Commit: 3363fb29bacc4b474cfe98808c698596578d7979
		
//		Project: wildfly
//		File: pom.xml
//		Old Commit: e53c0fabd1334dd08265ad9f69946ffba442296b
//		New Commit: 38b6fd1422f54ef4b85a80c6e252250eba0a78a4
		
//		Project: wildfly
//		File: pom.xml
//		Old Commit: bb74a37877d552529ab15b67a042349ae88ade2e
//		New Commit: 194e685027f6bd44afa9e6757a04e5776ea78b79
		
//		Project: hbase
//		File: pom.xml
//		Old Commit: 1b1725188ef4218dad4adbb22c6d6bd61ad44588
//		New Commit: 4de54f355289e4c15f057df5b27b956515a214b8
		
		return tmp;
//		return retVal;
	}
}
