
//<Beginning of snippet n. 0>


import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


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

/**
* Create contents of the dialog.
*/
@SuppressWarnings("unused")
@Override
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

//<End of snippet n. 0>








