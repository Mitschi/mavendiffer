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
public class HibernatesearchEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun1/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/evaluationRun1/";
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hibernate-search/0_0_old[412fe55f5a4a771450b64b386c236f52872bbe65]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hibernate-search/0_1_new[2688548051a96ac7eb3182fdf28827f1dc7842fb]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//NO SEMANTICAL BUILD CHANGE HERE
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/1_0_old[2688548051a96ac7eb3182fdf28827f1dc7842fb]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/1_1_new[9d94c5c78527690f7d55b0ff0ceb31d599de4fe6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-javabean",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-plural",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-propfiles",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-configurable",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-jpa",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-finder",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-logging",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-dod",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-test",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-backup",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-web-mvc-controller",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-web-mvc-embedded",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-web-mvc-jsp",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-security",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-web-selenium",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-jdbc",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-dbre",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-creator",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-json",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-layers-service",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-layers-repository-jpa",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-oscommands",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-tailor",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-cloud",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-jms",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-email",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","addon-push-in",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","annotations",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","web-ui",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernate-search/2_0_old[93158db215884c41c65c68d76b27e782b03c653d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernate-search/2_1_new[e8e7d2914f96d26b9b3f3f9723f68701e31a400a]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.addon.beaninfo/UKN",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
