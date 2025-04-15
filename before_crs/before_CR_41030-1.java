/*NPE in WallpaperHandler

In WallpaperHandler, BitmapFactory.decodeStream() is used to create
a Bitmap that is then set by WallpaperManager as a wallpaper.
This method has null as return value when something fails as
decoding, resulting in a nullpointer exception in WallpaperManager.
Adding nullcheck on the Bitmap before setting it as wallpaper to
avoid the NPE.

Change-Id:Ib1a0f9331898162b21094c3ebe80bc21d66f8bd7*/
//Synthetic comment -- diff --git a/src/com/android/browser/WallpaperHandler.java b/src/com/android/browser/WallpaperHandler.java
//Synthetic comment -- index b76861c..0c60664 100644

//Synthetic comment -- @@ -130,7 +130,12 @@
}
Bitmap scaledWallpaper = BitmapFactory.decodeStream(inputstream,
null, options);
                wm.setBitmap(scaledWallpaper);
}
} catch (IOException e) {
Log.e(LOGTAG, "Unable to set new wallpaper");







