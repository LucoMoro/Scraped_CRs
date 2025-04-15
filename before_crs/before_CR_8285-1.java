/*Better way to find whether the device requires provisioning.

This has the advantage of not relying on non-public APIs.*/
//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindowManager.java b/mid/com/android/internal/policy/impl/MidWindowManager.java
//Synthetic comment -- index 8330346..75450bc 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import static android.provider.Settings.System.END_BUTTON_BEHAVIOR;

//Synthetic comment -- @@ -194,7 +193,7 @@
}

private boolean isDeviceProvisioned() {
        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
return true;
}
return Settings.System.getInt(








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index a9f9356..1b47775 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Telephony;
import static android.provider.Telephony.Intents.EXTRA_PLMN;
//Synthetic comment -- @@ -37,6 +36,7 @@
import static android.provider.Telephony.Intents.SPN_STRINGS_UPDATED_ACTION;
import com.android.internal.telephony.SimCard;
import com.android.internal.telephony.TelephonyIntents;
import android.util.Log;
import com.android.internal.R;
import com.google.android.collect.Lists;
//Synthetic comment -- @@ -161,7 +161,7 @@
}
};

        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
mDeviceProvisioned = true;
} else {
mDeviceProvisioned = Settings.Secure.getInt(








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 2dcf81d..760dd64 100644

//Synthetic comment -- @@ -350,7 +350,7 @@
}

boolean isDeviceProvisioned() {
        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
return true;
}
return Settings.Secure.getInt(







