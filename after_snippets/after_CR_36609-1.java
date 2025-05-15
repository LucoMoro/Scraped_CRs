
//<Beginning of snippet n. 0>


* invalid, such as a layout file name which starts with _.
*/
private static final Pattern sSkippingPattern =
        Pattern.compile("    \\(skipping (.+) .+ '(.*)'\\)"); //$NON-NLS-1$

/**
* Suffix of error message which points to the first occurrence of a repeated resource

m = sSkippingPattern.matcher(p);
if (m.matches()) {
                String location = m.group(2);

                // Certain files can safely be skipped without marking the project
                // as having errors. See isHidden() in AaptAssets.cpp:
                String type = m.group(1);
                if (type.equals("backup")          //$NON-NLS-1$   // main.xml~, etc
                        || type.equals("hidden")   //$NON-NLS-1$   // .gitignore, etc
                        || type.equals("index")) { //$NON-NLS-1$   // thumbs.db, etc
                    continue;
                }

// check the values and attempt to mark the file.
if (checkAndMark(location, null, p.trim(), osRoot, project,

//<End of snippet n. 0>








