/*Revert "Shutdown when capacity is 0% and no charging or when battery is dead"

This reverts commit fd04143a47770256dabcfa4d8447127b3ec8b2bf.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 6ac35fd..5cf61bd 100644

//Synthetic comment -- @@ -136,13 +136,7 @@

final boolean isPowered() {
// assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
        // Do not look at the plug status to check if we are powered.
        // Charger can be plugged but not charging because of i.e. USB suspend,
        // battery temperature reasons etc. We are powered only if battery is
        // being charged. This function fill return false if charger is
        // connected and battery is reported full.
        return (mBatteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
}

final boolean isPowered(int plugTypeSet) {
//Synthetic comment -- @@ -154,13 +148,11 @@
if (plugTypeSet == 0) {
return false;
}

        // we are not powered when plug is connected and not charging
int plugTypeBit = 0;
        if (mAcOnline && mBatteryStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
plugTypeBit |= BatteryManager.BATTERY_PLUGGED_AC;
}
        if (mUsbOnline && mBatteryStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
plugTypeBit |= BatteryManager.BATTERY_PLUGGED_USB;
}
return (plugTypeSet & plugTypeBit) != 0;
//Synthetic comment -- @@ -191,11 +183,7 @@
private final void shutdownIfNoPower() {
// shut down gracefully if our battery is critically low and we are not powered.
// wait until the system has booted before attempting to display the shutdown dialog.
        // Also shutdown if battery is reported to be 'dead' independent of
        // battery level and power supply.
        if (((mBatteryLevel == 0 && !isPowered()) ||
             mBatteryHealth == BatteryManager.BATTERY_HEALTH_DEAD) &&
            ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);







