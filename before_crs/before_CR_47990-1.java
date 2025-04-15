/*Fix bugs regarding StaleDataException during requerying a already closed cursor in CursorTreeAdapter.

When CursorTreeAdapter is changed, a related cursor is requeried.
But it might try to requery a already closed cursor, because there is no defence code for that.

This patch contains the defence code to check if the cursor is already closed.

[written by jangwon.lee@lge.com]

Change-Id:Ib9071f20792319c012f71654e5fc6f174930be09*/
//Synthetic comment -- diff --git a/core/java/android/widget/CursorTreeAdapter.java b/core/java/android/widget/CursorTreeAdapter.java
//Synthetic comment -- index 44d1656..405e45a 100644

//Synthetic comment -- @@ -497,7 +497,7 @@

@Override
public void onChange(boolean selfChange) {
                if (mAutoRequery && mCursor != null) {
if (false) Log.v("Cursor", "Auto requerying " + mCursor +
" due to update");
mDataValid = mCursor.requery();







