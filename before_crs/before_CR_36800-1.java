/*frameworks/base: release references of UriPermissionOwner

Change-Id:I72e2310458de15f18e6f2c67f383bbb5c8f60ae2*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/UriPermission.java b/services/java/com/android/server/am/UriPermission.java
//Synthetic comment -- index e3347cb..c5b1c7b 100644

//Synthetic comment -- @@ -59,11 +59,11 @@
if ((modeFlagsToClear&Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
globalModeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
modeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            if (readOwners.size() > 0) {
for (UriPermissionOwner r : writeOwners) {
r.removeWritePermission(this);
}
                readOwners.clear();
}
}
}







