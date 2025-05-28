
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

= new LongSparseArray<Drawable.ConstantState>();
private static final SparseArray<ColorStateList> mPreloadedColorStateLists
= new SparseArray<ColorStateList>();
private static boolean mPreloaded;

/*package*/ final TypedValue mTmpValue = new TypedValue();
= new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private final SparseArray<WeakReference<ColorStateList> > mColorStateListCache
= new SparseArray<WeakReference<ColorStateList> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
(int)(mMetrics.density*160), mConfiguration.keyboard,
keyboardHidden, mConfiguration.navigation, width, height,
mConfiguration.screenLayout, mConfiguration.uiMode, sSdkVersion);
            int N = mDrawableCache.size();
            if (DEBUG_CONFIG) {
                Log.d(TAG, "Cleaning up drawables config changes: 0x"
                        + Integer.toHexString(configChanges));
            }
            for (int i=0; i<N; i++) {
                WeakReference<Drawable.ConstantState> ref = mDrawableCache.valueAt(i);
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
}
}

/**
* Update the system resources configuration if they have previously
* been initialized.
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
cs = dr.getConstantState();
if (cs != null) {
if (mPreloading) {
                    sPreloadedDrawables.put(key, cs);
} else {
synchronized (mTmpValue) {
//Log.i(TAG, "Saving cached drawable @ #" +
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + cs);
                        mDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
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
//Log.i(TAG, "Returning cached drawable @ #" +
//        Integer.toHexString(((Integer)key).intValue())
//        + " in " + this + ": " + entry);
                    return entry.newDrawable(this);
}
else {  // our entry has been purged
                    mDrawableCache.delete(key);
}
}
}
return null;
    }

/*package*/ ColorStateList loadColorStateList(TypedValue value, int id)
throws NotFoundException {

//<End of snippet n. 0>








