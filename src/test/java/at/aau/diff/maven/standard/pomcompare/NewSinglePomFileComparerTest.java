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
	//	private static final String PATH_PREFIX="/Users/chris/Documents/workspaceBuildChangeDifferVariants/semantic-differ/differ-maven-differ/";
	private static final String PATH_PREFIX=new File("").getAbsolutePath()+"/";
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
        
        File oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1444274359000][435e5bfaf877f2adc9752f0271431269a40d572c].xml");
		File newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1444510427000][85bdca5b1e59890ad46f89eaedcda8560e8e06e2].xml");
		List<Change> expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROFILE_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_INSERT"));
        Object[] o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
        
		oldTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1141721070000][b80ccdf412b442b978ac66006f2f471097e7d78c].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1141888297000][c40f989966be2085e2d7a02442b7129ff0d48702].xml");
		expectedChanges=new ArrayList<Change>();
		//no logical change expected (we ignore the move because it has no impact
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1163472184000][2d64943d45f555c0467b88ba6d4a02fbbb56489e].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1163723343000][86cbdfd5db5b1c90c4a5f4dcdef702b0cf4901ce].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
        oldTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1221001253000][cc90a50844a23a60aeddcde00480f765a9001572].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1221001314000][d3bebf209bd0577e2e178a0a505aa02cc92973ad].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PROJECT_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("SCM_CONNECTION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("SCM_DEVCONNECTION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("SCM_URL_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1352304007000][c5cf03806d646045e01f6cd382504fa73dedfeee].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1352309191000][bfbe90b39929db4512be95d8931d8884c1c11c9a].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("MODULE_UPDATE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1421259661000][0d815642c1c441b3bbca69c4987430a14e28c441].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/activemq/pom[1421409698000][efe68aa0327d71a92a78751c924d97997752ed6d].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1425711186000][92fcf851dde192c7889e554b95d9febc95c2eed5].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1425799321000][cfab63a588e0461e67a2f93a1bb99cdde560b02e].xml");
        expectedChanges=new ArrayList<Change>();//no changes as moves are not relevant here
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1392993109000][d10d5b86668d7cdd39d75d2fca20241710bf42d4].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1395102483000][40c9b25435c0fd678a080f3898a3faeab03fecf0].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1286258973000][4eac4a5b2ff82eb8cbc8be6c1260ca08951d693d].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1286259343000][74cbd4ae10cc4beac1985bf29c7897d8238f2a36].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
		//*********** DOES NOT WORK ATM!!! *********** nr 9
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1243213953000][87c8eb57329f0ece046716dd90a3805308e861fc].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1243846152000][ebb6196ce82399e888baae7430399881380ce980].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_DELETE"));
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1220205450000][f31708b525c2e908a4f9b6885af079798a8a6614].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1220240063000][c909245804d27e18608a828155e681f859dd7313].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
        oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1189839161000][81c8e77f57f6ea8d4f00dfa49e102bee1ad0cd35].xml");
        newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1189842269000][ab2ff9945c52203f9bb2c82f7905388e3c50766e].xml");
        expectedChanges=new ArrayList<Change>();
        expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
        o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
        
		oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1178288515000][72574e063829d27f200ec677fe65e0d0b4496cb0].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1178294569000][a3a2ce3e7e3e88eb2dd075a6c30e64a2ee55bd16].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1177409458000][2d116623e302f149806158d74c11344ccef3c452].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/camel/pom[1177499376000][51d117729a3d52203ebc2fed95ebe4fb668e668a].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1418727876000][07bb0b0bbb4732687c4992e8ce61a43ce8ba3db6].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1418853953000][316613bdaee282cdd5c0958bf2719e0601a5b66c].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DEPENDENCY_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1391578922000][cec369149b4205af0e20dfbe7f2f541f46688fc5].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1409947750000][0571b4561bad7e0230920e52d3758a3658fcf20d].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1382581839000][b3195f1283c4524c32e67640e965b70e96530fcc].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1384473173000][6141d2ed27f817680da1a7859c9eb88cf32440a6].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1350506984000][f91a2cb14db2f43dedf0bb19a6f21992b7776341].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1350600041000][53c598ab11d815fdd1bd67c6fc5cd3d9aef10b7a].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1320302072000][5d5e2df9d33ba43ba445f9710da47b88fbcb12e1].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1321575593000][7edfff57954b382926d7c771dc13f0e8a70f215f].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1319847376000][cd77a1668fc7f03d86af073f0db2a0409633762d].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1320122830000][8e04fa8b8414b66966499347453adbace256559b].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		//nr 20
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1318014061000][68d1162f7ac559c43c692a774c146aaf69a18a59].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1318034321000][49729591731c5ec1c781ea0ab92b9127e160a071].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_DELETE"));
		expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_DELETE"));
		expectedChanges.add(new MavenBuildChange("REPOSITORY_DELETE"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		// nr 21
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1317949278000][21cb78d246d9fbdc2be854280e1366c92dadf694].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1318014061000][68d1162f7ac559c43c692a774c146aaf69a18a59].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DIST_REPOSITORY_INSERT"));
		expectedChanges.add(new MavenBuildChange("DIST_SNAPSHOT_REPOSITORY_INSERT"));
		expectedChanges.add(new MavenBuildChange("REPOSITORY_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_DELETE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
		retVal.add(o);
		
		//*********** DOES NOT WORK ATM!!! *********** !!! Does not work because of wrong matching nr 22 
		//(we should try sorting of tags for this!)
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1314034858000][14b97a91d9a7d721bd1dc276640dcf9c99af188e].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1314231652000][6316e662791c3fe19ce43550a57ae4e9beac05ad].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE"));
		expectedChanges.add(new MavenBuildChange("MODULE_DELETE"));
		expectedChanges.add(new MavenBuildChange("MODULE_UPDATE"));
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges}; 
//		retVal.add(o);
//		tmp.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1312912980000][0a2b40fad59267849a1f6286a38ae068d00595f1].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hadoop/pom[1313775383000][d86f3183d93714ba078416af4f609d26376eadb0].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		expectedChanges.add(new MavenBuildChange("MODULE_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1450471729000][eb59d4d7690bb4d8d0afa460202c68885ef6a271].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1451408400000][822fead744a308df7ae45da440047207841d7abc].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		expectedChanges.add(new MavenBuildChange("PROFILE_INSERT"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1414427363000][70a312e53fc798c178f6abc95614a6673e9da244].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1414447135000][f2c83704f4b004faba10f1695c75fa56d7caf1c5].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1361821725000][582cd3b26340c416b60a4e07b8e8b5dc53b450df].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1361832617000][24ebbcabc39b0e8daa01c6ec004da62a3909b803].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);

		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1358399385000][029fb95739d63cfa2f6101ab4a290edf5877adcb].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1358884853000][461c3c0e7fd236c08e96a386b2bbc45341f0d2c2].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		//nr28
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1330709194000][2c37d21a529fe341235fe2836d88bf47a4201d68].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1330711543000][514d7cc8b6444d84b0cd880415252ab2e98bfea4].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_CONFIGURATION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1322093781000][d3da94321c2b55ebb026796b1e6d8435fe294077].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1322149488000][70f14867fb6bb8e2173f155c250990e2ce05890c].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		//nr30
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1316493101000][1434e9611741d2558c5891a20ff37f0011673f37].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1317236156000][504762b2593207aee05afcd6db75b63088cec952].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1312462712000][c0acc54f57084e8e36334df897474fe5b758e4b1].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hbase/pom[1312675102000][df4059d36ca2ea0f11eed28a6704dfeee7529f55].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEVELOPER_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1431968701000][a48b7c953314de4acc67fecca45f015a4ad99926].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1431968701000][c06e4fe5fbc6b5a09195f40d760a093691f2c7f2].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PARENT_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1373471399000][944ac8e3f8cc68158c477bcea36a4b6e44fea4ca].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1374168219000][cbc0909de40c01eafcec405d21b40cba89353b99].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1335183640000][99ca774ea09f12173ab2c0dcf150f3c5f05d8dfa].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1335218561000][a8ec4a06ffe9825b6d36e4992deaa6f130524f60].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("GENERAL_PROPERTY_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1300371816000][4f4d8c69cc33823e9f4865cc35f7e255d7d8fd4e].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1300375121000][23b275efb46186e12a05074cae8ed66a2a43d244].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_VERSION_DELETE"));
		expectedChanges.add(new MavenBuildChange("PLUGIN_INSERT"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1282813885000][c259b8cbf569cfb929a4cc527a023059dbc579ea].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1282890121000][9e798e2c5c5ff8bb55fc52bc6b39077491610181].xml");
		expectedChanges=new ArrayList<Change>();
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
		expectedChanges.add(new MavenBuildChange("DEPENDENCY_VERSION_UPDATE"));
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
		retVal.add(o);
		
		//TODO finish this test
		oldTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1253021336000][a76b0bb37a6a0ed1f5e50511f21569c8c91927a8].xml");
		newTmpFile = new File(PATH_PREFIX+"poms/hibernatesearch/pom[1255696370000][cd05ea8c1244bc3d118a15a7a919a4aef7004ab8].xml");
		expectedChanges=new ArrayList<Change>();
		o = new Object[]{oldTmpFile,newTmpFile,expectedChanges};
////		retVal.add(o);
		
        return retVal;
//		return tmp;
    }
}