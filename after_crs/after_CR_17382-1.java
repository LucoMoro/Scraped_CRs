/*Apps/Music: Add onDestroy function to release Jni Object references

Add onDestory function for Video browser activity to release
the resources which cause memory leaks.

Change-Id:I6e5150c5a8a21cb0c4cece83bf1fefd3f351aa5a*/




//Synthetic comment -- diff --git a/src/com/android/music/VideoBrowserActivity.java b/src/com/android/music/VideoBrowserActivity.java
//Synthetic comment -- index e8aaf74..e366ce6 100644

//Synthetic comment -- @@ -105,6 +105,15 @@
}
}

    @Override
    public void onDestroy()
    {
        if (mCursor != null) {
            mCursor.close();
        }
        super.onDestroy();
    }

private Cursor mCursor;
private String mWhereClause;
private String mSortOrder;







