//<Beginning of snippet n. 0>

private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;
private final static int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;
private final static int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;
private ActionType defaultAction;

public enum ActionType {
    FIND_NEXT, FIND_PREVIOUS
}

public FindDialog(Shell shell, IFindTarget target, ActionType defaultAction) {
    super(shell);
    this.mTarget = target;
    this.defaultAction = defaultAction;
    setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
    setBlockOnOpen(true);
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
    mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next", false);
    mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous", false);
    
    boolean isFindNextEnabled = mSearchText != null && !mSearchText.getText().isEmpty();
    boolean isFindPreviousEnabled = mPreviousSearchText != null && !mPreviousSearchText.equals(mSearchText.getText());

    mFindNext.setEnabled(isFindNextEnabled);
    mFindPrevious.setEnabled(isFindPreviousEnabled);
    
    if (defaultAction == ActionType.FIND_NEXT && isFindNextEnabled) {
        setDefaultButton(mFindNext);
    } else {
        setDefaultButton(mFindPrevious);
    }
}

//<End of snippet n. 0>