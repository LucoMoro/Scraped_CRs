/*Wrap error text

Wide lines in the error label for rendering errors were not
wrapped. Annoying when you're trying to copy & paste a path out of it
to examine the file in question, etc.

Change-Id:I29b5a8cb25e26dbf8e5a5314bb82c400352e6760*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 21e21ca..b58346f 100644

//Synthetic comment -- @@ -416,7 +416,7 @@

mCanvasViewer = new LayoutCanvasViewer(mLayoutEditor, mRulesEngine, mSashError, SWT.NONE);

        mErrorLabel = new StyledText(mSashError, SWT.READ_ONLY);
mErrorLabel.setEditable(false);
mErrorLabel.setBackground(d.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
mErrorLabel.setForeground(d.getSystemColor(SWT.COLOR_INFO_FOREGROUND));







