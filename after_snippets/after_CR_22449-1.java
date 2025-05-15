
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>








