/*Optional provisioning

This introduces an ro.requires_provisioning property, which defaults to false,
such that by default the platform doesn't require any setup application to
set a run-time "provisioned" bit and allows all behaviors right away (home key,
lock screen, etc...).*/




//Synthetic comment -- diff --git a/mid/com/android/internal/policy/impl/MidWindowManager.java b/mid/com/android/internal/policy/impl/MidWindowManager.java
//Synthetic comment -- index 3cf36a5..8330346 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import static android.provider.Settings.System.END_BUTTON_BEHAVIOR;

//Synthetic comment -- @@ -193,6 +194,9 @@
}

private boolean isDeviceProvisioned() {
        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
            return true;
        }
return Settings.System.getInt(
mContext.getContentResolver(), Settings.System.DEVICE_PROVISIONED, 0) != 0;
}








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index 4671957..a9f9356 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Telephony;
import static android.provider.Telephony.Intents.EXTRA_PLMN;
//Synthetic comment -- @@ -160,8 +161,12 @@
}
};

        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
            mDeviceProvisioned = true;
        } else {
            mDeviceProvisioned = Settings.Secure.getInt(
                    mContext.getContentResolver(), Settings.Secure.DEVICE_PROVISIONED, 0) != 0;
        }

// Since device can't be un-provisioned, we only need to register a content observer
// to update mDeviceProvisioned when we are...








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 603b221..2dcf81d 100644

//Synthetic comment -- @@ -350,6 +350,9 @@
}

boolean isDeviceProvisioned() {
        if (!SystemProperties.getBoolean("ro.requires_provisioning", false)) {
            return true;
        }
return Settings.Secure.getInt(
mContext.getContentResolver(), Settings.Secure.DEVICE_PROVISIONED, 0) != 0;
}







