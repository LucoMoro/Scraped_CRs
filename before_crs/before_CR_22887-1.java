/*Modify compatibility check to include qHD (960x540) resolution

Change-Id:I1b2c5c2e4452116c54c1ba63a0fdca8d3e2cf1e8*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 26cf55f..48e5c31 100644

//Synthetic comment -- @@ -5035,10 +5035,10 @@
mScreenLayout = Configuration.SCREENLAYOUT_SIZE_NORMAL;
}

                // If this screen is wider than normal HVGA, or taller
                // than FWVGA, then for old apps we want to run in size
// compatibility mode.
                if (shortSize > 321 || longSize > 570) {
mScreenLayout |= Configuration.SCREENLAYOUT_COMPAT_NEEDED;
}








