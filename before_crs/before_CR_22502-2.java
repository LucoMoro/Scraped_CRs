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
//Synthetic comment -- @@ -69,17 +55,17 @@
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
//Synthetic comment -- @@ -89,7 +75,7 @@

public void testCreateAvdWithSnapshot() {

        mAvdManager.createAvd(
mAvdFolder,
this.getName(),
mTarget,
//Synthetic comment -- @@ -100,14 +86,14 @@
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








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTestCase.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTestCase.java
new file mode 100755
//Synthetic comment -- index 0000000..73c1d2c

//Synthetic comment -- @@ -0,0 +1,201 @@








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java b/sdkmanager/app/tests/com/android/sdkmanager/MainTest.java
//Synthetic comment -- index 3acb01a..d5000ff 100644

//Synthetic comment -- @@ -19,9 +19,6 @@

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;
//Synthetic comment -- @@ -32,49 +29,36 @@
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
//Synthetic comment -- @@ -85,10 +69,10 @@
false,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

        mLog.clear();
        main.displayAvdList(mAvdManager);
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -96,14 +80,14 @@
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
//Synthetic comment -- @@ -114,10 +98,10 @@
true,  // createSnapshot
false,  // removePrevious
false,  // editExisting
                mLog);

        mLog.clear();
        main.displayAvdList(mAvdManager);
assertEquals(
"[P Available Android Virtual Devices:\n"
+ ", P     Name: " + this.getName() + "\n"
//Synthetic comment -- @@ -126,7 +110,7 @@
+ ", P     Skin: HVGA\n"
+ ", P Snapshot: true\n"
+ "]",
                mLog.toString());
}

public void testCheckFilterValues() {
//Synthetic comment -- @@ -178,7 +162,7 @@

// Finally check that checkFilterValues accepts all these values, one by one.
Main main = new Main();
        main.setLogger(mLog);

for (int step = 0; step < 3; step++) {
for (String value : expectedValues) {








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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 627bdb9..9bfe4ee 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.avd;

import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -48,7 +50,7 @@
/**
* Android Virtual Device Manager to manage AVDs.
*/
public final class AvdManager {

/**
* Exception thrown when something is wrong with a target path.
//Synthetic comment -- @@ -730,7 +732,9 @@
* Creates a new AVD. It is expected that there is no existing AVD with this name already.
*
* @param avdFolder the data folder for the AVD. It will be created as needed.
     * @param name the name of the AVD
* @param target the target of the AVD
* @param abiType the abi type of the AVD
* @param skinName the name of the skin. Can be null. Must have been verified by caller.
//Synthetic comment -- @@ -747,7 +751,7 @@
*/
public AvdInfo createAvd(
File avdFolder,
            String name,
IAndroidTarget target,
String abiType,
String skinName,
//Synthetic comment -- @@ -789,7 +793,7 @@
}

// actually write the ini file
            iniFile = createAvdIniFile(name, avdFolder, target, removePrevious);

// writes the userdata.img in it.
String imagePath = target.getImagePath(abiType);
//Synthetic comment -- @@ -1004,17 +1008,17 @@
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
//Synthetic comment -- @@ -1033,12 +1037,12 @@
log.printf(report.toString());

// create the AvdInfo object, and add it to the list
            AvdInfo newAvdInfo = new AvdInfo(name,
avdFolder.getAbsolutePath(),
target.hashString(),
target, abiType, values);

            AvdInfo oldAvdInfo = getAvd(name, false /*validAvdOnly*/);

synchronized (mAllAvdList) {
if (oldAvdInfo != null && (removePrevious || editExisting)) {
//Synthetic comment -- @@ -1409,14 +1413,33 @@
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







