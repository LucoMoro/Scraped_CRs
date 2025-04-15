/*Fix for issue 8543

The database operation can fail when target is an URI to a content
provider that is not the default Media provider.

In this case item is NULL and cannot be assigned an id

Change-Id:Id912fb7b934964701c0a5518fe8951c3b6bc0a95*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/LocalDataSource.java b/src/com/cooliris/media/LocalDataSource.java
//Synthetic comment -- index 7006ba3..54d1d00 100644

//Synthetic comment -- @@ -393,6 +393,7 @@
if (cursor.moveToFirst()) {
item = new MediaItem();
CacheService.populateMediaItemFromCursor(item, cr, cursor, uri.toString() + "/");
                    item.mId = id;
}
cursor.close();
cursor = null;
//Synthetic comment -- @@ -401,7 +402,6 @@
// If the database operation failed for any reason.
;
}
return item;
}








