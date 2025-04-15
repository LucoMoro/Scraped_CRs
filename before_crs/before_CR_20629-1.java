/*Add Korean Phone number formatting feature that inserts '-' in Korean phone numbers for easier reading.

Change-Id:I82aad1e5f6a917bbe0a434e4e12b845dc2f7ef2a*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/KoreanPhoneNumberFormatter.java b/telephony/java/android/telephony/KoreanPhoneNumberFormatter.java
new file mode 100644
//Synthetic comment -- index 0000000..0a63daf

//Synthetic comment -- @@ -0,0 +1,153 @@








//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 8e4f6fc..be8d815 100644

//Synthetic comment -- @@ -1059,6 +1059,8 @@
public static final int FORMAT_NANP = 1;
/** Japanese formatting */
public static final int FORMAT_JAPAN = 2;

/** List of country codes for countries that use the NANP */
private static final String[] NANP_COUNTRIES = new String[] {
//Synthetic comment -- @@ -1149,6 +1151,9 @@
} else if (text.length() >= 3 && text.charAt(1) == '8'
&& text.charAt(2) == '1') {
formatType = FORMAT_JAPAN;
} else {
formatType = FORMAT_UNKNOWN;
}
//Synthetic comment -- @@ -1161,6 +1166,9 @@
case FORMAT_JAPAN:
formatJapaneseNumber(text);
return;
case FORMAT_UNKNOWN:
removeDashes(text);
return;
//Synthetic comment -- @@ -1303,6 +1311,28 @@
JapanesePhoneNumberFormatter.format(text);
}

/**
* Removes all dashes from the number.
*
//Synthetic comment -- @@ -1646,6 +1676,9 @@
if ("jp".compareToIgnoreCase(country) == 0) {
return FORMAT_JAPAN;
}
return FORMAT_UNKNOWN;
}








