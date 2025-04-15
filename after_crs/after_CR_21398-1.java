/*Add wait in ViewTest#setUp.

There are some cases where setUp() finished without activity has gained a focus,
resulting in failure in following 2 checks:
  assertTrue(view.hasWindowFocus())
  assertTrue(editText.hasCalledOnCreateInputConnection())

To avoid this, I added wait to make sure that activity has gained focus within setUp().*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 726b0f7..9fc57b2 100644

//Synthetic comment -- @@ -104,6 +104,13 @@
mActivity = getActivity();
mResources = mActivity.getResources();
mMockParent = new MockViewParent(mActivity);

        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return mActivity.hasWindowFocus();
            }
        }.run();
}

@TestTargets({







