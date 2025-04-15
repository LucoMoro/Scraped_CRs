/*Fix for data and roaming icons

- Enable data connected flag based on the data state.

- Do not display data icon in data connected state
if radio type is unknown

- Display data icon independent of voice service state
i.e even if voice network is unavailable

- Show cdma roaming indicators if either voice
or data is roaming.

- Cleanup roaming indicator default values.
Set default CDMA Eri index to roaming off

- Show service if either voice/data is in
service if ro.config.combined_signal is true

- Consider eri only when in service

- Add in service check to isCdmaEri.
roaming related information
in registration state will not be valid
during out of service.

CRs-Fixed: 285986, 303275, 326204, 326634, 333008,
           339297
Change-Id:I9380d19b7dc763ed43c8b8442150674e48d9d595*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 07bc6ea..a20a830 100644

//Synthetic comment -- @@ -206,6 +206,11 @@

mLteSS.setRadioTechnology(type);
mLteSS.setState(regCodeToServiceState(regState));
} else {
super.handlePollStateResultMessage(what, ar);
}
//Synthetic comment -- @@ -373,6 +378,7 @@
newSS.setStateOutOfService();
mLteSS.setStateOutOfService();

if ((hasMultiApnSupport)
&& (phone.mDataConnectionTracker instanceof CdmaDataConnectionTracker)) {
if (DBG) log("GsmDataConnectionTracker Created");
//Synthetic comment -- @@ -411,7 +417,8 @@
String eriText;
// Now the CDMAPhone sees the new ServiceState so it can get the
// new ERI text
                if (ss.getState() == ServiceState.STATE_IN_SERVICE) {
eriText = phone.getCdmaEriText();
} else if (ss.getState() == ServiceState.STATE_POWER_OFF) {
eriText = (mIccRecords != null) ? mIccRecords.getServiceProviderName() : null;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2554691..1cae0de 100755

//Synthetic comment -- @@ -96,9 +96,10 @@
NITZ_UPDATE_DIFF_DEFAULT);

private boolean mCdmaRoaming = false;
    private int mRoamingIndicator;
private boolean mIsInPrl;
    private int mDefaultRoamingIndicator;

/**
* Initially assume no data connection.
//Synthetic comment -- @@ -583,9 +584,9 @@
int cssIndicator = 0;          //[7] init with 0, because it is treated as a boolean
int systemId = 0;              //[8] systemId
int networkId = 0;             //[9] networkId
            int roamingIndicator = -1;     //[10] Roaming indicator
int systemIsInPrl = 0;         //[11] Indicates if current system is in PRL
            int defaultRoamingIndicator = 0;  //[12] Is default roaming indicator from PRL
int reasonForDenial = 0;       //[13] Denial reason if registrationState = 3

if (states.length >= 14) {
//Synthetic comment -- @@ -646,6 +647,7 @@
// list of ERIs for home system, mCdmaRoaming is true.
mCdmaRoaming =
regCodeIsRoaming(registrationState) && !isRoamIndForHomeSystem(states[10]);
newSS.setState (regCodeToServiceState(registrationState));

setCdmaTechnology(radioTechnology);
//Synthetic comment -- @@ -1187,7 +1189,7 @@
* code is registration state 0-5 from TS 27.007 7.2
* returns true if registered roam, false otherwise
*/
    private boolean
regCodeIsRoaming (int code) {
// 5 is  "in service -- roam"
return 5 == code;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6110bd1..7a03ecb 100644

//Synthetic comment -- @@ -676,6 +676,7 @@
mDataRoaming = regCodeIsRoaming(regState);
mNewRilRadioTechnology = type;
newSS.setRadioTechnology(type);
break;

case EVENT_POLL_STATE_OPERATOR:







