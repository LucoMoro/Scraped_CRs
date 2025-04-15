/*Revert "Shutdown when capacity is 0% and no charging or when battery is dead"

This reverts commit fd04143a47770256dabcfa4d8447127b3ec8b2bf.

This break most automated tests because with this change, if the
device is fully charged, it will simply turn off the screen, regardless
if USB is connected to hsot PC or not; so for our tests, most of them
are always connected to host PC, and fully charged 99% of the time,
as soon as the screen turns off, the front-end test Activity will be
suspended.

Change-Id:I1e6590611af43812f1bac223dd31570d1d90cfc5*/




//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 6ac35fd..5cf61bd 100644

//Synthetic comment -- @@ -136,13 +136,7 @@

final boolean isPowered() {
// assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
        return (mAcOnline || mUsbOnline || mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
}

final boolean isPowered(int plugTypeSet) {
//Synthetic comment -- @@ -154,13 +148,11 @@
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
//Synthetic comment -- @@ -191,11 +183,7 @@
private final void shutdownIfNoPower() {
// shut down gracefully if our battery is critically low and we are not powered.
// wait until the system has booted before attempting to display the shutdown dialog.
        if (mBatteryLevel == 0 && !isPowered() && ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);







