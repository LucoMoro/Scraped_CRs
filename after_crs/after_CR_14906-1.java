/*Added tests for new Toast.LENGTH_LONGER Property

Change-Id:I93582c55d155e94d5ae6354ef24dc1b53fbb7473*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ToastTest.java b/tests/tests/widget/src/android/widget/cts/ToastTest.java
//Synthetic comment -- index c54b205..ab6eede 100644

//Synthetic comment -- @@ -257,7 +257,22 @@
assertShowAndHide(view);
long shortDuration = SystemClock.uptimeMillis() - start;

        start = SystemClock.uptimeMillis();
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mToast.setDuration(Toast.LENGTH_LONGER);
                mToast.show();
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(Toast.LENGTH_LONGER, mToast.getDuration());

        view = mToast.getView();
        assertShowAndHide(view);
        long longerDuration = SystemClock.uptimeMillis() - start;

assertTrue(longDuration > shortDuration);
        assertTrue(longerDuration > longDuration);
}

@TestTargets({
//Synthetic comment -- @@ -439,6 +454,12 @@
view = mToast.getView();
assertNotNull(view);

        mToast = Toast.makeText(mActivity, "longer", Toast.LENGTH_LONGER);
        assertNotNull(mToast);
        assertEquals(Toast.LENGTH_LONGER, mToast.getDuration());
        view = mToast.getView();
        assertNotNull(view);

mToast = Toast.makeText(mActivity, null, Toast.LENGTH_LONG);
assertNotNull(mToast);
assertEquals(Toast.LENGTH_LONG, mToast.getDuration());
//Synthetic comment -- @@ -476,6 +497,12 @@
view = mToast.getView();
assertNotNull(view);

        mToast = Toast.makeText(mActivity, R.string.hello_world, Toast.LENGTH_LONGER);
        assertNotNull(mToast);
        assertEquals(Toast.LENGTH_LONGER, mToast.getDuration());
        view = mToast.getView();
        assertNotNull(view);

try {
mToast = Toast.makeText(null, R.string.hello_android, Toast.LENGTH_SHORT);
fail("did not throw NullPointerException when context is null.");







