/*SDK Manager dialog to manage user add-on sites.

Change-Id:I36209964b8a59c6f8b987032e175849a35cf467a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 58bf314..1a8a9f4 100755

//Synthetic comment -- @@ -57,7 +57,7 @@
* It may be a full repository or an add-on only repository.
* A repository describes one or {@link Package}s available for download.
*/
public abstract class SdkSource implements IDescription, Comparable<SdkSource> {

private String mUrl;

//Synthetic comment -- @@ -151,6 +151,14 @@
}

/**
     * Implementation of the {@link Comparable} interface.
     * Simply compares the URL using the string's default ordering.
     */
    public int compareTo(SdkSource rhs) {
        return this.getUrl().compareTo(rhs.getUrl());
    }

    /**
* Returns the UI-visible name of the source. Can be null.
*/
public String getUiName() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index 22678b3..be99f22 100755

//Synthetic comment -- @@ -111,7 +111,7 @@
}

/**
     * Returns a new array of sources attached to the given category.
* Might return an empty array, but never returns null.
*/
public SdkSource[] getSources(SdkSourceCategory category) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
new file mode 100755
//Synthetic comment -- index 0000000..c616379

//Synthetic comment -- @@ -0,0 +1,372 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.Arrays;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class AddonSitesDialog extends Dialog {

    private boolean mChanged;
    private final UpdaterData mUpdaterData;

    private Shell mShell;
    private Table mTable;
    private TableViewer mTableViewer;
    private Button mButtonNew;
    private Button mButtonDelete;
    private Button mButtonClose;
    private Label mlabel;
    private Button mButtonEdit;
    private TableColumn mColumnUrl;

    /**
     * Create the dialog.
     *
     * @param parent The parent's shell
     */
    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, SWT.NONE);
        mUpdaterData = updaterData;
        setText("Add-on Sites");
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

        mTableViewer = new TableViewer(mShell, SWT.BORDER | SWT.FULL_SELECTION);
        mTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
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
        mTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));

        TableViewerColumn tableViewerColumn = new TableViewerColumn(mTableViewer, SWT.NONE);
        mColumnUrl = tableViewerColumn.getColumn();
        mColumnUrl.setWidth(100);
        mColumnUrl.setText("New Column");

        mButtonNew = new Button(mShell, SWT.NONE);
        mButtonNew.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(false /*isEdit*/);
            }
        });
        mButtonNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        mButtonNew.setText("New...");

        mButtonEdit = new Button(mShell, SWT.NONE);
        mButtonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                newOrEdit(true /*isEdit*/);
            }
        });
        mButtonEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        mButtonEdit.setText("Edit...");

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

        adjustColumnsWidth(mTable, mColumnUrl);
    }

    /**
     * Adds a listener to adjust the column width when the parent is resized.
     */
    private void adjustColumnsWidth(final Table table, final TableColumn column0) {
        // Add a listener to resize the column to the full width of the table
        table.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle r = table.getClientArea();
                column0.setWidth(r.width * 100 / 100); // 100%
            }
        });
    }

    private void newOrEdit(final boolean isEdit) {
        SdkSources sources = mUpdaterData.getSources();
        final SdkSource[] knownSources = sources.getAllSources();
        String title = isEdit ? "Edit Add-on Site URL" : "Add Add-on Site URL";
        String msg = "Please enter the URL of the addon.xml:";
        IStructuredSelection sel = (IStructuredSelection) mTableViewer.getSelection();
        final String initialValue = !isEdit || sel.isEmpty() ? null :
                                                               sel.getFirstElement().toString();

        if (isEdit && initialValue == null) {
            // Edit with no actual value is not supposed to happen. Ignore this case.
            return;
        }

        InputDialog dlg = new InputDialog(mShell, title, msg, initialValue, new IInputValidator() {
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

                if (isEdit && newText.equals(initialValue)) {
                    // Edited value hasn't changed. This isn't an error.
                    return null;
                }

                // Reject URLs that are already in the source list.
                // URLs are generally case-insensitive (except for file:// where it all depends
                // on the current OS so we'll ignore this case.)
                for (SdkSource s : knownSources) {
                    if (newText.equalsIgnoreCase(s.getUrl())) {
                        return "Error: This site is already listed.";
                    }
                }

                return null;
            }
        });

        if (dlg.open() == Window.OK) {
            String url = dlg.getValue().trim();

            if (!url.equals(initialValue)) {
                if (isEdit && initialValue != null) {
                    // Remove the old value before we add the new one, which is we just
                    // asserted will be different.
                    for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
                        if (initialValue.equals(source.getUrl())) {
                            sources.remove(source);
                            break;
                        }
                    }

                }

                // create the source, store it and update the list
                SdkAddonSource newSource = new SdkAddonSource(url, null/*uiName*/);
                sources.add(
                        SdkSourceCategory.USER_ADDONS,
                        newSource);
                mChanged = true;
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
            return;
        }

        MessageBox mb = new MessageBox(mShell,
                SWT.YES | SWT.NO | SWT.ICON_QUESTION | SWT.APPLICATION_MODAL);
        mb.setText("Delete add-on site");
        mb.setMessage(String.format("Do you want to delete the URL %1$s?", selectedUrl));
        if (mb.open() == SWT.YES) {
            SdkSources sources = mUpdaterData.getSources();
            for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
                if (selectedUrl.equals(source.getUrl())) {
                    sources.remove(source);
                    mChanged = true;
                    loadList();
                }
            }
        }
    }

    private void on_ButtonClose_widgetSelected(SelectionEvent e) {
        mShell.close();
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

    private void postCreate() {
        // initialize the list
        mTableViewer.setLabelProvider(new LabelProvider());
        mTableViewer.setContentProvider(new SourcesContentProvider());
        loadList();
    }

    private void loadList() {
        if (mUpdaterData != null) {
            SdkSource[] knownSources =
                mUpdaterData.getSources().getSources(SdkSourceCategory.USER_ADDONS);
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
//Synthetic comment -- index 51d20f7..6f9e1fa 100755

//Synthetic comment -- @@ -95,6 +95,8 @@
private Color mColorUpdate;
private Color mColorNew;
private Font mTreeFontItalic;
    private Button mButtonReload;
    private Button mButtonAddonSites;

public PackagesPage(Composite parent, UpdaterData updaterData) {
super(parent, SWT.BORDER);
//Synthetic comment -- @@ -188,61 +190,89 @@
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
        mButtonAddonSites.setText("Add-on Sites...");

        Label label6 = new Label(mGroupButtons, SWT.NONE);
        label6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        mButtonReload = new Button(mGroupButtons, SWT.NONE);
        mButtonReload.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onButtonReload(e);
            }
        });
        mButtonReload.setToolTipText("Reload the package list");
        mButtonReload.setText("Reload");

        Label label5 = new Label(mGroupButtons, SWT.NONE);
        label5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
//Synthetic comment -- @@ -806,4 +836,14 @@
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







