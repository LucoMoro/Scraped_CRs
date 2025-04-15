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









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 566981d..3c7207a 100755

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.sdkuilib.internal.repository.ui;

import com.android.SdkConstants;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
//Synthetic comment -- @@ -27,9 +25,6 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -92,16 +87,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public final class PackagesPage extends Composite implements ISdkChangeListener {

enum MenuAction {
RELOAD                      (SWT.NONE,  "Reload"),
//Synthetic comment -- @@ -133,9 +119,8 @@

private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

    private final PackagesPageImpl mImpl;
private final SdkInvocationContext mContext;

private boolean mDisplayArchives = false;
private boolean mOperationPending;
//Synthetic comment -- @@ -148,15 +133,8 @@
private Button mCheckFilterNew;
private Composite mGroupOptions;
private Composite mGroupSdk;
private Button mButtonDelete;
private Button mButtonInstall;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

//Synthetic comment -- @@ -166,25 +144,35 @@
UpdaterData updaterData,
SdkInvocationContext context) {
super(parent, swtStyle);
        mImpl = new PackagesPageImpl(updaterData) {
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
                return PackagesPage.this.getTreeFontItalic();
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
//Synthetic comment -- @@ -202,66 +190,67 @@
GridDataBuilder.create(mTextSdkOsPath).hFill().vCenter().hGrab();
mTextSdkOsPath.setEnabled(false);

        Group groupPackages = new Group(parent, SWT.NONE);
        mImpl.mGroupPackages = groupPackages;
        GridDataBuilder.create(mImpl.mGroupPackages).fill().grab().hSpan(2);
        groupPackages.setText("Packages");
        GridLayoutBuilder.create(groupPackages).columns(1);

        mImpl.mTreeViewer = new CheckboxTreeViewer(groupPackages, SWT.BORDER);
        mImpl.mTreeViewer.addFilter(new ViewerFilter() {
@Override
public boolean select(Viewer viewer, Object parentElement, Object element) {
return filterViewerItem(element);
}
});

        mImpl.mTreeViewer.addCheckStateListener(new ICheckStateListener() {
@Override
public void checkStateChanged(CheckStateChangedEvent event) {
onTreeCheckStateChanged(event); //$hide$
}
});

        mImpl.mTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
@Override
public void doubleClick(DoubleClickEvent event) {
onTreeDoubleClick(event); //$hide$
}
});

        Tree tree = mImpl.mTreeViewer.getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        GridDataBuilder.create(tree).fill().grab();

// column name icon is set when loading depending on the current filter type
// (e.g. API level or source)
        mImpl.mColumnName = new TreeViewerColumn(mImpl.mTreeViewer, SWT.NONE);
        mTreeColumnName = mImpl.mColumnName.getColumn();
mTreeColumnName.setText("Name");
mTreeColumnName.setWidth(340);

        mImpl.mColumnApi = new TreeViewerColumn(mImpl.mTreeViewer, SWT.NONE);
        TreeColumn treeColumn2 = mImpl.mColumnApi.getColumn();
treeColumn2.setText("API");
treeColumn2.setAlignment(SWT.CENTER);
treeColumn2.setWidth(50);

        mImpl.mColumnRevision = new TreeViewerColumn(mImpl.mTreeViewer, SWT.NONE);
        TreeColumn treeColumn3 = mImpl.mColumnRevision.getColumn();
treeColumn3.setText("Rev.");
treeColumn3.setToolTipText("Revision currently installed");
treeColumn3.setAlignment(SWT.CENTER);
treeColumn3.setWidth(50);


        mImpl.mColumnStatus = new TreeViewerColumn(mImpl.mTreeViewer, SWT.NONE);
        TreeColumn treeColumn4 = mImpl.mColumnStatus.getColumn();
treeColumn4.setText("Status");
treeColumn4.setAlignment(SWT.LEAD);
treeColumn4.setWidth(190);

        mGroupOptions = new Composite(groupPackages, SWT.NONE);
GridDataBuilder.create(mGroupOptions).hFill().vCenter().hGrab();
GridLayoutBuilder.create(mGroupOptions).columns(6).noMargins();

//Synthetic comment -- @@ -393,8 +382,8 @@
}

private Image getImage(String filename) {
        if (mImpl.mUpdaterData != null) {
            ImageFactory imgFactory = mImpl.mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -418,20 +407,20 @@

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
                    ((PkgContentProvider) mImpl.mTreeViewer.getContentProvider()).
                        setDisplayArchives(mDisplayArchives);
                    mImpl.mTreeViewer.setInput(null);
refreshViewerInput();
syncViewerSelection();
updateButtonsState();
//Synthetic comment -- @@ -533,29 +522,23 @@
}

private void postCreate() {
        mImpl.postCreate();

        if (mImpl.mUpdaterData != null) {
            mTextSdkOsPath.setText(mImpl.mUpdaterData.getOsSdkRoot());
}

        ((PkgContentProvider) mImpl.mTreeViewer.getContentProvider()).setDisplayArchives(
                mDisplayArchives);

        ColumnViewerToolTipSupport.enableFor(mImpl.mTreeViewer, ToolTip.NO_RECREATE);

        Tree tree = mImpl.mTreeViewer.getTree();
        FontData fontData = tree.getFont().getFontData()[0];
fontData.setStyle(SWT.ITALIC);
        mTreeFontItalic = new Font(tree.getDisplay(), fontData);

        tree.addDisposeListener(new DisposeListener() {
@Override
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
//Synthetic comment -- @@ -564,52 +547,8 @@
});
}

    private void loadPackages(boolean useLocalCache, boolean overrideExisting) {
        if (mImpl.mUpdaterData == null) {
return;
}

//Synthetic comment -- @@ -619,7 +558,7 @@
// action done after loadPackages must check the UI hasn't been
// disposed yet. Otherwise hilarity ensues.

        boolean displaySortByApi = isSortByApi();

if (mTreeColumnName.isDisposed()) {
// If the UI got disposed, don't try to load anything since we won't be
//Synthetic comment -- @@ -627,96 +566,22 @@
return;
}

        mTreeColumnName.setImage(getImage(
                displaySortByApi ? PackagesPageIcons.ICON_SORT_BY_API
                                 : PackagesPageIcons.ICON_SORT_BY_SOURCE));

        mImpl.loadPackagesImpl(useLocalCache, overrideExisting);
}

private void refreshViewerInput() {
// Dynamically update the table while we load after each source.
// Since the official Android source gets loaded first, it makes the
// window look non-empty a lot sooner.
        if (!mImpl.mGroupPackages.isDisposed()) {
            mImpl.setViewerInput();

// set the initial expanded state
            expandInitial(mImpl.mTreeViewer.getInput());

updateButtonsState();
updateMenuCheckmarks();
//Synthetic comment -- @@ -784,14 +649,15 @@
if (elem == null) {
return;
}
        if (mImpl.mTreeViewer != null && !mImpl.mTreeViewer.getTree().isDisposed()) {

boolean enablePreviews =
                mImpl.mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

            mImpl.mTreeViewer.setExpandedState(elem, true);
nextCategory: for (Object pkg :
                    ((ITreeContentProvider) mImpl.mTreeViewer.getContentProvider()).
                        getChildren(elem)) {
if (pkg instanceof PkgCategory) {
PkgCategory cat = (PkgCategory) pkg;

//Synthetic comment -- @@ -826,7 +692,7 @@
boolean checked = event.getChecked();
Object elem = event.getElement();

        assert event.getSource() == mImpl.mTreeViewer;

// When selecting, we want to only select compatible archives and expand the super nodes.
checkAndExpandItem(elem, checked, true/*fixChildren*/, true/*fixParent*/);
//Synthetic comment -- @@ -834,7 +700,7 @@
}

private void onTreeDoubleClick(DoubleClickEvent event) {
        assert event.getSource() == mImpl.mTreeViewer;
ISelection sel = event.getSelection();
if (sel.isEmpty() || !(sel instanceof ITreeSelection)) {
return;
//Synthetic comment -- @@ -845,7 +711,8 @@
return;
}

        ITreeContentProvider provider =
            (ITreeContentProvider) mImpl.mTreeViewer.getContentProvider();
Object[] children = provider.getElements(elem);
if (children == null) {
return;
//Synthetic comment -- @@ -853,16 +720,16 @@

if (children.length > 0) {
// If the element has children, expand/collapse it.
            if (mImpl.mTreeViewer.getExpandedState(elem)) {
                mImpl.mTreeViewer.collapseToLevel(elem, 1);
} else {
                mImpl.mTreeViewer.expandToLevel(elem, 1);
}
} else {
// If the element is a terminal one, select/deselect it.
checkAndExpandItem(
elem,
                    !mImpl.mTreeViewer.getChecked(elem),
false /*fixChildren*/,
true /*fixParent*/);
updateButtonsState();
//Synthetic comment -- @@ -874,11 +741,12 @@
boolean checked,
boolean fixChildren,
boolean fixParent) {
        ITreeContentProvider provider =
            (ITreeContentProvider) mImpl.mTreeViewer.getContentProvider();

// fix the item itself
        if (checked != mImpl.mTreeViewer.getChecked(elem)) {
            mImpl.mTreeViewer.setChecked(elem, checked);
}
if (elem instanceof PkgItem) {
// update the PkgItem to reflect the selection
//Synthetic comment -- @@ -888,7 +756,7 @@
if (!checked) {
if (fixChildren) {
// when de-selecting, we deselect all children too
                mImpl.mTreeViewer.setSubtreeChecked(elem, checked);
for (Object child : provider.getChildren(elem)) {
checkAndExpandItem(child, checked, fixChildren, false/*fixParent*/);
}
//Synthetic comment -- @@ -897,8 +765,8 @@
// fix the parent when deselecting
if (fixParent) {
Object parent = provider.getParent(elem);
                if (parent != null && mImpl.mTreeViewer.getChecked(parent)) {
                    mImpl.mTreeViewer.setChecked(parent, false);
}
}
return;
//Synthetic comment -- @@ -917,7 +785,7 @@
checkAndExpandItem(
children[0], true, false/*fixChildren*/, true/*fixParent*/);
} else {
                        mImpl.mTreeViewer.setChecked(elem, false);
}
}
} else if (elem instanceof Package) {
//Synthetic comment -- @@ -928,17 +796,17 @@

if (fixParent && checked && elem instanceof PkgItem) {
Object parent = provider.getParent(elem);
            if (!mImpl.mTreeViewer.getChecked(parent)) {
Object[] children = provider.getChildren(parent);
boolean allChecked = children.length > 0;
for (Object e : children) {
                    if (!mImpl.mTreeViewer.getChecked(e)) {
allChecked = false;
break;
}
}
if (allChecked) {
                    mImpl.mTreeViewer.setChecked(parent, true);
}
}
}
//Synthetic comment -- @@ -947,7 +815,7 @@
private void selectCompatibleArchives(Object pkg, ITreeContentProvider provider) {
for (Object archive : provider.getChildren(pkg)) {
if (archive instanceof Archive) {
                mImpl.mTreeViewer.setChecked(archive, ((Archive) archive).isCompatible());
}
}
}
//Synthetic comment -- @@ -958,11 +826,7 @@
*/
private void onSelectNewUpdates(boolean selectNew, boolean selectUpdates, boolean selectTop) {
// This does not update the tree itself, syncViewerSelection does it below.
        mImpl.onSelectNewUpdates(selectNew, selectUpdates, selectTop);
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -972,7 +836,7 @@
*/
private void onDeselectAll() {
// This does not update the tree itself, syncViewerSelection does it below.
        mImpl.onDeselectAll();
syncViewerSelection();
updateButtonsState();
}
//Synthetic comment -- @@ -983,8 +847,10 @@
* This does not update the tree itself.
*/
private void copySelection(boolean fromSourceToApi) {
        List<PkgItem> fromItems =
            mImpl.mDiffLogic.getAllPkgItems(!fromSourceToApi, fromSourceToApi);
        List<PkgItem> toItems =
            mImpl.mDiffLogic.getAllPkgItems(fromSourceToApi, !fromSourceToApi);

// deselect all targets
for (PkgItem item : toItems) {
//Synthetic comment -- @@ -1009,9 +875,10 @@
* Synchronize the 'checked' state of PkgItems in the tree with their internal isChecked state.
*/
private void syncViewerSelection() {
        ITreeContentProvider provider =
            (ITreeContentProvider) mImpl.mTreeViewer.getContentProvider();

        Object input = mImpl.mTreeViewer.getInput();
if (input == null) {
return;
}
//Synthetic comment -- @@ -1024,10 +891,10 @@
boolean checked = item.isChecked();
allChecked &= checked;

                    if (checked != mImpl.mTreeViewer.getChecked(item)) {
if (checked) {
                            if (!mImpl.mTreeViewer.getExpandedState(cat)) {
                                mImpl.mTreeViewer.setExpandedState(cat, true);
}
}
checkAndExpandItem(item, checked, true/*fixChildren*/, false/*fixParent*/);
//Synthetic comment -- @@ -1035,8 +902,8 @@
}
}

            if (allChecked != mImpl.mTreeViewer.getChecked(cat)) {
                mImpl.mTreeViewer.setChecked(cat, allChecked);
}
}
}
//Synthetic comment -- @@ -1090,12 +957,12 @@
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
//Synthetic comment -- @@ -1107,7 +974,7 @@

if (needsRefresh) {
// The local package list has changed, make sure to refresh it
                    mImpl.localReload();
}
}
}
//Synthetic comment -- @@ -1123,12 +990,12 @@
* @return The number of archives that can be installed.
*/
private int getArchivesForInstall(List<Archive> outArchives) {
        if (mImpl.mTreeViewer == null ||
                mImpl.mTreeViewer.getTree() == null ||
                mImpl.mTreeViewer.getTree().isDisposed()) {
return 0;
}
        Object[] checked = mImpl.mTreeViewer.getCheckedElements();
if (checked == null) {
return 0;
}
//Synthetic comment -- @@ -1219,7 +1086,7 @@
try {
beginOperationPending();

                    mImpl.mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);
//Synthetic comment -- @@ -1245,7 +1112,7 @@
endOperationPending();

// The local package list has changed, make sure to refresh it
                    mImpl.localReload();
}
}
}
//Synthetic comment -- @@ -1263,12 +1130,12 @@
* @return The number of archives that can be deleted.
*/
private int getArchivesToDelete(StringBuilder outMsg, List<Archive> outArchives) {
        if (mImpl.mTreeViewer == null ||
                mImpl.mTreeViewer.getTree() == null ||
                mImpl.mTreeViewer.getTree().isDisposed()) {
return 0;
}
        Object[] checked = mImpl.mTreeViewer.getCheckedElements();
if (checked == null) {
// This should not happen since the button should be disabled
return 0;
//Synthetic comment -- @@ -1347,7 +1214,7 @@
@Override
public String getText(Object element) {

            if (mColumn == mImpl.mColumnName) {
if (element instanceof PkgCategory) {
return ((PkgCategory) element).getLabel();
} else if (element instanceof PkgItem) {
//Synthetic comment -- @@ -1356,7 +1223,7 @@
return ((IDescription) element).getShortDescription();
}

            } else if (mColumn == mImpl.mColumnApi) {
int api = -1;
if (element instanceof PkgItem) {
api = ((PkgItem) element).getApi();
//Synthetic comment -- @@ -1365,13 +1232,13 @@
return Integer.toString(api);
}

            } else if (mColumn == mImpl.mColumnRevision) {
if (element instanceof PkgItem) {
PkgItem pkg = (PkgItem) element;
return pkg.getRevision().toShortString();
}

            } else if (mColumn == mImpl.mColumnStatus) {
if (element instanceof PkgItem) {
PkgItem pkg = (PkgItem) element;

//Synthetic comment -- @@ -1438,7 +1305,7 @@
}

private PkgCategory findCategoryForItem(PkgItem item) {
            List<PkgCategory> cats = mImpl.mDiffLogic.getCategories(isSortByApi());
for (PkgCategory cat : cats) {
for (PkgItem i : cat.getItems()) {
if (i == item) {
//Synthetic comment -- @@ -1452,10 +1319,10 @@

@Override
public Image getImage(Object element) {
            ImageFactory imgFactory = mImpl.mUpdaterData.getImageFactory();

if (imgFactory != null) {
                if (mColumn == mImpl.mColumnName) {
if (element instanceof PkgCategory) {
return imgFactory.getImageForObject(((PkgCategory) element).getIconRef());
} else if (element instanceof PkgItem) {
//Synthetic comment -- @@ -1463,21 +1330,21 @@
}
return imgFactory.getImageForObject(element);

                } else if (mColumn == mImpl.mColumnStatus && element instanceof PkgItem) {
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
//Synthetic comment -- @@ -1566,6 +1433,10 @@
}
}

    private Font getTreeFontItalic() {
        return mTreeFontItalic;
    }

// --- Implementation of ISdkChangeListener ---

@Override
//Synthetic comment -- @@ -1577,7 +1448,7 @@
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
//Synthetic comment -- index 0000000..1929999

//Synthetic comment -- @@ -0,0 +1,536 @@
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

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

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

    Composite mGroupPackages;
    CheckboxTreeViewer mTreeViewer;
    TreeViewerColumn mColumnName;
    TreeViewerColumn mColumnApi;
    TreeViewerColumn mColumnRevision;
    TreeViewerColumn mColumnStatus;

    PackagesPageImpl(UpdaterData updaterData) {
        mUpdaterData = updaterData;
        mDiffLogic = new PackagesDiffLogic(updaterData);
    }

    /**
     * Utility method that derived classes can override to check whether the UI is disposed.
     * When the UI is disposed, most operations that affect the UI will be bypassed.
     * @return True if UI is not available and should not be touched.
     */
    boolean isUiDisposed() {
        return mGroupPackages == null || mGroupPackages.isDisposed();
    }

    /**
     * Utility method to execute a runnable on the main UI thread.
     * Will do nothing if {@link #isUiDisposed()} returns false.
     * This is factored out so that derived classes can override as needed.
     * @param runnable The runnable to execute on the main UI thread.
     */
    void syncExec(Runnable runnable) {
        if (!isUiDisposed()) {
            mGroupPackages.getDisplay().syncExec(runnable);
        }
    }

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

    void postCreate() {
        assert mGroupPackages  != null;
        assert mTreeViewer     != null;
        assert mColumnApi      != null;
        assert mColumnName     != null;
        assert mColumnStatus   != null;
        assert mColumnRevision != null;

        mTreeViewer.setContentProvider(new PkgContentProvider(mTreeViewer));

        mColumnApi.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnApi)));
        mColumnName.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnName)));
        mColumnStatus.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnStatus)));
        mColumnRevision.setLabelProvider(
                new PkgTreeColumnViewerLabelProvider(new PkgCellLabelProvider(mColumnRevision)));
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
                            mTreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
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
                            mTreeViewer.getInput() != mDiffLogic.getCategories(isSortByApi())) {
                            refreshViewerInput();
                        }

                        if (!useLocalCache &&
                                mDiffLogic.isFirstLoadComplete() &&
                                !isUiDisposed()) {
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
        if (mTreeViewer.getInput() != cats) {
            // set initial input
            mTreeViewer.setInput(cats);
        } else {
            // refresh existing, which preserves the expanded state, the selection
            // and the checked state.
            mTreeViewer.refresh();
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
//Synthetic comment -- index 0000000..a56a675

//Synthetic comment -- @@ -0,0 +1,208 @@
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

import com.android.sdkuilib.internal.repository.UpdaterData;

import org.easymock.EasyMock;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class MockPackagesPageImpl extends PackagesPageImpl {

    private static class MockTreeViewer extends CheckboxTreeViewer {

        private final TreeItem mMockTreeItem;

        public MockTreeViewer(Tree mockTreeWidget, TreeItem mockTreeItem) {
            super(mockTreeWidget);
            mMockTreeItem = mockTreeItem;
        }

        @Override
        protected void createChildren(Widget widget) {
            // pass
        };

        @Override
        protected Item[] getChildren(Widget o) {
            return new Item[0];
        };

        @Override
        protected Item[] getSelection(Control widget) {
            return new Item[0];
        };

        @Override
        public ISelection getSelection() {
            return TreeSelection.EMPTY;
        };

        @Override
        protected ViewerRow getViewerRowFromItem(Widget item) {
            return new MockViewerRow(doGetColumnCount(), null);
        }

        public void doUpdate(Object element) {
            doUpdateItem(mMockTreeItem, element);
        }
    };

    public MockPackagesPageImpl(UpdaterData updaterData) {
        super(updaterData);
    }

    /** UI is never disposed in the unit test. */
    @Override
    boolean isUiDisposed() {
        return false;
    }

    /** Sync exec always executes immediately in the unit test, no threading is used. */
    @Override
    void syncExec(Runnable runnable) {
        runnable.run();
    }

    @Override
    void postCreate() {
        // Create the enough fake UI to satisfy our needs

        Display mockDisplay = EasyMock.createNiceMock(Display.class);
        EasyMock.replay(mockDisplay);

        mGroupPackages = EasyMock.createNiceMock(Composite.class);
        EasyMock.expect(mGroupPackages.isDisposed()).andReturn(false).anyTimes();
        EasyMock.expect(mGroupPackages.getDisplay()).andReturn(mockDisplay).anyTimes();
        EasyMock.replay(mGroupPackages);

        Tree mockTreeWidget = EasyMock.createNiceMock(Tree.class);
        EasyMock.expect(mockTreeWidget.isDisposed()).andReturn(false).anyTimes();
        EasyMock.expect(mockTreeWidget.getDisplay()).andReturn(mockDisplay).anyTimes();
        EasyMock.replay(mockTreeWidget);

        TreeColumn mockColumnWidget = EasyMock.createNiceMock(TreeColumn.class);
        EasyMock.expect(mockColumnWidget.isDisposed()).andReturn(false).anyTimes();
        EasyMock.expect(mockColumnWidget.getDisplay()).andReturn(mockDisplay).anyTimes();
        EasyMock.replay(mockColumnWidget);

        final TreeItem mockTreeItem = EasyMock.createNiceMock(TreeItem.class);
        EasyMock.expect(mockTreeItem.isDisposed()).andReturn(false).anyTimes();
        EasyMock.expect(mockTreeItem.getDisplay()).andReturn(mockDisplay).anyTimes();
        EasyMock.replay(mockTreeItem);

        mTreeViewer = new MockTreeViewer(mockTreeWidget);
        mColumnApi      = new TreeViewerColumn(mTreeViewer, mockColumnWidget);
        mColumnName     = new TreeViewerColumn(mTreeViewer, mockColumnWidget);
        mColumnStatus   = new TreeViewerColumn(mTreeViewer, mockColumnWidget);
        mColumnRevision = new TreeViewerColumn(mTreeViewer, mockColumnWidget);

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
        StringBuilder sb = new StringBuilder();
        ITreeContentProvider provider =
            (ITreeContentProvider) mTreeViewer.getContentProvider();

        for (Object cat : provider.getElements(mTreeViewer.getInput())) {
            if (cat == null) {
                continue;
            }
            MockViewerRow row = new MockViewerRow(4, cat);
            row.refreshRow();
            row.asText(sb).append("\n");

            Object[] children = provider.getElements(cat);
            if (children == null) {
                continue;
            }
            for (Object child : children) {
                row = new MockViewerRow(4, child);
                row.refreshRow();
                row.asText(sb.append("+-- ")).append("\n");
            }
        }

        return sb.toString();
    }

    /*
    private void appendColumns(Object element, StringBuilder sb) throws Exception {


        ViewerCell cell = new
        ViewerColumn_refresh(mColumnName);
        sb.append(lp.update(arg0))
    }

    private CellLabelProvider ViewerColumn_getLabelProvider(ViewerColumn c) throws Exception {
        Method m = c.getClass().getMethod("getLabelProvider", CellLabelProvider.class);
        m.setAccessible(true);
        return (CellLabelProvider) m.invoke(c);
    }

    private void ViewerColumn_refresh(ViewerColumn col) throws Exception {

        //ViewerCell capturingViewerCell = new ViewerCell(null, 0, capturingViewerCell) {
        //};

        Method m = col.getClass().getMethod("refresh", ViewerCell.class);
        m.setAccessible(true);
        (CellLabelProvider) m.invoke(col, cell);
    }
    */

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/ui/MockViewerRow.java
new file mode 100755
//Synthetic comment -- index 0000000..8bab727

//Synthetic comment -- @@ -0,0 +1,285 @@
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
     * @throws Exception If reflection fails to invoke ViewerCell.update(ViewerRow, int, Object).
     */
    public void refreshRow() throws Exception {

        Method m = ViewerCell.class.getMethod("update", ViewerRow.class, int.class, Object.class);
        m.setAccessible(true);

        for (int col = 0; col < mNumColumns; col++) {
            ViewerCell cell = getCell(col);
            m.invoke(cell, this, col, mElement);
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







