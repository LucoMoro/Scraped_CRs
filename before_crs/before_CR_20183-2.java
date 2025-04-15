/*Set 'https' proxy system properties using the 'http' settings, allowing downloads via an HTTPS proxy to work.

Change-Id:I95fd0f377df4ff4372e8a7ca18cfc4195bbfc177*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 4d10b54..34b2472 100755

//Synthetic comment -- @@ -269,10 +269,18 @@
*/
public void applySettings() {
Properties props = System.getProperties();
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_HOST,
                mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_HOST, "")); //$NON-NLS-1$
        props.setProperty(ISettingsPage.KEY_HTTP_PROXY_PORT,
                mProperties.getProperty(ISettingsPage.KEY_HTTP_PROXY_PORT, ""));   //$NON-NLS-1$
    }

}







