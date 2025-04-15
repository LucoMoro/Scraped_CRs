/*Need enough time (1 sec) to read amounts of packet from /proc/stat_uid/[uid]/tcp_snd.

Change-Id:Id073133d691887e9bebd54c1f717f9cb46e0326b*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/TrafficStatsTest.java b/tests/tests/net/src/android/net/cts/TrafficStatsTest.java
//Synthetic comment -- index ee37244..9d23a87 100644

//Synthetic comment -- @@ -170,6 +170,12 @@
server.close();
}

long mobileTxPacketsAfter = TrafficStats.getTotalTxPackets();
long mobileRxPacketsAfter = TrafficStats.getTotalRxPackets();
long mobileTxBytesAfter = TrafficStats.getTotalTxBytes();







