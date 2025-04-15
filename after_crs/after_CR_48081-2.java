/*Sort modifier in the canonical order in sdklib and manifmerger

Automated IDE refactoring; no other changes.

Change-Id:I56a1992a2871fd1947f7e0f41d6960230444c0e6*/




//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/ArgvParser.java b/manifmerger/src/main/java/com/android/manifmerger/ArgvParser.java
//Synthetic comment -- index 6d22f57..9c9e374 100755

//Synthetic comment -- @@ -37,7 +37,7 @@
*   or optional) for the given action.
*/

    public static final String VERB_MERGE       = "merge";                          //$NON-NLS-1$
public static final String KEY_OUT          = "out";                            //$NON-NLS-1$
public static final String KEY_MAIN         = "main";                           //$NON-NLS-1$
public static final String KEY_LIBS         = "libs";                           //$NON-NLS-1$
//Synthetic comment -- @@ -57,7 +57,7 @@
* <li> an alternate form for the object (e.g. plural).
* </ul>
*/
    private static final String[][] ACTIONS = {

{ VERB_MERGE, NO_VERB_OBJECT,
"Merge two or more manifests." },








//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/IMergerLog.java b/manifmerger/src/main/java/com/android/manifmerger/IMergerLog.java
//Synthetic comment -- index 5402dd4..fb231a4 100755

//Synthetic comment -- @@ -111,7 +111,8 @@
* {@link IMergerLog#MAIN_MANIFEST}.
* When that fails, null is used.
*/
        @Nullable
        public String getFileName() {
return mFilePath;
}









//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/ManifestMerger.java b/manifmerger/src/main/java/com/android/manifmerger/ManifestMerger.java
//Synthetic comment -- index 073c768..616c2aa 100755

//Synthetic comment -- @@ -1477,7 +1477,8 @@
* @param node The node or document where the error occurs. Must not be null.
* @return A new non-null {@link FileAndLine} combining the file name and line number.
*/
    @NonNull
    private FileAndLine xmlFileAndLine(@NonNull Node node) {
return MergerXmlUtils.xmlFileAndLine(node);
}









//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/MergerLog.java b/manifmerger/src/main/java/com/android/manifmerger/MergerLog.java
//Synthetic comment -- index 446898c..36ade15 100755

//Synthetic comment -- @@ -32,7 +32,7 @@
* @param sdkLog A non-null {@link ILogger}.
* @return A new IMergerLog.
*/
    public static IMergerLog wrapSdkLog(@NonNull final ILogger sdkLog) {
return new IMergerLog() {
@Override
public void error(
//Synthetic comment -- @@ -110,9 +110,9 @@
* @return A new IMergerLog.
*/
public static IMergerLog mergerLogOverrideLocation(
            @NonNull final IMergerLog parentLog,
            @Nullable final String filePath1,
            @Nullable final String... filePath2) {
return new IMergerLog() {
@Override
public void error(








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/AddOnTarget.java b/sdklib2/src/main/java/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index 12d4a49..fe3e3c3 100644

//Synthetic comment -- @@ -34,9 +34,9 @@
* String to compute hash for add-on targets.
* Format is vendor:name:apiVersion
* */
    private static final String ADD_ON_FORMAT = "%s:%s:%s"; //$NON-NLS-1$

    private static final class OptionalLibrary implements IOptionalLibrary {
private final String mJarName;
private final String mJarPath;
private final String mName;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/AndroidVersion.java b/sdklib2/src/main/java/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 44ffa63..2eb9cc0 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
* Thrown when an {@link AndroidVersion} object could not be created.
* @see AndroidVersion#AndroidVersion(Properties)
*/
    public static final class AndroidVersionException extends Exception {
private static final long serialVersionUID = 1L;

AndroidVersionException(String message, Throwable cause) {
//Synthetic comment -- @@ -314,7 +314,8 @@
* @param codename A possible-null codename.
* @return Null for a release version or a non-empty codename.
*/
    @Nullable
    private String sanitizeCodename(@Nullable String codename) {
if (codename != null) {
codename = codename.trim();
if (codename.length() == 0 || SdkConstants.CODENAME_RELEASE.equals(codename)) {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/IAndroidTarget.java b/sdklib2/src/main/java/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index 18577cf..b1d9228 100644

//Synthetic comment -- @@ -32,75 +32,75 @@
public static final String PLATFORM_HASH_PREFIX = "android-";

/** OS Path to the "android.jar" file. */
    public static final int ANDROID_JAR         = 1;
/** OS Path to the "framework.aidl" file. */
    public static final int ANDROID_AIDL        = 2;
/** OS Path to the "samples" folder which contains sample projects. */
    public static final int SAMPLES             = 4;
/** OS Path to the "skins" folder which contains the emulator skins. */
    public static final int SKINS               = 5;
/** OS Path to the "templates" folder which contains the templates for new projects. */
    public static final int TEMPLATES           = 6;
/** OS Path to the "data" folder which contains data & libraries for the SDK tools. */
    public static final int DATA                = 7;
/** OS Path to the "attrs.xml" file. */
    public static final int ATTRIBUTES          = 8;
/** OS Path to the "attrs_manifest.xml" file. */
    public static final int MANIFEST_ATTRIBUTES = 9;
/** OS Path to the "data/layoutlib.jar" library. */
    public static final int LAYOUT_LIB          = 10;
/** OS Path to the "data/res" folder. */
    public static final int RESOURCES           = 11;
/** OS Path to the "data/fonts" folder. */
    public static final int FONTS               = 12;
/** OS Path to the "data/widgets.txt" file. */
    public static final int WIDGETS             = 13;
/** OS Path to the "data/activity_actions.txt" file. */
    public static final int ACTIONS_ACTIVITY    = 14;
/** OS Path to the "data/broadcast_actions.txt" file. */
    public static final int ACTIONS_BROADCAST   = 15;
/** OS Path to the "data/service_actions.txt" file. */
    public static final int ACTIONS_SERVICE     = 16;
/** OS Path to the "data/categories.txt" file. */
    public static final int CATEGORIES          = 17;
/** OS Path to the "sources" folder. */
    public static final int SOURCES             = 18;
/** OS Path to the target specific docs */
    public static final int DOCS                = 19;
/** OS Path to the target's version of the aapt tool.
* This is deprecated as aapt is now in the platform tools and not in the platform. */
@Deprecated
    public static final int AAPT                = 20;
/** OS Path to the target's version of the aidl tool.
* This is deprecated as aidl is now in the platform tools and not in the platform. */
@Deprecated
    public static final int AIDL                = 21;
/** OS Path to the target's version of the dx too.<br>
* This is deprecated as dx is now in the platform tools and not in the platform. */
@Deprecated
    public static final int DX                  = 22;
/** OS Path to the target's version of the dx.jar file.<br>
* This is deprecated as dx.jar is now in the platform tools and not in the platform. */
@Deprecated
    public static final int DX_JAR              = 23;
/** OS Path to the "ant" folder which contains the ant build rules (ver 2 and above) */
    public static final int ANT                 = 24;
/** OS Path to the Renderscript include folder.
* This is deprecated as this is now in the platform tools and not in the platform. */
@Deprecated
    public static final int ANDROID_RS          = 25;
/** OS Path to the Renderscript(clang) include folder.
* This is deprecated as this is now in the platform tools and not in the platform. */
@Deprecated
    public static final int ANDROID_RS_CLANG    = 26;
/** OS Path to the "uiautomator.jar" file. */
    public static final int UI_AUTOMATOR_JAR    = 27;

/**
* Return value for {@link #getUsbVendorId()} meaning no USB vendor IDs are defined by the
* Android target.
*/
    public static final int NO_USB_ID = 0;

/** An optional library provided by an Android Target */
public interface IOptionalLibrary {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/PlatformTarget.java b/sdklib2/src/main/java/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 7c2b4aa..4852cfd 100644

//Synthetic comment -- @@ -30,12 +30,12 @@
*/
final class PlatformTarget implements IAndroidTarget {
/** String used to get a hash to the platform target */
    private static final String PLATFORM_HASH = "android-%s";

    private static final String PLATFORM_VENDOR = "Android Open Source Project";

    private static final String PLATFORM_NAME = "Android %s";
    private static final String PLATFORM_NAME_PREVIEW = "Android %s (Preview)";

/** the OS path to the root folder of the platform component. */
private final String mRootFolderOsPath;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/SdkManager.java b/sdklib2/src/main/java/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 0bca185..6839317 100644

//Synthetic comment -- @@ -65,39 +65,39 @@

private static final boolean DEBUG = System.getenv("SDKMAN_DEBUG") != null;        //$NON-NLS-1$

    public static final String PROP_VERSION_SDK = "ro.build.version.sdk";              //$NON-NLS-1$
    public static final String PROP_VERSION_CODENAME = "ro.build.version.codename";    //$NON-NLS-1$
    public static final String PROP_VERSION_RELEASE = "ro.build.version.release";      //$NON-NLS-1$

    public static final String ADDON_NAME = "name";                                    //$NON-NLS-1$
    public static final String ADDON_VENDOR = "vendor";                                //$NON-NLS-1$
    public static final String ADDON_API = "api";                                      //$NON-NLS-1$
    public static final String ADDON_DESCRIPTION = "description";                      //$NON-NLS-1$
    public static final String ADDON_LIBRARIES = "libraries";                          //$NON-NLS-1$
    public static final String ADDON_DEFAULT_SKIN = "skin";                            //$NON-NLS-1$
    public static final String ADDON_USB_VENDOR = "usb-vendor";                        //$NON-NLS-1$
    public static final String ADDON_REVISION = "revision";                            //$NON-NLS-1$
    public static final String ADDON_REVISION_OLD = "version";                         //$NON-NLS-1$


    private static final Pattern PATTERN_LIB_DATA = Pattern.compile(
"^([a-zA-Z0-9._-]+\\.jar);(.*)$", Pattern.CASE_INSENSITIVE);               //$NON-NLS-1$

// usb ids are 16-bit hexadecimal values.
     private static final Pattern PATTERN_USB_IDS = Pattern.compile(
"^0x[a-f0-9]{4}$", Pattern.CASE_INSENSITIVE);                              //$NON-NLS-1$

/** List of items in the platform to check when parsing it. These paths are relative to the
* platform root folder. */
    private static final String[] sPlatformContentList = new String[] {
SdkConstants.FN_FRAMEWORK_LIBRARY,
SdkConstants.FN_FRAMEWORK_AIDL,
};

/** Preference file containing the usb ids for adb */
    private static final String ADB_INI_FILE = "adb_usb.ini";                          //$NON-NLS-1$
//0--------90--------90--------90--------90--------90--------90--------90--------9
       private static final String ADB_INI_HEADER =
"# ANDROID 3RD PARTY USB VENDOR ID LIST -- DO NOT EDIT.\n" +                   //$NON-NLS-1$
"# USE 'android update adb' TO GENERATE.\n" +                                  //$NON-NLS-1$
"# 1 USB VENDOR ID PER LINE.\n";                                               //$NON-NLS-1$
//Synthetic comment -- @@ -363,7 +363,8 @@
* @return A non-null possibly empty map of extra samples directories and their associated
*   extra package display name.
*/
    @NonNull
    public Map<File, String> getExtraSamples() {
LocalSdkParser parser = new LocalSdkParser();
Package[] packages = parser.parseSdk(mOsSdkPath,
this,
//Synthetic comment -- @@ -404,7 +405,8 @@
*
* @return A non-null possibly empty map of { string "vendor/path" => integer major revision }
*/
    @NonNull
    public Map<String, Integer> getExtrasVersions() {
LocalSdkParser parser = new LocalSdkParser();
Package[] packages = parser.parseSdk(mOsSdkPath,
this,
//Synthetic comment -- @@ -428,7 +430,8 @@
}

/** Returns the platform tools version if installed, null otherwise. */
    @Nullable
    public String getPlatformToolsVersion() {
LocalSdkParser parser = new LocalSdkParser();
Package[] packages = parser.parseSdk(mOsSdkPath, this, LocalSdkParser.PARSE_PLATFORM_TOOLS,
new NullTaskMonitor(NullLogger.getLogger()));
//Synthetic comment -- @@ -1260,7 +1263,8 @@
// -------------

private static class DirInfo {
        @NonNull
        private final File mDir;
private final long mDirModifiedTS;
private final long mPropsModifedTS;
private final long mPropsChecksum;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilder.java b/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index d499feb..ae2856e 100644

//Synthetic comment -- @@ -49,9 +49,9 @@
*/
public final class ApkBuilder implements IArchiveBuilder {

    private static final Pattern PATTERN_NATIVELIB_EXT = Pattern.compile("^.+\\.so$",
Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_BITCODELIB_EXT = Pattern.compile("^.+\\.bc$",
Pattern.CASE_INSENSITIVE);

/**
//Synthetic comment -- @@ -188,7 +188,7 @@
}

/** Internal implementation of {@link JarStatus}. */
    private static final class JarStatusImpl implements JarStatus {
public final List<String> mLibs;
public final boolean mNativeLibsConflict;

//Synthetic comment -- @@ -214,7 +214,7 @@
* Both the {@link PrivateKey} and the {@link X509Certificate} are guaranteed to be non-null.
*
*/
    public static final class SigningInfo {
public final PrivateKey key;
public final X509Certificate certificate;









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilderMain.java b/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilderMain.java
//Synthetic comment -- index 805b74a..cbdf7d5 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
*/
public final class ApkBuilderMain {

    private static final Pattern PATTERN_JAR_EXT = Pattern.compile("^.+\\.jar$",
Pattern.CASE_INSENSITIVE);

/**








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/devices/DeviceParser.java b/sdklib2/src/main/java/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index dd63af2..95b19ed 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
public class DeviceParser {

private static class DeviceHandler extends DefaultHandler {
        private static final String sSpaceRegex = "[\\s]+";
private final List<Device> mDevices = new ArrayList<Device>();
private final StringBuilder mStringAccumulator = new StringBuilder();
private final File mParentFolder;
//Synthetic comment -- @@ -343,7 +343,7 @@

}

    private static final SAXParserFactory sParserFactory;

static {
sParserFactory = SAXParserFactory.newInstance();








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/avd/AvdManager.java b/sdklib2/src/main/java/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index eae4eea..6fead16 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
}
}

    private static final Pattern INI_LINE_PATTERN =
Pattern.compile("^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");        //$NON-NLS-1$

public static final String AVD_FOLDER_EXTENSION = ".avd";           //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/avd/HardwareProperties.java b/sdklib2/src/main/java/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 02241ef..efe1f19 100644

//Synthetic comment -- @@ -67,26 +67,26 @@
public static final String HW_PROXIMITY_SENSOR = "hw.sensors.proximity";


    private static final Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

/** Property name in the generated avd config file; String; e.g. "hw.screen" */
    private static final String HW_PROP_NAME = "name";              //$NON-NLS-1$
/** Property type, one of {@link HardwarePropertyType} */
    private static final String HW_PROP_TYPE = "type";              //$NON-NLS-1$
/** Default value of the property. String matching the property type. */
    private static final String HW_PROP_DEFAULT = "default";        //$NON-NLS-1$
/** User-visible name of the property. String. */
    private static final String HW_PROP_ABSTRACT = "abstract";      //$NON-NLS-1$
/** User-visible description of the property. String. */
    private static final String HW_PROP_DESC = "description";       //$NON-NLS-1$
/** Comma-separate values for a property of type "enum" */
    private static final String HW_PROP_ENUM = "enum";              //$NON-NLS-1$

    public static final String BOOLEAN_YES = "yes";
    public static final String BOOLEAN_NO = "no";
    public static final String[] BOOLEAN_VALUES = new String[] { BOOLEAN_YES, BOOLEAN_NO };
    public static final Pattern DISKSIZE_PATTERN = Pattern.compile("\\d+[MK]B"); //$NON-NLS-1$

/** Represents the type of a hardware property value. */
public enum HardwarePropertyType {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java b/sdklib2/src/main/java/com/android/sdklib/internal/build/BuildConfigGenerator.java
//Synthetic comment -- index 038975a..22d0d9e 100644

//Synthetic comment -- @@ -33,10 +33,10 @@
*/
public class BuildConfigGenerator {

    public static final String BUILD_CONFIG_NAME = "BuildConfig.java";

    private static final String PH_PACKAGE = "#PACKAGE#";
    private static final String PH_DEBUG = "#DEBUG#";

private final String mGenFolder;
private final String mAppPackage;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectCreator.java b/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 1fdea64..4bec0a9 100644

//Synthetic comment -- @@ -54,32 +54,32 @@
public class ProjectCreator {

/** Version of the build.xml. Stored in version-tag */
    private static final int MIN_BUILD_VERSION_TAG = 1;

/** Package path substitution string used in template files, i.e. "PACKAGE_PATH" */
    private static final String PH_JAVA_FOLDER = "PACKAGE_PATH";
/** Package name substitution string used in template files, i.e. "PACKAGE" */
    private static final String PH_PACKAGE = "PACKAGE";
/** Activity name substitution string used in template files, i.e. "ACTIVITY_NAME".
* @deprecated This is only used for older templates. For new ones see
* {@link #PH_ACTIVITY_ENTRY_NAME}, and {@link #PH_ACTIVITY_CLASS_NAME}. */
@Deprecated
    private static final String PH_ACTIVITY_NAME = "ACTIVITY_NAME";
/** Activity name substitution string used in manifest templates, i.e. "ACTIVITY_ENTRY_NAME".*/
    private static final String PH_ACTIVITY_ENTRY_NAME = "ACTIVITY_ENTRY_NAME";
/** Activity name substitution string used in class templates, i.e. "ACTIVITY_CLASS_NAME".*/
    private static final String PH_ACTIVITY_CLASS_NAME = "ACTIVITY_CLASS_NAME";
/** Activity FQ-name substitution string used in class templates, i.e. "ACTIVITY_FQ_NAME".*/
    private static final String PH_ACTIVITY_FQ_NAME = "ACTIVITY_FQ_NAME";
/** Original Activity class name substitution string used in class templates, i.e.
* "ACTIVITY_TESTED_CLASS_NAME".*/
    private static final String PH_ACTIVITY_TESTED_CLASS_NAME = "ACTIVITY_TESTED_CLASS_NAME";
/** Project name substitution string used in template files, i.e. "PROJECT_NAME". */
    public static final String PH_PROJECT_NAME = "PROJECT_NAME";
/** Application icon substitution string used in the manifest template */
    private static final String PH_ICON = "ICON";
/** Version tag name substitution string used in template files, i.e. "VERSION_TAG". */
    private static final String PH_VERSION_TAG = "VERSION_TAG";

/** The xpath to find a project name in an Ant build file. */
private static final String XPATH_PROJECT_NAME = "/project/@name";
//Synthetic comment -- @@ -88,7 +88,7 @@
* directory name, we're being a bit conservative on purpose: dot and space cannot be used. */
public static final Pattern RE_PROJECT_NAME = Pattern.compile("[a-zA-Z0-9_]+");
/** List of valid characters for a project name. Used for display purposes. */
    public static final String CHARS_PROJECT_NAME = "a-z A-Z 0-9 _";

/** Pattern for characters accepted in a package name. A package is list of Java identifier
* separated by a dot. We need to have at least one dot (e.g. a two-level package name).
//Synthetic comment -- @@ -96,13 +96,13 @@
public static final Pattern RE_PACKAGE_NAME =
Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(?:\\.[a-zA-Z_][a-zA-Z0-9_]*)+");
/** List of valid characters for a project name. Used for display purposes. */
    public static final String CHARS_PACKAGE_NAME = "a-z A-Z 0-9 _";

/** Pattern for characters accepted in an activity name, which is a Java identifier. */
public static final Pattern RE_ACTIVITY_NAME =
Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
/** List of valid characters for a project name. Used for display purposes. */
    public static final String CHARS_ACTIVITY_NAME = "a-z A-Z 0-9 _";


public enum OutputLevel {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectProperties.java b/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 48acb56..b0be258 100644

//Synthetic comment -- @@ -55,39 +55,39 @@
*
*/
public class ProjectProperties implements IPropertySource {
    protected static final Pattern PATTERN_PROP = Pattern.compile(
"^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

/** The property name for the project target */
    public static final String PROPERTY_TARGET = "target";
/** The property name for the renderscript build target */
    public static final String PROPERTY_RS_TARGET = "renderscript.target";

    public static final String PROPERTY_LIBRARY = "android.library";
    public static final String PROPERTY_LIB_REF = "android.library.reference.";
    private static final String PROPERTY_LIB_REF_REGEX = "android.library.reference.\\d+";

    public static final String PROPERTY_PROGUARD_CONFIG = "proguard.config";
    public static final String PROPERTY_RULES_PATH = "layoutrules.jars";

    public static final String PROPERTY_SDK = "sdk.dir";
// LEGACY - Kept so that we can actually remove it from local.properties.
    private static final String PROPERTY_SDK_LEGACY = "sdk-location";

    public static final String PROPERTY_SPLIT_BY_DENSITY = "split.density";
    public static final String PROPERTY_SPLIT_BY_ABI = "split.abi";
    public static final String PROPERTY_SPLIT_BY_LOCALE = "split.locale";

    public static final String PROPERTY_TESTED_PROJECT = "tested.project.dir";

    public static final String PROPERTY_BUILD_SOURCE_DIR = "source.dir";
    public static final String PROPERTY_BUILD_OUT_DIR = "out.dir";

    public static final String PROPERTY_PACKAGE = "package";
    public static final String PROPERTY_VERSIONCODE = "versionCode";
    public static final String PROPERTY_PROJECTS = "projects";
    public static final String PROPERTY_KEY_STORE = "key.store";
    public static final String PROPERTY_KEY_ALIAS = "key.alias";

public static enum PropertyType {
ANT(SdkConstants.FN_ANT_PROPERTIES, BUILD_HEADER, new String[] {
//Synthetic comment -- @@ -174,7 +174,7 @@
}
}

    private static final String LOCAL_HEADER =
//           1-------10--------20--------30--------40--------50--------60--------70--------80
"# This file is automatically generated by Android Tools.\n" +
"# Do not modify this file -- YOUR CHANGES WILL BE ERASED!\n" +
//Synthetic comment -- @@ -183,7 +183,7 @@
"# as it contains information specific to your local configuration.\n" +
"\n";

    private static final String DEFAULT_HEADER =
//          1-------10--------20--------30--------40--------50--------60--------70--------80
"# This file is automatically generated by Android Tools.\n" +
"# Do not modify this file -- YOUR CHANGES WILL BE ERASED!\n" +
//Synthetic comment -- @@ -204,7 +204,7 @@
+ SdkConstants.FN_PROJECT_PROGUARD_FILE +'\n' +
"\n";

    private static final String BUILD_HEADER =
//          1-------10--------20--------30--------40--------50--------60--------70--------80
"# This file is used to override default values used by the Ant build system.\n" +
"#\n" +








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index 0bb9bd6..7bbd48d 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
*/
public class ProjectPropertiesWorkingCopy extends ProjectProperties {

    private static final Map<String, String> COMMENT_MAP = new HashMap<String, String>();
static {
//               1-------10--------20--------30--------40--------50--------60--------70--------80
COMMENT_MAP.put(PROPERTY_TARGET,








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/DownloadCache.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index e02023d..b37a1e8 100755

//Synthetic comment -- @@ -79,11 +79,11 @@
private static final String KEY_URL = "URL";                        //$NON-NLS-1$

/** Prefix of binary files stored in the {@link SdkConstants#FD_CACHE} directory. */
    private static final String BIN_FILE_PREFIX = "sdkbin";             //$NON-NLS-1$
/** Prefix of meta info files stored in the {@link SdkConstants#FD_CACHE} directory. */
    private static final String INFO_FILE_PREFIX = "sdkinf";            //$NON-NLS-1$
/* Revision suffixed to the prefix. */
    private static final String REV_FILE_PREFIX = "-1_";                //$NON-NLS-1$

/**
* Minimum time before we consider a cached entry is potentially stale.
//Synthetic comment -- @@ -98,7 +98,7 @@
* <p/>
* TODO: change for a dynamic preference later.
*/
    private static final long MIN_TIME_EXPIRED_MS =  10*60*1000;
/**
* Maximum time before we consider a cache entry to be stale.
* Expressed in milliseconds.
//Synthetic comment -- @@ -111,20 +111,20 @@
* <p/>
* TODO: change for a dynamic preference later.
*/
    private static final long MAX_TIME_EXPIRED_MS = 4*60*60*1000;

/**
* The maximum file size we'll cache for "small" files.
* 640KB is more than enough and is already a stretch since these are read in memory.
* (The actual typical size of the files handled here is in the 4-64KB range.)
*/
    private static final int MAX_SMALL_FILE_SIZE = 640 * 1024;

/**
* HTTP Headers that are saved in an info file.
* For HTTP/1.1 header names, see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
*/
    private static final String[] INFO_HTTP_HEADERS = {
HttpHeaders.LAST_MODIFIED,
HttpHeaders.ETAG,
HttpHeaders.CONTENT_LENGTH,








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index 313819b..0e1144c 100755

//Synthetic comment -- @@ -115,7 +115,8 @@
* @param monitor A monitor to track progress. Cannot be null.
* @return The packages found. Can be retrieved later using {@link #getPackages()}.
*/
    @NonNull
    public Package[] parseSdk(
@NonNull String osSdkRoot,
@NonNull SdkManager sdkManager,
@NonNull ITaskMonitor monitor) {
//Synthetic comment -- @@ -135,7 +136,8 @@
* @param monitor A monitor to track progress. Cannot be null.
* @return The packages found. Can be retrieved later using {@link #getPackages()}.
*/
    @NonNull
    public Package[] parseSdk(
@NonNull String osSdkRoot,
@NonNull SdkManager sdkManager,
int parseFilter,








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/UrlOpener.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 52724c77..a2c45c7 100644

//Synthetic comment -- @@ -168,7 +168,8 @@
* @throws CanceledByUserException Exception thrown if the user cancels the
*              authentication dialog.
*/
    @NonNull
    static Pair<InputStream, HttpResponse> openUrl(
@NonNull String url,
boolean needsMarkResetSupport,
@NonNull ITaskMonitor monitor,
//Synthetic comment -- @@ -332,7 +333,8 @@
return Pair.of(is, outResponse);
}

    @NonNull
    private static Pair<InputStream, HttpResponse> openWithHttpClient(
@NonNull String url,
@NonNull ITaskMonitor monitor,
Header[] inHeaders)








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/AddonPackage.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/AddonPackage.java
//Synthetic comment -- index a388f54..d85dd85 100755

//Synthetic comment -- @@ -406,22 +406,26 @@
}

/** Returns the vendor id, a string, for add-on packages. */
    @NonNull
    public String getVendorId() {
return mVendorId;
}

/** Returns the vendor, a string for display purposes. */
    @NonNull
    public String getDisplayVendor() {
return mVendorDisplay;
}

/** Returns the name id, a string, for add-on packages or for libraries. */
    @NonNull
    public String getNameId() {
return mNameId;
}

/** Returns the name, a string for display purposes. */
    @NonNull
    public String getDisplayName() {
return mDisplayName;
}

//Synthetic comment -- @@ -436,7 +440,8 @@
}

/** Returns the libs defined in this add-on. Can be an empty array but not null. */
    @NonNull
    public Lib[] getLibs() {
return mLibs;
}

//Synthetic comment -- @@ -452,8 +457,9 @@
*
* @since sdk-addon-2.xsd
*/
    @NonNull
@Override
    public Pair<Integer, Integer> getLayoutlibVersion() {
return mLayoutlibVersion.getLayoutlibVersion();
}

//Synthetic comment -- @@ -463,8 +469,9 @@
* <p/>
* {@inheritDoc}
*/
    @NonNull
@Override
    public String installId() {
return encodeAddonName();
}









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/FullRevision.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/FullRevision.java
//Synthetic comment -- index 715028e..d7e25d2 100755

//Synthetic comment -- @@ -37,7 +37,7 @@
public static final int IMPLICIT_MICRO_REV = 0;
public static final int NOT_A_PREVIEW      = 0;

    private static final Pattern FULL_REVISION_PATTERN =
//                   1=major       2=minor       3=micro              4=preview
Pattern.compile("\\s*([0-9]+)(?:\\.([0-9]+)(?:\\.([0-9]+))?)?\\s*(?:rc([0-9]+))?\\s*");

//Synthetic comment -- @@ -96,7 +96,8 @@
* @return A new non-null {@link FullRevision}.
* @throws NumberFormatException if the parsing failed.
*/
    @NonNull
    public static FullRevision parseRevision(@NonNull String revision)
throws NumberFormatException {

if (revision == null) {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/IAndroidVersionProvider.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/IAndroidVersionProvider.java
//Synthetic comment -- index 14d6214..65bed4a 100755

//Synthetic comment -- @@ -33,5 +33,6 @@
* Returns the android version, for platform, add-on and doc packages.
* Can be 0 if this is a local package of unknown api-level.
*/
    @NonNull
    public abstract AndroidVersion getAndroidVersion();
}








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java
//Synthetic comment -- index ad33ed4..eefda3b 100755

//Synthetic comment -- @@ -44,7 +44,8 @@
* @return A new non-null {@link MajorRevision}.
* @throws NumberFormatException if the parsing failed.
*/
    @NonNull
    public static MajorRevision parseRevision(@NonNull String revision)
throws NumberFormatException {

if (revision == null) {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/io/FileOp.java b/sdklib2/src/main/java/com/android/sdklib/io/FileOp.java
//Synthetic comment -- index f9793cc..c0b061f 100755

//Synthetic comment -- @@ -47,7 +47,7 @@
/**
* Parameters to call File.setExecutable through reflection.
*/
    private static final Object[] sFileSetExecutableParams = new Object[] {
Boolean.TRUE, Boolean.FALSE };

// static initialization of sFileSetExecutable.
//Synthetic comment -- @@ -347,9 +347,10 @@
return new FileOutputStream(file);
}

    @NonNull
@Override
    @SuppressWarnings("resource") // Eclipse doesn't understand Closeables.closeQuietly
    public Properties loadProperties(@NonNull File file) {
Properties props = new Properties();
FileInputStream fis = null;
try {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/io/IFileOp.java b/sdklib2/src/main/java/com/android/sdklib/io/IFileOp.java
//Synthetic comment -- index 5b131d5..7cebd4e 100755

//Synthetic comment -- @@ -122,7 +122,8 @@
* @return A new {@link Properties} with the properties loaded from the file,
*          or an empty property set in case of error.
*/
    @NonNull
    public Properties loadProperties(@NonNull File file);

/**
* Saves (write, store) the given {@link Properties} into the given {@link File}.








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/io/NonClosingInputStream.java b/sdklib2/src/main/java/com/android/sdklib/io/NonClosingInputStream.java
//Synthetic comment -- index 470b706..f939e48 100755

//Synthetic comment -- @@ -69,7 +69,8 @@
* Returns the current {@link CloseBehavior}.
* @return the current {@link CloseBehavior}. Never null.
*/
    @NonNull
    public CloseBehavior getCloseBehavior() {
return mCloseBehavior;
}









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/util/CommandLineParser.java b/sdklib2/src/main/java/com/android/sdklib/util/CommandLineParser.java
//Synthetic comment -- index 3a86ea7..5c19052 100644

//Synthetic comment -- @@ -51,10 +51,10 @@
*/

/** Internal verb name for internally hidden flags. */
    public static final String GLOBAL_FLAG_VERB = "@@internal@@";   //$NON-NLS-1$

/** String to use when the verb doesn't need any object. */
    public static final String NO_VERB_OBJECT = "";                 //$NON-NLS-1$

/** The global help flag. */
public static final String KEY_HELP = "help";








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/util/FormatUtils.java b/sdklib2/src/main/java/com/android/sdklib/util/FormatUtils.java
//Synthetic comment -- index fc9258c..0ff5e69 100755

//Synthetic comment -- @@ -31,7 +31,8 @@
* @return A new non-null string, with the size expressed in either Bytes
*   or KiB or MiB or GiB.
*/
    @NonNull
    public static String byteSizeToString(long size) {
String sizeStr;

if (size < 1024) {







