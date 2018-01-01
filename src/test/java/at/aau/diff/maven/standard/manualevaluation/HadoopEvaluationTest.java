package at.aau.diff.maven.standard.manualevaluation;

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
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hadoop/0_0_old[17161d90419d8295fc15c83a1195689f20c05786]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hadoop/0_1_new[66aca6d25d50f2aa9580ad8061697e892bf4005a]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","2.7.2","2.9"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hadoop/1_0_old[72f8755cca3fa751539e3e8e2c3a2f4f1d99e429]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/1_1_new[e0cd544a5368d5ff8e1a6808ec3e481f0f96629f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//NO CHANGE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/2_0_old[3fe00a6c776be304902c2bd108ce002df1477209]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/2_1_new[9a0fc23c822d8b07255e25a3244f4489142bb3f6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","2.3.2","2.5.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/3_0_old[eddb326669df6c6bfb928d4a4a8aad42dca1d3f6]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/3_1_new[f3296a19846e4cc2ea64b2293349079792055783]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","junit/junit/UKN","junit/junit/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.mockito/mockito-all/UKN","org.mockito/mockito-all/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/4_0_old[d647f98cb8bd9b0750f6d79dcd983dafe03550ac]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/4_1_new[e49fb149972d2bc55b4d4a1b60a7488124c8b9d5]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/5_0_old[2106c8bec89b18f2733e9f269aff86b9b136f9f9]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/5_1_new[5f69497e355b7f6639d740965658e3ed1d9f95c4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_VERSION_UPDATE","3.2.3.Final","3.2.4.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/6_0_old[3943008b073e5eef16dcb8e1525320dcac26095e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/6_1_new[5e826c4b2870e071700eb9bf2bb073ba9fde8f6c]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/7_0_old[ddef6a062eca41dd78774d8ace0f9bf095045f84]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/7_1_new[36c8624924bd5a2aee6d14b27d85467a6b1b8857]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROPERTY_INSERT","","runas.home/")); 
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.apache.maven.plugins/maven-antrun-plugin/UKN",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.codehaus.mojo/make-maven-plugin/UKN",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","org.apache.maven.plugins/maven-antrun-plugin/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/8_0_old[fe9369fb3c8d1490bf03b10ac4d07e67f1a34e24]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/8_1_new[2f0489edab5436fc313f60edfeef76521abbee31]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","2.7","2.8.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/9_0_old[70ee0a35fb815e5651e5e81a6c14ffb134c6b1c3]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/9_1_new[9b38eb2c2e2de634ac64841080ff9a4c184ae959]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.zookeeper/zookeeper/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
