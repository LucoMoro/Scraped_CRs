/*Fix WebView.loadData ignores encoding on Android 3.x and after

In WebView.loadData encoding parameter was ignored (expect if the value was 'base64').
Now, charset encoding is used to display the data.

The following call should display well.
webView.loadData("�����", "text/plain", "utf-8") will display '�����' where it display garbage in version > 3.x.
webview.loadData("aGVsb68=", "text/plain", "base64") will still display 'hello'.
webview.loadData("6eDn6Pk=", "text/plain", "utf-8,base64") will show '�����' where it display garbage in the current version.
webview.loadDataWithBaseURL(url, "�����", "text/plain", "utf-8", null) will still display '�����' if (url is null or different of "data:").
webView.loadDataWithBaseURL("data:", "�����", "text/plain", "utf-8", null) will display '�����' where it display garbage in version > 3.x

This assure downward compatibility with the change made in 3.x

This fix a bug that appears in the 3.x android version.
Fix issues:
19747http://code.google.com/p/android/issues/detail?id=1974719306http://code.google.com/p/android/issues/detail?id=1930617163http://code.google.com/p/android/issues/detail?id=17163Sources:
-http://en.wikipedia.org/wiki/Data_URI_scheme-http://tools.ietf.org/html/rfc2397Amends:
remove + string concat
remove "or base64" from the url encoded case
integrate jb remastering
rebase to master

Change-Id:I518d916204423f3fc78f76e4e868e5d5377c1ceaSigned-off-by: Guillaume Ch�telet <guillaume.chatelet@orange.com>*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index e493653..b16c96c 100644

//Synthetic comment -- @@ -792,15 +792,20 @@
* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
* If the value of the encoding parameter is 'base64', then the data must
     * be encoded as base64 US-ASCII data.
     * If the value of the encoding parameter is a {@code charset} (for example
     * 'utf-8'), then the data must be encoded as {@code charset} data.
     * If the value of the encoding parameter is 'charset;base64', then the
     * data must be encoded as base64 {@code charset} data.
     * <p>
     * If the value of encoding parameter is null, then the data
     * must use US-ASCII encoding for
* octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example,
* '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
* The 'data' scheme URL formed by this method uses the default US-ASCII
     * charset.
     * <p>
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
*








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index 34c08ef..bfded4f 100644

//Synthetic comment -- @@ -2534,6 +2534,9 @@
dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
dataUrl.append(";base64");
        } else if (encoding != null) {
            dataUrl.append(";charset=");
            dataUrl.append(encoding);
}
dataUrl.append(",");
dataUrl.append(data);







