/*Use SERVICE_CLASS while updating call waiting status.

Query for call waiting returns status for all service classes when we
query for SERVICE_CLASS_NONE. Android supports call waiting for SERVICE_CLASS_VOICE
only. Service class in the response of RIL_REQUEST_QUERY_CALL_WAITING should be
checked for VOICE while updating call waiting status.

Change-Id:Ia52c6b3a47010321d4d4817ca2445382fa92ad69*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallWaitingCheckBoxPreference.java b/src/com/android/phone/CallWaitingCheckBoxPreference.java
//Synthetic comment -- index ed17de5..b98b192 100644

//Synthetic comment -- @@ -101,7 +101,11 @@
if (tcpListener != null) tcpListener.onError(CallWaitingCheckBoxPreference.this, RESPONSE_ERROR);
} else {
if (DBG) Log.d(LOG_TAG, "handleGetCallWaitingResponse: CW state successfully queried.");
                setChecked(((int[]) ar.result)[0] == 1);
}
}








