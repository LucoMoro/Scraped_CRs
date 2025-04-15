/*Reorganize sdklib.repository packages.

This splits the sdklib repository in 3 sub-packages
for archives, packages and sources.

There are a lot of files moved around but its just
a move refactoring and the only thing changes are
imports and a few methods made public.

Change-Id:I6ce0e872ac7afea2a6a4eb70ee7bbad0c04b6678*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 6fcc804..9cd0dd5 100644

//Synthetic comment -- @@ -36,8 +36,8 @@
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.xml.AndroidXPathFactory;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index fb7f0bf..8284054 100644

//Synthetic comment -- @@ -25,11 +25,11 @@
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.repository.PkgProps;
import com.android.util.Pair;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index 29ea5ff..a4d1599 100755

//Synthetic comment -- @@ -23,8 +23,18 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SamplePackage;
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.util.Pair;

import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index 69d232a..74b023e 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
* This class holds methods for adding URLs management.
* @see #openUrl(String, ITaskMonitor, Header[])
*/
public class UrlOpener {

public static class CanceledByUserException extends Exception {
private static final long serialVersionUID = -7669346110926032403L;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/XmlParserUtils.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/XmlParserUtils.java
//Synthetic comment -- index 61b5b24..e4f2419 100755

//Synthetic comment -- @@ -21,7 +21,7 @@
/**
* Misc utilities to help extracting elements and attributes out of an XML document.
*/
public class XmlParserUtils {

/**
* Returns the first child element with the given XML local name.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/Archive.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/Archive.java
//Synthetic comment -- index 617ba42..cd79275 100755

//Synthetic comment -- @@ -14,10 +14,13 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.archives;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;

import java.io.File;
//Synthetic comment -- @@ -190,7 +193,7 @@
/**
* Creates a new remote archive.
*/
    public Archive(Package pkg, Os os, Arch arch, String url, long size, String checksum) {
mPackage = pkg;
mOs = os;
mArch = arch;
//Synthetic comment -- @@ -206,7 +209,7 @@
* Uses the properties from props first, if possible. Props can be null.
*/
@VisibleForTesting(visibility=Visibility.PACKAGE)
    public Archive(Package pkg, Properties props, Os os, Arch arch, String localOsPath) {
mPackage = pkg;

mOs   = props == null ? os   : Os.valueOf(  props.getProperty(PROP_OS,   os.toString()));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
similarity index 98%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 53fc140..3addb31 100755

//Synthetic comment -- @@ -14,12 +14,16 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.archives;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.RepoConstants;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveReplacement.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveReplacement.java
similarity index 94%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveReplacement.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveReplacement.java
//Synthetic comment -- index 6dcd9bb..10987fa 100755

//Synthetic comment -- @@ -14,7 +14,10 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.archives;

import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.packages.Package;


/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/AddonPackage.java
similarity index 97%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/AddonPackage.java
//Synthetic comment -- index 606775f..cc35e54 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -24,8 +24,11 @@
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -126,7 +129,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public AddonPackage(
SdkSource source,
Node packageNode,
String nsUri,
//Synthetic comment -- @@ -207,7 +210,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(IAndroidTarget target, Properties props) {
return new AddonPackage(target, props);
}

//Synthetic comment -- @@ -303,7 +306,7 @@
* @param addonProps The properties parsed from the addon manifest (NOT the source.properties).
* @param error The error indicating why this addon failed to be loaded.
*/
    public static Package createBroken(
String archiveOsPath,
Properties sourceProps,
Map<String, String> addonProps,
//Synthetic comment -- @@ -357,7 +360,7 @@
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

mVersion.saveProperties(props);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/BrokenPackage.java
similarity index 93%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/BrokenPackage.java
//Synthetic comment -- index 3e6b2e9..6a7e9c6 100755

//Synthetic comment -- @@ -14,11 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;

import java.io.File;
import java.util.Properties;
//Synthetic comment -- @@ -81,7 +84,7 @@
* Base implementation override: We don't actually save properties for a broken package.
*/
@Override
    public void saveProperties(Properties props) {
// Nop. We don't actually save properties for a broken package.
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java
similarity index 94%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DocPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/DocPackage.java
//Synthetic comment -- index 2c78cd1..54dfc5e 100755

//Synthetic comment -- @@ -14,13 +14,16 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.SdkRepoConstants;

import org.w3c.dom.Node;
//Synthetic comment -- @@ -50,7 +53,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public DocPackage(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

int apiLevel = XmlParserUtils.getXmlInt   (packageNode, SdkRepoConstants.NODE_API_LEVEL, 0);
//Synthetic comment -- @@ -68,7 +71,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(SdkSource source,
Properties props,
int apiLevel,
String codename,
//Synthetic comment -- @@ -111,7 +114,7 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

mVersion.saveProperties(props);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java
similarity index 97%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java
//Synthetic comment -- index 4e922c5..cc27853 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -22,8 +22,14 @@
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.RepoConstants;

//Synthetic comment -- @@ -90,7 +96,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public ExtraPackage(
SdkSource source,
Node packageNode,
String nsUri,
//Synthetic comment -- @@ -173,7 +179,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(SdkSource source,
Properties props,
String vendor,
String path,
//Synthetic comment -- @@ -271,7 +277,7 @@
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

props.setProperty(PkgProps.EXTRA_PATH, mPath);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IExactApiLevelDependency.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IExactApiLevelDependency.java
//Synthetic comment -- index 2a0130c..eaeccdb 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.RepoConstants;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ILayoutlibVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ILayoutlibVersion.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ILayoutlibVersion.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ILayoutlibVersion.java
//Synthetic comment -- index eb57343..74b18bf 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.util.Pair;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinApiLevelDependency.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinApiLevelDependency.java
//Synthetic comment -- index e23f3b6..8baafe9 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinPlatformToolsDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinPlatformToolsDependency.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinPlatformToolsDependency.java
//Synthetic comment -- index c536b3e..32468a4 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinToolsDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinToolsDependency.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IMinToolsDependency.java
//Synthetic comment -- index 2f0b8fa..76cdd66 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IPackageVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPackageVersion.java
similarity index 95%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IPackageVersion.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPackageVersion.java
//Synthetic comment -- index 911ba8d..77a6a1d 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IPlatformDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPlatformDependency.java
similarity index 95%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IPlatformDependency.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/IPlatformDependency.java
//Synthetic comment -- index 3aba333..a61fbea 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LayoutlibVersionMixin.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/LayoutlibVersionMixin.java
similarity index 97%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LayoutlibVersionMixin.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/LayoutlibVersionMixin.java
//Synthetic comment -- index eb819d7..5b582c1 100755

//Synthetic comment -- @@ -14,8 +14,9 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.RepoConstants;
import com.android.util.Pair;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/MinToolsPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MinToolsPackage.java
similarity index 91%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/MinToolsPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MinToolsPackage.java
//Synthetic comment -- index 9a9f416..99602c8 100755

//Synthetic comment -- @@ -14,10 +14,12 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;

//Synthetic comment -- @@ -93,7 +95,7 @@
}

@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

if (getMinToolsRevision() != MIN_TOOLS_REV_NOT_SPECIFIED) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java
similarity index 97%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index a86171c..0e2b615 100755

//Synthetic comment -- @@ -14,15 +14,22 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -214,7 +221,7 @@
* Save the properties of the current packages in the given {@link Properties} object.
* These properties will later be give the constructor that takes a {@link Properties} object.
*/
    public void saveProperties(Properties props) {
props.setProperty(PkgProps.PKG_REVISION, Integer.toString(mRevision));
if (mLicense != null && mLicense.length() > 0) {
props.setProperty(PkgProps.PKG_LICENSE, mLicense);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java
similarity index 94%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformPackage.java
//Synthetic comment -- index 937a4b4..391c32e 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -22,8 +22,11 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;
//Synthetic comment -- @@ -61,7 +64,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public PlatformPackage(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

mVersionName = XmlParserUtils.getXmlString(packageNode, SdkRepoConstants.NODE_VERSION);
//Synthetic comment -- @@ -87,7 +90,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(IAndroidTarget target, Properties props) {
return new PlatformPackage(target, props);
}

//Synthetic comment -- @@ -120,7 +123,7 @@
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

mVersion.saveProperties(props);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
similarity index 93%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index 33fb8ee..6070593 100755

//Synthetic comment -- @@ -14,14 +14,19 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.AdbWrapper;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;

import org.w3c.dom.Node;

//Synthetic comment -- @@ -49,7 +54,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public PlatformToolPackage(SdkSource source, Node packageNode,
String nsUri, Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);
}
//Synthetic comment -- @@ -61,7 +66,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(
SdkSource source,
Properties props,
int revision,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java
similarity index 95%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SamplePackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java
//Synthetic comment -- index 9b2daf7..ed5eda5 100755

//Synthetic comment -- @@ -14,15 +14,20 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;
//Synthetic comment -- @@ -65,7 +70,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public SamplePackage(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

int apiLevel = XmlParserUtils.getXmlInt   (packageNode, SdkRepoConstants.NODE_API_LEVEL, 0);
//Synthetic comment -- @@ -92,7 +97,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(IAndroidTarget target, Properties props) {
return new SamplePackage(target, props);
}

//Synthetic comment -- @@ -128,7 +133,7 @@
* @throws AndroidVersionException if the {@link AndroidVersion} can't be restored
*                                 from properties.
*/
    public static Package create(String archiveOsPath, Properties props) throws AndroidVersionException {
return new SamplePackage(archiveOsPath, props);
}

//Synthetic comment -- @@ -157,7 +162,7 @@
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

mVersion.saveProperties(props);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SourcePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java
similarity index 94%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SourcePackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SourcePackage.java
//Synthetic comment -- index 1eb00e1..2c577e4 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -22,8 +22,13 @@
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.repository.SdkRepoConstants;

//Synthetic comment -- @@ -56,7 +61,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public SourcePackage(SdkSource source,
Node packageNode,
String nsUri,
Map<String,String> licenses) {
//Synthetic comment -- @@ -171,7 +176,7 @@
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);
mVersion.saveProperties(props);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SystemImagePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java
similarity index 96%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SystemImagePackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java
//Synthetic comment -- index a246584..c862882 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -23,8 +23,11 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.SystemImage;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdklib.repository.SdkRepoConstants;

//Synthetic comment -- @@ -57,7 +60,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public SystemImagePackage(SdkSource source,
Node packageNode,
String nsUri,
Map<String,String> licenses) {
//Synthetic comment -- @@ -74,7 +77,7 @@
}

@VisibleForTesting(visibility=Visibility.PRIVATE)
    public SystemImagePackage(
AndroidVersion platformVersion,
int revision,
String abi,
//Synthetic comment -- @@ -185,7 +188,7 @@
* These properties will later be given to a constructor that takes a {@link Properties} object.
*/
@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

mVersion.saveProperties(props);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
similarity index 93%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ToolPackage.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 3ddacb4..e4a8fe6 100755

//Synthetic comment -- @@ -14,14 +14,19 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
//Synthetic comment -- @@ -43,7 +48,7 @@
/** The value returned by {@link ToolPackage#installId()}. */
public static final String INSTALL_ID = "tools";                             //$NON-NLS-1$

    public static final String PROP_MIN_PLATFORM_TOOLS_REV =
"Platform.MinPlatformToolsRev";  //$NON-NLS-1$

/**
//Synthetic comment -- @@ -62,7 +67,7 @@
*          parameters that vary according to the originating XML schema.
* @param licenses The licenses loaded from the XML originating document.
*/
    public ToolPackage(SdkSource source, Node packageNode, String nsUri, Map<String,String> licenses) {
super(source, packageNode, nsUri, licenses);

mMinPlatformToolsRevision = XmlParserUtils.getXmlInt(
//Synthetic comment -- @@ -102,7 +107,7 @@
* <p/>
* By design, this creates a package with one and only one archive.
*/
    public static Package create(
SdkSource source,
Properties props,
int revision,
//Synthetic comment -- @@ -226,7 +231,7 @@
}

@Override
    public void saveProperties(Properties props) {
super.saveProperties(props);

if (getMinPlatformToolsRevision() != MIN_PLATFORM_TOOLS_REV_INVALID) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkAddonSource.java
similarity index 95%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkAddonSource.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkAddonSource.java
//Synthetic comment -- index 42f7603..39a134b 100755

//Synthetic comment -- @@ -14,9 +14,10 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.annotations.Nullable;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.repository.SdkAddonConstants;

import org.w3c.dom.Document;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java
similarity index 98%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkRepoSource.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkRepoSource.java
//Synthetic comment -- index 00392de..07c3a86 100755

//Synthetic comment -- @@ -14,11 +14,13 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.annotations.Nullable;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.XmlParserUtils;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkRepoConstants;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
similarity index 97%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 749bc01..45646f0 100755

//Synthetic comment -- @@ -14,12 +14,26 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.UrlOpener;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SamplePackage;
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSourceCategory.java
similarity index 95%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceCategory.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSourceCategory.java
//Synthetic comment -- index 546e991..fac2c8b 100755

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.sdklib.internal.repository.IDescription;


/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSourceProperties.java
similarity index 99%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSourceProperties.java
//Synthetic comment -- index 7f7b8c2..915dc36 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java
similarity index 99%
rename from sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java
//Synthetic comment -- index dc33966..b1354c3 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java
//Synthetic comment -- index 41b184e..52d3a14 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.sdklib.repository;


import com.android.sdklib.internal.repository.sources.SdkSource;

import java.io.InputStream;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java
//Synthetic comment -- index b4a75a9..258ea26 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.sdklib.repository;


import com.android.sdklib.internal.repository.sources.SdkSource;

import java.io.InputStream;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformTarget.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformTarget.java
//Synthetic comment -- index 903a276..f0d7565 100755

//Synthetic comment -- @@ -30,7 +30,7 @@
* A mock PlatformTarget.
* This reimplements the minimum needed from the interface for our limited testing needs.
*/
public class MockPlatformTarget implements IAndroidTarget {

private final int mApiLevel;
private final int mRevision;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveInstallerTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java
similarity index 93%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveInstallerTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveInstallerTest.java
//Synthetic comment -- index 8f42f8a..3f70d79 100755

//Synthetic comment -- @@ -14,10 +14,23 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.archives;

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.MockMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
import com.android.sdklib.internal.repository.packages.MockExtraPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.IFileOp;
import com.android.sdklib.io.MockFileOp;
import com.android.sdklib.repository.PkgProps;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveTest.java
similarity index 93%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/archives/ArchiveTest.java
//Synthetic comment -- index f6bd5a4..c0ae718 100755

//Synthetic comment -- @@ -14,10 +14,11 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.archives;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;

import junit.framework.TestCase;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/BrokenPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/BrokenPackageTest.java
similarity index 92%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/BrokenPackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/BrokenPackageTest.java
//Synthetic comment -- index 03b1e1d..92a04c4 100755

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.packages.BrokenPackage;

import junit.framework.TestCase;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ExtraPackageTest_v3.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/ExtraPackageTest_v3.java
similarity index 95%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ExtraPackageTest_v3.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/ExtraPackageTest_v3.java
//Synthetic comment -- index c895fc9..6102f13 100755

//Synthetic comment -- @@ -14,10 +14,11 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.repository.PkgProps;

import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ExtraPackageTest_v4.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/ExtraPackageTest_v4.java
similarity index 95%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ExtraPackageTest_v4.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/ExtraPackageTest_v4.java
//Synthetic comment -- index 19e50f0..eff3020 100755

//Synthetic comment -- @@ -14,10 +14,11 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.repository.PkgProps;

import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MinToolsPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MinToolsPackageTest.java
similarity index 90%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MinToolsPackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MinToolsPackageTest.java
//Synthetic comment -- index 451bdb2..00986a4 100755

//Synthetic comment -- @@ -14,11 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockAddonPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockAddonPackage.java
similarity index 97%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockAddonPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockAddonPackage.java
//Synthetic comment -- index 3953f79..40d563f 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.ISystemImage.LocationType;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SystemImage;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.io.FileOp;
import com.android.sdklib.repository.PkgProps;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockBrokenPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockBrokenPackage.java
similarity index 93%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockBrokenPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockBrokenPackage.java
//Synthetic comment -- index 9ea9dfa..e4b307b 100755

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.packages.BrokenPackage;


/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockEmptyPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java
similarity index 92%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockEmptyPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockEmptyPackage.java
//Synthetic comment -- index 7fdee5b..f1e4344 100755

//Synthetic comment -- @@ -14,14 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;

import java.io.File;
import java.util.Properties;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockExtraPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockExtraPackage.java
similarity index 85%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockExtraPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockExtraPackage.java
//Synthetic comment -- index 4011f3e..6225935 100755

//Synthetic comment -- @@ -14,10 +14,13 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;

import java.util.Properties;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java
similarity index 90%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformPackage.java
//Synthetic comment -- index 8313b8a..01181a1 100755

//Synthetic comment -- @@ -14,9 +14,12 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.MockPlatformTarget;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
similarity index 84%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockPlatformToolPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockPlatformToolPackage.java
//Synthetic comment -- index 788fdf7..fc01b3c 100755

//Synthetic comment -- @@ -14,10 +14,12 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
* A mock {@link PlatformToolPackage} for testing.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockSourcePackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockSourcePackage.java
similarity index 86%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockSourcePackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockSourcePackage.java
//Synthetic comment -- index ea65566..585ca2e 100755

//Synthetic comment -- @@ -14,9 +14,12 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.sources.SdkSource;


/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockSystemImagePackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockSystemImagePackage.java
similarity index 90%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockSystemImagePackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockSystemImagePackage.java
//Synthetic comment -- index 3237fbb..534dad8 100755

//Synthetic comment -- @@ -14,7 +14,10 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.sources.SdkSource;


/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockToolPackage.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java
similarity index 86%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockToolPackage.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/MockToolPackage.java
//Synthetic comment -- index 7cbf802..e1da422 100755

//Synthetic comment -- @@ -14,10 +14,12 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;

import java.util.Properties;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
similarity index 92%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/PackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index 5cf1c98..ce41a4d 100755

//Synthetic comment -- @@ -14,11 +14,16 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.BrokenPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;

import java.io.File;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/PlatformPackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PlatformPackageTest.java
similarity index 89%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/PlatformPackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PlatformPackageTest.java
//Synthetic comment -- index e657425..a0b4ab7 100755

//Synthetic comment -- @@ -14,11 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.MockPlatformTarget;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SourcePackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/SourcePackageTest.java
similarity index 92%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SourcePackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/SourcePackageTest.java
//Synthetic comment -- index 2311feb..66f37dc 100755

//Synthetic comment -- @@ -14,12 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SystemImagePackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/SystemImagePackageTest.java
similarity index 93%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SystemImagePackageTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/SystemImagePackageTest.java
//Synthetic comment -- index e6f0a2d..14e1c3e 100755

//Synthetic comment -- @@ -14,12 +14,14 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.packages;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.Archive.Arch;
import com.android.sdklib.internal.repository.archives.Archive.Os;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.repository.PkgProps;

import java.util.Properties;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkAddonSourceTest.java
similarity index 97%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkAddonSourceTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkAddonSourceTest.java
//Synthetic comment -- index 11626e6..b9a3436 100755

//Synthetic comment -- @@ -14,9 +14,16 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.MockMonitor;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.util.Pair;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkRepoSourceTest.java
similarity index 97%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkRepoSourceTest.java
//Synthetic comment -- index 0659fc4..1bc9639 100755

//Synthetic comment -- @@ -14,9 +14,20 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.MockMonitor;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SourcePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkSourcePropertiesTest.java
similarity index 97%
rename from sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java
rename to sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkSourcePropertiesTest.java
//Synthetic comment -- index b4aa2e5..6313e69 100755

//Synthetic comment -- @@ -14,9 +14,11 @@
* limitations under the License.
*/

package com.android.sdklib.internal.repository.sources;


import com.android.sdklib.internal.repository.sources.SdkSourceProperties;

import junit.framework.TestCase;

public class SdkSourcePropertiesTest extends TestCase {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ArchiveInfo.java
//Synthetic comment -- index d98af7e..dafcc05 100755

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;

import java.util.ArrayList;
import java.util.Collection;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 8577da4..b5ab90b 100755

//Synthetic comment -- @@ -18,10 +18,10 @@

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IPackageVersion;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index 0d9b10a..0b9f396 100755

//Synthetic comment -- @@ -17,28 +17,28 @@
package com.android.sdkuilib.internal.repository;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.packages.IMinToolsDependency;
import com.android.sdklib.internal.repository.packages.IPackageVersion;
import com.android.sdklib.internal.repository.packages.IPlatformDependency;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SamplePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;

import java.util.ArrayList;
import java.util.Arrays;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 0def6fa..a58e0f0 100755

//Synthetic comment -- @@ -24,25 +24,25 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AdbWrapper;
import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/icons/ImageFactory.java
//Synthetic comment -- index 1d58541..aaa190c 100755

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.sdkuilib.internal.repository.icons;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdkuilib.internal.repository.sdkman2.PkgContentProvider;

import org.eclipse.swt.SWTException;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java
//Synthetic comment -- index 015539c..10b02c6 100755

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java
//Synthetic comment -- index 2311df2..d619ea0 100755

//Synthetic comment -- @@ -19,12 +19,12 @@

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader.IAutoInstallTask;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index ad9a2a2..4ac03e9 100755

//Synthetic comment -- @@ -16,14 +16,14 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;

import org.eclipse.swt.widgets.Display;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 0774905..46b5697 100755

//Synthetic comment -- @@ -19,14 +19,14 @@
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IPackageVersion;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.sdkman2.PkgItem.PkgState;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index e0d97f9..4ef7d6b 100755

//Synthetic comment -- @@ -17,13 +17,13 @@
package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategorySource.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategorySource.java
//Synthetic comment -- index 5b589d0..7e08cf1 100755

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;










//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgContentProvider.java
//Synthetic comment -- index 4867ebb..f1be1ec 100755

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgItem.java
//Synthetic comment -- index e943819..5b90375 100755

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.IPackageVersion;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
* A {@link PkgItem} represents one main {@link Package} combined with its state








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index febbf49..9a7ffe1 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index e691429..2260739 100755

//Synthetic comment -- @@ -18,14 +18,14 @@

import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index d1d95d7..6b8c850 100755

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;

import java.util.ArrayList;
import java.util.Arrays;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 245ca84..f186c84 100755

//Synthetic comment -- @@ -19,17 +19,17 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.packages.MockToolPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index bc532a5..a328ef1 100755

//Synthetic comment -- @@ -17,18 +17,18 @@
package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.packages.BrokenPackage;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
import com.android.sdklib.internal.repository.packages.MockExtraPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.packages.MockSystemImagePackage;
import com.android.sdklib.internal.repository.packages.MockToolPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.MockUpdaterData;








