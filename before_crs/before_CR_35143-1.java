/*SDK: primitive implementation of download cache.

WIP -- Not ready for review.

Change-Id:I515e77291fb6810453e82e73f6508cfc60b2f422*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index ebdc9c8..6e6c657 100644

//Synthetic comment -- @@ -260,6 +260,9 @@
/** Name of the addon libs folder. */
public final static String FD_ADDON_LIBS = "libs";                  //$NON-NLS-1$

/** Namespace for the resource XML, i.e. "http://schemas.android.com/apk/res/android" */
public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index 919a30e..e3215b5 100755

//Synthetic comment -- @@ -29,7 +29,6 @@
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -82,13 +81,13 @@
/**
* Fetches the addons list from the given URL.
*
     * @param monitor A monitor to report errors. Cannot be null.
* @param url The URL of an XML file resource that conforms to the latest sdk-addons-list-N.xsd.
*   For the default operation, use {@link SdkAddonsListConstants#URL_ADDON_LIST}.
*   Cannot be null.
* @return An array of {@link Site} on success (possibly empty), or null on error.
*/
    public Site[] fetch(ITaskMonitor monitor, String url) {

url = url == null ? "" : url.trim();

//Synthetic comment -- @@ -102,7 +101,7 @@
Document validatedDoc = null;
String validatedUri = null;

        ByteArrayInputStream xml = fetchUrl(url, monitor.createSubMonitor(1), exception);

if (xml != null) {
monitor.setDescription("Validate XML");
//Synthetic comment -- @@ -187,41 +186,12 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private ByteArrayInputStream fetchUrl(String urlString, ITaskMonitor monitor,
Exception[] outException) {
try {

            InputStream is = null;

            int inc = 65536;
            int curr = 0;
            byte[] result = new byte[inc];

            try {
                is = UrlOpener.openUrl(urlString, monitor);

                int n;
                while ((n = is.read(result, curr, result.length - curr)) != -1) {
                    curr += n;
                    if (curr == result.length) {
                        byte[] temp = new byte[curr + inc];
                        System.arraycopy(result, 0, temp, 0, curr);
                        result = temp;
                    }
                }

                return new ByteArrayInputStream(result, 0, curr);

            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // pass
                    }
                }
            }

} catch (Exception e) {
if (outException != null) {
outException[0] = e;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ArchiveInstaller.java
//Synthetic comment -- index 2e2396f..3d50dd6 100755

//Synthetic comment -- @@ -96,6 +96,7 @@
String osSdkRoot,
boolean forceHttp,
SdkManager sdkManager,
ITaskMonitor monitor) {

Archive newArchive = archiveInfo.getNewArchive();
//Synthetic comment -- @@ -122,7 +123,7 @@
return false;
}

        archiveFile = downloadFile(newArchive, osSdkRoot, monitor, forceHttp);
if (archiveFile != null) {
// Unarchive calls the pre/postInstallHook methods.
if (unarchive(archiveInfo, osSdkRoot, archiveFile, sdkManager, monitor)) {
//Synthetic comment -- @@ -143,6 +144,7 @@
@VisibleForTesting(visibility=Visibility.PRIVATE)
protected File downloadFile(Archive archive,
String osSdkRoot,
ITaskMonitor monitor,
boolean forceHttp) {

//Synthetic comment -- @@ -219,7 +221,7 @@
mFileOp.deleteFileOrFolder(tmpFile);
}

        if (fetchUrl(archive, tmpFile, link, pkgName, monitor)) {
// Fetching was successful, let's use this file.
return tmpFile;
} else {
//Synthetic comment -- @@ -303,12 +305,13 @@
File tmpFile,
String urlString,
String pkgName,
ITaskMonitor monitor) {

FileOutputStream os = null;
InputStream is = null;
try {
            is = UrlOpener.openUrl(urlString, monitor);
os = new FileOutputStream(tmpFile);

MessageDigest digester = archive.getChecksumType().getMessageDigest();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
new file mode 100755
//Synthetic comment -- index 0000000..980c468

//Synthetic comment -- @@ -0,0 +1,227 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 38417f7..cc5e631 100755

//Synthetic comment -- @@ -32,7 +32,6 @@
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -192,7 +191,7 @@
/**
* Returns the list of known packages found by the last call to load().
* This is null when the source hasn't been loaded yet -- caller should
     * then call {@link #load(ITaskMonitor, boolean)} to load the packages.
*/
public Package[] getPackages() {
return mPackages;
//Synthetic comment -- @@ -306,7 +305,7 @@
* null in case of error, in which case {@link #getFetchError()} can be used to an
* error message.
*/
    public void load(ITaskMonitor monitor, boolean forceHttp) {

setDefaultDescription();
monitor.setProgressMax(7);
//Synthetic comment -- @@ -338,7 +337,7 @@
String[] defaultNames = getDefaultXmlFileUrls();
String firstDefaultName = defaultNames.length > 0 ? defaultNames[0] : "";

        InputStream xml = fetchUrl(url, monitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
//Synthetic comment -- @@ -365,7 +364,7 @@
if (newUrl.equals(url)) {
continue;
}
                xml = fetchUrl(newUrl, subMonitor.createSubMonitor(1), exception);
if (xml != null) {
int version = getXmlSchemaVersion(xml);
if (version == 0) {
//Synthetic comment -- @@ -394,7 +393,7 @@
}
url += firstDefaultName;

            xml = fetchUrl(url, monitor.createSubMonitor(1), exception);
usingAlternateUrl = true;
} else {
monitor.incProgress(1);
//Synthetic comment -- @@ -467,7 +466,8 @@
}
url += firstDefaultName;

                        xml = fetchUrl(url, subMonitor.createSubMonitor(1), null /* outException */);
subMonitor.incProgress(1);
// Loop to try the alternative document
if (xml != null) {
//Synthetic comment -- @@ -596,40 +596,12 @@
*            happens during the fetch.
* @see UrlOpener UrlOpener, which handles all URL logic.
*/
    private InputStream fetchUrl(String urlString, ITaskMonitor monitor, Exception[] outException) {
try {

            InputStream is = null;

            int inc = 65536;
            int curr = 0;
            byte[] result = new byte[inc];

            try {
                is = UrlOpener.openUrl(urlString, monitor);

                int n;
                while ((n = is.read(result, curr, result.length - curr)) != -1) {
                    curr += n;
                    if (curr == result.length) {
                        byte[] temp = new byte[curr + inc];
                        System.arraycopy(result, 0, temp, 0, curr);
                        result = temp;
                    }
                }

                return new ByteArrayInputStream(result, 0, curr);

            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // pass
                    }
                }
            }

} catch (Exception e) {
if (outException != null) {
outException[0] = e;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveInstallerTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/ArchiveInstallerTest.java
//Synthetic comment -- index 46390b5..8f42f8a 100755

//Synthetic comment -- @@ -58,6 +58,7 @@
protected File downloadFile(
Archive archive,
String osSdkRoot,
ITaskMonitor monitor,
boolean forceHttp) {
File file = mDownloadMap.get(archive);
//Synthetic comment -- @@ -102,7 +103,8 @@
MockEmptyPackage p = new MockEmptyPackage("testPkg");
ArchiveReplacement ar = new ArchiveReplacement(p.getArchives()[0], null /*replaced*/);

        assertFalse(mArchInst.install(ar, mSdkRoot, false /*forceHttp*/, mSdkMan, mMon));
assertTrue(mMon.getCapturedLog().indexOf("Skipping already installed archive") != -1);
}

//Synthetic comment -- @@ -116,7 +118,8 @@
mArchInst.setDownloadResponse(
p.getArchives()[0], createFile("/sdk", "tmp", "download1.zip"));

        assertTrue(mArchInst.install(ar, mSdkRoot, false /*forceHttp*/, mSdkMan, mMon));

// check what was created
assertEquals(
//Synthetic comment -- @@ -162,7 +165,8 @@
mArchInst.setDownloadResponse(
newPkg.getArchives()[0], createFile("/sdk", "tmp", "download1.zip"));

        assertTrue(mArchInst.install(ar, mSdkRoot, false /*forceHttp*/, mSdkMan, mMon));

// check what was created
assertEquals(
//Synthetic comment -- @@ -237,7 +241,8 @@
mArchInst.setDownloadResponse(
newPkg.getArchives()[0], createFile("/sdk", "tmp", "download1.zip"));

        assertTrue(mArchInst.install(ar, mSdkRoot, false /*forceHttp*/, mSdkMan, mMon));

// check what was created
assertEquals(








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterData.java
//Synthetic comment -- index 8ec6596..26c52d5 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -35,6 +36,8 @@

public abstract ISdkLog getSdkLog();

public abstract ImageFactory getImageFactory();

public abstract SdkManager getSdkManager();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index 4f35b26..0d9b10a 100755

//Synthetic comment -- @@ -1276,7 +1276,7 @@
Package[] pkgs = remoteSrc.getPackages();

if (pkgs == null) {
                        remoteSrc.load(monitor, forceHttp);
pkgs = remoteSrc.getPackages();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index dd51a59..0a3918b 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ArchiveInstaller;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -83,18 +84,13 @@

private SdkManager mSdkManager;
private AvdManager mAvdManager;

private final LocalSdkParser mLocalSdkParser = new LocalSdkParser();
private final SdkSources mSources = new SdkSources();

private ImageFactory mImageFactory;

private final SettingsController mSettingsController;

private final ArrayList<ISdkChangeListener> mListeners = new ArrayList<ISdkChangeListener>();

private Shell mWindowShell;

private AndroidLocationException mAvdManagerInitError;

/**
//Synthetic comment -- @@ -115,6 +111,7 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;

mSettingsController = new SettingsController(this);

initSdk();
//Synthetic comment -- @@ -126,6 +123,14 @@
return mOsSdkRoot;
}

public void setTaskFactory(ITaskFactory taskFactory) {
mTaskFactory = taskFactory;
}
//Synthetic comment -- @@ -441,6 +446,7 @@
mOsSdkRoot,
forceHttp,
mSdkManager,
monitor)) {
// We installed this archive.
newlyInstalledArchives.add(archive);
//Synthetic comment -- @@ -1003,7 +1009,7 @@
if (forceFetching ||
source.getPackages() != null ||
source.getFetchError() != null) {
                        source.load(monitor.createSubMonitor(1), forceHttp);
}
monitor.incProgress(1);
}
//Synthetic comment -- @@ -1053,7 +1059,7 @@
boolean fetch3rdParties = System.getenv("SDK_SKIP_3RD_PARTIES") == null;

AddonsListFetcher fetcher = new AddonsListFetcher();
        Site[] sites = fetcher.fetch(monitor, url);
if (sites != null) {
mSources.removeAll(SdkSourceCategory.ADDONS_3RD_PARTY);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index af7ce2c..5dd3cca 100755

//Synthetic comment -- @@ -177,7 +177,9 @@
for (SdkSource source : sources) {
Package[] pkgs = source.getPackages();
if (pkgs == null) {
                                    source.load(subMonitor.createSubMonitor(1), forceHttp);
pkgs = source.getPackages();
}
if (pkgs == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index fff0814..5a057d2 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ArchiveInstaller;
import com.android.sdklib.internal.repository.ArchiveReplacement;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -30,6 +31,7 @@

import org.eclipse.swt.graphics.Image;

import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -40,6 +42,8 @@

private final List<ArchiveReplacement> mInstalled = new ArrayList<ArchiveReplacement>();

public MockUpdaterData() {
super(SDK_PATH, new MockLog());

//Synthetic comment -- @@ -76,6 +80,7 @@
String osSdkRoot,
boolean forceHttp,
SdkManager sdkManager,
ITaskMonitor monitor) {
mInstalled.add(archiveInfo);
return true;
//Synthetic comment -- @@ -83,6 +88,25 @@
};
}

//------------

private class MockTaskFactory implements ITaskFactory {
//Synthetic comment -- @@ -91,6 +115,7 @@
start(title, null /*parentMonitor*/, task);
}

@Override
public void start(String title, ITaskMonitor parentMonitor, ITask task) {
new MockTask(task);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 5d735e3..245ca84 100755

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -60,6 +61,11 @@
}

@Override
public SdkManager getSdkManager() {
return null;
}







