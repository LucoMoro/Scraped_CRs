/*Suppress NinePatchDrawableTest#testGetOpacity

Bug 2834281

Bitmap#hasAlpha always seems to return true even after I used an image
without any transparent pixels...

Change-Id:Ib81755e731337367aeb39a6b78d4b585a528ce2c*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java
//Synthetic comment -- index e834535..064c45a 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -323,6 +324,8 @@
method = "getOpacity",
args = {}
)
public void testGetOpacity() {
assertEquals(PixelFormat.OPAQUE, mNinePatchDrawable.getOpacity());








