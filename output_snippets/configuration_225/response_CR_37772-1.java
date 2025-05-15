//<Beginning of snippet n. 0>

private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;

private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;

public enum FindAction {
    FIND_NEXT, FIND_PREVIOUS
}

public FindDialog(Shell shell, IFindTarget target, FindAction defaultAction) {
    super(shell);

    mTarget = target;

    setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
    setBlockOnOpen(true);
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
    
    mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", false);
    mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", false);
    
    if (mTarget instanceof LogcatTarget) {
        mFindPrevious.setEnabled(true);
        mFindPrevious.setDefaultButton(true);
    } else {
        mFindNext.setEnabled(true);
        mFindNext.setDefaultButton(true);
    }
    
    mFindNext.setEnabled(false);
    mFindPrevious.setEnabled(false);
}

//<End of snippet n. 0>