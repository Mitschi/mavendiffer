package at.aau.diff.maven.standard;

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
public class PersonalChangesTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public PersonalChangesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more.xml");
		File newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_addDeveloper.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_INSERT","","jb/::/::/James Bond/::/::/james@bond.com"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_addDeveloper.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_DELETE","jb/::/::/James Bond/::/::/james@bond.com",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_changeDeveloper.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEVELOPER_UPDATE","\"timezone\"::\"+5\"","\"timezone\"::\"+6\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more_cont.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_addContributor.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("CONTRIBUTOR_INSERT","","James Laessig"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_addContributor.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more_cont.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("CONTRIBUTOR_DELETE","James Laessig",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/personal/pom_base_more_cont.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/personal/pom_changeContributor.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("CONTRIBUTOR_UPDATE","\"name\"::\"Sanne Grinovero\"","\"name\"::\"Sanne Grinoveroas\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
//        return tmp;
        return retVal;
    }
}