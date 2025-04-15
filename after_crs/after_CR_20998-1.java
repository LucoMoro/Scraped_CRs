/*RadioInfo crashes if pdp.getApn returns null.

The automated test cases found a problem where the pdp.getApn()
returns null and as a consequence crashes the RadioInfo.

This activity is not acessible from a user menu in settings,
you have to start it with
adb shell am start -a android.intent.action.MAIN
 -n com.android.settings/.RadioInfo

Change-Id:I3e2c0805a1d911824954c86f682f612ea5e379c0*/




//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index f0fcdd7..04aa31c 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneStateIntentReceiver;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.gsm.ApnSetting;
import com.android.internal.telephony.gsm.GsmDataConnection;

import org.apache.http.HttpResponse;
//Synthetic comment -- @@ -790,12 +791,13 @@
.append("\n    fail because ")
.append(dc.getLastFailCause().toString());
} else {
                sb.append("    is connecting");
if (dc instanceof GsmDataConnection) {
GsmDataConnection pdp = (GsmDataConnection)dc;
                    ApnSetting apn = pdp.getApn();
                    if (apn != null) {
                        sb.append(" to ").append(apn.toString());
                    }
}
}
sb.append("\n===================");







