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
public class JenkinsEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun2/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public JenkinsEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"jenkins/0_0_old[6f0c96d3a9c0df403d426832abc1af242e660e50]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"jenkins/0_1_new[2fe8ec9d81844fc0608f6568bd94d6b7fe068674]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"jenkins/1_0_old[42783ab2d7af283d0e8fa84947d3f16b7f932f27]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/1_1_new[90a335764323f2f87ef9f58f09350c4c8df1e5a6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/2_0_old[db19f19d6a0199ae2c1886df4cc41d16a676492a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/2_1_new[1d5d871a8d41cef9153b7aa537417cefa690d0cc]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/3_0_old[25b936f043fe410181677a292ea32aeb1d6844a4]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/3_1_new[0eede5decbcf54624282daa19303e774ea65efa7]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/4_0_old[69f27780ae53c49ca093d4a3bd0bdfa80749fb7e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/4_1_new[5b05828599ddc52482580ddfc2b57b8f8820e342]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/5_0_old[a47be482ee2a6bb487c948beadd636f5df0f4b26]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/5_1_new[38774a635f85d8cd7cf2a57d154ae95820145884]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/6_0_old[9c9a97df533366a623fa8a727825abf1463a0b66]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/6_1_new[09659f693b87b639967b1a14d39e46fbe600ee9f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/7_0_old[13dfa60471785fd795380787f08e335f21ab464e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/7_1_new[d6a84a7621fa1350a61db498f3653a3f1180d330]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/8_0_old[654d352300555489eca1ef625343fc571f06328a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/8_1_new[975f2f9f67e7c5bdc7fc2ae47a7d8a1ebdd43c62]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/9_0_old[93158db215884c41c65c68d76b27e782b03c653d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/9_1_new[e8e7d2914f96d26b9b3f3f9723f68701e31a400a]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
