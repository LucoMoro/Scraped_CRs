/*Modified so that assertLoadUrlSuccessfully() will use WebChromeClient class.

There is a process to wait view.getProgress() in assertLoadUrlSuccessfully() within the test of  WebHistoryItemTest to become 100%.

Since getProgress() is already 100 before the page starts to load, there are some cases where this test fails due to process gets out of DelayedCheck before page is loaded.

To avoid this, I made change so that progress is checked within onProgressChanged of WebChromeClient class to become 100%.*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 016d566..1bdf35e 100644

//Synthetic comment -- @@ -40,6 +40,10 @@
super("com.android.cts.stub", WebViewStubActivity.class);
}

    class Sync {
        public boolean mIsDone;
    }

@Override
protected void setUp() throws Exception {
super.setUp();
//Synthetic comment -- @@ -141,13 +145,28 @@
}

private void assertLoadUrlSuccessfully(final WebView view, String url) {
        final Sync mSync = new Sync();
        view.setWebChromeClient ( new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                synchronized(mSync) {
                    if( progress == 100 ){
                        mSync.mIsDone = true;
                        mSync.notify();
                    }
                }
            }
        } );

view.loadUrl(url);
// wait for the page load to complete
        synchronized(mSync) {
            try {
                if( mSync.mIsDone == false ){
                    mSync.wait(10000,0);
                }
}
            catch(InterruptedException e) {
            }
        }
}
}







