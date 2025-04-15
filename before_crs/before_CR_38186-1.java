/*Format unknown API level a bit better in the new project wizard.

Change-Id:Ib4608c92d38f32fd2418d5c2442fc10d1b678a0b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 37b569d..c7c8cd8 100644

//Synthetic comment -- @@ -817,7 +817,8 @@
if (target.isPlatform()) {
AndroidVersion version = target.getVersion();
if (version.getApiLevel() == api) {
                                return version.getApiString();
}
}
}







