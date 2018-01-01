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
public class BuildTagChangesTest {
	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private File oldTmpFile;
	private File newTmpFile;
	private List<Change> expectedChanges;
	
	public BuildTagChangesTest(File oldTmpFile, File newTmpFile,List<Change> expectedChanges) {
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
        
        File oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		File newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPlugin.xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT","","at.aau/donothing-plugin/1.0"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_deletePlugin.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE","org.apache.maven.plugins/maven-jar-plugin/2.6",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginConfiguration.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT","x.y.z/bla-plugin/4.1","x.y.z/bla-plugin/4.1"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginConfiguration.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_DELETE","x.y.z/bla-plugin/4.1","x.y.z/bla-plugin/4.1"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginConfigurationTag.xml");
		expectedChanges=new ArrayList<Change>();
//		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","","\"aNewTagHere\"::\"GoodValue\"")); // cannot be done that way, because of the possibility to have more than one insert...
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginConfigurationTag.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		expectedChanges=new ArrayList<Change>();
//		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","\"aNewTagHere\"::\"GoodValue\"",""));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE","",""));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePluginVersion.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE","2.6","2.8"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_removePluginVersion.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_DELETE","2.6",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_removePluginVersion.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_INSERT","","2.6"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
		newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginDep.xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_INSERT","","com.google.guava/guava/19.0"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_removePluginDep.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_DELETE","commons-io/commons-io/2.4",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePluginDepArtifactId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","junit/junit/4.12","junit/junit-new/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePluginDepVersion.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_VERSION_UPDATE","4.12","4.27"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePluginDepGroupId.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","junit/junit/4.12","junit-new/junit/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addPluginDepScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","commons-io/commons-io/2.4","commons-io/commons-io/2.4"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_removePluginDepScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","junit/junit/4.12","junit/junit/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePluginDepScope.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_UPDATE","junit/junit/4.12","junit/junit/4.12"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);




        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePlugin1.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_UPDATE","x.y.z/bla-plugin/4.1","x.y.nn/bla-plugin/4.1"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_basePluginDep.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changePlugin2.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_UPDATE","x.y.z/bla-plugin/4.1","x.y.z/bla-plugins/4.1"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);



        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_removeDistRepo.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_INSERT","","repository.jboss.org/::/::/UKN/::/::/file://${maven.repository.root}"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeDistRepo.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_UPDATE","\"url\"::\"file://${maven.repository.root}\"","\"url\"::\"file://${maven.repository.root}/url\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_removeDistRepo.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_DELETE","repository.jboss.org/::/::/UKN/::/::/file://${maven.repository.root}",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_removeDistSnapshotRepo.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_INSERT","","snapshots.jboss.org/::/::/JBoss Snapshot Repository/::/::/dav:https://snapshots.jboss.org/maven2"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeDistSnapshotRepo.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_UPDATE","\"url\"::\"dav:https://snapshots.jboss.org/maven2\"","\"url\"::\"dav:https://snapshots.jboss.org/maven2/newone\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_removeDistSnapshotRepo.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_DELETE","snapshots.jboss.org/::/::/JBoss Snapshot Repository/::/::/dav:https://snapshots.jboss.org/maven2",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);




        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSite.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SITE_INSERT","","apache.website/::/::/scm:svn:https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-configuration/"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSite.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_baseDist.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SITE_DELETE","apache.website/::/::/scm:svn:https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-configuration/",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSite.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeSitePart1.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SITE_UPDATE","\"id\"::\"apache.website\"","\"id\"::\"apache.websites\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSite.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeSitePart2.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DIST_SITE_UPDATE","\"url\"::\"scm:svn:https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-configuration/\"","\"url\"::\"scm:svn:https://git.apache.org/repos/infra/websites/production/commons/content/proper/commons-configuration/\""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);





        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_INSERT","","src/test/resources"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_DELETE","src/test/resources",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeTestResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_UPDATE","",""));   //TODO SEE BELOW
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; //TODO ALSO HERE
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_change2TestResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_UPDATE","","")); //TODO CHECK IF WE CAN do this; atm: (same for configuration tag and probably many others)
        																   //what is the old/new value if there is more than one change? --> if the changes are aggregated,
        																   //there should not be a detailed old/new value, but maybe the parent tag? VERIFY and DECIDE!
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResourcePart.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_UPDATE","",""));   //TODO SEE ABOVE
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResourcePart.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_RESOURCE_UPDATE","",""));   //TODO SEE ABOVE
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);




        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_INSERT","","."));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_DELETE",".",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_change2Resource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResourcePart.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResourcePart.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addResource.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("RESOURCE_UPDATE","",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSourceDir.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SOURCE_DIRECTORY_INSERT","","src/main/myJava"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSourceDir.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SOURCE_DIRECTORY_DELETE","src/main/myJava",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addSourceDir.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeSourceDir.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("SOURCE_DIRECTORY_UPDATE","src/main/myJava","src/main/myNewJava"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestSourceDir.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_SOURCE_DIRECTORY_INSERT","","src/test/myJava"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestSourceDir.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_base.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_SOURCE_DIRECTORY_DELETE","src/test/myJava",""));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);

        oldTmpFile = new File(PATH_PREFIX+"poms/build/pom_addTestSourceDir.xml");
        newTmpFile = new File(PATH_PREFIX+"poms/build/pom_changeTestSourceDir.xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("TEST_SOURCE_DIRECTORY_UPDATE","src/test/myJava","src/test/myNewJava"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
        retVal.add(o);


//        return tmp;
        return retVal;
    }
}