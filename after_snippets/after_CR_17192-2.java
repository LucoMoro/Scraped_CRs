
//<Beginning of snippet n. 0>


mToken = MusicUtils.bindToService(this, new ServiceConnection() {
public void onServiceConnected(ComponentName classname, IBinder obj) {
if (Intent.ACTION_VIEW.equals(action)) {
                    Bundle b = intent.getExtras();
                    if (b == null) {
                        Log.w(TAG, "Unexpected:getExtras() returns null.");
} else {
                        try {
                            long id = Long.parseLong(b.getString("playlist"));
                            if (id == RECENTLY_ADDED_PLAYLIST) {
                                playRecentlyAdded();
                            } else if (id == PODCASTS_PLAYLIST) {
                                playPodcasts();
                            } else if (id == ALL_SONGS_PLAYLIST) {
                                long[] list = MusicUtils.getAllSongs(PlaylistBrowserActivity.this);
                                if (list != null) {
                                    MusicUtils.playAll(PlaylistBrowserActivity.this, list, 0);
                                }
                            } else {
                                MusicUtils.playPlaylist(PlaylistBrowserActivity.this, id);
                            }
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Playlist id missing or broken");
                        }
}
finish();
return;

//<End of snippet n. 0>








