<<Beginning of snippet n. 0>>
public void run() {
    ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
    MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
    if (item == null || mContentUri == null) {
        // Notify user that images cannot be displayed
        notifyUser("No images available.");
        return;
    }
    mGridLayer.deselectAll();
}
<<End of snippet n. 0>>