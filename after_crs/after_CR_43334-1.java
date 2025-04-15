/*gltrace: Preserve alpha information

While reading in framebuffer data into an SWT ImageData, the alpha
values have to be set explicitly.

This CL also moves toolbars to the right of filter bar.

Change-Id:I628752382c82ff9df729590459fba09bf4f340b0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java
//Synthetic comment -- index d08b726..8c8600a 100644

//Synthetic comment -- @@ -52,8 +52,13 @@
palette,
1,          // scan line padding
uncompressed);
        byte []alpha = new byte[width*height];
        for (int i = 0; i < width * height; i++) {
            alpha[i] = uncompressed[i * 4 + 3];
        }
        imageData.alphaData = alpha;

        imageData = imageData.scaledTo(imageData.width, -imageData.height);
return imageData;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index 50ee717..fa506e2 100644

//Synthetic comment -- @@ -383,6 +383,41 @@
GridData gd = new GridData(GridData.FILL_HORIZONTAL);
c.setLayoutData(gd);

        Label l = new Label(c, SWT.NONE);
        l.setText("Filter:");

        mFilterText = new Text(c, SWT.BORDER | SWT.ICON_SEARCH | SWT.SEARCH | SWT.ICON_CANCEL);
        mFilterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mFilterText.setMessage(DEFAULT_FILTER_MESSAGE);
        mFilterText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                updateAppliedFilters();
            }
        });

        if (mShowContextSwitcher) {
            mContextSwitchCombo = new Combo(c, SWT.BORDER | SWT.READ_ONLY);

            // Setup the combo such that "All Contexts" is the first item,
            // and then we have an item for each context.
            mContextSwitchCombo.add("All Contexts");
            mContextSwitchCombo.select(0);
            mCurrentlyDisplayedContext = -1; // showing all contexts
            for (int i = 0; i < mTrace.getContexts().size(); i++) {
                mContextSwitchCombo.add("Context " + i);
            }

            mContextSwitchCombo.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectContext(mContextSwitchCombo.getSelectionIndex() - 1);
                }
            });
        } else {
            mCurrentlyDisplayedContext = 0;
        }

ToolBar toolBar = new ToolBar(c, SWT.FLAT | SWT.BORDER);

mExpandAllToolItem = new ToolItem(toolBar, SWT.PUSH);
//Synthetic comment -- @@ -422,41 +457,6 @@
mExpandAllToolItem.addSelectionListener(toolbarSelectionListener);
mCollapseAllToolItem.addSelectionListener(toolbarSelectionListener);
mSaveAsToolItem.addSelectionListener(toolbarSelectionListener);
}

private void updateAppliedFilters() {







