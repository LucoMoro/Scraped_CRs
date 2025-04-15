/*SDK Repository: new build-tool package type.

Related changes:
tools/base.git:Idb6d745156afca44eac83c1f3d5dbecaba8d053atools/swt.git:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fbChange-Id:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fb*/
//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 3bbeb54..e28f827 100755

//Synthetic comment -- @@ -19,7 +19,10 @@
import com.android.SdkConstants;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IFullRevisionProvider;
import com.android.sdklib.internal.repository.packages.Package;
//Synthetic comment -- @@ -34,6 +37,7 @@
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -95,6 +99,11 @@
Set<Integer> installedPlatforms = new HashSet<Integer>();
SparseArray<List<PkgItem>> platformItems = new SparseArray<List<PkgItem>>();

// sort items in platforms... directly deal with new/update items
List<PkgItem> allItems = getAllPkgItems(true /*byApi*/, true /*bySource*/);
for (PkgItem item : allItems) {
//Synthetic comment -- @@ -148,7 +157,7 @@
Package installed = item2.getMainPackage();

if (installed.getRevision().isPreview() &&
                                    newPkg2.sameItemAs(installed, true /*ignorePreviews*/)) {
sameFound = true;

if (installed.canBeUpdatedBy(newPkg) == UpdateInfo.UPDATE) {
//Synthetic comment -- @@ -167,8 +176,50 @@
} else if (selectUpdates && item.hasUpdatePkg()) {
item.setChecked(true);
}
}

List<PkgItem> items = platformItems.get(maxApi);
if (selectTop && maxApi > 0 && items != null) {
if (!installedPlatforms.contains(maxApi)) {
//Synthetic comment -- @@ -670,7 +721,9 @@
if (pkg instanceof IAndroidVersionProvider) {
return ((IAndroidVersionProvider) pkg).getAndroidVersion();

            } else if (pkg instanceof ToolPackage || pkg instanceof PlatformToolPackage) {
if (pkg.getRevision().isPreview()) {
return PkgCategoryApi.KEY_TOOLS_PREVIEW;
} else {
//Synthetic comment -- @@ -689,7 +742,7 @@
List<PkgCategory> cats = getCategories();
for (PkgCategory cat : cats) {
if (cat.getKey().equals(PkgCategoryApi.KEY_TOOLS)) {
                    // Mark them as no unused to prevent their removal in updateEnd().
keep(cat);
needTools = false;
} else if (cat.getKey().equals(PkgCategoryApi.KEY_EXTRA)) {








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java
//Synthetic comment -- index 1f484df..d3625ba 100755

//Synthetic comment -- @@ -47,10 +47,8 @@

public final static String SDK_PATH = "/tmp/SDK";

    private final List<ArchiveReplacement> mInstalled = new ArrayList<ArchiveReplacement>();

private DownloadCache mMockDownloadCache = new MockDownloadCache();

private final SdkSources mMockSdkSources = new SdkSources() {
@Override
public void loadUserAddons(ILogger log) {
//Synthetic comment -- @@ -150,7 +148,7 @@

public static SettingsController createSettingsController(ILogger sdkLog) {
Properties props = new Properties();
        Settings settings = new Settings(props) {}; // this constructor is protected
MockSettingsController controller = new MockSettingsController(sdkLog, settings);
controller.setProperties(props);
return controller;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java
//Synthetic comment -- index cc8a1b9..6f868cf 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
import com.android.sdklib.internal.repository.packages.MockExtraPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
//Synthetic comment -- @@ -34,8 +35,6 @@
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;

import java.util.Properties;

//Synthetic comment -- @@ -1786,6 +1785,141 @@
getTree(m, false /*displaySortByApi*/));
}



// ----








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
//Synthetic comment -- index 1746eec..a31cbac 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.repository.MockDownloadCache;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;

//Synthetic comment -- @@ -55,12 +56,17 @@
// the fake locally-installed SDK.
String actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                   |  |   |          \n" +
                " L_[] Android SDK Tools       |  | 0 | Installed\n" +
                "[]    Android 0.0 (API 0)     |  |   |          \n" +
                " L_[] SDK Platform            |  | 1 | Installed\n" +
                " L_[] Sources for Android SDK |  | 0 | Installed\n" +
                "[]    Extras                  |  |   |          ",
actual);

assertEquals(
//Synthetic comment -- @@ -68,29 +74,37 @@
Arrays.toString(cache.getDirectHits()));
assertEquals(
"[<https://dl-ssl.google.com/android/repository/addons_list-1.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/addons_list-2.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-5.xml : 2>, " +
                "<https://dl-ssl.google.com/android/repository/repository-6.xml : 2>, " +
                "<https://dl-ssl.google.com/android/repository/repository-7.xml : 2>, " +
                "<https://dl-ssl.google.com/android/repository/repository.xml : 2>]",
Arrays.toString(cache.getCachedHits()));


        // Now prepare a tools update on the server and reload
setupToolsXml1(cache);
cache.clearDirectHits();
cache.clearCachedHits();
pageImpl.fullReload();

actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                      |  |    |                              \n" +
                " L_[] Android SDK Tools          |  |  0 | Update available: rev. 20.0.3\n" +
                " L_[] Android SDK Platform-tools |  | 14 | Not installed                \n" +
                "[]    Android 0.0 (API 0)        |  |    |                              \n" +
                " L_[] SDK Platform               |  |  1 | Installed                    \n" +
                " L_[] Sources for Android SDK    |  |  0 | Installed                    \n" +
                "[]    Extras                     |  |    |                              ",
actual);

assertEquals(
//Synthetic comment -- @@ -98,9 +112,10 @@
Arrays.toString(cache.getDirectHits()));
assertEquals(
"[<https://dl-ssl.google.com/android/repository/repository-5.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-6.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-7.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository.xml : 1>]",
Arrays.toString(cache.getCachedHits()));


//Synthetic comment -- @@ -115,13 +130,18 @@

actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                      |  |    |                              \n" +
                " L_[] Android SDK Tools          |  |  0 | Update available: rev. 20.0.3\n" +
                " L_[] Android SDK Platform-tools |  | 14 | Not installed                \n" +
                "[]    Android 0.0 (API 0)        |  |    |                              \n" +
                " L_[] SDK Platform               |  |  1 | Installed                    \n" +
                " L_[] Sources for Android SDK    |  |  0 | Installed                    \n" +
                "[]    Extras                     |  |    |                              ",
actual);

assertEquals(
//Synthetic comment -- @@ -131,6 +151,44 @@
"[<https://dl-ssl.google.com/android/repository/repository-5.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository-6.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository-7.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository.xml : 1>]",
Arrays.toString(cache.getCachedHits()));
}
//Synthetic comment -- @@ -138,28 +196,78 @@
private void setupToolsXml1(MockDownloadCache cache) throws Exception {
String repoXml =
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<sdk:sdk-repository xmlns:sdk=\"http://schemas.android.com/sdk/android/repository/7\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"<sdk:license id=\"android-sdk-license\" type=\"text\">Blah blah blah.</sdk:license>\n" +
"\n" +
            "<sdk:platform-tool>\n" +
"    <sdk:revision>\n" +
            "        <sdk:major>14</sdk:major>\n" +
"    </sdk:revision>\n" +
"    <sdk:archives>\n" +
"        <sdk:archive arch=\"any\" os=\"windows\">\n" +
"            <sdk:size>11159472</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">6028258d8f2fba14d8b40c3cf507afa0289aaa13</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r14-windows.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"        <sdk:archive arch=\"any\" os=\"linux\">\n" +
"            <sdk:size>10985068</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">6e2bc329c9485eb383172cbc2cde8b0c0cd1843f</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r14-linux.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"        <sdk:archive arch=\"any\" os=\"macosx\">\n" +
"            <sdk:size>11342461</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">4a015090c6a209fc33972acdbc65745e0b3c08b9</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r14-macosx.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"    </sdk:archives>\n" +
"</sdk:platform-tool>\n" +
//Synthetic comment -- @@ -171,7 +279,7 @@
"        <sdk:micro>3</sdk:micro>\n" +
"    </sdk:revision>\n" +
"    <sdk:min-platform-tools-rev>\n" +
            "        <sdk:major>12</sdk:major>\n" +
"    </sdk:min-platform-tools-rev>\n" +
"    <sdk:archives>\n" +
"        <sdk:archive arch=\"any\" os=\"windows\">\n" +







