/*extend battery level field from int to long

Previously, BatteryLevelInt couldn't represent negative battery temperature.
Becuase the type is unsigned and the size of bit field is 10 bits.

To fix it, extend the bit fields from 10 bits to 16 bits.
Also use extend the battery level field from int to long.

Before:
31             24                    14                 0
+---------------+---------------------+-----------------+
| Battery Level | Battery temperature | Battery Voltage |
+---------------+---------------------+-----------------+

After:
63                             48                      32
+-------------------------------+-----------------------+
|                               | Battery temperature   |
+-------------------------------+-----------------------+
31             24                    14                 0
+---------------+---------------------+-----------------+
| Battery Level |                     | Battery Voltage |
+---------------+---------------------+-----------------+

Bug: 8009514
Change-Id:Iaa12f4d3f14e6cf4d73bc1a23d81c60f9677a499*/




//Synthetic comment -- diff --git a/core/java/android/os/BatteryStats.java b/core/java/android/os/BatteryStats.java
//Synthetic comment -- index 54f2fe3..e569c31 100644

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
//Synthetic comment -- @@ -520,7 +520,7 @@
// Part of initial delta int holding the command code.
static final int DELTA_CMD_MASK = 0x3;
static final int DELTA_CMD_SHIFT = 18;
        // Flag in delta long: a new battery level long follows.
static final int DELTA_BATTERY_LEVEL_FLAG = 1<<20;
// Flag in delta int: a new full state and battery status int follows.
static final int DELTA_STATE_FLAG = 1<<21;
//Synthetic comment -- @@ -534,7 +534,7 @@
}

final long deltaTime = time - last.time;
            final long lastBatteryLevelLong = last.buildBatteryLevelLong();
final int lastStateInt = last.buildStateInt();

int deltaTimeToken;
//Synthetic comment -- @@ -548,9 +548,9 @@
int firstToken = deltaTimeToken
| (cmd<<DELTA_CMD_SHIFT)
| (states&DELTA_STATE_MASK);
            final long batteryLevelLong = buildBatteryLevelLong();
            final boolean batteryLevelLongChanged = batteryLevelLong != lastBatteryLevelLong;
            if (batteryLevelLongChanged) {
firstToken |= DELTA_BATTERY_LEVEL_FLAG;
}
final int stateInt = buildStateInt();
//Synthetic comment -- @@ -571,12 +571,12 @@
dest.writeLong(deltaTime);
}
}
            if (batteryLevelLongChanged) {
                dest.writeLong(batteryLevelLong);
if (DEBUG) Slog.i(TAG, "WRITE DELTA: batteryToken=0x"
                        + Long.toHexString(batteryLevelLong)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}
if (stateIntChanged) {
//Synthetic comment -- @@ -590,10 +590,10 @@
}
}

        private long buildBatteryLevelLong() {
            return (((((long)batteryTemperature)<<32)&0x0000ffff00000000L)
                    | (((long)batteryLevel)<<24)&0x000000ff000000L)
                    | (((long)batteryVoltage)&0x0000000000003fffL);
}

private int buildStateInt() {
//Synthetic comment -- @@ -627,14 +627,14 @@
}

if ((firstToken&DELTA_BATTERY_LEVEL_FLAG) != 0) {
                long batteryLevelLong = src.readLong();
                batteryTemperature = (short)((batteryLevelLong>>32)&0xffff);
                batteryLevel = (byte)((batteryLevelLong>>24)&0xff);
                batteryVoltage = (char)(batteryLevelLong&0x3fff);
if (DEBUG) Slog.i(TAG, "READ DELTA: batteryToken=0x"
                        + Long.toHexString(batteryLevelLong)
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







