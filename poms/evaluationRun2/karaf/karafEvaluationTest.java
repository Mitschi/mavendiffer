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
        
        File oldTmpFile = new File(PATH_PREFIX+"karaf/0_0_old[ede83f92a6f79c8210755cb31d94197ee085f7ec]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"karaf/0_1_new[fad16cd33379b3a4ac2d087feec6825993eb56c8]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"karaf/1_0_old[694a8f4024e0524b88788a24141b12ffee59ac59]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/1_1_new[c2169319852bbb77fafa73a159b1aa3e7b6cc26f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/2_0_old[6f0c96d3a9c0df403d426832abc1af242e660e50]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/2_1_new[2fe8ec9d81844fc0608f6568bd94d6b7fe068674]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/3_0_old[06d62123b144a7a5446a6c8a6a9b89fb8abd89f1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/3_1_new[c2626e227d87b58a78103ffd051d517a30cc6a84]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/4_0_old[71a78c3aca9b76b09afab1e4acd24f0cc7250b3b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/4_1_new[0f323a13b2da091da09b0a7a6939495272bbdb10]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/5_0_old[c5988721cdfa8de981f610897386d392ab0a5a97]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/5_1_new[aa96fc153862059f62d116e5bee374b9adbcabc4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/6_0_old[17aa1929b655835fa46d8976630ecc9dcfb184d2]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/6_1_new[0d4b657df1752413d65d5951240160f40841c5c6]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/7_0_old[cf9047dc6eb3f72fbb68a4a67a3bb229405f385c]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/7_1_new[2a7d24279027a91d46eabf44a826c3bb4097ffb7]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/8_0_old[5d5c7d77f36c0754335b7e457404d11618d79afd]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/8_1_new[0279717bad2167001b4f5a561d438b40c5acf54b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"karaf/9_0_old[d23f2b6d61ec506ad07460388b9f521cdfdec462]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"karaf/9_1_new[215e71d0d5e1de10952ecf52bad384bac06ce46f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
