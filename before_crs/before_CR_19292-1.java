/*onJsBeforeUnload in PopularUrlsTest

The test suite PopularUrlsTest hang when running a page that
contains a script that uses onJsBeforeUnload.

Added automatic response to onJsBeforeUnload to PopularUrlTest
so test suite answers dialog and continues. Positive answer on
dialog means move away from the page.

Change-Id:Idb51e514baa7d98b224ddcf1f20321b40d91ba30*/
//Synthetic comment -- diff --git a/tests/src/com/android/browser/PopularUrlsTest.java b/tests/src/com/android/browser/PopularUrlsTest.java
//Synthetic comment -- index d3806a0..d206e94 100644

//Synthetic comment -- @@ -173,6 +173,20 @@

return true;
}
});

webView.setWebViewClient(new TestWebViewClient(webView.getWebViewClient()) {







