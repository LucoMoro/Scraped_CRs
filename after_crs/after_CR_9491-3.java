/*Fix system service crash when booting while on battery power*/




//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 4188005..e9f9fe2 100644

//Synthetic comment -- @@ -241,10 +241,14 @@
// since the standard intent will not wake any applications and some
// applications may want to have smart behavior based on this.
if (mPlugType != 0 && mLastPlugType == 0) {
                Intent intent = new Intent(Intent.ACTION_POWER_CONNECTED);
                intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
                mContext.sendBroadcast(intent);
}
else if (mPlugType == 0 && mLastPlugType != 0) {
                Intent intent = new Intent(Intent.ACTION_POWER_DISCONNECTED);
                intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT);
                mContext.sendBroadcast(intent);
}

mLastBatteryStatus = mBatteryStatus;







