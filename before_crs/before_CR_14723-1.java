/*Fix some typos and other cosmetic cleanups in telephony framework.

- Fix typos in Javadoc and comments.
- Fix Javadoc @link references to other classes/methods.
- Rename MISSING_UKNOWN_APN to MISSING_UNKNOWN_APN in DataConnection.
- Remove unused (and misspelled) RETRYIES_* consts in RetryManager.

Change-Id:I3b44ac8320d6c1e4c350be600c7ef266aaf735e4*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index 446bbc2..71e258f 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
public static final int STATE_EMERGENCY_ONLY = 2;

/**
     * Radio of telephony is explictly powered off.
*/
public static final int STATE_POWER_OFF = 3;

//Synthetic comment -- @@ -215,7 +215,8 @@
return 0;
}

    public static final Parcelable.Creator<ServiceState> CREATOR = new Parcelable.Creator() {
public ServiceState createFromParcel(Parcel in) {
return new ServiceState(in);
}
//Synthetic comment -- @@ -226,7 +227,7 @@
};

/**
     * Get current servcie state of phone
*
* @see #STATE_IN_SERVICE
* @see #STATE_OUT_OF_SERVICE
//Synthetic comment -- @@ -278,10 +279,10 @@
}

/**
     * Get current registered operator name in long alphanumeric format
*
     * In GSM/UMTS, long format can be upto 16 characters long
     * In CDMA, returns the ERI text, if set, otherwise the ONS
*
* @return long name of operator, null if unregistered or unknown
*/
//Synthetic comment -- @@ -290,9 +291,9 @@
}

/**
     * Get current registered operator name in short lphanumeric format
*
     * In GSM/UMST, short format can be upto 8 characters long
*
* @return short name of operator, null if unregistered or unknown
*/
//Synthetic comment -- @@ -301,12 +302,13 @@
}

/**
     * Get current registered operator numeric id
*
* In GSM/UMTS, numeric format is 3 digit country code plus 2 or 3 digit
     * network code
*
     * The country code can be decoded using MccTable.countryCodeForMcc()
*
* @return numeric format of operator, null if unregistered or unknown
*/
//Synthetic comment -- @@ -315,7 +317,7 @@
}

/**
     * Get current network selection mode
*
* @return true if manual mode, false if automatic mode
*/
//Synthetic comment -- @@ -442,7 +444,7 @@
mCdmaEriIconMode = -1;
}

    // TODO - can't this be combined with the above func..
public void setStateOff() {
mState = STATE_POWER_OFF;
mRoaming = false;
//Synthetic comment -- @@ -503,8 +505,8 @@
}

/**
     * In CDMA mOperatorAlphaLong can be set from the ERI
     * text, this is done from the CDMAPhone and not from the CdmaServiceStateTracker
*
* @hide
*/
//Synthetic comment -- @@ -517,7 +519,7 @@
}

/**
     * Test whether two objects hold the same data values or both are null
*
* @param a first obj
* @param b second obj
//Synthetic comment -- @@ -528,7 +530,7 @@
}

/**
     * Set ServiceState based on intent notifier map
*
* @param m intent notifier map
* @hide
//Synthetic comment -- @@ -549,7 +551,7 @@
}

/**
     * Set intent notifier Bundle based on service state
*
* @param m intent notifier Bundle
* @hide








//Synthetic comment -- diff --git a/telephony/java/android/telephony/gsm/SmsManager.java b/telephony/java/android/telephony/gsm/SmsManager.java
//Synthetic comment -- index 241c485..3b75298 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
*  the current default SMSC
* @param text the body of the message to send
* @param sentIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is sucessfully sent, or failed.
*  The result code will be <code>Activity.RESULT_OK<code> for success,
*  or one of these errors:
*  <code>RESULT_ERROR_GENERIC_FAILURE</code>








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java b/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java
//Synthetic comment -- index 802e79b..798a5a5 100644

//Synthetic comment -- @@ -284,7 +284,7 @@
*/
public static CallerInfoAsyncQuery startQuery(int token, Context context, String number,
OnQueryCompleteListener listener, Object cookie) {
        //contruct the URI object and start Query.
Uri contactRef = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Connection.java b/telephony/java/com/android/internal/telephony/Connection.java
//Synthetic comment -- index 0bcb63a..11d0b1b 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
POWER_OFF,                      /* radio is turned off explicitly */
OUT_OF_SERVICE,                 /* out of service */
ICC_ERROR,                      /* No ICC, ICC locked, or other ICC error */
        CALL_BARRED,                    /* call was blocked by call barrring */
FDN_BLOCKED,                    /* call was blocked by fixed dial number */
CS_RESTRICTED,                  /* call was blocked by restricted all voice access */
CS_RESTRICTED_NORMAL,           /* call was blocked by restricted normal voice access */
//Synthetic comment -- @@ -56,7 +56,7 @@
CDMA_INTERCEPT,                 /* INTERCEPT order received, MS state idle entered */
CDMA_REORDER,                   /* MS has been redirected, call is cancelled */
CDMA_SO_REJECT,                 /* service option rejection */
        CDMA_RETRY_ORDER,               /* requeseted service is rejected, retry delay is set */
CDMA_ACCESS_FAILURE,
CDMA_PREEMPTED,
CDMA_NOT_EMERGENCY,              /* not an emergency call */
//Synthetic comment -- @@ -69,8 +69,8 @@
/* Instance Methods */

/**
     * Gets address (e.g., phone number) associated with connection
     * TODO: distinguish reasons for unavailablity
*
* @return address or null if unavailable
*/
//Synthetic comment -- @@ -78,7 +78,7 @@
public abstract String getAddress();

/**
     * Gets cdma CNAP name  associated with connection
* @return cnap name or null if unavailable
*/
public String getCnapName() {
//Synthetic comment -- @@ -86,15 +86,15 @@
}

/**
     * Get orignal dial string
     * @return orignal dial string or null if unavailable
*/
public String getOrigDialString(){
return null;
}

/**
     * Gets cdma CNAP presentation associated with connection
* @return cnap name or null if unavailable
*/

//Synthetic comment -- @@ -116,45 +116,45 @@
public abstract long getCreateTime();

/**
     * Connection connect time in currentTimeMillis() format
     * For outgoing calls: Begins at (DIALING|ALERTING) -> ACTIVE transition
     * For incoming calls: Begins at (INCOMING|WAITING) -> ACTIVE transition
     * Returns 0 before then
*/
public abstract long getConnectTime();

/**
     * Disconnect time in currentTimeMillis() format
     * The time when this Connection makes a transition into ENDED or FAIL
     * Returns 0 before then
*/
public abstract long getDisconnectTime();

/**
     * returns the number of milliseconds the call has been connected,
* or 0 if the call has never connected.
* If the call is still connected, then returns the elapsed
     * time since connect
*/
public abstract long getDurationMillis();

/**
* If this connection is HOLDING, return the number of milliseconds
     * that it has been on hold for (approximently)
     * If this connection is in any other state, return 0
*/

public abstract long getHoldDurationMillis();

/**
     * Returns "NOT_DISCONNECTED" if not yet disconnected
*/
public abstract DisconnectCause getDisconnectCause();

/**
* Returns true of this connection originated elsewhere
* ("MT" or mobile terminated; another party called this terminal)
     * or false if this call originated here (MO or mobile originated)
*/
public abstract boolean isIncoming();









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnection.java b/telephony/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 7809fed..5522c62 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
NONE,
OPERATOR_BARRED,
INSUFFICIENT_RESOURCES,
        MISSING_UKNOWN_APN,
UNKNOWN_PDP_ADDRESS,
USER_AUTHENTICATION,
ACTIVATION_REJECT_GGSN,
//Synthetic comment -- @@ -75,7 +75,7 @@
RADIO_ERROR_RETRY;

public boolean isPermanentFail() {
            return (this == OPERATOR_BARRED) || (this == MISSING_UKNOWN_APN) ||
(this == UNKNOWN_PDP_ADDRESS) || (this == USER_AUTHENTICATION) ||
(this == ACTIVATION_REJECT_GGSN) || (this == ACTIVATION_REJECT_UNSPECIFIED) ||
(this == SERVICE_OPTION_NOT_SUPPORTED) ||
//Synthetic comment -- @@ -102,12 +102,12 @@
return "Operator Barred";
case INSUFFICIENT_RESOURCES:
return "Insufficient Resources";
            case MISSING_UKNOWN_APN:
return "Missing / Unknown APN";
case UNKNOWN_PDP_ADDRESS:
return "Unknown PDP Address";
case USER_AUTHENTICATION:
                return "Error User Autentication";
case ACTIVATION_REJECT_GGSN:
return "Activation Reject GGSN";
case ACTIVATION_REJECT_UNSPECIFIED:








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 24f08cb9..dd2b1bf 100644

//Synthetic comment -- @@ -243,15 +243,14 @@
/**
* Get the current DataState. No change notification exists at this
* interface -- use
     * {@link com.android.telephony.PhoneStateListener PhoneStateListener}
     * instead.
*/
DataState getDataConnectionState();

/**
* Get the current DataActivityState. No change notification exists at this
* interface -- use
     * {@link TelephonyManager} instead.
*/
DataActivityState getDataActivityState();

//Synthetic comment -- @@ -853,7 +852,7 @@
* @param dtmfString is string representing the dialing digit(s) in the active call
* @param on the DTMF ON length in milliseconds, or 0 for default
* @param off the DTMF OFF length in milliseconds, or 0 for default
     * @param onCompelte is the callback message when the action is processed by BP
*
*/
void sendBurstDtmf(String dtmfString, int on, int off, Message onComplete);
//Synthetic comment -- @@ -993,7 +992,7 @@
* ((AsyncResult)onComplete.obj) is an array of int, with a length of 2.
*
* @param onComplete a callback message when the action is completed.
     *        @see com.android.internal.telephony.CommandsInterface.getCLIR for details.
*/
void getOutgoingCallerIdDisplay(Message onComplete);

//Synthetic comment -- @@ -1015,7 +1014,7 @@
* ((AsyncResult)onComplete.obj) is an array of int, with a length of 1.
*
* @param onComplete a callback message when the action is completed.
     *        @see com.android.internal.telephony.CommandsInterface.queryCallWaiting for details.
*/
void getCallWaiting(Message onComplete);

//Synthetic comment -- @@ -1485,7 +1484,7 @@
* setTTYMode
* sets a TTY mode option.
*
     * @param enable is a boolean representing the state that you are
*        requesting, true for enabled, false for disabled.
* @param onComplete a callback message when the action is completed
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneSubInfo.java b/telephony/java/com/android/internal/telephony/PhoneSubInfo.java
//Synthetic comment -- index 19900c8..21035ad 100644

//Synthetic comment -- @@ -60,7 +60,7 @@
}

/**
     * Retrieves the unique sbuscriber ID, e.g., IMSI for GSM phones.
*/
public String getSubscriberId() {
mContext.enforceCallingOrSelfPermission(READ_PHONE_STATE, "Requires READ_PHONE_STATE");








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneSubInfoProxy.java b/telephony/java/com/android/internal/telephony/PhoneSubInfoProxy.java
//Synthetic comment -- index adfbe20..202ded2 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
}

/**
     * Retrieves the unique sbuscriber ID, e.g., IMSI for GSM phones.
*/
public String getSubscriberId() {
return mPhoneSubInfo.getSubscriberId();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RetryManager.java b/telephony/java/com/android/internal/telephony/RetryManager.java
//Synthetic comment -- index 385b191..c83fb3c 100644

//Synthetic comment -- @@ -25,7 +25,7 @@

/**
* Retry manager allows a simple way to declare a series of
 * retires timeouts. After creating a RetryManager the configure
* method is used to define the sequence. A simple linear series
* may be initialized using configure with three integer parameters
* The other configure method allows a series to be declared using
//Synthetic comment -- @@ -54,18 +54,18 @@
*<p>
* Examples:
* <ul>
 * <li>3 retires with no randomization value which means its 0:
* <ul><li><code>"1000, 2000, 3000"</code></ul>
*
 * <li>10 retires with a 500 default randomization value for each and
* the 4..10 retries all using 3000 as the delay:
* <ul><li><code>"max_retries=10, default_randomization=500, 1000, 2000, 3000"</code></ul>
*
 * <li>4 retires with a 100 as the default randomization value for the first 2 values and
* the other two having specified values of 500:
* <ul><li><code>"default_randomization=100, 1000, 2000, 4000:500, 5000:500"</code></ul>
*
 * <li>Infinite number of retires with the first one at 1000, the second at 2000 all
* others will be at 3000.
* <ul><li><code>"max_retries=infinite,1000,2000,3000</code></ul>
* </ul>
//Synthetic comment -- @@ -75,9 +75,6 @@
public class RetryManager {
static public final String LOG_TAG = "RetryManager";
static public final boolean DBG = false;
    static public final int RETRYIES_NOT_STARTED = 0;
    static public final int RETRYIES_ON_GOING = 1;
    static public final int RETRYIES_COMPLETED = 2;

/**
* Retry record with times in milli-seconds
//Synthetic comment -- @@ -104,7 +101,7 @@
*/
private int mMaxRetryCount;

    /** The current number of retires */
private int mRetryCount;

/** Random number generator */
//Synthetic comment -- @@ -125,7 +122,7 @@
* @param randomizationTime a random value between 0 and
*        randomizationTime will be added to retryTime. this
*        parameter may be 0.
     * @return true if successfull
*/
public boolean configure(int maxRetryCount, int retryTime, int randomizationTime) {
Pair<Boolean, Integer> value;
//Synthetic comment -- @@ -238,7 +235,7 @@
/**
* Report whether data reconnection should be retried
*
     * @return {@code true} if the max retires has not been reached. {@code
*         false} otherwise.
*/
public boolean isRetryNeeded() {
//Synthetic comment -- @@ -285,7 +282,7 @@
if (mRetryCount > mMaxRetryCount) {
mRetryCount = mMaxRetryCount;
}
        if (DBG) log("increseRetryCount: " + mRetryCount);
}

/**








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/ServiceStateTracker.java b/telephony/java/com/android/internal/telephony/ServiceStateTracker.java
//Synthetic comment -- index be4fdb4..b50dc30 100644

//Synthetic comment -- @@ -207,8 +207,8 @@
}

/**
     * Reregister network through toggle perferred network type
     * This is a work aorund to deregister and register network since there is
* no ril api to set COPS=2 (deregister) only.
*
* @param onComplete is dispatched when this is complete.  it will be
//Synthetic comment -- @@ -230,7 +230,7 @@
/**
* These two flags manage the behavior of the cell lock -- the
* lock should be held if either flag is true.  The intention is
     * to allow temporary aquisition of the lock to get a single
* update.  Such a lock grab and release can thus be made to not
* interfere with more permanent lock holds -- in other words, the
* lock will only be released if both flags are false, and so








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index 1005d20..3669e60 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
RegistrantList callWaitingRegistrants =  new RegistrantList();


    // connections dropped durin last poll
ArrayList<CdmaConnection> droppedDuringPoll
= new ArrayList<CdmaConnection>(MAX_CONNECTIONS);









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9bc5e8e..a91dc11 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
Phone.APN_TYPE_MMS,
Phone.APN_TYPE_HIPRI };

    // Possibly promoate to base class, the only difference is
// the INTENT_RECONNECT_ALARM action is a different string.
// Do consider technology changes if it is promoted.
BroadcastReceiver mIntentReceiver = new BroadcastReceiver ()
//Synthetic comment -- @@ -415,7 +415,7 @@
CdmaDataConnection conn = findFreeDataConnection();

if (conn == null) {
            if (DBG) log("setupData: No free CdmaDataConnectionfound!");
return false;
}

//Synthetic comment -- @@ -636,7 +636,7 @@
}

/**
     * @override com.android.intenral.telephony.DataConnectionTracker
*/
@Override
protected void onEnableNewApn() {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index e0c3a47..7f8da3c 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
CdmaCellLocation cellLoc;
CdmaCellLocation newCellLoc;

     /** if time between NTIZ updates is less than mNitzUpdateSpacing the update may be ignored. */
private static final int NITZ_UPDATE_SPACING_DEFAULT = 1000 * 60 * 10;
private int mNitzUpdateSpacing = SystemProperties.getInt("ro.nitz_update_spacing",
NITZ_UPDATE_SPACING_DEFAULT);
//Synthetic comment -- @@ -391,7 +391,7 @@
}

// Release any temporary cell lock, which could have been
            // aquired to allow a single-shot location update.
disableSingleLocationUpdate();
break;

//Synthetic comment -- @@ -579,7 +579,7 @@
boolean showPlmn = false;
int rule = 0;
if (cm.getRadioState().isRUIMReady()) {
            // TODO RUIM SPN is not implemnted, EF_SPN has to be read and Display Condition
//   Character Encoding, Language Indicator and SPN has to be set
// rule = phone.mRuimRecords.getDisplayRule(ss.getOperatorNumeric());
// spn = phone.mSIMRecords.getServiceProvideName();
//Synthetic comment -- @@ -846,7 +846,6 @@
* and start over again if the radio notifies us that some
* event has changed
*/

private void
pollState() {
pollingContext = new int[1];
//Synthetic comment -- @@ -1227,7 +1226,7 @@
return ServiceState.STATE_IN_SERVICE;
case 2: // 2 is "searching", fall through
case 3: // 3 is "registration denied", fall through
        case 4: // 4 is "unknown" no vaild in current baseband
return ServiceState.STATE_OUT_OF_SERVICE;
case 5:// 5 is "Registered, roaming"
return ServiceState.STATE_IN_SERVICE;
//Synthetic comment -- @@ -1266,12 +1265,12 @@
*/
private boolean isRoamIndForHomeSystem(String roamInd) {
// retrieve the carrier-specified list of ERIs for home system
        String homeRoamIndcators = SystemProperties.get("ro.cdma.homesystem");

        if (!TextUtils.isEmpty(homeRoamIndcators)) {
// searches through the comma-separated list for a match,
// return true if one is found.
            for (String homeRoamInd : homeRoamIndcators.split(",")) {
if (homeRoamInd.equals(roamInd)) {
return true;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index 3e491d1..835e2ee 100755

//Synthetic comment -- @@ -313,7 +313,7 @@
}

/**
     * Get an SMS-SUBMIT PDU for a data message to a destination address &amp; port
*
* @param scAddr Service Centre address. null == use default
* @param destAddr the address of the destination for the message








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/TtyIntent.java b/telephony/java/com/android/internal/telephony/cdma/TtyIntent.java
//Synthetic comment -- index 3813b1d..4907aa9 100644

//Synthetic comment -- @@ -56,10 +56,10 @@
/**
* The lookup key for an int that indicates preferred TTY mode.
* Valid modes are:
     * - {@link Phone.TTY_MODE_OFF}
     * - {@link Phone.TTY_MODE_FULL}
     * - {@link Phone.TTY_MODE_HCO}
     * - {@link Phone.TTY_MODE_VCO}
*
* {@hide}
*/








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index b82fefd..151333f 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
RegistrantList voiceCallStartedRegistrants = new RegistrantList();


    // connections dropped durin last poll
ArrayList<GsmConnection> droppedDuringPoll
= new ArrayList<GsmConnection>(MAX_CONNECTIONS);









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 7b60474..4bfb707a 100644

//Synthetic comment -- @@ -44,7 +44,6 @@
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

//Synthetic comment -- @@ -150,9 +149,9 @@
static final String APN_ID = "apn_id";
private boolean canSetPreferApn = false;

    // for tracking retrys on the default APN
private RetryManager mDefaultRetryManager;
    // for tracking retrys on a secondary APN
private RetryManager mSecondaryRetryManager;

BroadcastReceiver mIntentReceiver = new BroadcastReceiver ()
//Synthetic comment -- @@ -189,8 +188,8 @@
WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED;

if (!enabled) {
                    // when wifi got disabeled, the NETWORK_STATE_CHANGED_ACTION
                    // quit and wont report disconnected til next enalbing.
mIsWifiConnected = false;
}
}
//Synthetic comment -- @@ -452,7 +451,7 @@
waitingApns = buildWaitingApns();
if (waitingApns.isEmpty()) {
if (DBG) log("No APN found");
                    notifyNoData(PdpConnection.FailCause.MISSING_UKNOWN_APN);
return false;
} else {
log ("Create from allApns : " + apnListToString(allApns));
//Synthetic comment -- @@ -1135,7 +1134,7 @@
if (isApnTypeActive(Phone.APN_TYPE_DEFAULT)) {
SystemProperties.set("gsm.defaultpdpcontext.active", "true");
if (canSetPreferApn && preferredApn == null) {
                            Log.d(LOG_TAG, "PREFERED APN is null");
preferredApn = mActiveApn;
setPreferredApn(preferredApn.id);
}
//Synthetic comment -- @@ -1273,7 +1272,7 @@
if (allApns.isEmpty()) {
if (DBG) log("No APN found for carrier: " + operator);
preferredApn = null;
            notifyNoData(PdpConnection.FailCause.MISSING_UKNOWN_APN);
} else {
preferredApn = getPreferredApn();
Log.d(LOG_TAG, "Get PreferredAPN");








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index cacf057..aa16fa3 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
* {@hide}
*
*/
public final class GsmMmiCode  extends Handler implements MmiCode {
static final String LOG_TAG = "GSM";

//***** Constants
//Synthetic comment -- @@ -51,7 +51,7 @@
static final String ACTION_REGISTER = "**";
static final String ACTION_ERASURE = "##";

    // Supp Service cocdes from TS 22.030 Annex B

//Called line presentation
static final String SC_CLIP    = "30";
//Synthetic comment -- @@ -154,7 +154,7 @@

/**
* Some dial strings in GSM are defined to do non-call setup
     * things, such as modify or query supplementry service settings (eg, call
* forwarding). These are generally referred to as "MMI codes".
* We look to see if the dial string contains a valid MMI code (potentially
* with a dial string at the end as well) and return info here.
//Synthetic comment -- @@ -457,12 +457,13 @@
&& !PhoneNumberUtils.isEmergencyNumber(dialString)
&& (phone.isInCall()
|| !((dialString.length() == 2 && dialString.charAt(0) == '1')
                         /* While contrary to TS 22.030, there is strong precendence
* for treating "0" and "00" as call setup strings.
*/
|| dialString.equals("0")
|| dialString.equals("00"))));
}
/**
* @return true if the Service Code is PIN/PIN2/PUK/PUK2-related
*/
//Synthetic comment -- @@ -472,13 +473,12 @@
}

/**
     * *See TS 22.030 Annex B
* In temporary mode, to suppress CLIR for a single call, enter:
     *      " * 31 # <called number> SEND "
*  In temporary mode, to invoke CLIR for a single call enter:
     *       " # 31 # <called number> SEND "
*/

boolean
isTemporaryModeCLIR() {
return sc != null && sc.equals(SC_CLIR) && dialingNumber != null
//Synthetic comment -- @@ -779,7 +779,7 @@
// Note that unlike most everything else, the USSD complete
// response does not complete this MMI code...we wait for
// an unsolicited USSD "Notify" or "Request".
        // The matching up of this is doene in GSMPhone.

phone.mCM.sendUSSD(ussdMessage,
obtainMessage(EVENT_USSD_COMPLETE, this));
//Synthetic comment -- @@ -1156,7 +1156,7 @@

// Each bit in the service class gets its own result line
// The service classes may be split up over multiple
                // CallForwardInfos. So, for each service classs, find out
// which CallForwardInfo represents it and then build
// the response text based on that









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bc7b7fa..2960d34 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
import android.provider.Telephony.Intents;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Config;
//Synthetic comment -- @@ -132,7 +131,7 @@
*/
private boolean mNeedToRegForSimLoaded;

    /** Started the recheck process after finding gprs should registerd but not. */
private boolean mStartedGprsRegCheck = false;

/** Already sent the event-log for no gprs register. */
//Synthetic comment -- @@ -417,7 +416,7 @@
}

// Release any temporary cell lock, which could have been
                // aquired to allow a single-shot location update.
disableSingleLocationUpdate();
break;

//Synthetic comment -- @@ -502,9 +501,9 @@
break;

case EVENT_CHECK_REPORT_GPRS:
                if (ss != null && !isGprsConsistant(gprsState, ss.getState())) {

                    // Can't register data sevice while voice service is ok
// i.e. CREG is ok while CGREG is not
// possible a network or baseband side error
int cid = -1;
//Synthetic comment -- @@ -1022,7 +1021,7 @@
phone.notifyLocationChanged();
}

        if (! isGprsConsistant(gprsState, ss.getState())) {
if (!mStartedGprsRegCheck && !mReportedGprsNoReg) {
mStartedGprsRegCheck = true;

//Synthetic comment -- @@ -1039,13 +1038,13 @@
}

/**
     * Check if GPRS got registred while voice is registered
*
* @param gprsState for GPRS registration state, i.e. CGREG in GSM
* @param serviceState for voice registration state, i.e. CREG in GSM
* @return false if device only register to voice but not gprs
*/
    private boolean isGprsConsistant (int gprsState, int serviceState) {
return !((serviceState == ServiceState.STATE_IN_SERVICE) &&
(gprsState != ServiceState.STATE_IN_SERVICE));
}
//Synthetic comment -- @@ -1100,13 +1099,13 @@

long nextTime;

        // TODO Done't poll signal strength if screen is off
sendMessageDelayed(msg, POLL_PERIOD_MILLIS);
}

/**
     *  send signal-strength-changed notification if changed
     *  Called both for solicited and unsolicited signal stength updates
*/
private void onSignalStrengthResult(AsyncResult ar) {
SignalStrength oldSignalStrength = mSignalStrength;
//Synthetic comment -- @@ -1327,7 +1326,7 @@

/**
* @return true if phone is camping on a technology (eg UMTS)
     * that could support voice and data simultaniously.
*/
boolean isConcurrentVoiceAndData() {
return (networkType >= DATA_ACCESS_UMTS);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java b/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java
//Synthetic comment -- index cb85002..e656f48 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.util.Log;

import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataLink;
import com.android.internal.telephony.Phone;
//Synthetic comment -- @@ -40,7 +39,7 @@
/** Fail cause of last PDP activate, from RIL_LastPDPActivateFailCause */
private static final int PDP_FAIL_OPERATOR_BARRED = 0x08;
private static final int PDP_FAIL_INSUFFICIENT_RESOURCES = 0x1A;
    private static final int PDP_FAIL_MISSING_UKNOWN_APN = 0x1B;
private static final int PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE = 0x1C;
private static final int PDP_FAIL_USER_AUTHENTICATION = 0x1D;
private static final int PDP_FAIL_ACTIVATION_REJECT_GGSN = 0x1E;
//Synthetic comment -- @@ -192,8 +191,8 @@
case PDP_FAIL_INSUFFICIENT_RESOURCES:
cause = FailCause.INSUFFICIENT_RESOURCES;
break;
            case PDP_FAIL_MISSING_UKNOWN_APN:
                cause = FailCause.MISSING_UKNOWN_APN;
break;
case PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE:
cause = FailCause.UNKNOWN_PDP_ADDRESS;







