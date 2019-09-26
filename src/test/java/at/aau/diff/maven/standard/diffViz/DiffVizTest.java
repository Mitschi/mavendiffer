package at.aau.diff.maven.standard.diffViz;

import at.aau.diff.common.Change;
import at.aau.diff.common.Differ;
import at.aau.diff.maven.MavenBuildChange;
import at.aau.diff.maven.MavenBuildFileDiffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DiffVizTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public DiffVizTest(File oldTmpFile, File newTmpFile, List<Change> expectedChanges) {
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
			System.out.println(((MavenBuildChange)change).getName()+" || "+ ((MavenBuildChange)change).getOldValue() + " || "+ ((MavenBuildChange)change).getNewValue());
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
        
        File oldTmpFile = new File(PATH_PREFIX+"camel/0_0_old[d5193d13f6fe3827120cb3df13035aded367089a]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"camel/0_1_new[a81db57cde62a4349af4db60d5e038fea5b7a316]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","scala-version/2.7.1"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE","2.7.0","${scala-version}"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.camel/camel-velocity/UKN"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
//		retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"springroo/6_0_old[9733e3da8ef1c265d091bf2fb1736c65ae78f1df]_pom.xml");
        newTmpFile = new File(PATH_PREFIX+"springroo/6_1_new[b37ccb68560c828fce7447f632eae5d3c289b84f]_pom.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo.wrapping/org.springframework.roo.wrapping.antlr-java-parser/UKN",""));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","com.github.antlrjavaparser/antlr-java-parser/UKN"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
//        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"wicket/8_0_old[3f8408cb394f5d2230ee15ad46ee08ae0faea209]_pom.xml");
        newTmpFile = new File(PATH_PREFIX+"wicket/8_1_new[583287eb8f2c1f2c13c0956bd81d7d43a73abe53]_pom.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","2.2-SNAPSHOT","2.2-beta-1"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.0.1"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","1.0-alpha-3"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.0.2"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.1"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.2"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.0-beta-5"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.0.3"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","6.1.1"));
        expectedChanges.add(new MavenBuildChange("PLUGIN_UPDATE","org.apache.maven.plugins/maven-remote-resources-plugin/1.0-alpha-5","org.apache.maven.plugins/maven-remote-resources-plugin/1.0-alpha-5"));//
//		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","1.0-alpha-5"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
//        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"../../poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"../../poms/standard/pom_changeVersion.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE","19.0","22.0"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

		return retVal;
	}
}
