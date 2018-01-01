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
public class CamelEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public CamelEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"camel/1_0_old[d5c19e08288d2a6aa72b41f26baf38e27ff5f2a5]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/1_1_new[757dd1fd7afeb3716b36ae35cd5c3ecf08a61f3b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.camel/camel-jdbc/UKN"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/2_0_old[882857fa9cc058c880db3e55061e2409cf8370b3]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/2_1_new[f261f610ddf4f7b2abc472d455d01caa8413cb00]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","camel-jira"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/3_0_old[b4c97d79410587176577e918381922784f1e0b25]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/3_1_new[e5c0cd00c055e5bbf3f436cbbd18e3c1c2fdafd7]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//NO CHANGE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/4_0_old[16f48908066d7e6478127b7d825511c353c677d9]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/4_1_new[45f12263e400ac52f0e1f643ade64fddf9e55eb0]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","spring-version/${spring32-version}","spring-version/${spring4-version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/5_0_old[f10bfdd64d93613730e4230078e2813dbd7fe140]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/5_1_new[2e59f1d6495307013b5d7d4fd0fa889c7cfa99c7]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","log4j/log4j/UKN"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE","1.1","1.7"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/6_0_old[f7e10072912dea76faf4d16a51ec4274b43d4535]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/6_1_new[1f7264f859e6960d2e67b3a03f8441564ea75d93]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","ical4j-version/1.0.6","ical4j-version/1.0.7"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/7_0_old[68e09794437d52601a2b219c078bbabc0b6b8f2f]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/7_1_new[c1a428f89d706e01fe0cf9e56042643ac25daf92]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","activemq-version/5.5.0","activemq-version/5.5.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/8_0_old[dc39c8723845f668bed3ada181904dc4bee0c704]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/8_1_new[28b2da46e6fc37cadc89f76512c48b89b4136805]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","google-guava-version/${jclouds-google-guava-version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/9_0_old[944967bca09fbd3ebf91eac5fcf64f885fc5f303]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/9_1_new[772124aab674f1081a5839fe22ec57758bd88eac]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("REPOSITORY_INSERT","","fusesource.m2/::/::/Fusesource Dependency Repository/::/::/http://repo.fusesource.com/maven2-all"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","jdom/jdom/1.0","org.jdom/jdom/1.0"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE","2.4.2","${jexcelapi-version}"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.slf4j/slf4j-simple/UKN",""));//error here is recognized as update
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.slf4j/slf4j-log4j12/UKN"));//error here is recognized as update
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","commons-logging/commons-logging/UKN",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//return tmp;
		return retVal;
	}
}
