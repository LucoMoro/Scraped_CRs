/*Avoid system_server crashing due to mac address is null

It's possible that the mac address is null (getMacAddress() of WifiNative.java).
In this case system_server will crash like:

E AndroidRuntime: *** FATAL EXCEPTION IN SYSTEM PROCESS: WifiWatchdogStateMachine
E AndroidRuntime: java.lang.NullPointerException
E AndroidRuntime:        at android.net.arp.ArpPeer.<init>(ArpPeer.java:57)
E AndroidRuntime:        at android.net.wifi.WifiWatchdogStateMachine.doArpTest(WifiWatchdogStateMachine.java:866)

Check the mac address before parsing to avoid crashing.

Change-Id:I5d4205c04d479a3a2837172c6382816ea4bf74d6*/




//Synthetic comment -- diff --git a/core/java/android/net/arp/ArpPeer.java b/core/java/android/net/arp/ArpPeer.java
//Synthetic comment -- index 8e666bc..6ba1e7c 100644

//Synthetic comment -- @@ -53,9 +53,11 @@
mInterfaceName = interfaceName;
mMyAddr = myAddr;

        if (mac != null) {
            for (int i = 0; i < MAC_ADDR_LENGTH; i++) {
                mMyMac[i] = (byte) Integer.parseInt(mac.substring(
                            i*3, (i*3) + 2), 16);
            }
}

if (myAddr instanceof Inet6Address || peer instanceof Inet6Address) {







