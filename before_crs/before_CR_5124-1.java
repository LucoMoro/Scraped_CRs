/*Enforce permission for using addGpsStatusListener. Even though it's currently hidden and inaccessible from the SDK, it's still
possible for an Android app to use, so it needs to check perms.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 1bf60d4b..aa8bdbb 100644

//Synthetic comment -- @@ -905,6 +905,11 @@
if (mGpsLocationProvider == null) {
return false;
}
try {
mGpsLocationProvider.addGpsStatusListener(listener);
} catch (RemoteException e) {







