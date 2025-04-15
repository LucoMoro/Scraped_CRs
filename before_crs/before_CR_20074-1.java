/*Add Delay to ThreadTest#test_getState

Wait a bit before checking that the thread state is WAITING. A context
switch between updating the semaphore's internal state and invoking wait
can cause the test to see that the thread is still RUNNABLE.

Change-Id:Ie0ea934b83e6efe76b721289665bb20a13cd6eea*/
//Synthetic comment -- diff --git a/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/ThreadTest.java b/libcore/luni/src/test/java/org/apache/harmony/luni/tests/java/lang/ThreadTest.java
//Synthetic comment -- index a24f457..39755c2 100644

//Synthetic comment -- @@ -1999,7 +1999,10 @@

while (!sem.hasQueuedThreads()){}

assertEquals(Thread.State.WAITING, th.getState());
synchronized (lock) {
sem.release();
long start = System.currentTimeMillis();







