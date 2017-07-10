# README #

This README documents the steps to get this application up and running.

## Javadoc ##

The documentation of the project can be found under doc/buildtype/javadoc.

To generate the Javadoc run one of the following gradle tasks:

* Debug build: `generateDebugJavadoc`
* Release build: `generateReleaseJavadoc`

## Gradle build system  ##

For the build system to work you have to setup your build environment first:

**0. Download the Android libraries**

Download the Android [SDK](https://developer.android.com/sdk/index.html#Other) and [NDK](https://developer.android.com/tools/sdk/ndk/index.html) and extract them.

Setup the environment variables for the libraries:

Windows:

* Name: `ANDROID_HOME` Path: `/path/to/android-sdk`
* Name: `ANDROID_NDK_HOME` Path: `/path/to/android-ndk`

Linux:

* `export ANDROID_HOME=/path/to/android-sdk`
* `export ANDROID_NDK_HOME=/path/to/android-ndk`

**1. Install the following packages with the Android SDK Manager**

* `Tools/Android SDK Tools`
* `Tools/Android SDK Platform-tools Rev. 24`
* `Tools/Android SDK Build-tools Rev. 24`
* `Android 5.1 (API 22)`
* `Extras/Android Support Repository`
* `Extras/Android Support Library`

**2. Install the driver to access your android device**

Windows:

* Install the package `Extras/Google USB Driver` from the Android SDK Manager

Note for Windows users:

Some smartphone vendors use dedicated programs for their smartphones. One of those vendors is `Samsung` with its `Kies` software. If the build script shouldn't find your android device check if your smartphone vendor uses any dedicated software and use it to install additional drivers.

Linux:

1. Setup udev rules so that your linux distribution recognises a connected android device

    * Install the adb tool (Android Debug Bridge) with your package management system
    * The package name should be something like `android-tools` or `android-tools-adb`
    * Reboot the computer to apply the udev rule

2. Set group permissions so the gradle build script can access a connected android device

    * For this you need to change the group of the adb executable in the android sdk folder which is used by the gradle build script to push the android app onto the device
    * Under /etc/group search for a group named `adb` or similar which was created with the installation of the adb tool package in step 1
    * The following instructions use the name `adb` for that group, use the one appropriate for you system
    * Change the group of the adb executable to `adb` so it can access your android device
        * cd $ANDROID_HOME/platform-tools
        * chgrp `adb` ./adb
    * Repeat this step each time you update the Platform-tools with the Android SDK Manager as it overwrites the executable

**3. Building the application**

To build, install, deploy and run the application on an android device connected to your computer or a running instance of an emulator execute the following gradle task:

* `appStart`

Using the commandline to build the application:

* Navigate to the project root folder
* Execute the gradle wrapper script with the gradle task from above
    * Windows: `./gradlew.bat appStart`
    * Linux / Mac: `./gradlew appStart`

**4. Signing the application**

**Signing:**

If you want to build a signed release of the application execute the `assembleRelease` task like this:

* ./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=`$KEYFILE` \
  -Pandroid.injected.signing.store.password=`$STORE_PASSWORD` \
  -Pandroid.injected.signing.key.alias=`$KEY_ALIAS` \
  -Pandroid.injected.signing.key.password=`$KEY_PASSWORD`

Parameters:

* `$KEYFILE` = path to the keystore file
* `$STORE_PASSWORD` = password of the keystore file
* `$KEY_ALIAS` = e-mail address for the google play store account
* `$KEY_PASSWORD` = password for the google play store account

**Aligning**

After signing the application you need to run the archive alignment tool `zipalign` to optimize the .apk file.

Align .apk file:

  * zipalign [-f] [-v] \<alignment\> infile.apk outfile.apk

Confirm alignment of .apk file:

  * zipalign -c -v \<alignment\> existing.apk

The \<alignment\> is an integer that defines the byte-alignment boundaries. This must always be 4 (which provides 32-bit alignment) or else it effectively does nothing.

Flags:

-f : overwrite existing outfile.zip
-v : verbose output
-c : confirm the alignment of the given file

## Android Studio ##

**Installation**

Android Studio:

Install Android Studio from [here](https://developer.android.com/studio/index.html#Other). You need to `download` the `ide without sdk tools`.

**Plugins**

Git:

  * For git support you need to download [git](http://www.git-scm.com/downloads). The bundled git plugin of Android Studio is only a graphical frontend and won't work without the underlying command line tool.
  * After the download open Android Studio, go to the preferences page of the git plugin and set the path to the git executable.