/*Nuke TrafficStatsTest#testTrafficStatsWithHost...

Bug 3189208

Removing this test due to potential problems with GB. This will need to
be brought back and investigated.

Change-Id:Ifeafdf44464c652b063edfb51c5f7931624b6973*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/TrafficStatsTest.java b/tests/tests/net/src/android/net/cts/TrafficStatsTest.java
//Synthetic comment -- index 9d23a87..183f891 100644

//Synthetic comment -- @@ -16,24 +16,20 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.net.TrafficStats;
import android.os.Process;
import android.test.AndroidTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@TestTargetClass(TrafficStats.class)
public class TrafficStatsTest extends AndroidTestCase {
//Synthetic comment -- @@ -58,63 +54,6 @@
}

@TestTargets({
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileTxPackets"),
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileRxPackets"),
@TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, method = "getMobileTxBytes"),







