/*Update sdklib to use the new sdk organization (build tools moved)

Also do some minor clean-up from the recent change to ProjectProperties.
FileWrapper extends java.io.File so there's no need to create a File
and then create a FileWrapper around it. Directly create a FileWrapper.

Change-Id:I3f072bf7f848d63b90fa03bec2683f6a009deb88*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 1bdd2fa..222cd40 100644

//Synthetic comment -- @@ -925,11 +925,11 @@

// get the hardware properties for this skin
File skinFolder = avdManager.getSkinPath(skin, target);
                        File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
skinHardwareConfig = ProjectProperties.parsePropertyFile(
                                    new FileWrapper(skinHardwareFile),
                                    mSdkLog);
}
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 4d912b7..61c59c7 100644

//Synthetic comment -- @@ -33,7 +33,8 @@
private final static String PLATFORM_NAME = "Android %s";
private final static String PLATFORM_NAME_PREVIEW = "Android %s (Preview)";

    private final String mLocation;
private final String mName;
private final AndroidVersion mVersion;
private final String mVersionName;
//Synthetic comment -- @@ -43,12 +44,22 @@
private String[] mSkins;


    PlatformTarget(String location, Map<String, String> properties,
int apiLevel, String codeName, String versionName, int revision) {
        if (location.endsWith(File.separator) == false) {
            location = location + File.separator;
}
        mLocation = location;
mProperties = Collections.unmodifiableMap(properties);
mVersion = new AndroidVersion(apiLevel, codeName);
mVersionName = versionName;
//Synthetic comment -- @@ -61,40 +72,46 @@
}

// pre-build the path to the platform components
        mPaths.put(ANDROID_JAR, mLocation + SdkConstants.FN_FRAMEWORK_LIBRARY);
        mPaths.put(SOURCES, mLocation + SdkConstants.FD_ANDROID_SOURCES);
        mPaths.put(ANDROID_AIDL, mLocation + SdkConstants.FN_FRAMEWORK_AIDL);
        mPaths.put(IMAGES, mLocation + SdkConstants.OS_IMAGES_FOLDER);
        mPaths.put(SAMPLES, mLocation + SdkConstants.OS_PLATFORM_SAMPLES_FOLDER);
        mPaths.put(SKINS, mLocation + SdkConstants.OS_SKINS_FOLDER);
        mPaths.put(TEMPLATES, mLocation + SdkConstants.OS_PLATFORM_TEMPLATES_FOLDER);
        mPaths.put(DATA, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER);
        mPaths.put(ATTRIBUTES, mLocation + SdkConstants.OS_PLATFORM_ATTRS_XML);
        mPaths.put(MANIFEST_ATTRIBUTES, mLocation + SdkConstants.OS_PLATFORM_ATTRS_MANIFEST_XML);
        mPaths.put(RESOURCES, mLocation + SdkConstants.OS_PLATFORM_RESOURCES_FOLDER);
        mPaths.put(FONTS, mLocation + SdkConstants.OS_PLATFORM_FONTS_FOLDER);
        mPaths.put(LAYOUT_LIB, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_LAYOUTLIB_JAR);
        mPaths.put(WIDGETS, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_WIDGETS);
        mPaths.put(ACTIONS_ACTIVITY, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_INTENT_ACTIONS_ACTIVITY);
        mPaths.put(ACTIONS_BROADCAST, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_INTENT_ACTIONS_BROADCAST);
        mPaths.put(ACTIONS_SERVICE, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_INTENT_ACTIONS_SERVICE);
        mPaths.put(CATEGORIES, mLocation + SdkConstants.OS_PLATFORM_DATA_FOLDER +
SdkConstants.FN_INTENT_CATEGORIES);
        mPaths.put(AAPT, mLocation + SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_AAPT);
        mPaths.put(AIDL, mLocation + SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_AIDL);
        mPaths.put(DX, mLocation + SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_DX);
        mPaths.put(DX_JAR, mLocation + SdkConstants.OS_SDK_TOOLS_LIB_FOLDER +
SdkConstants.FN_DX_JAR);
        mPaths.put(ANT, mLocation + SdkConstants.OS_PLATFORM_ANT_FOLDER);
}

public String getLocation() {
        return mLocation;
}

/*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 43930a1..c22bbba 100644

//Synthetic comment -- @@ -233,6 +233,15 @@
public final static String OS_SDK_TOOLS_LIB_FOLDER =
OS_SDK_TOOLS_FOLDER + FD_LIB + File.separator;

/* Folder paths relative to a platform or add-on folder */

/** Path of the images directory relative to a platform or addon folder.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 9c90817..ad9e390 100644

//Synthetic comment -- @@ -70,10 +70,6 @@
private final static String[] sPlatformContentList = new String[] {
SdkConstants.FN_FRAMEWORK_LIBRARY,
SdkConstants.FN_FRAMEWORK_AIDL,
        SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_AAPT,
        SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_AIDL,
        SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_DX,
        SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + SdkConstants.FN_DX_JAR,
};

/** Preference file containing the usb ids for adb */
//Synthetic comment -- @@ -231,19 +227,19 @@

/**
* Loads the Platforms from the SDK.
     * @param location Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static void loadPlatforms(String location, ArrayList<IAndroidTarget> list,
ISdkLog log) {
        File platformFolder = new File(location, SdkConstants.FD_PLATFORMS);
if (platformFolder.isDirectory()) {
File[] platforms  = platformFolder.listFiles();

for (File platform : platforms) {
if (platform.isDirectory()) {
                    PlatformTarget target = loadPlatform(platform, log);
if (target != null) {
list.add(target);
}
//Synthetic comment -- @@ -268,16 +264,16 @@

/**
* Loads a specific Platform at a given location.
     * @param platform the location of the platform.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static PlatformTarget loadPlatform(File platform, ISdkLog log) {
        File buildProp = new File(platform, SdkConstants.FN_BUILD_PROP);

if (buildProp.isFile()) {
            Map<String, String> map = ProjectProperties.parsePropertyFile(
                    new FileWrapper(buildProp),
                    log);

if (map != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -285,9 +281,9 @@
// version string
String apiName = map.get(PROP_VERSION_RELEASE);
if (apiName == null) {
                    log.warning(null,
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platform.getName(), PROP_VERSION_RELEASE,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -296,9 +292,9 @@
int apiNumber;
String stringValue = map.get(PROP_VERSION_SDK);
if (stringValue == null) {
                    log.warning(null,
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platform.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
} else {
//Synthetic comment -- @@ -307,9 +303,9 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this platform.
                        log.warning(null,
"Ignoring platform '%1$s': %2$s is not a valid number in %3$s.",
                                platform.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -324,10 +320,10 @@

// platform rev number
int revision = 1;
                File sourcePropFile = new File(platform, SdkConstants.FN_SOURCE_PROP);
Map<String, String> sourceProp = ProjectProperties.parsePropertyFile(
                        new FileWrapper(sourcePropFile),
                        log);
if (sourceProp != null) {
try {
revision = Integer.parseInt(sourceProp.get("Pkg.Revision"));
//Synthetic comment -- @@ -338,21 +334,23 @@
}

// Ant properties
                File sdkPropFile = new File(platform, SdkConstants.FN_SDK_PROP);
Map<String, String> antProp = ProjectProperties.parsePropertyFile(
                        new FileWrapper(sdkPropFile),
                        log);
if (antProp != null) {
map.putAll(antProp);
}

// api number and name look valid, perform a few more checks
                if (checkPlatformContent(platform, log) == false) {
return null;
}
// create the target.
PlatformTarget target = new PlatformTarget(
                        platform.getAbsolutePath(),
map,
apiNumber,
apiCodename,
//Synthetic comment -- @@ -366,7 +364,7 @@
return target;
}
} else {
            log.warning(null, "Ignoring platform '%1$s': %2$s is missing.", platform.getName(),
SdkConstants.FN_BUILD_PROP);
}

//Synthetic comment -- @@ -417,12 +415,11 @@
*/
private static AddOnTarget loadAddon(File addon, ArrayList<IAndroidTarget> targetList,
ISdkLog log) {
        File addOnManifest = new File(addon, SdkConstants.FN_MANIFEST_INI);

if (addOnManifest.isFile()) {
Map<String, String> propertyMap = ProjectProperties.parsePropertyFile(
                    new FileWrapper(addOnManifest),
                    log);

if (propertyMap != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -455,7 +452,7 @@

if (baseTarget == null) {
// Ignore this add-on.
                        log.warning(null,
"Ignoring add-on '%1$s': Unable to find base platform with API level '%2$s'",
addon.getName(), api);
return null;
//Synthetic comment -- @@ -477,7 +474,7 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this add-on.
                        log.warning(null,
"Ignoring add-on '%1$s': %2$s is not a valid number in %3$s.",
addon.getName(), ADDON_REVISION, SdkConstants.FN_BUILD_PROP);
return null;
//Synthetic comment -- @@ -508,12 +505,12 @@
libMap.put(libName, new String[] {
m.group(1), m.group(2) });
} else {
                                        log.warning(null,
"Ignoring library '%1$s', property value has wrong format\n\t%2$s",
libName, libData);
}
} else {
                                    log.warning(null,
"Ignoring library '%1$s', missing property value",
libName, libData);
}
//Synthetic comment -- @@ -549,7 +546,7 @@
return target;
}
} else {
            log.warning(null, "Ignoring add-on '%1$s': %2$s is missing.", addon.getName(),
SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -584,8 +581,9 @@
* @param addonName The addon name, for display.
* @param valueName The missing manifest value, for display.
*/
    private static void displayAddonManifestWarning(ISdkLog log, String addonName, String valueName) {
        log.warning(null, "Ignoring add-on '%1$s': '%2$s' is missing from %3$s.",
addonName, valueName, SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -602,7 +600,7 @@
for (String relativePath : sPlatformContentList) {
File f = new File(platform, relativePath);
if (!f.exists()) {
                log.warning(null,
"Ignoring platform '%1$s': %2$s is missing.",
platform.getName(), relativePath);
return false;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 273eab3..963fc3d 100644

//Synthetic comment -- @@ -690,11 +690,12 @@

HashMap<String, String> finalHardwareValues = new HashMap<String, String>();

            File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(targetHardwareFile),
                        log);
if (targetHardwareConfig != null) {
finalHardwareValues.putAll(targetHardwareConfig);
values.putAll(targetHardwareConfig);
//Synthetic comment -- @@ -703,11 +704,11 @@

// get the hardware properties for this skin
File skinFolder = getSkinPath(skinName, target);
            File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(skinHardwareFile),
                        log);
if (skinHardwareConfig != null) {
finalHardwareValues.putAll(skinHardwareConfig);
values.putAll(skinHardwareConfig);
//Synthetic comment -- @@ -1152,7 +1153,7 @@
String targetHash = map.get(AVD_INFO_TARGET);

IAndroidTarget target = null;
        File configIniFile = null;
Map<String, String> properties = null;

if (targetHash != null) {
//Synthetic comment -- @@ -1161,16 +1162,14 @@

// load the AVD properties.
if (avdPath != null) {
            configIniFile = new File(avdPath, CONFIG_INI);
}

if (configIniFile != null) {
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = ProjectProperties.parsePropertyFile(
                        new FileWrapper(configIniFile),
                        log);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index cdb8054..f32279c 100644

//Synthetic comment -- @@ -876,11 +876,12 @@
// (if applicable)
HashMap<String, String> hardwareValues = new HashMap<String, String>();
if (target.isPlatform() == false) {
            File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(targetHardwareFile),
                        null /*log*/);
if (targetHardwareConfig != null) {
hardwareValues.putAll(targetHardwareConfig);
}
//Synthetic comment -- @@ -888,11 +889,11 @@
}

// from the skin
        File skinHardwareFile = new File(skin, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                    new FileWrapper(skinHardwareFile),
                    null /*log*/);
if (skinHardwareConfig != null) {
hardwareValues.putAll(skinHardwareConfig);
}







