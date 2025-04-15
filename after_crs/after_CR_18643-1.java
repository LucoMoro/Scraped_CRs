/*Fixing formatting rules test to support hyphenated formats as default.

The default phone number formatting rules may include hyphens depending
on the country and/or operator. Removing the hyphens before asserting
the output format is required for UNKNOWN default format.

Change-Id:I0f6986187b0c474a6a1197f240cd0db316e42079Signed-off-by: David Sobreira <davidps.marques@lge.com>*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java b/tests/tests/telephony/src/android/telephony/cts/PhoneNumberUtilsTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index dad3dd6..7c8a118

//Synthetic comment -- @@ -413,7 +413,7 @@
// Test formatNumber(Editable, int)
Editable editNumber = Editable.Factory.getInstance().newEditable("0377777777");
PhoneNumberUtils.formatNumber(editNumber, PhoneNumberUtils.FORMAT_UNKNOWN);
        assertEquals("0377777777", editNumber.toString().replaceAll("-", ""));
editNumber = Editable.Factory.getInstance().newEditable("+177777777");
PhoneNumberUtils.formatNumber(editNumber, PhoneNumberUtils.FORMAT_UNKNOWN);
assertEquals("+1-777-777-77", editNumber.toString());







