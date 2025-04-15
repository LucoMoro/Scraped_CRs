/*BrowserSettings required a Controller sometimes

BrowserSettings can be used without a browser Controller, e.g.
when accessed from system settings. But some null checks for
this case were missing, these have now been added.

Change-Id:I57762898d34ef3018569e0de1499be2922dabded*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 7d3195a..4555c18 100644

//Synthetic comment -- @@ -372,11 +372,11 @@
if (PREF_SEARCH_ENGINE.equals(key)) {
updateSearchEngine(false);
} else if (PREF_FULLSCREEN.equals(key)) {
            if (mController != null && mController.getUi() != null) {
mController.getUi().setFullscreen(useFullscreen());
}
} else if (PREF_ENABLE_QUICK_CONTROLS.equals(key)) {
            if (mController != null && mController.getUi() != null) {
mController.getUi().setUseQuickControls(sharedPreferences.getBoolean(key, false));
}
} else if (PREF_LINK_PREFETCH.equals(key)) {







