/*Fix system service crash when booting while on battery power*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 4188005..7c48449 100644

//Synthetic comment -- @@ -241,10 +241,14 @@
// since the standard intent will not wake any applications and some
// applications may want to have smart behavior based on this.
if (mPlugType != 0 && mLastPlugType == 0) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_POWER_CONNECTED));
}
else if (mPlugType == 0 && mLastPlugType != 0) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_POWER_DISCONNECTED));
}

mLastBatteryStatus = mBatteryStatus;
//Synthetic comment -- @@ -268,6 +272,7 @@
private final void sendIntent() {
//  Pack up the values and broadcast them to everyone
Intent intent = new Intent(Intent.ACTION_BATTERY_CHANGED);
intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
try {
mBatteryStats.setOnBattery(mPlugType == BATTERY_PLUGGED_NONE, mBatteryLevel);







