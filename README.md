Resilience for Android
======================

Prerequisite software
---------------------

* Java 1.6
* Google Android Developer SDK
	* Android 4.0.3 (API 15)
	* Android 2.3.3 (API 10) - Required by one of the testing libraries, Robolectric
* Maven 3.x
* Eclipse + ADT plugin *or* IntelliJ *or* whatever works for you


Setup procedure
----------------
There are a few jars that are not available in any public maven repositories which are hosted on DiUS Resilience artifactory instance which is on the same box as the CI Server.  Therefore if you are not on the DiUS network when you do your first build, you will need the following jars to be in your local maven repository.  They are checked in to the libs/external folder for convenience.  Run the install command in that folder.

	./install-local-libs.sh

  * Parse.jar, required for integration with the parse service.
  * maps-15r2.jar, required to integrate with google maps

Emulator
--------
  An emulator is required, follow the steps in this article to setup a performant emulator.

  http://codebutler.com/2012/10/10/configuring-a-usable-android-emualtor

  Deviations from this article
  ----------------------------

  Set the SD card size to 256 MB, not 512
  When pushing the google maps libraries to your new emulator, use the files int repository directory libraries/emulator
  Creating a system image did is problematic.  If it works for you, GREAT!  Otherwise, each time you power down your emulator, you will need to re push the libraries and ensure they are installed onto the device. Copying the files does NOT install them and make them available.  You need to do a software restart to ensure they are installed.  Do this by issuing the following commands
	
	adb shell stop
	adb shell start
	
  This will restart the software.

Building
--------

	mvn clean install


Running tests:
--------------

  Unit tests:

    mvn test

  Integration (instrumentation) tests

    mvn clean install 
  or 
	mvn android:instrument

  Running unit tests from the IDE
	Ensure the working directory for JUnit tests is set to resilience/resilience-app


Integration with Eclipse:
-------------------------

    mvn eclipse:eclipse


Integration with IntelliJ
-------------------------
  * Import the project as a regular maven project, ensuring all submodules are imported
  * IntelliJ should pick up the two Android facets, resilience-app and resilience-it
  * When running integration tests from the IDE, you will need to change the classpath to ensure that the Junit 4.x dependency is before the Android dependency