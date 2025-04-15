/*Fix LocationManagerTest#testExitProximity

- delay 2 seconds before updating location. For proximity alert,
location update in less than 1s will be neglected since JellyBean.

issue 35181
Change-Id:Iac1d902ce4185457b57d4c38b936794fd47c3d19*/




//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationManagerTest.java b/tests/tests/location/src/android/location/cts/LocationManagerTest.java
//Synthetic comment -- index 07091cc..b316e55 100755

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.test.InstrumentationTestCase;

import java.util.List;
import java.lang.Thread;

/**
* Requires the permissions
//Synthetic comment -- @@ -606,6 +607,9 @@

// now update to trigger exit proximity proximity
mIntentReceiver.clearReceivedIntents();

        // delay 2 seconds since location update in less than 1s will be neglected.
        Thread.sleep(2000);
updateLocation(20, 20);
waitForReceiveBroadcast();
assertProximityType(false);







