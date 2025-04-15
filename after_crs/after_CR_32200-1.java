/*revisit  Wi-Fi: Handle disconnect in middle of DHCP operation

Previous solution caused issues with turning off Wi-Fi.

Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 4539c6b..7487160 100644

//Synthetic comment -- @@ -1621,10 +1621,6 @@
private void handleNetworkDisconnect() {
if (DBG) log("Stopping DHCP and clearing IP");

/*
* stop DHCP
*/
//Synthetic comment -- @@ -3136,6 +3132,7 @@
* and handle the rest of the events there
*/
deferMessage(message);
                    handlePostDhcpSetup();
handleNetworkDisconnect();
transitionTo(mDisconnectedState);
break;







