//<Beginning of snippet n. 0>

/**
 * Invalid, such as a layout file name which starts with _.
 */
private static final Pattern sSkippingPattern =
        Pattern.compile("\\(skipping .+ .+ '(.*)'\\)"); //$NON-NLS-1$

private static final Set<String> BENIGN_FILE_TYPES = new HashSet<>(Arrays.asList(".git", "layout.xml~"));

/**
 * Suffix of error message which points to the first occurrence of a repeated resource
 */
m = sSkippingPattern.matcher(p);
if (m.matches()) {
    String location = m.group(1);
    String fileType = location.substring(location.lastIndexOf('.'));

    // Check for benign file types
    if (!BENIGN_FILE_TYPES.contains(fileType)) {
        // Check the values and attempt to mark the file.
        checkAndMark(location, null, p.trim(), osRoot, project);
    }
}

//<End of snippet n. 0>