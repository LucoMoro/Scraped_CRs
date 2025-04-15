/*Sdkman2: Load sources asynchronously.

[new ready for review]

Change-Id:Ic79b987ce8594e7bfe30b32e961b9384e2538b6b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index faf5a00..d50d358 100755

//Synthetic comment -- @@ -72,7 +72,9 @@
public static enum UpdateInfo {
/** Means that the 2 packages are not the same thing */
INCOMPATIBLE,
        /** Means that the 2 packages are the same thing but one does not upgrade the other */
NOT_UPDATE,
/** Means that the 2 packages are the same thing, and one is the upgrade of the other */
UPDATE;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index be99f22..d4622ad 100755

//Synthetic comment -- @@ -51,30 +51,33 @@
* Adds a new source to the Sources list.
*/
public void add(SdkSourceCategory category, SdkSource source) {

        ArrayList<SdkSource> list = mSources.get(category);
        if (list == null) {
            list = new ArrayList<SdkSource>();
            mSources.put(category, list);
}

        list.add(source);
}

/**
* Removes a source from the Sources list.
*/
public void remove(SdkSource source) {
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
//Synthetic comment -- @@ -84,7 +87,9 @@
* Removes all the sources in the given category.
*/
public void removeAll(SdkSourceCategory category) {
        mSources.remove(category);
}

/**
//Synthetic comment -- @@ -100,9 +105,11 @@
if (cat.getAlwaysDisplay()) {
cats.add(cat);
} else {
                ArrayList<SdkSource> list = mSources.get(cat);
                if (list != null && !list.isEmpty()) {
                    cats.add(cat);
}
}
}
//Synthetic comment -- @@ -115,11 +122,13 @@
* Might return an empty array, but never returns null.
*/
public SdkSource[] getSources(SdkSourceCategory category) {
        ArrayList<SdkSource> list = mSources.get(category);
        if (list == null) {
            return new SdkSource[0];
        } else {
            return list.toArray(new SdkSource[list.size()]);
}
}

//Synthetic comment -- @@ -127,22 +136,24 @@
* Returns an array of the sources across all categories. This is never null.
*/
public SdkSource[] getAllSources() {
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

/**
//Synthetic comment -- @@ -155,9 +166,11 @@
*/
public SdkSourceCategory getCategory(SdkSource source) {
if (source != null) {
            for (Entry<SdkSourceCategory, ArrayList<SdkSource>> entry : mSources.entrySet()) {
                if (entry.getValue().contains(source)) {
                    return entry.getKey();
}
}
}
//Synthetic comment -- @@ -175,14 +188,16 @@
* The search is O(N), which should be acceptable on the expectedly small source list.
*/
public boolean hasSourceUrl(SdkSource source) {
        for (ArrayList<SdkSource> list : mSources.values()) {
            for (SdkSource s : list) {
                if (s.equals(source)) {
                    return true;
}
}
}
        return false;
}

/**
//Synthetic comment -- @@ -196,15 +211,17 @@
* The search is O(N), which should be acceptable on the expectedly small source list.
*/
public boolean hasSourceUrl(SdkSourceCategory category, SdkSource source) {
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

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java
//Synthetic comment -- index 0b4879b..2314446 100755

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.sdklib.internal.repository.ExtraPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdkuilib.internal.repository.PackageManager.IAutoInstallTask;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -66,7 +66,7 @@
private File    mResultPath = null;
private SettingsController mSettingsController;
private PackageFilter mPackageFilter;
    private PackageManager mPackageMananger;

private ProgressBar mProgressBar;
private Label mStatusText;
//Synthetic comment -- @@ -132,6 +132,8 @@
shell.setMinimumSize(new Point(450, 100));
shell.setSize(450, 100);

GridLayoutBuilder.create(shell).columns(1);

Composite composite1 = new Composite(shell, SWT.NONE);
//Synthetic comment -- @@ -166,19 +168,12 @@

mUpdaterData.broadcastOnSdkLoaded();

        mPackageMananger = new PackageManager(mUpdaterData) {
            @Override
            public void updatePackageTable() {
                // pass
            }
        };

}

@Override
protected void eventLoop() {
        mPackageMananger.loadPackages();
        mPackageMananger.performAutoInstallTask(new IAutoInstallTask() {
public boolean acceptPackage(Package pkg) {
// Is this the package we want to install?
return mPackageFilter.accept(pkg);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java
new file mode 100755
//Synthetic comment -- index 0000000..935f59d

//Synthetic comment -- @@ -0,0 +1,537 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageManager.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageManager.java
deleted file mode 100755
//Synthetic comment -- index 0447385..0000000

//Synthetic comment -- @@ -1,399 +0,0 @@
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
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.Package.UpdateInfo;

import org.eclipse.swt.widgets.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages packages fetched from the remote SDK Repository and keeps track
 * of their state compared with the current local SDK installation.
 * <p/>
 * This is currently just an embryonic manager which should evolve over time.
 * There's a lot of overlap with the functionality of {@link UpdaterData} so
 * merging them together might just be the way to go.
 * <p/>
 * Right now all it does is provide the ability to load packages using a task
 * and perform some semi-automatic installation of a given package if available.
 */
abstract class PackageManager {

    private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
    private final UpdaterData mUpdaterData;

    /**
     * Interface describing the task of installing a specific package.
     * For details on the operation,
     * see {@link PackageManager#performAutoInstallTask(IAutoInstallTask)}.
     *
     * @see PackageManager#performAutoInstallTask(IAutoInstallTask)
     */
    public interface IAutoInstallTask {
        /**
         * Called by the install task for every package available (new ones, updates as well
         * as existing ones that don't have a potential update.)
         * The method should return true if this is the package that should be installed.
         */
        public boolean acceptPackage(Package pkg);

        /**
         * Called when the accepted package has been installed, successfully or not.
         * If an already installed (aka existing) package has been accepted, this will
         * be called with a 'true' success and the actual install path.
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
    public PackageManager(UpdaterData updaterData) {
        mUpdaterData = updaterData;
    }

    /**
     * Derived classes sohuld implement that to update their display of the current
     * package list. Called by {@link #loadPackages()} when the package list has
     * significantly changed.
     * <p/>
     * The package list is retrieved using {@link #getPackages()} which is guaranteed
     * not to change during this call.
     * <p/>
     * This is called from a non-UI thread. An UI interaction done here <em>must</em>
     * be wrapped in a {@link Display#syncExec(Runnable)}.
     */
    public abstract void updatePackageTable();

    /**
     * Returns the current package list.
     */
    public List<PkgItem> getPackages() {
        return mPackages;
    }

    /**
     * Load all packages from the remote repository and
     * updates the {@link PkgItem} package list.
     *
     * This runs in an {@link ITask}. The call is blocking.
     */
    public void loadPackages() {
        if (mUpdaterData == null) {
            return;
        }

        mPackages.clear();

        // get local packages
        for (Package pkg : mUpdaterData.getInstalledPackages()) {
            PkgItem pi = new PkgItem(pkg, PkgState.INSTALLED);
            mPackages.add(pi);
        }

        // get remote packages
        final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();
        mUpdaterData.loadRemoteAddonsList();
        mUpdaterData.getTaskFactory().start("Loading Sources", new ITask() {
            public void run(ITaskMonitor monitor) {
                SdkSource[] sources = mUpdaterData.getSources().getAllSources();
                for (SdkSource source : sources) {
                    Package[] pkgs = source.getPackages();
                    if (pkgs == null) {
                        source.load(monitor, forceHttp);
                        pkgs = source.getPackages();
                    }
                    if (pkgs == null) {
                        continue;
                    }

                    nextPkg: for(Package pkg : pkgs) {
                        boolean isUpdate = false;
                        for (PkgItem pi: mPackages) {
                            if (pi.isSameAs(pkg)) {
                                continue nextPkg;
                            }
                            if (pi.isUpdatedBy(pkg)) {
                                isUpdate = true;
                                break;
                            }
                        }

                        if (!isUpdate) {
                            PkgItem pi = new PkgItem(pkg, PkgState.NEW);
                            mPackages.add(pi);
                        }
                    }

                    // Dynamically update the table while we load after each source.
                    // Since the official Android source gets loaded first, it makes the
                    // window look non-empty a lot sooner.
                    updatePackageTable();
                }

                monitor.setDescription("Done loading %1$d packages from %2$d sources",
                        mPackages.size(),
                        sources.length);
            }
        });
    }

    /**
     * Executes the given {@link IAutoInstallTask} on the current package list.
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
     * @param installTask The task to perform.
     */
    public void performAutoInstallTask(IAutoInstallTask installTask) {
        try {
            for (PkgItem item : mPackages) {
                Package pkg = null;
                switch(item.getState()) {
                case NEW:
                    if (installTask.acceptPackage(item.getPackage())) {
                        pkg = item.getPackage();
                    }
                    break;
                case HAS_UPDATE:
                    for (Package upd : item.getUpdatePkgs()) {
                        if (installTask.acceptPackage(upd)) {
                            pkg = upd;
                            break;
                        }
                    }
                    break;
                case INSTALLED:
                    if (installTask.acceptPackage(item.getPackage())) {
                        // If the caller is accepting an installed package,
                        // return a success and give the package's install path
                        pkg = item.getPackage();
                        Archive[] a = pkg.getArchives();
                        // an installed package should have one local compatible archive
                        if (a.length == 1 && a[0].isCompatible()) {
                            installTask.setResult(
                                    pkg,
                                    true /*success*/,
                                    new File(a[0].getLocalOsPath()));
                            return;
                        }
                    }
                }

                if (pkg != null) {
                    // Try to install this package if it has one compatible archive.
                    Archive a = null;

                    for (Archive a2 : pkg.getArchives()) {
                        if (a2.isCompatible()) {
                            a = a2;
                            break;
                        }
                    }

                    if (a != null) {
                        ArrayList<Archive> archives = new ArrayList<Archive>();
                        archives.add(a);

                        mUpdaterData.updateOrInstallAll_WithGUI(
                                archives,
                                true /* includeObsoletes */);

                        // loadPackages will also re-enable the UI
                        loadPackages();

                        // Try to locate the installed package in the new package list
                        for (PkgItem item2 : mPackages) {
                            if (item2.getState() == PkgState.INSTALLED) {
                                Package pkg2 = item2.getPackage();
                                if (pkg2.canBeUpdatedBy(pkg) == UpdateInfo.NOT_UPDATE) {
                                    Archive[] a2 = pkg.getArchives();
                                    if (a2.length == 1 && a2[0].isCompatible()) {
                                        installTask.setResult(
                                                pkg,
                                                true /*success*/,
                                                new File(a2[0].getLocalOsPath()));
                                        return;
                                    }
                                }
                            }
                        }

                        // We failed to find the installed package.
                        // We'll assume we failed to install it.
                        installTask.setResult(pkg, false /*success*/, null);
                        return;
                    }
                }

            }
        } finally {
            installTask.taskCompleted();
        }
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
         */
        HAS_UPDATE,

        /**
         * There's a new package available on the remote site that isn't
         * installed locally.
         */
        NEW
    }

    /**
     * A {@link PkgItem} represents one {@link Package} available for the manager.
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

        public boolean isSameAs(Package pkg) {
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
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 2b7a0db..dd4a0c9 100755

//Synthetic comment -- @@ -26,8 +26,9 @@
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdkuilib.internal.repository.PackageManager.PkgItem;
import com.android.sdkuilib.internal.repository.PackageManager.PkgState;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;
//Synthetic comment -- @@ -65,7 +66,6 @@

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
//Synthetic comment -- @@ -74,6 +74,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
//Synthetic comment -- @@ -122,8 +123,11 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

    private final PackageManagerImpl mPkgManager;
private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
private final UpdaterData mUpdaterData;

private boolean mDisplayArchives = false;
//Synthetic comment -- @@ -153,14 +157,14 @@
super(parent, swtStyle);

mUpdaterData = updaterData;
        mPkgManager = new PackageManagerImpl(updaterData);
createContents(this);

postCreate();  //$hide$
}

public void onPageSelected() {
        if (mPkgManager.getPackages().isEmpty()) {
// Initialize the package list the first time the page is shown.
loadPackages();
}
//Synthetic comment -- @@ -494,31 +498,66 @@
return;
}

        try {
            enableUi(mGroupPackages, false);

            boolean firstLoad = mPkgManager.getPackages().isEmpty();

            // Load package is synchronous but does not block the UI.
            // Consequently it's entirely possible for the user
            // to request the app to close whilst the packages are loading. Any
            // action done after loadPackages must check the UI hasn't been
            // disposed yet. Otherwise hilarity ensues.

            mPkgManager.loadPackages();

            if (firstLoad && !mGroupPackages.isDisposed()) {
                // set the initial expanded state
                expandInitial(mCategories);
}

        } finally {
            if (!mGroupPackages.isDisposed()) {
                enableUi(mGroupPackages, true);
                updateButtonsState();
                updateMenuCheckmarks();
}
        }
}

private void enableUi(Composite root, boolean enabled) {
//Synthetic comment -- @@ -602,67 +641,69 @@
mCategories.add(cat);
}

        for (PkgItem item : mPkgManager.getPackages()) {
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
//Synthetic comment -- @@ -726,15 +767,25 @@
mLastSortWasByApi = false;
mCategories.clear();

        Set<SdkSource> sourceSet = new HashSet<SdkSource>();
        for (PkgItem item : mPkgManager.getPackages()) {
            if (keepItem(item)) {
                sourceSet.add(item.getSource());
}
}

        SdkSource[] sources = sourceSet.toArray(new SdkSource[sourceSet.size()]);
        Arrays.sort(sources, new Comparator<SdkSource>() {
public int compare(SdkSource o1, SdkSource o2) {
if (o1 == o2) {
return 0;
//Synthetic comment -- @@ -747,6 +798,7 @@
return o1.toString().compareTo(o2.toString());
}
});

for (SdkSource source : sources) {
Object key = source != null ? source : "Locally Installed Packages";
//Synthetic comment -- @@ -758,7 +810,7 @@
key.toString(),
iconRef);

            for (PkgItem item : mPkgManager.getPackages()) {
if (item.getSource() == source) {
cat.getItems().add(item);
}
//Synthetic comment -- @@ -801,6 +853,10 @@
/**
* Performs the initial expansion of the tree. This expands categories that contain
* at least one installed item and collapses the ones with nothing installed.
*/
private void expandInitial(Object elem) {
mTreeViewer.setExpandedState(elem, true);
//Synthetic comment -- @@ -989,9 +1045,10 @@
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */);
} finally {
// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();
                // loadPackages will also re-enable the UI
loadPackages();
}
}
//Synthetic comment -- @@ -1055,9 +1112,10 @@
}
});
} finally {
// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();
                    // loadPackages will also re-enable the UI
loadPackages();
}
}
//Synthetic comment -- @@ -1403,28 +1461,6 @@
}
}

    private class PackageManagerImpl extends PackageManager {

        public PackageManagerImpl(UpdaterData updaterData) {
            super(updaterData);
        }

        @Override
        public void updatePackageTable() {
            // Dynamically update the table while we load after each source.
            // Since the official Android source gets loaded first, it makes the
            // window look non-empty a lot sooner.
            if (!mGroupPackages.isDisposed()) {
                mGroupPackages.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        sortPackages(true /* updateButtons */);
                    }
                });
            }
        }

    }


// --- Implementation of ISdkChangeListener ---








