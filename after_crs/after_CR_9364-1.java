/*Improve warning messages for permission denial due to non-exported intent receivers.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 9d720d1..737a3619 100644

//Synthetic comment -- @@ -5254,6 +5254,7 @@
}
// If the target requires a specific UID, always fail for others.
if (reqUid >= 0 && uid != reqUid) {
            Log.w(TAG, "Permission Denial: checkComponentPermission() reqUid=" + reqUid);
return PackageManager.PERMISSION_DENIED;
}
if (permission == null) {







