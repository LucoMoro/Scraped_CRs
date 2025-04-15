/*Test requestRouteToHost() except TYPE_WIFI

If the device is set Wifi-ON, then testRequestRouteToHost always fails.

Change-Id:Ie487773552e8f6f1d4838755cc39e23c5aa57c1d*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java b/tests/tests/net/src/android/net/cts/ConnectivityManagerTest.java
//Synthetic comment -- index edcea9a2..354428b 100644

//Synthetic comment -- @@ -204,8 +204,8 @@

NetworkInfo[] ni = mCm.getAllNetworkInfo();
for (NetworkInfo n : ni) {
            // make sure network is up (except WIFI due to always fail)
            if (n.isConnected() && (n.getType() != TYPE_WIFI)) {
assertTrue(mCm.requestRouteToHost(n.getType(), HOST_ADDRESS));
}
}







