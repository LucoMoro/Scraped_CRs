/*Performance: Improve Resource caching

Keeping the cache a longer with SoftReferences. The weak
references are cleared on each gc cycle which makes them
not so efficient for caching.

On average this leads to half the time to create the new
view when changing from landscape to portrait. Also, the
time for starting a new application will be reduced.

Change-Id:I24d6ec4a73b50cc6dc698614380b0eaab769fdfb*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
old mode 100755
new mode 100644
//Synthetic comment -- index a6513aa..34d00d5

//Synthetic comment -- @@ -39,7 +39,7 @@

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Locale;

/**
//Synthetic comment -- @@ -67,19 +67,15 @@
= new LongSparseArray<Drawable.ConstantState>();
private static final SparseArray<ColorStateList> mPreloadedColorStateLists
= new SparseArray<ColorStateList>();
private static boolean mPreloaded;

/*package*/ final TypedValue mTmpValue = new TypedValue();

// These are protected by the mTmpValue lock.
    private final LongSparseArray<SoftReference<Drawable.ConstantState> > mDrawableCache
            = new LongSparseArray<SoftReference<Drawable.ConstantState> >();
    private final SparseArray<SoftReference<ColorStateList> > mColorStateListCache
            = new SparseArray<SoftReference<ColorStateList> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
//Synthetic comment -- @@ -1304,13 +1300,37 @@
(int)(mMetrics.density*160), mConfiguration.keyboard,
keyboardHidden, mConfiguration.navigation, width, height,
mConfiguration.screenLayout, mConfiguration.uiMode, sSdkVersion);
            int N = mDrawableCache.size();
            if (DEBUG_CONFIG) {
                Log.d(TAG, "Cleaning up drawables config changes: 0x"
                        + Integer.toHexString(configChanges));
            }
            for (int i=0; i<N; i++) {
                SoftReference<Drawable.ConstantState> ref = mDrawableCache.valueAt(i);
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
                            mDrawableCache.setValueAt(i, null);
                        } else if (DEBUG_CONFIG) {
                            Log.d(TAG, "(Keeping #0x"
                                    + Long.toHexString(mDrawableCache.keyAt(i))
                                    + " / " + cs + " with changes: 0x"
                                    + Integer.toHexString(cs.getChangingConfigurations())
                                    + ")");
                        }
                    }
                }
            }
            mDrawableCache.clear();
mColorStateListCache.clear();
flushLayoutCache();
}
synchronized (mSync) {
//Synthetic comment -- @@ -1320,41 +1340,6 @@
}
}

/**
* Update the system resources configuration if they have previously
* been initialized.
//Synthetic comment -- @@ -1677,18 +1662,13 @@
}

final long key = (((long) value.assetCookie) << 32) | value.data;
        Drawable dr = getCachedDrawable(key);

if (dr != null) {
return dr;
}

        Drawable.ConstantState cs = sPreloadedDrawables.get(key);
if (cs != null) {
dr = cs.newDrawable(this);
} else {
//Synthetic comment -- @@ -1747,21 +1727,13 @@
cs = dr.getConstantState();
if (cs != null) {
if (mPreloading) {
                    sPreloadedDrawables.put(key, cs);
} else {
synchronized (mTmpValue) {
//Log.i(TAG, "Saving cached drawable @ #" +
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + cs);
                        mDrawableCache.put(key, new SoftReference<Drawable.ConstantState>(cs));
}
}
}
//Synthetic comment -- @@ -1770,13 +1742,11 @@
return dr;
}

    private Drawable getCachedDrawable(long key) {
synchronized (mTmpValue) {
            SoftReference<Drawable.ConstantState> ref = mDrawableCache.get(key);
            if (ref != null) {   // we have the key
                Drawable.ConstantState entry = ref.get();
if (entry != null) {
//Log.i(TAG, "Returning cached drawable @ #" +
//        Integer.toHexString(((Integer)key).intValue())
//Synthetic comment -- @@ -1784,7 +1754,7 @@
return entry.newDrawable(this);
}
else {  // our entry has been purged
                    mDrawableCache.delete(key);
}
}
}
//Synthetic comment -- @@ -1866,7 +1836,7 @@
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + csl);
mColorStateListCache.put(
                        key, new SoftReference<ColorStateList>(csl));
}
}
}
//Synthetic comment -- @@ -1876,9 +1846,9 @@

private ColorStateList getCachedColorStateList(int key) {
synchronized (mTmpValue) {
            SoftReference<ColorStateList> ref = mColorStateListCache.get(key);
            if (ref != null) {   // we have the key
                ColorStateList entry = ref.get();
if (entry != null) {
//Log.i(TAG, "Returning cached color state list @ #" +
//        Integer.toHexString(((Integer)key).intValue())







