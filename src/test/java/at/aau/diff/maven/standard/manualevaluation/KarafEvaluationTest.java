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
public class KarafEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public KarafEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"karaf/0_0_old[a55bd3da75dc09b95a55ee351e4e75cbd8524dd2]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"karaf/0_1_new[1c40ddcf9c6530f68d1a6f2d54288b21ec5be54e]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","felix.webconsole.version/3.1.6","felix.webconsole.version/3.1.8"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"karaf/1_0_old[c2106be5dc11e1dd56c64ed0fc71c84d2fbf62f6]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/1_1_new[b8114878e76cf322db408679c874ab15102e7d4e]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","felix.framework.version/5.0.0","felix.framework.version/5.0.1"));
		expectedChanges.add(new MavenBuildChange("REPOSITORY_INSERT","","Felix Staging/::/::/Felix  Staging/::/::/https://repository.apache.org/content/repositories/orgapachefelix-1072"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/2_0_old[8e7d51972a20154da2b5c2c84c30eb8b0368cfcd]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/2_1_new[1f41368aafe17592569b433313e278ae4b3e5def]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","asm.version/5.0.2","asm.version/5.0.3"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/3_0_old[60c0f820953a95c2e08f0475789af20d041ff849]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/3_1_new[7373515e77fc9ab19e32526dcb7c545af471c5d6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		//retVal.add(o);
		//tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/4_0_old[819d26440ab79629d04c91c20999d3cfaeeb548a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/4_1_new[0995164eaa88bfa87ef81431a8dcbbce9bd59271]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","jasypt.bundle.version/1.6_1","jasypt.bundle.version/1.7_1-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/5_0_old[1d1cc8a487ed11ebecd38eafae24f4da0e967293]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/5_1_new[902f0272f54e1b601db3d52cc038cd2f3c1ff233]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.apache.servicemix.specs/org.apache.servicemix.specs.jaxp-api-1.3/UKN","org.apache.servicemix.specs/org.apache.servicemix.specs.jaxp-api-1.4/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.apache.servicemix.specs/org.apache.servicemix.specs.stax-api-1.0/UKN",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		//retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/6_0_old[7c8b5a720716d781bbd92d486ef9fba34e6d81c1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/6_1_new[89bbd1d8c0363a09c4c5109712b480685457eaa5]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","sshd.version/0.9.0","sshd.version/0.10.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/7_0_old[0f6f738ffad2a0e72d5bd2a7460f36e9e1aec929]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/7_1_new[48318e0e50f400de6f3b526c5f1dba1c16c5d0b1]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","aries.jndi.url.version/1.1.0-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/8_0_old[14cad9e0325e827c8edfec31e120574d8bd7a595]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/8_1_new[320503f9a091389652bff862e71c2e923f3fef50]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","commons-logging/commons-logging/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.ops4j.pax.swissbox/pax-swissbox-tinybundles/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/9_0_old[c8967860e06f1de50e022e79f95c8c9b76353f15]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/9_1_new[b7927ec4cb61106b3f79286dd02bfa4307c7980f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","branding"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
