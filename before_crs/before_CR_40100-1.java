/*Fix problem with Runtime.runFinalization()

This is a fix for a problem with runFinalization(). The
problem was that all FinalizerReferences to objects that had
not yet been garbage collected was lost when calling this
function. When a FinalizerReference was lost, it is was not
possible to call the finalize() method when the object
was garbage collected.

The result was that finalizers was sometimes never
called, which typically lead to memory leaks.

Change-Id:Ief515edbb5a1823c06d7371415d131165baef7f2*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/FinalizerReference.java b/luni/src/main/java/java/lang/ref/FinalizerReference.java
//Synthetic comment -- index aadf1f6..0cd36ff 100644

//Synthetic comment -- @@ -30,6 +30,8 @@

private FinalizerReference next;

public FinalizerReference(T r, ReferenceQueue<? super T> q) {
super(r, q);
}
//Synthetic comment -- @@ -58,6 +60,11 @@

public static void remove(FinalizerReference reference) {
synchronized (FinalizerReference.class) {
FinalizerReference next = reference.next;
FinalizerReference prev = reference.prev;
reference.next = null;
//Synthetic comment -- @@ -80,6 +87,7 @@
Sentinel sentinel = new Sentinel();
FinalizerReference<Object> reference = new FinalizerReference<Object>(null, queue);
reference.zombie = sentinel;
reference.enqueueInternal();
sentinel.awaitFinalization();
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java b/luni/src/test/java/libcore/java/lang/ref/FinalizeTest.java
//Synthetic comment -- index 10a26fe..8ec910f 100644

//Synthetic comment -- @@ -32,10 +32,28 @@
fail();
}
}

/** Do not inline this method; that could break non-precise GCs. See FinalizationTester. */
    private void createFinalizableObject(final AtomicBoolean finalized) {
        new X() {
@Override protected void finalize() throws Throwable {
super.finalize();
finalized.set(true);







