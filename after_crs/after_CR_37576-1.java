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
    private boolean         mAllowUniversalAccessFromFileURLs = true;
    private boolean         mAllowFileAccessFromFileURLs = true;
private boolean         mHardwareAccelSkia = false;
private boolean         mShowVisualIndicator = false;
private PluginState     mPluginState = PluginState.OFF;
//Synthetic comment -- @@ -1264,6 +1266,47 @@
}

/**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from any origin. This includes
     * access to content from other file scheme URLs. See
     * {@link #setAllowFileAccessFromFileURLs}. To enable the most restrictive,
     * and therefore secure policy, this setting should be disabled.
     * <p>
     * The default value is true.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from any origin
     * @hide
     */
    public synchronized void setAllowUniversalAccessFromFileURLs(boolean flag) {
        if (mAllowUniversalAccessFromFileURLs != flag) {
            mAllowUniversalAccessFromFileURLs = flag;
            postSync();
        }
    }

    /**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from other file scheme URLs. To
     * enable the most restrictive, and therefore secure policy, this setting
     * should be disabled. Note that the value of this setting is ignored if
     * the value of {@link #getAllowUniversalAccessFromFileURLs} is true.
     * <p>
     * The default value is true.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from other file
     *             scheme URLs
     * @hide
     */
    public synchronized void setAllowFileAccessFromFileURLs(boolean flag) {
        if (mAllowFileAccessFromFileURLs != flag) {
            mAllowFileAccessFromFileURLs = flag;
            postSync();
        }
    }

    /**
* Tell the WebView to use Skia's hardware accelerated rendering path
* @param flag True if the WebView should use Skia's hw-accel path
* @hide
//Synthetic comment -- @@ -1500,6 +1543,33 @@
}

/**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from any origin. This includes access to content from
     * other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from any origin
     * @see #setAllowUniversalAccessFromFileURLs
     * @hide
     */
    public synchronized boolean getAllowUniversalAccessFromFileURLs() {
        return mAllowUniversalAccessFromFileURLs;
    }

    /**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from other file scheme URLs
     * @see #setAllowFileAccessFromFileURLs
     * @hide
     */
    public synchronized boolean getAllowFileAccessFromFileURLs() {
        return mAllowFileAccessFromFileURLs;
    }

    /**
* Return true if plugins are enabled.
* @return True if plugins are enabled.
* @deprecated This method has been replaced by {@link #getPluginState}







