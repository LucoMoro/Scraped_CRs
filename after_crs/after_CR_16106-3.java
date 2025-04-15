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
                        FileWrapper skinHardwareFile = new FileWrapper(skinFolder,
                                AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
skinHardwareConfig = ProjectProperties.parsePropertyFile(
                                    skinHardwareFile, mSdkLog);
}
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 4d912b7..61c59c7 100644

//Synthetic comment -- @@ -33,7 +33,8 @@
private final static String PLATFORM_NAME = "Android %s";
private final static String PLATFORM_NAME_PREVIEW = "Android %s (Preview)";

    /** the OS path to the root folder of the platform component. */
    private final String mRootFolderOsPath;
private final String mName;
private final AndroidVersion mVersion;
private final String mVersionName;
//Synthetic comment -- @@ -43,12 +44,22 @@
private String[] mSkins;


    /**
     * Creates a Platform target.
     * @param sdkOsPath the root folder of the SDK
     * @param platformOSPath the root folder of the platform component
     * @param properties the platform properties
     * @param apiLevel the API Level
     * @param codeName the codename. can be null.
     * @param versionName the version name of the platform.
     * @param revision the revision of the platform component.
     */
    PlatformTarget(String sdkOsPath, String platformOSPath, Map<String, String> properties,
int apiLevel, String codeName, String versionName, int revision) {
        if (platformOSPath.endsWith(File.separator) == false) {
            platformOSPath = platformOSPath + File.separator;
}
        mRootFolderOsPath = platformOSPath;
mProperties = Collections.unmodifiableMap(properties);
mVersion = new AndroidVersion(apiLevel, codeName);
mVersionName = versionName;
//Synthetic comment -- @@ -61,40 +72,46 @@
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
        return mRootFolderOsPath;
}

/*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 43930a1..c22bbba 100644

//Synthetic comment -- @@ -233,6 +233,15 @@
public final static String OS_SDK_TOOLS_LIB_FOLDER =
OS_SDK_TOOLS_FOLDER + FD_LIB + File.separator;

    /** Path of the platform tools directory relative to the sdk folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLATFORM_TOOLS_FOLDER = FD_PLATFORM_TOOLS + File.separator;

    /** Path of the Platform tools Llib directory relative to the sdk folder.
     *  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLATFORM_TOOLS_LIB_FOLDER =
            OS_SDK_PLATFORM_TOOLS_FOLDER + FD_LIB + File.separator;

/* Folder paths relative to a platform or add-on folder */

/** Path of the images directory relative to a platform or addon folder.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 9c90817..ad9e390 100644

//Synthetic comment -- @@ -70,10 +70,6 @@
private final static String[] sPlatformContentList = new String[] {
SdkConstants.FN_FRAMEWORK_LIBRARY,
SdkConstants.FN_FRAMEWORK_AIDL,
};

/** Preference file containing the usb ids for adb */
//Synthetic comment -- @@ -231,19 +227,19 @@

/**
* Loads the Platforms from the SDK.
     * @param sdkOsPath Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list,
ISdkLog log) {
        File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
if (platformFolder.isDirectory()) {
File[] platforms  = platformFolder.listFiles();

for (File platform : platforms) {
if (platform.isDirectory()) {
                    PlatformTarget target = loadPlatform(sdkOsPath, platform, log);
if (target != null) {
list.add(target);
}
//Synthetic comment -- @@ -268,16 +264,16 @@

/**
* Loads a specific Platform at a given location.
     * @param sdkOsPath Location of the SDK
     * @param platformFolder the root folder of the platform.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static PlatformTarget loadPlatform(String sdkOsPath, File platformFolder,
            ISdkLog log) {
        FileWrapper buildProp = new FileWrapper(platformFolder, SdkConstants.FN_BUILD_PROP);

if (buildProp.isFile()) {
            Map<String, String> map = ProjectProperties.parsePropertyFile(buildProp, log);

if (map != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -285,9 +281,9 @@
// version string
String apiName = map.get(PROP_VERSION_RELEASE);
if (apiName == null) {
                    log.warning(
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platformFolder.getName(), PROP_VERSION_RELEASE,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -296,9 +292,9 @@
int apiNumber;
String stringValue = map.get(PROP_VERSION_SDK);
if (stringValue == null) {
                    log.warning(
"Ignoring platform '%1$s': %2$s is missing from '%3$s'",
                            platformFolder.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
} else {
//Synthetic comment -- @@ -307,9 +303,9 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this platform.
                        log.warning(
"Ignoring platform '%1$s': %2$s is not a valid number in %3$s.",
                                platformFolder.getName(), PROP_VERSION_SDK,
SdkConstants.FN_BUILD_PROP);
return null;
}
//Synthetic comment -- @@ -324,10 +320,10 @@

// platform rev number
int revision = 1;
                FileWrapper sourcePropFile = new FileWrapper(platformFolder,
                        SdkConstants.FN_SOURCE_PROP);
Map<String, String> sourceProp = ProjectProperties.parsePropertyFile(
                        sourcePropFile, log);
if (sourceProp != null) {
try {
revision = Integer.parseInt(sourceProp.get("Pkg.Revision"));
//Synthetic comment -- @@ -338,21 +334,23 @@
}

// Ant properties
                FileWrapper sdkPropFile = new FileWrapper(platformFolder, SdkConstants.FN_SDK_PROP);
Map<String, String> antProp = ProjectProperties.parsePropertyFile(
                        sdkPropFile, log);

if (antProp != null) {
map.putAll(antProp);
}

// api number and name look valid, perform a few more checks
                if (checkPlatformContent(platformFolder, log) == false) {
return null;
}

// create the target.
PlatformTarget target = new PlatformTarget(
                        sdkOsPath,
                        platformFolder.getAbsolutePath(),
map,
apiNumber,
apiCodename,
//Synthetic comment -- @@ -366,7 +364,7 @@
return target;
}
} else {
            log.warning("Ignoring platform '%1$s': %2$s is missing.", platformFolder.getName(),
SdkConstants.FN_BUILD_PROP);
}

//Synthetic comment -- @@ -417,12 +415,11 @@
*/
private static AddOnTarget loadAddon(File addon, ArrayList<IAndroidTarget> targetList,
ISdkLog log) {
        FileWrapper addOnManifest = new FileWrapper(addon, SdkConstants.FN_MANIFEST_INI);

if (addOnManifest.isFile()) {
Map<String, String> propertyMap = ProjectProperties.parsePropertyFile(
                    addOnManifest, log);

if (propertyMap != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -455,7 +452,7 @@

if (baseTarget == null) {
// Ignore this add-on.
                        log.warning(
"Ignoring add-on '%1$s': Unable to find base platform with API level '%2$s'",
addon.getName(), api);
return null;
//Synthetic comment -- @@ -477,7 +474,7 @@
} catch (NumberFormatException e) {
// looks like apiNumber does not parse to a number.
// Ignore this add-on.
                        log.warning(
"Ignoring add-on '%1$s': %2$s is not a valid number in %3$s.",
addon.getName(), ADDON_REVISION, SdkConstants.FN_BUILD_PROP);
return null;
//Synthetic comment -- @@ -508,12 +505,12 @@
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
//Synthetic comment -- @@ -549,7 +546,7 @@
return target;
}
} else {
            log.warning("Ignoring add-on '%1$s': %2$s is missing.", addon.getName(),
SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -584,8 +581,9 @@
* @param addonName The addon name, for display.
* @param valueName The missing manifest value, for display.
*/
    private static void displayAddonManifestWarning(ISdkLog log, String addonName,
            String valueName) {
        log.warning("Ignoring add-on '%1$s': '%2$s' is missing from %3$s.",
addonName, valueName, SdkConstants.FN_MANIFEST_INI);
}

//Synthetic comment -- @@ -602,7 +600,7 @@
for (String relativePath : sPlatformContentList) {
File f = new File(platform, relativePath);
if (!f.exists()) {
                log.warning(
"Ignoring platform '%1$s': %2$s is missing.",
platform.getName(), relativePath);
return false;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 273eab3..963fc3d 100644

//Synthetic comment -- @@ -690,11 +690,12 @@

HashMap<String, String> finalHardwareValues = new HashMap<String, String>();

            FileWrapper targetHardwareFile = new FileWrapper(target.getLocation(),
                    AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        targetHardwareFile, log);

if (targetHardwareConfig != null) {
finalHardwareValues.putAll(targetHardwareConfig);
values.putAll(targetHardwareConfig);
//Synthetic comment -- @@ -703,11 +704,11 @@

// get the hardware properties for this skin
File skinFolder = getSkinPath(skinName, target);
            FileWrapper skinHardwareFile = new FileWrapper(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                        skinHardwareFile, log);

if (skinHardwareConfig != null) {
finalHardwareValues.putAll(skinHardwareConfig);
values.putAll(skinHardwareConfig);
//Synthetic comment -- @@ -1152,7 +1153,7 @@
String targetHash = map.get(AVD_INFO_TARGET);

IAndroidTarget target = null;
        FileWrapper configIniFile = null;
Map<String, String> properties = null;

if (targetHash != null) {
//Synthetic comment -- @@ -1161,16 +1162,14 @@

// load the AVD properties.
if (avdPath != null) {
            configIniFile = new FileWrapper(avdPath, CONFIG_INI);
}

if (configIniFile != null) {
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = ProjectProperties.parsePropertyFile(configIniFile, log);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index cdb8054..f32279c 100644

//Synthetic comment -- @@ -876,11 +876,12 @@
// (if applicable)
HashMap<String, String> hardwareValues = new HashMap<String, String>();
if (target.isPlatform() == false) {
            FileWrapper targetHardwareFile = new FileWrapper(target.getLocation(),
                    AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        targetHardwareFile, null /*log*/);

if (targetHardwareConfig != null) {
hardwareValues.putAll(targetHardwareConfig);
}
//Synthetic comment -- @@ -888,11 +889,11 @@
}

// from the skin
        FileWrapper skinHardwareFile = new FileWrapper(skin, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                    skinHardwareFile, null /*log*/);

if (skinHardwareConfig != null) {
hardwareValues.putAll(skinHardwareConfig);
}







