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
public class SpringrooEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public SpringrooEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"springroo/0_0_old[131a17e16523b3107d46aa475c298af10d3cc9a5]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"springroo/0_1_new[80f08d8e08975598a39995547c70b71d8223ba97]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","com.googlecode.maven-java-formatter-plugin/maven-java-formatter-plugin/0.4.0.e371sr1",""));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"springroo/1_0_old[611c65ec23c6ced8eaeac77773ed5578ee000296]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/1_1_new[9512ad7f40f89e3351eb18ef33cb57fa9bac0b3d]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_MINOR_VERSION_INCREASE","4.10","4.11"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/2_0_old[b4ed7661fd63e9cadc3d4a6d024df080745d4eb3]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/2_1_new[274b6a0c47a70b529fee5e799158278d9d5dca61]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.springframework.roo/org.springframework.roo.addon.web.mvc.controller/UKN","org.springframework.roo/org.springframework.roo.addon.web.mvc.controller.addon/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/3_0_old[845e92e6bcd4823bc96ad353794ded611c59a2e1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/3_1_new[6a8c3ed3429392d379b5387bb682a901b60ebcad]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","org.apache.maven.plugins/maven-gpg-plugin/1.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/4_0_old[81327fd1aa387ed275124e65b073332f02b61caa]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/4_1_new[87b1f9d723e5fb25419e8f99149ae536475a7758]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","project-settings"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/5_0_old[e5b60b7f8d06452fb871f2ba02d5a266e60156e7]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/5_1_new[328b953120d0b9f24c9e89c0efa79769b50776c9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//NO CHANGE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/6_0_old[9733e3da8ef1c265d091bf2fb1736c65ae78f1df]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/6_1_new[b37ccb68560c828fce7447f632eae5d3c289b84f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.springframework.roo.wrapping/org.springframework.roo.wrapping.antlr-java-parser/UKN",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","com.github.antlrjavaparser/antlr-java-parser/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/7_0_old[b0c2a3680f0a72a50617c3a8ce0d5b58cf48a00b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/7_1_new[0db3742cf11a1a78b701876def826dc7797a62fd]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.springframework.roo/org.springframework.roo.addon.propfiles/${roo.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/8_0_old[f942a4ef593123c6e7ada1e4ced23673faa92924]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/8_1_new[bfbb4e5f57dcf65786c77f03b24b91ba7d890d9e]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_POSTFIX_VERSION_UPDATE","3.0-beta-3","3.0"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_MAJOR_VERSION_INCREASE","0.93","1.0"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"springroo/9_0_old[a7e33c3f96e9a53cf8809428dcce568ea41df005]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"springroo/9_1_new[f6865072c206b40228d819516f4ef00233838a13]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","1.1","1.2"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE", "2.7.2","2.8"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_MINOR_VERSION_INCREASE","2.5","2.6"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
