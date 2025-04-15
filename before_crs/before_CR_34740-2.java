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
import com.android.sdklib.internal.repository.UrlOpener.CanceledByUserException;
import com.android.sdklib.repository.RepoConstants;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -33,7 +35,10 @@
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -41,6 +46,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -68,6 +74,9 @@
private String mFetchError;
private final String mUiName;

/**
* Constructs a new source for the given repository URL.
* @param url The source URL. Cannot be null. If the URL ends with a /, the default
//Synthetic comment -- @@ -183,7 +192,8 @@

/**
* Returns the list of known packages found by the last call to load().
     * This is null when the source hasn't been loaded yet.
*/
public Package[] getPackages() {
return mPackages;
//Synthetic comment -- @@ -208,6 +218,107 @@
}

/**
* Returns the short description of the source, if not null.
* Otherwise returns the default Object toString result.
* <p/>
//Synthetic comment -- @@ -258,13 +369,26 @@
}

/**
     * Tries to fetch the repository index for the given URL.
*/
public void load(ITaskMonitor monitor, boolean forceHttp) {

monitor.setProgressMax(7);

        setDefaultDescription();

String url = mUrl;
if (forceHttp) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index 22311a7..dc33966 100755

//Synthetic comment -- @@ -44,11 +44,19 @@
private final EnumMap<SdkSourceCategory, ArrayList<SdkSource>> mSources =
new EnumMap<SdkSourceCategory, ArrayList<SdkSource>>(SdkSourceCategory.class);

public SdkSources() {
}

/**
* Adds a new source to the Sources list.
*/
public void add(SdkSourceCategory category, SdkSource source) {
synchronized (mSources) {
//Synthetic comment -- @@ -64,6 +72,9 @@

/**
* Removes a source from the Sources list.
*/
public void remove(SdkSource source) {
synchronized (mSources) {
//Synthetic comment -- @@ -85,6 +96,9 @@

/**
* Removes all the sources in the given category.
*/
public void removeAll(SdkSourceCategory category) {
synchronized (mSources) {
//Synthetic comment -- @@ -243,53 +257,63 @@
/**
* Loads all user sources. This <em>replaces</em> all existing user sources
* by the ones from the property file.
*/
public void loadUserAddons(ISdkLog log) {

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

/**
//Synthetic comment -- @@ -297,38 +321,93 @@
* @param log Logger. Cannot be null.
*/
public void saveUserAddons(ISdkLog log) {
        FileOutputStream fos = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SRC_FILENAME);

            fos = new FileOutputStream(f);

            Properties props = new Properties();

            int count = 0;
            for (SdkSource s : getSources(SdkSourceCategory.USER_ADDONS)) {
                props.setProperty(String.format("%s%02d", KEY_SRC, count), s.getUrl());  //$NON-NLS-1$
                count++;
            }
            props.setProperty(KEY_COUNT, Integer.toString(count));

            props.store( fos, "## User Sources for Android tool");  //$NON-NLS-1$

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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 4f39b71..dd51a59 100755

//Synthetic comment -- @@ -328,7 +328,8 @@
new SdkRepoSource(baseUrl,
SdkSourceCategory.ANDROID_REPO.getUiName()));

        // Load user sources
sources.loadUserAddons(getSdkLog());
}

//Synthetic comment -- @@ -1063,6 +1064,8 @@
}
}

mStateFetchRemoteAddonsList = 1;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AddonSitesDialog.java
//Synthetic comment -- index 28a065a..02b3657 100755

//Synthetic comment -- @@ -23,9 +23,14 @@
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
//Synthetic comment -- @@ -46,110 +51,220 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.Arrays;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private TableColumn mColumnUrl;

/**
* Create the dialog.
*
* @param parent The parent's shell
*/
public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
super(parent, updaterData, "Add-on Sites");
}

/**
* Create contents of the dialog.
*/
@SuppressWarnings("unused")
@Override
protected void createContents() {
super.createContents();
Shell shell = getShell();
        shell.setMinimumSize(new Point(450, 300));
        shell.setSize(450, 300);

Label label = new Label(shell, SWT.NONE);
GridDataBuilder.create(label).hLeft().vCenter().hSpan(2);
label.setText(
            "This dialog lets you manage the URLs of external add-on sites to be used.\n" +
"\n" +
            "Add-on sites can provide new add-ons or \"user\" packages.\n" +
            "They cannot provide standard Android platforms, docs or samples packages.\n" +
"Adding a URL here will not allow you to clone an official Android repository."
);

        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
@Override
public void selectionChanged(SelectionChangedEvent event) {
                on_TableViewer_selectionChanged(event);
}
});
        mTable = mTableViewer.getTable();
        mTable.setLinesVisible(false);
        mTable.addMouseListener(new MouseAdapter() {
@Override
            public void mouseUp(MouseEvent e) {
                on_Table_mouseUp(e);
}
});
        GridDataBuilder.create(mTable).fill().grab().vSpan(5);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        mColumnUrl = tableViewerColumn.getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("New Column");

        mButtonNew = new Button(shell, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                newOrEdit(false /*isEdit*/);
}
});
        GridDataBuilder.create(mButtonNew).hFill().vCenter();
        mButtonNew.setText("New...");

        mButtonEdit = new Button(shell, SWT.NONE);
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                newOrEdit(true /*isEdit*/);
}
});
        GridDataBuilder.create(mButtonEdit).hFill().vCenter();
        mButtonEdit.setText("Edit...");

        mButtonDelete = new Button(shell, SWT.NONE);
        mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                on_ButtonDelete_widgetSelected(e);
}
});
        GridDataBuilder.create(mButtonDelete).hFill().vCenter();
        mButtonDelete.setText("Delete...");
        new Label(shell, SWT.NONE);

        createCloseButton();

        adjustColumnsWidth(mTable, mColumnUrl);
}

/**
//Synthetic comment -- @@ -166,12 +281,11 @@
});
}

    private void newOrEdit(final boolean isEdit) {
        SdkSources sources = getUpdaterData().getSources();
        final SdkSource[] knownSources = sources.getAllSources();
String title = isEdit ? "Edit Add-on Site URL" : "Add Add-on Site URL";
String msg = "Please enter the URL of the addon.xml:";
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
final String initialValue = !isEdit || sel.isEmpty() ? null :
sel.getFirstElement().toString();

//Synthetic comment -- @@ -228,9 +342,9 @@
if (isEdit && initialValue != null) {
// Remove the old value before we add the new one, which is we just
// asserted will be different.
                    for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (initialValue.equals(source.getUrl())) {
                            sources.remove(source);
break;
}
}
//Synthetic comment -- @@ -239,21 +353,22 @@

// create the source, store it and update the list
SdkAddonSource newSource = new SdkAddonSource(url, null/*uiName*/);
                sources.add(
SdkSourceCategory.USER_ADDONS,
newSource);
setReturnValue(true);
                loadList();

// select the new source
IStructuredSelection newSel = new StructuredSelection(newSource);
                mTableViewer.setSelection(newSel, true /*reveal*/);
}
}
}

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
String selectedUrl = sel.isEmpty() ? null : sel.getFirstElement().toString();

if (selectedUrl == null) {
//Synthetic comment -- @@ -265,58 +380,116 @@
mb.setText("Delete add-on site");
mb.setMessage(String.format("Do you want to delete the URL %1$s?", selectedUrl));
if (mb.open() == SWT.YES) {
            SdkSources sources = getUpdaterData().getSources();
            for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (selectedUrl.equals(source.getUrl())) {
                    sources.remove(source);
setReturnValue(true);
                    loadList();
}
}
}
}

    private void on_Table_mouseUp(MouseEvent e) {
        Point p = new Point(e.x, e.y);
        if (mTable.getItem(p) == null) {
            mTable.deselectAll();
            on_TableViewer_selectionChanged(null /*event*/);
}
}

    private void on_TableViewer_selectionChanged(SelectionChangedEvent event) {
        ISelection sel = mTableViewer.getSelection();
        mButtonDelete.setEnabled(!sel.isEmpty());
        mButtonEdit.setEnabled(!sel.isEmpty());
}

@Override
protected void postCreate() {
// initialize the list
        mTableViewer.setLabelProvider(new LabelProvider());
        mTableViewer.setContentProvider(new SourcesContentProvider());
        loadList();
}

    private void loadList() {
        if (getUpdaterData() != null) {
            SdkSource[] knownSources =
                getUpdaterData().getSources().getSources(SdkSourceCategory.USER_ADDONS);
            Arrays.sort(knownSources);

            ISelection oldSelection = mTableViewer.getSelection();

            mTableViewer.setInput(knownSources);
            mTableViewer.refresh();
            // initialize buttons' state that depend on the list
            on_TableViewer_selectionChanged(null /*event*/);

            if (oldSelection != null && !oldSelection.isEmpty()) {
                mTableViewer.setSelection(oldSelection, true /*reveal*/);
            }
}
}

private static class SourcesContentProvider implements IStructuredContentProvider {

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogic.java
//Synthetic comment -- index 3d7cdb0..8bbeb65 100755

//Synthetic comment -- @@ -302,9 +302,7 @@
}

public boolean updateSourcePackages(SdkSource source, Package[] newPackages) {
            if (newPackages.length > 0) {
                mVisitedSources.add(source);
            }
if (source == null) {
return processLocals(this, newPackages);
} else {
//Synthetic comment -- @@ -759,6 +757,29 @@
* {@link UpdateOp} describing the Sort-by-Source operation.
*/
private class UpdateOpSource extends UpdateOp {
@Override
public Object getCategoryKey(Package pkg) {
// Sort by source







