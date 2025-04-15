/*This is a first (and largest) patch in a series of patches over the next month to extend the
AVD and ADT eclipse plugin to support processor-specific platform images and emulators.  This
patch is intended to co-exist with patches to create x86 emulator environments and overall
SDK support.

There is an overall expectation that the sdk building process will be updated to meet the
following expectations...  It is not in the scope of these UI patches to change the overall
sdk building structure.

expectation #1:
tools/emulator[.exe]     -- ARM
tools/emulator-x86[.exe] -- x86
tools/emulator-foo[.exe] -- an arbitrary additional architecture (extensible)

expectation #2:
platforms/android-XXX/images/arm - location of kernel/images for ARM
platforms/android-XXX/images/x86 - location of kernel/images for x86
platforms/android-XXX/images/foo - location of kernel/images for arbitrary architecture

expectation #3
In the event that add-ons are in the SDK,
add-ons/addon_XXX/images/arm     - location of kernel/images for ARM
add-ons/addon_XXX/images/x86     - location of kernel/images for x86
add-ons/addon_XXX/images/foo     - location of kernel/images for arbitrary architecture

NOTE:  For "earlier"/legacy api levels, it is assumed that it is ARM only and the images will
be in platforms/android-XXX/images and add-ons/addon_XXX/images

When an API level is chosen in AVD, it scans the appropriate API directories and determines
if the image directory is "legacy" or if it has subdirectories.  In the latter case, it
populates the list of potential processors using these directory names (and some
prettyprinting for well known architectures)

tested using "android" command line to start AVD on linux and windows
tested using Eclipse plugin AVD integration on linux and windows
REMINDER: You need to change the directory layout of images and add the right
          emulator-XXX[.exe] to test it

If one uses the "android" command line to create an AVD from the command line, the
processor type is assumed to be arm today.  A future patch will be needed to add
command line processor type selectivity

Change-Id:Ifd7c39bf93c6e926f62407bfed024d2789efb41a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 7f70859..c42b496 100644

//Synthetic comment -- @@ -1148,7 +1148,7 @@
// not meant to be exhaustive.
String[] filesToCheck = new String[] {
osSdkLocation + getOsRelativeAdb(),
                osSdkLocation + getOsRelativeEmulator()
};
for (String file : filesToCheck) {
if (checkFile(file) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 6ee74ed..3615dc5 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.xml.ManifestData;
//Synthetic comment -- @@ -1187,7 +1188,16 @@
// build the command line based on the available parameters.
ArrayList<String> list = new ArrayList<String>();

        list.add(AdtPlugin.getOsAbsoluteEmulator());
list.add(FLAG_AVD);
list.add(avdToLaunch.getName());









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 1fe6d97..1715f4d 100644

//Synthetic comment -- @@ -981,10 +981,15 @@
oldAvdInfo = avdManager.getAvd(avdName, false /*validAvdOnly*/);
}

@SuppressWarnings("unused") // newAvdInfo is never read, yet useful for debugging
AvdInfo newAvdInfo = avdManager.createAvd(avdFolder,
avdName,
target,
skin,
mSdkCommandLine.getParamSdCard(),
hardwareConfig,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index f3da39c..f93e482 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
* Represents an add-on target in the SDK.
//Synthetic comment -- @@ -67,6 +68,7 @@
private final String mLocation;
private final PlatformTarget mBasePlatform;
private final String mName;
private final String mVendor;
private final int mRevision;
private final String mDescription;
//Synthetic comment -- @@ -98,6 +100,7 @@
mRevision = revision;
mDescription = description;
mBasePlatform = basePlatform;

// handle the optional libraries.
if (libMap != null) {
//Synthetic comment -- @@ -121,6 +124,42 @@
return mName;
}

public String getVendor() {
return mVendor;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index c0dcaa7..a1e5673 100644

//Synthetic comment -- @@ -225,6 +225,28 @@
* <p/>If the target defines no USB Vendor ID, then the method return 0.
*/
int getUsbVendorId();

/**
* Returns whether the given target is compatible with the receiver.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 6aeeade..582a49c 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.io.File;
import java.util.Collections;
import java.util.Map;

/**
* Represents a platform target in the SDK.
//Synthetic comment -- @@ -43,6 +44,7 @@
private final Map<String, String> mProperties;
private final SparseArray<String> mPaths = new SparseArray<String>();
private String[] mSkins;


/**
//Synthetic comment -- @@ -112,6 +114,54 @@
SdkConstants.FN_DX);
mPaths.put(DX_JAR, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_LIB_FOLDER +
SdkConstants.FN_DX_JAR);
}

public String getLocation() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index f8f7f19..cb79bc4 100644

//Synthetic comment -- @@ -130,9 +130,13 @@
public final static String FN_ADB = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
"adb.exe" : "adb"; //$NON-NLS-1$ //$NON-NLS-2$

    /** emulator executable (with extension for the current OS) */
    public final static String FN_EMULATOR = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
            "emulator.exe" : "emulator"; //$NON-NLS-1$ //$NON-NLS-2$

/** zipalign executable (with extension for the current OS)  */
public final static String FN_ZIPALIGN = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
//Synthetic comment -- @@ -223,6 +227,10 @@
public static final String FD_DOCS_REFERENCE = "reference";
/** Name of the SDK images folder. */
public final static String FD_IMAGES = "images";
/** Name of the SDK skins folder. */
public final static String FD_SKINS = "skins";
/** Name of the SDK samples folder. */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index eba8e07..2bb1f2d 100644

//Synthetic comment -- @@ -66,6 +66,14 @@
public final static String AVD_INFO_TARGET = "target";     //$NON-NLS-1$

/**
* AVD/config.ini key name representing the SDK-relative path of the skin folder, if any,
* or a 320x480 like constant for a numeric skin size.
*
//Synthetic comment -- @@ -175,6 +183,7 @@
private final String mPath;
private final String mTargetHash;
private final IAndroidTarget mTarget;
private final Map<String, String> mProperties;
private final AvdStatus mStatus;

//Synthetic comment -- @@ -188,11 +197,12 @@
* @param path The path to the config.ini file
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param properties The property map. Cannot be null.
*/
        public AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
Map<String, String> properties) {
            this(name, path, targetHash, target, properties, AvdStatus.OK);
}

/**
//Synthetic comment -- @@ -205,15 +215,17 @@
* @param path The path to the config.ini file
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
* @param properties The property map. Can be null.
* @param status The {@link AvdStatus} of this AVD. Cannot be null.
*/
        public AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
Map<String, String> properties, AvdStatus status) {
mName = name;
mPath = path;
mTargetHash = targetHash;
mTarget = target;
mProperties = properties == null ? null : Collections.unmodifiableMap(properties);
mStatus = status;
}
//Synthetic comment -- @@ -228,6 +240,26 @@
return mPath;
}

/**
* Returns the target hash string.
*/
//Synthetic comment -- @@ -500,10 +532,10 @@
* See {@link #createAvd(File, String, IAndroidTarget, String, String, Map, boolean, boolean, ISdkLog)}
**/
@Deprecated
    public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target, String skinName,
String sdcard, Map<String, String> hardwareConfig, boolean removePrevious,
ISdkLog log) {
        return createAvd(avdFolder, name, target, skinName, sdcard, hardwareConfig, removePrevious,
false, log);
}

//Synthetic comment -- @@ -513,6 +545,7 @@
* @param avdFolder the data folder for the AVD. It will be created as needed.
* @param name the name of the AVD
* @param target the target of the AVD
* @param skinName the name of the skin. Can be null. Must have been verified by caller.
* @param sdcard the parameter value for the sdCard. Can be null. This is either a path to
*        an existing sdcard image or a sdcard size (\d+, \d+K, \dM).
//Synthetic comment -- @@ -523,9 +556,9 @@
* @return The new {@link AvdInfo} in case of success (which has just been added to the
*         internal list) or null in case of failure.
*/
    public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target,
            String skinName, String sdcard, Map<String,String> hardwareConfig,
            boolean removePrevious, boolean createSnapshot, ISdkLog log) {
if (log == null) {
throw new IllegalArgumentException("log cannot be null");
}
//Synthetic comment -- @@ -558,17 +591,18 @@
iniFile = createAvdIniFile(name, avdFolder, target);

// writes the userdata.img in it.
            String imagePath = target.getPath(IAndroidTarget.IMAGES);
File userdataSrc = new File(imagePath, USERDATA_IMG);

if (userdataSrc.exists() == false && target.isPlatform() == false) {
                imagePath = target.getParent().getPath(IAndroidTarget.IMAGES);
userdataSrc = new File(imagePath, USERDATA_IMG);
}

if (userdataSrc.exists() == false) {
                log.error(null, "Unable to find a '%1$s' file to copy into the AVD folder.",
                        USERDATA_IMG);
needCleanup = true;
return null;
}
//Synthetic comment -- @@ -576,10 +610,18 @@

copyImageFile(userdataSrc, userdataDest);

// Config file.
HashMap<String, String> values = new HashMap<String, String>();

            if (setImagePathProperties(target, values, log) == false) {
needCleanup = true;
return null;
}
//Synthetic comment -- @@ -600,6 +642,9 @@
values.put(AVD_INI_SNAPSHOT_PRESENT, "true");
}

// Now the skin.
if (skinName == null || skinName.length() == 0) {
skinName = target.getDefaultSkin();
//Synthetic comment -- @@ -616,6 +661,7 @@
// assume skin name is valid
String skinPath = getSkinRelativePath(skinName, target, log);
if (skinPath == null) {
needCleanup = true;
return null;
}
//Synthetic comment -- @@ -688,6 +734,7 @@
}

if (createSdCard(mkSdCard.getAbsolutePath(), sdcard, path, log) == false) {
needCleanup = true;
return null; // mksdcard output has already been displayed, no need to
// output anything else.
//Synthetic comment -- @@ -763,6 +810,7 @@
report.append(String.format("Created AVD '%1$s' based on %2$s (%3$s)", name,
target.getName(), target.getVendor()));
}

// display the chosen hardware config
if (finalHardwareValues.size() > 0) {
//Synthetic comment -- @@ -780,7 +828,7 @@
AvdInfo newAvdInfo = new AvdInfo(name,
avdFolder.getAbsolutePath(),
target.hashString(),
                    target, values);

AvdInfo oldAvdInfo = getAvd(name, false /*validAvdOnly*/);

//Synthetic comment -- @@ -861,7 +909,7 @@
*/
private String getImageRelativePath(IAndroidTarget target)
throws InvalidTargetPathException {
        String imageFullPath = target.getPath(IAndroidTarget.IMAGES);

// make this path relative to the SDK location
String sdkLocation = mSdkManager.getLocation();
//Synthetic comment -- @@ -1070,7 +1118,7 @@

// update AVD info
AvdInfo info = new AvdInfo(avdInfo.getName(), paramFolderPath,
                        avdInfo.getTargetHash(), avdInfo.getTarget(), avdInfo.getProperties());
replaceAvd(avdInfo, info);

// update the ini file
//Synthetic comment -- @@ -1090,7 +1138,7 @@

// update AVD info
AvdInfo info = new AvdInfo(newName, avdInfo.getPath(),
                        avdInfo.getTargetHash(), avdInfo.getTarget(), avdInfo.getProperties());
replaceAvd(avdInfo, info);
}

//Synthetic comment -- @@ -1233,7 +1281,15 @@
name = matcher.group(1);
}

        // check the image.sysdir are valid
boolean validImageSysdir = true;
if (properties != null) {
String imageSysDir = properties.get(AVD_INI_IMAGES_1);
//Synthetic comment -- @@ -1278,6 +1334,7 @@
avdPath,
targetHash,
target,
properties,
status);

//Synthetic comment -- @@ -1489,7 +1546,7 @@
AvdStatus status;

// create the path to the new system images.
        if (setImagePathProperties(avd.getTarget(), properties, log)) {
if (properties.containsKey(AVD_INI_IMAGES_1)) {
log.printf("Updated '%1$s' with value '%2$s'\n", AVD_INI_IMAGES_1,
properties.get(AVD_INI_IMAGES_1));
//Synthetic comment -- @@ -1521,6 +1578,7 @@
avd.getPath(),
avd.getTargetHash(),
avd.getTarget(),
properties,
status);

//Synthetic comment -- @@ -1530,13 +1588,14 @@
/**
* Sets the paths to the system images in a properties map.
* @param target the target in which to find the system images.
* @param properties the properties in which to set the paths.
* @param log the log object to receive action logs. Cannot be null.
* @return true if success, false if some path are missing.
*/
private boolean setImagePathProperties(IAndroidTarget target,
            Map<String, String> properties,
            ISdkLog log) {
properties.remove(AVD_INI_IMAGES_1);
properties.remove(AVD_INI_IMAGES_2);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4411034..8e427c5 100644

//Synthetic comment -- @@ -101,6 +101,9 @@

private Text mAvdName;
private Combo mTargetCombo;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
//Synthetic comment -- @@ -285,10 +288,29 @@
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
reloadSkinCombo();
validatePage();
}
});

// --- sd card group
label = new Label(parent, SWT.NONE);
label.setText("SD Card:");
//Synthetic comment -- @@ -668,6 +690,21 @@
for (int i = 0;i < n; i++) {
if (target.equals(mCurrentTargets.get(mTargetCombo.getItem(i)))) {
mTargetCombo.select(i);
reloadSkinCombo();
break;
}
//Synthetic comment -- @@ -742,6 +779,7 @@
mProperties.putAll(props);

// Cleanup known non-hardware properties
mProperties.remove(AvdManager.AVD_INI_SKIN_PATH);
mProperties.remove(AvdManager.AVD_INI_SKIN_NAME);
mProperties.remove(AvdManager.AVD_INI_SDCARD_SIZE);
//Synthetic comment -- @@ -881,6 +919,50 @@
}
}

/**
* Validates the fields, displays errors and warnings.
* Enables the finish button if there are no errors.
//Synthetic comment -- @@ -905,7 +987,17 @@
error = "A target must be selected in order to create an AVD.";
}

        // Validate SDCard path or value
if (error == null) {
// get the mode. We only need to check the file since the
// verifier on the size Text will prevent invalid input
//Synthetic comment -- @@ -1102,6 +1194,21 @@
return false;
}

// get the SD card data from the UI.
String sdName = null;
if (mSdCardSizeRadio.getSelection()) {
//Synthetic comment -- @@ -1169,6 +1276,7 @@
avdFolder,
avdName,
target,
skinName,
sdName,
mProperties,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 409c25d..4a9e888 100644

//Synthetic comment -- @@ -101,6 +101,8 @@

if (mAvdInfo != null) {
displayValue(c, "Name:", mAvdInfo.getName());
displayValue(c, "Path:", mAvdInfo.getPath());

if (mAvdInfo.getStatus() != AvdStatus.OK) {
//Synthetic comment -- @@ -135,6 +137,7 @@
// display other hardware
HashMap<String, String> copy = new HashMap<String, String>(properties);
// remove stuff we already displayed (or that we don't want to display)
copy.remove(AvdManager.AVD_INI_SKIN_NAME);
copy.remove(AvdManager.AVD_INI_SKIN_PATH);
copy.remove(AvdManager.AVD_INI_SDCARD_SIZE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 5e26a41..f9e0419 100644

//Synthetic comment -- @@ -372,8 +372,10 @@
column2.setText("Platform");
final TableColumn column3 = new TableColumn(mTable, SWT.NONE);
column3.setText("API Level");

        adjustColumnsWidth(mTable, column0, column1, column2, column3);
setupSelectionListener(mTable);
fillTable(mTable);
setEnabled(true);
//Synthetic comment -- @@ -633,16 +635,18 @@
final TableColumn column0,
final TableColumn column1,
final TableColumn column2,
            final TableColumn column3) {
// Add a listener to resize the column to the full width of the table
table.addControlListener(new ControlAdapter() {
@Override
public void controlResized(ControlEvent e) {
Rectangle r = table.getClientArea();
                column0.setWidth(r.width * 25 / 100); // 25%
                column1.setWidth(r.width * 45 / 100); // 45%
column2.setWidth(r.width * 15 / 100); // 15%
column3.setWidth(r.width * 15 / 100); // 15%
}
});
}
//Synthetic comment -- @@ -777,10 +781,12 @@
item.setText(1, target.getFullName());
item.setText(2, target.getVersionName());
item.setText(3, target.getVersion().getApiString());
} else {
item.setText(1, "?");
item.setText(2, "?");
item.setText(3, "?");
}
}
}
//Synthetic comment -- @@ -1027,8 +1033,16 @@
if (dialog.open() == Window.OK) {
String path = mOsSdkPath +
File.separator +
                SdkConstants.OS_SDK_TOOLS_FOLDER +
                SdkConstants.FN_EMULATOR;

final String avdName = avdInfo.getName();








