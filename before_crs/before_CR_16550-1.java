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
            // these older rules are obselete, and not versioned, and therefore it's hard
// to maintain compatibility.

// if the platform itself is obsolete, display a different warning








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index f3da39c..530ce90 100644

//Synthetic comment -- @@ -161,9 +161,9 @@
public String getPath(int pathId) {
switch (pathId) {
case IMAGES:
                return mLocation + SdkConstants.OS_IMAGES_FOLDER;
case SKINS:
                return mLocation + SdkConstants.OS_SKINS_FOLDER;
case DOCS:
return mLocation + SdkConstants.FD_DOCS + File.separator
+ SdkConstants.FD_DOCS_REFERENCE;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 61c59c7..ec9153c 100644

//Synthetic comment -- @@ -72,42 +72,78 @@
}

// pre-build the path to the platform components
        mPaths.put(ANDROID_JAR, mRootFolderOsPath + SdkConstants.FN_FRAMEWORK_LIBRARY);
        mPaths.put(SOURCES, mRootFolderOsPath + SdkConstants.FD_ANDROID_SOURCES);
        mPaths.put(ANDROID_AIDL, mRootFolderOsPath + SdkConstants.FN_FRAMEWORK_AIDL);
        mPaths.put(IMAGES, mRootFolderOsPath + SdkConstants.OS_IMAGES_FOLDER);
        mPaths.put(SAMPLES, mRootFolderOsPath + SdkConstants.OS_PLATFORM_SAMPLES_FOLDER);
        mPaths.put(SKINS, mRootFolderOsPath + SdkConstants.OS_SKINS_FOLDER);
        mPaths.put(TEMPLATES, mRootFolderOsPath + SdkConstants.OS_PLATFORM_TEMPLATES_FOLDER);
        mPaths.put(DATA, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER);
        mPaths.put(ATTRIBUTES, mRootFolderOsPath + SdkConstants.OS_PLATFORM_ATTRS_XML);
mPaths.put(MANIFEST_ATTRIBUTES,
                mRootFolderOsPath + SdkConstants.OS_PLATFORM_ATTRS_MANIFEST_XML);
        mPaths.put(RESOURCES, mRootFolderOsPath + SdkConstants.OS_PLATFORM_RESOURCES_FOLDER);
        mPaths.put(FONTS, mRootFolderOsPath + SdkConstants.OS_PLATFORM_FONTS_FOLDER);
        mPaths.put(LAYOUT_LIB, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_LAYOUTLIB_JAR);
        mPaths.put(WIDGETS, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_WIDGETS);
        mPaths.put(ACTIONS_ACTIVITY, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_INTENT_ACTIONS_ACTIVITY);
        mPaths.put(ACTIONS_BROADCAST, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_INTENT_ACTIONS_BROADCAST);
        mPaths.put(ACTIONS_SERVICE, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_INTENT_ACTIONS_SERVICE);
        mPaths.put(CATEGORIES, mRootFolderOsPath + SdkConstants.OS_PLATFORM_DATA_FOLDER +
                SdkConstants.FN_INTENT_CATEGORIES);
        mPaths.put(ANT, mRootFolderOsPath + SdkConstants.OS_PLATFORM_ANT_FOLDER);

// location for aapt, aidl, dx is now in the platform-tools folder.
        mPaths.put(AAPT, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER +
                SdkConstants.FN_AAPT);
        mPaths.put(AIDL, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER +
                SdkConstants.FN_AIDL);
        mPaths.put(DX, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER +
                SdkConstants.FN_DX);
        mPaths.put(DX_JAR, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_LIB_FOLDER +
                SdkConstants.FN_DX_JAR);
}

public String getLocation() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index c22bbba..32a08d4 100644

//Synthetic comment -- @@ -237,31 +237,12 @@
*  This is an OS path, ending with a separator. */
public final static String OS_SDK_PLATFORM_TOOLS_FOLDER = FD_PLATFORM_TOOLS + File.separator;

    /** Path of the Platform tools Llib directory relative to the sdk folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLATFORM_TOOLS_LIB_FOLDER =
            OS_SDK_PLATFORM_TOOLS_FOLDER + FD_LIB + File.separator;

    /* Folder paths relative to a platform or add-on folder */

    /** Path of the images directory relative to a platform or addon folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_IMAGES_FOLDER = FD_IMAGES + File.separator;

    /** Path of the skin directory relative to a platform or addon folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SKINS_FOLDER = FD_SKINS + File.separator;

/* Folder paths relative to a Platform folder */

/** Path of the data directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_DATA_FOLDER = FD_DATA + File.separator;

    /** Path of the samples directory relative to a platform folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_SAMPLES_FOLDER = FD_SAMPLES + File.separator;

/** Path of the resources directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_RESOURCES_FOLDER =
//Synthetic comment -- @@ -276,14 +257,6 @@
*  This is an OS path, ending with a separator. */
public final static String OS_PLATFORM_SOURCES_FOLDER = FD_ANDROID_SOURCES + File.separator;

    /** Path of the android templates directory relative to a platform folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_TEMPLATES_FOLDER = FD_TEMPLATES + File.separator;

    /** Path of the Ant build rules directory relative to a platform folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_ANT_FOLDER = FD_ANT + File.separator;

/** Path of the attrs.xml file relative to a platform folder. */
public final static String OS_PLATFORM_ATTRS_XML =
OS_PLATFORM_RESOURCES_FOLDER + FD_VALUES + File.separator + FN_ATTRS_XML;







