/*Change judgement

My device is SetTopBox, so it cannot allow blank area.
But if lancher change the wallpaper size with suggestDeriredDimensions,
and User change lancher to another one, the wallpaper size cannot reset to
original size. So my device retern maxmum wallpaper size to suggestDesiredDimensions.

Change-Id:I09241ae71fe87343059b046d5c15178fc7b06aa6*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/WallpaperManagerTest.java b/tests/tests/app/src/android/app/cts/WallpaperManagerTest.java
//Synthetic comment -- index 62e00e9..5036a5a 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
mWallpaperManager.suggestDesiredDimensions(320, 480);
int desiredMinimumWidth = mWallpaperManager.getDesiredMinimumWidth();
int desiredMinimumHeight = mWallpaperManager.getDesiredMinimumHeight();
        assertTrue(320 <= desiredMinimumWidth);
        assertTrue(480 <= desiredMinimumHeight);
}
}







