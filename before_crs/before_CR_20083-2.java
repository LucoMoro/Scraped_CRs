/*Fix InsetDrawableTest#testGetOpacity

Bug 2397630

BitmapDrawableTest#testMutate changed the constant state which affected
InsetDrawableTest#testGetOpacity. However, the test wasn't doing much of
anything, so I've changed it a bit to call setAlpha and check
getOpacity a couple times. This test shouldn't have a problem with
constant state, because it calls setAlpha before checking anything.

Change-Id:I1cca5cb18107a647e40c13a7c88c905856436595*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/InsetDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/InsetDrawableTest.java
//Synthetic comment -- index 6ca1806..32dc5ca 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -305,15 +304,14 @@
method = "getOpacity",
args = {}
)
    @BrokenTest(value="bug 2397630 - needs investigation")
public void testGetOpacity() {
        Drawable d = mContext.getResources().getDrawable(R.drawable.pass);
InsetDrawable insetDrawable = new InsetDrawable(d, 0);
assertEquals(PixelFormat.OPAQUE, insetDrawable.getOpacity());

        d = mContext.getResources().getDrawable(R.drawable.testimage);
        insetDrawable = new InsetDrawable(d, 0);
        assertEquals(PixelFormat.OPAQUE, insetDrawable.getOpacity());
}

@TestTargetNew(
//Synthetic comment -- @@ -443,22 +441,6 @@
assertNotNull(constantState);
}

    @TestTargetNew(
        level = TestLevel.SUFFICIENT,
        method = "mutate",
        args = {}
    )
    public void testMutate() {
        Resources resources = mContext.getResources();
        InsetDrawable d1 = (InsetDrawable) resources.getDrawable(R.drawable.insetdrawable);

        d1.setAlpha(100);
        d1.mutate();
        d1.setAlpha(200);

        // Cannot test whether alpha was set properly.
    }

private class MockInsetDrawable extends InsetDrawable {
public MockInsetDrawable(Drawable drawable, int inset) {
super(drawable, inset);







