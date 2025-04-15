/*Support broken addons in SDK Manager UI.

Change-Id:Idec7365e440e865a8225ed6f0c9c156206141d73*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 32c5838..2920674 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -43,27 +44,27 @@
*/
public final class SdkManager {

    public final static String PROP_VERSION_SDK = "ro.build.version.sdk";
    public final static String PROP_VERSION_CODENAME = "ro.build.version.codename";
    public final static String PROP_VERSION_RELEASE = "ro.build.version.release";

    private final static String ADDON_NAME = "name";
    private final static String ADDON_VENDOR = "vendor";
    private final static String ADDON_API = "api";
    private final static String ADDON_DESCRIPTION = "description";
    private final static String ADDON_LIBRARIES = "libraries";
    private final static String ADDON_DEFAULT_SKIN = "skin";
    private final static String ADDON_USB_VENDOR = "usb-vendor";
    private final static String ADDON_REVISION = "revision";
    private final static String ADDON_REVISION_OLD = "version";


private final static Pattern PATTERN_LIB_DATA = Pattern.compile(
            "^([a-zA-Z0-9._-]+\\.jar);(.*)$", Pattern.CASE_INSENSITIVE);

// usb ids are 16-bit hexadecimal values.
private final static Pattern PATTERN_USB_IDS = Pattern.compile(
            "^0x[a-f0-9]{4}$", Pattern.CASE_INSENSITIVE);

/** List of items in the platform to check when parsing it. These paths are relative to the
* platform root folder. */
//Synthetic comment -- @@ -73,39 +74,40 @@
};

/** Preference file containing the usb ids for adb */
    private final static String ADB_INI_FILE = "adb_usb.ini";
//0--------90--------90--------90--------90--------90--------90--------90--------9
private final static String ADB_INI_HEADER =
        "# ANDROID 3RD PARTY USB VENDOR ID LIST -- DO NOT EDIT.\n" +
        "# USE 'android update adb' TO GENERATE.\n" +
        "# 1 USB VENDOR ID PER LINE.\n";

    /** the location of the SDK */
    private final String mSdkLocation;
private IAndroidTarget[] mTargets;

/**
* Create a new {@link SdkManager} instance.
* External users should use {@link #createManager(String, ISdkLog)}.
*
     * @param sdkLocation the location of the SDK.
*/
    private SdkManager(String sdkLocation) {
        mSdkLocation = sdkLocation;
}

/**
* Creates an {@link SdkManager} for a given sdk location.
     * @param sdkLocation the location of the SDK.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the created {@link SdkManager} or null if the location is not valid.
*/
    public static SdkManager createManager(String sdkLocation, ISdkLog log) {
try {
            SdkManager manager = new SdkManager(sdkLocation);
ArrayList<IAndroidTarget> list = new ArrayList<IAndroidTarget>();
            loadPlatforms(sdkLocation, list, log);
            loadAddOns(sdkLocation, list, log);

// sort the targets/add-ons
Collections.sort(list);
//Synthetic comment -- @@ -127,7 +129,7 @@
* Returns the location of the SDK.
*/
public String getLocation() {
        return mSdkLocation;
}

/**
//Synthetic comment -- @@ -193,7 +195,7 @@

// now write the Id in a text file, one per line.
for (Integer i : set) {
                writer.write(String.format("0x%04x\n", i));
}
} finally {
if (writer != null) {
//Synthetic comment -- @@ -210,8 +212,8 @@
public void reloadSdk(ISdkLog log) {
// get the current target list.
ArrayList<IAndroidTarget> list = new ArrayList<IAndroidTarget>();
        loadPlatforms(mSdkLocation, list, log);
        loadAddOns(mSdkLocation, list, log);

// For now replace the old list with the new one.
// In the future we may want to keep the current objects, so that ADT doesn't have to deal
//Synthetic comment -- @@ -326,7 +328,7 @@
sourcePropFile, log);
if (sourceProp != null) {
try {
                        revision = Integer.parseInt(sourceProp.get("Pkg.Revision"));
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}
//Synthetic comment -- @@ -366,7 +368,8 @@
return target;
}
} else {
            log.warning("Ignoring platform '%1$s': %2$s is missing.", platformFolder.getName(),
SdkConstants.FN_BUILD_PROP);
}

//Synthetic comment -- @@ -376,19 +379,21 @@

/**
* Loads the Add-on from the SDK.
     * @param location Location of the SDK
* @param list the list to fill with the add-ons.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static void loadAddOns(String location, ArrayList<IAndroidTarget> list, ISdkLog log) {
        File addonFolder = new File(location, SdkConstants.FD_ADDONS);
if (addonFolder.isDirectory()) {
File[] addons  = addonFolder.listFiles();

for (File addon : addons) {
// Add-ons have to be folders. Ignore files and no need to warn about them.
if (addon.isDirectory()) {
                    AddOnTarget target = loadAddon(addon, list, log);
if (target != null) {
list.add(target);
}
//Synthetic comment -- @@ -411,151 +416,222 @@

/**
* Loads a specific Add-on at a given location.
     * @param addon the location of the addon.
* @param targetList The list of Android target that were already loaded from the SDK.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static AddOnTarget loadAddon(File addon, ArrayList<IAndroidTarget> targetList,
ISdkLog log) {
        FileWrapper addOnManifest = new FileWrapper(addon, SdkConstants.FN_MANIFEST_INI);

        if (addOnManifest.isFile()) {
            Map<String, String> propertyMap = ProjectProperties.parsePropertyFile(
                    addOnManifest, log);

            if (propertyMap != null) {
                // look for some specific values in the map.
                // we require name, vendor, and api
                String name = propertyMap.get(ADDON_NAME);
                if (name == null) {
                    displayAddonManifestWarning(log, addon.getName(), ADDON_NAME);
                    return null;
}

                String vendor = propertyMap.get(ADDON_VENDOR);
                if (vendor == null) {
                    displayAddonManifestWarning(log, addon.getName(), ADDON_VENDOR);
                    return null;
                }

                String api = propertyMap.get(ADDON_API);
                PlatformTarget baseTarget = null;
                if (api == null) {
                    displayAddonManifestWarning(log, addon.getName(), ADDON_API);
                    return null;
                } else {
                    // Look for a platform that has a matching api level or codename.
                    for (IAndroidTarget target : targetList) {
                        if (target.isPlatform() && target.getVersion().equals(api)) {
                            baseTarget = (PlatformTarget)target;
                            break;
                        }
                    }

                    if (baseTarget == null) {
                        // Ignore this add-on.
                        log.warning(
                                "Ignoring add-on '%1$s': Unable to find base platform with API level '%2$s'",
                                addon.getName(), api);
                        return null;
                    }
                }

                // get the optional description
                String description = propertyMap.get(ADDON_DESCRIPTION);

                // get the add-on revision
                int revisionValue = 1;
                String revision = propertyMap.get(ADDON_REVISION);
                if (revision == null) {
                    revision = propertyMap.get(ADDON_REVISION_OLD);
                }
                if (revision != null) {
                    try {
                        revisionValue = Integer.parseInt(revision);
                    } catch (NumberFormatException e) {
                        // looks like apiNumber does not parse to a number.
                        // Ignore this add-on.
                        log.warning(
                                "Ignoring add-on '%1$s': %2$s is not a valid number in %3$s.",
                                addon.getName(), ADDON_REVISION, SdkConstants.FN_BUILD_PROP);
                        return null;
                    }
                }

                // get the optional libraries
                String librariesValue = propertyMap.get(ADDON_LIBRARIES);
                Map<String, String[]> libMap = null;

                if (librariesValue != null) {
                    librariesValue = librariesValue.trim();
                    if (librariesValue.length() > 0) {
                        // split in the string into the libraries name
                        String[] libraries = librariesValue.split(";");
                        if (libraries.length > 0) {
                            libMap = new HashMap<String, String[]>();
                            for (String libName : libraries) {
                                libName = libName.trim();

                                // get the library data from the properties
                                String libData = propertyMap.get(libName);

                                if (libData != null) {
                                    // split the jar file from the description
                                    Matcher m = PATTERN_LIB_DATA.matcher(libData);
                                    if (m.matches()) {
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
}
}
}
}

                AddOnTarget target = new AddOnTarget(addon.getAbsolutePath(), name, vendor,
                        revisionValue, description, libMap, baseTarget);

                // need to parse the skins.
                String[] skins = parseSkinFolder(target.getPath(IAndroidTarget.SKINS));

                // get the default skin, or take it from the base platform if needed.
                String defaultSkin = propertyMap.get(ADDON_DEFAULT_SKIN);
                if (defaultSkin == null) {
                    if (skins.length == 1) {
                        defaultSkin = skins[0];
                    } else {
                        defaultSkin = baseTarget.getDefaultSkin();
                    }
                }

                // get the USB ID (if available)
                int usbVendorId = convertId(propertyMap.get(ADDON_USB_VENDOR));
                if (usbVendorId != IAndroidTarget.NO_USB_ID) {
                    target.setUsbVendorId(usbVendorId);
                }

                target.setSkins(skins, defaultSkin);

                return target;
}
        } else {
            log.warning("Ignoring add-on '%1$s': %2$s is missing.", addon.getName(),
                    SdkConstants.FN_MANIFEST_INI);
}

return null;
}

/**
* Converts a string representation of an hexadecimal ID into an int.
* @param value the string to convert.
* @return the int value, or {@link IAndroidTarget#NO_USB_ID} if the convertion failed.
//Synthetic comment -- @@ -577,16 +653,14 @@
}

/**
     * Displays a warning in the log about the addon being ignored due to a missing manifest value.
*
     * @param log The logger object. Cannot be null.
     * @param addonName The addon name, for display.
* @param valueName The missing manifest value, for display.
*/
    private static void displayAddonManifestWarning(ISdkLog log, String addonName,
            String valueName) {
        log.warning("Ignoring add-on '%1$s': '%2$s' is missing from %3$s.",
                addonName, valueName, SdkConstants.FN_MANIFEST_INI);
}

/**
//Synthetic comment -- @@ -603,7 +677,7 @@
File f = new File(platform, relativePath);
if (!f.exists()) {
log.warning(
                        "Ignoring platform '%1$s': %2$s is missing.",
platform.getName(), relativePath);
return false;
}
//Synthetic comment -- @@ -650,7 +724,7 @@
* @param log Logger. Cannot be null.
*/
private void loadSamples(ISdkLog log) {
        File sampleFolder = new File(mSdkLocation, SdkConstants.FD_SAMPLES);
if (sampleFolder.isDirectory()) {
File[] platforms  = sampleFolder.listFiles();

//Synthetic comment -- @@ -688,13 +762,13 @@

return new AndroidVersion(p);
} catch (FileNotFoundException e) {
            log.warning("Ignoring sample '%1$s': does not contain %2$s.", //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
} catch (IOException e) {
            log.warning("Ignoring sample '%1$s': failed reading %2$s.", //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
} catch (AndroidVersionException e) {
            log.warning("Ignoring sample '%1$s': no android version found in %2$s.", //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index 2a58a89..8226a60 100755

//Synthetic comment -- @@ -21,6 +21,8 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -98,7 +100,12 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    AddonPackage(IAndroidTarget target, Properties props) {
super(  null,                       //source
props,                      //properties
target.getRevision(),       //revision
//Synthetic comment -- @@ -126,6 +133,42 @@
}

/**
* Save the properties of the current packages in the given {@link Properties} object.
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..646172d

//Synthetic comment -- @@ -0,0 +1,147 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java
//Synthetic comment -- index 5e0a767..8a4c19d 100755

//Synthetic comment -- @@ -68,7 +68,22 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    DocPackage(SdkSource source,
Properties props,
int apiLevel,
String codename,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index d06b08d..cac622d 100755

//Synthetic comment -- @@ -87,7 +87,38 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    ExtraPackage(SdkSource source,
Properties props,
String vendor,
String path,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index 915a8e6..e0a6b21 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -29,6 +30,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

//Synthetic comment -- @@ -102,13 +104,12 @@

// for platforms, add-ons and samples, rely on the SdkManager parser
for(IAndroidTarget target : sdkManager.getTargets()) {

Properties props = parseProperties(new File(target.getLocation(),
SdkConstants.FN_SOURCE_PROP));

try {
if (target.isPlatform()) {
                    pkg = new PlatformPackage(target, props);

if (samplesRoot.isDirectory()) {
// Get the samples dir for a platform if it is located in the new
//Synthetic comment -- @@ -119,14 +120,14 @@
Properties samplesProps = parseProperties(
new File(samplesDir, SdkConstants.FN_SOURCE_PROP));
if (samplesProps != null) {
                                SamplePackage pkg2 = new SamplePackage(target, samplesProps);
packages.add(pkg2);
}
visited.add(samplesDir);
}
}
} else {
                    pkg = new AddonPackage(target, props);
}
} catch (Exception e) {
log.error(e, null);
//Synthetic comment -- @@ -138,6 +139,7 @@
}
}

scanMissingSamples(osSdkRoot, visited, packages, log);
scanExtras(osSdkRoot, visited, packages, log);

//Synthetic comment -- @@ -167,7 +169,7 @@
Properties props = parseProperties(new File(dir, SdkConstants.FN_SOURCE_PROP));
if (props != null) {
try {
                        ExtraPackage pkg = new ExtraPackage(
null,                       //source
props,                      //properties
null,                       //vendor
//Synthetic comment -- @@ -181,11 +183,8 @@
dir.getPath()               //archiveOsPath
);

                        // We only accept this as an extra package if it has a valid local path.
                        if (pkg.isPathValid()) {
                            packages.add(pkg);
                            visited.add(dir);
                        }
} catch (Exception e) {
log.error(e, null);
}
//Synthetic comment -- @@ -217,7 +216,7 @@
Properties props = parseProperties(new File(dir, SdkConstants.FN_SOURCE_PROP));
if (props != null) {
try {
                        SamplePackage pkg = new SamplePackage(dir.getAbsolutePath(), props);
packages.add(pkg);
visited.add(dir);
} catch (Exception e) {
//Synthetic comment -- @@ -229,6 +228,42 @@
}

/**
* Try to find a tools package at the given location.
* Returns null if not found.
*/
//Synthetic comment -- @@ -252,7 +287,7 @@

// Create our package. use the properties if we found any.
try {
            ToolPackage pkg = new ToolPackage(
null,                       //source
props,                      //properties
0,                          //revision
//Synthetic comment -- @@ -289,20 +324,9 @@
return null;
}

        Set<String> names = new HashSet<String>();
        for (File file : platformToolsFolder.listFiles()) {
            names.add(file.getName());
        }
        if (!names.contains(SdkConstants.FN_ADB) ||
                !names.contains(SdkConstants.FN_AAPT) ||
                !names.contains(SdkConstants.FN_AIDL) ||
                !names.contains(SdkConstants.FN_DX)) {
            return null;
        }

// Create our package. use the properties if we found any.
try {
            PlatformToolPackage pkg = new PlatformToolPackage(
null,                           //source
props,                          //properties
0,                              //revision
//Synthetic comment -- @@ -333,7 +357,7 @@
// We don't actually check the content of the file.
if (new File(docFolder, "index.html").isFile()) {
try {
                DocPackage pkg = new DocPackage(
null,                       //source
props,                      //properties
0,                          //apiLevel








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
//Synthetic comment -- index 1e5e391..b8a8623 100755

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -73,7 +75,12 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    PlatformPackage(IAndroidTarget target, Properties props) {
super(  null,                       //source
props,                      //properties
target.getRevision(),       //revision








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index 717948e..2e1f2c4 100755

//Synthetic comment -- @@ -18,14 +18,18 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

import org.w3c.dom.Node;

import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
* Represents a platform-tool XML node in an SDK repository.
//Synthetic comment -- @@ -54,7 +58,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    PlatformToolPackage(
SdkSource source,
Properties props,
int revision,
//Synthetic comment -- @@ -64,6 +68,68 @@
Os archiveOs,
Arch archiveArch,
String archiveOsPath) {
super(source,
props,
revision,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java
//Synthetic comment -- index 920a7e0..b6976e9 100755

//Synthetic comment -- @@ -91,7 +91,11 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    SamplePackage(IAndroidTarget target, Properties props) {
super(  null,                                   //source
props,                                  //properties
0,                                      //revision will be taken from props
//Synthetic comment -- @@ -121,7 +125,11 @@
* @throws AndroidVersionException if the {@link AndroidVersion} can't be restored
*                                 from properties.
*/
    SamplePackage(String archiveOsPath, Properties props) throws AndroidVersionException {
super(null,                                   //source
props,                                  //properties
0,                                      //revision will be taken from props








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
//Synthetic comment -- index c76de30..73ddc24 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -97,7 +99,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    ToolPackage(
SdkSource source,
Properties props,
int revision,
//Synthetic comment -- @@ -107,6 +109,21 @@
Os archiveOs,
Arch archiveArch,
String archiveOsPath) {
super(source,
props,
revision,







