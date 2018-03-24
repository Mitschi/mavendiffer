package at.aau.diff.maven.standard.evaluation1;

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
public class WicketEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun1/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public WicketEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
			System.out.println(((MavenBuildChange)change).getName());
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
        
        File oldTmpFile = new File(PATH_PREFIX+"wicket/0_0_old[5c4dd98b7eb545b014bb0a24478d14b7104b3086]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"wicket/0_1_new[e3fadc4969870ed5552e1e3f8ff41cd476ce087f]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_PATCH_VERSION_INCREASE","2.3.2.0001","2.3.3.0001"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo.wrapping/org.springframework.roo.wrapping.velocity/1.6.4.0002",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","commons-lang/commons-lang/2.4",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","commons-collections/commons-collections/3.2.1",""));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"wicket/1_0_old[71a78c3aca9b76b09afab1e4acd24f0cc7250b3b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/1_1_new[0f323a13b2da091da09b0a7a6939495272bbdb10]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_PATCH_VERSION_INCREASE","1.0.12","1.0.14"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/2_0_old[4229651b53dcaa7586e8ce62f29bce5e47701a50]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/2_1_new[0cc8765ad8a5818fcf852f5a354ff7fbc49477f9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","org.apache.maven.plugins/maven-surefire-plugin/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
