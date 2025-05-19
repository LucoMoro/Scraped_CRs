//<Beginning of snippet n. 0>
private static final LongSparseArray<Drawable.ConstantState> mDrawableCache = new LongSparseArray<Drawable.ConstantState>();
private static final SparseArray<ColorStateList> mColorStateListCache = new SparseArray<ColorStateList>();
private static boolean mPreloaded;

final TypedValue mTmpValue = new TypedValue();
private final LongSparseArray<WeakReference<Drawable.ConstantState>> mDrawableCacheColor = new LongSparseArray<WeakReference<Drawable.ConstantState>>();
private final SparseArray<WeakReference<ColorStateList>> mColorStateListCache = new SparseArray<WeakReference<ColorStateList>>();
private boolean mPreloading;

//... other code ...

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

synchronized (mSync) {
    // Synchronization block.
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
                long drawableKey = key | 0x8000000000000000L;
                if (mDrawableCache.get(drawableKey) == null) {
                    mDrawableCache.put(drawableKey, new WeakReference<Drawable.ConstantState>(cs));
                } else {
                    Log.e(TAG, "Drawable key conflict detected for 0x" + Long.toHexString(drawableKey));
                }
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

/*package*/ ColorStateList loadColorStateList(TypedValue value, int id) throws NotFoundException {
    long key = 0x8000000000000000L | id;
    WeakReference<ColorStateList> wr = mColorStateListCache.get(key);
    if (wr != null) {
        ColorStateList entry = wr.get();
        if (entry != null) {
            return entry;
        } else {
            mColorStateListCache.delete(key);
        }
    }
    // Additional code to load the ColorStateList
}
//<End of snippet n. 0>