
//<Beginning of snippet n. 0>


public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

    //Caller Name Display
    String cnapName;
    int cnapNamePresentation  = Connection.PRESENTATION_ALLOWED;

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
* @return cnap name or null if unavailable
*/
public String getCnapName() {
        return cnapName;
}

/**
*/

public int getCnapNamePresentation() {
       return cnapNamePresentation;
};

/**

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

/*
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;


Handler h;
return address;
}

public CdmaCall getCall() {
return parent;
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.text.TextUtils;

import com.android.internal.telephony.*;


isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
        cnapName = dc.name;
        cnapNamePresentation = dc.namePresentation;
numberPresentation = dc.numberPresentation;
uusInfo = dc.uusInfo;

index = -1;

isIncoming = false;
        cnapName = null;
        cnapNamePresentation = Connection.PRESENTATION_ALLOWED;
        numberPresentation = Connection.PRESENTATION_ALLOWED;
createTime = System.currentTimeMillis();

this.parent = parent;
changed = true;
}

        // A null cnapName should be the same as ""
        if (TextUtils.isEmpty(dc.name)) {
            if (!TextUtils.isEmpty(cnapName)) {
                changed = true;
                cnapName = "";
            }
        } else if (!dc.name.equals(cnapName)) {
            changed = true;
            cnapName = dc.name;
        }

        if (Phone.DEBUG_PHONE) log("--dssds----"+cnapName);
        cnapNamePresentation = dc.namePresentation;
        numberPresentation = dc.numberPresentation;

if (newParent != parent) {
if (parent != null) {
parent.detach(this);

//<End of snippet n. 2>








