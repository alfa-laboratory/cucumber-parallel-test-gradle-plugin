package ru.alfalab.gradle

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by aleksandr on 22.11.16.
 */
@Slf4j
@CompileStatic
class CucumberParallelTestPlugin implements Plugin<Project> {

    @Delegate
    Project project

    @Override
    void apply(Project project) {
        this.project = project
    }
}
