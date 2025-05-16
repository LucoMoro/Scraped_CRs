//<Beginning of snippet n. 0>


import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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

import java.util.Arrays;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private TableColumn mColumnUrl;

    private int currentPage = 1;
    private String newSourceUrl;
    private String[] officialAddonSites = {"Official Site 1", "Official Site 2"};

    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, updaterData, "Add-on Sites");
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setMinimumSize(new Point(450, 300));
        newShell.setSize(450, 300);
    }

    @Override
    protected void createDialogArea(Composite parent) {
        createPage1(parent);
        createPage2(parent);
        createNavigationButtons(parent);
    }

    private void createPage1(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        GridDataBuilder.create(label).hLeft().vCenter().hSpan(2);
        label.setText("This dialog lets you manage the URLs of external add-on sites to be used.\n" +
                "\n" +
                "Add-on sites can provide new add-ons or \"user\" packages.\n" +
                "They cannot provide standard Android platforms, docs or samples packages.\n" +
                "Adding a URL here will not allow you to clone an official Android repository.");

        mTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
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
        mColumnUrl.setText("Add-on Site URL");

        mButtonNew = new Button(parent, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(false);
            }
        });
        GridDataBuilder.create(mButtonNew).hFill().vCenter();
        mButtonNew.setText("New...");

        mButtonEdit = new Button(parent, SWT.NONE);
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(true);
            }
        });
        GridDataBuilder.create(mButtonEdit).hFill().vCenter();
        mButtonEdit.setText("Edit...");

        mButtonDelete = new Button(parent, SWT.NONE);
        mButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_ButtonDelete_widgetSelected(e);
            }
        });
        GridDataBuilder.create(mButtonDelete).hFill().vCenter();
        mButtonDelete.setText("Delete...");
        new Label(parent, SWT.NONE);
    }

    private void createPage2(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Please select an official add-on site:");
        
        // Add checkboxes or list for official addon sites selection
        for (String site : officialAddonSites) {
            Button siteButton = new Button(parent, SWT.CHECK);
            siteButton.setText(site);
        }
    }

    private void createNavigationButtons(Composite parent) {
        Button nextButton = new Button(parent, SWT.PUSH);
        nextButton.setText("Next");
        nextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 1) {
                    currentPage = 2;
                    // Implement logic to show next page
                }
            }
        });

        Button backButton = new Button(parent, SWT.PUSH);
        backButton.setText("Back");
        backButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 2) {
                    currentPage = 1;
                    // Implement logic to show previous page
                }
            }
        });
    }

    private void newOrEdit(final boolean isEdit) {
        // Input validation for URL
        InputDialog dialog = new InputDialog(getShell(), "URL Input", "Please enter the URL of the addon.xml:", newSourceUrl, new IInputValidator() {
            public String isValid(String newText) {
                return isValidUrl(newText) ? null : "Invalid URL format.";
            }
        });
        if (dialog.open() == Window.OK) {
            String newSource = dialog.getValue();
            // Logic for adding or editing the new source
            setReturnValue(true);
            loadList();
        }
    }

    private boolean isValidUrl(String url) {
        // Implement URL validation logic here
        return true;
    }

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
        String selectedUrl = sel.isEmpty() ? null : sel.getFirstElement().toString();

        if (selectedUrl != null) {
            // Logic for deletion
            loadList();
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
        mTableViewer.setContentProvider(new SourcesContentProvider());
        loadList();
    }

    private void loadList() {
        if (getUpdaterData() != null) {
            SdkSource[] knownSources = getUpdaterData().getSources().getSources(SdkSourceCategory.USER_ADDONS);
            Arrays.sort(knownSources);
            mTableViewer.setInput(knownSources);
            mTableViewer.refresh();
            on_TableViewer_selectionChanged(null);
        }
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return (SdkSource[]) inputElement;
        }
    }
}
//<End of snippet n. 0>