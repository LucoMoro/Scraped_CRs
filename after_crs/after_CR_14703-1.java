/*View_AnimationTest QVGA Fixes

Bug 2597973

The animations time out, because the view being animated is not
visible on the screen. This is because the layout being used has
two views that are 400 pixels in height and push the animated
view down. Just use the first view in the layout.

Also incorporate a prior fix for testClearDuringAnimation which
was incorrectly asserting about the animation ending.

Change-Id:Icc5032131aa78a16ade0d73b0c83c2122d688ece*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/View_AnimationTest.java b/tests/tests/view/src/android/view/cts/View_AnimationTest.java
//Synthetic comment -- index 7a22a3b..d071781a 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
)
})
public void testAnimation() throws Throwable {
        final View view = mActivity.findViewById(R.id.mock_view);
// set null animation
view.setAnimation(null);
assertNull(view.getAnimation());
//Synthetic comment -- @@ -89,7 +89,7 @@
})
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete")
public void testStartAnimation() throws Throwable {
        final View view = mActivity.findViewById(R.id.mock_view);
// start null animation
try {
view.startAnimation(null);
//Synthetic comment -- @@ -120,7 +120,7 @@
)
})
public void testClearBeforeAnimation() throws Throwable {
        final View view = mActivity.findViewById(R.id.mock_view);
assertFalse(mAnimation.hasStarted());

view.setAnimation(mAnimation);
//Synthetic comment -- @@ -147,10 +147,11 @@
)
})
public void testClearDuringAnimation() throws Throwable {
        final View view = mActivity.findViewById(R.id.mock_view);
runTestOnUiThread(new Runnable() {
public void run() {
view.startAnimation(mAnimation);
                assertNotNull(view.getAnimation());
}
});

//Synthetic comment -- @@ -164,6 +165,7 @@
view.clearAnimation();
Thread.sleep(TIME_OUT);
assertTrue(mAnimation.hasStarted());
        assertTrue(mAnimation.hasEnded());
        assertNull(view.getAnimation());
}
}







