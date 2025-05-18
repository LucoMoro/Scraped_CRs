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
    sr.underline = true; // Removed SWT.UNDERLINE_LINK for compatibility with SWT 3.4
    styledText.setStyleRange(sr);
}

private class ErrorLabelListener extends MouseAdapter {
    @Override
    public void mouseDown(MouseEvent e) {
        Point p = new Point(e.x, e.y);
        int offset = mErrorLabel.getOffsetAtLocation(p);
        StyleRange[] styles = mErrorLabel.getStyleRanges();
        for (StyleRange style : styles) {
            if (offset >= style.start && offset < (style.start + style.length)) {
                String link = mErrorLabel.getText(style.start, style.start + style.length - 1);
                createNewClass(link);
                break;
            }
        }
    }
}

//<End of snippet n. 0>