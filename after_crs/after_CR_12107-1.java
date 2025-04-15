/*ShapeDrawable should have default intrinsic dimentions set to -1, just like ColorDrawable (which inherits them from Drawable)*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/ShapeDrawable.java b/graphics/java/android/graphics/drawable/ShapeDrawable.java
//Synthetic comment -- index 6677a35..59636219 100644

//Synthetic comment -- @@ -371,8 +371,8 @@
Paint mPaint;
Shape mShape;
Rect mPadding;
        int mIntrinsicWidth = -1;
        int mIntrinsicHeight = -1;
int mAlpha = 255;
ShaderFactory mShaderFactory;








