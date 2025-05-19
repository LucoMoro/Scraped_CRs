//<Beginning of snippet n. 0>


private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;

private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;
private String defaultAction;

public FindDialog(Shell shell, IFindTarget target, String defaultAction) {
    super(shell);
    this.mTarget = target;

    this.defaultAction = "logcat".equals(context) ? "next" : "previous";
    if (!"next".equals(this.defaultAction) && !"previous".equals(this.defaultAction)) {
        throw new IllegalArgumentException("Invalid defaultAction: " + defaultAction);
    } 

    setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
    setBlockOnOpen(true);
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
    mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", "next".equals(defaultAction));
    mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", "previous".equals(defaultAction));
}

//<End of snippet n. 0>


//<Beginning of snippet n. 1>


return;
}

String context = /* Determine context here (logcat or gltrace) */;
mFindDialog = new FindDialog(Display.getDefault().getActiveShell(), mFindTarget, context);
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
//<End of snippet n. 1>