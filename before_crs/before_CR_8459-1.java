/*Revert "Optional provisioning"

This reverts commit dab6c7163ec0daf37ac2cd3cb50642529ca574c1.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SimFullReceiver.java b/src/com/android/mms/transaction/SimFullReceiver.java
//Synthetic comment -- index 21640a9..c6a5b28 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Telephony;

//Synthetic comment -- @@ -37,10 +36,9 @@

@Override
public void onReceive(Context context, Intent intent) {
        if ((Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.DEVICE_PROVISIONED, 0) == 1 ||
                !SystemProperties.getBoolean("ro.requires_provisioning", false)) &&
                Telephony.Sms.Intents.SIM_FULL_ACTION.equals(intent.getAction())) {

NotificationManager nm = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);







