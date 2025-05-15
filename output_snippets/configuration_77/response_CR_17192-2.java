//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (Intent.ACTION_VIEW.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String playlistString = extras.getString("playlist");
                if (playlistString != null && !playlistString.isEmpty()) {
                    try {
                        long id = Long.parseLong(playlistString);
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
                        // Log error for invalid playlist id
                    }
                } else {
                    // Log missing playlist key
                }
            } else {
                // Log extras are null
            }
        }
        finish();
        return;
    }
//<End of snippet n. 0>