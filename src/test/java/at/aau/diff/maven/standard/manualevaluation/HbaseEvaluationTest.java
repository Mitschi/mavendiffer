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
public class HbaseEvaluationTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public HbaseEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hbase/0_0_old[157f4eb83dc8b76901bab385137e2aeb1a748323]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hbase/0_1_new[b11479cf9ea351a5ab9e76c983ba567e1a55e203]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_UPDATE","\"organizationUrl\"::\"http://www.ebay.com\"","\"organizationUrl\"::\"http://www.hortonworks.com\""));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hbase/1_0_old[5fce119685c9b2db1afb7189dc7bbf631b2933ac]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/1_1_new[1fab626bc722d2a39c0563279a89b6b58afe85c0]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","1.0-beta-3","1.0"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/2_0_old[28c2b18d30de4ce9564e328e5fdf42188e83fb63]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/2_1_new[164aeb53992150f0336f17a32ecb2fb733495964]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/3_0_old[a06597e34aca16f553406cc7f3663f1fc456816c]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/3_1_new[430060332d5c12321f5e1b5d97d590269768a563]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","junit.version/4.10-HBASE-1","junit.version/4.11"));
		expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","junit/junit/${junit.version}","junit/junit/${junit.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/4_0_old[fcf583eb19d5bb5c0cb3d2ad8a8abc1b87930c7b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/4_1_new[6ddb2f196544e98918730be513e7a10d431d496f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.hbase/hbase-rest/${project.version}"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.apache.hbase/hbase-thrift/${project.version}"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/5_0_old[3f385c2e2462d9fe805cf18ba1f98fd2a562bf97]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/5_1_new[098022ef70a5c18180214bbf89d40413a04809d4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_REPORT_SET_UPDATE","",""));
		expectedChanges.add(new MavenBuildChange("DIST_SITE_DELETE","hbase.apache.org/::/::/file:///tmp",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/6_0_old[6f542792e3093e426837fb1910f21b1a962f78f8]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/6_1_new[efe471f74b0aab2a807fdd606af256d8eebe3f25]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","commons-logging/commons-logging/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/7_0_old[4cadb9a7a87a20d80c1843e2e142a1b67ccb861b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/7_1_new[05980e26b887745219165d98567b3914be64dc9e]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","org.apache.rat/apache-rat-plugin/UKN"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.apache.maven.plugins/maven-pmd-plugin/2.4",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.apache.maven.plugins/maven-checkstyle-plugin/2.5",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.codehaus.mojo/findbugs-maven-plugin/2.3.1",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.codehaus.mojo/cobertura-maven-plugin/2.3",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.codehaus.mojo/jdepend-maven-plugin/2.0-beta-2",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","UKN/maven-changes-plugin/2.3",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","com.atlassian.maven.plugins/maven-clover2-plugin/2.6.3",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.codehaus.mojo/taglist-maven-plugin/2.4",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","org.apache.rat/apache-rat-plugin/UKN"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/8_0_old[86da57f498c68703979dcd7ecc4b0ef2ce03723d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/8_1_new[68b94886a5cc0d8db26c49616669c244f9cae916]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/9_0_old[720f2a88154653a13892b36997129d3f77fc9702]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/9_1_new[5dd8fefd75269e1f07c1451619a785cbbee11f8c]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_INSERT","","syuanjiang/::/::/Stephen Yuan Jiang/::/::/syuanjiang@apache.org"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
