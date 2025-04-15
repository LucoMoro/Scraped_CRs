/*SdkManager method to query known build-tools.

Also refactors FullRevision and MajorRevision to move them
out of internal.repository. Their API is not going to change.

Change-Id:I3eab40fc2c4844a9ddb4fcadd9eb0c571fc437d4*/




//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java b/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..487e950

//Synthetic comment -- @@ -0,0 +1,146 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.sdklib;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.sdklib.repository.FullRevision;
import com.android.utils.ILogger;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;



/**
 * Information on a specific build-tool folder.
 */
public class BuildToolInfo {

    public enum PathId {
        /** OS Path to the target's version of the aapt tool. */
        AAPT,
        /** OS Path to the target's version of the aidl tool. */
        AIDL,
        /** OS Path to the target's version of the dx too. */
        DX,
        /** OS Path to the target's version of the dx.jar file. */
        DX_JAR,
        ///** OS Path to the llvm-rs-cc binary for Renderscript. */
        LLVM_RS_CC,
        ///** OS Path to the Renderscript include folder. */
        ANDROID_RS,
        ///** OS Path to the Renderscript(clang) include folder. */
        ANDROID_RS_CLANG,
    }

    /** The build-tool revision. */
    private final FullRevision mRevision;
    /** The path to the build-tool folder specific to this revision. */
    private final File mPath;

    private final Map<PathId, String> mPaths = Maps.newEnumMap(PathId.class);

    public BuildToolInfo(FullRevision revision, File path) {
        mRevision = revision;
        mPath = path;

        add(PathId.AAPT, SdkConstants.FN_AAPT);
        add(PathId.AIDL, SdkConstants.FN_AIDL);
        add(PathId.DX, SdkConstants.FN_DX);
        add(PathId.DX_JAR, SdkConstants.FN_DX_JAR);
        add(PathId.LLVM_RS_CC, SdkConstants.FN_RENDERSCRIPT);
        add(PathId.ANDROID_RS, SdkConstants.OS_FRAMEWORK_RS);
        add(PathId.ANDROID_RS_CLANG, SdkConstants.OS_FRAMEWORK_RS_CLANG);

    }

    private void add(PathId id, String leaf) {
        File f = new File(mPath, leaf);
        String str = f.getAbsolutePath();
        if (f.isDirectory() && str.charAt(str.length() - 1) != File.separatorChar) {
            str += File.separatorChar;
        }
        mPaths.put(id, str);
    }

    /**
     * Returns the revision.
     */
    public FullRevision getRevision() {
        return mRevision;
    }

    /**
     * Returns the build-tool revision-specific folder.
     * <p/>
     * For compatibility reasons, use {@link #getPath(PathId)} if you need the path to a
     * specific tool.
     */
    File getLocation() {
        return mPath;
    }

    /**
     * Returns the path of a build-tool component.
     *
     * @param pathId the id representing the path to return.
     * @return The absolute path for that tool, with a / separator if it's a folder.
     *         Null if the path-id is unknown.
     */
    String getPath(PathId pathId) {
        return mPaths.get(pathId);
    }

    /**
     * Checks whether the build-tool is valid by verifying that the expected binaries
     * are actually present. This checks that all known paths point to a valid file
     * or directory.
     *
     * @param log An optional logger. If non-null, errors will be printed there.
     * @return True if the build-tool folder contains all the expected tools.
     */
    public boolean isValid(@Nullable ILogger log) {
        for (Map.Entry<PathId, String> entry : mPaths.entrySet()) {
            File f = new File(entry.getValue());
            if (!f.exists()) {
                if (log != null) {
                    log.warning("Build-tool %1$s is missing %2$s",  //$NON-NLS-1$
                            mRevision.toString(),
                            entry.getKey());
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a debug representation suitable for unit-tests.
     * Note that unit-tests need to clean up the paths to avoid inconsistent results.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<BuildToolInfo rev=").append(mRevision);    //$NON-NLS-1$
        builder.append(", mPath=").append(mPath);                   //$NON-NLS-1$
        builder.append(", mPaths=").append(mPaths);                 //$NON-NLS-1$
        builder.append(">");                                        //$NON-NLS-1$
        return builder.toString();
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/SdkManager.java b/sdklib/src/main/java/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 0a5e06b..bb9f54d 100644

//Synthetic comment -- @@ -33,10 +33,14 @@
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.ILogger;
import com.android.utils.NullLogger;
import com.android.utils.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -106,8 +110,11 @@
private final String mOsSdkPath;
/** Valid targets that have been loaded. Can be empty but not null. */
private IAndroidTarget[] mTargets = new IAndroidTarget[0];
    /** Valid build-tool folders that have been loaded. Can be empty but not null. */
    private Map<FullRevision, BuildToolInfo> mBuildTools = Maps.newTreeMap();
/** A map to keep information on directories to see if they change later. */
    private final Map<File, DirInfo> mVisistedDirs = new HashMap<File, SdkManager.DirInfo>();


/**
* Create a new {@link SdkManager} instance.
//Synthetic comment -- @@ -116,17 +123,20 @@
* @param osSdkPath the location of the SDK.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected SdkManager(@NonNull String osSdkPath) {
mOsSdkPath = osSdkPath;
}

/**
* Creates an {@link SdkManager} for a given sdk location.
* @param osSdkPath the location of the SDK.
     * @param log the ILogger object receiving warning/error from the parsing.
* @return the created {@link SdkManager} or null if the location is not valid.
*/
    @Nullable
    public static SdkManager createManager(
            @NonNull String osSdkPath,
            @NonNull ILogger log) {
try {
SdkManager manager = new SdkManager(osSdkPath);
manager.reloadSdk(log);
//Synthetic comment -- @@ -142,14 +152,16 @@
/**
* Reloads the content of the SDK.
*
     * @param log the ILogger object receiving warning/error from the parsing.
*/
    public void reloadSdk(@NonNull ILogger log) {
// get the current target list.
        mVisistedDirs.clear();
        ArrayList<IAndroidTarget> targets = Lists.newArrayList();
        loadPlatforms(mOsSdkPath, targets, mVisistedDirs, log);
        loadAddOns(mOsSdkPath, targets, mVisistedDirs, log);
        Map<FullRevision, BuildToolInfo> buildTools = Maps.newHashMap();
        loadBuildTools(mOsSdkPath, buildTools, mVisistedDirs, log);

// For now replace the old list with the new one.
// In the future we may want to keep the current objects, so that ADT doesn't have to deal
//Synthetic comment -- @@ -158,13 +170,14 @@
// sort the targets/add-ons
Collections.sort(targets);
setTargets(targets.toArray(new IAndroidTarget[targets.size()]));
        setBuildTools(buildTools);

// load the samples, after the targets have been set.
initializeSamplePaths(log);
}

/**
     * Checks whether any of the SDK platforms/add-ons/build-tools have changed on-disk
* since we last loaded the SDK. This does not reload the SDK nor does it
* change the underlying targets.
*
//Synthetic comment -- @@ -174,16 +187,22 @@
Set<File> visited = new HashSet<File>();
boolean changed = false;

        for (String dirName : new String[] { SdkConstants.FD_PLATFORMS,
                                             SdkConstants.FD_ADDONS,
                                             SdkConstants.FD_BUILD_TOOLS }) {

            File folder = new File(mOsSdkPath, dirName);
            if (folder.isDirectory()) {
                File[] subFolders = folder.listFiles();
                if (subFolders == null) {
                    continue;
                }
                for (File subFolder : subFolders) {
                    if (!subFolder.isDirectory()) {
continue;
}
                    visited.add(subFolder);
                    DirInfo dirInfo = mVisistedDirs.get(subFolder);
if (dirInfo == null) {
// This is a new platform directory.
changed = true;
//Synthetic comment -- @@ -193,43 +212,18 @@
if (changed) {
if (DEBUG) {
System.out.println("SDK changed due to " +              //$NON-NLS-1$
                                (dirInfo != null ? dirInfo.toString() : subFolder.getPath()));
}
                        break;
}
}
}
}


if (!changed) {
// Check whether some pre-existing target directories have vanished.
            for (File previousDir : mVisistedDirs.keySet()) {
if (!visited.contains(previousDir)) {
// This directory is no longer present.
changed = true;
//Synthetic comment -- @@ -248,6 +242,7 @@
/**
* Returns the location of the SDK.
*/
    @NonNull
public String getLocation() {
return mOsSdkPath;
}
//Synthetic comment -- @@ -257,6 +252,7 @@
* <p/>
* The array can be empty but not null.
*/
    @NonNull
public IAndroidTarget[] getTargets() {
return mTargets;
}
//Synthetic comment -- @@ -267,18 +263,42 @@
* The array can be empty but not null.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void setTargets(@NonNull IAndroidTarget[] targets) {
assert targets != null;
mTargets = targets;
}

    private void setBuildTools(@NonNull Map<FullRevision, BuildToolInfo> buildTools) {
        assert buildTools != null;
        mBuildTools = buildTools;
    }

    /** Returns an unmodifiable set of known build-tools revisions. Can be empty but not null. */
    @NonNull
    public Set<FullRevision> getBuildTools() {
        return Collections.unmodifiableSet(mBuildTools.keySet());
    }

    /**
     * Returns the {@link BuildToolInfo} for the given revision.
     *
     * @param revision The requested revision.
     * @return A {@link BuildToolInfo}. Can be null if {@code revision} is null or is
     *  not part of the known set returned by {@link #getBuildTools()}.
     */
    @Nullable
    public BuildToolInfo getBuildTool(@Nullable FullRevision revision) {
        return mBuildTools.get(revision);
    }

/**
* Returns a target from a hash that was generated by {@link IAndroidTarget#hashString()}.
*
* @param hash the {@link IAndroidTarget} hash string.
* @return The matching {@link IAndroidTarget} or null.
*/
    @Nullable
    public IAndroidTarget getTargetFromHashString(@Nullable String hash) {
if (hash != null) {
for (IAndroidTarget target : mTargets) {
if (hash.equals(target.hashString())) {
//Synthetic comment -- @@ -337,6 +357,7 @@
* @deprecated This does NOT solve the right problem and will be changed later.
*/
@Deprecated
    @Nullable
public LayoutlibVersion getMaxLayoutlibVersion() {
LayoutlibVersion maxVersion = null;

//Synthetic comment -- @@ -455,13 +476,14 @@
* @param sdkOsPath Location of the SDK
* @param targets the list to fill with the platforms.
* @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing.
* @throws RuntimeException when the "platforms" folder is missing and cannot be created.
*/
private static void loadPlatforms(
            @NonNull String sdkOsPath,
            @NonNull ArrayList<IAndroidTarget> targets,
            @NonNull Map<File, DirInfo> dirInfos,
            @NonNull ILogger log) {
File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);

if (platformFolder.isDirectory()) {
//Synthetic comment -- @@ -503,12 +525,13 @@
* Loads a specific Platform at a given location.
* @param sdkOsPath Location of the SDK
* @param platformFolder the root folder of the platform.
     * @param log the ILogger object receiving warning/error from the parsing.
*/
    @Nullable
private static PlatformTarget loadPlatform(
            @NonNull String sdkOsPath,
            @NonNull File platformFolder,
            @NonNull ILogger log) {
FileWrapper buildProp = new FileWrapper(platformFolder, SdkConstants.FN_BUILD_PROP);
FileWrapper sourcePropFile = new FileWrapper(platformFolder, SdkConstants.FN_SOURCE_PROP);

//Synthetic comment -- @@ -647,9 +670,10 @@
*
* @param root Root of the add-on target being loaded.
* @return an array of ISystemImage containing all the system images for the target.
     *              The list can be empty but not null.
*/
    @NonNull
    private static ISystemImage[] getAddonSystemImages(@NonNull File root) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();

root = new File(root, SdkConstants.OS_IMAGES_FOLDER);
//Synthetic comment -- @@ -695,12 +719,13 @@
* @param root Root of the platform target being loaded.
* @param version API level + codename of platform being loaded.
* @return an array of ISystemImage containing all the system images for the target.
     *              The list can be empty but not null.
     */
    @NonNull
private static ISystemImage[] getPlatformSystemImages(
            @NonNull String sdkOsPath,
            @NonNull File root,
            @NonNull AndroidVersion version) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();
Set<String> abiFound = new HashSet<String>();

//Synthetic comment -- @@ -741,8 +766,7 @@
abi));
abiFound.add(abi);
}
                        } catch (Exception ignore) {}
}
}
}
//Synthetic comment -- @@ -795,13 +819,14 @@
* @param osSdkPath Location of the SDK
* @param targets the list to fill with the add-ons.
* @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing.
* @throws RuntimeException when the "add-ons" folder is missing and cannot be created.
*/
private static void loadAddOns(
            @NonNull String osSdkPath,
            @NonNull ArrayList<IAndroidTarget> targets,
            @NonNull Map<File, DirInfo> dirInfos,
            @NonNull ILogger log) {
File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);

if (addonFolder.isDirectory()) {
//Synthetic comment -- @@ -846,12 +871,13 @@
* Loads a specific Add-on at a given location.
* @param addonDir the location of the add-on directory.
* @param targetList The list of Android target that were already loaded from the SDK.
     * @param log the ILogger object receiving warning/error from the parsing.
*/
    @Nullable
    private static AddOnTarget loadAddon(
            @NonNull File addonDir,
            @NonNull IAndroidTarget[] targetList,
            @NonNull ILogger log) {
// Parse the addon properties to ensure we can load it.
Pair<Map<String, String>, String> infos = parseAddonProperties(addonDir, targetList, log);

//Synthetic comment -- @@ -978,8 +1004,8 @@
target.setSkins(skins, defaultSkin);

return target;

        } catch (Exception e) {
log.warning("Ignoring add-on '%1$s': error %2$s.",
addonDir.getName(), e.toString());
}
//Synthetic comment -- @@ -992,15 +1018,16 @@
*
* @param addonDir the location of the addon directory.
* @param targetList The list of Android target that were already loaded from the SDK.
     * @param log the ILogger object receiving warning/error from the parsing.
* @return A pair with the property map and an error string. Both can be null but not at the
*  same time. If a non-null error is present then the property map must be ignored. The error
*  should be translatable as it might show up in the SdkManager UI.
*/
    @NonNull
public static Pair<Map<String, String>, String> parseAddonProperties(
            @NonNull File addonDir,
            @NonNull IAndroidTarget[] targetList,
            @NonNull ILogger log) {
Map<String, String> propertyMap = null;
String error = null;

//Synthetic comment -- @@ -1079,7 +1106,7 @@
* @param value the string to convert.
* @return the int value, or {@link IAndroidTarget#NO_USB_ID} if the conversion failed.
*/
    private static int convertId(@Nullable String value) {
if (value != null && value.length() > 0) {
if (PATTERN_USB_IDS.matcher(value).matches()) {
String v = value.substring(2);
//Synthetic comment -- @@ -1101,7 +1128,8 @@
*
* @param valueName The missing manifest value, for display.
*/
    @NonNull
    private static String addonManifestWarning(@NonNull String valueName) {
return String.format("'%1$s' is missing from %2$s.",
valueName, SdkConstants.FN_MANIFEST_INI);
}
//Synthetic comment -- @@ -1113,9 +1141,11 @@
* aidl(.exe), dx(.bat), and dx.jar
*
* @param platform The folder containing the platform.
     * @param log Logger.
*/
    private static boolean checkPlatformContent(
            @NonNull File platform,
            @NonNull ILogger log) {
for (String relativePath : sPlatformContentList) {
File f = new File(platform, relativePath);
if (!f.exists()) {
//Synthetic comment -- @@ -1134,7 +1164,8 @@
* Parses the skin folder and builds the skin list.
* @param osPath The path of the skin root folder.
*/
    @NonNull
    private static String[] parseSkinFolder(@NonNull String osPath) {
File skinRootFolder = new File(osPath);

if (skinRootFolder.isDirectory()) {
//Synthetic comment -- @@ -1168,9 +1199,9 @@
* have a separate SDK/samples/samples-API directory. This parses either directories
* and sets the targets' sample path accordingly.
*
     * @param log Logger.
*/
    private void initializeSamplePaths(@NonNull ILogger log) {
File sampleFolder = new File(mOsSdkPath, SdkConstants.FD_SAMPLES);
if (sampleFolder.isDirectory()) {
File[] platforms  = sampleFolder.listFiles();
//Synthetic comment -- @@ -1198,10 +1229,13 @@
* Returns the {@link AndroidVersion} of the sample in the given folder.
*
* @param folder The sample's folder.
     * @param log Logger for errors.
* @return An {@link AndroidVersion} or null on error.
*/
    @Nullable
    private AndroidVersion getSamplesVersion(
            @NonNull File folder,
            @NonNull ILogger log) {
File sourceProp = new File(folder, SdkConstants.FN_SOURCE_PROP);
try {
Properties p = new Properties();
//Synthetic comment -- @@ -1230,6 +1264,95 @@
return null;
}

    /**
     * Loads the build-tools from the SDK.
     * Creates the "build-tools" folder if necessary.
     *
     * @param sdkOsPath Location of the SDK
     * @param infos the map to fill with the build-tools.
     * @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing.
     * @throws RuntimeException when the "platforms" folder is missing and cannot be created.
     */
    private static void loadBuildTools(
            @NonNull String sdkOsPath,
            @NonNull Map<FullRevision, BuildToolInfo> infos,
            @NonNull Map<File, DirInfo> dirInfos,
            @NonNull ILogger log) {
        File buildToolsFolder = new File(sdkOsPath, SdkConstants.FD_BUILD_TOOLS);

        if (buildToolsFolder.isDirectory()) {
            File[] folders  = buildToolsFolder.listFiles();

            for (File subFolder : folders) {
                if (subFolder.isDirectory()) {
                    BuildToolInfo info = loadBuildTool(sdkOsPath, subFolder, log);
                    if (info != null) {
                        infos.put(info.getRevision(), info);
                    }
                    // Remember we visited this file/directory,
                    // even if we failed to load anything from it.
                    dirInfos.put(subFolder, new DirInfo(subFolder));
                } else {
                    log.warning("Ignoring build-tool '%1$s', not a folder.", subFolder.getName());
                }
            }

            return;
        }

        // Try to create it or complain if something else is in the way.
        if (!buildToolsFolder.exists()) {
            if (!buildToolsFolder.mkdir()) {
                throw new RuntimeException(
                        String.format("Failed to create %1$s.",
                                buildToolsFolder.getAbsolutePath()));
            }
        } else {
            throw new RuntimeException(
                    String.format("%1$s is not a folder.",
                            buildToolsFolder.getAbsolutePath()));
        }
    }

    /**
     * Loads a specific Platform at a given location.
     * @param sdkOsPath Location of the SDK
     * @param folder the root folder of the platform.
     * @param log the ILogger object receiving warning/error from the parsing.
     */
    @Nullable
    private static BuildToolInfo loadBuildTool(
            @NonNull String sdkOsPath,
            @NonNull File folder,
            @NonNull ILogger log) {
        FileOp f = new FileOp();

        File sourcePropFile = new File(folder, SdkConstants.FN_SOURCE_PROP);
        if (!f.isFile(sourcePropFile)) {
            log.warning("Ignoring build-tool '%1$s': missing file %2$s",
                    folder.getName(), SdkConstants.FN_SOURCE_PROP);
        } else {
            Properties props = f.loadProperties(sourcePropFile);
            String revStr = props.getProperty(PkgProps.PKG_REVISION);

            try {
                FullRevision rev =
                    FullRevision.parseRevision(props.getProperty(PkgProps.PKG_REVISION));

                BuildToolInfo info = new BuildToolInfo(rev, folder);
                return info.isValid(log) ? info : null;

            } catch (NumberFormatException e) {
                log.warning("Ignoring build-tool '%1$s': invalid revision '%2$s'",
                        folder.getName(), revStr);
            }

        }

        return null;
    }

// -------------

public static class LayoutlibVersion implements Comparable<LayoutlibVersion> {
//Synthetic comment -- @@ -1252,7 +1375,7 @@
}

@Override
        public int compareTo(@NonNull LayoutlibVersion rhs) {
boolean useRev = this.mRevision > NOT_SPECIFIED && rhs.mRevision > NOT_SPECIFIED;
int lhsValue = (this.mApi << 16) + (useRev ? this.mRevision : 0);
int rhsValue = (rhs.mApi  << 16) + (useRev ? rhs.mRevision  : 0);
//Synthetic comment -- @@ -1336,7 +1459,7 @@
* Computes an adler32 checksum (source.props are small files, so this
* should be OK with an acceptable collision rate.)
*/
        private static long getFileChecksum(@NonNull File file) {
FileInputStream fis = null;
try {
fis = new FileInputStream(file);








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/BuildToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/BuildToolPackage.java
//Synthetic comment -- index 23a6eec..23fe5ac 100755

//Synthetic comment -- @@ -23,9 +23,10 @@
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.FullRevision.PreviewComparison;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index 698b211..bf63752 100755

//Synthetic comment -- @@ -18,10 +18,11 @@

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.repository.FullRevision.PreviewComparison;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java
//Synthetic comment -- index 10f66d2..8a6a15e 100755

//Synthetic comment -- @@ -16,7 +16,9 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.MajorRevision;
import com.android.sdklib.repository.FullRevision.PreviewComparison;











//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java
//Synthetic comment -- index d17b800..b8aae78 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.SdkRepoConstants;

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java
//Synthetic comment -- index 064f1d3..d0f9e8d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.SdkRepoConstants;

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java
//Synthetic comment -- index 4591297..45a018c 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.MajorRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MinToolsPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MinToolsPackage.java
//Synthetic comment -- index a608a3c..049137e 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 397cce1..1ca90dd 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PackageParserUtils.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PackageParserUtils.java
//Synthetic comment -- index a6986e1..a71247b 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.SdkRepoConstants;

import org.w3c.dom.Node;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index 29828f0..0930f86 100755

//Synthetic comment -- @@ -26,8 +26,8 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision.PreviewComparison;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index ddf71d8..2f0094d 100755

//Synthetic comment -- @@ -26,10 +26,11 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.repository.FullRevision.PreviewComparison;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java
//Synthetic comment -- index 9e0912e..c5eb07e 100755

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;

/**
* A {@link PkgItem} represents one main {@link Package} combined with its state








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java
//Synthetic comment -- index be214f7..006e45f 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.sdklib.internal.repository.packages.BuildToolPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
//Synthetic comment -- @@ -41,6 +40,7 @@
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.repository.FullRevision;

import java.util.ArrayList;
import java.util.Arrays;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevision.java b/sdklib/src/main/java/com/android/sdklib/repository/FullRevision.java
similarity index 99%
rename from sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevision.java
rename to sdklib/src/main/java/com/android/sdklib/repository/FullRevision.java
//Synthetic comment -- index 67852ff..0986a34 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.repository;

import com.android.annotations.NonNull;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdklib/src/main/java/com/android/sdklib/repository/MajorRevision.java
similarity index 96%
rename from sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java
rename to sdklib/src/main/java/com/android/sdklib/repository/MajorRevision.java
//Synthetic comment -- index eefda3b..c953e84 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.repository;

import com.android.annotations.NonNull;









//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTest.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTest.java
//Synthetic comment -- index 487a96a..989260d 100755

//Synthetic comment -- @@ -20,10 +20,15 @@
import com.android.SdkConstants;
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.SdkManager.LayoutlibVersion;
import com.android.sdklib.repository.FullRevision;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/** Setup will build an SDK Manager local install matching the latest repository-N.xsd. */
public class SdkManagerTest extends SdkManagerTestCase {

@SuppressWarnings("deprecation")
//Synthetic comment -- @@ -41,6 +46,51 @@
assertSame(lv, sdkman.getMaxLayoutlibVersion());
}

    public void testSdkManager_getBuildTools() {
        SdkManager sdkman = getSdkManager();

        Set<FullRevision> v = sdkman.getBuildTools();
        // Make sure we get a stable set -- hashmap order isn't stable and can't be used in tests.
        if (!(v instanceof TreeSet<?>)) {
            v = Sets.newTreeSet(v);
        }

        assertEquals("[]", getLog().toString());  // no errors in the logger
        assertEquals("[3.0.0, 3.0.1, 12.3.4 rc5]", Arrays.toString(v.toArray()));

        // Get infos, first one that doesn't exit returns null.
        assertNull(sdkman.getBuildTool(new FullRevision(1)));

        // Now some that exist.
        BuildToolInfo i = sdkman.getBuildTool(new FullRevision(3, 0, 0));
        assertEquals(
                "<BuildToolInfo rev=3.0.0, " +
                "mPath=$SDK/build-tools/3.0.0, " +
                "mPaths={" +
                    "AAPT=$SDK/build-tools/3.0.0/aapt, " +
                    "AIDL=$SDK/build-tools/3.0.0/aidl, " +
                    "DX=$SDK/build-tools/3.0.0/dx, " +
                    "DX_JAR=$SDK/build-tools/3.0.0/dx.jar, " +
                    "LLVM_RS_CC=$SDK/build-tools/3.0.0/llvm-rs-cc, " +
                    "ANDROID_RS=$SDK/build-tools/3.0.0/renderscript/include/, " +
                    "ANDROID_RS_CLANG=$SDK/build-tools/3.0.0/renderscript/clang-include/}>",
                cleanPath(sdkman, i.toString()));

        i = sdkman.getBuildTool(new FullRevision(12, 3, 4, 5));
        assertEquals(
                "<BuildToolInfo rev=12.3.4 rc5, " +
                "mPath=$SDK/build-tools/12.3.4 rc5, " +
                "mPaths={" +
                    "AAPT=$SDK/build-tools/12.3.4 rc5/aapt, " +
                    "AIDL=$SDK/build-tools/12.3.4 rc5/aidl, " +
                    "DX=$SDK/build-tools/12.3.4 rc5/dx, " +
                    "DX_JAR=$SDK/build-tools/12.3.4 rc5/dx.jar, " +
                    "LLVM_RS_CC=$SDK/build-tools/12.3.4 rc5/llvm-rs-cc, " +
                    "ANDROID_RS=$SDK/build-tools/12.3.4 rc5/renderscript/include/, " +
                    "ANDROID_RS_CLANG=$SDK/build-tools/12.3.4 rc5/renderscript/clang-include/}>",
                cleanPath(sdkman, i.toString()));
    }

public void testSdkManager_SystemImage() throws Exception {
SdkManager sdkman = getSdkManager();
assertEquals("[PlatformTarget API 0 rev 1]", Arrays.toString(sdkman.getTargets()));
//Synthetic comment -- @@ -139,14 +189,16 @@
/**
* Sanitizes the paths used when testing results.
* <p/>
     * Some methods return absolute paths to the SDK.
* However the SDK path is actually a randomized location.
* We clean it by replacing it by the constant '$SDK'.
     * Also all the Windows path separators are converted to unix-like / separators
     * and ".exe" and ".bat" are removed (e.g. for build-tools binaries).
*/
private String cleanPath(SdkManager sdkman, String string) {
return string
            .replaceAll(Pattern.quote(sdkman.getLocation()), "\\$SDK")  //$NON-NLS-1$
            .replaceAll("\\.(?:bat|exe)", "")                           //$NON-NLS-1$ //$NON-NLS-2$
.replace('\\', '/');
}
}








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTest7.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTest7.java
new file mode 100755
//Synthetic comment -- index 0000000..9c41bd2

//Synthetic comment -- @@ -0,0 +1,52 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.sdklib;


import com.android.sdklib.repository.FullRevision;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class SdkManagerTest7 extends SdkManagerTestCase {

    /** Setup will build an SDK Manager local install matching a repository-7.xsd. */
    @Override
    public void setUp() throws Exception {
        super.setUp(7);
    }

    public void testSdkManager_getBuildTools() {
        // There is no build-tools folder in this repository.
        SdkManager sdkman = getSdkManager();

        Set<FullRevision> v = sdkman.getBuildTools();
        // Make sure we get a stable set -- hashmap order isn't stable and can't be used in tests.
        if (!(v instanceof TreeSet<?>)) {
            v = Sets.newTreeSet(v);
        }

        assertEquals("[]", getLog().toString());  // no errors in the logger
        assertEquals("[]", Arrays.toString(v.toArray()));

        assertNull(sdkman.getBuildTool(new FullRevision(1)));
        assertNull(sdkman.getBuildTool(new FullRevision(3, 0, 0)));
        assertNull(sdkman.getBuildTool(new FullRevision(12, 3, 4, 5)));
    }
}








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 9ca4c32..8073f64 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.sdklib.io.FileOp;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.utils.ILogger;

import java.io.File;
//Synthetic comment -- @@ -42,6 +43,7 @@
private MockLog mLog;
private SdkManager mSdkManager;
private TmpAvdManager mAvdManager;
    private int mRepoXsdLevel;

/** Returns the {@link MockLog} for this test case. */
public MockLog getLog() {
//Synthetic comment -- @@ -62,8 +64,8 @@
* Sets up a {@link MockLog}, a fake SDK in a temporary directory
* and an AVD Manager pointing to an initially-empty AVD directory.
*/
    public void setUp(int repoXsdLevel) throws Exception {
        mRepoXsdLevel = repoXsdLevel;
mLog = new MockLog();
mFakeSdk = makeFakeSdk();
mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
//Synthetic comment -- @@ -73,6 +75,15 @@
}

/**
     * Sets up a {@link MockLog}, a fake SDK in a temporary directory
     * and an AVD Manager pointing to an initially-empty AVD directory.
     */
    @Override
    public void setUp() throws Exception {
        setUp(SdkRepoConstants.NS_LATEST_VERSION);
    }

    /**
* Removes the temporary SDK and AVD directories.
*/
@Override
//Synthetic comment -- @@ -151,8 +162,10 @@
new File(toolsDir, SdkConstants.FN_EMULATOR).createNewFile();

makePlatformTools(new File(sdkDir, SdkConstants.FD_PLATFORM_TOOLS));

        if (mRepoXsdLevel >= 8) {
            makeBuildTools(new File(sdkDir, SdkConstants.FD_BUILD_TOOLS));
        }

File toolsLibEmuDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
toolsLibEmuDir.mkdirs();
//Synthetic comment -- @@ -256,6 +269,12 @@
new File(buildToolsDir, SdkConstants.FN_AAPT).createNewFile();
new File(buildToolsDir, SdkConstants.FN_AIDL).createNewFile();
new File(buildToolsDir, SdkConstants.FN_DX).createNewFile();
            new File(buildToolsDir, SdkConstants.FN_DX_JAR).createNewFile();
            new File(buildToolsDir, SdkConstants.FN_RENDERSCRIPT).createNewFile();
            new File(buildToolsDir, SdkConstants.FN_FRAMEWORK_RENDERSCRIPT).mkdir();
            new File(buildToolsDir, SdkConstants.OS_FRAMEWORK_RS).mkdir();
            new File(buildToolsDir, SdkConstants.OS_FRAMEWORK_RS_CLANG).mkdir();

}
}









//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
//Synthetic comment -- index 9bf2703..621ac0a 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index 07e8186..2803052 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;

import java.util.Arrays;

import junit.framework.TestCase;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java
//Synthetic comment -- index b77caad..b6dc5d4 100755

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.MajorRevision;

import junit.framework.TestCase;

public class MajorRevisionTest extends TestCase {








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockBuildToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockBuildToolPackage.java
//Synthetic comment -- index 85fb617..3150fb2 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;

/**
* A mock {@link BuildToolPackage} for testing.








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java
//Synthetic comment -- index dd744ec..ccd2048 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.internal.repository.MockPlatformTarget;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
//Synthetic comment -- index e742dee..734a596 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;

/**
* A mock {@link PlatformToolPackage} for testing.








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index 0382f34..3503621 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.FullRevision;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;







