/*The display density between different screens cause the cases RatingBarTest#testOnMeasure and WebViewTest#testFindNext fail. So this patch corrected the effect of density.

Change-Id:Iecd49ca7e946566afb62e2ffe382d6fe318bdbfe*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index bd805a3..a768a1b 100644

//Synthetic comment -- @@ -923,7 +923,9 @@
})
public void testFindNext() throws Throwable {
DisplayMetrics metrics = mWebView.getContext().getResources().getDisplayMetrics();
        int dimension = Math.max(metrics.widthPixels, metrics.heightPixels);
// create a paragraph high enough to take up the entire screen
String p = "<p style=\"height:" + dimension + "px;\">" +
"Find all instances of a word on the page and highlight them.</p>";








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/RatingBarTest.java b/tests/tests/widget/src/android/widget/cts/RatingBarTest.java
//Synthetic comment -- index 373e8e4..c54013b 100644

//Synthetic comment -- @@ -312,7 +312,8 @@
if (getActivity().getResources().getDisplayMetrics().density >= 1) {
WidgetTestUtils.assertScaledPixels(285, mRatingBar.getMeasuredWidth(), getActivity());
} else {
            assertEquals(285, mRatingBar.getMeasuredWidth());
}
assertEquals(57, mRatingBar.getMeasuredHeight());








