/*Update sdklib to use the new sdk organization (build tools moved)

Change-Id:I3f072bf7f848d63b90fa03bec2683f6a009deb88*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 4d912b7..21738fa 100644

//Synthetic comment -- @@ -43,12 +43,12 @@
private String[] mSkins;


    PlatformTarget(String sdkLocation, String platformLocation, Map<String, String> properties,
int apiLevel, String codeName, String versionName, int revision) {
        if (platformLocation.endsWith(File.separator) == false) {
            platformLocation = platformLocation + File.separator;
}
        mLocation = platformLocation;
mProperties = Collections.unmodifiableMap(properties);
mVersion = new AndroidVersion(apiLevel, codeName);
mVersionName = versionName;
//Synthetic comment -- @@ -85,12 +85,17 @@
SdkConstants.FN_INTENT_ACTIONS_SERVICE);
mPaths.put(CATEGORIES, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_INTENT_CATEGORIES);
mPaths.put(ANT, mLocation + SdkConstants.OS_PLATFORM_ANT_FOLDER);

        // location for aapt, aidl, dx is now in the platform-tools folder.
        mPaths.put(AAPT, sdkLocation + SdkConstants.OS_SDK_PLAT_TOOLS_FOLDER +
                SdkConstants.FN_AAPT);
        mPaths.put(AIDL, sdkLocation + SdkConstants.OS_SDK_PLAT_TOOLS_FOLDER +
                SdkConstants.FN_AIDL);
        mPaths.put(DX, sdkLocation + SdkConstants.OS_SDK_PLAT_TOOLS_FOLDER +
                SdkConstants.FN_DX);
        mPaths.put(DX_JAR, sdkLocation + SdkConstants.OS_SDK_PLAT_TOOLS_LIB_FOLDER +
                SdkConstants.FN_DX_JAR);
}

public String getLocation() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index c643d92..418eb35 100644

//Synthetic comment -- @@ -184,6 +184,8 @@
public final static String FD_ADDONS = "add-ons";
/** Name of the SDK tools folder. */
public final static String FD_TOOLS = "tools";
    /** Name of the Platform tools folder. */
    public final static String FD_PLAT_TOOLS = "platform-tools";
/** Name of the SDK tools/lib folder. */
public final static String FD_LIB = "lib";
/** Name of the SDK docs folder. */
//Synthetic comment -- @@ -231,6 +233,15 @@
public final static String OS_SDK_TOOLS_LIB_FOLDER =
OS_SDK_TOOLS_FOLDER + FD_LIB + File.separator;

    /** Path of the platform tools directory relative to the sdk folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLAT_TOOLS_FOLDER = FD_PLAT_TOOLS + File.separator;

    /** Path of the Platform tools Llib directory relative to the sdk folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLAT_TOOLS_LIB_FOLDER =
        OS_SDK_PLAT_TOOLS_FOLDER + FD_LIB + File.separator;

/* Folder paths relative to a platform or add-on folder */

/** Path of the images directory relative to a platform or addon folder.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 4249365..04d7dce 100644

//Synthetic comment -- @@ -69,10 +69,6 @@
private final static String[] sPlatformContentList = new String[] {
SdkConstants.FN_FRAMEWORK_LIBRARY,
SdkConstants.FN_FRAMEWORK_AIDL,
};

/** Preference file containing the usb ids for adb */
//Synthetic comment -- @@ -230,19 +226,19 @@

/**
* Loads the Platforms from the SDK.
     * @param sdkLocation Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static void loadPlatforms(String sdkLocation, ArrayList<IAndroidTarget> list,
ISdkLog log) {
        File platformFolder = new File(sdkLocation, SdkConstants.FD_PLATFORMS);
if (platformFolder.isDirectory()) {
File[] platforms  = platformFolder.listFiles();

for (File platform : platforms) {
if (platform.isDirectory()) {
                    PlatformTarget target = loadPlatform(sdkLocation, platform, log);
if (target != null) {
list.add(target);
}
//Synthetic comment -- @@ -267,11 +263,13 @@

/**
* Loads a specific Platform at a given location.
     * @param sdkLocation
     * @param platformLocation the location of the platform.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static PlatformTarget loadPlatform(String sdkLocation, File platformLocation,
            ISdkLog log) {
        File buildProp = new File(platformLocation, SdkConstants.FN_BUILD_PROP);

if (buildProp.isFile()) {
Map<String, String> map = ProjectProperties.parsePropertyFile(buildProp, log);
//Synthetic comment -- @@ -282,9 +280,9 @@
// version string
String apiName = map.get(PROP_VERSION_RELEASE);
if (apiName == null) {
                    log.warning(
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platformLocation.getName(), PROP_VERSION_RELEASE,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -293,9 +291,9 @@
int apiNumber;
String stringValue = map.get(PROP_VERSION_SDK);
if (stringValue == null) {
                    log.warning(
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platformLocation.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
} else {
//Synthetic comment -- @@ -304,9 +302,9 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this platform.
                        log.warning(
"Ignoring platform '%1$s': %2$s is not a valid number in %3$s.",
                                platformLocation.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -321,7 +319,7 @@

// platform rev number
int revision = 1;
                File sourcePropFile = new File(platformLocation, SdkConstants.FN_SOURCE_PROP);
Map<String, String> sourceProp = ProjectProperties.parsePropertyFile(sourcePropFile,
log);
if (sourceProp != null) {
//Synthetic comment -- @@ -334,19 +332,20 @@
}

// Ant properties
                File sdkPropFile = new File(platformLocation, SdkConstants.FN_SDK_PROP);
Map<String, String> antProp = ProjectProperties.parsePropertyFile(sdkPropFile, log);
if (antProp != null) {
map.putAll(antProp);
}

// api number and name look valid, perform a few more checks
                if (checkPlatformContent(platformLocation, log) == false) {
return null;
}
// create the target.
PlatformTarget target = new PlatformTarget(
                        sdkLocation,
                        platformLocation.getAbsolutePath(),
map,
apiNumber,
apiCodename,
//Synthetic comment -- @@ -360,7 +359,7 @@
return target;
}
} else {
            log.warning("Ignoring platform '%1$s': %2$s is missing.", platformLocation.getName(),
SdkConstants.FN_BUILD_PROP);
}

//Synthetic comment -- @@ -448,7 +447,7 @@

if (baseTarget == null) {
// Ignore this add-on.
                        log.warning(
"Ignoring add-on '%1$s': Unable to find base platform with API level '%2$s'",
addon.getName(), api);
return null;
//Synthetic comment -- @@ -470,7 +469,7 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this add-on.
                        log.warning(
"Ignoring add-on '%1$s': %2$s is not a valid number in %3$s.",
addon.getName(), ADDON_REVISION, SdkConstants.FN_BUILD_PROP);
return null;
//Synthetic comment -- @@ -501,12 +500,12 @@
libMap.put(libName, new String[] {
m.group(1), m.group(2) });
} else {
                                        log.warning(
"Ignoring library '%1$s', property value has wrong format\n\t%2$s",
libName, libData);
}
} else {
                                    log.warning(
"Ignoring library '%1$s', missing property value",
libName, libData);
}
//Synthetic comment -- @@ -542,7 +541,7 @@
return target;
}
} else {
            log.warning("Ignoring add-on '%1$s': %2$s is missing.", addon.getName(),
SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -577,8 +576,9 @@
* @param addonName The addon name, for display.
* @param valueName The missing manifest value, for display.
*/
    private static void displayAddonManifestWarning(ISdkLog log, String addonName,
            String valueName) {
        log.warning("Ignoring add-on '%1$s': '%2$s' is missing from %3$s.",
addonName, valueName, SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -595,7 +595,7 @@
for (String relativePath : sPlatformContentList) {
File f = new File(platform, relativePath);
if (!f.exists()) {
                log.warning(
"Ignoring platform '%1$s': %2$s is missing.",
platform.getName(), relativePath);
return false;







