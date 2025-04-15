/*Better way to find whether the device requires provisioning.

This has the advantage of not relying on non-public APIs.*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SimFullReceiver.java b/src/com/android/mms/transaction/SimFullReceiver.java
//Synthetic comment -- index 21640a9..6c9a173 100644

//Synthetic comment -- @@ -26,9 +26,9 @@
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Config;

/**
* Receive Intent.SIM_FULL_ACTION.  Handle notification that SIM is full.
//Synthetic comment -- @@ -39,7 +39,7 @@
public void onReceive(Context context, Intent intent) {
if ((Settings.Secure.getInt(context.getContentResolver(),
Settings.Secure.DEVICE_PROVISIONED, 0) == 1 ||
                !Config.getBoolean(Config.REQUIRES_PROVISIONING, false)) &&
Telephony.Sms.Intents.SIM_FULL_ACTION.equals(intent.getAction())) {

NotificationManager nm = (NotificationManager)







