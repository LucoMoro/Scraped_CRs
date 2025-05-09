/*Change values of ACTION_POWER constants to better match established practice.

Trim code to 100 lines to match style guide.*/
//Synthetic comment -- diff --git a/core/java/android/content/Intent.java b/core/java/android/content/Intent.java
//Synthetic comment -- index 598e0e8..aa4622b 100644

//Synthetic comment -- @@ -1187,7 +1187,7 @@
* that wait until power is available to trigger.
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
/**
* Broadcast Action:  External power has been removed from the device.
* This is intended for applications that wish to register specifically to this notification.
//Synthetic comment -- @@ -1196,7 +1196,8 @@
* that wait until power is available to trigger. 
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";    
/**
* Broadcast Action:  Indicates low memory condition on the device
*/







