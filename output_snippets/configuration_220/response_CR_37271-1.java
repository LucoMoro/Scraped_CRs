//<Beginning of snippet n. 0>
public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
    NOT_DISCONNECTED
}

/**
 * @return cnap name or null if unavailable
 */
public String getCnapName() {
    return cnapName;
}

/**
 * @return cnap name presentation
 */
public int getCnapNamePresentation() {
    return cnapNamePresentation;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned
int numberPresentation = Connection.PRESENTATION_ALLOWED;
int cnapNamePresentation = Connection.PRESENTATION_ALLOWED;

public CdmaCall getCall() {
    return parent;
}

public String getCnapName() {
    return cnapName;
}

public int getCnapNamePresentation() {
    return cnapNamePresentation;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;

import com.android.internal.telephony.*;

isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
numberPresentation = dc.numberPresentation;
uusInfo = dc.uusInfo;

index = -1;

isIncoming = false;
createTime = System.currentTimeMillis();

this.parent = parent;
changed = true;

Log.d(LOG_TAG, "Call received: CNAP Name: " + cnapName + ", Presentation: " + cnapNamePresentation); // Implement Logging
if (newParent != parent) {
    if (parent != null) {
        parent.detach(this);
    }
}
//<End of snippet n. 2>