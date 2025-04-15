/*settings: remove GsmDataConnection in favor of MMDataConnection

Change-Id:I540b7d98cea5623b49ad2910c363ae14c864ce14*/
//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index f0fcdd7..8856523 100644

//Synthetic comment -- @@ -54,12 +54,13 @@
import android.widget.TextView;
import android.widget.EditText;

import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneStateIntentReceiver;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.gsm.GsmDataConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
//Synthetic comment -- @@ -769,17 +770,17 @@
.append(" and elapsed ")
.append(DateUtils.formatElapsedTime(timeElapsed));

                if (dc instanceof GsmDataConnection) {
                    GsmDataConnection pdp = (GsmDataConnection)dc;
sb.append("\n    to ")
                      .append(pdp.getApn().toString());
}
sb.append("\ninterface: ")
                  .append(phone.getInterfaceName(phone.getActiveApnTypes()[0]))
.append("\naddress: ")
.append(phone.getIpAddress(phone.getActiveApnTypes()[0]))
.append("\ngateway: ")
                  .append(phone.getGateway(phone.getActiveApnTypes()[0]));
String[] dns = phone.getDnsServers(phone.getActiveApnTypes()[0]);
if (dns != null) {
sb.append("\ndns: ").append(dns[0]).append(", ").append(dns[1]);
//Synthetic comment -- @@ -790,10 +791,10 @@
.append("\n    fail because ")
.append(dc.getLastFailCause().toString());
} else {
                if (dc instanceof GsmDataConnection) {
                    GsmDataConnection pdp = (GsmDataConnection)dc;
sb.append("    is connecting to ")
                      .append(pdp.getApn().toString());
} else {
sb.append("    is connecting");
}







