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
    private Button mButtonNext;
    private Button mButtonBack;
    private TableColumn mColumnUrl;
    private TableViewer mOfficialAddonTableViewer;
    private String[] customAddonUrls;
    private int[] officialAddonSelections;
    private String selectedOfficialAddon;
    private int currentPage = 1; // Page tracker

    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, updaterData, "Add-on Sites");
    }

    @Override
    protected void createContents() {
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
            public void selectionChanged(ISelectionChangedEvent event) {
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
        mColumnUrl.setText("Installed Add-on URLs");

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

        mButtonNext = new Button(shell, SWT.NONE);
        mButtonNext.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToPage(2);
            }
        });
        GridDataBuilder.create(mButtonNext).hFill().vCenter();
        mButtonNext.setText("Next");

        mButtonBack = new Button(shell, SWT.NONE);
        mButtonBack.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                navigateToPage(1);
            }
        });
        GridDataBuilder.create(mButtonBack).hFill().vCenter();
        mButtonBack.setText("Back");

        new Label(shell, SWT.NONE);
        createCloseButton();
        adjustColumnsWidth(mTable, mColumnUrl);

        createOfficialAddonComponents();
    }

    private void createOfficialAddonComponents() {
        mOfficialAddonTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
        mOfficialAddonTableViewer.setContentProvider(new OfficialAddonContentProvider());
        mOfficialAddonTableViewer.setLabelProvider(new LabelProvider());
        Table nonCustomTable = mOfficialAddonTableViewer.getTable();
        nonCustomTable.setLinesVisible(false);
        GridDataBuilder.create(nonCustomTable).fill().grab().vSpan(5);
        nonCustomTable.setVisible(false);
    }

    private void navigateToPage(int page) {
        currentPage = page;
        if (currentPage == 1) {
            mTableViewer.setInput(customAddonUrls);
            mButtonNext.setEnabled(true);
            mButtonBack.setEnabled(false);
            mOfficialAddonTableViewer.getTable().setVisible(false);
            mTable.setVisible(true);
        } else {
            loadOfficialAddonSites();
            mButtonNext.setEnabled(false);
            mButtonBack.setEnabled(true);
            mTable.setVisible(false);
            mOfficialAddonTableViewer.getTable().setVisible(true);
        }
    }

    private void loadOfficialAddonSites() {
        officialAddonSelections = new int[] {0, 1, 2}; // This should be replaced by actual logic to load official sites
        mOfficialAddonTableViewer.setInput(officialAddonSelections);
        mOfficialAddonTableViewer.refresh();
    }

    private void newOrEdit(final boolean isEdit) {
        // Functionality to add or edit URL
    }

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        // Functionality to delete selected URL
    }

    private void on_Table_mouseUp(MouseEvent e) {
        Point p = new Point(e.x, e.y);
        if (mTable.getItem(p) == null) {
            mTable.deselectAll();
            on_TableViewer_selectionChanged(null);
        }
    }

    private void on_TableViewer_selectionChanged(ISelectionChangedEvent event) {
        ISelection sel = mTableViewer.getSelection();
        mButtonDelete.setEnabled(!sel.isEmpty());
        mButtonEdit.setEnabled(!sel.isEmpty());
    }

    @Override
    protected void postCreate() {
        loadList();
    }

    private void loadList() {
        if (getUpdaterData() != null) {
            customAddonUrls = getUpdaterData().getSources().getSources(SdkSourceCategory.USER_ADDONS);
            Arrays.sort(customAddonUrls);

            ISelection oldSelection = mTableViewer.getSelection();
            mTableViewer.setInput(customAddonUrls);
            mTableViewer.refresh();
            on_TableViewer_selectionChanged(null);

            if (oldSelection != null && !oldSelection.isEmpty()) {
                mTableViewer.setSelection(oldSelection, true);
            }
        }
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return (Object[]) inputElement;
        }

        @Override
        public void dispose() {}

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
    }

    private static class OfficialAddonContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return (Object[]) inputElement; 
        }

        @Override
        public void dispose() {}

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
    }
}

//<End of snippet n. 0>