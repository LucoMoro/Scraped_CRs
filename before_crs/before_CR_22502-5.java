/*Fix SdkManager unit tests. Refactor AvdInfo.

A couple issues with the current unit tests:
- It was correctly allocating a temporary SDK folder.
- It was correctly creating temporary AVDs in a temp folder.
- However it was still using the regular AVD root for the
  AVD ini files and leaving them behind.
- Minor windows dir-sep issue.

This replaces the SdkManagerTestUtils class by a
new base TestClass specific to SDK testing that creates
both the temporary SDK and AVD manager and correctly overrides
the AVD root.

One issue is that we have different ways to find what the
correct AVD Root is and the default paths used for an AvdInfo
where setup using static methods, making them impossible to
override in unit tests.
This refactors AvdInfo out of AvdManager and most important
there's a single non-static method, AvdManager.getBaseAvdFolder(),
that is used to know where AVDs should be stored.

Change-Id:I4270203f52de15ca9418e9b4f1bf61dbc843c218*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3391ad4..d61150a 100644

//Synthetic comment -- @@ -26,9 +26,9 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectCreator;
import com.android.sdklib.internal.project.ProjectProperties;
//Synthetic comment -- @@ -897,7 +897,7 @@
mSdkLog.printf("---------\n");
}
mSdkLog.printf("    Name: %s\n", info.getName());
            mSdkLog.printf("    Path: %s\n", info.getPath());

// get the target of the AVD
IAndroidTarget target = info.getTarget();
//Synthetic comment -- @@ -946,7 +946,7 @@
mSdkLog.printf("---------\n");
}
mSdkLog.printf("    Name: %s\n", info.getName() == null ? "--" : info.getName());
            mSdkLog.printf("    Path: %s\n", info.getPath() == null ? "--" : info.getPath());

String error = info.getErrorMessage();
mSdkLog.printf("   Error: %s\n", error == null ? "Uknown error" : error);
//Synthetic comment -- @@ -1013,7 +1013,7 @@
if (paramFolderPath != null) {
avdFolder = new File(paramFolderPath);
} else {
                avdFolder = AvdManager.AvdInfo.getAvdFolder(avdName);
}

// Validate skin is either default (empty) or NNNxMMM or a valid skin name.
//Synthetic comment -- @@ -1139,7 +1139,7 @@
// check if paths are the same. Use File methods to account for OS idiosyncrasies.
try {
File f1 = new File(paramFolderPath).getCanonicalFile();
                    File f2 = new File(info.getPath()).getCanonicalFile();
if (f1.equals(f2)) {
// same canonical path, so not actually a move
paramFolderPath = null;
//Synthetic comment -- @@ -1165,7 +1165,7 @@
File originalFolder = new File(
AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD,
info.getName() + AvdManager.AVD_FOLDER_EXTENSION);
                if (originalFolder.equals(info.getPath())) {
try {
// The AVD is using the default data folder path based on the AVD name.
// That folder needs to be adjusted to use the new name.
//Synthetic comment -- @@ -1187,7 +1187,7 @@
}

File ini = info.getIniFile();
                if (ini.equals(AvdInfo.getIniFile(newName))) {
errorAndExit("The AVD file '%s' is in the way.", ini.getCanonicalPath());
return;
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
//Synthetic comment -- index 9579bf5..a5a8289 100644

//Synthetic comment -- @@ -16,49 +16,36 @@

package com.android.sdkmanager;

import static java.io.File.createTempFile;

import com.android.io.FileWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.SdkConstants;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

public class AvdManagerTest extends TestCase {

    private AvdManager mAvdManager;
    private SdkManager mSdkManager;
    private MockLog mLog;
    private File mFakeSdk;
    private File mAvdFolder;
private IAndroidTarget mTarget;

@Override
public void setUp() throws Exception {
        mLog = new MockLog();
        mFakeSdk = SdkManagerTestUtil.makeFakeSdk(createTempFile(this.getClass().getSimpleName(), null));
        mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
        assertNotNull("sdkManager location was invalid", mSdkManager);

        mAvdManager = new AvdManager(mSdkManager, mLog);
        mAvdFolder = new File(mFakeSdk, "avdData");
        mTarget = mSdkManager.getTargets()[0];
}

@Override
public void tearDown() throws Exception {
        SdkManagerTestUtil.deleteDir(mFakeSdk);
}

public void testCreateAvdWithoutSnapshot() {
        mAvdManager.createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -69,17 +56,17 @@
false,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0, ARM (armeabi) processor\n]",
                mLog.toString());
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
//Synthetic comment -- @@ -89,7 +76,7 @@

public void testCreateAvdWithSnapshot() {

        mAvdManager.createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -100,14 +87,14 @@
true,   // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0, ARM (armeabi) processor\n]",
                mLog.toString());
assertTrue("Expected snapshots.img in " + mAvdFolder,
new File(mAvdFolder, "snapshots.img").exists());
Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(mAvdFolder, "config.ini"), mLog);
assertEquals("true", map.get("snapshot.present"));
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 3acb01a..4a17e32 100644

//Synthetic comment -- @@ -19,9 +19,7 @@

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;
//Synthetic comment -- @@ -32,49 +30,36 @@
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

public class MainTest extends TestCase {

    private File mFakeSdk;
    private MockLog mLog;
    private SdkManager mSdkManager;
    private AvdManager mAvdManager;
    private File mAvdFolder;
private IAndroidTarget mTarget;
    private File fakeSdkDir;

@Override
public void setUp() throws Exception {
        mLog = new MockLog();
        fakeSdkDir = File.createTempFile(
                this.getClass().getSimpleName() + '_' + this.getName(), null);
        mFakeSdk = SdkManagerTestUtil.makeFakeSdk(fakeSdkDir);
        mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
        assertNotNull("sdkManager location was invalid", mSdkManager);

        mAvdManager = new AvdManager(mSdkManager, mLog);
        mAvdFolder = new File(mFakeSdk, "avdData");
        mTarget = mSdkManager.getTargets()[0];
}

@Override
public void tearDown() throws Exception {
        SdkManagerTestUtil.deleteDir(mFakeSdk);
}

public void testDisplayEmptyAvdList() {
Main main = new Main();
        main.setLogger(mLog);
        mLog.clear();
        main.displayAvdList(mAvdManager);
        assertEquals("P Available Android Virtual Devices:\n", mLog.toString());
}

public void testDisplayAvdListOfOneNonSnapshot() {
Main main = new Main();
        main.setLogger(mLog);
        mAvdManager.createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -85,10 +70,10 @@
false,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

        mLog.clear();
        main.displayAvdList(mAvdManager);
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -96,14 +81,14 @@
+ ", P   Target: Android 0.0 (API level 0)\n"
+ ", P     Skin: HVGA\n"
+ "]",
                mLog.toString());
}

public void testDisplayAvdListOfOneSnapshot() {
Main main = new Main();
        main.setLogger(mLog);

        mAvdManager.createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -114,10 +99,10 @@
true,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

        mLog.clear();
        main.displayAvdList(mAvdManager);
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -126,7 +111,7 @@
+ ", P     Skin: HVGA\n"
+ ", P Snapshot: true\n"
+ "]",
                mLog.toString());
}

public void testCheckFilterValues() {
//Synthetic comment -- @@ -178,7 +163,7 @@

// Finally check that checkFilterValues accepts all these values, one by one.
Main main = new Main();
        main.setLogger(mLog);

for (int step = 0; step < 3; step++) {
for (String value : expectedValues) {








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestCase.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestCase.java
new file mode 100755
//Synthetic comment -- index 0000000..9fdd852

//Synthetic comment -- @@ -0,0 +1,182 @@








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestUtil.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestUtil.java
deleted file mode 100644
//Synthetic comment -- index 96efb5c..0000000

//Synthetic comment -- @@ -1,88 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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

import com.android.prefs.AndroidLocation;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SdkManagerTestUtil {
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
        File toolsLibEmuDir = new File(fakeSdk, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
        toolsLibEmuDir.mkdirs();
        new File(toolsLibEmuDir, "snapshots.img").createNewFile();
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

    /**
     * Recursive delete directory. Mostly for fake SDKs.
     *
     * @param root directory to delete
     */
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

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..949b861

//Synthetic comment -- @@ -0,0 +1,332 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 627bdb9..cfb612b 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.util.Pair;

//Synthetic comment -- @@ -38,7 +38,6 @@
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
//Synthetic comment -- @@ -48,7 +47,7 @@
/**
* Android Virtual Device Manager to manage AVDs.
*/
public final class AvdManager {

/**
* Exception thrown when something is wrong with a target path.
//Synthetic comment -- @@ -132,11 +131,11 @@
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$

private final static String USERDATA_IMG = "userdata.img"; //$NON-NLS-1$
    private final static String CONFIG_INI = "config.ini"; //$NON-NLS-1$
private final static String SDCARD_IMG = "sdcard.img"; //$NON-NLS-1$
private final static String SNAPSHOTS_IMG = "snapshots.img"; //$NON-NLS-1$

    private final static String INI_EXTENSION = ".ini"; //$NON-NLS-1$
private final static Pattern INI_NAME_PATTERN = Pattern.compile("(.+)\\" + //$NON-NLS-1$
INI_EXTENSION + "$",                                               //$NON-NLS-1$
Pattern.CASE_INSENSITIVE);
//Synthetic comment -- @@ -192,305 +191,12 @@
CONFLICT_EXISTING_PATH,
}

    /**
     * An immutable structure describing an Android Virtual Device.
     */
    public static final class AvdInfo implements Comparable<AvdInfo> {

        /**
         * Status for an {@link AvdInfo}. Indicates whether or not this AVD is valid.
         */
        public static enum AvdStatus {
            /** No error */
            OK,
            /** Missing 'path' property in the ini file */
            ERROR_PATH,
            /** Missing config.ini file in the AVD data folder */
            ERROR_CONFIG,
            /** Missing 'target' property in the ini file */
            ERROR_TARGET_HASH,
            /** Target was not resolved from its hash */
            ERROR_TARGET,
            /** Unable to parse config.ini */
            ERROR_PROPERTIES,
            /** System Image folder in config.ini doesn't exist */
            ERROR_IMAGE_DIR;
        }

        private final String mName;
        private final String mPath;
        private final String mTargetHash;
        private final IAndroidTarget mTarget;
        private final String mAbiType;
        private final Map<String, String> mProperties;
        private final AvdStatus mStatus;

        /**
         * Creates a new valid AVD info. Values are immutable.
         * <p/>
         * Such an AVD is available and can be used.
         * The error string is set to null.
         *
         * @param name The name of the AVD (for display or reference)
         * @param path The path to the config.ini file
         * @param targetHash the target hash
         * @param target The target. Can be null, if the target was not resolved.
         * @param abiType Name of the abi.
         * @param properties The property map. Cannot be null.
         */
        public AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
                String abiType, Map<String, String> properties) {
             this(name, path, targetHash, target, abiType, properties, AvdStatus.OK);
        }

        /**
         * Creates a new <em>invalid</em> AVD info. Values are immutable.
         * <p/>
         * Such an AVD is not complete and cannot be used.
         * The error string must be non-null.
         *
         * @param name The name of the AVD (for display or reference)
         * @param path The path to the config.ini file
         * @param targetHash the target hash
         * @param target The target. Can be null, if the target was not resolved.
         * @param abiType Name of the abi.
         * @param properties The property map. Can be null.
         * @param status The {@link AvdStatus} of this AVD. Cannot be null.
         */
        public AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
                String abiType, Map<String, String> properties, AvdStatus status) {
            mName = name;
            mPath = path;
            mTargetHash = targetHash;
            mTarget = target;
            mAbiType = abiType;
            mProperties = properties == null ? null : Collections.unmodifiableMap(properties);
            mStatus = status;
        }

        /** Returns the name of the AVD. */
        public String getName() {
            return mName;
        }

        /** Returns the path of the AVD data directory. */
        public String getPath() {
            return mPath;
        }

        /** Returns the processor type of the AVD. */
        public String getAbiType() {
            return mAbiType;
        }

        /** Convenience function to return a more user friendly name of the abi type. */
        public static String getPrettyAbiType(String raw) {
            String s = null;
            if (raw.equalsIgnoreCase(SdkConstants.ABI_ARMEABI)) {
                s = "ARM (" + SdkConstants.ABI_ARMEABI + ")";
            }
            else if (raw.equalsIgnoreCase(SdkConstants.ABI_INTEL_ATOM)) {
                s = "Intel Atom (" + SdkConstants.ABI_INTEL_ATOM + ")";
            }
            else {
                s = raw + " (" + raw + ")";
            }
            return s;
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
                path = path + "-" + getAbiType();
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
                path = path + "-" + getAbiType().replace(SdkConstants.ABI_ARMEABI, "arm"); //$NON-NLS-1$
                path = path + SdkConstants.FN_EMULATOR_EXTENSION;
            }

            return path;
        }

        /**
         * Returns the target hash string.
         */
        public String getTargetHash() {
            return mTargetHash;
        }

        /** Returns the target of the AVD, or <code>null</code> if it has not been resolved. */
        public IAndroidTarget getTarget() {
            return mTarget;
        }

        /** Returns the {@link AvdStatus} of the receiver. */
        public AvdStatus getStatus() {
            return mStatus;
        }

        /**
         * Helper method that returns the default AVD folder that would be used for a given
         * AVD name <em>if and only if</em> the AVD was created with the default choice.
         * <p/>
         * Callers must NOT use this to "guess" the actual folder from an actual AVD since
         * the purpose of the AVD .ini file is to be able to change this folder.
         * <p/>
         * For an actual existing AVD, callers must use {@link #getPath()} instead.
         *
         * @throws AndroidLocationException if there's a problem getting android root directory.
         */
        public static File getAvdFolder(String name) throws AndroidLocationException {
            return new File(AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD,
                            name + AvdManager.AVD_FOLDER_EXTENSION);
        }

        /**
         * Helper method that returns the .ini {@link File} for a given AVD name.
         * @throws AndroidLocationException if there's a problem getting android root directory.
         */
        public static File getIniFile(String name) throws AndroidLocationException {
            String avdRoot = getBaseAvdFolder();
            return new File(avdRoot, name + INI_EXTENSION);
        }

        /**
         * Returns the .ini {@link File} for this AVD.
         * @throws AndroidLocationException if there's a problem getting android root directory.
         */
        public File getIniFile() throws AndroidLocationException {
            return getIniFile(mName);
        }

        /**
         * Helper method that returns the Config {@link File} for a given AVD name.
         */
        public static File getConfigFile(String path) {
            return new File(path, CONFIG_INI);
        }

        /**
         * Returns the Config {@link File} for this AVD.
         */
        public File getConfigFile() {
            return getConfigFile(mPath);
        }

        /**
         * Returns an unmodifiable map of properties for the AVD. This can be null.
         */
        public Map<String, String> getProperties() {
            return mProperties;
        }

        /**
         * Returns the error message for the AVD or <code>null</code> if {@link #getStatus()}
         * returns {@link AvdStatus#OK}
         */
        public String getErrorMessage() {
            try {
                switch (mStatus) {
                    case ERROR_PATH:
                        return String.format("Missing AVD 'path' property in %1$s", getIniFile());
                    case ERROR_CONFIG:
                        return String.format("Missing config.ini file in %1$s", mPath);
                    case ERROR_TARGET_HASH:
                        return String.format("Missing 'target' property in %1$s", getIniFile());
                    case ERROR_TARGET:
                        return String.format("Unknown target '%1$s' in %2$s",
                                mTargetHash, getIniFile());
                    case ERROR_PROPERTIES:
                        return String.format("Failed to parse properties from %1$s",
                                getConfigFile());
                    case ERROR_IMAGE_DIR:
                        return String.format(
                                "Invalid value in image.sysdir. Run 'android update avd -n %1$s'",
                                mName);
                    case OK:
                        assert false;
                        return null;
                }
            } catch (AndroidLocationException e) {
                return "Unable to get HOME folder.";
            }

            return null;
        }

        /**
         * Returns whether an emulator is currently running the AVD.
         */
        public boolean isRunning() {
            File f = new File(mPath, "userdata-qemu.img.lock");
            return f.isFile();
        }

        /**
         * Compares this object with the specified object for order. Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * @param o the Object to be compared.
         * @return a negative integer, zero, or a positive integer as this object is
         *         less than, equal to, or greater than the specified object.
         */
        public int compareTo(AvdInfo o) {
            // first handle possible missing targets (if the AVD failed to load for
            // unresolved targets.
            if (mTarget == null) {
                return +1;
            } else if (o.mTarget == null) {
                return -1;
            }

            // then compare the targets
            int targetDiff = mTarget.compareTo(o.mTarget);

            if (targetDiff == 0) {
                // same target? compare on the avd name
                return mName.compareTo(o.mName);
            }

            return targetDiff;
        }
    }

private final ArrayList<AvdInfo> mAllAvdList = new ArrayList<AvdInfo>();
private AvdInfo[] mValidAvdList;
private AvdInfo[] mBrokenAvdList;
private final SdkManager mSdkManager;

/**
     * Returns the base folder where AVDs are created.
     *
     * @throws AndroidLocationException
     */
    public static String getBaseAvdFolder() throws AndroidLocationException {
        return AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD;
    }

    /**
* Creates an AVD Manager for a given SDK represented by a {@link SdkManager}.
* @param sdkManager The SDK.
* @param log The log object to receive the log of the initial loading of the AVDs.
//Synthetic comment -- @@ -506,6 +212,15 @@
}

/**
* Returns the {@link SdkManager} associated with the {@link AvdManager}.
*/
public SdkManager getSdkManager() {
//Synthetic comment -- @@ -689,12 +404,12 @@
// Are some existing files/folders in the way of creating this AVD?

try {
            File file = AvdInfo.getIniFile(name);
if (file.exists()) {
return Pair.of(AvdConflict.CONFLICT_EXISTING_PATH, file.getPath());
}

            file = AvdInfo.getAvdFolder(name);
if (file.exists()) {
return Pair.of(AvdConflict.CONFLICT_EXISTING_PATH, file.getPath());
}
//Synthetic comment -- @@ -730,7 +445,9 @@
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
     * @param name the name of the AVD
* @param target the target of the AVD
* @param abiType the abi type of the AVD
* @param skinName the name of the skin. Can be null. Must have been verified by caller.
//Synthetic comment -- @@ -747,7 +464,7 @@
*/
public AvdInfo createAvd(
File avdFolder,
            String name,
IAndroidTarget target,
String abiType,
String skinName,
//Synthetic comment -- @@ -789,7 +506,7 @@
}

// actually write the ini file
            iniFile = createAvdIniFile(name, avdFolder, target, removePrevious);

// writes the userdata.img in it.
String imagePath = target.getImagePath(abiType);
//Synthetic comment -- @@ -1004,17 +721,17 @@
if (target.isPlatform()) {
if (editExisting) {
report.append(String.format("Updated AVD '%1$s' based on %2$s",
                            name, target.getName()));
} else {
report.append(String.format("Created AVD '%1$s' based on %2$s",
                            name, target.getName()));
}
} else {
if (editExisting) {
                    report.append(String.format("Updated AVD '%1$s' based on %2$s (%3$s)", name,
target.getName(), target.getVendor()));
} else {
                    report.append(String.format("Created AVD '%1$s' based on %2$s (%3$s)", name,
target.getName(), target.getVendor()));
}
}
//Synthetic comment -- @@ -1033,12 +750,14 @@
log.printf(report.toString());

// create the AvdInfo object, and add it to the list
            AvdInfo newAvdInfo = new AvdInfo(name,
avdFolder.getAbsolutePath(),
target.hashString(),
target, abiType, values);

            AvdInfo oldAvdInfo = getAvd(name, false /*validAvdOnly*/);

synchronized (mAllAvdList) {
if (oldAvdInfo != null && (removePrevious || editExisting)) {
//Synthetic comment -- @@ -1051,10 +770,10 @@
if ((removePrevious || editExisting) &&
newAvdInfo != null &&
oldAvdInfo != null &&
                    !oldAvdInfo.getPath().equals(newAvdInfo.getPath())) {
                log.warning("Removing previous AVD directory at %s", oldAvdInfo.getPath());
// Remove the old data directory
                File dir = new File(oldAvdInfo.getPath());
try {
deleteContentOf(dir);
dir.delete();
//Synthetic comment -- @@ -1222,7 +941,7 @@
IAndroidTarget target,
boolean removePrevious)
throws AndroidLocationException, IOException {
        File iniFile = AvdInfo.getIniFile(name);

if (removePrevious) {
if (iniFile.isFile()) {
//Synthetic comment -- @@ -1250,7 +969,7 @@
*/
private File createAvdIniFile(AvdInfo info) throws AndroidLocationException, IOException {
return createAvdIniFile(info.getName(),
                new File(info.getPath()),
info.getTarget(),
false /*removePrevious*/);
}
//Synthetic comment -- @@ -1283,7 +1002,7 @@
}
}

            String path = avdInfo.getPath();
if (path != null) {
f = new File(path);
if (f.exists()) {
//Synthetic comment -- @@ -1305,8 +1024,6 @@
return true;
}

        } catch (AndroidLocationException e) {
            log.error(e, null);
} catch (IOException e) {
log.error(e, null);
} catch (SecurityException e) {
//Synthetic comment -- @@ -1333,17 +1050,23 @@

try {
if (paramFolderPath != null) {
                File f = new File(avdInfo.getPath());
                log.warning("Moving '%1$s' to '%2$s'.", avdInfo.getPath(), paramFolderPath);
if (!f.renameTo(new File(paramFolderPath))) {
log.error(null, "Failed to move '%1$s' to '%2$s'.",
                            avdInfo.getPath(), paramFolderPath);
return false;
}

// update AVD info
                AvdInfo info = new AvdInfo(avdInfo.getName(), paramFolderPath,
                      avdInfo.getTargetHash(), avdInfo.getTarget(), avdInfo.getAbiType(), avdInfo.getProperties());
replaceAvd(avdInfo, info);

// update the ini file
//Synthetic comment -- @@ -1352,7 +1075,7 @@

if (newName != null) {
File oldIniFile = avdInfo.getIniFile();
                File newIniFile = AvdInfo.getIniFile(newName);

log.warning("Moving '%1$s' to '%2$s'.", oldIniFile.getPath(), newIniFile.getPath());
if (!oldIniFile.renameTo(newIniFile)) {
//Synthetic comment -- @@ -1362,9 +1085,14 @@
}

// update AVD info
                AvdInfo info = new AvdInfo(newName, avdInfo.getPath(),
                        avdInfo.getTargetHash(), avdInfo.getTarget(),
                        avdInfo.getAbiType(), avdInfo.getProperties());
replaceAvd(avdInfo, info);
}

//Synthetic comment -- @@ -1409,19 +1137,20 @@
* <p/>
* This lists the $HOME/.android/avd/<name>.ini files.
* Such files are properties file than then indicate where the AVD folder is located.
*
* @return A new {@link File} array or null. The array might be empty.
* @throws AndroidLocationException if there's a problem getting android root directory.
*/
private File[] buildAvdFilesList() throws AndroidLocationException {
        // get the Android prefs location.
        String avdRoot = AvdManager.getBaseAvdFolder();

// ensure folder validity.
        File folder = new File(avdRoot);
if (folder.isFile()) {
throw new AndroidLocationException(
                    String.format("%1$s is not a valid folder.", avdRoot));
} else if (folder.exists() == false) {
// folder is not there, we create it and return
folder.mkdirs();
//Synthetic comment -- @@ -1466,14 +1195,14 @@
/**
* Parses an AVD .ini file to create an {@link AvdInfo}.
*
     * @param path The path to the AVD .ini file
* @param log the log object to receive action logs. Cannot be null.
* @return A new {@link AvdInfo} with an {@link AvdStatus} indicating whether this AVD is
*         valid or not.
*/
    private AvdInfo parseAvdInfo(File path, ISdkLog log) {
Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(path),
log);

String avdPath = map.get(AVD_INFO_PATH);
//Synthetic comment -- @@ -1501,8 +1230,8 @@
}

// get name
        String name = path.getName();
        Matcher matcher = INI_NAME_PATTERN.matcher(path.getName());
if (matcher.matches()) {
name = matcher.group(1);
}
//Synthetic comment -- @@ -1557,6 +1286,7 @@

AvdInfo info = new AvdInfo(
name,
avdPath,
targetHash,
target,
//Synthetic comment -- @@ -1792,7 +1522,7 @@
}

// now write the config file
        File configIniFile = new File(avd.getPath(), CONFIG_INI);
writeIniFile(configIniFile, properties);

// finally create a new AvdInfo for this unbroken avd and add it to the list.
//Synthetic comment -- @@ -1801,7 +1531,8 @@
// FIXME: We may want to create this AvdInfo by reparsing the AVD instead. This could detect other errors.
AvdInfo newAvd = new AvdInfo(
avd.getName(),
                avd.getPath(),
avd.getTargetHash(),
avd.getTarget(),
avd.getAbiType(),








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java
//Synthetic comment -- index 7594f16..7c78983 100755

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.sdkuilib.internal.repository;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -58,7 +57,7 @@
try {
label.setText(String.format(
"List of existing Android Virtual Devices located at %s",
                    AvdManager.getBaseAvdFolder()));
} catch (AndroidLocationException e) {
label.setText(e.getMessage());
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index b545dbd..0932378 100644

//Synthetic comment -- @@ -22,10 +22,10 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -1312,7 +1312,7 @@

File avdFolder = null;
try {
            avdFolder = AvdManager.AvdInfo.getAvdFolder(avdName);
} catch (AndroidLocationException e) {
return false;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 46f3eaf..6a85c14 100644

//Synthetic comment -- @@ -18,9 +18,9 @@

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -103,7 +103,7 @@
displayValue(c, "Name:", mAvdInfo.getName());
displayValue(c, "ABI:", AvdInfo.getPrettyAbiType(mAvdInfo.getAbiType()));

            displayValue(c, "Path:", mAvdInfo.getPath());

if (mAvdInfo.getStatus() != AvdStatus.OK) {
displayValue(c, "Error:", mAvdInfo.getErrorMessage());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 86a094f..56f2c7e 100644

//Synthetic comment -- @@ -20,9 +20,9 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdkuilib.internal.repository.SettingsController;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 77f47d1..7731dc1 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;








