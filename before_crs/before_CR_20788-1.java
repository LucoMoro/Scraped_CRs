/*Use Async Task to prevent an ANR

In the Launcher application, the wallpaper chooser activity uses
the main UI thread to make a binder call to the WallPaper Manager
service. This happens when the user selects a wall paper and sets
it. However, an ANR occurs when the binder call does not respond
in time. Hence, such calls should not be done in the main UI
thread.

Change-Id:I3bcba39f3086b7d1842b4d1c2f1ff1f71786041c*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/WallpaperChooser.java b/src/com/android/launcher2/WallpaperChooser.java
//Synthetic comment -- index bf8ba2e..1a41694 100644

//Synthetic comment -- @@ -139,14 +139,29 @@
}

mIsWallpaperSet = true;
        try {
            WallpaperManager wpm = (WallpaperManager)getSystemService(WALLPAPER_SERVICE);
            wpm.setResource(mImages.get(position));
            setResult(RESULT_OK);
            finish();
        } catch (IOException e) {
            Log.e(TAG, "Failed to set wallpaper: " + e);
        }
}

public void onNothingSelected(AdapterView parent) {







