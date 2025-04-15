/*frameworks/base: Scale wallpaper image to fit the screen size

This change scales up/down the wallpaper image to fit the device screen
resolution maintaining the aspect ratio

Change-Id:I0a2819854dde8e712825f887945ae519cc280c79*/
//Synthetic comment -- diff --git a/core/java/android/app/WallpaperManager.java b/core/java/android/app/WallpaperManager.java
//Synthetic comment -- index 92b7cf51..162fc22 100644

//Synthetic comment -- @@ -713,25 +713,26 @@
targetRect.left = targetRect.top = 0;
targetRect.right = bm.getWidth();
targetRect.bottom = bm.getHeight();
        
        int deltaw = width - targetRect.right;
        int deltah = height - targetRect.bottom;
        
        if (deltaw > 0 || deltah > 0) {
// We need to scale up so it covers the entire
// area.
            float scale = 1.0f;
            if (deltaw > deltah) {
                scale = width / (float)targetRect.right;
} else {
                scale = height / (float)targetRect.bottom;
}
            targetRect.right = (int)(targetRect.right*scale);
            targetRect.bottom = (int)(targetRect.bottom*scale);
deltaw = width - targetRect.right;
deltah = height - targetRect.bottom;
}
        
targetRect.offset(deltaw/2, deltah/2);
Paint paint = new Paint();
paint.setFilterBitmap(true);







