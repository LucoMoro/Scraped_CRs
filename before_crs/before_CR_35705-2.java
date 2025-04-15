/*Fix Setting->Display always force close issue

CTS's AdminTest will change the display timeout to 5 seconds.
It will cause Setting->Display 100% force close. It has been reported inhttp://code.google.com/p/android/issues/detail?id=27026. This patch is
for solving this issue.

Change-Id:Icf4061023f040ee164d161b9db717acbc9697124*/
//Synthetic comment -- diff --git a/src/com/android/settings/DisplaySettings.java b/src/com/android/settings/DisplaySettings.java
//Synthetic comment -- index 5887140..4410d46 100644

//Synthetic comment -- @@ -107,7 +107,7 @@
private void updateTimeoutPreferenceDescription(long currentTimeout) {
ListPreference preference = mScreenTimeoutPreference;
String summary;
        if (currentTimeout < 0) {
// Unsupported value
summary = "";
} else {







