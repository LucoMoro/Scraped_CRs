/*'uses-library' was not working for persistent applications.
Fix by generating the applicationInfo, when asked for info.

Change-Id:I44686d5a306562c6649148dce8f709e682adcdf4*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index 86504a0..cc78300 100644

//Synthetic comment -- @@ -1861,7 +1861,7 @@
&& (p.applicationInfo.flags&ApplicationInfo.FLAG_PERSISTENT) != 0
&& (!mSafeMode || (p.applicationInfo.flags
&ApplicationInfo.FLAG_SYSTEM) != 0)) {
                    finalList.add(p.applicationInfo);
}
}
}







