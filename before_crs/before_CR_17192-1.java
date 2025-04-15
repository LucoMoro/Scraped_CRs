/*Make PlaylistBrowserActivity handle missing playlist extras.

Some applications on Market dont properly set up the playlist
extras for ACTION_VIEW. This is a problem that was observed
in the wild.

Adding a test for missing extras to avoid crashing Music.

Change-Id:Icfc45f656adf61dc0855f551d32f4156eb669971*/
//Synthetic comment -- diff --git a/src/com/android/music/PlaylistBrowserActivity.java b/src/com/android/music/PlaylistBrowserActivity.java
//Synthetic comment -- index 8c79e64..31a4cbe 100644

//Synthetic comment -- @@ -98,18 +98,23 @@
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
public void onServiceConnected(ComponentName classname, IBinder obj) {
if (Intent.ACTION_VIEW.equals(action)) {
                    long id = Long.parseLong(intent.getExtras().getString("playlist"));
                    if (id == RECENTLY_ADDED_PLAYLIST) {
                        playRecentlyAdded();
                    } else if (id == PODCASTS_PLAYLIST) {
                        playPodcasts();
                    } else if (id == ALL_SONGS_PLAYLIST) {
                        long [] list = MusicUtils.getAllSongs(PlaylistBrowserActivity.this);
                        if (list != null) {
                            MusicUtils.playAll(PlaylistBrowserActivity.this, list, 0);
                        }
} else {
                        MusicUtils.playPlaylist(PlaylistBrowserActivity.this, id);
}
finish();
return;







