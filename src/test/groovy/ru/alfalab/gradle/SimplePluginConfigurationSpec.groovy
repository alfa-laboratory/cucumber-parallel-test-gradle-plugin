package ru.alfalab.gradle

import nebula.test.PluginProjectSpec

/**
 * Created by aleksandr on 22.11.16.
 */
class SimplePluginConfigurationSpec extends PluginProjectSpec {
    @Override
    String getPluginName() {
        return 'ru.alfalab.cucumber-parallel-test'
    }
}
