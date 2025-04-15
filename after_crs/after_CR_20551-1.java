/*frameworks/base: release references of UriPermissionOwner

The list of references to UriPermissionOwner kept track in
writeOwners isn't cleared when requested by ActivityManagerService.
Fix is to check and clear writeOwners.

Change-Id:I18202444c7d4fcb84ef889b5b6773f2afc39950b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/UriPermission.java b/services/java/com/android/server/am/UriPermission.java
//Synthetic comment -- index e3347cb..c5b1c7b 100644

//Synthetic comment -- @@ -59,11 +59,11 @@
if ((modeFlagsToClear&Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
globalModeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
modeFlags &= ~Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            if (writeOwners.size() > 0) {
for (UriPermissionOwner r : writeOwners) {
r.removeWritePermission(this);
}
                writeOwners.clear();
}
}
}







