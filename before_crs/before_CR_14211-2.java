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
//Synthetic comment -- index 1aa1c76..e5dab12 100644

//Synthetic comment -- @@ -203,6 +203,11 @@
}
}

return ret.toString();
}

//Synthetic comment -- @@ -268,6 +273,30 @@
}
}

/**
* Extracts the post-dial sequence of DTMF control digits, pauses, and
* waits. Strips separators. This string may be empty, but will not be null







