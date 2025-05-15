
//<Beginning of snippet n. 0>


private final IFindTarget mTarget;
private Text mSearchText;
private String mPreviousSearchText;
    private final int mDefaultButtonId;

    /** Id of the "Find Next" button */
    public static final int FIND_NEXT_ID = IDialogConstants.CLIENT_ID;

    /** Id of the "Find Previous button */
    public static final int FIND_PREVIOUS_ID = IDialogConstants.CLIENT_ID + 1;

public FindDialog(Shell shell, IFindTarget target) {
        this(shell, target, FIND_PREVIOUS_ID);
    }

    /**
     * Construct a find dialog.
     * @param shell shell to use
     * @param target delegate to be invoked on user action
     * @param defaultButtonId one of {@code #FIND_NEXT_ID} or {@code #FIND_PREVIOUS_ID}.
     */
    public FindDialog(Shell shell, IFindTarget target, int defaultButtonId) {
super(shell);

mTarget = target;
        mDefaultButtonId = defaultButtonId;

setShellStyle((getShellStyle() & ~SWT.APPLICATION_MODAL) | SWT.MODELESS);
setBlockOnOpen(true);
@Override
protected void createButtonsForButtonBar(Composite parent) {
createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);

        mFindNext = createButton(parent, FIND_NEXT_ID, "Find Next",
                mDefaultButtonId == FIND_NEXT_ID);
        mFindPrevious = createButton(parent, FIND_PREVIOUS_ID, "Find Previous",
                mDefaultButtonId != FIND_NEXT_ID);
mFindNext.setEnabled(false);
mFindPrevious.setEnabled(false);
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


return;
}

        mFindDialog = new FindDialog(Display.getDefault().getActiveShell(),
                mFindTarget,
                FindDialog.FIND_NEXT_ID);
mFindDialog.open(); // blocks until find dialog is closed
mFindDialog = null;
}

//<End of snippet n. 1>








