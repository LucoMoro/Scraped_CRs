/*Fix test checking whether a newer support lib is needed in NPW.

(cherry picked from commit 9134e737eab8ec3d62ea5440fe632b23996c6a3f)

Change-Id:Ic3be5fbb34b0d0888331fc355caefeaaa3b72226*/




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







