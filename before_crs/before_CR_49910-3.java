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
//Synthetic comment -- @@ -520,7 +520,7 @@
// Part of initial delta int holding the command code.
static final int DELTA_CMD_MASK = 0x3;
static final int DELTA_CMD_SHIFT = 18;
        // Flag in delta int: a new battery level int follows.
static final int DELTA_BATTERY_LEVEL_FLAG = 1<<20;
// Flag in delta int: a new full state and battery status int follows.
static final int DELTA_STATE_FLAG = 1<<21;
//Synthetic comment -- @@ -534,7 +534,7 @@
}

final long deltaTime = time - last.time;
            final int lastBatteryLevelInt = last.buildBatteryLevelInt();
final int lastStateInt = last.buildStateInt();

int deltaTimeToken;
//Synthetic comment -- @@ -548,9 +548,9 @@
int firstToken = deltaTimeToken
| (cmd<<DELTA_CMD_SHIFT)
| (states&DELTA_STATE_MASK);
            final int batteryLevelInt = buildBatteryLevelInt();
            final boolean batteryLevelIntChanged = batteryLevelInt != lastBatteryLevelInt;
            if (batteryLevelIntChanged) {
firstToken |= DELTA_BATTERY_LEVEL_FLAG;
}
final int stateInt = buildStateInt();
//Synthetic comment -- @@ -571,12 +571,12 @@
dest.writeLong(deltaTime);
}
}
            if (batteryLevelIntChanged) {
                dest.writeInt(batteryLevelInt);
if (DEBUG) Slog.i(TAG, "WRITE DELTA: batteryToken=0x"
                        + Integer.toHexString(batteryLevelInt)
+ " batteryLevel=" + batteryLevel
                        + " batteryTemp=" + (int)batteryTemperature
+ " batteryVolt=" + (int)batteryVoltage);
}
if (stateIntChanged) {
//Synthetic comment -- @@ -590,10 +590,10 @@
}
}

        private int buildBatteryLevelInt() {
            return ((((int)batteryLevel)<<24)&0xff000000)
                    | ((((int)batteryTemperature)<<14)&0x00ffc000)
                    | (((int)batteryVoltage)&0x00003fff);
}

private int buildStateInt() {
//Synthetic comment -- @@ -627,14 +627,14 @@
}

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
//Synthetic comment -- index 94e7a06..7c2b6fb 100644

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







