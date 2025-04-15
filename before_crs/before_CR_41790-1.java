/*ActivityThread: Avoid creating duplicate handlers and reuse mH

sMainThreadHandler is used for notification of changes in
SharedPreferences and refers to a new instance of Handler instead
of the existing instance, mH, of the ActivityThread. Fix to reuse
existing handlers associated with main looper.

Change-Id:I794a5802a9eacdb188bd0619db5e70e3ae89a07d*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 7242029..b7e0683a 100644

//Synthetic comment -- @@ -4728,13 +4728,14 @@
Process.setArgV0("<pre-initialized>");

Looper.prepareMainLooper();
        if (sMainThreadHandler == null) {
            sMainThreadHandler = new Handler();
        }

ActivityThread thread = new ActivityThread();
thread.attach(false);

AsyncTask.init();

if (false) {







