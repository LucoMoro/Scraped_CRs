/*fix bug about view cache in RemoteViews

clear the data in cache before reloading new items,
& the method 'getCount()' returns tmpMetaData.count.

Change-Id:I0d37d7a99408f60b1d63fa5ca0c0a0f265dd0a8a*/
//Synthetic comment -- diff --git a/core/java/android/widget/RemoteViewsAdapter.java b/core/java/android/widget/RemoteViewsAdapter.java
//Synthetic comment -- index f0109ce..9d853f5 100644

//Synthetic comment -- @@ -1002,9 +1002,9 @@
}

public int getCount() {
        final RemoteViewsMetaData metaData = mCache.getMetaData();
        synchronized (metaData) {
            return metaData.count;
}
}

//Synthetic comment -- @@ -1186,6 +1186,11 @@
return;
}

// Flush the cache so that we can reload new items from the service
synchronized (mCache) {
mCache.reset();







