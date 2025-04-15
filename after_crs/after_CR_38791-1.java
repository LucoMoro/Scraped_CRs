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
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("restriction") // for AdtPlugin internal classes
public class NdkCommandLauncher extends CommandLauncher {
private static CygPath sCygPath = null;

    private static final List<String> WINDOWS_NATIVE_EXECUTABLES = Arrays.asList(
            "exe",      //$NON-NLS-1$
            "cmd",      //$NON-NLS-1$
            "bat"       //$NON-NLS-1$
            );

static {
if (Platform.OS_WIN32.equals(Platform.getOS())) {
//Synthetic comment -- @@ -66,10 +73,8 @@
}

if (isWindowsExecutable(commandPath)) {
                // append necessary extension
                commandPath = appendExecutableExtension(commandPath);
} else {
// Invoke using Cygwin shell if this is not a native windows executable
String[] newargs = new String[args.length + 1];
//Synthetic comment -- @@ -85,15 +90,44 @@
}

private boolean isWindowsExecutable(IPath commandPath) {
        String ext = commandPath.getFileExtension();
        if (isWindowsExecutableExtension(ext)) {
return true;
}

        ext = findWindowsExecutableExtension(commandPath);
        if (ext != null) {
return true;
}

return false;
}

    private IPath appendExecutableExtension(IPath commandPath) {
        if (isWindowsExecutableExtension(commandPath.getFileExtension())) {
            return commandPath;
        }

        String ext = findWindowsExecutableExtension(commandPath);
        if (ext != null) {
            return commandPath.addFileExtension(ext);
        }

        return commandPath;
    }

    private String findWindowsExecutableExtension(IPath command) {
        for (String e: WINDOWS_NATIVE_EXECUTABLES) {
            File exeFile = command.addFileExtension(e).toFile();
            if (exeFile.exists()) {
                return e;
            }
        }

        return null;
    }

    private boolean isWindowsExecutableExtension(String extension) {
        return extension != null && WINDOWS_NATIVE_EXECUTABLES.contains(extension);
    }
}







