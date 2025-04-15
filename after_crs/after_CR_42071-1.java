/*WifiStateMachine: ignore auth-fail event during WPS connection

Disregard auth failure events during WPS connection. The EAP sequence is
retried several times, and there might be failures (especially for wps pin).
We will get a WPS_XXX event at the end of the sequence anyway.

Without this change, the SupplicantStateTracker class will disable
the WPS network we are connecting to after 2 failed authentication events.
Then, even if WPS succeeds, we will never connect to the selected network.

Change-Id:I57c8e508bfd738405b7f2c2c2325df7838e1b4afSigned-off-by: Arik Nemtsov <arik@wizery.com>
Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index bb09704..78c4b6c 100644

//Synthetic comment -- @@ -3535,6 +3535,13 @@
if (DBG) log("Network connection lost");
handleNetworkDisconnect();
break;
                case WifiMonitor.AUTHENTICATION_FAILURE_EVENT:
                    // Disregard auth failure events during WPS connection. The
                    // EAP sequence is retried several times, and there might be
                    // failures (especially for wps pin). We will get a WPS_XXX
                    // event at the end of the sequence anyway.
                    if (DBG) log("Ignore auth failure during WPS connection");
                    break;
case WifiMonitor.SUPPLICANT_STATE_CHANGE_EVENT:
//Throw away supplicant state changes when WPS is running.
//We will start getting supplicant state changes once we get







