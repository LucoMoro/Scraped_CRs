/*Refactor some common Dialog methods in a base class.

Change-Id:I4a81badcfeeca70fde747590daee9ae91c17795f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
//Synthetic comment -- index 9502099..d9d4111 100755

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdklib.internal.repository.SdkSources;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
//Synthetic comment -- @@ -47,8 +47,6 @@
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -57,22 +55,10 @@

import java.util.Arrays;

public class AddonSitesDialog extends Dialog {

    /**
     * Min Y location for dialog. Need to deal with the menu bar on mac os.
     */
    private final static int MIN_Y = SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN ?
            20 : 0;


    /** Last dialog size for this session. */
    private static Point sLastSize;

private final UpdaterData mUpdaterData;
    private boolean mChanged;

    private Shell mShell;
private Table mTable;
private TableViewer mTableViewer;
private Button mButtonNew;
//Synthetic comment -- @@ -88,45 +74,24 @@
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
//Synthetic comment -- @@ -136,7 +101,7 @@
"Adding a URL here will not allow you to clone an official Android repository."
);

        mTableViewer = new TableViewer(mShell, SWT.BORDER | SWT.FULL_SELECTION);
mTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
public void selectionChanged(SelectionChangedEvent event) {
on_TableViewer_selectionChanged(event);
//Synthetic comment -- @@ -157,7 +122,7 @@
mColumnUrl.setWidth(100);
mColumnUrl.setText("New Column");

        mButtonNew = new Button(mShell, SWT.NONE);
mButtonNew.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -167,7 +132,7 @@
mButtonNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonNew.setText("New...");

        mButtonEdit = new Button(mShell, SWT.NONE);
mButtonEdit.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -177,7 +142,7 @@
mButtonEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonEdit.setText("Edit...");

        mButtonDelete = new Button(mShell, SWT.NONE);
mButtonDelete.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -186,9 +151,9 @@
});
mButtonDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
mButtonDelete.setText("Delete...");
        new Label(mShell, SWT.NONE);

        mButtonClose = new Button(mShell, SWT.NONE);
mButtonClose.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -215,39 +180,6 @@
});
}

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
            Point childSize = sLastSize != null ? sLastSize : child.getSize();
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

private void newOrEdit(final boolean isEdit) {
SdkSources sources = mUpdaterData.getSources();
final SdkSource[] knownSources = sources.getAllSources();
//Synthetic comment -- @@ -262,7 +194,12 @@
return;
}

        InputDialog dlg = new InputDialog(mShell, title, msg, initialValue, new IInputValidator() {
public String isValid(String newText) {

newText = newText == null ? null : newText.trim();
//Synthetic comment -- @@ -318,7 +255,7 @@
sources.add(
SdkSourceCategory.USER_ADDONS,
newSource);
                mChanged = true;
loadList();

// select the new source
//Synthetic comment -- @@ -336,7 +273,7 @@
return;
}

        MessageBox mb = new MessageBox(mShell,
SWT.YES | SWT.NO | SWT.ICON_QUESTION | SWT.APPLICATION_MODAL);
mb.setText("Delete add-on site");
mb.setMessage(String.format("Do you want to delete the URL %1$s?", selectedUrl));
//Synthetic comment -- @@ -345,7 +282,7 @@
for (SdkSource source : sources.getSources(SdkSourceCategory.USER_ADDONS)) {
if (selectedUrl.equals(source.getUrl())) {
sources.remove(source);
                    mChanged = true;
loadList();
}
}
//Synthetic comment -- @@ -353,7 +290,7 @@
}

private void on_ButtonClose_widgetSelected(SelectionEvent e) {
        mShell.close();
}

private void on_Table_mouseUp(MouseEvent e) {
//Synthetic comment -- @@ -370,7 +307,8 @@
mButtonEdit.setEnabled(!sel.isEmpty());
}

    private void postCreate() {
// initialize the list
mTableViewer.setLabelProvider(new LabelProvider());
mTableViewer.setContentProvider(new SourcesContentProvider());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 6a85c14..4c0ea8c 100644

//Synthetic comment -- @@ -21,15 +21,12 @@
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -40,55 +37,22 @@
/**
* Dialog displaying the details of an AVD.
*/
final class AvdDetailsDialog extends Dialog {

    /** Last dialog size for this session. */
    private static Point sLastSize;

    private Shell mDialogShell;
private final AvdInfo mAvdInfo;

private Composite mRootComposite;

public AvdDetailsDialog(Shell shell, AvdInfo avdInfo) {
        super(shell, SWT.APPLICATION_MODAL);
mAvdInfo = avdInfo;

        setText("AVD details");
    }

    /**
     * Open the dialog and blocks till it gets closed
     */
    public void open() {
        createContents();
        positionShell();            //$hide$ (hide from SWT designer)
        mDialogShell.open();
        mDialogShell.layout();

        Display display = getParent().getDisplay();
        while (!mDialogShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        if (!mDialogShell.isDisposed()) {
            sLastSize = mDialogShell.getSize();
            mDialogShell.close();
        }
}

/**
* Create contents of the dialog.
*/
    private void createContents() {
        mDialogShell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
        mDialogShell.setLayout(new GridLayout(1, false));
        mDialogShell.setSize(450, 300);
        mDialogShell.setText(getText());

        mRootComposite = new Composite(mDialogShell, SWT.NONE);
mRootComposite.setLayout(new GridLayout(2, false));
mRootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

//Synthetic comment -- @@ -169,6 +133,12 @@
// Hide everything down-below from SWT designer
//$hide>>$

/**
* Displays a value with a label.
*
//Synthetic comment -- @@ -187,33 +157,6 @@
l.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
}

    /**
     * Centers the dialog in its parent shell.
     */
    private void positionShell() {
        // Centers the dialog in its parent shell
        Shell child = mDialogShell;
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
            Point childSize = sLastSize != null ? sLastSize : child.getSize();
            int cw = childSize.x;
            int ch = childSize.y;

            child.setLocation(px + (pw - cw) / 2, py + (ph - ch) / 2);
            child.setSize(cw, ch);
        }
    }

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDialog.java
//Synthetic comment -- index 8329fd6..9bf9c29 100644

//Synthetic comment -- @@ -26,12 +26,15 @@
import org.eclipse.swt.widgets.Shell;

/**
 * jface-based dialog that properly sets up a {@link GridLayout} top composite with the proper
* margin.
 *
* Implementing dialog must create the content of the dialog in
* {@link #createDialogContent(Composite)}.
 *
*/
public abstract class GridDialog extends Dialog {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/SwtBaseDialog.java
new file mode 100755
//Synthetic comment -- index 0000000..524c7b5

//Synthetic comment -- @@ -0,0 +1,214 @@







