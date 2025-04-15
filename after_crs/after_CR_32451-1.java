/*Bitmap reference not removed in destroyDrawingCache

When calling destroyDrawingCache in View.java the
bitmap is not freed since there is a reference to
it in mAttachInfo.mCanvas. The bitmap will get
freed eventually, either when GC:ing the View
or if we create a new drawing cache bitmap
(i.e calling buildDrawingCache), but there
is no point in keeping it around for any
of that to happen.

Change-Id:I8aa082a570e1022a59629f4bfada687ec843efed*/




//Synthetic comment -- diff --git a/core/java/android/view/View.java b/core/java/android/view/View.java
//Synthetic comment -- index 54bb056..d203d64 100644

//Synthetic comment -- @@ -10497,6 +10497,9 @@
mUnscaledDrawingCache.recycle();
mUnscaledDrawingCache = null;
}
        if (mAttachInfo != null) {
            mAttachInfo.mCanvas = null;
        }
}

/**







