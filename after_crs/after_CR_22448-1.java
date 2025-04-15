/*Fixed the issue that the same album gets duplicated in Gallery3D

If we take a photo in camera and then go directly into Gallery
album view by touching the folder icon twice (located in the upper
left corner of image review screen), the same "Camera" Album is
being displayed twice.

In this case, the addMediaSet() is called twice with the same setid
and datasource, which refers to the same "Camera" album. We avoid
the same album being added into mMediaSets.

Change-Id:I19fe227c6a036de262825ba11bbbf13c25271471Signed-off-by: madan ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaFeed.java b/src/com/cooliris/media/MediaFeed.java
//Synthetic comment -- index c190ef9..d99c08f 100644

//Synthetic comment -- @@ -191,6 +191,18 @@
}

public MediaSet addMediaSet(final long setId, DataSource dataSource) {
        int numSets = mMediaSets.size();
        for (int i = 0; i < numSets; i++) {
            MediaSet set = mMediaSets.get(i);
            if ((set.mId == setId) && (set.mDataSource == dataSource)) {
                // The mediaset already exists, but might be out-dated.
                // To avoid the same mediaset being added twice, we delete
                // the old one first, then add the new one below.
                mMediaSets.remove(i);
                break;
            }
        }

MediaSet mediaSet = new MediaSet(dataSource);
mediaSet.mId = setId;
mMediaSets.add(mediaSet);







