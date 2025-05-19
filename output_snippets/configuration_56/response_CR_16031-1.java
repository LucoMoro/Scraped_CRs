//<Beginning of snippet n. 0>
addText(mErrorLabel, "The following classes could not be found:\n");
for (String clazz : missingClasses) {
    addText(mErrorLabel, "- ");
    addLink(mErrorLabel, clazz);
    addText(mErrorLabel, "\n");
}

private void addLink(StyledText styledText, String link) {
    String s = styledText.getText();
    int start = (s == null ? 0 : s.length());
    styledText.append(link);

    StyleRange sr = new StyleRange();
    sr.start = start;
    sr.length = link.length();
    sr.fontStyle = SWT.NORMAL;
    sr.underlineStyle = SWT.UNDERLINE_SINGLE; // Updated for SWT 3.4 compatibility
    sr.underline = true;
    styledText.setStyleRange(sr);
}

private class ErrorLabelListener extends MouseAdapter {
    public void mouseDown(MouseEvent e) {
        Point pt = new Point(e.x, e.y);
        int offset = mErrorLabel.getOffsetAtLocation(pt);
        StyleRange r = mErrorLabel.getStyleRangeAtOffset(offset);
        
        if (r != null && r.underlineStyle == SWT.UNDERLINE_SINGLE) { // Updated for SWT 3.4 compatibility
            String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
            createNewClass(link);
        }
    }
}
//<End of snippet n. 0>