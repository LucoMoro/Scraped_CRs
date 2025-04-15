/*Change ThreadsTest to use CyclicBarrier to address flakiness

(cherry-picked from f86b7fe243d8d116fa708259f84a25331b2a235d)

Bug: 7337970
Change-Id:I17318afadd04d972f907fe968e050ece98116c6a*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java b/luni/src/test/java/tests/api/org/apache/harmony/kernel/dalvik/ThreadsTest.java
//Synthetic comment -- index bdc580d..95b5c137 100644

//Synthetic comment -- @@ -17,7 +17,8 @@
package tests.api.org.apache.harmony.kernel.dalvik;

import java.lang.reflect.Field;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import junit.framework.TestCase;
import sun.misc.Unsafe;
//Synthetic comment -- @@ -50,10 +51,11 @@

/** Test the case where the park times out. */
public void test_parkFor_1() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, false, 500);
Thread parkerThread = new Thread(parker);
Thread waiterThread =
            new Thread(new WaitAndUnpark(barrier, 1000, parkerThread));

parkerThread.start();
waiterThread.start();
//Synthetic comment -- @@ -62,10 +64,11 @@

/** Test the case where the unpark happens before the timeout. */
public void test_parkFor_2() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, false, 1000);
Thread parkerThread = new Thread(parker);
Thread waiterThread =
            new Thread(new WaitAndUnpark(barrier, 300, parkerThread));

parkerThread.start();
waiterThread.start();
//Synthetic comment -- @@ -74,7 +77,8 @@

/** Test the case where the thread is preemptively unparked. */
public void test_parkFor_3() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, false, 1000);
Thread parkerThread = new Thread(parker);

UNSAFE.unpark(parkerThread);
//Synthetic comment -- @@ -84,10 +88,11 @@

/** Test the case where the park times out. */
public void test_parkUntil_1() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, true, 500);
Thread parkerThread = new Thread(parker);
Thread waiterThread =
            new Thread(new WaitAndUnpark(barrier, 1000, parkerThread));

parkerThread.start();
waiterThread.start();
//Synthetic comment -- @@ -96,10 +101,11 @@

/** Test the case where the unpark happens before the timeout. */
public void test_parkUntil_2() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, true, 1000);
Thread parkerThread = new Thread(parker);
Thread waiterThread =
            new Thread(new WaitAndUnpark(barrier, 300, parkerThread));

parkerThread.start();
waiterThread.start();
//Synthetic comment -- @@ -108,7 +114,8 @@

/** Test the case where the thread is preemptively unparked. */
public void test_parkUntil_3() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        Parker parker = new Parker(barrier, true, 1000);
Thread parkerThread = new Thread(parker);

UNSAFE.unpark(parkerThread);
//Synthetic comment -- @@ -123,6 +130,9 @@
* the indicated value, noting the duration of time actually parked.
*/
private static class Parker implements Runnable {

        private final CyclicBarrier barrier;

/** whether {@link #amount} is milliseconds to wait in an
* absolute fashion (<code>true</code>) or nanoseconds to wait
* in a relative fashion (<code>false</code>) */
//Synthetic comment -- @@ -147,7 +157,8 @@
* either case, this constructor takes a duration to park for
* @param parkMillis the number of milliseconds to be parked
*/
        public Parker(CyclicBarrier barrier, boolean absolute, long parkMillis) {
            this.barrier = barrier;
this.absolute = absolute;

// Multiply by 1000000 because parkFor() takes nanoseconds.
//Synthetic comment -- @@ -155,8 +166,14 @@
}

public void run() {
            try {
                barrier.await(60, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
boolean absolute = this.absolute;
long amount = this.amount;
            long startNanos = System.nanoTime();
long start = System.currentTimeMillis();

if (absolute) {
//Synthetic comment -- @@ -165,11 +182,11 @@
UNSAFE.park(false, amount);
}

            long endNanos = System.nanoTime();

synchronized (this) {
                startMillis = startNanos / 1000000;
                endMillis = endNanos / 1000000;
completed = true;
notifyAll();
}
//Synthetic comment -- @@ -230,16 +247,23 @@
* specified amount of time and then unparks an indicated thread.
*/
private static class WaitAndUnpark implements Runnable {
        private final CyclicBarrier barrier;
private final long waitMillis;
private final Thread thread;

        public WaitAndUnpark(CyclicBarrier barrier, long waitMillis, Thread thread) {
            this.barrier = barrier;
this.waitMillis = waitMillis;
this.thread = thread;
}

public void run() {
try {
                barrier.await(60, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
            try {
Thread.sleep(waitMillis);
} catch (InterruptedException ex) {
throw new RuntimeException("shouldn't happen", ex);







