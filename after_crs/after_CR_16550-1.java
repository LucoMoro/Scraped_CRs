/*Use File instead of string concatenation to build paths.

This prevent the case where the root folder isn't terminated
with a file separator.

Using the File object is much safer.

Change-Id:I63f3e81e902a419d7906c49ee9ae1eb5eff70994*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index c66b956..ee44d37 100644

//Synthetic comment -- @@ -205,7 +205,7 @@
}

if (antBuildVersion < 2) {
            // these older rules are obsolete, and not versioned, and therefore it's hard
// to maintain compatibility.

// if the platform itself is obsolete, display a different warning








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index f3da39c..530ce90 100644

//Synthetic comment -- @@ -161,9 +161,9 @@
public String getPath(int pathId) {
switch (pathId) {
case IMAGES:
                return new File(mLocation, SdkConstants.FD_IMAGES).getAbsolutePath();
case SKINS:
                return new File(mLocation, SdkConstants.FD_SKINS).getAbsolutePath();
case DOCS:
return mLocation + SdkConstants.FD_DOCS + File.separator
+ SdkConstants.FD_DOCS_REFERENCE;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 61c59c7..ec9153c 100644

//Synthetic comment -- @@ -72,42 +72,78 @@
}

// pre-build the path to the platform components
        mPaths.put(ANDROID_JAR,
                new File(mRootFolderOsPath, SdkConstants.FN_FRAMEWORK_LIBRARY).getAbsolutePath());

        mPaths.put(SOURCES,
                new File(mRootFolderOsPath, SdkConstants.FD_ANDROID_SOURCES).getAbsolutePath());

        mPaths.put(ANDROID_AIDL,
                new File(mRootFolderOsPath, SdkConstants.FN_FRAMEWORK_AIDL).getAbsolutePath());

        mPaths.put(IMAGES,
                new File(mRootFolderOsPath, SdkConstants.FD_IMAGES).getAbsolutePath());

        mPaths.put(SAMPLES,
                new File(mRootFolderOsPath, SdkConstants.FD_SAMPLES).getAbsolutePath());

        mPaths.put(SKINS,
                new File(mRootFolderOsPath, SdkConstants.FD_SKINS).getAbsolutePath());

        mPaths.put(TEMPLATES,
                new File(mRootFolderOsPath, SdkConstants.FD_TEMPLATES).getAbsolutePath());

        mPaths.put(DATA,
                new File(mRootFolderOsPath, SdkConstants.FD_DATA).getAbsolutePath());

        mPaths.put(ATTRIBUTES,
                new File(mRootFolderOsPath, SdkConstants.OS_PLATFORM_ATTRS_XML).getAbsolutePath());

mPaths.put(MANIFEST_ATTRIBUTES,
                new File(mRootFolderOsPath,
                        SdkConstants.OS_PLATFORM_ATTRS_MANIFEST_XML).getAbsolutePath());

        mPaths.put(RESOURCES,
                new File(mRootFolderOsPath,
                        SdkConstants.OS_PLATFORM_RESOURCES_FOLDER).getAbsolutePath());

        mPaths.put(FONTS,
                new File(mRootFolderOsPath,
                        SdkConstants.OS_PLATFORM_FONTS_FOLDER).getAbsolutePath());

        File dataFolder = new File(mRootFolderOsPath, SdkConstants.FD_DATA);

        mPaths.put(LAYOUT_LIB,
                new File(dataFolder, SdkConstants.FN_LAYOUTLIB_JAR).getAbsolutePath());

        mPaths.put(WIDGETS,
                new File(dataFolder, SdkConstants.FN_WIDGETS).getAbsolutePath());

        mPaths.put(ACTIONS_ACTIVITY,
                new File(dataFolder, SdkConstants.FN_INTENT_ACTIONS_ACTIVITY).getAbsolutePath());

        mPaths.put(ACTIONS_BROADCAST,
                new File(dataFolder, SdkConstants.FN_INTENT_ACTIONS_BROADCAST).getAbsolutePath());

        mPaths.put(ACTIONS_SERVICE,
                new File(dataFolder, SdkConstants.FN_INTENT_ACTIONS_SERVICE).getAbsolutePath());

        mPaths.put(CATEGORIES,
                new File(dataFolder, SdkConstants.FN_INTENT_CATEGORIES).getAbsolutePath());

        mPaths.put(ANT,
                new File(mRootFolderOsPath, SdkConstants.FD_ANT).getAbsolutePath());

// location for aapt, aidl, dx is now in the platform-tools folder.
        File platformToolsFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORM_TOOLS);

        mPaths.put(AAPT, new File(platformToolsFolder, SdkConstants.FN_AAPT).getAbsolutePath());
        mPaths.put(AIDL, new File(platformToolsFolder, SdkConstants.FN_AIDL).getAbsolutePath());
        mPaths.put(DX, new File(platformToolsFolder, SdkConstants.FN_DX).getAbsolutePath());
        mPaths.put(DX_JAR,
                new File(
                        new File(platformToolsFolder, SdkConstants.FD_LIB),
                        SdkConstants.FN_DX_JAR).getAbsolutePath());
}

public String getLocation() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index c22bbba..32a08d4 100644

//Synthetic comment -- @@ -237,31 +237,12 @@
*  This is an OS path, ending with a separator. */
public final static String OS_SDK_PLATFORM_TOOLS_FOLDER = FD_PLATFORM_TOOLS + File.separator;

/* Folder paths relative to a Platform folder */

/** Path of the data directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_DATA_FOLDER = FD_DATA + File.separator;

/** Path of the resources directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_RESOURCES_FOLDER =
//Synthetic comment -- @@ -276,14 +257,6 @@
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_SOURCES_FOLDER = FD_ANDROID_SOURCES + File.separator;

/** Path of the attrs.xml file relative to a platform folder. */
public final static String OS_PLATFORM_ATTRS_XML =
OS_PLATFORM_RESOURCES_FOLDER + FD_VALUES + File.separator + FN_ATTRS_XML;







