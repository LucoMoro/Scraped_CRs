/*PackageManager: Restrict system packages to protected storage

Don't allow packages signed with the platform key to be
installed into user (or app) controlled storage

Change-Id:I37b72d982fd7536c327061788fc785f7cad3ce16*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 8c74566..4c9ee90 100644

//Synthetic comment -- @@ -2874,6 +2874,20 @@
return null;
}

// Initialize package source and resource directories
File destCodeFile = new File(pkg.applicationInfo.sourceDir);
File destResourceFile = new File(pkg.applicationInfo.publicSourceDir);







