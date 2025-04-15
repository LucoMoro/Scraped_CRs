/*SDK Manager: fix parsing of empty AndroidVersion codename.

SDK Bug: 29952

To reproduce the bug, install any system image with the SDK
Manager and add the line "AndroidVersion.CodeName=" in the
source.properties. When the SDK Manager loads, it will
incorrectly flag the system image as "broken" because it
can't understand the empty codename.

This fixes it by sanitizing the codename when creating
an AndroidVersion and using that class when loading the
properties instead of hand checking the codename in
various places.

Change-Id:Ie4a02739e56f576c7644b5539697c943d0082aac*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 996aee4..38d3bbb 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib;

import com.android.sdklib.repository.PkgProps;

import java.util.Properties;
//Synthetic comment -- @@ -61,7 +62,7 @@
*/
public AndroidVersion(int apiLevel, String codename) {
mApiLevel = apiLevel;
        mCodename = codename;
}

/**
//Synthetic comment -- @@ -73,11 +74,12 @@
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
if (properties == null) {
mApiLevel = defaultApiLevel;
            mCodename = defaultCodeName;
} else {
mApiLevel = Integer.parseInt(properties.getProperty(PkgProps.VERSION_API_LEVEL,
                    Integer.toString(defaultApiLevel)));
            mCodename = properties.getProperty(PkgProps.VERSION_CODENAME, defaultCodeName);
}
}

//Synthetic comment -- @@ -95,7 +97,8 @@
if (apiLevel != null) {
try {
mApiLevel = Integer.parseInt(apiLevel);
                mCodename = properties.getProperty(PkgProps.VERSION_CODENAME, null/*defaultValue*/);
return;
} catch (NumberFormatException e) {
error = e;
//Synthetic comment -- @@ -298,4 +301,25 @@
public boolean isGreaterOrEqualThan(int api) {
return compareTo(api, null /*codename*/) >= 0;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersionTest.java
new file mode 100755
//Synthetic comment -- index 0000000..653d006

//Synthetic comment -- @@ -0,0 +1,71 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 02688c0..ac8de23 100644

//Synthetic comment -- @@ -53,8 +53,7 @@
*
* @param sdkOsPath the root folder of the SDK
* @param platformOSPath the root folder of the platform component
     * @param apiLevel the API Level
     * @param codeName the codename. can be null.
* @param versionName the version name of the platform.
* @param revision the revision of the platform component.
* @param layoutlibVersion The {@link LayoutlibVersion}. May be null.
//Synthetic comment -- @@ -65,8 +64,7 @@
PlatformTarget(
String sdkOsPath,
String platformOSPath,
            int apiLevel,
            String codeName,
String versionName,
int revision,
LayoutlibVersion layoutlibVersion,
//Synthetic comment -- @@ -77,7 +75,7 @@
}
mRootFolderOsPath = platformOSPath;
mProperties = Collections.unmodifiableMap(properties);
        mVersion = new AndroidVersion(apiLevel, codeName);
mVersionName = versionName;
mRevision = revision;
mLayoutlibVersion = layoutlibVersion;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 6e6c657..b5ff9da 100644

//Synthetic comment -- @@ -263,6 +263,9 @@
/** Name of the cache folder in the $HOME/.android. */
public final static String FD_CACHE = "cache";                      //$NON-NLS-1$

/** Namespace for the resource XML, i.e. "http://schemas.android.com/apk/res/android" */
public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$
//Synthetic comment -- @@ -377,7 +380,6 @@
FN_FRAMEWORK_RENDERSCRIPT + File.separator + FN_FRAMEWORK_INCLUDE_CLANG;

/* Folder paths relative to a addon folder */

/** Path of the images directory relative to a folder folder.
*  This is an OS path, ending with a separator. */
public final static String OS_ADDON_LIBS_FOLDER = FD_ADDON_LIBS + File.separator;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 8284054..5c570ce 100644

//Synthetic comment -- @@ -437,12 +437,10 @@
}
}

            // codename (optional)
            String apiCodename = platformProp.get(PROP_VERSION_CODENAME);
            if (apiCodename != null && apiCodename.equals("REL")) {
                apiCodename = null; // REL means it's a release version and therefore the
                                    // codename is irrelevant at this point.
            }

// version string
String apiName = platformProp.get(PkgProps.PLATFORM_VERSION);
//Synthetic comment -- @@ -489,14 +487,13 @@
}

ISystemImage[] systemImages =
                getPlatformSystemImages(sdkOsPath, platformFolder, apiNumber, apiCodename);

// create the target.
PlatformTarget target = new PlatformTarget(
sdkOsPath,
platformFolder.getAbsolutePath(),
                    apiNumber,
                    apiCodename,
apiName,
revision,
layoutlibVersion,
//Synthetic comment -- @@ -574,16 +571,14 @@
*
* @param sdkOsPath The path to the SDK.
* @param root Root of the platform target being loaded.
     * @param apiNumber API level of platform being loaded
     * @param apiCodename Optional codename of platform being loaded
* @return an array of ISystemImage containing all the system images for the target.
*              The list can be empty.
*/
private static ISystemImage[] getPlatformSystemImages(
String sdkOsPath,
File root,
            int apiNumber,
            String apiCodename) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();
Set<String> abiFound = new HashSet<String>();

//Synthetic comment -- @@ -592,8 +587,6 @@
// The actual directory names are irrelevant.
// If we find multiple occurrences of the same platform/abi, the first one read wins.

        AndroidVersion version = new AndroidVersion(apiNumber, apiCodename);

File[] firstLevelFiles = new File(sdkOsPath, SdkConstants.FD_SYSTEM_IMAGES).listFiles();
if (firstLevelFiles != null) {
for (File firstLevel : firstLevelFiles) {







