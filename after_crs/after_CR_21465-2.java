/*Adding a test that Japanese FMC-phone number start with 060.

[FMC: Fixed Mobile Convergence]

Change-Id:Iefb02c5a02eba583b62aed3e6e24aecdf7310406*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
//Synthetic comment -- index dad3dd6..b22bd4f 100644

//Synthetic comment -- @@ -398,6 +398,9 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("09077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("090-7777-7777", jpEditNumber.toString());
        jpEditNumber = Editable.Factory.getInstance().newEditable("06077777777");
        PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
        assertEquals("060-7777-7777", jpEditNumber.toString());
jpEditNumber = Editable.Factory.getInstance().newEditable("0120777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("0120-777-777", jpEditNumber.toString());
//Synthetic comment -- @@ -407,6 +410,9 @@
jpEditNumber = Editable.Factory.getInstance().newEditable("+819077777777");
PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
assertEquals("+81-90-7777-7777", jpEditNumber.toString());
        jpEditNumber = Editable.Factory.getInstance().newEditable("+816077777777");
        PhoneNumberUtils.formatJapaneseNumber(jpEditNumber);
        assertEquals("+81-60-7777-7777", jpEditNumber.toString());

// Test formatNumber(String). Only numbers begin with +1 or +81 can be formatted.
assertEquals("+1-888-888-888", PhoneNumberUtils.formatNumber("+1888888888"));







