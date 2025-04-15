/*telephony: clean up radio technology constants

- remove all redundant radio technology constants and put them in a single place
- to add a new radio technology just modify Phone.java and ril.h

Change-Id:Ief13a56b6afffd1b213b0c543e761fc64800abd1*/




//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarPolicy.java b/services/java/com/android/server/status/StatusBarPolicy.java
//Synthetic comment -- index 3b0c436..e08b6d4 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.RadioTechnology;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -950,13 +951,7 @@
}

private boolean isEvdo() {
        return mServiceState != null && RadioTechnology.isEvdo(mServiceState.getRadioTechnology());
}

private boolean hasService() {








//Synthetic comment -- diff --git a/telephony/java/android/telephony/RadioTechnology.java b/telephony/java/android/telephony/RadioTechnology.java
new file mode 100644
//Synthetic comment -- index 0000000..f101563

//Synthetic comment -- @@ -0,0 +1,120 @@
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

package android.telephony;

/**
 * @hide
 * Radio technology constants
 */
public class RadioTechnology {

    /* should match those defined in ril.h */

    public static final int RADIO_TECH_UNKNOWN = 0;
    public static final int RADIO_TECH_GPRS = 1;
    public static final int RADIO_TECH_EDGE = 2;
    public static final int RADIO_TECH_UMTS = 3;
    public static final int RADIO_TECH_IS95A = 4;
    public static final int RADIO_TECH_IS95B = 5;
    public static final int RADIO_TECH_1xRTT = 6;
    public static final int RADIO_TECH_EVDO_0 = 7;
    public static final int RADIO_TECH_EVDO_A = 8;
    public static final int RADIO_TECH_HSDPA = 9;
    public static final int RADIO_TECH_HSUPA = 10;
    public static final int RADIO_TECH_HSPA = 11;
    public static final int RADIO_TECH_EVDO_B = 12;

    /**
     * @return true if radio technology is unknown or invalid.
     */
    public static boolean isUnknown(int rt) {
        return rt <= RADIO_TECH_UNKNOWN || rt > RADIO_TECH_EVDO_B;
    }

    /**
     * @return true if radio technology is 3GPP (gsm).
     */
    public static boolean isGsm(int rt) {
        return rt == RADIO_TECH_GPRS || rt == RADIO_TECH_EDGE || rt == RADIO_TECH_UMTS
                || rt == RADIO_TECH_HSDPA || rt == RADIO_TECH_HSUPA || rt == RADIO_TECH_HSPA;
    }

    /**
     * @return true if radio technology is 3GPP2 (cdma).
     */
    public static boolean isCdma(int rt) {
        return rt == RADIO_TECH_IS95A || rt == RADIO_TECH_IS95B || rt == RADIO_TECH_1xRTT
                || rt == RADIO_TECH_EVDO_0 || rt == RADIO_TECH_EVDO_A || rt == RADIO_TECH_EVDO_B;
    }

    /**
     * @return true if radio technology is 3GPP2 (cdma) EvDo Rev. 0, A or B.
     */
    public static boolean isEvdo(int rt) {
        return rt == RADIO_TECH_EVDO_0 || rt == RADIO_TECH_EVDO_A || rt == RADIO_TECH_EVDO_B;
    }

    /**
     * @return name of the radio technology.
     */
    public static String toString(int rt) {
        String ret = "UNKNOWN";
        switch (rt) {
            case RADIO_TECH_UNKNOWN:
                ret = "UNKNOWN";
                break;
            case RADIO_TECH_GPRS:
                ret = "GPRS";
                break;
            case RADIO_TECH_EDGE:
                ret = "EDGE";
                break;
            case RADIO_TECH_UMTS:
                ret = "UMTS";
                break;
            case RADIO_TECH_IS95A:
                ret = "IS95A";
                break;
            case RADIO_TECH_IS95B:
                ret = "IS95B";
                break;
            case RADIO_TECH_1xRTT:
                ret = "1xRTT";
                break;
            case RADIO_TECH_EVDO_0:
                ret = "EvDo Rev. 0";
                break;
            case RADIO_TECH_EVDO_A:
                ret = "EvDo Rev. A";
                break;
            case RADIO_TECH_HSDPA:
                ret = "HSDPA";
                break;
            case RADIO_TECH_HSUPA:
                ret = "HSUPA";
                break;
            case RADIO_TECH_HSPA:
                ret = "HSPA";
                break;
            case RADIO_TECH_EVDO_B:
                ret = "EvDo Rev. B";
                break;
            default:
        }
        return ret;
    }
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 35a2c19..038aa963 100644

//Synthetic comment -- @@ -61,37 +61,6 @@
*/
public static final int STATE_POWER_OFF = 3;

/**
* Available registration states for GSM, UMTS and CDMA.
*/
//Synthetic comment -- @@ -118,7 +87,7 @@
private boolean mIsEmergencyOnly;

//***** CDMA
    private int mRadioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
private boolean mCssIndicator;
private int mNetworkId;
private int mSystemId;
//Synthetic comment -- @@ -383,51 +352,7 @@

@Override
public String toString() {
        String radioTechnology = RadioTechnology.toString(mRadioTechnology);

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
//Synthetic comment -- @@ -601,8 +526,8 @@

//***** CDMA
/** @hide */
    public void setRadioTechnology(int radioTech) {
        this.mRadioTechnology = radioTech;
}

/** @hide */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..9be1816 100644

//Synthetic comment -- @@ -29,23 +29,6 @@
*/
public abstract class ServiceStateTracker extends Handler {

protected CommandsInterface cm;

public ServiceState ss;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2cad6cc..05cf869 100644

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
    private int networkType = RadioTechnology.RADIO_TECH_UNKNOWN;
    private int newNetworkType = RadioTechnology.RADIO_TECH_UNKNOWN;

private boolean mCdmaRoaming = false;
private int mRoamingIndicator;
//Synthetic comment -- @@ -665,7 +666,8 @@
states = (String[])ar.result;

int registrationState = 4;     //[0] registrationState
                int radioTechnology = RadioTechnology.RADIO_TECH_UNKNOWN;
                                               //[3] radioTechnology
int baseStationId = -1;        //[4] baseStationId
//[5] baseStationLatitude
int baseStationLatitude = CdmaCellLocation.INVALID_LAT_LONG;
//Synthetic comment -- @@ -927,36 +929,6 @@
}
}

private void fixTimeZone(String isoCountryCode) {
TimeZone zone = null;
// If the offset is (0, false) and the time zone property
//Synthetic comment -- @@ -1051,8 +1023,9 @@
newSS.setStateOutOfService(); // clean slate for next time

if (hasNetworkTypeChanged) {
            phone.setSystemProperty(
                    TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    RadioTechnology.toString(networkType));
}

if (hasRegistered) {
//Synthetic comment -- @@ -1226,27 +1199,10 @@
}


    private int radioTechnologyToDataServiceState(int radioTech) {
        if (RadioTechnology.isCdma(radioTech))
            return ServiceState.STATE_IN_SERVICE;
        return ServiceState.STATE_OUT_OF_SERVICE;
}

/** code is registration state 0-5 from TS 27.007 7.2 */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..77861c9 100644

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

    private int networkType = RadioTechnology.RADIO_TECH_UNKNOWN;
    private int newNetworkType = RadioTechnology.RADIO_TECH_UNKNOWN;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
//Synthetic comment -- @@ -699,7 +697,7 @@
newGPRSState = regCodeToServiceState(regState);
mDataRoaming = regCodeIsRoaming(regState);
newNetworkType = type;
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
                " oldType=" + RadioTechnology.toString(networkType)+
                " newType=" + RadioTechnology.toString(newNetworkType));
}

boolean hasRegistered =
//Synthetic comment -- @@ -909,10 +876,11 @@
int cid = -1;
GsmCellLocation loc = ((GsmCellLocation)phone.getCellLocation());
if (loc != null) cid = loc.getCid();
            EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED, cid,
                    RadioTechnology.toString(networkType), RadioTechnology.toString(newNetworkType));
Log.d(LOG_TAG,
                    "RAT switched " + RadioTechnology.toString(networkType) + " -> "
                    + RadioTechnology.toString(newNetworkType) + " at cell " + cid);
}

gprsState = newGPRSState;
//Synthetic comment -- @@ -922,7 +890,7 @@

if (hasNetworkTypeChanged) {
phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    RadioTechnology.toString(newNetworkType));
}

if (hasRegistered) {
//Synthetic comment -- @@ -1334,7 +1302,9 @@
* that could support voice and data simultaneously.
*/
boolean isConcurrentVoiceAndData() {
        return (RadioTechnology.isGsm(networkType) &&
                networkType != RadioTechnology.RADIO_TECH_EDGE &&
                networkType != RadioTechnology.RADIO_TECH_GPRS);
}

/**







