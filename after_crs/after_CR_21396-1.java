/*Add wait in TextViewTest#setUp.

There are some cases where setUp() finished without activity has gained a focus,
resulting in failure in following 2 checks:
  assertTrue(mTextView.isFocused())
  assertTrue(mTextView.isInputMethodTarget())

To avoid this, I added wait to make sure that activity has gained focus within setUp().*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
//Synthetic comment -- index 98b5962..82cc48c 100644

//Synthetic comment -- @@ -123,6 +123,13 @@
super.setUp();
mActivity = getActivity();
mInstrumentation = getInstrumentation();

        new DelayedCheck(TIMEOUT) {
            @Override
            protected boolean check() {
                return mActivity.hasWindowFocus();
            }
        }.run();
}

@TestTargets({







