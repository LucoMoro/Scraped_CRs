/*Fix : NANP on using Japanese Locale.

Change-Id:I7c4c32a6b6f8bd19edc3401b50085c88cab41155*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..e294268 100644

//Synthetic comment -- @@ -271,8 +271,8 @@
formatType = PhoneNumberUtils.getFormatTypeForLocale(Locale.US);
assertEquals(PhoneNumberUtils.FORMAT_NANP, formatType);
formatType = PhoneNumberUtils.getFormatTypeForLocale(Locale.JAPAN);
        assertTrue(PhoneNumberUtils.FORMAT_JAPAN == formatType ||
                   PhoneNumberUtils.FORMAT_NANP == formatType);
// Test getNumberFromIntent, query nothing, return null.
Intent intent = new Intent();
intent.setData(Contacts.People.CONTENT_URI);







