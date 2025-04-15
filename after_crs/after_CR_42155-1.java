/*Enable FileListingService to properly traverse symlinks-to-directories

Bug: 7048633
Change-Id:I85d4780286a663ab75555049f21b7215e45870d0*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index d731a4d..97c57b9 100644

//Synthetic comment -- @@ -88,8 +88,13 @@
/**
* Regexp pattern to parse the result from ls.
*/
    private static final Pattern LS_L_PATTERN = Pattern.compile(
            "^([bcdlsp-][-r][-w][-xsS][-r][-w][-xsS][-r][-w][-xstST])\\s+(\\S+)\\s+(\\S+)\\s+" +
            "([\\d\\s,]*)\\s+(\\d{4}-\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.*)$"); //$NON-NLS-1$

    private static final Pattern LS_LD_PATTERN = Pattern.compile(
                    "d[rwx-]{9}\\s+\\S+\\s+\\S+\\s+[0-9-]{10}\\s+\\d{2}:\\d{2}$"); //$NON-NLS-1$


private Device mDevice;
private FileEntry mRoot;
//Synthetic comment -- @@ -261,13 +266,20 @@
}

/**
         * Returns the Entry type as an int, which will match one of the TYPE_(...) constants
*/
public int getType() {
return type;
}

/**
         * Sets a new type.
         */
        public void setType(int type) {
            this.type = type;
        }

        /**
* Returns if the entry is a folder or a link to a folder.
*/
public boolean isDirectory() {
//Synthetic comment -- @@ -404,7 +416,7 @@
}
}

    private static class LsReceiver extends MultiLineReceiver {

private ArrayList<FileEntry> mEntryList;
private ArrayList<String> mLinkList;
//Synthetic comment -- @@ -438,7 +450,7 @@
}

// run the line through the regexp
                Matcher m = LS_L_PATTERN.matcher(line);
if (m.matches() == false) {
continue;
}
//Synthetic comment -- @@ -567,8 +579,50 @@
return false;
}

        /**
         * Determine if any symlinks in the <code entries> list are links-to-directories, and if so
         * mark them as such.  This allows us to traverse them properly later on.
         */
        public void finishLinks(IDevice device, ArrayList<FileEntry> entries)
                throws TimeoutException, AdbCommandRejectedException,
                ShellCommandUnresponsiveException, IOException {
            final int[] nLines = {0};
            MultiLineReceiver receiver = new MultiLineReceiver() {
                @Override
                public void processNewLines(String[] lines) {
                    for (String line : lines) {
                        Matcher m = LS_LD_PATTERN.matcher(line);
                        if (m.matches()) {
                            nLines[0]++;
                        }
                    }
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }
            };

            for (FileEntry entry : entries) {
                if (entry.getType() != TYPE_LINK) continue;

                // We simply need to determine whether the referent is a directory or not.
                // We do this by running `ls -ld ${link}/`.  If the referent exists and is a
                // directory, we'll see the normal directory listing.  Otherwise, we'll see an
                // error of some sort.
                nLines[0] = 0;

                final String command = String.format("ls -l -d %s%s", entry.getFullEscapedPath(),
                        FILE_SEPARATOR);

                device.executeShellCommand(command, receiver);

                if (nLines[0] > 0) {
                    // We saw lines matching the directory pattern, so it's a directory!
                    entry.setType(TYPE_DIRECTORY_LINK);
                }
            }
}
}

//Synthetic comment -- @@ -773,6 +827,12 @@
try {
// create the command
String command = "ls -l " + entry.getFullEscapedPath(); //$NON-NLS-1$
            if (entry.isDirectory()) {
                // If we expect a file to behave like a directory, we should stick a "/" at the end.
                // This is a good habit, and is mandatory for symlinks-to-directories, which will
                // otherwise behave like symlinks.
                command += FILE_SEPARATOR;
            }

// create the receiver object that will parse the result from ls
LsReceiver receiver = new LsReceiver(entry, entryList, linkList);
//Synthetic comment -- @@ -781,7 +841,7 @@
mDevice.executeShellCommand(command, receiver);

// finish the process of the receiver to handle links
            receiver.finishLinks(mDevice, entryList);
} finally {
// at this point we need to refresh the viewer
entry.fetchTime = System.currentTimeMillis();







