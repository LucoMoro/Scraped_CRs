//<Beginning of snippet n. 0>
private static final Pattern sSkippingPattern =
        Pattern.compile("\\(skipping (.+?) (.+?) '(.*?)'\\)"); //$NON-NLS-1$

private static final Set<String> benignFileTypes = Set.of(".git", "layout.xml~", ".svg", ".png", ".jpg", ".jpeg", ".txt", ".xml", ".json", ".css", ".js");

m = sSkippingPattern.matcher(p);
if (m.matches()) {
    String fileType = m.group(2);
    String location = m.group(3);

    if (!benignFileTypes.contains(fileType)) {
        checkAndMark(location, null, p.trim(), osRoot, project);
    } else {
        logBenignFileSkip(location, fileType);
    }
} else {
    logInvalidFile(p);
}
//<End of snippet n. 0>