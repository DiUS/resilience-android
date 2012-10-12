resilience-android
==================

Building:

    gradle buildDependencies assemble

Running tests:

  Unit tests:

    gradle unitTests

  Integration (instrumentation) tests:

    gradle androidInstrumentationTests

Integration with Eclipse:

    gradle eclipse

  Then use File -> Import -> Existing Projects into Workspace and select the location of the *top level* build.gradle file.

**NOTE**: Each of the above build commands should be invoked form the top level, i.e. the parent  build.gradle file.
