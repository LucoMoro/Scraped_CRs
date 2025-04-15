/*Format unknown API level a bit better in the new project wizard.

(cherry picked from commit f0512725a6925778be3b5282efdaf0b8c53069a5)

Change-Id:I9eead906442715484c0c4bcfa6d3834afe8fd2cd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 37b569d..c7c8cd8 100644

//Synthetic comment -- @@ -817,7 +817,8 @@
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
                                return String.format("API %1$d: Android %2$s", api,
                                        target.getProperty("ro.build.version.release"));
}
}
}







