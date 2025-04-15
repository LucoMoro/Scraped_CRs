/*Modified so that LoadUrl() will use WebChromeClient class.

There is a process to wait view.getProgress() in LoadUrl() within the test of  WebSettingsTest to become 100%.

Since getProgress() is already 100 before the page starts to load, there are some cases where this test fails due to process gets out of DelayedCheck before page is loaded.

To avoid this, I made change so that progress is checked within onProgressChanged of WebChromeClient class to become 100%.*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index de80713..16124b2 100644

//Synthetic comment -- @@ -54,6 +54,10 @@
super("com.android.cts.stub", WebViewStubActivity.class);
}

    class Sync {
        public boolean mIsDone;
    }

@Override
protected void setUp() throws Exception {
super.setUp();
//Synthetic comment -- @@ -1028,13 +1032,28 @@
* @param url The URL of the page to load.
*/
private void loadUrl(String url) {
        final Sync mSync = new Sync();
        mWebView.setWebChromeClient ( new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                synchronized(mSync) {
                    if( progress == 101 ){
                        mSync.mIsDone = true;
                        mSync.notify();
                    }
                }
}
        } );

        mWebView.loadUrl(url);
        synchronized(mSync) {
            try {
                if( mSync.mIsDone == false ){
                    mSync.wait(10000,0);
                }
            }
            catch(InterruptedException e) {
            }
        }
assertEquals(100, mWebView.getProgress());
}
}







