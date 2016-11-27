package ru.alfalab.gradle

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Created by ruslanmikhalev on 24/11/16.
 */
@CompileStatic
class GenerateRunnerTask extends DefaultTask {

    final static String TASK_NAME = "generateRunner"

    @Input
    String glue = [ "tests", "steps" ].join(File.separator);

    @TaskAction
    def run() {
        CucumberRunnerGenerator generator = new CucumberRunnerGenerator(
            project: project,
            features: inputs.files,
            buildDir: outputs.files.singleFile,
            glue: glue
        )
        generator.generate()
    }

}
