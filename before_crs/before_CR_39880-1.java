/*ddms fileservice: update characters that need escaping.

Add ampersand to the list of characters that need to be escaped.
Fix forhttp://code.google.com/p/android/issues/detail?id=34138Change-Id:I48bdece34d41b713b3f61211528faaedc9a2b40f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 8cfac8f..8d804d2 100644

//Synthetic comment -- @@ -100,9 +100,11 @@
* Represents an entry in a directory. This can be a file or a directory.
*/
public final static class FileEntry {
        /** Pattern to escape filenames for shell command consumption. */
private final static Pattern sEscapePattern = Pattern.compile(
                "([\\\\()*+?\"'#/\\s])"); //$NON-NLS-1$

/**
* Comparator object for FileEntry







