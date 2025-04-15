/*Support for User to User Signaling (UUS)

Enabling passing UUS information during MO and MT calls.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d90c305..8e03c5a 100644

//Synthetic comment -- @@ -664,6 +664,19 @@
*  retMsg.obj = AsyncResult ar
*  ar.exception carries exception on failure
*  ar.userObject contains the orignal value of result.obj
     *  ar.result is null on success and failure
     *
     * CLIR_DEFAULT     == on "use subscription default value"
     * CLIR_SUPPRESSION == on "CLIR suppression" (allow CLI presentation)
     * CLIR_INVOCATION  == on "CLIR invocation" (restrict CLI presentation)
     */
    void dial(String address, int clirMode, UUSInfo uusInfo, Message result);

    /**
     *  returned message
     *  retMsg.obj = AsyncResult ar
     *  ar.exception carries exception on failure
     *  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
void getIMSI(Message result);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 37e8a99..4ccf240 100644

//Synthetic comment -- @@ -273,6 +273,13 @@
public abstract int getNumberPresentation();

/**
     * Returns the User to User Signaling (UUS) information associated with
     * incoming and waiting calls
     * @return UUSInfo containing the UUS userdata.
     */
    public abstract UUSInfo getUUSInfo();

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
    public UUSInfo uusInfo;

/** returns null on error */
static DriverCall








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 11386aea..24f08cb9 100644

//Synthetic comment -- @@ -789,6 +789,19 @@
Connection dial(String dialString) throws CallStateException;

/**
     * Initiate a new voice connection with supplementary User to User
     * Information. This happens asynchronously, so you cannot assume the audio
     * path is connected (or a call index has been assigned) until
     * PhoneStateChanged notification has occurred.
     *
     * @exception CallStateException if a new outgoing call is not currently
     *                possible because no more call slots exist or a call exists
     *                that is dialing, alerting, ringing, or waiting. Other
     *                errors are handled asynchronously.
     */
    Connection dial(String dialString, UUSInfo uusInfo) throws CallStateException;

    /**
* Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
* without SEND (so <code>dial</code> is not appropriate).
*








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 1ef8d37..77c7c28 100644

//Synthetic comment -- @@ -415,6 +415,10 @@
return mActivePhone.dial(dialString);
}

    public Connection dial(String dialString, UUSInfo uusInfo) throws CallStateException {
        return mActivePhone.dial(dialString, uusInfo);
    }

public boolean handlePinMmi(String dialString) {
return mActivePhone.handlePinMmi(dialString);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 86cb6fb..7239889 100644

//Synthetic comment -- @@ -788,11 +788,25 @@

public void
dial (String address, int clirMode, Message result) {
        dial(address, clirMode, null, result);
    }

    public void
    dial(String address, int clirMode, UUSInfo uusInfo, Message result) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_DIAL, result);

rr.mp.writeString(address);
rr.mp.writeInt(clirMode);

        if (uusInfo == null) {
            rr.mp.writeInt(0); // UUS information is absent
        } else {
            rr.mp.writeInt(1); // UUS information is present
            rr.mp.writeInt(uusInfo.getType());
            rr.mp.writeInt(uusInfo.getDcs());
            rr.mp.writeByteArray(uusInfo.getUserData());
        }

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

send(rr);
//Synthetic comment -- @@ -2825,6 +2839,24 @@
dc.numberPresentation = DriverCall.presentationFromCLIP(np);
dc.name = p.readString();
dc.namePresentation = p.readInt();
            int uusInfoPresent = p.readInt();
            if (uusInfoPresent == 1) {
                dc.uusInfo = new UUSInfo();
                dc.uusInfo.setType(p.readInt());
                dc.uusInfo.setDcs(p.readInt());
                byte[] userData = p.createByteArray();
                dc.uusInfo.setUserData(userData);
                Log
                        .v(LOG_TAG, String.format("Incoming UUS : type=%d, dcs=%d, length=%d",
                                dc.uusInfo.getType(), dc.uusInfo.getDcs(),
                                dc.uusInfo.getUserData().length));
                Log.v(LOG_TAG, "Incoming UUS : data (string)="
                        + new String(dc.uusInfo.getUserData()));
                Log.v(LOG_TAG, "Incoming UUS : data (hex): "
                        + IccUtils.bytesToHexString(dc.uusInfo.getUserData()));
            } else {
                Log.v(LOG_TAG, "Incoming UUS : NOT present!");
            }

// Make sure there's a leading + on addresses with a TOA of 145
dc.number = PhoneNumberUtils.stringFromStringAndTOA(dc.number, dc.TOA);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/UUSInfo.java b/telephony/java/com/android/internal/telephony/UUSInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..30f7c82

//Synthetic comment -- @@ -0,0 +1,112 @@
/* Copyright (c) 2010, Code Aurora Forum. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Code Aurora nor
 *       the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written
 *       permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.android.internal.telephony;

public class UUSInfo {

    /*
     * User-to-User signaling Info activation types derived from 3GPP 23.087
     * v8.0
     */

    public static final int UUS_TYPE1_IMPLICIT = 0;

    public static final int UUS_TYPE1_REQUIRED = 1;

    public static final int UUS_TYPE1_NOT_REQUIRED = 2;

    public static final int UUS_TYPE2_REQUIRED = 3;

    public static final int UUS_TYPE2_NOT_REQUIRED = 4;

    public static final int UUS_TYPE3_REQUIRED = 5;

    public static final int UUS_TYPE3_NOT_REQUIRED = 6;

    /*
     * User-to-User Signaling Information data coding schemes. Possible values
     * for Octet 3 (Protocol Discriminator field) in the UUIE. The values have
     * been specified in section 10.5.4.25 of 3GPP TS 24.008
     */

    public static final int UUS_DCS_USP = 0; /* User specified protocol */

    public static final int UUS_DCS_OSIHLP = 1; /* OSI higher layer protocol */

    public static final int UUS_DCS_X244 = 2; /* X.244 */

    public static final int UUS_DCS_RMCF = 3; /*
                                               * Reserved for system management
                                               * convergence function
                                               */

    public static final int UUS_DCS_IA5c = 4; /* IA5 characters */

    private int uusType;

    private int uusDcs;

    private byte[] uusData;

    public UUSInfo() {
        this.uusType = UUS_TYPE1_IMPLICIT;
        this.uusDcs = UUS_DCS_IA5c;
        this.uusData = null;
    }

    public UUSInfo(int uusType, int uusDcs, byte[] uusData) {
        this.uusType = uusType;
        this.uusDcs = uusDcs;
        this.uusData = uusData;
    }

    public int getDcs() {
        return uusDcs;
    }

    public void setDcs(int uusDcs) {
        this.uusDcs = uusDcs;
    }

    public int getType() {
        return uusType;
    }

    public void setType(int uusType) {
        this.uusType = uusType;
    }

    public byte[] getUserData() {
        return uusData;
    }

    public void setUserData(byte[] uusData) {
        this.uusData = uusData;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 3548cad..e1fde7a 100755

//Synthetic comment -- @@ -62,6 +62,7 @@
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
//Synthetic comment -- @@ -354,6 +355,10 @@
return mCT.dial(newDialString);
}

    public Connection dial(String dialString, UUSInfo uusInfo) throws CallStateException {
        throw new CallStateException("Sending UUS information NOT supported in CDMA!");
    }

public SignalStrength getSignalStrength() {
return mSST.mSignalStrength;
}
//Synthetic comment -- @@ -1436,5 +1441,4 @@
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

    @Override
    public UUSInfo getUUSInfo() {
        // UUS information not supported in CDMA
        return null;
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
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.gsm.stk.StkService;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.IccVmNotSupportedException;
//Synthetic comment -- @@ -719,7 +720,12 @@
}

public Connection
    dial(String dialString) throws CallStateException {
        return dial(dialString, null);
    }

    public Connection
    dial (String dialString, UUSInfo uusInfo) throws CallStateException {
// Need to make sure dialString gets parsed properly
String newDialString = PhoneNumberUtils.stripSeparators(dialString);

//Synthetic comment -- @@ -735,9 +741,9 @@
"dialing w/ mmi '" + mmi + "'...");

if (mmi == null) {
            return mCT.dial(newDialString, uusInfo);
} else if (mmi.isTemporaryModeCLIR()) {
            return mCT.dial(mmi.dialingNumber, mmi.getCLIRMode(), uusInfo);
} else {
mPendingMMIs.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 91c089e..b82fefd 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.gsm.CallFailCause;
import com.android.internal.telephony.gsm.GsmCall;
import com.android.internal.telephony.gsm.GsmConnection;
//Synthetic comment -- @@ -162,7 +163,7 @@
* clirMode is one of the CLIR_ constants
*/
Connection
    dial (String dialString, int clirMode, UUSInfo uusInfo) throws CallStateException {
// note that this triggers call state changed notif
clearDisconnected();

//Synthetic comment -- @@ -208,7 +209,7 @@
// Always unmute when initiating a new call
setMute(false);

            cm.dial(pendingMO.address, clirMode, uusInfo, obtainCompleteMessage());
}

updatePhoneState();
//Synthetic comment -- @@ -217,10 +218,19 @@
return pendingMO;
}

    Connection
    dial(String dialString) throws CallStateException {
        return dial(dialString, CommandsInterface.CLIR_DEFAULT, null);
    }

Connection
    dial(String dialString, UUSInfo uusInfo) throws CallStateException {
        return dial(dialString, CommandsInterface.CLIR_DEFAULT, uusInfo);
    }

    Connection
    dial(String dialString, int clirMode) throws CallStateException {
        return dial(dialString, clirMode, null);
}

void








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 445be39..12e6fe5 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
DisconnectCause cause = DisconnectCause.NOT_DISCONNECTED;
PostDialState postDialState = PostDialState.NOT_STARTED;
int numberPresentation = Connection.PRESENTATION_ALLOWED;
    UUSInfo uusInfo;

Handler h;

//Synthetic comment -- @@ -126,6 +127,7 @@
isIncoming = dc.isMT;
createTime = System.currentTimeMillis();
numberPresentation = dc.numberPresentation;
        uusInfo = dc.uusInfo;

this.index = index;

//Synthetic comment -- @@ -728,4 +730,9 @@
public int getNumberPresentation() {
return numberPresentation;
}

    @Override
    public UUSInfo getUUSInfo() {
        return uusInfo;
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 11b3fd6..a120f52 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.gsm.CallFailCause;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
//Synthetic comment -- @@ -496,6 +497,23 @@
*  retMsg.obj = AsyncResult ar
*  ar.exception carries exception on failure
*  ar.userObject contains the orignal value of result.obj
     *  ar.result is null on success and failure
     *
     * CLIR_DEFAULT     == on "use subscription default value"
     * CLIR_SUPPRESSION == on "CLIR suppression" (allow CLI presentation)
     * CLIR_INVOCATION  == on "CLIR invocation" (restrict CLI presentation)
     */
    public void dial(String address, int clirMode, UUSInfo uusInfo, Message result) {
        simulatedCallState.onDial(address);

        resultSuccess(result, null);
    }

    /**
     *  returned message
     *  retMsg.obj = AsyncResult ar
     *  ar.exception carries exception on failure
     *  ar.userObject contains the orignal value of result.obj
*  ar.result is String containing IMSI on success
*/
public void getIMSI(Message result) {







