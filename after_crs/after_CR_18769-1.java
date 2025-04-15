/*Collect signatures when calling PackageManager.getPackageArchiveInfo with flag GET_SIGNATURES*/




//Synthetic comment -- diff --git a/core/java/android/content/pm/PackageManager.java b/core/java/android/content/pm/PackageManager.java
//Synthetic comment -- index 68b44e7..fb78924 100644

//Synthetic comment -- @@ -1733,6 +1733,8 @@
if (pkg == null) {
return null;
}
        if ((flags & GET_SIGNATURES) != 0)
            packageParser.collectCertificates(pkg, 0);
return PackageParser.generatePackageInfo(pkg, null, flags);
}








