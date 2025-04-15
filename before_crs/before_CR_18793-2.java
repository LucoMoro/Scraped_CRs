/*Buttons do not go off while modifying brightness in settings.

When user changes backlight brighness in settings,
PowerManagerService will modify the brightness of display, keyboard
and buttons. That is, they will be enabled. However, the state in
PowerManagerService will not get updated causing the keyboard and
button not to be turned off after delay.

Also, add BUTTON_BRIGHT_BIT to dump().

Change-Id:Ib0436954164bcd9f6cb8d4453304e645e6ff70f0*/
//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index a6daaef..5f204fc 100644

//Synthetic comment -- @@ -1080,6 +1080,8 @@
? "SCREEN_BRIGHT_BIT " : "")
+ (((state & SCREEN_ON_BIT) != 0)
? "SCREEN_ON_BIT " : "")
+ (((state & BATTERY_LOW_BIT) != 0)
? "BATTERY_LOW_BIT " : "");
}
//Synthetic comment -- @@ -2793,7 +2795,10 @@
brightness = Math.max(brightness, Power.BRIGHTNESS_DIM);
mLcdLight.setBrightness(brightness);
mKeyboardLight.setBrightness(mKeyboardVisible ? brightness : 0);
mButtonLight.setBrightness(brightness);
long identity = Binder.clearCallingIdentity();
try {
mBatteryStats.noteScreenBrightness(brightness);







