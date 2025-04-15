/*NdkCommandLauncher: find the correct executable extension

Assume that extensions .exe, .bat and .cmd all refer to native
Windows executables. For such executables, do not use "sh"
(Cygwin shell) to launch them.

(cherry picked from commit cd1f0ebda9d518ab0e491b4c97c1c3d7c625eb62)

Change-Id:I705fafef75d521e544fdf39244b15025dfd79917*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/build/NdkCommandLauncher.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/build/NdkCommandLauncher.java
//Synthetic comment -- index 71c635a..0a1f7dc 100644

//Synthetic comment -- @@ -29,11 +29,18 @@

import java.io.File;
import java.io.IOException;

@SuppressWarnings("restriction") // for AdtPlugin internal classes
public class NdkCommandLauncher extends CommandLauncher {
private static CygPath sCygPath = null;
    private static final String WINDOWS_EXE = "exe"; //$NON-NLS-1$

static {
if (Platform.OS_WIN32.equals(Platform.getOS())) {
//Synthetic comment -- @@ -66,10 +73,8 @@
}

if (isWindowsExecutable(commandPath)) {
                // Make sure it has the full file name extension
                if (!WINDOWS_EXE.equalsIgnoreCase(commandPath.getFileExtension())) {
                    commandPath = commandPath.addFileExtension(WINDOWS_EXE);
                }
} else {
// Invoke using Cygwin shell if this is not a native windows executable
String[] newargs = new String[args.length + 1];
//Synthetic comment -- @@ -85,15 +90,44 @@
}

private boolean isWindowsExecutable(IPath commandPath) {
        if (WINDOWS_EXE.equalsIgnoreCase(commandPath.getFileExtension())) {
return true;
}

        File exeFile = commandPath.addFileExtension(WINDOWS_EXE).toFile();
        if (exeFile.exists()) {
return true;
}

return false;
}
}







