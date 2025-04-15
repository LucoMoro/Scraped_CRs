/*Fixed memory leak in Settings open source license activity.

The SettingsLicenseActivity leaks memory, via the webview, when
changing orientation or restarting the application.

Change-Id:Ia51e18c9dc7b24da224a8305bdb448c8621897bf*/
//Synthetic comment -- diff --git a/src/com/android/settings/SettingsLicenseActivity.java b/src/com/android/settings/SettingsLicenseActivity.java
//Synthetic comment -- index 0b809e1..d50c808 100644

//Synthetic comment -- @@ -46,6 +46,8 @@
private static final String DEFAULT_LICENSE_PATH = "/system/etc/NOTICE.html.gz";
private static final String PROPERTY_LICENSE_PATH = "ro.config.license_path";

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -95,11 +97,11 @@
return;
}

        WebView webView = new WebView(this);

// Begin the loading.  This will be done in a separate thread in WebView.
        webView.loadDataWithBaseURL(null, data.toString(), "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {
@Override
public void onPageFinished(WebView view, String url) {
// Change from 'Loading...' to the real title
//Synthetic comment -- @@ -109,7 +111,7 @@

final AlertController.AlertParams p = mAlertParams;
p.mTitle = getString(R.string.settings_license_activity_loading);
        p.mView = webView;
p.mForceInverseBackground = true;
setupAlert();
}
//Synthetic comment -- @@ -120,4 +122,12 @@
finish();
}

}







