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
 *
 */
public int getCnapNamePresentation() {
    // This should reflect actual network capability
    return (/* check network capability */) ? PRESENTATION_ALLOWED : PRESENTATION_UNKNOWN;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
int cnapNamePresentation = Connection.PRESENTATION_ALLOWED;

Handler h;

public void onIncomingCall(PhoneNumberUtils dc) { 
    isIncoming = dc.isMT;
    createTime = System.currentTimeMillis();
    numberPresentation = dc.numberPresentation;
    cnapName = getCnapName(); // Set CNAP name on call reception
    Log.d(LOG_TAG, "Incoming call from: " + (cnapName != null ? cnapName : "Unknown Number"));
    index = -1;
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

public void handleNewCall(CdmaCallTracker dc) {
    isIncoming = dc.isMT;
    createTime = System.currentTimeMillis();
    numberPresentation = dc.numberPresentation;
    // Additional logging
    Log.d(LOG_TAG, "Call received. Number presentation: " + numberPresentation);
    
    // Assign the correct CNAP name based on the incoming call
    cnapName = getCnapName();
    cnapNamePresentation = getCnapNamePresentation();
    Log.d(LOG_TAG, "CNAP Name: " + (cnapName != null ? cnapName : "None"));

    index = -1;
    if (newParent != parent) {
        if (parent != null) {
            parent.detach(this);
        }
    }
}

//<End of snippet n. 2>