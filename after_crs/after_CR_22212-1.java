/*SDK Manager 2 UI

- Remove "details" and go back to the "obsolete" filter.

- Refactor: constants for all icons names.

- Rework sortByAPI to refresh in-place. This allows the table
  to preserve its state (expanded, selected, checked) when
  being refreshed. In-place not done for the sortBySource mode
  yet.

Change-Id:I81560091253f5a3250b7472d050ffa7e8b86fb88*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 2d10d4f..78115c4 100755

//Synthetic comment -- @@ -23,16 +23,20 @@
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.Package.UpdateInfo;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
//Synthetic comment -- @@ -63,10 +67,15 @@
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* Page that displays both locally installed packages as well as all known
//Synthetic comment -- @@ -76,15 +85,24 @@
public class PackagesPage extends Composite
implements ISdkChangeListener, IPageListener {

    private static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
    private static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$
    private static final String ICON_SORT_BY_SOURCE = "source_icon16.png";      //$NON-NLS-1$
    private static final String ICON_COLUMN_NAME    = "platform_pkg_16.png";    //$NON-NLS-1$
    private static final String ICON_PKG_NEW        = "pkg_new_16.png";         //$NON-NLS-1$
    private static final String ICON_PKG_UPDATE     = "pkg_update_16.png";      //$NON-NLS-1$
    private static final String ICON_PKG_INSTALLED  = "pkg_installed_16.png";   //$NON-NLS-1$

private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
private final UpdaterData mUpdaterData;

    private boolean mDisplayArchives = false;   // TODO toggle via a menu item

    private Text mTextSdkOsPath;
private Button mCheckSortSource;
private Button mCheckSortApi;
    private Button mCheckFilterObsolete;
private Button mCheckFilterInstalled;
private Button mCheckFilterNew;
private Composite mGroupOptions;
//Synthetic comment -- @@ -99,7 +117,6 @@
private TreeViewerColumn mColumnRevision;
private TreeViewerColumn mColumnStatus;
private Color mColorUpdate;
private Font mTreeFontItalic;
private Button mButtonReload;

//Synthetic comment -- @@ -156,7 +173,7 @@

mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn1 = mColumnName.getColumn();
        treeColumn1.setImage(getImage(ICON_COLUMN_NAME));      //$NON-NLS-1$
treeColumn1.setWidth(340);
treeColumn1.setText("Name");

//Synthetic comment -- @@ -195,10 +212,10 @@
mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages(true /*updateButtons*/);
}
});
        mCheckFilterNew.setImage(getImage(ICON_PKG_NEW));        //$NON-NLS-1$
mCheckFilterNew.setSelection(true);
mCheckFilterNew.setText("Updates/New");

//Synthetic comment -- @@ -207,24 +224,23 @@
mCheckFilterInstalled.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages(true /*updateButtons*/);
}
});
        mCheckFilterInstalled.setImage(getImage(ICON_PKG_INSTALLED));  //$NON-NLS-1$
mCheckFilterInstalled.setSelection(true);
mCheckFilterInstalled.setText("Installed");

        mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterObsolete.setToolTipText("Also show obsolete packages");
        mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages(true /*updateButtons*/);
}
});
        mCheckFilterObsolete.setSelection(false);
        mCheckFilterObsolete.setText("Obsolete");

mButtonReload = new Button(mGroupOptions, SWT.NONE);
mButtonReload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//Synthetic comment -- @@ -259,10 +275,12 @@
mCheckSortApi.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages(true /*updateButtons*/);
                // Reset the exnanded state when changing sort algorithm
                expandInitial(mCategories);
}
});
        mCheckSortApi.setImage(getImage(ICON_COLUMN_NAME));  //$NON-NLS-1$
mCheckSortApi.setText("API level");
mCheckSortApi.setSelection(true);

//Synthetic comment -- @@ -271,10 +289,12 @@
mCheckSortSource.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages(true /*updateButtons*/);
                // Reset the exnanded state when changing sort algorithm
                expandInitial(mCategories);
}
});
        mCheckSortSource.setImage(getImage(ICON_SORT_BY_SOURCE));  //$NON-NLS-1$
mCheckSortSource.setText("Source");

Link link = new Link(mGroupOptions, SWT.NONE);
//Synthetic comment -- @@ -338,16 +358,13 @@
mTreeFontItalic = new Font(mTree.getDisplay(), fontData);

mColorUpdate = new Color(mTree.getDisplay(), 0xff, 0xff, 0xcc);

mTree.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
mColorUpdate.dispose();
mTreeFontItalic = null;
mColorUpdate = null;
}
});
}
//Synthetic comment -- @@ -360,6 +377,7 @@
try {
enableUi(mGroupPackages, false);

            boolean firstLoad = mPackages.size() == 0;
mPackages.clear();

// get local packages
//Synthetic comment -- @@ -397,10 +415,19 @@
}

if (!isUpdate) {
                                PkgItem pi = new PkgItem(pkg, PkgState.NEW);
mPackages.add(pi);
}
}

                        // Dynamically update the table while we load after each source.
                        // Since the official Android source gets loaded first, it makes the
                        // window seem to appear to load faster.
                        mGroupPackages.getDisplay().syncExec(new Runnable() {
                            public void run() {
                                sortPackages(true /*updateButtons*/);
                            }
                        });
}

monitor.setDescription("Done loading %1$d packages from %2$d sources",
//Synthetic comment -- @@ -409,7 +436,11 @@
}
});

            if (firstLoad) {
                // set the initial expanded state
                expandInitial(mCategories);
            }

} finally {
enableUi(mGroupPackages, true);
updateButtonsState();
//Synthetic comment -- @@ -427,68 +458,156 @@
}
}

    private void sortPackages(boolean updateButtons) {
if (mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection()) {
sortByAPI();
} else {
sortBySource();
}
        if (updateButtons) {
            updateButtonsState();
        }
}

/**
* Recompute the tree by sorting all the packages by API.
     * This does an update in-place of the mCategories list so that the table
     * can preserve its state (checked / expanded / selected) properly.
*/
private void sortByAPI() {

ImageFactory imgFactory = mUpdaterData.getImageFactory();

        // keep a map of the initial state so that we can detect which items or categories are
        // no longer being used, so that we can removed them at the end of the in-place update.
        final Map<Integer, Pair<PkgCategory, HashSet<PkgItem>> > unusedItemsMap =
            new HashMap<Integer, Pair<PkgCategory, HashSet<PkgItem>> >();
        final Set<PkgCategory> unusedCatSet = new HashSet<PkgCategory>();

        // get existing categories
        for (PkgCategory cat : mCategories) {
            unusedCatSet.add(cat);
            unusedItemsMap.put(cat.getKey(), Pair.of(cat, new HashSet<PkgItem>(cat.getItems())));
        }

        // always add the tools & extras categories, even if empty (unlikely anyway)
        if (!unusedItemsMap.containsKey(PkgCategory.KEY_TOOLS)) {
PkgCategory cat = new PkgCategory(
                    PkgCategory.KEY_TOOLS,
                    "Tools",
                    imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgCategory.KEY_TOOLS, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);
}

        if (!unusedItemsMap.containsKey(PkgCategory.KEY_EXTRA)) {
            PkgCategory cat = new PkgCategory(
                    PkgCategory.KEY_EXTRA,
                    "Add-ons & Extras",
                    imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgCategory.KEY_EXTRA, Pair.of(cat, new HashSet<PkgItem>()));
            mCategories.add(cat);
        }

        for (PkgItem item : mPackages) {
            if (!keepItem(item)) {
                continue;
            }

            int apiKey = item.getApi();

            if (apiKey < 1) {
                Package p = item.getPackage();
                if (p instanceof ToolPackage || p instanceof PlatformToolPackage) {
                    apiKey = PkgCategory.KEY_TOOLS;
                } else {
                    apiKey = PkgCategory.KEY_EXTRA;
                }
            }

            Pair<PkgCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apiKey);

            if (mapEntry == null) {
                // This is a new category. Create it and add it to the map.
                // We need a label for the category. Use null right now and set it later.
                PkgCategory cat = new PkgCategory(
                        apiKey,
                        null /*label*/,
                        imgFactory.getImageByName(ICON_CAT_PLATFORM));
                mapEntry = Pair.of(cat, new HashSet<PkgItem>());
                unusedItemsMap.put(apiKey, mapEntry);
                mCategories.add(0, cat);
            }
            PkgCategory cat = mapEntry.getFirst();
            assert cat != null;
            unusedCatSet.remove(cat);

            HashSet<PkgItem> unusedItemsSet = mapEntry.getSecond();
            unusedItemsSet.remove(item);
            if (!cat.getItems().contains(item)) {
                cat.getItems().add(item);
            }

            if (apiKey != -1 && cat.getLabel() == null) {
                // Check whether we can get the actual platform version name (e.g. "1.5")
                // from the first Platform package we find in this category.
                Package p = item.getPackage();
                if (p instanceof PlatformPackage) {
                    String vn = ((PlatformPackage) p).getVersionName();
                    String name = String.format("Android %1$s (API %2$d)", vn, apiKey);
                    cat.setLabel(name);
                }
            }
        }

        for (Iterator<PkgCategory> iterCat = mCategories.iterator(); iterCat.hasNext(); ) {
            PkgCategory cat = iterCat.next();

            // Remove any unused categories.
            if (unusedCatSet.contains(cat)) {
                iterCat.remove();
                continue;
            }

            // Remove any unused items in the category.
            Integer apikey = cat.getKey();
            Pair<PkgCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apikey);
            assert mapEntry != null;
            HashSet<PkgItem> unusedItems = mapEntry.getSecond();
            for (Iterator<PkgItem> iterItem = cat.getItems().iterator(); iterItem.hasNext(); ) {
                PkgItem item = iterItem.next();
                if (unusedItems.contains(item)) {
                    iterItem.remove();
                }
            }

            // Sort the items
            Collections.sort(cat.getItems());

            // Fix the category name for any API where we might not have found a platform package.
            if (cat.getLabel() == null) {
                int api = cat.getKey().intValue();
                String name = String.format("API %1$d", api);
                cat.setLabel(name);
            }
        }

        // Sort the categories list in decreasing order
        Collections.sort(mCategories, new Comparator<PkgCategory>() {
            public int compare(PkgCategory cat1, PkgCategory cat2) {
                // compare in descending order (o2-o1)
                return cat2.getKey().compareTo(cat1.getKey());
            }
        });

        if (mTreeViewer.getInput() != mCategories) {
            // set initial input
            mTreeViewer.setInput(mCategories);
        } else {
            // refresh existing, which preserves the expanded state, the selection
            // and the checked state.
            mTreeViewer.refresh();
        }
}

/**
//Synthetic comment -- @@ -522,34 +641,32 @@
for (SdkSource source : sources) {
Object key = source != null ? source : "Installed Packages";
Object iconRef = source != null ? source :
                        mUpdaterData.getImageFactory().getImageByName(ICON_PKG_INSTALLED);

            PkgCategory cat = new PkgCategory(
                    key.hashCode(),
                    key.toString(),
                    iconRef);

for (PkgItem item : mPackages) {
if (item.getSource() == source) {
                    cat.getItems().add(item);
}
}

mCategories.add(cat);
}

        // We don't support in-place incremental updates so the table gets reset
        // each time we load when sorted by source.
mTreeViewer.setInput(mCategories);
}

/**
* Decide whether to keep an item in the current tree based on user-choosen filter options.
*/
private boolean keepItem(PkgItem item) {
        if (!mCheckFilterObsolete.getSelection()) {
if (item.isObsolete()) {
return false;
}
//Synthetic comment -- @@ -562,8 +679,8 @@
}

if (!mCheckFilterNew.getSelection()) {
            if (item.getState() == PkgState.NEW ||
                    item.getState() == PkgState.HAS_UPDATE) {
return false;
}
}
//Synthetic comment -- @@ -583,7 +700,7 @@
PkgCategory cat = (PkgCategory) pkg;
for (PkgItem item : cat.getItems()) {
if (item.getState() == PkgState.INSTALLED
                            || item.getState() == PkgState.HAS_UPDATE) {
expandInitial(pkg);
break;
}
//Synthetic comment -- @@ -643,7 +760,7 @@
private void updateButtonsState() {
boolean canInstall = false;

        if (mDisplayArchives) {
// In detail mode, we display archives so we can install if at
// least one archive is selected.

//Synthetic comment -- @@ -715,8 +832,7 @@
protected void onButtonInstall() {
ArrayList<Archive> archives = new ArrayList<Archive>();

        if (mDisplayArchives) {
// In detail mode, we display archives so we can install only the
// archives that are actually selected.

//Synthetic comment -- @@ -761,7 +877,7 @@

mUpdaterData.updateOrInstallAll_WithGUI(
archives,
                    mCheckFilterObsolete.getSelection() /* includeObsoletes */);
} finally {
// loadPackages will also re-enable the UI
loadPackages();
//Synthetic comment -- @@ -872,7 +988,7 @@
PkgItem pkg = (PkgItem) element;

if (pkg.getState() == PkgState.INSTALLED ||
                            pkg.getState() == PkgState.HAS_UPDATE) {
return Integer.toString(pkg.getRevision());
}
}
//Synthetic comment -- @@ -885,12 +1001,10 @@
switch(pkg.getState()) {
case INSTALLED:
return "Installed";
                    case HAS_UPDATE:
return "Update available";
                    case NEW:
return "Not installed. New revision " + Integer.toString(pkg.getRevision());
}
return pkg.getState().toString();

//Synthetic comment -- @@ -919,13 +1033,11 @@
} else if (mColumn == mColumnStatus && element instanceof PkgItem) {
switch(((PkgItem) element).getState()) {
case INSTALLED:
                        return imgFactory.getImageByName(ICON_PKG_INSTALLED);   //$NON-NLS-1$
                    case HAS_UPDATE:
                        return imgFactory.getImageByName(ICON_PKG_UPDATE);      //$NON-NLS-1$
                    case NEW:
                        return imgFactory.getImageByName(ICON_PKG_NEW);         //$NON-NLS-1$
}
}
}
//Synthetic comment -- @@ -936,7 +1048,7 @@

public Font getFont(Object element, int columnIndex) {
if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() == PkgState.NEW) {
return mTreeFontItalic;
}
} else if (element instanceof Package) {
//Synthetic comment -- @@ -950,11 +1062,8 @@

public Color getBackground(Object element, int columnIndex) {
if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() == PkgState.HAS_UPDATE) {
                    return mColorUpdate;
}
}
return null;
//Synthetic comment -- @@ -974,7 +1083,7 @@
return ((ArrayList<?>) parentElement).toArray();

} else if (parentElement instanceof PkgCategory) {
                return ((PkgCategory) parentElement).getItems().toArray();

} else if (parentElement instanceof PkgItem) {
List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();
//Synthetic comment -- @@ -982,12 +1091,12 @@
return pkgs.toArray();
}

                if (mDisplayArchives) {
return ((PkgItem) parentElement).getArchives();
}

} else if (parentElement instanceof Package) {
                if (mDisplayArchives) {
return ((Package) parentElement).getArchives();
}

//Synthetic comment -- @@ -1014,12 +1123,12 @@
return !pkgs.isEmpty();
}

                if (mDisplayArchives) {
Archive[] archives = ((PkgItem) parentElement).getArchives();
return archives.length > 0;
}
} else if (parentElement instanceof Package) {
                if (mDisplayArchives) {
return ((Package) parentElement).getArchives().length > 0;
}
}
//Synthetic comment -- @@ -1042,34 +1151,64 @@
}

private static class PkgCategory {
        private final Integer mKey;
private final Object mIconRef;
        private final List<PkgItem> mItems = new ArrayList<PkgItem>();
        private String mLabel;

        // When storing by API, key is the API level (>=1), except 0 is tools and 1 is extra/addons.
        // When sorting by Source, key is the hash of the source's name.
        public final static Integer KEY_TOOLS = Integer.valueOf(0);
        public final static Integer KEY_EXTRA = Integer.valueOf(-1);

        public PkgCategory(Integer key, String label, Object iconRef) {
mKey = key;
            mLabel = label;
mIconRef = iconRef;
        }

        public Integer getKey() {
            return mKey;
}

public String getLabel() {
            return mLabel;
        }

        public void setLabel(String label) {
            mLabel = label;
}

public Object getIconRef() {
return mIconRef;
}

        public List<PkgItem> getItems() {
return mItems;
}
}

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

    public static class PkgItem implements Comparable<PkgItem> {
private final Package mPkg;
private PkgState mState;
private List<Package> mUpdatePkgs;
//Synthetic comment -- @@ -1077,6 +1216,7 @@
public PkgItem(Package pkg, PkgState state) {
mPkg = pkg;
mState = state;
            assert mPkg != null;
}

public boolean isObsolete() {
//Synthetic comment -- @@ -1098,7 +1238,7 @@
mUpdatePkgs = new ArrayList<Package>();
}
mUpdatePkgs.add(pkg);
                mState = PkgState.HAS_UPDATE;
return true;
}

//Synthetic comment -- @@ -1126,7 +1266,7 @@
}

public SdkSource getSource() {
            if (mState == PkgState.NEW) {
return mPkg.getParentSource();
} else {
return null;
//Synthetic comment -- @@ -1146,6 +1286,10 @@
public Archive[] getArchives() {
return mPkg.getArchives();
}

        public int compareTo(PkgItem pkg) {
            return getPackage().compareTo(pkg.getPackage());
        }
}










//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index e552da8..fec846b 100755

//Synthetic comment -- @@ -51,21 +51,22 @@

// UI fields
private final Label mLabel;
    private final Control mStopButton;
private final ProgressBar mProgressBar;
private final StringBuffer mResultText = new StringBuffer();


/**
     * Creates a new {@link ProgressView} object, a simple "holder" for the various
     * widget used to display and update a progress + status bar.
*/
    public ProgressView(Label label, ProgressBar progressBar, Control stopButton) {
mLabel = label;
mProgressBar = progressBar;
mProgressBar.setEnabled(false);

        mStopButton = stopButton;
        mStopButton.addListener(SWT.Selection, new Listener() {
public void handleEvent(Event event) {
if (mState == State.ACTIVE) {
changeState(State.STOP_PENDING);
//Synthetic comment -- @@ -125,9 +126,9 @@
mState = state;
}

        syncExec(mStopButton, new Runnable() {
public void run() {
                mStopButton.setEnabled(mState == State.ACTIVE);
}
});








