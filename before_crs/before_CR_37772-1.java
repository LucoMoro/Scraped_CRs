/*Find Dialog: allow control over the default action.

This CL allows the default action to be either of the "Find Next"
or the "Find Previous" buttons in the find dialog.

Typically, in logcat you want to find previous, while in gltrace
view you want to find next.

Change-Id:Ie11cbd8a7987b8011ec2ee3664034e9ecfc4e24b*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/FindDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/FindDialog.java
//Synthetic comment -- index 6370be4..fe3f438 100644

//Synthetic comment -- @@ -44,14 +44,29 @@
private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;

    private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
    private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;

public FindDialog(Shell shell, IFindTarget target) {
super(shell);

mTarget = target;

setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
setBlockOnOpen(true);
//Synthetic comment -- @@ -91,8 +106,11 @@
@Override
protected void createButtonsForButtonBar(Composite parent) {
createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
        mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", false);
        mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", /* default */ true);
mFindNext.setEnabled(false);
mFindPrevious.setEnabled(false);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index fc99764..db69ffe 100644

//Synthetic comment -- @@ -778,7 +778,9 @@
return;
}

        mFindDialog = new FindDialog(Display.getDefault().getActiveShell(), mFindTarget);
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
}







