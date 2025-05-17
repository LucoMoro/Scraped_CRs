//<Beginning of snippet n. 0>

public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
    NOT_DISCONNECTED
}

/**
 * Store CNAP name for the connection
 */
private String cnapName = ""; // Initialize to empty string

/**
 * @return cnap name or empty string if unavailable
 */
public String getCnapName() {
    return cnapName != null ? cnapName : "";
}

/**
 * @return CNAP name presentation status
 */
public int getCnapNamePresentation() {
    return cnapNamePresentation;
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
int cnapNamePresentation = Connection.PRESENTATION_ALLOWED;

Handler h;

public void handleIncomingCall(CallData callData) {
    if (callData != null) {
        isIncoming = callData.isMT;
        createTime = System.currentTimeMillis();
        numberPresentation = callData.numberPresentation;
        cnapName = (callData.cnapName != null) ? callData.cnapName : ""; // Store CNAP name from call data
        cnapNamePresentation = callData.cnapNamePresentation; // Store CNAP presentation
        
        Log.d(LOG_TAG, "CNAP Name: " + (cnapName.isEmpty() ? "Unknown" : cnapName) + 
                       ", Presentation: " + cnapNamePresentation);
    }
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
}

if (newParent != parent) {
    if (parent != null) {
        parent.detach(this);
    }
}

//<End of snippet n. 2>