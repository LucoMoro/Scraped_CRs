/*ADT GLE2: fix, don't use constant from SWT 3.5.00

The build server still builds using 3.4 and we
still support 3.4 at runtime.

Change-Id:I8f6ac24e77eef1daac68ee7f595fb66654762d6b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 7ecd0fa..2daaae5 100755

//Synthetic comment -- @@ -1328,7 +1328,7 @@
addText(mErrorLabel, "The following classes could not be found:\n");
for (String clazz : missingClasses) {
addText(mErrorLabel, "- ");
            addLink(mErrorLabel, clazz);
addText(mErrorLabel, "\n");
}

//Synthetic comment -- @@ -1348,24 +1348,32 @@
* A mouse-click listener is setup and it interprets the link as being a missing class name.
* The logic *must* be changed if this is used later for a different purpose.
*/
    private void addLink(StyledText styledText, String link) {
String s = styledText.getText();
int start = (s == null ? 0 : s.length());
styledText.append(link);

        StyleRange sr = new StyleRange();
sr.start = start;
sr.length = link.length();
sr.fontStyle = SWT.NORMAL;
        sr.underlineStyle = SWT.UNDERLINE_LINK;
sr.underline = true;
styledText.setStyleRange(sr);
}

/**
* Monitor clicks on the error label.
* If the click happens on a style range created by
     * {@link GraphicalEditorPart#addLink(StyledText, String)}, we assume it's about
* a missing class and we then proceed to display the standard Eclipse class creator wizard.
*/
private class ErrorLabelListener extends MouseAdapter {
//Synthetic comment -- @@ -1391,7 +1399,7 @@
}
}

            if (r != null && r.underlineStyle == SWT.UNDERLINE_LINK) {
String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
createNewClass(link);
}







