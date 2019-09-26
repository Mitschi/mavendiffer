package at.aau.diff.maven.standard.verifyThese;

import static org.junit.Assert.*;

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
public class GumTreeUpdateTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/verifyThese/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/verifyThese/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public GumTreeUpdateTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
		
        File oldTmpFile = new File(PATH_PREFIX+"easy1.xml");
        File newTmpFile = new File(PATH_PREFIX+"easy2.xml");
        List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","main.basedir//data/something","main.basedir//data/something/subfolder"));
		Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_camel_old_2.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_camel_new_1.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","ahc-version/1.9.38","ahc-version/2.0.7"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"gumtree/gumtreetest1.xml");
		newTmpFile = new File(PATH_PREFIX+"gumtree/gumtreetest2.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","at.gi/artiCool3/1.0.0"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"pom_camel_old_4.xml");
		newTmpFile = new File(PATH_PREFIX+"pom_camel_new_3.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","ahc-version/1.9.38","ahc-version/2.0.7"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		tmp.add(o);
		
//		return tmp;
		return retVal;
	}
}
