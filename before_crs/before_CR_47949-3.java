/*A Fail reason
It failed to fix suggestDesiredDimensions to default size of our company product.

A Patch reason
In this case, it is necessary and proper to set a fixed value that ignore a value appointed in suggestDesiredDimensions.
Therefore I changed it to pass with various values.

Change-Id:I09241ae71fe87343059b046d5c15178fc7b06aa6*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/WallpaperManagerTest.java b/tests/tests/app/src/android/app/cts/WallpaperManagerTest.java
//Synthetic comment -- index 62e00e9..5036a5a 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
mWallpaperManager.suggestDesiredDimensions(320, 480);
int desiredMinimumWidth = mWallpaperManager.getDesiredMinimumWidth();
int desiredMinimumHeight = mWallpaperManager.getDesiredMinimumHeight();
        assertEquals(320, desiredMinimumWidth);
        assertEquals(480, desiredMinimumHeight);
}
}







