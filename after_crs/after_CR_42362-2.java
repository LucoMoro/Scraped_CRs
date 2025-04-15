/*Telephony: Specify App Id on facility lock operations

This is needed when using cards that could have multiple
applications(USIM, CSIM), to be able to uniquely identify the
application.

Also, log parameters for facility lock operations.

Change-Id:Ib834928ffd76a0f0e4fa0552cc27a09ccb4cd987*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 648b73e..344c866 100644

//Synthetic comment -- @@ -371,8 +371,8 @@

mDesiredPinLocked = enabled;

         mPhone.mCM.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_SIM,
                 enabled, password, serviceClassX, getAid(),
mHandler.obtainMessage(EVENT_CHANGE_FACILITY_LOCK_DONE, onComplete));
}

//Synthetic comment -- @@ -397,8 +397,8 @@

mDesiredFdnEnabled = enabled;

         mPhone.mCM.setFacilityLockForApp(CommandsInterface.CB_FACILITY_BA_FD,
                 enabled, password, serviceClassX, getAid(),
mHandler.obtainMessage(EVENT_CHANGE_FACILITY_FDN_DONE, onComplete));
}

//Synthetic comment -- @@ -674,17 +674,17 @@
break;
case EVENT_ICC_READY:
if(isSubscriptionFromIccCard) {
                        mPhone.mCM.queryFacilityLockForApp (
                                CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX, getAid(),
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
                        mPhone.mCM.queryFacilityLockForApp (
                                CommandsInterface.CB_FACILITY_BA_FD, "", serviceClassX, getAid(),
obtainMessage(EVENT_QUERY_FACILITY_FDN_DONE));
}
break;
case EVENT_ICC_LOCKED:
                    mPhone.mCM.queryFacilityLockForApp (
                             CommandsInterface.CB_FACILITY_BA_SIM, "", serviceClassX, getAid(),
obtainMessage(EVENT_QUERY_FACILITY_LOCK_DONE));
break;
case EVENT_GET_ICC_STATUS_DONE:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c359652..485d064 100755

//Synthetic comment -- @@ -1658,7 +1658,9 @@
Message response) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_QUERY_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                                                 + " [" + facility + " " + serviceClass
                                                 + " " + appId + "]");

// count strings
rr.mp.writeInt(4);
//Synthetic comment -- @@ -1687,7 +1689,9 @@
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SET_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                                                        + " [" + facility + " " + lockState
                                                        + " " + serviceClass + " " + appId + "]");

// count strings
rr.mp.writeInt(5);







