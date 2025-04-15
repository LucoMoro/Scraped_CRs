/*Phone: Release all calls on pressing end key.

End all call sessions (except waiting call) when
end key (hard key) is pressed as per 3GPP TS 22.030,
6.5.5.1.

Change-Id:Ie0290a888af8d14fd3d262d2f97e5c71e023e3f1*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..4449881 100644

//Synthetic comment -- @@ -142,8 +142,8 @@
// ending the complete call session
hungUp = PhoneUtils.hangupRingingAndActive(mPhone);
} else if (phoneType == Phone.PHONE_TYPE_GSM) {
                        // GSM: End the call as per the Phone state
                        hungUp = PhoneUtils.hangup(mPhone);
} else {
throw new IllegalStateException("Unexpected phone type: " + phoneType);
}








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index aab89ff..c30dea4 100755

//Synthetic comment -- @@ -406,6 +406,17 @@
return hungUpRingingCall || hungUpFgCall;
}

/**
* Trivial wrapper around Call.hangup(), except that we return a
* boolean success code rather than throwing CallStateException on







