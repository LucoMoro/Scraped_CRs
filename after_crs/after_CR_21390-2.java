/*Adding a test that Japanese IP-phone number start with 050.

Change-Id:Ibfaa1c99795c993df9b9ebac25e96f41f5e66b28*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..f118753 100644

//Synthetic comment -- @@ -398,6 +398,9 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("09077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("090-7777-7777", jpEditNumber.toString());
        jpEditNumber = Editable.Factory.getInstance().newEditable("05077777777");
        PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
        assertEquals("050-7777-7777", jpEditNumber.toString());
jpEditNumber = Editable.Factory.getInstance().newEditable("0120777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("0120-777-777", jpEditNumber.toString());
//Synthetic comment -- @@ -407,6 +410,9 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("+819077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("+81-90-7777-7777", jpEditNumber.toString());
        jpEditNumber = Editable.Factory.getInstance().newEditable("+815077777777");
        PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
        assertEquals("+81-50-7777-7777", jpEditNumber.toString());

// Test formatNumber(String). Only numbers begin with +1 or +81 can be formatted.
assertEquals("+1-888-888-888", PhoneNumberUtils.formatNumber("+1888888888"));







