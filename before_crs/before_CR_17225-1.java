/*Need enough time (1 sec) to read amounts of packet from /proc/stat_uid/[uid]/tcp_snd.

Change-Id:I93fdd1bc383e8b944a76e211166e2c5fdb9707cd*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/TrafficStatsTest.java b/tests/tests/net/src/android/net/cts/TrafficStatsTest.java
//Synthetic comment -- index ee37244..9d23a87 100644

//Synthetic comment -- @@ -170,6 +170,12 @@
server.close();
}

long mobileTxPacketsAfter = TrafficStats.getTotalTxPackets();
long mobileRxPacketsAfter = TrafficStats.getTotalRxPackets();
long mobileTxBytesAfter = TrafficStats.getTotalTxBytes();







