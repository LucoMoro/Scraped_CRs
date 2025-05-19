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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableViewer;
import org.eclipse.swt.widgets.TableViewerColumn;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private TableColumn mColumnUrl;
    private Button mButtonNext;
    private Button mButtonBack;
    private int currentPage = 1; // 1 for custom URLs, 2 for official sites
    private String[] customUrls = new String[0]; // To retain input across pages

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
    protected void createButtonsForButtonBar(Composite parent) {
        mButtonNext = new Button(parent, SWT.PUSH);
        mButtonNext.setText("Next");
        mButtonNext.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 1) {
                    if (validateCustomUrlInputs()) {
                        currentPage = 2;
                        updateDialogLayout();
                    } else {
                        showMessage("Please enter valid custom URLs.");
                    }
                }
            }
        });

        mButtonBack = new Button(parent, SWT.PUSH);
        mButtonBack.setText("Back");
        mButtonBack.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 2) {
                    currentPage = 1;
                    updateDialogLayout();
                }
            }
        });

        createCloseButton(parent);
    }

    private void updateDialogLayout() {
        if (currentPage == 1) {
            createCustomUrlPage();
        } else {
            createOfficialSitesPage();
        }
        shell.layout(true, true);
    }

    private void createCustomUrlPage() {
        if (mTableViewer != null) {
            mTableViewer.getControl().dispose();
        }
        Label label = new Label(shell, SWT.NONE);
        label.setText("Manage custom add-on sites:");
        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mTable = mTableViewer.getTable();
        mColumnUrl = new TableViewerColumn(mTableViewer, SWT.NONE).getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("Custom URL");

        mButtonNew = new Button(shell, SWT.PUSH);
        mButtonNew.setText("Add");
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String newUrl = inputUrl("Enter Custom URL:");
                if (newUrl != null) {
                    customUrls = Arrays.copyOf(customUrls, customUrls.length + 1);
                    customUrls[customUrls.length - 1] = newUrl;
                    loadList();
                }
            }
        });

        mButtonEdit = new Button(shell, SWT.PUSH);
        mButtonEdit.setText("Edit");
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Logic to edit selected custom URL
            }
        });

        mButtonDelete = new Button(shell, SWT.PUSH);
        mButtonDelete.setText("Delete");
        mButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Logic to delete selected custom URL
            }
        });

        loadList();
    }

    private String inputUrl(String message) {
        InputDialog dialog = new InputDialog(shell, "Input", message, "", new IInputValidator() {
            public String isValid(String newText) {
                return newText.isEmpty() ? "URL must not be empty." : null;
            }
        });
        if (dialog.open() == InputDialog.OK) {
            return dialog.getValue();
        }
        return null;
    }

    private void createOfficialSitesPage() {
        if (mTableViewer != null) {
            mTableViewer.getControl().dispose();
        }
        Label label = new Label(shell, SWT.NONE);
        label.setText("Select official add-on sites:");
        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mTable = mTableViewer.getTable();
        mColumnUrl = new TableViewerColumn(mTableViewer, SWT.NONE).getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("Official Site");
        loadOfficialSites();
    }

    private boolean validateCustomUrlInputs() {
        if (customUrls.length == 0) return false;
        Pattern urlPattern = Pattern.compile("^(http|https)://[a-zA-Z0-9.-]+(:[0-9]{1,5})?(/.*)?$");
        for (String url : customUrls) {
            Matcher matcher = urlPattern.matcher(url);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private void showMessage(String message) {
        MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
        dialog.setText("Information");
        dialog.setMessage(message);
        dialog.open();
    }

    private void loadList() {
        if (currentPage == 1) {
            mTableViewer.setInput(customUrls);
        } else {
            loadOfficialSites();
        }
    }

    private void loadOfficialSites() {
        // Logic to load official addons into the table
    }

    @Override
    protected void postCreate() {
        updateDialogLayout();
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return new Object[0]; // Implement actual data fetching logic
        }
    }
}

//<End of snippet n. 0>