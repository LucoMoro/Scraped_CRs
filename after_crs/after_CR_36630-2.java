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

    public File getCacheRoot() {
        return mCacheRoot;
    }

    /**
     * Computes the size of the cached files.
     *
     * @return The sum of the byte size of the cached files.
     */
    public long getCurrentSize() {
        long size = 0;

        if (mCacheRoot != null) {
            File[] files = mCacheRoot.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        String name = f.getName();
                        if (name.startsWith(BIN_FILE_PREFIX) ||
                                name.startsWith(INFO_FILE_PREFIX)) {
                            size += f.length();
                        }
                    }
                }
            }
        }

        return size;
    }

    /**
     * Removes all cached files from the cache directory.
     */
    public void clearCache() {
        if (mCacheRoot != null) {
            File[] files = mCacheRoot.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        String name = f.getName();
                        if (name.startsWith(BIN_FILE_PREFIX) ||
                                name.startsWith(INFO_FILE_PREFIX)) {
                            f.delete();
                        }
                    }
                }
            }
        }
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
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.sdklib.util;

/**
 * Helper methods to do some convertions.
 */
public abstract class ConvertionsUtil {

    /**
     * Converts a byte size to a human readable string,
     * for example "3 MiB", "1020 Bytes" or "1.2 GiB".
     *
     * @param size The byte size to convert.
     * @return A new non-null string, with the size expressed in either Bytes
     *   or KiB or MiB or GiB.
     */
    public static String bytesToString(long size) {
        String sizeStr;

        if (size < 1024) {
            sizeStr = String.format("%d Bytes", size);
        } else if (size < 1024 * 1024) {
            sizeStr = String.format("%d KiB", Math.round(size / 1024.0));
        } else if (size < 1024 * 1024 * 1024) {
            sizeStr = String.format("%.1f MiB",
                    Math.round(10.0 * size / (1024 * 1024.0))/ 10.0);
        } else {
            sizeStr = String.format("%.1f GiB",
                    Math.round(10.0 * size / (1024 * 1024 * 1024.0))/ 10.0);
        }

        return sizeStr;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/util/SparseArray.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/util/SparseArray.java
//Synthetic comment -- index 7c535a8..f0693fe 100644

//Synthetic comment -- @@ -343,7 +343,6 @@
}

@Override
public E valueAt(int index) {
return mStorage.valueAt(index);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index 7d730d6..a14337d 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.DownloadCache;

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
     * Setting to use the {@link DownloadCache}, for small manifest XML files.
     * Type: Boolean.
     * Default: True.
     */
    public static final String KEY_USE_DOWNLOAD_CACHE = "sdkman.use.dl.cache";   //$NON-NLS-1$

/**
* Setting to set the density of the monitor.
* Type: Integer.
//Synthetic comment -- @@ -60,11 +74,11 @@
public static final String KEY_MONITOR_DENSITY = "sdkman.monitor.density"; //$NON-NLS-1$

/** Loads settings from the given {@link Properties} container and update the page UI. */
    public abstract void loadSettings(Properties inSettings);

/** Called by the application to retrieve settings from the UI and store them in
* the given {@link Properties} container. */
    public abstract void retrieveSettings(Properties outSettings);

/**
* Called by the application to give a callback that the page should invoke when








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
//Synthetic comment -- index ba1cd2d..62a16d1 100755

//Synthetic comment -- @@ -1276,7 +1276,7 @@
return;
}

        final boolean forceHttp = mUpdaterData.getSettingsController().getSettings().getForceHttp();

mUpdaterData.getTaskFactory().start("Refresh Sources", new ITask() {
@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 2d8b57e..8ccb688 100755

//Synthetic comment -- @@ -20,13 +20,14 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

/**
//Synthetic comment -- @@ -40,48 +41,113 @@

private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

    private final ISdkLog mSdkLog;
    private final Settings mSettings = new Settings();

    public interface OnChangedListener {
        public void onSettingsChanged(SettingsController controller, Settings oldSettings);
    }
    private final List<OnChangedListener> mChangedListeners = new ArrayList<OnChangedListener>(1);

/** The currently associated {@link ISettingsPage}. Can be null. */
private ISettingsPage mSettingsPage;


    public SettingsController(ISdkLog sdkLog) {
        mSdkLog = sdkLog;
    }

    public Settings getSettings() {
        return mSettings;
    }

    public void registerOnChangedListener(OnChangedListener listener) {
        if (listener != null && !mChangedListeners.contains(listener)) {
            mChangedListeners.add(listener);
        }
    }

    public void unregisterOnChangedListener(OnChangedListener listener) {
        if (listener != null) {
            mChangedListeners.remove(listener);
        }
}

//--- Access to settings ------------


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
         * Returns the value of the {@link ISettingsPage#KEY_USE_DOWNLOAD_CACHE} setting.
         *
         * @see ISettingsPage#KEY_USE_DOWNLOAD_CACHE
         */
        public boolean getUseDownloadCache() {
            return Boolean.parseBoolean(
                    mProperties.getProperty(
                            ISettingsPage.KEY_USE_DOWNLOAD_CACHE,
                            Boolean.TRUE.toString()));
        }

        /**
         * Returns the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
         *
         * @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
         */
        public boolean getShowUpdateOnly() {
            return Boolean.parseBoolean(
                    mProperties.getProperty(
                            ISettingsPage.KEY_SHOW_UPDATE_ONLY,
                            Boolean.TRUE.toString()));
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
}

/**
//Synthetic comment -- @@ -95,37 +161,21 @@
}

/**
* Sets the value of the {@link ISettingsPage#KEY_MONITOR_DENSITY} setting.
*
* @param density the density of the monitor
* @see ISettingsPage#KEY_MONITOR_DENSITY
*/
public void setMonitorDensity(int density) {
        mSettings.mProperties.setProperty(
                ISettingsPage.KEY_MONITOR_DENSITY, Integer.toString(density));
}

/**
* Internal helper to set a boolean setting.
*/
void setSetting(String key, boolean value) {
        mSettings.mProperties.setProperty(key, Boolean.toString(value));
}

//--- Controller methods -------------
//Synthetic comment -- @@ -144,7 +194,7 @@
mSettingsPage = settingsPage;

if (settingsPage != null) {
            settingsPage.loadSettings(mSettings.mProperties);

settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
@Override
//Synthetic comment -- @@ -168,17 +218,19 @@
if (f.exists()) {
fis = new FileInputStream(f);

                mSettings.mProperties.load(fis);

// Properly reformat some settings to enforce their default value when missing.
                setShowUpdateOnly(mSettings.getShowUpdateOnly());
                setSetting(ISettingsPage.KEY_ASK_ADB_RESTART, mSettings.getAskBeforeAdbRestart());
                setSetting(ISettingsPage.KEY_USE_DOWNLOAD_CACHE, mSettings.getUseDownloadCache());
}

} catch (Exception e) {
            if (mSdkLog != null) {
                mSdkLog.error(e,
                        "Failed to load settings from .android folder. Path is '%1$s'.",
                        path);
}
} finally {
if (fis != null) {
//Synthetic comment -- @@ -204,34 +256,27 @@

fos = new FileOutputStream(f);

            mSettings.mProperties.store(fos, "## Settings for Android Tool");  //$NON-NLS-1$

} catch (Exception e) {
            if (mSdkLog != null) {
                // This is important enough that we want to really nag the user about it
                String reason = null;

                if (e instanceof FileNotFoundException) {
                    reason = "File not found";
                } else if (e instanceof AndroidLocationException) {
                    reason = ".android folder not found, please define ANDROID_SDK_HOME";
                } else if (e.getMessage() != null) {
                    reason = String.format("%1$s: %2$s",
                            e.getClass().getSimpleName(),
                            e.getMessage());
                } else {
                    reason = e.getClass().getName();
                }

                mSdkLog.error(e, "Failed to save settings file '%1$s': %2$s", path, reason);
}
} finally {
if (fos != null) {
try {
//Synthetic comment -- @@ -243,28 +288,23 @@
}

/**
     * When settings have changed: retrieve the new settings, apply them, save them
     * and notify on-settings-changed listeners.
*/
private void onSettingsChanged() {
if (mSettingsPage == null) {
return;
}

        Settings oldSettings = new Settings(mSettings);
        mSettingsPage.retrieveSettings(mSettings.mProperties);
applySettings();
saveSettings();

        for (OnChangedListener listener : mChangedListeners) {
            try {
                listener.onSettingsChanged(this, oldSettings);
            } catch (Throwable ignore) {}
}
}

//Synthetic comment -- @@ -275,9 +315,9 @@
Properties props = System.getProperties();

// Get the configured HTTP proxy settings
        String proxyHost = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
""); //$NON-NLS-1$
        String proxyPort = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
""); //$NON-NLS-1$

// Set both the HTTP and HTTPS proxy system properties.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index a90002c..cfd2579 100755

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.util.ConvertionsUtil;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

//Synthetic comment -- @@ -35,19 +38,18 @@

public class SettingsDialog extends UpdaterBaseDialog implements ISettingsPage {


// data members
    private final DownloadCache mDownloadCache = new DownloadCache(Strategy.SERVE_CACHE);
private final SettingsController mSettingsController;
private SettingsChangedCallback mSettingsChangedCallback;

// UI widgets
    private Text mTextProxyServer;
    private Text mTextProxyPort;
    private Button mCheckUseCache;
    private Button mCheckForceHttp;
    private Button mCheckAskAdbRestart;

private SelectionAdapter mApplyOnSelected = new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -62,6 +64,7 @@
applyNewSettings(); //$hide$
}
};
    private Text mTextCacheSize;

public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
super(parentShell, updaterData, "Settings" /*title*/);
//Synthetic comment -- @@ -74,53 +77,102 @@
super.createContents();
Shell shell = getShell();

        Group group = new Group(shell, SWT.NONE);
        group.setText("Proxy Settings");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(2);

        Label label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("HTTP Proxy Server");
        String tooltip = "The hostname or IP of the HTTP & HTTPS proxy server to use (e.g. proxy.example.com). " +
                         "When empty, the default Java proxy setting is used.";
        label.setToolTipText(tooltip);

        mTextProxyServer = new Text(group, SWT.BORDER);
        GridDataBuilder.create(mTextProxyServer).hFill().hGrab().vCenter();
        mTextProxyServer.addModifyListener(mApplyOnModified);
        mTextProxyServer.setToolTipText(tooltip);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("HTTP Proxy Port");
        tooltip = "The port of the HTTP & HTTPS proxy server to use (e.g. 3128). " +
                  "When empty, the default Java proxy setting is used.";
        label.setToolTipText(tooltip);

        mTextProxyPort = new Text(group, SWT.BORDER);
        GridDataBuilder.create(mTextProxyPort).hFill().hGrab().vCenter();
        mTextProxyPort.addModifyListener(mApplyOnModified);
        mTextProxyPort.setToolTipText(tooltip);

        // ----
        group = new Group(shell, SWT.NONE);
        group.setText("Manifest Cache");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(3);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("Directory:");

        Text text = new Text(group, SWT.NONE);
        GridDataBuilder.create(text).hFill().hGrab().vCenter().hSpan(2);
        text.setEnabled(false);
        text.setText(mDownloadCache.getCacheRoot().getAbsolutePath());

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hRight().vCenter();
        label.setText("Current Size:");

        mTextCacheSize = new Text(group, SWT.NONE);
        GridDataBuilder.create(mTextCacheSize).hFill().hGrab().vCenter().hSpan(2);
        mTextCacheSize.setEnabled(false);
        updateDownloadCacheSize();

        mCheckUseCache = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckUseCache).vCenter().hSpan(1);
        mCheckUseCache.setText("Use download cache");
        mCheckUseCache.setToolTipText("When checked, small manifest files are cached locally. " +
                "Large binary files are never cached locally.");
        mCheckUseCache.addSelectionListener(mApplyOnSelected);

        label = new Label(group, SWT.NONE);
        GridDataBuilder.create(label).hFill().hGrab().hSpan(1);

        Button button = new Button(group, SWT.PUSH);
        GridDataBuilder.create(button).vCenter().hSpan(1);
        button.setText("Clear Cache");
        button.setToolTipText("Deletes all cached files.");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mDownloadCache.clearCache();
                updateDownloadCacheSize();
            }
        });

        // ----
        group = new Group(shell, SWT.NONE);
        group.setText("Misc");
        GridDataBuilder.create(group).fill().grab().hSpan(2);
        GridLayoutBuilder.create(group).columns(2);

        mCheckForceHttp = new Button(group, SWT.CHECK);
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
//Synthetic comment -- @@ -149,23 +201,29 @@

/** Loads settings from the given {@link Properties} container and update the page UI. */
@Override
    public void loadSettings(Properties inSettings) {
        mTextProxyServer.setText(inSettings.getProperty(KEY_HTTP_PROXY_HOST, ""));  //$NON-NLS-1$
        mTextProxyPort.setText(  inSettings.getProperty(KEY_HTTP_PROXY_PORT, ""));  //$NON-NLS-1$
        mCheckForceHttp.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_FORCE_HTTP)));
        mCheckAskAdbRestart.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_ASK_ADB_RESTART)));
        mCheckUseCache.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_USE_DOWNLOAD_CACHE)));
}

/** Called by the application to retrieve settings from the UI and store them in
* the given {@link Properties} container. */
@Override
    public void retrieveSettings(Properties outSettings) {
        outSettings.setProperty(KEY_HTTP_PROXY_HOST, mTextProxyServer.getText());
        outSettings.setProperty(KEY_HTTP_PROXY_PORT, mTextProxyPort.getText());
        outSettings.setProperty(KEY_FORCE_HTTP,
                Boolean.toString(mCheckForceHttp.getSelection()));
        outSettings.setProperty(KEY_ASK_ADB_RESTART,
                Boolean.toString(mCheckAskAdbRestart.getSelection()));
        outSettings.setProperty(KEY_USE_DOWNLOAD_CACHE,
                Boolean.toString(mCheckUseCache.getSelection()));
}

/**
//Synthetic comment -- @@ -189,6 +247,13 @@
}
}

    private void updateDownloadCacheSize() {
        long size = mDownloadCache.getCurrentSize();
        String str = ConvertionsUtil.bytesToString(size);
        mTextCacheSize.setText(str);
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
import com.android.sdkuilib.internal.repository.SettingsController.OnChangedListener;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;
//Synthetic comment -- @@ -113,9 +114,33 @@
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
//Synthetic comment -- @@ -127,7 +152,10 @@
@Override
public DownloadCache getDownloadCache() {
if (mDownloadCache == null) {
            mDownloadCache = new DownloadCache(
                    mSettingsController.getSettings().getUseDownloadCache() ?
                            DownloadCache.Strategy.FRESH_CACHE :
                            DownloadCache.Strategy.DIRECT);
}
return mDownloadCache;
}
//Synthetic comment -- @@ -382,7 +410,7 @@
// this will accumulate all the packages installed.
final List<Archive> newlyInstalledArchives = new ArrayList<Archive>();

        final boolean forceHttp = getSettingsController().getSettings().getForceHttp();

// sort all archives based on their dependency level.
Collections.sort(archives, new InstallOrderComparator());
//Synthetic comment -- @@ -609,7 +637,8 @@
private void askForAdbRestart(ITaskMonitor monitor) {
final boolean[] canRestart = new boolean[] { true };

        if (getWindowShell() != null &&
                getSettingsController().getSettings().getAskBeforeAdbRestart()) {
// need to ask for permission first
final Shell shell = getWindowShell();
if (shell != null && !shell.isDisposed()) {
//Synthetic comment -- @@ -1001,7 +1030,7 @@
public void refreshSources(final boolean forceFetching) {
assert mTaskFactory != null;

        final boolean forceHttp = getSettingsController().getSettings().getForceHttp();

mTaskFactory.start("Refresh Sources", new ITask() {
@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index ecbefb7..4ad78b8 100755

//Synthetic comment -- @@ -206,7 +206,8 @@
}

// get remote packages
                    boolean forceHttp =
                        mUpdaterData.getSettingsController().getSettings().getForceHttp();
loadRemoteAddonsList(monitor.createSubMonitor(1));

SdkSource[] sources = mUpdaterData.getSources().getAllSources();
//Synthetic comment -- @@ -432,7 +433,7 @@
}
}

        if (mUpdaterData.getSettingsController().getSettings().getForceHttp()) {
url = url.replaceAll("https://", "http://");    //$NON-NLS-1$ //$NON-NLS-2$
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index edecb17..e2ac96b 100644

//Synthetic comment -- @@ -417,7 +417,7 @@
*/
private int getMonitorDpi() {
if (mSettingsController != null) {
            sMonitorDpi = mSettingsController.getSettings().getMonitorDensity();
}

if (sMonitorDpi == -1) { // first time? try to get a value







