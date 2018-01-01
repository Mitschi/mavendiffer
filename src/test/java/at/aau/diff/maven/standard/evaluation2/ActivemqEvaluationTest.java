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
public class ActivemqEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun2/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public ActivemqEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"activemq/0_0_old[6641fbe515584b868b7bcd606268a59e90702392]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"activemq/0_1_new[78fffcc75340601b576f5b92aff25f52c7e077c9]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//NO CHANGE EXPECTED
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"activemq/1_0_old[d5c2f18a621ef782d46f22340fae2c279c00a2d8]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/1_1_new[53b23f91e8298217d8db873d7a50ef5088c5bacf]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/2_0_old[0cad382a5ddbe40e1f6d975573edf8debc107737]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/2_1_new[0aeac23fef5a638b2cd8f0b1e73274f8df8e0685]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/3_0_old[44df49fe99656266ca1742be38b4aad7f6a3100e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/3_1_new[872e711b69568f6a5efdbb87c0a92e962e17c23a]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/4_0_old[6197f1a36ee45a5bb1a88607d246b84bb15046a9]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/4_1_new[8d380e44cbee98a406c2c3c46b371477d6ac4995]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/5_0_old[8b292a2d5d93417aede6904a83a2cc8c1a43e65c]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/5_1_new[e6919c560eb59bc179f962fa5906f6f103021e01]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/6_0_old[d7353394b7d621ea20d425c09644f03258a7fbab]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/6_1_new[344b38163b7ef3e479c965dcdd51bd617a082ab4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/7_0_old[0d11599c9534df972978bc9d106922b5ad3e8ccb]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/7_1_new[1ee869475a9d7552f7c7764d32b246c4d8aef805]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/8_0_old[9c9a97df533366a623fa8a727825abf1463a0b66]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/8_1_new[09659f693b87b639967b1a14d39e46fbe600ee9f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/9_0_old[47187250e6579d514ca185256261096df82bb493]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/9_1_new[5fc2d1e9c0027ef911290ee9898e924f27f9dd4f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		return retVal;
	}
}
