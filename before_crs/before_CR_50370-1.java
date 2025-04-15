/*SystemUI: Fix static wallpaper redraw on orientation change

On orientation change mRedrawNeeded is not getting set, which
in turn not allowing static wallpaper redraw. This is forcing
rotation to happen at each composition cycle and low FPS at
static wallpaper.
Make mRedrawNeeded true on orientation change, so that
rotated frame is rendered and rotation is avoided at
each composition cycle.

Change-Id:I23a5992eb45654f83258c00e1bef65edb71b9e3f*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java b/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java
//Synthetic comment -- index 9da883a..c464d7d 100644

//Synthetic comment -- @@ -269,6 +269,8 @@
mYOffset = yOffset;
mOffsetsChanged = true;
}
drawFrameLocked();
}
}







