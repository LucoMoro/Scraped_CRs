/*Fix the problem that color drawable cache key conflicts another drawable one.
The cache key of a color drawable resource may be the same as another
drawable resource's value.

Change-Id:Ia971bb242ceac5e8f9346094009a10f356399ab9(Reduced duplicated codes and replace TAB to white spaces)

And try to fix compile error.*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0608cc0..3387fc9

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
//Synthetic comment -- @@ -66,6 +67,8 @@
= new LongSparseArray<Drawable.ConstantState>();
private static final SparseArray<ColorStateList> mPreloadedColorStateLists
= new SparseArray<ColorStateList>();
    private static final LongSparseArray<Drawable.ConstantState> sPreloadedColorDrawables
            = new LongSparseArray<Drawable.ConstantState>();
private static boolean mPreloaded;

/*package*/ final TypedValue mTmpValue = new TypedValue();
//Synthetic comment -- @@ -75,6 +78,8 @@
= new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private final SparseArray<WeakReference<ColorStateList> > mColorStateListCache
= new SparseArray<WeakReference<ColorStateList> >();
    private final LongSparseArray<WeakReference<Drawable.ConstantState> > mColorDrawableCache
            = new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
//Synthetic comment -- @@ -1299,37 +1304,13 @@
(int)(mMetrics.density*160), mConfiguration.keyboard,
keyboardHidden, mConfiguration.navigation, width, height,
mConfiguration.screenLayout, mConfiguration.uiMode, sSdkVersion);

            drawableCacheClear(mDrawableCache, configChanges);
            drawableCacheClear(mColorDrawableCache, configChanges);

mColorStateListCache.clear();


flushLayoutCache();
}
synchronized (mSync) {
//Synthetic comment -- @@ -1339,6 +1320,41 @@
}
}

    private void drawableCacheClear(
            LongSparseArray<WeakReference<ConstantState>> cache,
            int configChanges) {
        int N = cache.size();
        if (DEBUG_CONFIG) {
            Log.d(TAG, "Cleaning up drawables config changes: 0x"
                    + Integer.toHexString(configChanges));
        }
        for (int i=0; i<N; i++) {
            WeakReference<Drawable.ConstantState> ref = cache.valueAt(i);
            if (ref != null) {
                Drawable.ConstantState cs = ref.get();
                if (cs != null) {
                    if (Configuration.needNewResources(
                            configChanges, cs.getChangingConfigurations())) {
                        if (DEBUG_CONFIG) {
                            Log.d(TAG, "FLUSHING #0x"
                                    + Long.toHexString(mDrawableCache.keyAt(i))
                                    + " / " + cs + " with changes: 0x"
                                    + Integer.toHexString(cs.getChangingConfigurations()));
                        }
                        cache.setValueAt(i, null);
                    } else if (DEBUG_CONFIG) {
                        Log.d(TAG, "(Keeping #0x"
                                + Long.toHexString(cache.keyAt(i))
                                + " / " + cs + " with changes: 0x"
                                + Integer.toHexString(cs.getChangingConfigurations())
                                + ")");
                    }
                }
            }
        }
        cache.clear();
    }

/**
* Update the system resources configuration if they have previously
* been initialized.
//Synthetic comment -- @@ -1661,13 +1677,18 @@
}

final long key = (((long) value.assetCookie) << 32) | value.data;
        boolean isColorDrawable = false;
        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
                value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            isColorDrawable = true;
        }
        Drawable dr = getCachedDrawable(isColorDrawable ? mColorDrawableCache : mDrawableCache, key);

if (dr != null) {
return dr;
}

        Drawable.ConstantState cs = isColorDrawable ? sPreloadedColorDrawables.get(key) : sPreloadedDrawables.get(key);
if (cs != null) {
dr = cs.newDrawable(this);
} else {
//Synthetic comment -- @@ -1726,13 +1747,21 @@
cs = dr.getConstantState();
if (cs != null) {
if (mPreloading) {
                    if (isColorDrawable) {
                        sPreloadedColorDrawables.put(key, cs);
                    } else {
                        sPreloadedDrawables.put(key, cs);
                    }
} else {
synchronized (mTmpValue) {
//Log.i(TAG, "Saving cached drawable @ #" +
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + cs);
                        if (isColorDrawable) {
                            mColorDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
                        } else {
                            mDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
                        }
}
}
}
//Synthetic comment -- @@ -1741,19 +1770,21 @@
return dr;
}

    private Drawable getCachedDrawable(
            LongSparseArray<WeakReference<ConstantState>> drawableCache,
            long key) {
synchronized (mTmpValue) {
            WeakReference<Drawable.ConstantState> wr = drawableCache.get(key);
if (wr != null) {   // we have the key
Drawable.ConstantState entry = wr.get();
if (entry != null) {
//Log.i(TAG, "Returning cached drawable @ #" +
//        Integer.toHexString(((Integer)key).intValue())
//        + " in " + this + ": " + entry);
                    return entry.newDrawable();
}
else {  // our entry has been purged
                    drawableCache.delete(key);
}
}
}







