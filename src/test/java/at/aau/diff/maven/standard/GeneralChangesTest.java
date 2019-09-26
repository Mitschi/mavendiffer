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
public class GeneralChangesTest {
//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
    private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public GeneralChangesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
		File newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeProjectVersion.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_MAJOR_VERSION_INCREASE","1.0-SNAPSHOT","2.0-SNAPSHOT"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeArtifactId.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_ARTIFACTID_UPDATE","at.aau/testartifact/1.0-SNAPSHOT","at.aau/testartifact-new/1.0-SNAPSHOT"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeGroupId.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_GROUPID_UPDATE","at.aau/testartifact/1.0-SNAPSHOT","at.aau.tools/testartifact/1.0-SNAPSHOT"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changePackaging.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE","pom","war"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addGeneralProperty.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT","","prop2/valueProp2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeGeneralProperty.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE","prop1/valueProp1","prop1/valueProp2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeGeneralProperty.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE","prop1/valueProp1",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        //=============
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addModule.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MODULE_INSERT","","module4"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeModule.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MODULE_DELETE","module2",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeModule.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MODULE_UPDATE","module2","module2-new"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        //=============
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_CONNECTION_INSERT","","scm:git:git://repo.git"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_CONNECTION_DELETE","scm:git:git://repo.git",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_CONNECTION_UPDATE","scm:git:git://repo.git","scm:git:git://verynewrepo.git"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        //=============
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addDevConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_DEVCONNECTION_INSERT","","scm:git:git://repo.git"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeDevConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_DEVCONNECTION_DELETE","scm:git:git://repo.git",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeDevConnection.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_DEVCONNECTION_UPDATE","scm:git:git://repo.git","scm:git:git://verynewrepo.git"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
      //=============
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addUrl.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_URL_INSERT","","http://url.at"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeUrl.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_URL_DELETE","http://url.at",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_more.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeUrl.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SCM_URL_UPDATE","http://url.at","http://verynewurl.at"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
      //============ NEW
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeParentVersion.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_MAJOR_VERSION_INCREASE","17","20"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeParentGroupId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_GROUPID_UPDATE","org.apache","at.aau"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
                
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeParentArtifactId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_ARTIFACTID_UPDATE","apache","apacheNew"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeParent.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_UPDATE","org.apache/apache/17","org.apache/apache/17"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeParent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_UPDATE","org.apache/apache/17","org.apache/apache/17"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeProjectName.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROJECT_NAME_UPDATE","Test","Test New Project"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeProjectName.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROJECT_NAME_INSERT","","Test"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeProjectName.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROJECT_NAME_DELETE","Test",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeProjectPackaging.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PROJECT_PACKAGING_UPDATE","pom","jar"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeLicense.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_UPDATE","\"name\"::\"GNU Lesser General Public License\"","\"name\"::\"GNU Much Lesser General Public License\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeLicense2.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_UPDATE","\"url\"::\"http://www.gnu.org/licenses/lgpl-2.1.html\"","\"url\"::\"http://www.gnu.org/licenses/lgpl-2.2.html\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_INSERT","","GNU Lesser General Public License/::/::/http://www.gnu.org/licenses/lgpl-2.1.html/::/::/See also: http://hibernate.org/license"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_DELETE","GNU Lesser General Public License/::/::/http://www.gnu.org/licenses/lgpl-2.1.html/::/::/See also: http://hibernate.org/license",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license_mod.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_UPDATE","","\"url\"::\"http://www.gnu.org/licenses/lgpl-2.1.html\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_parent_license_mod.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("LICENSE_UPDATE","\"url\"::\"http://www.gnu.org/licenses/lgpl-2.1.html\"",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

//        return tmp;
        return retVal;
    }
}