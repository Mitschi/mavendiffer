package at.aau.diff.maven.standard.pomcompare;

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
public class NewSinglePomFileComparerTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public NewSinglePomFileComparerTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"spring-roo/0_0_old[47ba3e0071684de8c44c79b3cf39cb97994c1e6c]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"spring-roo/0_1_new[47aa119b298b1f6c9a596942ad81792ce772bb36]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"spring-roo/1_0_old[1f488ae7d7f5797e46d81f5f1a93771106bea83f]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"spring-roo/1_1_new[231f0a92518bb2467c0c9538c635fdc69fef3148]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"spring-roo/2_0_old[2825a67c10343567f34accd66113594a05489281]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"spring-roo/2_1_new[4229651b53dcaa7586e8ce62f29bce5e47701a50]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
