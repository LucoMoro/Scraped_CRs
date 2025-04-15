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
            addClassLink(mErrorLabel, clazz);
addText(mErrorLabel, "\n");
}

//Synthetic comment -- @@ -1348,24 +1348,32 @@
* A mouse-click listener is setup and it interprets the link as being a missing class name.
* The logic *must* be changed if this is used later for a different purpose.
*/
    private void addClassLink(StyledText styledText, String link) {
String s = styledText.getText();
int start = (s == null ? 0 : s.length());
styledText.append(link);

        StyleRange sr = new ClassLinkStyleRange();
sr.start = start;
sr.length = link.length();
sr.fontStyle = SWT.NORMAL;
        // We want to use SWT.UNDERLINE_LINK but the constant is only
        // available when using SWT from Eclipse 3.5+
        int version = SWT.getVersion();
        if (version > 3500) {
            sr.underlineStyle = 4 /*SWT.UNDERLINE_LINK*/;
        }
sr.underline = true;
styledText.setStyleRange(sr);
}

    /** This StyleRange represents a missing class link that the user can click */
    private static class ClassLinkStyleRange extends StyleRange {}

/**
* Monitor clicks on the error label.
* If the click happens on a style range created by
     * {@link GraphicalEditorPart#addClassLink(StyledText, String)}, we assume it's about
* a missing class and we then proceed to display the standard Eclipse class creator wizard.
*/
private class ErrorLabelListener extends MouseAdapter {
//Synthetic comment -- @@ -1391,7 +1399,7 @@
}
}

            if (r instanceof ClassLinkStyleRange) {
String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
createNewClass(link);
}







