/*WindowManagerService reports ANR when target app died during dispatching event.

Reset internal state of WindowManagerService when target app died.
That will allow new events to be dispatched.

Change-Id:I6d5a56d6a537f6fe99320ab04b3b639084b8514f*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 2bcf4cd..d2662ec 100644

//Synthetic comment -- @@ -5128,6 +5128,7 @@
} catch (android.os.RemoteException e) {
Log.i(TAG, "WINDOW DIED during motion dispatch: " + target);
mKeyWaiter.mMotionTarget = null;
            mKeyWaiter.forceFinishCurrentKey();
try {
removeWindow(target.mSession, target.mClient);
} catch (java.util.NoSuchElementException ex) {
//Synthetic comment -- @@ -5199,6 +5200,7 @@
return INJECT_SUCCEEDED;
} catch (android.os.RemoteException e) {
Log.i(TAG, "WINDOW DIED during key dispatch: " + focus);
            mKeyWaiter.forceFinishCurrentKey();
try {
removeWindow(focus.mSession, focus.mClient);
} catch (java.util.NoSuchElementException ex) {
//Synthetic comment -- @@ -5269,6 +5271,7 @@
return INJECT_SUCCEEDED;
} catch (android.os.RemoteException e) {
Log.i(TAG, "WINDOW DIED during key dispatch: " + focus);
            mKeyWaiter.forceFinishCurrentKey();
try {
removeWindow(focus.mSession, focus.mClient);
} catch (java.util.NoSuchElementException ex) {
//Synthetic comment -- @@ -6178,6 +6181,12 @@
mFinished = true;
notifyAll();
}

        void forceFinishCurrentKey() {
            synchronized(this) {
                doFinishedKeyLocked(false);
            }
        }
}

private class KeyQ extends KeyInputQueue







