Added broadcasts for external power events.












diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
index c76158c..598e0e8 100644

@@ -507,6 +507,8 @@
*     <li> {@link #ACTION_PACKAGE_REMOVED}
*     <li> {@link #ACTION_UID_REMOVED}
*     <li> {@link #ACTION_BATTERY_CHANGED}
 *     <li> {@link #ACTION_POWER_CONNECTED}
 *     <li> {@link #ACTION_POWER_DISCONNECTED} 
* </ul>
*
* <h3>Standard Categories</h3>
@@ -1178,6 +1180,24 @@
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
/**
     * Broadcast Action:  External power has been connected to the device.
     * This is intended for applications that wish to register specifically to this notification.
     * Unlike ACTION_BATTERY_CHANGED, applications will be woken for this and so do not have to
     * stay active to receive this notification.  This action can be used to implement actions
     * that wait until power is available to trigger.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
    /**
     * Broadcast Action:  External power has been removed from the device.
     * This is intended for applications that wish to register specifically to this notification.
     * Unlike ACTION_BATTERY_CHANGED, applications will be woken for this and so do not have to
     * stay active to receive this notification.  This action can be used to implement actions
     * that wait until power is available to trigger. 
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";    
    /**
* Broadcast Action:  Indicates low memory condition on the device
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)








diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
index 608299c..faa8208 100644

@@ -149,6 +149,16 @@
mBatteryLevel, mBatteryVoltage, mBatteryTemperature);
}

            // Separate broadcast is sent for power connected / not connected
            // since the standard intent will not wake any applications and some
            // applications may want to have smart behavior based on this.
            if (mPlugType != 0 && mLastPlugType == 0) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_POWER_CONNECTED));
            }
            else if (mPlugType == 0 && mLastPlugType != 0) {
                mContext.sendBroadcast(new Intent(Intent.ACTION_POWER_DISCONNECTED));
            }
            
mLastBatteryStatus = mBatteryStatus;
mLastBatteryHealth = mBatteryHealth;
mLastBatteryPresent = mBatteryPresent;







