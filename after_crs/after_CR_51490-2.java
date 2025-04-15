/*SdkManager method to query known build-tools.

Also refactors FullRevision and MajorRevision to move them
out of internal.repository. Their API is not going to change.

Change-Id:I3eab40fc2c4844a9ddb4fcadd9eb0c571fc437d4*/




//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java b/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..4c75821

//Synthetic comment -- @@ -0,0 +1,170 @@
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
        /** OS Path to the target's version of the aapt tool.
          * This is deprecated as aapt is now in the platform tools and not in the platform. */
        AAPT,
        /** OS Path to the target's version of the aidl tool.
          * This is deprecated as aidl is now in the platform tools and not in the platform. */
        AIDL,
        /** OS Path to the target's version of the dx too.<br>
         * This is deprecated as dx is now in the platform tools and not in the platform. */
        DX,
        /** OS Path to the target's version of the dx.jar file.<br>
         * This is deprecated as dx.jar is now in the platform tools and not in the platform. */
        DX_JAR,
        /** OS Path to the "ant" folder which contains the ant build rules (ver 2 and above) */
        ANT,
        // FIXME: do we want to put the rs tool in build-tools too?
        ///** OS Path to the Renderscript include folder.
        //  * This is deprecated as this is now in the platform tools and not in the platform. */
        //ANDROID_RS,
        ///** OS Path to the Renderscript(clang) include folder.
        //  * This is deprecated as this is now in the platform tools and not in the platform. */
        //ANDROID_RS_CLANG,
    }

    /** The build-tool revision. */
    private final FullRevision mRevision;
    /** The path to the build-tool folder specific to this revision. */
    private final File mPath;

    private final Map<PathId, String> mPaths = Maps.newEnumMap(PathId.class);

    public BuildToolInfo(FullRevision revision, File path) {
        mRevision = revision;
        mPath = path;

        add(PathId.ANT, SdkConstants.FD_ANT);

        add(PathId.AAPT, SdkConstants.FN_AAPT);
        add(PathId.AIDL, SdkConstants.FN_AIDL);
        add(PathId.DX, SdkConstants.FN_DX);
        add(PathId.DX_JAR, SdkConstants.FN_DX_JAR);
        //TODO do we want to put Renderscript in build-tools?
        //add(PathId.ANDROID_RS, SdkConstants.OS_FRAMEWORK_RS);
        //add(PathId.ANDROID_RS_CLANG, SdkConstants.OS_FRAMEWORK_RS_CLANG);

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
     *          Any of the constants defined in {@link BuildToolInfo} can be used.
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
     * Only the last segment of the path is returned, to avoid having temporary
     * folders contaminate the tests results.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<BuildToolInfo rev=").append(mRevision);    //$NON-NLS-1$
        builder.append(", mPath=.../").append(mPath.getName());     //$NON-NLS-1$
        builder.append(", mPaths=(");                               //$NON-NLS-1$
        boolean prefix = false;
        for (Map.Entry<PathId, String> entry : mPaths.entrySet()) {
            if (prefix) {
                builder.append(", ");                               //$NON-NLS-1$
            }
            builder.append(entry.getKey())
                   .append(": .../")                                //$NON-NLS-1$
                   .append(new File(entry.getValue()).getName());
            prefix = true;
        }
        builder.append(")>");                                       //$NON-NLS-1$
        return builder.toString();
    }



}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/SdkManager.java b/sdklib/src/main/java/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 0a5e06b..f8c481b 100644

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
//Synthetic comment -- @@ -146,10 +153,12 @@
*/
public void reloadSdk(ILogger log) {
// get the current target list.
        mVisistedDirs.clear();
        ArrayList<IAndroidTarget> targets = Lists.newArrayList();
        loadPlatforms(mOsSdkPath, targets, mVisistedDirs, log);
        loadAddOns(mOsSdkPath, targets, mVisistedDirs, log);
        Map<FullRevision, BuildToolInfo> buildTools = Maps.newHashMap();
        loadBuildTools(mOsSdkPath, buildTools, mVisistedDirs, log);

// For now replace the old list with the new one.
// In the future we may want to keep the current objects, so that ADT doesn't have to deal
//Synthetic comment -- @@ -158,13 +167,14 @@
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
//Synthetic comment -- @@ -174,16 +184,22 @@
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
//Synthetic comment -- @@ -193,43 +209,18 @@
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
//Synthetic comment -- @@ -272,6 +263,29 @@
mTargets = targets;
}

    private void setBuildTools(Map<FullRevision, BuildToolInfo> buildTools) {
        assert buildTools != null;
        mBuildTools = buildTools;
    }

    /** Returns the set of known build-tools revisions. Can be empty but not null. */
    @NonNull
    public Set<FullRevision> getBuildTools() {
        return mBuildTools.keySet();
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
//Synthetic comment -- @@ -461,7 +475,8 @@
private static void loadPlatforms(
String sdkOsPath,
ArrayList<IAndroidTarget> targets,
            Map<File, DirInfo> dirInfos,
            ILogger log) {
File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);

if (platformFolder.isDirectory()) {
//Synthetic comment -- @@ -801,7 +816,8 @@
private static void loadAddOns(
String osSdkPath,
ArrayList<IAndroidTarget> targets,
            Map<File, DirInfo> dirInfos,
            ILogger log) {
File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);

if (addonFolder.isDirectory()) {
//Synthetic comment -- @@ -1230,6 +1246,94 @@
return null;
}

    /**
     * Loads the build-tools from the SDK.
     * Creates the "build-tools" folder if necessary.
     *
     * @param sdkOsPath Location of the SDK
     * @param infos the list to fill with the build-tools.
     * @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
     * @throws RuntimeException when the "platforms" folder is missing and cannot be created.
     */
    private static void loadBuildTools(
            String sdkOsPath,
            Map<FullRevision, BuildToolInfo> infos,
            Map<File, DirInfo> dirInfos,
            ILogger log) {
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
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
     */
    private static BuildToolInfo loadBuildTool(
            String sdkOsPath,
            File folder,
            ILogger log) {
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
//Synthetic comment -- index 487a96a..9efe001 100755

//Synthetic comment -- @@ -20,8 +20,12 @@
import com.android.SdkConstants;
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.SdkManager.LayoutlibVersion;
import com.android.sdklib.repository.FullRevision;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class SdkManagerTest extends SdkManagerTestCase {
//Synthetic comment -- @@ -41,6 +45,37 @@
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
                "mPath=.../3.0.0, " +
                "mPaths=(AAPT: .../aapt.exe, AIDL: .../aidl.exe, DX: .../dx.bat, DX_JAR: .../dx.jar, ANT: .../ant)>",
                i.toString());

        i = sdkman.getBuildTool(new FullRevision(12, 3, 4, 5));
        assertEquals(
                "<BuildToolInfo rev=12.3.4 rc5, " +
                "mPath=.../12.3.4 rc5, " +
                "mPaths=(AAPT: .../aapt.exe, AIDL: .../aidl.exe, DX: .../dx.bat, DX_JAR: .../dx.jar, ANT: .../ant)>",
                i.toString());
    }

public void testSdkManager_SystemImage() throws Exception {
SdkManager sdkman = getSdkManager();
assertEquals("[PlatformTarget API 0 rev 1]", Arrays.toString(sdkman.getTargets()));








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 9ca4c32..030f6c9 100755

//Synthetic comment -- @@ -256,6 +256,8 @@
new File(buildToolsDir, SdkConstants.FN_AAPT).createNewFile();
new File(buildToolsDir, SdkConstants.FN_AIDL).createNewFile();
new File(buildToolsDir, SdkConstants.FN_DX).createNewFile();
            new File(buildToolsDir, SdkConstants.FN_DX_JAR).createNewFile();
            new File(buildToolsDir, SdkConstants.FD_ANT).mkdir();
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







