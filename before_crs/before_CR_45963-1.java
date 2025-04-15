/*Telephony(DSDS): Xdivert enhancements.

Added registerForSimRecordsLoaded function which is
required for XDivert implementation.

Change-Id:I1612111b115f46f7b9fa318636d0597ff48e2025*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 195e6ea..1020f21 100644

//Synthetic comment -- @@ -569,6 +569,20 @@
public void unregisterForSubscriptionInfoReady(Handler h);

/**
* Returns SIM record load state. Use
* <code>getSimCard().registerForReady()</code> for change notification.
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1a95d04..e07e1a8 100755

//Synthetic comment -- @@ -178,6 +178,9 @@
protected final RegistrantList mSuppServiceFailedRegistrants
= new RegistrantList();

protected Looper mLooper; /* to insure registrants are in correct thread*/

protected final Context mContext;
//Synthetic comment -- @@ -486,6 +489,12 @@
mMmiCompleteRegistrants.remove(h);
}

/**
* Method to retrieve the saved operator id from the Shared Preferences
*/








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index b1bdf5c..bb3d3e8 100644

//Synthetic comment -- @@ -480,6 +480,14 @@
mActivePhone.unregisterForResendIncallMute(h);
}

public boolean getIccRecordsLoaded() {
return mIccCardProxy.getIccRecordsLoaded();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3a20126..12254cc 100644

//Synthetic comment -- @@ -432,6 +432,16 @@
if (mSsnRegistrants.size() == 0) mCM.setSuppServiceNotifications(false, null);
}

public void
acceptCall() throws CallStateException {
mCT.acceptCall();
//Synthetic comment -- @@ -1202,6 +1212,7 @@
setVmSimImsi(null);
}

break;

case EVENT_GET_BASEBAND_VERSION_DONE:







