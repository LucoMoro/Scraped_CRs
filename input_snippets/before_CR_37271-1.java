
//<Beginning of snippet n. 0>


public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
* @return cnap name or null if unavailable
*/
public String getCnapName() {
        return null;
}

/**
*/

public int getCnapNamePresentation() {
       return 0;
};

/**

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
    String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

/*
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
    int cnapNamePresentation  = Connection.PRESENTATION_ALLOWED;


Handler h;
return address;
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
}

if (newParent != parent) {
if (parent != null) {
parent.detach(this);

//<End of snippet n. 2>








