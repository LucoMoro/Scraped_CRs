/*Fix SdkManager unit tests.

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
correct AVD Root is. Most notable the current case failed
if one had the getenv ANDROID_SDK_HOME defined. Code that
relies on the static AvdManager.AvdInfo.getAvdFolder(avdName)
call will still end up using the non-temporary AVD root.

Change-Id:I4270203f52de15ca9418e9b4f1bf61dbc843c218*/




//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
//Synthetic comment -- index 9579bf5..3f69f22 100644

//Synthetic comment -- @@ -16,49 +16,35 @@

package com.android.sdkmanager;

import com.android.io.FileWrapper;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;

import java.io.File;
import java.util.Map;

public class AvdManagerTest extends AvdManagerTestCase {

private IAndroidTarget mTarget;
    private File mAvdFolder;

@Override
public void setUp() throws Exception {
        super.setUp();

        mTarget = getSdkManager().getTargets()[0];
        mAvdFolder = getAvdFolder(this.getName());
}

@Override
public void tearDown() throws Exception {
        super.tearDown();
}

public void testCreateAvdWithoutSnapshot() {

        getAvdManager().createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -69,17 +55,17 @@
false,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                getLog());

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0, ARM (armeabi) processor\n]",
                getLog().toString());
assertTrue("Expected config.ini in " + mAvdFolder,
new File(mAvdFolder, "config.ini").exists());
Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(mAvdFolder, "config.ini"), getLog());
assertEquals("HVGA", map.get("skin.name"));
        assertEquals("platforms/v0_0/skins/HVGA", map.get("skin.path").replace(File.separatorChar, '/'));
        assertEquals("platforms/v0_0/images/", map.get("image.sysdir.1").replace(File.separatorChar, '/'));
assertEquals(null, map.get("snapshot.present"));
assertTrue("Expected userdata.img in " + mAvdFolder,
new File(mAvdFolder, "userdata.img").exists());
//Synthetic comment -- @@ -89,7 +75,7 @@

public void testCreateAvdWithSnapshot() {

        getAvdManager().createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -100,14 +86,14 @@
true,   // createSnapshot
false,  // removePrevious
false,  // editExisting
                getLog());

assertEquals("[P Created AVD '" + this.getName() + "' based on Android 0.0, ARM (armeabi) processor\n]",
                getLog().toString());
assertTrue("Expected snapshots.img in " + mAvdFolder,
new File(mAvdFolder, "snapshots.img").exists());
Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(mAvdFolder, "config.ini"), getLog());
assertEquals("true", map.get("snapshot.present"));
}
}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTestCase.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTestCase.java
new file mode 100755
//Synthetic comment -- index 0000000..73c1d2c

//Synthetic comment -- @@ -0,0 +1,201 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test case that allocates a temporary SDK, a temporary AVD base folder
 * with an SdkManager and an AvdManager that points to them.
 */
public abstract class AvdManagerTestCase extends TestCase {

    private File mFakeSdk;
    private MockLog mLog;
    private SdkManager mSdkManager;
    private TmpAvdManager mAvdManager;

    /** Returns the {@link MockLog} for this test case. */
    public MockLog getLog() {
        return mLog;
    }

    /** Returns the {@link SdkManager} for this test case. */
    public SdkManager getSdkManager() {
        return mSdkManager;
    }

    /** Returns the {@link AvdManager} for this test case. */
    public TmpAvdManager getAvdManager() {
        return mAvdManager;
    }

    /**
     * A replacement for {@code AvdManager.AvdInfo.getAvdFolder(avdName)}
     * that proposes an AVD folder under the temporary AVD Root.
     * <p/>
     * This should be used for the first argument to {@link AvdManager#createAvd}.
     *
     * @param avdName The desired AVD name.
     * @return A suggested folder location for the AVD data.
     *   No actual directory is been created.
     */
    public File getAvdFolder(String avdName) {
        return new File(getAvdManager().getTmpAvdRoot(), avdName);
    }

    /**
     * Sets up a {@link MockLog}, a fake SDK in a temporary directory
     * and an AVD Manager pointing to an initially-empty AVD directory.
     */
    @Override
    public void setUp() throws Exception {
        mLog = new MockLog();
        mFakeSdk = makeFakeSdk();
        mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
        assertNotNull("sdkManager location was invalid", mSdkManager);

        mAvdManager = new TmpAvdManager(mSdkManager, mLog);
    }

    /**
     * Removes the temporary SDK and AVD directories.
     */
    @Override
    public void tearDown() throws Exception {
        deleteDir(mFakeSdk);
    }

    /**
     * An {@link AvdManager} that uses a temporary directory
     * located <em>inside</em> the SDK directory for testing.
     * The AVD list should be initially empty.
     */
    protected static class TmpAvdManager extends AvdManager {

        /*
         * Implementation detail:
         * - when the super.AvdManager constructor is invoked, it will invoke
         *   the buildAvdFilesList() to fill the initial AVD list.
         * - this means the first time buildAvdFilesList() is invoked, mTmpAvdRoot
         *   is null since we can't setup class fields in the constructor before the
         *   super() call.
         * - that's what mTmpAvdRoot is actually allocated from within getTmpAvdRoot().
         */

        /**
         * AVD Root, initialized "lazily" from {@link #getTmpAvdRoot()}
         * when the AVD root is first requested.
         */
        private File mTmpAvdRoot;

        public TmpAvdManager(SdkManager sdkManager, ISdkLog log) throws AndroidLocationException {
            super(sdkManager, log);
        }

        public File getTmpAvdRoot() {
            if (mTmpAvdRoot == null) {
                mTmpAvdRoot = new File(getSdkManager().getLocation(), "tmp_avds");
                mTmpAvdRoot.mkdirs();
            }
            return mTmpAvdRoot;
        }

        @Override
        protected File[] buildAvdFilesList() throws AndroidLocationException {
            return buildAvdFilesList(getTmpAvdRoot().getAbsolutePath());
        }
    }

    /**
     * Build enough of a skeleton SDK to make the tests pass.
     * <p/>
     * Ideally this wouldn't touch the file system but the current
     * structure of the SdkManager and AvdManager makes this difficult.
     *
     * @return Path to the temporary SDK root
     * @throws IOException
     */
    private File makeFakeSdk() throws IOException {

        File tmpFile = File.createTempFile(
                this.getClass().getSimpleName() + '_' + this.getName(), null);
        tmpFile.delete();
        tmpFile.mkdirs();

        AndroidLocation.resetFolder();
        System.setProperty("user.home", tmpFile.getAbsolutePath());
        File addonsDir = new File(tmpFile, SdkConstants.FD_ADDONS);
        addonsDir.mkdir();
        File toolsLibEmuDir = new File(tmpFile, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
        toolsLibEmuDir.mkdirs();
        new File(toolsLibEmuDir, "snapshots.img").createNewFile();
        File platformsDir = new File(tmpFile, SdkConstants.FD_PLATFORMS);

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
        return tmpFile;
    }

    /**
     * Recursive delete directory. Mostly for fake SDKs.
     *
     * @param root directory to delete
     */
    private void deleteDir(File root) {
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








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 3acb01a..d5000ff 100644

//Synthetic comment -- @@ -19,9 +19,6 @@

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;
//Synthetic comment -- @@ -32,49 +29,36 @@
import java.util.Set;
import java.util.TreeSet;

public class MainTest extends AvdManagerTestCase {

private IAndroidTarget mTarget;
    private File mAvdFolder;

@Override
public void setUp() throws Exception {
        super.setUp();

        mTarget = getSdkManager().getTargets()[0];
        mAvdFolder = getAvdFolder(this.getName());
}

@Override
public void tearDown() throws Exception {
        super.tearDown();
}

public void testDisplayEmptyAvdList() {
Main main = new Main();
        main.setLogger(getLog());
        getLog().clear();
        main.displayAvdList(getAvdManager());
        assertEquals("[P Available Android Virtual Devices:\n]", getLog().toString());
}

public void testDisplayAvdListOfOneNonSnapshot() {
Main main = new Main();
        main.setLogger(getLog());
        getAvdManager().createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -85,10 +69,10 @@
false,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                getLog());

        getLog().clear();
        main.displayAvdList(getAvdManager());
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -96,14 +80,14 @@
+ ", P   Target: Android 0.0 (API level 0)\n"
+ ", P     Skin: HVGA\n"
+ "]",
                getLog().toString());
}

public void testDisplayAvdListOfOneSnapshot() {
Main main = new Main();
        main.setLogger(getLog());

        getAvdManager().createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -114,10 +98,10 @@
true,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                getLog());

        getLog().clear();
        main.displayAvdList(getAvdManager());
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -126,7 +110,7 @@
+ ", P     Skin: HVGA\n"
+ ", P Snapshot: true\n"
+ "]",
                getLog().toString());
}

public void testCheckFilterValues() {
//Synthetic comment -- @@ -178,7 +162,7 @@

// Finally check that checkFilterValues accepts all these values, one by one.
Main main = new Main();
        main.setLogger(getLog());

for (int step = 0; step < 3; step++) {
for (String value : expectedValues) {








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestUtil.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkManagerTestUtil.java
deleted file mode 100644
//Synthetic comment -- index 96efb5c..0000000

//Synthetic comment -- @@ -1,88 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 627bdb9..9bfe4ee 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.avd;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -48,7 +50,7 @@
/**
* Android Virtual Device Manager to manage AVDs.
*/
public class AvdManager {

/**
* Exception thrown when something is wrong with a target path.
//Synthetic comment -- @@ -730,7 +732,9 @@
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
     *   Unless you want to located in a specific location, the ideal default is
     *   {@code AvdManager.AvdInfo.getAvdFolder(avdName)}.
     * @param avdName the name of the AVD
* @param target the target of the AVD
* @param abiType the abi type of the AVD
* @param skinName the name of the skin. Can be null. Must have been verified by caller.
//Synthetic comment -- @@ -747,7 +751,7 @@
*/
public AvdInfo createAvd(
File avdFolder,
            String avdName,
IAndroidTarget target,
String abiType,
String skinName,
//Synthetic comment -- @@ -789,7 +793,7 @@
}

// actually write the ini file
            iniFile = createAvdIniFile(avdName, avdFolder, target, removePrevious);

// writes the userdata.img in it.
String imagePath = target.getImagePath(abiType);
//Synthetic comment -- @@ -1004,17 +1008,17 @@
if (target.isPlatform()) {
if (editExisting) {
report.append(String.format("Updated AVD '%1$s' based on %2$s",
                            avdName, target.getName()));
} else {
report.append(String.format("Created AVD '%1$s' based on %2$s",
                            avdName, target.getName()));
}
} else {
if (editExisting) {
                    report.append(String.format("Updated AVD '%1$s' based on %2$s (%3$s)", avdName,
target.getName(), target.getVendor()));
} else {
                    report.append(String.format("Created AVD '%1$s' based on %2$s (%3$s)", avdName,
target.getName(), target.getVendor()));
}
}
//Synthetic comment -- @@ -1033,12 +1037,12 @@
log.printf(report.toString());

// create the AvdInfo object, and add it to the list
            AvdInfo newAvdInfo = new AvdInfo(avdName,
avdFolder.getAbsolutePath(),
target.hashString(),
target, abiType, values);

            AvdInfo oldAvdInfo = getAvd(avdName, false /*validAvdOnly*/);

synchronized (mAllAvdList) {
if (oldAvdInfo != null && (removePrevious || editExisting)) {
//Synthetic comment -- @@ -1409,14 +1413,33 @@
* <p/>
* This lists the $HOME/.android/avd/<name>.ini files.
* Such files are properties file than then indicate where the AVD folder is located.
     * <p/>
     * Note: the method is to be considered private. It is made protected so that
     * unit tests can easily override the AVR root.
*
* @return A new {@link File} array or null. The array might be empty.
* @throws AndroidLocationException if there's a problem getting android root directory.
*/
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected File[] buildAvdFilesList() throws AndroidLocationException {
// get the Android prefs location.
String avdRoot = AvdManager.getBaseAvdFolder();

        return buildAvdFilesList(avdRoot);
    }

    /**
     * Implementation detail for {@link #buildAvdFilesList()}.
     * Typically unit tests would override {@link #buildAvdFilesList()} and call
     * this method with a different AVD root.
     *
     * @param avdRoot The AVD root folder. Must not be null.
     * @return A new {@link File} array or null. The array might be empty.
     * @throws AndroidLocationException
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected File[] buildAvdFilesList(String avdRoot)
            throws AndroidLocationException {
// ensure folder validity.
File folder = new File(avdRoot);
if (folder.isFile()) {







