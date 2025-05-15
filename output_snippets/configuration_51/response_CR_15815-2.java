//<Beginning of snippet n. 0>
private static final LongSparseArray<Drawable.ConstantState> mDrawableCache = new LongSparseArray<>();
private static final SparseArray<ColorStateList> mColorStateListCache = new SparseArray<>();
private static final LongSparseArray<WeakReference<Drawable.ConstantState>> mWeakDrawableCache = new LongSparseArray<>();
private static final SparseArray<WeakReference<ColorStateList>> mWeakColorStateListCache = new SparseArray<>();
private static boolean mPreloading;

/*package*/ final TypedValue mTmpValue = new TypedValue();

/*package*/ TypedArray mCachedStyledAttributes = null;

private void cleanupDrawables(int configChanges) {
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
    // Potentially other operations
}

/**
 * Update the system resources configuration if they have previously
 * been initialized.
 */

final long key = (((long) value.assetCookie) << 32) | value.data;
Drawable dr = getCachedDrawable(key);

if (dr != null) {
    return dr;
}

Drawable.ConstantState cs = sPreloadedDrawables.get(key);
if (cs != null) {
    dr = cs.newDrawable(this);
} else {
    cs = dr.getConstantState();
    if (cs != null) {
        if (mPreloading) {
            sPreloadedDrawables.put(key, cs);
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
        if (wr != null) {   // we have the key
            Drawable.ConstantState entry = wr.get();
            if (entry != null) {
                return entry.newDrawable(this);
            } else {  // our entry has been purged
                mDrawableCache.delete(key);
            }
        }
    }
    return null;
}

/*package*/ ColorStateList loadColorStateList(TypedValue value, int id) throws NotFoundException {
    // Implement caching logic similar to Drawable caching
    // Add conflict resolution for ColorStateList key management
}
//<End of snippet n. 0>