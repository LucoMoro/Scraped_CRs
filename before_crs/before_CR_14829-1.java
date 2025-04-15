/*Replaced deprecated API with new WallpaperManager

Change-Id:I971da2c0fd1bb4671320f2894c3c0d43c2de90fe*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/Photographs.java b/src/com/cooliris/media/Photographs.java
//Synthetic comment -- index 250fd5b..ded8f8b 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -89,7 +90,7 @@
@Override
public void run() {
try {
                mContext.setWallpaper(mBitmap);
} catch (IOException e) {
Log.e(LOG_TAG, "Failed to set wallpaper.", e);
} finally {








//Synthetic comment -- diff --git a/src/com/cooliris/media/Wallpaper.java b/src/com/cooliris/media/Wallpaper.java
//Synthetic comment -- index 0ab86db..5c58f77 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -89,7 +90,7 @@
@Override
public void run() {
try {
                mContext.setWallpaper(mBitmap);
} catch (IOException e) {
Log.e(LOG_TAG, "Failed to set wallpaper.", e);
} finally {







