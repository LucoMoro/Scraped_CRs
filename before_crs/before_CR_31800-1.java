/*Wi-Fi: Handle disconnect in middle of DHCP operation

Restore power mode and BT co-ex settings when disconnect
happens in middle of DHCP operation.

Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 82abe3a..4539c6b 100644

//Synthetic comment -- @@ -1621,6 +1621,10 @@
private void handleNetworkDisconnect() {
if (DBG) log("Stopping DHCP and clearing IP");

/*
* stop DHCP
*/







