# cucumber-parallel-test-plugin
Plugin for Cucumber parallel testing

What we need.

First. Create source set for .features files
Second. Create task with generating source GradleRunner files into `build/generated-sources/cucumber` folder. One for one feature
Third. Add this folder to java source set.
And final. Add Extension to controlling