/*libphonenumber: Fix inability to dial 0001

When entering "0001" in to the dialer, AsYouTypeFormatter will
errornously reformat it as "001". Fix lifted from r348 of
libphonenumber.

Change-Id:I5f4d64694d9f9bced135cfd9a3e483fbe1ed1612*/




//Synthetic comment -- diff --git a/java/src/com/android/i18n/phonenumbers/PhoneNumberUtil.java b/java/src/com/android/i18n/phonenumbers/PhoneNumberUtil.java
//Synthetic comment -- index 16a975e..d65de33 100644

//Synthetic comment -- @@ -2027,6 +2027,10 @@
// 0 if fullNumber doesn't start with a valid country calling code, and leaves nationalNumber
// unmodified.
int extractCountryCode(StringBuilder fullNumber, StringBuilder nationalNumber) {
    if ((fullNumber.length() == 0) || (fullNumber.charAt(0) == '0')) {
      // Country codes do not begin with a '0'.
      return 0;
    }
int potentialCountryCode;
int numberLength = fullNumber.length();
for (int i = 1; i <= MAX_LENGTH_COUNTRY_CODE && i <= numberLength; i++) {







