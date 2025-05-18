//<Beginning of snippet n. 0>
private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;

private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;
private final String defaultAction;

public FindDialog(Shell shell, IFindTarget target, String defaultAction) {
    super(shell);
    mTarget = target;

    if (!"Find Next".equals(defaultAction) && !"Find Previous".equals(defaultAction)) {
        throw new IllegalArgumentException("Invalid default action");
    }
    this.defaultAction = defaultAction;

    setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
    setBlockOnOpen(true);
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
    mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", false);
    mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", false);
    
    if ("Find Next".equals(defaultAction)) {
        setDefaultButton(mFindNext);
        mFindNext.setEnabled(true);
        mFindPrevious.setEnabled(false);
    } else {
        setDefaultButton(mFindPrevious);
        mFindNext.setEnabled(false);
        mFindPrevious.setEnabled(true);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
String action = (isLogcatContext() ? "Find Previous" : "Find Next");
mFindDialog = new FindDialog(Display.getDefault().getActiveShell(), mFindTarget, action);
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
//<End of snippet n. 1>