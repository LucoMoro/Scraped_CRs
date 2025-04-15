/*Setting to enable preview packages.

Change-Id:I7af78e81e5433396c7a1039782ab8ceda35187a0*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index a14337d..6d15360 100755

//Synthetic comment -- @@ -67,6 +67,13 @@
public static final String KEY_USE_DOWNLOAD_CACHE = "sdkman.use.dl.cache";   //$NON-NLS-1$

/**
     * Setting to enabling previews in the package list
     * Type: Boolean.
     * Default: False.
     */
    public static final String KEY_ENABLE_PREVIEWS = "sdkman.enable.previews";   //$NON-NLS-1$

    /**
* Setting to set the density of the monitor.
* Type: Integer.
* Default: -1








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 8ccb688..f36e749 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.annotations.NonNull;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.ISdkLog;
//Synthetic comment -- @@ -42,7 +43,7 @@
private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

private final ISdkLog mSdkLog;
    private final Settings mSettings;

public interface OnChangedListener {
public void onSettingsChanged(SettingsController controller, Settings oldSettings);
//Synthetic comment -- @@ -52,9 +53,27 @@
/** The currently associated {@link ISettingsPage}. Can be null. */
private ISettingsPage mSettingsPage;

    /**
     * Constructs a new default {@link SettingsController}.
     *
     * @param sdkLog A non-null logger to use.
     */
    public SettingsController(@NonNull ISdkLog sdkLog) {
mSdkLog = sdkLog;
        mSettings = new Settings();
    }

    /**
     * Specialized constructor that wraps an existing {@link Settings} instance.
     * This is mostly used in unit-tests to override settings that are being used.
     * Normal usage should NOT need to call that constructor.
     *
     * @param sdkLog   A non-null logger to use.
     * @param settings A non-null {@link Settings} to use as-is. It is not duplicated.
     */
    protected SettingsController(ISdkLog sdkLog, Settings settings) {
        mSdkLog = sdkLog;
        mSettings = settings;
}

public Settings getSettings() {
//Synthetic comment -- @@ -77,20 +96,31 @@


public static class Settings {
        private final Properties mProperties;

/** Initialize an empty set of settings. */
public Settings() {
            mProperties = new Properties();
}

/** Duplicates a set of settings. */
public Settings(Settings settings) {
            this();
for (Entry<Object, Object> entry : settings.mProperties.entrySet()) {
mProperties.put(entry.getKey(), entry.getValue());
}
}

/**
         * Specialized constructor for unit-tests that wraps an existing
         * {@link Properties} instance. The properties instance is not duplicated,
         * it's merely used as-is and changes will be reflected directly.
         */
        protected Settings(Properties properties) {
            mProperties = properties;
        }

        /**
* Returns the value of the {@link ISettingsPage#KEY_FORCE_HTTP} setting.
*
* @see ISettingsPage#KEY_FORCE_HTTP
//Synthetic comment -- @@ -133,6 +163,15 @@
}

/**
         * Returns the value of the {@link ISettingsPage#KEY_ENABLE_PREVIEWS} setting.
         *
         * @see ISettingsPage#KEY_ENABLE_PREVIEWS
         */
        public boolean getEnablePreviews() {
            return Boolean.parseBoolean(mProperties.getProperty(ISettingsPage.KEY_ENABLE_PREVIEWS));
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
    private Text mTextCacheSize;
private Button mCheckUseCache;
private Button mCheckForceHttp;
private Button mCheckAskAdbRestart;
    private Button mCheckEnablePreviews;

private SelectionAdapter mApplyOnSelected = new SelectionAdapter() {
@Override
//Synthetic comment -- @@ -64,7 +66,6 @@
applyNewSettings(); //$hide$
}
};

public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
super(parentShell, updaterData, "Settings" /*title*/);
//Synthetic comment -- @@ -85,7 +86,7 @@
Label label = new Label(group, SWT.NONE);
GridDataBuilder.create(label).hRight().vCenter();
label.setText("HTTP Proxy Server");
        String tooltip = "The hostname or IP of the HTTP & HTTPS proxy server to use (e.g. proxy.example.com).\r\n" +
"When empty, the default Java proxy setting is used.";
label.setToolTipText(tooltip);

//Synthetic comment -- @@ -97,7 +98,7 @@
label = new Label(group, SWT.NONE);
GridDataBuilder.create(label).hRight().vCenter();
label.setText("HTTP Proxy Port");
        tooltip = "The port of the HTTP & HTTPS proxy server to use (e.g. 3128).\r\n" +
"When empty, the default Java proxy setting is used.";
label.setToolTipText(tooltip);

//Synthetic comment -- @@ -133,8 +134,9 @@
mCheckUseCache = new Button(group, SWT.CHECK);
GridDataBuilder.create(mCheckUseCache).vCenter().hSpan(1);
mCheckUseCache.setText("Use download cache");
        mCheckUseCache.setToolTipText(
            "When checked, small manifest files are cached locally.\r\n" +
            "Large binary files are never cached locally.");
mCheckUseCache.addSelectionListener(mApplyOnSelected);

label = new Label(group, SWT.NONE);
//Synthetic comment -- @@ -154,7 +156,7 @@

// ----
group = new Group(shell, SWT.NONE);
        group.setText("Others");
GridDataBuilder.create(group).fill().grab().hSpan(2);
GridLayoutBuilder.create(group).columns(2);

//Synthetic comment -- @@ -162,18 +164,27 @@
GridDataBuilder.create(mCheckForceHttp).hFill().hGrab().vCenter().hSpan(2);
mCheckForceHttp.setText("Force https://... sources to be fetched using http://...");
mCheckForceHttp.setToolTipText(
            "If you are not able to connect to the official Android repository using HTTPS,\r\n" +
            "enable this setting to force accessing it via HTTP.");
mCheckForceHttp.addSelectionListener(mApplyOnSelected);

mCheckAskAdbRestart = new Button(group, SWT.CHECK);
GridDataBuilder.create(mCheckAskAdbRestart).hFill().hGrab().vCenter().hSpan(2);
mCheckAskAdbRestart.setText("Ask before restarting ADB");
mCheckAskAdbRestart.setToolTipText(
                "When checked, the user will be asked for permission to restart ADB\r\n" +
                "after updating an addon-on package or a tool package.");
mCheckAskAdbRestart.addSelectionListener(mApplyOnSelected);

        mCheckEnablePreviews = new Button(group, SWT.CHECK);
        GridDataBuilder.create(mCheckEnablePreviews).hFill().hGrab().vCenter().hSpan(2);
        mCheckEnablePreviews.setText("Enable Preview Tools");
        mCheckEnablePreviews.setToolTipText(
            "When checked, the package list will also display preview versions of the tools.\r\n" +
            "These are optional future release candidates that the Android tools team\r\n" +
            "publishes from time to time for early feedback.");
        mCheckEnablePreviews.addSelectionListener(mApplyOnSelected);

Label filler = new Label(shell, SWT.NONE);
GridDataBuilder.create(filler).hFill().hGrab();

//Synthetic comment -- @@ -210,6 +221,9 @@
Boolean.parseBoolean(inSettings.getProperty(KEY_ASK_ADB_RESTART)));
mCheckUseCache.setSelection(
Boolean.parseBoolean(inSettings.getProperty(KEY_USE_DOWNLOAD_CACHE)));
        mCheckEnablePreviews.setSelection(
                Boolean.parseBoolean(inSettings.getProperty(KEY_ENABLE_PREVIEWS)));

}

/** Called by the application to retrieve settings from the UI and store them in
//Synthetic comment -- @@ -224,6 +238,9 @@
Boolean.toString(mCheckAskAdbRestart.getSelection()));
outSettings.setProperty(KEY_USE_DOWNLOAD_CACHE,
Boolean.toString(mCheckUseCache.getSelection()));
        outSettings.setProperty(KEY_ENABLE_PREVIEWS,
                Boolean.toString(mCheckEnablePreviews.getSelection()));

}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index e64075a..e0d4047 100755

//Synthetic comment -- @@ -114,33 +114,9 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;


        mSettingsController = initSettingsController();
initSdk();
}

// ----- getters, setters ----
//Synthetic comment -- @@ -276,6 +252,7 @@

/**
* Initializes the {@link SdkManager} and the {@link AvdManager}.
     * Extracted so that we can override this in unit tests.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
protected void initSdk() {
//Synthetic comment -- @@ -299,6 +276,41 @@
broadcastOnSdkReload();
}

    /**
     * Initializes the {@link SettingsController}
     * Extracted so that we can override this in unit tests.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected SettingsController initSettingsController() {
        SettingsController settingsController = new SettingsController(mSdkLog);
        settingsController.registerOnChangedListener(new OnChangedListener() {
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

                if (oldSettings.getForceHttp() != controller.getSettings().getForceHttp() ||
                        oldSettings.getEnablePreviews() !=
                            controller.getSettings().getEnablePreviews()) {
                    refreshSources(false /*forceFetching*/);
                }
            }
        });
        return settingsController;
    }

@VisibleForTesting(visibility=Visibility.PRIVATE)
protected void setSdkManager(SdkManager sdkManager) {
mSdkManager = sdkManager;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 9e45748..0bda937 100755

//Synthetic comment -- @@ -522,7 +522,16 @@
boolean hasChanged = false;
List<PkgCategory> cats = op.getCategories();

        boolean enablePreviews =
            mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

nextPkg: for (Package newPkg : packages) {

            if (!enablePreviews && newPkg.getRevision().isPreview()) {
                // This is a preview and previews are not enabled. Ignore the package.
                continue nextPkg;
            }

for (PkgCategory cat : cats) {
for (PkgState state : PKG_STATES) {
for (Iterator<PkgItem> currItemIt = cat.getItems().iterator();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index 2260739..0fc5b55 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.DownloadCache;
//Synthetic comment -- @@ -27,6 +28,7 @@
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -34,6 +36,7 @@
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** A mock UpdaterData that simply records what would have been installed. */
public class MockUpdaterData extends UpdaterData {
//Synthetic comment -- @@ -60,11 +63,18 @@
return mInstalled.toArray(new ArchiveReplacement[mInstalled.size()]);
}

    /** Overrides the sdk manager with our mock instance. */
@Override
protected void initSdk() {
setSdkManager(new MockEmptySdkManager(SDK_PATH));
}

    /** Overrides the settings controller with our mock instance. */
    @Override
    protected SettingsController initSettingsController() {
        return createSettingsController(getSdkLog());
    }

@Override
public void reloadSdk() {
// bypass original implementation
//Synthetic comment -- @@ -107,6 +117,48 @@
return mMockDownloadCache;
}

    public void overrideSetting(String key, boolean boolValue) {
        SettingsController sc = getSettingsController();
        assert sc instanceof MockSettingsController;
        ((MockSettingsController)sc).overrideSetting(key, boolValue);
    }
    //------------

    public static SettingsController createSettingsController(ISdkLog sdkLog) {
        Properties props = new Properties();
        Settings settings = new Settings(props) {}; // this constructor is protected
        MockSettingsController controller = new MockSettingsController(sdkLog, settings);
        controller.setProperties(props);
        return controller;
    }

    static class MockSettingsController extends SettingsController {

        private Properties mProperties;

        MockSettingsController(ISdkLog sdkLog, Settings settings) {
            super(sdkLog, settings);
        }

        void setProperties(Properties properties) {
            mProperties = properties;
        }

        public void overrideSetting(String key, boolean boolValue) {
            mProperties.setProperty(key, Boolean.valueOf(boolValue).toString());
        }

        @Override
        public void loadSettings() {
            // This mock setting controller does not load live file settings.
        }

        @Override
        public void saveSettings() {
            // This mock setting controller does not save live file settings.
        }
    }

//------------

private class MockTaskFactory implements ITaskFactory {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index 26b39ed..57573e1 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.ISettingsPage;
import com.android.sdkuilib.internal.repository.MockUpdaterData;

import java.util.Properties;
//Synthetic comment -- @@ -1455,10 +1456,42 @@
getTree(m, false /*displaySortByApi*/));
}

    public void testToolsPreviewsDisabled() {
        // Test: No local tools installed. The remote server has both tools and platforms
        // in release and RC versions. However the settings "enable previews" is disabled
        // (which is the default) so the previews are not actually loaded from the server.

        SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

        m.updateStart();
        m.updateSourcePackages(true /*sortByApi*/, src1, new Package[] {
                new MockToolPackage(src1, new FullRevision(2, 0, 0), 3),          // Tools 2
                new MockToolPackage(src1, new FullRevision(4, 0, 0, 1), 3),       // Tools 4 rc1
                new MockPlatformToolPackage(src1, new FullRevision(3, 0, 0)),     // Plat-T 3
                new MockPlatformToolPackage(src1, new FullRevision(5, 0, 0, 1)),  // Plat-T 5 rc1
        });
        m.updateEnd(true /*sortByApi*/);

        assertEquals(
                "PkgCategoryApi <API=TOOLS, label=Tools, #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n" +
                "PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
                getTree(m, true /*displaySortByApi*/));
        assertEquals(
                "PkgCategorySource <source=repo1 (1.example.com), #items=2>\n" +
                "-- <NEW, pkg:Android SDK Tools, revision 2>\n" +
                "-- <NEW, pkg:Android SDK Platform-tools, revision 3>\n",
                getTree(m, false /*displaySortByApi*/));
    }

public void testToolsPreviews() {
// Test: No local tools installed. The remote server has both tools and platforms
// in release and RC versions.

        // Enable previews in the settings
        u.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();
//Synthetic comment -- @@ -1494,6 +1527,9 @@
// => v3 is updated by 3.0.1
// => v4.0.0rc1 does not update 3.0.0, instead it's a separate download.

        // Enable previews in the settings
        u.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();
//Synthetic comment -- @@ -1568,6 +1604,9 @@
// => Installed 3.0.1rc1 can be updated by 3.0.1rc2
// => There's a separate "new" download for 3.0.0, not installed and NOT updating 3.0.1rc1.

        // Enable previews in the settings
        u.overrideSetting(ISettingsPage.KEY_ENABLE_PREVIEWS, true);

SdkSource src1 = new SdkRepoSource("http://1.example.com/url1", "repo1");

m.updateStart();







