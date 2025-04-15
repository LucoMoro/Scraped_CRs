/*SDK Manager dialog to manager user add-on sites.

Not ready for review.

Change-Id:I36209964b8a59c6f8b987032e175849a35cf467a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
new file mode 100755
//Synthetic comment -- index 0000000..3b06c68

//Synthetic comment -- @@ -0,0 +1,231 @@
package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class AddonSitesDialog extends Dialog {

    private boolean mChanged;
    private final UpdaterData mUpdaterData;

    private Shell mShell;
    private ListViewer mListViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonClose;
    private Label mlabel;

    /**
     * Create the dialog.
     *
     * @param parent The parent's shell
     */
    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, SWT.NONE);
        mUpdaterData = updaterData;
        setText("Add-ons Sites");
    }

    /**
     * Open the dialog.
     *
     * @return True if anything was changed.
     */
    public boolean open() {
        createContents();
        postCreate();
        mShell.open();
        mShell.layout();
        Display display = getParent().getDisplay();
        while (!mShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return mChanged;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        mShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        mShell.setMinimumSize(new Point(450, 300));
        mShell.setSize(450, 300);
        mShell.setText(getText());
        GridLayout gl_shell = new GridLayout();
        gl_shell.numColumns = 2;
        mShell.setLayout(gl_shell);

        mlabel = new Label(mShell, SWT.NONE);
        mlabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        mlabel.setText(
            "This dialog lets you manage the URLs of external add-on sites to be used.\n" +
            "\n" +
            "Add-on sites can provide new add-ons or \"user\" packages.\n" +
            "They cannot provide standard Android platforms, docs or samples packages.\n" +
            "Adding a URL here will not allow you to clone an official Android repository."
        );

        mListViewer = new ListViewer(mShell, SWT.BORDER | SWT.V_SCROLL);
        mListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                on_ListViewer_selectionChanged(event);
            }
        });
        List list = mListViewer.getList();
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));

        mButtonNew = new Button(mShell, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_ButtonNew_widgetSelected(e);
            }
        });
        mButtonNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        mButtonNew.setText("New...");

        mButtonDelete = new Button(mShell, SWT.NONE);
        mButtonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_ButtonDelete_widgetSelected(e);
            }
        });
        mButtonDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        mButtonDelete.setText("Delete...");
        new Label(mShell, SWT.NONE);

        mButtonClose = new Button(mShell, SWT.NONE);
        mButtonClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                on_ButtonClose_widgetSelected(e);
            }
        });
        mButtonClose.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        mButtonClose.setText("Close");
    }

    private void on_ButtonNew_widgetSelected(SelectionEvent e) {
        final SdkSource[] knownSources = mUpdaterData.getSources().getAllSources();
        String title = "Add Add-on Site URL";
        String msg = "Please enter the URL of the addon.xml:";
        String value = mListViewer.getSelection().isEmpty() ? null :
            mListViewer.getSelection().toString();

        InputDialog dlg = new InputDialog(mShell, title, msg, value, new IInputValidator() {
            public String isValid(String newText) {

                newText = newText == null ? null : newText.trim();

                if (newText == null || newText.length() == 0) {
                    return "Error: URL field is empty. Please enter a URL.";
                }

                // A URL should have one of the following prefixes
                if (!newText.startsWith("file://") &&                 //$NON-NLS-1$
                        !newText.startsWith("ftp://") &&              //$NON-NLS-1$
                        !newText.startsWith("http://") &&             //$NON-NLS-1$
                        !newText.startsWith("https://")) {            //$NON-NLS-1$
                    return "Error: The URL must start by one of file://, ftp://, http:// or https://";
                }

                // Reject URLs that are already in the source list.
                // URLs are generally case-insensitive (except for file:// where it all depends
                // on the current OS so we'll ignore this case.)
                for (SdkSource s : knownSources) {
                    if (newText.equalsIgnoreCase(s.getUrl())) {
                        return "Error : This site is already listed.";
                    }
                }

                return null;
            }
        });

        if (dlg.open() == Window.OK) {
            String url = dlg.getValue().trim();
            mUpdaterData.getSources().add(
                    SdkSourceCategory.USER_ADDONS,
                    new SdkAddonSource(url, null/*uiName*/));
            mChanged = true;
            loadList();
        }
    }

    private void on_ButtonDelete_widgetSelected(SelectionEvent e) {
        // TODO
        mChanged = true;
    }

    private void on_ButtonClose_widgetSelected(SelectionEvent e) {
        mShell.close();
    }

    private void on_ListViewer_selectionChanged(SelectionChangedEvent event) {
        ISelection sel = mListViewer.getSelection();
        mButtonDelete.setEnabled(!sel.isEmpty());
        mButtonNew.setText(sel.isEmpty() ? "New..." : "Edit...");
    }

    private void postCreate() {
        mListViewer.setLabelProvider(new LabelProvider());
        mListViewer.setContentProvider(new SourcesContentProvider());
        if (mUpdaterData != null) {
            loadList();
        }
    }

    private void loadList() {
        final SdkSource[] knownSources =
            mUpdaterData.getSources().getSources(SdkSourceCategory.USER_ADDONS);

        mListViewer.setInput(knownSources);
        mListViewer.refresh();
    }

    private static class SourcesContentProvider implements IStructuredContentProvider {

        public void dispose() {
            // pass
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof SdkSource[]) {
                return (Object[]) inputElement;
            } else {
                return new Object[0];
            }
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 51d20f7..88398b4 100755

//Synthetic comment -- @@ -95,6 +95,10 @@
private Color mColorUpdate;
private Color mColorNew;
private Font mTreeFontItalic;
    private Button mButtonReload;
    private Button mButtonAddonSites;
    private Label mlabel;
    private Label mlabel_1;

public PackagesPage(Composite parent, UpdaterData updaterData) {
super(parent, SWT.BORDER);
//Synthetic comment -- @@ -188,61 +192,89 @@
mCheckSortApi.setText("API level");
mCheckSortApi.setSelection(true);

        Label expandPlaceholder = new Label(mGroupOptions, SWT.NONE);
        expandPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label3 = new Label(mGroupOptions, SWT.NONE);
        label3.setText("Show:");

        mCheckFilterNew = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterNew.setToolTipText("Show Updates and New");
        mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                sortPackages();
            }
        });
        mCheckFilterNew.setImage(getImage("reject_icon16.png"));
        mCheckFilterNew.setSelection(true);
        mCheckFilterNew.setText("Updates/New");

        mCheckFilterInstalled = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterInstalled.setToolTipText("Show Installed");
        mCheckFilterInstalled.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                sortPackages();
            }
        });
        mCheckFilterInstalled.setImage(getImage("accept_icon16.png"));  //$NON-NLS-1$
        mCheckFilterInstalled.setSelection(true);
        mCheckFilterInstalled.setText("Installed");

        mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
        mCheckFilterObsolete.setToolTipText("Show everything including obsolete packages and all archives)");
        mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                sortPackages();
            }
        });
        mCheckFilterObsolete.setImage(getImage("nopkg_icon16.png"));  //$NON-NLS-1$
        mCheckFilterObsolete.setSelection(false);
        mCheckFilterObsolete.setText("Details");

mGroupButtons = new Composite(parent, SWT.NONE);
mGroupButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
false, 1, 1));
        mGroupButtons.setLayout(new GridLayout(7, false));

        mButtonAddonSites = new Button(mGroupButtons, SWT.NONE);
        mButtonAddonSites.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onButtonAddonSites(e);
            }
        });
        mButtonAddonSites.setToolTipText("Manage the list of add-on sites");
        mButtonAddonSites.setText("Add-ons Sites...");

        mlabel_1 = new Label(mGroupButtons, SWT.NONE);
        mlabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        mButtonReload = new Button(mGroupButtons, SWT.NONE);
        mButtonReload.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onButtonReload(e);
            }
        });
        mButtonReload.setToolTipText("Reload the package list");
        mButtonReload.setText("Reload");

        mlabel = new Label(mGroupButtons, SWT.NONE);
        mlabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

mButtonDelete = new Button(mGroupButtons, SWT.NONE);
        mButtonDelete.setToolTipText("Delete an installed package");
        mButtonDelete.setText("Delete...");

Label label4 = new Label(mGroupButtons, SWT.NONE);
label4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

mButtonInstall = new Button(mGroupButtons, SWT.NONE);
        mButtonInstall.setToolTipText("Install all the selected packages");
        mButtonInstall.setText("Install Selected");
}

private Image getImage(String filename) {
//Synthetic comment -- @@ -806,4 +838,14 @@
// --- End of hiding from SWT Designer ---
//$hide<<$

    protected void onButtonReload(SelectionEvent e) {
        loadPackages();
    }

    protected void onButtonAddonSites(SelectionEvent e) {
        AddonSitesDialog d = new AddonSitesDialog(getShell(), mUpdaterData);
        if (d.open()) {
            loadPackages();
        }
    }
}







