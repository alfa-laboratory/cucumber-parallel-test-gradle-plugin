package ru.alfalab.gradle

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by aleksandr on 22.11.16.
 */
@Slf4j
class CucumberParallelTestPlugin implements Plugin<Project> {

    @Delegate
    Project project

    @Override
    void apply(Project project) {
        this.project = project
        plugins.apply "java"

        def conf = new Config ([
            genDir: [ buildDir, "cucumber-parallel-test", "generated", "src", "test", "java" ].join(File.separator),
            featuresDir: [ buildDir, "resources", "test", "features" ].join(File.separator)
        ])

        repositories {
            mavenCentral()
        }

        dependencies {
            compile group: 'info.cukes', name: 'cucumber-java', version: '1.2.4'
            compile group: 'info.cukes', name: 'cucumber-junit', version: '1.2.4'
        }

        addGeneratedToSource(project, "test", conf.genDir)

        task(GenerateRunnerTask.TASK_NAME, type: GenerateRunnerTask) {
            inputs.files fileTree(dir: conf.featuresDir).include("**/*.feature")
            outputs.dir  conf.genDir
        }

        project['compileTestJava'].dependsOn('generateRunner')
        project['generateRunner'].dependsOn('processTestResources')
    }

    @CompileStatic
    static class Config {
        String genDir
        String featuresDir
    }

    void addGeneratedToSource(Project project, String type, String buildDir) {
        logger.info("Adding ${buildDir} to test source set")
        project.sourceSets[ type ].java.srcDir(buildDir)
    }

}
