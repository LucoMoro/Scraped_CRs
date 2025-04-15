/*Support for User to User Signaling (UUS)

Enabling passing UUS information during MO and MT calls.

Change-Id:I31621c0a9d3c0607d99d18c49bb6c593cadd0327*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d90c305..8e03c5a 100644

//Synthetic comment -- @@ -664,6 +664,19 @@
*  retMsg.obj = AsyncResult ar
*  ar.exception carries exception on failure
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
void getIMSI(Message result);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 6b4c551..0bcb63a 100644

//Synthetic comment -- @@ -274,6 +274,13 @@
public abstract int getNumberPresentation();

/**
* Build a human representation of a connection instance, suitable for debugging.
* Don't log personal stuff unless in debug mode.
* @return a string representing the internal state of this connection.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DriverCall.java b/telephony/java/com/android/internal/telephony/DriverCall.java
//Synthetic comment -- index 0d9a60f..2382441 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
public int numberPresentation;
public String name;
public int namePresentation;

/** returns null on error */
static DriverCall








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 11386aea..24f08cb9 100644

//Synthetic comment -- @@ -789,6 +789,19 @@
Connection dial(String dialString) throws CallStateException;

/**
* Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
* without SEND (so <code>dial</code> is not appropriate).
*








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 1ef8d37..77c7c28 100644

//Synthetic comment -- @@ -415,6 +415,10 @@
return mActivePhone.dial(dialString);
}

public boolean handlePinMmi(String dialString) {
return mActivePhone.handlePinMmi(dialString);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 86cb6fb..7239889 100644

//Synthetic comment -- @@ -788,11 +788,25 @@

public void
dial (String address, int clirMode, Message result) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_DIAL, result);

rr.mp.writeString(address);
rr.mp.writeInt(clirMode);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

send(rr);
//Synthetic comment -- @@ -2825,6 +2839,24 @@
dc.numberPresentation = DriverCall.presentationFromCLIP(np);
dc.name = p.readString();
dc.namePresentation = p.readInt();

// Make sure there's a leading + on addresses with a TOA of 145
dc.number = PhoneNumberUtils.stringFromStringAndTOA(dc.number, dc.TOA);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UUSInfo.java b/telephony/java/com/android/internal/telephony/UUSInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..30f7c82

//Synthetic comment -- @@ -0,0 +1,112 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f94ec4b0..ea80ea4 100755

//Synthetic comment -- @@ -62,6 +62,7 @@
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
//Synthetic comment -- @@ -358,6 +359,10 @@
return mCT.dial(newDialString);
}

public SignalStrength getSignalStrength() {
return mSST.mSignalStrength;
}
//Synthetic comment -- @@ -1475,5 +1480,4 @@
}
return false;
}

}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java b/telephony/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index f637d33..81dec21 100755

//Synthetic comment -- @@ -933,4 +933,10 @@
public int getNumberPresentation() {
return numberPresentation;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
old mode 100755
new mode 100644
//Synthetic comment -- index a5188ce..b0de4f5

//Synthetic comment -- @@ -67,6 +67,7 @@
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.gsm.stk.StkService;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -719,7 +720,12 @@
}

public Connection
    dial (String dialString) throws CallStateException {
// Need to make sure dialString gets parsed properly
String newDialString = PhoneNumberUtils.stripSeparators(dialString);

//Synthetic comment -- @@ -735,9 +741,9 @@
"dialing w/ mmi '" + mmi + "'...");

if (mmi == null) {
            return mCT.dial(newDialString);
} else if (mmi.isTemporaryModeCLIR()) {
            return mCT.dial(mmi.dialingNumber, mmi.getCLIRMode());
} else {
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 91c089e..b82fefd 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.gsm.CallFailCause;
import com.android.internal.telephony.gsm.GsmCall;
import com.android.internal.telephony.gsm.GsmConnection;
//Synthetic comment -- @@ -162,7 +163,7 @@
* clirMode is one of the CLIR_ constants
*/
Connection
    dial (String dialString, int clirMode) throws CallStateException {
// note that this triggers call state changed notif
clearDisconnected();

//Synthetic comment -- @@ -208,7 +209,7 @@
// Always unmute when initiating a new call
setMute(false);

            cm.dial(pendingMO.address, clirMode, obtainCompleteMessage());
}

updatePhoneState();
//Synthetic comment -- @@ -217,10 +218,19 @@
return pendingMO;
}


Connection
    dial (String dialString) throws CallStateException {
        return dial(dialString, CommandsInterface.CLIR_DEFAULT);
}

void








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 985ec9a..0024c9b 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;

Handler h;

//Synthetic comment -- @@ -126,6 +127,7 @@
isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
numberPresentation = dc.numberPresentation;

this.index = index;

//Synthetic comment -- @@ -731,4 +733,9 @@
public int getNumberPresentation() {
return numberPresentation;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 11b3fd6..a120f52 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.gsm.CallFailCause;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
//Synthetic comment -- @@ -496,6 +497,23 @@
*  retMsg.obj = AsyncResult ar
*  ar.exception carries exception on failure
*  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
public void getIMSI(Message result) {







