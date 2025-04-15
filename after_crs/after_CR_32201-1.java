/*SystemServer: Looper's getMainLooper to return ServerThread

Fix to ensure Context.getMainLooper and Looper.getMainLooper
returns ServerThread for system_server. Currently, Looper's
getMainLooper returns null.

Change-Id:I5324a1cc00e27a79ba97836a9377a584e6f76b15*/




//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
//Synthetic comment -- index 3ae62ad..ce133f0 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_SYSTEM_RUN,
SystemClock.uptimeMillis());

        Looper.prepareMainLooper();

android.os.Process.setThreadPriority(
android.os.Process.THREAD_PRIORITY_FOREGROUND);







