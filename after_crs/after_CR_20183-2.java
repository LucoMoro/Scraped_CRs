/*Set 'https' proxy system properties using the 'http' settings, allowing downloads via an HTTPS proxy to work.

Change-Id:I95fd0f377df4ff4372e8a7ca18cfc4195bbfc177*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 4d10b54..34b2472 100755

//Synthetic comment -- @@ -269,10 +269,18 @@
*/
public void applySettings() {
Properties props = System.getProperties();

        // Get the configured HTTP proxy settings
        String proxyHost = mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
                ""); //$NON-NLS-1$
        String proxyPort = mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
                ""); //$NON-NLS-1$

        // Set both the HTTP and HTTPS proxy system properties
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_HOST, proxyHost);
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_PORT, proxyPort);
        props.setProperty("https.proxyHost", proxyHost); //$NON-NLS-1$
        props.setProperty("https.proxyPort", proxyPort); //$NON-NLS-1$
     }

}







