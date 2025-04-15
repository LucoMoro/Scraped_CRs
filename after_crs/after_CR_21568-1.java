/*Testing file operations done by the sdk manager.

This still needs works.
The idea was to create a wrapper around File and record operations
done instead of performing them. An idea was to reuse FileWrapper
for that, but this fails since File.exits() has a different semantic
than FileWrapper.exists().

Change-Id:Ib2b9d127b219215a0e0cd0d2bbbea2f3ad54a3af*/




//Synthetic comment -- diff --git a/common/src/com/android/io/FileWrapper.java b/common/src/com/android/io/FileWrapper.java
//Synthetic comment -- index c1e8f81..d834e38 100644

//Synthetic comment -- @@ -30,14 +30,18 @@
* An implementation of {@link IAbstractFile} extending {@link File}.
*/
public class FileWrapper extends File implements IAbstractFile {

private static final long serialVersionUID = 1L;

    private FileWrapperFactory mFactory;

/**
* Creates a new File instance matching a given {@link File} object.
* @param file the file to match
*/
public FileWrapper(File file) {
super(file.getAbsolutePath());
        setFactory(FileWrapperFactory.getFactory(file));
}

/**
//Synthetic comment -- @@ -49,6 +53,7 @@
*/
public FileWrapper(File parent, String child) {
super(parent, child);
        setFactory(FileWrapperFactory.getFactory(parent));
}

/**
//Synthetic comment -- @@ -144,4 +149,70 @@
}
return new FolderWrapper(p);
}

    /**
     * Copy this file to the given destination.
     *
     * @param destination The file to copy to.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void copyFileTo(File destination)
            throws FileNotFoundException, IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(this);
            fos = new FileOutputStream(destination);

            byte[] buffer = new byte[1<<16];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
    }

    /**
     * Copy the source file to the given destination.
     *
     * @param source The file to copy.
     * @param destination The file to copy to.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void copyFileTo(File source, File destination)
            throws FileNotFoundException, IOException {

        if (!(source instanceof FileWrapper)) {
            source = new FileWrapper(source);
        }

        ((FileWrapper) source).copyFileTo(destination);
    }


    // ------------

    public FileWrapperFactory getFactory() {
        return mFactory;
    }

    public void setFactory(FileWrapperFactory factory) {
        mFactory = factory;
    }

}








//Synthetic comment -- diff --git a/common/src/com/android/io/FileWrapperFactory.java b/common/src/com/android/io/FileWrapperFactory.java
new file mode 100755
//Synthetic comment -- index 0000000..854bf40

//Synthetic comment -- @@ -0,0 +1,60 @@
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

package com.android.io;

import java.io.File;

/**
 *
 */
public class FileWrapperFactory {

    public static FileWrapperFactory getFactory(File file) {
        if (file instanceof FileWrapper) {
            FileWrapperFactory factory = ((FileWrapper) file).getFactory();
            if (factory != null) {
                return factory;
            }
        }

        return new FileWrapperFactory();
    }

    public File createFile(File file) {
        FileWrapper fw = new FileWrapper(file);
        fw.setFactory(this);
        return fw;
    }

    public File createFile(String path) {
        FileWrapper fw = new FileWrapper(path);
        fw.setFactory(this);
        return fw;
    }

    public File createFile(File parent, String child) {
        FileWrapper fw = new FileWrapper(parent, child);
        fw.setFactory(this);
        return fw;
    }

    public File createFile(String parent, String child) {
        FileWrapper fw = new FileWrapper(parent, child);
        fw.setFactory(this);
        return fw;
    }
}








//Synthetic comment -- diff --git a/common/src/com/android/io/MockFileWrapper.java b/common/src/com/android/io/MockFileWrapper.java
new file mode 100755
//Synthetic comment -- index 0000000..238a239

//Synthetic comment -- @@ -0,0 +1,41 @@
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

package com.android.io;

import java.io.File;

public class MockFileWrapper extends FileWrapper {
    public MockFileWrapper(File file) {
        super(file);
    }

    public MockFileWrapper(File file, String child) {
        super(file, child);
    }

    public MockFileWrapper(String path) {
        super(path);
    }

    public MockFileWrapper(String parent, String child) {
        super(parent, child);
    }

    public MockFileWrapperFactory getMockFactory() {
        return (MockFileWrapperFactory) super.getFactory();
    }
}








//Synthetic comment -- diff --git a/common/src/com/android/io/MockFileWrapperFactory.java b/common/src/com/android/io/MockFileWrapperFactory.java
new file mode 100755
//Synthetic comment -- index 0000000..9ec92a5

//Synthetic comment -- @@ -0,0 +1,86 @@
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

package com.android.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockFileWrapperFactory extends FileWrapperFactory {

    private static class FileInfo {
        private int mSize;
        private List<String> mActions;

        public void addAction(String verb) {
            if (mActions == null) {
                mActions = new ArrayList<String>();
            }
            mActions.add(verb);
        }
    }

    private final Map<String, FileInfo> mFiles = new HashMap<String, FileInfo>();

    public MockFileWrapperFactory(String...files) {
        for (String file : files) {
            file = file.replace('\\', File.separatorChar);
            file = file.replace('/', File.separatorChar);
            mFiles.put(file, new FileInfo());
        }
    }

    @Override
    public File createFile(File file) {
        FileWrapper fw = new MockFileWrapper(file);
        fw.setFactory(this);
        return fw;
    }

    @Override
    public File createFile(String path) {
        FileWrapper fw = new MockFileWrapper(path);
        fw.setFactory(this);
        return fw;
    }

    @Override
    public File createFile(File parent, String child) {
        FileWrapper fw = new MockFileWrapper(parent, child);
        fw.setFactory(this);
        return fw;
    }

    @Override
    public File createFile(String parent, String child) {
        FileWrapper fw = new MockFileWrapper(parent, child);
        fw.setFactory(this);
        return fw;
    }

    public void addAction(String verb, String filePath) {
        FileInfo fi = mFiles.get(filePath);
        if (fi == null) {
            fi = new FileInfo();
            mFiles.put(filePath, fi);
        }
        fi.addAction(verb);
    }

}








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java b/sdkmanager/app/tests/com/android/sdkmanager/AvdManagerTest.java
//Synthetic comment -- index b5bea69..91d0ed6 100644

//Synthetic comment -- @@ -19,10 +19,15 @@
import static java.io.File.createTempFile;

import com.android.io.FileWrapper;
import com.android.io.FileWrapperFactory;
import com.android.io.MockFileWrapperFactory;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.repository.MockSdkManager;
import com.android.sdklib.mock.MockLog;

import java.io.File;
//Synthetic comment -- @@ -107,4 +112,162 @@
new FileWrapper(mAvdFolder, "config.ini"), mLog);
assertEquals("true", map.get("snapshot.present"));
}

    // ---------

    private static class MockTarget0 implements IAndroidTarget {

        private final String mSdkOsPath;

        public MockTarget0(String sdkOsPath) {
            mSdkOsPath = sdkOsPath;
        }

        public boolean canRunOn(IAndroidTarget target) {
            return false;
        }

        public String getClasspathName() {
            return null;
        }

        public String getDefaultSkin() {
            return "HVGA";
        }

        public String getDescription() {
            return null;
        }

        public String getFullName() {
            return getName();
        }

        public String getLocation() {
            return mSdkOsPath
                + SdkConstants.FD_PLATFORMS + File.separator
                + getName();
        }

        public String getName() {
            return "p0";
        }

        public IOptionalLibrary[] getOptionalLibraries() {
            return null;
        }

        public IAndroidTarget getParent() {
            return null;
        }

        public String getPath(int pathId) {
            return null;
        }

        public String[] getPlatformLibraries() {
            return null;
        }

        public Map<String, String> getProperties() {
            return null;
        }

        public String getProperty(String name) {
            return null;
        }

        public Integer getProperty(String name, Integer defaultValue) {
            return null;
        }

        public Boolean getProperty(String name, Boolean defaultValue) {
            return null;
        }

        public int getRevision() {
            return 1;
        }

        public String[] getSkins() {
            return null;
        }

        public int getUsbVendorId() {
            return 0;
        }

        public String getVendor() {
            return null;
        }

        public AndroidVersion getVersion() {
            return null;
        }

        public String getVersionName() {
            return null;
        }

        public String hashString() {
            return null;
        }

        public boolean isPlatform() {
            return true;
        }

        public int compareTo(IAndroidTarget arg0) {
            return 0;
        }

    }

    public void testCreateAvd1() throws Exception {

        String root = "/tmp/test/sdk/";
        MockFileWrapperFactory factory = new MockFileWrapperFactory(
                root,
                root + SdkConstants.FD_ADDONS + "/",
                root + SdkConstants.OS_SDK_DOCS_FOLDER,
                root + SdkConstants.FD_PLATFORMS,
                root + SdkConstants.OS_SDK_TOOLS_FOLDER,
                root + SdkConstants.OS_SDK_TOOLS_LIB_FOLDER,
                root + SdkConstants.OS_SDK_TOOLS_LIB_EMULATOR_FOLDER,
                root + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER,
                root + SdkConstants.OS_SDK_PLATFORM_TOOLS_LIB_FOLDER,
                root + SdkConstants.OS_SDK_TOOLS_PROGUARD_BIN_FOLDER,
                root + SdkConstants.OS_SDK_TOOLS_LIB_EMULATOR_FOLDER + "/snapshots.img" ,
                root + SdkConstants.FD_PLATFORMS + "/p0",
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.FN_FRAMEWORK_LIBRARY,
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.FN_FRAMEWORK_AIDL,
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.FN_SOURCE_PROP,
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.FN_BUILD_PROP,
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.OS_IMAGES_FOLDER,
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.OS_IMAGES_FOLDER
                                                          + "userdata.img",
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.OS_IMAGES_FOLDER
                                                          + "skins/",
                root + SdkConstants.FD_PLATFORMS + "/p0/" + SdkConstants.OS_IMAGES_FOLDER
                                                          + "HVGA/"
                );

        MockTarget0 target0 = new MockTarget0(root);
        mSdkManager = new MockSdkManager(root, target0);
        mAvdManager = new AvdManager(mSdkManager, mLog, factory);

        mAvdManager.createAvd(
                factory.createFile(root, "1.avd"),    // avdFolder
                "avd1", //name
                target0,
                "HVGA", //skinName
                "16M",  //sdcard
                null,   //hardwareConfig
                true,   //createSnapshot
                false,  //removePrevious
                false,  //editExisting
                mLog);

        assertEquals("P Available Android Virtual Devices:\n", mLog.toString());
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index cb21e3f..90b3758 100644

//Synthetic comment -- @@ -101,6 +101,7 @@

/**
* Creates an {@link SdkManager} for a given sdk location.
     *
* @param osSdkPath the location of the SDK.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the created {@link SdkManager} or null if the location is not valid.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 57cb1bf..7ccdde5 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.avd;

import com.android.io.FileWrapper;
import com.android.io.FileWrapperFactory;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -29,8 +30,6 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
//Synthetic comment -- @@ -229,7 +228,7 @@
* @param target The target. Can be null, if the target was not resolved.
* @param properties The property map. Cannot be null.
*/
        AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
Map<String, String> properties) {
this(name, path, targetHash, target, properties, AvdStatus.OK);
}
//Synthetic comment -- @@ -247,7 +246,7 @@
* @param properties The property map. Can be null.
* @param status The {@link AvdStatus} of this AVD. Cannot be null.
*/
        AvdInfo(String name, String path, String targetHash, IAndroidTarget target,
Map<String, String> properties, AvdStatus status) {
mName = name;
mPath = path;
//Synthetic comment -- @@ -410,22 +409,17 @@
}
}

    /** Factory for creating new {@link File} instances. Useful for unit-testing. */
    private final FileWrapperFactory mFileFactory;

private final ArrayList<AvdInfo> mAllAvdList = new ArrayList<AvdInfo>();
private AvdInfo[] mValidAvdList;
private AvdInfo[] mBrokenAvdList;
private final SdkManager mSdkManager;

/**
* Creates an AVD Manager for a given SDK represented by a {@link SdkManager}.
     *
* @param sdkManager The SDK.
* @param log The log object to receive the log of the initial loading of the AVDs.
*            This log object is not kept by this instance of AvdManager and each
//Synthetic comment -- @@ -435,7 +429,27 @@
* @throws AndroidLocationException
*/
public AvdManager(SdkManager sdkManager, ISdkLog log) throws AndroidLocationException {
        this(sdkManager, log, new FileWrapperFactory());
    }

    /**
     * Creates an AVD Manager for a given SDK represented by a {@link SdkManager}.
     * This version lets callers override the way files are created. This is useful for testing.
     *
     * @param sdkManager The SDK.
     * @param log The log object to receive the log of the initial loading of the AVDs.
     *            This log object is not kept by this instance of AvdManager and each
     *            method takes its own logger. The rationale is that the AvdManager
     *            might be called from a variety of context, each with different
     *            logging needs. Cannot be null.
     * @param fileFactory A {@link FileWrapperFactory} for creating new {@link File}s.
     *            Cannot be null.
     * @throws AndroidLocationException
     */
    public AvdManager(SdkManager sdkManager, ISdkLog log, FileWrapperFactory fileFactory)
            throws AndroidLocationException {
mSdkManager = sdkManager;
        mFileFactory = fileFactory;
buildAvdList(mAllAvdList, log);
}

//Synthetic comment -- @@ -447,6 +461,15 @@
}

/**
     * Returns the base folder where AVDs are created.
     *
     * @throws AndroidLocationException
     */
    public static String getBaseAvdFolder() throws AndroidLocationException {
        return AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD;
    }

    /**
* Parse the sdcard string to decode the size.
* Returns:
* <ul>
//Synthetic comment -- @@ -725,11 +748,11 @@

// writes the userdata.img in it.
String imagePath = target.getPath(IAndroidTarget.IMAGES);
            File userdataSrc = mFileFactory.createFile(imagePath, USERDATA_IMG);

if (userdataSrc.exists() == false && target.isPlatform() == false) {
imagePath = target.getParent().getPath(IAndroidTarget.IMAGES);
                userdataSrc = mFileFactory.createFile(imagePath, USERDATA_IMG);
}

if (userdataSrc.exists() == false) {
//Synthetic comment -- @@ -738,9 +761,10 @@
needCleanup = true;
return null;
}

            File userdataDest = mFileFactory.createFile(avdFolder, USERDATA_IMG);

            FileWrapper.copyFileTo(userdataSrc, userdataDest);

// Config file.
HashMap<String, String> values = new HashMap<String, String>();
//Synthetic comment -- @@ -752,14 +776,14 @@

// Create the snapshot file
if (createSnapshot) {
                File snapshotDest = mFileFactory.createFile(avdFolder, SNAPSHOTS_IMG);
if (snapshotDest.isFile() && editExisting) {
log.printf("Snapshot image already present, was not changed.");

} else {
String toolsLib = mSdkManager.getLocation() + File.separator
+ SdkConstants.OS_SDK_TOOLS_LIB_EMULATOR_FOLDER;
                    File snapshotBlank = mFileFactory.createFile(toolsLib, SNAPSHOTS_IMG);
if (snapshotBlank.exists() == false) {
log.error(null,
"Unable to find a '%2$s%1$s' file to copy into the AVD folder.",
//Synthetic comment -- @@ -767,7 +791,7 @@
needCleanup = true;
return null;
}
                    FileWrapper.copyFileTo(snapshotBlank, snapshotDest);
}
values.put(AVD_INI_SNAPSHOT_PRESENT, "true");
}
//Synthetic comment -- @@ -813,9 +837,9 @@
return null;

} else if (sdcardSize == SDCARD_NOT_SIZE_PATTERN) {
                    File sdcardFile = mFileFactory.createFile(sdcard);
if (sdcardFile.isFile()) {
                        // it's an external sdcard, so we put its path into the config.ini
values.put(AVD_INI_SDCARD_PATH, sdcard);
} else {
log.error(null, "'%1$s' is not recognized as a valid sdcard value.\n"
//Synthetic comment -- @@ -826,7 +850,7 @@
}
} else {
// create the sdcard.
                    File sdcardFile = mFileFactory.createFile(avdFolder, SDCARD_IMG);

boolean runMkSdcard = true;
if (sdcardFile.exists()) {
//Synthetic comment -- @@ -842,9 +866,10 @@
String path = sdcardFile.getAbsolutePath();

// execute mksdcard with the proper parameters.
                        File toolsFolder = mFileFactory.createFile(mSdkManager.getLocation(),
SdkConstants.FD_TOOLS);
                        File mkSdCard = mFileFactory.createFile(
                                toolsFolder, SdkConstants.mkSdCardCmdName());

if (mkSdCard.isFile() == false) {
log.error(null, "'%1$s' is missing from the SDK tools folder.",
//Synthetic comment -- @@ -878,8 +903,10 @@

HashMap<String, String> finalHardwareValues = new HashMap<String, String>();

            FileWrapper targetHardwareFile = (FileWrapper) mFileFactory.createFile(
                    target.getLocation(),
AvdManager.HARDWARE_INI);

if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
targetHardwareFile, log);
//Synthetic comment -- @@ -892,7 +919,10 @@

// get the hardware properties for this skin
File skinFolder = getSkinPath(skinName, target);
            FileWrapper skinHardwareFile = (FileWrapper) mFileFactory.createFile(
                    skinFolder,
                    AvdManager.HARDWARE_INI);

if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
skinHardwareFile, log);
//Synthetic comment -- @@ -909,7 +939,7 @@
values.putAll(hardwareConfig);
}

            File configIniFile = mFileFactory.createFile(avdFolder, CONFIG_INI);
writeIniFile(configIniFile, values);

// Generate the log report first because we want to control where line breaks
//Synthetic comment -- @@ -968,7 +998,7 @@
!oldAvdInfo.getPath().equals(newAvdInfo.getPath())) {
log.warning("Removing previous AVD directory at %s", oldAvdInfo.getPath());
// Remove the old data directory
                File dir = mFileFactory.createFile(oldAvdInfo.getPath());
try {
deleteContentOf(dir);
dir.delete();
//Synthetic comment -- @@ -1002,28 +1032,6 @@
return null;
}

/**
* Returns the path to the target images folder as a relative path to the SDK, if the folder
* is not empty. If the image folder is empty or does not exist, <code>null</code> is returned.
//Synthetic comment -- @@ -1041,7 +1049,7 @@
throw new InvalidTargetPathException("Target location is not inside the SDK.");
}

        File folder = mFileFactory.createFile(imageFullPath);
if (folder.isDirectory()) {
String[] list = folder.list(new FilenameFilter() {
public boolean accept(File dir, String name) {
//Synthetic comment -- @@ -1109,13 +1117,13 @@
*/
public File getSkinPath(String skinName, IAndroidTarget target) {
String path = target.getPath(IAndroidTarget.SKINS);
        File skin = mFileFactory.createFile(path, skinName);

if (skin.exists() == false && target.isPlatform() == false) {
target = target.getParent();

path = target.getPath(IAndroidTarget.SKINS);
            skin = mFileFactory.createFile(path, skinName);
}

return skin;
//Synthetic comment -- @@ -1136,7 +1144,8 @@
IAndroidTarget target,
boolean removePrevious)
throws AndroidLocationException, IOException {

        File iniFile = mFileFactory.createFile(AvdInfo.getIniFile(name));

if (removePrevious) {
if (iniFile.isFile()) {
//Synthetic comment -- @@ -1164,7 +1173,7 @@
*/
private File createAvdIniFile(AvdInfo info) throws AndroidLocationException, IOException {
return createAvdIniFile(info.getName(),
                mFileFactory.createFile(info.getPath()),
info.getTarget(),
false /*removePrevious*/);
}
//Synthetic comment -- @@ -1199,7 +1208,7 @@

String path = avdInfo.getPath();
if (path != null) {
                f = mFileFactory.createFile(path);
if (f.exists()) {
log.printf("Deleting folder %1$s\n", f.getCanonicalPath());
if (deleteContentOf(f) == false || f.delete() == false) {
//Synthetic comment -- @@ -1247,9 +1256,9 @@

try {
if (paramFolderPath != null) {
                File f = mFileFactory.createFile(avdInfo.getPath());
log.warning("Moving '%1$s' to '%2$s'.", avdInfo.getPath(), paramFolderPath);
                if (!f.renameTo(mFileFactory.createFile(paramFolderPath))) {
log.error(null, "Failed to move '%1$s' to '%2$s'.",
avdInfo.getPath(), paramFolderPath);
return false;
//Synthetic comment -- @@ -1331,7 +1340,7 @@
String avdRoot = AvdManager.getBaseAvdFolder();

// ensure folder validity.
        File folder = mFileFactory.createFile(avdRoot);
if (folder.isFile()) {
throw new AndroidLocationException(
String.format("%1$s is not a valid folder.", avdRoot));
//Synthetic comment -- @@ -1345,7 +1354,7 @@
public boolean accept(File parent, String name) {
if (INI_NAME_PATTERN.matcher(name).matches()) {
// check it's a file and not a folder
                    boolean isFile = mFileFactory.createFile(parent, name).isFile();
return isFile;
}

//Synthetic comment -- @@ -1386,7 +1395,7 @@
*/
private AvdInfo parseAvdInfo(File path, ISdkLog log) {
Map<String, String> map = ProjectProperties.parsePropertyFile(
                (FileWrapper) mFileFactory.createFile(path),
log);

String avdPath = map.get(AVD_INFO_PATH);
//Synthetic comment -- @@ -1402,7 +1411,7 @@

// load the AVD properties.
if (avdPath != null) {
            configIniFile = (FileWrapper) mFileFactory.createFile(avdPath, CONFIG_INI);
}

if (configIniFile != null) {
//Synthetic comment -- @@ -1425,13 +1434,13 @@
if (properties != null) {
String imageSysDir = properties.get(AVD_INI_IMAGES_1);
if (imageSysDir != null) {
                File f = mFileFactory.createFile(mSdkManager.getLocation(), imageSysDir);
if (f.isDirectory() == false) {
validImageSysdir = false;
} else {
imageSysDir = properties.get(AVD_INI_IMAGES_2);
if (imageSysDir != null) {
                        f = mFileFactory.createFile(mSdkManager.getLocation(), imageSysDir);
if (f.isDirectory() == false) {
validImageSysdir = false;
}
//Synthetic comment -- @@ -1696,7 +1705,7 @@
}

// now write the config file
        File configIniFile = mFileFactory.createFile(avd.getPath(), CONFIG_INI);
writeIniFile(configIniFile, properties);

// finally create a new AvdInfo for this unbroken avd and add it to the list.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockEmptySdkManager.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockEmptySdkManager.java
deleted file mode 100755
//Synthetic comment -- index f66734b..0000000

//Synthetic comment -- @@ -1,30 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockSdkManager.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockSdkManager.java
new file mode 100755
//Synthetic comment -- index 0000000..f873d08

//Synthetic comment -- @@ -0,0 +1,48 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;

/**
 * A mock SDK Manager, for testing.
 */
public class MockSdkManager extends SdkManager {

    /**
     * Creates a mock SDK Manager with no targets.
     * Use this when you need a non-null SDK Manager with nothing in it.
     *
     * @param osSdkPath The OS path for the SDK.
     */
    public MockSdkManager(String osSdkPath) {
        super(osSdkPath);
        setTargets(new IAndroidTarget[0]);
    }

    /**
     * Creates a mock SDK Manager with targets.
     * Use this when you need a non-null SDK Manager with nothing in it.
     *
     * @param osSdkPath The OS path for the SDK.
     */
    public MockSdkManager(String osSdkPath, IAndroidTarget...targets) {
        super(osSdkPath);
        setTargets(targets);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
//Synthetic comment -- index ea6c4f6..636d592 100755

//Synthetic comment -- @@ -190,7 +190,7 @@
// Check the extra packages path, vendor, install folder

final String osSdkPath = "SDK";
        final SdkManager sdkManager = new MockSdkManager(osSdkPath);

ArrayList<String> extraPaths   = new ArrayList<String>();
ArrayList<String> extraVendors = new ArrayList<String>();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index aee811f..ea487af 100755

//Synthetic comment -- @@ -213,7 +213,7 @@
// Check the extra packages path, vendor, install folder

final String osSdkPath = "SDK";
        final SdkManager sdkManager = new MockSdkManager(osSdkPath);

ArrayList<String> extraPaths   = new ArrayList<String>();
ArrayList<String> extraVendors = new ArrayList<String>();
//Synthetic comment -- @@ -291,7 +291,7 @@
// Check the extra packages path, vendor, install folder

final String osSdkPath = "SDK";
        final SdkManager sdkManager = new MockSdkManager(osSdkPath);

ArrayList<String> extraPaths   = new ArrayList<String>();
ArrayList<String> extraVendors = new ArrayList<String>();
//Synthetic comment -- @@ -367,7 +367,7 @@
// Check the extra packages path, vendor, install folder

final String osSdkPath = "SDK";
        final SdkManager sdkManager = new MockSdkManager(osSdkPath);

ArrayList<String> extraPaths   = new ArrayList<String>();
ArrayList<String> extraVendors = new ArrayList<String>();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index abc63d0..5122fc3 100755

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockSdkManager;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
//Synthetic comment -- @@ -129,7 +129,7 @@

@Override
protected void initSdk() {
            setSdkManager(new MockSdkManager("/tmp/SDK"));
}

@Override







