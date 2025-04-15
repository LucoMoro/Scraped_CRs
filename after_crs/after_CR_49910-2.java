/*handle negative temperature

When the battery temperature drops to below zero, BatteryStat fails to
show negative temperature.
So to handle negative temperature, change the type of battery temperature
from "char" to "short".

Bug: 8009514
Change-Id:Iaa12f4d3f14e6cf4d73bc1a23d81c60f9677a499*/




//Synthetic comment -- diff --git a/core/java/android/os/BatteryStats.java b/core/java/android/os/BatteryStats.java
//Synthetic comment -- index 54f2fe3..e75db74 100644

//Synthetic comment -- @@ -430,7 +430,7 @@
public byte batteryHealth;
public byte batteryPlugType;

        public short batteryTemperature;
public char batteryVoltage;

// Constants from SCREEN_BRIGHTNESS_*
//Synthetic comment -- @@ -507,7 +507,7 @@
batteryHealth = (byte)((bat>>20)&0xf);
batteryPlugType = (byte)((bat>>24)&0xf);
bat = src.readInt();
            batteryTemperature = (short)(bat&0xffff);
batteryVoltage = (char)((bat>>16)&0xffff);
states = src.readInt();
}
//Synthetic comment -- @@ -576,7 +576,7 @@
if (DEBUG) Slog.i(TAG, "WRITE DELTA: batteryToken=0x"
+ Integer.toHexString(batteryLevelInt)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}
if (stateIntChanged) {
//Synthetic comment -- @@ -629,12 +629,12 @@
if ((firstToken&DELTA_BATTERY_LEVEL_FLAG) != 0) {
int batteryLevelInt = src.readInt();
batteryLevel = (byte)((batteryLevelInt>>24)&0xff);
                batteryTemperature = (short)((batteryLevelInt>>14)&0x3ff);
batteryVoltage = (char)(batteryLevelInt&0x3fff);
if (DEBUG) Slog.i(TAG, "READ DELTA: batteryToken=0x"
+ Integer.toHexString(batteryLevelInt)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}









//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 94e7a06..7c2b6fb 100644

//Synthetic comment -- @@ -4424,7 +4424,7 @@
mHistoryCur.batteryStatus = (byte)status;
mHistoryCur.batteryHealth = (byte)health;
mHistoryCur.batteryPlugType = (byte)plugType;
                mHistoryCur.batteryTemperature = (short)temp;
mHistoryCur.batteryVoltage = (char)volt;
setOnBatteryLocked(onBattery, oldStatus, level);
} else {
//Synthetic comment -- @@ -4447,7 +4447,7 @@
}
if (temp >= (mHistoryCur.batteryTemperature+10)
|| temp <= (mHistoryCur.batteryTemperature-10)) {
                    mHistoryCur.batteryTemperature = (short)temp;
changed = true;
}
if (volt > (mHistoryCur.batteryVoltage+20)







