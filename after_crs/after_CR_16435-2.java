/*telephony: clean up radio technology constants

- remove all redundant radio technology constants and put them in a single place
- to add a new radio technology just modify Phone.java and ril.h

Change-Id:Ief13a56b6afffd1b213b0c543e761fc64800abd1*/




//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarPolicy.java b/services/java/com/android/server/status/StatusBarPolicy.java
//Synthetic comment -- index 3b0c436..9b48711 100644

//Synthetic comment -- @@ -950,13 +950,7 @@
}

private boolean isEvdo() {
        return mServiceState != null && mServiceState.getRadioTechnology().isEvdo();
}

private boolean hasService() {








//Synthetic comment -- diff --git a/telephony/java/android/telephony/RadioTechnology.java b/telephony/java/android/telephony/RadioTechnology.java
new file mode 100644
//Synthetic comment -- index 0000000..7a94d5b

//Synthetic comment -- @@ -0,0 +1,67 @@
package android.telephony;

import android.util.Log;

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
\ No newline at end of file








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 35a2c19..7c471ec 100644

//Synthetic comment -- @@ -61,37 +61,6 @@
*/
public static final int STATE_POWER_OFF = 3;

/**
* Available registration states for GSM, UMTS and CDMA.
*/
//Synthetic comment -- @@ -118,7 +87,7 @@
private boolean mIsEmergencyOnly;

//***** CDMA
    private RadioTechnology mRadioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
private boolean mCssIndicator;
private int mNetworkId;
private int mSystemId;
//Synthetic comment -- @@ -187,7 +156,7 @@
mOperatorAlphaShort = in.readString();
mOperatorNumeric = in.readString();
mIsManualNetworkSelection = in.readInt() != 0;
        mRadioTechnology = RadioTechnology.getRadioTechFromInt(in.readInt());
mCssIndicator = (in.readInt() != 0);
mNetworkId = in.readInt();
mSystemId = in.readInt();
//Synthetic comment -- @@ -205,7 +174,7 @@
out.writeString(mOperatorAlphaShort);
out.writeString(mOperatorNumeric);
out.writeInt(mIsManualNetworkSelection ? 1 : 0);
        out.writeInt(mRadioTechnology.ordinal());
out.writeInt(mCssIndicator ? 1 : 0);
out.writeInt(mNetworkId);
out.writeInt(mSystemId);
//Synthetic comment -- @@ -383,51 +352,7 @@

@Override
public String toString() {
        String radioTechnology = mRadioTechnology.toString();

return (mState + " " + (mRoaming ? "roaming" : "home")
+ " " + mOperatorAlphaLong
//Synthetic comment -- @@ -450,7 +375,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -469,7 +394,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -568,7 +493,7 @@
mOperatorAlphaShort = m.getString("operator-alpha-short");
mOperatorNumeric = m.getString("operator-numeric");
mIsManualNetworkSelection = m.getBoolean("manual");
        mRadioTechnology = RadioTechnology.getRadioTechFromInt(m.getInt("radioTechnology"));
mCssIndicator = m.getBoolean("cssIndicator");
mNetworkId = m.getInt("networkId");
mSystemId = m.getInt("systemId");
//Synthetic comment -- @@ -590,7 +515,7 @@
m.putString("operator-alpha-short", mOperatorAlphaShort);
m.putString("operator-numeric", mOperatorNumeric);
m.putBoolean("manual", Boolean.valueOf(mIsManualNetworkSelection));
        m.putInt("radioTechnology", mRadioTechnology.ordinal());
m.putBoolean("cssIndicator", mCssIndicator);
m.putInt("networkId", mNetworkId);
m.putInt("systemId", mSystemId);
//Synthetic comment -- @@ -601,8 +526,10 @@

//***** CDMA
/** @hide */
    public void setRadioTechnology(RadioTechnology radioTech) {
        if (radioTech == null)
            radioTech = RadioTechnology.RADIO_TECH_UNKNOWN;
        this.mRadioTechnology = radioTech;
}

/** @hide */
//Synthetic comment -- @@ -617,7 +544,7 @@
}

/** @hide */
    public RadioTechnology getRadioTechnology() {
return this.mRadioTechnology;
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..9be1816 100644

//Synthetic comment -- @@ -29,23 +29,6 @@
*/
public abstract class ServiceStateTracker extends Handler {

protected CommandsInterface cm;

public ServiceState ss;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2cad6cc..2055266 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.cdma.CdmaCellLocation;
//Synthetic comment -- @@ -79,8 +80,8 @@
/**
*  Values correspond to ServiceStateTracker.DATA_ACCESS_ definitions.
*/
    private RadioTechnology networkType = RadioTechnology.RADIO_TECH_UNKNOWN;
    private RadioTechnology newNetworkType = RadioTechnology.RADIO_TECH_UNKNOWN;

private boolean mCdmaRoaming = false;
private int mRoamingIndicator;
//Synthetic comment -- @@ -665,7 +666,8 @@
states = (String[])ar.result;

int registrationState = 4;     //[0] registrationState
                RadioTechnology radioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
                                               //[3] radioTechnology
int baseStationId = -1;        //[4] baseStationId
//[5] baseStationLatitude
int baseStationLatitude = CdmaCellLocation.INVALID_LAT_LONG;
//Synthetic comment -- @@ -685,7 +687,8 @@
registrationState = Integer.parseInt(states[0]);
}
if (states[3] != null) {
                            radioTechnology = RadioTechnology.getRadioTechFromInt(
                                    Integer.parseInt(states[3]));
}
if (states[4] != null) {
baseStationId = Integer.parseInt(states[4]);
//Synthetic comment -- @@ -927,36 +930,6 @@
}
}

private void fixTimeZone(String isoCountryCode) {
TimeZone zone = null;
// If the offset is (0, false) and the time zone property
//Synthetic comment -- @@ -1051,8 +1024,9 @@
newSS.setStateOutOfService(); // clean slate for next time

if (hasNetworkTypeChanged) {
            phone.setSystemProperty(
                    TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkType.toString());
}

if (hasRegistered) {
//Synthetic comment -- @@ -1226,27 +1200,10 @@
}


    private int radioTechnologyToDataServiceState(RadioTechnology radioTech) {
        if (radioTech != null && radioTech.isCdma())
            return ServiceState.STATE_IN_SERVICE;
        return ServiceState.STATE_OUT_OF_SERVICE;
}

/** code is registration state 0-5 from TS 27.007 7.2 */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..8c7990f 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.gsm.GsmCellLocation;
//Synthetic comment -- @@ -79,11 +80,8 @@
private int gprsState = ServiceState.STATE_OUT_OF_SERVICE;
private int newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;

    private RadioTechnology networkType = RadioTechnology.RADIO_TECH_UNKNOWN;
    private RadioTechnology newNetworkType = RadioTechnology.RADIO_TECH_UNKNOWN;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
//Synthetic comment -- @@ -698,8 +696,8 @@
}
newGPRSState = regCodeToServiceState(regState);
mDataRoaming = regCodeIsRoaming(regState);
                    newNetworkType = RadioTechnology.getRadioTechFromInt(type);
                    newSS.setRadioTechnology(newNetworkType);
break;

case EVENT_POLL_STATE_OPERATOR:
//Synthetic comment -- @@ -819,44 +817,13 @@
}
}

private void pollStateDone() {
if (DBG) {
Log.d(LOG_TAG, "Poll ServiceState done: " +
" oldSS=[" + ss + "] newSS=[" + newSS +
"] oldGprs=" + gprsState + " newGprs=" + newGPRSState +
                " oldType=" + networkType.toString()+
                " newType=" + newNetworkType.toString());
}

boolean hasRegistered =
//Synthetic comment -- @@ -909,10 +876,11 @@
int cid = -1;
GsmCellLocation loc = ((GsmCellLocation)phone.getCellLocation());
if (loc != null) cid = loc.getCid();
            EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED, cid,
                    networkType.toString(), newNetworkType.toString());
Log.d(LOG_TAG,
                    "RAT switched " + networkType.toString() + " -> "
                    + newNetworkType.toString() + " at cell " + cid);
}

gprsState = newGPRSState;
//Synthetic comment -- @@ -922,7 +890,7 @@

if (hasNetworkTypeChanged) {
phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkType.toString());
}

if (hasRegistered) {
//Synthetic comment -- @@ -1334,7 +1302,9 @@
* that could support voice and data simultaneously.
*/
boolean isConcurrentVoiceAndData() {
        return (networkType.isGsm() &&
                networkType != RadioTechnology.RADIO_TECH_EDGE &&
                networkType != RadioTechnology.RADIO_TECH_GPRS);
}

/**







