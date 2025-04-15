/*Shutdown when capacity is 0% and no charging or when battery is dead

Android framework does not shutdown when battery capacity is 0% and a
charger is attached (USB or AC). This handling is incomplete since a
charger might very well be attached but charging has stopped because
USB suspended or the charging algorithm has stopped because of
battery safety handling. Also shutdown when battery is reported 'dead'.
This might happen although charging is present.

Change-Id:If328260ebf4d38f912e4d2fad204431cbb19c993*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 5cf61bd..6ac35fd 100644

//Synthetic comment -- @@ -136,7 +136,13 @@

final boolean isPowered() {
// assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
        return (mAcOnline || mUsbOnline || mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
}

final boolean isPowered(int plugTypeSet) {
//Synthetic comment -- @@ -148,11 +154,13 @@
if (plugTypeSet == 0) {
return false;
}
int plugTypeBit = 0;
        if (mAcOnline) {
plugTypeBit |= BatteryManager.BATTERY_PLUGGED_AC;
}
        if (mUsbOnline) {
plugTypeBit |= BatteryManager.BATTERY_PLUGGED_USB;
}
return (plugTypeSet & plugTypeBit) != 0;
//Synthetic comment -- @@ -183,7 +191,11 @@
private final void shutdownIfNoPower() {
// shut down gracefully if our battery is critically low and we are not powered.
// wait until the system has booted before attempting to display the shutdown dialog.
        if (mBatteryLevel == 0 && !isPowered() && ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);







