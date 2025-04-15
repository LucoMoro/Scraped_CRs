/*Don't assume that all permissions are displayable

The AppSecurityPermissions widget doesn't display signature level
permissions. If the package being installed only requests signature
level permissions, we end up displaying it unnecessarily (and
confusingly). So rather than testing the length of the list, it's
better to query the widget to find out if any of the permissions are
displayable.

Change-Id:Ib15fca3a8897e66f65f48669e706f956cad391f2*/
//Synthetic comment -- diff --git a/src/com/android/packageinstaller/PackageInstallerActivity.java b/src/com/android/packageinstaller/PackageInstallerActivity.java
//Synthetic comment -- index 1b967ff..00f7936 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
boolean permVisible = false;
if(mPkgInfo != null) {
AppSecurityPermissions asp = new AppSecurityPermissions(this, mPkgInfo);
            if(asp.getPermissionCount() > 0) {
permVisible = true;
securityList.addView(asp.getPermissionsView());
}







