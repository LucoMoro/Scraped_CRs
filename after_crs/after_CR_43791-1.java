/*Fix flaky FinalizeTest

Chained finalizer starts when finalizer is run and the
current induceFinalization does not sufficiently induce
finalization.

Add more gc() and sleep() calls to ensure chained
finalizer is running to fix flaky
testSystemRunFinalizationReturnsEvenIfQueueIsNonEmpty.

Signed-off-by: Lawrence Mok <lawrencem@gmail.com>*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java b/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java
//Synthetic comment -- index 10a26fe..f017f98 100644

//Synthetic comment -- @@ -101,6 +101,21 @@
AtomicInteger count = new AtomicInteger();
AtomicBoolean keepGoing = new AtomicBoolean(true);
createChainedFinalizer(count, keepGoing);

        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
        System.gc();

FinalizationTester.induceFinalization();
keepGoing.set(false);
assertTrue(count.get() > 0);







