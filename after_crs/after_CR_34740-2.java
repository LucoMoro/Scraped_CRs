/*SDK Manager dialog to enable 3rd party addons.

=> Not ready for review.

- Change AddonUser dialog: transform current dialog in
  2 pages. First one is for user to add custom addon site
  URLs (like the dialog was doing before, unchanged.)
- Second page is to select which official addon sites
  should be enabled.

Change-Id:Icc8e392d90550e53f1c76dd7aefb31669219973b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 7933602..14224f2 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -33,7 +35,10 @@
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -41,6 +46,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -68,6 +74,9 @@
private String mFetchError;
private final String mUiName;

    private static final Properties sDisabledSourceUrls = new Properties();
    private static final String     SRC_FILENAME = "sites-settings.cfg"; //$NON-NLS-1$

/**
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
//Synthetic comment -- @@ -183,7 +192,8 @@

/**
* Returns the list of known packages found by the last call to load().
     * This is null when the source hasn't been loaded yet -- caller should
     * then call {@link #load(ITaskMonitor, boolean)} to load the packages.
*/
public Package[] getPackages() {
return mPackages;
//Synthetic comment -- @@ -208,6 +218,107 @@
}

/**
     * Indicates if the source is enabled.
     * <p/>
     * A 3rd-party add-on source can be disabled by the user to prevent from loading it.
     * This loads the persistent state from a settings file when first called.
     *
     * @return True if the source is enabled (default is true).
     */
    public boolean isEnabled() {
        synchronized (sDisabledSourceUrls) {
            if (sDisabledSourceUrls.isEmpty()) {
                // Load state from persistent file

                FileInputStream fis = null;
                try {
                    String folder = AndroidLocation.getFolder();
                    File f = new File(folder, SRC_FILENAME);
                    if (f.exists()) {
                        fis = new FileInputStream(f);

                        sDisabledSourceUrls.load(fis);
                    }
                } catch (IOException ignore) {
                    // nop
                } catch (AndroidLocationException ignore) {
                    // nop
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ignore) {}
                    }
                }

                if (sDisabledSourceUrls.isEmpty()) {
                    // Nothing was loaded. Initialize the storage with a version
                    // identified. This isn't currently checked back, but we might
                    // want it later if we decide to change the way this works.
                    // The version key is choosen on purpose to not match any valid URL.
                    sDisabledSourceUrls.setProperty("@version", "1"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            // A URL is enabled if it's not in the disabled list.
            return sDisabledSourceUrls.getProperty(mUrl) == null;
        }
    }

    /**
     * Changes whether the source is marked as enabled.
     * <p/>
     * When setting enable to false, the current package list is purged
     * and {@code load} will not do anything.
     * This also persistent the change by updating a settings file.
     *
     * @param enabled True for the source to be enabled (can be loaded), false otherwise.
     */
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            clearPackages();
        }
        // Comparing using isEnabled() has the voluntary side-effect of also
        // loading the map from the persistent file the first time.
        if (enabled != isEnabled()) {
            synchronized (sDisabledSourceUrls) {
                // Change the map
                if (enabled) {
                    sDisabledSourceUrls.remove(mUrl);
                } else {
                    // The "disabled" value is not being checked when reloading the map.
                    // We might want to do something with it later if a URL can have
                    // more attributes than just disabled.
                    sDisabledSourceUrls.setProperty(mUrl, "disabled"); //$NON-NLS-1$
                }

                // Persist it to the file
                FileOutputStream fos = null;
                try {
                    String folder = AndroidLocation.getFolder();
                    File f = new File(folder, SRC_FILENAME);

                    fos = new FileOutputStream(f);

                    sDisabledSourceUrls.store(fos,
                            "## Disabled Sources for Android SDK Manager");  //$NON-NLS-1$

                } catch (AndroidLocationException ignore) {
                    // nop
                } catch (IOException ignore) {
                    // nop
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ignore) {}
                    }
                }
            }
        }
    }

    /**
* Returns the short description of the source, if not null.
* Otherwise returns the default Object toString result.
* <p/>
//Synthetic comment -- @@ -258,13 +369,26 @@
}

/**
     * Tries to fetch the repository index for the given URL and updates the package list.
     * When a source is disabled, this create an empty non-null package list.
     * <p/>
     * Callers can get the package list using {@link #getPackages()} after this. It will be
     * null in case of error, in which case {@link #getFetchError()} can be used to an
     * error message.
*/
public void load(ITaskMonitor monitor, boolean forceHttp) {

        setDefaultDescription();
monitor.setProgressMax(7);

        if (!isEnabled()) {
            setPackages(new Package[0]);
            mDescription += "\nSource is disabled.";
            mFetchError = "Source is disabled."; // TODO change

            monitor.incProgress(7);
            return;
        }

String url = mUrl;
if (forceHttp) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index 22311a7..dc33966 100755

//Synthetic comment -- @@ -44,11 +44,19 @@
private final EnumMap<SdkSourceCategory, ArrayList<SdkSource>> mSources =
new EnumMap<SdkSourceCategory, ArrayList<SdkSource>>(SdkSourceCategory.class);

    private ArrayList<Runnable> mChangeListeners; // lazily initialized


public SdkSources() {
}

/**
* Adds a new source to the Sources list.
     * <p/>
     * Implementation detail: {@link SdkSources} doesn't invoke {@link #notifyChangeListeners()}
     * directly. Callers who use {@code add()} are responsible for notifying the listeners once
     * they are done modifying the sources list. The intent is to notify the listeners only once
     * at the end, not for every single addition.
*/
public void add(SdkSourceCategory category, SdkSource source) {
synchronized (mSources) {
//Synthetic comment -- @@ -64,6 +72,9 @@

/**
* Removes a source from the Sources list.
     * <p/>
     * Callers who remove entries are responsible for notifying the listeners using
     * {@link #notifyChangeListeners()} once they are done modifying the sources list.
*/
public void remove(SdkSource source) {
synchronized (mSources) {
//Synthetic comment -- @@ -85,6 +96,9 @@

/**
* Removes all the sources in the given category.
     * <p/>
     * Callers who remove entries are responsible for notifying the listeners using
     * {@link #notifyChangeListeners()} once they are done modifying the sources list.
*/
public void removeAll(SdkSourceCategory category) {
synchronized (mSources) {
//Synthetic comment -- @@ -243,53 +257,63 @@
/**
* Loads all user sources. This <em>replaces</em> all existing user sources
* by the ones from the property file.
     * <p/>
     * This calls {@link #notifyChangeListeners()} at the end of the operation.
*/
public void loadUserAddons(ISdkLog log) {
        // Implementation detail: synchronize on the sources list to make sure that
        // a- the source list doesn't change while we load/save it, and most important
        // b- to make sure it's not being saved while loaded or the reverse.
        // In most cases we do these operation from the UI thread so it's not really
        // that necessary. This is more a protection in case of someone calls this
        // from a worker thread by mistake.
        synchronized (mSources) {
            // Remove all existing user sources
            removeAll(SdkSourceCategory.USER_ADDONS);

            // Load new user sources from property file
            FileInputStream fis = null;
            try {
                String folder = AndroidLocation.getFolder();
                File f = new File(folder, SRC_FILENAME);
                if (f.exists()) {
                    fis = new FileInputStream(f);

                    Properties props = new Properties();
                    props.load(fis);

                    int count = Integer.parseInt(props.getProperty(KEY_COUNT, "0"));

                    for (int i = 0; i < count; i++) {
                        String url = props.getProperty(String.format("%s%02d", KEY_SRC, i));  //$NON-NLS-1$
                        if (url != null) {
                            SdkSource s = new SdkAddonSource(url, null/*uiName*/);
                            if (!hasSourceUrl(s)) {
                                add(SdkSourceCategory.USER_ADDONS, s);
                            }
}
}
}

            } catch (NumberFormatException e) {
                log.error(e, null);

            } catch (AndroidLocationException e) {
                log.error(e, null);

            } catch (IOException e) {
                log.error(e, null);

            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
}
}
}
        notifyChangeListeners();
}

/**
//Synthetic comment -- @@ -297,38 +321,93 @@
* @param log Logger. Cannot be null.
*/
public void saveUserAddons(ISdkLog log) {
        // See the implementation detail note in loadUserAddons() about the synchronization.
        synchronized (mSources) {
            FileOutputStream fos = null;
            try {
                String folder = AndroidLocation.getFolder();
                File f = new File(folder, SRC_FILENAME);

                fos = new FileOutputStream(f);

                Properties props = new Properties();

                int count = 0;
                for (SdkSource s : getSources(SdkSourceCategory.USER_ADDONS)) {
                    props.setProperty(String.format("%s%02d", KEY_SRC, count), //$NON-NLS-1$
                                      s.getUrl());
                    count++;
                }
                props.setProperty(KEY_COUNT, Integer.toString(count));

                props.store( fos, "## User Sources for Android SDK Manager");  //$NON-NLS-1$

            } catch (AndroidLocationException e) {
                log.error(e, null);

            } catch (IOException e) {
                log.error(e, null);

            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
}
}
}
    }

    /**
     * Adds a listener that will be notified when the sources list has changed.
     *
     * @param changeListener A non-null listener to add. Ignored if already present.
     * @see SdkSources#notifyChangeListeners()
     */
    public void addChangeListener(Runnable changeListener) {
        assert changeListener != null;
        if (mChangeListeners == null) {
            mChangeListeners = new ArrayList<Runnable>();
        }
        synchronized (mChangeListeners) {
            if (changeListener != null && !mChangeListeners.contains(changeListener)) {
                mChangeListeners.add(changeListener);
            }
        }
    }

    /**
     * Removes a listener from the list of listeners to notify when the sources change.
     *
     * @param changeListener A listener to remove. Ignored if not previously added.
     */
    public void removeChangeListener(Runnable changeListener) {
        if (mChangeListeners != null && changeListener != null) {
            synchronized (mChangeListeners) {
                mChangeListeners.remove(changeListener);
            }
        }
    }

    /**
     * Invoke all the registered change listeners, if any.
     * <p/>
     * This <em>may</em> be called from a worker thread, in which case the runnable
     * should take care of only updating UI from a main thread.
     */
    public void notifyChangeListeners() {
        if (mChangeListeners == null) {
            return;
        }
        synchronized (mChangeListeners) {
            for (Runnable runnable : mChangeListeners) {
                try {
                    runnable.run();
                } catch (Throwable ignore) {
                    assert ignore == null : "A SdkSource.ChangeListener failed with an exception.";
                }
            }
        }
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 4f39b71..dd51a59 100755

//Synthetic comment -- @@ -328,7 +328,8 @@
new SdkRepoSource(baseUrl,
SdkSourceCategory.ANDROID_REPO.getUiName()));

        // Load user sources (this will also notify change listeners but this operation is
        // done early enough that there shouldn't be any anyway.)
sources.loadUserAddons(getSdkLog());
}

//Synthetic comment -- @@ -1063,6 +1064,8 @@
}
}

            mSources.notifyChangeListeners();

mStateFetchRemoteAddonsList = 1;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java
//Synthetic comment -- index 28a065a..02b3657 100755

//Synthetic comment -- @@ -23,9 +23,14 @@
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
//Synthetic comment -- @@ -46,110 +51,220 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.Arrays;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private final SdkSources mSources;
    private Table mUserTable;
    private TableViewer mUserTableViewer;
    private CheckboxTableViewer mSitesTableViewer;
    private Button mUserButtonNew;
    private Button mUserButtonDelete;
    private Button mUserButtonEdit;
    private Runnable mSourcesChangeListener;

/**
* Create the dialog.
*
* @param parent The parent's shell
     * @wbp.parser.entryPoint
*/
public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
super(parent, updaterData, "Add-on Sites");
        mSources = updaterData.getSources();
        assert mSources != null;
}

/**
* Create contents of the dialog.
     * @wbp.parser.entryPoint
*/
@SuppressWarnings("unused")
@Override
protected void createContents() {
super.createContents();
Shell shell = getShell();
        shell.setMinimumSize(new Point(300, 300));
        shell.setSize(600, 300);

        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

        TabItem userTabItem = new TabItem(tabFolder, SWT.NONE);
        userTabItem.setText("User Defined Sites");

        TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);
        sitesTabItem.setText("Official Third-Party Sites"); // TODO find better name

        // placeholder for aligning close button
Label label = new Label(shell, SWT.NONE);
        GridDataBuilder.create(label).hFill().hGrab();
        createCloseButton();

        // --- tab 1 : user defined addons

        Composite root = new Composite(tabFolder, SWT.NONE);
        userTabItem.setControl(root);
        GridLayoutBuilder.create(root).columns(2);

        label = new Label(root, SWT.NONE);
GridDataBuilder.create(label).hLeft().vCenter().hSpan(2);
label.setText(
            "This lets you manage a list of user-contributed external add-on sites URLs.\n" +
"\n" +
            "Add-on sites can provide new add-ons and extra packages.\n" +
            "They cannot provide standard Android platforms, system images or docs.\n" +
"Adding a URL here will not allow you to clone an official Android repository."
);

        mUserTableViewer = new TableViewer(root, SWT.BORDER | SWT.FULL_SELECTION);
        mUserTableViewer.setContentProvider(new SourcesContentProvider());

        mUserTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
@Override
public void selectionChanged(SelectionChangedEvent event) {
                on_UserTableViewer_selectionChanged(event);
}
});
        mUserTable = mUserTableViewer.getTable();
        mUserTable.setLinesVisible(true);
        mUserTable.addMouseListener(new MouseAdapter() {
@Override
            public void mouseUp(MouseEvent event) {
                on_UserTable_mouseUp(event);
}
});
        GridDataBuilder.create(mUserTable).fill().grab().vSpan(5);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(mUserTableViewer, SWT.NONE);
        TableColumn userColumnUrl = tableViewerColumn.getColumn();
        userColumnUrl.setWidth(100);

        // Implementation detail: set the label provider on the table viewer *after* associating
        // a column. This will set the label provider on the column for us.
        mUserTableViewer.setLabelProvider(new LabelProvider());


        mUserButtonNew = new Button(root, SWT.NONE);
        mUserButtonNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                userNewOrEdit(false /*isEdit*/);
}
});
        GridDataBuilder.create(mUserButtonNew).hFill().vCenter();
        mUserButtonNew.setText("New...");

        mUserButtonEdit = new Button(root, SWT.NONE);
        mUserButtonEdit.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                userNewOrEdit(true /*isEdit*/);
}
});
        GridDataBuilder.create(mUserButtonEdit).hFill().vCenter();
        mUserButtonEdit.setText("Edit...");

        mUserButtonDelete = new Button(root, SWT.NONE);
        mUserButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                on_UserButtonDelete_widgetSelected(e);
}
});
        GridDataBuilder.create(mUserButtonDelete).hFill().vCenter();
        mUserButtonDelete.setText("Delete...");
        new Label(root, SWT.NONE);

        adjustColumnsWidth(mUserTable, userColumnUrl);

        // --- tab 2 : official 3rd party addon sites

        root = new Composite(tabFolder, SWT.NONE);
        sitesTabItem.setControl(root);
        GridLayoutBuilder.create(root).columns(1);

        label = new Label(root, SWT.NONE);
        GridDataBuilder.create(label).hLeft().vCenter();
        label.setText(
            "This lets select which official 3rd-party sites you want to load.\n" +
            "\n" +
            "These sites are managed by non-Android vendors to provide add-ons and extra packages.\n" +
            "They are by default all enabled. When you disable one, the SDK Manager will not check the site for new packages."
        );

        mSitesTableViewer = CheckboxTableViewer.newCheckList(root, SWT.BORDER | SWT.FULL_SELECTION);
        mSitesTableViewer.setContentProvider(new SourcesContentProvider());

        Table sitesTable = mSitesTableViewer.getTable();
        sitesTable.setToolTipText("Enable 3rd-Party Site");
        sitesTable.setLinesVisible(true);
        sitesTable.setHeaderVisible(true);
        GridDataBuilder.create(sitesTable).fill().grab();

        TableViewerColumn columnViewer = new TableViewerColumn(mSitesTableViewer, SWT.NONE);
        TableColumn column = columnViewer.getColumn();
        column.setResizable(true);
        column.setWidth(150);
        column.setText("Name");
        columnViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof SdkSource) {
                    String name = ((SdkSource) element).getUiName();
                    if (name != null) {
                        return name;
                    }
                    return ((SdkSource) element).getShortDescription();
                }
                return super.getText(element);
            }
        });

        columnViewer = new TableViewerColumn(mSitesTableViewer, SWT.NONE);
        column = columnViewer.getColumn();
        column.setResizable(true);
        column.setWidth(400);
        column.setText("URL");
        columnViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof SdkSource) {
                    return ((SdkSource) element).getUrl();
                }
                return super.getText(element);
            }
        });

        mSitesTableViewer.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                on_SitesTableViewer_checkStateChanged(event);
            }
        });
    }

    @Override
    protected void close() {
        if (mSources != null && mSourcesChangeListener != null) {
            mSources.removeChangeListener(mSourcesChangeListener);
        }
        super.close();
}

/**
//Synthetic comment -- @@ -166,12 +281,11 @@
});
}

    private void userNewOrEdit(final boolean isEdit) {
        final SdkSource[] knownSources = mSources.getAllSources();
String title = isEdit ? "Edit Add-on Site URL" : "Add Add-on Site URL";
String msg = "Please enter the URL of the addon.xml:";
        IStructuredSelection sel = (IStructuredSelection) mUserTableViewer.getSelection();
final String initialValue = !isEdit || sel.isEmpty() ? null :
sel.getFirstElement().toString();

//Synthetic comment -- @@ -228,9 +342,9 @@
if (isEdit && initialValue != null) {
// Remove the old value before we add the new one, which is we just
// asserted will be different.
                    for (SdkSource source : mSources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (initialValue.equals(source.getUrl())) {
                            mSources.remove(source);
break;
}
}
//Synthetic comment -- @@ -239,21 +353,22 @@

// create the source, store it and update the list
SdkAddonSource newSource = new SdkAddonSource(url, null/*uiName*/);
                mSources.add(
SdkSourceCategory.USER_ADDONS,
newSource);
setReturnValue(true);
                // notify sources change listeners. This will invoke our own loadUserUrlsList().
                mSources.notifyChangeListeners();

// select the new source
IStructuredSelection newSel = new StructuredSelection(newSource);
                mUserTableViewer.setSelection(newSel, true /*reveal*/);
}
}
}

    private void on_UserButtonDelete_widgetSelected(SelectionEvent e) {
        IStructuredSelection sel = (IStructuredSelection) mUserTableViewer.getSelection();
String selectedUrl = sel.isEmpty() ? null : sel.getFirstElement().toString();

if (selectedUrl == null) {
//Synthetic comment -- @@ -265,58 +380,116 @@
mb.setText("Delete add-on site");
mb.setMessage(String.format("Do you want to delete the URL %1$s?", selectedUrl));
if (mb.open() == SWT.YES) {
            for (SdkSource source : mSources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (selectedUrl.equals(source.getUrl())) {
                    mSources.remove(source);
setReturnValue(true);
                    mSources.notifyChangeListeners();
                    break;
}
}
}
}

    private void on_UserTable_mouseUp(MouseEvent event) {
        Point p = new Point(event.x, event.y);
        if (mUserTable.getItem(p) == null) {
            mUserTable.deselectAll();
            on_UserTableViewer_selectionChanged(null /*event*/);
}
}

    private void on_UserTableViewer_selectionChanged(SelectionChangedEvent event) {
        ISelection sel = mUserTableViewer.getSelection();
        mUserButtonDelete.setEnabled(!sel.isEmpty());
        mUserButtonEdit.setEnabled(!sel.isEmpty());
    }

    private void on_SitesTableViewer_checkStateChanged(CheckStateChangedEvent event) {
        Object element = event.getElement();
        if (element instanceof SdkSource) {
            SdkSource source = (SdkSource) element;
            boolean isChecked = event.getChecked();
            if (source.isEnabled() != isChecked) {
                setReturnValue(true);
                source.setEnabled(isChecked);
                mSources.notifyChangeListeners();
            }
        }
}

@Override
protected void postCreate() {
        // A runnable to initially load and then update the user urls & sites lists.
        final Runnable updateInUiThread = new Runnable() {
            @Override
            public void run() {
                loadUserUrlsList();
                loadSiteUrlsList();
            }
        };

        // A listener that runs when the sources have changed.
        // This is most likely called on a worker thread.
        mSourcesChangeListener = new Runnable() {
            @Override
            public void run() {
                Shell shell = getShell();
                if (shell != null) {
                    Display display = shell.getDisplay();
                    if (display != null) {
                        display.syncExec(updateInUiThread);
                    }
                }
            }
        };

        mSources.addChangeListener(mSourcesChangeListener);

// initialize the list
        updateInUiThread.run();
}

    private void loadUserUrlsList() {
        SdkSource[] knownSources = mSources.getSources(SdkSourceCategory.USER_ADDONS);
        Arrays.sort(knownSources);

        ISelection oldSelection = mUserTableViewer.getSelection();

        mUserTableViewer.setInput(knownSources);
        mUserTableViewer.refresh();
        // initialize buttons' state that depend on the list
        on_UserTableViewer_selectionChanged(null /*event*/);

        if (oldSelection != null && !oldSelection.isEmpty()) {
            mUserTableViewer.setSelection(oldSelection, true /*reveal*/);
}
}

    private void loadSiteUrlsList() {
        SdkSource[] knownSources = mSources.getSources(SdkSourceCategory.ADDONS_3RD_PARTY);
        Arrays.sort(knownSources);

        ISelection oldSelection = mSitesTableViewer.getSelection();

        mSitesTableViewer.setInput(knownSources);
        mSitesTableViewer.refresh();

        if (oldSelection != null && !oldSelection.isEmpty()) {
            mSitesTableViewer.setSelection(oldSelection, true /*reveal*/);
        }

        // Check the sources which are currently enabled.
        ArrayList<SdkSource> disabled = new ArrayList<SdkSource>(knownSources.length);
        for (SdkSource source : knownSources) {
            if (source.isEnabled()) {
                disabled.add(source);
            }
        }
        mSitesTableViewer.setCheckedElements(disabled.toArray());
    }


private static class SourcesContentProvider implements IStructuredContentProvider {

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 3d7cdb0..8bbeb65 100755

//Synthetic comment -- @@ -302,9 +302,7 @@
}

public boolean updateSourcePackages(SdkSource source, Package[] newPackages) {
            mVisitedSources.add(source);
if (source == null) {
return processLocals(this, newPackages);
} else {
//Synthetic comment -- @@ -759,6 +757,29 @@
* {@link UpdateOp} describing the Sort-by-Source operation.
*/
private class UpdateOpSource extends UpdateOp {

        @Override
        public boolean updateSourcePackages(SdkSource source, Package[] newPackages) {
            // When displaying the repo by source, we want to create all the
            // categories so that they can appear on the UI even if empty.
            if (source != null) {
                List<PkgCategory> cats = getCategories();
                Object catKey = source;
                PkgCategory cat = findCurrentCategory(cats, catKey);

                if (cat == null) {
                    // This is a new category. Create it and add it to the list.
                    cat = createCategory(catKey);
                    synchronized (cats) {
                        cats.add(cat);
                    }
                    sortCategoryList();
                }
            }

            return super.updateSourcePackages(source, newPackages);
        }

@Override
public Object getCategoryKey(Package pkg) {
// Sort by source







