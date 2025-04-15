/*Fix intermittent deadlocks during boot, observed on a hardware platform.

Since the variable read is atomic, the only effect of the
synchronized() call would be to block the calling thread until the
holder of the lock releases it, and it is possible that some code path
might depend on this side-effect.  But testing using the cupcake
branch running on hardware and emulator (by many users) showed no
problems with the change.  Review by an expert in the code may
nevertheless be in order.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index d4c27b7..809cb08 100644

//Synthetic comment -- @@ -5574,9 +5574,7 @@
}

public boolean getInTouchMode() {
            synchronized(mWindowMap) {
return mInTouchMode;
            }
}

public boolean performHapticFeedback(IWindow window, int effectId,







