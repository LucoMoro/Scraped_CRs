/*CursorToBulkCursorAdapter.close must call mCursor.close instead of mCursor.deactivate. This prevent us to call Cursor.close on cross process ContentProvider and may cause a database leak problem.

Change-Id:I126457c1b709e853727f460095b518b0420aa34f*/
//Synthetic comment -- diff --git a/core/java/android/database/CursorToBulkCursorAdaptor.java b/core/java/android/database/CursorToBulkCursorAdaptor.java
//Synthetic comment -- index 19ad946..05f8014 100644

//Synthetic comment -- @@ -143,8 +143,7 @@

public void close() {
maybeUnregisterObserverProxy();
        mCursor.deactivate();       
        
}

public int requery(IContentObserver observer, CursorWindow window) {







