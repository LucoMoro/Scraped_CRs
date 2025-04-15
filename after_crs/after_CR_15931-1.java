/*Shoud specify the Resource class instance as an argument of the method
newDrawable and change the name from 'DrawableCacheClear' to 'clearDrawableCache'
(additional changes for ID:15815)https://review.source.android.com/#change,15815Change-Id:I6bf19b8e6e187df8c8e3cb57d9e04278ddfe5055*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
//Synthetic comment -- index 3387fc9..e671359 100755

//Synthetic comment -- @@ -1305,8 +1305,8 @@
keyboardHidden, mConfiguration.navigation, width, height,
mConfiguration.screenLayout, mConfiguration.uiMode, sSdkVersion);

            clearDrawableCache(mDrawableCache, configChanges);
            clearDrawableCache(mColorDrawableCache, configChanges);

mColorStateListCache.clear();

//Synthetic comment -- @@ -1320,7 +1320,7 @@
}
}

    private void clearDrawableCache(
LongSparseArray<WeakReference<ConstantState>> cache,
int configChanges) {
int N = cache.size();
//Synthetic comment -- @@ -1781,7 +1781,7 @@
//Log.i(TAG, "Returning cached drawable @ #" +
//        Integer.toHexString(((Integer)key).intValue())
//        + " in " + this + ": " + entry);
                    return entry.newDrawable(this);
}
else {  // our entry has been purged
drawableCache.delete(key);







