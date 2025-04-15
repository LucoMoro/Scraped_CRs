/*[WIP] Refactor PackagesPage to make it testable.

Not ready for review. Don't look at me!

Change-Id:I58ff45895916a14a10f501a9bd664782d777ed42*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 46d1db4..86a555a 100755

//Synthetic comment -- @@ -143,6 +143,14 @@
AndroidLocation.resetFolder();
File addonsDir = new File(sdkDir, SdkConstants.FD_ADDONS);
addonsDir.mkdir();
File toolsLibEmuDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
toolsLibEmuDir.mkdirs();
new File(toolsLibEmuDir, "snapshots.img").createNewFile();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 5353b74..f5a2ed3 100755

//Synthetic comment -- @@ -32,7 +32,7 @@
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgItem.PkgState;
import com.android.sdkuilib.internal.repository.ui.PackagesPage;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -688,9 +688,9 @@
// Always add the tools & extras categories, even if empty (unlikely anyway)
if (needTools) {
PkgCategoryApi acat = new PkgCategoryApi(
                        PkgCategoryApi.KEY_TOOLS,
                        null,
                        mUpdaterData.getImageFactory().getImageByName(PackagesPage.ICON_CAT_OTHER));
synchronized (cats) {
cats.add(acat);
}
//Synthetic comment -- @@ -698,9 +698,9 @@

if (needExtras) {
PkgCategoryApi acat = new PkgCategoryApi(
                        PkgCategoryApi.KEY_EXTRA,
                        null,
                        mUpdaterData.getImageFactory().getImageByName(PackagesPage.ICON_CAT_OTHER));
synchronized (cats) {
cats.add(acat);
}
//Synthetic comment -- @@ -733,9 +733,9 @@
}

cat = new PkgCategoryApi(
                    key,
                    platformName,
                    mUpdaterData.getImageFactory().getImageByName(PackagesPage.ICON_CAT_PLATFORM));

return cat;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java
//Synthetic comment -- index cdf6b59..b73288b 100755

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPage;


public class PkgCategorySource extends PkgCategory {
//Synthetic comment -- @@ -42,8 +42,8 @@
source, // the source is the key and it can be null
source == UNKNOWN_SOURCE ? "Local Packages" : source.toString(),
source == UNKNOWN_SOURCE ?
                    updaterData.getImageFactory().getImageByName(PackagesPage.ICON_PKG_INSTALLED) :
                        source);
mSource = source;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java
//Synthetic comment -- index 6f7589c..8adf428 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.ui.PackagesPage;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

//Synthetic comment -- @@ -33,10 +34,10 @@
*/
public class PkgContentProvider implements ITreeContentProvider {

    private final Viewer mViewer;
private boolean mDisplayArchives;

    public PkgContentProvider(Viewer viewer) {
mViewer = viewer;
}

//Synthetic comment -- @@ -139,7 +140,7 @@
}

@Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
// unused
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 566981d..025be46 100755

//Synthetic comment -- @@ -16,20 +16,12 @@

package com.android.sdkuilib.internal.repository.ui;

import com.android.SdkConstants;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -50,7 +42,6 @@
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
//Synthetic comment -- @@ -65,7 +56,6 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//Synthetic comment -- @@ -79,8 +69,6 @@
import org.eclipse.swt.widgets.TreeColumn;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -92,16 +80,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public class PackagesPage extends Composite implements ISdkChangeListener {

    public  static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
    public  static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$
    private static final String ICON_SORT_BY_SOURCE = "source_icon16.png";      //$NON-NLS-1$
    private static final String ICON_SORT_BY_API    = "platform_pkg_16.png";    //$NON-NLS-1$
    private static final String ICON_PKG_NEW        = "pkg_new_16.png";         //$NON-NLS-1$
    private static final String ICON_PKG_INCOMPAT   = "pkg_incompat_16.png";    //$NON-NLS-1$
    private static final String ICON_PKG_UPDATE     = "pkg_update_16.png";      //$NON-NLS-1$
    public  static final String ICON_PKG_INSTALLED  = "pkg_installed_16.png";   //$NON-NLS-1$

enum MenuAction {
RELOAD                      (SWT.NONE,  "Reload"),
//Synthetic comment -- @@ -133,13 +112,13 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

private final SdkInvocationContext mContext;
    private final UpdaterData mUpdaterData;
    private final PackagesDiffLogic mDiffLogic;

private boolean mDisplayArchives = false;
private boolean mOperationPending;

private Text mTextSdkOsPath;
private Button mCheckSortSource;
private Button mCheckSortApi;
//Synthetic comment -- @@ -148,17 +127,11 @@
private Button mCheckFilterNew;
private Composite mGroupOptions;
private Composite mGroupSdk;
    private Group mGroupPackages;
private Button mButtonDelete;
private Button mButtonInstall;
    private Tree mTree;
    private CheckboxTreeViewer mTreeViewer;
    private TreeViewerColumn mColumnName;
    private TreeViewerColumn mColumnApi;
    private TreeViewerColumn mColumnRevision;
    private TreeViewerColumn mColumnStatus;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

public PackagesPage(
Composite parent,
//Synthetic comment -- @@ -166,25 +139,45 @@
UpdaterData updaterData,
SdkInvocationContext context) {
super(parent, swtStyle);
        mUpdaterData = updaterData;
        mContext = context;

        mDiffLogic = new PackagesDiffLogic(updaterData);

createContents(this);
postCreate();  //$hide$
}

public void performFirstLoad() {
        // First a package loader is created that only checks
        // the local cache xml files. It populates the package
        // list based on what the client got last, essentially.
        loadPackages(true /*useLocalCache*/, false /*overrideExisting*/);

        // Next a regular package loader is created that will
        // respect the expiration and refresh parameters of the
        // download cache.
        loadPackages(false /*useLocalCache*/, true /*overrideExisting*/);
}

@SuppressWarnings("unused")
//Synthetic comment -- @@ -202,12 +195,39 @@
GridDataBuilder.create(mTextSdkOsPath).hFill().vCenter().hGrab();
mTextSdkOsPath.setEnabled(false);

        mGroupPackages = new Group(parent, SWT.NONE);
GridDataBuilder.create(mGroupPackages).fill().grab().hSpan(2);
        mGroupPackages.setText("Packages");
        GridLayoutBuilder.create(mGroupPackages).columns(1);

        mTreeViewer = new CheckboxTreeViewer(mGroupPackages, SWT.BORDER);
mTreeViewer.addFilter(new ViewerFilter() {
@Override
public boolean select(Viewer viewer, Object parentElement, Object element) {
//Synthetic comment -- @@ -229,39 +249,45 @@
}
});

        mTree = mTreeViewer.getTree();
        mTree.setLinesVisible(true);
        mTree.setHeaderVisible(true);
        GridDataBuilder.create(mTree).fill().grab();

// column name icon is set when loading depending on the current filter type
// (e.g. API level or source)
        mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        mTreeColumnName = mColumnName.getColumn();
mTreeColumnName.setText("Name");
mTreeColumnName.setWidth(340);

        mColumnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn2 = mColumnApi.getColumn();
treeColumn2.setText("API");
treeColumn2.setAlignment(SWT.CENTER);
treeColumn2.setWidth(50);

        mColumnRevision = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn3 = mColumnRevision.getColumn();
treeColumn3.setText("Rev.");
treeColumn3.setToolTipText("Revision currently installed");
treeColumn3.setAlignment(SWT.CENTER);
treeColumn3.setWidth(50);


        mColumnStatus = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn4 = mColumnStatus.getColumn();
treeColumn4.setText("Status");
treeColumn4.setAlignment(SWT.LEAD);
treeColumn4.setWidth(190);

        mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
GridLayoutBuilder.create(mGroupOptions).columns(6).noMargins();

//Synthetic comment -- @@ -392,9 +418,18 @@
});
}

private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -418,19 +453,19 @@

switch (action) {
case RELOAD:
                    fullReload();
break;
case SHOW_ADDON_SITES:
                    AddonSitesDialog d = new AddonSitesDialog(getShell(), mUpdaterData);
if (d.open()) {
                        loadPackages();
}
break;
case TOGGLE_SHOW_ARCHIVES:
mDisplayArchives = !mDisplayArchives;
// Force the viewer to be refreshed
                    ((PkgContentProvider) mTreeViewer.getContentProvider()).setDisplayArchives(
                                                                                  mDisplayArchives);
mTreeViewer.setInput(null);
refreshViewerInput();
syncViewerSelection();
//Synthetic comment -- @@ -533,29 +568,23 @@
}

private void postCreate() {
        if (mUpdaterData != null) {
            mTextSdkOsPath.setText(mUpdaterData.getOsSdkRoot());
}

        mTreeViewer.setContentProvider(new PkgContentProvider(mTreeViewer));
((PkgContentProvider) mTreeViewer.getContentProvider()).setDisplayArchives(
                                                                                mDisplayArchives);
ColumnViewerToolTipSupport.enableFor(mTreeViewer, ToolTip.NO_RECREATE);

        mColumnApi.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnApi)));
        mColumnName.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnName)));
        mColumnStatus.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnStatus)));
        mColumnRevision.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnRevision)));

        FontData fontData = mTree.getFont().getFontData()[0];
fontData.setStyle(SWT.ITALIC);
        mTreeFontItalic = new Font(mTree.getDisplay(), fontData);

        mTree.addDisposeListener(new DisposeListener() {
@Override
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
//Synthetic comment -- @@ -564,52 +593,8 @@
});
}

    /**
     * Performs a full reload by removing all cached packages data, including the platforms
     * and addons from the sdkmanager instance. This will perform a full local parsing
     * as well as a full reload of the remote data (by fetching all sources again.)
     */
    private void fullReload() {
        // Clear all source information, forcing them to be refreshed.
        mUpdaterData.getSources().clearAllPackages();
        // Clear and reload all local data too.
        localReload();
    }

    /**
     * Performs a full reload of all the local package information, including the platforms
     * and addons from the sdkmanager instance. This will perform a full local parsing.
     * <p/>
     * This method does NOT force a new fetch of the remote sources.
     *
     * @see #fullReload()
     */
    private void localReload() {
        // Clear all source caches, otherwise loading will use the cached data
        mUpdaterData.getLocalSdkParser().clearPackages();
        mUpdaterData.getSdkManager().reloadSdk(mUpdaterData.getSdkLog());
        loadPackages();
    }

    /**
     * Performs a "normal" reload of the package information, use the default download
     * cache and refreshing strategy as needed.
     */
    private void loadPackages() {
        loadPackages(false /*useLocalCache*/, false /*overrideExisting*/);
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
    private void loadPackages(final boolean useLocalCache, final boolean overrideExisting) {
        if (mUpdaterData == null) {
return;
}

//Synthetic comment -- @@ -619,7 +604,7 @@
// action done after loadPackages must check the UI hasn't been
// disposed yet. Otherwise hilarity ensues.

        final boolean displaySortByApi = isSortByApi();

if (mTreeColumnName.isDisposed()) {
// If the UI got disposed, don't try to load anything since we won't be
//Synthetic comment -- @@ -627,76 +612,11 @@
return;
}

        mTreeColumnName.setImage(getImage(displaySortByApi ? ICON_SORT_BY_API
                                                           : ICON_SORT_BY_SOURCE));

        PackageLoader packageLoader = null;
        if (useLocalCache) {
            packageLoader =
                new PackageLoader(mUpdaterData, new DownloadCache(Strategy.ONLY_CACHE));
        } else {
            packageLoader = mUpdaterData.getPackageLoader();
        }
        assert packageLoader != null;

        mDiffLogic.updateStart();
        packageLoader.loadPackages(overrideExisting, new ISourceLoadedCallback() {
            @Override
            public boolean onUpdateSource(SdkSource source, Package[] newPackages) {
                // This runs in a thread and must not access UI directly.
                final boolean changed = mDiffLogic.updateSourcePackages(
                        displaySortByApi, source, newPackages);

                if (!mGroupPackages.isDisposed()) {
                    mGroupPackages.getDisplay().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (changed ||
                                mTreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
                                refreshViewerInput();
                            }
                        }
                    });
                }

                // Return true to tell the loader to continue with the next source.
                // Return false to stop the loader if any UI has been disposed, which can
                // happen if the user is trying to close the window during the load operation.
                return !mGroupPackages.isDisposed();
            }

            @Override
            public void onLoadCompleted() {
                // This runs in a thread and must not access UI directly.
                final boolean changed = mDiffLogic.updateEnd(displaySortByApi);

                if (!mGroupPackages.isDisposed()) {
                    mGroupPackages.getDisplay().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            if (changed ||
                                mTreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
                                refreshViewerInput();
                            }

                            if (!useLocalCache &&
                                    mDiffLogic.isFirstLoadComplete() &&
                                    !mGroupPackages.isDisposed()) {
                                // At the end of the first load, if nothing is selected then
                                // automatically select all new and update packages.
                                Object[] checked = mTreeViewer.getCheckedElements();
                                if (checked == null || checked.length == 0) {
                                    onSelectNewUpdates(
                                            false, //selectNew
                                            true,  //selectUpdates,
                                            true); //selectTop
                                }
                            }
                        }
                    });
                }
            }
        });
}

private void refreshViewerInput() {
//Synthetic comment -- @@ -704,16 +624,9 @@
// Since the official Android source gets loaded first, it makes the
// window look non-empty a lot sooner.
if (!mGroupPackages.isDisposed()) {

            List<PkgCategory> cats = mDiffLogic.getCategories(isSortByApi());
            if (mTreeViewer.getInput() != cats) {
                // set initial input
                mTreeViewer.setInput(cats);
            } else {
                // refresh existing, which preserves the expanded state, the selection
                // and the checked state.
                mTreeViewer.refresh();
            }

// set the initial expanded state
expandInitial(mTreeViewer.getInput());
//Synthetic comment -- @@ -787,11 +700,12 @@
if (mTreeViewer != null && !mTreeViewer.getTree().isDisposed()) {

boolean enablePreviews =
                mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

mTreeViewer.setExpandedState(elem, true);
nextCategory: for (Object pkg :
                    ((ITreeContentProvider) mTreeViewer.getContentProvider()).getChildren(elem)) {
if (pkg instanceof PkgCategory) {
PkgCategory cat = (PkgCategory) pkg;

//Synthetic comment -- @@ -845,7 +759,8 @@
return;
}

        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();
Object[] children = provider.getElements(elem);
if (children == null) {
return;
//Synthetic comment -- @@ -874,7 +789,8 @@
boolean checked,
boolean fixChildren,
boolean fixParent) {
        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();

// fix the item itself
if (checked != mTreeViewer.getChecked(elem)) {
//Synthetic comment -- @@ -958,11 +874,7 @@
*/
private void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
// This does not update the tree itself, syncViewerSelection does it below.
        mDiffLogic.checkNewUpdateItems(
                selectNew,
                selectUpdates,
                selectTop,
                SdkConstants.CURRENT_PLATFORM);
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -972,7 +884,7 @@
*/
private void onDeselectAll() {
// This does not update the tree itself, syncViewerSelection does it below.
        mDiffLogic.uncheckAllItems();
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -983,8 +895,10 @@
* This does not update the tree itself.
*/
private void copySelection(boolean fromSourceToApi) {
        List<PkgItem> fromItems = mDiffLogic.getAllPkgItems(!fromSourceToApi, fromSourceToApi);
        List<PkgItem> toItems = mDiffLogic.getAllPkgItems(fromSourceToApi, !fromSourceToApi);

// deselect all targets
for (PkgItem item : toItems) {
//Synthetic comment -- @@ -1090,12 +1004,12 @@
ArrayList<Archive> archives = new ArrayList<Archive>();
getArchivesForInstall(archives);

        if (mUpdaterData != null) {
boolean needsRefresh = false;
try {
beginOperationPending();

                List<Archive> installed = mUpdaterData.updateOrInstallAll_WithGUI(
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */,
mContext == SdkInvocationContext.IDE ?
//Synthetic comment -- @@ -1107,7 +1021,7 @@

if (needsRefresh) {
// The local package list has changed, make sure to refresh it
                    localReload();
}
}
}
//Synthetic comment -- @@ -1219,7 +1133,7 @@
try {
beginOperationPending();

                    mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);
//Synthetic comment -- @@ -1245,7 +1159,7 @@
endOperationPending();

// The local package list has changed, make sure to refresh it
                    localReload();
}
}
}
//Synthetic comment -- @@ -1335,236 +1249,6 @@

// ----------------------

    public class PkgCellLabelProvider extends ColumnLabelProvider implements ITableFontProvider {

        private final TreeViewerColumn mColumn;

        public PkgCellLabelProvider(TreeViewerColumn column) {
            super();
            mColumn = column;
        }

        @Override
        public String getText(Object element) {

            if (mColumn == mColumnName) {
                if (element instanceof PkgCategory) {
                    return ((PkgCategory) element).getLabel();
                } else if (element instanceof PkgItem) {
                    return getPkgItemName((PkgItem) element);
                } else if (element instanceof IDescription) {
                    return ((IDescription) element).getShortDescription();
                }

            } else if (mColumn == mColumnApi) {
                int api = -1;
                if (element instanceof PkgItem) {
                    api = ((PkgItem) element).getApi();
                }
                if (api >= 1) {
                    return Integer.toString(api);
                }

            } else if (mColumn == mColumnRevision) {
                if (element instanceof PkgItem) {
                    PkgItem pkg = (PkgItem) element;
                    return pkg.getRevision().toShortString();
                }

            } else if (mColumn == mColumnStatus) {
                if (element instanceof PkgItem) {
                    PkgItem pkg = (PkgItem) element;

                    switch(pkg.getState()) {
                    case INSTALLED:
                        Package update = pkg.getUpdatePkg();
                        if (update != null) {
                            return String.format(
                                    "Update available: rev. %1$s",
                                    update.getRevision().toShortString());
                        }
                        return "Installed";

                    case NEW:
                        Package p = pkg.getMainPackage();
                        if (p != null && p.hasCompatibleArchive()) {
                            return "Not installed";
                        } else {
                            return String.format("Not compatible with %1$s",
                                    SdkConstants.currentPlatformName());
                        }
                    }
                    return pkg.getState().toString();

                } else if (element instanceof Package) {
                    // This is an update package.
                    return "New revision " + ((Package) element).getRevision().toShortString();
                }
            }

            return ""; //$NON-NLS-1$
        }

        private String getPkgItemName(PkgItem item) {
            String name = item.getName().trim();

            if (isSortByApi()) {
                // When sorting by API, the package name might contains the API number
                // or the platform name at the end. If we find it, cut it out since it's
                // redundant.

                PkgCategoryApi cat = (PkgCategoryApi) findCategoryForItem(item);
                String apiLabel = cat.getApiLabel();
                String platLabel = cat.getPlatformName();

                if (platLabel != null && name.endsWith(platLabel)) {
                    return name.substring(0, name.length() - platLabel.length());

                } else if (apiLabel != null && name.endsWith(apiLabel)) {
                    return name.substring(0, name.length() - apiLabel.length());

                } else if (platLabel != null && item.isObsolete() && name.indexOf(platLabel) > 0) {
                    // For obsolete items, the format is "<base name> <platform name> (Obsolete)"
                    // so in this case only accept removing a platform name that is not at
                    // the end.
                    name = name.replace(platLabel, ""); //$NON-NLS-1$
                }
            }

            // Collapse potential duplicated spacing
            name = name.replaceAll(" +", " "); //$NON-NLS-1$ //$NON-NLS-2$

            return name;
        }

        private PkgCategory findCategoryForItem(PkgItem item) {
            List<PkgCategory> cats = mDiffLogic.getCategories(isSortByApi());
            for (PkgCategory cat : cats) {
                for (PkgItem i : cat.getItems()) {
                    if (i == item) {
                        return cat;
                    }
                }
            }

            return null;
        }

        @Override
        public Image getImage(Object element) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();

            if (imgFactory != null) {
                if (mColumn == mColumnName) {
                    if (element instanceof PkgCategory) {
                        return imgFactory.getImageForObject(((PkgCategory) element).getIconRef());
                    } else if (element instanceof PkgItem) {
                        return imgFactory.getImageForObject(((PkgItem) element).getMainPackage());
                    }
                    return imgFactory.getImageForObject(element);

                } else if (mColumn == mColumnStatus && element instanceof PkgItem) {
                    PkgItem pi = (PkgItem) element;
                    switch(pi.getState()) {
                    case INSTALLED:
                        if (pi.hasUpdatePkg()) {
                            return imgFactory.getImageByName(ICON_PKG_UPDATE);
                        } else {
                            return imgFactory.getImageByName(ICON_PKG_INSTALLED);
                        }
                    case NEW:
                        Package p = pi.getMainPackage();
                        if (p != null && p.hasCompatibleArchive()) {
                            return imgFactory.getImageByName(ICON_PKG_NEW);
                        } else {
                            return imgFactory.getImageByName(ICON_PKG_INCOMPAT);
                        }
                    }
                }
            }
            return super.getImage(element);
        }

        // -- ITableFontProvider

        @Override
        public Font getFont(Object element, int columnIndex) {
            if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() == PkgState.NEW) {
                    return mTreeFontItalic;
                }
            } else if (element instanceof Package) {
                // update package
                return mTreeFontItalic;
            }
            return super.getFont(element);
        }

        // -- Tooltip support

        @Override
        public String getToolTipText(Object element) {
            PkgItem pi = element instanceof PkgItem ? (PkgItem) element : null;
            if (pi != null) {
                element = pi.getMainPackage();
            }
            if (element instanceof IDescription) {
                String s = getTooltipDescription((IDescription) element);

                if (pi != null && pi.hasUpdatePkg()) {
                    s += "\n-----------------" +        //$NON-NLS-1$
                         "\nUpdate Available:\n" +      //$NON-NLS-1$
                         getTooltipDescription(pi.getUpdatePkg());
                }

                return s;
            }
            return super.getToolTipText(element);
        }

        private String getTooltipDescription(IDescription element) {
            String s = element.getLongDescription();
            if (element instanceof Package) {
                Package p = (Package) element;

                if (!p.isLocal()) {
                    // For non-installed item, try to find a download size
                    for (Archive a : p.getArchives()) {
                        if (!a.isLocal() && a.isCompatible()) {
                            s += '\n' + a.getSizeDescription();
                            break;
                        }
                    }
                }

                // Display info about where this package comes/came from
                SdkSource src = p.getParentSource();
                if (src != null) {
                    try {
                        URL url = new URL(src.getUrl());
                        String host = url.getHost();
                        if (p.isLocal()) {
                            s += String.format("\nInstalled from %1$s", host);
                        } else {
                            s += String.format("\nProvided by %1$s", host);
                        }
                    } catch (MalformedURLException ignore) {
                    }
                }
            }
            return s;
        }

        @Override
        public Point getToolTipShift(Object object) {
            return new Point(15, 5);
        }

        @Override
        public int getToolTipDisplayDelayTime(Object object) {
            return 500;
        }
    }

// --- Implementation of ISdkChangeListener ---

//Synthetic comment -- @@ -1577,7 +1261,7 @@
public void onSdkReload() {
// The sdkmanager finished reloading its data. We must not call localReload() from here
// since we don't want to alter the sdkmanager's data that just finished loading.
        loadPackages();
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageIcons.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageIcons.java
new file mode 100755
//Synthetic comment -- index 0000000..4fe8fca

//Synthetic comment -- @@ -0,0 +1,33 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
new file mode 100755
//Synthetic comment -- index 0000000..c61bef9

//Synthetic comment -- @@ -0,0 +1,555 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index 45e3163..a63b237 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.utils.ILogger;
import com.android.utils.NullLogger;
//Synthetic comment -- @@ -47,6 +48,7 @@

private DownloadCache mMockDownloadCache;

public MockUpdaterData() {
super(SDK_PATH, new MockLog());

//Synthetic comment -- @@ -54,6 +56,14 @@
setImageFactory(new NullImageFactory());
}

/** Gives access to the internal {@link #installArchives(List, int)}. */
public void _installArchives(List<ArchiveInfo> result) {
installArchives(result, 0/*flags*/);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java
new file mode 100755
//Synthetic comment -- index 0000000..8e9778e

//Synthetic comment -- @@ -0,0 +1,210 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java
new file mode 100755
//Synthetic comment -- index 0000000..db3179d

//Synthetic comment -- @@ -0,0 +1,293 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
new file mode 100755
//Synthetic comment -- index 0000000..45a921b

//Synthetic comment -- @@ -0,0 +1,51 @@







