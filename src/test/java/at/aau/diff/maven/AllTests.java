package at.aau.diff.maven;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.aau.diff.maven.standard.BuildTagChangesTest;
import at.aau.diff.maven.standard.DependencyChangesTest;
import at.aau.diff.maven.standard.GeneralChangesTest;
import at.aau.diff.maven.standard.GeneralRepositoryChangesTest;
import at.aau.diff.maven.standard.PersonalChangesTest;
import at.aau.diff.maven.standard.ProfileChangesTest;
import at.aau.diff.maven.standard.ReportingChangesTest;
import at.aau.diff.maven.standard.verifyThese.GumTreeUpdateTest;

//254/0/6
@RunWith(Suite.class)
@SuiteClasses({ 
	//120/0/0
	BuildTagChangesTest.class, DependencyChangesTest.class, GeneralChangesTest.class, GeneralRepositoryChangesTest.class, PersonalChangesTest.class, ProfileChangesTest.class, ReportingChangesTest.class, 
	//30/0/0
	at.aau.diff.maven.standard.evaluation1.ActivemqEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.CamelEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.HadoopEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.HbaseEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.HibernatesearchEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.JenkinsEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.KarafEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.SpringrooEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.WicketEvaluationTest.class,
	at.aau.diff.maven.standard.evaluation1.WildflyEvaluationTest.class,
	//100/0/6
	at.aau.diff.maven.standard.manualevaluation.ActivemqEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.CamelEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.HadoopEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.HbaseEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.HibernatesearchEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.JenkinsEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.KarafEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.SpringrooEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.WicketEvaluationTest.class,
	at.aau.diff.maven.standard.manualevaluation.WildflyEvaluationTest.class,
	
	//4/0/0
	GumTreeUpdateTest.class,
})
public class AllTests {

}
