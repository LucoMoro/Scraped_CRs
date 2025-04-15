/*Fix the problem that color drawable cache key conflicts another drawable one.
The cache key of a color drawable resource may be the same as another
drawable resource's value.

Change-Id:Ia971bb242ceac5e8f9346094009a10f356399ab9*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
//Synthetic comment -- index 0608cc0..abd1ac8 100644

//Synthetic comment -- @@ -66,6 +66,8 @@
= new LongSparseArray<Drawable.ConstantState>();
private static final SparseArray<ColorStateList> mPreloadedColorStateLists
= new SparseArray<ColorStateList>();
    private static final LongSparseArray<Drawable.ConstantState> sPreloadedColorDrawables
            = new LongSparseArray<Drawable.ConstantState>();
private static boolean mPreloaded;

/*package*/ final TypedValue mTmpValue = new TypedValue();
//Synthetic comment -- @@ -75,6 +77,8 @@
= new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private final SparseArray<WeakReference<ColorStateList> > mColorStateListCache
= new SparseArray<WeakReference<ColorStateList> >();
    private final LongSparseArray<WeakReference<Drawable.ConstantState> > mColorDrawableCache
            = new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
//Synthetic comment -- @@ -1330,6 +1334,38 @@
}
mDrawableCache.clear();
mColorStateListCache.clear();

            int colorDrawableCacheSize = mColorDrawableCache.size();
            if (DEBUG_CONFIG) {
                Log.d(TAG, "Cleaning up color drawables config changes: 0x"
                        + Integer.toHexString(configChanges));
            }
            for (int i=0; i<colorDrawableCacheSize; i++) {
                WeakReference<Drawable.ConstantState> ref = mColorDrawableCache.valueAt(i);
                if (ref != null) {
                    Drawable.ConstantState cs = ref.get();
                    if (cs != null) {
                        if (Configuration.needNewResources(
                                configChanges, cs.getChangingConfigurations())) {
                            if (DEBUG_CONFIG) {
                                Log.d(TAG, "FLUSHING #0x"
                                        + Long.toHexString(mColorDrawableCache.keyAt(i))
                                        + " / " + cs + " with changes: 0x"
                                        + Integer.toHexString(cs.getChangingConfigurations()));
                            }
                            mColorDrawableCache.setValueAt(i, null);
                        } else if (DEBUG_CONFIG) {
                            Log.d(TAG, "(Keeping #0x"
                                    + Long.toHexString(mColorDrawableCache.keyAt(i))
                                    + " / " + cs + " with changes: 0x"
                                    + Integer.toHexString(cs.getChangingConfigurations())
                                    + ")");
                        }
                    }
                }
            }
            mColorDrawableCache.clear();

flushLayoutCache();
}
synchronized (mSync) {
//Synthetic comment -- @@ -1661,13 +1697,18 @@
}

final long key = (((long) value.assetCookie) << 32) | value.data;
        boolean isColorDrawable = false;
        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
                value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            isColorDrawable = true;
        }
        Drawable dr = isColorDrawable ? getCachedColorDrawable(key) : getCachedDrawable(key);

if (dr != null) {
return dr;
}

        Drawable.ConstantState cs = isColorDrawable ? sPreloadedColorDrawables.get(key) : sPreloadedDrawables.get(key);
if (cs != null) {
dr = cs.newDrawable(this);
} else {
//Synthetic comment -- @@ -1726,13 +1767,21 @@
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
//Synthetic comment -- @@ -1760,6 +1809,25 @@
return null;
}

    private Drawable getCachedColorDrawable(long key) {
        synchronized (mTmpValue) {
            WeakReference<Drawable.ConstantState> wr = mColorDrawableCache.get(key);
            if (wr != null) {   // we have the key
                Drawable.ConstantState entry = wr.get();
                if (entry != null) {
                    //Log.i(TAG, "Returning cached color drawable @ #" +
                    //        Integer.toHexString(((Integer)key).intValue())
                    //        + " in " + this + ": " + entry);
                    return entry.newDrawable();
                }
                else {  // our entry has been purged
                    mColorDrawableCache.delete(key);
                }
            }
        }
        return null;
    }

/*package*/ ColorStateList loadColorStateList(TypedValue value, int id)
throws NotFoundException {
if (TRACE_FOR_PRELOAD) {







