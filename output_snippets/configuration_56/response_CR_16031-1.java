//<Beginning of snippet n. 0>
addText(mErrorLabel, "The following classes could not be found:\n");
for (String clazz : missingClasses) {
    addText(mErrorLabel, "- ");
    addLink(mErrorLabel, clazz);
    addText(mErrorLabel, "\n");
}

/* A mouse-click listener is setup and it interprets the link as being a missing class name.
 * The logic *must* be changed if this is used later for a different purpose.
 */
private void addLink(StyledText styledText, String link) {
    String s = styledText.getText();
    int start = (s == null ? 0 : s.length());
    styledText.append(link);

    StyleRange sr = new StyleRange();
    sr.start = start;
    sr.length = link.length();
    sr.fontStyle = SWT.NORMAL; // Assuming we can still use SWT.NORMAL as it is
    sr.underlineStyle = 1; // Using 1 for underline style as a workaround
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
    public void mouseDown(MouseEvent e) {
        Point point = new Point(e.x, e.y);
        int offset = mErrorLabel.getCaretOffset();
        StyleRange r = mErrorLabel.getStyleRangeAtOffset(offset);

        if (r != null && r.underline) { // Checking for underline instead of using SWT.UNDERLINE_LINK
            String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
            createNewClass(link);
        }
    }
}
//<End of snippet n. 0>