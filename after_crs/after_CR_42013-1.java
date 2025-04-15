/*telephony: Fix issue in short code ussd detection

According to the 3PGG TS 22.030 specification
Figure 3.5.3.2: A 1 or 2 digit "short code" is
treated as USSD if it is entered while on a call or
does not satisfy the condition (exactly 2 digits
&& starts with '1').

Following rule is already addressed in function
GsmMmiCode::newFromDialString.

If the user of the device enters one digit followed
by the #-key, phone shall initiate a USSD/SS command

Change-Id:I70795da1fb5144d1c91059f6200b74b5fd33de22Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Author-tracking-BZ: 28800*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 1f7836e..947ed7b 100644

//Synthetic comment -- @@ -517,27 +517,17 @@
* equal or less then the MAX_LENGTH_SHORT_CODE [constant that is equal to 2]
*
* The phone shall initiate a USSD/SS commands.
*/
static private boolean isShortCodeUSSD(String dialString, GSMPhone phone) {
        if (dialString != null && dialString.length() <= MAX_LENGTH_SHORT_CODE) {
if (phone.isInCall()) {
                return true;
}

// The maximum length of a Short Code (aka Short String) is 2
            if (!(dialString.length() == MAX_LENGTH_SHORT_CODE &&
                    dialString.charAt(0) == '1')) {
                return true;
}
}
return false;







