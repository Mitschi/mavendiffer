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
public class HibernatesearchEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public HibernatesearchEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/0_0_old[e4e404e361d46acd28027bb5639b9dada965b02f]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hibernatesearch/0_1_new[99c008e1dece07c988e39872f5f3d21f6b230ae9]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/1_0_old[e645436f92b72ba4dfcf9479b764501845d2e1ef]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/1_1_new[52d32405b6606e0a49e2cb3f6cadc4aacab436c3]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UNKNOWN_VERSION_UPDATE","2.0.0","${elasticsearchJestVersion}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/2_0_old[bc8e2bdf5bec4a1f52959efe2934c79d9b570914]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/2_1_new[cbec7266ce2f537a8ef34dc3dca2cab1e2417dc1]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","hibernateVersion/5.0.1.Final","hibernateVersion/5.0.2.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/3_0_old[837f6a8d7c897201e79f21f9fff323ff5d4446da]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/3_1_new[201ee1e41c860abc47c3d959f0f121973446faac]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.apache.solr/solr-analysis-extras/${luceneVersion}","org.apache.solr/solr-analysis-extras/${luceneVersion}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		//retVal.add(o);
		//tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/4_0_old[edcec4b6feddbbfbd2d927d9039806dae381a495]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/4_1_new[7c0340953f10ec083334df0d836534f97fb98844]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("LICENSE_UPDATE","","\"comments\"::\"See also: http://hibernate.org/license\""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/5_0_old[88a3b0c8015b131b3fe9b73baebd79d265b4380c]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/5_1_new[95547dff6b946b42ea40dc6aa9d0661fa4ee0fc7]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","hibernate-search-infinispan"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/6_0_old[3dd0e7b630f8ca3947b57833bc9ea5e66d79ddf7]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/6_1_new[6fcd7c425ee271853d6e987791aeebbe69ea89a9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_POSTFIX_VERSION_UPDATE","5.3.0.Beta1","5.3.0-SNAPSHOT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/7_0_old[e86d6d945585337a87bb92419a257beefab1bff7]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/7_1_new[225344e435895169fb104a6798d18e08e877a931]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","wildflyVersion/10.0.0.Alpha6","wildflyVersion/10.0.0.Beta2"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/8_0_old[697d6dd87db071da7a151b6e853f06d3eb11926e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/8_1_new[b681224b01af3e31f9c03a28499997c901f92071]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","org.hibernate/hibernate-search-infinispan/${project.version}","org.hibernate/hibernate-search-infinispan/${project.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hibernatesearch/9_0_old[5d28fa23fd69897647ec9e195c5ea96fdf65d407]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hibernatesearch/9_1_new[41a73ada8e3af90281f1beb63911958783b7fb07]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","luceneVersion/4.8.0","luceneVersion/4.8.1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
