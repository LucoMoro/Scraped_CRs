/*Telephony: Always create IccCard

There is some bug in master branch which is not in AOSP code
that prevents KeyGuard from showing up unless IccCard broadcasts
its status

Force creation of IccCard (even if it really is absent) so that
it broadcasts its state and KeyGuard gets displayed

Fix NullPointerException in case card was removed by checking
return value of phone.getIccCard()

bug: 6983013
Change-Id:I95de1cc8a70a9e3d66d3e5d6059e82626057c5d4*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index b093ef7..648b73e 100644

//Synthetic comment -- @@ -136,7 +136,6 @@

public void dispose() {
if (mDbg) log("Disposing card type " + (is3gpp ? "3gpp" : "3gpp2"));
        mPhone.mCM.unregisterForIccStatusChanged(mHandler);
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
mPhone.mCM.unregisterForOn(mHandler);
mCatService.dispose();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 9fc94a5..83e1b0e 100644

//Synthetic comment -- @@ -376,7 +376,8 @@
} else if (serviceState == ServiceState.STATE_OUT_OF_SERVICE
|| serviceState == ServiceState.STATE_EMERGENCY_ONLY ) {
return DisconnectCause.OUT_OF_SERVICE;
                } else if (phone.getIccCard().getState() != IccCardConstants.State.READY) {
return DisconnectCause.ICC_ERROR;
} else if (causeCode == CallFailCause.ERROR_UNSPECIFIED) {
if (phone.mSST.mRestrictedState.isCsRestricted()) {








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index ab7d561..211dad6 100644

//Synthetic comment -- @@ -112,18 +112,12 @@
IccCardStatus status = (IccCardStatus)ar.result;

//Update already existing card
        if (mIccCard != null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
mIccCard.update(mCurrentPhone, status);
}

        //Dispose of removed card
        if (mIccCard != null && status.getCardState() != CardState.CARDSTATE_PRESENT) {
            mIccCard.dispose();
            mIccCard = null;
        }

//Create new card
        if (mIccCard == null && status.getCardState() == CardState.CARDSTATE_PRESENT) {
mIccCard = new IccCard(mCurrentPhone, status, mCurrentPhone.getPhoneName(), true);
}








