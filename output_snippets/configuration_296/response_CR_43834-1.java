//<Beginning of snippet n. 0>
@Override
public void createControl(Composite parent) {
    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    mTextControl = new Text(scrolledComposite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
    mTextControl.setEditable(false);
    mTextControl.setText("Initial content"); // Set initial content first
    mTextControl.setSize(400, 200); // Size to require scrolling

    scrolledComposite.setContent(mTextControl);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setMinSize(mTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    mTextControl.addModifyListener(e -> {
        scrolledComposite.setMinSize(mTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
    });
}
//<End of snippet n. 0>