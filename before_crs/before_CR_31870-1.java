/*Partial Fix for 17535 - code.google.com/p/android/issues/detail?id=17535

inputStreamForAndroidResource, which handles opening of resources, assets and content
did not handle the '#' or '?' characters in URIs properly.  This has been added.  However,
this does not fully fix the bug, since the Chrome Network Stack fails to load a URL
with '?' after the inputstream has been passed back from Android.

Change-Id:Ie348c03ec6c06be50b24a2fea9ab257754924f8fSigned-off-by: Joe Bowser <bowserj@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/webkit/BrowserFrame.java b/core/java/android/webkit/BrowserFrame.java
//Synthetic comment -- index 74d4833..78086c6 100644

//Synthetic comment -- @@ -753,12 +753,19 @@
}

// file:///android_asset
} else if (url.startsWith(ANDROID_ASSET)) {
url = url.replaceFirst(ANDROID_ASSET, "");
try {
AssetManager assets = mContext.getAssets();
return assets.open(url, AssetManager.ACCESS_STREAMING);
} catch (IOException e) {
return null;
}








