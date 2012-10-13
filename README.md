Resilience for Android
======================

Prerequisite software
---------------------

* Gradle version 1.1 to 1.2 *
* Groovy version 2.0.1 *
* Eclipse + ADT plugin *or* IntelliJ *or* whatever works for you

\* Later versions may work.

Building
--------

    android update project -p . -s
    gradle buildDependencies assemble

Running tests:
--------------

  Unit tests:

    gradle unitTests

  Integration (instrumentation) tests:

    gradle androidInstrumentationTests

  Running tests from the IDE
	Ensure the working directory for JUnit tests is set to resilience-android/resilienceTest


Integration with Eclipse:
-------------------------

    gradle eclipse

  Then use File -> Import -> Existing Projects into Workspace and select the location of the *top level* build.gradle file.

Integration with IntelliJ
-------------------------

  Integration with intellij is pretty woeful.  Once you configure it, it's ok, but getting there can be a pain.  Once thing to note, we have created a custom gradle unitTests configuration which is used to run unit tests in the resilienceTest project. The JetGradle plugin knows nothing about this due to a gradle limitation apparently, so there are some manual steps to get the unitTests dependencies recognised by intelliJ

  NOTE:  Due to gradle api limitation and intelliJ not recognising non default configurations you will need to ensure that each "unitCompile" dependency is also specified as a "compile" dependency during the import.  Alter the file for import to do this.  I know, it's ugly...

    -  Import an existing gradle project
	-  Once imported, in the project settings, remove the top level resilience-android module.  This should leave two modules, resilience and resilienceTest	
	-  Remove the module root resilience-android from each of the remaining two modules.
	-  Remove references to the source folders that do not exist i.e. src/main/resources e.t.c	
	-  Add src/unit/java as a test source root for the resilienceTest module.	
	-  Select the Android 4.0 Google APIs SDK as a dependency if not already set.	
	-  Ensure the 'Android' facet for each module is configured to use the modules directory for its compiler output.  By default, it will use the 'resilience-android' directory for things like the AndroindManifest.xml and gen directory. This needs to be changed to resilience-android/resilience and resilience-android/resilienceTest respectively.  Make these changes for both modules on the 'Structure' and 'Compiler' tabs	
	-  Ensure 'Adroid' is the last dependency in the resilienceTest module.  This will allow the unit tests to run from the IDE as they require junit-4.x and android contains a version of 3.x
	

**NOTE**: Each of the above build commands should be invoked form the top level, i.e. the parent  build.gradle file.
