/*Buttons do not go off while modifying brightness in settings.

When user changes backlight brighness in settings,
PowerManagerService will modify the brightness of display, keyboard
and buttons. That is, they will be enabled. However, the state in
PowerManagerService will not get updated causing the keyboard and
button not to be turned off after delay.

Change-Id:Ib0436954164bcd9f6cb8d4453304e645e6ff70f0*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 5848cc9..0d67e23 100644

//Synthetic comment -- @@ -2940,7 +2940,10 @@
brightness = Math.max(brightness, mScreenBrightnessDim);
mLcdLight.setBrightness(brightness);
mKeyboardLight.setBrightness(mKeyboardVisible ? brightness : 0);
            mUserState |= (mKeyboardVisible ? KEYBOARD_BRIGHT_BIT : 0);
mButtonLight.setBrightness(brightness);
            mUserState |= BUTTON_BRIGHT_BIT;
            forceUserActivityLocked();
long identity = Binder.clearCallingIdentity();
try {
mBatteryStats.noteScreenBrightness(brightness);







