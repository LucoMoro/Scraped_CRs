//<Beginning of snippet n. 0>
private static final LongSparseArray<WeakReference<Drawable.ConstantState>> mDrawableCache = new LongSparseArray<WeakReference<Drawable.ConstantState>>();
private static final SparseArray<ColorStateList> mPreloadedColorStateLists = new SparseArray<ColorStateList>();
private static boolean mPreloaded;

final TypedValue mTmpValue = new TypedValue();
private static final LongSparseArray<WeakReference<Drawable.ConstantState>> mColorDrawableCache = new LongSparseArray<WeakReference<Drawable.ConstantState>>();
private static final SparseArray<WeakReference<ColorStateList>> mColorStateListCache = new SparseArray<WeakReference<ColorStateList>>();
private boolean mPreloading;
TypedArray mCachedStyledAttributes = null;

private void flushCache(int configChanges) {
    int N = mDrawableCache.size();
    if (DEBUG_CONFIG) {
        Log.d(TAG, "Cleaning up drawables config changes: 0x" + Integer.toHexString(configChanges));
    }
    for (int i = 0; i < N; i++) {
        WeakReference<Drawable.ConstantState> ref = mDrawableCache.valueAt(i);
        if (ref != null) {
            Drawable.ConstantState cs = ref.get();
            if (cs != null) {
                if (Configuration.needNewResources(configChanges, cs.getChangingConfigurations())) {
                    if (DEBUG_CONFIG) {
                        Log.d(TAG, "FLUSHING #0x" + Long.toHexString(mDrawableCache.keyAt(i)) + " / " + cs + " with changes: 0x" + Integer.toHexString(cs.getChangingConfigurations()));
                    }
                    mDrawableCache.setValueAt(i, null);
                } else if (DEBUG_CONFIG) {
                    Log.d(TAG, "(Keeping #0x" + Long.toHexString(mDrawableCache.keyAt(i)) + " / " + cs + " with changes: 0x" + Integer.toHexString(cs.getChangingConfigurations()) + ")");
                }
            }
        }
    }
    mDrawableCache.clear();
    mColorStateListCache.clear();
    flushLayoutCache();
}

synchronized (mSync) {
}

final long key = (((long) value.assetCookie) << 32) | value.data;
Drawable dr = getCachedDrawable(key);

if (dr != null) {
    return dr;
}

Drawable.ConstantState cs = mColorDrawableCache.get(key);
if (cs != null) {
    dr = cs.newDrawable(this);
} else {
    cs = dr.getConstantState();
    if (cs != null) {
        if (mPreloading) {
            mColorDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
        } else {
            synchronized (mTmpValue) {
                mDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
            }
        }
    }
}
return dr;
}

private Drawable getCachedDrawable(long key) {
    synchronized (mTmpValue) {
        WeakReference<Drawable.ConstantState> wr = mDrawableCache.get(key);
        if (wr != null) { 
            Drawable.ConstantState entry = wr.get();
            if (entry != null) {
                return entry.newDrawable(this);
            } else {
                mDrawableCache.delete(key);
            }
        }
    }
    return null;
}

ColorStateList loadColorStateList(TypedValue value, int id) throws NotFoundException {
    final long colorKey = "color_".concat(String.valueOf(((long) value.assetCookie) << 32 | id));
    ColorStateList colorList = mColorStateListCache.get(colorKey) != null ? mColorStateListCache.get(colorKey).get() : null;
    if (colorList != null) {
        return colorList;
    }
    // Load color state list and cache it accordingly
    return null;
}
//<End of snippet n. 0>