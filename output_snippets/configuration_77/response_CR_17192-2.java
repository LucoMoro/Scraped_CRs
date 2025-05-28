//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (intent != null && Intent.ACTION_VIEW.equals(action)) {
            if (intent.hasExtra("playlist")) {
                String playlistExtra = intent.getStringExtra("playlist");
                try {
                    long id = Long.parseLong(playlistExtra);
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
                    Toast.makeText(PlaylistBrowserActivity.this, "Invalid playlist ID", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PlaylistBrowserActivity.this, "Missing playlist extra", Toast.LENGTH_SHORT).show();
                MusicUtils.playPlaylist(PlaylistBrowserActivity.this, DEFAULT_PLAYLIST_ID);
            }
        } else {
            Toast.makeText(PlaylistBrowserActivity.this, "Missing or null intent", Toast.LENGTH_SHORT).show();
        }
        finish();
        return;
    }
});
//<End of snippet n. 0>