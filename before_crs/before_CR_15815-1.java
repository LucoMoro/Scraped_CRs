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
private static boolean mPreloaded;

/*package*/ final TypedValue mTmpValue = new TypedValue();
//Synthetic comment -- @@ -75,6 +77,8 @@
= new LongSparseArray<WeakReference<Drawable.ConstantState> >();
private final SparseArray<WeakReference<ColorStateList> > mColorStateListCache
= new SparseArray<WeakReference<ColorStateList> >();
private boolean mPreloading;

/*package*/ TypedArray mCachedStyledAttributes = null;
//Synthetic comment -- @@ -1330,6 +1334,38 @@
}
mDrawableCache.clear();
mColorStateListCache.clear();
flushLayoutCache();
}
synchronized (mSync) {
//Synthetic comment -- @@ -1661,13 +1697,18 @@
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
//Synthetic comment -- @@ -1726,13 +1767,21 @@
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
//Synthetic comment -- @@ -1760,6 +1809,25 @@
return null;
}

/*package*/ ColorStateList loadColorStateList(TypedValue value, int id)
throws NotFoundException {
if (TRACE_FOR_PRELOAD) {







