/*NDK: Search through folders in prebuilt to locate make.

The assumption that there is only one platform inside NDK/prebuilt
is incorrect. This CL iterates through all the folders to locate
make.

(cherry picked from commit 9dbd469c31f906902fd25c6bf52a42c6f10ce257)

Change-Id:I41007e3b653974435c1b7f0e3650a69ffc890b21*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/NdkHelper.java
//Synthetic comment -- index 8feafde..505e3d8 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -123,26 +124,36 @@
}

private static IPath getPathToMake() {
        return getFullPathTo(MAKE);
}

/**
     * Obtain a path to the utilities prebuilt folder in NDK. This is typically
     * "${NdkRoot}/prebuilt/<platform>/bin/". If the executable is not found, it simply returns
     * the name of the executable (which is equal to assuming that it is available on the path).
*/
    private static synchronized IPath getFullPathTo(String executable) {
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            executable += ".exe";
        }

IPath ndkRoot = new Path(NdkManager.getNdkLocation());
IPath prebuilt = ndkRoot.append("prebuilt");                      //$NON-NLS-1$
if (!prebuilt.toFile().exists() || !prebuilt.toFile().canRead()) {
            return new Path(executable);
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

        return new Path(executable);
}

public static void setLaunchConfigDefaults(ILaunchConfigurationWorkingCopy config) {







