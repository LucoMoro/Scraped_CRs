/*'uses-library' was not working for persistent applications.
Fix by generating the applicationInfo, when asked for info.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index a83459e..f5c7c53 100644

//Synthetic comment -- @@ -1838,7 +1838,7 @@
&& (p.applicationInfo.flags&ApplicationInfo.FLAG_PERSISTENT) != 0
&& (!mSafeMode || (p.applicationInfo.flags
&ApplicationInfo.FLAG_SYSTEM) != 0)) {
                    finalList.add(PackageParser.generateApplicationInfo(p, flags));
}
}
}







