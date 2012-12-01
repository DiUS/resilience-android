#!/bin/bash

# In case of emergency, break glass.  Only here for first time checkouts when not at DiUS

mvn install:install-file -Dfile=Parse.jar -DgroupId=Parse -DartifactId=Parse -Dversion=1.1.6 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=maps.jar -DgroupId=com.google.android.maps -DartifactId=maps -Dversion=15r2 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=mapviewballoons.apklib -DgroupId=com.readystatesoftware -DartifactId=mapviewballoons -Dversion=1.0-SNAPSHOT -Dpackaging=apklib -DgeneratePom=true
