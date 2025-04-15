/*Telephony: Remove references to mExitECMRunnable object

When CDMA phone is destroyed, mExitECMRunnable object is not being
destroyed. So the CDMA phone instance is still remaining. This leads
to phone crash as it tries to access CDMA phone object even though
CDMA phone is not active. Fix is to remove the callbacks of
mExitECMRunnable.

Change-Id:I10fa7e59c2cfe83d24cb7af0dbbd7504310d5ee2*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 27eae22..efde2c9 100755

//Synthetic comment -- @@ -256,6 +256,8 @@
this.mSST = null;
this.mEriManager = null;
this.mCcatService = null;
            removeCallbacks(mExitEcmRunnable);
            this.mExitEcmRunnable = null;
}

protected void finalize() {







