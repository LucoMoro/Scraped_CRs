/*Clean up lint in java.lang.ref.

Change-Id:Ie54af5965f07e8f0261eaeb5803d718658da2a23*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/FinalizerReference.java b/luni/src/main/java/java/lang/ref/FinalizerReference.java
//Synthetic comment -- index 7a6d871..14eaae4 100644

//Synthetic comment -- @@ -28,11 +28,11 @@

// This list contains a FinalizerReference for every finalizable object in the heap.
// Objects in this list may or may not be eligible for finalization yet.
    private static FinalizerReference head = null;

// The links used to construct the list.
    private FinalizerReference prev;
    private FinalizerReference next;

// When the GC wants something finalized, it moves it from the 'referent' field to
// the 'zombie' field instead.
//Synthetic comment -- @@ -50,7 +50,7 @@
zombie = null;
}

    static void add(Object referent) {
FinalizerReference<?> reference = new FinalizerReference<Object>(referent, queue);
synchronized (LIST_LOCK) {
reference.prev = null;
//Synthetic comment -- @@ -62,10 +62,10 @@
}
}

    public static void remove(FinalizerReference reference) {
synchronized (LIST_LOCK) {
            FinalizerReference next = reference.next;
            FinalizerReference prev = reference.prev;
reference.next = null;
reference.prev = null;
if (prev != null) {
//Synthetic comment -- @@ -93,11 +93,12 @@
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








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/Reference.java b/luni/src/main/java/java/lang/ref/Reference.java
//Synthetic comment -- index 85fbb04..9cf49a7 100644

//Synthetic comment -- @@ -55,8 +55,7 @@
* VM requirement: this field <em>must</em> be called "queue"
* and be a java.lang.ref.ReferenceQueue.
*/
    @SuppressWarnings("unchecked")
    volatile ReferenceQueue queue;

/**
* Used internally by java.lang.ref.ReferenceQueue.
//Synthetic comment -- @@ -82,7 +81,7 @@
Reference() {
}

    Reference(T r, ReferenceQueue q) {
referent = r;
queue = q;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ref/ReferenceQueue.java b/luni/src/main/java/java/lang/ref/ReferenceQueue.java
//Synthetic comment -- index 6c9b4d5..2b8089c 100644

//Synthetic comment -- @@ -131,8 +131,6 @@
*
* @param reference
*            reference object to be enqueued.
     * @return boolean true if reference is enqueued. false if reference failed
     *         to enqueue.
*/
synchronized void enqueue(Reference<? extends T> reference) {
if (head == null) {
//Synthetic comment -- @@ -145,7 +143,7 @@
}

/** @hide */
    public static Reference unenqueued = null;

static void add(Reference<?> list) {
synchronized (ReferenceQueue.class) {







