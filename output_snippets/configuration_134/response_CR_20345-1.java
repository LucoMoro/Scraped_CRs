//<Beginning of snippet n. 0>

mCanvasViewer = new LayoutCanvasViewer(mLayoutEditor, mRulesEngine, mSashError, SWT.NONE);

mErrorLabel = new StyledText(mSashError, SWT.READ_ONLY | SWT.WRAP);
mErrorLabel.setEditable(false);
mErrorLabel.setBackground(d.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
mErrorLabel.setForeground(d.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
mErrorLabel.setWordWrap(true);
mErrorLabel.setSize(SWT.DEFAULT, 100); // Assuming 100 is the desired max height for readability

//<End of snippet n. 0>