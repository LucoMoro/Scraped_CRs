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
    private Button mButtonNext;
    private Button mButtonBack;
    private Button mButtonFinish;
    private TableColumn mColumnUrl;

    private String[] officialAddonSites = {"Official Site 1", "Official Site 2", "Official Site 3"};
    private String customAddonUrl;
    private String selectedOfficialSite;

    private enum Page {
        CUSTOM, OFFICIAL
    }

    private Page currentPage = Page.CUSTOM;

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
        GridDataBuilder.create(mTable).fill().grab().vSpan(5);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        mColumnUrl = tableViewerColumn.getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("URLs");

        mButtonNew = new Button(shell, SWT.NONE);
        mButtonNew.setText("Add Custom URL...");
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addCustomUrl();
            }
        });

        mButtonEdit = new Button(shell, SWT.NONE);
        mButtonEdit.setText("Edit Selected URL...");
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                editSelectedUrl();
            }
        });

        mButtonDelete = new Button(shell, SWT.NONE);
        mButtonDelete.setText("Delete Selected URL...");
        mButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedUrl();
            }
        });

        mButtonNext = new Button(shell, SWT.NONE);
        mButtonNext.setText("Next");
        mButtonNext.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToOfficialSites();
            }
        });

        mButtonBack = new Button(shell, SWT.NONE);
        mButtonBack.setText("Back");
        mButtonBack.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToCustomUrls();
            }
        });

        mButtonFinish = new Button(shell, SWT.NONE);
        mButtonFinish.setText("Finish");
        mButtonFinish.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                finishDialog();
            }
        });

        createCloseButton();
        adjustColumnsWidth(mTable, mColumnUrl);
        updateDialog();
    }

    private void addCustomUrl() {
        InputDialog inputDialog = new InputDialog(shell, "Add Custom URL", "Enter URL:", "", new IInputValidator() {
            @Override
            public String isValid(String newText) {
                return newText.isEmpty() ? "URL cannot be empty" : null;
            }
        });
        if (inputDialog.open() == InputDialog.OK) {
            customAddonUrl = inputDialog.getValue();
            // Persist the new custom URL logic
            loadList();
        }
    }

    private void editSelectedUrl() {
        // Editing logic goes here
    }

    private void deleteSelectedUrl() {
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
        // Delete logic goes here
    }

    private void navigateToOfficialSites() {
        currentPage = Page.OFFICIAL;
        updateDialog();
    }

    private void navigateToCustomUrls() {
        currentPage = Page.CUSTOM;
        updateDialog();
    }

    private void finishDialog() {
        // Finish logic goes here
    }

    private void updateDialog() {
        switch (currentPage) {
            case CUSTOM:
                mTableViewer.setInput(null);  // Clear the current input
                mTableViewer.refresh();
                // Show custom URL components
                // Hide official site selection
                break;
            case OFFICIAL:
                mTableViewer.setInput(officialAddonSites);
                mTableViewer.refresh();
                // Show official site selection components
                break;
        }
    }

    private void on_TableViewer_selectionChanged(SelectionChangedEvent event) {
        ISelection sel = mTableViewer.getSelection();
        mButtonDelete.setEnabled(!sel.isEmpty());
        mButtonEdit.setEnabled(!sel.isEmpty());
    }

    @Override
    protected void postCreate() {
        // Initialize the list
        mTableViewer.setLabelProvider(new LabelProvider());
        loadList();
    }

    private void loadList() {
        // Logic to load the list of URLs
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return new Object[0]; // Implement with actual sources logic
        }
    } 

//<End of snippet n. 0>