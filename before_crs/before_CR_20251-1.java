/*Nuke TrafficStatsTest#testTrafficStatsWithHost...

Bug 3189208

Removing this test due to potential problems with GB. This will need to
be brought back and investigated.

Change-Id:Ifeafdf44464c652b063edfb51c5f7931624b6973*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/TrafficStatsTest.java b/tests/tests/net/src/android/net/cts/TrafficStatsTest.java
//Synthetic comment -- index 9d23a87..183f891 100644

//Synthetic comment -- @@ -16,24 +16,20 @@

package android.net.cts;

import android.os.Process;
import android.net.TrafficStats;
import android.test.AndroidTestCase;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

@TestTargetClass(TrafficStats.class)
public class TrafficStatsTest extends AndroidTestCase {
//Synthetic comment -- @@ -58,63 +54,6 @@
}

@TestTargets({
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getTotalTxPackets"),
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getTotalRxPackets"),
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getTotalTxBytes"),
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getTotalRxBytes"),
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getUidTxBytes"),
        @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getUidRxBytes")
    })
    public void testTrafficStatsWithHostLookup() {
        long txPacketsBefore = TrafficStats.getTotalTxPackets();
        long rxPacketsBefore = TrafficStats.getTotalRxPackets();
        long txBytesBefore = TrafficStats.getTotalTxBytes();
        long rxBytesBefore = TrafficStats.getTotalRxBytes();
        long uidTxBytesBefore = TrafficStats.getUidTxBytes(Process.myUid());
        long uidRxBytesBefore = TrafficStats.getUidRxBytes(Process.myUid());

        // Look up random hostnames in a wildcard domain owned by Google.
        // This will require a DNS request, which should generate traffic.

        int found = 0;
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            try {
                String host = "test" + r.nextInt(100000) + ".clients.google.com";
                InetAddress[] addr = InetAddress.getAllByName(host);
                if (addr.length > 0) found++;
            } catch (UnknownHostException e) {
                // Ignore -- our purpose is not to test network connectivity,
                // and we'd rather have false positives than a flaky test.
            }
        }

        long txPacketsAfter = TrafficStats.getTotalTxPackets();
        long rxPacketsAfter = TrafficStats.getTotalRxPackets();
        long txBytesAfter = TrafficStats.getTotalTxBytes();
        long rxBytesAfter = TrafficStats.getTotalRxBytes();
        long uidTxBytesAfter = TrafficStats.getUidTxBytes(Process.myUid());
        long uidRxBytesAfter = TrafficStats.getUidRxBytes(Process.myUid());

        // Make some conservative assertions about the data used:
        // each successful resolution should exchange at least one packet,
        // and at least 20 bytes in each direction.

        assertTrue("txp: " + txPacketsBefore + " [" + found + "] " + txPacketsAfter,
                   txPacketsAfter >= txPacketsBefore + found);
        assertTrue("rxp: " + rxPacketsBefore + " [" + found + "] " + rxPacketsAfter,
                   rxPacketsAfter >= rxPacketsBefore + found);
        assertTrue("txb: " + txBytesBefore + " [" + found + "] " + txBytesAfter,
                   txBytesAfter >= txBytesBefore + found * 20);
        assertTrue("rxb: " + rxBytesBefore + " [" + found + "] " + rxBytesAfter,
                   rxBytesAfter >= rxBytesBefore + found * 20);
        assertTrue("uidtxb: " + uidTxBytesBefore + " [" + found + "] " + uidTxBytesAfter,
                   uidTxBytesAfter >= uidTxBytesBefore + found * 20);
        assertTrue("uidrxb: " + uidRxBytesBefore + " [" + found + "] " + uidRxBytesAfter,
                   uidRxBytesAfter >= uidRxBytesBefore + found * 20);
    }

    @TestTargets({
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileTxPackets"),
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileRxPackets"),
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileTxBytes"),







