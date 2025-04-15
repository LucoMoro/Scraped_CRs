/*Adds the ability to Android to initiate a 2 Digit Call.

Over here the phone number to the switch-board is "99". Trying
to call "99" on an Android device dont work as expected (it
works on other devices though).

Exception (1) to Call initiation is:
  If the user of the device is already in a call and enters a
  Short String without any #-key at the end and the length of
  the Short String is equal or less than 2 the phone shall
  initiate a USSD/SS commands.

Exception (2) to Call initiation is:
  If the user of the device enters one digit followed by the
  #-key. This rule defines this String as the "#-String" which
  is a USSD/SS command. The phone shall initiate a USSD/SS
  commands.

Change-Id:I950ac10b347b8e9d113d208b08c3e2c7cdb50221*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index aa16fa3..95a97d0 100644

//Synthetic comment -- @@ -44,6 +44,13 @@

//***** Constants

    // ***** Max Size of the Short Code (aka Short String from TS 22.030 6.5.2)
    static final int MAX_LENGTH_SHORT_CODE = 2;

    // ***** TS 22.030 6.5.2 Every Short String USSD command will end with #-key
    // (known as #-String)
    static final char END_OF_USSD_COMMAND = '#';

// From TS 22.030 6.5.2
static final String ACTION_ACTIVATE = "*";
static final String ACTION_DEACTIVATE = "#";
//Synthetic comment -- @@ -446,22 +453,63 @@
}

/**
     * Helper function for newFromDialString. Returns true if dialString appears
     * to be a short code AND conditions are correct for it to be treated as
     * such.
*/
static private boolean isShortCode(String dialString, GSMPhone phone) {
// Refer to TS 22.030 Figure 3.5.3.2:
        if (dialString == null) {
            return false;
        }

        if (PhoneNumberUtils.isEmergencyNumber(dialString)) {
            return false;
        } else {
            return isShortCodeUSSD(dialString, phone);
        }
    }

    /**
     * Helper function for isShortCode. Returns true if dialString appears to be
     * a short code and it is a USSD structure
     *
     * According to the 3PGG TS 22.030 specification Figure 3.5.3.2: A 1 or 2
     * digit "short code" is treated as USSD if it is entered while on a call or
     * does not satisfy the condition (exactly 2 digits && starts with '1').
     *
     * However, since this is not mandatory a Short String will initiate a call.
     * Exception to the rule above (see below)
     *
     * Exception (1) to Call initiation is: If the user of the device is already in a call
     * and enters a Short String without any #-key at the end and the length of the Short String is
     * equal or less then the MAX_LENGTH_SHORT_CODE [constant that is equal to 2]
     *
     * The phone shall initiate a USSD/SS commands.
     *
     * Exception (2) to Call initiation is: If the user of the device enters one
     * Digit followed by the #-key. This rule defines this String as the
     * #-String which is a USSD/SS command.
     *
     * The phone shall initiate a USSD/SS command.
     */
    static private boolean isShortCodeUSSD(String dialString, GSMPhone phone) {
        if (dialString != null) {
            if (phone.isInCall()) {
                // The maximum length of a Short Code (aka Short String) is 2
                if (dialString.length() <= MAX_LENGTH_SHORT_CODE) {
                    return true;
                }
            }

            // The maximum length of a Short Code (aka Short String) is 2
            if (dialString.length() <= MAX_LENGTH_SHORT_CODE) {
                if (dialString.charAt(dialString.length() - 1) == END_OF_USSD_COMMAND) {
                    return true;
                }
            }
        }
        return false;
}

/**







