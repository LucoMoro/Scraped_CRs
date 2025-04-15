/*Fix for Screen Blank issue when accepting call.

This fixes the issue of screen going blank while the user
is in the process of accepting a call, caused by 6d6523c51.
Adding the flag ON_AFTER_RELEASE to reset user activity timer
when wake lock is released. That will keep the screen on for
a while before it goes to blank.

Change-Id:I6aef162d94bb557c045a268ec23760d887c52859*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneApp.java b/src/com/android/phone/PhoneApp.java
//Synthetic comment -- index e519102..ed0dea8 100644

//Synthetic comment -- @@ -463,7 +463,8 @@
// before registering for phone state changes
PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP,
LOG_TAG);
// lock used to keep the processor awake, when we don't care for the display.
mPartialWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK







