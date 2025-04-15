/*Fix problem with Runtime.runFinalization()

This is a fix for a problem with runFinalization(). The
problem was that all FinalizerReferences to objects that had
not yet been garbage collected was lost when calling this
function. When a FinalizerReference was lost, it is was not
possible to call the finalize() method when the object
was garbage collected.

The result was that finalizers was sometimes never
called, which typically lead to memory leaks.

Bug: 6907299
Change-Id:Ief515edbb5a1823c06d7371415d131165baef7f2*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/FinalizerReference.java b/luni/src/main/java/java/lang/ref/FinalizerReference.java
//Synthetic comment -- index aadf1f6..e0660c3 100644

//Synthetic comment -- @@ -20,15 +20,25 @@
* @hide
*/
public final class FinalizerReference<T> extends Reference<T> {
    // This queue contains those objects eligible for finalization.
public static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

    // This list contains a FinalizerReference for every finalizable object in the heap.
    // Objects in this list may or may not be eligible for finalization yet.
private static FinalizerReference head = null;

    // The links used to construct the list.
    private FinalizerReference prev;
    private FinalizerReference next;

private T zombie;

    // Instances of Sentinel cause a FinalizerReference to be added to the list when they're
    // created, by virtue of being finalizable. Because only the runtime itself (not the library)
    // has access to that FinalizerReference, we add a *different* FinalizerReference to the
    // queue straight away. When that FinalizerReference gets processed, we shouldn't try to
    // remove it from the list because it isn't on the list. This field lets us detect that.
    private boolean onList = true;

public FinalizerReference(T r, ReferenceQueue<? super T> q) {
super(r, q);
//Synthetic comment -- @@ -58,6 +68,10 @@

public static void remove(FinalizerReference reference) {
synchronized (FinalizerReference.class) {
            if (!reference.onList) {
               return;
            }

FinalizerReference next = reference.next;
FinalizerReference prev = reference.prev;
reference.next = null;
//Synthetic comment -- @@ -80,6 +94,7 @@
Sentinel sentinel = new Sentinel();
FinalizerReference<Object> reference = new FinalizerReference<Object>(null, queue);
reference.zombie = sentinel;
        reference.onList = false;
reference.enqueueInternal();
sentinel.awaitFinalization();
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java b/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java
//Synthetic comment -- index 10a26fe..89d0f29 100644

//Synthetic comment -- @@ -29,18 +29,41 @@

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
              System.err.println("finalizer running...");
finalized.set(true);
}
};
        FinalizationTester.induceFinalization();
        // Dance around a bit to discourage dx from realizing that 'result' is no longer live.
        boolean wasFinalized = finalized.get();
        if (wasFinalized) {
            fail("finalizer called early"); // ...because 'result' is still live until we return.
        }
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







