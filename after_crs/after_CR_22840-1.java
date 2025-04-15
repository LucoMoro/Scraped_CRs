/*Refactor some common Dialog methods in a base class.

Change-Id:I4a81badcfeeca70fde747590daee9ae91c17795f*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
//Synthetic comment -- index 9502099..d9d4111 100755

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
//Synthetic comment -- @@ -47,8 +47,6 @@
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -57,22 +55,10 @@

import java.util.Arrays;

public class AddonSitesDialog extends SwtBaseDialog {

private final UpdaterData mUpdaterData;

private Table mTable;
private TableViewer mTableViewer;
private Button mButtonNew;
//Synthetic comment -- @@ -88,45 +74,24 @@
* @param parent The parent's shell
*/
public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
        super(parent, SWT.APPLICATION_MODAL, "Add-on Sites");
mUpdaterData = updaterData;
}

/**
* Create contents of the dialog.
*/
    @Override
    protected void createContents() {
        Shell shell = getShell();
        shell.setMinimumSize(new Point(450, 300));
        shell.setSize(450, 300);

GridLayout gl_shell = new GridLayout();
gl_shell.numColumns = 2;
        shell.setLayout(gl_shell);

        mlabel = new Label(shell, SWT.NONE);
mlabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
mlabel.setText(
"This dialog lets you manage the URLs of external add-on sites to be used.\n" +
//Synthetic comment -- @@ -136,7 +101,7 @@
"Adding a URL here will not allow you to clone an official Android repository."
);

        mTableViewer = new TableViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
mTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
public void selectionChanged(SelectionChangedEvent event) {
on_TableViewer_selectionChanged(event);
//Synthetic comment -- @@ -157,7 +122,7 @@
mColumnUrl.setWidth(100);
mColumnUrl.setText("New Column");

        mButtonNew = new Button(shell, SWT.NONE);
mButtonNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -167,7 +132,7 @@
mButtonNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonNew.setText("New...");

        mButtonEdit = new Button(shell, SWT.NONE);
mButtonEdit.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -177,7 +142,7 @@
mButtonEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonEdit.setText("Edit...");

        mButtonDelete = new Button(shell, SWT.NONE);
mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -186,9 +151,9 @@
});
mButtonDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonDelete.setText("Delete...");
        new Label(shell, SWT.NONE);

        mButtonClose = new Button(shell, SWT.NONE);
mButtonClose.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -215,39 +180,6 @@
});
}

private void newOrEdit(final boolean isEdit) {
SdkSources sources = mUpdaterData.getSources();
final SdkSource[] knownSources = sources.getAllSources();
//Synthetic comment -- @@ -262,7 +194,12 @@
return;
}

        InputDialog dlg = new InputDialog(
                getShell(),
                title,
                msg,
                initialValue,
                new IInputValidator() {
public String isValid(String newText) {

newText = newText == null ? null : newText.trim();
//Synthetic comment -- @@ -318,7 +255,7 @@
sources.add(
SdkSourceCategory.USER_ADDONS,
newSource);
                setReturnValue(true);
loadList();

// select the new source
//Synthetic comment -- @@ -336,7 +273,7 @@
return;
}

        MessageBox mb = new MessageBox(getShell(),
SWT.YES | SWT.NO | SWT.ICON_QUESTION | SWT.APPLICATION_MODAL);
mb.setText("Delete add-on site");
mb.setMessage(String.format("Do you want to delete the URL %1$s?", selectedUrl));
//Synthetic comment -- @@ -345,7 +282,7 @@
for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (selectedUrl.equals(source.getUrl())) {
sources.remove(source);
                    setReturnValue(true);
loadList();
}
}
//Synthetic comment -- @@ -353,7 +290,7 @@
}

private void on_ButtonClose_widgetSelected(SelectionEvent e) {
        close();
}

private void on_Table_mouseUp(MouseEvent e) {
//Synthetic comment -- @@ -370,7 +307,8 @@
mButtonEdit.setEnabled(!sel.isEmpty());
}

    @Override
    protected void postCreate() {
// initialize the list
mTableViewer.setLabelProvider(new LabelProvider());
mTableViewer.setContentProvider(new SourcesContentProvider());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 6a85c14..4c0ea8c 100644

//Synthetic comment -- @@ -21,15 +21,12 @@
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -40,55 +37,22 @@
/**
* Dialog displaying the details of an AVD.
*/
final class AvdDetailsDialog extends SwtBaseDialog {

private final AvdInfo mAvdInfo;
private Composite mRootComposite;

public AvdDetailsDialog(Shell shell, AvdInfo avdInfo) {
        super(shell, SWT.APPLICATION_MODAL, "AVD details");
mAvdInfo = avdInfo;
}

/**
* Create contents of the dialog.
*/
    @Override
    protected void createContents() {
        mRootComposite = new Composite(getShell(), SWT.NONE);
mRootComposite.setLayout(new GridLayout(2, false));
mRootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

//Synthetic comment -- @@ -169,6 +133,12 @@
// Hide everything down-below from SWT designer
//$hide>>$


    @Override
    protected void postCreate() {
        // pass
    }

/**
* Displays a value with a label.
*
//Synthetic comment -- @@ -187,33 +157,6 @@
l.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDialog.java
//Synthetic comment -- index 8329fd6..9bf9c29 100644

//Synthetic comment -- @@ -26,12 +26,15 @@
import org.eclipse.swt.widgets.Shell;

/**
 * JFace-based dialog that properly sets up a {@link GridLayout} top composite with the proper
* margin.
 * <p/>
* Implementing dialog must create the content of the dialog in
* {@link #createDialogContent(Composite)}.
 * <p/>
 * A JFace dialog is perfect if you want a typical "OK | cancel" workflow, with the OK and
 * cancel things all handled for you using a predefined layout. If you want a different set
 * of buttons or a different layout, consider {@link SwtBaseDialog} instead.
*/
public abstract class GridDialog extends Dialog {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java
new file mode 100755
//Synthetic comment -- index 0000000..524c7b5

//Synthetic comment -- @@ -0,0 +1,214 @@
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

package com.android.sdkuilib.ui;

import com.android.sdklib.SdkConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.HashMap;
import java.util.Map;

/**
 * A base class for an SWT Dialog.
 * <p/>
 * The base class offers the following goodies: <br/>
 * - Dialog is automatically centered on its parent. <br/>
 * - Dialog size is reused during the session. <br/>
 * - A simple API with an {@link #open()} method that returns a boolean. <br/>
 * <p/>
 * A typical usage is:
 * <pre>
 *   MyDialog extends SwtBaseDialog { ... }
 *   MyDialog d = new MyDialog(parentShell, "My Dialog Title");
 *   if (d.open()) {
 *      ...do something like refresh parent list view
 *   }
 * </pre>
 * We also have a JFace-base {@link GridDialog}.
 * The JFace dialog is good when you just want a typical OK/Cancel layout with the
 * buttons all managed for you.
 * This SWT base dialog has little decoration.
 * It's up to you to manage whatever buttons you want, if any.
 */
public abstract class SwtBaseDialog extends Dialog {

    /**
     * Min Y location for dialog. Need to deal with the menu bar on mac os.
     */
    private final static int MIN_Y =
        SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ? 20 : 0;

    /** Last dialog size for this session, different for each dialog class. */
    private static Map<Class<?>, Point> sLastSizeMap = new HashMap<Class<?>, Point>();

    private boolean mReturnValue;
    private Shell mShell;

    /**
     * Create the dialog.
     *
     * @param parent The parent's shell
     * @param title The dialog title. Must not be null.
     */
    public SwtBaseDialog(Shell parent, int swtStyle, String title) {
        super(parent, swtStyle);
        setText(title);
    }

    /**
     * Open the dialog.
     *
     * @return The last value set using {@link #setReturnValue(boolean)} or false by default.
     */
    public boolean open() {
        createShell();
        createContents();
        positionShell();
        postCreate();
        mShell.open();
        mShell.layout();
        Display display = getParent().getDisplay();
        while (!mShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        return mReturnValue;
    }

    /**
     * Creates the shell for this dialog.
     * The default shell has a size of 450x300, which is also its minimum size.
     * You might want to override these values.
     * <p/>
     * Called before {@link #createContents()}.
     */
    protected void createShell() {
        mShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        mShell.setMinimumSize(new Point(450, 300));
        mShell.setSize(450, 300);
        mShell.setText(getText());
        mShell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                saveSize();
            }
        });
    }

    /**
     * Creates the content and attaches it to the current shell (cf. {@link #getShell()}).
     * <p/>
     * Derived classes should consider creating the UI here and initializing their
     * state in {@link #postCreate()}.
     */
    protected abstract void createContents();

    /**
     * Called after {@link #createContents()} and after {@link #positionShell()}
     * just before the dialog is actually shown on screen.
     * <p/>
     * Derived classes should consider creating the UI in {@link #createContents()} and
     * initialize it here.
     */
    protected abstract void postCreate();

    /**
     * Returns the current value that {@link #open()} will return to the caller.
     * Default is false.
     */
    protected boolean getReturnValue() {
        return mReturnValue;
    }

    /**
     * Sets the value that {@link #open()} will return to the caller.
     * @param returnValue The new value to be returned by {@link #open()}.
     */
    protected void setReturnValue(boolean returnValue) {
        mReturnValue = returnValue;
    }

    /**
     * Returns the shell created by {@link #createShell()}.
     * @return The current {@link Shell}.
     */
    protected Shell getShell() {
        return mShell;
    }

    /**
     * Saves the dialog size and close the dialog.
     * The {@link #open()} method will given return value (see {@link #setReturnValue(boolean)}.
     */
    protected void close() {
        saveSize();
        getShell().close();
    }

    //-------

    /**
     * Centers the dialog in its parent shell.
     */
    private void positionShell() {
        // Centers the dialog in its parent shell
        Shell child = mShell;
        Shell parent = getParent();
        if (child != null && parent != null) {
            // get the parent client area with a location relative to the display
            Rectangle parentArea = parent.getClientArea();
            Point parentLoc = parent.getLocation();
            int px = parentLoc.x;
            int py = parentLoc.y;
            int pw = parentArea.width;
            int ph = parentArea.height;

            // Reuse the last size if there's one, otherwise use the default
            Point childSize = sLastSizeMap.get(this.getClass());
            if (childSize == null) {
                childSize = child.getSize();
            }
            int cw = childSize.x;
            int ch = childSize.y;

            int x = px + (pw - cw) / 2;
            if (x < 0) x = 0;

            int y = py + (ph - ch) / 2;
            if (y < MIN_Y) y = MIN_Y;

            child.setLocation(x, y);
            child.setSize(cw, ch);
        }
    }

    private void saveSize() {
        if (mShell != null && !mShell.isDisposed()) {
            sLastSizeMap.put(this.getClass(), mShell.getSize());
        }
    }

}







