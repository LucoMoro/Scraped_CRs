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









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 566981d..3c7207a 100755

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.sdkuilib.internal.repository.ui;

import com.android.SdkConstants;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -27,9 +25,6 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -92,16 +87,7 @@
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
//Synthetic comment -- @@ -133,9 +119,8 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

private final SdkInvocationContext mContext;
    private final UpdaterData mUpdaterData;
    private final PackagesDiffLogic mDiffLogic;

private boolean mDisplayArchives = false;
private boolean mOperationPending;
//Synthetic comment -- @@ -148,15 +133,8 @@
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

//Synthetic comment -- @@ -166,25 +144,35 @@
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
//Synthetic comment -- @@ -202,66 +190,67 @@
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
return filterViewerItem(element);
}
});

        mTreeViewer.addCheckStateListener(new ICheckStateListener() {
@Override
public void checkStateChanged(CheckStateChangedEvent event) {
onTreeCheckStateChanged(event); //$hide$
}
});

        mTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
@Override
public void doubleClick(DoubleClickEvent event) {
onTreeDoubleClick(event); //$hide$
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

//Synthetic comment -- @@ -393,8 +382,8 @@
}

private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -418,20 +407,20 @@

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
updateButtonsState();
//Synthetic comment -- @@ -533,29 +522,23 @@
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
//Synthetic comment -- @@ -564,52 +547,8 @@
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

//Synthetic comment -- @@ -619,7 +558,7 @@
// action done after loadPackages must check the UI hasn't been
// disposed yet. Otherwise hilarity ensues.

        final boolean displaySortByApi = isSortByApi();

if (mTreeColumnName.isDisposed()) {
// If the UI got disposed, don't try to load anything since we won't be
//Synthetic comment -- @@ -627,96 +566,22 @@
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
// Dynamically update the table while we load after each source.
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

updateButtonsState();
updateMenuCheckmarks();
//Synthetic comment -- @@ -784,14 +649,15 @@
if (elem == null) {
return;
}
        if (mTreeViewer != null && !mTreeViewer.getTree().isDisposed()) {

boolean enablePreviews =
                mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

            mTreeViewer.setExpandedState(elem, true);
nextCategory: for (Object pkg :
                    ((ITreeContentProvider) mTreeViewer.getContentProvider()).getChildren(elem)) {
if (pkg instanceof PkgCategory) {
PkgCategory cat = (PkgCategory) pkg;

//Synthetic comment -- @@ -826,7 +692,7 @@
boolean checked = event.getChecked();
Object elem = event.getElement();

        assert event.getSource() == mTreeViewer;

// When selecting, we want to only select compatible archives and expand the super nodes.
checkAndExpandItem(elem, checked, true/*fixChildren*/, true/*fixParent*/);
//Synthetic comment -- @@ -834,7 +700,7 @@
}

private void onTreeDoubleClick(DoubleClickEvent event) {
        assert event.getSource() == mTreeViewer;
ISelection sel = event.getSelection();
if (sel.isEmpty() || !(sel instanceof ITreeSelection)) {
return;
//Synthetic comment -- @@ -845,7 +711,8 @@
return;
}

        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();
Object[] children = provider.getElements(elem);
if (children == null) {
return;
//Synthetic comment -- @@ -853,16 +720,16 @@

if (children.length > 0) {
// If the element has children, expand/collapse it.
            if (mTreeViewer.getExpandedState(elem)) {
                mTreeViewer.collapseToLevel(elem, 1);
} else {
                mTreeViewer.expandToLevel(elem, 1);
}
} else {
// If the element is a terminal one, select/deselect it.
checkAndExpandItem(
elem,
                    !mTreeViewer.getChecked(elem),
false /*fixChildren*/,
true /*fixParent*/);
updateButtonsState();
//Synthetic comment -- @@ -874,11 +741,12 @@
boolean checked,
boolean fixChildren,
boolean fixParent) {
        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();

// fix the item itself
        if (checked != mTreeViewer.getChecked(elem)) {
            mTreeViewer.setChecked(elem, checked);
}
if (elem instanceof PkgItem) {
// update the PkgItem to reflect the selection
//Synthetic comment -- @@ -888,7 +756,7 @@
if (!checked) {
if (fixChildren) {
// when de-selecting, we deselect all children too
                mTreeViewer.setSubtreeChecked(elem, checked);
for (Object child : provider.getChildren(elem)) {
checkAndExpandItem(child, checked, fixChildren, false/*fixParent*/);
}
//Synthetic comment -- @@ -897,8 +765,8 @@
// fix the parent when deselecting
if (fixParent) {
Object parent = provider.getParent(elem);
                if (parent != null && mTreeViewer.getChecked(parent)) {
                    mTreeViewer.setChecked(parent, false);
}
}
return;
//Synthetic comment -- @@ -917,7 +785,7 @@
checkAndExpandItem(
children[0], true, false/*fixChildren*/, true/*fixParent*/);
} else {
                        mTreeViewer.setChecked(elem, false);
}
}
} else if (elem instanceof Package) {
//Synthetic comment -- @@ -928,17 +796,17 @@

if (fixParent && checked && elem instanceof PkgItem) {
Object parent = provider.getParent(elem);
            if (!mTreeViewer.getChecked(parent)) {
Object[] children = provider.getChildren(parent);
boolean allChecked = children.length > 0;
for (Object e : children) {
                    if (!mTreeViewer.getChecked(e)) {
allChecked = false;
break;
}
}
if (allChecked) {
                    mTreeViewer.setChecked(parent, true);
}
}
}
//Synthetic comment -- @@ -947,7 +815,7 @@
private void selectCompatibleArchives(Object pkg, ITreeContentProvider provider) {
for (Object archive : provider.getChildren(pkg)) {
if (archive instanceof Archive) {
                mTreeViewer.setChecked(archive, ((Archive) archive).isCompatible());
}
}
}
//Synthetic comment -- @@ -958,11 +826,7 @@
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
//Synthetic comment -- @@ -972,7 +836,7 @@
*/
private void onDeselectAll() {
// This does not update the tree itself, syncViewerSelection does it below.
        mDiffLogic.uncheckAllItems();
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -983,8 +847,10 @@
* This does not update the tree itself.
*/
private void copySelection(boolean fromSourceToApi) {
        List<PkgItem> fromItems = mDiffLogic.getAllPkgItems(!fromSourceToApi, fromSourceToApi);
        List<PkgItem> toItems = mDiffLogic.getAllPkgItems(fromSourceToApi, !fromSourceToApi);

// deselect all targets
for (PkgItem item : toItems) {
//Synthetic comment -- @@ -1009,9 +875,10 @@
* Synchronize the 'checked' state of PkgItems in the tree with their internal isChecked state.
*/
private void syncViewerSelection() {
        ITreeContentProvider provider = (ITreeContentProvider) mTreeViewer.getContentProvider();

        Object input = mTreeViewer.getInput();
if (input == null) {
return;
}
//Synthetic comment -- @@ -1024,10 +891,10 @@
boolean checked = item.isChecked();
allChecked &= checked;

                    if (checked != mTreeViewer.getChecked(item)) {
if (checked) {
                            if (!mTreeViewer.getExpandedState(cat)) {
                                mTreeViewer.setExpandedState(cat, true);
}
}
checkAndExpandItem(item, checked, true/*fixChildren*/, false/*fixParent*/);
//Synthetic comment -- @@ -1035,8 +902,8 @@
}
}

            if (allChecked != mTreeViewer.getChecked(cat)) {
                mTreeViewer.setChecked(cat, allChecked);
}
}
}
//Synthetic comment -- @@ -1090,12 +957,12 @@
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
//Synthetic comment -- @@ -1107,7 +974,7 @@

if (needsRefresh) {
// The local package list has changed, make sure to refresh it
                    localReload();
}
}
}
//Synthetic comment -- @@ -1123,12 +990,12 @@
* @return The number of archives that can be installed.
*/
private int getArchivesForInstall(List<Archive> outArchives) {
        if (mTreeViewer == null ||
                mTreeViewer.getTree() == null ||
                mTreeViewer.getTree().isDisposed()) {
return 0;
}
        Object[] checked = mTreeViewer.getCheckedElements();
if (checked == null) {
return 0;
}
//Synthetic comment -- @@ -1219,7 +1086,7 @@
try {
beginOperationPending();

                    mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);
//Synthetic comment -- @@ -1245,7 +1112,7 @@
endOperationPending();

// The local package list has changed, make sure to refresh it
                    localReload();
}
}
}
//Synthetic comment -- @@ -1263,12 +1130,12 @@
* @return The number of archives that can be deleted.
*/
private int getArchivesToDelete(StringBuilder outMsg, List<Archive> outArchives) {
        if (mTreeViewer == null ||
                mTreeViewer.getTree() == null ||
                mTreeViewer.getTree().isDisposed()) {
return 0;
}
        Object[] checked = mTreeViewer.getCheckedElements();
if (checked == null) {
// This should not happen since the button should be disabled
return 0;
//Synthetic comment -- @@ -1347,7 +1214,7 @@
@Override
public String getText(Object element) {

            if (mColumn == mColumnName) {
if (element instanceof PkgCategory) {
return ((PkgCategory) element).getLabel();
} else if (element instanceof PkgItem) {
//Synthetic comment -- @@ -1356,7 +1223,7 @@
return ((IDescription) element).getShortDescription();
}

            } else if (mColumn == mColumnApi) {
int api = -1;
if (element instanceof PkgItem) {
api = ((PkgItem) element).getApi();
//Synthetic comment -- @@ -1365,13 +1232,13 @@
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

//Synthetic comment -- @@ -1438,7 +1305,7 @@
}

private PkgCategory findCategoryForItem(PkgItem item) {
            List<PkgCategory> cats = mDiffLogic.getCategories(isSortByApi());
for (PkgCategory cat : cats) {
for (PkgItem i : cat.getItems()) {
if (i == item) {
//Synthetic comment -- @@ -1452,10 +1319,10 @@

@Override
public Image getImage(Object element) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();

if (imgFactory != null) {
                if (mColumn == mColumnName) {
if (element instanceof PkgCategory) {
return imgFactory.getImageForObject(((PkgCategory) element).getIconRef());
} else if (element instanceof PkgItem) {
//Synthetic comment -- @@ -1463,21 +1330,21 @@
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
//Synthetic comment -- @@ -1566,6 +1433,10 @@
}
}

// --- Implementation of ISdkChangeListener ---

@Override
//Synthetic comment -- @@ -1577,7 +1448,7 @@
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
//Synthetic comment -- index 0000000..1929999

//Synthetic comment -- @@ -0,0 +1,536 @@








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
//Synthetic comment -- index 0000000..a56a675

//Synthetic comment -- @@ -0,0 +1,208 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java
new file mode 100755
//Synthetic comment -- index 0000000..8bab727

//Synthetic comment -- @@ -0,0 +1,285 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
new file mode 100755
//Synthetic comment -- index 0000000..45a921b

//Synthetic comment -- @@ -0,0 +1,51 @@







