/*Use SoftReferences for Resource caching to improve performance

Keeping the cache a longer with SoftReferences. WeakReferences
are cleared on each gc cycle which makes them not so efficient
for caching.

On average this leads to half the time to create the new view
when changing from landscape to portrait. Also, the time for
starting a new application will be reduced.

Change-Id:I1b028b380948c54ffe7bd796e1cc18a6127698bc*/
//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
//Synthetic comment -- index e671359..ab223aa 100755

//Synthetic comment -- @@ -39,7 +39,7 @@

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;

/**
//Synthetic comment -- @@ -74,12 +74,12 @@
/*package*/ final TypedValue mTmpValue = new TypedValue();

// These are protected by the mTmpValue lock.
    private final LongSparseArray<WeakReference<Drawable.ConstantState> > mDrawableCache
            = new LongSparseArray<WeakReference<Drawable.ConstantState> >();
    private final SparseArray<WeakReference<ColorStateList> > mColorStateListCache
            = new SparseArray<WeakReference<ColorStateList> >();
    private final LongSparseArray<WeakReference<Drawable.ConstantState> > mColorDrawableCache
            = new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
//Synthetic comment -- @@ -1321,7 +1321,7 @@
}

private void clearDrawableCache(
            LongSparseArray<WeakReference<ConstantState>> cache,
int configChanges) {
int N = cache.size();
if (DEBUG_CONFIG) {
//Synthetic comment -- @@ -1329,7 +1329,7 @@
+ Integer.toHexString(configChanges));
}
for (int i=0; i<N; i++) {
            WeakReference<Drawable.ConstantState> ref = cache.valueAt(i);
if (ref != null) {
Drawable.ConstantState cs = ref.get();
if (cs != null) {
//Synthetic comment -- @@ -1758,9 +1758,9 @@
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + cs);
if (isColorDrawable) {
                            mColorDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
} else {
                            mDrawableCache.put(key, new WeakReference<Drawable.ConstantState>(cs));
}
}
}
//Synthetic comment -- @@ -1771,12 +1771,12 @@
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
//Synthetic comment -- @@ -1866,7 +1866,7 @@
//        Integer.toHexString(key.intValue())
//        + " in " + this + ": " + csl);
mColorStateListCache.put(
                        key, new WeakReference<ColorStateList>(csl));
}
}
}
//Synthetic comment -- @@ -1876,9 +1876,9 @@

private ColorStateList getCachedColorStateList(int key) {
synchronized (mTmpValue) {
            WeakReference<ColorStateList> wr = mColorStateListCache.get(key);
            if (wr != null) {   // we have the key
                ColorStateList entry = wr.get();
if (entry != null) {
//Log.i(TAG, "Returning cached color state list @ #" +
//        Integer.toHexString(((Integer)key).intValue())







