/*SdkManager2: fixes for asynchronous sources UI.

- Properly refresh display list when installing/deleting
  a package.
- Gray install/delete buttons during an install/delete
  to avoid nesting operations.

Change-Id:I72fdca1252c447b046040afb70e67dfae77188e4*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index c0b7041..11dad83 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -132,6 +133,10 @@
if (exception[0] instanceof FileNotFoundException) {
// FNF has no useful getMessage, so we need to special handle it.
reason = "File not found";
            } else if (exception[0] instanceof UnknownHostException &&
                    exception[0].getMessage() != null) {
                // This has no useful getMessage yet could really use one
                reason = String.format("Unknown Host %1$s", exception[0].getMessage());
} else if (exception[0] instanceof SSLKeyException) {
// That's a common error and we have a pref for it.
reason = "HTTPS SSL error. You might want to force download through HTTP in the settings.";








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index d50d358..a9c5996 100755

//Synthetic comment -- @@ -241,6 +241,7 @@

/**
* Parses an XML node to process the <archives> element.
     * Always return a non-null array. The array may be empty.
*/
private Archive[] parseArchives(Node archivesNode) {
ArrayList<Archive> archives = new ArrayList<Archive>();
//Synthetic comment -- @@ -352,6 +353,19 @@
}

/**
     * Returns true if this package contains the exact given archive.
     * Important: This compares object references, not object equality.
     */
    public boolean hasArchive(Archive archive) {
        for (Archive a : mArchives) {
            if (a == archive) {
                return true;
            }
        }
        return false;
    }

    /**
* Returns whether the {@link Package} has at least one {@link Archive} compatible with
* the host platform.
*/
//Synthetic comment -- @@ -539,7 +553,6 @@
return UpdateInfo.NOT_UPDATE;
}

/**
* Returns an ordering like this: <br/>
* - Tools <br/>








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackageLoader.java
//Synthetic comment -- index 30e0188..6a6b423 100755

//Synthetic comment -- @@ -45,10 +45,10 @@
* Interface for the callback called by
* {@link PackageLoader#loadPackages(ISourceLoadedCallback)}.
* <p/>
     * After processing each source, the package loader calls {@link #onSourceLoaded(List)}
* with the list of package items found in that source. The client should process that
* list as it want, typically by accumulating the package items in a list of its own.
     * By returning true from {@link #onSourceLoaded(List)}, the client tells the loader to
* continue and process the next source. By returning false, it tells to stop loading.
* <p/>
* The {@link #onLoadCompleted()} method is guaranteed to be called at the end, no
//Synthetic comment -- @@ -60,7 +60,7 @@
* After processing each source, the package loader calls this method with the
* list of package items found in that source. The client should process that
* list as it want, typically by accumulating the package items in a list of its own.
         * By returning true from {@link #onSourceLoaded(List)}, the client tells the loader to
* continue and process the next source. By returning false, it tells to stop loading.
* <p/>
* <em>Important</em>: This method is called from a sub-thread, so clients who try
//Synthetic comment -- @@ -71,7 +71,7 @@
*  This is a copy and the client can hold to this list or modify it in any way.
* @return True if the load operation should continue, false if it should stop.
*/
        public boolean onSourceLoaded(List<PkgItem> pkgItems);

/**
* This method is guaranteed to be called at the end, no matter how the
//Synthetic comment -- @@ -147,7 +147,7 @@
// Notify the callback by giving it a copy of the current list.
// (in case the callback holds to the list... we still need this list of
// ourselves below).
                if (!sourceLoadedCallback.onSourceLoaded(new ArrayList<PkgItem>(allPkgItems))) {
return;
}
}
//Synthetic comment -- @@ -192,7 +192,7 @@

// Notify the callback a new source has finished loading.
// If the callback requests so, stop right away.
                            if (!sourceLoadedCallback.onSourceLoaded(sourcePkgItems)) {
return;
}
}
//Synthetic comment -- @@ -260,7 +260,7 @@
public void loadPackagesWithInstallTask(final IAutoInstallTask installTask) {

loadPackages(new ISourceLoadedCallback() {
            public boolean onSourceLoaded(List<PkgItem> pkgItems) {
for (PkgItem item : pkgItems) {
Package acceptedPkg = null;
switch(item.getState()) {
//Synthetic comment -- @@ -540,6 +540,25 @@
return getPackage().compareTo(pkg.getPackage());
}

        /**
         * Returns true if this package or any of the updating packages contains
         * the exact given archive.
         * Important: This compares object references, not object equality.
         */
        public boolean hasArchive(Archive archive) {
            if (mPkg.hasArchive(archive)) {
                return true;
            }
            if (mUpdatePkgs != null && !mUpdatePkgs.isEmpty()) {
                for (Package p : mUpdatePkgs) {
                    if (p.hasArchive(archive)) {
                        return true;
                    }
                }
            }
            return false;
        }

/** Returns a string representation of this item, useful when debugging. */
@Override
public String toString() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index dd4a0c9..8372a38 100755

//Synthetic comment -- @@ -74,6 +74,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

//Synthetic comment -- @@ -152,6 +153,7 @@
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;
private boolean mLastSortWasByApi;
    private boolean mOperationPending;

public PackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
super(parent, swtStyle);
//Synthetic comment -- @@ -507,7 +509,7 @@
// disposed yet. Otherwise hilarity ensues.

mPackageLoader.loadPackages(new ISourceLoadedCallback() {
            public boolean onSourceLoaded(List<PkgItem> newPkgItems) {
boolean somethingNew = false;

synchronized(mPackages) {
//Synthetic comment -- @@ -560,17 +562,6 @@
});
}

private void sortPackages(boolean updateButtons) {
if (isSortByApi()) {
sortByApiLevel();
//Synthetic comment -- @@ -923,7 +914,27 @@
}
}

    /**
     * Indicate an install/delete operation is pending.
     * This disable the install/delete buttons.
     * Use {@link #endOperationPending()} to revert.
     */
    private void beginOperationPending() {
        mOperationPending = true;
        mButtonInstall.setEnabled(false);
        mButtonDelete.setEnabled(false);
    }

    private void endOperationPending() {
        mOperationPending = false;
        updateButtonsState();
    }

private void updateButtonsState() {
        if (mOperationPending) {
            return;
        }

boolean canInstall = false;

if (mDisplayArchives) {
//Synthetic comment -- @@ -1039,13 +1050,28 @@

if (mUpdaterData != null) {
try {
                beginOperationPending();

mUpdaterData.updateOrInstallAll_WithGUI(
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */);
} finally {
                endOperationPending();

                // Remove any pkg item matching anything we potentially installed
                // then request the package list to be updated. This will prevent
                // from having stale entries.
                synchronized(mPackages) {
                    for (Archive a : archives) {
                        for (Iterator<PkgItem> it = mPackages.iterator(); it.hasNext(); ) {
                            PkgItem pi = it.next();
                            if (pi.hasArchive(a)) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }

// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();
//Synthetic comment -- @@ -1062,15 +1088,18 @@
return;
}

        final String title = "Delete SDK Package";
String msg = "Are you sure you want to delete:";

        // A map of archives to deleted versus their internal PkgItem representation
        final Map<Archive, PkgItem> archives = new TreeMap<Archive, PkgItem>();

for (Object c : checked) {
if (c instanceof PkgItem) {
                PkgItem pi = (PkgItem) c;
                PkgState state = pi.getState();
if (state == PkgState.INSTALLED || state == PkgState.HAS_UPDATE) {
                    Package p = pi.getPackage();

Archive[] as = p.getArchives();
if (as.length == 1 && as[0] != null && as[0].isLocal()) {
//Synthetic comment -- @@ -1080,7 +1109,7 @@
File dir = new File(osPath);
if (dir.isDirectory()) {
msg += "\n - " + p.getShortDescription();
                            archives.put(archive, pi);
}
}
}
//Synthetic comment -- @@ -1091,16 +1120,25 @@
msg += "\n" + "This cannot be undone.";
if (MessageDialog.openQuestion(getShell(), title, msg)) {
try {
                    beginOperationPending();

                    mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);
                            for (Entry<Archive, PkgItem> entry : archives.entrySet()) {
                                Archive a = entry.getKey();

monitor.setDescription("Deleting '%1$s' (%2$s)",
a.getParentPackage().getShortDescription(),
a.getLocalOsPath());

                                // Delete the actual package and its internal representation
a.deleteLocal();

                                synchronized(mPackages) {
                                    mPackages.remove(entry.getValue());
                                }

monitor.incProgress(1);
if (monitor.isCancelRequested()) {
break;
//Synthetic comment -- @@ -1112,7 +1150,7 @@
}
});
} finally {
                    endOperationPending();

// The local package list has changed, make sure to refresh it
mUpdaterData.getLocalSdkParser().clearPackages();







