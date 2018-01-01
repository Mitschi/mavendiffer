package at.aau.diff.maven.standard.evaluation2;

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
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/evaluationRun2/";
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
			System.out.println(((MavenBuildChange)change).getName());
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
        
        File oldTmpFile = new File(PATH_PREFIX+"hadoop/0_0_old[8776065650f2a49391790b3f9de3cf05ae11c698]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hadoop/0_1_new[57232ebce2ed0d9e68eef22847fddc83969b5d2b]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hadoop/1_0_old[00daca18c3193ea4d5e101c295ec917120382dea]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/1_1_new[1559de97de762acfba92c5313fca7052798dd29b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/2_0_old[c4a3ffb15dde4c8ee0e7f7273810888183ed072f]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/2_1_new[0179e09093b5e657142698f15612641cc1945fcf]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/3_0_old[9b871bbdba1a932627185c197b4945fd4323506f]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/3_1_new[1f6df612ef647b2fbe398d8edaffbfd0142001d5]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("REPOSITORY_DELETE"));
		expectedChanges.add(new MavenBuildChange("REPOSITORY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/4_0_old[856283dce038c5f9c212dd4ecda2a8edd04ea566]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/4_1_new[4761d0360979d8cee4a3f58d4255528fcfa6e512]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/5_0_old[4b975b759ae96f2297f93cf6ef6ef2e797cdad0b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/5_1_new[2a90c4cd86e1d5000c591a4fc6be90b0d5a67b1f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/6_0_old[9ac68dd0d031106ae5074404e6d01176ec49292b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/6_1_new[5d3d3cc51798e3dc811e5767786dba5e23da9517]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//NO CHANGE (CHANGE IN COMMENT!)
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/7_0_old[c28d3c9c0f2005f3b5b9c49a1e2affac3d0c4ee2]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/7_1_new[ede83f92a6f79c8210755cb31d94197ee085f7ec]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/8_0_old[3f4e96d8f8c1cabc93623c216bfc5c10fc1ea8b6]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/8_1_new[bc16bd7f2fca9eaa847b346f6c93935faa08fbcf]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hadoop/9_0_old[73ad4734a0d3f0dacd3e4ebd0fa47bf110e3fa2e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hadoop/9_1_new[82f36fa01a42d1509e5ea69a4c1234d0dcbe401c]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PROFILE_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

//		return tmp;
		return retVal;
	}
}
