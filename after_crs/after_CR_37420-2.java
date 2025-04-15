/*Fix dumpPowerState method

dumpPowerState method shows the current power manger service state but,
BUTTON_BRIGHT_BIT is omitted.

Add a routine of checking BUTTON_BRIGHT_BIT
Signed-off-by: jaiyoung.park <jaiyoung.park@lge.com>

Change-Id:I00484fb384963bafdc58ce89b3251a1f5585d992*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 2a0d2a0..5848cc9 100644

//Synthetic comment -- @@ -1103,6 +1103,8 @@
? "SCREEN_BRIGHT_BIT " : "")
+ (((state & SCREEN_ON_BIT) != 0)
? "SCREEN_ON_BIT " : "")
                + (((state & BUTTON_BRIGHT_BIT) != 0)
                        ? "BUTTON_BRIGHT_BIT " : "")
+ (((state & BATTERY_LOW_BIT) != 0)
? "BATTERY_LOW_BIT " : "");
}







