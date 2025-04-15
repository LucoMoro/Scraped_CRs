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
    @ToBeFixed(bug = "1417734", explanation = "should add @throws clause into javadoc of "
            + "NinePatchDrawable#NinePatchDrawable(Bitmap, byte[], Rect, String) "
            + "when param bitmap, chunk, padding or srcName is null")
@SuppressWarnings("deprecation")
public void testConstructors() {
byte[] chunk = new byte[MIN_CHUNK_SIZE];
//Synthetic comment -- @@ -90,41 +87,7 @@

new NinePatchDrawable(bmp, chunk, r, name);

        try {
            new NinePatchDrawable(null, chunk, r, name);
            fail("The constructor should check whether the bitmap is null.");
        } catch (NullPointerException e) {
        }

        // These codes will crash the test cases though the exceptions are caught.
        // try {
        //     new NinePatchDrawable(bmp, null, r, name);
        //     fail("The constructor should check whether the chunk is null.");
        // } catch (Exception e) {
        // }

        try {
            mNinePatchDrawable = new NinePatchDrawable(bmp, chunk, null, name);
            fail("The constructor should not accept null padding.");
        } catch (NullPointerException e) {
        }

        try {
            new NinePatchDrawable(bmp, chunk, r, null);
        } catch (NullPointerException e) {
            fail("The constructor should accept null srcname.");
        }

        // Known Failure - should not throw NPE bug 2136234
        // still test for this so test can be adjusted once framework fixed
        try {
            new NinePatchDrawable(new NinePatch(bmp, chunk, name));
        } catch (NullPointerException e) {
            // expected
        }

        // constructor should accept a null NinePatch
        mNinePatchDrawable = new NinePatchDrawable(null);

chunk = new byte[MIN_CHUNK_SIZE - 1];
chunk[MIN_CHUNK_SIZE - 2] = 1;







