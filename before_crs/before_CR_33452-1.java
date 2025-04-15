/*fix bug: light-sensor can't change lcd brightness

In auto-brightness mode, the value for getting the lcd brightness value is always mHighestLightSensorValue.

Change-Id:Idc3a395b6b0d9656afd13b0743a01bce6b848565Signed-off-by: Leox Yan <yanx730@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 2a0d2a0..fcb6652 100644

//Synthetic comment -- @@ -2519,7 +2519,7 @@
// we only do this if we are undocked, since lighting should be stable when
// stationary in a dock.
int lcdValue = getAutoBrightnessValue(
                        (mIsDocked ? value : mHighestLightSensorValue),
mLcdBacklightValues);
int buttonValue = getAutoBrightnessValue(value, mButtonBacklightValues);
int keyboardValue;







