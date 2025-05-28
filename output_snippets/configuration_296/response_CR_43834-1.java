//<Beginning of snippet n. 0>
@Override
public void createControl(Composite parent) {
    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    mTextControl = new Text(scrolledComposite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
    mTextControl.setEditable(false);
    mTextControl.setSize(400, 200); // Ensuring sufficient size for scrolling

    scrolledComposite.setContent(mTextControl);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setMinSize(mTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    mTextControl.addModifyListener(e -> {
        scrolledComposite.setMinSize(mTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
    });
    
    // Trigger the scrollbar to update when the contents change
    mTextControl.setText("Initial content");
    mTextControl.addListener(SWT.Modify, event -> {
        scrolledComposite.setMinSize(mTextControl.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
    });
}
//<End of snippet n. 0>