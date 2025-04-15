/*replaced Deprecated Wallpaper API with WallpaperManager

Change-Id:Ic2b1d0fbc84764d21acae7f96df8e9a834073c42*/




//Synthetic comment -- diff --git a/core/java/android/content/ContextWrapper.java b/core/java/android/content/ContextWrapper.java
//Synthetic comment -- index 1b34320c..ca113d4 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package android.content;

import android.app.WallpaperManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
//Synthetic comment -- @@ -43,9 +44,11 @@
*/
public class ContextWrapper extends Context {
Context mBase;
    WallpaperManager mWallpaperManager;

public ContextWrapper(Context base) {
mBase = base;
        mWallpaperManager = WallpaperManager.getInstance(mBase);
}

/**
//Synthetic comment -- @@ -60,6 +63,7 @@
throw new IllegalStateException("Base context already set");
}
mBase = base;
        mWallpaperManager = WallpaperManager.getInstance(mBase);
}

/**
//Synthetic comment -- @@ -210,37 +214,37 @@

@Override
public Drawable getWallpaper() {
        return mWallpaperManager.getDrawable();
}

@Override
public Drawable peekWallpaper() {
        return mWallpaperManager.peekDrawable();
}

@Override
public int getWallpaperDesiredMinimumWidth() {
        return mWallpaperManager.getDesiredMinimumWidth();
}

@Override
public int getWallpaperDesiredMinimumHeight() {
        return mWallpaperManager.getDesiredMinimumHeight();
}

@Override
public void setWallpaper(Bitmap bitmap) throws IOException {
        mWallpaperManager.setBitmap(bitmap);
}

@Override
public void setWallpaper(InputStream data) throws IOException {
        mWallpaperManager.setStream(data);
}

@Override
public void clearWallpaper() throws IOException {
        mWallpaperManager.clear();
}

@Override







