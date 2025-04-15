/*Add snapshot handling for AVD creation, details, and launch

Change-Id:I5bc94c316e550b2585ca80185a02ffbe6d3e8401*/
//Synthetic comment -- diff --git a/androidprefs/src/com/android/prefs/AndroidLocation.java b/androidprefs/src/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index 9a537d5..613cc99 100644

//Synthetic comment -- @@ -82,6 +82,13 @@
}

/**
* Checks a list of system properties and/or system environment variables for validity, and
* existing director, and returns the first one.
* @param names








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java b/sdkmanager/app/src/com/android/sdkmanager/CommandLineProcessor.java
//Synthetic comment -- index 5276bed..2fba95f 100644

//Synthetic comment -- @@ -801,6 +801,7 @@
* Internal helper to define a new argument for a give action.
*
* @param mode The {@link Mode} for the argument.
* @param verb The verb name. Can be #INTERNAL_VERB.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
* @param shortName The one-letter short argument name. Cannot be empty nor null.








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index ddb7979..216421f 100644

//Synthetic comment -- @@ -133,6 +133,11 @@
};
}

/**
* Init the application by making sure the SDK path is available and
* doing basic parsing of the SDK.
//Synthetic comment -- @@ -790,73 +795,85 @@
private void displayAvdList() {
try {
AvdManager avdManager = new AvdManager(mSdkManager, mSdkLog);

            mSdkLog.printf("Available Android Virtual Devices:\n");

            AvdInfo[] avds = avdManager.getValidAvds();
            for (int index = 0 ; index < avds.length ; index++) {
                AvdInfo info = avds[index];
                if (index > 0) {
                    mSdkLog.printf("---------\n");
                }
                mSdkLog.printf("    Name: %s\n", info.getName());
                mSdkLog.printf("    Path: %s\n", info.getPath());

                // get the target of the AVD
                IAndroidTarget target = info.getTarget();
                if (target.isPlatform()) {
                    mSdkLog.printf("  Target: %s (API level %s)\n", target.getName(),
                            target.getVersion().getApiString());
                } else {
                    mSdkLog.printf("  Target: %s (%s)\n", target.getName(), target
                            .getVendor());
                    mSdkLog.printf("          Based on Android %s (API level %s)\n",
                            target.getVersionName(), target.getVersion().getApiString());
                }

                // display some extra values.
                Map<String, String> properties = info.getProperties();
                if (properties != null) {
                    String skin = properties.get(AvdManager.AVD_INI_SKIN_NAME);
                    if (skin != null) {
                        mSdkLog.printf("    Skin: %s\n", skin);
                    }
                    String sdcard = properties.get(AvdManager.AVD_INI_SDCARD_SIZE);
                    if (sdcard == null) {
                        sdcard = properties.get(AvdManager.AVD_INI_SDCARD_PATH);
                    }
                    if (sdcard != null) {
                        mSdkLog.printf("  Sdcard: %s\n", sdcard);
                    }
                }
            }

            // Are there some unused AVDs?
            AvdInfo[] badAvds = avdManager.getBrokenAvds();

            if (badAvds.length == 0) {
                return;
            }

            mSdkLog.printf("\nThe following Android Virtual Devices could not be loaded:\n");
            boolean needSeparator = false;
            for (AvdInfo info : badAvds) {
                if (needSeparator) {
                    mSdkLog.printf("---------\n");
                }
                mSdkLog.printf("    Name: %s\n", info.getName() == null ? "--" : info.getName());
                mSdkLog.printf("    Path: %s\n", info.getPath() == null ? "--" : info.getPath());

                String error = info.getErrorMessage();
                mSdkLog.printf("   Error: %s\n", error == null ? "Uknown error" : error);
                needSeparator = true;
            }
} catch (AndroidLocationException e) {
errorAndExit(e.getMessage());
}
}

/**
* Creates a new AVD. This is a text based creation with command line prompt.
*/
private void createAvd() {
//Synthetic comment -- @@ -969,6 +986,7 @@
mSdkCommandLine.getParamSdCard(),
hardwareConfig,
removePrevious,
mSdkLog);

} catch (AndroidLocationException e) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 2cc721d..35427f8 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
public static final String KEY_NO_HTTPS     = "no-https";
public static final String KEY_DRY_MODE     = "dry-mode";
public static final String KEY_OBSOLETE     = "obsolete";

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -164,6 +165,9 @@
define(Mode.BOOLEAN, false,
VERB_CREATE, OBJECT_AVD, "f", KEY_FORCE,
"Forces creation (overwrites an existing AVD)", false);

// --- delete avd ---

//Synthetic comment -- @@ -396,6 +400,11 @@
return ((Boolean) getValue(null, null, KEY_FORCE)).booleanValue();
}

// -- some helpers for avd action flags

/** Helper to retrieve the --rename value for a move verb. */








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7236aab

//Synthetic comment -- @@ -0,0 +1,178 @@








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
new file mode 100644
//Synthetic comment -- index 0000000..167618a

//Synthetic comment -- @@ -0,0 +1,99 @@








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java
//Synthetic comment -- index 8206e2a..d835154 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
public class SdkCommandLineTest extends TestCase {

private StdSdkLog mLog;
    
/**
* A mock version of the {@link SdkCommandLine} class that does not
* exits and discards its stdout/stderr output.
//Synthetic comment -- @@ -32,7 +32,7 @@
public static class MockSdkCommandLine extends SdkCommandLine {
private boolean mExitCalled;
private boolean mHelpCalled;
        
public MockSdkCommandLine(ISdkLog logger) {
super(logger);
}
//Synthetic comment -- @@ -48,12 +48,12 @@
protected void exit() {
mExitCalled = true;
}
        
@Override
protected void stdout(String format, Object... args) {
// discard
}
        
@Override
protected void stderr(String format, Object... args) {
// discard
//Synthetic comment -- @@ -62,7 +62,7 @@
public boolean wasExitCalled() {
return mExitCalled;
}
        
public boolean wasHelpCalled() {
return mHelpCalled;
}
//Synthetic comment -- @@ -139,4 +139,30 @@
assertEquals("target", c.getDirectObject());
assertFalse(c.isVerbose());
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 27d849c..fd6b4b1 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
//Synthetic comment -- @@ -108,6 +109,13 @@
* @see #AVD_INI_IMAGES_1
*/
public final static String AVD_INI_IMAGES_2 = "image.sysdir.2"; //$NON-NLS-1$

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
//Synthetic comment -- @@ -117,6 +125,7 @@
private final static String USERDATA_IMG = "userdata.img"; //$NON-NLS-1$
private final static String CONFIG_INI = "config.ini"; //$NON-NLS-1$
private final static String SDCARD_IMG = "sdcard.img"; //$NON-NLS-1$

private final static String INI_EXTENSION = ".ini"; //$NON-NLS-1$
private final static Pattern INI_NAME_PATTERN = Pattern.compile("(.+)\\" + //$NON-NLS-1$
//Synthetic comment -- @@ -486,6 +495,19 @@
}

/**
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
//Synthetic comment -- @@ -496,13 +518,14 @@
*        an existing sdcard image or a sdcard size (\d+, \d+K, \dM).
* @param hardwareConfig the hardware setup for the AVD. Can be null to use defaults.
* @param removePrevious If true remove any previous files.
* @param log the log object to receive action logs. Cannot be null.
* @return The new {@link AvdInfo} in case of success (which has just been added to the
*         internal list) or null in case of failure.
*/
public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target,
String skinName, String sdcard, Map<String,String> hardwareConfig,
            boolean removePrevious, ISdkLog log) {
if (log == null) {
throw new IllegalArgumentException("log cannot be null");
}
//Synthetic comment -- @@ -549,20 +572,9 @@
needCleanup = true;
return null;
}

            FileInputStream fis = new FileInputStream(userdataSrc);

File userdataDest = new File(avdFolder, USERDATA_IMG);
            FileOutputStream fos = new FileOutputStream(userdataDest);

            byte[] buffer = new byte[4096];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }

            fos.close();
            fis.close();

// Config file.
HashMap<String, String> values = new HashMap<String, String>();
//Synthetic comment -- @@ -572,6 +584,22 @@
return null;
}

// Now the skin.
if (skinName == null || skinName.length() == 0) {
skinName = target.getDefaultSkin();
//Synthetic comment -- @@ -804,6 +832,28 @@
return null;
}

/**
* Returns the path to the target images folder as a relative path to the SDK, if the folder
* is not empty. If the image folder is empty or does not exist, <code>null</code> is returned.
//Synthetic comment -- @@ -1203,6 +1253,8 @@
}
}

AvdStatus status;

if (avdPath == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index f32279c..edd7f12 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
//Synthetic comment -- @@ -73,8 +73,8 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
* AVD creator dialog.
//Synthetic comment -- @@ -139,6 +139,7 @@
}
}
};

/**
* Callback when the AVD name is changed.
//Synthetic comment -- @@ -316,6 +317,21 @@
mSdCardSizeRadio.setSelection(true);
enableSdCardWidgets(true);

// --- skin group
label = new Label(parent, SWT.NONE);
label.setText("Skin:");
//Synthetic comment -- @@ -989,6 +1005,7 @@
}

boolean force = mForceCreation.getSelection();

boolean success = false;
AvdInfo avdInfo = mAvdManager.createAvd(
//Synthetic comment -- @@ -999,6 +1016,7 @@
sdName,
mProperties,
force,
log);

success = avdInfo != null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index a845056..409c25d 100644

//Synthetic comment -- @@ -127,6 +127,11 @@
displayValue(c, "SD Card:", sdcard);
}

// display other hardware
HashMap<String, String> copy = new HashMap<String, String>(properties);
// remove stuff we already displayed (or that we don't want to display)








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 7962f04..82d0ee6 100644

//Synthetic comment -- @@ -1007,6 +1007,14 @@
if (dialog.getWipeData()) {
list.add("-wipe-data");                   //$NON-NLS-1$
}
float scale = dialog.getScale();
if (scale != 0.f) {
// do the rounding ourselves. This is because %.1f will write .4899 as .4








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index a2a9218..151cbfd 100644

//Synthetic comment -- @@ -84,6 +84,9 @@
private String mSkinDisplay;
private boolean mEnableScaling = true;
private Label mScaleField;

AvdStartDialog(Shell parentShell, AvdInfo avd, String sdkLocation,
SettingsController settingsController) {
//Synthetic comment -- @@ -242,6 +245,36 @@
}
});

l = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = 2;
//Synthetic comment -- @@ -520,4 +553,25 @@

return false;
}
}







