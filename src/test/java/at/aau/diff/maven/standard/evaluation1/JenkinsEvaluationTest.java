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
public class JenkinsEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun1/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/evaluationRun1/";
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
			System.out.println(((MavenBuildChange)change).getName()+" || "+((MavenBuildChange)change).getOldValue()+" || "+((MavenBuildChange)change).getNewValue());
		}
		assertEquals(expectedChanges.size(),actualChanges.size());
		for (Change expectedChange : expectedChanges) {
			assertContainsChange(actualChanges,expectedChange);
		}
	}

	private void assertContainsChange(List<Change> actualChanges, Change expectedChange) {
		assertTrue("Error with: "+expectedChange,actualChanges.contains(expectedChange));
	}

	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> retVal = new ArrayList<Object[]>();
		ArrayList<Object[]> tmp = new ArrayList<Object[]>();
        
        File oldTmpFile = new File(PATH_PREFIX+"jenkins/0_0_old[61d90732172ac082b2f56e279f3f268e75e1af9d]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"jenkins/0_1_new[cf319f9a412394d797c57c241ec21c3910e947b6]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE","bundle","pom"));//
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE","Spring Roo - Addon - Finder","Spring Roo - Finder"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","annotations"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","addon"));//
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.osgi/osgi.core/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.osgi/osgi.cmpn/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.apache.felix/org.apache.felix.scr.annotations/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.addon.configurable.addon/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.addon.jpa.addon/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.addon.plural.addon/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.classpath/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.file.monitor/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.file.undo/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.metadata/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.model/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.process.manager/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.project/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.shell/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo/org.springframework.roo.support/UKN",""));//
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","com.googlecode.maven-java-formatter-plugin/maven-java-formatter-plugin/UKN",""));//
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","target.jar.directory/${basedir}/../target/all"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE","main.basedir/${basedir}/..",""));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"jenkins/1_0_old[e3c810a1b497934aa0da6c078e29dba72440fbbf]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/1_1_new[d3f5f5d65f1d4d052cd1b54b4f2d6822c7b5746e]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_MINOR_VERSION_INCREASE","1.2.6.BUILD-SNAPSHOT","1.3.0.RELEASE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/2_0_old[45e5b878211572e3442eaa39d8eeb7662dac0cd8]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/2_1_new[87b50cc5f82413a70421ecaba6ee7348701ebd29]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT","org.apache.maven.plugins/maven-gpg-plugin/1.1","org.apache.maven.plugins/maven-gpg-plugin/1.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
