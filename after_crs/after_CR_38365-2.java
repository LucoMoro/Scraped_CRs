/*Fix CTS case testStartUsingNetworkFeature of ConnectivityManagerTest

Check if type of network is supported on the devices.
The device supporting TYPE_WIFI returns failureCode(-1).
But, not supported device returns wifiOnlyStartFailureCode or
wifiOnlyStopFailureCode.

Change-Id:I070bb19f6740367c476e78b884c2a36d111ff3ee*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index cd80be2..4704582 100644

//Synthetic comment -- @@ -161,9 +161,14 @@
invalidateFeature));
}

        ni = mCm.getNetworkInfo(TYPE_WIFI);
        if (ni != null) {
            // Should return failure(-1) because MMS is not supported on WIFI.
            assertEquals(failureCode, mCm.startUsingNetworkFeature(TYPE_WIFI,
                    mmsFeature));
            assertEquals(failureCode, mCm.stopUsingNetworkFeature(TYPE_WIFI,
                    mmsFeature));
        }
}

@TestTargetNew(







