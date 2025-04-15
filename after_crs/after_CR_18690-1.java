/*In two places involving locking, reordered the code so that the lock
acquisition is performed outside of the `try` block and everything
else that needs to run while the lock is locked *within* the `try`
block.

Change-Id:I3dad2c4bbf60b219fc6db2aa35e2ed296cb39128*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteCursor.java b/core/java/android/database/sqlite/SQLiteCursor.java
//Synthetic comment -- index c7e58faf..380ae8d4 100644

//Synthetic comment -- @@ -131,11 +131,11 @@
// the cursor's state doesn't change
while (true) {
mLock.lock();
try {
                    if (mCursorState != mThreadState) {
                        break;
                    }
                    
int count = mQuery.fillWindow(cw, mMaxRead, mCount);
// return -1 means not finished
if (count != 0) {
//Synthetic comment -- @@ -217,9 +217,8 @@
mColumnNameMap = null;
mQuery = query;

        db.lock();
try {
// Setup the list of columns
int columnCount = mQuery.columnCountLocked();
mColumns = new String[columnCount];







