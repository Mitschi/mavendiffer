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
public class HadoopEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun1/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/evaluationRun1/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public HadoopEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
			System.out.println(((MavenBuildChange)change).getName()+" || "+((MavenBuildChange)change).getOldValue()+" || "+((MavenBuildChange)change).getNewValue());
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hadoop/0_0_old[57caf5a945d258481faaec6905c8d5ab40b19c99]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hadoop/0_1_new[375971152dcb83d54ec68438b85e137cd5994326]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("REPOSITORY_INSERT","","jgit-repository/::/::/UKN/::/::/http://download.eclipse.org/jgit/maven"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hadoop/1_0_old[3292216ec8e59be2cbd678add0f54ce28794f9bc]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/1_1_new[a141d005a5b7ad2b734e039cb2ad1c3d89f4717e]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.springframework.roo/org.springframework.roo.shell/${roo.version}"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.springframework.roo/org.springframework.roo.support.osgi/${roo.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/2_0_old[848338c4900aa9bbb77acf0fb3bee256908d6e5e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/2_1_new[86cf02f8697065ea1819666bbe4d3e06fcaf66bb]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","2.4.1","2.5"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","2.4.3","2.5"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","2.8","2.12"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","2.3.2","2.4"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","spring.version/3.1.0.RELEASE","spring.version/3.1.1.RELEASE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
