/*Fix TextViewTest#testSingleLine

Bug 2459518

Use the same technique used to fix testMarquee which is to put
the TextView into a narrow layout widget so it will wrap
regardless of display size.

Change-Id:Ic836df399f8147e6283d663e2ee72ab8839d7b6e*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
//Synthetic comment -- index aad008c..9bd8cc4 100644

//Synthetic comment -- @@ -2699,37 +2699,52 @@
)
})
public void testSingleLine() {
        // singleLine
        mTextView = findTextView(R.id.textview_singleLine);
        setSpannableText(mTextView, "This is a really long sentence"
                        + " which can not be placed in one line on the screen.");

        assertEquals(SingleLineTransformationMethod.getInstance(),
                mTextView.getTransformationMethod());
        int singleLineWidth = mTextView.getLayout().getWidth();
        int singleLineHeight = mTextView.getLayout().getHeight();

mActivity.runOnUiThread(new Runnable() {
public void run() {
                mTextView.setSingleLine(false);
}
});
mInstrumentation.waitForIdleSync();
        assertEquals(null, mTextView.getTransformationMethod());
        assertTrue(mTextView.getLayout().getHeight() > singleLineHeight);
        assertTrue(mTextView.getLayout().getWidth() < singleLineWidth);

// same behaviours as setSingLine(true)
mActivity.runOnUiThread(new Runnable() {
public void run() {
                mTextView.setSingleLine();
}
});
mInstrumentation.waitForIdleSync();
assertEquals(SingleLineTransformationMethod.getInstance(),
                mTextView.getTransformationMethod());
        assertEquals(singleLineHeight, mTextView.getLayout().getHeight());
        assertEquals(singleLineWidth, mTextView.getLayout().getWidth());
}

@TestTargetNew(







