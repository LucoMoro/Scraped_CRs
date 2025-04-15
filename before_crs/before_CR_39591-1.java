/*SDK Manager fix: override in-memory sources when checking for updates.

An issue that showed up with the cache mechanism in Tools r20
is that users are not notified of tools updates unless they
clear their cache. The issue is that the SDK Manager first tries
to load an existing cache for a fast startup and then performs
a network check. However it doesn't clear the sources already
memory loaded in memory and thus skips the actual network check
and simply uses the first loaded result. This changes it so that
the network check actually happens even if there's a source
loaded in memory.

Change-Id:I3fde77b9aec8ccbd1cf66f79f99f8c5f4e84d900*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 2f154a6..e77c7d4 100755

//Synthetic comment -- @@ -79,9 +79,11 @@
private static final String KEY_URL = "URL";                        //$NON-NLS-1$

/** Prefix of binary files stored in the {@link SdkConstants#FD_CACHE} directory. */
    private final static String BIN_FILE_PREFIX = "sdkbin-";            //$NON-NLS-1$
/** Prefix of meta info files stored in the {@link SdkConstants#FD_CACHE} directory. */
    private final static String INFO_FILE_PREFIX = "sdkinf-";           //$NON-NLS-1$

/**
* Minimum time before we consider a cached entry is potentially stale.
//Synthetic comment -- @@ -226,6 +228,31 @@
}

/**
* Returns the directory to be used as a cache.
* Creates it if necessary.
* Makes it possible to disable or override the cache location in unit tests.
//Synthetic comment -- @@ -735,12 +762,13 @@
leaf = leaf.replaceAll("__+", "_");

leaf = hash + '-' + leaf;
        int n = 64 - BIN_FILE_PREFIX.length();
if (leaf.length() > n) {
leaf = leaf.substring(0, n);
}

        return BIN_FILE_PREFIX + leaf;
}

private String getInfoFilename(String cacheFilename) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackageLoader.java
//Synthetic comment -- index 983c94d..822240c 100755

//Synthetic comment -- @@ -70,7 +70,7 @@

/**
* Interface for the callback called by
     * {@link PackageLoader#loadPackages(ISourceLoadedCallback)}.
* <p/>
* After processing each source, the package loader calls {@link #onUpdateSource}
* with the list of packages found in that source.
//Synthetic comment -- @@ -184,8 +184,18 @@
* The caller is responsible to accumulate the packages given to the callback
* after each source is finished loaded. In return the callback tells the loader
* whether to continue loading sources.
*/
    public void loadPackages(final ISourceLoadedCallback sourceLoadedCallback) {
try {
if (mUpdaterData == null) {
return;
//Synthetic comment -- @@ -218,7 +228,7 @@
subMonitor.setProgressMax(sources.length);
for (SdkSource source : sources) {
Package[] pkgs = source.getPackages();
                                if (pkgs == null) {
source.load(getDownloadCache(),
subMonitor.createSubMonitor(1),
forceHttp);
//Synthetic comment -- @@ -248,7 +258,8 @@
}

/**
     * Load packages, source by source using {@link #loadPackages(ISourceLoadedCallback)},
* and executes the given {@link IAutoInstallTask} on the current package list.
* That is for each package known, the install task is queried to find if
* the package is the one to be installed or updated.
//Synthetic comment -- @@ -280,7 +291,7 @@
final int installFlags,
final IAutoInstallTask installTask) {

        loadPackages(new ISourceLoadedCallback() {
List<Archive> mArchivesToInstall = new ArrayList<Archive>();
Map<Package, File> mInstallPaths = new HashMap<Package, File>();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index 0b6d725..5ea9bb0 100755

//Synthetic comment -- @@ -174,12 +174,12 @@
// First a package loader is created that only checks
// the local cache xml files. It populates the package
// list based on what the client got last, essentially.
        loadPackages(true /*useLocalCache*/);

// Next a regular package loader is created that will
// respect the expiration and refresh parameters of the
// download cache.
        loadPackages(false /*useLocalCache*/);
}

@SuppressWarnings("unused")
//Synthetic comment -- @@ -591,7 +591,7 @@
* cache and refreshing strategy as needed.
*/
private void loadPackages() {
        loadPackages(false /*useLocalCache*/);
}

/**
//Synthetic comment -- @@ -603,7 +603,7 @@
*  manifests. This is used once the very first time the sdk manager window opens
*  and is typically followed by a regular load with refresh.
*/
    private void loadPackages(final boolean useLocalCache) {
if (mUpdaterData == null) {
return;
}
//Synthetic comment -- @@ -635,7 +635,7 @@
assert packageLoader != null;

mDiffLogic.updateStart();
        packageLoader.loadPackages(new ISourceLoadedCallback() {
@Override
public boolean onUpdateSource(SdkSource source, Package[] newPackages) {
// This runs in a thread and must not access UI directly.







