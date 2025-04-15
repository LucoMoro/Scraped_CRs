/*SDK Manager: rework package loader.

When the SDK Manager window opens, the process is
changed to:

- first a package loader is created that only checks
  the local cache xml files. It populates the package
  list based on what the client last got, essentially.

- next a regular package loader is created that will
  respect the expiration and refresh parameters of the
  download cache.

This means for users, in the majority of cases when
remove servers do not change, the package list will
be populated as fast as possible and then an asynchronous
refresh happens.

Change-Id:Ifd1f58412dcc643eaae37257a9bc0a01fc222c90*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index c29e988..591e447 100755

//Synthetic comment -- @@ -134,6 +134,12 @@

public enum Strategy {
/**
         * Exclusively serves data from the cache. If files are available in the
         * cache, serve them as is (without trying to refresh them). If files are
         * not available, they are <em>not</em> fetched at all.
         */
        ONLY_CACHE,
        /**
* If the files are available in the cache, serve them as-is, otherwise
* download them and return the cached version. No expiration or refresh
* is attempted if a file is in the cache.
//Synthetic comment -- @@ -237,6 +243,7 @@
* @param monitor {@link ITaskMonitor} which is related to this URL
*            fetching.
* @return Returns an {@link InputStream} holding the URL content.
     *   Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
//Synthetic comment -- @@ -414,6 +421,14 @@
} catch (IOException ignore) {}
}

        if (!useCached && mStrategy == Strategy.ONLY_CACHE) {
            // We don't have a document to serve from the cache.
            if (DEBUG) {
                System.out.println(String.format("%s : file not in cache", urlString)); //$NON-NLS-1$
            }
            return null;
        }

// If we're not using the cache, try to remove the cache and download again.
try {
cached.delete();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskFactory.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskFactory.java
//Synthetic comment -- index fb59b42..dfd197d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository;


/**
* A factory that can start and run new {@link ITask}s.
*/
//Synthetic comment -- @@ -23,6 +24,12 @@

/**
* Starts a new task with a new {@link ITaskMonitor}.
     * <p/>
     * The task will execute in a thread and runs it own UI loop.
     * This means the task can perform UI operations using
     * {@code Display#asyncExec(Runnable)}.
     * <p/>
     * In either case, the method only returns when the task has finished.
*
* @param title The title of the task, displayed in the monitor if any.
* @param task The task to run.
//Synthetic comment -- @@ -36,6 +43,13 @@
* and give the sub-monitor to the new task with the number of work units you want
* it to fill. The {@link #start} method will make sure to <em>fill</em> the progress
* when the task is completed, in case the actual task did not.
     * <p/>
     * When a task is started from within a monitor, it reuses the thread
     * from the parent. Otherwise it starts a new thread and runs it own
     * UI loop. This means the task can perform UI operations using
     * {@code Display#asyncExec(Runnable)}.
     * <p/>
     * In either case, the method only returns when the task has finished.
*
* @param title The title of the task, displayed in the monitor if any.
* @param parentMonitor The parent monitor. Can be null.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index a58e0f0..af40d78 100755

//Synthetic comment -- @@ -24,31 +24,28 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AdbWrapper;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.LineUtil;
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;
import com.android.sdkuilib.repository.ISdkChangeListener;

//Synthetic comment -- @@ -79,27 +76,32 @@

private String mOsSdkRoot;

private final LocalSdkParser mLocalSdkParser = new LocalSdkParser();
private final SdkSources mSources = new SdkSources();
private final SettingsController mSettingsController;
private final ArrayList<ISdkChangeListener> mListeners = new ArrayList<ISdkChangeListener>();
    private final ISdkLog mSdkLog;
    private ITaskFactory mTaskFactory;
private Shell mWindowShell;
    private SdkManager mSdkManager;
    private AvdManager mAvdManager;
/**
     * The current {@link PackageLoader} to use.
     * Lazily created in {@link #getPackageLoader()}.
*/
    private PackageLoader mPackageLoader;
    /**
     * The current {@link DownloadCache} to use.
     * Lazily created in {@link #getDownloadCache()}.
     */
    private DownloadCache mDownloadCache;
    /**
     * The current {@link ImageFactory}.
     * Set via {@link #setImageFactory(ImageFactory)} by the window implementation.
     * It is null when invoked using the command-line interface.
     */
    private ImageFactory mImageFactory;
    private AndroidLocationException mAvdManagerInitError;

/**
* Creates a new updater data.
//Synthetic comment -- @@ -111,7 +113,6 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;

mSettingsController = new SettingsController(this);

initSdk();
//Synthetic comment -- @@ -198,6 +199,14 @@
return mWindowShell;
}

    public PackageLoader getPackageLoader() {
        // The package loader is lazily initialized here.
        if (mPackageLoader == null) {
            mPackageLoader = new PackageLoader(this);
        }
        return mPackageLoader;
    }

/**
* Check if any error occurred during initialization.
* If it did, display an error message.
//Synthetic comment -- @@ -446,7 +455,7 @@
mOsSdkRoot,
forceHttp,
mSdkManager,
                                              getDownloadCache(),
monitor)) {
// We installed this archive.
newlyInstalledArchives.add(archive);
//Synthetic comment -- @@ -698,7 +707,7 @@
includeObsoletes);

if (selectedArchives == null) {
            getPackageLoader().loadRemoteAddonsList(new NullTaskMonitor(getSdkLog()));
ul.addNewPlatforms(
archives,
getSources(),
//Synthetic comment -- @@ -733,7 +742,7 @@
*/
private List<ArchiveInfo> getRemoteArchives_NoGUI(boolean includeAll) {
refreshSources(true);
        getPackageLoader().loadRemoteAddonsList(new NullTaskMonitor(getSdkLog()));

List<ArchiveInfo> archives;
SdkUpdaterLogic ul = new SdkUpdaterLogic(this);
//Synthetic comment -- @@ -998,9 +1007,7 @@
@Override
public void run(ITaskMonitor monitor) {

                getPackageLoader().loadRemoteAddonsList(monitor);

SdkSource[] sources = mSources.getAllSources();
monitor.setDescription("Refresh Sources");
//Synthetic comment -- @@ -1009,7 +1016,7 @@
if (forceFetching ||
source.getPackages() != null ||
source.getFetchError() != null) {
                        source.load(getDownloadCache(), monitor.createSubMonitor(1), forceHttp);
}
monitor.incProgress(1);
}
//Synthetic comment -- @@ -1018,67 +1025,6 @@
}

/**
* Safely invoke all the registered {@link ISdkChangeListener#onSdkLoaded()}.
* This can be called from any thread.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AdtUpdateDialog.java
//Synthetic comment -- index f481b0a..ba9bce7 100755

//Synthetic comment -- @@ -76,7 +76,7 @@
private Map<Package, File> mResultPaths = null;
private SettingsController mSettingsController;
private PackageFilter mPackageFilter;
    private PackageLoader mPackageLoader;

private ProgressBar mProgressBar;
private Label mStatusText;
//Synthetic comment -- @@ -221,12 +221,12 @@

mUpdaterData.broadcastOnSdkLoaded();

        mPackageLoader = new PackageLoader(mUpdaterData);
}

@Override
protected void eventLoop() {
        mPackageLoader.loadPackagesWithInstallTask(
mPackageFilter.installFlags(),
new IAutoInstallTask() {
@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index 4ac03e9..ecbefb7 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -23,7 +25,12 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.UpdaterData;

import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -39,13 +46,30 @@
* Loads packages fetched from the remote SDK Repository and keeps track
* of their state compared with the current local SDK installation.
*/
public class PackageLoader {

    /** The update data context. Never null. */
private final UpdaterData mUpdaterData;

/**
     * The {@link DownloadCache} override. Can be null, in which case the one from
     * {@link UpdaterData} is used instead.
     * @see #getDownloadCache()
     */
    private final DownloadCache mOverrideCache;

    /**
     * 0 = need to fetch remote addons list once..
     * 1 = fetch succeeded, don't need to do it any more.
     * -1= fetch failed, do it again only if the user requests a refresh
     *     or changes the force-http setting.
     */
    private int mStateFetchRemoteAddonsList;


    /**
* Interface for the callback called by
     * {@link PackageLoader#loadPackages(ISourceLoadedCallback)}.
* <p/>
* After processing each source, the package loader calls {@link #onUpdateSource}
* with the list of packages found in that source.
//Synthetic comment -- @@ -128,12 +152,27 @@
}

/**
     * Creates a new PackageManager associated with the given {@link UpdaterData}
     * and using the {@link UpdaterData}'s default {@link DownloadCache}.
*
* @param updaterData The {@link UpdaterData}. Must not be null.
*/
public PackageLoader(UpdaterData updaterData) {
mUpdaterData = updaterData;
        mOverrideCache = null;
    }

    /**
     * Creates a new PackageManager associated with the given {@link UpdaterData}
     * but using the specified {@link DownloadCache} instead of the one from
     * {@link UpdaterData}.
     *
     * @param updaterData The {@link UpdaterData}. Must not be null.
     * @param cache The {@link DownloadCache} to use instead of the one from {@link UpdaterData}.
     */
    public PackageLoader(UpdaterData updaterData, DownloadCache cache) {
        mUpdaterData = updaterData;
        mOverrideCache = cache;
}

/**
//Synthetic comment -- @@ -145,20 +184,12 @@
* after each source is finished loaded. In return the callback tells the loader
* whether to continue loading sources.
*/
    public void loadPackages(final ISourceLoadedCallback sourceLoadedCallback) {
try {
if (mUpdaterData == null) {
return;
}

mUpdaterData.getTaskFactory().start("Loading Sources", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
//Synthetic comment -- @@ -176,7 +207,7 @@

// get remote packages
boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();
                    loadRemoteAddonsList(monitor.createSubMonitor(1));

SdkSource[] sources = mUpdaterData.getSources().getAllSources();
try {
//Synthetic comment -- @@ -186,7 +217,7 @@
for (SdkSource source : sources) {
Package[] pkgs = source.getPackages();
if (pkgs == null) {
                                    source.load(getDownloadCache(),
subMonitor.createSubMonitor(1),
forceHttp);
pkgs = source.getPackages();
//Synthetic comment -- @@ -215,8 +246,7 @@
}

/**
     * Load packages, source by source using {@link #loadPackages(ISourceLoadedCallback)},
* and executes the given {@link IAutoInstallTask} on the current package list.
* That is for each package known, the install task is queried to find if
* the package is the one to be installed or updated.
//Synthetic comment -- @@ -248,8 +278,7 @@
final int installFlags,
final IAutoInstallTask installTask) {

        loadPackages(new ISourceLoadedCallback() {
List<Archive> mArchivesToInstall = new ArrayList<Archive>();
Map<Package, File> mInstallPaths = new HashMap<Package, File>();

//Synthetic comment -- @@ -367,4 +396,77 @@
}
});
}


    /**
     * Loads the remote add-ons list.
     */
    public void loadRemoteAddonsList(ITaskMonitor monitor) {

        if (mStateFetchRemoteAddonsList != 0) {
            return;
        }

        mUpdaterData.getTaskFactory().start("Load Add-ons List", monitor, new ITask() {
            @Override
            public void run(ITaskMonitor subMonitor) {
                loadRemoteAddonsListInTask(subMonitor);
            }
        });
    }

    private void loadRemoteAddonsListInTask(ITaskMonitor monitor) {
        mStateFetchRemoteAddonsList = -1;

        String url = SdkAddonsListConstants.URL_ADDON_LIST;

        // We override SdkRepoConstants.URL_GOOGLE_SDK_SITE if this is defined
        String baseUrl = System.getenv("SDK_TEST_BASE_URL");            //$NON-NLS-1$
        if (baseUrl != null) {
            if (baseUrl.length() > 0 && baseUrl.endsWith("/")) {        //$NON-NLS-1$
                if (url.startsWith(SdkRepoConstants.URL_GOOGLE_SDK_SITE)) {
                    url = baseUrl + url.substring(SdkRepoConstants.URL_GOOGLE_SDK_SITE.length());
                }
            } else {
                monitor.logError("Ignoring invalid SDK_TEST_BASE_URL: %1$s", baseUrl);  //$NON-NLS-1$
            }
        }

        if (mUpdaterData.getSettingsController().getForceHttp()) {
            url = url.replaceAll("https://", "http://");    //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Hook to bypass loading 3rd party addons lists.
        boolean fetch3rdParties = System.getenv("SDK_SKIP_3RD_PARTIES") == null;

        AddonsListFetcher fetcher = new AddonsListFetcher();
        Site[] sites = fetcher.fetch(url, getDownloadCache(), monitor);
        if (sites != null) {
            SdkSources sources = mUpdaterData.getSources();
            sources.removeAll(SdkSourceCategory.ADDONS_3RD_PARTY);

            if (fetch3rdParties) {
                for (Site s : sites) {
                    sources.add(SdkSourceCategory.ADDONS_3RD_PARTY,
                                 new SdkAddonSource(s.getUrl(), s.getUiName()));
                }
            }

            sources.notifyChangeListeners();

            mStateFetchRemoteAddonsList = 1;
        }

        monitor.setDescription("Fetched Add-ons List successfully");
    }

    /**
     * Returns the {@link DownloadCache} to use.
     *
     * @return Returns {@link #mOverrideCache} if not null; otherwise returns the
     *  one from {@link UpdaterData} is used instead.
     */
    private DownloadCache getDownloadCache() {
        return mOverrideCache != null ? mOverrideCache : mUpdaterData.getDownloadCache();
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 8e1cbb3..9e45748 100755

//Synthetic comment -- @@ -48,17 +48,11 @@
* so that we can test it using head-less unit tests.
*/
class PackagesDiffLogic {
private final UpdaterData mUpdaterData;
private boolean mFirstLoadComplete = true;

public PackagesDiffLogic(UpdaterData updaterData) {
mUpdaterData = updaterData;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index 51543e8..cf5cb55 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.repository.sdkman2;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -129,6 +131,7 @@
private final SdkInvocationContext mContext;
private final UpdaterData mUpdaterData;
private final PackagesDiffLogic mDiffLogic;

private boolean mDisplayArchives = false;
private boolean mOperationPending;

//Synthetic comment -- @@ -169,7 +172,8 @@

public void performFirstLoad() {
// Initialize the package list the first time the page is shown.
        loadPackages(true /*useLocalCache*/);
        loadPackages(false /*useLocalCache*/);
}

@SuppressWarnings("unused")
//Synthetic comment -- @@ -576,11 +580,24 @@
loadPackages();
}

    /**
     * Performs a "normal" reload of the package information, use the default download
     * cache and refreshing it as needed.
     */
private void loadPackages() {
        loadPackages(false /*useLocalCache*/);
}

    /**
     * Performs a reload of the package information.
     *
     * @param useLocalCache When true, the {@link PackageLoader} is switched to use
     *  a specific {@link DownloadCache} using the {@link Strategy#ONLY_CACHE}, meaning
     *  it will only use data from the local cache. It will not try to fetch or refresh
     *  manifests. This is used once the very first time the sdk manager window opens
     *  and is typically followed by a regular load with refresh.
     */
    private void loadPackages(final boolean useLocalCache) {
if (mUpdaterData == null) {
return;
}
//Synthetic comment -- @@ -598,10 +615,17 @@
getImage(displaySortByApi ? ICON_SORT_BY_API : ICON_SORT_BY_SOURCE));
}

        PackageLoader packageLoader = null;
        if (useLocalCache) {
            packageLoader =
                new PackageLoader(mUpdaterData, new DownloadCache(Strategy.ONLY_CACHE));
        } else {
            packageLoader = mUpdaterData.getPackageLoader();
        }
        assert packageLoader != null;

mDiffLogic.updateStart();
        packageLoader.loadPackages(new ISourceLoadedCallback() {
@Override
public boolean onUpdateSource(SdkSource source, Package[] newPackages) {
// This runs in a thread and must not access UI directly.
//Synthetic comment -- @@ -640,7 +664,9 @@
refreshViewerInput();
}

                            if (!useLocalCache &&
                                    mDiffLogic.isFirstLoadComplete() &&
                                    !mGroupPackages.isDisposed()) {
// At the end of the first load, if nothing is selected then
// automatically select all new and update packages.
Object[] checked = mTreeViewer.getCheckedElements();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index 8444f9f..3448852 100755

//Synthetic comment -- @@ -100,6 +100,13 @@
/**
* Starts the task and block till it's either finished or canceled.
* This can be called from a non-UI thread safely.
     * <p/>
     * When a task is started from within a monitor, it reuses the thread
     * from the parent. Otherwise it starts a new thread and runs it own
     * UI loop. This means the task can perform UI operations using
     * {@link Display#asyncExec(Runnable)}.
     * <p/>
     * In either case, the method only returns when the task has finished.
*/
public void startTask(
final String title,







