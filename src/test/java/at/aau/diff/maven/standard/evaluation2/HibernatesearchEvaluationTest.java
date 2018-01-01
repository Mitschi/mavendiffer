package at.aau.diff.maven.standard.evaluation2;

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
public class HibernatesearchEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun2/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public HibernatesearchEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hibernate-search/0_0_old[c1db284a85a233d025abb8a3bd33ef4422ebcc94]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hibernate-search/0_1_new[11cba67ae4d6da28de433f434015baba94c039ee]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/1_0_old[cafb469dabab8b02067b0e92ad2af6de353cc0e3]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/1_1_new[d912559e964f8d03e390e60ec69b2cd39aa73e29]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/2_0_old[ede83f92a6f79c8210755cb31d94197ee085f7ec]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/2_1_new[fad16cd33379b3a4ac2d087feec6825993eb56c8]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/3_0_old[6bc1f3b95120c17f404cea7b5369701621d9bfc6]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/3_1_new[1272e43c10a88b6502342d0c62035e80e9fd72ae]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//NO CHANGE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/4_0_old[5a45022d798af8c2caa8f38dd4f8f4dcf83f3b28]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/4_1_new[aca7e465fb9d4964763bf75caed1ef92c7b5b740]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/5_0_old[ad1d73b2b2461990730f9200e020e9fdfeea55ec]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/5_1_new[7be7b46f1486d564f6ac3f26cd4d3e2bdbe93484]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/6_0_old[aa96fc153862059f62d116e5bee374b9adbcabc4]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/6_1_new[32ef576062eb2c626f786f45e654ccf329341c53]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/7_0_old[7a8cdc7ee8b1180ea00fa7fae80d7ef79617ff7f]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/7_1_new[b14705cf6c05107aa36c8bda006fa40325c449a4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/8_0_old[caaf65a2353562a204ddc721b2fb93e2a33c4e8c]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/8_1_new[01627300e21540e31de5470579410b493e434342]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/9_0_old[89f4f035d4fdf928502ff20c429580415add281d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/9_1_new[539472ef594947e165edae30cf6954ed70a2554d]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
