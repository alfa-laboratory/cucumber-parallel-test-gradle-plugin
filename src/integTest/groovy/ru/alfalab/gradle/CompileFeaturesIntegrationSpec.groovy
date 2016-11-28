package ru.alfalab.gradle

import nebula.test.IntegrationSpec

/**
 * Created by aleksandr on 22.11.16.
 */
class CompileFeaturesIntegrationSpec extends IntegrationSpec {

    def "generate task no throw exception"() {
        setup:
        copyResources("create-runner-from-features.gradle", "build.gradle")
        when:
        runTasksSuccessfully("generateRunner")
        then:
        noExceptionThrown()
    }

    def "generateRunner depends on compileTestJava"() {
        setup:
        copyResources("create-runner-from-features.gradle", "build.gradle")
        createFile(["src", "test", "resources", "features", "Test.feature"].join(File.separator))
        when:
        runTasksSuccessfully("test")
        then:
        fileExists([ "build", "classes", "test", "GradleTestRunner.class" ].join(File.separator))
    }

    def "built features turn into java-file"() {
        setup:
        def path = [ "build", "cucumber-parallel-test", "generated", "src", "test", "java", "GradleTestRunner.java" ]
                .join(File.separator);

        copyResources("create-runner-from-features.gradle", "build.gradle")
        createFile(["build", "resources", "test", "features", "Test.feature"].join(File.separator))
        when:
        runTasksSuccessfully("generateRunner")
        then:
        fileExists(path)
        new File(projectDir, path).text.contains("Test.feature")
    }

    def "generated java code must be correct"() {
        setup:
        copyResources("create-runner-from-features.gradle", "build.gradle")
        def file1 = createFile(["build", "resources", "test", "features", "first.feature"].join(File.separator))
        def file2 = createFile(["build", "resources", "test", "features", "second.feature"].join(File.separator))
        when:
        runTasksSuccessfully("generateRunner")
        def code = new File( projectDir, [ "build", "cucumber-parallel-test", "generated", "src", "test", "java", "GradleTestRunner.java" ].join(File.separator) ).text
        then:
        code == """
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
public class GradleTestRunner {

    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"tests/steps"},
            format = {"pretty", "json:build/cucumber/cucumber1.json"},
            features = {"${file1.absolutePath}"}
    )
    public static class GradleTestRunner1 { }

    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"tests/steps"},
            format = {"pretty", "json:build/cucumber/cucumber2.json"},
            features = {"${file2.absolutePath}"}
    )
    public static class GradleTestRunner2 { }

}
""" as String
    }

    def "custom glue value must change glue in a java-file content"() {
        setup:
        copyResources("create-runner-from-features-with-changed-glue.gradle", "build.gradle")
        def file = createFile(["build", "resources", "test", "features", "first.feature"].join(File.separator))
        when:
        runTasksSuccessfully("generateRunner")
        def code = new File( projectDir, [ "build", "cucumber-parallel-test", "generated", "src", "test", "java", "GradleTestRunner.java" ].join(File.separator) ).text
        then:
        code == """
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
public class GradleTestRunner {

    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"mytests/steps"},
            format = {"pretty", "json:build/cucumber/cucumber1.json"},
            features = {"${file.absolutePath}"}
    )
    public static class GradleTestRunner1 { }

}
""" as String
    }


}
