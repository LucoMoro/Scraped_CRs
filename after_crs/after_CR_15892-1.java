/*Fix WebViewTest#testAccessCertificate Assertion

Bug 2841873

setCertificate was being called twice once with a null and then with
a non-null certificate. The first call though set the
mHasCalledSetCertificate and then this caused the later not-null
assertion to fail. Get rid of those methods and just directly
use setCertificate and getCertificate to check that the certificate
is set properly.

Change-Id:I6710daa77e42a95f22b8ed9e7d8633fdb93e7f33*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index 0a0ef62..1e6625e 100644

//Synthetic comment -- @@ -33,11 +33,11 @@
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.AttributeSet;
//Synthetic comment -- @@ -1631,28 +1631,27 @@
public void testAccessCertificate() throws Throwable {
runTestOnUiThread(new Runnable() {
public void run() {
                mWebView = new WebView(getActivity());
getActivity().setContentView(mWebView);
}
});
getInstrumentation().waitForIdleSync();

// need the client to handle error
        mWebView.setWebViewClient(new MockWebViewClient());

        mWebView.setCertificate(null);
startWebServer(true);
String url = mWebServer.getAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
// attempt to load the url.
        mWebView.loadUrl(url);
new DelayedCheck(TEST_TIMEOUT) {
@Override
protected boolean check() {
                return mWebView.getCertificate() != null;
}
}.run();
        SslCertificate cert = mWebView.getCertificate();
assertNotNull(cert);
assertEquals("Android", cert.getIssuedTo().getUName());
}
//Synthetic comment -- @@ -1962,28 +1961,6 @@
// Do not test these APIs. They are implementation details.
}

private static class MockWebViewClient extends WebViewClient {
private boolean mOnScaleChangedCalled = false;








