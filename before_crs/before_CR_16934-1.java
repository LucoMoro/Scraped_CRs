/*Shutdown when capacity is 0% and no charging or when battery is dead v2

Android framework does not shutdown when battery capacity is 0% and a
charger is attached (USB or AC). This handling is incomplete since a
charger might very well be attached but charging has stopped because
USB suspended or the charging algorithm has stopped because of
battery safety handling. Also shutdown when battery is reported 'dead'.
This might happen although charging is present.

Change-Id:I6e685d0ae3c838c2ae66bb97c49b67ae4003b17e*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 5cf61bd..99654744c 100644

//Synthetic comment -- @@ -135,11 +135,45 @@
}

final boolean isPowered() {
        // assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
        return (mAcOnline || mUsbOnline || mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
}

final boolean isPowered(int plugTypeSet) {
// assume we are powered if battery state is unknown so
// the "stay on while plugged in" option will work.
if (mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
//Synthetic comment -- @@ -148,6 +182,7 @@
if (plugTypeSet == 0) {
return false;
}
int plugTypeBit = 0;
if (mAcOnline) {
plugTypeBit |= BatteryManager.BATTERY_PLUGGED_AC;
//Synthetic comment -- @@ -183,7 +218,11 @@
private final void shutdownIfNoPower() {
// shut down gracefully if our battery is critically low and we are not powered.
// wait until the system has booted before attempting to display the shutdown dialog.
        if (mBatteryLevel == 0 && !isPowered() && ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);








//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 493a348..2436dd7 100644

//Synthetic comment -- @@ -340,7 +340,7 @@
public void onReceive(Context context, Intent intent) {
synchronized (mLocks) {
boolean wasPowered = mIsPowered;
                mIsPowered = mBatteryService.isPowered();

if (mIsPowered != wasPowered) {
// update mStayOnWhilePluggedIn wake lock
//Synthetic comment -- @@ -603,7 +603,7 @@
}

private void updateWakeLockLocked() {
        if (mStayOnConditions != 0 && mBatteryService.isPowered(mStayOnConditions)) {
// keep the device on if we're plugged in and mStayOnWhilePluggedIn is set.
mStayOnWhilePluggedInScreenDimLock.acquire();
mStayOnWhilePluggedInPartialLock.acquire();
//Synthetic comment -- @@ -1690,7 +1690,8 @@
// was dim
steps = (int)(ANIM_STEPS*ratio);
}
                        if (mStayOnConditions != 0 && mBatteryService.isPowered(mStayOnConditions)) {
// If the "stay on while plugged in" option is
// turned on, then the screen will often not
// automatically turn off while plugged in.  To








//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index d4133f3..0fb78f3 100644

//Synthetic comment -- @@ -713,7 +713,7 @@
* text of why it is not a good time.
*/
String shouldWeBeBrutalLocked(long curTime) {
        if (mBattery == null || !mBattery.isPowered()) {
return "battery";
}








