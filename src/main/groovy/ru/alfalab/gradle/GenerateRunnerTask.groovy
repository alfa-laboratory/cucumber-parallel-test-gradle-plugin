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
    private List<String> glue = [["tests", "steps"].join(File.separator)]

    @Input
    public List<String> format = []

    @Input
    public String tags

    @Input
    boolean monochrome

    void setGlue(String glue) {
        this.glue = [glue]
    }

    void setGlue(List<String> glue) {
        this.glue = glue
    }

    @TaskAction
    def run() {
        CucumberRunnerGenerator generator = new CucumberRunnerGenerator(
                project: project,
                features: FeatureReader.filterByTags(inputs.files.files, tags),
                buildDir: outputs.files.singleFile,
                glue: glue,
                format: format,
                monochrome: monochrome
        )
        generator.generate()
    }

}
