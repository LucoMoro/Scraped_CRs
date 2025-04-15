/*Fix coding convention in ValueAnimatorTest

It changes to use Runnable instead of Thread when using
runTestOnUiThread() and also corrects the naming of function
parameter.

Change-Id:Ida204042463b73810570f1e4be6dab94c1b3519d*/




//Synthetic comment -- diff --git a/tests/tests/animation/src/android/animation/cts/ValueAnimatorTest.java b/tests/tests/animation/src/android/animation/cts/ValueAnimatorTest.java
//Synthetic comment -- index 3a28fe8..5451472 100644

//Synthetic comment -- @@ -242,29 +242,29 @@
}
return values;
}

    private void startAnimation(final ValueAnimator animator) throws Throwable {
        this.runTestOnUiThread(new Runnable() {
public void run() {
                mActivity.startAnimation(animator);
}
});
}

    private void endAnimation(final ValueAnimator animator) throws Throwable {
        this.runTestOnUiThread(new Runnable() {
public void run() {
                animator.end();
}
        });
}

    private void cancelAnimation(final ValueAnimator animator) throws Throwable {
        this.runTestOnUiThread(new Runnable() {
public void run() {
                animator.cancel();
}
        });
}

private String errorMessage(float[] values) {







