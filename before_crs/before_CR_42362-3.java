/*Telephony: Specify App Id on facility lock operations

This is needed when using cards that could have multiple
applications(USIM, CSIM), to be able to uniquely identify the
application.

Also, log parameters for facility lock operations.

Change-Id:Ib834928ffd76a0f0e4fa0552cc27a09ccb4cd987*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 99294ed..6131792 100755

//Synthetic comment -- @@ -1659,7 +1659,9 @@
Message response) {
RILRequest rr = RILRequest.obtain(RIL_REQUEST_QUERY_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

// count strings
rr.mp.writeInt(4);
//Synthetic comment -- @@ -1688,7 +1690,9 @@
RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SET_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

// count strings
rr.mp.writeInt(5);







