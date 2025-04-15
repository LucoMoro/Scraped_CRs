/*ndk: Add missing null check

(cherry picked from commit 25ce20a8beb7fd38a93b056f86bdb4d2278318e1)

Change-Id:I3463a28eb58adbb3ba0122bf7cdcac000d7e4b0a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java
//Synthetic comment -- index 505e3d8..8ad1f24 100644

//Synthetic comment -- @@ -144,12 +144,14 @@
}

File[] platforms = prebuilt.toFile().listFiles();
        for (File p: platforms) {
            IPath exePath = prebuilt.append(p.getName())
                                    .append("bin")          //$NON-NLS-1$
                                    .append(executable);
            if (exePath.toFile().exists()) {
                return exePath;
}
}








