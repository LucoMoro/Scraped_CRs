/*Add support for Caller Name Display

Copied relevant parts from CdmaConnection.java to get the information
as/if presented by network operator (Fido/Rogers in canada provide that
information, and it get displayed if there's no match in phonebook).
Although it works (the nale is displayed when the call is received),
the information is not stored (not kept in the log).

Change-Id:I837251715184b3f59ccbad418ff001dd0f292c40*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index c1ad7b3..632d1f7 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.text.TextUtils;

import com.android.internal.telephony.*;

//Synthetic comment -- @@ -46,6 +47,7 @@
boolean isIncoming;
boolean disconnected;

    String cnapName;
int index;          // index in GsmCallTracker.connections[], -1 if unassigned
// The GSM index is 1 + this

//Synthetic comment -- @@ -72,6 +74,7 @@
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
    int cnapNamePresentation = Connection.PRESENTATION_ALLOWED;
UUSInfo uusInfo;

Handler h;
//Synthetic comment -- @@ -125,6 +128,8 @@

isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
        cnapName = dc.name;
        cnapNamePresentation = dc.namePresentation;
numberPresentation = dc.numberPresentation;
uusInfo = dc.uusInfo;

//Synthetic comment -- @@ -151,6 +156,9 @@
index = -1;

isIncoming = false;
        cnapName = null;
        cnapNamePresentation = Connection.PRESENTATION_ALLOWED;
        numberPresentation = Connection.PRESENTATION_ALLOWED;
createTime = System.currentTimeMillis();

this.parent = parent;
//Synthetic comment -- @@ -189,6 +197,14 @@
return parent;
}

    public String getCnapName() {
        return cnapName;
    }

    public int getCnapNamePresentation() {
        return cnapNamePresentation;
    }

public long getCreateTime() {
return createTime;
}
//Synthetic comment -- @@ -437,6 +453,21 @@
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







