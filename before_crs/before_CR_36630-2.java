/*SDK Manager: Settings to control download cache.

SDK Manager > Tools > Options now has a checkbox to
deactivate the download cache. It also indicates the
path to the cache directory, its size and a button
to clear the cache immediately.

This only affects the sdk manager files for the download
cache, it doesn't clear other cached files such as the
lint binaries.

Also restructured the settings controller a tiny bit.
There's more that could be restructured here but it's
not a priority right now.

Change-Id:I474e6155bdc041770f3f7664366d0d92bd96d9b0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 591e447..8d7b47f 100755

//Synthetic comment -- @@ -175,6 +175,56 @@
return mStrategy;
}

/**
* Returns the directory to be used as a cache.
* Creates it if necessary.
//Synthetic comment -- @@ -439,6 +489,8 @@
null /*headers*/, null /*statusCode*/);
}

// --------------

private InputStream readCachedFile(File cached) throws IOException {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/util/ConvertionsUtil.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/util/ConvertionsUtil.java
new file mode 100755
//Synthetic comment -- index 0000000..0e3cfad

//Synthetic comment -- @@ -0,0 +1,50 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/util/SparseArray.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/util/SparseArray.java
//Synthetic comment -- index 7c535a8..f0693fe 100644

//Synthetic comment -- @@ -343,7 +343,6 @@
}

@Override
            @SuppressWarnings("unchecked")
public E valueAt(int index) {
return mStorage.valueAt(index);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index 7d730d6..a14337d 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.repository;

import java.net.URL;
import java.util.Properties;

//Synthetic comment -- @@ -29,29 +31,41 @@
* Type: String.
*/
public static final String KEY_HTTP_PROXY_PORT = "http.proxyPort";           //$NON-NLS-1$
/**
* Java system setting picked up by {@link URL} for http proxy host.
* Type: String.
*/
public static final String KEY_HTTP_PROXY_HOST = "http.proxyHost";           //$NON-NLS-1$
/**
* Setting to force using http:// instead of https:// connections.
* Type: Boolean.
* Default: False.
*/
public static final String KEY_FORCE_HTTP = "sdkman.force.http";             //$NON-NLS-1$
/**
* Setting to display only packages that are new or updates.
* Type: Boolean.
* Default: True.
*/
public static final String KEY_SHOW_UPDATE_ONLY = "sdkman.show.update.only"; //$NON-NLS-1$
/**
* Setting to ask for permission before restarting ADB.
* Type: Boolean.
* Default: False.
*/
public static final String KEY_ASK_ADB_RESTART = "sdkman.ask.adb.restart";   //$NON-NLS-1$
/**
* Setting to set the density of the monitor.
* Type: Integer.
//Synthetic comment -- @@ -60,11 +74,11 @@
public static final String KEY_MONITOR_DENSITY = "sdkman.monitor.density"; //$NON-NLS-1$

/** Loads settings from the given {@link Properties} container and update the page UI. */
    public abstract void loadSettings(Properties in_settings);

/** Called by the application to retrieve settings from the UI and store them in
* the given {@link Properties} container. */
    public abstract void retrieveSettings(Properties out_settings);

/**
* Called by the application to give a callback that the page should invoke when








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index ba1cd2d..62a16d1 100755

//Synthetic comment -- @@ -1276,7 +1276,7 @@
return;
}

        final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();

mUpdaterData.getTaskFactory().start("Refresh Sources", new ITask() {
@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 2d8b57e..8ccb688 100755

//Synthetic comment -- @@ -20,13 +20,14 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;

import org.eclipse.jface.dialogs.MessageDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
//Synthetic comment -- @@ -40,48 +41,113 @@

private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

    private final Properties mProperties = new Properties();

/** The currently associated {@link ISettingsPage}. Can be null. */
private ISettingsPage mSettingsPage;

    private final UpdaterData mUpdaterData;

    public SettingsController(UpdaterData updaterData) {
        mUpdaterData = updaterData;
}

//--- Access to settings ------------

    /**
     * Returns the value of the {@link ISettingsPage#KEY_FORCE_HTTP} setting.
     *
     * @see ISettingsPage#KEY_FORCE_HTTP
     */
    public boolean getForceHttp() {
        return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_FORCE_HTTP));
    }

    /**
     * Returns the value of the {@link ISettingsPage#KEY_ASK_ADB_RESTART} setting.
     *
     * @see ISettingsPage#KEY_ASK_ADB_RESTART
     */
    public boolean getAskBeforeAdbRestart() {
        return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_ASK_ADB_RESTART));
    }

    /**
     * Returns the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
     *
     * @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
     */
    public boolean getShowUpdateOnly() {
        String value = mProperties.getProperty(ISettingsPage.KEY_SHOW_UPDATE_ONLY);
        if (value == null) {
            return true;
}
        return Boolean.parseBoolean(value);
}

/**
//Synthetic comment -- @@ -95,37 +161,21 @@
}

/**
     * Returns the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting
     * @see ISettingsPage#KEY_MONITOR_DENSITY
     */
    public int getMonitorDensity() {
        String value = mProperties.getProperty(ISettingsPage.KEY_MONITOR_DENSITY, null);
        if (value == null) {
            return -1;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
* Sets the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting.
*
* @param density the density of the monitor
* @see ISettingsPage#KEY_MONITOR_DENSITY
*/
public void setMonitorDensity(int density) {
        mProperties.setProperty(ISettingsPage.KEY_MONITOR_DENSITY, Integer.toString(density));
}

/**
* Internal helper to set a boolean setting.
*/
void setSetting(String key, boolean value) {
        mProperties.setProperty(key, Boolean.toString(value));
}

//--- Controller methods -------------
//Synthetic comment -- @@ -144,7 +194,7 @@
mSettingsPage = settingsPage;

if (settingsPage != null) {
            settingsPage.loadSettings(mProperties);

settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
@Override
//Synthetic comment -- @@ -168,17 +218,19 @@
if (f.exists()) {
fis = new FileInputStream(f);

                mProperties.load(fis);

// Properly reformat some settings to enforce their default value when missing.
                setShowUpdateOnly(getShowUpdateOnly());
                setSetting(ISettingsPage.KEY_ASK_ADB_RESTART, getAskBeforeAdbRestart());
}

} catch (Exception e) {
            ISdkLog log = mUpdaterData.getSdkLog();
            if (log != null) {
                log.error(e, "Failed to load settings from .android folder. Path is '%1$s'.", path);
}
} finally {
if (fis != null) {
//Synthetic comment -- @@ -204,34 +256,27 @@

fos = new FileOutputStream(f);

            mProperties.store( fos, "## Settings for Android Tool");  //$NON-NLS-1$

} catch (Exception e) {
            ISdkLog log = mUpdaterData.getSdkLog();

            if (log != null) {
                log.error(e, "Failed to save settings at '%1$s'", path);
}

            // This is important enough that we want to really nag the user about it
            String reason = null;

            if (e instanceof FileNotFoundException) {
                reason = "File not found";
            } else if (e instanceof AndroidLocationException) {
                reason = ".android folder not found, please define ANDROID_SDK_HOME";
            } else if (e.getMessage() != null) {
                reason = String.format("%1$s: %2$s", e.getClass().getSimpleName(), e.getMessage());
            } else {
                reason = e.getClass().getName();
            }

            MessageDialog.openInformation(mUpdaterData.getWindowShell(),
                    "SDK Manager Settings",
                    String.format(
                        "The Android SDK and AVD Manager failed to save its settings (%1$s) at %2$s",
                        reason, path));

} finally {
if (fos != null) {
try {
//Synthetic comment -- @@ -243,28 +288,23 @@
}

/**
     * When settings have changed: retrieve the new settings, apply them and save them.
     *
     * This updates Java system properties for the HTTP proxy.
*/
private void onSettingsChanged() {
if (mSettingsPage == null) {
return;
}

        String oldHttpsSetting = mProperties.getProperty(ISettingsPage.KEY_FORCE_HTTP,
                Boolean.FALSE.toString());

        mSettingsPage.retrieveSettings(mProperties);
applySettings();
saveSettings();

        String newHttpsSetting = mProperties.getProperty(ISettingsPage.KEY_FORCE_HTTP,
                Boolean.FALSE.toString());
        if (!newHttpsSetting.equals(oldHttpsSetting)) {
            // In case the HTTP/HTTPS setting changes, force sources to be reloaded
            // (this only refreshes sources that the user has already tried to open.)
            mUpdaterData.refreshSources(false /*forceFetching*/);
}
}

//Synthetic comment -- @@ -275,9 +315,9 @@
Properties props = System.getProperties();

// Get the configured HTTP proxy settings
        String proxyHost = mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
""); //$NON-NLS-1$
        String proxyPort = mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
""); //$NON-NLS-1$

// Set both the HTTP and HTTPS proxy system properties.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index a90002c..cfd2579 100755

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

//Synthetic comment -- @@ -35,19 +38,18 @@

public class SettingsDialog extends UpdaterBaseDialog implements ISettingsPage {

// data members
private final SettingsController mSettingsController;
private SettingsChangedCallback mSettingsChangedCallback;

// UI widgets
    private Group mProxySettingsGroup;
    private Group mMiscGroup;
    private Label mProxyServerLabel;
    private Label mProxyPortLabel;
    private Text mProxyServerText;
    private Text mProxyPortText;
    private Button mForceHttpCheck;
    private Button mAskAdbRestartCheck;

private SelectionAdapter mApplyOnSelected = new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -62,6 +64,7 @@
applyNewSettings(); //$hide$
}
};

public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
super(parentShell, updaterData, "Settings" /*title*/);
//Synthetic comment -- @@ -74,53 +77,102 @@
super.createContents();
Shell shell = getShell();

        mProxySettingsGroup = new Group(shell, SWT.NONE);
        mProxySettingsGroup.setText("Proxy Settings");
        GridDataBuilder.create(mProxySettingsGroup).fill().grab().hSpan(2);
        GridLayoutBuilder.create(mProxySettingsGroup).columns(2);

        mProxyServerLabel = new Label(mProxySettingsGroup, SWT.NONE);
        GridDataBuilder.create(mProxyServerLabel).hRight().vCenter();
        mProxyServerLabel.setText("HTTP Proxy Server");
        String tooltip = "The DNS name or IP of the HTTP proxy server to use. " +
                         "When empty, no HTTP proxy is used.";
        mProxyServerLabel.setToolTipText(tooltip);

        mProxyServerText = new Text(mProxySettingsGroup, SWT.BORDER);
        GridDataBuilder.create(mProxyServerText).hFill().hGrab().vCenter();
        mProxyServerText.addModifyListener(mApplyOnModified);
        mProxyServerText.setToolTipText(tooltip);

        mProxyPortLabel = new Label(mProxySettingsGroup, SWT.NONE);
        GridDataBuilder.create(mProxyPortLabel).hRight().vCenter();
        mProxyPortLabel.setText("HTTP Proxy Port");
        tooltip = "The port of the HTTP proxy server to use. " +
                  "When empty, the default for HTTP or HTTPS is used.";
        mProxyPortLabel.setToolTipText(tooltip);

        mProxyPortText = new Text(mProxySettingsGroup, SWT.BORDER);
        GridDataBuilder.create(mProxyPortText).hFill().hGrab().vCenter();
        mProxyPortText.addModifyListener(mApplyOnModified);
        mProxyPortText.setToolTipText(tooltip);

        mMiscGroup = new Group(shell, SWT.NONE);
        mMiscGroup.setText("Misc");
        GridDataBuilder.create(mMiscGroup).fill().grab().hSpan(2);
        GridLayoutBuilder.create(mMiscGroup).columns(2);

        mForceHttpCheck = new Button(mMiscGroup, SWT.CHECK);
        GridDataBuilder.create(mForceHttpCheck).hFill().hGrab().vCenter().hSpan(2);
        mForceHttpCheck.setText("Force https://... sources to be fetched using http://...");
        mForceHttpCheck.setToolTipText("If you are not able to connect to the official Android repository " +
"using HTTPS, enable this setting to force accessing it via HTTP.");
        mForceHttpCheck.addSelectionListener(mApplyOnSelected);

        mAskAdbRestartCheck = new Button(mMiscGroup, SWT.CHECK);
        GridDataBuilder.create(mAskAdbRestartCheck).hFill().hGrab().vCenter().hSpan(2);
        mAskAdbRestartCheck.setText("Ask before restarting ADB");
        mAskAdbRestartCheck.setToolTipText("When checked, the user will be asked for permission " +
"to restart ADB after updating an addon-on package or a tool package.");
        mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);

Label filler = new Label(shell, SWT.NONE);
GridDataBuilder.create(filler).hFill().hGrab();
//Synthetic comment -- @@ -149,23 +201,29 @@

/** Loads settings from the given {@link Properties} container and update the page UI. */
@Override
    public void loadSettings(Properties in_settings) {
        mProxyServerText.setText(in_settings.getProperty(KEY_HTTP_PROXY_HOST, ""));  //$NON-NLS-1$
        mProxyPortText.setText(  in_settings.getProperty(KEY_HTTP_PROXY_PORT, ""));  //$NON-NLS-1$
        mForceHttpCheck.setSelection(Boolean.parseBoolean(in_settings.getProperty(KEY_FORCE_HTTP)));
        mAskAdbRestartCheck.setSelection(Boolean.parseBoolean(in_settings.getProperty(KEY_ASK_ADB_RESTART)));
}

/** Called by the application to retrieve settings from the UI and store them in
* the given {@link Properties} container. */
@Override
    public void retrieveSettings(Properties out_settings) {
        out_settings.setProperty(KEY_HTTP_PROXY_HOST, mProxyServerText.getText());
        out_settings.setProperty(KEY_HTTP_PROXY_PORT, mProxyPortText.getText());
        out_settings.setProperty(KEY_FORCE_HTTP,
                Boolean.toString(mForceHttpCheck.getSelection()));
        out_settings.setProperty(KEY_ASK_ADB_RESTART,
                Boolean.toString(mAskAdbRestartCheck.getSelection()));
}

/**
//Synthetic comment -- @@ -189,6 +247,13 @@
}
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index af40d78..e64075a 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.LineUtil;
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;
//Synthetic comment -- @@ -113,9 +114,33 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;

        mSettingsController = new SettingsController(this);

initSdk();
}

// ----- getters, setters ----
//Synthetic comment -- @@ -127,7 +152,10 @@
@Override
public DownloadCache getDownloadCache() {
if (mDownloadCache == null) {
            mDownloadCache = new DownloadCache(DownloadCache.Strategy.FRESH_CACHE);
}
return mDownloadCache;
}
//Synthetic comment -- @@ -382,7 +410,7 @@
// this will accumulate all the packages installed.
final List<Archive> newlyInstalledArchives = new ArrayList<Archive>();

        final boolean forceHttp = getSettingsController().getForceHttp();

// sort all archives based on their dependency level.
Collections.sort(archives, new InstallOrderComparator());
//Synthetic comment -- @@ -609,7 +637,8 @@
private void askForAdbRestart(ITaskMonitor monitor) {
final boolean[] canRestart = new boolean[] { true };

        if (getWindowShell() != null && getSettingsController().getAskBeforeAdbRestart()) {
// need to ask for permission first
final Shell shell = getWindowShell();
if (shell != null && !shell.isDisposed()) {
//Synthetic comment -- @@ -1001,7 +1030,7 @@
public void refreshSources(final boolean forceFetching) {
assert mTaskFactory != null;

        final boolean forceHttp = getSettingsController().getForceHttp();

mTaskFactory.start("Refresh Sources", new ITask() {
@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index ecbefb7..4ad78b8 100755

//Synthetic comment -- @@ -206,7 +206,8 @@
}

// get remote packages
                    boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();
loadRemoteAddonsList(monitor.createSubMonitor(1));

SdkSource[] sources = mUpdaterData.getSources().getAllSources();
//Synthetic comment -- @@ -432,7 +433,7 @@
}
}

        if (mUpdaterData.getSettingsController().getForceHttp()) {
url = url.replaceAll("https://", "http://");    //$NON-NLS-1$ //$NON-NLS-2$
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index edecb17..e2ac96b 100644

//Synthetic comment -- @@ -417,7 +417,7 @@
*/
private int getMonitorDpi() {
if (mSettingsController != null) {
            sMonitorDpi = mSettingsController.getMonitorDensity();
}

if (sMonitorDpi == -1) { // first time? try to get a value







