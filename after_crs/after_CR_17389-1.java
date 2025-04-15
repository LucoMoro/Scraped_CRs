/*Include debugger connection status in error entry

Include the debugger connection status when adding error entry
to DropBox if debugger is connected, "Debugger: Connected".

This can be useful to sort out crashes comming from developers
vs from regular usage.

Change-Id:Ic309066c63778af1577f2b91a95ffca0bd40338c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 804af9c..f5a8119 100644

//Synthetic comment -- @@ -9252,6 +9252,9 @@
sb.append("Subject: ").append(subject).append("\n");
}
sb.append("Build: ").append(Build.FINGERPRINT).append("\n");
        if (Debug.isDebuggerConnected()) {
            sb.append("Debugger: Connected\n");
        }
sb.append("\n");

// Do the rest in a worker thread to avoid blocking the caller on I/O







