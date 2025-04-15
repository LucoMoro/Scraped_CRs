/*Enable FileListingService to properly traverse symlinks-to-directories

Bug: 7048633
Change-Id:I85d4780286a663ab75555049f21b7215e45870d0*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index d731a4d..97c57b9 100644

//Synthetic comment -- @@ -88,8 +88,13 @@
/**
* Regexp pattern to parse the result from ls.
*/
    private static Pattern sLsPattern = Pattern.compile(
        "^([bcdlsp-][-r][-w][-xsS][-r][-w][-xsS][-r][-w][-xstST])\\s+(\\S+)\\s+(\\S+)\\s+([\\d\\s,]*)\\s+(\\d{4}-\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.*)$"); //$NON-NLS-1$

private Device mDevice;
private FileEntry mRoot;
//Synthetic comment -- @@ -261,13 +266,20 @@
}

/**
         * Returns true if the entry is a directory, false otherwise;
*/
public int getType() {
return type;
}

/**
* Returns if the entry is a folder or a link to a folder.
*/
public boolean isDirectory() {
//Synthetic comment -- @@ -404,7 +416,7 @@
}
}

    private class LsReceiver extends MultiLineReceiver {

private ArrayList<FileEntry> mEntryList;
private ArrayList<String> mLinkList;
//Synthetic comment -- @@ -438,7 +450,7 @@
}

// run the line through the regexp
                Matcher m = sLsPattern.matcher(line);
if (m.matches() == false) {
continue;
}
//Synthetic comment -- @@ -567,8 +579,50 @@
return false;
}

        public void finishLinks() {
            // TODO Handle links in the listing service
}
}

//Synthetic comment -- @@ -773,6 +827,12 @@
try {
// create the command
String command = "ls -l " + entry.getFullEscapedPath(); //$NON-NLS-1$

// create the receiver object that will parse the result from ls
LsReceiver receiver = new LsReceiver(entry, entryList, linkList);
//Synthetic comment -- @@ -781,7 +841,7 @@
mDevice.executeShellCommand(command, receiver);

// finish the process of the receiver to handle links
            receiver.finishLinks();
} finally {
// at this point we need to refresh the viewer
entry.fetchTime = System.currentTimeMillis();







