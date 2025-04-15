/*Fix that InputDeviceReaderThread that got killed during startup

InputDeviceReaderThread could be killed if a key or touch event
was received before initiation made by PolicyThread was made. To
solve this, the start call for the InputDeviceReader thread was
delayed until initalization of the PolicyThread was done in
the WindowManagerService.

Change-Id:Ifa7de7ccfadd66ecc2b14c6273e9be32b8e0cb4a*/




//Synthetic comment -- diff --git a/services/java/com/android/server/KeyInputQueue.java b/services/java/com/android/server/KeyInputQueue.java
//Synthetic comment -- index 1bb897b..e05c800 100644

//Synthetic comment -- @@ -299,7 +299,9 @@
mLast = new QueuedEvent();
mFirst.next = mLast;
mLast.prev = mFirst;
    }

    void start() {
mThread.start();
}









//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index b90b03b..4bad5b4 100644

//Synthetic comment -- @@ -621,6 +621,7 @@
}

mInputThread.start();
        mQueue.start();

// Add ourself to the Watchdog monitors.
Watchdog.getInstance().addMonitor(this);







