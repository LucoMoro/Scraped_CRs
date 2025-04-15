/*Force closed at settings application when delete Misc files

NullPointerException in MiscFilesHandler.java, missing check to
ensure that dir.list() does not return null.

Change-Id:I13aac19949826b385610fd1fee2befb411684023*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/MiscFilesHandler.java b/src/com/android/settings/deviceinfo/MiscFilesHandler.java
//Synthetic comment -- index 1e0cc46..93e352b 100644

//Synthetic comment -- @@ -144,8 +144,8 @@
// Returns true if all deletions were successful.
// If a deletion fails, the method stops attempting to delete and returns false.
private boolean deleteDir(File dir) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
for (int i=0; i < children.length; i++) {
boolean success = deleteDir(new File(dir, children[i]));
if (!success) {
//Synthetic comment -- @@ -283,4 +283,4 @@
return view;
}
}
}
\ No newline at end of file







