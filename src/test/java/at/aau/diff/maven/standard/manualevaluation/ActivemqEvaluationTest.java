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
public class ActivemqEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
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
        
        File oldTmpFile = new File(PATH_PREFIX+"activemq/0_0_old[47ea8ecf5d32bacbfd1e14120b5451ca808ebc56]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"activemq/0_1_new[35b8ea9308bc9b303a2ed652aca041b11e66a175]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_UPDATE","org.apache.activemq/activemq/4.0M4","org.apache.activemq/activemq/4.0M4"));
		expectedChanges.add(new MavenBuildChange("PARENT_GROUPID_UPDATE","activemq","org.apache.activemq"));
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE","4.0-SNAPSHOT","4.0M4"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","junit/junit/3.8.1"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","activemq/activemq-jaas/UKN","org.apache.activemq/activemq-jaas/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","springframework/spring/UKN", "org.springframework/spring/UKN"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"activemq/1_0_old[601f54f0df8ee8b5be9e25b8c7563afe9f41fb21]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/1_1_new[11ddb747d71c139af3d6dc7fc55d566ee33d1de9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.slf4j/slf4j-log4j12/UKN","org.slf4j/slf4j-log4j12/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/2_0_old[87b654a533fe09b91c6f6390c0a46089128fded3]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/2_1_new[8abbfb2376c98fde9ce6cec1ec47348bb6599611]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","activemq.osgi.import.pkg/org.apache.xbean*;version=\"[3.7,4)\",\n        org.apache.aries.blueprint.*;version=\"[0.3,2)\",\n          *",
				"activemq.osgi.import.pkg/org.apache.xbean*;version=\"[3.13,4)\",\n        org.apache.aries.blueprint.*;version=\"[1.0,2)\",\n          *"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.apache.aries.blueprint/org.apache.aries.blueprint/${aries-version}","org.apache.aries.blueprint/org.apache.aries.blueprint/${aries-version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/3_0_old[a918194d80631cc3ab63df842da664a8416f76b2]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/3_1_new[c49cb00200efb85bce68190542ed0c2d4147c542]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/4_0_old[610d5272ec7b948afa4bbfaa2b011b8a2aa74ee6]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/4_1_new[a18a55f75ce2410c534a97a94f6b71c2ac40ce6b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/5_0_old[a3578eb91beb7ae2ae2938b46af2a9ea25abe283]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/5_1_new[b65fd2710907863e6faa53de1a4b0acb963dbc49]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/6_0_old[d0d7231414768d919c570991b84841f65c495078]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/6_1_new[450b509d58e0cdab12c10edd692ba7a79ea7cca2]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.slf4j/slf4j-api/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/7_0_old[0b9f0e34fdf121bb6098463232c4a76b6e054652]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/7_1_new[21f67b88e31f5355a0eaf4cbef257859d698faf4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_INSERT","","apache-release"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/8_0_old[b792f117672bfacbaed3db6622a4df715bbdd99d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/8_1_new[8cb2141080bff9f9df7bcad263b9ea70aa8285cb]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","jetty/jetty/UKN","org.mortbay.jetty/jetty/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","jetty/servlet-api/UKN",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"activemq/9_0_old[f2733a26eca8ffca54f0c614868fcd261fb5cd48]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"activemq/9_1_new[d28be4dfb4e64ecd85158d3f129261fd6b8b0130]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("REPOSITORY_DELETE","servicemix/::/::/Apache ServiceMix Repository/::/::/http://svn.apache.org/repos/asf/servicemix/m2-repo",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
