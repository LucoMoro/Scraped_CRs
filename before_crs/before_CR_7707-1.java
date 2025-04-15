/*Optional provisioning

This introduces an ro.requires_provisioning property, which defaults to false,
such that by default the platform doesn't require any setup application to
set a run-time "provisioned" bit and allows all behaviors right away (home key,
lock screen, etc...).*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SimFullReceiver.java b/src/com/android/mms/transaction/SimFullReceiver.java
//Synthetic comment -- index c6a5b28..21640a9 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Telephony;

//Synthetic comment -- @@ -36,9 +37,10 @@

@Override
public void onReceive(Context context, Intent intent) {
        if (Settings.Secure.getInt(context.getContentResolver(),
            Settings.Secure.DEVICE_PROVISIONED, 0) == 1 &&
            Telephony.Sms.Intents.SIM_FULL_ACTION.equals(intent.getAction())) {

NotificationManager nm = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);







