package ru.alfalab.gradle

import groovy.transform.CompileStatic
import org.junit.Before
import org.junit.Test

@CompileStatic
public class CucumberParallelTestPluginTest {
    CucumberRunnerClassGenerator helper;

    @Before
    public void prepare() {
        helper = new CucumberRunnerClassGenerator(
                glue: ["steps"],
                monochrome: false,
                formatter: ["com.epam.reportportal.cucumber.ScenarioReporter", "myRandomParameter"]
        )
    }

    @Test
    public void checkGets() {
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
    public void checkCucumberFormatOptions() {
        assert helper.getCucumberFormatOptions() == "\"pretty\", \"json:build/cucumber/cucumber1.json\", \"com.epam.reportportal.cucumber.ScenarioReporter\", \"myRandomParameter\""
    }

    @Test
    public void checkPathToJavaSource() {
        String path = "a\\b\\c"
        path = helper.pathToJavaSource(path)
        assert path == "a\\\\b\\\\c"
    }

    @Test
    public void checkMainMethodInClass() {
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
    public void checkGenerateInnerRunnerClass() {
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
    public void checkWorkWithFile() {
        File file = new File("filename.txt");
        assert helper.generateInnerRunnerClass(file) == null
    }

    @Test
    public void checkWorkWithFeatureFile() {
        File file = new File("/tmp/filename.feature");
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


}