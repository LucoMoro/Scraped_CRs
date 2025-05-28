
//<Beginning of snippet n. 0>


* invalid, such as a layout file name which starts with _.
*/
private static final Pattern sSkippingPattern =
        Pattern.compile("    \\(skipping .+ .+ '(.*)'\\)"); //$NON-NLS-1$

/**
* Suffix of error message which points to the first occurrence of a repeated resource

m = sSkippingPattern.matcher(p);
if (m.matches()) {
                String location = m.group(1);

// check the values and attempt to mark the file.
if (checkAndMark(location, null, p.trim(), osRoot, project,

//<End of snippet n. 0>








