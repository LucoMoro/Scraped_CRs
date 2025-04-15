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
        final TextView textView = new TextView(mActivity);
        setSpannableText(textView, "This is a really long sentence"
                + " which can not be placed in one line on the screen.");

        // Narrow layout assures that the text will get wrapped.
        FrameLayout innerLayout = new FrameLayout(mActivity);
        innerLayout.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        innerLayout.addView(textView);

        final FrameLayout layout = new FrameLayout(mActivity);
        layout.addView(innerLayout);

mActivity.runOnUiThread(new Runnable() {
public void run() {
                mActivity.setContentView(layout);
                textView.setSingleLine(true);
}
});
mInstrumentation.waitForIdleSync();

        assertEquals(SingleLineTransformationMethod.getInstance(),
                textView.getTransformationMethod());
        int singleLineWidth = textView.getLayout().getWidth();
        int singleLineHeight = textView.getLayout().getHeight();

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                textView.setSingleLine(false);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(null, textView.getTransformationMethod());
        assertTrue(textView.getLayout().getHeight() > singleLineHeight);
        assertTrue(textView.getLayout().getWidth() < singleLineWidth);

// same behaviours as setSingLine(true)
mActivity.runOnUiThread(new Runnable() {
public void run() {
                textView.setSingleLine();
}
});
mInstrumentation.waitForIdleSync();
assertEquals(SingleLineTransformationMethod.getInstance(),
                textView.getTransformationMethod());
        assertEquals(singleLineHeight, textView.getLayout().getHeight());
        assertEquals(singleLineWidth, textView.getLayout().getWidth());
}

@TestTargetNew(







