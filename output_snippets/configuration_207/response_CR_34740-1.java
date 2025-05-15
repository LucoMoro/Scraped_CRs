//<Beginning of snippet n. 0>


import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.layout.GridLayout;

import java.util.Arrays;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private Button mButtonNext;
    private Button mButtonPrevious;
    private TableColumn mColumnUrl;
    private int currentPage = 0;
    private String[] customUrls = new String[]{};
    private boolean[] officialSiteSelections;

    private static final int TOTAL_PAGES = 2;

    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, updaterData, "Add-on Sites");

        shell.setMinimumSize(new Point(450, 300));
        shell.setSize(450, 300);
        shell.setLayout(new GridLayout(2, false));

        Label label = new Label(shell, SWT.NONE);
        GridDataBuilder.create(label).hLeft().vCenter().hSpan(2);
        label.setText("This dialog lets you manage the URLs of external add-on sites to be used.\n" +
                        "\n" +
                        "Add-on sites can provide new add-ons or \"user\" packages.\n" +
                        "They cannot provide standard Android platforms, docs or samples packages.\n" +
                        "Adding a URL here will not allow you to clone an official Android repository."
        );

        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mTableViewer.addPostSelectionChangedListener(this::on_TableViewer_selectionChanged);
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
        mColumnUrl.setText("URLs");

        mButtonNew = new Button(shell, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(false);
            }
        });
        GridDataBuilder.create(mButtonNew).hFill().vCenter();
        mButtonNew.setText("New...");

        mButtonEdit = new Button(shell, SWT.NONE);
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(true);
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

        // Navigation Buttons
        mButtonPrevious = new Button(shell, SWT.NONE);
        mButtonPrevious.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToPage(currentPage - 1);
            }
        });
        mButtonPrevious.setText("Previous");

        mButtonNext = new Button(shell, SWT.NONE);
        mButtonNext.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToPage(currentPage + 1);
            }
        });
        mButtonNext.setText("Next");

        createCloseButton();
        adjustColumnsWidth(mTable, mColumnUrl);
        loadPage();
    }

    private void navigateToPage(int page) {
        if (page >= 0 && page < TOTAL_PAGES) {
            currentPage = page;
            loadPage();
        }
    }

    private void loadPage() {
        clearPage();
        if (currentPage == 0) {
            loadCustomUrlsPage();
        } else if (currentPage == 1) {
            loadOfficialSitesPage();
        }
    }

    private void clearPage() {
        mTableViewer.setInput(new String[]{});
        mButtonEdit.setEnabled(false);
        mButtonDelete.setEnabled(false);
    }

    private void loadCustomUrlsPage() {
        mTableViewer.setInput(customUrls);
    }

    private void loadOfficialSitesPage() {
        // Dummy data for official sites
        String[] officialSites = {"Official Site 1", "Official Site 2", "Official Site 3"};
        officialSiteSelections = new boolean[officialSites.length];
        mTableViewer.setInput(officialSites);
        mTableViewer.refresh();
        Arrays.fill(officialSiteSelections, false);
        // Add GUI Touch for enabling selection (checkboxes)
    }

    private void newOrEdit(final boolean isEdit) {
        String title = isEdit ? "Edit Add-on Site URL" : "Add Add-on Site URL";
        String msg = "Please enter the URL of the addon.xml:";
        // Implementation of the InputDialog here
    }

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
        String selectedUrl = sel.isEmpty() ? null : sel.getFirstElement().toString();
        if (selectedUrl != null) {
            // Implementation for deletion
        }
    }

    private void on_Table_mouseUp(MouseEvent e) {
        Point p = new Point(e.x, e.y);
        if (mTable.getItem(p) == null) {
            mTable.deselectAll();
            on_TableViewer_selectionChanged(null);
        }
    }

    private void on_TableViewer_selectionChanged(SelectionChangedEvent event) {
        ISelection sel = mTableViewer.getSelection();
        mButtonDelete.setEnabled(!sel.isEmpty());
        mButtonEdit.setEnabled(!sel.isEmpty());
    }

    @Override
    protected void postCreate() {
        mTableViewer.setLabelProvider(new LabelProvider());
        mTableViewer.setContentProvider(new SourcesContentProvider());
        loadPage();
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        // Content provider implementation here
    }
}
//<End of snippet n. 0>