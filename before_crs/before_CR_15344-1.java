/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I8a6b3217a20c071bf93a82bcaa8a0792e47a8111*/
//Synthetic comment -- diff --git a/tests/src/com/android/music/MusicPlayerNames.java b/tests/src/com/android/music/MusicPlayerNames.java
//Synthetic comment -- index f5f5d48..655d578 100644

//Synthetic comment -- @@ -16,34 +16,36 @@

package com.android.music.tests;

/**
 * 
 * This class has the names of the all the activity name and variables 
* in the instrumentation test.
*
*/
public class MusicPlayerNames {
  
//Expected result of the sorted playlistname
public static final String expectedPlaylistTitle[] = { "**1E?:|}{[]~~.,;'",
"//><..", "0123456789",
"0random@112", "MyPlaylist", "UPPERLETTER",
"combination011", "loooooooog",
        "normal", "~!@#$%^&*()_+"    
    }; 
  
//Unsorted input playlist name
public static final String unsortedPlaylistTitle[] = { "//><..","MyPlaylist",
        "0random@112", "UPPERLETTER","normal", 
"combination011", "0123456789",
"~!@#$%^&*()_+","**1E?:|}{[]~~.,;'",
        "loooooooog"    
};
    
public static final String DELETE_PLAYLIST_NAME = "testDeletPlaylist";
public static final String ORIGINAL_PLAYLIST_NAME = "original_playlist_name";
public static final String RENAMED_PLAYLIST_NAME = "rename_playlist_name";
    
public static int NO_OF_PLAYLIST = 10;
public static int WAIT_SHORT_TIME = 1000;
public static int WAIT_LONG_TIME = 2000;
//Synthetic comment -- @@ -52,10 +54,12 @@
public static int DEFAULT_PLAYLIST_LENGTH = 15;
public static int NO_ALBUMS_TOBE_PLAYED = 50;
public static int NO_SKIPPING_SONGS = 500;
    
    public static final String DELETESONG = "/sdcard/toBeDeleted.amr"; 
    public static final String GOLDENSONG = "/sdcard/media_api/music/AMRNB.amr";
    public static final String TOBEDELETESONGNAME = "toBeDeleted";   
    
public static int EXPECTED_NO_RINGTONE = 1;
}







