/*Fix OldThreadTest.test_getState to work when run repeatedly

Change-Id:I8a51f073aa47d75fdfe9c3843a019dd44254fb14*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/OldThreadTest.java b/luni/src/test/java/libcore/java/lang/OldThreadTest.java
//Synthetic comment -- index 2b304f2..03031ac 100644

//Synthetic comment -- @@ -307,6 +307,7 @@
assertNotNull(state);
assertEquals(Thread.State.RUNNABLE, state);

final Semaphore sem = new Semaphore(0);
final Object lock = new Object();
Thread th = new Thread() {
//Synthetic comment -- @@ -369,7 +370,7 @@
th.join(1000);
assertEquals(Thread.State.TERMINATED, th.getState());
}
    volatile boolean run = true;

public void test_holdsLock() {
MonitoredClass monitor = new MonitoredClass();







