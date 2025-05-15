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
    sr.underlineStyle = SWT.UNDERLINE_LINK; // Ensure compatibility with SWT 3.4
    sr.underline = true;
    styledText.setStyleRange(sr);
}

private class ErrorLabelListener extends MouseAdapter {
    @Override
    public void mouseDown(MouseEvent e) {
        Point pt = new Point(e.x, e.y);
        int offset = mErrorLabel.getOffsetAtLocation(pt);
        if (offset >= 0) {
            StyleRange r = mErrorLabel.getStyleRangeAt(offset);
            if (r != null && r.underlineStyle == SWT.UNDERLINE_LINK) {
                String link = mErrorLabel.getText(r.start, r.start + r.length - 1);
                if (isValidClassName(link)) {
                    createNewClass(link);
                } else {
                    // Handle invalid class name scenario
                }
            }
        }
    }

    private boolean isValidClassName(String className) {
        // Implement validation logic for class names
        return className != null && !className.trim().isEmpty();
    }
}