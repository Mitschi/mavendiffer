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
public class WildflyEvaluationTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/poms/manualevaluation/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/poms/manualevaluation/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public WildflyEvaluationTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"wildfly/0_0_old[d9c26379cf58677b3899e7e5eb85ccccc706437d]_pom.xml");
		File newTmpFile = new File(PATH_PREFIX+"wildfly/0_1_new[8a3ea86e600adf3586e5bca79a9aaa0b3f11d786]_pom.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","UKN/maven-surefire-plugin/UKN"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		oldTmpFile = new File(PATH_PREFIX+"wildfly/1_0_old[550763408f5131074242996be936afac17763ce1]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/1_1_new[579322785cd6ca75d885cd0dd95148888fea46af]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.jboss.remotingjmx.remoting-jmx/1.0.3.Final","version.org.jboss.remotingjmx.remoting-jmx/1.0.4.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/2_0_old[b204062c4be72a274b01e41f43995695b4567335]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/2_1_new[42749b70cef0958159c660b14d957b85a78fb633]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","eclipselink.version.2.4.milestone/2.4.0","eclipselink.version.2.4.milestone/2.4.2"));
		expectedChanges.add(new MavenBuildChange("REPOSITORY_DELETE","eclipselink/::/::/UKN/::/::/http://www.eclipse.org/downloads/download.php?r=1&nf=1&file=/rt/eclipselink/maven.repo",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/3_0_old[f33180f5619bb81f14c0b5c2930799c960598c27]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/3_1_new[b6a366f70ca7562675643afe5145cf29911a8e27]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.jboss.modules.jboss-modules/1.0.0.Beta3","version.org.jboss.modules.jboss-modules/1.0.0.Beta4"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/4_0_old[0e3d35e545e78dd85fd36e26406cbe7b1ab66598]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/4_1_new[56410f4ee157e20c049e6b2e0f98eb0d79000f31]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.jboss.narayana/5.2.5.Final","version.org.jboss.narayana/5.2.6.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/5_0_old[5daeb876089aafd6000d22ef118d15dec0ce117b]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/5_1_new[1672f6e4910cf338f03614e68b9d1aeb7beddfd9]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.jboss.logmanager.jboss-logmanager/1.5.0.Final","version.org.jboss.logmanager.jboss-logmanager/1.5.2.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/6_0_old[eba8afdb4258b8608240bd0679ce10ebfffe3f85]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/6_1_new[080e6f976b018fbf9fd38fd7a247b6a4a40bfb49]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.javassist/3.18.0-GA","version.org.javassist/3.18.1-Beta1"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/7_0_old[65f35fa002d7369237dcac2dd88bc7f2e3a196ce]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/7_1_new[f0d5c1cbbafc96567f054c99e74e18ddd1201131]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.org.jboss.ironjacamar/1.0.0.CR2","version.org.jboss.ironjacamar/1.0.0.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/8_0_old[5769049eb842c17a3e00f0b6e781a8cf9dbdf07a]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/8_1_new[888a4b694a6607489e2e48c6129ada013654d682]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","version.io.undertow.js/1.0.0.Final","version.io.undertow.js/1.0.1.Final"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"wildfly/9_0_old[38974ab933f8f9eb10b8642f22ded3143eb836ad]_pom.xml");
		newTmpFile = new File(PATH_PREFIX+"wildfly/9_1_new[4318f80787bc5b4e8592d2a95b5a32909b13dc12]_pom.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","secman",""));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE","secman",""));
		expectedChanges.add(new MavenBuildChange("PROFILE_DELETE","ts.integ.group.secman",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		return retVal;
	}
}
