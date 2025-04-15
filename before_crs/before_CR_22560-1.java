/*Fix Broken Build

I need to either figure out how to build these tests as part of
the cts target or delete them.

Change-Id:I258425c271fff3627460238c9389bb235512c7ee*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/CtsVerifierActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/CtsVerifierActivityTest.java
//Synthetic comment -- index c3e01b2..a7d5df5 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
super.setUp();
mActivity = getActivity();
mInstrumentation = getInstrumentation();
        mWelcomeTextView = (TextView) mActivity.findViewById(R.id.welcome);
mWelcomeText = mActivity.getString(R.string.welcome_text);
mContinueButton = (Button) mActivity.findViewById(R.id.continue_button);
mContinueText = mActivity.getString(R.string.continue_button_text);







