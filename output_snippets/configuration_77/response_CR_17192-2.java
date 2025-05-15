//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent.hasExtra("playlist")) {
                try {
                    long id = Long.parseLong(intent.getExtras().getString("playlist"));
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
                    // Handle invalid playlist ID gracefully, possibly log the error or show a message to the user
                }
            } else {
                // Handle case when "playlist" extra is missing, e.g., show a default message or navigate to a default playlist
            }
            finish();
        }
    }
});
//<End of snippet n. 0>