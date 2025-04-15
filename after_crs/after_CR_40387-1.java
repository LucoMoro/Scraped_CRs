/*LightsService: Fix to avoid pulse request on a light when its flashing

Change-Id:I69ec6b998d56f20b5b2bae00fe27969a8f5132c8*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LightsService.java b/services/java/com/android/server/LightsService.java
//Synthetic comment -- index 1e95f3e..b57e18b 100644

//Synthetic comment -- @@ -77,7 +77,7 @@

public void setColor(int color) {
synchronized (this) {
                setLightLocked(color, LIGHT_FLASH_NONE, 0, 0, BRIGHTNESS_MODE_USER);
}
}

//Synthetic comment -- @@ -119,6 +119,18 @@
+ Integer.toHexString(color));
mColor = color;
mMode = mode;

                switch (mode) {
                   case LIGHT_FLASH_TIMED:
                   case LIGHT_FLASH_HARDWARE:
                         mFlashing = true;
                         break;
                   case LIGHT_FLASH_NONE:
                   default:
                         mFlashing = false;
                         break;
                }

mOnMS = onMS;
mOffMS = offMS;
setLight_native(mNativePointer, mId, color, mode, onMS, offMS, brightnessMode);







