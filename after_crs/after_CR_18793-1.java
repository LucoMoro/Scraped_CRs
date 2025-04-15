/*Light behind keyboard does not get turned off in sleep mode

Key LED does not turn off when entering into sleep mode at
the brightness allignment mode in setting.

1 Open the brightness allignment mode in setting
2 Enter sleep mode

Make sure that the keyboard light is turned off.

Change-Id:Ib0436954164bcd9f6cb8d4453304e645e6ff70f0*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 493a348..b3da14b 100644

//Synthetic comment -- @@ -1567,6 +1567,10 @@
EventLog.writeEvent(EventLogTags.POWER_SCREEN_STATE, 0, reason, mTotalTouchDownTime, mTouchCycles);
mLastTouchDown = 0;
int err = setScreenStateLocked(false);
        // Turn off the keyboard light aswell
        if (reason == WindowManagerPolicy.OFF_BECAUSE_OF_TIMEOUT) {
            setLightBrightness(LightsService.LIGHT_ID_KEYBOARD, LightsService.BRIGHTNESS_MODE_USER);
        }
if (err == 0) {
mScreenOffReason = reason;
sendNotificationLocked(false, reason);







