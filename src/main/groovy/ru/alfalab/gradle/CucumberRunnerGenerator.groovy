package ru.alfalab.gradle

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

/**
 * Created by ruslanmikhalev on 24/11/16.
 */
@CompileStatic
@Slf4j
class CucumberRunnerGenerator {
    File buildDir;
    FileCollection features;
    Project project;
    List<String> glue;
    List<String> formatter;
    boolean monochrome;
    boolean strict;
    CucumberRunnerClassGenerator helper;

    public void generate() {
        project.mkdir(buildDir);
        helper = new CucumberRunnerClassGenerator(
                formatter: formatter,
                glue: glue,
                monochrome: monochrome,
        );
        new File(buildDir, "GradleTestRunner.java").withWriter("utf8") { writer ->
            writer << helper.getHeader();
            features.files.sort( { file -> file.name } ).each { file -> writer << helper.generateInnerRunnerClass(file) }
            writer << helper.getFooter();
        }
    }
}
