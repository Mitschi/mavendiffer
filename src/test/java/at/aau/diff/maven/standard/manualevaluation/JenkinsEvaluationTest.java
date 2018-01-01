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
public class JenkinsEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
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
        
        File oldTmpFile = new File(PATH_PREFIX+"jenkins/0_0_old[5ae399ab970936ac0b98f3f28c4dfbd9bf81767e]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"jenkins/0_1_new[e5f16f86fc243b61de75c7714306a95a05280e65]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.apache.maven.wagon/wagon-http-lightweight/UKN","org.apache.maven.wagon/wagon-http-lightweight/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.jvnet.hudson/maven-embedder/3.0-SNAPSHOT","org.jvnet.hudson/maven-embedder/3.0-SNAPSHOT"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","net.sourceforge.nekohtml/nekohtml/1.9.13"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"jenkins/1_0_old[1a48b12f46fc80484a25557a3e609c1cdf39ed4b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/1_1_new[b06abc8e8cbb4aa9c53721cca08d3fccc1e329d6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.jvnet.hudson.main/hudson-test-harness/${project.version}","org.jvnet.hudson.main/hudson-test-harness/${project.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/2_0_old[407d54152d2808092a4ece5114c3c1e93c9ad232]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/2_1_new[bffa1d16fd96f18a0777ecde69f7ba21225b5490]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE","1.255-SNAPSHOT","1.255"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/3_0_old[b1024d749376c9d909ffcb91992a1745806b2cf8]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/3_1_new[c498c0f6a740c6f4b6e90e2af90d7904b3709b8a]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_VERSION_UPDATE","2.38","2.39"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/4_0_old[89c18838a1c7b78fe7b3985b1c5bf7b4a83bfdc1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/4_1_new[0ae1ff71d0e8bfa62f98216570bd5471a4e311e6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/5_0_old[25e35de7ccd4cf1eedeb0284b737882435437ece]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/5_1_new[435c7e09249c93d33113109cafad82d752add576]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.sonatype.aether/aether-api/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.sonatype.aether/aether-connector-wagon/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.sonatype.aether/aether-impl/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.sonatype.aether/aether-spi/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.sonatype.aether/aether-util/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/6_0_old[0b162cb9dc56fe5d50a53460d9e90a5047c47e5e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/6_1_new[2e3b332c49badbc93721d59c09f17344ef05b502]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/7_0_old[eb191593cf04592ee42e722e9200aebb482141b4]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/7_1_new[3c99828edfe02f9edd228c269ac8fc258ff78753]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE","1.582-SNAPSHOT","1.582"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/8_0_old[b757e28ba9b125ffff8c1fd31f56c4566cba02ec]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/8_1_new[16f287772910820fea2d50ceda620f7180f6ce79]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE","1.629","1.630-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"jenkins/9_0_old[dcbdce45283e6ca83848ef24958f7574401e1981]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"jenkins/9_1_new[72afd5e44c54a4248c5f5da327cf47941ac49932]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE","1.90","1.91-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
