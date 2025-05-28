//<Beginning of snippet n. 0>
}
++ctr;
} while (ctr < numSets);
final byte[] albumData = sAlbumCache.get(ALBUM_CACHE_METADATA_INDEX, 0);
if (albumData != null && albumData.length > 0) {
    final DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(albumData), 256));
    try {
        final int numAlbums = dis.readInt();
        boolean hasValidAlbums = false;
        for (int i = 0; i < numAlbums; ++i) {
            final long setId = dis.readLong();
            Utils.readUTF(dis);
            dis.readBoolean();
            dis.readBoolean();
            if (setIds.contains(setId)) {
                hasValidAlbums = true;
            } else {
                markDirty();
                break;
            }
        }
        if (!hasValidAlbums && numSets == 0) {
            markDirty();
        }
    } catch (Exception e) {
        Logger.error("Error processing album data: " + e.getMessage(), e);
        resetCache();
    }
}
if (numSets == 0) {
    markDirty();
}
} finally {
//<End of snippet n. 0>