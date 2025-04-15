/*Refactoring: move SDK Updater core code into sdklib.

This requires the 2 following changes:
in       sdk.git:I79742d366b176cee2443bbed1f96dc253e6c74bbin tools/swt.git:I97c5874e6b5dcb5d6c0ca25ca921a291c6330fccChange-Id:I507a2bebe348fae598bc6e6fe24af3c5bf78acf0*/




//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ArchiveInfo.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ArchiveInfo.java
new file mode 100755
//Synthetic comment -- index 0000000..8068d4b

//Synthetic comment -- @@ -0,0 +1,160 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an archive that we want to install.
 * Note that the installer deals with archives whereas the user mostly sees packages
 * but as far as we are concerned for installation there's a 1-to-1 mapping.
 * <p/>
 * A new archive is always a remote archive that needs to be downloaded and then
 * installed. It can replace an existing local one. It can also depends on another
 * (new or local) archive, which means the dependent archive needs to be successfully
 * installed first. Finally this archive can also be a dependency for another one.
 * <p/>
 * The accepted and rejected flags are used by {@code SdkUpdaterChooserDialog} to follow
 * user choices. The installer should never install something that is not accepted.
 * <p/>
 * <em>Note</em>: There is currently no logic to support more than one level of
 * dependency, either here or in the {@code SdkUpdaterChooserDialog}, since we currently
 * have no need for it.
 *
 * @see ArchiveInfo#ArchiveInfo(Archive, Archive, ArchiveInfo[])
 */
public class ArchiveInfo extends ArchiveReplacement implements Comparable<ArchiveInfo> {

    private final ArchiveInfo[] mDependsOn;
    private final ArrayList<ArchiveInfo> mDependencyFor = new ArrayList<ArchiveInfo>();
    private boolean mAccepted;
    private boolean mRejected;

    /**
     * Creates a new replacement where the {@code newArchive} will replace the
     * currently installed {@code replaced} archive.
     * When {@code newArchive} is not intended to replace anything (e.g. because
     * the user is installing a new package not present on her system yet), then
     * {@code replace} shall be null.
     *
     * @param newArchive A "new archive" to be installed. This is always an archive
     *          that comes from a remote site. This <em>may</em> be null.
     * @param replaced An optional local archive that the new one will replace.
     *          Can be null if this archive does not replace anything.
     * @param dependsOn An optional new or local dependency, that is an archive that
     *          <em>this</em> archive depends upon. In other words, we can only install
     *          this archive if the dependency has been successfully installed. It also
     *          means we need to install the dependency first. Can be null or empty.
     *          However it cannot contain nulls.
     */
    public ArchiveInfo(Archive newArchive, Archive replaced, ArchiveInfo[] dependsOn) {
        super(newArchive, replaced);
        mDependsOn = dependsOn;
    }

    /**
     * Returns an optional new or local dependency, that is an archive that <em>this</em>
     * archive depends upon. In other words, we can only install this archive if the
     * dependency has been successfully installed. It also means we need to install the
     * dependency first.
     * <p/>
     * This array can be null or empty. It can't contain nulls though.
     */
    public ArchiveInfo[] getDependsOn() {
        return mDependsOn;
    }

    /**
     * Returns true if this new archive is a dependency for <em>another</em> one that we
     * want to install.
     */
    public boolean isDependencyFor() {
        return mDependencyFor.size() > 0;
    }

    /**
     * Adds an {@link ArchiveInfo} for which <em>this</em> package is a dependency.
     * This means the package added here depends on this package.
     */
    public ArchiveInfo addDependencyFor(ArchiveInfo dependencyFor) {
        if (!mDependencyFor.contains(dependencyFor)) {
            mDependencyFor.add(dependencyFor);
        }

        return this;
    }

    /**
     * Returns the list of {@link ArchiveInfo} for which <em>this</em> package is a dependency.
     * This means the packages listed here depend on this package.
     * <p/>
     * Implementation detail: this is the internal mutable list. Callers should not modify it.
     * This list can be empty but is never null.
     */
    public Collection<ArchiveInfo> getDependenciesFor() {
        return mDependencyFor;
    }

    /**
     * Sets whether this archive was accepted (either manually by the user or
     * automatically if it doesn't have a license) for installation.
     */
    public void setAccepted(boolean accepted) {
        mAccepted = accepted;
    }

    /**
     * Returns whether this archive was accepted (either manually by the user or
     * automatically if it doesn't have a license) for installation.
     */
    public boolean isAccepted() {
        return mAccepted;
    }

    /**
     * Sets whether this archive was rejected manually by the user.
     * An archive can neither accepted nor rejected.
     */
    public void setRejected(boolean rejected) {
        mRejected = rejected;
    }

    /**
     * Returns whether this archive was rejected manually by the user.
     * An archive can neither accepted nor rejected.
     */
    public boolean isRejected() {
        return mRejected;
    }

    /**
     * ArchiveInfos are compared using ther "new archive" ordering.
     *
     * @see Archive#compareTo(Archive)
     */
    @Override
    public int compareTo(ArchiveInfo rhs) {
        if (getNewArchive() != null && rhs != null) {
            return getNewArchive().compareTo(rhs.getNewArchive());
        }
        return 0;
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ISettingsPage.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/ISettingsPage.java
new file mode 100755
//Synthetic comment -- index 0000000..a1a4e76

//Synthetic comment -- @@ -0,0 +1,108 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.internal.repository.DownloadCache;

import java.net.URL;
import java.util.Properties;

/**
 * Interface that a settings page must implement.
 */
public interface ISettingsPage {

    /**
     * Java system setting picked up by {@link URL} for http proxy port.
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
     * Setting to enabling previews in the package list
     * Type: Boolean.
     * Default: False.
     */
    public static final String KEY_ENABLE_PREVIEWS = "sdkman.enable.previews";   //$NON-NLS-1$

    /**
     * Setting to set the density of the monitor.
     * Type: Integer.
     * Default: -1
     */
    public static final String KEY_MONITOR_DENSITY = "sdkman.monitor.density"; //$NON-NLS-1$

    /** Loads settings from the given {@link Properties} container and update the page UI. */
    public abstract void loadSettings(Properties inSettings);

    /** Called by the application to retrieve settings from the UI and store them in
     * the given {@link Properties} container. */
    public abstract void retrieveSettings(Properties outSettings);

    /**
     * Called by the application to give a callback that the page should invoke when
     * settings have changed.
     */
    public abstract void setOnSettingsChanged(SettingsChangedCallback settingsChangedCallback);

    /**
     * Callback used to notify the application that settings have changed and need to be
     * applied.
     */
    public interface SettingsChangedCallback {
        /**
         * Invoked by the settings page when settings have changed and need to be
         * applied. The application will call {@link ISettingsPage#retrieveSettings(Properties)}
         * and apply the new settings.
         */
        public abstract void onSettingsChanged(ISettingsPage page);
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/IUpdaterData.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/IUpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..f25dde5

//Synthetic comment -- @@ -0,0 +1,44 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.utils.ILogger;


/**
 * Interface used to retrieve some parameters from an {@link UpdaterData} instance.
 * Useful mostly for unit tests purposes.
 */
public interface IUpdaterData {

    public abstract ITaskFactory getTaskFactory();

    public abstract ILogger getSdkLog();

    public abstract DownloadCache getDownloadCache();

    public abstract SdkManager getSdkManager();

    public abstract AvdManager getAvdManager();

    public abstract SettingsController getSettingsController();

}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PackageLoader.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PackageLoader.java
new file mode 100755
//Synthetic comment -- index 0000000..48c9a7b

//Synthetic comment -- @@ -0,0 +1,497 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.annotations.NonNull;
import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkAddonSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.sources.SdkSysImgSource;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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
     * {@link PackageLoader#loadPackages(boolean, ISourceLoadedCallback)}.
     * <p/>
     * After processing each source, the package loader calls {@link #onUpdateSource}
     * with the list of packages found in that source.
     * By returning true from {@link #onUpdateSource}, the client tells the loader to
     * continue and process the next source. By returning false, it tells to stop loading.
     * <p/>
     * The {@link #onLoadCompleted()} method is guaranteed to be called at the end, no
     * matter how the loader stopped, so that the client can clean up or perform any
     * final action.
     */
    public interface ISourceLoadedCallback {
        /**
         * After processing each source, the package loader calls this method with the
         * list of packages found in that source.
         * By returning true from {@link #onUpdateSource}, the client tells
         * the loader to continue and process the next source.
         * By returning false, it tells to stop loading.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients which
         * try to access any UI widgets must wrap their calls into
         * {@code Display.syncExec(Runnable)} or {@code Display.asyncExec(Runnable)}.
         *
         * @param packages All the packages loaded from the source. Never null.
         * @return True if the load operation should continue, false if it should stop.
         */
        public boolean onUpdateSource(SdkSource source, Package[] packages);

        /**
         * This method is guaranteed to be called at the end, no matter how the
         * loader stopped, so that the client can clean up or perform any final action.
         */
        public void onLoadCompleted();
    }

    /**
     * Interface describing the task of installing a specific package.
     * For details on the operation,
     * see {@link PackageLoader#loadPackagesWithInstallTask(int, IAutoInstallTask)}.
     *
     * @see PackageLoader#loadPackagesWithInstallTask(int, IAutoInstallTask)
     */
    public interface IAutoInstallTask {
        /**
         * Invoked by the loader once a source has been loaded and its package
         * definitions are known. The method should return the {@code packages}
         * array and can modify it if necessary.
         * The loader will call {@link #acceptPackage(Package)} on all the packages returned.
         *
         * @param source The source of the packages. Null for the locally installed packages.
         * @param packages The packages found in the source.
         */
        public Package[] filterLoadedSource(SdkSource source, Package[] packages);

        /**
         * Called by the install task for every package available (new ones, updates as well
         * as existing ones that don't have a potential update.)
         * The method should return true if this is a package that should be installed.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@code Display.syncExec(Runnable)}
         * or {@code Display.asyncExec(Runnable)}.
         */
        public boolean acceptPackage(Package pkg);

        /**
         * Called when the accepted package has been installed, successfully or not.
         * If an already installed (aka existing) package has been accepted, this will
         * be called with a 'true' success and the actual install paths.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@code Display.syncExec(Runnable)}
         * or {@code Display.asyncExec(Runnable)}.
         */
        public void setResult(boolean success, Map<Package, File> installPaths);

        /**
         * Called when the task is done iterating and completed.
         */
        public void taskCompleted();
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

    public UpdaterData getUpdaterData() {
        return mUpdaterData;
    }

    /**
     * Runs a runnable on the UI thread.
     * The base implementation just runs the runnable right away.
     *
     * @param r Non-null runnable.
     */
    protected void runOnUiThread(@NonNull Runnable r) {
        r.run();
    }

    /**
     * Loads all packages from the remote repository.
     * This runs in an {@link ITask}. The call is blocking.
     * <p/>
     * The callback is called with each set of {@link PkgItem} found in each source.
     * The caller is responsible to accumulate the packages given to the callback
     * after each source is finished loaded. In return the callback tells the loader
     * whether to continue loading sources.
     * <p/>
     * Normally this method doesn't access the remote source if it's already
     * been loaded in the in-memory source (e.g. don't fetch twice).
     *
     * @param overrideExisting Set this to true  when the caller wants to
     *          check for updates and discard any existing source already
     *          loaded in memory. It should be false for normal use.
     * @param sourceLoadedCallback The callback to invoke for each loaded source.
     */
    public void loadPackages(
            final boolean overrideExisting,
            final ISourceLoadedCallback sourceLoadedCallback) {
        try {
            if (mUpdaterData == null) {
                return;
            }

            mUpdaterData.getTaskFactory().start("Loading Sources", new ITask() {
                @Override
                public void run(ITaskMonitor monitor) {
                    monitor.setProgressMax(10);

                    // get local packages and offer them to the callback
                    Package[] localPkgs =
                        mUpdaterData.getInstalledPackages(monitor.createSubMonitor(1));
                    if (localPkgs == null) {
                        localPkgs = new Package[0];
                    }
                    if (!sourceLoadedCallback.onUpdateSource(null, localPkgs)) {
                        return;
                    }

                    // get remote packages
                    boolean forceHttp =
                        mUpdaterData.getSettingsController().getSettings().getForceHttp();
                    loadRemoteAddonsList(monitor.createSubMonitor(1));

                    SdkSource[] sources = mUpdaterData.getSources().getAllSources();
                    try {
                        if (sources != null && sources.length > 0) {
                            ITaskMonitor subMonitor = monitor.createSubMonitor(8);
                            subMonitor.setProgressMax(sources.length);
                            for (SdkSource source : sources) {
                                Package[] pkgs = source.getPackages();
                                if (pkgs == null || overrideExisting) {
                                    source.load(getDownloadCache(),
                                            subMonitor.createSubMonitor(1),
                                            forceHttp);
                                    pkgs = source.getPackages();
                                }
                                if (pkgs == null) {
                                    continue;
                                }

                                // Notify the callback a new source has finished loading.
                                // If the callback requests so, stop right away.
                                if (!sourceLoadedCallback.onUpdateSource(source, pkgs)) {
                                    return;
                                }
                            }
                        }
                    } catch(Exception e) {
                        monitor.logError("Loading source failed: %1$s", e.toString());
                    } finally {
                        monitor.setDescription("Done loading packages.");
                    }
                }
            });
        } finally {
            sourceLoadedCallback.onLoadCompleted();
        }
    }

    /**
     * Load packages, source by source using
     * {@link #loadPackages(boolean, ISourceLoadedCallback)},
     * and executes the given {@link IAutoInstallTask} on the current package list.
     * That is for each package known, the install task is queried to find if
     * the package is the one to be installed or updated.
     * <p/>
     * - If an already installed package is accepted by the task, it is returned. <br/>
     * - If a new package (remotely available but not installed locally) is accepted,
     * the user will be <em>prompted</em> for permission to install it. <br/>
     * - If an existing package has updates, the install task will be accept if it
     * accepts one of the updating packages, and if yes the the user will be
     * <em>prompted</em> for permission to install it. <br/>
     * <p/>
     * Only one package can be accepted, after which the task is completed.
     * There is no direct return value, {@link IAutoInstallTask#setResult} is called on the
     * result of the accepted package.
     * When the task is completed, {@link IAutoInstallTask#taskCompleted()} is called.
     * <p/>
     * The call is blocking. Although the name says "Task", this is not an {@link ITask}
     * running in its own thread but merely a synchronous call.
     *
     * @param installFlags Flags for installation such as
     *  {@link UpdaterData#TOOLS_MSG_UPDATED_FROM_ADT}.
     * @param installTask The task to perform.
     */
    public void loadPackagesWithInstallTask(
            final int installFlags,
            final IAutoInstallTask installTask) {

        loadPackages(false /*overrideExisting*/, new ISourceLoadedCallback() {
            List<Archive> mArchivesToInstall = new ArrayList<Archive>();
            Map<Package, File> mInstallPaths = new HashMap<Package, File>();

            @Override
            public boolean onUpdateSource(SdkSource source, Package[] packages) {
                packages = installTask.filterLoadedSource(source, packages);
                if (packages == null || packages.length == 0) {
                    // Tell loadPackages() to process the next source.
                    return true;
                }

                for (Package pkg : packages) {
                    if (pkg.isLocal()) {
                        // This is a local (aka installed) package
                        if (installTask.acceptPackage(pkg)) {
                            // If the caller is accepting an installed package,
                            // return a success and give the package's install path
                            Archive[] a = pkg.getArchives();
                            // an installed package should have one local compatible archive
                            if (a.length == 1 && a[0].isCompatible()) {
                                mInstallPaths.put(pkg, new File(a[0].getLocalOsPath()));
                            }
                        }

                    } else {
                        // This is a remote package
                        if (installTask.acceptPackage(pkg)) {
                            // The caller is accepting this remote package. We'll install it.
                            for (Archive archive : pkg.getArchives()) {
                                if (archive.isCompatible()) {
                                    mArchivesToInstall.add(archive);
                                    break;
                                }
                            }
                        }
                    }
                }

                // Tell loadPackages() to process the next source.
                return true;
            }

            @Override
            public void onLoadCompleted() {
                if (!mArchivesToInstall.isEmpty()) {
                    installArchives(mArchivesToInstall);
                }
                if (mInstallPaths == null) {
                    installTask.setResult(false, null);
                } else {
                    installTask.setResult(true, mInstallPaths);
                }

                installTask.taskCompleted();
            }

            /**
             * Shows the UI of the install selector.
             * If the package is then actually installed, refresh the local list and
             * notify the install task of the installation path.
             *
             * @param archivesToInstall The archives to install.
             */
            private void installArchives(final List<Archive> archivesToInstall) {
                // Actually install the new archives that we just found.
                // This will display some UI so we need a shell's sync exec.

                final List<Archive> installedArchives = new ArrayList<Archive>();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Archive> archives =
                            mUpdaterData.updateOrInstallAll_WithGUI(
                                archivesToInstall,
                                true /* includeObsoletes */,
                                installFlags);

                        if (archives != null) {
                            installedArchives.addAll(archives);
                        }
                    }
                });

                if (installedArchives.isEmpty()) {
                    // We failed to install anything.
                    mInstallPaths = null;
                    return;
                }

                // The local package list has changed, make sure to refresh it
                mUpdaterData.getSdkManager().reloadSdk(mUpdaterData.getSdkLog());
                mUpdaterData.getLocalSdkParser().clearPackages();
                final Package[] localPkgs = mUpdaterData.getInstalledPackages(
                        new NullTaskMonitor(mUpdaterData.getSdkLog()));

                // Scan the installed package list to find the install paths.
                for (Archive installedArchive : installedArchives) {
                    Package pkg = installedArchive.getParentPackage();

                    for (Package localPkg : localPkgs) {
                        if (localPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
                            Archive[] localArchive = localPkg.getArchives();
                            if (localArchive.length == 1 && localArchive[0].isCompatible()) {
                                mInstallPaths.put(
                                    localPkg,
                                    new File(localArchive[0].getLocalOsPath()));
                            }
                        }
                    }
                }
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

        if (mUpdaterData.getSettingsController().getSettings().getForceHttp()) {
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
                    switch (s.getType()) {
                    case ADDON_SITE:
                        sources.add(SdkSourceCategory.ADDONS_3RD_PARTY,
                                new SdkAddonSource(s.getUrl(), s.getUiName()));
                        break;
                    case SYS_IMG_SITE:
                        sources.add(SdkSourceCategory.ADDONS_3RD_PARTY,
                                new SdkSysImgSource(s.getUrl(), s.getUiName()));
                        break;
                    }
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








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/PkgItem.java
new file mode 100755
//Synthetic comment -- index 0000000..9e0912e

//Synthetic comment -- @@ -0,0 +1,277 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.sources.SdkSource;

/**
 * A {@link PkgItem} represents one main {@link Package} combined with its state
 * and an optional update package.
 * <p/>
 * The main package is final and cannot change since it's what "defines" this PkgItem.
 * The state or update package can change later.
 */
public class PkgItem implements Comparable<PkgItem> {
    private final PkgState mState;
    private final Package mMainPkg;
    private Package mUpdatePkg;
    private boolean mChecked;

    /**
     * The state of the a given {@link PkgItem}, that is the relationship between
     * a given remote package and the local repository.
     */
    public enum PkgState {
        // Implementation detail: if this is changed then PackageDiffLogic#STATES
        // and PackageDiffLogic#processSource() need to be changed accordingly.

        /**
         * Package is locally installed and may or may not have an update.
         */
        INSTALLED,

        /**
         * There's a new package available on the remote site that isn't installed locally.
         */
        NEW
    }

    /**
     * Create a new {@link PkgItem} for this main package.
     * The main package is final and cannot change since it's what "defines" this PkgItem.
     * The state or update package can change later.
     */
    public PkgItem(Package mainPkg, PkgState state) {
        mMainPkg = mainPkg;
        mState = state;
        assert mMainPkg != null;
    }

    public boolean isObsolete() {
        return mMainPkg.isObsolete();
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public Package getUpdatePkg() {
        return mUpdatePkg;
    }

    public boolean hasUpdatePkg() {
        return mUpdatePkg != null;
    }

    public String getName() {
        return mMainPkg.getListDescription();
    }

    public FullRevision getRevision() {
        return mMainPkg.getRevision();
    }

    public String getDescription() {
        return mMainPkg.getDescription();
    }

    public Package getMainPackage() {
        return mMainPkg;
    }

    public PkgState getState() {
        return mState;
    }

    public SdkSource getSource() {
        return mMainPkg.getParentSource();
    }

    public int getApi() {
        return mMainPkg instanceof IAndroidVersionProvider ?
                ((IAndroidVersionProvider) mMainPkg).getAndroidVersion().getApiLevel() :
                    -1;
    }

    public Archive[] getArchives() {
        return mMainPkg.getArchives();
    }

    @Override
    public int compareTo(PkgItem pkg) {
        return getMainPackage().compareTo(pkg.getMainPackage());
    }

    /**
     * Returns true if this package or its updating packages contains
     * the exact given archive.
     * Important: This compares object references, not object equality.
     */
    public boolean hasArchive(Archive archive) {
        if (mMainPkg.hasArchive(archive)) {
            return true;
        }
        if (mUpdatePkg != null && mUpdatePkg.hasArchive(archive)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the main package has at least one archive
     * compatible with the current platform.
     */
    public boolean hasCompatibleArchive() {
        return mMainPkg.hasCompatibleArchive();
    }

    /**
     * Checks whether the main packages are of the same type and are
     * not an update of each other and have the same revision number.
     */
    public boolean isSameMainPackageAs(Package pkg) {
        if (mMainPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
            // package revision numbers must match
            return mMainPkg.getRevision().equals(pkg.getRevision());
        }
        return false;
    }

    /**
     * Checks whether the update packages are of the same type and are
     * not an update of each other and have the same revision numbers.
     */
    public boolean isSameUpdatePackageAs(Package pkg) {
        if (mUpdatePkg != null && mUpdatePkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
            // package revision numbers must match
            return mUpdatePkg.getRevision().equals(pkg.getRevision());
        }
        return false;
    }

    /**
     * Checks whether too {@link PkgItem} are the same.
     * This checks both items have the same state, both main package are similar
     * and that they have the same updating packages.
     */
    public boolean isSameItemAs(PkgItem item) {
        if (this == item) {
            return true;
        }
        boolean same = this.mState == item.mState;
        if (same) {
            same = isSameMainPackageAs(item.getMainPackage());
        }

        if (same) {
            // check updating packages are the same
            Package p1 = this.mUpdatePkg;
            Package p2 = item.getUpdatePkg();
            same = (p1 == p2) || (p1 == null && p2 == null) || (p1 != null && p2 != null);

            if (same && p1 != null) {
                same = p1.canBeUpdatedBy(p2) == UpdateInfo.NOT_UPDATE;
            }
        }

        return same;
    }

    /**
     * Equality is defined as {@link #isSameItemAs(PkgItem)}: state, main package
     * and update package must be the similar.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PkgItem) && this.isSameItemAs((PkgItem) obj);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mState     == null) ? 0 : mState.hashCode());
        result = prime * result + ((mMainPkg   == null) ? 0 : mMainPkg.hashCode());
        result = prime * result + ((mUpdatePkg == null) ? 0 : mUpdatePkg.hashCode());
        return result;
    }

    /**
     * Check whether the 'pkg' argument is an update for this package.
     * If it is, record it as an updating package.
     * If there's already an updating package, only keep the most recent update.
     * Returns true if it is update (even if there was already an update and this
     * ended up not being the most recent), false if incompatible or not an update.
     *
     * This should only be used for installed packages.
     */
    public boolean mergeUpdate(Package pkg) {
        if (mUpdatePkg == pkg) {
            return true;
        }
        if (mMainPkg.canBeUpdatedBy(pkg) == UpdateInfo.UPDATE) {
            if (mUpdatePkg == null) {
                mUpdatePkg = pkg;
            } else if (mUpdatePkg.canBeUpdatedBy(pkg) == UpdateInfo.UPDATE) {
                // If we have more than one, keep only the most recent update
                mUpdatePkg = pkg;
            }
            return true;
        }

        return false;
    }

    public void removeUpdate() {
        mUpdatePkg = null;
    }

    /** Returns a string representation of this item, useful when debugging. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('<');

        if (mChecked) {
            sb.append(" * "); //$NON-NLS-1$
        }

        sb.append(mState.toString());

        if (mMainPkg != null) {
            sb.append(", pkg:"); //$NON-NLS-1$
            sb.append(mMainPkg.toString());
        }

        if (mUpdatePkg != null) {
            sb.append(", updated by:"); //$NON-NLS-1$
            sb.append(mUpdatePkg.toString());
        }

        sb.append('>');
        return sb.toString();
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterLogic.java
new file mode 100755
//Synthetic comment -- index 0000000..ffe8fa0

//Synthetic comment -- @@ -0,0 +1,1477 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.AddonPackage;
import com.android.sdklib.internal.repository.packages.DocPackage;
import com.android.sdklib.internal.repository.packages.ExtraPackage;
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.internal.repository.packages.IAndroidVersionProvider;
import com.android.sdklib.internal.repository.packages.IExactApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.packages.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.packages.IMinToolsDependency;
import com.android.sdklib.internal.repository.packages.IPlatformDependency;
import com.android.sdklib.internal.repository.packages.MinToolsPackage;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.UpdateInfo;
import com.android.sdklib.internal.repository.packages.PlatformPackage;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.SamplePackage;
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The logic to compute which packages to install, based on the choices
 * made by the user. This adds required packages as needed.
 * <p/>
 * When the user doesn't provide a selection, looks at local package to find
 * those that can be updated and compute dependencies too.
 */
public class SdkUpdaterLogic {

    private final IUpdaterData mUpdaterData;

    public SdkUpdaterLogic(IUpdaterData updaterData) {
        mUpdaterData = updaterData;
    }

    /**
     * Retrieves an unfiltered list of all remote archives.
     * The archives are guaranteed to be compatible with the current platform.
     */
    public List<ArchiveInfo> getAllRemoteArchives(
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        List<Package> remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getAllSources();
        fetchRemotePackages(remotePkgs, remoteSources);

        ArrayList<Archive> archives = new ArrayList<Archive>();
        for (Package remotePkg : remotePkgs) {
            // Only look for non-obsolete updates unless requested to include them
            if (includeAll || !remotePkg.isObsolete()) {
                // Found a suitable update. Only accept the remote package
                // if it provides at least one compatible archive

                addArchives:
                for (Archive a : remotePkg.getArchives()) {
                    if (a.isCompatible()) {

                        // If we're trying to add a package for revision N,
                        // make sure we don't also have a package for revision N-1.
                        for (int i = archives.size() - 1; i >= 0; i--) {
                            Package pkgFound = archives.get(i).getParentPackage();
                            if (pkgFound.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                                // This package can update one we selected earlier.
                                // Remove the one that can be updated by this new one.
                                archives.remove(i);
                            } else if (remotePkg.canBeUpdatedBy(pkgFound) == UpdateInfo.UPDATE) {
                                // There is a package in the list that is already better
                                // than the one we want to add, so don't add it.
                                break addArchives;
                            }
                        }

                        archives.add(a);
                        break;
                    }
                }
            }
        }

        ArrayList<ArchiveInfo> result = new ArrayList<ArchiveInfo>();

        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        for (Archive a : archives) {
            insertArchive(a,
                    result,
                    archives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    false /*automated*/);
        }

        return result;
    }

    /**
     * Compute which packages to install by taking the user selection
     * and adding required packages as needed.
     *
     * When the user doesn't provide a selection, looks at local packages to find
     * those that can be updated and compute dependencies too.
     */
    public List<ArchiveInfo> computeUpdates(
            Collection<Archive> selectedArchives,
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        List<ArchiveInfo> archives = new ArrayList<ArchiveInfo>();
        List<Package>   remotePkgs = new ArrayList<Package>();
        SdkSource[] remoteSources = sources.getAllSources();

        // Create ArchiveInfos out of local (installed) packages.
        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        // If we do not have a specific list of archives to install (that is the user
        // selected "update all" rather than request specific packages), then we try to
        // find updates based on the *existing* packages.
        if (selectedArchives == null) {
            selectedArchives = findUpdates(
                    localArchives,
                    remotePkgs,
                    remoteSources,
                    includeAll);
        }

        // Once we have a list of packages to install, we try to solve all their
        // dependencies by automatically adding them to the list of things to install.
        // This works on the list provided either by the user directly or the list
        // computed from potential updates.
        for (Archive a : selectedArchives) {
            insertArchive(a,
                    archives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    false /*automated*/);
        }

        // Finally we need to look at *existing* packages which are not being updated
        // and check if they have any missing dependencies and suggest how to fix
        // these dependencies.
        fixMissingLocalDependencies(
                archives,
                selectedArchives,
                remotePkgs,
                remoteSources,
                localArchives);

        return archives;
    }

    private double getRevisionRank(FullRevision rev) {
        int p = rev.isPreview() ? 999 : 999 - rev.getPreview();
        return  rev.getMajor() +
                rev.getMinor() / 1000.d +
                rev.getMicro() / 1000000.d +
                p              / 1000000000.d;
    }

    /**
     * Finds new packages that the user does not have in his/her local SDK
     * and adds them to the list of archives to install.
     * <p/>
     * The default is to only find "new" platforms, that is anything more
     * recent than the highest platform currently installed.
     * A side effect is that for an empty SDK install this will list *all*
     * platforms available (since there's no "highest" installed platform.)
     *
     * @param archives The in-out list of archives to install. Typically the
     *  list is not empty at first as it should contain any archives that is
     *  already scheduled for install. This method will add to the list.
     * @param sources The list of all sources, to fetch them as necessary.
     * @param localPkgs The list of all currently installed packages.
     * @param includeAll When true, this will list all platforms.
     * (included these lower than the highest installed one) as well as
     * all obsolete packages of these platforms.
     */
    public void addNewPlatforms(
            Collection<ArchiveInfo> archives,
            SdkSources sources,
            Package[] localPkgs,
            boolean includeAll) {

        // Create ArchiveInfos out of local (installed) packages.
        ArchiveInfo[] localArchives = createLocalArchives(localPkgs);

        // Find the highest platform installed
        double currentPlatformScore = 0;
        double currentSampleScore = 0;
        double currentAddonScore = 0;
        double currentDocScore = 0;
        HashMap<String, Double> currentExtraScore = new HashMap<String, Double>();
        if (!includeAll) {
            if (localPkgs != null) {
                for (Package p : localPkgs) {
                    double rev = getRevisionRank(p.getRevision());
                    int api = 0;
                    boolean isPreview = false;
                    if (p instanceof IAndroidVersionProvider) {
                        AndroidVersion vers = ((IAndroidVersionProvider) p).getAndroidVersion();
                        api = vers.getApiLevel();
                        isPreview = vers.isPreview();
                    }

                    // The score is 1000*api + (999 if preview) + rev
                    // This allows previews to rank above a non-preview and
                    // allows revisions to rank appropriately.
                    double score = api * 1000 + (isPreview ? 999 : 0) + rev;

                    if (p instanceof PlatformPackage) {
                        currentPlatformScore = Math.max(currentPlatformScore, score);
                    } else if (p instanceof SamplePackage) {
                        currentSampleScore = Math.max(currentSampleScore, score);
                    } else if (p instanceof AddonPackage) {
                        currentAddonScore = Math.max(currentAddonScore, score);
                    } else if (p instanceof ExtraPackage) {
                        currentExtraScore.put(((ExtraPackage) p).getPath(), score);
                    } else if (p instanceof DocPackage) {
                        currentDocScore = Math.max(currentDocScore, score);
                    }
                }
            }
        }

        SdkSource[] remoteSources = sources.getAllSources();
        ArrayList<Package> remotePkgs = new ArrayList<Package>();
        fetchRemotePackages(remotePkgs, remoteSources);

        Package suggestedDoc = null;

        for (Package p : remotePkgs) {
            // Skip obsolete packages unless requested to include them.
            if (p.isObsolete() && !includeAll) {
                continue;
            }

            double rev = getRevisionRank(p.getRevision());
            int api = 0;
            boolean isPreview = false;
            if (p instanceof IAndroidVersionProvider) {
                AndroidVersion vers = ((IAndroidVersionProvider) p).getAndroidVersion();
                api = vers.getApiLevel();
                isPreview = vers.isPreview();
            }

            double score = api * 1000 + (isPreview ? 999 : 0) + rev;

            boolean shouldAdd = false;
            if (p instanceof PlatformPackage) {
                shouldAdd = score > currentPlatformScore;
            } else if (p instanceof SamplePackage) {
                shouldAdd = score > currentSampleScore;
            } else if (p instanceof AddonPackage) {
                shouldAdd = score > currentAddonScore;
            } else if (p instanceof ExtraPackage) {
                String key = ((ExtraPackage) p).getPath();
                shouldAdd = !currentExtraScore.containsKey(key) ||
                    score > currentExtraScore.get(key).doubleValue();
            } else if (p instanceof DocPackage) {
                // We don't want all the doc, only the most recent one
                if (score > currentDocScore) {
                    suggestedDoc = p;
                    currentDocScore = score;
                }
            }

            if (shouldAdd) {
                // We should suggest this package for installation.
                for (Archive a : p.getArchives()) {
                    if (a.isCompatible()) {
                        insertArchive(a,
                                archives,
                                null /*selectedArchives*/,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }

            if (p instanceof PlatformPackage && (score >= currentPlatformScore)) {
                // We just added a new platform *or* we are visiting the highest currently
                // installed platform. In either case we want to make sure it either has
                // its own system image or that we provide one by default.
                PlatformPackage pp = (PlatformPackage) p;
                if (pp.getIncludedAbi() == null) {
                    for (Package p2 : remotePkgs) {
                        if (!(p2 instanceof SystemImagePackage) ||
                             (p2.isObsolete() && !includeAll)) {
                            continue;
                        }
                        SystemImagePackage sip = (SystemImagePackage) p2;
                        if (sip.getAndroidVersion().equals(pp.getAndroidVersion())) {
                            for (Archive a : sip.getArchives()) {
                                if (a.isCompatible()) {
                                    insertArchive(a,
                                            archives,
                                            null /*selectedArchives*/,
                                            remotePkgs,
                                            remoteSources,
                                            localArchives,
                                            true /*automated*/);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (suggestedDoc != null) {
            // We should suggest this package for installation.
            for (Archive a : suggestedDoc.getArchives()) {
                if (a.isCompatible()) {
                    insertArchive(a,
                            archives,
                            null /*selectedArchives*/,
                            remotePkgs,
                            remoteSources,
                            localArchives,
                            true /*automated*/);
                }
            }
        }
    }

    /**
     * Create a array of {@link ArchiveInfo} based on all local (already installed)
     * packages. The array is always non-null but may be empty.
     * <p/>
     * The local {@link ArchiveInfo} are guaranteed to have one non-null archive
     * that you can retrieve using {@link ArchiveInfo#getNewArchive()}.
     */
    public ArchiveInfo[] createLocalArchives(Package[] localPkgs) {

        if (localPkgs != null) {
            ArrayList<ArchiveInfo> list = new ArrayList<ArchiveInfo>();
            for (Package p : localPkgs) {
                // Only accept packages that have one compatible archive.
                // Local package should have 1 and only 1 compatible archive anyway.
                for (Archive a : p.getArchives()) {
                    if (a != null && a.isCompatible()) {
                        // We create an "installed" archive info to wrap the local package.
                        // Note that dependencies are not computed since right now we don't
                        // deal with more than one level of dependencies and installed archives
                        // are deemed implicitly accepted anyway.
                        list.add(new LocalArchiveInfo(a));
                    }
                }
            }

            return list.toArray(new ArchiveInfo[list.size()]);
        }

        return new ArchiveInfo[0];
    }

    /**
     * Find suitable updates to all current local packages.
     * <p/>
     * Returns a list of potential updates for *existing* packages. This does NOT solve
     * dependencies for the new packages.
     * <p/>
     * Always returns a non-null collection, which can be empty.
     */
    private Collection<Archive> findUpdates(
            ArchiveInfo[] localArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            boolean includeAll) {
        ArrayList<Archive> updates = new ArrayList<Archive>();

        fetchRemotePackages(remotePkgs, remoteSources);

        for (ArchiveInfo ai : localArchives) {
            Archive na = ai.getNewArchive();
            if (na == null) {
                continue;
            }
            Package localPkg = na.getParentPackage();

            for (Package remotePkg : remotePkgs) {
                // Only look for non-obsolete updates unless requested to include them
                if ((includeAll || !remotePkg.isObsolete()) &&
                        localPkg.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                    // Found a suitable update. Only accept the remote package
                    // if it provides at least one compatible archive

                    addArchives:
                    for (Archive a : remotePkg.getArchives()) {
                        if (a.isCompatible()) {

                            // If we're trying to add a package for revision N,
                            // make sure we don't also have a package for revision N-1.
                            for (int i = updates.size() - 1; i >= 0; i--) {
                                Package pkgFound = updates.get(i).getParentPackage();
                                if (pkgFound.canBeUpdatedBy(remotePkg) == UpdateInfo.UPDATE) {
                                    // This package can update one we selected earlier.
                                    // Remove the one that can be updated by this new one.
                                   updates.remove(i);
                                } else if (remotePkg.canBeUpdatedBy(pkgFound) ==
                                                UpdateInfo.UPDATE) {
                                    // There is a package in the list that is already better
                                    // than the one we want to add, so don't add it.
                                    break addArchives;
                                }
                            }

                            updates.add(a);
                            break;
                        }
                    }
                }
            }
        }

        return updates;
    }

    /**
     * Check all local archives which are NOT being updated and see if they
     * miss any dependency. If they do, try to fix that dependency by selecting
     * an appropriate package.
     */
    private void fixMissingLocalDependencies(
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        nextLocalArchive: for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            Package p = a == null ? null : a.getParentPackage();
            if (p == null) {
                continue;
            }

            // Is this local archive being updated?
            for (ArchiveInfo ai2 : outArchives) {
                if (ai2.getReplaced() == a) {
                    // this new archive will replace the current local one,
                    // so we don't have to care about fixing dependencies (since the
                    // new archive should already have had its dependencies resolved)
                    continue nextLocalArchive;
                }
            }

            // find dependencies for the local archive and add them as needed
            // to the outArchives collection.
            ArchiveInfo[] deps = findDependency(p,
                  outArchives,
                  selectedArchives,
                  remotePkgs,
                  remoteSources,
                  localArchives);

            if (deps != null) {
                // The already installed archive has a missing dependency, which we
                // just selected for install. Make sure we remember the dependency
                // so that we can enforce it later in the UI.
                for (ArchiveInfo aid : deps) {
                    aid.addDependencyFor(ai);
                }
            }
        }
    }

    private ArchiveInfo insertArchive(Archive archive,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives,
            boolean automated) {
        Package p = archive.getParentPackage();

        // Is this an update?
        Archive updatedArchive = null;
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package lp = a.getParentPackage();

                if (lp.canBeUpdatedBy(p) == UpdateInfo.UPDATE) {
                    updatedArchive = a;
                }
            }
        }

        // Find dependencies and adds them as needed to outArchives
        ArchiveInfo[] deps = findDependency(p,
                outArchives,
                selectedArchives,
                remotePkgs,
                remoteSources,
                localArchives);

        // Make sure it's not a dup
        ArchiveInfo ai = null;

        for (ArchiveInfo ai2 : outArchives) {
            Archive a2 = ai2.getNewArchive();
            if (a2 != null && a2.getParentPackage().sameItemAs(archive.getParentPackage())) {
                ai = ai2;
                break;
            }
        }

        if (ai == null) {
            ai = new ArchiveInfo(
                archive,        //newArchive
                updatedArchive, //replaced
                deps            //dependsOn
                );
            outArchives.add(ai);
        }

        if (deps != null) {
            for (ArchiveInfo d : deps) {
                d.addDependencyFor(ai);
            }
        }

        return ai;
    }

    /**
     * Resolves dependencies for a given package.
     *
     * Returns null if no dependencies were found.
     * Otherwise return an array of {@link ArchiveInfo}, which is guaranteed to have
     * at least size 1 and contain no null elements.
     */
    private ArchiveInfo[] findDependency(Package pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        // Current dependencies can be:
        // - addon: *always* depends on platform of same API level
        // - platform: *might* depends on tools of rev >= min-tools-rev
        // - extra: *might* depends on platform with api >= min-api-level

        Set<ArchiveInfo> aiFound = new HashSet<ArchiveInfo>();

        if (pkg instanceof IPlatformDependency) {
            ArchiveInfo ai = findPlatformDependency(
                    (IPlatformDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinToolsDependency) {

            ArchiveInfo ai = findToolsDependency(
                    (IMinToolsDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinPlatformToolsDependency) {

            ArchiveInfo ai = findPlatformToolsDependency(
                    (IMinPlatformToolsDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IMinApiLevelDependency) {

            ArchiveInfo ai = findMinApiLevelDependency(
                    (IMinApiLevelDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (pkg instanceof IExactApiLevelDependency) {

            ArchiveInfo ai = findExactApiLevelDependency(
                    (IExactApiLevelDependency) pkg,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives);

            if (ai != null) {
                aiFound.add(ai);
            }
        }

        if (aiFound.size() > 0) {
            ArchiveInfo[] result = aiFound.toArray(new ArchiveInfo[aiFound.size()]);
            Arrays.sort(result);
            return result;
        }

        return null;
    }

    /**
     * Resolves dependencies on tools.
     *
     * A platform or an extra package can both have a min-tools-rev, in which case it
     * depends on having a tools package of the requested revision.
     * Finds the tools dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    public ArchiveInfo findToolsDependency(
            IMinToolsDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        FullRevision rev = pkg.getMinToolsRevision();

        if (rev.equals(MinToolsPackage.MIN_TOOLS_REV_NOT_SPECIFIED)) {
            // Well actually there's no requirement.
            return null;
        }

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof ToolPackage) {
                    if (((ToolPackage) p).getRevision().compareTo(rev) >= 0) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        FullRevision localRev = rev;
        Archive localArch = null;
        for (Package p : remotePkgs) {
            if (p instanceof ToolPackage) {
                FullRevision r = ((ToolPackage) p).getRevision();
                if (r.compareTo(localRev) >= 0) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            localRev = r;
                            localArch = a;
                            break;
                        }
                    }
                }
            }
        }
        if (localArch != null) {
            return insertArchive(localArch,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    true /*automated*/);

        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_TOOL, rev);
    }

    /**
     * Resolves dependencies on platform-tools.
     *
     * A tool package can have a min-platform-tools-rev, in which case it depends on
     * having a platform-tool package of the requested revision.
     * Finds the platform-tool dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    public ArchiveInfo findPlatformToolsDependency(
            IMinPlatformToolsDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        FullRevision rev = pkg.getMinPlatformToolsRevision();
        boolean findMax = false;
        int compareThreshold = 0;
        ArchiveInfo aiMax = null;
        Archive aMax = null;

        if (rev.equals(IMinPlatformToolsDependency.MIN_PLATFORM_TOOLS_REV_INVALID)) {
            // The requirement is invalid, which is not supposed to happen since this
            // property is mandatory. However in a typical upgrade scenario we can end
            // up with the previous updater managing a new package and not dealing
            // correctly with the new unknown property.
            // So instead we parse all the existing and remote packages and try to find
            // the max available revision and we'll use it.
            findMax = true;
            // When findMax is false, we want r.compareTo(rev) >= 0.
            // When findMax is true, we want r.compareTo(rev) > 0 (so >= 1).
            compareThreshold = 1;
        }

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (findMax && r.compareTo(rev) > compareThreshold) {
                        rev = r;
                        aiMax = ai;
                    } else if (!findMax && r.compareTo(rev) >= compareThreshold) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Because of previews, we can have more than 1 choice, so get the local max.
        FullRevision localRev = rev;
        ArchiveInfo localAiMax = null;
        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (r.compareTo(localRev) >= compareThreshold) {
                        localRev = r;
                        localAiMax = ai;
                    }
                }
            }
        }
        if (localAiMax != null) {
            if (findMax) {
                rev = localRev;
                aiMax = localAiMax;
            } else {
                // The dependency is already scheduled for install, nothing else to do.
                return localAiMax;
            }
        }


        // Otherwise look in the selected archives.
        localRev = rev;
        Archive localAMax = null;
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformToolPackage) {
                    FullRevision r = ((PlatformToolPackage) p).getRevision();
                    if (r.compareTo(localRev) >= compareThreshold) {
                        localRev = r;
                        localAiMax = null;
                        localAMax = a;
                    }
                }
            }
            if (localAMax != null) {
                if (findMax) {
                    rev = localRev;
                    aiMax = null;
                    aMax = localAMax;
                } else {
                    // It's not already in the list of things to install, so add it now
                    return insertArchive(localAMax,
                            outArchives,
                            selectedArchives,
                            remotePkgs,
                            remoteSources,
                            localArchives,
                            true /*automated*/);
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        localRev = rev;
        localAMax = null;
        for (Package p : remotePkgs) {
            if (p instanceof PlatformToolPackage) {
                FullRevision r = ((PlatformToolPackage) p).getRevision();
                if (r.compareTo(rev) >= 0) {
                    // Make sure there's at least one valid archive here
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            if (r.compareTo(localRev) >= compareThreshold) {
                                localRev = r;
                                localAiMax = null;
                                localAMax = a;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (localAMax != null) {
            if (findMax) {
                rev = localRev;
                aiMax = null;
                aMax = localAMax;
            } else {
                // It's not already in the list of things to install, so add the
                // first compatible archive we can find.
                return insertArchive(localAMax,
                        outArchives,
                        selectedArchives,
                        remotePkgs,
                        remoteSources,
                        localArchives,
                        true /*automated*/);
            }
        }

        if (findMax) {
            if (aMax != null) {
                return insertArchive(aMax,
                        outArchives,
                        selectedArchives,
                        remotePkgs,
                        remoteSources,
                        localArchives,
                        true /*automated*/);
            } else if (aiMax != null) {
                return aiMax;
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this package depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingArchiveInfo(MissingArchiveInfo.TITLE_PLATFORM_TOOL, rev);
    }

    /**
     * Resolves dependencies on platform for an addon.
     *
     * An addon depends on having a platform with the same API level.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    public ArchiveInfo findPlatformDependency(
            IPlatformDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {
        // This is the requirement to match.
        AndroidVersion v = pkg.getAndroidVersion();

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // The dependency is already scheduled for install, nothing else to do.
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (v.equals(((PlatformPackage) p).getAndroidVersion())) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            return insertArchive(a,
                                    outArchives,
                                    selectedArchives,
                                    remotePkgs,
                                    remoteSources,
                                    localArchives,
                                    true /*automated*/);
                        }
                    }
                }
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this addon depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(pkg.getAndroidVersion());
    }

    /**
     * Resolves platform dependencies for extras.
     * An extra depends on having a platform with a minimun API level.
     *
     * We try to return the highest API level available above the specified minimum.
     * Note that installed packages have priority so if one installed platform satisfies
     * the dependency, we'll use it even if there's a higher API platform available but
     * not installed yet.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    protected ArchiveInfo findMinApiLevelDependency(
            IMinApiLevelDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        int api = pkg.getMinApiLevel();

        if (api == IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED) {
            return null;
        }

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install
        int foundApi = 0;
        ArchiveInfo foundAi = null;

        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        if (api > foundApi) {
                            foundApi = api;
                            foundAi = ai;
                        }
                    }
                }
            }
        }

        if (foundAi != null) {
            // The dependency is already scheduled for install, nothing else to do.
            return foundAi;
        }

        // Otherwise look in the selected archives *or* available remote packages
        // and takes the best out of the two sets.
        foundApi = 0;
        Archive foundArchive = null;
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                        if (api > foundApi) {
                            foundApi = api;
                            foundArchive = a;
                        }
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (((PlatformPackage) p).getAndroidVersion().isGreaterOrEqualThan(api)) {
                    if (api > foundApi) {
                        // It's not already in the list of things to install, so add the
                        // first compatible archive we can find.
                        for (Archive a : p.getArchives()) {
                            if (a.isCompatible()) {
                                foundApi = api;
                                foundArchive = a;
                            }
                        }
                    }
                }
            }
        }

        if (foundArchive != null) {
            // It's not already in the list of things to install, so add it now
            return insertArchive(foundArchive,
                    outArchives,
                    selectedArchives,
                    remotePkgs,
                    remoteSources,
                    localArchives,
                    true /*automated*/);
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
    }

    /**
     * Resolves platform dependencies for add-ons.
     * An add-ons depends on having a platform with an exact specific API level.
     *
     * Finds the platform dependency. If found, add it to the list of things to install.
     * Returns the archive info dependency, if any.
     */
    public ArchiveInfo findExactApiLevelDependency(
            IExactApiLevelDependency pkg,
            Collection<ArchiveInfo> outArchives,
            Collection<Archive> selectedArchives,
            Collection<Package> remotePkgs,
            SdkSource[] remoteSources,
            ArchiveInfo[] localArchives) {

        int api = pkg.getExactApiLevel();

        if (api == IExactApiLevelDependency.API_LEVEL_INVALID) {
            return null;
        }

        // Find a platform that would satisfy the requirement.

        // First look in locally installed packages.
        for (ArchiveInfo ai : localArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        // We found one already installed.
                        return null;
                    }
                }
            }
        }

        // Look in archives already scheduled for install

        for (ArchiveInfo ai : outArchives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        return ai;
                    }
                }
            }
        }

        // Otherwise look in the selected archives.
        if (selectedArchives != null) {
            for (Archive a : selectedArchives) {
                Package p = a.getParentPackage();
                if (p instanceof PlatformPackage) {
                    if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                        // It's not already in the list of things to install, so add it now
                        return insertArchive(a,
                                outArchives,
                                selectedArchives,
                                remotePkgs,
                                remoteSources,
                                localArchives,
                                true /*automated*/);
                    }
                }
            }
        }

        // Finally nothing matched, so let's look at all available remote packages
        fetchRemotePackages(remotePkgs, remoteSources);
        for (Package p : remotePkgs) {
            if (p instanceof PlatformPackage) {
                if (((PlatformPackage) p).getAndroidVersion().equals(api)) {
                    // It's not already in the list of things to install, so add the
                    // first compatible archive we can find.
                    for (Archive a : p.getArchives()) {
                        if (a.isCompatible()) {
                            return insertArchive(a,
                                    outArchives,
                                    selectedArchives,
                                    remotePkgs,
                                    remoteSources,
                                    localArchives,
                                    true /*automated*/);
                        }
                    }
                }
            }
        }

        // We end up here if nothing matches. We don't have a good platform to match.
        // We need to indicate this extra depends on a missing platform archive
        // so that it can be impossible to install later on.
        return new MissingPlatformArchiveInfo(new AndroidVersion(api, null /*codename*/));
    }

    /**
     * Fetch all remote packages only if really needed.
     * <p/>
     * This method takes a list of sources. Each source is only fetched once -- that is each
     * source keeps the list of packages that we fetched from the remote XML file. If the list
     * is null, it means this source has never been fetched so we'll do it once here. Otherwise
     * we rely on the cached list of packages from this source.
     * <p/>
     * This method also takes a remote package list as input, which it will fill out.
     * If a source has already been fetched, we'll add its packages to the remote package list
     * if they are not already present. Otherwise, the source will be fetched and the packages
     * added to the list.
     *
     * @param remotePkgs An in-out list of packages available from remote sources.
     *                   This list must not be null.
     *                   It can be empty or already contain some packages.
     * @param remoteSources A list of available remote sources to fetch from.
     */
    protected void fetchRemotePackages(
            final Collection<Package> remotePkgs,
            final SdkSource[] remoteSources) {
        if (remotePkgs.size() > 0) {
            return;
        }

        // First check if there's any remote source we need to fetch.
        // This will bring the task window, so we rather not display it unless
        // necessary.
        boolean needsFetch = false;
        for (final SdkSource remoteSrc : remoteSources) {
            Package[] pkgs = remoteSrc.getPackages();
            if (pkgs == null) {
                // This source has never been fetched. We'll do it below.
                needsFetch = true;
            } else {
                // This source has already been fetched and we know its package list.
                // We still need to make sure all of its packages are present in the
                // remotePkgs list.

                nextPackage: for (Package pkg : pkgs) {
                    for (Archive a : pkg.getArchives()) {
                        // Only add a package if it contains at least one compatible archive
                        // and is not already in the remote package list.
                        if (a.isCompatible()) {
                            if (!remotePkgs.contains(pkg)) {
                                remotePkgs.add(pkg);
                                continue nextPackage;
                            }
                        }
                    }
                }
            }
        }

        if (!needsFetch) {
            return;
        }

        final boolean forceHttp = mUpdaterData.getSettingsController().getSettings().getForceHttp();

        mUpdaterData.getTaskFactory().start("Refresh Sources", new ITask() {
            @Override
            public void run(ITaskMonitor monitor) {
                for (SdkSource remoteSrc : remoteSources) {
                    Package[] pkgs = remoteSrc.getPackages();

                    if (pkgs == null) {
                        remoteSrc.load(mUpdaterData.getDownloadCache(), monitor, forceHttp);
                        pkgs = remoteSrc.getPackages();
                    }

                    if (pkgs != null) {
                        nextPackage: for (Package pkg : pkgs) {
                            for (Archive a : pkg.getArchives()) {
                                // Only add a package if it contains at least one compatible archive
                                // and is not already in the remote package list.
                                if (a.isCompatible()) {
                                    if (!remotePkgs.contains(pkg)) {
                                        remotePkgs.add(pkg);
                                        continue nextPackage;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * A {@link LocalArchiveInfo} is an {@link ArchiveInfo} that wraps an already installed
     * "local" package/archive.
     * <p/>
     * In this case, the "new Archive" is still expected to be non null and the
     * "replaced Archive" is null. Installed archives are always accepted and never
     * rejected.
     * <p/>
     * Dependencies are not set.
     */
    private static class LocalArchiveInfo extends ArchiveInfo {

        public LocalArchiveInfo(Archive localArchive) {
            super(localArchive, null /*replaced*/, null /*dependsOn*/);
        }

        /** Installed archives are always accepted. */
        @Override
        public boolean isAccepted() {
            return true;
        }

        /** Installed archives are never rejected. */
        @Override
        public boolean isRejected() {
            return false;
        }
    }

    /**
     * A {@link MissingPlatformArchiveInfo} is an {@link ArchiveInfo} that represents a
     * package/archive that we <em>really</em> need as a dependency but that we don't have.
     * <p/>
     * This is currently used for addons and extras in case we can't find a matching base platform.
     * <p/>
     * This kind of archive has specific properties: the new archive to install is null,
     * there are no dependencies and no archive is being replaced. The info can never be
     * accepted and is always rejected.
     */
    private static class MissingPlatformArchiveInfo extends ArchiveInfo {

        private final AndroidVersion mVersion;

        /**
         * Constructs a {@link MissingPlatformArchiveInfo} that will indicate the
         * given platform version is missing.
         */
        public MissingPlatformArchiveInfo(AndroidVersion version) {
            super(null /*newArchive*/, null /*replaced*/, null /*dependsOn*/);
            mVersion = version;
        }

        /** Missing archives are never accepted. */
        @Override
        public boolean isAccepted() {
            return false;
        }

        /** Missing archives are always rejected. */
        @Override
        public boolean isRejected() {
            return true;
        }

        @Override
        public String getShortDescription() {
            return String.format("Missing SDK Platform Android%1$s, API %2$d",
                    mVersion.isPreview() ? " Preview" : "",
                    mVersion.getApiLevel());
        }
    }

    /**
     * A {@link MissingArchiveInfo} is an {@link ArchiveInfo} that represents a
     * package/archive that we <em>really</em> need as a dependency but that we don't have.
     * <p/>
     * This is currently used for extras in case we can't find a matching tool revision
     * or when a platform-tool is missing.
     * <p/>
     * This kind of archive has specific properties: the new archive to install is null,
     * there are no dependencies and no archive is being replaced. The info can never be
     * accepted and is always rejected.
     */
    private static class MissingArchiveInfo extends ArchiveInfo {

        private final FullRevision mRevision;
        private final String mTitle;

        public static final String TITLE_TOOL = "Tools";
        public static final String TITLE_PLATFORM_TOOL = "Platform-tools";

        /**
         * Constructs a {@link MissingPlatformArchiveInfo} that will indicate the
         * given platform version is missing.
         *
         * @param title Typically "Tools" or "Platform-tools".
         * @param revision The required revision.
         */
        public MissingArchiveInfo(String title, FullRevision revision) {
            super(null /*newArchive*/, null /*replaced*/, null /*dependsOn*/);
            mTitle = title;
            mRevision = revision;
        }

        /** Missing archives are never accepted. */
        @Override
        public boolean isAccepted() {
            return false;
        }

        /** Missing archives are always rejected. */
        @Override
        public boolean isRejected() {
            return true;
        }

        @Override
        public String getShortDescription() {
            return String.format("Missing Android SDK %1$s, revision %2$s",
                    mTitle,
                    mRevision.toShortString());
        }
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterNoWindow.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SdkUpdaterNoWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..f0f377d

//Synthetic comment -- @@ -0,0 +1,624 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.annotations.NonNull;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.utils.ILogger;
import com.android.utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Performs an update using only a non-interactive console output with no GUI.
 */
public class SdkUpdaterNoWindow {

    /** The {@link UpdaterData} to use. */
    private final UpdaterData mUpdaterData;
    /** The {@link ILogger} logger to use. */
    private final ILogger mSdkLog;
    /** The reply to any question asked by the update process. Currently this will
     *   be yes/no for ability to replace modified samples or restart ADB. */
    private final boolean mForce;

    /**
     * Creates an UpdateNoWindow object that will update using the given SDK root
     * and outputs to the given SDK logger.
     *
     * @param osSdkRoot The OS path of the SDK folder to update.
     * @param sdkManager An existing SDK manager to list current platforms and addons.
     * @param sdkLog A logger object, that should ideally output to a write-only console.
     * @param force The reply to any question asked by the update process. Currently this will
     *   be yes/no for ability to replace modified samples or restart ADB.
     * @param useHttp True to force using HTTP instead of HTTPS for downloads.
     * @param proxyPort An optional HTTP/HTTPS proxy port. Can be null.
     * @param proxyHost An optional HTTP/HTTPS proxy host. Can be null.
     */
    public SdkUpdaterNoWindow(String osSdkRoot,
            SdkManager sdkManager,
            ILogger sdkLog,
            boolean force,
            boolean useHttp,
            String proxyHost,
            String proxyPort) {
        mSdkLog = sdkLog;
        mForce = force;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

        // Read and apply settings from settings file, so that http/https proxy is set
        // and let the command line args override them as necessary.
        SettingsController settingsController = mUpdaterData.getSettingsController();
        settingsController.loadSettings();
        settingsController.applySettings();
        setupProxy(proxyHost, proxyPort);

        // Change the in-memory settings to force the http/https mode
        settingsController.setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

        // Use a factory that only outputs to the given ILogger.
        mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

        // Check that the AVD Manager has been correctly initialized. This is done separately
        // from the constructor in the GUI-based UpdaterWindowImpl to give time to the UI to
        // initialize before displaying a message box. Since we don't have any GUI here
        // we can call it whenever we want.
        if (mUpdaterData.checkIfInitFailed()) {
            return;
        }

        // Setup the default sources including the getenv overrides.
        mUpdaterData.setupDefaultSources();

        mUpdaterData.getLocalSdkParser().parseSdk(
                osSdkRoot,
                sdkManager,
                new NullTaskMonitor(sdkLog));
    }

    /**
     * Performs the actual update.
     *
     * @param pkgFilter A list of {@link SdkRepoConstants#NODES} to limit the type of packages
     *   we can update. A null or empty list means to update everything possible.
     * @param includeAll True to list and install all packages, including obsolete ones.
     * @param dryMode True to check what would be updated/installed but do not actually
     *   download or install anything.
     */
    public void updateAll(
            ArrayList<String> pkgFilter,
            boolean includeAll,
            boolean dryMode) {
        mUpdaterData.updateOrInstallAll_NoGUI(pkgFilter, includeAll, dryMode);
    }

    /**
     * Lists remote packages available for install using 'android update sdk --no-ui'.
     *
     * @param includeAll True to list and install all packages, including obsolete ones.
     * @param extendedOutput True to display more details on each package.
     */
    public void listRemotePackages(boolean includeAll, boolean extendedOutput) {
        mUpdaterData.listRemotePackages_NoGUI(includeAll, extendedOutput);
    }

    // -----

    /**
     * Sets both the HTTP and HTTPS proxy system properties, overriding the ones
     * from the settings with these values if they are defined.
     */
    private void setupProxy(String proxyHost, String proxyPort) {

        // The system property constants can be found in the Java SE documentation at
        // http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
        final String JAVA_PROP_HTTP_PROXY_HOST =  "http.proxyHost";      //$NON-NLS-1$
        final String JAVA_PROP_HTTP_PROXY_PORT =  "http.proxyPort";      //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        Properties props = System.getProperties();

        if (proxyHost != null && proxyHost.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        }
        if (proxyPort != null && proxyPort.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        }
    }

    /**
     * A custom implementation of {@link ITaskFactory} that
     * provides {@link ConsoleTaskMonitor} objects.
     */
    private class ConsoleTaskFactory implements ITaskFactory {
        @Override
        public void start(String title, ITask task) {
            start(title, null /*parentMonitor*/, task);
        }

        @Override
        public void start(String title, ITaskMonitor parentMonitor, ITask task) {
            if (parentMonitor == null) {
                task.run(new ConsoleTaskMonitor(title, task));
            } else {
                // Use all the reminder of the parent monitor.
                if (parentMonitor.getProgressMax() == 0) {
                    parentMonitor.setProgressMax(1);
                }

                ITaskMonitor sub = parentMonitor.createSubMonitor(
                        parentMonitor.getProgressMax() - parentMonitor.getProgress());
                try {
                    task.run(sub);
                } finally {
                    int delta =
                        sub.getProgressMax() - sub.getProgress();
                    if (delta > 0) {
                        sub.incProgress(delta);
                    }
                }
            }
        }
    }

    /**
     * A custom implementation of {@link ITaskMonitor} that defers all output to the
     * super {@link SdkUpdaterNoWindow#mSdkLog}.
     */
    private class ConsoleTaskMonitor implements ITaskMonitor {

        private static final double MAX_COUNT = 10000.0;
        private double mIncCoef = 0;
        private double mValue = 0;
        private String mLastDesc = null;
        private String mLastProgressBase = null;

        /**
         * Creates a new {@link ConsoleTaskMonitor} with the given title.
         */
        public ConsoleTaskMonitor(String title, ITask task) {
            mSdkLog.info("%s:\n", title);
        }

        /**
         * Sets the description in the current task dialog.
         */
        @Override
        public void setDescription(String format, Object...args) {

            String last = mLastDesc;
            String line = String.format("  " + format, args);                       //$NON-NLS-1$

            // If the description contains a %, it generally indicates a recurring
            // progress so we want a \r at the end.
            int pos = line.indexOf('%');
            if (pos > -1) {
                String base = line.trim();
                if (mLastProgressBase != null && base.startsWith(mLastProgressBase)) {
                    line = "    " + base.substring(mLastProgressBase.length());     //$NON-NLS-1$
                }
                line += '\r';
            } else {
                mLastProgressBase = line.trim();
                line += '\n';
            }

            // Skip line if it's the same as the last one.
            if (last != null && last.equals(line.trim())) {
                return;
            }
            mLastDesc = line.trim();

            // If the last line terminated with a \r but the new one doesn't, we need to
            // insert a \n to avoid erasing the previous line.
            if (last != null &&
                    last.endsWith("\r") &&                                          //$NON-NLS-1$
                    !line.endsWith("\r")) {                                         //$NON-NLS-1$
                line = '\n' + line;
            }

            mSdkLog.info("%s", line);                                             //$NON-NLS-1$
        }

        @Override
        public void log(String format, Object...args) {
            setDescription("  " + format, args);                                    //$NON-NLS-1$
        }

        @Override
        public void logError(String format, Object...args) {
            setDescription(format, args);
        }

        @Override
        public void logVerbose(String format, Object...args) {
            // The ConsoleTask does not display verbose log messages.
        }

        // --- ILogger ---

        @Override
        public void error(Throwable t, String errorFormat, Object... args) {
            mSdkLog.error(t, errorFormat, args);
        }

        @Override
        public void warning(@NonNull String warningFormat, Object... args) {
            mSdkLog.warning(warningFormat, args);
        }

        @Override
        public void info(@NonNull String msgFormat, Object... args) {
            mSdkLog.info(msgFormat, args);
        }

        @Override
        public void verbose(@NonNull String msgFormat, Object... args) {
            mSdkLog.verbose(msgFormat, args);
        }

        /**
         * Sets the max value of the progress bar.
         *
         * Weird things will happen if setProgressMax is called multiple times
         * *after* {@link #incProgress(int)}: we don't try to adjust it on the
         * fly.
         */
        @Override
        public void setProgressMax(int max) {
            assert max > 0;
            // Always set the dialog's progress max to 10k since it only handles
            // integers and we want to have a better inner granularity. Instead
            // we use the max to compute a coefficient for inc deltas.
            mIncCoef = max > 0 ? MAX_COUNT / max : 0;
            assert mIncCoef > 0;
        }

        @Override
        public int getProgressMax() {
            return mIncCoef > 0 ? (int) (MAX_COUNT / mIncCoef) : 0;
        }

        /**
         * Increments the current value of the progress bar.
         */
        @Override
        public void incProgress(int delta) {
            if (delta > 0 && mIncCoef > 0) {
                internalIncProgress(delta * mIncCoef);
            }
        }

        private void internalIncProgress(double realDelta) {
            mValue += realDelta;
            // max value is 10k, so 10k/100 == 100%.
            // Experimentation shows that it is not really useful to display this
            // progression since during download the description line will change.
            // mSdkLog.printf("    [%3d%%]\r", ((int)mValue) / 100);
        }

        /**
         * Returns the current value of the progress bar,
         * between 0 and up to {@link #setProgressMax(int)} - 1.
         */
        @Override
        public int getProgress() {
            assert mIncCoef > 0;
            return mIncCoef > 0 ? (int)(mValue / mIncCoef) : 0;
        }

        /**
         * Returns true if the "Cancel" button was selected.
         */
        @Override
        public boolean isCancelRequested() {
            return false;
        }

        /**
         * Display a yes/no question dialog box.
         *
         * This implementation allow this to be called from any thread, it
         * makes sure the dialog is opened synchronously in the ui thread.
         *
         * @param title The title of the dialog box
         * @param message The error message
         * @return true if YES was clicked.
         */
        @Override
        public boolean displayPrompt(final String title, final String message) {
            // TODO Make it interactive if mForce==false
            mSdkLog.info("\n%1$s\n%2$s\n%3$s",        //$NON-NLS-1$
                    title,
                    message,
                    mForce ? "--force used, will reply yes\n" :
                             "Note: you  can use --force to override to yes.\n");
            if (mForce) {
                return true;
            }

            while (true) {
                mSdkLog.info("%1$s", "[y/n] =>");     //$NON-NLS-1$
                try {
                    byte[] readBuffer = new byte[2048];
                    String reply = readLine(readBuffer).trim();
                    mSdkLog.info("\n");               //$NON-NLS-1$
                    if (reply.length() > 0 && reply.length() <= 3) {
                        char c = reply.charAt(0);
                        if (c == 'y' || c == 'Y') {
                            return true;
                        } else if (c == 'n' || c == 'N') {
                            return false;
                        }
                    }
                    mSdkLog.info("Unknown reply '%s'. Please use y[es]/n[o].\n");  //$NON-NLS-1$

                } catch (IOException e) {
                    // Exception. Be conservative and say no.
                    mSdkLog.info("\n");               //$NON-NLS-1$
                    return false;
                }
            }
        }

        /**
         * Displays a prompt message to the user and read two values,
         * login/password.
         * <p>
         * <i>Asks user for login/password information.</i>
         * <p>
         * This method shows a question in the standard output, asking for login
         * and password.</br>
         * <b>Method Output:</b></br>
         *     Title</br>
         *     Message</br>
         *     Login: (Wait for user input)</br>
         *     Password: (Wait for user input)</br>
         * <p>
         *
         * @param title The title of the iteration.
         * @param message The message to be displayed.
         * @return A {@link Pair} holding the entered login and password. The
         *         <b>first element</b> is always the <b>Login</b>, and the
         *         <b>second element</b> is always the <b>Password</b>. This
         *         method will never return null, in case of error the pair will
         *         be filled with empty strings.
         * @see ITaskMonitor#displayLoginCredentialsPrompt(String, String)
         */
        @Override
        public UserCredentials displayLoginCredentialsPrompt(String title, String message) {
            String login = "";    //$NON-NLS-1$
            String password = ""; //$NON-NLS-1$
            String workstation = ""; //$NON-NLS-1$
            String domain = ""; //$NON-NLS-1$

            mSdkLog.info("\n%1$s\n%2$s", title, message);
            byte[] readBuffer = new byte[2048];
            try {
                mSdkLog.info("\nLogin: ");
                login = readLine(readBuffer);
                mSdkLog.info("\nPassword: ");
                password = readLine(readBuffer);
                mSdkLog.info("\nIf your proxy uses NTLM authentication, provide the following information. Leave blank otherwise.");
                mSdkLog.info("\nWorkstation: ");
                workstation = readLine(readBuffer);
                mSdkLog.info("\nDomain: ");
                domain = readLine(readBuffer);

                /*
                 * TODO: Implement a way to don't echo the typed password On
                 * Java 5 there's no simple way to do this. There's just a
                 * workaround which is output backspaces on each keystroke.
                 * A good alternative is to use Java 6 java.io.Console
                 */
            } catch (IOException e) {
                // Reset login/pass to empty Strings.
                login = "";    //$NON-NLS-1$
                password = ""; //$NON-NLS-1$
                workstation = ""; //$NON-NLS-1$
                domain = ""; //$NON-NLS-1$
                //Just print the error to console.
                mSdkLog.info("\nError occurred during login/pass query: %s\n", e.getMessage());
            }

            return new UserCredentials(login, password, workstation, domain);
        }

        /**
         * Reads current console input in the given buffer.
         *
         * @param buffer Buffer to hold the user input. Must be larger than the largest
         *   expected input. Cannot be null.
         * @return A new string. May be empty but not null.
         * @throws IOException in case the buffer isn't long enough.
         */
        private String readLine(byte[] buffer) throws IOException {
            int count = System.in.read(buffer);

            // is the input longer than the buffer?
            if (count == buffer.length && buffer[count-1] != 10) {
                throw new IOException(String.format(
                        "Input is longer than the buffer size, (%1$s) bytes", buffer.length));
            }

            // ignore end whitespace
            while (count > 0 && (buffer[count-1] == '\r' || buffer[count-1] == '\n')) {
                count--;
            }

            return new String(buffer, 0, count);
        }

        /**
         * Creates a sub-monitor that will use up to tickCount on the progress bar.
         * tickCount must be 1 or more.
         */
        @Override
        public ITaskMonitor createSubMonitor(int tickCount) {
            assert mIncCoef > 0;
            assert tickCount > 0;
            return new ConsoleSubTaskMonitor(this, null, mValue, tickCount * mIncCoef);
        }
    }

    private interface IConsoleSubTaskMonitor extends ITaskMonitor {
        public void subIncProgress(double realDelta);
    }

    private static class ConsoleSubTaskMonitor implements IConsoleSubTaskMonitor {

        private final ConsoleTaskMonitor mRoot;
        private final IConsoleSubTaskMonitor mParent;
        private final double mStart;
        private final double mSpan;
        private double mSubValue;
        private double mSubCoef;

        /**
         * Creates a new sub task monitor which will work for the given range [start, start+span]
         * in its parent.
         *
         * @param root The ProgressTask root
         * @param parent The immediate parent. Can be the null or another sub task monitor.
         * @param start The start value in the root's coordinates
         * @param span The span value in the root's coordinates
         */
        public ConsoleSubTaskMonitor(ConsoleTaskMonitor root,
                IConsoleSubTaskMonitor parent,
                double start,
                double span) {
            mRoot = root;
            mParent = parent;
            mStart = start;
            mSpan = span;
            mSubValue = start;
        }

        @Override
        public boolean isCancelRequested() {
            return mRoot.isCancelRequested();
        }

        @Override
        public void setDescription(String format, Object... args) {
            mRoot.setDescription(format, args);
        }

        @Override
        public void log(String format, Object... args) {
            mRoot.log(format, args);
        }

        @Override
        public void logError(String format, Object... args) {
            mRoot.logError(format, args);
        }

        @Override
        public void logVerbose(String format, Object... args) {
            mRoot.logVerbose(format, args);
        }

        @Override
        public void setProgressMax(int max) {
            assert max > 0;
            mSubCoef = max > 0 ? mSpan / max : 0;
            assert mSubCoef > 0;
        }

        @Override
        public int getProgressMax() {
            return mSubCoef > 0 ? (int) (mSpan / mSubCoef) : 0;
        }

        @Override
        public int getProgress() {
            assert mSubCoef > 0;
            return mSubCoef > 0 ? (int)((mSubValue - mStart) / mSubCoef) : 0;
        }

        @Override
        public void incProgress(int delta) {
            if (delta > 0 && mSubCoef > 0) {
                subIncProgress(delta * mSubCoef);
            }
        }

        @Override
        public void subIncProgress(double realDelta) {
            mSubValue += realDelta;
            if (mParent != null) {
                mParent.subIncProgress(realDelta);
            } else {
                mRoot.internalIncProgress(realDelta);
            }
        }

        @Override
        public boolean displayPrompt(String title, String message) {
            return mRoot.displayPrompt(title, message);
        }

        @Override
        public UserCredentials displayLoginCredentialsPrompt(String title, String message) {
            return mRoot.displayLoginCredentialsPrompt(title, message);
        }

        @Override
        public ITaskMonitor createSubMonitor(int tickCount) {
            assert mSubCoef > 0;
            assert tickCount > 0;
            return new ConsoleSubTaskMonitor(mRoot,
                    this,
                    mSubValue,
                    tickCount * mSubCoef);
        }

        // --- ILogger ---

        @Override
        public void error(Throwable t, String errorFormat, Object... args) {
            mRoot.error(t, errorFormat, args);
        }

        @Override
        public void warning(@NonNull String warningFormat, Object... args) {
            mRoot.warning(warningFormat, args);
        }

        @Override
        public void info(@NonNull String msgFormat, Object... args) {
            mRoot.info(msgFormat, args);
        }

        @Override
        public void verbose(@NonNull String msgFormat, Object... args) {
            mRoot.verbose(msgFormat, args);
        }
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SettingsController.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/SettingsController.java
new file mode 100755
//Synthetic comment -- index 0000000..dc45443

//Synthetic comment -- @@ -0,0 +1,382 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.annotations.NonNull;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.utils.ILogger;

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
 * Controller class to get settings values. Settings are kept in-memory.
 * Users of this class must first load the settings before changing them and save
 * them when modified.
 * <p/>
 * Settings are enumerated by constants in {@link ISettingsPage}.
 */
public class SettingsController {

    private static final String SETTINGS_FILENAME = "androidtool.cfg"; //$NON-NLS-1$

    private final ILogger mSdkLog;
    private final Settings mSettings;

    public interface OnChangedListener {
        public void onSettingsChanged(SettingsController controller, Settings oldSettings);
    }
    private final List<OnChangedListener> mChangedListeners = new ArrayList<OnChangedListener>(1);

    /** The currently associated {@link ISettingsPage}. Can be null. */
    private ISettingsPage mSettingsPage;

    /**
     * Constructs a new default {@link SettingsController}.
     *
     * @param sdkLog A non-null logger to use.
     */
    public SettingsController(@NonNull ILogger sdkLog) {
        mSdkLog = sdkLog;
        mSettings = new Settings();
    }

    /**
     * Specialized constructor that wraps an existing {@link Settings} instance.
     * This is mostly used in unit-tests to override settings that are being used.
     * Normal usage should NOT need to call this constructor.
     *
     * @param sdkLog   A non-null logger to use.
     * @param settings A non-null {@link Settings} to use as-is. It is not duplicated.
     */
    protected SettingsController(@NonNull ILogger sdkLog, @NonNull Settings settings) {
        mSdkLog = sdkLog;
        mSettings = settings;
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
     * Sets the value of the {@link ISettingsPage#KEY_SHOW_UPDATE_ONLY} setting.
     *
     * @param enabled True if only compatible non-obsolete update items should be shown.
     * @see ISettingsPage#KEY_SHOW_UPDATE_ONLY
     */
    public void setShowUpdateOnly(boolean enabled) {
        setSetting(ISettingsPage.KEY_SHOW_UPDATE_ONLY, enabled);
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

    /**
     * Associate the given {@link ISettingsPage} with this {@link SettingsController}.
     * <p/>
     * This loads the current properties into the setting page UI.
     * It then associates the SettingsChanged callback with this controller.
     * <p/>
     * If the setting page given is null, it will be unlinked from controller.
     *
     * @param settingsPage An {@link ISettingsPage} to associate with the controller.
     */
    public void setSettingsPage(ISettingsPage settingsPage) {
        mSettingsPage = settingsPage;

        if (settingsPage != null) {
            settingsPage.loadSettings(mSettings.mProperties);

            settingsPage.setOnSettingsChanged(new ISettingsPage.SettingsChangedCallback() {
                @Override
                public void onSettingsChanged(ISettingsPage page) {
                    SettingsController.this.onSettingsChanged();
                }
            });
        }
    }

    /**
     * Load settings from the settings file.
     */
    public void loadSettings() {
        FileInputStream fis = null;
        String path = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            path = f.getPath();
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
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Saves settings to the settings file.
     */
    public void saveSettings() {

        FileOutputStream fos = null;
        String path = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SETTINGS_FILENAME);
            path = f.getPath();

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
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
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

    /**
     * Applies the current settings.
     */
    public void applySettings() {
        Properties props = System.getProperties();

        // Get the configured HTTP proxy settings
        String proxyHost = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
                ""); //$NON-NLS-1$
        String proxyPort = mSettings.mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
                ""); //$NON-NLS-1$

        // Set both the HTTP and HTTPS proxy system properties.
        // The system property constants can be found in the Java SE documentation at
        // http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
        final String JAVA_PROP_HTTP_PROXY_HOST =  "http.proxyHost";      //$NON-NLS-1$
        final String JAVA_PROP_HTTP_PROXY_PORT =  "http.proxyPort";      //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_HOST = "https.proxyHost";     //$NON-NLS-1$
        final String JAVA_PROP_HTTPS_PROXY_PORT = "https.proxyPort";     //$NON-NLS-1$

        // Only change the proxy if have something in the preferences.
        // Do not erase the default settings by empty values.
        if (proxyHost != null && proxyHost.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_HOST,  proxyHost);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_HOST, proxyHost);
        }
        if (proxyPort != null && proxyPort.length() > 0) {
            props.setProperty(JAVA_PROP_HTTP_PROXY_PORT,  proxyPort);
            props.setProperty(JAVA_PROP_HTTPS_PROXY_PORT, proxyPort);
        }
     }

}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/UpdaterData.java b/sdklib/src/main/java/com/android/sdklib/internal/repository/updater/UpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..3acb0c4

//Synthetic comment -- @@ -0,0 +1,1088 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdklib.internal.repository.updater;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation.AndroidLocationException;
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
import com.android.sdklib.internal.repository.updater.SettingsController.OnChangedListener;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdklib.util.LineUtil;
import com.android.sdklib.util.SparseIntArray;
import com.android.utils.ILogger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data shared by the SDK Manager updaters.
 */
public class UpdaterData implements IUpdaterData {

    public static final int NO_TOOLS_MSG = 0;
    public static final int TOOLS_MSG_UPDATED_FROM_ADT = 1;
    public static final int TOOLS_MSG_UPDATED_FROM_SDKMAN = 2;

    private String mOsSdkRoot;

    private final LocalSdkParser mLocalSdkParser = new LocalSdkParser();
    /** Holds all sources. Do not use this directly.
     * Instead use {@link #getSources()} so that unit tests can override this as needed. */
    private final SdkSources mSources = new SdkSources();
    /** Holds settings. Do not use this directly.
     * Instead use {@link #getSettingsController()} so that unit tests can override this. */
    private final SettingsController mSettingsController;
    private final ArrayList<ISdkChangeListener> mListeners = new ArrayList<ISdkChangeListener>();
    private final ILogger mSdkLog;
    private ITaskFactory mTaskFactory;

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
    private AndroidLocationException mAvdManagerInitError;

    /**
     * Creates a new updater data.
     *
     * @param sdkLog Logger. Cannot be null.
     * @param osSdkRoot The OS path to the SDK root.
     */
    public UpdaterData(String osSdkRoot, ILogger sdkLog) {
        mOsSdkRoot = osSdkRoot;
        mSdkLog = sdkLog;

        mSettingsController = initSettingsController();
        initSdk();
    }

    // ----- getters, setters ----

    public String getOsSdkRoot() {
        return mOsSdkRoot;
    }

    @Override
    public DownloadCache getDownloadCache() {
        if (mDownloadCache == null) {
            mDownloadCache = new DownloadCache(
                    getSettingsController().getSettings().getUseDownloadCache() ?
                            DownloadCache.Strategy.FRESH_CACHE :
                            DownloadCache.Strategy.DIRECT);
        }
        return mDownloadCache;
    }

    public void setTaskFactory(ITaskFactory taskFactory) {
        mTaskFactory = taskFactory;
    }

    @Override
    public ITaskFactory getTaskFactory() {
        return mTaskFactory;
    }

    public SdkSources getSources() {
        return mSources;
    }

    public LocalSdkParser getLocalSdkParser() {
        return mLocalSdkParser;
    }

    @Override
    public ILogger getSdkLog() {
        return mSdkLog;
    }

    @Override
    public SdkManager getSdkManager() {
        return mSdkManager;
    }

    @Override
    public AvdManager getAvdManager() {
        return mAvdManager;
    }

    @Override
    public SettingsController getSettingsController() {
        return mSettingsController;
    }

    /** Adds a listener ({@link ISdkChangeListener}) that is notified when the SDK is reloaded. */
    public void addListeners(ISdkChangeListener listener) {
        if (mListeners.contains(listener) == false) {
            mListeners.add(listener);
        }
    }

    /** Removes a listener ({@link ISdkChangeListener}) that is notified when the SDK is reloaded. */
    public void removeListener(ISdkChangeListener listener) {
        mListeners.remove(listener);
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
     *
     * @return True if an error occurred, false if we should continue.
     */
    public boolean checkIfInitFailed() {
        if (mAvdManagerInitError != null) {
            String example;
            if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS) {
                example = "%USERPROFILE%";     //$NON-NLS-1$
            } else {
                example = "~";                 //$NON-NLS-1$
            }

            String error = String.format(
                "The AVD manager normally uses the user's profile directory to store " +
                "AVD files. However it failed to find the default profile directory. " +
                "\n" +
                "To fix this, please set the environment variable ANDROID_SDK_HOME to " +
                "a valid path such as \"%s\".",
                example);

            displayInitError(error);

            return true;
        }
        return false;
    }

    protected void displayInitError(String error) {
        mSdkLog.error(null /* Throwable */, "%s", error);  //$NON-NLS-1$
    }

    // -----

    /**
     * Runs a runnable on the UI thread.
     * The base implementation just runs the runnable right away.
     *
     * @param r Non-null runnable.
     */
    protected void runOnUiThread(@NonNull Runnable r) {
        r.run();
    }

    /**
     * Initializes the {@link SdkManager} and the {@link AvdManager}.
     * Extracted so that we can override this in unit tests.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void initSdk() {
        setSdkManager(SdkManager.createManager(mOsSdkRoot, mSdkLog));
        try {
          mAvdManager = null;
          mAvdManager = AvdManager.getInstance(mSdkManager, mSdkLog);
        } catch (AndroidLocationException e) {
            mSdkLog.error(e, "Unable to read AVDs: " + e.getMessage());  //$NON-NLS-1$

            // Note: we used to continue here, but the thing is that
            // mAvdManager==null so nothing is really going to work as
            // expected. Let's just display an error later in checkIfInitFailed()
            // and abort right there. This step is just too early in the SWT
            // setup process to display a message box yet.

            mAvdManagerInitError = e;
        }

        // notify listeners.
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
            }
        });
        return settingsController;
    }

    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void setSdkManager(SdkManager sdkManager) {
        mSdkManager = sdkManager;
    }

    /**
     * Reloads the SDK content (targets).
     * <p/>
     * This also reloads the AVDs in case their status changed.
     * <p/>
     * This does not notify the listeners ({@link ISdkChangeListener}).
     */
    public void reloadSdk() {
        // reload SDK
        mSdkManager.reloadSdk(mSdkLog);

        // reload AVDs
        if (mAvdManager != null) {
            try {
                mAvdManager.reloadAvds(mSdkLog);
            } catch (AndroidLocationException e) {
                // FIXME
            }
        }

        mLocalSdkParser.clearPackages();

        // notify listeners
        broadcastOnSdkReload();
    }

    /**
     * Reloads the AVDs.
     * <p/>
     * This does not notify the listeners.
     */
    public void reloadAvds() {
        // reload AVDs
        if (mAvdManager != null) {
            try {
                mAvdManager.reloadAvds(mSdkLog);
            } catch (AndroidLocationException e) {
                mSdkLog.error(e, null);
            }
        }
    }

    /**
     * Sets up the default sources: <br/>
     * - the default google SDK repository, <br/>
     * - the user sources from prefs <br/>
     * - the extra repo URLs from the environment, <br/>
     * - and finally the extra user repo URLs from the environment.
     */
    public void setupDefaultSources() {
        SdkSources sources = getSources();

        // Load the conventional sources.
        // For testing, the env var can be set to replace the default root download URL.
        // It must end with a / and its the location where the updater will look for
        // the repository.xml, addons_list.xml and such files.

        String baseUrl = System.getenv("SDK_TEST_BASE_URL");                        //$NON-NLS-1$
        if (baseUrl == null || baseUrl.length() <= 0 || !baseUrl.endsWith("/")) {   //$NON-NLS-1$
            baseUrl = SdkRepoConstants.URL_GOOGLE_SDK_SITE;
        }

        sources.add(SdkSourceCategory.ANDROID_REPO,
                new SdkRepoSource(baseUrl,
                                  SdkSourceCategory.ANDROID_REPO.getUiName()));

        // Load user sources (this will also notify change listeners but this operation is
        // done early enough that there shouldn't be any anyway.)
        sources.loadUserAddons(getSdkLog());
    }

    /**
     * Returns the list of installed packages, parsing them if this has not yet been done.
     * <p/>
     * The package list is cached in the {@link LocalSdkParser} and will be reset when
     * {@link #reloadSdk()} is invoked.
     */
    public Package[] getInstalledPackages(ITaskMonitor monitor) {
        LocalSdkParser parser = getLocalSdkParser();

        Package[] packages = parser.getPackages();

        if (packages == null) {
            // load on demand the first time
            packages = parser.parseSdk(getOsSdkRoot(), getSdkManager(), monitor);
        }

        return packages;
    }
    /**
     * Install the list of given {@link Archive}s. This is invoked by the user selecting some
     * packages in the remote page and then clicking "install selected".
     *
     * @param archives The archives to install. Incompatible ones will be skipped.
     * @param flags Optional flags for the installer, such as {@link #NO_TOOLS_MSG}.
     * @return A list of archives that have been installed. Can be empty but not null.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected List<Archive> installArchives(final List<ArchiveInfo> archives, final int flags) {
        if (mTaskFactory == null) {
            throw new IllegalArgumentException("Task Factory is null");
        }

        // this will accumulate all the packages installed.
        final List<Archive> newlyInstalledArchives = new ArrayList<Archive>();

        final boolean forceHttp = getSettingsController().getSettings().getForceHttp();

        // sort all archives based on their dependency level.
        Collections.sort(archives, new InstallOrderComparator());

        mTaskFactory.start("Installing Archives", new ITask() {
            @Override
            public void run(ITaskMonitor monitor) {

                final int progressPerArchive = 2 * ArchiveInstaller.NUM_MONITOR_INC;
                monitor.setProgressMax(1 + archives.size() * progressPerArchive);
                monitor.setDescription("Preparing to install archives");

                boolean installedAddon = false;
                boolean installedTools = false;
                boolean installedPlatformTools = false;
                boolean preInstallHookInvoked = false;

                // Mark all current local archives as already installed.
                HashSet<Archive> installedArchives = new HashSet<Archive>();
                for (Package p : getInstalledPackages(monitor.createSubMonitor(1))) {
                    for (Archive a : p.getArchives()) {
                        installedArchives.add(a);
                    }
                }

                int numInstalled = 0;
                nextArchive: for (ArchiveInfo ai : archives) {
                    Archive archive = ai.getNewArchive();
                    if (archive == null) {
                        // This is not supposed to happen.
                        continue nextArchive;
                    }

                    int nextProgress = monitor.getProgress() + progressPerArchive;
                    try {
                        if (monitor.isCancelRequested()) {
                            break;
                        }

                        ArchiveInfo[] adeps = ai.getDependsOn();
                        if (adeps != null) {
                            for (ArchiveInfo adep : adeps) {
                                Archive na = adep.getNewArchive();
                                if (na == null) {
                                    // This archive depends on a missing archive.
                                    // We shouldn't get here.
                                    // Skip it.
                                    monitor.log("Skipping '%1$s'; it depends on a missing package.",
                                            archive.getParentPackage().getShortDescription());
                                    continue nextArchive;
                                } else if (!installedArchives.contains(na)) {
                                    // This archive depends on another one that was not installed.
                                    // We shouldn't get here.
                                    // Skip it.
                                    monitor.logError("Skipping '%1$s'; it depends on '%2$s' which was not installed.",
                                            archive.getParentPackage().getShortDescription(),
                                            adep.getShortDescription());
                                    continue nextArchive;
                                }
                            }
                        }

                        if (!preInstallHookInvoked) {
                            preInstallHookInvoked = true;
                            broadcastPreInstallHook();
                        }

                        ArchiveInstaller installer = createArchiveInstaler();
                        if (installer.install(ai,
                                              mOsSdkRoot,
                                              forceHttp,
                                              mSdkManager,
                                              getDownloadCache(),
                                              monitor)) {
                            // We installed this archive.
                            newlyInstalledArchives.add(archive);
                            installedArchives.add(archive);
                            numInstalled++;

                            // If this package was replacing an existing one, the old one
                            // is no longer installed.
                            installedArchives.remove(ai.getReplaced());

                            // Check if we successfully installed a platform-tool or add-on package.
                            if (archive.getParentPackage() instanceof AddonPackage) {
                                installedAddon = true;
                            } else if (archive.getParentPackage() instanceof ToolPackage) {
                                installedTools = true;
                            } else if (archive.getParentPackage() instanceof PlatformToolPackage) {
                                installedPlatformTools = true;
                            }
                        }

                    } catch (Throwable t) {
                        // Display anything unexpected in the monitor.
                        String msg = t.getMessage();
                        if (msg != null) {
                            msg = String.format("Unexpected Error installing '%1$s': %2$s: %3$s",
                                    archive.getParentPackage().getShortDescription(),
                                    t.getClass().getCanonicalName(), msg);
                        } else {
                            // no error info? get the stack call to display it
                            // At least that'll give us a better bug report.
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            t.printStackTrace(new PrintStream(baos));

                            msg = String.format("Unexpected Error installing '%1$s'\n%2$s",
                                    archive.getParentPackage().getShortDescription(),
                                    baos.toString());
                        }

                        monitor.log(     "%1$s", msg);      //$NON-NLS-1$
                        mSdkLog.error(t, "%1$s", msg);      //$NON-NLS-1$
                    } finally {

                        // Always move the progress bar to the desired position.
                        // This allows internal methods to not have to care in case
                        // they abort early
                        monitor.incProgress(nextProgress - monitor.getProgress());
                    }
                }

                if (installedAddon) {
                    // Update the USB vendor ids for adb
                    try {
                        mSdkManager.updateAdb();
                        monitor.log("Updated ADB to support the USB devices declared in the SDK add-ons.");
                    } catch (Exception e) {
                        mSdkLog.error(e, "Update ADB failed");
                        monitor.logError("failed to update adb to support the USB devices declared in the SDK add-ons.");
                    }
                }

                if (preInstallHookInvoked) {
                    broadcastPostInstallHook();
                }

                if (installedAddon || installedPlatformTools) {
                    // We need to restart ADB. Actually since we don't know if it's even
                    // running, maybe we should just kill it and not start it.
                    // Note: it turns out even under Windows we don't need to kill adb
                    // before updating the tools folder, as adb.exe is (surprisingly) not
                    // locked.

                    askForAdbRestart(monitor);
                }

                if (installedTools) {
                    notifyToolsNeedsToBeRestarted(flags);
                }

                if (numInstalled == 0) {
                    monitor.setDescription("Done. Nothing was installed.");
                } else {
                    monitor.setDescription("Done. %1$d %2$s installed.",
                            numInstalled,
                            numInstalled == 1 ? "package" : "packages");

                    //notify listeners something was installed, so that they can refresh
                    reloadSdk();
                }
            }
        });

        return newlyInstalledArchives;
    }

    /**
     * A comparator to sort all the {@link ArchiveInfo} based on their
     * dependency level. This forces the installer to install first all packages
     * with no dependency, then those with one level of dependency, etc.
     */
    private static class InstallOrderComparator implements Comparator<ArchiveInfo> {

        private final Map<ArchiveInfo, Integer> mOrders = new HashMap<ArchiveInfo, Integer>();

        @Override
        public int compare(ArchiveInfo o1, ArchiveInfo o2) {
            int n1 = getDependencyOrder(o1);
            int n2 = getDependencyOrder(o2);

            return n1 - n2;
        }

        private int getDependencyOrder(ArchiveInfo ai) {
            if (ai == null) {
                return 0;
            }

            // reuse cached value, if any
            Integer cached = mOrders.get(ai);
            if (cached != null) {
                return cached.intValue();
            }

            ArchiveInfo[] deps = ai.getDependsOn();
            if (deps == null) {
                return 0;
            }

            // compute dependencies, recursively
            int n = deps.length;

            for (ArchiveInfo dep : deps) {
                n += getDependencyOrder(dep);
            }

            // cache it
            mOrders.put(ai, Integer.valueOf(n));

            return n;
        }

    }

    /**
     * Attempts to restart ADB.
     * <p/>
     * If the "ask before restart" setting is set (the default), prompt the user whether
     * now is a good time to restart ADB.
     */
    protected void askForAdbRestart(ITaskMonitor monitor) {
        // Restart ADB if we don't need to ask.
        if (!getSettingsController().getSettings().getAskBeforeAdbRestart()) {
            AdbWrapper adb = new AdbWrapper(getOsSdkRoot(), monitor);
            adb.stopAdb();
            adb.startAdb();
        }
    }

    protected void notifyToolsNeedsToBeRestarted(int flags) {

        String msg = null;
        if ((flags & TOOLS_MSG_UPDATED_FROM_ADT) != 0) {
            msg =
            "The Android SDK and AVD Manager that you are currently using has been updated. " +
            "Please also run Eclipse > Help > Check for Updates to see if the Android " +
            "plug-in needs to be updated.";

        } else if ((flags & TOOLS_MSG_UPDATED_FROM_SDKMAN) != 0) {
            msg =
            "The Android SDK and AVD Manager that you are currently using has been updated. " +
            "It is recommended that you now close the manager window and re-open it. " +
            "If you use Eclipse, please run Help > Check for Updates to see if the Android " +
            "plug-in needs to be updated.";
        }

        mSdkLog.info("%s", msg);  //$NON-NLS-1$
    }

    /**
     * Fetches all archives available on the known remote sources.
     *
     * Used by {@link UpdaterData#listRemotePackages_NoGUI} and
     * {@link UpdaterData#updateOrInstallAll_NoGUI}.
     *
     * @param includeAll True to list and install all packages, including obsolete ones.
     * @return A list of potential {@link ArchiveInfo} to install.
     */
    private List<ArchiveInfo> getRemoteArchives_NoGUI(boolean includeAll) {
        refreshSources(true);
        getPackageLoader().loadRemoteAddonsList(new NullTaskMonitor(getSdkLog()));

        List<ArchiveInfo> archives;
        SdkUpdaterLogic ul = new SdkUpdaterLogic(this);

        if (includeAll) {
            archives = ul.getAllRemoteArchives(
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeAll);

        } else {
            archives = ul.computeUpdates(
                    null /*selectedArchives*/,
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeAll);

            ul.addNewPlatforms(
                    archives,
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeAll);
        }

        Collections.sort(archives);
        return archives;
    }

    /**
     * Lists remote packages available for install using
     * {@link UpdaterData#updateOrInstallAll_NoGUI}.
     *
     * @param includeAll True to list and install all packages, including obsolete ones.
     * @param extendedOutput True to display more details on each package.
     */
    public void listRemotePackages_NoGUI(boolean includeAll, boolean extendedOutput) {

        List<ArchiveInfo> archives = getRemoteArchives_NoGUI(includeAll);

        mSdkLog.info("Packages available for installation or update: %1$d\n", archives.size());

        int index = 1;
        for (ArchiveInfo ai : archives) {
            Archive a = ai.getNewArchive();
            if (a != null) {
                Package p = a.getParentPackage();
                if (p != null) {
                    if (extendedOutput) {
                        mSdkLog.info("----------\n");
                        mSdkLog.info("id: %1$d or \"%2$s\"\n", index, p.installId());
                        mSdkLog.info("     Type: %1$s\n",
                                p.getClass().getSimpleName().replaceAll("Package", "")); //$NON-NLS-1$ //$NON-NLS-2$
                        String desc = LineUtil.reformatLine("     Desc: %s\n",
                                p.getLongDescription());
                        mSdkLog.info("%s", desc); //$NON-NLS-1$
                    } else {
                        mSdkLog.info("%1$ 4d- %2$s\n",
                                index,
                                p.getShortDescription());
                    }
                    index++;
                }
            }
        }
    }

    /**
     * Tries to update all the *existing* local packages.
     * This version *requires* to be run with a GUI.
     * <p/>
     * There are two modes of operation:
     * <ul>
     * <li>If selectedArchives is null, refreshes all sources, compares the available remote
     * packages with the current local ones and suggest updates to be done to the user (including
     * new platforms that the users doesn't have yet).
     * <li>If selectedArchives is not null, this represents a list of archives/packages that
     * the user wants to install or update, so just process these.
     * </ul>
     *
     * @param selectedArchives The list of remote archives to consider for the update.
     *  This can be null, in which case a list of remote archive is fetched from all
     *  available sources.
     * @param includeObsoletes True if obsolete packages should be used when resolving what
     *  to update.
     * @param flags Optional flags for the installer, such as {@link #NO_TOOLS_MSG}.
     * @return A list of archives that have been installed. Can be null if nothing was done.
     */
    public List<Archive> updateOrInstallAll_WithGUI(
            Collection<Archive> selectedArchives,
            boolean includeObsoletes,
            int flags) {
        // FIXME revisit this logic. This is just an transitional implementation
        // while I refactor the way the sdk manager works internally.

        SdkUpdaterLogic ul = new SdkUpdaterLogic(this);
        List<ArchiveInfo> archives = ul.computeUpdates(
                selectedArchives,
                getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

        if (selectedArchives == null) {
            getPackageLoader().loadRemoteAddonsList(new NullTaskMonitor(getSdkLog()));
            ul.addNewPlatforms(
                    archives,
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeObsoletes);
        }

        Collections.sort(archives);

        if (archives.size() > 0) {
            return installArchives(archives, flags);
        }
        return null;
    }

    /**
     * Tries to update all the *existing* local packages.
     * This version is intended to run without a GUI and
     * only outputs to the current {@link ILogger}.
     *
     * @param pkgFilter A list of {@link SdkRepoConstants#NODES} or {@link Package#installId()}
     *   or package indexes to limit the packages we can update or install.
     *   A null or empty list means to update everything possible.
     * @param includeAll True to list and install all packages, including obsolete ones.
     * @param dryMode True to check what would be updated/installed but do not actually
     *   download or install anything.
     * @return A list of archives that have been installed. Can be null if nothing was done.
     */
    public List<Archive> updateOrInstallAll_NoGUI(
            Collection<String> pkgFilter,
            boolean includeAll,
            boolean dryMode) {

        List<ArchiveInfo> archives = getRemoteArchives_NoGUI(includeAll);

        // Filter the selected archives to only keep the ones matching the filter
        if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
            // Map filter types to an SdkRepository Package type,
            // e.g. create a map "platform" => PlatformPackage.class
            HashMap<String, Class<? extends Package>> pkgMap =
                new HashMap<String, Class<? extends Package>>();

            mapFilterToPackageClass(pkgMap, SdkRepoConstants.NODES);
            mapFilterToPackageClass(pkgMap, SdkAddonConstants.NODES);

            // Prepare a map install-id => package instance
            HashMap<String, Package> installIdMap = new HashMap<String, Package>();
            for (ArchiveInfo ai : archives) {
                Archive a = ai.getNewArchive();
                if (a != null) {
                    Package p = a.getParentPackage();
                    if (p != null) {
                        String id = p.installId();
                        if (id != null && id.length() > 0 && !installIdMap.containsKey(id)) {
                            installIdMap.put(id, p);
                        }
                    }
                }
            }

            // Now intersect this with the pkgFilter requested by the user, in order to
            // only keep the classes that the user wants to install.
            // We also create a set with the package indices requested by the user
            // and a set of install-ids requested by the user.

            HashSet<Class<? extends Package>> userFilteredClasses =
                new HashSet<Class<? extends Package>>();
            SparseIntArray userFilteredIndices = new SparseIntArray();
            Set<String> userFilteredInstallIds = new HashSet<String>();

            for (String type : pkgFilter) {
                if (installIdMap.containsKey(type)) {
                    userFilteredInstallIds.add(type);

                } else if (type.replaceAll("[0-9]+", "").length() == 0) {//$NON-NLS-1$ //$NON-NLS-2$
                    // An all-digit number is a package index requested by the user.
                    int index = Integer.parseInt(type);
                    userFilteredIndices.put(index, index);

                } else if (pkgMap.containsKey(type)) {
                    userFilteredClasses.add(pkgMap.get(type));

                } else {
                    // This should not happen unless there's a mismatch in the package map.
                    mSdkLog.error(null, "Ignoring unknown package filter '%1$s'", type);
                }
            }

            // we don't need the maps anymore
            pkgMap = null;
            installIdMap = null;

            // Now filter the remote archives list to keep:
            // - any package which class matches userFilteredClasses
            // - any package index which matches userFilteredIndices
            // - any package install id which matches userFilteredInstallIds

            int index = 1;
            for (Iterator<ArchiveInfo> it = archives.iterator(); it.hasNext(); ) {
                boolean keep = false;
                ArchiveInfo ai = it.next();
                Archive a = ai.getNewArchive();
                if (a != null) {
                    Package p = a.getParentPackage();
                    if (p != null) {
                        if (userFilteredInstallIds.contains(p.installId()) ||
                                userFilteredClasses.contains(p.getClass()) ||
                                userFilteredIndices.get(index) > 0) {
                            keep = true;
                        }

                        index++;
                    }
                }

                if (!keep) {
                    it.remove();
                }
            }

            if (archives.size() == 0) {
                mSdkLog.info(LineUtil.reflowLine(
                        "Warning: The package filter removed all packages. There is nothing to install.\nPlease consider trying to update again without a package filter.\n"));
                return null;
            }
        }

        if (archives != null && archives.size() > 0) {
            if (dryMode) {
                mSdkLog.info("Packages selected for install:\n");
                for (ArchiveInfo ai : archives) {
                    Archive a = ai.getNewArchive();
                    if (a != null) {
                        Package p = a.getParentPackage();
                        if (p != null) {
                            mSdkLog.info("- %1$s\n", p.getShortDescription());
                        }
                    }
                }
                mSdkLog.info("\nDry mode is on so nothing is actually being installed.\n");
            } else {
                return installArchives(archives, NO_TOOLS_MSG);
            }
        } else {
            mSdkLog.info("There is nothing to install or update.\n");
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void mapFilterToPackageClass(
            HashMap<String, Class<? extends Package>> inOutPkgMap,
            String[] nodes) {

        // Automatically find the classes matching the node names
        ClassLoader classLoader = getClass().getClassLoader();
        String basePackage = Package.class.getPackage().getName();

        for (String node : nodes) {
            // Capitalize the name
            String name = node.substring(0, 1).toUpperCase() + node.substring(1);

            // We can have one dash at most in a name. If it's present, we'll try
            // with the dash or with the next letter capitalized.
            int dash = name.indexOf('-');
            if (dash > 0) {
                name = name.replaceFirst("-", "");
            }

            for (int alternatives = 0; alternatives < 2; alternatives++) {

                String fqcn = basePackage + '.' + name + "Package";  //$NON-NLS-1$
                try {
                    Class<? extends Package> clazz =
                        (Class<? extends Package>) classLoader.loadClass(fqcn);
                    if (clazz != null) {
                        inOutPkgMap.put(node, clazz);
                        continue;
                    }
                } catch (Throwable ignore) {
                }

                if (alternatives == 0 && dash > 0) {
                    // Try an alternative where the next letter after the dash
                    // is converted to an upper case.
                    name = name.substring(0, dash) +
                           name.substring(dash, dash + 1).toUpperCase() +
                           name.substring(dash + 1);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Refresh all sources. This is invoked either internally (reusing an existing monitor)
     * or as a UI callback on the remote page "Refresh" button (in which case the monitor is
     * null and a new task should be created.)
     *
     * @param forceFetching When true, load sources that haven't been loaded yet.
     *                      When false, only refresh sources that have been loaded yet.
     */
    public void refreshSources(final boolean forceFetching) {
        assert mTaskFactory != null;

        final boolean forceHttp = getSettingsController().getSettings().getForceHttp();

        mTaskFactory.start("Refresh Sources", new ITask() {
            @Override
            public void run(ITaskMonitor monitor) {

                getPackageLoader().loadRemoteAddonsList(monitor);

                SdkSource[] sources = getSources().getAllSources();
                monitor.setDescription("Refresh Sources");
                monitor.setProgressMax(monitor.getProgress() + sources.length);
                for (SdkSource source : sources) {
                    if (forceFetching ||
                            source.getPackages() != null ||
                            source.getFetchError() != null) {
                        source.load(getDownloadCache(), monitor.createSubMonitor(1), forceHttp);
                    }
                    monitor.incProgress(1);
                }
            }
        });
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#onSdkLoaded()}.
     * This can be called from any thread.
     */
    public void broadcastOnSdkLoaded() {
        if (mListeners.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.onSdkLoaded();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#onSdkReload()}.
     * This can be called from any thread.
     */
    private void broadcastOnSdkReload() {
        if (mListeners.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.onSdkReload();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#preInstallHook()}.
     * This can be called from any thread.
     */
    private void broadcastPreInstallHook() {
        if (mListeners.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.preInstallHook();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#postInstallHook()}.
     * This can be called from any thread.
     */
    private void broadcastPostInstallHook() {
        if (mListeners.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.postInstallHook();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Internal helper to return a new {@link ArchiveInstaller}.
     * This allows us to override the installer for unit-testing.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    protected ArchiveInstaller createArchiveInstaler() {
        return new ArchiveInstaller();
    }
}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/repository/ISdkChangeListener.java b/sdklib/src/main/java/com/android/sdklib/repository/ISdkChangeListener.java
new file mode 100755
//Synthetic comment -- index 0000000..5c0cab8

//Synthetic comment -- @@ -0,0 +1,54 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.sdklib.repository;


/**
 * Interface for listeners on SDK modifications by the SDK Manager UI.
 * This notifies when the SDK manager is first loading the SDK or before/after it installed
 * a package.
 */
public interface ISdkChangeListener {
    /**
     * Invoked when the content of the SDK is being loaded by the SDK Manager UI
     * for the first time.
     * This is generally followed by a call to {@link #onSdkReload()}
     * or by a call to {@link #preInstallHook()}.
     */
    void onSdkLoaded();

    /**
     * Invoked when the SDK Manager UI is about to start installing packages.
     * This will be followed by a call to {@link #postInstallHook()}.
     */
    void preInstallHook();

    /**
     * Invoked when the SDK Manager UI is done installing packages.
     * Some new packages might have been installed or the user might have cancelled the operation.
     * This is generally followed by a call to {@link #onSdkReload()}.
     */
    void postInstallHook();

    /**
     * Invoked when the content of the SDK is being reloaded by the SDK Manager UI,
     * typically after a package was installed. The SDK content might or might not
     * have changed.
     */
    void onSdkReload();
}









//Synthetic comment -- diff --git a/sdklib/src/test/java/com/android/sdklib/internal/repository/MockDownloadCache.java b/sdklib/src/test/java/com/android/sdklib/internal/repository/MockDownloadCache.java
new file mode 100755
//Synthetic comment -- index 0000000..78e5b85

//Synthetic comment -- @@ -0,0 +1,236 @@
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

package com.android.sdklib.internal.repository;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.internal.repository.CanceledByUserException;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.utils.Pair;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/** A mock UpdaterData that simply records what would have been installed. */
public class MockDownloadCache extends DownloadCache {

    private final File mCacheRoot;

    /** Map url => payload bytes, http code response.
     *  If the payload pair is null, an exception such as FNF is thrown. */
    private final Map<String, Payload> mDirectPayloads = new HashMap<String, Payload>();
    /** Map url => payload bytes, http code response.
     *  If the payload pair is null, an exception such as FNF is thrown. */
    private final Map<String, Payload> mCachedPayloads = new HashMap<String, Payload>();

    private final Map<String, Integer> mDirectHits = new TreeMap<String, Integer>();
    private final Map<String, Integer> mCachedHits = new TreeMap<String, Integer>();

    private Strategy mOverrideStrategy;

    public final static int THROW_FNF = -1;

    /**
     * Creates a download cache with a {@code DIRECT} strategy and
     * no root {@code $HOME/.android} folder, which effectively disables the cache.
     */
    public MockDownloadCache() {
        super(DownloadCache.Strategy.DIRECT);
        mCacheRoot = null;
    }

    /**
     * Creates a download with the given strategy and the given cache root.
     */
    public MockDownloadCache(DownloadCache.Strategy strategy, File cacheRoot) {
        super(strategy);
        mCacheRoot = cacheRoot;
    }

    @Override
    protected File initCacheRoot() {
        return mCacheRoot;
    }

    /**
     * Override the {@link DownloadCache.Strategy} of the cache.
     * This lets us set it temporarily to {@link DownloadCache.Strategy#ONLY_CACHE},
     * which will force {@link #openCachedUrl(String, ITaskMonitor)} to throw an FNF,
     * essentially simulating an empty cache at first.
     * <p/>
     * Setting it back to null reverts the behavior to its default.
     */
    public void overrideStrategy(DownloadCache.Strategy strategy) {
        mOverrideStrategy = strategy;
    }

    /**
     * Register a direct payload response.
     *
     * @param url The URL to match.
     * @param httpCode The expected response code.
     *                 Use {@link #THROW_FNF} to mean an FNF should be thrown (which is what the
     *                 httpClient stack seems to return instead of {@link HttpStatus#SC_NOT_FOUND}.)
     * @param content The payload to return.
     *                As a shortcut a null will be replaced by an empty byte array.
     */
    public void registerDirectPayload(String url, int httpCode, byte[] content) {
        mDirectPayloads.put(url, new Payload(httpCode, content));
    }

    /**
     * Register a cached payload response.
     *
     * @param url The URL to match.
     * @param content The payload to return or null to throw a FNF.
     */
    public void registerCachedPayload(String url, byte[] content) {
        mCachedPayloads.put(url,
                new Payload(content == null ? THROW_FNF : HttpStatus.SC_OK, content));
    }

    public String[] getDirectHits() {
        ArrayList<String> list = new ArrayList<String>();
        synchronized (mDirectHits) {
            for (Entry<String, Integer> entry : mDirectHits.entrySet()) {
                list.add(String.format("<%1$s : %2$d>",
                        entry.getKey(), entry.getValue().intValue()));
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public String[] getCachedHits() {
        ArrayList<String> list = new ArrayList<String>();
        synchronized (mCachedHits) {
            for (Entry<String, Integer> entry : mCachedHits.entrySet()) {
                list.add(String.format("<%1$s : %2$d>",
                        entry.getKey(), entry.getValue().intValue()));
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void clearDirectHits() {
        synchronized (mDirectHits) {
            mDirectHits.clear();
        }
    }

    public void clearCachedHits() {
        synchronized (mCachedHits) {
            mCachedHits.clear();
        }
    }

    /**
     * Override openDirectUrl to return one of the registered payloads or throw a FNF exception.
     * This totally ignores the cache's {@link DownloadCache.Strategy}.
     */
    @Override
    public Pair<InputStream, HttpResponse> openDirectUrl(
            @NonNull String urlString,
            @Nullable Header[] headers,
            @NonNull ITaskMonitor monitor) throws IOException, CanceledByUserException {

        synchronized (mDirectHits) {
            Integer count = mDirectHits.get(urlString);
            mDirectHits.put(urlString, (count == null ? 0 : count.intValue()) + 1);
        }

        Payload payload = mDirectPayloads.get(urlString);

        if (payload == null || payload.mHttpCode == THROW_FNF) {
            throw new FileNotFoundException(urlString);
        }

        byte[] content = payload.mContent;
        if (content == null) {
            content = new byte[0];
        }

        InputStream is  = new ByteArrayInputStream(content);
        HttpResponse hr = new BasicHttpResponse(
                new ProtocolVersion("HTTP", 1, 1),
                payload.mHttpCode,
                "Http-Code-" + payload.mHttpCode);

        return Pair.of(is, hr);
    }

    /**
     * Override openCachedUrl to return one of the registered payloads or throw a FNF exception.
     * This totally ignores the cache's {@link DownloadCache.Strategy}.
     * It will however throw a FNF if {@link #overrideStrategy(Strategy)} is set to
     * {@link DownloadCache.Strategy#ONLY_CACHE}.
     */
    @Override
    public InputStream openCachedUrl(String urlString, ITaskMonitor monitor)
            throws IOException, CanceledByUserException {

        synchronized (mCachedHits) {
            Integer count = mCachedHits.get(urlString);
            mCachedHits.put(urlString, (count == null ? 0 : count.intValue()) + 1);
        }

        if (Strategy.ONLY_CACHE.equals(mOverrideStrategy)) {
            // Override the cache to read only "local cached" data.
            // In this first phase, we assume there's nothing cached.
            // TODO register first-pass files later.
            throw new FileNotFoundException(urlString);
        }

        Payload payload = mCachedPayloads.get(urlString);

        if (payload == null || payload.mHttpCode != HttpStatus.SC_OK) {
            throw new FileNotFoundException(urlString);
        }

        byte[] content = payload.mContent;
        if (content == null) {
            content = new byte[0];
        }

        return new ByteArrayInputStream(content);
    }

    private static class Payload {
        final byte[] mContent;
        final int    mHttpCode;

        Payload(int httpCode, byte[] content) {
            mHttpCode = httpCode;
            mContent = content;
        }
    }

}







