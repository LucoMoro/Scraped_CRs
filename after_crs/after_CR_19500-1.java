/*Fix TelephonyManagerTest Iso Checks

Even though a device may not have the telephony feature it may define
these properties if it uses a SIM.

Change-Id:I29caedc365112e76a8e47a8df9646361b62954f7*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 9aa9b0c..0528897 100644

//Synthetic comment -- @@ -452,7 +452,7 @@
+ ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
} else {
            // Non-telephony may still have the property defined if it has a SIM.
}
}

//Synthetic comment -- @@ -464,7 +464,7 @@
+ ISO_COUNTRY_CODE_PATTERN,
Pattern.matches(ISO_COUNTRY_CODE_PATTERN, countryCode));
} else {
            // Non-telephony may still have the property defined if it has a SIM.
}
}
}







