/*list all files (ls -a) in ddms file explorer

Currently ddms file explorer does not display files whose name begins with a "." and other hidden files.
The file explorer would be more useful if I could view hidden files and directories.
The fix is to have ddms file explorer invoke the ls command with the -a switch.
Main assumption is that there is not a compelling reason to hide these files/folders in DDMS.
Since this feature provides the same information as is available from adb shell this seems unlikely.

Change-Id:I260eb456166fdac44ce188dd283ebde45f1dce40Signed-off-by: Carlos Rendon <crendon@gmail.com>*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 8cfac8f..95d37f9 100644

//Synthetic comment -- @@ -756,7 +756,7 @@

try {
// create the command
            String command = "ls -a -l " + entry.getFullEscapedPath(); //$NON-NLS-1$

// create the receiver object that will parse the result from ls
LsReceiver receiver = new LsReceiver(entry, entryList, linkList);







