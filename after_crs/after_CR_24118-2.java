/*Sdkman2: Load sources asynchronously.

[not ready for review]

Change-Id:Ic79b987ce8594e7bfe30b32e961b9384e2538b6b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index faf5a00..d50d358 100755

//Synthetic comment -- @@ -72,7 +72,9 @@
public static enum UpdateInfo {
/** Means that the 2 packages are not the same thing */
INCOMPATIBLE,
        /** Means that the 2 packages are the same thing but one does not upgrade the other.
         *  </p>
         *  TODO: this name is confusing. We need to dig deeper. */
NOT_UPDATE,
/** Means that the 2 packages are the same thing, and one is the upgrade of the other */
UPDATE;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index be99f22..d4622ad 100755

//Synthetic comment -- @@ -51,30 +51,33 @@
* Adds a new source to the Sources list.
*/
public void add(SdkSourceCategory category, SdkSource source) {
        synchronized(mSources) {
            ArrayList<SdkSource> list = mSources.get(category);
            if (list == null) {
                list = new ArrayList<SdkSource>();
                mSources.put(category, list);
            }

            list.add(source);
}
}

/**
* Removes a source from the Sources list.
*/
public void remove(SdkSource source) {
        synchronized(mSources) {
            Iterator<Entry<SdkSourceCategory, ArrayList<SdkSource>>> it =
                mSources.entrySet().iterator();
            while (it.hasNext()) {
                Entry<SdkSourceCategory, ArrayList<SdkSource>> entry = it.next();
                ArrayList<SdkSource> list = entry.getValue();

                if (list.remove(source)) {
                    if (list.isEmpty()) {
                        // remove the entry since the source list became empty
                        it.remove();
                    }
}
}
}
//Synthetic comment -- @@ -84,7 +87,9 @@
* Removes all the sources in the given category.
*/
public void removeAll(SdkSourceCategory category) {
        synchronized(mSources) {
            mSources.remove(category);
        }
}

/**
//Synthetic comment -- @@ -100,9 +105,11 @@
if (cat.getAlwaysDisplay()) {
cats.add(cat);
} else {
                synchronized(mSources) {
                    ArrayList<SdkSource> list = mSources.get(cat);
                    if (list != null && !list.isEmpty()) {
                        cats.add(cat);
                    }
}
}
}
//Synthetic comment -- @@ -115,11 +122,13 @@
* Might return an empty array, but never returns null.
*/
public SdkSource[] getSources(SdkSourceCategory category) {
        synchronized(mSources) {
            ArrayList<SdkSource> list = mSources.get(category);
            if (list == null) {
                return new SdkSource[0];
            } else {
                return list.toArray(new SdkSource[list.size()]);
            }
}
}

//Synthetic comment -- @@ -127,22 +136,24 @@
* Returns an array of the sources across all categories. This is never null.
*/
public SdkSource[] getAllSources() {
        synchronized(mSources) {
            int n = 0;

            for (ArrayList<SdkSource> list : mSources.values()) {
                n += list.size();
}

            SdkSource[] sources = new SdkSource[n];

            int i = 0;
            for (ArrayList<SdkSource> list : mSources.values()) {
                for (SdkSource source : list) {
                    sources[i++] = source;
                }
            }

            return sources;
        }
}

/**
//Synthetic comment -- @@ -155,9 +166,11 @@
*/
public SdkSourceCategory getCategory(SdkSource source) {
if (source != null) {
            synchronized(mSources) {
                for (Entry<SdkSourceCategory, ArrayList<SdkSource>> entry : mSources.entrySet()) {
                    if (entry.getValue().contains(source)) {
                        return entry.getKey();
                    }
}
}
}
//Synthetic comment -- @@ -175,14 +188,16 @@
* The search is O(N), which should be acceptable on the expectedly small source list.
*/
public boolean hasSourceUrl(SdkSource source) {
        synchronized(mSources) {
            for (ArrayList<SdkSource> list : mSources.values()) {
                for (SdkSource s : list) {
                    if (s.equals(source)) {
                        return true;
                    }
}
}
            return false;
}
}

/**
//Synthetic comment -- @@ -196,15 +211,17 @@
* The search is O(N), which should be acceptable on the expectedly small source list.
*/
public boolean hasSourceUrl(SdkSourceCategory category, SdkSource source) {
        synchronized(mSources) {
            ArrayList<SdkSource> list = mSources.get(category);
            if (list != null) {
                for (SdkSource s : list) {
                    if (s.equals(source)) {
                        return true;
                    }
}
}
            return false;
}
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java
//Synthetic comment -- index 0b4879b..2314446 100755

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.sdklib.internal.repository.ExtraPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdkuilib.internal.repository.PackageLoader.IAutoInstallTask;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -66,7 +66,7 @@
private File    mResultPath = null;
private SettingsController mSettingsController;
private PackageFilter mPackageFilter;
    private PackageLoader mPackageMananger;

private ProgressBar mProgressBar;
private Label mStatusText;
//Synthetic comment -- @@ -132,6 +132,8 @@
shell.setMinimumSize(new Point(450, 100));
shell.setSize(450, 100);

        mUpdaterData.setWindowShell(shell);

GridLayoutBuilder.create(shell).columns(1);

Composite composite1 = new Composite(shell, SWT.NONE);
//Synthetic comment -- @@ -166,19 +168,12 @@

mUpdaterData.broadcastOnSdkLoaded();

        mPackageMananger = new PackageLoader(mUpdaterData);
}

@Override
protected void eventLoop() {
        mPackageMananger.loadPackagesWithInstallTask(new IAutoInstallTask() {
public boolean acceptPackage(Package pkg) {
// Is this the package we want to install?
return mPackageFilter.accept(pkg);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java
new file mode 100755
//Synthetic comment -- index 0000000..935f59d

//Synthetic comment -- @@ -0,0 +1,537 @@
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

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.IPackageVersion;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.Package.UpdateInfo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads packages fetched from the remote SDK Repository and keeps track
 * of their state compared with the current local SDK installation.
 */
class PackageLoader {

    private final UpdaterData mUpdaterData;

    /**
     * Interface for the callback called by
     * {@link PackageLoader#loadPackages(ISourceLoadedCallback)}.
     * <p/>
     * After processing each source, the package loader calls {@link #onSouceLoaded(List)}
     * with the list of package items found in that source. The client should process that
     * list as it want, typically by accumulating the package items in a list of its own.
     * By returning true from {@link #onSouceLoaded(List)}, the client tells the loader to
     * continue and process the next source. By returning false, it tells to stop loading.
     * <p/>
     * The {@link #onLoadCompleted()} method is guaranteed to be called at the end, no
     * matter how the loader stopped, so that the client can clean up or perform any
     * final action.
     */
    public interface ISourceLoadedCallback {
        /**
         * After processing each source, the package loader calls this method with the
         * list of package items found in that source. The client should process that
         * list as it want, typically by accumulating the package items in a list of its own.
         * By returning true from {@link #onSouceLoaded(List)}, the client tells the loader to
         * continue and process the next source. By returning false, it tells to stop loading.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@link Display#syncExec(Runnable)}
         * or {@link Display#asyncExec(Runnable)}.
         *
         * @param pkgItems All the package items loaded from the last processed source.
         *  This is a copy and the client can hold to this list or modify it in any way.
         * @return True if the load operation should continue, false if it should stop.
         */
        public boolean onSouceLoaded(List<PkgItem> pkgItems);

        /**
         * This method is guaranteed to be called at the end, no matter how the
         * loader stopped, so that the client can clean up or perform any final action.
         */
        public void onLoadCompleted();
    }

    /**
     * Interface describing the task of installing a specific package.
     * For details on the operation,
     * see {@link PackageLoader#loadPackagesWithInstallTask(IAutoInstallTask)}.
     *
     * @see PackageLoader#loadPackagesWithInstallTask(IAutoInstallTask)
     */
    public interface IAutoInstallTask {
        /**
         * Called by the install task for every package available (new ones, updates as well
         * as existing ones that don't have a potential update.)
         * The method should return true if this is the package that should be installed,
         * at which point the packager loader will stop processing the next packages and sources.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@link Display#syncExec(Runnable)}
         * or {@link Display#asyncExec(Runnable)}.
         */
        public boolean acceptPackage(Package pkg);

        /**
         * Called when the accepted package has been installed, successfully or not.
         * If an already installed (aka existing) package has been accepted, this will
         * be called with a 'true' success and the actual install path.
         * <p/>
         * <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@link Display#syncExec(Runnable)}
         * or {@link Display#asyncExec(Runnable)}.
         */
        public void setResult(Package pkg, boolean success, File installPath);

        /**
         * Called when the task is done iterating and completed.
         */
        public void taskCompleted();
    }

    /**
     * Creates a new PackageManager associated with the given {@link UpdaterData}.
     *
     * @param updaterData The {@link UpdaterData}. Must not be null.
     */
    public PackageLoader(UpdaterData updaterData) {
        mUpdaterData = updaterData;
    }

    /**
     * Loads all packages from the remote repository.
     * This runs in an {@link ITask}. The call is blocking.
     * <p/>
     * The callback is called with each set of {@link PkgItem} found in each source.
     * The caller is responsible to accumulate the packages given to the callback
     * after each source is finished loaded. In return the callback tells the loader
     * whether to continue loading sources.
     */
    public void loadPackages(final ISourceLoadedCallback sourceLoadedCallback) {
        try {
            if (mUpdaterData == null) {
                return;
            }

            // get local packages and offer them to the callback
            final List<PkgItem> allPkgItems =  loadLocalPackages();
            if (!allPkgItems.isEmpty()) {
                // Notify the callback by giving it a copy of the current list.
                // (in case the callback holds to the list... we still need this list of
                // ourselves below).
                if (!sourceLoadedCallback.onSouceLoaded(new ArrayList<PkgItem>(allPkgItems))) {
                    return;
                }
            }

            // get remote packages
            final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();
            mUpdaterData.loadRemoteAddonsList();
            mUpdaterData.getTaskFactory().start("Loading Sources", new ITask() {
                public void run(ITaskMonitor monitor) {
                    SdkSource[] sources = mUpdaterData.getSources().getAllSources();
                    try {
                        for (SdkSource source : sources) {
                            Package[] pkgs = source.getPackages();
                            if (pkgs == null) {
                                source.load(monitor, forceHttp);
                                pkgs = source.getPackages();
                            }
                            if (pkgs == null) {
                                continue;
                            }

                            List<PkgItem> sourcePkgItems = new ArrayList<PkgItem>();

                            nextPkg: for(Package pkg : pkgs) {
                                boolean isUpdate = false;
                                for (PkgItem pi: allPkgItems) {
                                    if (pi.isSamePackageAs(pkg)) {
                                        continue nextPkg;
                                    }
                                    if (pi.isUpdatedBy(pkg)) {
                                        isUpdate = true;
                                        break;
                                    }
                                }

                                if (!isUpdate) {
                                    PkgItem pi = new PkgItem(pkg, PkgState.NEW);
                                    sourcePkgItems.add(pi);
                                    allPkgItems.add(pi);
                                }
                            }

                            // Notify the callback a new source has finished loading.
                            // If the callback requests so, stop right away.
                            if (!sourceLoadedCallback.onSouceLoaded(sourcePkgItems)) {
                                return;
                            }
                        }
                    } catch(Exception e) {
                        monitor.logError("Loading source failed: %1$s", e.toString());
                    } finally {
                        monitor.setDescription("Done loading %1$d packages from %2$d sources",
                                allPkgItems.size(),
                                sources.length);
                    }
                }
            });
        } finally {
            sourceLoadedCallback.onLoadCompleted();
        }
    }

    /**
     * Internal method that returns all installed packages from the {@link LocalSdkParser}
     * associated with the {@link UpdaterData}.
     * <p/>
     * Note that the {@link LocalSdkParser} maintains a cache, so callers need to clear
     * it if they know they changed the local installation.
     *
     * @return A new list of {@link PkgItem}. May be empty but never null.
     */
    private List<PkgItem> loadLocalPackages() {
        List<PkgItem> pkgItems = new ArrayList<PkgItem>();

        for (Package pkg : mUpdaterData.getInstalledPackages()) {
            PkgItem pi = new PkgItem(pkg, PkgState.INSTALLED);
            pkgItems.add(pi);
        }

        return pkgItems;
    }

    /**
     * Load packages, source by source using {@link #loadPackages(ISourceLoadedCallback)},
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
     * <em>Important</em>: Since some UI will be displayed to install the selected package,
     * the {@link UpdaterData} must have a window {@link Shell} associated using
     * {@link UpdaterData#setWindowShell(Shell)}.
     * <p/>
     * The call is blocking. Although the name says "Task", this is not an {@link ITask}
     * running in its own thread but merely a synchronous call.
     *
     * @param installTask The task to perform.
     */
    public void loadPackagesWithInstallTask(final IAutoInstallTask installTask) {

        loadPackages(new ISourceLoadedCallback() {
            public boolean onSouceLoaded(List<PkgItem> pkgItems) {
                for (PkgItem item : pkgItems) {
                    Package acceptedPkg = null;
                    switch(item.getState()) {
                    case NEW:
                        if (installTask.acceptPackage(item.getPackage())) {
                            acceptedPkg = item.getPackage();
                        }
                        break;
                    case HAS_UPDATE:
                        for (Package upd : item.getUpdatePkgs()) {
                            if (installTask.acceptPackage(upd)) {
                                acceptedPkg = upd;
                                break;
                            }
                        }
                        break;
                    case INSTALLED:
                        if (installTask.acceptPackage(item.getPackage())) {
                            // If the caller is accepting an installed package,
                            // return a success and give the package's install path
                            acceptedPkg = item.getPackage();
                            Archive[] a = acceptedPkg.getArchives();
                            // an installed package should have one local compatible archive
                            if (a.length == 1 && a[0].isCompatible()) {
                                installTask.setResult(
                                        acceptedPkg,
                                        true /*success*/,
                                        new File(a[0].getLocalOsPath()));

                                // return false to tell loadPackages() that we don't
                                // need to continue processing any more sources.
                                return false;
                            }
                        }
                    }

                    if (acceptedPkg != null) {
                        // Try to install this package if it has one compatible archive.
                        Archive a = null;

                        for (Archive a2 : acceptedPkg.getArchives()) {
                            if (a2.isCompatible()) {
                                a = a2;
                                break;
                            }
                        }

                        if (a != null) {
                            final ArrayList<Archive> archives = new ArrayList<Archive>();
                            archives.add(a);

                            // Actually install the new package that we just found.
                            mUpdaterData.getWindowShell().getDisplay().syncExec(new Runnable() {
                                public void run() {
                                    mUpdaterData.updateOrInstallAll_WithGUI(
                                            archives,
                                            true /* includeObsoletes */);
                                }
                            });

                            // The local package list has changed, make sure to refresh it
                            mUpdaterData.getLocalSdkParser().clearPackages();
                            final List<PkgItem> localPkgItems = loadLocalPackages();

                            // Try to locate the installed package in the new package list
                            for (PkgItem localItem : localPkgItems) {
                                if (localItem.getState() == PkgState.INSTALLED) {
                                    Package localPpkg = localItem.getPackage();
                                    if (localPpkg.canBeUpdatedBy(acceptedPkg) ==
                                            UpdateInfo.NOT_UPDATE) {
                                        Archive[] a2 = acceptedPkg.getArchives();
                                        if (a2.length == 1 && a2[0].isCompatible()) {
                                            installTask.setResult(
                                                    acceptedPkg,
                                                    true /*success*/,
                                                    new File(a2[0].getLocalOsPath()));
                                            // return false to tell loadPackages() that we don't
                                            // need to continue processing any more sources.
                                            return false;
                                        }
                                    }
                                }
                            }

                            // We failed to find the installed package.
                            // We'll assume we failed to install it.
                            installTask.setResult(acceptedPkg, false /*success*/, null);

                            // return false to tell loadPackages() that we don't
                            // need to continue processing any more sources.
                            return false;
                        }
                    }

                }
                // Tell loadPackages() to process the next source.
                return true;
            }

            public void onLoadCompleted() {
                installTask.taskCompleted();
            }
        });

    }

    /**
     * The state of the a given {@link PkgItem}, that is the relationship between
     * a given remote package and the local repository.
     */
    public enum PkgState {
        /**
         * Package is locally installed and has no update available on remote sites.
         */
        INSTALLED,

        /**
         * Package is installed and has an update available.
         * In this case, {@link PkgItem#getUpdatePkgs()} provides the list of 1 or more
         * packages that can update this {@link PkgItem}.
         * <p/>
         * Although not structurally enforced, it can be reasonably expected that
         * the original package and the updating packages all come from the same source.
         */
        HAS_UPDATE,

        /**
         * There's a new package available on the remote site that isn't
         * installed locally.
         */
        NEW
    }

    /**
     * A {@link PkgItem} represents one {@link Package} combined with its state.
     * <p/>
     * It can be either a locally installed package, or a remotely available package.
     * If the later, it can be either a new package or an update for a locally installed one.
     * <p/>
     * In the case of an update, the {@link PkgItem#getPackage()} represents the currently
     * installed package and there's a separate list of {@link PkgItem#getUpdatePkgs()} that
     * links to the updating packages. Note that in a typical repository there should only
     * one update for a given installed package however the system is designed to be more
     * generic and allow many.
     */
    public static class PkgItem implements Comparable<PkgItem> {
        private final Package mPkg;
        private PkgState mState;
        private List<Package> mUpdatePkgs;

        public PkgItem(Package pkg, PkgState state) {
            mPkg = pkg;
            mState = state;
            assert mPkg != null;
        }

        public boolean isObsolete() {
            return mPkg.isObsolete();
        }

        public boolean isSameItemAs(PkgItem item) {
            boolean same = this.mState == item.mState;
            if (same) {
                same = isSamePackageAs(item.getPackage());
            }
            // check updating packages are the same
            if (same) {
                List<Package> u1 = getUpdatePkgs();
                List<Package> u2 = item.getUpdatePkgs();
                same = (u1 == null && u2 == null) ||
                       (u1 != null && u2 != null);
                if (same && u1 != null && u2 != null) {
                    int n = u1.size();
                    same = n == u2.size();
                    if (same) {
                        for (int i = 0; same && i < n; i++) {
                            Package p1 = u1.get(i);
                            Package p2 = u2.get(i);
                            same = p1.canBeUpdatedBy(p2) == UpdateInfo.NOT_UPDATE;
                        }
                    }
                }
            }

            return same;
        }

        public boolean isSamePackageAs(Package pkg) {
            return mPkg.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE;
        }

        /**
         * Check whether the 'pkg' argument updates this package.
         * If it does, record it as a sub-package.
         * Returns true if it was recorded as an update, false otherwise.
         */
        public boolean isUpdatedBy(Package pkg) {
            if (mPkg.canBeUpdatedBy(pkg) == UpdateInfo.UPDATE) {
                if (mUpdatePkgs == null) {
                    mUpdatePkgs = new ArrayList<Package>();
                }
                mUpdatePkgs.add(pkg);
                mState = PkgState.HAS_UPDATE;
                return true;
            }

            return false;
        }

        public String getName() {
            return mPkg.getListDescription();
        }

        public int getRevision() {
            return mPkg.getRevision();
        }

        public String getDescription() {
            return mPkg.getDescription();
        }

        public Package getPackage() {
            return mPkg;
        }

        public PkgState getState() {
            return mState;
        }

        public SdkSource getSource() {
            if (mState == PkgState.NEW) {
                return mPkg.getParentSource();
            } else {
                return null;
            }
        }

        public int getApi() {
            return mPkg instanceof IPackageVersion ?
                    ((IPackageVersion) mPkg).getVersion().getApiLevel() :
                        -1;
        }

        public List<Package> getUpdatePkgs() {
            return mUpdatePkgs;
        }

        public Archive[] getArchives() {
            return mPkg.getArchives();
        }

        public int compareTo(PkgItem pkg) {
            return getPackage().compareTo(pkg.getPackage());
        }

        /** Returns a string representation of this item, useful when debugging. */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(mState.toString());

            if (mPkg != null) {
                sb.append(", pkg:"); //$NON-NLS-1$
                sb.append(mPkg.toString());
            }

            if (mUpdatePkgs != null && !mUpdatePkgs.isEmpty()) {
                sb.append(", updated by:"); //$NON-NLS-1$
                sb.append(Arrays.toString(mUpdatePkgs.toArray()));
            }

            return sb.toString();
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageManager.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageManager.java
deleted file mode 100755
//Synthetic comment -- index 0447385..0000000

//Synthetic comment -- @@ -1,399 +0,0 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 2b7a0db..dd4a0c9 100755

//Synthetic comment -- @@ -26,8 +26,9 @@
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdkuilib.internal.repository.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.PackageLoader.PkgItem;
import com.android.sdkuilib.internal.repository.PackageLoader.PkgState;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;
//Synthetic comment -- @@ -65,7 +66,6 @@

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
//Synthetic comment -- @@ -74,6 +74,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

/**
//Synthetic comment -- @@ -122,8 +123,11 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

    private final PackageLoader mPackageLoader;

private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
    /** Access to this list must be synchronized on {@link #mPackages}. */
    private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
private final UpdaterData mUpdaterData;

private boolean mDisplayArchives = false;
//Synthetic comment -- @@ -153,14 +157,14 @@
super(parent, swtStyle);

mUpdaterData = updaterData;
        mPackageLoader = new PackageLoader(updaterData);
createContents(this);

postCreate();  //$hide$
}

public void onPageSelected() {
        if (mPackages.isEmpty()) {
// Initialize the package list the first time the page is shown.
loadPackages();
}
//Synthetic comment -- @@ -494,31 +498,66 @@
return;
}

        final boolean firstLoad = mPackages.isEmpty();

        // Load package is synchronous but does not block the UI.
        // Consequently it's entirely possible for the user
        // to request the app to close whilst the packages are loading. Any
        // action done after loadPackages must check the UI hasn't been
        // disposed yet. Otherwise hilarity ensues.

        mPackageLoader.loadPackages(new ISourceLoadedCallback() {
            public boolean onSouceLoaded(List<PkgItem> newPkgItems) {
                boolean somethingNew = false;

                synchronized(mPackages) {
                    nextNewItem: for (PkgItem newItem : newPkgItems) {
                        for (PkgItem existingItem : mPackages) {
                            if (existingItem.isSameItemAs(newItem)) {
                                // This isn't a new package, we already have it.
                                continue nextNewItem;
                            }
                        }
                        mPackages.add(newItem);
                        somethingNew = true;
                    }
                }

                if (somethingNew) {
                    // Dynamically update the table while we load after each source.
                    // Since the official Android source gets loaded first, it makes the
                    // window look non-empty a lot sooner.
                    if (!mGroupPackages.isDisposed()) {
                        mGroupPackages.getDisplay().syncExec(new Runnable() {
                            public void run() {
                                sortPackages(true /* updateButtons */);

                                if (!mGroupPackages.isDisposed()) {
                                    if (firstLoad) {
                                        // set the initial expanded state
                                        expandInitial(mCategories);
                                    }
                                    updateButtonsState();
                                    updateMenuCheckmarks();
                                }
                            }
                        });
                    }
                }

                // Return true to tell the loader to continue with the next source.
                // Return false to stop the loader if any UI has been disposed, which can
                // happen if the user is trying to close the window during the load operation.
                return !mGroupPackages.isDisposed();
}

            public void onLoadCompleted() {
                if (firstLoad && !mGroupPackages.isDisposed()) {
                    updateButtonsState();
                    updateMenuCheckmarks();
                }
}
        });
}

private void enableUi(Composite root, boolean enabled) {
//Synthetic comment -- @@ -602,67 +641,69 @@
mCategories.add(cat);
}

        synchronized (mPackages) {
            for (PkgItem item : mPackages) {
                if (!keepItem(item)) {
                    continue;
}

                int apiKey = item.getApi();

                if (apiKey < 1) {
                    Package p = item.getPackage();
                    if (p instanceof ToolPackage || p instanceof PlatformToolPackage) {
                        apiKey = PkgApiCategory.KEY_TOOLS;
                    } else {
                        apiKey = PkgApiCategory.KEY_EXTRA;
}
}

                Pair<PkgApiCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apiKey);

                if (mapEntry == null) {
                    // This is a new category. Create it and add it to the map.

                    // We need a label for the category.
                    // If we have an API level, try to get the info from the SDK Manager.
                    // If we don't (e.g. when installing a new platform that isn't yet available
                    // locally in the SDK Manager), it's OK we'll try to find the first platform
                    // package available.
                    String platformName = null;
                    if (apiKey != -1) {
                        for (IAndroidTarget target : mUpdaterData.getSdkManager().getTargets()) {
                            if (target.isPlatform() && target.getVersion().getApiLevel() == apiKey) {
                                platformName = target.getVersionName();
                                break;
                            }
                        }
                    }

                    PkgApiCategory cat = new PkgApiCategory(
                            apiKey,
                            platformName,
                            imgFactory.getImageByName(ICON_CAT_PLATFORM));
                    mapEntry = Pair.of(cat, new HashSet<PkgItem>());
                    unusedItemsMap.put(apiKey, mapEntry);
                    mCategories.add(0, cat);
                }
                PkgApiCategory cat = mapEntry.getFirst();
                assert cat != null;
                unusedCatSet.remove(cat);

                HashSet<PkgItem> unusedItemsSet = mapEntry.getSecond();
                unusedItemsSet.remove(item);
                if (!cat.getItems().contains(item)) {
                    cat.getItems().add(item);
                }

                if (apiKey != -1 && cat.getPlatformName() == null) {
                    // Check whether we can get the actual platform version name (e.g. "1.5")
                    // from the first Platform package we find in this category.
                    Package p = item.getPackage();
                    if (p instanceof PlatformPackage) {
                        String platformName = ((PlatformPackage) p).getVersionName();
                        cat.setPlatformName(platformName);
                    }
}
}
}
//Synthetic comment -- @@ -726,15 +767,25 @@
mLastSortWasByApi = false;
mCategories.clear();

        Map<SdkSource, List<PkgItem>> sourceMap = new HashMap<SdkSource, List<PkgItem>>();

        synchronized(mPackages) {
            for (PkgItem item : mPackages) {
                if (keepItem(item)) {
                    SdkSource source = item.getSource();
                    List<PkgItem> list = sourceMap.get(source);
                    if (list == null) {
                        list = new ArrayList<PkgItem>();
                        sourceMap.put(source, list);
                    }
                    list.add(item);
                }
}
}

        // Sort the sources so that we can create categories sorted the same way
        // (the categories don't link to the sources, so we can't just sort the categories.)
        Set<SdkSource> sources = new TreeSet<SdkSource>(new Comparator<SdkSource>() {
public int compare(SdkSource o1, SdkSource o2) {
if (o1 == o2) {
return 0;
//Synthetic comment -- @@ -747,6 +798,7 @@
return o1.toString().compareTo(o2.toString());
}
});
        sources.addAll(sourceMap.keySet());

for (SdkSource source : sources) {
Object key = source != null ? source : "Locally Installed Packages";
//Synthetic comment -- @@ -758,7 +810,7 @@
key.toString(),
iconRef);

            for (PkgItem item : sourceMap.get(source)) {
if (item.getSource() == source) {
cat.getItems().add(item);
}
//Synthetic comment -- @@ -801,6 +853,10 @@
/**
* Performs the initial expansion of the tree. This expands categories that contain
* at least one installed item and collapses the ones with nothing installed.
     *
     * TODO: change this to only change the expanded state on categories that have not
     * been touched by the user yet. Once we do that, call this every time a new source
     * is added or the list is reloaded.
*/
private void expandInitial(Object elem) {
mTreeViewer.setExpandedState(elem, true);
//Synthetic comment -- @@ -989,9 +1045,10 @@
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */);
} finally {
                enableUi(mGroupPackages, true);

// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();
loadPackages();
}
}
//Synthetic comment -- @@ -1055,9 +1112,10 @@
}
});
} finally {
                    enableUi(mGroupPackages, true);

// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();
loadPackages();
}
}
//Synthetic comment -- @@ -1403,28 +1461,6 @@
}
}


// --- Implementation of ISdkChangeListener ---








