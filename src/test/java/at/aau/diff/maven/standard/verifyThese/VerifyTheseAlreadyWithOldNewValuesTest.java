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
public class VerifyTheseAlreadyWithOldNewValuesTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/verifyThese/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public VerifyTheseAlreadyWithOldNewValuesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
			System.out.println(((MavenBuildChange)change));
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
        
        File oldTmpFile = new File(PATH_PREFIX+"pom_hadoop_old_70527c875456ca0fba49e88c576083c45a9ba2a6.xml");
		File newTmpFile = new File(PATH_PREFIX+"pom_hadoop_new_dd00bb71aaccb5996edeb5fdd95b692ecf843a2a.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","snappy.prefix/"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","snappy.lib/"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","snappy.include/"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","require.snappy/false"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","bundle.snappy.in.bin/true"));
		
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
//		oldTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_0fabd40fc7dcf06ad485319e564b15f988f0d325.xml");
//		newTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_4ab65408cd757a0fadd7f98427d3a661931b3761.xml");
//		expectedChanges=new ArrayList<Change>();
//		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
//		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
//		retVal.add(o);
		
		
		return tmp;
//		return retVal;
	}
}
