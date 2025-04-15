/*Setting to enable preview packages.

Change-Id:I7af78e81e5433396c7a1039782ab8ceda35187a0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index a14337d..6d15360 100755

//Synthetic comment -- @@ -67,6 +67,13 @@
public static final String KEY_USE_DOWNLOAD_CACHE = "sdkman.use.dl.cache";   //$NON-NLS-1$

/**
* Setting to set the density of the monitor.
* Type: Integer.
* Default: -1








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 8ccb688..f36e749 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;
//Synthetic comment -- @@ -42,7 +43,7 @@
private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

private final ISdkLog mSdkLog;
    private final Settings mSettings = new Settings();

public interface OnChangedListener {
public void onSettingsChanged(SettingsController controller, Settings oldSettings);
//Synthetic comment -- @@ -52,9 +53,27 @@
/** The currently associated {@link ISettingsPage}. Can be null. */
private ISettingsPage mSettingsPage;


    public SettingsController(ISdkLog sdkLog) {
mSdkLog = sdkLog;
}

public Settings getSettings() {
//Synthetic comment -- @@ -77,20 +96,31 @@


public static class Settings {
        private final Properties mProperties = new Properties();

/** Initialize an empty set of settings. */
public Settings() {
}

/** Duplicates a set of settings. */
public Settings(Settings settings) {
for (Entry<Object, Object> entry : settings.mProperties.entrySet()) {
mProperties.put(entry.getKey(), entry.getValue());
}
}

/**
* Returns the value of the {@link ISettingsPage#KEY_FORCE_HTTP} setting.
*
* @see ISettingsPage#KEY_FORCE_HTTP
//Synthetic comment -- @@ -133,6 +163,15 @@
}

/**
* Returns the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting
* @see ISettingsPage#KEY_MONITOR_DENSITY
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index af7cc6ea..2594961 100755

//Synthetic comment -- @@ -47,9 +47,11 @@
// UI widgets
private Text mTextProxyServer;
private Text mTextProxyPort;
private Button mCheckUseCache;
private Button mCheckForceHttp;
private Button mCheckAskAdbRestart;

private SelectionAdapter mApplyOnSelected = new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -64,7 +66,6 @@
applyNewSettings(); //$hide$
}
};
    private Text mTextCacheSize;

public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
super(parentShell, updaterData, "Settings" /*title*/);
//Synthetic comment -- @@ -85,7 +86,7 @@
Label label = new Label(group, SWT.NONE);
GridDataBuilder.create(label).hRight().vCenter();
label.setText("HTTP Proxy Server");
        String tooltip = "The hostname or IP of the HTTP & HTTPS proxy server to use (e.g. proxy.example.com). " +
"When empty, the default Java proxy setting is used.";
label.setToolTipText(tooltip);

//Synthetic comment -- @@ -97,7 +98,7 @@
label = new Label(group, SWT.NONE);
GridDataBuilder.create(label).hRight().vCenter();
label.setText("HTTP Proxy Port");
        tooltip = "The port of the HTTP & HTTPS proxy server to use (e.g. 3128). " +
"When empty, the default Java proxy setting is used.";
label.setToolTipText(tooltip);

//Synthetic comment -- @@ -133,8 +134,9 @@
mCheckUseCache = new Button(group, SWT.CHECK);
GridDataBuilder.create(mCheckUseCache).vCenter().hSpan(1);
mCheckUseCache.setText("Use download cache");
        mCheckUseCache.setToolTipText("When checked, small manifest files are cached locally. " +
                "Large binary files are never cached locally.");
mCheckUseCache.addSelectionListener(mApplyOnSelected);

label = new Label(group, SWT.NONE);
//Synthetic comment -- @@ -154,7 +156,7 @@

// ----
group = new Group(shell, SWT.NONE);
        group.setText("Misc");
GridDataBuilder.create(group).fill().grab().hSpan(2);
GridLayoutBuilder.create(group).columns(2);

//Synthetic comment -- @@ -162,18 +164,27 @@
GridDataBuilder.create(mCheckForceHttp).hFill().hGrab().vCenter().hSpan(2);
mCheckForceHttp.setText("Force https://... sources to be fetched using http://...");
mCheckForceHttp.setToolTipText(
                "If you are not able to connect to the official Android repository " +
                "using HTTPS, enable this setting to force accessing it via HTTP.");
mCheckForceHttp.addSelectionListener(mApplyOnSelected);

mCheckAskAdbRestart = new Button(group, SWT.CHECK);
GridDataBuilder.create(mCheckAskAdbRestart).hFill().hGrab().vCenter().hSpan(2);
mCheckAskAdbRestart.setText("Ask before restarting ADB");
mCheckAskAdbRestart.setToolTipText(
                "When checked, the user will be asked for permission " +
                "to restart ADB after updating an addon-on package or a tool package.");
mCheckAskAdbRestart.addSelectionListener(mApplyOnSelected);

Label filler = new Label(shell, SWT.NONE);
GridDataBuilder.create(filler).hFill().hGrab();

//Synthetic comment -- @@ -210,6 +221,9 @@
Boolean.parseBoolean(inSettings.getProperty(KEY_ASK_ADB_RESTART)));
mCheckUseCache.setSelection(
Boolean.parseBoolean(inSettings.getProperty(KEY_USE_DOWNLOAD_CACHE)));
}

/** Called by the application to retrieve settings from the UI and store them in
//Synthetic comment -- @@ -224,6 +238,9 @@
Boolean.toString(mCheckAskAdbRestart.getSelection()));
outSettings.setProperty(KEY_USE_DOWNLOAD_CACHE,
Boolean.toString(mCheckUseCache.getSelection()));
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index e64075a..cdf5e7b 100755

//Synthetic comment -- @@ -114,33 +114,9 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;

        mSettingsController = new SettingsController(sdkLog);

initSdk();

        mSettingsController.registerOnChangedListener(new OnChangedListener() {
            @Override
            public void onSettingsChanged(
                    SettingsController controller,
                    SettingsController.Settings oldSettings) {

                // Reset the download cache if it doesn't match the right strategy.
                // The cache instance gets lazily recreated later in getDownloadCache().
                if (mDownloadCache != null) {
                    if (controller.getSettings().getUseDownloadCache() &&
                            mDownloadCache.getStrategy() != DownloadCache.Strategy.FRESH_CACHE) {
                        mDownloadCache = null;
                    } else if (!controller.getSettings().getUseDownloadCache() &&
                            mDownloadCache.getStrategy() != DownloadCache.Strategy.DIRECT) {
                        mDownloadCache = null;
                    }
                }

                if (oldSettings.getForceHttp() != controller.getSettings().getForceHttp()) {
                    refreshSources(false /*forceFetching*/);
                }
            }
        });
}

// ----- getters, setters ----
//Synthetic comment -- @@ -276,6 +252,7 @@

/**
* Initializes the {@link SdkManager} and the {@link AvdManager}.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
protected void initSdk() {
//Synthetic comment -- @@ -299,6 +276,35 @@
broadcastOnSdkReload();
}

@VisibleForTesting(visibility=Visibility.PRIVATE)
protected void setSdkManager(SdkManager sdkManager) {
mSdkManager = sdkManager;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 9e45748..0bda937 100755

//Synthetic comment -- @@ -522,7 +522,16 @@
boolean hasChanged = false;
List<PkgCategory> cats = op.getCategories();

nextPkg: for (Package newPkg : packages) {
for (PkgCategory cat : cats) {
for (PkgState state : PKG_STATES) {
for (Iterator<PkgItem> currItemIt = cat.getItems().iterator();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index 40a3538..109c061 100755

//Synthetic comment -- @@ -780,15 +780,29 @@
return;
}
if (mTreeViewer != null && !mTreeViewer.getTree().isDisposed()) {
mTreeViewer.setExpandedState(elem, true);
            for (Object pkg :
((ITreeContentProvider) mTreeViewer.getContentProvider()).getChildren(elem)) {
if (pkg instanceof PkgCategory) {
PkgCategory cat = (PkgCategory) pkg;
for (PkgItem item : cat.getItems()) {
if (item.getState() == PkgState.INSTALLED) {
expandInitial(pkg);
                            break;
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PkgCategoryApi.java
//Synthetic comment -- index d1e3a28..dbb5d31 100755

//Synthetic comment -- @@ -75,7 +75,7 @@
if (key.equals(KEY_TOOLS)) {
label = "Tools";
} else if (key.equals(KEY_TOOLS_PREVIEW)) {
                label = "Tools (Beta Channel)";
} else if (key.equals(KEY_EXTRA)) {
label = "Extras";
} else {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 7a7a5b3..4376ec8 100755

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -188,7 +189,6 @@
}

private void createContents() {

mPkgPage = new PackagesPage(mShell, SWT.NONE, mUpdaterData, mContext);
mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

//Synthetic comment -- @@ -325,8 +325,23 @@
new MenuBarWrapper(APP_NAME, menuTools) {
@Override
public void onPreferencesMenuSelected() {
SettingsDialog sd = new SettingsDialog(mShell, mUpdaterData);
sd.open();
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index 2260739..0fc5b55 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
//Synthetic comment -- @@ -27,6 +28,7 @@
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -34,6 +36,7 @@
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** A mock UpdaterData that simply records what would have been installed. */
public class MockUpdaterData extends UpdaterData {
//Synthetic comment -- @@ -60,11 +63,18 @@
return mInstalled.toArray(new ArchiveReplacement[mInstalled.size()]);
}

@Override
protected void initSdk() {
setSdkManager(new MockEmptySdkManager(SDK_PATH));
}

@Override
public void reloadSdk() {
// bypass original implementation
//Synthetic comment -- @@ -107,6 +117,48 @@
return mMockDownloadCache;
}

//------------

private class MockTaskFactory implements ITaskFactory {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index 26b39ed..57573e1 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.MockUpdaterData;

import java.util.Properties;
//Synthetic comment -- @@ -1455,10 +1456,42 @@
getTree(m, false /*displaySortByApi*/));
}

public void testToolsPreviews() {
// Test: No local tools installed. The remote server has both tools and platforms
// in release and RC versions.

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();
//Synthetic comment -- @@ -1494,6 +1527,9 @@
// => v3 is updated by 3.0.1
// => v4.0.0rc1 does not update 3.0.0, instead it's a separate download.

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();
//Synthetic comment -- @@ -1568,6 +1604,9 @@
// => Installed 3.0.1rc1 can be updated by 3.0.1rc2
// => There's a separate "new" download for 3.0.0, not installed and NOT updating 3.0.1rc1.

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();







