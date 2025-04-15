/*Add snapshot handling for AVD creation, details, and launch

Change-Id:I5bc94c316e550b2585ca80185a02ffbe6d3e8401*/




//Synthetic comment -- diff --git a/androidprefs/src/com/android/prefs/AndroidLocation.java b/androidprefs/src/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index 9a537d5..c36048a 100644

//Synthetic comment -- @@ -82,6 +82,13 @@
}

/**
     * Resets the folder used to store android related files. For testing.
     */
    public final static void resetFolder() {
        sPrefsLocation = null;
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
     * @param mandatory The argument is required (never if {@link Mode.BOOLEAN})
* @param verb The verb name. Can be #INTERNAL_VERB.
* @param directObject The action name. Can be #NO_VERB_OBJECT or #INTERNAL_FLAG.
* @param shortName The one-letter short argument name. Cannot be empty nor null.








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index ddb7979..216421f 100644

//Synthetic comment -- @@ -133,6 +133,11 @@
};
}

    /** For testing */
    public void setLogger(ISdkLog logger) {
        mSdkLog = logger;
    }

/**
* Init the application by making sure the SDK path is available and
* doing basic parsing of the SDK.
//Synthetic comment -- @@ -790,73 +795,85 @@
private void displayAvdList() {
try {
AvdManager avdManager = new AvdManager(mSdkManager, mSdkLog);
            displayAvdList(avdManager);
} catch (AndroidLocationException e) {
errorAndExit(e.getMessage());
}
}

/**
     * Displays the list of available AVDs.
     *
     * @param avdManager
     */
    public void displayAvdList(AvdManager avdManager) {
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
                String snapshot = properties.get(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
                if (snapshot != null) {
                    mSdkLog.printf("Snapshot: %s\n", snapshot);
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
    }

    /**
* Creates a new AVD. This is a text based creation with command line prompt.
*/
private void createAvd() {
//Synthetic comment -- @@ -969,6 +986,7 @@
mSdkCommandLine.getParamSdCard(),
hardwareConfig,
removePrevious,
                    mSdkCommandLine.getFlagSnapshot(),
mSdkLog);

} catch (AndroidLocationException e) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 2cc721d..2d3c7c7 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
public static final String KEY_NO_HTTPS     = "no-https";
public static final String KEY_DRY_MODE     = "dry-mode";
public static final String KEY_OBSOLETE     = "obsolete";
    public static final String KEY_SNAPSHOT     = "snapshot";

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -164,6 +165,9 @@
define(Mode.BOOLEAN, false,
VERB_CREATE, OBJECT_AVD, "f", KEY_FORCE,
"Forces creation (overwrites an existing AVD)", false);
        define(Mode.BOOLEAN, false,
               VERB_CREATE, OBJECT_AVD, "a", KEY_SNAPSHOT,
               "Place a snapshots file  in the AVD, to enable persistence.", false);

// --- delete avd ---

//Synthetic comment -- @@ -396,6 +400,11 @@
return ((Boolean) getValue(null, null, KEY_FORCE)).booleanValue();
}

    /** Helper to retrieve the --snapshot flag. */
    public boolean getFlagSnapshot() {
        return ((Boolean) getValue(null, null, KEY_SNAPSHOT)).booleanValue();
    }

// -- some helpers for avd action flags

/** Helper to retrieve the --rename value for a move verb. */








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7236aab

//Synthetic comment -- @@ -0,0 +1,178 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkmanager;

import static java.io.File.createTempFile;

import com.android.prefs.AndroidLocation;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

public class AvdManagerTest extends TestCase {

    private AvdManager mAvdManager;
    private SdkManager mSdkManager;
    private MockLog mLog;
    private File mFakeSdk;
    private File mAvdFolder;
    private IAndroidTarget mTarget;

    public static class MockLog implements ISdkLog {
        public Vector<String> messages = new Vector<String>();

        private void add(String code, String format, Object... args) {
            messages.add(new Formatter().format(code + format, args).toString());
        }

        @Override
        public void warning(String format, Object... args) {
            add("W ", format, args);
        }

        @Override
        public void printf(String format, Object... args) {
            add("P ", format, args);
        }

        @Override
        public void error(Throwable t, String format, Object... args) {
            if (t != null) {
                add("T", "%s", t.toString());
            }
            add("E ", format, args);
        }
    }

    public static void deleteDir(File root) {
        if (root.exists()) {
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
            }
            root.delete();
        }
    }

    @Override
    public void setUp() throws Exception {
        mLog = new MockLog();
        mFakeSdk = makeFakeSdk(createTempFile(this.getClass().getSimpleName(), null));
        mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
        assertNotNull("sdkManager location was invalid", mSdkManager);

        mAvdManager = new AvdManager(mSdkManager, mLog);
        mAvdFolder = new File(mFakeSdk, "avdData");
        mTarget = mSdkManager.getTargets()[0];
    }

    @Override
    public void tearDown() throws Exception {
        deleteDir(mFakeSdk);
    }

    public void testCreateAvdWithoutSnapshot() {
        mAvdManager.createAvd(
                mAvdFolder, this.getName(), mTarget, null, null, null, false, false, mLog);

        assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0\n]",
                mLog.messages.toString());
        assertTrue("Expected config.ini in " + mAvdFolder,
                new File(mAvdFolder, "config.ini").exists());
        Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(mAvdFolder, "config.ini"), mLog);
        assertEquals("HVGA", map.get("skin.name"));
        assertEquals("platforms/v0_0/skins/HVGA", map.get("skin.path"));
        assertEquals("platforms/v0_0/images/", map.get("image.sysdir.1"));
        assertEquals(null, map.get("snapshot.present"));
        assertTrue("Expected userdata.img in " + mAvdFolder,
                new File(mAvdFolder, "userdata.img").exists());
        assertFalse("Expected NO snapshots.img in " + mAvdFolder,
                new File(mAvdFolder, "snapshots.img").exists());
    }

    public void testCreateAvdWithSnapshot() {
        mAvdManager.createAvd(
                mAvdFolder, this.getName(), mTarget, null, null, null, false, true, mLog);

        assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0\n]",
                mLog.messages.toString());
        assertTrue("Expected snapshots.img in " + mAvdFolder,
                new File(mAvdFolder, "snapshots.img").exists());
        Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(mAvdFolder, "config.ini"), mLog);
        assertEquals("true", map.get("snapshot.present"));
    }

    /**
     * Build enough of a skeleton SDK to make the tests pass.
     *<p>
     * Ideally this wouldn't touch the file system, but I'm not inclined to
     * fiddle around with mock file systems just at the moment.
     *
     * @return an sdk manager to a fake sdk
     * @throws IOException
     */
    public static File makeFakeSdk(File fakeSdk) throws IOException {
        fakeSdk.delete();
        fakeSdk.mkdirs();
        AndroidLocation.resetFolder();
        System.setProperty("user.home", fakeSdk.getAbsolutePath());
        File addonsDir = new File(fakeSdk, SdkConstants.FD_ADDONS);
        addonsDir.mkdir();
        File toolsLibDir = new File(fakeSdk, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER);
        toolsLibDir.mkdirs();
        new File(toolsLibDir, "snapshots.img").createNewFile();
        File platformsDir = new File(fakeSdk, SdkConstants.FD_PLATFORMS);

        // Creating a fake target here on down
        File targetDir = new File(platformsDir, "v0_0");
        targetDir.mkdirs();
        new File(targetDir, SdkConstants.FN_FRAMEWORK_LIBRARY).createNewFile();
        new File(targetDir, SdkConstants.FN_FRAMEWORK_AIDL).createNewFile();
        new File(targetDir, SdkConstants.FN_SOURCE_PROP).createNewFile();
        File buildProp = new File(targetDir, SdkConstants.FN_BUILD_PROP);
        FileWriter out = new FileWriter(buildProp);
        out.write(SdkManager.PROP_VERSION_RELEASE + "=0.0\n");
        out.write(SdkManager.PROP_VERSION_SDK + "=0\n");
        out.write(SdkManager.PROP_VERSION_CODENAME + "=REL\n");
        out.close();
        File imagesDir = new File(targetDir, "images");
        imagesDir.mkdirs();
        new File(imagesDir, "userdata.img").createNewFile();
        File skinsDir = new File(targetDir, "skins");
        File hvgaDir = new File(skinsDir, "HVGA");
        hvgaDir.mkdirs();
        return fakeSdk;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2d7b743

//Synthetic comment -- @@ -0,0 +1,97 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkmanager;


import static java.io.File.createTempFile;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;

import java.io.File;

import junit.framework.TestCase;

public class MainTest extends TestCase {

    private File mFakeSdk;
    private AvdManagerTest.MockLog mLog;
    private SdkManager mSdkManager;
    private AvdManager mAvdManager;
    private File mAvdFolder;
    private IAndroidTarget mTarget;
    private File fakeSdkDir;

    @Override
    public void setUp() throws Exception {
        mLog = new AvdManagerTest.MockLog();
        fakeSdkDir = createTempFile(this.getClass().getSimpleName() + "_" + this.getName(), null);
        mFakeSdk = AvdManagerTest.makeFakeSdk(fakeSdkDir);
        mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
        assertNotNull("sdkManager location was invalid", mSdkManager);

        mAvdManager = new AvdManager(mSdkManager, mLog);
        mAvdFolder = new File(mFakeSdk, "avdData");
        mTarget = mSdkManager.getTargets()[0];
    }

    @Override
    public void tearDown() throws Exception {
        AvdManagerTest.deleteDir(mFakeSdk);
    }

    public void txestDisplayEmptyAvdList() {
        Main main = new Main();
        main.setLogger(mLog);
        mLog.messages.clear();
        main.displayAvdList(mAvdManager);
        assertEquals(1, mLog.messages.size());
        assertEquals("P Available Android Virtual Devices:\n", mLog.messages.get(0));
    }

    public void testDisplayAvdListOfOneNonSnapshot() {
        Main main = new Main();
        main.setLogger(mLog);
        mAvdManager.createAvd(
                mAvdFolder, this.getName(), mTarget, null, null, null, false, false, mLog);
        mLog.messages.clear();
        main.displayAvdList(mAvdManager);
        assertEquals(5, mLog.messages.size());
        assertEquals("P Available Android Virtual Devices:\n", mLog.messages.get(0));
        assertEquals("P     Name: " + this.getName() + "\n", mLog.messages.get(1));
        assertEquals("P     Path: " + mAvdFolder + "\n", mLog.messages.get(2));
        assertEquals("P   Target: Android 0.0 (API level 0)\n", mLog.messages.get(3));
        assertEquals("P     Skin: HVGA\n", mLog.messages.get(4));
    }

    public void testDisplayAvdListOfOneSnapshot() {
        Main main = new Main();
        main.setLogger(mLog);
        mAvdManager.createAvd(
                mAvdFolder, this.getName(), mTarget, null, null, null, false, true, mLog);
        mLog.messages.clear();
        main.displayAvdList(mAvdManager);
        assertEquals(6, mLog.messages.size());
        assertEquals("P Available Android Virtual Devices:\n", mLog.messages.get(0));
        assertEquals("P     Name: " + this.getName() + "\n", mLog.messages.get(1));
        assertEquals("P     Path: " + mAvdFolder + "\n", mLog.messages.get(2));
        assertEquals("P   Target: Android 0.0 (API level 0)\n", mLog.messages.get(3));
        assertEquals("P     Skin: HVGA\n", mLog.messages.get(4));
        assertEquals("P Snapshot: true\n", mLog.messages.get(5));
    }
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java
//Synthetic comment -- index 8206e2a..27599ab 100644

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

    public final void testCreate_Avd() {
        MockSdkCommandLine c = new MockSdkCommandLine(mLog);
        c.parseArgs(new String[] { "create", "avd", "-t", "android-100", "-n", "argh" });
        assertFalse(c.wasHelpCalled());
        assertFalse(c.wasExitCalled());
        assertEquals("create", c.getVerb());
        assertEquals("avd", c.getDirectObject());
        assertFalse(c.getFlagSnapshot());
        assertEquals("android-100", c.getParamTargetId());
        assertEquals("argh", c.getParamName());
        assertFalse(c.isVerbose());
    }

    public final void testCreate_Avd_Snapshot() {
        MockSdkCommandLine c = new MockSdkCommandLine(mLog);
        c.parseArgs(new String[] { "create", "avd", "-t", "android-100", "-n", "argh", "-a" });
        assertFalse(c.wasHelpCalled());
        assertFalse(c.wasExitCalled());
        assertEquals("create", c.getVerb());
        assertEquals("avd", c.getDirectObject());
        assertTrue(c.getFlagSnapshot());
        assertEquals("android-100", c.getParamTargetId());
        assertEquals("argh", c.getParamName());
        assertFalse(c.isVerbose());
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 27d849c..fd6b4b1 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
//Synthetic comment -- @@ -108,6 +109,13 @@
* @see #AVD_INI_IMAGES_1
*/
public final static String AVD_INI_IMAGES_2 = "image.sysdir.2"; //$NON-NLS-1$
    /**
     * AVD/config.ini key name representing the presence of the snapshots file.
     * This property is for UI purposes only. It is not used by the emulator.
     *
     * @see #SNAPSHOTS_IMG
     */
    public final static String AVD_INI_SNAPSHOT_PRESENT = "snapshot.present"; //$NON-NLS-1$

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
//Synthetic comment -- @@ -117,6 +125,7 @@
private final static String USERDATA_IMG = "userdata.img"; //$NON-NLS-1$
private final static String CONFIG_INI = "config.ini"; //$NON-NLS-1$
private final static String SDCARD_IMG = "sdcard.img"; //$NON-NLS-1$
    private final static String SNAPSHOTS_IMG = "snapshots.img"; //$NON-NLS-1$

private final static String INI_EXTENSION = ".ini"; //$NON-NLS-1$
private final static Pattern INI_NAME_PATTERN = Pattern.compile("(.+)\\" + //$NON-NLS-1$
//Synthetic comment -- @@ -486,6 +495,19 @@
}

/**
     * Creates a new AVD, but with no snapshot.
     *
     * See {@link #createAvd(File, String, IAndroidTarget, String, String, Map, boolean, boolean, ISdkLog)}
     **/
    @Deprecated
    public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target,
        String skinName, String sdcard, Map<String,String> hardwareConfig,
        boolean removePrevious, ISdkLog log) {
      return createAvd(avdFolder, name, target, skinName, sdcard, hardwareConfig,
          removePrevious, false, log);
    }

    /**
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
//Synthetic comment -- @@ -496,13 +518,14 @@
*        an existing sdcard image or a sdcard size (\d+, \d+K, \dM).
* @param hardwareConfig the hardware setup for the AVD. Can be null to use defaults.
* @param removePrevious If true remove any previous files.
     * @param createSnapshot If true copy a blank snapshot image into the AVD.
* @param log the log object to receive action logs. Cannot be null.
* @return The new {@link AvdInfo} in case of success (which has just been added to the
*         internal list) or null in case of failure.
*/
public AvdInfo createAvd(File avdFolder, String name, IAndroidTarget target,
String skinName, String sdcard, Map<String,String> hardwareConfig,
            boolean removePrevious, boolean createSnapshot, ISdkLog log) {
if (log == null) {
throw new IllegalArgumentException("log cannot be null");
}
//Synthetic comment -- @@ -549,20 +572,9 @@
needCleanup = true;
return null;
}
File userdataDest = new File(avdFolder, USERDATA_IMG);

            copyImageFile(userdataSrc, userdataDest);

// Config file.
HashMap<String, String> values = new HashMap<String, String>();
//Synthetic comment -- @@ -572,6 +584,22 @@
return null;
}

            // Create the snapshot file
            if (createSnapshot) {
                String toolsLib = mSdkManager.getLocation() + File.separator
                        + SdkConstants.OS_SDK_TOOLS_LIB_FOLDER;
                File snapshotBlank = new File(toolsLib, SNAPSHOTS_IMG);
                if (snapshotBlank.exists() == false) {
                    log.error(null, "Unable to find a '%2$s%1$s' file to copy into the AVD folder.",
                            SNAPSHOTS_IMG, toolsLib);
                    needCleanup = true;
                    return null;
                }
                File snapshotDest = new File(avdFolder, SNAPSHOTS_IMG);
                copyImageFile(snapshotBlank, snapshotDest);
                values.put(AVD_INI_SNAPSHOT_PRESENT, "true");
            }

// Now the skin.
if (skinName == null || skinName.length() == 0) {
skinName = target.getDefaultSkin();
//Synthetic comment -- @@ -804,6 +832,28 @@
return null;
}

    /** Copy the nominated file to the given destination.
     * @param source
     * @param destination
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void copyImageFile(File source, File destination)
            throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);

        byte[] buffer = new byte[4096];
        int count;
        while ((count = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, count);
        }

        fos.close();
        fis.close();
    }

/**
* Returns the path to the target images folder as a relative path to the SDK, if the folder
* is not empty. If the image folder is empty or does not exist, <code>null</code> is returned.
//Synthetic comment -- @@ -1203,6 +1253,8 @@
}
}

        // TODO: What about missing sdcard, skins, etc?

AvdStatus status;

if (avdPath == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index f32279c..edd7f12 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
//Synthetic comment -- @@ -73,8 +73,8 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
* AVD creator dialog.
//Synthetic comment -- @@ -139,6 +139,7 @@
}
}
};
    private Button mSnapshotTick;

/**
* Callback when the AVD name is changed.
//Synthetic comment -- @@ -316,6 +317,21 @@
mSdCardSizeRadio.setSelection(true);
enableSdCardWidgets(true);

        // --- snapshot group

        label = new Label(parent, SWT.NONE);
        label.setText("Snapshot:");
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING,
            false, false));

        final Group snapshotGroup = new Group(parent, SWT.NONE);
        snapshotGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        snapshotGroup.setLayout(new GridLayout(3, false));

        mSnapshotTick = new Button(snapshotGroup, SWT.CHECK);
        mSnapshotTick.setText("Enabled");
        mSnapshotTick.setToolTipText("Emulator's state will be persisted between emulator executions");

// --- skin group
label = new Label(parent, SWT.NONE);
label.setText("Skin:");
//Synthetic comment -- @@ -989,6 +1005,7 @@
}

boolean force = mForceCreation.getSelection();
        boolean snapshot = mSnapshotTick.getSelection();

boolean success = false;
AvdInfo avdInfo = mAvdManager.createAvd(
//Synthetic comment -- @@ -999,6 +1016,7 @@
sdName,
mProperties,
force,
                snapshot,
log);

success = avdInfo != null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index a845056..409c25d 100644

//Synthetic comment -- @@ -127,6 +127,11 @@
displayValue(c, "SD Card:", sdcard);
}

                    String snapshot = properties.get(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
                    if (snapshot != null) {
                        displayValue(c, "Snapshot:", snapshot);
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
            if (dialog.getHasSnapshot()) {
                if (!dialog.getSnapshotLaunchData()) {
                    list.add("-no-snapshot-load");
                }
                if (!dialog.getSnapshotSaveData()) {
                    list.add("-no-snapshot-save");
                }
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
    private boolean mHasSnapshot = true;
    private boolean mLaunchSnapshot = true;
    private boolean mSaveSnapshot = true;

AvdStartDialog(Shell parentShell, AvdInfo avd, String sdkLocation,
SettingsController settingsController) {
//Synthetic comment -- @@ -242,6 +245,36 @@
}
});

        Map<String, String> prop = mAvd.getProperties();
        String snapshotPresent = prop.get(AvdManager.AVD_INI_SNAPSHOT_PRESENT);
        mHasSnapshot = snapshotPresent.equals("true");

        final Button snapshotLaunchButton = new Button(parent, SWT.CHECK);
        snapshotLaunchButton.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
        gd.horizontalSpan = 2;
        snapshotLaunchButton.setText("Launch from snapshot");
        snapshotLaunchButton.setSelection(mHasSnapshot);
        snapshotLaunchButton.setEnabled(mHasSnapshot);
        snapshotLaunchButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mLaunchSnapshot = snapshotLaunchButton.getSelection();
            }
        });

        final Button snapshotSaveButton = new Button(parent, SWT.CHECK);
        snapshotSaveButton.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
        gd.horizontalSpan = 2;
        snapshotSaveButton.setText("Save to snapshot");
        snapshotSaveButton.setSelection(mHasSnapshot);
        snapshotSaveButton.setEnabled(mHasSnapshot);
        snapshotSaveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mSaveSnapshot = snapshotSaveButton.getSelection();
            }
        });

l = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = 2;
//Synthetic comment -- @@ -520,4 +553,25 @@

return false;
}

    /**
     * @return Whether there's a snapshot file available.
     */
    public boolean getHasSnapshot() {
        return mHasSnapshot;
    }

    /**
     * @return Whether to launch and load snapshot.
     */
    public boolean getSnapshotLaunchData() {
        return mLaunchSnapshot;
    }

    /**
     * @return Whether to preserve emulator state to snapshot.
     */
    public boolean getSnapshotSaveData() {
        return mSaveSnapshot;
    }
}







