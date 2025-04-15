/*SdkManager method to query known build-tools.

Also refactors FullRevision and MajorRevision to move them
out of internal.repository. Their API is not going to change.

Change-Id:I3eab40fc2c4844a9ddb4fcadd9eb0c571fc437d4*/
//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java b/sdklib/src/main/java/com/android/sdklib/BuildToolInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..3511f26

//Synthetic comment -- @@ -0,0 +1,149 @@








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/SdkManager.java b/sdklib/src/main/java/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 0a5e06b..bb9f54d 100644

//Synthetic comment -- @@ -33,10 +33,14 @@
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.ILogger;
import com.android.utils.NullLogger;
import com.android.utils.Pair;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -106,8 +110,11 @@
private final String mOsSdkPath;
/** Valid targets that have been loaded. Can be empty but not null. */
private IAndroidTarget[] mTargets = new IAndroidTarget[0];
/** A map to keep information on directories to see if they change later. */
    private final Map<File, DirInfo> mTargetDirs = new HashMap<File, SdkManager.DirInfo>();

/**
* Create a new {@link SdkManager} instance.
//Synthetic comment -- @@ -116,17 +123,20 @@
* @param osSdkPath the location of the SDK.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected SdkManager(String osSdkPath) {
mOsSdkPath = osSdkPath;
}

/**
* Creates an {@link SdkManager} for a given sdk location.
* @param osSdkPath the location of the SDK.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
* @return the created {@link SdkManager} or null if the location is not valid.
*/
    public static SdkManager createManager(String osSdkPath, ILogger log) {
try {
SdkManager manager = new SdkManager(osSdkPath);
manager.reloadSdk(log);
//Synthetic comment -- @@ -142,14 +152,16 @@
/**
* Reloads the content of the SDK.
*
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
*/
    public void reloadSdk(ILogger log) {
// get the current target list.
        mTargetDirs.clear();
        ArrayList<IAndroidTarget> targets = new ArrayList<IAndroidTarget>();
        loadPlatforms(mOsSdkPath, targets, mTargetDirs, log);
        loadAddOns(mOsSdkPath, targets, mTargetDirs, log);

// For now replace the old list with the new one.
// In the future we may want to keep the current objects, so that ADT doesn't have to deal
//Synthetic comment -- @@ -158,13 +170,14 @@
// sort the targets/add-ons
Collections.sort(targets);
setTargets(targets.toArray(new IAndroidTarget[targets.size()]));

// load the samples, after the targets have been set.
initializeSamplePaths(log);
}

/**
     * Checks whether any of the SDK platforms/add-ons have changed on-disk
* since we last loaded the SDK. This does not reload the SDK nor does it
* change the underlying targets.
*
//Synthetic comment -- @@ -174,16 +187,22 @@
Set<File> visited = new HashSet<File>();
boolean changed = false;

        File platformFolder = new File(mOsSdkPath, SdkConstants.FD_PLATFORMS);
        if (platformFolder.isDirectory()) {
            File[] platforms  = platformFolder.listFiles();
            if (platforms != null) {
                for (File platform : platforms) {
                    if (!platform.isDirectory()) {
continue;
}
                    visited.add(platform);
                    DirInfo dirInfo = mTargetDirs.get(platform);
if (dirInfo == null) {
// This is a new platform directory.
changed = true;
//Synthetic comment -- @@ -193,43 +212,18 @@
if (changed) {
if (DEBUG) {
System.out.println("SDK changed due to " +              //$NON-NLS-1$
                                (dirInfo != null ? dirInfo.toString() : platform.getPath()));
}
}
}
}
}

        File addonFolder = new File(mOsSdkPath, SdkConstants.FD_ADDONS);

        if (!changed && addonFolder.isDirectory()) {
            File[] addons  = addonFolder.listFiles();
            if (addons != null) {
                for (File addon : addons) {
                    if (!addon.isDirectory()) {
                        continue;
                    }
                    visited.add(addon);
                    DirInfo dirInfo = mTargetDirs.get(addon);
                    if (dirInfo == null) {
                        // This is a new add-on directory.
                        changed = true;
                    } else {
                        changed = dirInfo.hasChanged();
                    }
                    if (changed) {
                        if (DEBUG) {
                            System.out.println("SDK changed due to " +              //$NON-NLS-1$
                                (dirInfo != null ? dirInfo.toString() : addon.getPath()));
                        }
                    }
                }
            }
        }

if (!changed) {
// Check whether some pre-existing target directories have vanished.
            for (File previousDir : mTargetDirs.keySet()) {
if (!visited.contains(previousDir)) {
// This directory is no longer present.
changed = true;
//Synthetic comment -- @@ -248,6 +242,7 @@
/**
* Returns the location of the SDK.
*/
public String getLocation() {
return mOsSdkPath;
}
//Synthetic comment -- @@ -257,6 +252,7 @@
* <p/>
* The array can be empty but not null.
*/
public IAndroidTarget[] getTargets() {
return mTargets;
}
//Synthetic comment -- @@ -267,18 +263,42 @@
* The array can be empty but not null.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void setTargets(IAndroidTarget[] targets) {
assert targets != null;
mTargets = targets;
}

/**
* Returns a target from a hash that was generated by {@link IAndroidTarget#hashString()}.
*
* @param hash the {@link IAndroidTarget} hash string.
* @return The matching {@link IAndroidTarget} or null.
*/
    public IAndroidTarget getTargetFromHashString(String hash) {
if (hash != null) {
for (IAndroidTarget target : mTargets) {
if (hash.equals(target.hashString())) {
//Synthetic comment -- @@ -337,6 +357,7 @@
* @deprecated This does NOT solve the right problem and will be changed later.
*/
@Deprecated
public LayoutlibVersion getMaxLayoutlibVersion() {
LayoutlibVersion maxVersion = null;

//Synthetic comment -- @@ -455,13 +476,14 @@
* @param sdkOsPath Location of the SDK
* @param targets the list to fill with the platforms.
* @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
* @throws RuntimeException when the "platforms" folder is missing and cannot be created.
*/
private static void loadPlatforms(
            String sdkOsPath,
            ArrayList<IAndroidTarget> targets,
            Map<File, DirInfo> dirInfos, ILogger log) {
File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);

if (platformFolder.isDirectory()) {
//Synthetic comment -- @@ -503,12 +525,13 @@
* Loads a specific Platform at a given location.
* @param sdkOsPath Location of the SDK
* @param platformFolder the root folder of the platform.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
*/
private static PlatformTarget loadPlatform(
            String sdkOsPath,
            File platformFolder,
            ILogger log) {
FileWrapper buildProp = new FileWrapper(platformFolder, SdkConstants.FN_BUILD_PROP);
FileWrapper sourcePropFile = new FileWrapper(platformFolder, SdkConstants.FN_SOURCE_PROP);

//Synthetic comment -- @@ -647,9 +670,10 @@
*
* @param root Root of the add-on target being loaded.
* @return an array of ISystemImage containing all the system images for the target.
     *              The list can be empty.
*/
    private static ISystemImage[] getAddonSystemImages(File root) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();

root = new File(root, SdkConstants.OS_IMAGES_FOLDER);
//Synthetic comment -- @@ -695,12 +719,13 @@
* @param root Root of the platform target being loaded.
* @param version API level + codename of platform being loaded.
* @return an array of ISystemImage containing all the system images for the target.
     *              The list can be empty.
    */
private static ISystemImage[] getPlatformSystemImages(
            String sdkOsPath,
            File root,
            AndroidVersion version) {
Set<ISystemImage> found = new TreeSet<ISystemImage>();
Set<String> abiFound = new HashSet<String>();

//Synthetic comment -- @@ -741,8 +766,7 @@
abi));
abiFound.add(abi);
}
                        } catch (Exception ignore) {
                        }
}
}
}
//Synthetic comment -- @@ -795,13 +819,14 @@
* @param osSdkPath Location of the SDK
* @param targets the list to fill with the add-ons.
* @param dirInfos a map to keep information on directories to see if they change later.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
* @throws RuntimeException when the "add-ons" folder is missing and cannot be created.
*/
private static void loadAddOns(
            String osSdkPath,
            ArrayList<IAndroidTarget> targets,
            Map<File, DirInfo> dirInfos, ILogger log) {
File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);

if (addonFolder.isDirectory()) {
//Synthetic comment -- @@ -846,12 +871,13 @@
* Loads a specific Add-on at a given location.
* @param addonDir the location of the add-on directory.
* @param targetList The list of Android target that were already loaded from the SDK.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
*/
    private static AddOnTarget loadAddon(File addonDir,
            IAndroidTarget[] targetList,
            ILogger log) {

// Parse the addon properties to ensure we can load it.
Pair<Map<String, String>, String> infos = parseAddonProperties(addonDir, targetList, log);

//Synthetic comment -- @@ -978,8 +1004,8 @@
target.setSkins(skins, defaultSkin);

return target;
        }
        catch (Exception e) {
log.warning("Ignoring add-on '%1$s': error %2$s.",
addonDir.getName(), e.toString());
}
//Synthetic comment -- @@ -992,15 +1018,16 @@
*
* @param addonDir the location of the addon directory.
* @param targetList The list of Android target that were already loaded from the SDK.
     * @param log the ILogger object receiving warning/error from the parsing. Cannot be null.
* @return A pair with the property map and an error string. Both can be null but not at the
*  same time. If a non-null error is present then the property map must be ignored. The error
*  should be translatable as it might show up in the SdkManager UI.
*/
public static Pair<Map<String, String>, String> parseAddonProperties(
            File addonDir,
            IAndroidTarget[] targetList,
            ILogger log) {
Map<String, String> propertyMap = null;
String error = null;

//Synthetic comment -- @@ -1079,7 +1106,7 @@
* @param value the string to convert.
* @return the int value, or {@link IAndroidTarget#NO_USB_ID} if the conversion failed.
*/
    private static int convertId(String value) {
if (value != null && value.length() > 0) {
if (PATTERN_USB_IDS.matcher(value).matches()) {
String v = value.substring(2);
//Synthetic comment -- @@ -1101,7 +1128,8 @@
*
* @param valueName The missing manifest value, for display.
*/
    private static String addonManifestWarning(String valueName) {
return String.format("'%1$s' is missing from %2$s.",
valueName, SdkConstants.FN_MANIFEST_INI);
}
//Synthetic comment -- @@ -1113,9 +1141,11 @@
* aidl(.exe), dx(.bat), and dx.jar
*
* @param platform The folder containing the platform.
     * @param log Logger. Cannot be null.
*/
    private static boolean checkPlatformContent(File platform, ILogger log) {
for (String relativePath : sPlatformContentList) {
File f = new File(platform, relativePath);
if (!f.exists()) {
//Synthetic comment -- @@ -1134,7 +1164,8 @@
* Parses the skin folder and builds the skin list.
* @param osPath The path of the skin root folder.
*/
    private static String[] parseSkinFolder(String osPath) {
File skinRootFolder = new File(osPath);

if (skinRootFolder.isDirectory()) {
//Synthetic comment -- @@ -1168,9 +1199,9 @@
* have a separate SDK/samples/samples-API directory. This parses either directories
* and sets the targets' sample path accordingly.
*
     * @param log Logger. Cannot be null.
*/
    private void initializeSamplePaths(ILogger log) {
File sampleFolder = new File(mOsSdkPath, SdkConstants.FD_SAMPLES);
if (sampleFolder.isDirectory()) {
File[] platforms  = sampleFolder.listFiles();
//Synthetic comment -- @@ -1198,10 +1229,13 @@
* Returns the {@link AndroidVersion} of the sample in the given folder.
*
* @param folder The sample's folder.
     * @param log Logger for errors. Cannot be null.
* @return An {@link AndroidVersion} or null on error.
*/
    private AndroidVersion getSamplesVersion(File folder, ILogger log) {
File sourceProp = new File(folder, SdkConstants.FN_SOURCE_PROP);
try {
Properties p = new Properties();
//Synthetic comment -- @@ -1230,6 +1264,95 @@
return null;
}

// -------------

public static class LayoutlibVersion implements Comparable<LayoutlibVersion> {
//Synthetic comment -- @@ -1252,7 +1375,7 @@
}

@Override
        public int compareTo(LayoutlibVersion rhs) {
boolean useRev = this.mRevision > NOT_SPECIFIED && rhs.mRevision > NOT_SPECIFIED;
int lhsValue = (this.mApi << 16) + (useRev ? this.mRevision : 0);
int rhsValue = (rhs.mApi  << 16) + (useRev ? rhs.mRevision  : 0);
//Synthetic comment -- @@ -1336,7 +1459,7 @@
* Computes an adler32 checksum (source.props are small files, so this
* should be OK with an acceptable collision rate.)
*/
        private static long getFileChecksum(File file) {
FileInputStream fis = null;
try {
fis = new FileInputStream(file);








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/BuildToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/BuildToolPackage.java
//Synthetic comment -- index 23a6eec..23fe5ac 100755

//Synthetic comment -- @@ -23,9 +23,10 @@
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index 698b211..bf63752 100755

//Synthetic comment -- @@ -18,10 +18,11 @@

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IFullRevisionProvider.java
//Synthetic comment -- index 10f66d2..8a6a15e 100755

//Synthetic comment -- @@ -16,7 +16,9 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;











//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java
//Synthetic comment -- index d17b800..b8aae78 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java
//Synthetic comment -- index 064f1d3..d0f9e8d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;

/**








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java
//Synthetic comment -- index 4591297..45a018c 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MinToolsPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MinToolsPackage.java
//Synthetic comment -- index a608a3c..049137e 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 397cce1..1ca90dd 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PackageParserUtils.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PackageParserUtils.java
//Synthetic comment -- index a6986e1..a71247b 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;

import org.w3c.dom.Node;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index 29828f0..0930f86 100755

//Synthetic comment -- @@ -26,8 +26,8 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.sources.SdkSource;

import org.w3c.dom.Node;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index ddf71d8..2f0094d 100755

//Synthetic comment -- @@ -26,10 +26,11 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java
//Synthetic comment -- index 9e0912e..c5eb07e 100755

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
* A {@link PkgItem} represents one main {@link Package} combined with its state








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java
//Synthetic comment -- index be214f7..006e45f 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.sdklib.internal.repository.packages.BuildToolPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
//Synthetic comment -- @@ -41,6 +40,7 @@
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;

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

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;









//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java b/sdklib/src/main/java/com/android/sdklib/repository/MajorRevision.java
similarity index 96%
rename from sdklib/src/main/java/com/android/sdklib/internal/repository/packages/MajorRevision.java
rename to sdklib/src/main/java/com/android/sdklib/repository/MajorRevision.java
//Synthetic comment -- index eefda3b..c953e84 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;









//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTest.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTest.java
//Synthetic comment -- index 487a96a..7070ac1 100755

//Synthetic comment -- @@ -20,10 +20,15 @@
import com.android.SdkConstants;
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.SdkManager.LayoutlibVersion;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SdkManagerTest extends SdkManagerTestCase {

@SuppressWarnings("deprecation")
//Synthetic comment -- @@ -41,6 +46,47 @@
assertSame(lv, sdkman.getMaxLayoutlibVersion());
}

public void testSdkManager_SystemImage() throws Exception {
SdkManager sdkman = getSdkManager();
assertEquals("[PlatformTarget API 0 rev 1]", Arrays.toString(sdkman.getTargets()));
//Synthetic comment -- @@ -139,14 +185,16 @@
/**
* Sanitizes the paths used when testing results.
* <p/>
     * The system image text representation contains the absolute path to the SDK.
* However the SDK path is actually a randomized location.
* We clean it by replacing it by the constant '$SDK'.
     * Also all the Windows path separators are converted to unix-like / separators.
*/
private String cleanPath(SdkManager sdkman, String string) {
return string
            .replaceAll(Pattern.quote(sdkman.getLocation()), "\\$SDK")      //$NON-NLS-1$
.replace('\\', '/');
}
}








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTest7.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTest7.java
new file mode 100755
//Synthetic comment -- index 0000000..9c41bd2

//Synthetic comment -- @@ -0,0 +1,52 @@








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java b/sdklib/src/test/java/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 9ca4c32..30961a4 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.sdklib.io.FileOp;
import com.android.sdklib.mock.MockLog;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.ILogger;

import java.io.File;
//Synthetic comment -- @@ -42,6 +43,7 @@
private MockLog mLog;
private SdkManager mSdkManager;
private TmpAvdManager mAvdManager;

/** Returns the {@link MockLog} for this test case. */
public MockLog getLog() {
//Synthetic comment -- @@ -62,8 +64,8 @@
* Sets up a {@link MockLog}, a fake SDK in a temporary directory
* and an AVD Manager pointing to an initially-empty AVD directory.
*/
    @Override
    public void setUp() throws Exception {
mLog = new MockLog();
mFakeSdk = makeFakeSdk();
mSdkManager = SdkManager.createManager(mFakeSdk.getAbsolutePath(), mLog);
//Synthetic comment -- @@ -73,6 +75,15 @@
}

/**
* Removes the temporary SDK and AVD directories.
*/
@Override
//Synthetic comment -- @@ -151,8 +162,10 @@
new File(toolsDir, SdkConstants.FN_EMULATOR).createNewFile();

makePlatformTools(new File(sdkDir, SdkConstants.FD_PLATFORM_TOOLS));
        makeBuildTools(new File(sdkDir, SdkConstants.FD_BUILD_TOOLS));


File toolsLibEmuDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
toolsLibEmuDir.mkdirs();
//Synthetic comment -- @@ -256,6 +269,8 @@
new File(buildToolsDir, SdkConstants.FN_AAPT).createNewFile();
new File(buildToolsDir, SdkConstants.FN_AIDL).createNewFile();
new File(buildToolsDir, SdkConstants.FN_DX).createNewFile();
}
}









//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionPackageTest.java
//Synthetic comment -- index 9bf2703..621ac0a 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.PkgProps;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index 07e8186..2803052 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;

import java.util.Arrays;

import junit.framework.TestCase;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MajorRevisionTest.java
//Synthetic comment -- index b77caad..b6dc5d4 100755

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdklib.internal.repository.packages;

import junit.framework.TestCase;

public class MajorRevisionTest extends TestCase {








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockBuildToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockBuildToolPackage.java
//Synthetic comment -- index 85fb617..3150fb2 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
* A mock {@link BuildToolPackage} for testing.








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java
//Synthetic comment -- index dd744ec..ccd2048 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.internal.repository.MockPlatformTarget;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
//Synthetic comment -- index e742dee..734a596 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
* A mock {@link PlatformToolPackage} for testing.








//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockToolPackage.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index 0382f34..3503621 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;







