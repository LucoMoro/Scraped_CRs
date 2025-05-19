//<Beginning of snippet n. 0>

mCanvasViewer = new LayoutCanvasViewer(mLayoutEditor, mRulesEngine, mSashError, SWT.NONE);

mErrorLabel = new StyledText(mSashError, SWT.READ_ONLY);
mErrorLabel.setEditable(false);
mErrorLabel.setBackground(d.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
mErrorLabel.setForeground(d.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
mErrorLabel.setWordWrap(true);

//<End of snippet n. 0>