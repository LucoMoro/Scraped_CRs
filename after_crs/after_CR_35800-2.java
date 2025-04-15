/*AsyncTask: Fix to fail attempts to create AsyncTasks from non-main(UI) threads

Change-Id:I79e9f5f16fb19484b3d927a5892cee863d724ed0*/




//Synthetic comment -- diff --git a/core/java/android/os/AsyncTask.java b/core/java/android/os/AsyncTask.java
//Synthetic comment -- index 9dea4c4..0491aa6 100644

//Synthetic comment -- @@ -605,6 +605,14 @@
}

private static class InternalHandler extends Handler {

        InternalHandler() {
            if (getLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("Only the main "
                          + "UI thread can create AsyncTasks");
            }
        }

@SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
@Override
public void handleMessage(Message msg) {








//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
//Synthetic comment -- index 3ae62ad..ce133f0 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_SYSTEM_RUN,
SystemClock.uptimeMillis());

        Looper.prepareMainLooper();

android.os.Process.setThreadPriority(
android.os.Process.THREAD_PRIORITY_FOREGROUND);







