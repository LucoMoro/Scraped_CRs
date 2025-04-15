/*Set 'https' proxy system properties using the 'http' settings, allowing downloads via an HTTPS proxy to work.

Change-Id:I95fd0f377df4ff4372e8a7ca18cfc4195bbfc177*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISettingsPage.java
//Synthetic comment -- index 739c479..d06f45c 100755

//Synthetic comment -- @@ -35,6 +35,16 @@
*/
public static final String KEY_HTTP_PROXY_HOST = "http.proxyHost";           //$NON-NLS-1$
/**
     * Java system setting picked up by {@link URL} for https proxy port.
     * Type: String.
     */
    public static final String KEY_HTTPS_PROXY_PORT = "https.proxyPort";           //$NON-NLS-1$
    /**
     * Java system setting picked up by {@link URL} for https proxy host.
     * Type: String.
     */
    public static final String KEY_HTTPS_PROXY_HOST = "https.proxyHost";           //$NON-NLS-1$
    /**
* Setting to force using http:// instead of https:// connections.
* Type: Boolean.
* Default: False.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 4d10b54..f432c3a 100755

//Synthetic comment -- @@ -269,10 +269,18 @@
*/
public void applySettings() {
Properties props = System.getProperties();

        // Set HTTP proxy properties using the configured settings
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_HOST, mProperties.getProperty(
                ISettingsPage.KEY_HTTP_PROXY_HOST, "")); //$NON-NLS-1$
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_PORT, mProperties.getProperty(
                ISettingsPage.KEY_HTTP_PROXY_PORT, "")); //$NON-NLS-1$

        // Set HTTPS proxy properties using the given HTTP proxy
        props.setProperty(ISettingsPage.KEY_HTTPS_PROXY_HOST, mProperties.getProperty(
                ISettingsPage.KEY_HTTP_PROXY_HOST, "")); //$NON-NLS-1$
        props.setProperty(ISettingsPage.KEY_HTTPS_PROXY_PORT, mProperties.getProperty(
                ISettingsPage.KEY_HTTP_PROXY_PORT, "")); //$NON-NLS-1$
}

}







