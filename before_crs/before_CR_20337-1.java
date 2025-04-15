/*Webview needs to set instance of ChromeClient to use getProgress value.
assertLoadUrlSuccessfully is using view.getProgress() which returns
no meaningful value  without ChromeClient.

Change-Id:Id10164dca9d64778cc9aa19474233d6d6eb9de3f*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 4f73cdc..016d566 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//Synthetic comment -- @@ -75,6 +76,7 @@
})
public void testWebHistoryItem() {
final WebView view = getActivity().getWebView();
WebBackForwardList list = view.copyBackForwardList();
assertEquals(0, list.getSize());

//Synthetic comment -- @@ -111,6 +113,7 @@
@BrokenTest(value = "Bug 2121787: Test times out on the host side. Not 100% reproducible.")
public void testRedirect() throws InterruptedException {
final WebView view = getActivity().getWebView();
// set the web view client so that redirects are loaded in the WebView itself
view.setWebViewClient(new WebViewClient());
WebBackForwardList list = view.copyBackForwardList();
//Synthetic comment -- @@ -137,15 +140,6 @@
// assertEquals(redirect, item.getOriginalUrl());
}

    @TestTargetNew(
        level = TestLevel.NOT_FEASIBLE,
        notes = "clone() is protected and WebHistoryItem cannot be subclassed",
        method = "clone",
        args = {}
    )
    public void testClone() {
    }

private void assertLoadUrlSuccessfully(final WebView view, String url) {
view.loadUrl(url);
// wait for the page load to complete







