/*telephony: clean up redundant radio technology definitions

- Keep just one radio technology enumeration in Phone.java
- Telephony Manager SDK is unaffected.

Change-Id:I06159522ae2d6ba6cecaa9720843a34152986d2a*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..9eb27d3 100644

//Synthetic comment -- @@ -473,8 +473,7 @@
cdmaIconLevel = (levelDbm < levelEcio) ? levelDbm : levelEcio;

if (mServiceState != null &&
                  (mServiceState.getRadioTechnology().isEvdo())) {
int evdoEcio = signalStrength.getEvdoEcio();
int evdoSnr = signalStrength.getEvdoSnr();
int levelEvdoEcio = 0;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..3e9b7dc 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone.RadioTechnology;

import java.util.List;
import java.util.ArrayList;
//Synthetic comment -- @@ -692,30 +693,30 @@
* Returns the network type
*/
public int getNetworkType() {
        Phone.RadioTechnology radiotech = mPhone.getServiceState().getRadioTechnology();
switch(radiotech) {
            case RADIO_TECH_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
            case RADIO_TECH_EDGE:
return TelephonyManager.NETWORK_TYPE_EDGE;
            case RADIO_TECH_UMTS:
return TelephonyManager.NETWORK_TYPE_UMTS;
            case RADIO_TECH_HSDPA:
return TelephonyManager.NETWORK_TYPE_HSDPA;
            case RADIO_TECH_HSUPA:
return TelephonyManager.NETWORK_TYPE_HSUPA;
            case RADIO_TECH_HSPA:
return TelephonyManager.NETWORK_TYPE_HSPA;
            case RADIO_TECH_IS95A:
            case RADIO_TECH_IS95B:
return TelephonyManager.NETWORK_TYPE_CDMA;
            case RADIO_TECH_1xRTT:
return TelephonyManager.NETWORK_TYPE_1xRTT;
            case RADIO_TECH_EVDO_0:
return TelephonyManager.NETWORK_TYPE_EVDO_0;
            case RADIO_TECH_EVDO_A:
return TelephonyManager.NETWORK_TYPE_EVDO_A;
            case RADIO_TECH_EVDO_B:
return TelephonyManager.NETWORK_TYPE_EVDO_B;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;







