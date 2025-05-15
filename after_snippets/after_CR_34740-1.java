
//<Beginning of snippet n. 0>


import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mUserTable;
    private TableViewer mUserTableViewer;
    private CheckboxTableViewer mSitesTableViewer;
    private Button mUserButtonNew;
    private Button mUserButtonDelete;
    private Button mUserButtonEdit;

/**
* Create the dialog.
*
* @param parent The parent's shell
     * @wbp.parser.entryPoint
*/
public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
super(parent, updaterData, "Add-on Sites");

/**
* Create contents of the dialog.
     * @wbp.parser.entryPoint
*/
@SuppressWarnings("unused")
@Override
shell.setMinimumSize(new Point(450, 300));
shell.setSize(450, 300);

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
public void mouseUp(MouseEvent e) {
                on_UserTable_mouseUp(e);
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
        GridDataBuilder.create(sitesTable).fill().grab();

        TableViewerColumn columnViewer = new TableViewerColumn(mSitesTableViewer, SWT.NONE);
        TableColumn column = columnViewer.getColumn();
        column.setResizable(true);
        column.setWidth(200);
        column.setText("Name");
        columnViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof SdkSource) {
                    return ((SdkSource) element).getShortDescription();
                }
                return super.getText(element);
            }
        });

        columnViewer = new TableViewerColumn(mSitesTableViewer, SWT.NONE);
        column = columnViewer.getColumn();
        column.setResizable(true);
        column.setWidth(250);
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
}

/**
});
}

    private void userNewOrEdit(final boolean isEdit) {
SdkSources sources = getUpdaterData().getSources();
final SdkSource[] knownSources = sources.getAllSources();
String title = isEdit ? "Edit Add-on Site URL" : "Add Add-on Site URL";
String msg = "Please enter the URL of the addon.xml:";
        IStructuredSelection sel = (IStructuredSelection) mUserTableViewer.getSelection();
final String initialValue = !isEdit || sel.isEmpty() ? null :
sel.getFirstElement().toString();

SdkSourceCategory.USER_ADDONS,
newSource);
setReturnValue(true);
                loadUserUrlsList();

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
if (selectedUrl.equals(source.getUrl())) {
sources.remove(source);
setReturnValue(true);
                    loadUserUrlsList();
}
}
}
}

    private void on_UserTable_mouseUp(MouseEvent e) {
Point p = new Point(e.x, e.y);
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

@Override
protected void postCreate() {
// initialize the list
        loadUserUrlsList();
        loadSiteUrlsList();
}

    private void loadUserUrlsList() {
        if (getUpdaterData() == null) {
            return;
        }

        SdkSource[] knownSources =
            getUpdaterData().getSources().getSources(SdkSourceCategory.USER_ADDONS);
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
        if (getUpdaterData() == null) {
            return;
        }

        SdkSource[] knownSources =
            getUpdaterData().getSources().getSources(SdkSourceCategory.ADDONS_3RD_PARTY);
        Arrays.sort(knownSources);

        ISelection oldSelection = mSitesTableViewer.getSelection();

        mSitesTableViewer.setInput(knownSources);
        mSitesTableViewer.refresh();

        if (oldSelection != null && !oldSelection.isEmpty()) {
            mSitesTableViewer.setSelection(oldSelection, true /*reveal*/);
        }
    }


private static class SourcesContentProvider implements IStructuredContentProvider {

@Override

//<End of snippet n. 0>








