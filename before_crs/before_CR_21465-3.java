/*Adding tests that Japanese phone number start with 050 and 060.

Change-Id:Iefb02c5a02eba583b62aed3e6e24aecdf7310406*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index 57e610b..a535116 100644

//Synthetic comment -- @@ -320,6 +320,12 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("09077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("090-7777-7777", jpEditNumber.toString());
jpEditNumber = Editable.Factory.getInstance().newEditable("0120777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("0120-777-777", jpEditNumber.toString());
//Synthetic comment -- @@ -329,6 +335,12 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("+819077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("+81-90-7777-7777", jpEditNumber.toString());

// Test formatNumber(String). Only numbers begin with +1 or +81 can be formatted.
assertEquals("+1-888-888-888", PhoneNumberUtils.formatNumber("+1888888888"));







