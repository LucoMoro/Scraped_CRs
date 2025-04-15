/*Wait for threads to finish in ThreadsTest before continuing

Bug: 7355008
Change-Id:Ifdedca5cf5c96c86f94a0deead61e1798d1a495f*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java b/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java
//Synthetic comment -- index 9c9f346..5185885 100644

//Synthetic comment -- @@ -28,8 +28,6 @@
*/
public class ThreadsTest extends TestCase {
private static Unsafe UNSAFE = null;
static {
/*
* Set up {@link #UNSAFE}. This subverts the access check to
//Synthetic comment -- @@ -43,14 +41,14 @@

UNSAFE = (Unsafe) field.get(null);
} catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
} catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
}
}

/** Test the case where the park times out. */
    public void test_parkFor_1() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, false, 500);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -60,10 +58,12 @@
parkerThread.start();
waiterThread.start();
parker.assertDurationIsInRange(500);
        waiterThread.join();
        parkerThread.join();
}

/** Test the case where the unpark happens before the timeout. */
    public void test_parkFor_2() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, false, 1000);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -73,10 +73,12 @@
parkerThread.start();
waiterThread.start();
parker.assertDurationIsInRange(300);
        waiterThread.join();
        parkerThread.join();
}

/** Test the case where the thread is preemptively unparked. */
    public void test_parkFor_3() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(1);
Parker parker = new Parker(barrier, false, 1000);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -84,10 +86,11 @@
UNSAFE.unpark(parkerThread);
parkerThread.start();
parker.assertDurationIsInRange(0);
        parkerThread.join();
}

/** Test the case where the park times out. */
    public void test_parkUntil_1() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, true, 500);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -97,10 +100,12 @@
parkerThread.start();
waiterThread.start();
parker.assertDurationIsInRange(500);
        waiterThread.join();
        parkerThread.join();
}

/** Test the case where the unpark happens before the timeout. */
    public void test_parkUntil_2() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(2);
Parker parker = new Parker(barrier, true, 1000);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -110,10 +115,12 @@
parkerThread.start();
waiterThread.start();
parker.assertDurationIsInRange(300);
        waiterThread.join();
        parkerThread.join();
}

/** Test the case where the thread is preemptively unparked. */
    public void test_parkUntil_3() throws Exception {
CyclicBarrier barrier = new CyclicBarrier(1);
Parker parker = new Parker(barrier, true, 1000);
Thread parkerThread = new Thread(parker);
//Synthetic comment -- @@ -121,6 +128,7 @@
UNSAFE.unpark(parkerThread);
parkerThread.start();
parker.assertDurationIsInRange(0);
        parkerThread.join();
}

// TODO: Add more tests.
//Synthetic comment -- @@ -234,10 +242,12 @@

if (duration < minimum) {
Assert.fail("expected duration: " + expectedMillis +
                            " minimum duration: " + minimum +
                            " actual duration too short: " + duration);
} else if (duration > maximum) {
Assert.fail("expected duration: " + expectedMillis +
                            " maximum duration: " + maximum +
                            " actual duration too long: " + duration);
}
}
}
//Synthetic comment -- @@ -272,11 +282,4 @@
UNSAFE.unpark(thread);
}
}
}







