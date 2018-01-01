package at.aau.diff.maven.standard.verifyThese;

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
public class VerifyTheseTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/verifyThese/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public VerifyTheseTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"pom_spring-roo_c8e7c4e3b1eb2e0b5ea008ca56a3ea4e64f76e2d.xml");
		File newTmpFile = new File(PATH_PREFIX+"pom_spring-roo_2d804007e6ffa08abebf1674f38b56bc8887a058.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPO_INSERT"));
		
		
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_0fabd40fc7dcf06ad485319e564b15f988f0d325.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_hibernate-search_4ab65408cd757a0fadd7f98427d3a661931b3761.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_jenkins_0_0_old[61d90732172ac082b2f56e279f3f268e75e1af9d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_jenkins_0_1_new[cf319f9a412394d797c57c241ec21c3910e947b6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));//
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE"));//
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"1.xml");
		newTmpFile = new File(PATH_PREFIX+"2.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));//
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		
		oldTmpFile = new File(PATH_PREFIX+"3.xml");
		newTmpFile = new File(PATH_PREFIX+"4.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));//
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));//
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		
		//TODO
		oldTmpFile = new File(PATH_PREFIX+"pom_camel_old_801ff094a9f28b3275f193536a0bc4df4a54d1a5.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_camel_new_e325c9144d90d9d098ede9e39e93b2a8531da30b.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);

		oldTmpFile = new File(PATH_PREFIX+"pom_camel_old_2.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_camel_new_1.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_camel_old_4.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_camel_new_3.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_testPluginWithDependencyDeletion_old.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_testPluginWithDependencyDeletion_new.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
		return tmp;
//		return retVal;
	}
}
