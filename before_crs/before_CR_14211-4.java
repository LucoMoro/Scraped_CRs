/*Makes PhoneNumberUtils support international numbers after a CLIR command.

Makes PhoneNumberUtils.java support numbers in international
format (starting with ‘+’ character) after a CLIR command.

Previously a plus character would always be removed unless it
occupied the first position of the number string. In this case,
when the number is preceded by #31# (CLIR), the plus character
will be removed as well.

This is an error, prohibiting a type approval of the phone.

This change will detect the plus character after the CLIR command
and will insert it at the right position.

Change-Id:Ib220aee7b3eda30cde960db8c7470523dc5fd313*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 454231f..5878713 100644

//Synthetic comment -- @@ -55,6 +55,12 @@
public static final char WILD = 'N';

/*
* TOA = TON + NPI
* See TS 24.008 section 10.5.4.7 for details.
* These are the only really useful TOA values
//Synthetic comment -- @@ -179,8 +185,6 @@
*  Please note that the GSM wild character is allowed in the result.
*  This must be resolved before dialing.
*
     *  Allows + only in the first  position in the result string.
     *
*  Returns null if phoneNumber == null
*/
public static String
//Synthetic comment -- @@ -203,6 +207,11 @@
}
}

return ret.toString();
}

//Synthetic comment -- @@ -268,6 +277,28 @@
}
}

/**
* Extracts the post-dial sequence of DTMF control digits, pauses, and
* waits. Strips separators. This string may be empty, but will not be null







