/*Removes dashes when locale and number is not NANP or Japan

Removes all dashes if locale isn't NANP or Japan and the number
don't have there country code.

Use case: If adding a number starting with 1nnnnnn and then
          trying to add a country code before (ex +46) we will first
          trigger NANP formatting with +1-nnn-nnn so when we get
          +41-nnn-nnn we will still have the old NANP formatting.
          This number should be shown as +461nnnnnn.

Change-Id:I5cab830350d785a58367eba79e268d9e8ee16aac*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 3f08676..454231f 100644

//Synthetic comment -- @@ -1058,7 +1058,7 @@
&& text.charAt(2) == '1') {
formatType = FORMAT_JAPAN;
} else {
                return;
}
}

//Synthetic comment -- @@ -1069,6 +1069,9 @@
case FORMAT_JAPAN:
formatJapaneseNumber(text);
return;
}
}

//Synthetic comment -- @@ -1104,14 +1107,7 @@
CharSequence saved = text.subSequence(0, length);

// Strip the dashes first, as we're going to add them back
        int p = 0;
        while (p < text.length()) {
            if (text.charAt(p) == '-') {
                text.delete(p, p + 1);
            } else {
                p++;
            }
        }
length = text.length();

// When scanning the number we record where dashes need to be added,
//Synthetic comment -- @@ -1215,6 +1211,22 @@
JapanesePhoneNumberFormatter.format(text);
}

// Three and four digit phone numbers for either special services,
// or 3-6 digit addresses from the network (eg carrier-originated SMS messages) should
// not match.







