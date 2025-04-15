/*frameworks/base: Scale wallpaper image to fit the screen size

This change scales up/down the wallpaper image to fit the device screen
resolution maintaining the aspect ratio

Change-Id:I0a2819854dde8e712825f887945ae519cc280c79*/




//Synthetic comment -- diff --git a/core/java/android/app/WallpaperManager.java b/core/java/android/app/WallpaperManager.java
//Synthetic comment -- index 92b7cf51..a4cc01f 100644

//Synthetic comment -- @@ -713,25 +713,26 @@
targetRect.left = targetRect.top = 0;
targetRect.right = bm.getWidth();
targetRect.bottom = bm.getHeight();

        int deltaw = 0;
        int deltah = 0;

        if(targetRect.right > 0 && targetRect.bottom > 0) {
// We need to scale up so it covers the entire
// area.
            float xScale = width / (float)targetRect.right;
            float yScale = height / (float)targetRect.bottom;
            if(xScale > yScale) {
                targetRect.right = (int)((float)targetRect.right * xScale);
                targetRect.bottom = (int)((float)targetRect.bottom * xScale);
} else {
                targetRect.right = (int)((float)targetRect.right * yScale);
                targetRect.bottom = (int)((float)targetRect.bottom * yScale);
}
deltaw = width - targetRect.right;
deltah = height - targetRect.bottom;
}

targetRect.offset(deltaw/2, deltah/2);
Paint paint = new Paint();
paint.setFilterBitmap(true);







