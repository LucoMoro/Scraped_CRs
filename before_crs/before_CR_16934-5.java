/*Shutdown when capacity is 0% and non charging or when battery is dead

The Android framework does not shutdown when battery capacity is 0% and
a (non-charging) charger is attached (USB or AC). This handling is wrong
since a charger might very well be attached but charging has stopped
because USB suspended or the charging algorithm has stopped because of
battery safety handling. Also shutdown when battery is reported 'dead'.
This might happen although charging is present.

Change-Id:I6e685d0ae3c838c2ae66bb97c49b67ae4003b17e*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index ab9ae69..44f7e72 100644

//Synthetic comment -- @@ -149,11 +149,24 @@
}

final boolean isPowered() {
        // assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
return (mAcOnline || mUsbOnline || mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
}

    final boolean isPowered(int plugTypeSet) {
// assume we are powered if battery state is unknown so
// the "stay on while plugged in" option will work.
if (mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
//Synthetic comment -- @@ -208,7 +221,11 @@
private final void shutdownIfNoPower() {
// shut down gracefully if our battery is critically low and we are not powered.
// wait until the system has booted before attempting to display the shutdown dialog.
        if (mBatteryLevel == 0 && !isPowered() && ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);








//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 888ec69..10d530b 100644

//Synthetic comment -- @@ -408,7 +408,7 @@
public void onReceive(Context context, Intent intent) {
synchronized (mLocks) {
boolean wasPowered = mIsPowered;
                mIsPowered = mBatteryService.isPowered();

if (mIsPowered != wasPowered) {
// update mStayOnWhilePluggedIn wake lock
//Synthetic comment -- @@ -766,7 +766,7 @@

private void updateWakeLockLocked() {
final int stayOnConditions = getStayOnConditionsLocked();
        if (stayOnConditions != 0 && mBatteryService.isPowered(stayOnConditions)) {
// keep the device on if we're plugged in and mStayOnWhilePluggedIn is set.
mStayOnWhilePluggedInScreenDimLock.acquire();
mStayOnWhilePluggedInPartialLock.acquire();
//Synthetic comment -- @@ -2108,7 +2108,8 @@
steps = (int)(ANIM_STEPS*ratio);
}
final int stayOnConditions = getStayOnConditionsLocked();
                    if (stayOnConditions != 0 && mBatteryService.isPowered(stayOnConditions)) {
// If the "stay on while plugged in" option is
// turned on, then the screen will often not
// automatically turn off while plugged in.  To








//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index c14e2f6..616e91b 100644

//Synthetic comment -- @@ -346,7 +346,7 @@
* text of why it is not a good time.
*/
String shouldWeBeBrutalLocked(long curTime) {
        if (mBattery == null || !mBattery.isPowered()) {
return "battery";
}








