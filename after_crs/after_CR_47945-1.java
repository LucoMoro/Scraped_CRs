/*Make string match without case sensitive

This test case compares the network type string retrieved from
NetworkInfo instance object. In this case, there is the difference
caused by case sensitive comparison between "WIFI" and "wifi".
Initially ConnectivityService create instance of wifi NetworkInfo
as named lower case characters. However the string is overrided
as the upper case characters by WifiStateTracker when it's connected.
Therefore, the expected string depends on its connection state.*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/NetworkInfoTest.java b/tests/tests/net/src/android/net/cts/NetworkInfoTest.java
//Synthetic comment -- index 6800c43..feacc79 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
}

private void assertNetworkInfo(NetworkInfo netInfo, String expectedTypeName) {
        assertTrue(expectedTypeName.equalsIgnoreCase(netInfo.getTypeName()));
if(netInfo.isConnectedOrConnecting()) {
assertTrue(netInfo.isAvailable());
if (State.CONNECTED == netInfo.getState()) {







