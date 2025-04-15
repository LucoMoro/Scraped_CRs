/*Don't direct to market if no markets installed

The search intent cannot be resolved if there aren't any
market apps installed, so it will cause Settings crashed by
the ActivityNotFound exception.

So we need to check if the market intent requst can be resolved.
Then determine to notify user to direct the accessibility services
to market to get TalkBack or not.

Change-Id:I0f0d7b54eb5215e1a886315bb6816704384f6c59Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index 6167f78..89937ec 100644

//Synthetic comment -- @@ -179,9 +179,9 @@
super.onResume();
loadInstalledServices();
updateAllPreferences();
        if (mServicesCategory.getPreference(0) == mNoServicesMessagePreference) {
            offerInstallAccessibilitySerivceOnce();
        }
mSettingsPackageMonitor.register(getActivity(), getActivity().getMainLooper(), false);
RotationPolicy.registerRotationPolicyListener(getActivity(),
mRotationPolicyListener);
//Synthetic comment -- @@ -465,6 +465,17 @@
final boolean offerInstallService = !preferences.getBoolean(
KEY_INSTALL_ACCESSIBILITY_SERVICE_OFFERED_ONCE, false);
if (offerInstallService) {
preferences.edit().putBoolean(KEY_INSTALL_ACCESSIBILITY_SERVICE_OFFERED_ONCE,
true).commit();
// Notify user that they do not have any accessibility







