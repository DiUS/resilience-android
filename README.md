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


Emulator
--------
  An emulator is required, follow the steps in this article to setup a performant emulator.

  http://codebutler.com/2012/10/10/configuring-a-usable-android-emualtor

  Deviations from this article
  ----------------------------

  Set the SD card size to 256 MB, not 512.
  
  When pushing the google maps libraries to your new emulator, use the files int repository directory libraries/emulator
  
  Creating a system image can be problematic and in may not actually work!.  
  If it works for you, GREAT!  Otherwise, each time you power down your emulator, you will need to re push the libraries and  ensure they are installed onto the device. 

  Copying the files does NOT install them and make them available.  You need to do a software restart to ensure they are installed.  Do this by issuing the following commands
	
	adb shell stop
	adb shell start
	
  This will restart the software.

Building
--------

	ant debug install


-- NOT CURRENTLY WORKING WITH NEW BUILD --
	
  We have externalised parse API keys and Google maps api keys as they differ per developer.  Create a *resilience-local.properties* in the resilience-app directory and ensure you have the relevant key values specified, then run the maven resources target generate the filtered resource. e.g. add the the following keys

	map.key=some_key
	parse.client.key=some_other_key
	parse.app.key=some_other_other_key

	mvn resource:resources

----

Instead (for now):
  * Modify config.xml manually, and specify the base url of your Open311 server.
  * Edit AndroidManifest.xml and set the com.google.android.maps.v2.API_KEY property to your Google Maps V2 API key.


Running tests:
--------------

  Unit tests:

    ant test

  -- NOT CURRENTLY WORKING WITH NEW BUILD --

  Integration (instrumentation) tests

    mvn clean install 
  or 
	mvn android:instrument

  Running unit tests from the IDE
	Ensure the working directory for JUnit tests is set to resilience/resilience-app
  ----


Integration with IntelliJ
-------------------------
  * Create a new Android project using existing sources. Point it to the resilience-app folder.
  * Create a new Android library project and point it to the libraries/google-play-services_lib project.
  * Add google-play-services_lib as a compile dependency to resilience-app.
  * Have the jars under libs/ to your compile classpath, and the ones under libs/test to your test classpath.
  * Launch the main activity - ResilienceActivity.
