//<Beginning of snippet n. 0>


* invalid, such as a layout file name which starts with _.
*/
private static final Pattern sSkippingPattern =
        Pattern.compile("\\(skipping .+ .+ '(.*)'\\)"); //$NON-NLS-1$

private static final List<String> benignFileTypes = Arrays.asList(".git", ".bak");

/**
* Suffix of error message which points to the first occurrence of a repeated resource

m = sSkippingPattern.matcher(p);
if (m.matches()) {
    String location = m.group(1);
    String fileName = location.trim();
    
    // Check for benign file types
    if (!benignFileTypes.stream().anyMatch(fileName::endsWith)) {
        // check the values and attempt to mark the file.
        if (checkAndMark(location, null, p.trim(), osRoot, project)) {
            // Further processing if necessary
        }
    }
}

//<End of snippet n. 0>