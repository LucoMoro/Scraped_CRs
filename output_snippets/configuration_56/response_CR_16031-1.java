//<Beginning of snippet n. 0>


addText(mErrorLabel, "The following classes could not be found:\n");
for (String clazz : missingClasses) {
    addText(mErrorLabel, "- ");
    addLink(mErrorLabel, clazz);
    addText(mErrorLabel, "\n");
}

/**
 * Adds a styled link to the StyledText widget.
 */
private void addLink(StyledText styledText, String link) {
    String s = styledText.getText();
    int start = (s == null ? 0 : s.length());
    styledText.append(link);

    StyleRange sr = new StyleRange();
    sr.start = start;
    sr.length = link.length();
    sr.fontStyle = SWT.NORMAL;
    sr.underline = true; // Removed UNDERLINE_LINK to maintain SWT 3.4 compatibility
    styledText.setStyleRange(sr);
}

/**
 * Monitor clicks on the error label.
 * If the click happens on a style range created by
 * {@link GraphicalEditorPart#addLink(StyledText, String)}, we assume it's about
 * a missing class and we then proceed to display the standard Eclipse class creator wizard.
 */
private class ErrorLabelListener extends MouseAdapter {
    @Override
    public void mouseDown(MouseEvent e) {
        int offset = mErrorLabel.getCaretOffset();
        StyleRange[] ranges = mErrorLabel.getStyleRanges();
        
        for (StyleRange range : ranges) {
            if (range.start <= offset && offset < range.start + range.length && range.underline) {
                String link = mErrorLabel.getText(range.start, range.start + range.length - 1);
                createNewClass(link);
                break;
            }
        }
    }
}

//<End of snippet n. 0>