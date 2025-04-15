/*Fix ThreadsTest use of CyclicBarrier

(cherry-picked from f86b7fe243d8d116fa708259f84a25331b2a235d)

Bug: 7337970
Change-Id:I7da3e22bc2c773258d30f0919d51d386f7b92e7d*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java b/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java
//Synthetic comment -- index 95b5c137..9c9f346 100644

//Synthetic comment -- @@ -77,7 +77,7 @@

/** Test the case where the thread is preemptively unparked. */
public void test_parkFor_3() {
        CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, false, 1000);
Thread parkerThread = new Thread(parker);

//Synthetic comment -- @@ -114,7 +114,7 @@

/** Test the case where the thread is preemptively unparked. */
public void test_parkUntil_3() {
        CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, true, 1000);
Thread parkerThread = new Thread(parker);








