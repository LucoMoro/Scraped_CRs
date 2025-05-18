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
public synchronized String getCnapName() {
    Log.d(LOG_TAG, "Getting CNAP Name: " + cnapName);
    return cnapName;
}

/**
 * @return CNAP name presentation based on network conditions
 */
public synchronized int getCnapNamePresentation() {
    Log.d(LOG_TAG, "Getting CNAP Name Presentation: " + cnapNamePresentation);
    return cnapNamePresentation;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned
int cnapNamePresentation = PRESENTATION_UNKNOWN;

/*
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
*/

Handler h;
return address;
}

public synchronized void updateCnapName(String newCnapName, int presentation) {
    if (newCnapName != null && !newCnapName.isEmpty()) {
        this.cnapName = newCnapName;
        this.cnapNamePresentation = presentation;
        Log.d(LOG_TAG, "CNAP Name updated to: " + cnapName);
    } else {
        Log.w(LOG_TAG, "Invalid CNAP Name provided, not updating.");
    }
}

public synchronized String getCnapName() {
    Log.d(LOG_TAG, "Getting CNAP Name: " + cnapName);
    return cnapName;
}

public synchronized int getCnapNamePresentation() {
    Log.d(LOG_TAG, "Getting CNAP Name Presentation: " + cnapNamePresentation);
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
}

if (newParent != parent) {
    if (parent != null) {
        parent.detach(this);
    }
//<End of snippet n. 2>