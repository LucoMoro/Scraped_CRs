/*Telephony: Fix npe when making a call after ejecting card

Bug: 6983013
Change-Id:Ibda258e632ecff79f22eebe093df0061ed4cdf4d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 875b680..59aa12a 100644

//Synthetic comment -- @@ -373,10 +373,11 @@
default:
GSMPhone phone = owner.phone;
int serviceState = phone.getServiceState().getState();
                AppState uiccAppState = UiccController
.getInstance()
                        .getUiccCardApplication(UiccController.APP_FAM_3GPP)
                        .getState();
if (serviceState == ServiceState.STATE_POWER_OFF) {
return DisconnectCause.POWER_OFF;
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE







