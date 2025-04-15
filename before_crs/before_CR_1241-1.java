/*Added broadcasts for external power events.*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index c76158c..598e0e8 100644

//Synthetic comment -- @@ -507,6 +507,8 @@
*     <li> {@link #ACTION_PACKAGE_REMOVED}
*     <li> {@link #ACTION_UID_REMOVED}
*     <li> {@link #ACTION_BATTERY_CHANGED}
* </ul>
*
* <h3>Standard Categories</h3>
//Synthetic comment -- @@ -1178,6 +1180,24 @@
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
/**
* Broadcast Action:  Indicates low memory condition on the device
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)








//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index 608299c..faa8208 100644

//Synthetic comment -- @@ -149,6 +149,16 @@
mBatteryLevel, mBatteryVoltage, mBatteryTemperature);
}

mLastBatteryStatus = mBatteryStatus;
mLastBatteryHealth = mBatteryHealth;
mLastBatteryPresent = mBatteryPresent;







