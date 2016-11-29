# cucumber-parallel-test-gradle-plugin

Gradle plugin for generating test runners for parallel testing with Cucumber [Cucumber](https://cucumber.io/)

## Project quality

[![Build Status](https://travis-ci.org/alfa-laboratory/cucumber-parallel-test-gradle-plugin.svg?branch=master)](https://travis-ci.org/alfa-laboratory/cucumber-parallel-test-gradle-plugin)
[![Coverage Status](https://coveralls.io/repos/github/alfa-laboratory/cucumber-parallel-test-gradle-plugin/badge.svg)](https://coveralls.io/github/alfa-laboratory/cucumber-parallel-test-gradle-plugin)

## Usage

### Applying the Plugin

To include, add the following to your build.gradle

    buildscript {
      repositories { jcenter() }

      dependencies {
        classpath 'ru.alfalab.gradle:cucumber-parallel-test-gradle-plugin:x.x.+'
      }
    }

    apply plugin: 'ru.alfalab.cucumber-parallel-test'

### Tasks Provided

`generateRunner` generate test runners one for `.feature` file in test/resources directory

### Extensions Provided

Nothing at the moment
    
### Runners

Runners can be found in dir ${project.buildDir}/cucumber-parallel-test/generated/src/test/java

All runners clasess are added to `testJava` `sourceSet`.

### Test

Run `./gradlew test integrationTest` an see results in console output or follow to `./build/test/`
This directory contain integration tests and their data.

### Build and publish

1. `./gradlew build` build
2. `./gradlew pTML` publish to maven local
