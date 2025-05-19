//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (intent == null) {
            Toast.makeText(PlaylistBrowserActivity.this, "Intent is null.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (Intent.ACTION_VIEW.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras == null || !extras.containsKey("playlist")) {
                Toast.makeText(PlaylistBrowserActivity.this, "Playlist could not be loaded: missing extra.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String playlistIdStr = extras.getString("playlist");
            if (playlistIdStr == null || playlistIdStr.isEmpty()) {
                Toast.makeText(PlaylistBrowserActivity.this, "Playlist could not be loaded: empty ID.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            try {
                long id = Long.parseLong(playlistIdStr);
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
                Toast.makeText(PlaylistBrowserActivity.this, "Invalid playlist ID.", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
//<End of snippet n. 0>