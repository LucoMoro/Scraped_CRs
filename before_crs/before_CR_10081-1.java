/*AI 148824: Fix issue #1851541 (sharedUserId requests not being checked for matching signatures on .apk).
  To address issues where we could leave half-installed applications around if there was a failure during the install, we split getPackageLP() into two phases: the first would create the record, and only after doing all verification checks would we call insertPackageSettingLP() to do the remaining part of the original implementation and actually insert the record into the data structures.
  Unfortunately it was only in insertPackageSettingLP() that we would set the sharedUser field of the PackageSetting structure, so when before that we went to verify certificates, we didn't think it had requested a shared user ID, and let it through without checking.
  This fix simply sets the sharedUser field when the PackageSetting structure is first created, so we will actually check against its certs.  We still also set this again in insertPackageSettingLP(), but there is no harm in this because the only time we call this function is in that big install func, which just passes in the same shared user that it had when first getting the package.
  BUG=1851541

Automated import of CL 148824*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 9e062f4..1f3ec2b 100644

//Synthetic comment -- @@ -5543,6 +5543,7 @@
}
p = new PackageSetting(name, codePath, resourcePath, pkgFlags);
p.setTimeStamp(codePath.lastModified());
if (sharedUser != null) {
p.userId = sharedUser.userId;
} else if (MULTIPLE_APPLICATION_UIDS) {







