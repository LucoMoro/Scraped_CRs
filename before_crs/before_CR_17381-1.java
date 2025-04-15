/*Apps/Music: Add onDestroy function to release Jni Object references

 Add onDestory function for Video browser activity to release
 the resources which cause memory leaks.

Change-Id:I3450cd875ef6d3acba7887ac2772112f4ff43cab*/
//Synthetic comment -- diff --git a/src/com/android/music/VideoBrowserActivity.java b/src/com/android/music/VideoBrowserActivity.java
//Synthetic comment -- index e8aaf74..e366ce6 100644

//Synthetic comment -- @@ -105,6 +105,15 @@
}
}

private Cursor mCursor;
private String mWhereClause;
private String mSortOrder;







