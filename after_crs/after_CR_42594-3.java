/*[WIP] Refactor PackagesPage to make it testable.

Not ready for review. Don't look at me!

Change-Id:I58ff45895916a14a10f501a9bd664782d777ed42*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/SdkManagerTestCase.java
//Synthetic comment -- index 46d1db4..86a555a 100755

//Synthetic comment -- @@ -143,6 +143,14 @@
AndroidLocation.resetFolder();
File addonsDir = new File(sdkDir, SdkConstants.FD_ADDONS);
addonsDir.mkdir();

        File toolsDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_FOLDER);
        toolsDir.mkdir();
        new File(toolsDir, SdkConstants.androidCmdName()).createNewFile();
        new File(toolsDir, SdkConstants.FN_EMULATOR).createNewFile();

        // TODO makePlatformTools with at least a source props

File toolsLibEmuDir = new File(sdkDir, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER + "emulator");
toolsLibEmuDir.mkdirs();
new File(toolsLibEmuDir, "snapshots.img").createNewFile();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 5353b74..f5a2ed3 100755

//Synthetic comment -- @@ -32,7 +32,7 @@
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgItem.PkgState;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -688,9 +688,9 @@
// Always add the tools & extras categories, even if empty (unlikely anyway)
if (needTools) {
PkgCategoryApi acat = new PkgCategoryApi(
                   PkgCategoryApi.KEY_TOOLS,
                   null,
                   mUpdaterData.getImageFactory().getImageByName(PackagesPageIcons.ICON_CAT_OTHER));
synchronized (cats) {
cats.add(acat);
}
//Synthetic comment -- @@ -698,9 +698,9 @@

if (needExtras) {
PkgCategoryApi acat = new PkgCategoryApi(
                   PkgCategoryApi.KEY_EXTRA,
                   null,
                   mUpdaterData.getImageFactory().getImageByName(PackagesPageIcons.ICON_CAT_OTHER));
synchronized (cats) {
cats.add(acat);
}
//Synthetic comment -- @@ -733,9 +733,9 @@
}

cat = new PkgCategoryApi(
                key,
                platformName,
                mUpdaterData.getImageFactory().getImageByName(PackagesPageIcons.ICON_CAT_PLATFORM));

return cat;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java
//Synthetic comment -- index cdf6b59..b73288b 100755

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;


public class PkgCategorySource extends PkgCategory {
//Synthetic comment -- @@ -42,8 +42,8 @@
source, // the source is the key and it can be null
source == UNKNOWN_SOURCE ? "Local Packages" : source.toString(),
source == UNKNOWN_SOURCE ?
                updaterData.getImageFactory().getImageByName(PackagesPageIcons.ICON_PKG_INSTALLED) :
                source);
mSource = source;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java
//Synthetic comment -- index 6f7589c..8adf428 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.ui.PackagesPage;

import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

//Synthetic comment -- @@ -33,10 +34,10 @@
*/
public class PkgContentProvider implements ITreeContentProvider {

    private final IInputProvider mViewer;
private boolean mDisplayArchives;

    public PkgContentProvider(IInputProvider viewer) {
mViewer = viewer;
}

//Synthetic comment -- @@ -139,7 +140,7 @@
}

@Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
// unused
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 566981d..025be46 100755

//Synthetic comment -- @@ -16,20 +16,12 @@

package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -50,7 +42,6 @@
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
//Synthetic comment -- @@ -65,7 +56,6 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//Synthetic comment -- @@ -79,8 +69,6 @@
import org.eclipse.swt.widgets.TreeColumn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -92,16 +80,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public final class PackagesPage extends Composite implements ISdkChangeListener {

enum MenuAction {
RELOAD                      (SWT.NONE,  "Reload"),
//Synthetic comment -- @@ -133,13 +112,13 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

    private final PackagesPageImpl mImpl;
private final SdkInvocationContext mContext;

private boolean mDisplayArchives = false;
private boolean mOperationPending;

    private Composite mGroupPackages;
private Text mTextSdkOsPath;
private Button mCheckSortSource;
private Button mCheckSortApi;
//Synthetic comment -- @@ -148,17 +127,11 @@
private Button mCheckFilterNew;
private Composite mGroupOptions;
private Composite mGroupSdk;
private Button mButtonDelete;
private Button mButtonInstall;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;
    private CheckboxTreeViewer mTreeViewer;

public PackagesPage(
Composite parent,
//Synthetic comment -- @@ -166,25 +139,45 @@
UpdaterData updaterData,
SdkInvocationContext context) {
super(parent, swtStyle);
        mImpl = new PackagesPageImpl(updaterData) {
            @Override
            protected boolean isUiDisposed() {
                return mGroupPackages == null || mGroupPackages.isDisposed();
            };
            @Override
            protected void syncExec(Runnable runnable) {
                if (!isUiDisposed()) {
                    mGroupPackages.getDisplay().syncExec(runnable);
                }
            };
            @Override
            protected void refreshViewerInput() {
                PackagesPage.this.refreshViewerInput();
            }

            @Override
            protected boolean isSortByApi() {
                return PackagesPage.this.isSortByApi();
            }

            @Override
            protected Font getTreeFontItalic() {
                return mTreeFontItalic;
            }

            @Override
            protected void loadPackages(boolean useLocalCache, boolean overrideExisting) {
                PackagesPage.this.loadPackages(useLocalCache, overrideExisting);
            }
        };
        mContext = context;

createContents(this);
postCreate();  //$hide$
}

public void performFirstLoad() {
        mImpl.performFirstLoad();
}

@SuppressWarnings("unused")
//Synthetic comment -- @@ -202,12 +195,39 @@
GridDataBuilder.create(mTextSdkOsPath).hFill().vCenter().hGrab();
mTextSdkOsPath.setEnabled(false);

        Group groupPackages = new Group(parent, SWT.NONE);
        mGroupPackages = groupPackages;
GridDataBuilder.create(mGroupPackages).fill().grab().hSpan(2);
        groupPackages.setText("Packages");
        GridLayoutBuilder.create(groupPackages).columns(1);

        mTreeViewer = new CheckboxTreeViewer(groupPackages, SWT.BORDER);
        mImpl.setITreeViewer(new PackagesPageImpl.ICheckboxTreeViewer() {
            @Override
            public Object getInput() {
                return mTreeViewer.getInput();
            }

            @Override
            public void setInput(List<PkgCategory> cats) {
                mTreeViewer.setInput(cats);
            }

            @Override
            public void setContentProvider(PkgContentProvider pkgContentProvider) {
                mTreeViewer.setContentProvider(pkgContentProvider);
            }

            @Override
            public void refresh() {
                mTreeViewer.refresh();
            }

            @Override
            public Object[] getCheckedElements() {
                return mTreeViewer.getCheckedElements();
            }
        });
mTreeViewer.addFilter(new ViewerFilter() {
@Override
public boolean select(Viewer viewer, Object parentElement, Object element) {
//Synthetic comment -- @@ -229,39 +249,45 @@
}
});

        Tree tree = mTreeViewer.getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        GridDataBuilder.create(tree).fill().grab();

// column name icon is set when loading depending on the current filter type
// (e.g. API level or source)
        TreeViewerColumn columnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        mTreeColumnName = columnName.getColumn();
mTreeColumnName.setText("Name");
mTreeColumnName.setWidth(340);

        TreeViewerColumn columnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn2 = columnApi.getColumn();
treeColumn2.setText("API");
treeColumn2.setAlignment(SWT.CENTER);
treeColumn2.setWidth(50);

        TreeViewerColumn columnRevision = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn3 = columnRevision.getColumn();
treeColumn3.setText("Rev.");
treeColumn3.setToolTipText("Revision currently installed");
treeColumn3.setAlignment(SWT.CENTER);
treeColumn3.setWidth(50);


        TreeViewerColumn columnStatus = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        TreeColumn treeColumn4 = columnStatus.getColumn();
treeColumn4.setText("Status");
treeColumn4.setAlignment(SWT.LEAD);
treeColumn4.setWidth(190);

        mImpl.setIColumns(
                wrapColumn(columnName),
                wrapColumn(columnApi),
                wrapColumn(columnRevision),
                wrapColumn(columnStatus));

        mGroupOptions = new Composite(groupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
GridLayoutBuilder.create(mGroupOptions).columns(6).noMargins();

//Synthetic comment -- @@ -392,9 +418,18 @@
});
}

    private PackagesPageImpl.ITreeViewerColumn wrapColumn(final TreeViewerColumn column) {
        return new PackagesPageImpl.ITreeViewerColumn() {
            @Override
            public void setLabelProvider(ColumnLabelProvider labelProvider) {
                column.setLabelProvider(labelProvider);
            }
        };
    }

private Image getImage(String filename) {
        if (mImpl.mUpdaterData != null) {
            ImageFactory imgFactory = mImpl.mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -418,19 +453,19 @@

switch (action) {
case RELOAD:
                    mImpl.fullReload();
break;
case SHOW_ADDON_SITES:
                    AddonSitesDialog d = new AddonSitesDialog(getShell(), mImpl.mUpdaterData);
if (d.open()) {
                        mImpl.loadPackages();
}
break;
case TOGGLE_SHOW_ARCHIVES:
mDisplayArchives = !mDisplayArchives;
// Force the viewer to be refreshed
                    ((PkgContentProvider) mTreeViewer.getContentProvider()).
                        setDisplayArchives(mDisplayArchives);
mTreeViewer.setInput(null);
refreshViewerInput();
syncViewerSelection();
//Synthetic comment -- @@ -533,29 +568,23 @@
}

private void postCreate() {
        mImpl.postCreate();

        if (mImpl.mUpdaterData != null) {
            mTextSdkOsPath.setText(mImpl.mUpdaterData.getOsSdkRoot());
}

((PkgContentProvider) mTreeViewer.getContentProvider()).setDisplayArchives(
                mDisplayArchives);

ColumnViewerToolTipSupport.enableFor(mTreeViewer, ToolTip.NO_RECREATE);

        Tree tree = mTreeViewer.getTree();
        FontData fontData = tree.getFont().getFontData()[0];
fontData.setStyle(SWT.ITALIC);
        mTreeFontItalic = new Font(tree.getDisplay(), fontData);

        tree.addDisposeListener(new DisposeListener() {
@Override
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
//Synthetic comment -- @@ -564,52 +593,8 @@
});
}

    private void loadPackages(boolean useLocalCache, boolean overrideExisting) {
        if (mImpl.mUpdaterData == null) {
return;
}

//Synthetic comment -- @@ -619,7 +604,7 @@
// action done after loadPackages must check the UI hasn't been
// disposed yet. Otherwise hilarity ensues.

        boolean displaySortByApi = isSortByApi();

if (mTreeColumnName.isDisposed()) {
// If the UI got disposed, don't try to load anything since we won't be
//Synthetic comment -- @@ -627,76 +612,11 @@
return;
}

        mTreeColumnName.setImage(getImage(
                displaySortByApi ? PackagesPageIcons.ICON_SORT_BY_API
                                 : PackagesPageIcons.ICON_SORT_BY_SOURCE));

        mImpl.loadPackagesImpl(useLocalCache, overrideExisting);
}

private void refreshViewerInput() {
//Synthetic comment -- @@ -704,16 +624,9 @@
// Since the official Android source gets loaded first, it makes the
// window look non-empty a lot sooner.
if (!mGroupPackages.isDisposed()) {
            try {
                mImpl.setViewerInput();
            } catch (Exception ignore) {}

// set the initial expanded state
expandInitial(mTreeViewer.getInput());
//Synthetic comment -- @@ -787,11 +700,12 @@
if (mTreeViewer != null && !mTreeViewer.getTree().isDisposed()) {

boolean enablePreviews =
                mImpl.mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

mTreeViewer.setExpandedState(elem, true);
nextCategory: for (Object pkg :
                    ((ITreeContentProvider) mTreeViewer.getContentProvider()).
                        getChildren(elem)) {
if (pkg instanceof PkgCategory) {
PkgCategory cat = (PkgCategory) pkg;

//Synthetic comment -- @@ -845,7 +759,8 @@
return;
}

        ITreeContentProvider provider =
            (ITreeContentProvider) mTreeViewer.getContentProvider();
Object[] children = provider.getElements(elem);
if (children == null) {
return;
//Synthetic comment -- @@ -874,7 +789,8 @@
boolean checked,
boolean fixChildren,
boolean fixParent) {
        ITreeContentProvider provider =
            (ITreeContentProvider) mTreeViewer.getContentProvider();

// fix the item itself
if (checked != mTreeViewer.getChecked(elem)) {
//Synthetic comment -- @@ -958,11 +874,7 @@
*/
private void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
// This does not update the tree itself, syncViewerSelection does it below.
        mImpl.onSelectNewUpdates(selectNew, selectUpdates, selectTop);
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -972,7 +884,7 @@
*/
private void onDeselectAll() {
// This does not update the tree itself, syncViewerSelection does it below.
        mImpl.onDeselectAll();
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -983,8 +895,10 @@
* This does not update the tree itself.
*/
private void copySelection(boolean fromSourceToApi) {
        List<PkgItem> fromItems =
            mImpl.mDiffLogic.getAllPkgItems(!fromSourceToApi, fromSourceToApi);
        List<PkgItem> toItems =
            mImpl.mDiffLogic.getAllPkgItems(fromSourceToApi, !fromSourceToApi);

// deselect all targets
for (PkgItem item : toItems) {
//Synthetic comment -- @@ -1090,12 +1004,12 @@
ArrayList<Archive> archives = new ArrayList<Archive>();
getArchivesForInstall(archives);

        if (mImpl.mUpdaterData != null) {
boolean needsRefresh = false;
try {
beginOperationPending();

                List<Archive> installed = mImpl.mUpdaterData.updateOrInstallAll_WithGUI(
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */,
mContext == SdkInvocationContext.IDE ?
//Synthetic comment -- @@ -1107,7 +1021,7 @@

if (needsRefresh) {
// The local package list has changed, make sure to refresh it
                    mImpl.localReload();
}
}
}
//Synthetic comment -- @@ -1219,7 +1133,7 @@
try {
beginOperationPending();

                    mImpl.mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);
//Synthetic comment -- @@ -1245,7 +1159,7 @@
endOperationPending();

// The local package list has changed, make sure to refresh it
                    mImpl.localReload();
}
}
}
//Synthetic comment -- @@ -1335,236 +1249,6 @@

// ----------------------


// --- Implementation of ISdkChangeListener ---

//Synthetic comment -- @@ -1577,7 +1261,7 @@
public void onSdkReload() {
// The sdkmanager finished reloading its data. We must not call localReload() from here
// since we don't want to alter the sdkmanager's data that just finished loading.
        mImpl.loadPackages();
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageIcons.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageIcons.java
new file mode 100755
//Synthetic comment -- index 0000000..4fe8fca

//Synthetic comment -- @@ -0,0 +1,33 @@
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

package com.android.sdkuilib.internal.repository.ui;


/**
 * Icons used by {@link PackagesPage}.
 */
public class PackagesPageIcons {

    public static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
    public static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$
    public static final String ICON_SORT_BY_SOURCE = "source_icon16.png";      //$NON-NLS-1$
    public static final String ICON_SORT_BY_API    = "platform_pkg_16.png";    //$NON-NLS-1$
    public static final String ICON_PKG_NEW        = "pkg_new_16.png";         //$NON-NLS-1$
    public static final String ICON_PKG_INCOMPAT   = "pkg_incompat_16.png";    //$NON-NLS-1$
    public static final String ICON_PKG_UPDATE     = "pkg_update_16.png";      //$NON-NLS-1$
    public static final String ICON_PKG_INSTALLED  = "pkg_installed_16.png";   //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
new file mode 100755
//Synthetic comment -- index 0000000..c61bef9

//Synthetic comment -- @@ -0,0 +1,555 @@
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

package com.android.sdkuilib.internal.repository.ui;

import com.android.SdkConstants;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
import com.android.sdkuilib.internal.repository.core.PkgItem;
import com.android.sdkuilib.internal.repository.core.PkgItem.PkgState;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Base class for {@link PackagesPage} that holds most of the logic to display
 * the tree/list of packages. This class holds most of the logic and {@link PackagesPage}
 * holds most of the UI (creating the UI, dealing with menus and buttons and tree
 * selection.) This makes it easier to test the functionality by mocking only a
 * subset of the UI.
 */
abstract class PackagesPageImpl {

    final UpdaterData mUpdaterData;
    final PackagesDiffLogic mDiffLogic;

    private ICheckboxTreeViewer mITreeViewer;
    private ITreeViewerColumn   mIColumnName;
    private ITreeViewerColumn   mIColumnApi;
    private ITreeViewerColumn   mIColumnRevision;
    private ITreeViewerColumn   mIColumnStatus;

    PackagesPageImpl(UpdaterData updaterData) {
        mUpdaterData = updaterData;
        mDiffLogic = new PackagesDiffLogic(updaterData);
    }

    /**
     * Utility method that derived classes can override to check whether the UI is disposed.
     * When the UI is disposed, most operations that affect the UI will be bypassed.
     * @return True if UI is not available and should not be touched.
     */
    abstract protected boolean isUiDisposed();

    /**
     * Utility method to execute a runnable on the main UI thread.
     * Will do nothing if {@link #isUiDisposed()} returns false.
     * @param runnable The runnable to execute on the main UI thread.
     */
    abstract protected void syncExec(Runnable runnable);

    void performFirstLoad() {
        // First a package loader is created that only checks
        // the local cache xml files. It populates the package
        // list based on what the client got last, essentially.
        loadPackages(true /*useLocalCache*/, false /*overrideExisting*/);

        // Next a regular package loader is created that will
        // respect the expiration and refresh parameters of the
        // download cache.
        loadPackages(false /*useLocalCache*/, true /*overrideExisting*/);
    }

    public void setITreeViewer(ICheckboxTreeViewer iTreeViewer) {
        mITreeViewer = iTreeViewer;
    }

    public void setIColumns(
            ITreeViewerColumn columnName,
            ITreeViewerColumn columnApi,
            ITreeViewerColumn columnRevision,
            ITreeViewerColumn columnStatus) {
        mIColumnName = columnName;
        mIColumnApi = columnApi;
        mIColumnRevision = columnRevision;
        mIColumnStatus = columnStatus;
    }

    void postCreate() {
        // Caller needs to call setITreeViewer before this.
        assert mITreeViewer     != null;
        // Caller needs to call setIColumns before this.
        assert mIColumnApi      != null;
        assert mIColumnName     != null;
        assert mIColumnStatus   != null;
        assert mIColumnRevision != null;

        mITreeViewer.setContentProvider(new PkgContentProvider(mITreeViewer));

        mIColumnApi.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mIColumnApi)));
        mIColumnName.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mIColumnName)));
        mIColumnStatus.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mIColumnStatus)));
        mIColumnRevision.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mIColumnRevision)));
    }

    /**
     * Performs a full reload by removing all cached packages data, including the platforms
     * and addons from the sdkmanager instance. This will perform a full local parsing
     * as well as a full reload of the remote data (by fetching all sources again.)
     */
    void fullReload() {
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
    void localReload() {
        // Clear all source caches, otherwise loading will use the cached data
        mUpdaterData.getLocalSdkParser().clearPackages();
        mUpdaterData.getSdkManager().reloadSdk(mUpdaterData.getSdkLog());
        loadPackages();
    }

    /**
     * Performs a "normal" reload of the package information, use the default download
     * cache and refreshing strategy as needed.
     */
    void loadPackages() {
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
    abstract protected void loadPackages(boolean useLocalCache, boolean overrideExisting);

    /**
     * Actual implementation of {@link #loadPackages(boolean, boolean)}.
     * Derived implementations must call this to do the actual work after setting up the UI.
     */
    void loadPackagesImpl(final boolean useLocalCache, final boolean overrideExisting) {
        if (mUpdaterData == null) {
            return;
        }

        final boolean displaySortByApi = isSortByApi();

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

                syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (changed ||
                            mITreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
                                refreshViewerInput();
                        }
                    }
                });

                // Return true to tell the loader to continue with the next source.
                // Return false to stop the loader if any UI has been disposed, which can
                // happen if the user is trying to close the window during the load operation.
                return !isUiDisposed();
            }

            @Override
            public void onLoadCompleted() {
                // This runs in a thread and must not access UI directly.
                final boolean changed = mDiffLogic.updateEnd(displaySortByApi);

                syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (changed ||
                            mITreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
                            try {
                                refreshViewerInput();
                            } catch (Exception ignore) {}
                        }

                        if (!useLocalCache &&
                                mDiffLogic.isFirstLoadComplete() &&
                                !isUiDisposed()) {
                            // At the end of the first load, if nothing is selected then
                            // automatically select all new and update packages.
                            Object[] checked = mITreeViewer.getCheckedElements();
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
        });
    }

    /**
     * Overridden by the UI to respond to a request to refresh the tree viewer
     * when the input has changed.
     * The implementation must call {@link #setViewerInput()} somehow and will
     * also need to adjust the expand state of the tree items and/or update
     * some buttons or other state.
     */
    abstract protected void refreshViewerInput();

    /**
     * Invoked from {@link #refreshViewerInput()} to actually either set the
     * input of the tree viewer or refresh it if it's the <em>same</em> input
     * object.
     */
    protected void setViewerInput() {
        List<PkgCategory> cats = mDiffLogic.getCategories(isSortByApi());
        if (mITreeViewer.getInput() != cats) {
            // set initial input
            mITreeViewer.setInput(cats);
        } else {
            // refresh existing, which preserves the expanded state, the selection
            // and the checked state.
            mITreeViewer.refresh();
        }
    }

    /**
     * Overridden by the UI to determine if the tree should display packages sorted
     * by API (returns true) or by repository source (returns false.)
     */
    abstract protected boolean isSortByApi();

    /**
     * Checks all PkgItems that are either new or have updates or select top platform
     * for initial run.
     */
    void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
        // This does not update the tree itself, syncViewerSelection does it in the caller.
        mDiffLogic.checkNewUpdateItems(
                selectNew,
                selectUpdates,
                selectTop,
                SdkConstants.CURRENT_PLATFORM);
    }

    /**
     * Deselect all checked PkgItems.
     */
    void onDeselectAll() {
        // This does not update the tree itself, syncViewerSelection does it in the caller.
        mDiffLogic.uncheckAllItems();
    }

    // ----------------------

    abstract protected Font getTreeFontItalic();

    class PkgCellLabelProvider extends ColumnLabelProvider implements ITableFontProvider {

        private final ITreeViewerColumn mColumn;

        public PkgCellLabelProvider(ITreeViewerColumn column) {
            super();
            mColumn = column;
        }

        @Override
        public String getText(Object element) {

            if (mColumn == mIColumnName) {
                if (element instanceof PkgCategory) {
                    return ((PkgCategory) element).getLabel();
                } else if (element instanceof PkgItem) {
                    return getPkgItemName((PkgItem) element);
                } else if (element instanceof IDescription) {
                    return ((IDescription) element).getShortDescription();
                }

            } else if (mColumn == mIColumnApi) {
                int api = -1;
                if (element instanceof PkgItem) {
                    api = ((PkgItem) element).getApi();
                }
                if (api >= 1) {
                    return Integer.toString(api);
                }

            } else if (mColumn == mIColumnRevision) {
                if (element instanceof PkgItem) {
                    PkgItem pkg = (PkgItem) element;
                    return pkg.getRevision().toShortString();
                }

            } else if (mColumn == mIColumnStatus) {
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
                if (mColumn == mIColumnName) {
                    if (element instanceof PkgCategory) {
                        return imgFactory.getImageForObject(((PkgCategory) element).getIconRef());
                    } else if (element instanceof PkgItem) {
                        return imgFactory.getImageForObject(((PkgItem) element).getMainPackage());
                    }
                    return imgFactory.getImageForObject(element);

                } else if (mColumn == mIColumnStatus && element instanceof PkgItem) {
                    PkgItem pi = (PkgItem) element;
                    switch(pi.getState()) {
                    case INSTALLED:
                        if (pi.hasUpdatePkg()) {
                            return imgFactory.getImageByName(PackagesPageIcons.ICON_PKG_UPDATE);
                        } else {
                            return imgFactory.getImageByName(PackagesPageIcons.ICON_PKG_INSTALLED);
                        }
                    case NEW:
                        Package p = pi.getMainPackage();
                        if (p != null && p.hasCompatibleArchive()) {
                            return imgFactory.getImageByName(PackagesPageIcons.ICON_PKG_NEW);
                        } else {
                            return imgFactory.getImageByName(PackagesPageIcons.ICON_PKG_INCOMPAT);
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
                    return getTreeFontItalic();
                }
            } else if (element instanceof Package) {
                // update package
                return getTreeFontItalic();
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

    interface ICheckboxTreeViewer extends IInputProvider {
        void setContentProvider(PkgContentProvider pkgContentProvider);
        void refresh();
        void setInput(List<PkgCategory> cats);
        Object[] getCheckedElements();
    }

    interface ITreeViewerColumn {
        void setLabelProvider(ColumnLabelProvider labelProvider);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index 45e3163..a63b237 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.utils.ILogger;
import com.android.utils.NullLogger;
//Synthetic comment -- @@ -47,6 +48,7 @@

private DownloadCache mMockDownloadCache;

    /** Creates a {@link MockUpdaterData} using a {@link MockEmptySdkManager}. */
public MockUpdaterData() {
super(SDK_PATH, new MockLog());

//Synthetic comment -- @@ -54,6 +56,14 @@
setImageFactory(new NullImageFactory());
}

    /** Creates a {@link MockUpdaterData} using the given {@link SdkManager}. */
    public MockUpdaterData(SdkManager sdkManager) {
        super(sdkManager.getLocation(), new MockLog());
        setSdkManager(sdkManager);
        setTaskFactory(new MockTaskFactory());
        setImageFactory(new NullImageFactory());
    }

/** Gives access to the internal {@link #installArchives(List, int)}. */
public void _installArchives(List<ArchiveInfo> result) {
installArchives(result, 0/*flags*/);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java
new file mode 100755
//Synthetic comment -- index 0000000..8e9778e

//Synthetic comment -- @@ -0,0 +1,210 @@
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

package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Font;

import java.util.ArrayList;
import java.util.List;

public class MockPackagesPageImpl extends PackagesPageImpl {

    public MockPackagesPageImpl(UpdaterData updaterData) {
        super(updaterData);
    }

    /** UI is never disposed in the unit test. */
    @Override
    protected boolean isUiDisposed() {
        return false;
    }

    /** Sync exec always executes immediately in the unit test, no threading is used. */
    @Override
    protected void syncExec(Runnable runnable) {
        runnable.run();
    }

    private MockTreeViewer mTreeViewer;

    @Override
    void postCreate() {
        mTreeViewer = new MockTreeViewer();
        setITreeViewer(mTreeViewer);

        setIColumns(new MockTreeColumn(mTreeViewer),  // columnName
                    new MockTreeColumn(mTreeViewer),  // columnApi
                    new MockTreeColumn(mTreeViewer),  // columnRevision
                    new MockTreeColumn(mTreeViewer)); // columnStatus

        super.postCreate();
    }

    @Override
    protected void refreshViewerInput() {
        super.setViewerInput();
    }

    @Override
    protected boolean isSortByApi() {
        return true;
    }

    @Override
    protected Font getTreeFontItalic() {
        return null;
    }

    @Override
    protected void loadPackages(boolean useLocalCache, boolean overrideExisting) {
        super.loadPackagesImpl(useLocalCache, overrideExisting);
    }

    /**
     * Get a dump-out of the tree in a format suitable for unit testing.
     */
    public String getMockTreeDisplay() throws Exception {
        return mTreeViewer.getTreeDisplay();
    }

    private static class MockTreeViewer implements PackagesPageImpl.ICheckboxTreeViewer {
        private final SparseIntArray mWidths = new SparseIntArray();
        private final List<MockTreeColumn> mColumns = new ArrayList<MockTreeColumn>();
        private List<PkgCategory> mInput;
        private PkgContentProvider mPkgContentProvider;
        private String mLastRefresh;
        private static final String SPACE = "                                                 ";

        @Override
        public void setInput(List<PkgCategory> input) {
            mInput = input;
            refresh();
        }

        @Override
        public Object getInput() {
            return mInput;
        }

        @Override
        public void setContentProvider(PkgContentProvider pkgContentProvider) {
            mPkgContentProvider = pkgContentProvider;
        }

        @Override
        public void refresh() {
            // Recompute the display of the tree
            StringBuilder sb = new StringBuilder();
            boolean widthChanged = false;

            for (int render = 0; render < (widthChanged ? 2 : 1); render++) {
                for (Object cat : mPkgContentProvider.getElements(mInput)) {
                    if (cat == null) {
                        continue;
                    }

                    if (sb.length() > 0) {
                        sb.append('\n');
                    }

                    widthChanged = rowAsString(cat, sb, 4);

                    Object[] children = mPkgContentProvider.getElements(cat);
                    if (children == null) {
                        continue;
                    }
                    for (Object child : children) {
                        sb.append("\n+-- ");
                        widthChanged = rowAsString(child, sb, 0);
                    }
                }
            }

            mLastRefresh = sb.toString();
        }

        boolean rowAsString(Object element, StringBuilder sb, int space) {
            boolean widthChanged = false;
            sb.append("[ ");
            for (int col = 0; col < mColumns.size(); col++) {
                if (col > 0) {
                    sb.append(" | ");
                }
                String t = mColumns.get(col).getLabelProvider().getText(element);
                if (t == null) {
                    t = "(null)";
                }
                int len = t.length();
                int w = mWidths.get(col);
                if (len > w) {
                    widthChanged = true;
                    mWidths.put(col, len);
                    w = len;
                }
                String pad = len >= w ? "" : SPACE.substring(SPACE.length() - w + len);
                if (col == 0 && space > 0) {
                    sb.append(SPACE.substring(SPACE.length() - space));
                }
                if (col >= 1 && col <= 2) {
                    sb.append(pad);
                }
                sb.append(t);
                if (col == 0 || col > 2) {
                    sb.append(pad);
                }
            }
            sb.append(" ]");
            return widthChanged;
        }

        @Override
        public Object[] getCheckedElements() {
            return null;
        }

        public void addColumn(MockTreeColumn mockTreeColumn) {
            mColumns.add(mockTreeColumn);
        }

        public String getTreeDisplay() {
            return mLastRefresh;
        }
    }

    private static class MockTreeColumn implements PackagesPageImpl.ITreeViewerColumn {
        private ColumnLabelProvider mLabelProvider;

        public MockTreeColumn(MockTreeViewer treeViewer) {
            treeViewer.addColumn(this);
        }

        @Override
        public void setLabelProvider(ColumnLabelProvider labelProvider) {
            mLabelProvider = labelProvider;
        }

        public ColumnLabelProvider getLabelProvider() {
            return mLabelProvider;
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java
new file mode 100755
//Synthetic comment -- index 0000000..db3179d

//Synthetic comment -- @@ -0,0 +1,293 @@
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

package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.util.SparseArray;

import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import java.lang.reflect.Method;

public class MockViewerRow extends ViewerRow {

    private final int mNumColumns;
    private final Object mElement;

    SparseArray<Font> mFonts = new SparseArray<Font>();
    SparseArray<Image> mImages = new SparseArray<Image>();
    SparseArray<String> mTexts = new SparseArray<String>();
    SparseArray<Rectangle> mBounds = new SparseArray<Rectangle>();
    SparseArray<Color> mBackgrounds = new SparseArray<Color>();
    SparseArray<Color> mForegrounds = new SparseArray<Color>();


    public MockViewerRow(int numColumns, Object element) {
        mNumColumns = numColumns;
        mElement = element;
    }

    /**
     * Get the bounds of the entry at the columnIndex,
     *
     * @param columnIndex
     * @return {@link Rectangle}
     */
    @Override
    public Rectangle getBounds(int columnIndex) {
        return mBounds.get(columnIndex);
    }

    /**
     * Return the bounds for the whole item.
     *
     * @return {@link Rectangle}
     */
    @Override
    public Rectangle getBounds() {
        return mBounds.get(0);
    }

    /**
     * Return the item for the receiver.
     *
     * @return {@link Widget}
     */
    @Override
    public Widget getItem() {
        return null;
    }

    /**
     * Return the number of columns for the receiver.
     *
     * @return the number of columns
     */
    @Override
    public int getColumnCount() {
        return mNumColumns;
    }

    /**
     * Return the image at the columnIndex.
     *
     * @param columnIndex
     * @return {@link Image} or <code>null</code>
     */
    @Override
    public Image getImage(int columnIndex) {
        return mImages.get(columnIndex);
    }

    /**
     * Set the image at the columnIndex
     *
     * @param columnIndex
     * @param image
     */
    @Override
    public void setImage(int columnIndex, Image image) {
        mImages.put(columnIndex, image);
    }

    /**
     * Get the text at the columnIndex.
     *
     * @param columnIndex
     * @return {@link String}
     */
    @Override
    public String getText(int columnIndex) {
        return mTexts.get(columnIndex);
    }

    /**
     * Set the text at the columnIndex
     *
     * @param columnIndex
     * @param text
     */
    @Override
    public void setText(int columnIndex, String text) {
        mTexts.put(columnIndex, text);
    }

    /**
     * Get the background at the columnIndex,
     *
     * @param columnIndex
     * @return {@link Color} or <code>null</code>
     */
    @Override
    public Color getBackground(int columnIndex) {
        return mBackgrounds.get(columnIndex);
    }

    /**
     * Set the background at the columnIndex.
     *
     * @param columnIndex
     * @param color
     */
    @Override
    public void setBackground(int columnIndex, Color color) {
        mBackgrounds.put(columnIndex, color);
    }

    /**
     * Get the foreground at the columnIndex.
     *
     * @param columnIndex
     * @return {@link Color} or <code>null</code>
     */
    @Override
    public Color getForeground(int columnIndex) {
        return mForegrounds.get(columnIndex);
    }

    /**
     * Set the foreground at the columnIndex.
     *
     * @param columnIndex
     * @param color
     */
    @Override
    public void setForeground(int columnIndex, Color color) {
        mForegrounds.put(columnIndex, color);
    }

    /**
     * Get the font at the columnIndex.
     *
     * @param columnIndex
     * @return {@link Font} or <code>null</code>
     */
    @Override
    public Font getFont(int columnIndex) {
        return mFonts.get(columnIndex);
    }

    /**
     * Set the {@link Font} at the columnIndex.
     *
     * @param columnIndex
     * @param font
     */
    @Override
    public void setFont(int columnIndex, Font font) {
        mFonts.put(columnIndex, font);
    }

    /**
     * Get the Control for the receiver.
     *
     * @return {@link Control}
     */
    @Override
    public Control getControl() {
        return null;
    }

    /**
     * Returns a neighboring row, or <code>null</code> if no neighbor exists
     * in the given direction. If <code>sameLevel</code> is <code>true</code>,
     * only sibling rows (under the same parent) will be considered.
     *
     * @param direction
     *            the direction {@link #BELOW} or {@link #ABOVE}
     *
     * @param sameLevel
     *            if <code>true</code>, search only within sibling rows
     * @return the row above/below, or <code>null</code> if not found
     */
    @Override
    public ViewerRow getNeighbor(int direction, boolean sameLevel) {
        return null;
    }

    /**
     * The tree path used to identify an element by the unique path
     *
     * @return the path
     */
    @Override
    public TreePath getTreePath() {
        return null;
    }

    @Override
    public Object clone() {
        return null;
    }

    /**
     * @return the model element
     */
    @Override
    public Object getElement() {
        return mElement;
    }

    /**
     * Calls refresh on all the ViewerCells of the row (up to numColumns).
     */
    public void refreshRow() {

        Method m;
        try {
            m = ViewerCell.class.getMethod("update", ViewerRow.class, int.class, Object.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        m.setAccessible(true);

        for (int col = 0; col < mNumColumns; col++) {
            ViewerCell cell = getCell(col);
            try {
                m.invoke(cell, this, col, mElement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns a text representation of all the columns' text suitable for unit tests.
     * @param sb {@link StringBuilder} to fill in. It is not reset, only appended to.
     * @return The input {@code sb} argument, for chaining.
     */
    public StringBuilder asText(StringBuilder sb) {
        sb.append("[ ");
        for (int col = 0; col < mNumColumns; col++) {
            if (col > 0) {
                sb.append(" | ");
            }
            String t = getText(col);
            sb.append(t == null ? "(null)" : t);
        }
        sb.append(" ]");
        return sb;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
new file mode 100755
//Synthetic comment -- index 0000000..45a921b

//Synthetic comment -- @@ -0,0 +1,51 @@
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

package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdkuilib.internal.repository.MockUpdaterData;

import org.easymock.EasyMock;

public class SdkManagerUpgradeTest extends SdkManagerTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Create a mock page and list the current SDK state
     */
    public void testPackagesPage() throws Exception {
        SdkManager sdkman = getSdkManager();
        MockUpdaterData updaterData = new MockUpdaterData(sdkman);
        MockPackagesPageImpl pageImpl = new MockPackagesPageImpl(updaterData);
        pageImpl.postCreate();
        pageImpl.performFirstLoad();

        String actual = pageImpl.getMockTreeDisplay();
        assertEquals("", actual);
    }

}







