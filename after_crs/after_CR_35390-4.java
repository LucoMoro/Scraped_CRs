/*Add support for Caller Name Display

Copied relevant parts from CdmaConnection.java to get the information
as/if presented by network operator (Fido/Rogers in canada provide that
information, and it get displayed if there's no match in phonebook).
Although it works (the nale is displayed when the call is received),
the information is not stored (not kept in the log).

* Patch Set 2 : correct coding style
* Patch Set 3 : promote [get]cnapName, [get]cnapNapePresentation to parent
                class (Connection)
* Patch Set 4 : -remove ref to CDMA in get[CnapName|CnapNamePresentation] headers
                -remove unnecessary self ref-reference

Change-Id:I2279f75d679d5afa716d9904fc3b3e33c77948c6*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 07f90cd..c041b4a 100644

//Synthetic comment -- @@ -28,6 +28,10 @@
public static int PRESENTATION_UNKNOWN = 3;    // no specified or unknown by network
public static int PRESENTATION_PAYPHONE = 4;   // show pay phone info

    //Caller Name Display
    String cnapName;
    int cnapNamePresentation  = PRESENTATION_ALLOWED;

private static String LOG_TAG = "TelephonyConnection";

public enum DisconnectCause {
//Synthetic comment -- @@ -84,11 +88,11 @@
public abstract String getAddress();

/**
     * Gets CNAP name associated with connection.
* @return cnap name or null if unavailable
*/
public String getCnapName() {
        return cnapName;
}

/**
//Synthetic comment -- @@ -100,12 +104,12 @@
}

/**
     * Gets CNAP presentation associated with connection.
* @return cnap name or null if unavailable
*/

public int getCnapNamePresentation() {
       return cnapNamePresentation;
};

/**








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 8fb136e..e013d84 100755

//Synthetic comment -- @@ -50,7 +50,6 @@
String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

/*
//Synthetic comment -- @@ -76,7 +75,6 @@
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;


Handler h;
//Synthetic comment -- @@ -229,14 +227,6 @@
return address;
}

public CdmaCall getCall() {
return parent;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index c1ad7b3..a879dac 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.text.TextUtils;

import com.android.internal.telephony.*;

//Synthetic comment -- @@ -125,6 +126,8 @@

isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
        cnapName = dc.name;
        cnapNamePresentation = dc.namePresentation;
numberPresentation = dc.numberPresentation;
uusInfo = dc.uusInfo;

//Synthetic comment -- @@ -151,6 +154,9 @@
index = -1;

isIncoming = false;
        cnapName = null;
        cnapNamePresentation = Connection.PRESENTATION_ALLOWED;
        numberPresentation = Connection.PRESENTATION_ALLOWED;
createTime = System.currentTimeMillis();

this.parent = parent;
//Synthetic comment -- @@ -437,6 +443,21 @@
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







