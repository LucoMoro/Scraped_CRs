/*Webview needs to set instance of ChromeClient to use getProgress value.
assertLoadUrlSuccessfully is using view.getProgress() which returns
no meaningful value  without ChromeClient.*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java b/tests/tests/webkit/src/android/webkit/cts/WebHistoryItemTest.java
//Synthetic comment -- index 4f73cdc..7489aea 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@TestTargetClass(android.webkit.WebHistoryItem.class)
public class WebHistoryItemTest extends ActivityInstrumentationTestCase2<WebViewStubActivity> {
//Synthetic comment -- @@ -75,6 +76,7 @@
})
public void testWebHistoryItem() {
final WebView view = getActivity().getWebView();
WebBackForwardList list = view.copyBackForwardList();
assertEquals(0, list.getSize());








