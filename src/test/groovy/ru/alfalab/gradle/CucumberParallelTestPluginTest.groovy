package ru.alfalab.gradle

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test

@CompileStatic
class CucumberParallelTestPluginTest {
    CucumberRunnerClassGenerator helper

    @Before
    void prepare() {
        helper = new CucumberRunnerClassGenerator(
                glue: ["steps"],
                monochrome: false,
                format: ["com.epam.reportportal.cucumber.ScenarioReporter", "myRandomParameter"]
        )
    }

    @Test
    void checkGets() {
        assert helper.getHeader() == """
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
public class GradleTestRunner {
"""
        assert helper.getFooter() == """
}
"""
    }

    @Test
    void checkCucumberFormatOptions() {
        assert helper.getCucumberFormatOptions() == "\"pretty\", \"json:build/cucumber/cucumber1.json\", \"com.epam.reportportal.cucumber.ScenarioReporter\", \"myRandomParameter\""
    }

    @Test
    void checkPathToJavaSource() {
        String path = "a\\b\\c"
        path = helper.pathToJavaSource(path)
        assert path == "a\\\\b\\\\c"
    }

    @Test
    void checkMainMethodInClass() {
        assert helper.getFeatureClass("a\\b") == """
    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"steps"},
            format = {"pretty", "json:build/cucumber/cucumber1.json", "com.epam.reportportal.cucumber.ScenarioReporter", "myRandomParameter"},
            features = {"a\\\\b"},
            monochrome = false
    )
    public static class GradleTestRunner1 { }
"""
    }

    @Test
    void checkGenerateInnerRunnerClass() {
        assert helper.generateInnerRunnerClass("filename", "dir\\filename") == """
    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"steps"},
            format = {"pretty", "json:build/cucumber/cucumber1.json", "com.epam.reportportal.cucumber.ScenarioReporter", "myRandomParameter"},
            features = {"dir\\\\filename"},
            monochrome = false
    )
    public static class GradleTestRunner1 { }
"""
    }

    @Test
    void checkWorkWithFile() {
        File file = new File("filename.txt")
        assert helper.generateInnerRunnerClass(file) == null
    }

    @Test
    void checkWorkWithFeatureFile() {
        File file = new File("/tmp/filename.feature")
        assert helper.generateInnerRunnerClass(file) == """
    @RunWith(Cucumber.class)
    @CucumberOptions (
            glue = {"steps"},
            format = {"pretty", "json:build/cucumber/cucumber1.json", "com.epam.reportportal.cucumber.ScenarioReporter", "myRandomParameter"},
            features = {"/tmp/filename.feature"},
            monochrome = false
    )
    public static class GradleTestRunner1 { }
"""
    }

    @Test
    void checkParseTag() {
        String tags = "@abc,@qwerty,@qazwsx"
        Set<String> set = new HashSet<>()
        set.add("@abc")
        set.add("@qwerty")
        set.add("@qazwsx")

        assert set == FeatureReader.parseTag(tags)
    }


}