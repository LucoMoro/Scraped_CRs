//<Beginning of snippet n. 0>

private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;
private boolean isFindNextDefault;

private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;

public FindDialog(Shell shell, IFindTarget target, boolean isFindNextDefault) {
    super(shell);
    mTarget = target;
    this.isFindNextDefault = isFindNextDefault;
    setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
    setBlockOnOpen(true);
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
    mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", isFindNextDefault);
    mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", !isFindNextDefault);
    updateButtonStates();
}

private void updateButtonStates() {
    boolean isLogcatContext = /* logic to determine if current context is logcat */;
    boolean isGltraceContext = /* logic to determine if current context is gltrace */;

    mFindNext.setEnabled(isLogcatContext || isGltraceContext);
    mFindPrevious.setEnabled(isGltraceContext);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

return;
}

mFindDialog = new FindDialog(Display.getDefault().getActiveShell(), mFindTarget, true); // Example usage with Find Next as default
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;

//<End of snippet n. 1>