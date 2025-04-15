/*SDK Repository: new build-tool package type.

Related changes:
tools/base.git:Idb6d745156afca44eac83c1f3d5dbecaba8d053atools/swt.git:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fbChange-Id:Ia0cfa6e9dd1ddb8f92021c995f22e68dec6eb3fb*/




//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 3bbeb54..e28f827 100755

//Synthetic comment -- @@ -19,7 +19,10 @@
import com.android.SdkConstants;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.packages.BuildToolPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.FullRevision.PreviewComparison;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IFullRevisionProvider;
import com.android.sdklib.internal.repository.packages.Package;
//Synthetic comment -- @@ -34,6 +37,7 @@
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;
import com.android.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -95,6 +99,11 @@
Set<Integer> installedPlatforms = new HashSet<Integer>();
SparseArray<List<PkgItem>> platformItems = new SparseArray<List<PkgItem>>();

        boolean hasTools = false;
        Map<Class<?>, Pair<PkgItem, FullRevision>> toolsCandidates = new HashMap<Class<?>, Pair<PkgItem,FullRevision>>();
        toolsCandidates.put(PlatformToolPackage.class, Pair.of((PkgItem)null, (FullRevision)null));
        toolsCandidates.put(BuildToolPackage.class,    Pair.of((PkgItem)null, (FullRevision)null));

// sort items in platforms... directly deal with new/update items
List<PkgItem> allItems = getAllPkgItems(true /*byApi*/, true /*bySource*/);
for (PkgItem item : allItems) {
//Synthetic comment -- @@ -148,7 +157,7 @@
Package installed = item2.getMainPackage();

if (installed.getRevision().isPreview() &&
                                    newPkg2.sameItemAs(installed, PreviewComparison.IGNORE)) {
sameFound = true;

if (installed.canBeUpdatedBy(newPkg) == UpdateInfo.UPDATE) {
//Synthetic comment -- @@ -167,8 +176,50 @@
} else if (selectUpdates && item.hasUpdatePkg()) {
item.setChecked(true);
}

            // Keep track of the tools and offer to auto-select platform-tools/build-tools.
            if (selectTop) {
                if (p instanceof ToolPackage && p.isLocal()) {
                    hasTools = true; // main tool package is installed.
                } else if (p instanceof PlatformToolPackage || p instanceof BuildToolPackage) {
                    for (Class<?> clazz : toolsCandidates.keySet()) {
                        if (clazz.isInstance(p)) { // allow p to be a mock-derived class
                            if (p.isLocal()) {
                                // There's one such package installed, we don't need candidates.
                                toolsCandidates.remove(clazz);
                            } else if (toolsCandidates.containsKey(clazz)) {
                                Pair<PkgItem, FullRevision> val = toolsCandidates.get(clazz);
                                FullRevision rev = p.getRevision();
                                if (!rev.isPreview()) {
                                    // Don't auto-select previews.
                                    if (val.getSecond() == null ||
                                            rev.compareTo(val.getSecond()) > 0) {
                                        // No revision: set the first candidate.
                                        // Or we found a new higher revision
                                        toolsCandidates.put(clazz, Pair.of(item, rev));
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
}

        // Select the top platform/build-tool found above if needed.
        if (selectTop && hasTools) {
            for (Pair<PkgItem, FullRevision> candidate : toolsCandidates.values()) {
                PkgItem item = candidate.getFirst();
                if (item != null) {
                    item.setChecked(true);
                }
            }
        }


        // Select top platform items.

List<PkgItem> items = platformItems.get(maxApi);
if (selectTop && maxApi > 0 && items != null) {
if (!installedPlatforms.contains(maxApi)) {
//Synthetic comment -- @@ -670,7 +721,9 @@
if (pkg instanceof IAndroidVersionProvider) {
return ((IAndroidVersionProvider) pkg).getAndroidVersion();

            } else if (pkg instanceof ToolPackage ||
                    pkg instanceof PlatformToolPackage ||
                    pkg instanceof BuildToolPackage) {
if (pkg.getRevision().isPreview()) {
return PkgCategoryApi.KEY_TOOLS_PREVIEW;
} else {
//Synthetic comment -- @@ -689,7 +742,7 @@
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








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java
//Synthetic comment -- index cc8a1b9..6f868cf 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.MockAddonPackage;
import com.android.sdklib.internal.repository.packages.MockBrokenPackage;
import com.android.sdklib.internal.repository.packages.MockBuildToolPackage;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
import com.android.sdklib.internal.repository.packages.MockExtraPackage;
import com.android.sdklib.internal.repository.packages.MockPlatformPackage;
//Synthetic comment -- @@ -34,8 +35,6 @@
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;

import java.util.Properties;

//Synthetic comment -- @@ -1786,6 +1785,141 @@
getTree(m, false /*displaySortByApi*/));
}

    public void testBuildTool_New() {
        // Test: No local packages installed. Remote server has tools, platform-tools and
        // build-tools. Even though build-tools isn't a dependency we want to auto-select
        // the latest one as an install candidate.

        // Enable previews in the settings
        u.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage        (src1, new FullRevision(2, 0, 0), 3),  // Tools 2
                new MockPlatformToolPackage(src1, new FullRevision(3, 0, 0)),     // Plat-T 3
                new MockBuildToolPackage   (src1, new FullRevision(4, 0, 0)),     // Build-T 3
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=3>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=3>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Build-tools, revision 4>\n",
                getTree(m, false /*displaySortByApi*/));

        // Auto select top items. This doesn't selected build-tools since no tools are installed.
        m.checkNewUpdateItems(false, false, true, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=3>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- <NEW, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));

        // Auto select new items. This obviously selects the build-tools since its new.
        m.checkNewUpdateItems(true, false, false, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=3>\n" +
                "-- < * NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- < * NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- < * NEW, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
    }

    public void testBuildTool_InitialTop() {
        // Test Build tools auto-selected as an initial top package.
        // This time we have the tool package installed.
        // When we first start and select the top packages, we should also auto-select
        // the latest platform-tools and build-tools if none are installed.

        // Enable previews in the settings
        u.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        // First the local install only has tools, no plat-tools or build-tools.

        m.uncheckAllItems();
        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage        (null, new FullRevision(2, 0, 0), 3),  // Tools 2
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage        (src1, new FullRevision(2, 1, 0), 3),  // Tools 2.1
                new MockPlatformToolPackage(src1, new FullRevision(3, 0, 0)),     // Plat-T 3.1
                new MockBuildToolPackage   (src1, new FullRevision(4, 0, 0)),     // Build-T 4.1
        });
        m.updateEnd(true /*sortByApi*/);

        // Auto select top items.
        m.checkNewUpdateItems(false, false, true, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=3>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 2, updated by:Android SDK Tools, revision 2.1>\n" +
                "-- < * NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "-- < * NEW, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));

        // Next we start again but this time the local install as all 3 tools.
        // Auto-selecting the top shouldn't select the updated packages available.

        m.uncheckAllItems();
        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, null /*locals*/, new Package[] {
                new MockToolPackage        (null, new FullRevision(2, 0, 0), 3),  // Tools 2
                new MockPlatformToolPackage(null, new FullRevision(3, 0, 0)),     // Plat-T 3
                new MockBuildToolPackage   (null, new FullRevision(4, 0, 0)),     // Build-T 4
        });
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage        (src1, new FullRevision(2, 1, 0), 3),  // Tools 2.1
                new MockPlatformToolPackage(src1, new FullRevision(3, 1, 0)),     // Plat-T 3.1
                new MockBuildToolPackage   (src1, new FullRevision(4, 1, 0)),     // Build-T 4.1
        });
        m.updateEnd(true /*sortByApi*/);

        // Auto select top items.
        m.checkNewUpdateItems(false, false, true, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=4>\n" +
                "-- <INSTALLED, pkg:Android SDK Tools, revision 2, updated by:Android SDK Tools, revision 2.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.1>\n" +
                "-- <NEW, pkg:Android SDK Build-tools, revision 4.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));

        // If we do request updates + top, they are selected however except for build-tools
        // since new versions are not considered as updates.
        m.uncheckAllItems();
        m.checkNewUpdateItems(false, true, true, SdkConstants.PLATFORM_LINUX);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=4>\n" +
                "-- < * INSTALLED, pkg:Android SDK Tools, revision 2, updated by:Android SDK Tools, revision 2.1>\n" +
                "-- < * INSTALLED, pkg:Android SDK Platform-tools, revision 3, updated by:Android SDK Platform-tools, revision 3.1>\n" +
                "-- <NEW, pkg:Android SDK Build-tools, revision 4.1>\n" +
                "-- <INSTALLED, pkg:Android SDK Build-tools, revision 4>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
    }



// ----








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







