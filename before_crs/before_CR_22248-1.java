/*Pull CNAM related code and data into Connection class.

This change allows to expand CNAM support to GSM devices as well (currently, CNAM support is available on CDMA devices only).
Adds setting CNAM data in GsmConnection class (the same way as in CdmaConnection).
Simplifies CdmaConnection class as a result.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 07f90cd..eef88b0 100644

//Synthetic comment -- @@ -72,6 +72,9 @@

Object userData;

/* Instance Methods */

/**
//Synthetic comment -- @@ -88,7 +91,7 @@
* @return cnap name or null if unavailable
*/
public String getCnapName() {
        return null;
}

/**
//Synthetic comment -- @@ -105,8 +108,8 @@
*/

public int getCnapNamePresentation() {
       return 0;
    };

/**
* @return Call that owns this Connection, or null if none








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index fbe455e..8825e3a 100755

//Synthetic comment -- @@ -51,7 +51,6 @@
String postDialString;      // outgoing calls only
boolean isIncoming;
boolean disconnected;
    String cnapName;
int index;          // index in CdmaCallTracker.connections[], -1 if unassigned

/*
//Synthetic comment -- @@ -77,7 +76,6 @@
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
    int cnapNamePresentation  = Connection.PRESENTATION_ALLOWED;


Handler h;
//Synthetic comment -- @@ -230,14 +228,6 @@
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








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 7dc2504..11da73b 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.os.PowerManager;
import android.os.Registrant;
import android.os.SystemClock;
import android.util.Config;
import android.util.Log;
import android.telephony.PhoneNumberUtils;
//Synthetic comment -- @@ -125,6 +126,8 @@
address = dc.number;

isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
numberPresentation = dc.numberPresentation;
uusInfo = dc.uusInfo;
//Synthetic comment -- @@ -152,6 +155,8 @@
index = -1;

isIncoming = false;
createTime = System.currentTimeMillis();

this.parent = parent;
//Synthetic comment -- @@ -438,6 +443,19 @@
changed = true;
}

if (newParent != parent) {
if (parent != null) {
parent.detach(this);







