/*handle negative temperature

When the battery temperature drops to below zero, BatteryLevelInit()
fails to show negative temperature. Because the type is unsigned
and the size of bit field is 10 bits.
So to handle negative temperature, change the type of battery temperature
from "char" to "short". And extend the size of temperature bit field
from 10 to 11 bits, which first bit is used for the sign bit.

Before:
31             24                    14                 0
+---------------+---------------------+-----------------+
| Battery Level | Battery temperature | Battery Voltage |
+---------------+---------------------+-----------------+

After:
31           25                      14                 0
+-------------+-----------------------+-----------------+
|Battery Level|  Battery temperature  | Battery Voltage |
+-------------+-----------------------+-----------------+

Bits 31..25: battery level percentage (7 bits unsigned)
Bits 24..14: battery temperature (11 bits signed)
    First bit is used for the sign and others for the temperature
Bits 13..0: battery voltage in 0.001 volt units (14 bits unsigned)

Becuase of changing the format, let the BatteryStatsImpl.VERSION field
increment.

Bug: 8009514
Change-Id:Iaa12f4d3f14e6cf4d73bc1a23d81c60f9677a499*/
//Synthetic comment -- diff --git a/core/java/android/os/BatteryStats.java b/core/java/android/os/BatteryStats.java
//Synthetic comment -- index 54f2fe3..6c2958b 100644

//Synthetic comment -- @@ -430,7 +430,7 @@
public byte batteryHealth;
public byte batteryPlugType;

        public char batteryTemperature;
public char batteryVoltage;

// Constants from SCREEN_BRIGHTNESS_*
//Synthetic comment -- @@ -507,7 +507,7 @@
batteryHealth = (byte)((bat>>20)&0xf);
batteryPlugType = (byte)((bat>>24)&0xf);
bat = src.readInt();
            batteryTemperature = (char)(bat&0xffff);
batteryVoltage = (char)((bat>>16)&0xffff);
states = src.readInt();
}
//Synthetic comment -- @@ -576,7 +576,7 @@
if (DEBUG) Slog.i(TAG, "WRITE DELTA: batteryToken=0x"
+ Integer.toHexString(batteryLevelInt)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + (int)batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}
if (stateIntChanged) {
//Synthetic comment -- @@ -591,8 +591,8 @@
}

private int buildBatteryLevelInt() {
            return ((((int)batteryLevel)<<24)&0xff000000)
                    | ((((int)batteryTemperature)<<14)&0x00ffc000)
| (((int)batteryVoltage)&0x00003fff);
}

//Synthetic comment -- @@ -628,13 +628,15 @@

if ((firstToken&DELTA_BATTERY_LEVEL_FLAG) != 0) {
int batteryLevelInt = src.readInt();
                batteryLevel = (byte)((batteryLevelInt>>24)&0xff);
                batteryTemperature = (char)((batteryLevelInt>>14)&0x3ff);
batteryVoltage = (char)(batteryLevelInt&0x3fff);
if (DEBUG) Slog.i(TAG, "READ DELTA: batteryToken=0x"
+ Integer.toHexString(batteryLevelInt)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + (int)batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}









//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 94e7a06..2c09922 100644

//Synthetic comment -- @@ -87,7 +87,7 @@
private static final int MAGIC = 0xBA757475; // 'BATSTATS'

// Current on-disk Parcel version
    private static final int VERSION = 62 + (USE_OLD_HISTORY ? 1000 : 0);

// Maximum number of items we will record in the history.
private static final int MAX_HISTORY_ITEMS = 2000;
//Synthetic comment -- @@ -4424,7 +4424,7 @@
mHistoryCur.batteryStatus = (byte)status;
mHistoryCur.batteryHealth = (byte)health;
mHistoryCur.batteryPlugType = (byte)plugType;
                mHistoryCur.batteryTemperature = (char)temp;
mHistoryCur.batteryVoltage = (char)volt;
setOnBatteryLocked(onBattery, oldStatus, level);
} else {
//Synthetic comment -- @@ -4447,7 +4447,7 @@
}
if (temp >= (mHistoryCur.batteryTemperature+10)
|| temp <= (mHistoryCur.batteryTemperature-10)) {
                    mHistoryCur.batteryTemperature = (char)temp;
changed = true;
}
if (volt > (mHistoryCur.batteryVoltage+20)







