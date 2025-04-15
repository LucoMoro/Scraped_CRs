/*Telephony: Add PUK MMI code support for CDMA RUIM phones

Add support to unlock RUIM using PUK. PUK is entered using MMI codes.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 3548cad..f94ec4b0 100755

//Synthetic comment -- @@ -67,6 +67,7 @@
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;

import java.util.List;


//Synthetic comment -- @@ -101,6 +102,7 @@
RuimFileHandler mRuimFileHandler;
RuimRecords mRuimRecords;
RuimCard mRuimCard;
RuimPhoneBookInterfaceManager mRuimPhoneBookInterfaceManager;
RuimSmsInterfaceManager mRuimSmsInterfaceManager;
PhoneSubInfo mSubInfo;
//Synthetic comment -- @@ -223,6 +225,8 @@
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);

//Force all referenced classes to unregister their former registered events
mCT.dispose();
mDataConnection.dispose();
//Synthetic comment -- @@ -365,8 +369,7 @@

public List<? extends MmiCode>
getPendingMmiCodes() {
        Log.e(LOG_TAG, "method getPendingMmiCodes is NOT supported in CDMA!");
        return null;
}

public void registerForSuppServiceNotification(
//Synthetic comment -- @@ -383,6 +386,15 @@
return false;
}

public void
setNetworkSelectionModeAutomatic(Message response) {
Log.e(LOG_TAG, "method setNetworkSelectionModeAutomatic is NOT supported in CDMA!");
//Synthetic comment -- @@ -482,7 +494,18 @@
}

public boolean handlePinMmi(String dialString) {
        Log.e(LOG_TAG, "method handlePinMmi is NOT supported in CDMA!");
return false;
}

//Synthetic comment -- @@ -494,6 +517,22 @@
(mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
}

public void setLine1Number(String alphaTag, String number, Message onComplete) {
Log.e(LOG_TAG, "setLine1Number: not possible in CDMA");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
new file mode 100644
//Synthetic comment -- index 0000000..8dd8c2e

//Synthetic comment -- @@ -0,0 +1,296 @@







