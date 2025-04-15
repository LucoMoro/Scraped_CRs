/*Use SERVICE_CLASS while updating call waiting status.

Query for call waiting returns status for all service classes when we
query for SERVICE_CLASS_NONE. Android supports call waiting for SERVICE_CLASS_VOICE
only. Service class in the response of RIL_REQUEST_QUERY_CALL_WAITING should be
checked for VOICE while updating call waiting status.

Change-Id:Ia52c6b3a47010321d4d4817ca2445382fa92ad69*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallWaitingCheckBoxPreference.java b/src/com/android/phone/CallWaitingCheckBoxPreference.java
//Synthetic comment -- index ed17de5..3fcb1e5 100644

//Synthetic comment -- @@ -101,7 +101,16 @@
if (tcpListener != null) tcpListener.onError(CallWaitingCheckBoxPreference.this, RESPONSE_ERROR);
} else {
if (DBG) Log.d(LOG_TAG, "handleGetCallWaitingResponse: CW state successfully queried.");
                int[] cwArray = (int[])ar.result;
                // If cwArray[0] is = 1, then cwArray[1] must follow,
                // with the TS 27.007 service class bit vector of services
                // for which call waiting is enabled.
                try {
                    setChecked(((cwArray[0] == 1) && ((cwArray[1] & 0x01) == 0x01)));
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(LOG_TAG, "handleGetCallWaitingResponse: improper result: err ="
                            + e.getMessage());
                }
}
}








