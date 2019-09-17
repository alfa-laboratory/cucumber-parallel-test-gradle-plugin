package ru.alfalab.gradle

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Project

/**
 * Created by ruslanmikhalev on 24/11/16.
 */
@CompileStatic
@Slf4j
class CucumberRunnerGenerator {
    File buildDir
    Set<File> features
    Project project
    List<String> glue
    List<String> format
    boolean monochrome
    boolean strict
    CucumberRunnerClassGenerator helper

    void generate() {
        project.mkdir(buildDir)
        helper = new CucumberRunnerClassGenerator(
                format: format,
                glue: glue,
                monochrome: monochrome,
        )
        new File(buildDir, "GradleTestRunner.java").withWriter("utf8") { writer ->
            writer << helper.getHeader()
            features.sort({ file -> file.name }).each { file -> writer << helper.generateInnerRunnerClass(file) }
            writer << helper.getFooter()
        }
    }
}
