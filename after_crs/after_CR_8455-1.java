/*Better way to find whether the device requires provisioning.

This has the advantage of not relying on non-public APIs.*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 33a3ece..318b6f3 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Config;
import android.util.Log;


//Synthetic comment -- @@ -185,7 +186,7 @@

// Incoming calls are totally ignored if the device isn't provisioned yet
boolean provisioned;
        if (!Config.getBoolean(Config.REQUIRES_PROVISIONING, false)) {
provisioned = true;
} else {
provisioned = Settings.Secure.getInt(mPhone.getContext().getContentResolver(),







