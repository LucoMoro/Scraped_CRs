/*DO NOT MERGE Add API for file origin policy.

Bug: 6212665

Add hidden websettings api for configuring file origin policy.

Change-Id:I261ba6369fe606ca76f87c6a00d1168b44bcf1ab*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebSettings.java b/core/java/android/webkit/WebSettings.java
//Synthetic comment -- index f947f95..e2074ec 100644

//Synthetic comment -- @@ -181,6 +181,8 @@
private boolean         mBlockNetworkImage = false;
private boolean         mBlockNetworkLoads;
private boolean         mJavaScriptEnabled = false;
private boolean         mHardwareAccelSkia = false;
private boolean         mShowVisualIndicator = false;
private PluginState     mPluginState = PluginState.OFF;
//Synthetic comment -- @@ -1264,6 +1266,47 @@
}

/**
* Tell the WebView to use Skia's hardware accelerated rendering path
* @param flag True if the WebView should use Skia's hw-accel path
* @hide
//Synthetic comment -- @@ -1500,6 +1543,33 @@
}

/**
* Return true if plugins are enabled.
* @return True if plugins are enabled.
* @deprecated This method has been replaced by {@link #getPluginState}







