/*Prevent scanning during DHCP process

Wi-Fi should be in active state during the entire DHCP process, and
shouldn't go to IEEE 802.11 power save mode. If the framework requests
scan during the DHCP process, the Wi-Fi chip has to start scanning
on channels different from the current one, and going to power save
mode is a prerequisite for scan. The result directly impacts user
experience: DHCP process takes longer, and even can fail.

Change-Id:I8171388bb70072e4c42cb3c074dd955da84e494b*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index dafa8e8..99bc748 100644

//Synthetic comment -- @@ -3412,6 +3412,10 @@
case CMD_SET_HIGH_PERF_MODE:
deferMessage(message);
break;
                  /* Defer scan request since we should not switch to other channels at DHCP */
              case CMD_START_SCAN:
                  deferMessage(message);
                  break;
default:
return NOT_HANDLED;
}







