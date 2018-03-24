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
public class DependencyChangesTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public DependencyChangesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
		File newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addDependency.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT","","org.springframework/spring-core/4.2.5.RELEASE"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeDependency.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE","com.google.guava/guava/19.0",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeDependencyOrder.xml");
        expectedChanges=new ArrayList<Change>();
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeVersion.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_MAJOR_VERSION_INCREASE","19.0","22.0"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","com.google.guava/guava/19.0","com.google.guava/guava/19.0"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_removeScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","junit/junit/4.12","junit/junit/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","junit/junit/4.12","junit/junit/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeDependencyArtifactId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","commons-io/commons-io/2.4","commons-io/commons-io-new/2.4"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeDependencyGroupId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE","commons-io/commons-io/2.4","commons-io-new/commons-io/2.4"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        //additional changes for dependency management changes (in contrast to dependency changes)
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addMgdDep.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_INSERT","","org.apache.hadoop/hadoop-core/47.11"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addMgdDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_DELETE","org.apache.hadoop/hadoop-core/47.11",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeMgdArtifactId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","commons-math/commons-math/13.2","commons-calculations/commons-math/13.2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        tmp.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeMgdGroupId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","commons-math/commons-math/13.2","commons-math/commons-calculations/13.2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_changeMgdVersion.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_MINOR_VERSION_INCREASE","13.2","13.5"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addMgdScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","commons-math/commons-math/13.2","commons-math/commons-math/13.2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/standard/pom_addMgdScope.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/standard/pom_base_depmgmt.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MANAGED_DEPENDENCY_UPDATE","commons-math/commons-math/13.2","commons-math/commons-math/13.2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
//        return tmp;
        return retVal;
    }
}