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
public class WicketEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public WicketEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
		assertTrue("Change: "+expectedChange,actualChanges.contains(expectedChange));
	}

	@Parameters
	public static Collection<Object[]> data() {
		ArrayList<Object[]> retVal = new ArrayList<Object[]>();
        ArrayList<Object[]> tmp = new ArrayList<Object[]>();
        
        File oldTmpFile = new File(PATH_PREFIX+"wicket/0_0_old[44078d1e547998d7faf284be73cd1dd879b7537a]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"wicket/0_1_new[4efb34cc733b36939ed594cab1578b96a19c0123]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("SCM_CONNECTION_UPDATE","scm:svn:https://svn.apache.org/repos/asf/incubator/wicket/branches/wicket-1.x","scm:svn:http://svn.apache.org/repos/asf/incubator/wicket/branches/wicket-1.x"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"wicket/1_0_old[9b98c7fffd5933e4c97116d71ccc8da0c861d7fb]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/1_1_new[02235bca36c777a8eef51f0f11e1bee211dc2afb]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_DELETE","wicket-repo/::/::/UKN/::/::/scpexe://shell.sourceforge.net/home/groups/w/wi/wicket/htdocs/maven2",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/2_0_old[a039e5f8c81f7c38801ee7b43fca0dce4be20564]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/2_1_new[0a06ea43bde30dd6ad82dc4887a3f0c6a736e1d9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_MAJOR_VERSION_INCREASE","2.0","3.0"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/3_0_old[919619bee56072a7a35a1b7a65e057a76123f09a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/3_1_new[4f75e452eb6a5f9e4a04f01bf74bf591f836ac85]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_UPDATE","org.mortbay.jetty/maven-jetty6-plugin/6.0-SNAPSHOT","org.mortbay.jetty/maven-jetty-plugin/6.0-SNAPSHOT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_POSTFIX_VERSION_UPDATE","6.0-SNAPSHOT","6.0.0rc1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/4_0_old[f1e0daf42432e0a73f59499bcda92734fcda2375]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/4_1_new[722e007b00a0c1c7ffd492800b01db0b0baf2501]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_UPDATE","wicket/wicket-parent/1.2-SNAPSHOT","wicket/wicket-parent/1.2-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/5_0_old[89a461a496ed3a26b2ac34316f80b79bde2b90fc]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/5_1_new[7712249df470f3d7fb427b31edd5235e722f536f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_INSERT","","org.slf4j/jcl-over-slf4j/1.5.8"));
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","org.springframework/spring/2.5.6","org.springframework/spring/2.5.6"));
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_DELETE","commons-logging/commons-logging/1.1",""));
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","commons-httpclient/commons-httpclient/3.0.1","commons-httpclient/commons-httpclient/3.0.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/6_0_old[0dbcc4fc097881cadbde617667ccda4189605554]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/6_1_new[a7fd8502fa7914a8aeb8727c950c0cb863ba6dd0]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.wicket/wicket-cdi-1.1-weld/0.1-SNAPSHOT"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.jboss.seam.conversation/seam-conversation-weld/3.0.0.Final","org.jboss.seam.conversation/wicket-cdi-1.1-weld/3.0.0.Final"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.jboss.weld.servlet/weld-servlet-core/1.1.9.Final","org.jboss.weld.servlet/weld-servlet/1.1.9.Final"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.jboss.weld/weld-core/1.1.9.Final",""));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","org.apache.wicket/wicket-cdi/UKN",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/7_0_old[b46a9741b79e06fd2c841c19f6f6f250e78716e1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/7_1_new[a276357f84b2374164f5d563db7b4644da5f29d9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_DELETE","0.6-SNAPSHOT",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"wicket/8_0_old[3f8408cb394f5d2230ee15ad46ee08ae0faea209]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/8_1_new[583287eb8f2c1f2c13c0956bd81d7d43a73abe53]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_POSTFIX_VERSION_UPDATE","2.2-SNAPSHOT","2.2-beta-1"));
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
		retVal.add(o);
//		tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wicket/9_0_old[06027af8e11e7a4c5bde50d7d174b523539c9d73]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wicket/9_1_new[dab4dac177249c6db6a169890cc72e9b9b6ca6b9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_MINOR_VERSION_INCREASE","0.10-SNAPSHOT","0.22-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
