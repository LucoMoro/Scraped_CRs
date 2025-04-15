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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.net.http.SslError;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.AttributeSet;
//Synthetic comment -- @@ -1631,28 +1631,27 @@
public void testAccessCertificate() throws Throwable {
runTestOnUiThread(new Runnable() {
public void run() {
                mWebView = new MockWebView(getActivity());
getActivity().setContentView(mWebView);
}
});
getInstrumentation().waitForIdleSync();

        final MockWebView mockWebView = (MockWebView) mWebView;
// need the client to handle error
        mockWebView.setWebViewClient(new MockWebViewClient());

        mockWebView.reset();
startWebServer(true);
String url = mWebServer.getAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
// attempt to load the url.
        mockWebView.loadUrl(url);
new DelayedCheck(TEST_TIMEOUT) {
@Override
protected boolean check() {
                return mockWebView.hasCalledSetCertificate();
}
}.run();
        SslCertificate cert = mockWebView.getCertificate();
assertNotNull(cert);
assertEquals("Android", cert.getIssuedTo().getUName());
}
//Synthetic comment -- @@ -1962,28 +1961,6 @@
// Do not test these APIs. They are implementation details.
}

    private static class MockWebView extends WebView {
        private boolean mHasCalledSetCertificate;

        public MockWebView(Context context) {
            super(context);
        }

        @Override
        public void setCertificate(SslCertificate certificate) {
            super.setCertificate(certificate);
            mHasCalledSetCertificate = true;
        }

        public void reset() {
            mHasCalledSetCertificate = false;
        }

        public boolean hasCalledSetCertificate() {
            return mHasCalledSetCertificate;
        }
    }

private static class MockWebViewClient extends WebViewClient {
private boolean mOnScaleChangedCalled = false;








