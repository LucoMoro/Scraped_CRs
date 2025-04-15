/*SDK Repository: new build-tool package type.

Related changes:
sdk.git:I5b460e7e6d14536eb6a8df57c5d6f13b99ce30bftools/base.git:Idb6d745156afca44eac83c1f3d5dbecaba8d053atools/swt.git:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fbChange-Id:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fb*/




//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 3bbeb54..7da8c0d 100755

//Synthetic comment -- @@ -19,7 +19,9 @@
import com.android.SdkConstants;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.packages.BuildToolPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IFullRevisionProvider;
import com.android.sdklib.internal.repository.packages.Package;
//Synthetic comment -- @@ -94,7 +96,7 @@
int maxApi = 0;
Set<Integer> installedPlatforms = new HashSet<Integer>();
SparseArray<List<PkgItem>> platformItems = new SparseArray<List<PkgItem>>();
// TODO check we have one build-tool installed. Otherwise auto-select the highest one
// sort items in platforms... directly deal with new/update items
List<PkgItem> allItems = getAllPkgItems(true /*byApi*/, true /*bySource*/);
for (PkgItem item : allItems) {
//Synthetic comment -- @@ -148,7 +150,7 @@
Package installed = item2.getMainPackage();

if (installed.getRevision().isPreview() &&
                                    newPkg2.sameItemAs(installed, PreviewComparison.IGNORE)) {
sameFound = true;

if (installed.canBeUpdatedBy(newPkg) == UpdateInfo.UPDATE) {
//Synthetic comment -- @@ -670,7 +672,9 @@
if (pkg instanceof IAndroidVersionProvider) {
return ((IAndroidVersionProvider) pkg).getAndroidVersion();

            } else if (pkg instanceof ToolPackage ||
                    pkg instanceof PlatformToolPackage ||
                    pkg instanceof BuildToolPackage) {
if (pkg.getRevision().isPreview()) {
return PkgCategoryApi.KEY_TOOLS_PREVIEW;
} else {
//Synthetic comment -- @@ -689,7 +693,7 @@
List<PkgCategory> cats = getCategories();
for (PkgCategory cat : cats) {
if (cat.getKey().equals(PkgCategoryApi.KEY_TOOLS)) {
                    // Mark them as not unused to prevent their removal in updateEnd().
keep(cat);
needTools = false;
} else if (cat.getKey().equals(PkgCategoryApi.KEY_EXTRA)) {








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java
//Synthetic comment -- index 1f484df..d3625ba 100755

//Synthetic comment -- @@ -47,10 +47,8 @@

public final static String SDK_PATH = "/tmp/SDK";

private DownloadCache mMockDownloadCache = new MockDownloadCache();
    private final List<ArchiveReplacement> mInstalled = new ArrayList<ArchiveReplacement>();
private final SdkSources mMockSdkSources = new SdkSources() {
@Override
public void loadUserAddons(ILogger log) {
//Synthetic comment -- @@ -150,7 +148,7 @@

public static SettingsController createSettingsController(ILogger sdkLog) {
Properties props = new Properties();
        Settings settings = new Settings(props) {};   // this constructor is protected
MockSettingsController controller = new MockSettingsController(sdkLog, settings);
controller.setProperties(props);
return controller;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
//Synthetic comment -- index 1746eec..a31cbac 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.repository.MockDownloadCache;
import com.android.sdklib.internal.repository.updater.ISettingsPage;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;

//Synthetic comment -- @@ -55,12 +56,17 @@
// the fake locally-installed SDK.
String actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                      |  |            |          \n" +
                " L_[] Android SDK Tools          |  |      1.0.1 | Installed\n" +
                " L_[] Android SDK Platform-tools |  |     17.1.2 | Installed\n" +
                " L_[] Android SDK Build-tools    |  |      3.0.1 | Installed\n" +
                " L_[] Android SDK Build-tools    |  |          3 | Installed\n" +
                "[]    Tools (Preview Channel)    |  |            |          \n" +
                " L_[] Android SDK Build-tools    |  | 12.3.4 rc5 | Installed\n" +
                "[]    Android 0.0 (API 0)        |  |            |          \n" +
                " L_[] SDK Platform               |  |          1 | Installed\n" +
                " L_[] Sources for Android SDK    |  |          0 | Installed\n" +
                "[]    Extras                     |  |            |          ",
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
                 "<https://dl-ssl.google.com/android/repository/repository-8.xml : 2>, " +
                 "<https://dl-ssl.google.com/android/repository/repository.xml : 2>]",
Arrays.toString(cache.getCachedHits()));


        // Now prepare a tools update on the server and reload, with previews disabled.
setupToolsXml1(cache);
cache.clearDirectHits();
cache.clearCachedHits();
        updaterData.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, false);
pageImpl.fullReload();

actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                      |  |            |                              \n" +
                " L_[] Android SDK Tools          |  |      1.0.1 | Update available: rev. 20.0.3\n" +
                " L_[] Android SDK Platform-tools |  |     17.1.2 | Update available: rev. 18    \n" +
                " L_[] Android SDK Build-tools    |  |         18 | Not installed                \n" +
                " L_[] Android SDK Build-tools    |  |      3.0.1 | Installed                    \n" +
                " L_[] Android SDK Build-tools    |  |          3 | Installed                    \n" +
                "[]    Tools (Preview Channel)    |  |            |                              \n" +
                // Note: locally installed previews are always shown, even when enable previews is false.
                " L_[] Android SDK Build-tools    |  | 12.3.4 rc5 | Installed                    \n" +
                "[]    Android 0.0 (API 0)        |  |            |                              \n" +
                " L_[] SDK Platform               |  |          1 | Installed                    \n" +
                " L_[] Sources for Android SDK    |  |          0 | Installed                    \n" +
                "[]    Extras                     |  |            |                              ",
actual);

assertEquals(
//Synthetic comment -- @@ -98,9 +112,10 @@
Arrays.toString(cache.getDirectHits()));
assertEquals(
"[<https://dl-ssl.google.com/android/repository/repository-5.xml : 1>, " +
                 "<https://dl-ssl.google.com/android/repository/repository-6.xml : 1>, " +
                 "<https://dl-ssl.google.com/android/repository/repository-7.xml : 1>, " +
                 "<https://dl-ssl.google.com/android/repository/repository-8.xml : 1>, " +
                 "<https://dl-ssl.google.com/android/repository/repository.xml : 1>]",
Arrays.toString(cache.getCachedHits()));


//Synthetic comment -- @@ -115,13 +130,18 @@

actual = pageImpl.getMockTreeDisplay();
assertEquals(
                "[]    Tools                      |  |            |                              \n" +
                " L_[] Android SDK Tools          |  |      1.0.1 | Update available: rev. 20.0.3\n" +
                " L_[] Android SDK Platform-tools |  |     17.1.2 | Update available: rev. 18    \n" +
                " L_[] Android SDK Build-tools    |  |         18 | Not installed                \n" +
                " L_[] Android SDK Build-tools    |  |      3.0.1 | Installed                    \n" +
                " L_[] Android SDK Build-tools    |  |          3 | Installed                    \n" +
                "[]    Tools (Preview Channel)    |  |            |                              \n" +
                " L_[] Android SDK Build-tools    |  | 12.3.4 rc5 | Installed                    \n" +
                "[]    Android 0.0 (API 0)        |  |            |                              \n" +
                " L_[] SDK Platform               |  |          1 | Installed                    \n" +
                " L_[] Sources for Android SDK    |  |          0 | Installed                    \n" +
                "[]    Extras                     |  |            |                              ",
actual);

assertEquals(
//Synthetic comment -- @@ -131,6 +151,44 @@
"[<https://dl-ssl.google.com/android/repository/repository-5.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository-6.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository-7.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-8.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository.xml : 1>]",
                Arrays.toString(cache.getCachedHits()));


        // Now simulate a reload but this time enable previews.

        cache.clearDirectHits();
        cache.clearCachedHits();
        pageImpl = new MockPackagesPageImpl(updaterData);
        pageImpl.postCreate();
        updaterData.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);
        pageImpl.performFirstLoad();

        actual = pageImpl.getMockTreeDisplay();
        assertEquals(
                "[]    Tools                      |  |            |                                   \n" +
                " L_[] Android SDK Tools          |  |      1.0.1 | Update available: rev. 20.0.3     \n" +
                " L_[] Android SDK Platform-tools |  |     17.1.2 | Update available: rev. 18         \n" +
                " L_[] Android SDK Build-tools    |  |         18 | Not installed                     \n" +
                " L_[] Android SDK Build-tools    |  |      3.0.1 | Installed                         \n" +
                " L_[] Android SDK Build-tools    |  |          3 | Installed                         \n" +
                "[]    Tools (Preview Channel)    |  |            |                                   \n" +
                " L_[] Android SDK Build-tools    |  | 12.3.4 rc5 | Update available: rev. 12.3.4 rc15\n" +
                "[]    Android 0.0 (API 0)        |  |            |                                   \n" +
                " L_[] SDK Platform               |  |          1 | Installed                         \n" +
                " L_[] Sources for Android SDK    |  |          0 | Installed                         \n" +
                "[]    Extras                     |  |            |                                   ",
                actual);

        assertEquals(
                "[]",  // there are no direct downloads till we try to install.
                Arrays.toString(cache.getDirectHits()));
        assertEquals(
                "[<https://dl-ssl.google.com/android/repository/repository-5.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-6.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-7.xml : 1>, " +
                "<https://dl-ssl.google.com/android/repository/repository-8.xml : 1>, " +
"<https://dl-ssl.google.com/android/repository/repository.xml : 1>]",
Arrays.toString(cache.getCachedHits()));
}
//Synthetic comment -- @@ -138,28 +196,78 @@
private void setupToolsXml1(MockDownloadCache cache) throws Exception {
String repoXml =
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<sdk:sdk-repository xmlns:sdk=\"http://schemas.android.com/sdk/android/repository/8\" " +
            "                    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"<sdk:license id=\"android-sdk-license\" type=\"text\">Blah blah blah.</sdk:license>\n" +
"\n" +
            "<sdk:build-tool>\n" +
"    <sdk:revision>\n" +
            "        <sdk:major>18</sdk:major>\n" +
"    </sdk:revision>\n" +
"    <sdk:archives>\n" +
"        <sdk:archive arch=\"any\" os=\"windows\">\n" +
"            <sdk:size>11159472</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">6028258d8f2fba14d8b40c3cf507afa0289aaa13</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-windows.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"        <sdk:archive arch=\"any\" os=\"linux\">\n" +
"            <sdk:size>10985068</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">6e2bc329c9485eb383172cbc2cde8b0c0cd1843f</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-linux.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"        <sdk:archive arch=\"any\" os=\"macosx\">\n" +
"            <sdk:size>11342461</sdk:size>\n" +
"            <sdk:checksum type=\"sha1\">4a015090c6a209fc33972acdbc65745e0b3c08b9</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-macosx.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "    </sdk:archives>\n" +
            "</sdk:build-tool>\n" +
            "\n" +
            "<sdk:build-tool>\n" +
            "    <sdk:revision>\n" +
            "        <sdk:major>12</sdk:major>\n" +
            "        <sdk:minor>3</sdk:minor>\n" +
            "        <sdk:micro>4</sdk:micro>\n" +
            "        <sdk:preview>15</sdk:preview>\n" +
            "    </sdk:revision>\n" +
            "    <sdk:archives>\n" +
            "        <sdk:archive arch=\"any\" os=\"windows\">\n" +
            "            <sdk:size>11159472</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">6028258d8f2fba14d8b40c3cf507afa0289aaa13</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-windows.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "        <sdk:archive arch=\"any\" os=\"linux\">\n" +
            "            <sdk:size>10985068</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">6e2bc329c9485eb383172cbc2cde8b0c0cd1843f</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-linux.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "        <sdk:archive arch=\"any\" os=\"macosx\">\n" +
            "            <sdk:size>11342461</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">4a015090c6a209fc33972acdbc65745e0b3c08b9</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-macosx.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "    </sdk:archives>\n" +
            "</sdk:build-tool>\n" +
            "\n" +
            "<sdk:platform-tool>\n" +
            "    <sdk:revision>\n" +
            "        <sdk:major>18</sdk:major>\n" +
            "    </sdk:revision>\n" +
            "    <sdk:archives>\n" +
            "        <sdk:archive arch=\"any\" os=\"windows\">\n" +
            "            <sdk:size>11159472</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">6028258d8f2fba14d8b40c3cf507afa0289aaa13</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-windows.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "        <sdk:archive arch=\"any\" os=\"linux\">\n" +
            "            <sdk:size>10985068</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">6e2bc329c9485eb383172cbc2cde8b0c0cd1843f</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-linux.zip</sdk:url>\n" +
            "        </sdk:archive>\n" +
            "        <sdk:archive arch=\"any\" os=\"macosx\">\n" +
            "            <sdk:size>11342461</sdk:size>\n" +
            "            <sdk:checksum type=\"sha1\">4a015090c6a209fc33972acdbc65745e0b3c08b9</sdk:checksum>\n" +
            "            <sdk:url>platform-tools_r18-macosx.zip</sdk:url>\n" +
"        </sdk:archive>\n" +
"    </sdk:archives>\n" +
"</sdk:platform-tool>\n" +
//Synthetic comment -- @@ -171,7 +279,7 @@
"        <sdk:micro>3</sdk:micro>\n" +
"    </sdk:revision>\n" +
"    <sdk:min-platform-tools-rev>\n" +
            "        <sdk:major>18</sdk:major>\n" +
"    </sdk:min-platform-tools-rev>\n" +
"    <sdk:archives>\n" +
"        <sdk:archive arch=\"any\" os=\"windows\">\n" +







