/*WifiWatchdog: handle exception from ArpPeer

Sometimes IllegalArgumantException can happen in constructor of ArpPeer
class when IPv6 address is provided. This causes crash of systemserver.

Fixing it by adding appropriate exception handler

Change-Id:I4bb7bbca790745a1bf56ece96dd91b84b1f70cadSigned-off-by: Andrii Beregovenko <a.beregovenko@ti.com>
Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiWatchdogStateMachine.java b/wifi/java/android/net/wifi/WifiWatchdogStateMachine.java
//Synthetic comment -- index 1a42f93..c6d3eae 100644

//Synthetic comment -- @@ -881,6 +881,9 @@
//test to avoid any wifi connectivity issues
loge("ARP test initiation failure: " + se);
success = true;
        } catch (IllegalArgumentException ae) {
            log("ARP test initiation failure: " + ae);
            success = true;
}

return success;







