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
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
//Synthetic comment -- @@ -950,13 +951,7 @@
}

private boolean isEvdo() {
        return ( (mServiceState != null)
                 && ((mServiceState.getRadioTechnology()
                        == ServiceState.RADIO_TECHNOLOGY_EVDO_0)
                     || (mServiceState.getRadioTechnology()
                        == ServiceState.RADIO_TECHNOLOGY_EVDO_A)
                     || (mServiceState.getRadioTechnology()
                        == ServiceState.RADIO_TECHNOLOGY_EVDO_B)));
}

private boolean hasService() {








//Synthetic comment -- diff --git a/telephony/java/android/telephony/RadioTechnology.java b/telephony/java/android/telephony/RadioTechnology.java
new file mode 100644
//Synthetic comment -- index 0000000..62c701a

//Synthetic comment -- @@ -0,0 +1,120 @@








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 35a2c19..038aa963 100644

//Synthetic comment -- @@ -61,37 +61,6 @@
*/
public static final int STATE_POWER_OFF = 3;


    /**
     * Available radio technologies for GSM, UMTS and CDMA.
     */
    /** @hide */
    public static final int RADIO_TECHNOLOGY_UNKNOWN = 0;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_GPRS = 1;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_EDGE = 2;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_UMTS = 3;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_IS95A = 4;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_IS95B = 5;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_1xRTT = 6;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_EVDO_0 = 7;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_EVDO_A = 8;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_HSDPA = 9;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_HSUPA = 10;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_HSPA = 11;
    /** @hide */
    public static final int RADIO_TECHNOLOGY_EVDO_B = 12;

/**
* Available registration states for GSM, UMTS and CDMA.
*/
//Synthetic comment -- @@ -118,7 +87,7 @@
private boolean mIsEmergencyOnly;

//***** CDMA
    private int mRadioTechnology;
private boolean mCssIndicator;
private int mNetworkId;
private int mSystemId;
//Synthetic comment -- @@ -383,51 +352,7 @@

@Override
public String toString() {
        String radioTechnology = new String("Error in radioTechnology");
        switch(this.mRadioTechnology) {
        case 0:
            radioTechnology = "Unknown";
            break;
        case 1:
            radioTechnology = "GPRS";
            break;
        case 2:
            radioTechnology = "EDGE";
            break;
        case 3:
            radioTechnology = "UMTS";
            break;
        case 4:
            radioTechnology = "IS95A";
            break;
        case 5:
            radioTechnology = "IS95B";
            break;
        case 6:
            radioTechnology = "1xRTT";
            break;
        case 7:
            radioTechnology = "EvDo rev. 0";
            break;
        case 8:
            radioTechnology = "EvDo rev. A";
            break;
        case 9:
            radioTechnology = "HSDPA";
            break;
        case 10:
            radioTechnology = "HSUPA";
            break;
        case 11:
            radioTechnology = "HSPA";
            break;
        case 12:
            radioTechnology = "EvDo rev. B";
            break;
        default:
            Log.w(LOG_TAG, "mRadioTechnology variable out of range.");
        break;
        }

return (mState + " " + (mRoaming ? "roaming" : "home")
+ " " + mOperatorAlphaLong
//Synthetic comment -- @@ -450,7 +375,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = 0;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -469,7 +394,7 @@
mOperatorAlphaShort = null;
mOperatorNumeric = null;
mIsManualNetworkSelection = false;
        mRadioTechnology = 0;
mCssIndicator = false;
mNetworkId = -1;
mSystemId = -1;
//Synthetic comment -- @@ -601,8 +526,8 @@

//***** CDMA
/** @hide */
    public void setRadioTechnology(int state) {
        this.mRadioTechnology = state;
}

/** @hide */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index e8bbe5e..9be1816 100644

//Synthetic comment -- @@ -29,23 +29,6 @@
*/
public abstract class ServiceStateTracker extends Handler {

    /**
     *  Access technology currently in use.
     */
    protected static final int DATA_ACCESS_UNKNOWN = 0;
    protected static final int DATA_ACCESS_GPRS = 1;
    protected static final int DATA_ACCESS_EDGE = 2;
    protected static final int DATA_ACCESS_UMTS = 3;
    protected static final int DATA_ACCESS_CDMA_IS95A = 4;
    protected static final int DATA_ACCESS_CDMA_IS95B = 5;
    protected static final int DATA_ACCESS_CDMA_1xRTT = 6;
    protected static final int DATA_ACCESS_CDMA_EvDo_0 = 7;
    protected static final int DATA_ACCESS_CDMA_EvDo_A = 8;
    protected static final int DATA_ACCESS_HSDPA = 9;
    protected static final int DATA_ACCESS_HSUPA = 10;
    protected static final int DATA_ACCESS_HSPA = 11;
    protected static final int DATA_ACCESS_CDMA_EvDo_B = 12;

protected CommandsInterface cm;

public ServiceState ss;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2cad6cc..05cf869 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.cdma.CdmaCellLocation;
//Synthetic comment -- @@ -79,8 +80,8 @@
/**
*  Values correspond to ServiceStateTracker.DATA_ACCESS_ definitions.
*/
    private int networkType = 0;
    private int newNetworkType = 0;

private boolean mCdmaRoaming = false;
private int mRoamingIndicator;
//Synthetic comment -- @@ -665,7 +666,8 @@
states = (String[])ar.result;

int registrationState = 4;     //[0] registrationState
                int radioTechnology = -1;      //[3] radioTechnology
int baseStationId = -1;        //[4] baseStationId
//[5] baseStationLatitude
int baseStationLatitude = CdmaCellLocation.INVALID_LAT_LONG;
//Synthetic comment -- @@ -927,36 +929,6 @@
}
}

    private static String networkTypeToString(int type) {
        String ret = "unknown";

        switch (type) {
        case DATA_ACCESS_CDMA_IS95A:
        case DATA_ACCESS_CDMA_IS95B:
            ret = "CDMA";
            break;
        case DATA_ACCESS_CDMA_1xRTT:
            ret = "CDMA - 1xRTT";
            break;
        case DATA_ACCESS_CDMA_EvDo_0:
            ret = "CDMA - EvDo rev. 0";
            break;
        case DATA_ACCESS_CDMA_EvDo_A:
            ret = "CDMA - EvDo rev. A";
            break;
        case DATA_ACCESS_CDMA_EvDo_B:
            ret = "CDMA - EvDo rev. B";
            break;
        default:
            if (DBG) {
                Log.e(LOG_TAG, "Wrong network. Can not return a string.");
            }
        break;
        }

        return ret;
    }

private void fixTimeZone(String isoCountryCode) {
TimeZone zone = null;
// If the offset is (0, false) and the time zone property
//Synthetic comment -- @@ -1051,8 +1023,9 @@
newSS.setStateOutOfService(); // clean slate for next time

if (hasNetworkTypeChanged) {
            phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkTypeToString(networkType));
}

if (hasRegistered) {
//Synthetic comment -- @@ -1226,27 +1199,10 @@
}


    private int radioTechnologyToDataServiceState(int code) {
        int retVal = ServiceState.STATE_OUT_OF_SERVICE;
        switch(code) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            break;
        case 6: // RADIO_TECHNOLOGY_1xRTT
        case 7: // RADIO_TECHNOLOGY_EVDO_0
        case 8: // RADIO_TECHNOLOGY_EVDO_A
        case 12: // RADIO_TECHNOLOGY_EVDO_B
            retVal = ServiceState.STATE_IN_SERVICE;
            break;
        default:
            Log.e(LOG_TAG, "Wrong radioTechnology code.");
        break;
        }
        return(retVal);
}

/** code is registration state 0-5 from TS 27.007 7.2 */








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..77861c9 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Intents;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.gsm.GsmCellLocation;
//Synthetic comment -- @@ -79,11 +80,8 @@
private int gprsState = ServiceState.STATE_OUT_OF_SERVICE;
private int newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;

    /**
     *  Values correspond to ServiceStateTracker.DATA_ACCESS_ definitions.
     */
    private int networkType = 0;
    private int newNetworkType = 0;

/**
* GSM roaming status solely based on TS 27.007 7.2 CREG. Only used by
//Synthetic comment -- @@ -699,7 +697,7 @@
newGPRSState = regCodeToServiceState(regState);
mDataRoaming = regCodeIsRoaming(regState);
newNetworkType = type;
                    newSS.setRadioTechnology(type);
break;

case EVENT_POLL_STATE_OPERATOR:
//Synthetic comment -- @@ -819,44 +817,13 @@
}
}

    private static String networkTypeToString(int type) {
        //Network Type from GPRS_REGISTRATION_STATE
        String ret = "unknown";

        switch (type) {
            case DATA_ACCESS_GPRS:
                ret = "GPRS";
                break;
            case DATA_ACCESS_EDGE:
                ret = "EDGE";
                break;
            case DATA_ACCESS_UMTS:
                ret = "UMTS";
                break;
            case DATA_ACCESS_HSDPA:
                ret = "HSDPA";
                break;
            case DATA_ACCESS_HSUPA:
                ret = "HSUPA";
                break;
            case DATA_ACCESS_HSPA:
                ret = "HSPA";
                break;
            default:
                Log.e(LOG_TAG, "Wrong network type: " + Integer.toString(type));
                break;
        }

        return ret;
    }

private void pollStateDone() {
if (DBG) {
Log.d(LOG_TAG, "Poll ServiceState done: " +
" oldSS=[" + ss + "] newSS=[" + newSS +
"] oldGprs=" + gprsState + " newGprs=" + newGPRSState +
                " oldType=" + networkTypeToString(networkType) +
                " newType=" + networkTypeToString(newNetworkType));
}

boolean hasRegistered =
//Synthetic comment -- @@ -909,10 +876,11 @@
int cid = -1;
GsmCellLocation loc = ((GsmCellLocation)phone.getCellLocation());
if (loc != null) cid = loc.getCid();
            EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED, cid, networkType, newNetworkType);
Log.d(LOG_TAG,
                    "RAT switched " + networkTypeToString(networkType) + " -> "
                    + networkTypeToString(newNetworkType) + " at cell " + cid);
}

gprsState = newGPRSState;
//Synthetic comment -- @@ -922,7 +890,7 @@

if (hasNetworkTypeChanged) {
phone.setSystemProperty(TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE,
                    networkTypeToString(networkType));
}

if (hasRegistered) {
//Synthetic comment -- @@ -1334,7 +1302,9 @@
* that could support voice and data simultaneously.
*/
boolean isConcurrentVoiceAndData() {
        return (networkType >= DATA_ACCESS_UMTS);
}

/**







