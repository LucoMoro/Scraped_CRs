/*Need enough time (1 sec) to read amounts of packet from /proc/stat_uid/[uid]/tcp_snd.

Change-Id:I597a558ea4d78dec070804c096d77643d840fe45*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/TrafficStatsTest.java b/tests/tests/net/src/android/net/cts/TrafficStatsTest.java
//Synthetic comment -- index ee37244..9d23a87 100644

//Synthetic comment -- @@ -170,6 +170,12 @@
server.close();
}

long mobileTxPacketsAfter = TrafficStats.getTotalTxPackets();
long mobileRxPacketsAfter = TrafficStats.getTotalRxPackets();
long mobileTxBytesAfter = TrafficStats.getTotalTxBytes();







