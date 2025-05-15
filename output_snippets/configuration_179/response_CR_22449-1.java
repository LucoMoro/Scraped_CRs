//<Beginning of snippet n. 0>
}
++ctr;
} while (ctr < numSets);

final byte[] albumData = sAlbumCache.get(ALBUM_CACHE_METADATA_INDEX, 0);
if (albumData != null && albumData.length > 0) {
    final DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(albumData), 256));
    try {
        final int numAlbums = dis.readInt();
        if (numAlbums == 0) {
            markDirty();
        } else {
            for (int i = 0; i < numAlbums; ++i) {
                final long setId = dis.readLong();
                Utils.readUTF(dis);
                dis.readBoolean();
                dis.readBoolean();
                if (!setIds.contains(setId)) {
                    markDirty();
                    break;
                }
            }
        }
    } catch (Exception e) {
        Log.e("AlbumCacheError", "Error processing album data", e);
    }
}
} finally {
//<End of snippet n. 0>