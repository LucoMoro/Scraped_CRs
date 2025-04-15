/*Update NO_DELIVERY_REPORTS list.

Add LGT which does not support SMS delivery report

Change-Id:I4928250027e5838fac932a5baa11ad3ee9bfdffcSigned-off-by: papiya <pnath@sta.samsung.com>*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 57ce365..8483fbb 100755

//Synthetic comment -- @@ -62,7 +62,8 @@
"44010",    // NTT DOCOMO
"45005",    // SKT Mobility
"45002",    // SKT Mobility
                    "45008"     // KT Mobility
);

// List of network operators that doesn't support Data(binary) SMS message







