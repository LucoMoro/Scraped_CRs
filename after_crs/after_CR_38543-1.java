/*ndk: Add missing null check

Change-Id:I5052717f3fc00979c0da1e5eab501fa24fe21684*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java
//Synthetic comment -- index 505e3d8..8ad1f24 100644

//Synthetic comment -- @@ -144,12 +144,14 @@
}

File[] platforms = prebuilt.toFile().listFiles();
        if (platforms != null) {
            for (File p: platforms) {
                IPath exePath = prebuilt.append(p.getName())
                        .append("bin")          //$NON-NLS-1$
                        .append(executable);
                if (exePath.toFile().exists()) {
                    return exePath;
                }
}
}








