/*AppWidgetService avoid race condition

Race condition could cause AppWidget hosts to be denied access to their own
widgets. I have not seen this issue manifest on the stock Nexus One or Nexus S,
however since it's a race condition that doesn't mean it can't happen.

This has been especially an issue on the AOSP-derivative CyanogenMod,
when using WidgetLocker along with a normal launcher, and then using the
CM Theme Chooser.

CyanogenMod Issue # 3167 (http://code.google.com/p/cyanogenmod/issues/detail?id=3167)

Change-Id:If5406663a60a8e53a831d3e8e5dd67e7173771d7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/AppWidgetService.java b/services/java/com/android/server/AppWidgetService.java
//Synthetic comment -- index 731fb22..d5757a3 100644

//Synthetic comment -- @@ -126,8 +126,10 @@
public void systemReady(boolean safeMode) {
mSafeMode = safeMode;

        loadAppWidgetList();
        loadStateLocked();

// Register for the boot completed broadcast, so we can send the
// ENABLE broacasts.  If we try to send them now, they time out,







