/*Validate Telephony Manager Country Codes

Bug 3065551

Make sure getNetworkCountryIso and getSimCountryIso return
ISO country codes and not MCC country codes. Also prohibit
the use of 3-letter ISO codes...

Change-Id:I060383c74f2c4ce2cf702fb91ab319f5bb8dbc33*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/TelephonyManagerTest.java
//Synthetic comment -- index 1ced4cd..6c13007 100644

//Synthetic comment -- @@ -440,4 +440,18 @@
}
}
}
}







