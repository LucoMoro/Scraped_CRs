/*Telephony: Remove references to mExitECMRunnable object.

Free mExitECMRunnable object while destroying CDMA phone.

Change-Id:I411b3e5e41a9cc76c3571a98f13f91d84523f623*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 27eae22..7aecf5b 100755

//Synthetic comment -- @@ -224,6 +224,7 @@
mCM.unregisterForNVReady(this); //EVENT_NV_READY
mSST.unregisterForNetworkAttach(this); //EVENT_REGISTERED_TO_NETWORK
mCM.unSetOnSuppServiceNotification(this);
            removeCallbacks(mExitEcmRunnable);

mPendingMmis.clear();

//Synthetic comment -- @@ -256,6 +257,7 @@
this.mSST = null;
this.mEriManager = null;
this.mCcatService = null;
            this.mExitEcmRunnable = null;
}

protected void finalize() {







