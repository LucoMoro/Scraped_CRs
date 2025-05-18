//<Beginning of snippet n. 0>
mToken = MusicUtils.bindToService(this, new ServiceConnection() {
    public void onServiceConnected(ComponentName classname, IBinder obj) {
        if (intent == null) {
            Toast.makeText(PlaylistBrowserActivity.this, "Missing or null intent", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent.hasExtra("playlist")) {
                String playlistExtra = intent.getStringExtra("playlist");
                try {
                    long id = Long.parseLong(playlistExtra);
                    if (isValidPlaylist(id)) {
                        playSelectedPlaylist(id);
                    } else {
                        Toast.makeText(PlaylistBrowserActivity.this, "Unrecognized playlist ID, using default playlist", Toast.LENGTH_SHORT).show();
                        MusicUtils.playPlaylist(PlaylistBrowserActivity.this, DEFAULT_PLAYLIST_ID);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(PlaylistBrowserActivity.this, "Invalid playlist ID, using default playlist", Toast.LENGTH_SHORT).show();
                    MusicUtils.playPlaylist(PlaylistBrowserActivity.this, DEFAULT_PLAYLIST_ID);
                }
            } else {
                Toast.makeText(PlaylistBrowserActivity.this, "Missing playlist extra, using default playlist", Toast.LENGTH_SHORT).show();
                MusicUtils.playPlaylist(PlaylistBrowserActivity.this, DEFAULT_PLAYLIST_ID);
            }
        }
        finish();
        return;
    }

    private boolean isValidPlaylist(long id) {
        return id == RECENTLY_ADDED_PLAYLIST || id == PODCASTS_PLAYLIST || id == ALL_SONGS_PLAYLIST || MusicUtils.isPlaylistExists(id);
    }

    private void playSelectedPlaylist(long id) {
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