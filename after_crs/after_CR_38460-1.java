/*Fix android.animation.cts.ValueAnimatorTest#testCancel

According to the API documentation, regarding ValueAnimator class the cancel()
must be called on the thread that is running the animation. Otherwise it might
have unexpected result of isRunning() due to thread scheduling.*/




//Synthetic comment -- diff --git a/tests/tests/animation/src/android/animation/cts/ValueAnimatorTest.java b/tests/tests/animation/src/android/animation/cts/ValueAnimatorTest.java
//Synthetic comment -- index a7626bc..3455525 100644

//Synthetic comment -- @@ -110,7 +110,7 @@
public void testCancel() throws Throwable {
startAnimation(mValueAnimator);
Thread.sleep(100);
        cancelAnimation(mValueAnimator);
assertFalse(mValueAnimator.isRunning());
}

//Synthetic comment -- @@ -259,5 +259,13 @@
};
this.runTestOnUiThread(animationRunnable);
}
    private void cancelAnimation(final ValueAnimator mValueAnimator) throws Throwable {
        Thread animationRunnable = new Thread() {
            public void run() {
                mValueAnimator.cancel();
            }
        };
        this.runTestOnUiThread(animationRunnable);
    }
}








