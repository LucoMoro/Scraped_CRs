//<Beginning of snippet n. 0>

import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddonSitesDialog extends UpdaterBaseDialog {

    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonEdit;
    private Button mButtonNext;
    private Button mButtonBack;
    private List<String> userInputUrls = new ArrayList<>();
    private int currentPage = 0; // 0: add URL page, 1: select official sites page

    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, updaterData, "Add-on Sites");
    }

    @Override
    protected void createContents() {
        shell.setMinimumSize(new Point(450, 300));
        shell.setSize(450, 300);

        Label label = new Label(shell, SWT.NONE);
        GridDataBuilder.create(label).hLeft().vCenter().hSpan(2);
        label.setText("This dialog lets you manage the URLs of external add-on sites.");

        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mTable = mTableViewer.getTable();
        mTable.setLinesVisible(false);
        GridDataBuilder.create(mTable).fill().grab().vSpan(5);

        TableViewerColumn tableViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn mColumnUrl = tableViewerColumn.getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("Add-on URLs");

        createNavigationButtons();
        createCloseButton();
        adjustColumnsWidth(mTable, mColumnUrl);
        loadList();
    }

    private void createNavigationButtons() {
        mButtonNew = new Button(shell, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 0) newOrEdit(false);
            }
        });
        GridDataBuilder.create(mButtonNew).hFill().vCenter();
        mButtonNew.setText("New...");

        mButtonEdit = new Button(shell, SWT.NONE);
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 0) newOrEdit(true);
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

        mButtonNext = new Button(shell, SWT.NONE);
        mButtonNext.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 0 && validateInput()) {
                    currentPage = 1;
                    updatePage();
                }
            }
        });
        mButtonNext.setText("Next");

        mButtonBack = new Button(shell, SWT.NONE);
        mButtonBack.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (currentPage == 1) {
                    currentPage = 0;
                    updatePage();
                }
            }
        });
        mButtonBack.setText("Back");
    }

    private void updatePage() {
        mButtonNext.setVisible(currentPage == 0);
        mButtonBack.setVisible(currentPage == 1);
        mButtonDelete.setEnabled(currentPage == 0);
        mButtonEdit.setEnabled(currentPage == 0);
    }

    private boolean validateInput() {
        for (String url : userInputUrls) {
            if (!isValidURL(url)) {
                // Provide visual feedback for invalid URL
                return false;
            }
        }
        return true;
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private void newOrEdit(final boolean isEdit) {
        // Implement new or edit functionality, potentially with InputDialog for URL
        InputDialog dialog = new InputDialog(shell, isEdit ? "Edit URL" : "New URL", "Enter URL:", "", new IInputValidator() {
            @Override
            public String isValid(String newText) {
                return isValidURL(newText) ? null : "Invalid URL";
            }
        });
        if (dialog.open() == InputDialog.OK) {
            String url = dialog.getValue();
            if (isEdit) {
                // Find and update the existing URL in userInputUrls
            } else {
                userInputUrls.add(url);
            }
            loadList();
        }
    }

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        // Implement delete functionality
    }

    private void loadList() {
        if (getUpdaterData() != null) {
            String[] knownUrls = getUpdaterData().getSources().getSources(SdkSourceCategory.USER_ADDONS);
            Arrays.sort(knownUrls);
            mTableViewer.setInput(knownUrls);
            mTableViewer.refresh();
        }
    }

    @Override
    protected void postCreate() {
        mTableViewer.setLabelProvider(new LabelProvider());
        mTableViewer.setContentProvider(new SourcesContentProvider());
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return (Object[]) inputElement;
        }
    }
}

//<End of snippet n. 0>