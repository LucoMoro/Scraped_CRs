/*Fix for concurrency problem accessing a CursorWindow from two threads.

If two threads are concurrently calling SQLiteCursor.fillWindow(),
nothing prevents the second thread from resetting the CursorWindow
while it is still being accessed by the first, causing a crash in
the native layers.
Fixed by taking the database lock in SQLiteCursor.fillWindow() instead
of waiting until SQLiteQuery.fillWindow().

Change-Id:I725b4dd0e5b041c9acdcd673a2c0407bc131f064*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteCursor.java b/core/java/android/database/sqlite/SQLiteCursor.java
//Synthetic comment -- index 70b9b83..19c1eec 100644

//Synthetic comment -- @@ -272,20 +272,30 @@
}

private void fillWindow (int startPos) {
        if (mWindow != null) {
            // The queryThreadLock must be taken before the database lock,
            // to avoid deadlock with QueryThread
mCursorState++;
            queryThreadLock();
}
        mDatabase.lock(); // Database lock must be taken to avoid calling mWindow.clear() on a window being
                          // filled in another thread.
        try {
            if (mWindow == null) {
                // If there isn't a window set already it will only be accessed locally
                mWindow = new CursorWindow(true /* the window is local only */);
            } else {
                    try {
                        mWindow.clear();
                    } finally {
                        queryThreadUnlock();
                    }
            }
            mWindow.setStartPosition(startPos);
            mCount = mQuery.fillWindow(mWindow, mInitialRead, 0);
        } finally {
            mDatabase.unlock();
        }
// return -1 means not finished
if (mCount == NO_COUNT){
mCount = startPos + mInitialRead;







