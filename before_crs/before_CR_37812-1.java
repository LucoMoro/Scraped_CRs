/*Fix cursor memory leak

In current code, if an application opens a cursor to access a
provider, and doesn't close that cursor, later, when this cursor
is garbage collected, it won't get closed. This will cause a memory
leak in the provider. The leaked memory can only be reclaimed when
the application with the leaked cursor was dead.

The solution is, close the cursor when it's garbage collected.

Change-Id:I786915c46d4672b6b1b37414b3bc1ff8cea2e00b*/
//Synthetic comment -- diff --git a/core/java/android/database/AbstractCursor.java b/core/java/android/database/AbstractCursor.java
//Synthetic comment -- index 74fef29..ac3e5970 100644

//Synthetic comment -- @@ -406,6 +406,9 @@
if (mSelfObserver != null && mSelfObserverRegistered == true) {
mContentResolver.unregisterContentObserver(mSelfObserver);
}
}

/**







