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
public class ProfileChangesTest {
	//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public ProfileChangesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"poms/profile/pom_base.xml");
		File newTmpFile = new File(PATH_PREFIX+"poms/profile/pom_addProfile.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_INSERT","","p2"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/profile/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/profile/pom_deleteProfile.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_DELETE","p1",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/profile/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/profile/pom_addProfilePart.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/profile/pom_addProfilePart.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/profile/pom_base.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/profile/pom_addProfilePart.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/profile/pom_changeProfilePart.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROFILE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
//        return tmp;
        return retVal;
    }
}