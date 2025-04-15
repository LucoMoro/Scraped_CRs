/*telephony: clean up redundant radio technology definitions

- Keep just one radio technology enumeration, should match those defined in ril.h

Change-Id:I2214e3d297db5967f90386880e2b3aaa1db6d578*/




//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarPolicy.java b/services/java/com/android/server/status/StatusBarPolicy.java
//Synthetic comment -- index 3b0c436..9b48711 100644

//Synthetic comment -- @@ -950,13 +950,7 @@
}

private boolean isEvdo() {
        return mServiceState != null && mServiceState.getRadioTechnology().isEvdo();
}

private boolean hasService() {








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 35a2c19..319e80c 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package android.telephony;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone.RadioTechnology;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
//Synthetic comment -- @@ -61,37 +64,6 @@
*/
public static final int STATE_POWER_OFF = 3;

/**
* Available registration states for GSM, UMTS and CDMA.
*/
//Synthetic comment -- @@ -118,7 +90,7 @@
private boolean mIsEmergencyOnly;

//***** CDMA
    private Phone.RadioTechnology mRadioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
private boolean mCssIndicator;
private int mNetworkId;
private int mSystemId;
//Synthetic comment -- @@ -187,7 +159,7 @@
mOperatorAlphaShort = in.readString();
mOperatorNumeric = in.readString();
mIsManualNetworkSelection = in.readInt() != 0;
        mRadioTechnology = Phone.RadioTechnology.getRadioTechFromInt(in.readInt());
mCssIndicator = (in.readInt() != 0);
mNetworkId = in.readInt();
mSystemId = in.readInt();
//Synthetic comment -- @@ -205,7 +177,7 @@
out.writeString(mOperatorAlphaShort);
out.writeString(mOperatorNumeric);
out.writeInt(mIsManualNetworkSelection ? 1 : 0);
        out.writeInt(mRadioTechnology.ordinal());
out.writeInt(mCssIndicator ? 1 : 0);
out.writeInt(mNetworkId);
out.writeInt(mSystemId);
//Synthetic comment -- @@ -383,51 +355,7 @@

@Override
public String toString() {
        String radioTechnology = mRadioTechnology.toString();

return (mState + " " + (mRoaming ? "roaming" : "home")
+ " " + mOperatorAlphaLong
//Synthetic comment -- @@ -450,7 +378,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -469,7 +397,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -568,7 +496,7 @@
mOperatorAlphaShort = m.getString("operator-alpha-short");
mOperatorNumeric = m.getString("operator-numeric");
mIsManualNetworkSelection = m.getBoolean("manual");
        mRadioTechnology = Phone.RadioTechnology.getRadioTechFromInt(m.getInt("radioTechnology"));
mCssIndicator = m.getBoolean("cssIndicator");
mNetworkId = m.getInt("networkId");
mSystemId = m.getInt("systemId");
//Synthetic comment -- @@ -590,7 +518,7 @@
m.putString("operator-alpha-short", mOperatorAlphaShort);
m.putString("operator-numeric", mOperatorNumeric);
m.putBoolean("manual", Boolean.valueOf(mIsManualNetworkSelection));
        m.putInt("radioTechnology", mRadioTechnology.ordinal());
m.putBoolean("cssIndicator", mCssIndicator);
m.putInt("networkId", mNetworkId);
m.putInt("systemId", mSystemId);
//Synthetic comment -- @@ -601,8 +529,10 @@

//***** CDMA
/** @hide */
    public void setRadioTechnology(Phone.RadioTechnology radioTech) {
        if (radioTech == null)
            radioTech = RadioTechnology.RADIO_TECH_UNKNOWN;
        this.mRadioTechnology = radioTech;
}

/** @hide */
//Synthetic comment -- @@ -617,7 +547,7 @@
}

/** @hide */
    public Phone.RadioTechnology getRadioTechnology() {
return this.mRadioTechnology;
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..9a24530 100644

//Synthetic comment -- @@ -82,8 +82,6 @@
}
}

// Used as parameter to dial() and setCLIR() below
static final int CLIR_DEFAULT = 0;      // "use subscription default value"
static final int CLIR_INVOCATION = 1;   // (restrict CLI presentation)








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 23325f6..9d36252 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Log;

import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.gsm.NetworkInfo;
//Synthetic comment -- @@ -95,6 +96,82 @@
UNKNOWN, SWITCH, SEPARATE, TRANSFER, CONFERENCE, REJECT, HANGUP;
};

    public enum RadioTechnologyFamily {
        RADIO_TECH_UNKNOWN,
        RADIO_TECH_3GPP,        /* 3GPP Technologies - GSM, WCDMA, */
        RADIO_TECH_3GPP2;       /* 3GPP2 Technologies - CDMA, EVDO */

        public boolean isUnknown() {
            return this == RADIO_TECH_UNKNOWN;
        }

        public boolean isGsm() {
            return this == RADIO_TECH_3GPP;
        }

        public boolean isCdma() {
            return this == RADIO_TECH_3GPP2;
        }

        public static RadioTechnologyFamily getRadioTechFamilyFromInt(int techInt) {
            RadioTechnologyFamily ret = RADIO_TECH_UNKNOWN;
            try {
                ret = values()[techInt];
            } catch (IndexOutOfBoundsException e) {
                Log.e("RIL", "Invalid radio technology family : " + techInt);
            }
            return ret;
        }
    }

    public enum RadioTechnology {
        /* implicitly matches those defined in ril.h */
        RADIO_TECH_UNKNOWN, //0
        RADIO_TECH_GPRS,    //1
        RADIO_TECH_EDGE,    //2
        RADIO_TECH_UMTS,    //3
        RADIO_TECH_IS95A,   //4
        RADIO_TECH_IS95B,   //5
        RADIO_TECH_1xRTT,   //6
        RADIO_TECH_EVDO_0,  //7
        RADIO_TECH_EVDO_A,  //8
        RADIO_TECH_HSDPA,   //9
        RADIO_TECH_HSUPA,   //10
        RADIO_TECH_HSPA,    //11
        RADIO_TECH_EVDO_B;  //12

        public boolean isUnknown() {
            return this == RADIO_TECH_UNKNOWN;
        }

        public boolean isGsm() {
            return this == RADIO_TECH_GPRS || this == RADIO_TECH_EDGE || this == RADIO_TECH_UMTS
                    || this == RADIO_TECH_HSDPA || this == RADIO_TECH_HSUPA
                    || this == RADIO_TECH_HSPA;
        }

        public boolean isCdma() {
            return this == RADIO_TECH_IS95A || this == RADIO_TECH_IS95B || this == RADIO_TECH_1xRTT
                    || this == RADIO_TECH_EVDO_0 || this == RADIO_TECH_EVDO_A
                    || this == RADIO_TECH_EVDO_B;
        }

        public boolean isEvdo() {
            return this == RADIO_TECH_EVDO_0 || this == RADIO_TECH_EVDO_A
                    || this == RADIO_TECH_EVDO_B;
        }

        public static RadioTechnology getRadioTechFromInt(int techInt) {
            RadioTechnology rt = RADIO_TECH_UNKNOWN;
            try {
                rt = values()[techInt];
            } catch (IndexOutOfBoundsException e) {
                Log.e("RIL", "Invalid radio technology : " + techInt);
            }
            return rt;
        }
    }

static final String STATE_KEY = "state";
static final String PHONE_NAME_KEY = "phoneName";
static final String FAILURE_REASON_KEY = "reason";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..9be1816 100644

//Synthetic comment -- @@ -29,23 +29,6 @@
*/
public abstract class ServiceStateTracker extends Handler {

protected CommandsInterface cm;

public ServiceState ss;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2cad6cc..67520e0 100644

//Synthetic comment -- @@ -47,6 +47,8 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone.RadioTechnology;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
//Synthetic comment -- @@ -79,8 +81,8 @@
/**
*  Values correspond to ServiceStateTracker.DATA_ACCESS_ definitions.
*/
    private Phone.RadioTechnology networkType = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;
    private Phone.RadioTechnology newNetworkType = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;

private boolean mCdmaRoaming = false;
private int mRoamingIndicator;
//Synthetic comment -- @@ -665,7 +667,8 @@
states = (String[])ar.result;

int registrationState = 4;     //[0] registrationState
                Phone.RadioTechnology radioTechnology = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;
                                               //[3] radioTechnology
int baseStationId = -1;        //[4] baseStationId
//[5] baseStationLatitude
int baseStationLatitude = CdmaCellLocation.INVALID_LAT_LONG;
//Synthetic comment -- @@ -685,7 +688,8 @@
registrationState = Integer.parseInt(states[0]);
}
if (states[3] != null) {
                            radioTechnology = Phone.RadioTechnology.getRadioTechFromInt(
                                    Integer.parseInt(states[3]));
}
if (states[4] != null) {
baseStationId = Integer.parseInt(states[4]);
//Synthetic comment -- @@ -927,36 +931,6 @@
}
}

private void fixTimeZone(String isoCountryCode) {
TimeZone zone = null;
// If the offset is (0, false) and the time zone property
//Synthetic comment -- @@ -1051,8 +1025,9 @@
newSS.setStateOutOfService(); // clean slate for next time

if (hasNetworkTypeChanged) {
            phone.setSystemProperty(
                    TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkType.toString());
}

if (hasRegistered) {
//Synthetic comment -- @@ -1226,27 +1201,10 @@
}


    private int radioTechnologyToDataServiceState(Phone.RadioTechnology radioTech) {
        if (radioTech != null && radioTech.isCdma())
            return ServiceState.STATE_IN_SERVICE;
        return ServiceState.STATE_OUT_OF_SERVICE;
}

/** code is registration state 0-5 from TS 27.007 7.2 */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..7d005a2 100644

//Synthetic comment -- @@ -53,6 +53,8 @@
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone.RadioTechnology;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyIntents;
//Synthetic comment -- @@ -79,11 +81,8 @@
private int gprsState = ServiceState.STATE_OUT_OF_SERVICE;
private int newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;

    private Phone.RadioTechnology networkType = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;
    private Phone.RadioTechnology newNetworkType = Phone.RadioTechnology.RADIO_TECH_UNKNOWN;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
//Synthetic comment -- @@ -698,8 +697,8 @@
}
newGPRSState = regCodeToServiceState(regState);
mDataRoaming = regCodeIsRoaming(regState);
                    newNetworkType = Phone.RadioTechnology.getRadioTechFromInt(type);
                    newSS.setRadioTechnology(newNetworkType);
break;

case EVENT_POLL_STATE_OPERATOR:
//Synthetic comment -- @@ -819,44 +818,13 @@
}
}

private void pollStateDone() {
if (DBG) {
Log.d(LOG_TAG, "Poll ServiceState done: " +
" oldSS=[" + ss + "] newSS=[" + newSS +
"] oldGprs=" + gprsState + " newGprs=" + newGPRSState +
                " oldType=" + networkType+
                " newType=" + newNetworkType);
}

boolean hasRegistered =
//Synthetic comment -- @@ -909,10 +877,11 @@
int cid = -1;
GsmCellLocation loc = ((GsmCellLocation)phone.getCellLocation());
if (loc != null) cid = loc.getCid();
            EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED, cid,
                    networkType.ordinal(), newNetworkType.ordinal());
Log.d(LOG_TAG,
                    "RAT switched " + networkType + " -> "
                    + newNetworkType + " at cell " + cid);
}

gprsState = newGPRSState;
//Synthetic comment -- @@ -922,7 +891,7 @@

if (hasNetworkTypeChanged) {
phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkType.toString());
}

if (hasRegistered) {
//Synthetic comment -- @@ -1334,7 +1303,9 @@
* that could support voice and data simultaneously.
*/
boolean isConcurrentVoiceAndData() {
        return (networkType.isGsm() &&
                networkType != Phone.RadioTechnology.RADIO_TECH_EDGE &&
                networkType != Phone.RadioTechnology.RADIO_TECH_GPRS);
}

/**







