//<Beginning of snippet n. 0>
public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
    NOT_DISCONNECTED
}

private static String cnapName; // Persisted across calls
private static int cnapNamePresentation; // Persisted across calls

public synchronized static String getCnapName() {
    return cnapName;
}

public synchronized static int getCnapNamePresentation() {
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

Handler h;

public synchronized void onIncomingCall(CallDetails dc) {
    isIncoming = dc.isMT;
    createTime = System.currentTimeMillis();
    
    if(dc.numberPresentation == -1 || dc.cnapName == null) {
        Log.e(LOG_TAG, "Invalid CNAP values received"); 
        return; // Early return on invalid data
    }
    
    numberPresentation = dc.numberPresentation;
    cnapName = dc.cnapName; // Store CNAP name when call is received
    cnapNamePresentation = dc.cnapNamePresentation;

    Log.i(LOG_TAG, "Received call from CNAP: " + cnapName); // Log CNAP name
    Log.i(LOG_TAG, "CNAP presentation: " + cnapNamePresentation); // Log CNAP presentation
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
uusInfo = dc.uusInfo;

index = -1;

isIncoming = false;
createTime = System.currentTimeMillis();

this.parent = parent;
changed = true;
}

// Added synchronization to ensure thread safety
public synchronized void updateCnapDetails(CallDetails dc) {
    if(dc.cnapName != null && dc.cnapNamePresentation != -1) {
        cnapName = dc.cnapName;
        cnapNamePresentation = dc.cnapNamePresentation;
        
        Log.i(LOG_TAG, "Updated CNAP to: " + cnapName);
    } else {
        Log.e(LOG_TAG, "Failed to update CNAP: Invalid CNAP details"); // Enhanced error logging
    }
}
//<End of snippet n. 2>