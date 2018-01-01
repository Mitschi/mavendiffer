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
        
        File oldTmpFile = new File(PATH_PREFIX+"hbase/0_0_old[1f488ae7d7f5797e46d81f5f1a93771106bea83f]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"hbase/0_1_new[231f0a92518bb2467c0c9538c635fdc69fef3148]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"hbase/1_0_old[352f46d0dbe7b4dea04ad32accf202b783553fc1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/1_1_new[f336545873d312d7669a1ed3820db999eb427502]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/2_0_old[bb359683be9268bfc8857beba3a211aa1fdb1e7b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/2_1_new[8e9c4a1cd68de444996b9125159f6ae0387b0b2f]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/3_0_old[6f51cbaf71128f0cf72069265c936052f9d08bc2]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/3_1_new[caf1363610c413d7733a425a03fabd6ddee65c3c]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/4_0_old[33eeab1865806782dbea1e30259402231190fea8]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/4_1_new[5a076eff55a6fc4b3b449503ceff43b687162b3b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/5_0_old[391105286366c9ca65d35d402932b5bade9d294a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/5_1_new[8c27fb84bc7fea2c236b2ef52ef53ad8d0784a3b]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/6_0_old[4f79188997b6d5a5351b4dcb8e4b31e91dde0944]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/6_1_new[b8a45a87513f0b8691a192d189841db3ff955ccc]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/7_0_old[af188820b92ddbd32ccb12f5af530e0d55022864]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/7_1_new[e8ca1b0f35c876ebc63a2e51b0de8c905535dce0]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/8_0_old[8b7bf5dac42d407d91241a9aa576639ea86555ef]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/8_1_new[14427e4545ebeeea60a9cc4f4255637d3e1fffa2]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"hbase/9_0_old[4a1464c4360efd50977068dc4b032e636d179796]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"hbase/9_1_new[99dfa019bf70e5a46969284154add2c6a3f6cc37]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		//TODO ADD EXPECTED CHANGES HERE
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);


		return retVal;
	}
}
