/*restorecon /data/anr directory.

Restore the security contexts of anr directory
when initially created.

Change-Id:Ia731414ccbcdc7369d24be6db0003c53abcf6ef4Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..73deef0 100644

//Synthetic comment -- @@ -106,6 +106,7 @@
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
//Synthetic comment -- @@ -3032,7 +3033,12 @@
File tracesFile = new File(tracesPath);
try {
File tracesDir = tracesFile.getParentFile();
            if (!tracesDir.exists()) {
                tracesFile.mkdirs();
                if (!SELinux.restorecon(tracesDir)) {
                    return null;
                }
            }
FileUtils.setPermissions(tracesDir.getPath(), 0775, -1, -1);  // drwxrwxr-x

if (clearTraces && tracesFile.exists()) tracesFile.delete();
//Synthetic comment -- @@ -3136,7 +3142,12 @@
final File tracesDir = tracesFile.getParentFile();
final File tracesTmp = new File(tracesDir, "__tmp__");
try {
                if (!tracesDir.exists()) {
                    tracesFile.mkdirs();
                    if (!SELinux.restorecon(tracesDir.getPath())) {
                        return;
                    }
                }
FileUtils.setPermissions(tracesDir.getPath(), 0775, -1, -1);  // drwxrwxr-x

if (tracesFile.exists()) {








//Synthetic comment -- diff --git a/services/java/com/android/server/am/DeviceMonitor.java b/services/java/com/android/server/am/DeviceMonitor.java
//Synthetic comment -- index 5f3b0ce..21e7252 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.server.am;

import android.os.SELinux;
import android.util.Slog;

import java.io.*;
//Synthetic comment -- @@ -80,6 +81,9 @@
if (!BASE.isDirectory() && !BASE.mkdirs()) {
throw new AssertionError("Couldn't create " + BASE + ".");
}
        if (!SELinux.restorecon(BASE)) {
            throw new AssertionError("Couldn't restorecon " + BASE + ".");
        }
}

private static final File[] PATHS = {







