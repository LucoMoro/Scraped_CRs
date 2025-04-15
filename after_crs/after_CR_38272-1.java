/*Fix test checking whether a newer support lib is needed in NPW.

Change-Id:If019552c5d543fb09869c18a2e6cf4a32c0e6a4b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/AddCompatibilityJarAction.java
//Synthetic comment -- index ebbd9f0..1d84ad2 100755

//Synthetic comment -- @@ -168,7 +168,7 @@
if (path != null) {
assert path.exists(); // guaranteed by the getCompatJarFile call
int installedRevision = getInstalledRevision();
                if (installedRevision != -1 && minimumRevision <= installedRevision) {
return path;
}
}







