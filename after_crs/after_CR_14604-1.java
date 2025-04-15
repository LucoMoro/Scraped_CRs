/*DO NOT MERGE. Adjust NinePatchDrawableTest#testConstructors.

This test was failing due to a known bug which has been fixed in next platform
release.

Change-Id:I708fe153421ae1ded516bf25d5452d8fb4b577f9*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java
//Synthetic comment -- index 3b376fd..f62fea8 100644

//Synthetic comment -- @@ -87,7 +87,8 @@

new NinePatchDrawable(bmp, chunk, r, name);

        // Omit for now - known failure, fixed in froyo. Bug 2219785
        //new NinePatchDrawable(new NinePatch(bmp, chunk, name));

chunk = new byte[MIN_CHUNK_SIZE - 1];
chunk[MIN_CHUNK_SIZE - 2] = 1;







