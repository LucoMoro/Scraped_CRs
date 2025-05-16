//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (Intent.ACTION_VIEW.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras == null || !extras.containsKey("playlist")) {
                // Handle missing "playlist" extra (e.g., redirect to default playlist or show a message)
                MusicUtils.playDefaultPlaylist(PlaylistBrowserActivity.this);
                finish();
                return;
            }
            String playlistStr = extras.getString("playlist");
            if (playlistStr == null || playlistStr.isEmpty()) {
                // Handle invalid playlist ID (e.g., redirect to default playlist or show a message)
                MusicUtils.playDefaultPlaylist(PlaylistBrowserActivity.this);
                finish();
                return;
            }
            long id;
            try {
                id = Long.parseLong(playlistStr);
            } catch (NumberFormatException e) {
                // Log error and handle invalid number format
                MusicUtils.playDefaultPlaylist(PlaylistBrowserActivity.this);
                finish();
                return;
            }
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
            finish();
        }
    }
//<End of snippet n. 0>