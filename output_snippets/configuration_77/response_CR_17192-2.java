//<Beginning of snippet n. 0>

mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String playlistIdStr = extras.getString("playlist");
                    if (playlistIdStr != null && !playlistIdStr.isEmpty()) {
                        try {
                            long id = Long.parseLong(playlistIdStr);
                            handlePlaylist(id);
                        } catch (NumberFormatException e) {
                            MusicUtils.playPlaylist(PlaylistBrowserActivity.this, ALL_SONGS_PLAYLIST);
                        }
                    } else {
                        MusicUtils.playPlaylist(PlaylistBrowserActivity.this, ALL_SONGS_PLAYLIST);
                    }
                } else {
                    MusicUtils.playPlaylist(PlaylistBrowserActivity.this, ALL_SONGS_PLAYLIST);
                }
            } else {
                MusicUtils.playPlaylist(PlaylistBrowserActivity.this, ALL_SONGS_PLAYLIST);
            }
            finish();
        }
    }

    private void handlePlaylist(long id) {
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
    }
});

//<End of snippet n. 0>