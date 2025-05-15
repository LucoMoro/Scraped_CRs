//<Beginning of snippet n. 0>
@Override
public void createControl(Composite parent) {
    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    mTextControl = new Text(scrolledComposite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
    mTextControl.setEditable(false);
    scrolledComposite.setContent(mTextControl);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setExpandHorizontal(true);
    mTextControl.setSize(400, 300); // Set control size to ensure scroll bars appear
    scrolledComposite.setSize(400, 300); // Adjust the size of the scrolled composite
}
//<End of snippet n. 0>