/*Phone: Prevent immediate screen-off after call-answer

Acquire the full wakelock with ON_AFTER_RELEASE flag to
ensure screen stays on for a little longer. Therefore
prevent immediate screen-off after call-answer

Change-Id:Ib459e1c59889efe41d2b80f0e9eb7cd01031bd5e*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index 10099f8..c533846 100644

//Synthetic comment -- @@ -512,7 +512,8 @@
// before registering for phone state changes
PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP,
LOG_TAG);
// lock used to keep the processor awake, when we don't care for the display.
mPartialWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK







