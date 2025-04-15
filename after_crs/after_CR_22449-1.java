/*Fixed the issue that media sets still appear after deleting all albums in Gallery3D.

If we delete all media sets in Gallery3D by "Selecte All" and
"Delete" (basically, making Gallery3D empty through one shot
delete), next time we enter Gallery3D we can see delted albums
are still there.

The cause of this issue is when the number of media sets becomes
zero (e.g., after one shot delete), the original logic skips checking
for any deleted sets. We should check if there is any change in media
sets (including the case it is totally gone), regardless of the current
number of media sets

Change-Id:I9bc45fb13925b5ca9176d88fcfead0690c69ba63Signed-off-by: madan ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/cache/CacheService.java b/src/com/cooliris/cache/CacheService.java
//Synthetic comment -- index c8a5fc0..6f68913 100644

//Synthetic comment -- @@ -1011,26 +1011,33 @@
}
++ctr;
} while (ctr < numSets);
                }
                // We now check for any deleted sets.
                final byte[] albumData = sAlbumCache.get(ALBUM_CACHE_METADATA_INDEX, 0);
                if (albumData != null && albumData.length > 0) {
                    final DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(albumData), 256));
                    try {
                        final int numAlbums = dis.readInt();
                        for (int i = 0; i < numAlbums; ++i) {
                            final long setId = dis.readLong();
                            Utils.readUTF(dis);
                            dis.readBoolean();
                            dis.readBoolean();
                            if (!setIds.contains(setId)) {
                                final byte[] existingData = sAlbumCache.get(ALBUM_CACHE_DIRTY_INDEX, 0);
                                if (existingData != null && existingData.length == 1
                                    && existingData[0] == sDummyData[0]) {
                                    // markDirty() was already called, skip this time.
                                    // (not do it too aggressively)
                                } else {
// This set was deleted, we need to recompute the cache.
markDirty();
}
                                break;
}
}
                    } catch (Exception e) {
                        ;
}
}
} finally {







