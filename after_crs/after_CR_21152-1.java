/*Fix in FileListingService.

Using getFullEscapedPath() instead of getFullPath() in doLs().

Change-Id:Ib4a1178354872398daa42777a0f05aa3e246e8f6*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index bfeeea2..4cf0056 100644

//Synthetic comment -- @@ -745,7 +745,7 @@

try {
// create the command
            String command = "ls -l " + entry.getFullEscapedPath(); //$NON-NLS-1$

// create the receiver object that will parse the result from ls
LsReceiver receiver = new LsReceiver(entry, entryList, linkList);







