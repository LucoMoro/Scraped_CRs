/*frameworks/base: Update phone with CDMA registration state response from RIL

RIL returns the registration state response with fifteen fields while
CdmaServiceStateTracker checks for fourteen fields and therefore throws
an exception causing the phone to be not updated with the current
registration state. The fix is to check for correct number of fields in the
registration response.

Change-Id:Ife9dd41885e8d4659073c785a13f810918581dfa*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 5b6bc1f..10988e2 100755

//Synthetic comment -- @@ -699,7 +699,7 @@
int defaultRoamingIndicator = 0;  //[12] Is default roaming indicator from PRL
int reasonForDenial = 0;       //[13] Denial reason if registrationState = 3

                if (states.length == 14) {
try {
if (states[0] != null) {
registrationState = Integer.parseInt(states[0]);
//Synthetic comment -- @@ -747,7 +747,7 @@
}
} else {
throw new RuntimeException("Warning! Wrong number of parameters returned from "
                                         + "RIL_REQUEST_REGISTRATION_STATE: expected 14 got "
+ states.length);
}








