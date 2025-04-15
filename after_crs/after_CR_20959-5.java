/*AVD and ADT eclipse plugin to support processor-specific platform images and emulators.

This is a first (and largest) patch in a series of patches over the next month to extend the
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
                osSdkLocation + getOsRelativeEmulator() + SdkConstants.FN_EMULATOR_EXTENSION
};
for (String file : filesToCheck) {
if (checkFile(file) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 6ee74ed..38b26e5 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.xml.ManifestData;
//Synthetic comment -- @@ -1187,7 +1188,16 @@
// build the command line based on the available parameters.
ArrayList<String> list = new ArrayList<String>();

        String path = AdtPlugin.getOsAbsoluteEmulator();
        // If not using ARM, add processor type to emulator command line
        if(!avdToLaunch.getProcessorType().equalsIgnoreCase(SdkConstants.ARM_PROCESSOR)) {
            path = path + "-" + avdToLaunch.getProcessorType();
        }
        // Add OS appropriate emulator extension (e.g., .exe on windows)
        path = path + SdkConstants.FN_EMULATOR_EXTENSION;

        list.add(path);

list.add(FLAG_AVD);
list.add(avdToLaunch.getName());

//Synthetic comment -- @@ -1661,4 +1671,3 @@
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 1fe6d97..7e26723 100644

//Synthetic comment -- @@ -981,16 +981,21 @@
oldAvdInfo = avdManager.getAvd(avdName, false /*validAvdOnly*/);
}

            // NOTE: need to update with command line processor selectivity
            String preferredProcessor = "armeabi";
            //target.setProcessorType(preferredProcessor); //following review

@SuppressWarnings("unused") // newAvdInfo is never read, yet useful for debugging
AvdInfo newAvdInfo = avdManager.createAvd(avdFolder,
                        avdName,
                        target,
		        preferredProcessor,
                        skin,
                        mSdkCommandLine.getParamSdCard(),
                        hardwareConfig,
                        removePrevious,
                        mSdkCommandLine.getFlagSnapshot(),
                        mSdkLog);

} catch (AndroidLocationException e) {
errorAndExit(e.getMessage());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AddOnTarget.java
//Synthetic comment -- index f3da39c..b97abb1 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;

/**
* Represents an add-on target in the SDK.
//Synthetic comment -- @@ -67,6 +68,7 @@
private final String mLocation;
private final PlatformTarget mBasePlatform;
private final String mName;
    private String[] mAbis;
private final String mVendor;
private final int mRevision;
private final String mDescription;
//Synthetic comment -- @@ -74,6 +76,7 @@
private String mDefaultSkin;
private IOptionalLibrary[] mLibraries;
private int mVendorId = NO_USB_ID;
    private boolean mCompatibility;

/**
* Creates a new add-on
//Synthetic comment -- @@ -86,8 +89,8 @@
* library name. The value is a 2 string array with the .jar filename, and the description.
* @param basePlatform the platform the add-on is extending.
*/
    AddOnTarget(String location, String name, String vendor, int revision,
            String description, Map<String, String[]> libMap, PlatformTarget basePlatform) {
if (location.endsWith(File.separator) == false) {
location = location + File.separator;
}
//Synthetic comment -- @@ -99,6 +102,28 @@
mDescription = description;
mBasePlatform = basePlatform;

	//compute the Abi types
	ArrayList list = new ArrayList();
        File imagesFolder = new File(mLocation + SdkConstants.OS_IMAGES_FOLDER);
        File[] files = imagesFolder.listFiles();

        if(files != null) {
            // Loop through Images directory.  If subdirectories exist, set multiprocessor mode
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file.getName());
                }
            }
	}
        //check for previous API levels and set mAbis accordingly
        if(files != null && list.size() == 0) {
            mCompatibility = true;
            mAbis = new String[] { SdkConstants.ARM_PROCESSOR };
        } else {
            mAbis = new String[list.size()];
            list.toArray(mAbis);
        }

// handle the optional libraries.
if (libMap != null) {
mLibraries = new IOptionalLibrary[libMap.size()];
//Synthetic comment -- @@ -121,6 +146,19 @@
return mName;
}

    public String getPathForAbi(String abiType) {
        if(mCompatibility) {
        // Use legacy directory structure if only arm
            return ""; //$NON-NLS-1$
        } else {
            return abiType + File.separator;
          }
    }

    public String[] getAbiList() {
        return mAbis;
    }

public String getVendor() {
return mVendor;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/IAndroidTarget.java
//Synthetic comment -- index c0dcaa7..fe6b1ff 100644

//Synthetic comment -- @@ -227,6 +227,17 @@
int getUsbVendorId();

/**
    * Returns array of permitted processor architectures
    * <p/>If a single Processor is permitted, returns empty array
    */
    public String[] getAbiList();

    /**
    * Returns string to append to images directory for current ProcessorType
    */
    public String getPathForAbi(String abiType);

    /**
* Returns whether the given target is compatible with the receiver.
* <p/>
* This means that a project using the receiver's target can run on the given target.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 6aeeade..d11480e 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

/**
* Represents a platform target in the SDK.
//Synthetic comment -- @@ -43,6 +44,8 @@
private final Map<String, String> mProperties;
private final SparseArray<String> mPaths = new SparseArray<String>();
private String[] mSkins;
    private String[] mAbis;
    private boolean mCompatibility;


/**
//Synthetic comment -- @@ -56,8 +59,9 @@
* @param revision the revision of the platform component.
*/
@SuppressWarnings("deprecation")
    PlatformTarget(String sdkOsPath, String platformOSPath,
            Map<String, String> properties, int apiLevel, String codeName,
            String versionName, int revision) {
if (platformOSPath.endsWith(File.separator) == false) {
platformOSPath = platformOSPath + File.separator;
}
//Synthetic comment -- @@ -112,6 +116,45 @@
SdkConstants.FN_DX);
mPaths.put(DX_JAR, sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_LIB_FOLDER +
SdkConstants.FN_DX_JAR);

        //compute the Abi types
	ArrayList list = new ArrayList();
        File imagesFolder = new File(platformOSPath + SdkConstants.OS_IMAGES_FOLDER);
        File[] files = imagesFolder.listFiles();

        if(files != null) {
            // Loop through Images directory.  If subdirectories exist, set multiprocessor mode
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file.getName());
                }
            }
	}
        //check for previous API levels and set mAbis accordingly
        if(files != null && list.size() == 0) {
	    mCompatibility = true;
            mAbis = new String[] { SdkConstants.ARM_PROCESSOR };
        } else {
            mAbis = new String[list.size()];
            list.toArray(mAbis);
        }

    }

    public String getPathForAbi(String abiType) {
        if(mCompatibility) {
            // Use legacy directory structure if only arm is supported
            return "";
        } else {
               return abiType + File.separator; //following review
        }
    }

    /**
    * Retrieve and return the list of abis
    */
    public String[] getAbiList() {
        return mAbis;
}

public String getLocation() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index f8f7f19..68c73de 100644

//Synthetic comment -- @@ -130,9 +130,13 @@
public final static String FN_ADB = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
"adb.exe" : "adb"; //$NON-NLS-1$ //$NON-NLS-2$

    /** emulator executable (_WITHOUT_ extension for the current OS) */
    public final static String FN_EMULATOR =
            "emulator"; //$NON-NLS-1$ //$NON-NLS-2$

    /** emulator executable extension for the current OS */
    public final static String FN_EMULATOR_EXTENSION = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
            ".exe" : ""; //$NON-NLS-1$ //$NON-NLS-2$

/** zipalign executable (with extension for the current OS)  */
public final static String FN_ZIPALIGN = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
//Synthetic comment -- @@ -223,10 +227,16 @@
public static final String FD_DOCS_REFERENCE = "reference";
/** Name of the SDK images folder. */
public final static String FD_IMAGES = "images";
    /** Name of the processors to support. */
    public final static String ARM_PROCESSOR = "armeabi";
    public final static String INTEL_ATOM_PROCESSOR = "x86";

/** Name of the SDK skins folder. */
public final static String FD_SKINS = "skins";
/** Name of the SDK samples folder. */
public final static String FD_SAMPLES = "samples";
    /** Name of the SDK extras folder. */
    public final static String FD_EXTRAS = "extras";
/** Name of the SDK templates folder, i.e. "templates" */
public final static String FD_TEMPLATES = "templates";
/** Name of the SDK Ant folder, i.e. "ant" */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index eba8e07..36de184 100644

//Synthetic comment -- @@ -66,6 +66,13 @@
public final static String AVD_INFO_TARGET = "target";     //$NON-NLS-1$

/**
     * AVD/config.ini key name representing the processor type of the specific avd
     *
     */
     public final static String AVD_INI_ABI_TYPE = "proc.arch"; //$NON-NLS-1$


    /**
* AVD/config.ini key name representing the SDK-relative path of the skin folder, if any,
* or a 320x480 like constant for a numeric skin size.
*
//Synthetic comment -- @@ -175,6 +182,7 @@
private final String mPath;
private final String mTargetHash;
private final IAndroidTarget mTarget;
	private final String mAbiType;
private final Map<String, String> mProperties;
private final AvdStatus mStatus;

//Synthetic comment -- @@ -188,11 +196,12 @@
* @param path The path to the config.ini file
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
	 * @param processorType The processorType.
* @param properties The property map. Cannot be null.
*/
	public AvdInfo(String name, String path, String targetHash, IAndroidTarget target, String processorType,
Map<String, String> properties) {
             this(name, path, targetHash, target, processorType, properties, AvdStatus.OK);
}

/**
//Synthetic comment -- @@ -205,15 +214,17 @@
* @param path The path to the config.ini file
* @param targetHash the target hash
* @param target The target. Can be null, if the target was not resolved.
	 * @param processorType The processorType.
* @param properties The property map. Can be null.
* @param status The {@link AvdStatus} of this AVD. Cannot be null.
*/
        public AvdInfo(String name, String path, String targetHash, IAndroidTarget target, String processorType,
Map<String, String> properties, AvdStatus status) {
mName = name;
mPath = path;
mTargetHash = targetHash;
mTarget = target;
	    mAbiType = processorType;
mProperties = properties == null ? null : Collections.unmodifiableMap(properties);
mStatus = status;
}
//Synthetic comment -- @@ -228,6 +239,26 @@
return mPath;
}

        /** Returns the processor type of the AVD. */
	public String getProcessorType() {
            return mAbiType;
        }

        /** Convenience function to return a more user friendly name of the abi type. */
	public static String getPrettyProcessorType(String raw) {
            String s = null;
            if(raw.equalsIgnoreCase(SdkConstants.ARM_PROCESSOR))  {
                s = "ARM (" + SdkConstants.ARM_PROCESSOR + ")";
            }
            if(raw.equalsIgnoreCase(SdkConstants.INTEL_ATOM_PROCESSOR))  {
                s = new String("Intel Atom (" + SdkConstants.INTEL_ATOM_PROCESSOR + ")");
            }
            if(s == null) {
                s = raw + " (" + raw + ")";
            }
            return s;
        }

/**
* Returns the target hash string.
*/
//Synthetic comment -- @@ -500,10 +531,10 @@
* See {@link #createAvd(File, String, IAndroidTarget, String, String, Map, boolean, boolean, ISdkLog)}
**/
@Deprecated
   public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target, String processorType, String skinName,
String sdcard, Map<String, String> hardwareConfig, boolean removePrevious,
ISdkLog log) {
       return createAvd(avdFolder, name, target, processorType, skinName, sdcard, hardwareConfig, removePrevious,
false, log);
}

//Synthetic comment -- @@ -513,6 +544,7 @@
* @param avdFolder the data folder for the AVD. It will be created as needed.
* @param name the name of the AVD
* @param target the target of the AVD
     * @param processorType the processor type of the AVD
* @param skinName the name of the skin. Can be null. Must have been verified by caller.
* @param sdcard the parameter value for the sdCard. Can be null. This is either a path to
*        an existing sdcard image or a sdcard size (\d+, \d+K, \dM).
//Synthetic comment -- @@ -523,9 +555,9 @@
* @return The new {@link AvdInfo} in case of success (which has just been added to the
*         internal list) or null in case of failure.
*/
     public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target,
           String processorType, String skinName, String sdcard, Map<String,String> hardwareConfig,
           boolean removePrevious, boolean createSnapshot, ISdkLog log){
if (log == null) {
throw new IllegalArgumentException("log cannot be null");
}
//Synthetic comment -- @@ -558,17 +590,20 @@
iniFile = createAvdIniFile(name, avdFolder, target);

// writes the userdata.img in it.
            String imagePath = target.getPath(IAndroidTarget.IMAGES) + target.getPathForAbi(processorType);

File userdataSrc = new File(imagePath, USERDATA_IMG);

if (userdataSrc.exists() == false && target.isPlatform() == false) {
                imagePath =
                    target.getParent().getPath(IAndroidTarget.IMAGES) + target.getPathForAbi(processorType);
userdataSrc = new File(imagePath, USERDATA_IMG);
}

if (userdataSrc.exists() == false) {
                log.error(null,
			"Unable to find a '%1$s' file of '%2$s' to copy into the AVD folder.",
                        USERDATA_IMG, imagePath);
needCleanup = true;
return null;
}
//Synthetic comment -- @@ -576,12 +611,20 @@

copyImageFile(userdataSrc, userdataDest);

	    if (userdataDest.exists() == false) {
                log.error(null, "Unable to create '%1$s' file in the AVD folder.",
                        userdataDest);
                needCleanup = true;
                return null;
            }

// Config file.
HashMap<String, String> values = new HashMap<String, String>();

           if (setImagePathProperties(target, processorType, values, log) == false) {
               log.error(null, "Failed to set image path properties in the AVD folder.");
               needCleanup = true;
               return null;
}

// Create the snapshot file
//Synthetic comment -- @@ -590,7 +633,7 @@
+ SdkConstants.OS_SDK_TOOLS_LIB_EMULATOR_FOLDER;
File snapshotBlank = new File(toolsLib, SNAPSHOTS_IMG);
if (snapshotBlank.exists() == false) {
                   log.error(null, "Unable to find a '%2$s%1$s' file to copy into the AVD folder.",
SNAPSHOTS_IMG, toolsLib);
needCleanup = true;
return null;
//Synthetic comment -- @@ -600,6 +643,9 @@
values.put(AVD_INI_SNAPSHOT_PRESENT, "true");
}

	    // Now the processor type
            values.put(AVD_INI_ABI_TYPE, processorType);

// Now the skin.
if (skinName == null || skinName.length() == 0) {
skinName = target.getDefaultSkin();
//Synthetic comment -- @@ -616,6 +662,7 @@
// assume skin name is valid
String skinPath = getSkinRelativePath(skinName, target, log);
if (skinPath == null) {
		    log.error(null, "Missing skinpath in the AVD folder.");
needCleanup = true;
return null;
}
//Synthetic comment -- @@ -688,6 +735,7 @@
}

if (createSdCard(mkSdCard.getAbsolutePath(), sdcard, path, log) == false) {
			    log.error(null, "Failed to create sdcard in the AVD folder.");
needCleanup = true;
return null; // mksdcard output has already been displayed, no need to
// output anything else.
//Synthetic comment -- @@ -763,6 +811,7 @@
report.append(String.format("Created AVD '%1$s' based on %2$s (%3$s)", name,
target.getName(), target.getVendor()));
}
	    report.append(String.format(", %s processor", AvdInfo.getPrettyProcessorType(processorType)));

// display the chosen hardware config
if (finalHardwareValues.size() > 0) {
//Synthetic comment -- @@ -780,7 +829,7 @@
AvdInfo newAvdInfo = new AvdInfo(name,
avdFolder.getAbsolutePath(),
target.hashString(),
                    target, processorType, values);

AvdInfo oldAvdInfo = getAvd(name, false /*validAvdOnly*/);

//Synthetic comment -- @@ -859,9 +908,9 @@
* is not empty. If the image folder is empty or does not exist, <code>null</code> is returned.
* @throws InvalidTargetPathException if the target image folder is not in the current SDK.
*/
    private String getImageRelativePath(IAndroidTarget target, String abiType)
throws InvalidTargetPathException {
        String imageFullPath = target.getPath(IAndroidTarget.IMAGES) + target.getPathForAbi(abiType);

// make this path relative to the SDK location
String sdkLocation = mSdkManager.getLocation();
//Synthetic comment -- @@ -1070,7 +1119,7 @@

// update AVD info
AvdInfo info = new AvdInfo(avdInfo.getName(), paramFolderPath,
                      avdInfo.getTargetHash(), avdInfo.getTarget(), avdInfo.getProcessorType(), avdInfo.getProperties());
replaceAvd(avdInfo, info);

// update the ini file
//Synthetic comment -- @@ -1090,7 +1139,7 @@

// update AVD info
AvdInfo info = new AvdInfo(newName, avdInfo.getPath(),
                       avdInfo.getTargetHash(), avdInfo.getTarget(), avdInfo.getProcessorType(), avdInfo.getProperties());
replaceAvd(avdInfo, info);
}

//Synthetic comment -- @@ -1233,7 +1282,15 @@
name = matcher.group(1);
}

	// get processor type
        String processorType = properties.get(AVD_INI_ABI_TYPE);
       // for the avds created previously without enhancement, i.e. They are created based on previous API Levels.
       //They are supposed to have ARM processor type
       if(processorType == null) {
          processorType = SdkConstants.ARM_PROCESSOR;
       }

	// check the image.sysdir are valid
boolean validImageSysdir = true;
if (properties != null) {
String imageSysDir = properties.get(AVD_INI_IMAGES_1);
//Synthetic comment -- @@ -1278,6 +1335,7 @@
avdPath,
targetHash,
target,
		processorType,
properties,
status);

//Synthetic comment -- @@ -1489,7 +1547,7 @@
AvdStatus status;

// create the path to the new system images.
        if (setImagePathProperties(avd.getTarget(), avd.getProcessorType(), properties, log)) {
if (properties.containsKey(AVD_INI_IMAGES_1)) {
log.printf("Updated '%1$s' with value '%2$s'\n", AVD_INI_IMAGES_1,
properties.get(AVD_INI_IMAGES_1));
//Synthetic comment -- @@ -1521,6 +1579,7 @@
avd.getPath(),
avd.getTargetHash(),
avd.getTarget(),
		avd.getProcessorType(),
properties,
status);

//Synthetic comment -- @@ -1530,13 +1589,15 @@
/**
* Sets the paths to the system images in a properties map.
* @param target the target in which to find the system images.
     * @param processorType the processor type of the avd to find
     *        the architecture-dependent system images.
* @param properties the properties in which to set the paths.
* @param log the log object to receive action logs. Cannot be null.
* @return true if success, false if some path are missing.
*/
private boolean setImagePathProperties(IAndroidTarget target,
         String processorType,
	 Map<String, String> properties, ISdkLog log) {
properties.remove(AVD_INI_IMAGES_1);
properties.remove(AVD_INI_IMAGES_2);

//Synthetic comment -- @@ -1544,7 +1605,7 @@
String property = AVD_INI_IMAGES_1;

// First the image folders of the target itself
            String imagePath = getImageRelativePath(target, processorType);
if (imagePath != null) {
properties.put(property, imagePath);
property = AVD_INI_IMAGES_2;
//Synthetic comment -- @@ -1554,7 +1615,7 @@
// If the target is an add-on we need to add the Platform image as a backup.
IAndroidTarget parent = target.getParent();
if (parent != null) {
                imagePath = getImageRelativePath(parent, processorType);
if (imagePath != null) {
properties.put(property, imagePath);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 4411034..4f1b9af 100644

//Synthetic comment -- @@ -102,6 +102,9 @@
private Text mAvdName;
private Combo mTargetCombo;

    private Combo mProcessorTypeCombo;
    private String mProcessorType;

private Button mSdCardSizeRadio;
private Text mSdCardSize;
private Combo mSdCardSizeCombo;
//Synthetic comment -- @@ -285,10 +288,29 @@
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
reloadSkinCombo();
		reloadProcessorTypeCombo();
validatePage();
}
});

	//Processor Type group
        label = new Label(parent, SWT.NONE);
        label.setText("Processor Type:");
        tooltip = "The Processor Type to use in the virtual device";
        label.setToolTipText(tooltip);

         mProcessorTypeCombo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
         mProcessorTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
         mProcessorTypeCombo.setToolTipText(tooltip);
         mProcessorTypeCombo.addSelectionListener(new SelectionAdapter() {
		 @Override
		 public void widgetSelected(SelectionEvent e) {
                     super.widgetSelected(e);
                     validatePage();
                 }
         });
         mProcessorTypeCombo.setEnabled(false);

// --- sd card group
label = new Label(parent, SWT.NONE);
label.setText("SD Card:");
//Synthetic comment -- @@ -668,6 +690,21 @@
for (int i = 0;i < n; i++) {
if (target.equals(mCurrentTargets.get(mTargetCombo.getItem(i)))) {
mTargetCombo.select(i);
		    reloadProcessorTypeCombo();
                    reloadSkinCombo();
                    break;
                }
            }
        }

        // select the processor type
        if(target.getAbiList().length > 1) {
            mProcessorTypeCombo.setEnabled(true);
            String processorType = AvdInfo.getPrettyProcessorType(mEditAvdInfo.getProcessorType());
            int n = mProcessorTypeCombo.getItemCount();
            for (int i = 0;i < n; i++) {
                if (processorType.equals(mProcessorTypeCombo.getItem(i))) {
                    mProcessorTypeCombo.select(i);
reloadSkinCombo();
break;
}
//Synthetic comment -- @@ -742,6 +779,7 @@
mProperties.putAll(props);

// Cleanup known non-hardware properties
        mProperties.remove(AvdManager.AVD_INI_ABI_TYPE);
mProperties.remove(AvdManager.AVD_INI_SKIN_PATH);
mProperties.remove(AvdManager.AVD_INI_SKIN_NAME);
mProperties.remove(AvdManager.AVD_INI_SDCARD_SIZE);
//Synthetic comment -- @@ -881,6 +919,50 @@
}
}

    private void reloadProcessorTypeCombo() {
       String selected = null;
       boolean found = false;

       int index = mTargetCombo.getSelectionIndex();
       if (index >= 0) {
           String targetName = mTargetCombo.getItem(index);
           IAndroidTarget target = mCurrentTargets.get(targetName);
           String[] arches=target.getAbiList();
           if(arches.length > 1) {
              mProcessorTypeCombo.setEnabled(true);

              index = mProcessorTypeCombo.getSelectionIndex();
              if (index >= 0) {
                  selected = mProcessorTypeCombo.getItem(index);
              }

              mProcessorTypeCombo.removeAll();
              mProcessorTypeCombo.setEnabled(false);

              int i;
              for ( i=0; i < arches.length ; i++ ) {
                  mProcessorTypeCombo.add(AvdInfo.getPrettyProcessorType(arches[i]));
              }
              index = -1;
              for (String processorType : mProcessorTypeCombo.getItems()) {
                   if (!found) {
                       index++;
                       found = processorType.equals(selected);
                   }
              }

              mProcessorTypeCombo.setEnabled(true);

              if (found) {
                  mProcessorTypeCombo.select(index);
              }
           } else {
                mProcessorTypeCombo.removeAll();
                mProcessorTypeCombo.setEnabled(false);
             }
       }
    }

/**
* Validates the fields, displays errors and warnings.
* Enables the finish button if there are no errors.
//Synthetic comment -- @@ -905,7 +987,17 @@
error = "A target must be selected in order to create an AVD.";
}

        // validate processor type if the selected target supports multi archs.
        if (hasAvdName && error == null && mTargetCombo.getSelectionIndex() > 0) {
            int index = mTargetCombo.getSelectionIndex();
            String targetName = mTargetCombo.getItem(index);
            IAndroidTarget target = mCurrentTargets.get(targetName);
            if(target.getAbiList().length > 1 && mProcessorTypeCombo.getSelectionIndex() < 0) {
               error = "A processor type must be selected in order to create an AVD.";
            }
        }

	// Validate SDCard path or value
if (error == null) {
// get the mode. We only need to check the file since the
// verifier on the size Text will prevent invalid input
//Synthetic comment -- @@ -1102,6 +1194,21 @@
return false;
}

        // get the processor type
        mProcessorType = SdkConstants.ARM_PROCESSOR;
        if(target.getAbiList().length > 1) {
           int processorIndex = mProcessorTypeCombo.getSelectionIndex();
           if (processorIndex >= 0) {
               String prettyname = mProcessorTypeCombo.getItem(processorIndex);
               //Extract the processor type
               int firstIndex = prettyname.indexOf("(");
               int lastIndex = prettyname.indexOf(")");
               mProcessorType = prettyname.substring(firstIndex+1, lastIndex);

               //target.setProcessorType(mProcessorType); //following review
           }
        }

// get the SD card data from the UI.
String sdName = null;
if (mSdCardSizeRadio.getSelection()) {
//Synthetic comment -- @@ -1169,6 +1276,7 @@
avdFolder,
avdName,
target,
		mProcessorType,
skinName,
sdName,
mProperties,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 409c25d..4f4a4ac 100644

//Synthetic comment -- @@ -101,6 +101,8 @@

if (mAvdInfo != null) {
displayValue(c, "Name:", mAvdInfo.getName());
	    displayValue(c, "Processor Type:", AvdInfo.getPrettyProcessorType(mAvdInfo.getProcessorType()));

displayValue(c, "Path:", mAvdInfo.getPath());

if (mAvdInfo.getStatus() != AvdStatus.OK) {
//Synthetic comment -- @@ -135,6 +137,7 @@
// display other hardware
HashMap<String, String> copy = new HashMap<String, String>(properties);
// remove stuff we already displayed (or that we don't want to display)
                    copy.remove(AvdManager.AVD_INI_ABI_TYPE);
copy.remove(AvdManager.AVD_INI_SKIN_NAME);
copy.remove(AvdManager.AVD_INI_SKIN_PATH);
copy.remove(AvdManager.AVD_INI_SDCARD_SIZE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 5e26a41..f9e0419 100644

//Synthetic comment -- @@ -372,8 +372,10 @@
column2.setText("Platform");
final TableColumn column3 = new TableColumn(mTable, SWT.NONE);
column3.setText("API Level");
	final TableColumn column4 = new TableColumn(mTable, SWT.NONE);
        column4.setText("Processor Type");

        adjustColumnsWidth(mTable, column0, column1, column2, column3, column4);
setupSelectionListener(mTable);
fillTable(mTable);
setEnabled(true);
//Synthetic comment -- @@ -633,16 +635,18 @@
final TableColumn column0,
final TableColumn column1,
final TableColumn column2,
            final TableColumn column3,
            final TableColumn column4) {
// Add a listener to resize the column to the full width of the table
table.addControlListener(new ControlAdapter() {
@Override
public void controlResized(ControlEvent e) {
Rectangle r = table.getClientArea();
                column0.setWidth(r.width * 20 / 100); // 20%
                column1.setWidth(r.width * 30 / 100); // 30%
column2.setWidth(r.width * 15 / 100); // 15%
column3.setWidth(r.width * 15 / 100); // 15%
		column4.setWidth(r.width * 20 / 100); // 22%
}
});
}
//Synthetic comment -- @@ -777,10 +781,12 @@
item.setText(1, target.getFullName());
item.setText(2, target.getVersionName());
item.setText(3, target.getVersion().getApiString());
			item.setText(4, AvdInfo.getPrettyProcessorType(avd.getProcessorType()));
} else {
item.setText(1, "?");
item.setText(2, "?");
item.setText(3, "?");
			item.setText(4, "?");
}
}
}
//Synthetic comment -- @@ -1027,8 +1033,16 @@
if (dialog.open() == Window.OK) {
String path = mOsSdkPath +
File.separator +
                SdkConstants.OS_SDK_TOOLS_FOLDER;

            // Start with base name of the emulator
            path = path + SdkConstants.FN_EMULATOR;
            // If not using ARM, add processor type to emulator command line
            if(!avdInfo.getProcessorType().equalsIgnoreCase(SdkConstants.ARM_PROCESSOR)) {
                path = path + "-" + avdInfo.getProcessorType();
            }
            // Add OS appropriate emulator extension (e.g., .exe on windows)
            path = path + SdkConstants.FN_EMULATOR_EXTENSION;

final String avdName = avdInfo.getName();








