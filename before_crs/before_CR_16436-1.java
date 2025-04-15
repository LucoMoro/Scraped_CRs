/*telephony: clean up redundant radio technology definitions

- Keep just one radio technology enumeration in Phone.java
- Telephony Manager SDK is unaffected.

Change-Id:I06159522ae2d6ba6cecaa9720843a34152986d2a*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..9eb27d3 100644

//Synthetic comment -- @@ -473,8 +473,7 @@
cdmaIconLevel = (levelDbm < levelEcio) ? levelDbm : levelEcio;

if (mServiceState != null &&
                  (mServiceState.getRadioTechnology() == ServiceState.RADIO_TECHNOLOGY_EVDO_0 ||
                   mServiceState.getRadioTechnology() == ServiceState.RADIO_TECHNOLOGY_EVDO_A)) {
int evdoEcio = signalStrength.getEvdoEcio();
int evdoSnr = signalStrength.getEvdoSnr();
int levelEvdoEcio = 0;








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..3e9b7dc 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;

import java.util.List;
import java.util.ArrayList;
//Synthetic comment -- @@ -692,30 +693,30 @@
* Returns the network type
*/
public int getNetworkType() {
        int radiotech = mPhone.getServiceState().getRadioTechnology();
switch(radiotech) {
            case ServiceState.RADIO_TECHNOLOGY_GPRS:
return TelephonyManager.NETWORK_TYPE_GPRS;
            case ServiceState.RADIO_TECHNOLOGY_EDGE:
return TelephonyManager.NETWORK_TYPE_EDGE;
            case ServiceState.RADIO_TECHNOLOGY_UMTS:
return TelephonyManager.NETWORK_TYPE_UMTS;
            case ServiceState.RADIO_TECHNOLOGY_HSDPA:
return TelephonyManager.NETWORK_TYPE_HSDPA;
            case ServiceState.RADIO_TECHNOLOGY_HSUPA:
return TelephonyManager.NETWORK_TYPE_HSUPA;
            case ServiceState.RADIO_TECHNOLOGY_HSPA:
return TelephonyManager.NETWORK_TYPE_HSPA;
            case ServiceState.RADIO_TECHNOLOGY_IS95A:
            case ServiceState.RADIO_TECHNOLOGY_IS95B:
return TelephonyManager.NETWORK_TYPE_CDMA;
            case ServiceState.RADIO_TECHNOLOGY_1xRTT:
return TelephonyManager.NETWORK_TYPE_1xRTT;
            case ServiceState.RADIO_TECHNOLOGY_EVDO_0:
return TelephonyManager.NETWORK_TYPE_EVDO_0;
            case ServiceState.RADIO_TECHNOLOGY_EVDO_A:
return TelephonyManager.NETWORK_TYPE_EVDO_A;
            case ServiceState.RADIO_TECHNOLOGY_EVDO_B:
return TelephonyManager.NETWORK_TYPE_EVDO_B;
default:
return TelephonyManager.NETWORK_TYPE_UNKNOWN;







