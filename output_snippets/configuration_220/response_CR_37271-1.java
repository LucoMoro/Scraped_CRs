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
 * Returns the presentation status of the CNAP name.
 */
public int getCnapNamePresentation() {
    return (cnapName == null) ? PRESENTATION_UNKNOWN : PRESENTATION_ALLOWED;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned
int cnapNamePresentation = Connection.PRESENTATION_ALLOWED; // Set default presentation

// Assuming dc is an instance of some data class representing call data
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;

// Logging the CNAP name immediately when the call is received
if (isIncoming) {
    cnapName = dc.cnapName; // Assume dc gets CNAP name from network data
    Log.d(LOG_TAG, "Received incoming call from CNAP: " + cnapName);
}

public String getCnapName() {
    return cnapName;
}

public int getCnapNamePresentation() {
    return cnapNamePresentation;
}

public CdmaCall getCall() {
    return parent;
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

if (newParent != parent) {
    if (parent != null) {
        parent.detach(this);
    }
}

//<End of snippet n. 2>