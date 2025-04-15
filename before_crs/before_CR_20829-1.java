/*Fix "select all" problem in the gallery apps.

When pressing a "select all" menu in the gallery, several images isn't selcted correctly.

Change-Id:Ibf35b2ef2e413a8fe6b559d471af3a6de1c85b60Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/Gallery.java b/src/com/cooliris/media/Gallery.java
//Synthetic comment -- index bfa2575..f2f0cd9 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Images;
import android.util.Log;
//Synthetic comment -- @@ -173,6 +174,10 @@
sendInitialMessage();
}

@Override
public void onRestart() {
super.onRestart();








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 74aff5d..abce32f 100644

//Synthetic comment -- @@ -1002,7 +1002,7 @@
mFeedAboutToChange = false;
mFeedChanged = true;
if (feed != null) {
            if (mState == STATE_GRID_VIEW || mState == STATE_FULL_SCREEN)
mHud.setFeed(feed, mState, needsLayout);
}
if (mView != null) {
//Synthetic comment -- @@ -1186,6 +1186,31 @@
}
}

void addSlotToSelectedItems(int slotId, boolean removeIfAlreadyAdded, boolean updateCount) {
// mMediaFeed may be null because setDataSource() may not be called yet.
if (mFeedAboutToChange == false && mMediaFeed != null) {








//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaFeed.java b/src/com/cooliris/media/MediaFeed.java
//Synthetic comment -- index c190ef9..664682b 100644

//Synthetic comment -- @@ -244,6 +244,7 @@
public void performOperation(final int operation, final ArrayList<MediaBucket> mediaBuckets, final Object data) {
int numBuckets = mediaBuckets.size();
final ArrayList<MediaBucket> copyMediaBuckets = new ArrayList<MediaBucket>(numBuckets);
for (int i = 0; i < numBuckets; ++i) {
copyMediaBuckets.add(mediaBuckets.get(i));
}
//Synthetic comment -- @@ -255,6 +256,7 @@
ArrayList<MediaBucket> mediaBuckets = copyMediaBuckets;
if (operation == OPERATION_DELETE) {
int numBuckets = mediaBuckets.size();
for (int i = 0; i < numBuckets; ++i) {
MediaBucket bucket = mediaBuckets.get(i);
MediaSet set = bucket.mediaSet;
//Synthetic comment -- @@ -275,17 +277,26 @@
clustering.removeItemFromClustering(item);
}
}
                            set.updateNumExpectedItems();
                            set.generateTitle(true);
}
}
                    updateListener(true);
                    mMediaFeedNeedsToRun = true;
if (mDataSource != null) {
mDataSource.performOperation(OPERATION_DELETE, mediaBuckets, null);
}
} else {
                    mDataSource.performOperation(operation, mediaBuckets, data);
}
}
});
//Synthetic comment -- @@ -879,10 +890,22 @@

public MediaSet replaceMediaSet(long setId, DataSource dataSource) {
Log.i(TAG, "Replacing media set " + setId);
        final MediaSet set = getMediaSet(setId);
        if (set != null)
            set.refresh();
        return set;
}

public void setSingleImageMode(boolean singleImageMode) {







