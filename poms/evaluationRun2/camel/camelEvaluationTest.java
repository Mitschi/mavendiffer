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
        
        File oldTmpFile = new File(PATH_PREFIX+"camel/0_0_old[68814a66e0e86742f9fbd7d1349dfaa13aeefe26]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"camel/0_1_new[530e7192ab2a53d768c03151755bc2497c4eee1c]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"camel/1_0_old[9eb1ba2fa2229db7d491de58531699cb2c05b1df]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/1_1_new[cd8c0b526da5f3575dd4a666791a46d103a7f281]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/2_0_old[0b027173ccf3c07e84bb72ab432e19333febcb13]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/2_1_new[aa8aaa8fbf16d4bb35127348522a87a257d74c64]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/3_0_old[eba44c3152ea19285f4e96b0938f1d18d2c96416]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/3_1_new[224a6f10df16da86a977a75c2a934cbcc2cb40a5]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/4_0_old[f336545873d312d7669a1ed3820db999eb427502]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/4_1_new[352f46d0dbe7b4dea04ad32accf202b783553fc1]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/5_0_old[c631276e18af00d005656c209d5a46c24f32845d]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/5_1_new[cafb469dabab8b02067b0e92ad2af6de353cc0e3]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/6_0_old[68ed1da27b51506ee1c637334f08a8c37e30f184]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/6_1_new[8fb2ed37b57441d61eb89ededf751ddcfbf7d0f4]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/7_0_old[8df19eb59fd6f7ac9ccb2aba0285ed19126feb7e]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/7_1_new[8dd1f5ee6b675bc1ff4dd52b35869830e529c5fd]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/8_0_old[71a78c3aca9b76b09afab1e4acd24f0cc7250b3b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/8_1_new[0f323a13b2da091da09b0a7a6939495272bbdb10]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"camel/9_0_old[ad27aea39c7524381aa7370366871d67c96f221b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"camel/9_1_new[0c4865a56bff8aafa08f13d0e2de25c618bfefcb]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
