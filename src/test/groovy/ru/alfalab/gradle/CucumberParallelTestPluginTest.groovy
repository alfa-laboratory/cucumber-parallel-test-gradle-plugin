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

    @Test
    void checkFilterByTags() {
        Set<File> featSetBefore = new HashSet<>()
        featSetBefore.add(new File("src/test/resources/test.feature"))
        featSetBefore.add(new File("src/test/resources/test2.feature"))

        Set<File> featSetAfter = new HashSet<>()
        featSetAfter.add(new File("src/test/resources/test.feature"))

        assert featSetAfter == FeatureReader.filterByTags(featSetBefore, "@trojan")
    }

    @Test
    void checkReadFromFile() {
        Set<String> fileLine = new HashSet<>()

        fileLine.add("")
        fileLine.add("@alfa-travel")
        fileLine.add("  Сценарий: Заказ дебетовой карты Alfa-Travel Premium")
        fileLine.add("@debit-cards")
        fileLine.add("@desktop")
        fileLine.add("Функциональность: Заказ дебетовой карты Alfa-Travel")
        fileLine.add("  @trojan")
        fileLine.add("  Предыстория: Открылась страница дебетовой карты Alfa-Travel")
        fileLine.add("@middle")
        fileLine.add("#language:ru")
        fileLine.add("    Дано совершен переход на страницу \"Страница заказа Alfa-Travel\" по ссылке \"alfaTravel\"")
        fileLine.add("    Когда элемент \"Заказать карту\" существует на странице")
        fileLine.add("  @desktop-positive")

        assert fileLine == FeatureReader.readFromFile(new File("src/test/resources/test.feature"))
    }


}