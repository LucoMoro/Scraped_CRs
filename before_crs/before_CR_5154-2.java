/*fix (more) style issues from change #5124*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index aa8bdbb..172b3f2 100644

//Synthetic comment -- @@ -906,7 +906,8 @@
return false;
}

        if(mContext.checkCallingPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
throw new SecurityException("Requires ACCESS_FINE_LOCATION permission");
}








