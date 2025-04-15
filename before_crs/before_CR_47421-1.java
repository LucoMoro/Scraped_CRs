/*Obfuscate send USSD log

Content of the messages sent over USSD no longer copied in the radio logcat. There are USSD services requiring highly sensitive information to be sent by the user, you really don't want this information to stay in the logcat.

Change-Id:Ieba37be0e346a6728dc7b139105a148577e0c0ff*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index faae72a..ec80786 100644

//Synthetic comment -- @@ -41,6 +41,8 @@
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.PowerManager.WakeLock;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
//Synthetic comment -- @@ -1712,8 +1714,13 @@
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                            + " " + ussdString);

rr.mp.writeString(ussdString);








