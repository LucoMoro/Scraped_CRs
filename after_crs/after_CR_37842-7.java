/*Don't direct to market if no markets installed

The search intent cannot be resolved if there aren't any
market apps installed, so it will cause Settings crashed by
the ActivityNotFound exception.

So we need to check if the market intent requst can be resolved.
Then determine to notify user to direct the accessibility services
to market to get TalkBack or not.

Change-Id:I0f0d7b54eb5215e1a886315bb6816704384f6c59Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index 827af13..736b4ba 100644

//Synthetic comment -- @@ -181,9 +181,8 @@
super.onResume();
loadInstalledServices();
updateAllPreferences();
        offerInstallAccessibilitySerivceOnce();

mSettingsPackageMonitor.register(getActivity(), false);
}

//Synthetic comment -- @@ -488,6 +487,17 @@
final boolean offerInstallService = !preferences.getBoolean(
KEY_INSTALL_ACCESSIBILITY_SERVICE_OFFERED_ONCE, false);
if (offerInstallService) {
            String screenreaderMarketLink = SystemProperties.get(
                    SYSTEM_PROPERTY_MARKET_URL,
                    DEFAULT_SCREENREADER_MARKET_LINK);
            Uri marketUri = Uri.parse(screenreaderMarketLink);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

            if (getPackageManager().resolveActivity(marketIntent, 0) == null) {
                // Don't show the dialog if no market app is found/installed.
                return;
            }

preferences.edit().putBoolean(KEY_INSTALL_ACCESSIBILITY_SERVICE_OFFERED_ONCE,
true).commit();
// Notify user that they do not have any accessibility







