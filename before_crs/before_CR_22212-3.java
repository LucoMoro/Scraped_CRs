/*SDK Manager 2: details vs obsolete, better sort by API.

- Remove "details" and go back to the "obsolete" filter.

- Refactor: constants for all icons names.

- Rework sortByAPI to refresh in-place. This allows the table
  to preserve its state (expanded, selected, checked) when
  being refreshed. In-place not done for the sortBySource mode
  yet.

Change-Id:I81560091253f5a3250b7472d050ffa7e8b86fb88*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 2d10d4f..749f927 100755

//Synthetic comment -- @@ -23,10 +23,13 @@
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.Package.UpdateInfo;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
//Synthetic comment -- @@ -63,9 +66,13 @@
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
//Synthetic comment -- @@ -76,15 +83,24 @@
public class PackagesPage extends Composite
implements ISdkChangeListener, IPageListener {

private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
private final UpdaterData mUpdaterData;

    private Text mTextSdkOsPath;

private Button mCheckSortSource;
private Button mCheckSortApi;
    private Button mCheckFilterDetails;
private Button mCheckFilterInstalled;
private Button mCheckFilterNew;
private Composite mGroupOptions;
//Synthetic comment -- @@ -99,7 +115,6 @@
private TreeViewerColumn mColumnRevision;
private TreeViewerColumn mColumnStatus;
private Color mColorUpdate;
    private Color mColorNew;
private Font mTreeFontItalic;
private Button mButtonReload;

//Synthetic comment -- @@ -156,7 +171,7 @@

mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn1 = mColumnName.getColumn();
        treeColumn1.setImage(getImage("platform_pkg_16.png"));      //$NON-NLS-1$
treeColumn1.setWidth(340);
treeColumn1.setText("Name");

//Synthetic comment -- @@ -195,10 +210,10 @@
mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages();
}
});
        mCheckFilterNew.setImage(getImage("reject_icon16.png"));        //$NON-NLS-1$
mCheckFilterNew.setSelection(true);
mCheckFilterNew.setText("Updates/New");

//Synthetic comment -- @@ -207,24 +222,23 @@
mCheckFilterInstalled.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages();
}
});
        mCheckFilterInstalled.setImage(getImage("accept_icon16.png"));  //$NON-NLS-1$
mCheckFilterInstalled.setSelection(true);
mCheckFilterInstalled.setText("Installed");

        mCheckFilterDetails = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterDetails.setToolTipText("Show everything including obsolete packages and all archives)");
        mCheckFilterDetails.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages();
}
});
        mCheckFilterDetails.setImage(getImage("nopkg_icon16.png"));  //$NON-NLS-1$
        mCheckFilterDetails.setSelection(false);
        mCheckFilterDetails.setText("Details");

mButtonReload = new Button(mGroupOptions, SWT.NONE);
mButtonReload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//Synthetic comment -- @@ -259,10 +273,12 @@
mCheckSortApi.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages();
}
});
        mCheckSortApi.setImage(getImage("platform_pkg_16.png"));  //$NON-NLS-1$
mCheckSortApi.setText("API level");
mCheckSortApi.setSelection(true);

//Synthetic comment -- @@ -271,10 +287,12 @@
mCheckSortSource.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                sortPackages();
}
});
        mCheckSortSource.setImage(getImage("source_icon16.png"));  //$NON-NLS-1$
mCheckSortSource.setText("Source");

Link link = new Link(mGroupOptions, SWT.NONE);
//Synthetic comment -- @@ -338,16 +356,13 @@
mTreeFontItalic = new Font(mTree.getDisplay(), fontData);

mColorUpdate = new Color(mTree.getDisplay(), 0xff, 0xff, 0xcc);
        mColorNew = new Color(mTree.getDisplay(), 0xff, 0xee, 0xcc);

mTree.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
mColorUpdate.dispose();
                mColorNew.dispose();
mTreeFontItalic = null;
mColorUpdate = null;
                mColorNew = null;
}
});
}
//Synthetic comment -- @@ -360,6 +375,7 @@
try {
enableUi(mGroupPackages, false);

mPackages.clear();

// get local packages
//Synthetic comment -- @@ -397,10 +413,19 @@
}

if (!isUpdate) {
                                PkgItem pi = new PkgItem(pkg, PkgState.NEW_AVAILABLE);
mPackages.add(pi);
}
}
}

monitor.setDescription("Done loading %1$d packages from %2$d sources",
//Synthetic comment -- @@ -409,7 +434,11 @@
}
});

            sortPackages();
} finally {
enableUi(mGroupPackages, true);
updateButtonsState();
//Synthetic comment -- @@ -427,68 +456,156 @@
}
}

    private void sortPackages() {
if (mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection()) {
sortByAPI();
} else {
sortBySource();
}
        updateButtonsState();
}

/**
* Recompute the tree by sorting all the packages by API.
*/
private void sortByAPI() {
        mCategories.clear();

        Set<Integer> apiSet = new HashSet<Integer>();
        for (PkgItem item : mPackages) {
            if (keepItem(item)) {
                apiSet.add(item.getApi());
            }
        }

        Integer[] apis = apiSet.toArray(new Integer[apiSet.size()]);
        Arrays.sort(apis, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

ImageFactory imgFactory = mUpdaterData.getImageFactory();

        for (Integer api : apis) {
            String name = api > 0 ? "API " + api.toString() : "Other";
            Object iconRef = imgFactory.getImageByName(
                    api > 0 ? "pkgcat_16.png" :"pkgcat_other_16.png");  //$NON-NLS-1$ //$NON-NLS-2$

            List<PkgItem> items = new ArrayList<PkgItem>();
            for (PkgItem item : mPackages) {
                if (item.getApi() == api) {
                    items.add(item);

                    if (api != -1) {
                        Package p = item.getPackage();
                        if (p instanceof PlatformPackage) {
                            String vn = ((PlatformPackage) p).getVersionName();
                            name = String.format("%1$s (Android %2$s)", name, vn);
                        }
                    }
                }
            }

PkgCategory cat = new PkgCategory(
                    name,
                    iconRef,
                    items.toArray(new PkgItem[items.size()]));
mCategories.add(cat);
}

        mTreeViewer.setInput(mCategories);

        // expand all items by default
        expandInitial(mCategories);
}

/**
//Synthetic comment -- @@ -522,34 +639,32 @@
for (SdkSource source : sources) {
Object key = source != null ? source : "Installed Packages";
Object iconRef = source != null ? source :
                        mUpdaterData.getImageFactory().getImageByName(
                                "pkg_installed_16.png"); //$NON-NLS-1$

            List<PkgItem> items = new ArrayList<PkgItem>();
for (PkgItem item : mPackages) {
if (item.getSource() == source) {
                    items.add(item);
}
}

            PkgCategory cat = new PkgCategory(
                    key,
                    iconRef,
                    items.toArray(new PkgItem[items.size()]));
mCategories.add(cat);
}

mTreeViewer.setInput(mCategories);

        // expand all items by default
        expandInitial(mCategories);
}

/**
* Decide whether to keep an item in the current tree based on user-choosen filter options.
*/
private boolean keepItem(PkgItem item) {
        if (!mCheckFilterDetails.getSelection()) {
if (item.isObsolete()) {
return false;
}
//Synthetic comment -- @@ -562,8 +677,8 @@
}

if (!mCheckFilterNew.getSelection()) {
            if (item.getState() == PkgState.NEW_AVAILABLE ||
                    item.getState() == PkgState.UPDATE_AVAILABLE) {
return false;
}
}
//Synthetic comment -- @@ -583,7 +698,7 @@
PkgCategory cat = (PkgCategory) pkg;
for (PkgItem item : cat.getItems()) {
if (item.getState() == PkgState.INSTALLED
                            || item.getState() == PkgState.UPDATE_AVAILABLE) {
expandInitial(pkg);
break;
}
//Synthetic comment -- @@ -643,7 +758,7 @@
private void updateButtonsState() {
boolean canInstall = false;

        if (mCheckFilterDetails.getSelection()) {
// In detail mode, we display archives so we can install if at
// least one archive is selected.

//Synthetic comment -- @@ -715,8 +830,7 @@
protected void onButtonInstall() {
ArrayList<Archive> archives = new ArrayList<Archive>();

        boolean showDetails = mCheckFilterDetails.getSelection();
        if (showDetails) {
// In detail mode, we display archives so we can install only the
// archives that are actually selected.

//Synthetic comment -- @@ -761,7 +875,7 @@

mUpdaterData.updateOrInstallAll_WithGUI(
archives,
                    showDetails /* includeObsoletes */);
} finally {
// loadPackages will also re-enable the UI
loadPackages();
//Synthetic comment -- @@ -872,7 +986,7 @@
PkgItem pkg = (PkgItem) element;

if (pkg.getState() == PkgState.INSTALLED ||
                            pkg.getState() == PkgState.UPDATE_AVAILABLE) {
return Integer.toString(pkg.getRevision());
}
}
//Synthetic comment -- @@ -885,12 +999,10 @@
switch(pkg.getState()) {
case INSTALLED:
return "Installed";
                    case UPDATE_AVAILABLE:
return "Update available";
                    case NEW_AVAILABLE:
return "Not installed. New revision " + Integer.toString(pkg.getRevision());
                    case LOCKED_NO_INSTALL:
                        return "Locked";
}
return pkg.getState().toString();

//Synthetic comment -- @@ -919,13 +1031,11 @@
} else if (mColumn == mColumnStatus && element instanceof PkgItem) {
switch(((PkgItem) element).getState()) {
case INSTALLED:
                        return imgFactory.getImageByName("pkg_installed_16.png");   //$NON-NLS-1$
                    case UPDATE_AVAILABLE:
                        return imgFactory.getImageByName("pkg_update_16.png");      //$NON-NLS-1$
                    case NEW_AVAILABLE:
                        return imgFactory.getImageByName("pkg_new_16.png");         //$NON-NLS-1$
                    case LOCKED_NO_INSTALL:
                        return imgFactory.getImageByName("broken_pkg_16.png");      //$NON-NLS-1$
}
}
}
//Synthetic comment -- @@ -936,7 +1046,7 @@

public Font getFont(Object element, int columnIndex) {
if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() != PkgState.INSTALLED) {
return mTreeFontItalic;
}
} else if (element instanceof Package) {
//Synthetic comment -- @@ -950,11 +1060,8 @@

public Color getBackground(Object element, int columnIndex) {
if (element instanceof PkgItem) {
                if (((PkgItem) element).getState() == PkgState.NEW_AVAILABLE) {
                    // Disabled. Current color scheme is unpretty.
                    // return mColorNew;
                } else if (((PkgItem) element).getState() == PkgState.UPDATE_AVAILABLE) {
                        return mColorUpdate;
}
}
return null;
//Synthetic comment -- @@ -974,7 +1081,7 @@
return ((ArrayList<?>) parentElement).toArray();

} else if (parentElement instanceof PkgCategory) {
                return ((PkgCategory) parentElement).getItems();

} else if (parentElement instanceof PkgItem) {
List<Package> pkgs = ((PkgItem) parentElement).getUpdatePkgs();
//Synthetic comment -- @@ -982,12 +1089,12 @@
return pkgs.toArray();
}

                if (mCheckFilterDetails.getSelection()) {
return ((PkgItem) parentElement).getArchives();
}

} else if (parentElement instanceof Package) {
                if (mCheckFilterDetails.getSelection()) {
return ((Package) parentElement).getArchives();
}

//Synthetic comment -- @@ -1014,12 +1121,12 @@
return !pkgs.isEmpty();
}

                if (mCheckFilterDetails.getSelection()) {
Archive[] archives = ((PkgItem) parentElement).getArchives();
return archives.length > 0;
}
} else if (parentElement instanceof Package) {
                if (mCheckFilterDetails.getSelection()) {
return ((Package) parentElement).getArchives().length > 0;
}
}
//Synthetic comment -- @@ -1042,34 +1149,64 @@
}

private static class PkgCategory {
        private final Object mKey;
        private final PkgItem[] mItems;
private final Object mIconRef;

        public PkgCategory(Object key, Object iconRef, PkgItem[] items) {
mKey = key;
mIconRef = iconRef;
            mItems = items;
}

public String getLabel() {
            return mKey.toString();
}

public Object getIconRef() {
return mIconRef;
}

        public PkgItem[] getItems() {
return mItems;
}
}

public enum PkgState {
        INSTALLED, UPDATE_AVAILABLE, NEW_AVAILABLE, LOCKED_NO_INSTALL
}

    public static class PkgItem {
private final Package mPkg;
private PkgState mState;
private List<Package> mUpdatePkgs;
//Synthetic comment -- @@ -1077,6 +1214,7 @@
public PkgItem(Package pkg, PkgState state) {
mPkg = pkg;
mState = state;
}

public boolean isObsolete() {
//Synthetic comment -- @@ -1098,7 +1236,7 @@
mUpdatePkgs = new ArrayList<Package>();
}
mUpdatePkgs.add(pkg);
                mState = PkgState.UPDATE_AVAILABLE;
return true;
}

//Synthetic comment -- @@ -1126,7 +1264,7 @@
}

public SdkSource getSource() {
            if (mState == PkgState.NEW_AVAILABLE) {
return mPkg.getParentSource();
} else {
return null;
//Synthetic comment -- @@ -1146,6 +1284,10 @@
public Archive[] getArchives() {
return mPkg.getArchives();
}
}










//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/ProgressView.java
//Synthetic comment -- index e552da8..b923a17 100755

//Synthetic comment -- @@ -51,21 +51,22 @@

// UI fields
private final Label mLabel;
private final ProgressBar mProgressBar;
    private final Control mStateButton;
private final StringBuffer mResultText = new StringBuffer();


/**
     * TODO javadoc
*/
    public ProgressView(Label label, ProgressBar progressBar, Control stateButton) {
mLabel = label;
mProgressBar = progressBar;
mProgressBar.setEnabled(false);

        mStateButton = stateButton;
        mStateButton.addListener(SWT.Selection, new Listener() {
public void handleEvent(Event event) {
if (mState == State.ACTIVE) {
changeState(State.STOP_PENDING);
//Synthetic comment -- @@ -125,9 +126,9 @@
mState = state;
}

        syncExec(mStateButton, new Runnable() {
public void run() {
                mStateButton.setEnabled(mState == State.ACTIVE);
}
});








