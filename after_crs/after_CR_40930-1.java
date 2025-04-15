/*Telephony: Always create IccCard

There is some bug in master branch which is not in AOSP code
that prevents KeyGuard from showing up unless IccCard broadcasts
its status

Force creation of IccCard (even if it really is absent) so that
it broadcasts its state and KeyGuard gets displayed

Change-Id:I95de1cc8a70a9e3d66d3e5d6059e82626057c5d4*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index b093ef7..648b73e 100644

//Synthetic comment -- @@ -136,7 +136,6 @@

public void dispose() {
if (mDbg) log("Disposing card type " + (is3gpp ? "3gpp" : "3gpp2"));
mPhone.mCM.unregisterForOffOrNotAvailable(mHandler);
mPhone.mCM.unregisterForOn(mHandler);
mCatService.dispose();








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccController.java b/src/java/com/android/internal/telephony/uicc/UiccController.java
//Synthetic comment -- index ab7d561..211dad6 100644

//Synthetic comment -- @@ -112,18 +112,12 @@
IccCardStatus status = (IccCardStatus)ar.result;

//Update already existing card
        if (mIccCard != null) {
mIccCard.update(mCurrentPhone, status);
}

//Create new card
        if (mIccCard == null) {
mIccCard = new IccCard(mCurrentPhone, status, mCurrentPhone.getPhoneName(), true);
}








