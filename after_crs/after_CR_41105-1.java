/*Telephony: Fix NullPointerException in case card was removed

If card was removed during voice call phone.getIccCard() will
return null

Change-Id:I451e1aa8a454327872c25324ff68c220ed81badf*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 9fc94a5..83e1b0e 100644

//Synthetic comment -- @@ -376,7 +376,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getIccCard() != null &&
                        phone.getIccCard().getState() != IccCardConstants.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.mRestrictedState.isCsRestricted()) {







