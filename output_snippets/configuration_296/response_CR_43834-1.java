//<Beginning of snippet n. 0>
@Override
public void createControl(Composite parent) {
        mTextControl = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        mTextControl.setEditable(false);
        mTextControl.setSize(300, 200); // Set a default size for the Text control
        mTextControl.setMaximum(1000); // Example maximum for scrollbar
}
//<End of snippet n. 0>