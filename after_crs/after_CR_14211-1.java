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
//Synthetic comment -- index 1aa1c76..641af22 100644

//Synthetic comment -- @@ -203,6 +203,11 @@
}
}

        int pos = addPlusChar(phoneNumber);
        if (pos >= 0 && ret.length() > pos) {
            ret.insert(pos, '+');
        }

return ret.toString();
}

//Synthetic comment -- @@ -268,6 +273,30 @@
}
}

    /** GSM codes
     *  Finds if a GSM code includes the international prefix (+).
     *
     * @param number the number to dial.
     *
     * @return the position where the + char will be inserted, -1 if the GSM code was not found.
     */
    static private int
    addPlusChar(String number) {
        String CLIR_OFF = "#31#+";
        String CLIR_ON = "*31#+";
        int pos = -1;

        if (number.startsWith(CLIR_OFF)) {
            pos = CLIR_OFF.length() - 1;
        }

        if (number.startsWith(CLIR_ON)) {
            pos = CLIR_ON.length() - 1;
        }

        return pos;
    }

/**
* Extracts the post-dial sequence of DTMF control digits, pauses, and
* waits. Strips separators. This string may be empty, but will not be null







