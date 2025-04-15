/*SdkManager2: implement menu bar.

Change-Id:Id578a9e84cb091af5f5a60002173843ef61aa4f8*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 749f927..cd6006e 100755

//Synthetic comment -- @@ -56,9 +56,10 @@
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
//Synthetic comment -- @@ -74,6 +75,7 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
* Page that displays both locally installed packages as well as all known
//Synthetic comment -- @@ -86,16 +88,40 @@
private static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
private static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$
private static final String ICON_SORT_BY_SOURCE = "source_icon16.png";      //$NON-NLS-1$
    private static final String ICON_SORT_BY_API    = "platform_pkg_16.png";    //$NON-NLS-1$
private static final String ICON_PKG_NEW        = "pkg_new_16.png";         //$NON-NLS-1$
private static final String ICON_PKG_UPDATE     = "pkg_update_16.png";      //$NON-NLS-1$
private static final String ICON_PKG_INSTALLED  = "pkg_installed_16.png";   //$NON-NLS-1$

    enum MenuAction {
        RELOAD(false),
        SHOW_ADDON_SITES(false),
        TOGGLE_SHOW_ARCHIVES(true),
        TOGGLE_SHOW_INSTALLED_PKG(true),
        TOGGLE_SHOW_OBSOLETE_PKG(true),
        TOGGLE_SHOW_UPDATE_NEW_PKG(true),
        SORT_API_LEVEL(true),
        SORT_SOURCE(true)
        ;

        private final boolean mIsChecked;

        MenuAction(boolean isChecked) {
            mIsChecked = isChecked;
        }

        public boolean isChecked() {
            return mIsChecked;
        }
    };

    private final Map<MenuAction, MenuItem> mMenuActions = new HashMap<MenuAction, MenuItem>();

private final List<PkgItem> mPackages = new ArrayList<PkgItem>();
private final List<PkgCategory> mCategories = new ArrayList<PkgCategory>();
private final UpdaterData mUpdaterData;

    private boolean mDisplayArchives = false;

private Text mTextSdkOsPath;
private Button mCheckSortSource;
//Synthetic comment -- @@ -116,7 +142,7 @@
private TreeViewerColumn mColumnStatus;
private Color mColorUpdate;
private Font mTreeFontItalic;
    private TreeColumn mTreeColumnName;

public PackagesPage(Composite parent, UpdaterData updaterData) {
super(parent, SWT.NONE);
//Synthetic comment -- @@ -134,7 +160,7 @@
}
}

    private void createContents(Composite parent) {
GridLayout gridLayout = new GridLayout(2, false);
gridLayout.marginWidth = 0;
gridLayout.marginHeight = 0;
//Synthetic comment -- @@ -169,11 +195,12 @@
mTree.setHeaderVisible(true);
mTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // column name icon is set in sortPackages() depending on the current filter type
        // (e.g. API level or source)
mColumnName = new TreeViewerColumn(mTreeViewer, SWT.NONE);
        mTreeColumnName = mColumnName.getColumn();
        mTreeColumnName.setWidth(340);
        mTreeColumnName.setText("Name");

mColumnApi = new TreeViewerColumn(mTreeViewer, SWT.NONE);
TreeColumn treeColumn2 = mColumnApi.getColumn();
//Synthetic comment -- @@ -197,7 +224,7 @@

mGroupOptions = new Composite(mGroupPackages, SWT.NONE);
mGroupOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_GroupOptions = new GridLayout(6, false);
gl_GroupOptions.marginWidth = 0;
gl_GroupOptions.marginHeight = 0;
mGroupOptions.setLayout(gl_GroupOptions);
//Synthetic comment -- @@ -213,7 +240,6 @@
sortPackages(true /*updateButtons*/);
}
});
mCheckFilterNew.setSelection(true);
mCheckFilterNew.setText("Updates/New");

//Synthetic comment -- @@ -225,7 +251,6 @@
sortPackages(true /*updateButtons*/);
}
});
mCheckFilterInstalled.setSelection(true);
mCheckFilterInstalled.setText("Installed");

//Synthetic comment -- @@ -240,17 +265,6 @@
mCheckFilterObsolete.setSelection(false);
mCheckFilterObsolete.setText("Obsolete");

Label placeholder2 = new Label(mGroupOptions, SWT.NONE);
placeholder2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

//Synthetic comment -- @@ -278,7 +292,6 @@
expandInitial(mCategories);
}
});
mCheckSortApi.setText("API level");
mCheckSortApi.setSelection(true);

//Synthetic comment -- @@ -292,19 +305,8 @@
expandInitial(mCategories);
}
});
mCheckSortSource.setText("Source");

new Label(mGroupOptions, SWT.NONE);
new Label(mGroupOptions, SWT.NONE);

//Synthetic comment -- @@ -335,6 +337,125 @@
// Hide everything down-below from SWT designer
//$hide>>$


    // --- menu interactions ---

    public void registerMenuAction(final MenuAction action, MenuItem item) {
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = null;

                switch (action) {
                case RELOAD:
                    loadPackages();
                    break;
                case SHOW_ADDON_SITES:
                    AddonSitesDialog d = new AddonSitesDialog(getShell(), mUpdaterData);
                    if (d.open()) {
                        loadPackages();
                    }
                    break;
                case TOGGLE_SHOW_ARCHIVES:
                    mDisplayArchives = !mDisplayArchives;
                    sortPackages(true /*updateButtons*/);
                    break;
                case TOGGLE_SHOW_INSTALLED_PKG:
                    button = mCheckFilterInstalled;
                    break;
                case TOGGLE_SHOW_OBSOLETE_PKG:
                    button = mCheckFilterObsolete;
                    break;
                case TOGGLE_SHOW_UPDATE_NEW_PKG:
                    button = mCheckFilterNew;
                    break;
                case SORT_API_LEVEL:
                    button = mCheckSortApi;
                    break;
                case SORT_SOURCE:
                    button = mCheckSortSource;
                    break;
                }

                if (button != null && !button.isDisposed()) {
                    // Toggle this button (radio or checkbox)

                    boolean value = button.getSelection();

                    // SWT doesn't automatically switch radio buttons when using the
                    // Widget#setSelection method, so we'll do it here manually.
                    if (!value && button != null && (button.getStyle() & SWT.RADIO) != 0) {
                        // we'll be selecting this radio button, so deselect all ther other ones
                        // in the parent group.
                        for (Control child : button.getParent().getChildren()) {
                            if (child instanceof Button &&
                                    child != button &&
                                    (child.getStyle() & SWT.RADIO) != 0) {
                                ((Button) child).setSelection(value);
                            }
                        }
                    }

                    assert button != null; // used to please the Eclipse static verifier
                    button.setSelection(!value);

                    // SWT doesn't actually invoke the listeners when using Widget#setSelection
                    // so let's run the actual action.
                    button.notifyListeners(SWT.Selection, new Event());
                }

                updateMenuCheckmarks();
            }
        });

        mMenuActions.put(action, item);
    }

    // --- internal methods ---

    private void updateMenuCheckmarks() {

        for (Entry<MenuAction, MenuItem> entry : mMenuActions.entrySet()) {
            MenuAction action = entry.getKey();
            MenuItem item = entry.getValue();

            if (!action.isChecked()) {
                continue;
            }

            boolean value = false;
            Button button = null;

            switch (action) {
            case TOGGLE_SHOW_ARCHIVES:
                value = mDisplayArchives;
                break;
            case TOGGLE_SHOW_INSTALLED_PKG:
                button = mCheckFilterInstalled;
                break;
            case TOGGLE_SHOW_OBSOLETE_PKG:
                button = mCheckFilterObsolete;
                break;
            case TOGGLE_SHOW_UPDATE_NEW_PKG:
                button = mCheckFilterNew;
                break;
            case SORT_API_LEVEL:
                button = mCheckSortApi;
                break;
            case SORT_SOURCE:
                button = mCheckSortSource;
                break;
            }

            if (button != null && !button.isDisposed()) {
                value = button.getSelection();
            }

            item.setSelection(value);
        }

    }

private void postCreate() {
if (mUpdaterData != null) {
mTextSdkOsPath.setText(mUpdaterData.getOsSdkRoot());
//Synthetic comment -- @@ -442,6 +563,7 @@
} finally {
enableUi(mGroupPackages, true);
updateButtonsState();
            updateMenuCheckmarks();
}
}

//Synthetic comment -- @@ -458,12 +580,13 @@

private void sortPackages(boolean updateButtons) {
if (mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection()) {
            sortByApiLevel();
} else {
sortBySource();
}
if (updateButtons) {
updateButtonsState();
            updateMenuCheckmarks();
}
}

//Synthetic comment -- @@ -472,10 +595,14 @@
* This does an update in-place of the mCategories list so that the table
* can preserve its state (checked / expanded / selected) properly.
*/
    private void sortByApiLevel() {

ImageFactory imgFactory = mUpdaterData.getImageFactory();

        if (!mTreeColumnName.isDisposed()) {
            mTreeColumnName.setImage(getImage(ICON_SORT_BY_API));
        }

// keep a map of the initial state so that we can detect which items or categories are
// no longer being used, so that we can removed them at the end of the in-place update.
final Map<Integer, Pair<PkgCategory, HashSet<PkgItem>> > unusedItemsMap =
//Synthetic comment -- @@ -612,6 +739,11 @@
* Recompute the tree by sorting all packages by source.
*/
private void sortBySource() {

        if (!mTreeColumnName.isDisposed()) {
            mTreeColumnName.setImage(getImage(ICON_SORT_BY_SOURCE));
        }

mCategories.clear();

Set<SdkSource> sourceSet = new HashSet<SdkSource>();
//Synthetic comment -- @@ -816,18 +948,7 @@
mButtonDelete.setEnabled(canDelete);
}

    private void onButtonInstall() {
ArrayList<Archive> archives = new ArrayList<Archive>();

if (mDisplayArchives) {
//Synthetic comment -- @@ -883,7 +1004,7 @@
}
}

    private void onButtonDelete() {
// Find selected local packages to be delete
Object[] checked = mTreeViewer.getCheckedElements();
if (checked == null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 6d29dc0..39c5796 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.menubar.MenuBarEnhancer;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
//Synthetic comment -- @@ -100,9 +101,9 @@
}

createShell();
preCreateContent();
createContents();
        createMenuBar();
mShell.open();
mShell.layout();

//Synthetic comment -- @@ -187,30 +188,37 @@
Menu menuPkgs = new Menu(menuBarPackages);
menuBarPackages.setMenu(menuPkgs);

        MenuItem showUpdatesNew = new MenuItem(menuPkgs, SWT.CHECK);
        showUpdatesNew.setText("Show Updates/New Packages");
        mPkgPage.registerMenuAction(MenuAction.TOGGLE_SHOW_UPDATE_NEW_PKG, showUpdatesNew);

        MenuItem showInstalled = new MenuItem(menuPkgs, SWT.CHECK);
        showInstalled.setText("Show Installed Packages");
        mPkgPage.registerMenuAction(MenuAction.TOGGLE_SHOW_INSTALLED_PKG, showInstalled);

        MenuItem showObsoletePackages = new MenuItem(menuPkgs, SWT.CHECK);
        showObsoletePackages.setText("Show Obsolete Packages");
        mPkgPage.registerMenuAction(MenuAction.TOGGLE_SHOW_OBSOLETE_PKG, showObsoletePackages);

        MenuItem showArchives = new MenuItem(menuPkgs, SWT.CHECK);
        showArchives.setText("Show Archives");
        mPkgPage.registerMenuAction(MenuAction.TOGGLE_SHOW_ARCHIVES, showArchives);

new MenuItem(menuPkgs, SWT.SEPARATOR);

        MenuItem sortByApi = new MenuItem(menuPkgs, SWT.RADIO);
sortByApi.setText("Sort by API Level");
        mPkgPage.registerMenuAction(MenuAction.SORT_API_LEVEL, sortByApi);

        MenuItem sortBySource = new MenuItem(menuPkgs, SWT.RADIO);
sortBySource.setText("Sort by Source");
        mPkgPage.registerMenuAction(MenuAction.SORT_SOURCE, sortBySource);

new MenuItem(menuPkgs, SWT.SEPARATOR);

MenuItem reload = new MenuItem(menuPkgs, SWT.NONE);
reload.setText("Reload");
        mPkgPage.registerMenuAction(MenuAction.RELOAD, reload);

MenuItem menuBarTools = new MenuItem(menuBar, SWT.CASCADE);
menuBarTools.setText("Tools");
//Synthetic comment -- @@ -223,6 +231,7 @@

MenuItem manageSources = new MenuItem(menuTools, SWT.NONE);
manageSources.setText("Manage Sources...");
        mPkgPage.registerMenuAction(MenuAction.SHOW_ADDON_SITES, manageSources);

MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {







