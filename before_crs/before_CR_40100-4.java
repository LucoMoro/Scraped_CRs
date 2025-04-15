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
public static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

private static FinalizerReference head = null;

    private T zombie;

private FinalizerReference prev;

private FinalizerReference next;

public FinalizerReference(T r, ReferenceQueue<? super T> q) {
super(r, q);
}

    @Override
    public T get() {
return zombie;
}

    @Override
    public void clear() {
zombie = null;
}

static void add(Object referent) {
FinalizerReference<?> reference = new FinalizerReference<Object>(referent, queue);
        synchronized (FinalizerReference.class) {
reference.prev = null;
reference.next = head;
if (head != null) {
//Synthetic comment -- @@ -57,7 +63,7 @@
}

public static void remove(FinalizerReference reference) {
        synchronized (FinalizerReference.class) {
FinalizerReference next = reference.next;
FinalizerReference prev = reference.prev;
reference.next = null;
//Synthetic comment -- @@ -74,30 +80,49 @@
}

/**
     * Returns once all currently-enqueued references have been finalized.
*/
public static void finalizeAllEnqueued() throws InterruptedException {
Sentinel sentinel = new Sentinel();
        FinalizerReference<Object> reference = new FinalizerReference<Object>(null, queue);
        reference.zombie = sentinel;
        reference.enqueueInternal();
sentinel.awaitFinalization();
}

/**
* A marker object that we can immediately enqueue. When this object's
* finalize() method is called, we know all previously-enqueued finalizable
* references have been finalized.
     *
     * <p>Each instance of this class will be finalized twice as it is enqueued
     * directly and by the garbage collector.
*/
private static class Sentinel {
boolean finalized = false;
@Override protected synchronized void finalize() throws Throwable {
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
            fail();
}
}

/** Do not inline this method; that could break non-precise GCs. See FinalizationTester. */
    private void createFinalizableObject(final AtomicBoolean finalized) {
        new X() {
@Override protected void finalize() throws Throwable {
super.finalize();
finalized.set(true);
}
};
}

static class X {}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLConnectionTest.java b/luni/src/test/java/libcore/java/net/URLConnectionTest.java
//Synthetic comment -- index a1747fa..762bac4 100644

//Synthetic comment -- @@ -854,7 +854,7 @@
}

public boolean wasCloseGuardCalled() {
            // FinalizationTester.induceFinalization();
close();
return closeGuardCalled.get();
}







