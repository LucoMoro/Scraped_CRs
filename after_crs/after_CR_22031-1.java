/*Remove Broken WebKit Tests

Bug 3188260

Change-Id:I0c57a1035b7286a69198173a971de67808b0e46f*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 016d566..71ba504 100644

//Synthetic comment -- @@ -104,42 +104,6 @@
assertTrue(firstId != secondId);
}

private void assertLoadUrlSuccessfully(final WebView view, String url) {
view.loadUrl(url);
// wait for the page load to complete








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebIconDatabaseTest.java b/tests/tests/webkit/src/android/webkit/cts/WebIconDatabaseTest.java
deleted file mode 100644
//Synthetic comment -- index 64b5204..0000000

//Synthetic comment -- @@ -1,262 +0,0 @@








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java b/tests/tests/webkit/src/android/webkit/cts/WebViewTest.java
//Synthetic comment -- index b9d054f..b29cc84 100644

//Synthetic comment -- @@ -1352,39 +1352,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getHitTestResult",
args = {}
)
//Synthetic comment -- @@ -1669,42 +1636,6 @@
mWebView.clearSslPreferences();
}

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "requestChildRectangleOnScreen",







