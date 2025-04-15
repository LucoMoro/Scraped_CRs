/*Remove Broken WebKit Tests

Bug 3188260

Change-Id:I0c57a1035b7286a69198173a971de67808b0e46f*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 016d566..71ba504 100644

//Synthetic comment -- @@ -104,42 +104,6 @@
assertTrue(firstId != secondId);
}

    @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getOriginalUrl",
            args = {}
    )
    @ToBeFixed(explanation = "History item does not have the original URL set after a redirect.")
    @BrokenTest(value = "Bug 2121787: Test times out on the host side. Not 100% reproducible.")
    public void testRedirect() throws InterruptedException {
        final WebView view = getActivity().getWebView();
        view.setWebChromeClient(new WebChromeClient());
        // set the web view client so that redirects are loaded in the WebView itself
        view.setWebViewClient(new WebViewClient());
        WebBackForwardList list = view.copyBackForwardList();
        assertEquals(0, list.getSize());

        String url = mWebServer.getAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
        String redirect = mWebServer.getRedirectingAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
        assertLoadUrlSuccessfully(view, redirect);
        // wait for the redirect to take place
        new DelayedCheck(10000) {
            @Override
            protected boolean check() {
                WebBackForwardList list = view.copyBackForwardList();
                return list.getSize() >= 1;
            }
        }.run();
        list = view.copyBackForwardList();
        assertEquals(1, list.getSize());
        WebHistoryItem item = list.getCurrentItem();
        assertNotNull(item);
        assertEquals(url, item.getUrl());
        assertEquals(TestHtmlConstants.HELLO_WORLD_TITLE, item.getTitle());
        // To be fixed: item.getOriginalUrl() returns null
        // assertEquals(redirect, item.getOriginalUrl());
    }

private void assertLoadUrlSuccessfully(final WebView view, String url) {
view.loadUrl(url);
// wait for the page load to complete








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebIconDatabaseTest.java b/tests/tests/webkit/src/android/webkit/cts/WebIconDatabaseTest.java
deleted file mode 100644
//Synthetic comment -- index 64b5204..0000000

//Synthetic comment -- @@ -1,262 +0,0 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.webkit.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.animation.cts.DelayedCheck;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;

import java.io.File;

@TestTargetClass(android.webkit.WebIconDatabase.class)
public class WebIconDatabaseTest extends
                 ActivityInstrumentationTestCase2<WebViewStubActivity> {
    private static final long ICON_FETCH_TIMEOUT = 15000;
    private static final String DATA_FOLDER = "/webkittest/";
    private String mFilePath;
    private WebView mWebView;
    private CtsTestServer mWebServer;

    /**
     * Instantiates a new text view test.
     */
    public WebIconDatabaseTest() {
        super("com.android.cts.stub", WebViewStubActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        WebViewStubActivity activity = (WebViewStubActivity) getActivity();
        mFilePath = activity.getFilesDir().toString() + DATA_FOLDER;
        clearDatabasePath();

        mWebView = activity.getWebView();
        mWebView.clearCache(true);
    }

    @Override
    protected void tearDown() throws Exception {
        clearDatabasePath();
        if (mWebServer != null) {
            mWebServer.shutdown();
        }
        super.tearDown();
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "open",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "close",
            args = {}
        )
    })
    @BrokenTest(value="intermittently fails bug 2250024")
    public void testOpen() {
        final WebIconDatabase webIconDatabase = WebIconDatabase.getInstance();

        final File path = new File(mFilePath);
        // To assure no files under the directory
        assertNull(path.listFiles());
        // open() should create and open database file for storing icon related datum.
        webIconDatabase.open(mFilePath);

        // Need to wait for a moment, let the internal Handler complete the operation
        new DelayedCheck(10000) {
            @Override
            protected boolean check() {
                return path.listFiles() != null;
            }
        }.run();

        assertTrue(path.listFiles().length > 0);

        webIconDatabase.close();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getInstance",
        args = {}
    )
    @UiThreadTest
    public void testGetInstance() {
        WebIconDatabase webIconDatabase1 = WebIconDatabase.getInstance();
        WebIconDatabase webIconDatabase2 = WebIconDatabase.getInstance();

        assertSame(webIconDatabase1, webIconDatabase2);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "retainIconForPageUrl",
            args = {String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "requestIconForPageUrl",
            args = {String.class, WebIconDatabase.IconListener.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "releaseIconForPageUrl",
            args = {String.class}
        )
    })
    @BrokenTest(value="intermittently fails bug 2250024")
    public void testRetainIconForPageUrl() throws Exception {
        final WebIconDatabase webIconDatabase = WebIconDatabase.getInstance();
        webIconDatabase.open(mFilePath);

        mWebServer = new CtsTestServer(getActivity());
        String url = mWebServer.getAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
        assertLoadUrlSuccessfully(mWebView, url);

        MyIconListener listener = new MyIconListener();

        webIconDatabase.retainIconForPageUrl(url);

        webIconDatabase.requestIconForPageUrl(url, listener);

        listener.waitForIcon(ICON_FETCH_TIMEOUT);
        assertTrue(listener.hasReceivedStatus());
        assertNotNull(listener.getIcon());

        // release the icon.
        webIconDatabase.releaseIconForPageUrl(url);

        listener = new MyIconListener();
        webIconDatabase.requestIconForPageUrl(url, listener);

        listener.waitForIcon(ICON_FETCH_TIMEOUT);

        assertTrue(listener.hasReceivedStatus());
        assertNotNull(listener.getIcon());

        webIconDatabase.close();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "removeAllIcons",
        args = {}
    )
    @BrokenTest(value="intermittently fails bug 2250024")
    public void testRemoveAllIcons() throws Exception {
        final WebIconDatabase webIconDatabase = WebIconDatabase.getInstance();
        webIconDatabase.open(mFilePath);

        mWebServer = new CtsTestServer(getActivity());
        String url = mWebServer.getAssetUrl(TestHtmlConstants.HELLO_WORLD_URL);
        assertLoadUrlSuccessfully(mWebView, url);

        MyIconListener listener = new MyIconListener();

        webIconDatabase.retainIconForPageUrl(url);

        webIconDatabase.requestIconForPageUrl(url, listener);

        listener.waitForIcon(ICON_FETCH_TIMEOUT);
        assertTrue(listener.hasReceivedStatus());
        assertNotNull(listener.getIcon());

        // remove all icons.
        webIconDatabase.removeAllIcons();
        
        listener = new MyIconListener();
        webIconDatabase.requestIconForPageUrl(url, listener);

        listener.waitForIcon(ICON_FETCH_TIMEOUT);

        assertFalse(listener.hasReceivedStatus());
        assertNull(listener.getIcon());

        webIconDatabase.close();
    }

    private static class MyIconListener implements WebIconDatabase.IconListener {
        private Bitmap mIcon;
        private String mUrl;
        private boolean mHasReceivedIcon = false;

        public synchronized void onReceivedIcon(String url, Bitmap icon) {
            mHasReceivedIcon = true;
            mIcon = icon;
            mUrl = url;
            notifyAll();
        }

        public synchronized void waitForIcon(long timeout) throws InterruptedException {
            if (!mHasReceivedIcon) {
                wait(timeout);
            }
        }

        public boolean hasReceivedStatus() {
            return mHasReceivedIcon;
        }

        public Bitmap getIcon() {
            return mIcon;
        }

        public String getUrl() {
            return mUrl;
        }
    }

    private void clearDatabasePath() throws InterruptedException {
        File path = new File(mFilePath);
        if (path.exists()) {
            // FIXME: WebIconDatabase.close() is asynchronous, so some files may still be in use
            // after we return. Wait some time for the files to be closed.
            Thread.sleep(1000);
            File[] files = path.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    assertTrue(files[i].delete());
                }
            }
            path.delete();
        }
    }

    private void assertLoadUrlSuccessfully(final WebView view, String url) {
        view.loadUrl(url);
        new DelayedCheck(10000) {
            @Override
            protected boolean check() {
                return view.getProgress() == 100;
            }
        }.run();
    }
}








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index b9d054f..b29cc84 100644

//Synthetic comment -- @@ -1352,39 +1352,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        method = "clearFormData",
        args = {}
    )
    @BrokenTest(value = "Causes the process to crash some time after test completion.")
    public void testClearFormData() throws Throwable {
        String form = "<form><input type=\"text\" name=\"testClearFormData\"></form>";
        mWebView.loadData("<html><body>" + form + "</body></html>", "text/html", "UTF-8");
        waitForLoadComplete(mWebView, TEST_TIMEOUT);
        moveFocusDown();
        getInstrumentation().sendStringSync("test");
        sendKeys(KeyEvent.KEYCODE_ENTER);

        mWebView.reload();
        waitForLoadComplete(mWebView, TEST_TIMEOUT);
        moveFocusDown();
        View input = mWebView.findFocus();
        assertTrue(input instanceof AutoCompleteTextView);
        getInstrumentation().sendStringSync("te");
        assertTrue(((AutoCompleteTextView) input).isPopupShowing());

        mWebView.reload();
        waitForLoadComplete(mWebView, TEST_TIMEOUT);
        moveFocusDown();
        mWebView.clearFormData();
        // no auto completion choice after clearing
        input = mWebView.findFocus();
        assertTrue(input instanceof AutoCompleteTextView);
        getInstrumentation().sendStringSync("te");
        assertFalse(((AutoCompleteTextView) input).isPopupShowing());
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
method = "getHitTestResult",
args = {}
)
//Synthetic comment -- @@ -1669,42 +1636,6 @@
mWebView.clearSslPreferences();
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "pauseTimers",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "resumeTimers",
            args = {}
        )
    })
    @ToBeFixed(explanation = "WebView.pauseTimers() does not pause javascript timers")
    @BrokenTest(value = "Frequently crashes the process some time after test completion.")
    public void testPauseTimers() throws Exception {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        startWebServer(false);
        // load a page which increments the number in its title every second
        String url = mWebServer.getAssetUrl(TestHtmlConstants.TEST_TIMER_URL);
        assertLoadUrlSuccessfully(mWebView, url);
        int counter = Integer.parseInt(mWebView.getTitle());
        Thread.sleep(2000);
        assertTrue(Integer.parseInt(mWebView.getTitle()) > counter);
        mWebView.pauseTimers();
        Thread.sleep(2000); // give the implementation time to stop the timer
        counter = Integer.parseInt(mWebView.getTitle());
        Thread.sleep(2000);
        // ToBeFixed: Uncomment the following line once pauseTimers() is fixed
        // assertEquals(counter, Integer.parseInt(mWebView.getTitle()));
        mWebView.resumeTimers();
        Thread.sleep(2000);
        assertTrue(Integer.parseInt(mWebView.getTitle()) > counter);
    }


@TestTargetNew(
level = TestLevel.COMPLETE,
method = "requestChildRectangleOnScreen",







