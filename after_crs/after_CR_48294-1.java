/*Music: fix crash while music playback and the same time transfer music files to DUT through MTP.

root cause: The method checkPosition() in AbstractCursor throws
CursorIndexOutOfBoundsException if cursor position is out of
bounds. Subclass implements of get functions should call this
before attempting to retrieve data. Here all get functions in
MediaPlaybackService doesn't catch the exception.

solution: Check if cursor count is "0" and it won't raise up
exception any more.

Change-Id:I4b3172da11faa904d8602338c2c9c94501f23e50Author: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63040*/




//Synthetic comment -- diff --git a/src/com/android/music/MediaPlaybackService.java b/src/com/android/music/MediaPlaybackService.java
//Synthetic comment -- index 6414eb4..067e1bd 100644

//Synthetic comment -- @@ -1736,7 +1736,7 @@

public String getArtistName() {
synchronized(this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return null;
}
return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//Synthetic comment -- @@ -1745,7 +1745,7 @@

public long getArtistId() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return -1;
}
return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
//Synthetic comment -- @@ -1754,7 +1754,7 @@

public String getAlbumName() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return null;
}
return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
//Synthetic comment -- @@ -1763,7 +1763,7 @@

public long getAlbumId() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return -1;
}
return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
//Synthetic comment -- @@ -1772,7 +1772,7 @@

public String getTrackName() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return null;
}
return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
//Synthetic comment -- @@ -1781,7 +1781,7 @@

private boolean isPodcast() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return false;
}
return (mCursor.getInt(PODCASTCOLIDX) > 0);
//Synthetic comment -- @@ -1790,7 +1790,7 @@

private long getBookmark() {
synchronized (this) {
            if (mCursor == null || mCursor.getCount() == 0) {
return 0;
}
return mCursor.getLong(BOOKMARKCOLIDX);







