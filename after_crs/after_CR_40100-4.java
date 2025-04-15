/*Fix problem with Runtime.runFinalization()

This is a fix for a problem with runFinalization(). The
problem was that all FinalizerReferences to objects that had
not yet been garbage collected were lost when calling this
function. When a FinalizerReference was lost, it is was not
possible to call the finalize() method when the object
was garbage collected.

The result was that finalizers was sometimes never
called, which typically lead to memory leaks.

Also stop synchronizing on the class itself; use a private
lock instead.

Bug: 6907299
Bug: 5462944 # Synchronization on FinalizerReference.class
Change-Id:Ief515edbb5a1823c06d7371415d131165baef7f2*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/FinalizerReference.java b/luni/src/main/java/java/lang/ref/FinalizerReference.java
//Synthetic comment -- index aadf1f6..7a6d871 100644

//Synthetic comment -- @@ -20,33 +20,39 @@
* @hide
*/
public final class FinalizerReference<T> extends Reference<T> {
    // This queue contains those objects eligible for finalization.
public static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

    // Guards the list (not the queue).
    private static final Object LIST_LOCK = new Object();

    // This list contains a FinalizerReference for every finalizable object in the heap.
    // Objects in this list may or may not be eligible for finalization yet.
private static FinalizerReference head = null;

    // The links used to construct the list.
private FinalizerReference prev;
private FinalizerReference next;

    // When the GC wants something finalized, it moves it from the 'referent' field to
    // the 'zombie' field instead.
    private T zombie;

public FinalizerReference(T r, ReferenceQueue<? super T> q) {
super(r, q);
}

    @Override public T get() {
return zombie;
}

    @Override public void clear() {
zombie = null;
}

static void add(Object referent) {
FinalizerReference<?> reference = new FinalizerReference<Object>(referent, queue);
        synchronized (LIST_LOCK) {
reference.prev = null;
reference.next = head;
if (head != null) {
//Synthetic comment -- @@ -57,7 +63,7 @@
}

public static void remove(FinalizerReference reference) {
        synchronized (LIST_LOCK) {
FinalizerReference next = reference.next;
FinalizerReference prev = reference.prev;
reference.next = null;
//Synthetic comment -- @@ -74,30 +80,49 @@
}

/**
     * Waits for all currently-enqueued references to be finalized.
*/
public static void finalizeAllEnqueued() throws InterruptedException {
Sentinel sentinel = new Sentinel();
        enqueueSentinelReference(sentinel);
sentinel.awaitFinalization();
}

    private static void enqueueSentinelReference(Sentinel sentinel) {
        synchronized (LIST_LOCK) {
            // When a finalizable object is allocated, a FinalizerReference is added to the list.
            // We search the list for that FinalizerReference (it should be at or near the head),
            // and then put it on the queue so that it can be finalized.
            for (FinalizerReference r = head; r != null; r = r.next) {
                if (r.referent == sentinel) {
                    r.referent = null;
                    r.zombie = sentinel;
                    r.enqueueInternal();
                    return;
                }
            }
        }
        // We just created a finalizable object and still hold a reference to it.
        // It must be on the list.
        throw new AssertionError("newly-created live Sentinel not on list!");
    }

/**
* A marker object that we can immediately enqueue. When this object's
* finalize() method is called, we know all previously-enqueued finalizable
* references have been finalized.
*/
private static class Sentinel {
boolean finalized = false;

@Override protected synchronized void finalize() throws Throwable {
            if (finalized) {
                throw new AssertionError();
            }
finalized = true;
notifyAll();
}

synchronized void awaitFinalization() throws InterruptedException {
while (!finalized) {
wait();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java b/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java
//Synthetic comment -- index 10a26fe..ef303bd 100644

//Synthetic comment -- @@ -29,18 +29,42 @@

FinalizationTester.induceFinalization();
if (!finalized.get()) {
            fail("object not yet finalized");
        }
    }

    /**
     * Test verifies that runFinalization() does not mess up objects
     * that should be finalized later on. http://b/6907299
     */
    public void testInducedFinalization() throws Exception {
        AtomicBoolean finalized1 = new AtomicBoolean();
        AtomicBoolean finalized2 = new AtomicBoolean();
        createFinalizableObject(finalized1);
        createFinalizableObject(finalized2);
        FinalizationTester.induceFinalization();
        if (!finalized1.get() || !finalized2.get()) {
            fail("not yet finalized: " + finalized1.get() + " " + finalized2.get());
}
}

/** Do not inline this method; that could break non-precise GCs. See FinalizationTester. */
    private X createFinalizableObject(final AtomicBoolean finalized) {
        X result = new X() {
@Override protected void finalize() throws Throwable {
super.finalize();
finalized.set(true);
}
};
        FinalizationTester.induceFinalization();
        // Dance around a bit to discourage dx from realizing that 'result' is no longer live.
        boolean wasFinalized = finalized.get();
        if (wasFinalized) {
            fail("finalizer called early"); // ...because 'result' is still live until we return.
        }
        // But we don't actually want to return 'result' because then we'd have to worry about
        // the caller accidentally keeping it live.
        return wasFinalized ? result : null;
}

static class X {}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLConnectionTest.java b/luni/src/test/java/libcore/java/net/URLConnectionTest.java
//Synthetic comment -- index a1747fa..762bac4 100644

//Synthetic comment -- @@ -854,7 +854,7 @@
}

public boolean wasCloseGuardCalled() {
            FinalizationTester.induceFinalization();
close();
return closeGuardCalled.get();
}







