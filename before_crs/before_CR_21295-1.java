/*CtsNetTestCases
android.net.cts.NetworkInfo_DetailedStateTest -- testValues

the cts will fail,if state is added to DetailedState  by a vendor spec.

Change-Id:Ic3a5b0d3f2e8a38d2052c916de5372de30add59e*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/NetworkInfo_DetailedStateTest.java b/tests/tests/net/src/android/net/cts/NetworkInfo_DetailedStateTest.java
//Synthetic comment -- index 196e102..8de4963 100644

//Synthetic comment -- @@ -52,7 +52,7 @@
)
public void testValues() {
DetailedState[] expected = DetailedState.values();
        assertEquals(10, expected.length);
assertEquals(DetailedState.IDLE, expected[0]);
assertEquals(DetailedState.SCANNING, expected[1]);
assertEquals(DetailedState.CONNECTING, expected[2]);







