/*Stop loading subview in stopAllLoading

The main webview is stopped twice instead of stopping
the subview loading. Correcting this cut and paste error.

Change-Id:Ic12c7219d222d42f59e27f1cda0cd65a003b41ca*/
//Synthetic comment -- diff --git a/src/com/android/browser/TabControl.java b/src/com/android/browser/TabControl.java
//Synthetic comment -- index 993cd11..150ece0 100644

//Synthetic comment -- @@ -571,7 +571,7 @@
}
final WebView subview = t.getSubWebView();
if (subview != null) {
                webview.stopLoading();
}
}
}







