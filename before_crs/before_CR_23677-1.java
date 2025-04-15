/*Properly handle CPU Arch/ABI in AVDs.

When we introduced x86 support in the tree there was no
generic emulator exe able to act as a launcher to the -arm and
-x86 versions of the emulator.

This exe is now present and packaged with the SDK, so we remove the
code to launch either arch specific version and instead make the AVD
Manager and ADT simply launch the normal emulator once again.
(This has the side effect of making ADT 12 able to run on Tools r11
and below.)

For this to run though, hw.cpu.arch must be set in the AVD if the
arch is not arm. The new AVD manager sets this properly.

Also fixed some issues from my previous fix to the hardware property.
Now the list contains all of them but there's a isValidForUi that's used
to not show up some prop in the UI.

Change-Id:I7a264a59cb3c5051ff62f6103da9663c7b7eb22f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 55a5ef8..6bc4b38 100644

//Synthetic comment -- @@ -1168,7 +1168,7 @@
// not meant to be exhaustive.
String[] filesToCheck = new String[] {
osSdkLocation + getOsRelativeAdb(),
                osSdkLocation + getOsRelativeEmulator() + SdkConstants.FN_EMULATOR_EXTENSION
};
for (String file : filesToCheck) {
if (checkFile(file) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1b17e09..0fb6b3c 100644

//Synthetic comment -- @@ -1187,7 +1187,7 @@
// build the command line based on the available parameters.
ArrayList<String> list = new ArrayList<String>();

        String path = avdToLaunch.getEmulatorPath(AdtPlugin.getOsSdkFolder());

list.add(path);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 49f0547..d82437f 100644

//Synthetic comment -- @@ -132,13 +132,9 @@
public final static String FN_ADB = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
"adb.exe" : "adb"; //$NON-NLS-1$ //$NON-NLS-2$

    /** emulator executable (_WITHOUT_ extension for the current OS) */
    public final static String FN_EMULATOR =
            "emulator"; //$NON-NLS-1$

    /** emulator executable extension for the current OS */
    public final static String FN_EMULATOR_EXTENSION = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
            ".exe" : ""; //$NON-NLS-1$ //$NON-NLS-2$

/** zipalign executable (with extension for the current OS)  */
public final static String FN_ZIPALIGN = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
//Synthetic comment -- @@ -207,9 +203,13 @@
public static final String FD_DOCS_REFERENCE = "reference";
/** Name of the SDK images folder. */
public final static String FD_IMAGES = "images";
    /** Name of the processors to support. */
public final static String ABI_ARMEABI = "armeabi";
public final static String ABI_INTEL_ATOM = "x86";

/** Name of the SDK skins folder. */
public final static String FD_SKINS = "skins";








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 81ffa5d..33a62d0 100755

//Synthetic comment -- @@ -130,6 +130,16 @@
return mAbiType;
}

/** Convenience function to return a more user friendly name of the abi type. */
public static String getPrettyAbiType(String raw) {
String s = null;
//Synthetic comment -- @@ -146,42 +156,6 @@
}

/**
    * Returns the emulator executable path
    * @param sdkPath path of the sdk
    * @return path of the emulator executable
    */
    public String getEmulatorPath(String sdkPath) {
        String path = sdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER;

        // Start with base name of the emulator
        path = path + SdkConstants.FN_EMULATOR;

        // If not using ARM, add processor type to emulator command line
        boolean useAbi = !getAbiType().equalsIgnoreCase(SdkConstants.ABI_ARMEABI);

        if (useAbi) {
            path = path + "-" + getAbiType();   //$NON-NLS-1$
        }
        // Add OS appropriate emulator extension (e.g., .exe on windows)
        path = path + SdkConstants.FN_EMULATOR_EXTENSION;

        // HACK: The AVD manager should look for "emulator" or for "emulator-abi" (if not arm).
        // However this is a transition period and we don't have that unified "emulator" binary
        // in AOSP so if we can't find the generic one, look for an abi-specific one with the
        // special case that the armeabi one is actually named emulator-arm.
        // TODO remove this kludge once no longer necessary.
        if (!useAbi && !(new File(path).isFile())) {
            path = sdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER;
            path = path + SdkConstants.FN_EMULATOR;
            path = path + "-"                                                    //$NON-NLS-1$
                        + getAbiType().replace(SdkConstants.ABI_ARMEABI, "arm"); //$NON-NLS-1$
            path = path + SdkConstants.FN_EMULATOR_EXTENSION;
        }

        return path;
    }

    /**
* Returns the target hash string.
*/
public String getTargetHash() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index c9a3561..05a4d63 100644

//Synthetic comment -- @@ -71,6 +71,12 @@
*/
public final static String AVD_INI_ABI_TYPE = "abi.type"; //$NON-NLS-1$


/**
* AVD/config.ini key name representing the SDK-relative path of the skin folder, if any,
//Synthetic comment -- @@ -573,6 +579,18 @@
// Now the abi type
values.put(AVD_INI_ABI_TYPE, abiType);

// Now the skin.
if (skinName == null || skinName.length() == 0) {
skinName = target.getDefaultSkin();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 66cf0f4..bd21492 100644

//Synthetic comment -- @@ -47,7 +47,8 @@
public enum ValueType {
INTEGER("integer"),
BOOLEAN("boolean"),
        DISKSIZE("diskSize");

private String mValue;

//Synthetic comment -- @@ -98,8 +99,9 @@
return mDescription;
}

        public boolean isValid() {
            return mName != null && mType != null;
}
}

//Synthetic comment -- @@ -128,6 +130,7 @@
if (HW_PROP_NAME.equals(valueName)) {
prop = new HardwareProperty();
prop.mName = value;
}

if (prop == null) {
//Synthetic comment -- @@ -145,11 +148,6 @@
} else if (HW_PROP_DESC.equals(valueName)) {
prop.mDescription = value;
}

                        if (prop.isValid()) {
                            map.put(prop.mName, prop);
                        }

} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
file.getAbsolutePath(), line);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index c4b92b5..a8faeda 100755

//Synthetic comment -- @@ -304,11 +304,8 @@
}
}

        final String emulatorBinName =
            SdkConstants.FN_EMULATOR + SdkConstants.FN_EMULATOR_EXTENSION;

if (!names.contains(SdkConstants.androidCmdName()) ||
                !names.contains(emulatorBinName)) {
return null;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 0932378..98a7286 100644

//Synthetic comment -- @@ -305,8 +305,8 @@

//ABI group
label = new Label(parent, SWT.NONE);
        label.setText("ABI:");
        tooltip = "The ABI to use in the virtual device";
label.setToolTipText(tooltip);

mAbiTypeCombo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
//Synthetic comment -- @@ -792,11 +792,17 @@
mProperties.clear();

if (props != null) {
            mProperties.putAll(props);
}

// Cleanup known non-hardware properties
mProperties.remove(AvdManager.AVD_INI_ABI_TYPE);
mProperties.remove(AvdManager.AVD_INI_SKIN_PATH);
mProperties.remove(AvdManager.AVD_INI_SKIN_NAME);
mProperties.remove(AvdManager.AVD_INI_SDCARD_SIZE);
//Synthetic comment -- @@ -804,6 +810,7 @@
mProperties.remove(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
mProperties.remove(AvdManager.AVD_INI_IMAGES_1);
mProperties.remove(AvdManager.AVD_INI_IMAGES_2);
mHardwareViewer.refresh();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 2167248..a99a304 100644

//Synthetic comment -- @@ -66,7 +66,7 @@

if (mAvdInfo != null) {
displayValue(c, "Name:", mAvdInfo.getName());
            displayValue(c, "ABI:", AvdInfo.getPrettyAbiType(mAvdInfo.getAbiType()));

displayValue(c, "Path:", mAvdInfo.getDataFolderPath());

//Synthetic comment -- @@ -103,6 +103,7 @@
HashMap<String, String> copy = new HashMap<String, String>(properties);
// remove stuff we already displayed (or that we don't want to display)
copy.remove(AvdManager.AVD_INI_ABI_TYPE);
copy.remove(AvdManager.AVD_INI_SKIN_NAME);
copy.remove(AvdManager.AVD_INI_SKIN_PATH);
copy.remove(AvdManager.AVD_INI_SDCARD_SIZE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index bbf17fa..637a109 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
//Synthetic comment -- @@ -373,7 +374,7 @@
final TableColumn column3 = new TableColumn(mTable, SWT.NONE);
column3.setText("API Level");
final TableColumn column4 = new TableColumn(mTable, SWT.NONE);
        column4.setText("ABI");

adjustColumnsWidth(mTable, column0, column1, column2, column3, column4);
setupSelectionListener(mTable);
//Synthetic comment -- @@ -1032,7 +1033,9 @@
AvdStartDialog dialog = new AvdStartDialog(mTable.getShell(), avdInfo, mOsSdkPath,
mController);
if (dialog.open() == Window.OK) {
            String path = avdInfo.getEmulatorPath(mOsSdkPath + File.separator);

final String avdName = avdInfo.getName();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java
//Synthetic comment -- index ac2a38f..d92e0fb 100644

//Synthetic comment -- @@ -64,7 +64,8 @@
// simple list for index->name resolution.
final ArrayList<String> indexToName = new ArrayList<String>();
for (Entry<String, HardwareProperty> entry : mProperties.entrySet()) {
            if (mExceptProperties.contains(entry.getKey()) == false) {
c.add(entry.getValue().getAbstract());
indexToName.add(entry.getKey());
}







