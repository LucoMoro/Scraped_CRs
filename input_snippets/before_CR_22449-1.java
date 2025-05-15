
//<Beginning of snippet n. 0>


}
++ctr;
} while (ctr < numSets);
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
// This set was deleted, we need to recompute the cache.
markDirty();
                                    break;
}
}
                        } catch (Exception e) {
                            ;
}
}
}
} finally {

//<End of snippet n. 0>








