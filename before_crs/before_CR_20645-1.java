/*frameworks/base: Track owner in appropriate owners list of Uri

The owner isn't kept track it URI's writeOwners when
grantUriPermissionUncheckedLocked is invoked to provide both
read and write access to the URI. Fix is to check for both
read and write permissions and add owner to appropriate lists.

Change-Id:Id23688b96aefeb0a4911ee52ad47124bc5904fa0*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 2322ee1..a881bc4 100644

//Synthetic comment -- @@ -4385,12 +4385,15 @@
perm.modeFlags |= modeFlags;
if (owner == null) {
perm.globalModeFlags |= modeFlags;
        } else if ((modeFlags&Intent.FLAG_GRANT_READ_URI_PERMISSION) != 0) {
            perm.readOwners.add(owner);
            owner.addReadPermission(perm);
        } else if ((modeFlags&Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != 0) {
            perm.writeOwners.add(owner);
            owner.addWritePermission(perm);
}
}








