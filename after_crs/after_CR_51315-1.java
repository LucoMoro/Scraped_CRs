/*Telephony: Fix NPE in CdmaConnection

When card is removed and call gets terminated
getUiccCardApplication will return null

Change-Id:Ic861e5b5ee8515de431c33152fd11d2c45a2a353*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 354806e..29ed9cb 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;

import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;

//Synthetic comment -- @@ -415,10 +416,10 @@
default:
CDMAPhone phone = owner.phone;
int serviceState = phone.getServiceState().getState();
                UiccCardApplication app = UiccController
.getInstance()
                        .getUiccCardApplication(UiccController.APP_FAM_3GPP2);
                AppState uiccAppState = (app != null) ? app.getState() : AppState.APPSTATE_UNKNOWN;
if (serviceState == ServiceState.STATE_POWER_OFF) {
return DisconnectCause.POWER_OFF;
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE







