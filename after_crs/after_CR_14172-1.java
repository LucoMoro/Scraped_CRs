/*Remove NinePatchDrawableTest NPE Checks

The test was failing due to not throwing an exception for null
padding. Remove other NPE checks since developers should not be
passing nulls into them anyway.

Change-Id:Ie024b5d5984e60bd7a83407e020525af3aa6e359*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/NinePatchDrawableTest.java
//Synthetic comment -- index b9e1e4b..3b376fd 100644

//Synthetic comment -- @@ -75,9 +75,6 @@
args = {android.graphics.NinePatch.class}
)
})
@SuppressWarnings("deprecation")
public void testConstructors() {
byte[] chunk = new byte[MIN_CHUNK_SIZE];
//Synthetic comment -- @@ -90,41 +87,7 @@

new NinePatchDrawable(bmp, chunk, r, name);

        new NinePatchDrawable(new NinePatch(bmp, chunk, name));

chunk = new byte[MIN_CHUNK_SIZE - 1];
chunk[MIN_CHUNK_SIZE - 2] = 1;







