/*Prevent unhandled exception in NinePatchDrawable

Added null check in computeBitmapSize() to avoid unhandled
exception "java.lang.NullPointerException" at
android.graphics.Rect.<init>(Rect.java:72).

This problem was discovered in the wild.

Change-Id:I8a4729ceadcfc9744f11b8ceafdf24d6de88c645*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/NinePatchDrawable.java b/graphics/java/android/graphics/drawable/NinePatchDrawable.java
//Synthetic comment -- index bad94fb..00416d8 100644

//Synthetic comment -- @@ -164,15 +164,17 @@
sdensity, tdensity);
mBitmapHeight = Bitmap.scaleFromDensity(mNinePatch.getHeight(),
sdensity, tdensity);
            if (mNinePatchState.mPadding != null && mPadding != null) {
                Rect dest = mPadding;
                Rect src = mNinePatchState.mPadding;
                if (dest == src) {
                    mPadding = dest = new Rect(src);
                }
                dest.left = Bitmap.scaleFromDensity(src.left, sdensity, tdensity);
                dest.top = Bitmap.scaleFromDensity(src.top, sdensity, tdensity);
                dest.right = Bitmap.scaleFromDensity(src.right, sdensity, tdensity);
                dest.bottom = Bitmap.scaleFromDensity(src.bottom, sdensity, tdensity);
}
}
}








