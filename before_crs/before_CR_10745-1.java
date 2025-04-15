/*Fixed issue where when a package in a SharedUserId was deleted, it would remove overlapping permissions of packages with the same SharedUserId.

Instead of scanning grantedPermissions for a package (which don't exist with SharedUserId), it instead scans the requested permissions for a package.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 1f3ec2b..5a8d3df 100644

//Synthetic comment -- @@ -5608,7 +5608,7 @@
continue;
}
for (PackageSetting pkg:sus.packages) {
                    if (pkg.grantedPermissions.contains (eachPerm)) {
used = true;
break;
}







