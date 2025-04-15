/*Update java.util.concurrent to 2011-nov-1.

The most recent change to the upstream CVS tree was on Tue Oct 25 20:29:12 2011 UTC.

This removes references to security managers.

(cherry-pick of d206d1f85f051ec85bc1b00d576a67fa9be13228.)

Bug: 3289698
Change-Id:Id89c909407f268fdc828ebe2bebcb1c12dbb93aa*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/AbstractQueue.java b/luni/src/main/java/java/util/AbstractQueue.java
//Synthetic comment -- index d368ac9..b046559 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayDeque.java b/luni/src/main/java/java/util/ArrayDeque.java
//Synthetic comment -- index fafcdb4..c4d67b8 100644

//Synthetic comment -- @@ -1,6 +1,6 @@
/*
* Written by Josh Bloch of Google Inc. and released to the public domain,
 * as explained at http://creativecommons.org/publicdomain/zero/1.0/.
*/

package java.util;
//Synthetic comment -- @@ -9,8 +9,6 @@
// removed link to collections framework docs
// END android-note

/**
* Resizable-array implementation of the {@link Deque} interface.  Array
* deques have no capacity restrictions; they grow as necessary to support
//Synthetic comment -- @@ -53,7 +51,7 @@
* @param <E> the type of elements held in this collection
*/
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, java.io.Serializable
{
/**
* The array in which the elements of the deque are stored.
//Synthetic comment -- @@ -65,7 +63,7 @@
* other.  We also guarantee that all array cells not holding
* deque elements are always null.
*/
    private transient Object[] elements;

/**
* The index of the element at the head of the deque (which is the
//Synthetic comment -- @@ -109,7 +107,7 @@
if (initialCapacity < 0)   // Too many elements, must back off
initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
}
        elements = new Object[initialCapacity];
}

/**
//Synthetic comment -- @@ -127,7 +125,7 @@
Object[] a = new Object[newCapacity];
System.arraycopy(elements, p, a, 0, r);
System.arraycopy(elements, 0, a, r, p);
        elements = a;
head = 0;
tail = n;
}
//Synthetic comment -- @@ -155,7 +153,7 @@
* sufficient to hold 16 elements.
*/
public ArrayDeque() {
        elements = new Object[16];
}

/**
//Synthetic comment -- @@ -263,7 +261,8 @@

public E pollFirst() {
int h = head;
        @SuppressWarnings("unchecked") E result = (E) elements[h];
        // Element is null if deque empty
if (result == null)
return null;
elements[h] = null;     // Must null out slot
//Synthetic comment -- @@ -273,7 +272,7 @@

public E pollLast() {
int t = (tail - 1) & (elements.length - 1);
        @SuppressWarnings("unchecked") E result = (E) elements[t];
if (result == null)
return null;
elements[t] = null;
//Synthetic comment -- @@ -285,28 +284,33 @@
* @throws NoSuchElementException {@inheritDoc}
*/
public E getFirst() {
        @SuppressWarnings("unchecked") E result = (E) elements[head];
        if (result == null)
throw new NoSuchElementException();
        return result;
}

/**
* @throws NoSuchElementException {@inheritDoc}
*/
public E getLast() {
        @SuppressWarnings("unchecked")
        E result = (E) elements[(tail - 1) & (elements.length - 1)];
        if (result == null)
throw new NoSuchElementException();
        return result;
}

public E peekFirst() {
        @SuppressWarnings("unchecked") E result = (E) elements[head];
        // elements[head] is null if deque empty
        return result;
}

public E peekLast() {
        @SuppressWarnings("unchecked")
        E result = (E) elements[(tail - 1) & (elements.length - 1)];
        return result;
}

/**
//Synthetic comment -- @@ -326,7 +330,7 @@
return false;
int mask = elements.length - 1;
int i = head;
        Object x;
while ( (x = elements[i]) != null) {
if (o.equals(x)) {
delete(i);
//Synthetic comment -- @@ -354,7 +358,7 @@
return false;
int mask = elements.length - 1;
int i = (tail - 1) & mask;
        Object x;
while ( (x = elements[i]) != null) {
if (o.equals(x)) {
delete(i);
//Synthetic comment -- @@ -499,7 +503,7 @@
*/
private boolean delete(int i) {
checkInvariants();
        final Object[] elements = this.elements;
final int mask = elements.length - 1;
final int h = head;
final int t = tail;
//Synthetic comment -- @@ -597,7 +601,7 @@
public E next() {
if (cursor == fence)
throw new NoSuchElementException();
            @SuppressWarnings("unchecked") E result = (E) elements[cursor];
// This check doesn't catch all possible comodifications,
// but does catch the ones that corrupt traversal
if (tail != fence || result == null)
//Synthetic comment -- @@ -636,7 +640,7 @@
if (cursor == fence)
throw new NoSuchElementException();
cursor = (cursor - 1) & (elements.length - 1);
            @SuppressWarnings("unchecked") E result = (E) elements[cursor];
if (head != fence || result == null)
throw new ConcurrentModificationException();
lastRet = cursor;
//Synthetic comment -- @@ -667,7 +671,7 @@
return false;
int mask = elements.length - 1;
int i = head;
        Object x;
while ( (x = elements[i]) != null) {
if (o.equals(x))
return true;
//Synthetic comment -- @@ -750,8 +754,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of <tt>String</tt>:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -765,6 +768,7 @@
*         this deque
* @throws NullPointerException if the specified array is null
*/
    @SuppressWarnings("unchecked")
public <T> T[] toArray(T[] a) {
int size = size();
if (a.length < size)
//Synthetic comment -- @@ -785,6 +789,7 @@
*/
public ArrayDeque<E> clone() {
try {
            @SuppressWarnings("unchecked")
ArrayDeque<E> result = (ArrayDeque<E>) super.clone();
result.elements = Arrays.copyOf(elements, elements.length);
return result;
//Synthetic comment -- @@ -806,7 +811,8 @@
* followed by all of its elements (each an object reference) in
* first-to-last order.
*/
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
s.defaultWriteObject();

// Write out size
//Synthetic comment -- @@ -821,8 +827,8 @@
/**
* Deserialize this deque.
*/
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
s.defaultReadObject();

// Read in size and allocate array
//Synthetic comment -- @@ -833,6 +839,6 @@

// Read in all elements in the proper order.
for (int i = 0; i < size; i++)
            elements[i] = s.readObject();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Deque.java b/luni/src/main/java/java/util/Deque.java
//Synthetic comment -- index cb6bd90..f74a6b4 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
//Synthetic comment -- @@ -356,7 +355,7 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer(Object) offer}.
*
* <p>This method is equivalent to {@link #addLast}.
*








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/NavigableMap.java b/luni/src/main/java/java/util/NavigableMap.java
//Synthetic comment -- index 29961c8..beeb651 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of JCP
* JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
//Synthetic comment -- @@ -48,9 +47,9 @@
* method {@code put}.
*
* <p>Methods
 * {@link #subMap(Object, Object) subMap(K, K)},
 * {@link #headMap(Object) headMap(K)}, and
 * {@link #tailMap(Object) tailMap(K)}
* are specified to return {@code SortedMap} to allow existing
* implementations of {@code SortedMap} to be compatibly retrofitted to
* implement {@code NavigableMap}, but extensions and implementations








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/NavigableSet.java b/luni/src/main/java/java/util/NavigableSet.java
//Synthetic comment -- index cff0800..f410313 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of JCP
* JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
//Synthetic comment -- @@ -41,9 +40,9 @@
* Comparable} elements intrinsically do not permit {@code null}.)
*
* <p>Methods
 * {@link #subSet(Object, Object) subSet(E, E)},
 * {@link #headSet(Object) headSet(E)}, and
 * {@link #tailSet(Object) tailSet(E)}
* are specified to return {@code SortedSet} to allow existing
* implementations of {@code SortedSet} to be compatibly retrofitted to
* implement {@code NavigableSet}, but extensions and implementations








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Queue.java b/luni/src/main/java/java/util/Queue.java
//Synthetic comment -- index 5aef944..8b465e6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/AbstractExecutorService.java b/luni/src/main/java/java/util/concurrent/AbstractExecutorService.java
//Synthetic comment -- index 36fcecc..a7f7745 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ArrayBlockingQueue.java b/luni/src/main/java/java/util/concurrent/ArrayBlockingQueue.java
//Synthetic comment -- index a622832..e30ab67 100644

//Synthetic comment -- @@ -1,12 +1,17 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.ref.WeakReference;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -73,11 +78,20 @@

/** Main lock guarding all access */
final ReentrantLock lock;

/** Condition for waiting takes */
private final Condition notEmpty;

/** Condition for waiting puts */
private final Condition notFull;

    /**
     * Shared state for currently active iterators, or null if there
     * are known not to be any.  Allows queue operations to update
     * iterator state.
     */
    transient Itrs itrs = null;

// Internal helper methods

/**
//Synthetic comment -- @@ -94,16 +108,12 @@
return ((i == 0) ? items.length : i) - 1;
}

/**
* Returns item at index i.
*/
final E itemAt(int i) {
        @SuppressWarnings("unchecked") E x = (E) items[i];
        return x;
}

/**
//Synthetic comment -- @@ -120,10 +130,12 @@
* Inserts element at current put position, advances, and signals.
* Call only when holding lock.
*/
    private void enqueue(E x) {
        // assert lock.getHoldCount() == 1;
        // assert items[putIndex] == null;
items[putIndex] = x;
putIndex = inc(putIndex);
        count++;
notEmpty.signal();
}

//Synthetic comment -- @@ -131,42 +143,57 @@
* Extracts element at current take position, advances, and signals.
* Call only when holding lock.
*/
    private E dequeue() {
        // assert lock.getHoldCount() == 1;
        // assert items[takeIndex] != null;
final Object[] items = this.items;
        @SuppressWarnings("unchecked") E x = (E) items[takeIndex];
items[takeIndex] = null;
takeIndex = inc(takeIndex);
        count--;
        if (itrs != null)
            itrs.elementDequeued();
notFull.signal();
return x;
}

/**
     * Deletes item at array index removeIndex.
     * Utility for remove(Object) and iterator.remove.
* Call only when holding lock.
*/
    void removeAt(final int removeIndex) {
        // assert lock.getHoldCount() == 1;
        // assert items[removeIndex] != null;
        // assert removeIndex >= 0 && removeIndex < items.length;
final Object[] items = this.items;
        if (removeIndex == takeIndex) {
            // removing front item; just advance
items[takeIndex] = null;
takeIndex = inc(takeIndex);
            count--;
            if (itrs != null)
                itrs.elementDequeued();
} else {
            // an "interior" remove

// slide over all others up through putIndex.
            final int putIndex = this.putIndex;
            for (int i = removeIndex;;) {
                int next = inc(i);
                if (next != putIndex) {
                    items[i] = items[next];
                    i = next;
} else {
items[i] = null;
                    this.putIndex = i;
break;
}
}
            count--;
            if (itrs != null)
                itrs.removedAt(removeIndex);
}
notFull.signal();
}

//Synthetic comment -- @@ -271,7 +298,7 @@
if (count == items.length)
return false;
else {
                enqueue(e);
return true;
}
} finally {
//Synthetic comment -- @@ -293,7 +320,7 @@
try {
while (count == items.length)
notFull.await();
            enqueue(e);
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -320,7 +347,7 @@
return false;
nanos = notFull.awaitNanos(nanos);
}
            enqueue(e);
return true;
} finally {
lock.unlock();
//Synthetic comment -- @@ -331,7 +358,7 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            return (count == 0) ? null : dequeue();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -343,7 +370,7 @@
try {
while (count == 0)
notEmpty.await();
            return dequeue();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -359,7 +386,7 @@
return null;
nanos = notEmpty.awaitNanos(nanos);
}
            return dequeue();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -438,11 +465,15 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            if (count > 0) {
                final int putIndex = this.putIndex;
                int i = takeIndex;
                do {
                    if (o.equals(items[i])) {
                        removeAt(i);
                        return true;
                    }
                } while ((i = inc(i)) != putIndex);
}
return false;
} finally {
//Synthetic comment -- @@ -464,9 +495,14 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            if (count > 0) {
                final int putIndex = this.putIndex;
                int i = takeIndex;
                do {
                    if (o.equals(items[i]))
                        return true;
                } while ((i = inc(i)) != putIndex);
            }
return false;
} finally {
lock.unlock();
//Synthetic comment -- @@ -522,8 +558,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -589,12 +624,20 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            int k = count;
            if (k > 0) {
                final int putIndex = this.putIndex;
                int i = takeIndex;
                do {
                    items[i] = null;
                } while ((i = inc(i)) != putIndex);
                takeIndex = putIndex;
                count = 0;
                if (itrs != null)
                    itrs.queueIsEmpty();
                for (; k > 0 && lock.hasWaiters(notFull); k--)
                    notFull.signal();
            }
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -607,32 +650,7 @@
* @throws IllegalArgumentException      {@inheritDoc}
*/
public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
}

/**
//Synthetic comment -- @@ -651,21 +669,33 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            int n = Math.min(maxElements, count);
            int take = takeIndex;
            int i = 0;
            try {
                while (i < n) {
                    @SuppressWarnings("unchecked") E x = (E) items[take];
                    c.add(x);
                    items[take] = null;
                    take = inc(take);
                    i++;
                }
                return n;
            } finally {
                // Restore invariants even if c.add() threw
                if (i > 0) {
                    count -= i;
                    takeIndex = take;
                    if (itrs != null) {
                        if (count == 0)
                            itrs.queueIsEmpty();
                        else if (i > take)
                            itrs.takeIndexWrapped();
                    }
                    for (; i > 0 && lock.hasWaiters(notFull); i--)
                        notFull.signal();
                }
}
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -675,12 +705,12 @@
* Returns an iterator over the elements in this queue in proper sequence.
* The elements will be returned in order from first (head) to last (tail).
*
     * <p>The returned iterator is a "weakly consistent" iterator that
* will never throw {@link java.util.ConcurrentModificationException
     * ConcurrentModificationException}, and guarantees to traverse
     * elements as they existed upon construction of the iterator, and
     * may (but is not guaranteed to) reflect any modifications
     * subsequent to construction.
*
* @return an iterator over the elements in this queue in proper sequence
*/
//Synthetic comment -- @@ -689,88 +719,627 @@
}

/**
     * Shared data between iterators and their queue, allowing queue
     * modifications to update iterators when elements are removed.
     *
     * This adds a lot of complexity for the sake of correctly
     * handling some uncommon operations, but the combination of
     * circular-arrays and supporting interior removes (i.e., those
     * not at head) would cause iterators to sometimes lose their
     * places and/or (re)report elements they shouldn't.  To avoid
     * this, when a queue has one or more iterators, it keeps iterator
     * state consistent by:
     *
     * (1) keeping track of the number of "cycles", that is, the
     *     number of times takeIndex has wrapped around to 0.
     * (2) notifying all iterators via the callback removedAt whenever
     *     an interior element is removed (and thus other elements may
     *     be shifted).
     *
     * These suffice to eliminate iterator inconsistencies, but
     * unfortunately add the secondary responsibility of maintaining
     * the list of iterators.  We track all active iterators in a
     * simple linked list (accessed only when the queue's lock is
     * held) of weak references to Itr.  The list is cleaned up using
     * 3 different mechanisms:
     *
     * (1) Whenever a new iterator is created, do some O(1) checking for
     *     stale list elements.
     *
     * (2) Whenever takeIndex wraps around to 0, check for iterators
     *     that have been unused for more than one wrap-around cycle.
     *
     * (3) Whenever the queue becomes empty, all iterators are notified
     *     and this entire data structure is discarded.
     *
     * So in addition to the removedAt callback that is necessary for
     * correctness, iterators have the shutdown and takeIndexWrapped
     * callbacks that help remove stale iterators from the list.
     *
     * Whenever a list element is examined, it is expunged if either
     * the GC has determined that the iterator is discarded, or if the
     * iterator reports that it is "detached" (does not need any
     * further state updates).  Overhead is maximal when takeIndex
     * never advances, iterators are discarded before they are
     * exhausted, and all removals are interior removes, in which case
     * all stale iterators are discovered by the GC.  But even in this
     * case we don't increase the amortized complexity.
     *
     * Care must be taken to keep list sweeping methods from
     * reentrantly invoking another such method, causing subtle
     * corruption bugs.
*/
    class Itrs {

        /**
         * Node in a linked list of weak iterator references.
         */
        private class Node extends WeakReference<Itr> {
            Node next;

            Node(Itr iterator, Node next) {
                super(iterator);
                this.next = next;
}
}

        /** Incremented whenever takeIndex wraps around to 0 */
        int cycles = 0;

        /** Linked list of weak iterator references */
        private Node head;

        /** Used to expunge stale iterators */
        private Node sweeper = null;

        private static final int SHORT_SWEEP_PROBES = 4;
        private static final int LONG_SWEEP_PROBES = 16;

        Itrs(Itr initial) {
            register(initial);
}

        /**
         * Sweeps itrs, looking for and expunging stale iterators.
         * If at least one was found, tries harder to find more.
         * Called only from iterating thread.
         *
         * @param tryHarder whether to start in try-harder mode, because
         * there is known to be at least one iterator to collect
         */
        void doSomeSweeping(boolean tryHarder) {
            // assert lock.getHoldCount() == 1;
            // assert head != null;
            int probes = tryHarder ? LONG_SWEEP_PROBES : SHORT_SWEEP_PROBES;
            Node o, p;
            final Node sweeper = this.sweeper;
            boolean passedGo;   // to limit search to one full sweep

            if (sweeper == null) {
                o = null;
                p = head;
                passedGo = true;
            } else {
                o = sweeper;
                p = o.next;
                passedGo = false;
            }

            for (; probes > 0; probes--) {
                if (p == null) {
                    if (passedGo)
                        break;
                    o = null;
                    p = head;
                    passedGo = true;
}
                final Itr it = p.get();
                final Node next = p.next;
                if (it == null || it.isDetached()) {
                    // found a discarded/exhausted iterator
                    probes = LONG_SWEEP_PROBES; // "try harder"
                    // unlink p
                    p.clear();
                    p.next = null;
                    if (o == null) {
                        head = next;
                        if (next == null) {
                            // We've run out of iterators to track; retire
                            itrs = null;
                            return;
                        }
                    }
                    else
                        o.next = next;
                } else {
                    o = p;
                }
                p = next;
}

            this.sweeper = (p == null) ? null : o;
}

        /**
         * Adds a new iterator to the linked list of tracked iterators.
         */
        void register(Itr itr) {
            // assert lock.getHoldCount() == 1;
            head = new Node(itr, head);
        }

        /**
         * Called whenever takeIndex wraps around to 0.
         *
         * Notifies all iterators, and expunges any that are now stale.
         */
        void takeIndexWrapped() {
            // assert lock.getHoldCount() == 1;
            cycles++;
            for (Node o = null, p = head; p != null;) {
                final Itr it = p.get();
                final Node next = p.next;
                if (it == null || it.takeIndexWrapped()) {
                    // unlink p
                    // assert it == null || it.isDetached();
                    p.clear();
                    p.next = null;
                    if (o == null)
                        head = next;
                    else
                        o.next = next;
                } else {
                    o = p;
}
                p = next;
}
            if (head == null)   // no more iterators to track
                itrs = null;
        }

        /**
         * Called whenever an interior remove (not at takeIndex) occured.
         *
         * Notifies all iterators, and expunges any that are now stale.
         */
        void removedAt(int removedIndex) {
            for (Node o = null, p = head; p != null;) {
                final Itr it = p.get();
                final Node next = p.next;
                if (it == null || it.removedAt(removedIndex)) {
                    // unlink p
                    // assert it == null || it.isDetached();
                    p.clear();
                    p.next = null;
                    if (o == null)
                        head = next;
                    else
                        o.next = next;
                } else {
                    o = p;
                }
                p = next;
            }
            if (head == null)   // no more iterators to track
                itrs = null;
        }

        /**
         * Called whenever the queue becomes empty.
         *
         * Notifies all active iterators that the queue is empty,
         * clears all weak refs, and unlinks the itrs datastructure.
         */
        void queueIsEmpty() {
            // assert lock.getHoldCount() == 1;
            for (Node p = head; p != null; p = p.next) {
                Itr it = p.get();
                if (it != null) {
                    p.clear();
                    it.shutdown();
                }
            }
            head = null;
            itrs = null;
        }

        /**
         * Called whenever an element has been dequeued (at takeIndex).
         */
        void elementDequeued() {
            // assert lock.getHoldCount() == 1;
            if (count == 0)
                queueIsEmpty();
            else if (takeIndex == 0)
                takeIndexWrapped();
}
}

    /**
     * Iterator for ArrayBlockingQueue.
     *
     * To maintain weak consistency with respect to puts and takes, we
     * read ahead one slot, so as to not report hasNext true but then
     * not have an element to return.
     *
     * We switch into "detached" mode (allowing prompt unlinking from
     * itrs without help from the GC) when all indices are negative, or
     * when hasNext returns false for the first time.  This allows the
     * iterator to track concurrent updates completely accurately,
     * except for the corner case of the user calling Iterator.remove()
     * after hasNext() returned false.  Even in this case, we ensure
     * that we don't remove the wrong element by keeping track of the
     * expected element to remove, in lastItem.  Yes, we may fail to
     * remove lastItem from the queue if it moved due to an interleaved
     * interior remove while in detached mode.
     */
    private class Itr implements Iterator<E> {
        /** Index to look for new nextItem; NONE at end */
        private int cursor;

        /** Element to be returned by next call to next(); null if none */
        private E nextItem;

        /** Index of nextItem; NONE if none, REMOVED if removed elsewhere */
        private int nextIndex;

        /** Last element returned; null if none or not detached. */
        private E lastItem;

        /** Index of lastItem, NONE if none, REMOVED if removed elsewhere */
        private int lastRet;

        /** Previous value of takeIndex, or DETACHED when detached */
        private int prevTakeIndex;

        /** Previous value of iters.cycles */
        private int prevCycles;

        /** Special index value indicating "not available" or "undefined" */
        private static final int NONE = -1;

        /**
         * Special index value indicating "removed elsewhere", that is,
         * removed by some operation other than a call to this.remove().
         */
        private static final int REMOVED = -2;

        /** Special value for prevTakeIndex indicating "detached mode" */
        private static final int DETACHED = -3;

        Itr() {
            // assert lock.getHoldCount() == 0;
            lastRet = NONE;
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                if (count == 0) {
                    // assert itrs == null;
                    cursor = NONE;
                    nextIndex = NONE;
                    prevTakeIndex = DETACHED;
                } else {
                    final int takeIndex = ArrayBlockingQueue.this.takeIndex;
                    prevTakeIndex = takeIndex;
                    nextItem = itemAt(nextIndex = takeIndex);
                    cursor = incCursor(takeIndex);
                    if (itrs == null) {
                        itrs = new Itrs(this);
                    } else {
                        itrs.register(this); // in this order
                        itrs.doSomeSweeping(false);
                    }
                    prevCycles = itrs.cycles;
                    // assert takeIndex >= 0;
                    // assert prevTakeIndex == takeIndex;
                    // assert nextIndex >= 0;
                    // assert nextItem != null;
                }
            } finally {
                lock.unlock();
            }
        }

        boolean isDetached() {
            // assert lock.getHoldCount() == 1;
            return prevTakeIndex < 0;
        }

        private int incCursor(int index) {
            // assert lock.getHoldCount() == 1;
            index = inc(index);
            if (index == putIndex)
                index = NONE;
            return index;
        }

        /**
         * Returns true if index is invalidated by the given number of
         * dequeues, starting from prevTakeIndex.
         */
        private boolean invalidated(int index, int prevTakeIndex,
                                    long dequeues, int length) {
            if (index < 0)
                return false;
            int distance = index - prevTakeIndex;
            if (distance < 0)
                distance += length;
            return dequeues > distance;
        }

        /**
         * Adjusts indices to incorporate all dequeues since the last
         * operation on this iterator.  Call only from iterating thread.
         */
        private void incorporateDequeues() {
            // assert lock.getHoldCount() == 1;
            // assert itrs != null;
            // assert !isDetached();
            // assert count > 0;

            final int cycles = itrs.cycles;
            final int takeIndex = ArrayBlockingQueue.this.takeIndex;
            final int prevCycles = this.prevCycles;
            final int prevTakeIndex = this.prevTakeIndex;

            if (cycles != prevCycles || takeIndex != prevTakeIndex) {
                final int len = items.length;
                // how far takeIndex has advanced since the previous
                // operation of this iterator
                long dequeues = (cycles - prevCycles) * len
                    + (takeIndex - prevTakeIndex);

                // Check indices for invalidation
                if (invalidated(lastRet, prevTakeIndex, dequeues, len))
                    lastRet = REMOVED;
                if (invalidated(nextIndex, prevTakeIndex, dequeues, len))
                    nextIndex = REMOVED;
                if (invalidated(cursor, prevTakeIndex, dequeues, len))
                    cursor = takeIndex;

                if (cursor < 0 && nextIndex < 0 && lastRet < 0)
                    detach();
                else {
                    this.prevCycles = cycles;
                    this.prevTakeIndex = takeIndex;
                }
            }
        }

        /**
         * Called when itrs should stop tracking this iterator, either
         * because there are no more indices to update (cursor < 0 &&
         * nextIndex < 0 && lastRet < 0) or as a special exception, when
         * lastRet >= 0, because hasNext() is about to return false for the
         * first time.  Call only from iterating thread.
         */
        private void detach() {
            // Switch to detached mode
            // assert lock.getHoldCount() == 1;
            // assert cursor == NONE;
            // assert nextIndex < 0;
            // assert lastRet < 0 || nextItem == null;
            // assert lastRet < 0 ^ lastItem != null;
            if (prevTakeIndex >= 0) {
                // assert itrs != null;
                prevTakeIndex = DETACHED;
                // try to unlink from itrs (but not too hard)
                itrs.doSomeSweeping(true);
            }
        }

        /**
         * For performance reasons, we would like not to acquire a lock in
         * hasNext in the common case.  To allow for this, we only access
         * fields (i.e. nextItem) that are not modified by update operations
         * triggered by queue modifications.
         */
        public boolean hasNext() {
            // assert lock.getHoldCount() == 0;
            if (nextItem != null)
                return true;
            noNext();
            return false;
        }

        private void noNext() {
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                // assert cursor == NONE;
                // assert nextIndex == NONE;
                if (!isDetached()) {
                    // assert lastRet >= 0;
                    incorporateDequeues(); // might update lastRet
                    if (lastRet >= 0) {
                        lastItem = itemAt(lastRet);
                        // assert lastItem != null;
                        detach();
                    }
                }
                // assert isDetached();
                // assert lastRet < 0 ^ lastItem != null;
            } finally {
                lock.unlock();
            }
        }

        public E next() {
            // assert lock.getHoldCount() == 0;
            final E x = nextItem;
            if (x == null)
                throw new NoSuchElementException();
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                if (!isDetached())
                    incorporateDequeues();
                // assert nextIndex != NONE;
                // assert lastItem == null;
                lastRet = nextIndex;
                final int cursor = this.cursor;
                if (cursor >= 0) {
                    nextItem = itemAt(nextIndex = cursor);
                    // assert nextItem != null;
                    this.cursor = incCursor(cursor);
                } else {
                    nextIndex = NONE;
                    nextItem = null;
                }
            } finally {
                lock.unlock();
            }
            return x;
        }

        public void remove() {
            // assert lock.getHoldCount() == 0;
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                if (!isDetached())
                    incorporateDequeues(); // might update lastRet or detach
                final int lastRet = this.lastRet;
                this.lastRet = NONE;
                if (lastRet >= 0) {
                    if (!isDetached())
                        removeAt(lastRet);
                    else {
                        final E lastItem = this.lastItem;
                        // assert lastItem != null;
                        this.lastItem = null;
                        if (itemAt(lastRet) == lastItem)
                            removeAt(lastRet);
                    }
                } else if (lastRet == NONE)
                    throw new IllegalStateException();
                // else lastRet == REMOVED and the last returned element was
                // previously asynchronously removed via an operation other
                // than this.remove(), so nothing to do.

                if (cursor < 0 && nextIndex < 0)
                    detach();
            } finally {
                lock.unlock();
                // assert lastRet == NONE;
                // assert lastItem == null;
            }
        }

        /**
         * Called to notify the iterator that the queue is empty, or that it
         * has fallen hopelessly behind, so that it should abandon any
         * further iteration, except possibly to return one more element
         * from next(), as promised by returning true from hasNext().
         */
        void shutdown() {
            // assert lock.getHoldCount() == 1;
            cursor = NONE;
            if (nextIndex >= 0)
                nextIndex = REMOVED;
            if (lastRet >= 0) {
                lastRet = REMOVED;
                lastItem = null;
            }
            prevTakeIndex = DETACHED;
            // Don't set nextItem to null because we must continue to be
            // able to return it on next().
            //
            // Caller will unlink from itrs when convenient.
        }

        private int distance(int index, int prevTakeIndex, int length) {
            int distance = index - prevTakeIndex;
            if (distance < 0)
                distance += length;
            return distance;
        }

        /**
         * Called whenever an interior remove (not at takeIndex) occured.
         *
         * @return true if this iterator should be unlinked from itrs
         */
        boolean removedAt(int removedIndex) {
            // assert lock.getHoldCount() == 1;
            if (isDetached())
                return true;

            final int cycles = itrs.cycles;
            final int takeIndex = ArrayBlockingQueue.this.takeIndex;
            final int prevCycles = this.prevCycles;
            final int prevTakeIndex = this.prevTakeIndex;
            final int len = items.length;
            int cycleDiff = cycles - prevCycles;
            if (removedIndex < takeIndex)
                cycleDiff++;
            final int removedDistance =
                (cycleDiff * len) + (removedIndex - prevTakeIndex);
            // assert removedDistance >= 0;
            int cursor = this.cursor;
            if (cursor >= 0) {
                int x = distance(cursor, prevTakeIndex, len);
                if (x == removedDistance) {
                    if (cursor == putIndex)
                        this.cursor = cursor = NONE;
                }
                else if (x > removedDistance) {
                    // assert cursor != prevTakeIndex;
                    this.cursor = cursor = dec(cursor);
                }
            }
            int lastRet = this.lastRet;
            if (lastRet >= 0) {
                int x = distance(lastRet, prevTakeIndex, len);
                if (x == removedDistance)
                    this.lastRet = lastRet = REMOVED;
                else if (x > removedDistance)
                    this.lastRet = lastRet = dec(lastRet);
            }
            int nextIndex = this.nextIndex;
            if (nextIndex >= 0) {
                int x = distance(nextIndex, prevTakeIndex, len);
                if (x == removedDistance)
                    this.nextIndex = nextIndex = REMOVED;
                else if (x > removedDistance)
                    this.nextIndex = nextIndex = dec(nextIndex);
            }
            else if (cursor < 0 && nextIndex < 0 && lastRet < 0) {
                this.prevTakeIndex = DETACHED;
                return true;
            }
            return false;
        }

        /**
         * Called whenever takeIndex wraps around to zero.
         *
         * @return true if this iterator should be unlinked from itrs
         */
        boolean takeIndexWrapped() {
            // assert lock.getHoldCount() == 1;
            if (isDetached())
                return true;
            if (itrs.cycles - prevCycles > 1) {
                // All the elements that existed at the time of the last
                // operation are gone, so abandon further iteration.
                shutdown();
                return true;
            }
            return false;
        }

//         /** Uncomment for debugging. */
//         public String toString() {
//             return ("cursor=" + cursor + " " +
//                     "nextIndex=" + nextIndex + " " +
//                     "lastRet=" + lastRet + " " +
//                     "nextItem=" + nextItem + " " +
//                     "lastItem=" + lastItem + " " +
//                     "prevCycles=" + prevCycles + " " +
//                     "prevTakeIndex=" + prevTakeIndex + " " +
//                     "size()=" + size() + " " +
//                     "remainingCapacity()=" + remainingCapacity());
//         }
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BlockingDeque.java b/luni/src/main/java/java/util/concurrent/BlockingDeque.java
//Synthetic comment -- index 136df9c..34f103d 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -36,9 +36,9 @@
*  <tr>
*    <td><b>Insert</b></td>
*    <td>{@link #addFirst addFirst(e)}</td>
 *    <td>{@link #offerFirst(Object) offerFirst(e)}</td>
*    <td>{@link #putFirst putFirst(e)}</td>
 *    <td>{@link #offerFirst(Object, long, TimeUnit) offerFirst(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -67,9 +67,9 @@
*  <tr>
*    <td><b>Insert</b></td>
*    <td>{@link #addLast addLast(e)}</td>
 *    <td>{@link #offerLast(Object) offerLast(e)}</td>
*    <td>{@link #putLast putLast(e)}</td>
 *    <td>{@link #offerLast(Object, long, TimeUnit) offerLast(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -106,20 +106,20 @@
*    <td ALIGN=CENTER COLSPAN = 2> <b>Insert</b></td>
*  </tr>
*  <tr>
 *    <td>{@link #add(Object) add(e)}</td>
 *    <td>{@link #addLast(Object) addLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #offer(Object) offer(e)}</td>
 *    <td>{@link #offerLast(Object) offerLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #put(Object) put(e)}</td>
 *    <td>{@link #putLast(Object) putLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
 *    <td>{@link #offerLast(Object, long, TimeUnit) offerLast(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td ALIGN=CENTER COLSPAN = 2> <b>Remove</b></td>
//Synthetic comment -- @@ -181,7 +181,7 @@
* possible to do so immediately without violating capacity restrictions,
* throwing an <tt>IllegalStateException</tt> if no space is currently
* available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerFirst(Object) offerFirst}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -196,7 +196,7 @@
* possible to do so immediately without violating capacity restrictions,
* throwing an <tt>IllegalStateException</tt> if no space is currently
* available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerLast(Object) offerLast}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -212,7 +212,7 @@
* returning <tt>true</tt> upon success and <tt>false</tt> if no space is
* currently available.
* When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addFirst(Object) addFirst} method, which can
* fail to insert an element only by throwing an exception.
*
* @param e the element to add
//Synthetic comment -- @@ -228,7 +228,7 @@
* returning <tt>true</tt> upon success and <tt>false</tt> if no space is
* currently available.
* When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addLast(Object) addLast} method, which can
* fail to insert an element only by throwing an exception.
*
* @param e the element to add
//Synthetic comment -- @@ -371,8 +371,10 @@
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if an element was removed as a result of this call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
boolean removeFirstOccurrence(Object o);

//Synthetic comment -- @@ -387,8 +389,10 @@
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if an element was removed as a result of this call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
boolean removeLastOccurrence(Object o);

//Synthetic comment -- @@ -401,9 +405,9 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer(Object) offer}.
*
     * <p>This method is equivalent to {@link #addLast(Object) addLast}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -424,7 +428,7 @@
* generally preferable to the {@link #add} method, which can fail to
* insert an element only by throwing an exception.
*
     * <p>This method is equivalent to {@link #offerLast(Object) offerLast}.
*
* @param e the element to add
* @throws ClassCastException if the class of the specified element
//Synthetic comment -- @@ -440,7 +444,7 @@
* (in other words, at the tail of this deque), waiting if necessary for
* space to become available.
*
     * <p>This method is equivalent to {@link #putLast(Object) putLast}.
*
* @param e the element to add
* @throws InterruptedException {@inheritDoc}
//Synthetic comment -- @@ -458,7 +462,7 @@
* specified wait time if necessary for space to become available.
*
* <p>This method is equivalent to
     * {@link #offerLast(Object,long,TimeUnit) offerLast}.
*
* @param e the element to add
* @return <tt>true</tt> if the element was added to this deque, else
//Synthetic comment -- @@ -557,13 +561,15 @@
* (or equivalently, if this deque changed as a result of the call).
*
* <p>This method is equivalent to
     * {@link #removeFirstOccurrence(Object) removeFirstOccurrence}.
*
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if this deque changed as a result of the call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
boolean remove(Object o);

//Synthetic comment -- @@ -575,8 +581,10 @@
* @param o object to be checked for containment in this deque
* @return <tt>true</tt> if this deque contains the specified element
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
public boolean contains(Object o);

//Synthetic comment -- @@ -602,7 +610,7 @@
* words, inserts the element at the front of this deque unless it would
* violate capacity restrictions.
*
     * <p>This method is equivalent to {@link #addFirst(Object) addFirst}.
*
* @throws IllegalStateException {@inheritDoc}
* @throws ClassCastException {@inheritDoc}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BlockingQueue.java b/luni/src/main/java/java/util/concurrent/BlockingQueue.java
//Synthetic comment -- index d01c097..6cfe52b 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -42,7 +42,7 @@
*    <td>{@link #add add(e)}</td>
*    <td>{@link #offer offer(e)}</td>
*    <td>{@link #put put(e)}</td>
 *    <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -102,7 +102,7 @@
* Usage example, based on a typical producer-consumer scenario.
* Note that a <tt>BlockingQueue</tt> can safely be used with multiple
* producers and multiple consumers.
 *  <pre> {@code
* class Producer implements Runnable {
*   private final BlockingQueue queue;
*   Producer(BlockingQueue q) { queue = q; }
//Synthetic comment -- @@ -135,8 +135,7 @@
*     new Thread(c1).start();
*     new Thread(c2).start();
*   }
 * }}</pre>
*
* <p>Memory consistency effects: As with other concurrent
* collections, actions in a thread prior to placing an object into a
//Synthetic comment -- @@ -156,7 +155,7 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted queue, it is generally preferable to
     * use {@link #offer(Object) offer}.
*
* @param e the element to add
* @return <tt>true</tt> (as specified by {@link Collection#add})
//Synthetic comment -- @@ -274,8 +273,10 @@
* @param o element to be removed from this queue, if present
* @return <tt>true</tt> if this queue changed as a result of the call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
boolean remove(Object o);

//Synthetic comment -- @@ -287,8 +288,10 @@
* @param o object to be checked for containment in this queue
* @return <tt>true</tt> if this queue contains the specified element
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
public boolean contains(Object o);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BrokenBarrierException.java b/luni/src/main/java/java/util/concurrent/BrokenBarrierException.java
//Synthetic comment -- index 3f93fbb..76fae13 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Callable.java b/luni/src/main/java/java/util/concurrent/Callable.java
//Synthetic comment -- index abc4d04..293544b 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CancellationException.java b/luni/src/main/java/java/util/concurrent/CancellationException.java
//Synthetic comment -- index 2c29544..dc452e4 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CompletionService.java b/luni/src/main/java/java/util/concurrent/CompletionService.java
//Synthetic comment -- index df9f719..7b0931c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentHashMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentHashMap.java
//Synthetic comment -- index c142e2a..c85a5cc 100644

//Synthetic comment -- @@ -1,16 +1,13 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;
import java.io.Serializable;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -76,7 +73,25 @@

/*
* The basic strategy is to subdivide the table among Segments,
     * each of which itself is a concurrently readable hash table.  To
     * reduce footprint, all but one segments are constructed only
     * when first needed (see ensureSegment). To maintain visibility
     * in the presence of lazy construction, accesses to segments as
     * well as elements of segment's table must use volatile access,
     * which is done via Unsafe within methods segmentAt etc
     * below. These provide the functionality of AtomicReferenceArrays
     * but reduce the levels of indirection. Additionally,
     * volatile-writes of table elements and entry "next" fields
     * within locked operations use the cheaper "lazySet" forms of
     * writes (via putOrderedObject) because these writes are always
     * followed by lock releases that maintain sequential consistency
     * of table updates.
     *
     * Historical note: The previous version of this class relied
     * heavily on "final" fields, which avoided some volatile reads at
     * the expense of a large initial footprint.  Some remnants of
     * that design (including forced construction of segment 0) exist
     * to ensure serialization compatibility.
*/

/* ---------------- Constants -------------- */
//Synthetic comment -- @@ -108,8 +123,15 @@
static final int MAXIMUM_CAPACITY = 1 << 30;

/**
     * The minimum capacity for per-segment tables.  Must be a power
     * of two, at least two to avoid immediate resizing on next use
     * after lazy construction.
     */
    static final int MIN_SEGMENT_TABLE_CAPACITY = 2;

    /**
* The maximum number of segments to allow; used to bound
     * constructor arguments. Must be power of two less than 1 << 24.
*/
static final int MAX_SEGMENTS = 1 << 16; // slightly conservative

//Synthetic comment -- @@ -135,7 +157,7 @@
final int segmentShift;

/**
     * The segments, each of which is a specialized hash table.
*/
final Segment<K,V>[] segments;

//Synthetic comment -- @@ -143,7 +165,66 @@
transient Set<Map.Entry<K,V>> entrySet;
transient Collection<V> values;

    /**
     * ConcurrentHashMap list entry. Note that this is never exported
     * out as a user-visible Map.Entry.
     */
    static final class HashEntry<K,V> {
        final int hash;
        final K key;
        volatile V value;
        volatile HashEntry<K,V> next;

        HashEntry(int hash, K key, V value, HashEntry<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Sets next field with volatile write semantics.  (See above
         * about use of putOrderedObject.)
         */
        final void setNext(HashEntry<K,V> n) {
            UNSAFE.putOrderedObject(this, nextOffset, n);
        }

        // Unsafe mechanics
        static final sun.misc.Unsafe UNSAFE;
        static final long nextOffset;
        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = HashEntry.class;
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /**
     * Gets the ith element of given table (if nonnull) with volatile
     * read semantics. Note: This is manually integrated into a few
     * performance-sensitive methods to reduce call overhead.
     */
    @SuppressWarnings("unchecked")
    static final <K,V> HashEntry<K,V> entryAt(HashEntry<K,V>[] tab, int i) {
        return (tab == null) ? null :
            (HashEntry<K,V>) UNSAFE.getObjectVolatile
            (tab, ((long)i << TSHIFT) + TBASE);
    }

    /**
     * Sets the ith element of given table, with volatile write
     * semantics. (See above about use of putOrderedObject.)
     */
    static final <K,V> void setEntryAt(HashEntry<K,V>[] tab, int i,
                                       HashEntry<K,V> e) {
        UNSAFE.putOrderedObject(tab, ((long)i << TSHIFT) + TBASE, e);
    }

/**
* Applies a supplemental hash function to a given hashCode, which
//Synthetic comment -- @@ -164,104 +245,67 @@
}

/**
* Segments are specialized versions of hash tables.  This
* subclasses from ReentrantLock opportunistically, just to
* simplify some locking and avoid separate construction.
*/
static final class Segment<K,V> extends ReentrantLock implements Serializable {
/*
         * Segments maintain a table of entry lists that are always
         * kept in a consistent state, so can be read (via volatile
         * reads of segments and tables) without locking.  This
         * requires replicating nodes when necessary during table
         * resizing, so the old lists can be traversed by readers
         * still using old version of table.
*
         * This class defines only mutative methods requiring locking.
         * Except as noted, the methods of this class perform the
         * per-segment versions of ConcurrentHashMap methods.  (Other
         * methods are integrated directly into ConcurrentHashMap
         * methods.) These mutative methods use a form of controlled
         * spinning on contention via methods scanAndLock and
         * scanAndLockForPut. These intersperse tryLocks with
         * traversals to locate nodes.  The main benefit is to absorb
         * cache misses (which are very common for hash tables) while
         * obtaining locks so that traversal is faster once
         * acquired. We do not actually use the found nodes since they
         * must be re-acquired under lock anyway to ensure sequential
         * consistency of updates (and in any case may be undetectably
         * stale), but they will normally be much faster to re-locate.
         * Also, scanAndLockForPut speculatively creates a fresh node
         * to use in put if no node is found.
*/

private static final long serialVersionUID = 2249069246763182397L;

/**
         * The maximum number of times to tryLock in a prescan before
         * possibly blocking on acquire in preparation for a locked
         * segment operation. On multiprocessors, using a bounded
         * number of retries maintains cache acquired while locating
         * nodes.
*/
        static final int MAX_SCAN_RETRIES =
            Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;

/**
         * The per-segment table. Elements are accessed via
         * entryAt/setEntryAt providing volatile semantics.
         */
        transient volatile HashEntry<K,V>[] table;

        /**
         * The number of elements. Accessed only either within locks
         * or among other volatile reads that maintain visibility.
         */
        transient int count;

        /**
         * The total number of mutative operations in this segment.
         * Even though this may overflows 32 bits, it provides
         * sufficient accuracy for stability checks in CHM isEmpty()
         * and size() methods.  Accessed only either within locks or
         * among other volatile reads that maintain visibility.
*/
transient int modCount;

//Synthetic comment -- @@ -273,11 +317,6 @@
transient int threshold;

/**
* The load factor for the hash table.  Even though this value
* is same for all segments, it is replicated to avoid needing
* links to outer object.
//Synthetic comment -- @@ -285,202 +324,93 @@
*/
final float loadFactor;

        Segment(float lf, int threshold, HashEntry<K,V>[] tab) {
            this.loadFactor = lf;
            this.threshold = threshold;
            this.table = tab;
}

        final V put(K key, int hash, V value, boolean onlyIfAbsent) {
            HashEntry<K,V> node = tryLock() ? null :
                scanAndLockForPut(key, hash, value);
            V oldValue;
            try {
                HashEntry<K,V>[] tab = table;
                int index = (tab.length - 1) & hash;
                HashEntry<K,V> first = entryAt(tab, index);
                for (HashEntry<K,V> e = first;;) {
                    if (e != null) {
                        K k;
                        if ((k = e.key) == key ||
                            (e.hash == hash && key.equals(k))) {
                            oldValue = e.value;
                            if (!onlyIfAbsent) {
                                e.value = value;
                                ++modCount;
                            }
                            break;
                        }
                        e = e.next;
                    }
                    else {
                        if (node != null)
                            node.setNext(first);
                        else
                            node = new HashEntry<K,V>(hash, key, value, first);
                        int c = count + 1;
                        if (c > threshold && tab.length < MAXIMUM_CAPACITY)
                            rehash(node);
                        else
                            setEntryAt(tab, index, node);
                        ++modCount;
                        count = c;
                        oldValue = null;
                        break;
                    }
                }
            } finally {
                unlock();
            }
            return oldValue;
        }

        /**
         * Doubles size of table and repacks entries, also adding the
         * given node to new table
         */
@SuppressWarnings("unchecked")
        private void rehash(HashEntry<K,V> node) {
            /*
             * Reclassify nodes in each list to new table.  Because we
             * are using power-of-two expansion, the elements from
             * each bin must either stay at same index, or move with a
             * power of two offset. We eliminate unnecessary node
             * creation by catching cases where old nodes can be
             * reused because their next fields won't change.
             * Statistically, at the default threshold, only about
             * one-sixth of them need cloning when a table
             * doubles. The nodes they replace will be garbage
             * collectable as soon as they are no longer referenced by
             * any reader thread that may be in the midst of
             * concurrently traversing table. Entry accesses use plain
             * array indexing because they are followed by volatile
             * table write.
             */
HashEntry<K,V>[] oldTable = table;
int oldCapacity = oldTable.length;
            int newCapacity = oldCapacity << 1;
            threshold = (int)(newCapacity * loadFactor);
            HashEntry<K,V>[] newTable =
                (HashEntry<K,V>[]) new HashEntry<?,?>[newCapacity];
            int sizeMask = newCapacity - 1;
for (int i = 0; i < oldCapacity ; i++) {
HashEntry<K,V> e = oldTable[i];
if (e != null) {
HashEntry<K,V> next = e.next;
int idx = e.hash & sizeMask;
                    if (next == null)   //  Single node on list
newTable[idx] = e;
                    else { // Reuse consecutive sequence at same slot
HashEntry<K,V> lastRun = e;
int lastIdx = idx;
for (HashEntry<K,V> last = next;
//Synthetic comment -- @@ -493,74 +423,263 @@
}
}
newTable[lastIdx] = lastRun;
                        // Clone remaining nodes
for (HashEntry<K,V> p = e; p != lastRun; p = p.next) {
                            V v = p.value;
                            int h = p.hash;
                            int k = h & sizeMask;
HashEntry<K,V> n = newTable[k];
                            newTable[k] = new HashEntry<K,V>(h, p.key, v, n);
}
}
}
}
            int nodeIndex = node.hash & sizeMask; // add the new node
            node.setNext(newTable[nodeIndex]);
            newTable[nodeIndex] = node;
table = newTable;
}

/**
         * Scans for a node containing given key while trying to
         * acquire lock, creating and returning one if not found. Upon
         * return, guarantees that lock is held. Unlike in most
         * methods, calls to method equals are not screened: Since
         * traversal speed doesn't matter, we might as well help warm
         * up the associated code and accesses as well.
         *
         * @return a new node if key not found, else null
         */
        private HashEntry<K,V> scanAndLockForPut(K key, int hash, V value) {
            HashEntry<K,V> first = entryForHash(this, hash);
            HashEntry<K,V> e = first;
            HashEntry<K,V> node = null;
            int retries = -1; // negative while locating node
            while (!tryLock()) {
                HashEntry<K,V> f; // to recheck first below
                if (retries < 0) {
                    if (e == null) {
                        if (node == null) // speculatively create node
                            node = new HashEntry<K,V>(hash, key, value, null);
                        retries = 0;
                    }
                    else if (key.equals(e.key))
                        retries = 0;
                    else
                        e = e.next;
                }
                else if (++retries > MAX_SCAN_RETRIES) {
                    lock();
                    break;
                }
                else if ((retries & 1) == 0 &&
                         (f = entryForHash(this, hash)) != first) {
                    e = first = f; // re-traverse if entry changed
                    retries = -1;
                }
            }
            return node;
        }

        /**
         * Scans for a node containing the given key while trying to
         * acquire lock for a remove or replace operation. Upon
         * return, guarantees that lock is held.  Note that we must
         * lock even if the key is not found, to ensure sequential
         * consistency of updates.
         */
        private void scanAndLock(Object key, int hash) {
            // similar to but simpler than scanAndLockForPut
            HashEntry<K,V> first = entryForHash(this, hash);
            HashEntry<K,V> e = first;
            int retries = -1;
            while (!tryLock()) {
                HashEntry<K,V> f;
                if (retries < 0) {
                    if (e == null || key.equals(e.key))
                        retries = 0;
                    else
                        e = e.next;
                }
                else if (++retries > MAX_SCAN_RETRIES) {
                    lock();
                    break;
                }
                else if ((retries & 1) == 0 &&
                         (f = entryForHash(this, hash)) != first) {
                    e = first = f;
                    retries = -1;
                }
            }
        }

        /**
* Remove; match on key only if value null, else match both.
*/
        final V remove(Object key, int hash, Object value) {
            if (!tryLock())
                scanAndLock(key, hash);
            V oldValue = null;
try {
HashEntry<K,V>[] tab = table;
                int index = (tab.length - 1) & hash;
                HashEntry<K,V> e = entryAt(tab, index);
                HashEntry<K,V> pred = null;
                while (e != null) {
                    K k;
                    HashEntry<K,V> next = e.next;
                    if ((k = e.key) == key ||
                        (e.hash == hash && key.equals(k))) {
                        V v = e.value;
                        if (value == null || value == v || value.equals(v)) {
                            if (pred == null)
                                setEntryAt(tab, index, next);
                            else
                                pred.setNext(next);
                            ++modCount;
                            --count;
                            oldValue = v;
                        }
                        break;
                    }
                    pred = e;
                    e = next;
                }
            } finally {
                unlock();
            }
            return oldValue;
        }

        final boolean replace(K key, int hash, V oldValue, V newValue) {
            if (!tryLock())
                scanAndLock(key, hash);
            boolean replaced = false;
            try {
                HashEntry<K,V> e;
                for (e = entryForHash(this, hash); e != null; e = e.next) {
                    K k;
                    if ((k = e.key) == key ||
                        (e.hash == hash && key.equals(k))) {
                        if (oldValue.equals(e.value)) {
                            e.value = newValue;
                            ++modCount;
                            replaced = true;
                        }
                        break;
}
}
            } finally {
                unlock();
            }
            return replaced;
        }

        final V replace(K key, int hash, V value) {
            if (!tryLock())
                scanAndLock(key, hash);
            V oldValue = null;
            try {
                HashEntry<K,V> e;
                for (e = entryForHash(this, hash); e != null; e = e.next) {
                    K k;
                    if ((k = e.key) == key ||
                        (e.hash == hash && key.equals(k))) {
                        oldValue = e.value;
                        e.value = value;
                        ++modCount;
                        break;
                    }
                }
            } finally {
                unlock();
            }
            return oldValue;
        }

        final void clear() {
            lock();
            try {
                HashEntry<K,V>[] tab = table;
                for (int i = 0; i < tab.length ; i++)
                    setEntryAt(tab, i, null);
                ++modCount;
                count = 0;
} finally {
unlock();
}
}
    }

    // Accessing segments

    /**
     * Gets the jth element of given segment array (if nonnull) with
     * volatile element access semantics via Unsafe. (The null check
     * can trigger harmlessly only during deserialization.) Note:
     * because each element of segments array is set only once (using
     * fully ordered writes), some performance-sensitive methods rely
     * on this method only as a recheck upon null reads.
     */
    @SuppressWarnings("unchecked")
    static final <K,V> Segment<K,V> segmentAt(Segment<K,V>[] ss, int j) {
        long u = (j << SSHIFT) + SBASE;
        return ss == null ? null :
            (Segment<K,V>) UNSAFE.getObjectVolatile(ss, u);
    }

    /**
     * Returns the segment for the given index, creating it and
     * recording in segment table (via CAS) if not already present.
     *
     * @param k the index
     * @return the segment
     */
    @SuppressWarnings("unchecked")
    private Segment<K,V> ensureSegment(int k) {
        final Segment<K,V>[] ss = this.segments;
        long u = (k << SSHIFT) + SBASE; // raw offset
        Segment<K,V> seg;
        if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u)) == null) {
            Segment<K,V> proto = ss[0]; // use segment 0 as prototype
            int cap = proto.table.length;
            float lf = proto.loadFactor;
            int threshold = (int)(cap * lf);
            HashEntry<K,V>[] tab = (HashEntry<K,V>[])new HashEntry<?,?>[cap];
            if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
                == null) { // recheck
                Segment<K,V> s = new Segment<K,V>(lf, threshold, tab);
                while ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
                       == null) {
                    if (UNSAFE.compareAndSwapObject(ss, u, null, seg = s))
                        break;
}
}
}
        return seg;
}

    // Hash-based segment and entry accesses

    /**
     * Gets the segment for the given hash code.
     */
    @SuppressWarnings("unchecked")
    private Segment<K,V> segmentForHash(int h) {
        long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
        return (Segment<K,V>) UNSAFE.getObjectVolatile(segments, u);
    }

    /**
     * Gets the table entry for the given segment and hash code.
     */
    @SuppressWarnings("unchecked")
    static final <K,V> HashEntry<K,V> entryForHash(Segment<K,V> seg, int h) {
        HashEntry<K,V>[] tab;
        return (seg == null || (tab = seg.table) == null) ? null :
            (HashEntry<K,V>) UNSAFE.getObjectVolatile
            (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
    }

/* ---------------- Public operations -------------- */

//Synthetic comment -- @@ -580,14 +699,13 @@
* negative or the load factor or concurrencyLevel are
* nonpositive.
*/
    @SuppressWarnings("unchecked")
public ConcurrentHashMap(int initialCapacity,
float loadFactor, int concurrencyLevel) {
if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
throw new IllegalArgumentException();
if (concurrencyLevel > MAX_SEGMENTS)
concurrencyLevel = MAX_SEGMENTS;
// Find power-of-two sizes best matching arguments
int sshift = 0;
int ssize = 1;
//Synthetic comment -- @@ -595,21 +713,23 @@
++sshift;
ssize <<= 1;
}
        this.segmentShift = 32 - sshift;
        this.segmentMask = ssize - 1;
if (initialCapacity > MAXIMUM_CAPACITY)
initialCapacity = MAXIMUM_CAPACITY;
int c = initialCapacity / ssize;
if (c * ssize < initialCapacity)
++c;
        int cap = MIN_SEGMENT_TABLE_CAPACITY;
while (cap < c)
cap <<= 1;
        // create segments and segments[0]
        Segment<K,V> s0 =
            new Segment<K,V>(loadFactor, (int)(cap * loadFactor),
                             (HashEntry<K,V>[])new HashEntry<?,?>[cap]);
        Segment<K,V>[] ss = (Segment<K,V>[])new Segment<?,?>[ssize];
        UNSAFE.putOrderedObject(ss, SBASE, s0); // ordered write of segments[0]
        this.segments = ss;
}

/**
//Synthetic comment -- @@ -672,34 +792,37 @@
* @return <tt>true</tt> if this map contains no key-value mappings
*/
public boolean isEmpty() {
/*
         * Sum per-segment modCounts to avoid mis-reporting when
         * elements are concurrently added and removed in one segment
         * while checking another, in which case the table was never
         * actually empty at any point. (The sum ensures accuracy up
         * through at least 1<<31 per-segment modifications before
         * recheck.)  Methods size() and containsValue() use similar
         * constructions for stability checks.
*/
        long sum = 0L;
        final Segment<K,V>[] segments = this.segments;
        for (int j = 0; j < segments.length; ++j) {
            Segment<K,V> seg = segmentAt(segments, j);
            if (seg != null) {
                if (seg.count != 0)
return false;
                sum += seg.modCount;
}
}
        if (sum != 0L) { // recheck unless no modifications
            for (int j = 0; j < segments.length; ++j) {
                Segment<K,V> seg = segmentAt(segments, j);
                if (seg != null) {
                    if (seg.count != 0)
                        return false;
                    sum -= seg.modCount;
                }
            }
            if (sum != 0L)
                return false;
        }
return true;
}

//Synthetic comment -- @@ -711,45 +834,36 @@
* @return the number of key-value mappings in this map
*/
public int size() {
// Try a few times to get accurate count. On failure due to
// continuous async changes in table, resort to locking.
        final Segment<K,V>[] segments = this.segments;
        final int segmentCount = segments.length;

        long previousSum = 0L;
        for (int retries = -1; retries < RETRIES_BEFORE_LOCK; retries++) {
            long sum = 0L;    // sum of modCounts
            long size = 0L;
            for (int i = 0; i < segmentCount; i++) {
                Segment<K,V> segment = segmentAt(segments, i);
                if (segment != null) {
                    sum += segment.modCount;
                    size += segment.count;
}
}
            if (sum == previousSum)
                return ((size >>> 31) == 0) ? (int) size : Integer.MAX_VALUE;
            previousSum = sum;
}

        long size = 0L;
        for (int i = 0; i < segmentCount; i++) {
            Segment<K,V> segment = ensureSegment(i);
            segment.lock();
            size += segment.count;
}
        for (int i = 0; i < segmentCount; i++)
            segments[i].unlock();
        return ((size >>> 31) == 0) ? (int) size : Integer.MAX_VALUE;
}

/**
//Synthetic comment -- @@ -764,8 +878,21 @@
* @throws NullPointerException if the specified key is null
*/
public V get(Object key) {
        Segment<K,V> s; // manually integrate access methods to reduce overhead
        HashEntry<K,V>[] tab;
        int h = hash(key.hashCode());
        long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
        if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null &&
            (tab = s.table) != null) {
            for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
                     (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
                 e != null; e = e.next) {
                K k;
                if ((k = e.key) == key || (e.hash == h && key.equals(k)))
                    return e.value;
            }
        }
        return null;
}

/**
//Synthetic comment -- @@ -777,9 +904,23 @@
*         <tt>equals</tt> method; <tt>false</tt> otherwise.
* @throws NullPointerException if the specified key is null
*/
    @SuppressWarnings("unchecked")
public boolean containsKey(Object key) {
        Segment<K,V> s; // same as get() except no need for volatile value read
        HashEntry<K,V>[] tab;
        int h = hash(key.hashCode());
        long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
        if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null &&
            (tab = s.table) != null) {
            for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
                     (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
                 e != null; e = e.next) {
                K k;
                if ((k = e.key) == key || (e.hash == h && key.equals(k)))
                    return true;
            }
        }
        return false;
}

/**
//Synthetic comment -- @@ -794,53 +935,47 @@
* @throws NullPointerException if the specified value is null
*/
public boolean containsValue(Object value) {
        // Same idea as size()
if (value == null)
throw new NullPointerException();
final Segment<K,V>[] segments = this.segments;
        long previousSum = 0L;
        int lockCount = 0;
        try {
            for (int retries = -1; ; retries++) {
                long sum = 0L;    // sum of modCounts
                for (int j = 0; j < segments.length; j++) {
                    Segment<K,V> segment;
                    if (retries == RETRIES_BEFORE_LOCK) {
                        segment = ensureSegment(j);
                        segment.lock();
                        lockCount++;
                    } else {
                        segment = segmentAt(segments, j);
                        if (segment == null)
                            continue;
                    }
                    HashEntry<K,V>[] tab = segment.table;
                    if (tab != null) {
                        for (int i = 0 ; i < tab.length; i++) {
                            HashEntry<K,V> e;
                            for (e = entryAt(tab, i); e != null; e = e.next) {
                                V v = e.value;
                                if (v != null && value.equals(v))
                                    return true;
                            }
                        }
                        sum += segment.modCount;
}
}
                if ((retries >= 0 && sum == previousSum) || lockCount > 0)
                    return false;
                previousSum = sum;
}
} finally {
            for (int j = 0; j < lockCount; j++)
                segments[j].unlock();
}
}

/**
//Synthetic comment -- @@ -850,7 +985,7 @@
* full compatibility with class {@link java.util.Hashtable},
* which supported this method prior to introduction of the
* Java Collections framework.
     *
* @param  value a value to search for
* @return <tt>true</tt> if and only if some key maps to the
*         <tt>value</tt> argument in this table as
//Synthetic comment -- @@ -875,11 +1010,17 @@
*         <tt>null</tt> if there was no mapping for <tt>key</tt>
* @throws NullPointerException if the specified key or value is null
*/
    @SuppressWarnings("unchecked")
public V put(K key, V value) {
        Segment<K,V> s;
if (value == null)
throw new NullPointerException();
int hash = hash(key.hashCode());
        int j = (hash >>> segmentShift) & segmentMask;
        if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
             (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
            s = ensureSegment(j);
        return s.put(key, hash, value, false);
}

/**
//Synthetic comment -- @@ -889,11 +1030,17 @@
*         or <tt>null</tt> if there was no mapping for the key
* @throws NullPointerException if the specified key or value is null
*/
    @SuppressWarnings("unchecked")
public V putIfAbsent(K key, V value) {
        Segment<K,V> s;
if (value == null)
throw new NullPointerException();
int hash = hash(key.hashCode());
        int j = (hash >>> segmentShift) & segmentMask;
        if ((s = (Segment<K,V>)UNSAFE.getObject
             (segments, (j << SSHIFT) + SBASE)) == null)
            s = ensureSegment(j);
        return s.put(key, hash, value, true);
}

/**
//Synthetic comment -- @@ -919,7 +1066,8 @@
*/
public V remove(Object key) {
int hash = hash(key.hashCode());
        Segment<K,V> s = segmentForHash(hash);
        return s == null ? null : s.remove(key, hash, null);
}

/**
//Synthetic comment -- @@ -929,9 +1077,9 @@
*/
public boolean remove(Object key, Object value) {
int hash = hash(key.hashCode());
        Segment<K,V> s;
        return value != null && (s = segmentForHash(hash)) != null &&
            s.remove(key, hash, value) != null;
}

/**
//Synthetic comment -- @@ -940,10 +1088,11 @@
* @throws NullPointerException if any of the arguments are null
*/
public boolean replace(K key, V oldValue, V newValue) {
        int hash = hash(key.hashCode());
if (oldValue == null || newValue == null)
throw new NullPointerException();
        Segment<K,V> s = segmentForHash(hash);
        return s != null && s.replace(key, hash, oldValue, newValue);
}

/**
//Synthetic comment -- @@ -954,18 +1103,23 @@
* @throws NullPointerException if the specified key or value is null
*/
public V replace(K key, V value) {
        int hash = hash(key.hashCode());
if (value == null)
throw new NullPointerException();
        Segment<K,V> s = segmentForHash(hash);
        return s == null ? null : s.replace(key, hash, value);
}

/**
* Removes all of the mappings from this map.
*/
public void clear() {
        final Segment<K,V>[] segments = this.segments;
        for (int j = 0; j < segments.length; ++j) {
            Segment<K,V> s = segmentAt(segments, j);
            if (s != null)
                s.clear();
        }
}

/**
//Synthetic comment -- @@ -1066,42 +1220,41 @@
advance();
}

        /**
         * Sets nextEntry to first node of next non-empty table
         * (in backwards order, to simplify checks).
         */
final void advance() {
            for (;;) {
                if (nextTableIndex >= 0) {
                    if ((nextEntry = entryAt(currentTable,
                                             nextTableIndex--)) != null)
                        break;
}
                else if (nextSegmentIndex >= 0) {
                    Segment<K,V> seg = segmentAt(segments, nextSegmentIndex--);
                    if (seg != null && (currentTable = seg.table) != null)
                        nextTableIndex = currentTable.length - 1;
                }
                else
                    break;
}
}

        final HashEntry<K,V> nextEntry() {
            HashEntry<K,V> e = nextEntry;
            if (e == null)
throw new NoSuchElementException();
            lastReturned = e; // cannot assign until after null check
            if ((nextEntry = e.next) == null)
                advance();
            return e;
}

        public final boolean hasNext() { return nextEntry != null; }
        public final boolean hasMoreElements() { return nextEntry != null; }

        public final void remove() {
if (lastReturned == null)
throw new IllegalStateException();
ConcurrentHashMap.this.remove(lastReturned.key);
//Synthetic comment -- @@ -1113,16 +1266,16 @@
extends HashIterator
implements Iterator<K>, Enumeration<K>
{
        public final K next()        { return super.nextEntry().key; }
        public final K nextElement() { return super.nextEntry().key; }
}

final class ValueIterator
extends HashIterator
implements Iterator<V>, Enumeration<V>
{
        public final V next()        { return super.nextEntry().value; }
        public final V nextElement() { return super.nextEntry().value; }
}

/**
//Synthetic comment -- @@ -1137,7 +1290,7 @@
}

/**
         * Sets our entry's value and writes through to the map. The
* value to return is somewhat arbitrary here. Since a
* WriteThroughEntry does not necessarily track asynchronous
* changes, the most recent "previous" value could be
//Synthetic comment -- @@ -1233,24 +1386,30 @@
/* ---------------- Serialization Support -------------- */

/**
     * Saves the state of the <tt>ConcurrentHashMap</tt> instance to a
     * stream (i.e., serializes it).
* @param s the stream
* @serialData
* the key (Object) and value (Object)
* for each key-value mapping, followed by a null pair.
* The key-value mappings are emitted in no particular order.
*/
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // force all segments for serialization compatibility
        for (int k = 0; k < segments.length; ++k)
            ensureSegment(k);
s.defaultWriteObject();

        final Segment<K,V>[] segments = this.segments;
for (int k = 0; k < segments.length; ++k) {
            Segment<K,V> seg = segmentAt(segments, k);
seg.lock();
try {
HashEntry<K,V>[] tab = seg.table;
for (int i = 0; i < tab.length; ++i) {
                    HashEntry<K,V> e;
                    for (e = entryAt(tab, i); e != null; e = e.next) {
s.writeObject(e.key);
s.writeObject(e.value);
}
//Synthetic comment -- @@ -1264,17 +1423,24 @@
}

/**
     * Reconstitutes the <tt>ConcurrentHashMap</tt> instance from a
     * stream (i.e., deserializes it).
* @param s the stream
*/
    @SuppressWarnings("unchecked")
private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
s.defaultReadObject();

        // Re-initialize segments to be minimally sized, and let grow.
        int cap = MIN_SEGMENT_TABLE_CAPACITY;
        final Segment<K,V>[] segments = this.segments;
        for (int k = 0; k < segments.length; ++k) {
            Segment<K,V> seg = segments[k];
            if (seg != null) {
                seg.threshold = (int)(cap * seg.loadFactor);
                seg.table = (HashEntry<K,V>[]) new HashEntry<?,?>[cap];
            }
}

// Read the keys and values, and put the mappings in the table
//Synthetic comment -- @@ -1286,4 +1452,31 @@
put(key, value);
}
}

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long SBASE;
    private static final int SSHIFT;
    private static final long TBASE;
    private static final int TSHIFT;

    static {
        int ss, ts;
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> tc = HashEntry[].class;
            Class<?> sc = Segment[].class;
            TBASE = UNSAFE.arrayBaseOffset(tc);
            SBASE = UNSAFE.arrayBaseOffset(sc);
            ts = UNSAFE.arrayIndexScale(tc);
            ss = UNSAFE.arrayIndexScale(sc);
        } catch (Exception e) {
            throw new Error(e);
        }
        if ((ss & (ss-1)) != 0 || (ts & (ts-1)) != 0)
            throw new Error("data type scale not a power of two");
        SSHIFT = 31 - Integer.numberOfLeadingZeros(ss);
        TSHIFT = 31 - Integer.numberOfLeadingZeros(ts);
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentLinkedDeque.java b/luni/src/main/java/java/util/concurrent/ConcurrentLinkedDeque.java
//Synthetic comment -- index 72c2e08..30adb36 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea and Martin Buchholz with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -34,10 +34,17 @@
* ConcurrentModificationException}, and may proceed concurrently with
* other operations.
*
 * <p>Beware that, unlike in most collections, the {@code size} method
 * is <em>NOT</em> a constant-time operation. Because of the
* asynchronous nature of these deques, determining the current number
 * of elements requires a traversal of the elements, and so may report
 * inaccurate results if this collection is modified during traversal.
 * Additionally, the bulk operations {@code addAll},
 * {@code removeAll}, {@code retainAll}, {@code containsAll},
 * {@code equals}, and {@code toArray} are <em>not</em> guaranteed
 * to be performed atomically. For example, an iterator operating
 * concurrently with an {@code addAll} operation might view only some
 * of the added elements.
*
* <p>This class and its iterator implement all of the <em>optional</em>
* methods of the {@link Deque} and {@link Iterator} interfaces.
//Synthetic comment -- @@ -245,13 +252,6 @@

private static final Node<Object> PREV_TERMINATOR, NEXT_TERMINATOR;

@SuppressWarnings("unchecked")
Node<E> prevTerminator() {
return (Node<E>) PREV_TERMINATOR;
//Synthetic comment -- @@ -267,6 +267,9 @@
volatile E item;
volatile Node<E> next;

        Node() {  // default constructor for NEXT_TERMINATOR, PREV_TERMINATOR
        }

/**
* Constructs a new node.  Uses relaxed write because item can
* only be seen after publication via casNext or casPrev.
//Synthetic comment -- @@ -297,14 +300,25 @@

// Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE;
        private static final long prevOffset;
        private static final long itemOffset;
        private static final long nextOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                prevOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("prev"));
                itemOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/**
//Synthetic comment -- @@ -1209,8 +1223,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -1395,14 +1408,6 @@
initHeadTail(h, t);
}


private boolean casHead(Node<E> cmp, Node<E> val) {
return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
//Synthetic comment -- @@ -1412,15 +1417,25 @@
return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
}

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    static {
        PREV_TERMINATOR = new Node<Object>();
        PREV_TERMINATOR.next = PREV_TERMINATOR;
        NEXT_TERMINATOR = new Node<Object>();
        NEXT_TERMINATOR.prev = NEXT_TERMINATOR;
try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentLinkedDeque.class;
            headOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentLinkedQueue.java b/luni/src/main/java/java/util/concurrent/ConcurrentLinkedQueue.java
//Synthetic comment -- index 3ed0a7c..a0a26fd 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea and Martin Buchholz with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -47,7 +47,14 @@
* <p>Beware that, unlike in most collections, the {@code size} method
* is <em>NOT</em> a constant-time operation. Because of the
* asynchronous nature of these queues, determining the current number
 * of elements requires a traversal of the elements, and so may report
 * inaccurate results if this collection is modified during traversal.
 * Additionally, the bulk operations {@code addAll},
 * {@code removeAll}, {@code retainAll}, {@code containsAll},
 * {@code equals}, and {@code toArray} are <em>not</em> guaranteed
 * to be performed atomically. For example, an iterator operating
 * concurrently with an {@code addAll} operation might view only some
 * of the added elements.
*
* <p>This class and its iterator implement all of the <em>optional</em>
* methods of the {@link Queue} and {@link Iterator} interfaces.
//Synthetic comment -- @@ -165,12 +172,22 @@

// Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                itemOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/**
//Synthetic comment -- @@ -563,8 +580,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -761,14 +777,6 @@
throw new NullPointerException();
}

private boolean casTail(Node<E> cmp, Node<E> val) {
return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
}
//Synthetic comment -- @@ -777,15 +785,21 @@
return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
}

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    static {
try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentLinkedQueue.class;
            headOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentMap.java
//Synthetic comment -- index 2daebc5..3405acf 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -32,11 +32,12 @@
* If the specified key is not already associated
* with a value, associate it with the given value.
* This is equivalent to
     *  <pre> {@code
     * if (!map.containsKey(key))
     *   return map.put(key, value);
     * else
     *   return map.get(key);}</pre>
     *
* except that the action is performed atomically.
*
* @param key key with which the specified value is to be associated
//Synthetic comment -- @@ -61,11 +62,13 @@
/**
* Removes the entry for a key only if currently mapped to a given value.
* This is equivalent to
     *  <pre> {@code
     * if (map.containsKey(key) && map.get(key).equals(value)) {
     *   map.remove(key);
     *   return true;
     * } else
     *   return false;}</pre>
     *
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated
//Synthetic comment -- @@ -74,20 +77,24 @@
* @throws UnsupportedOperationException if the <tt>remove</tt> operation
*         is not supported by this map
* @throws ClassCastException if the key or value is of an inappropriate
     *         type for this map
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
* @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values
     *         (<a href="../Collection.html#optional-restrictions">optional</a>)
*/
boolean remove(Object key, Object value);

/**
* Replaces the entry for a key only if currently mapped to a given value.
* This is equivalent to
     *  <pre> {@code
     * if (map.containsKey(key) && map.get(key).equals(oldValue)) {
     *   map.put(key, newValue);
     *   return true;
     * } else
     *   return false;}</pre>
     *
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated
//Synthetic comment -- @@ -108,10 +115,12 @@
/**
* Replaces the entry for a key only if currently mapped to some value.
* This is equivalent to
     *  <pre> {@code
     * if (map.containsKey(key)) {
     *   return map.put(key, value);
     * } else
     *   return null;}</pre>
     *
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentNavigableMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentNavigableMap.java
//Synthetic comment -- index 7d86afb..ea99886 100644

//Synthetic comment -- @@ -1,20 +1,20 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.*;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
* A {@link ConcurrentMap} supporting {@link NavigableMap} operations,
* and recursively so for its navigable sub-maps.
*
* @author Doug Lea
* @param <K> the type of keys maintained by this map
* @param <V> the type of mapped values








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentSkipListMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentSkipListMap.java
//Synthetic comment -- index fbd1083..d0d6f14 100644

//Synthetic comment -- @@ -1,12 +1,15 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.*;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
* A scalable concurrent {@link ConcurrentNavigableMap} implementation.
//Synthetic comment -- @@ -15,8 +18,8 @@
* creation time, depending on which constructor is used.
*
* <p>This class implements a concurrent variant of <a
 * href="http://en.wikipedia.org/wiki/Skip_list" target="_top">SkipLists</a>
 * providing expected average <i>log(n)</i> time cost for the
* <tt>containsKey</tt>, <tt>get</tt>, <tt>put</tt> and
* <tt>remove</tt> operations and their variants.  Insertion, removal,
* update, and access operations safely execute concurrently by
//Synthetic comment -- @@ -37,12 +40,13 @@
* <p>Beware that, unlike in most collections, the <tt>size</tt>
* method is <em>not</em> a constant-time operation. Because of the
* asynchronous nature of these maps, determining the current number
 * of elements requires a traversal of the elements, and so may report
 * inaccurate results if this collection is modified during traversal.
 * Additionally, the bulk operations <tt>putAll</tt>, <tt>equals</tt>,
 * <tt>toArray</tt>, <tt>containsValue</tt>, and <tt>clear</tt> are
 * <em>not</em> guaranteed to be performed atomically. For example, an
 * iterator operating concurrently with a <tt>putAll</tt> operation
 * might view only some of the added elements.
*
* <p>This class and its views and iterators implement all of the
* <em>optional</em> methods of the {@link Map} and {@link Iterator}
//Synthetic comment -- @@ -51,10 +55,6 @@
* null return values cannot be reliably distinguished from the absence of
* elements.
*
* @author Doug Lea
* @param <K> the type of keys maintained by this map
* @param <V> the type of mapped values
//Synthetic comment -- @@ -322,11 +322,11 @@
private transient int randomSeed;

/** Lazily initialized key set */
    private transient KeySet<K> keySet;
/** Lazily initialized entry set */
    private transient EntrySet<K,V> entrySet;
/** Lazily initialized values collection */
    private transient Values<V> values;
/** Lazily initialized descending key set */
private transient ConcurrentNavigableMap<K,V> descendingMap;

//Synthetic comment -- @@ -478,13 +478,24 @@
return new AbstractMap.SimpleImmutableEntry<K,V>(key, v);
}

        // UNSAFE mechanics

        private static final sun.misc.Unsafe UNSAFE;
        private static final long valueOffset;
        private static final long nextOffset;

        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                valueOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("value"));
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/* ---------------- Indexing -------------- */
//Synthetic comment -- @@ -551,10 +562,18 @@
}

// Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long rightOffset;
        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Index.class;
                rightOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("right"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/* ---------------- Head nodes -------------- */
//Synthetic comment -- @@ -884,7 +903,7 @@
* direction.
*/
level = max + 1;
            Index<K,V>[] idxs = (Index<K,V>[])new Index<?,?>[level+1];
Index<K,V> idx = null;
for (int i = 1; i <= level; ++i)
idxs[i] = idx = new Index<K,V>(z, idx, null);
//Synthetic comment -- @@ -1387,16 +1406,16 @@
* @return a shallow copy of this map
*/
public ConcurrentSkipListMap<K,V> clone() {
try {
            @SuppressWarnings("unchecked")
            ConcurrentSkipListMap<K,V> clone =
                (ConcurrentSkipListMap<K,V>) super.clone();
            clone.initialize();
            clone.buildFromSorted(this);
            return clone;
} catch (CloneNotSupportedException e) {
throw new InternalError();
}
}

/**
//Synthetic comment -- @@ -1458,7 +1477,7 @@
/* ---------------- Serialization -------------- */

/**
     * Saves the state of this map to a stream (that is, serializes it).
*
* @serialData The key (Object) and value (Object) for each
* key-value mapping represented by the map, followed by
//Synthetic comment -- @@ -1483,7 +1502,9 @@
}

/**
     * Reconstitutes the map from a stream (that is, deserializes it).
     *
     * @param s the stream
*/
private void readObject(final java.io.ObjectInputStream s)
throws java.io.IOException, ClassNotFoundException {
//Synthetic comment -- @@ -1613,7 +1634,9 @@
/**
* Returns <tt>true</tt> if this map maps one or more keys to the
* specified value.  This operation requires time linear in the
     * map size. Additionally, it is possible for the map to change
     * during execution of this method, in which case the returned
     * result may be inaccurate.
*
* @param value value whose presence in this map is to be tested
* @return <tt>true</tt> if a mapping to <tt>value</tt> exists;
//Synthetic comment -- @@ -1703,14 +1726,14 @@
*
* @return a navigable set view of the keys in this map
*/
    public NavigableSet<K> keySet() {
        KeySet<K> ks = keySet;
        return (ks != null) ? ks : (keySet = new KeySet<K>(this));
}

public NavigableSet<K> navigableKeySet() {
        KeySet<K> ks = keySet;
        return (ks != null) ? ks : (keySet = new KeySet<K>(this));
}

/**
//Synthetic comment -- @@ -1732,8 +1755,8 @@
* reflect any modifications subsequent to construction.
*/
public Collection<V> values() {
        Values<V> vs = values;
        return (vs != null) ? vs : (values = new Values<V>(this));
}

/**
//Synthetic comment -- @@ -1761,8 +1784,8 @@
*         sorted in ascending key order
*/
public Set<Map.Entry<K,V>> entrySet() {
        EntrySet<K,V> es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet<K,V>(this));
}

public ConcurrentNavigableMap<K,V> descendingMap() {
//Synthetic comment -- @@ -2253,8 +2276,8 @@

static final class KeySet<E>
extends AbstractSet<E> implements NavigableSet<E> {
        private final ConcurrentNavigableMap<E,?> m;
        KeySet(ConcurrentNavigableMap<E,?> map) { m = map; }
public int size() { return m.size(); }
public boolean isEmpty() { return m.isEmpty(); }
public boolean contains(Object o) { return m.containsKey(o); }
//Synthetic comment -- @@ -2268,11 +2291,11 @@
public E first() { return m.firstKey(); }
public E last() { return m.lastKey(); }
public E pollFirst() {
            Map.Entry<E,?> e = m.pollFirstEntry();
return (e == null) ? null : e.getKey();
}
public E pollLast() {
            Map.Entry<E,?> e = m.pollLastEntry();
return (e == null) ? null : e.getKey();
}
public Iterator<E> iterator() {
//Synthetic comment -- @@ -2323,20 +2346,20 @@
return tailSet(fromElement, true);
}
public NavigableSet<E> descendingSet() {
            return new KeySet<E>(m.descendingMap());
}
}

static final class Values<E> extends AbstractCollection<E> {
        private final ConcurrentNavigableMap<?, E> m;
        Values(ConcurrentNavigableMap<?, E> map) {
m = map;
}
public Iterator<E> iterator() {
if (m instanceof ConcurrentSkipListMap)
                return ((ConcurrentSkipListMap<?,E>)m).valueIterator();
else
                return ((SubMap<?,E>)m).valueIterator();
}
public boolean isEmpty() {
return m.isEmpty();
//Synthetic comment -- @@ -2370,14 +2393,14 @@
public boolean contains(Object o) {
if (!(o instanceof Map.Entry))
return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
V1 v = m.get(e.getKey());
return v != null && v.equals(e.getValue());
}
public boolean remove(Object o) {
if (!(o instanceof Map.Entry))
return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
return m.remove(e.getKey(),
e.getValue());
}
//Synthetic comment -- @@ -2517,9 +2540,9 @@
if (lo == null)
return m.findFirst();
else if (loInclusive)
                return m.findNear(lo, GT|EQ);
else
                return m.findNear(lo, GT);
}

/**
//Synthetic comment -- @@ -2530,9 +2553,9 @@
if (hi == null)
return m.findLast();
else if (hiInclusive)
                return m.findNear(hi, LT|EQ);
else
                return m.findNear(hi, LT);
}

/**
//Synthetic comment -- @@ -2614,15 +2637,15 @@
*/
private Map.Entry<K,V> getNearEntry(K key, int rel) {
if (isDescending) { // adjust relation for direction
                if ((rel & LT) == 0)
                    rel |= LT;
else
                    rel &= ~LT;
}
if (tooLow(key))
                return ((rel & LT) != 0) ? null : lowestEntry();
if (tooHigh(key))
                return ((rel & LT) != 0) ? highestEntry() : null;
for (;;) {
Node<K,V> n = m.findNear(key, rel);
if (n == null || !inBounds(n.key))
//Synthetic comment -- @@ -2637,13 +2660,13 @@
// Almost the same as getNearEntry, except for keys
private K getNearKey(K key, int rel) {
if (isDescending) { // adjust relation for direction
                if ((rel & LT) == 0)
                    rel |= LT;
else
                    rel &= ~LT;
}
if (tooLow(key)) {
                if ((rel & LT) == 0) {
ConcurrentSkipListMap.Node<K,V> n = loNode();
if (isBeforeEnd(n))
return n.key;
//Synthetic comment -- @@ -2651,7 +2674,7 @@
return null;
}
if (tooHigh(key)) {
                if ((rel & LT) != 0) {
ConcurrentSkipListMap.Node<K,V> n = hiNode();
if (n != null) {
K last = n.key;
//Synthetic comment -- @@ -2683,7 +2706,7 @@
public V get(Object key) {
if (key == null) throw new NullPointerException();
K k = (K)key;
            return (!inBounds(k)) ? null : m.get(k);
}

public V put(K key, V value) {
//Synthetic comment -- @@ -2850,35 +2873,35 @@
/* ----------------  Relational methods -------------- */

public Map.Entry<K,V> ceilingEntry(K key) {
            return getNearEntry(key, GT|EQ);
}

public K ceilingKey(K key) {
            return getNearKey(key, GT|EQ);
}

public Map.Entry<K,V> lowerEntry(K key) {
            return getNearEntry(key, LT);
}

public K lowerKey(K key) {
            return getNearKey(key, LT);
}

public Map.Entry<K,V> floorEntry(K key) {
            return getNearEntry(key, LT|EQ);
}

public K floorKey(K key) {
            return getNearKey(key, LT|EQ);
}

public Map.Entry<K,V> higherEntry(K key) {
            return getNearEntry(key, GT);
}

public K higherKey(K key) {
            return getNearKey(key, GT);
}

public K firstKey() {
//Synthetic comment -- @@ -2909,22 +2932,22 @@

public NavigableSet<K> keySet() {
KeySet<K> ks = keySetView;
            return (ks != null) ? ks : (keySetView = new KeySet<K>(this));
}

public NavigableSet<K> navigableKeySet() {
KeySet<K> ks = keySetView;
            return (ks != null) ? ks : (keySetView = new KeySet<K>(this));
}

public Collection<V> values() {
Collection<V> vs = valuesView;
            return (vs != null) ? vs : (valuesView = new Values<V>(this));
}

public Set<Map.Entry<K,V>> entrySet() {
Set<Map.Entry<K,V>> es = entrySetView;
            return (es != null) ? es : (entrySetView = new EntrySet<K,V>(this));
}

public NavigableSet<K> descendingKeySet() {
//Synthetic comment -- @@ -3053,20 +3076,16 @@
}

// Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    static {
try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentSkipListMap.class;
            headOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("head"));
        } catch (Exception e) {
            throw new Error(e);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentSkipListSet.java b/luni/src/main/java/java/util/concurrent/ConcurrentSkipListSet.java
//Synthetic comment -- index d24876f..71431a9 100644

//Synthetic comment -- @@ -1,12 +1,15 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.*;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
* A scalable concurrent {@link NavigableSet} implementation based on
//Synthetic comment -- @@ -29,12 +32,14 @@
* <p>Beware that, unlike in most collections, the <tt>size</tt>
* method is <em>not</em> a constant-time operation. Because of the
* asynchronous nature of these sets, determining the current number
 * of elements requires a traversal of the elements, and so may report
 * inaccurate results if this collection is modified during traversal.
 * Additionally, the bulk operations <tt>addAll</tt>,
 * <tt>removeAll</tt>, <tt>retainAll</tt>, <tt>containsAll</tt>,
 * <tt>equals</tt>, and <tt>toArray</tt> are <em>not</em> guaranteed
 * to be performed atomically. For example, an iterator operating
 * concurrently with an <tt>addAll</tt> operation might view only some
 * of the added elements.
*
* <p>This class and its iterators implement all of the
* <em>optional</em> methods of the {@link Set} and {@link Iterator}
//Synthetic comment -- @@ -43,10 +48,6 @@
* because <tt>null</tt> arguments and return values cannot be reliably
* distinguished from the absence of elements.
*
* @author Doug Lea
* @param <E> the type of elements maintained by this set
* @since 1.6
//Synthetic comment -- @@ -127,15 +128,15 @@
* @return a shallow copy of this set
*/
public ConcurrentSkipListSet<E> clone() {
try {
            @SuppressWarnings("unchecked")
            ConcurrentSkipListSet<E> clone =
                (ConcurrentSkipListSet<E>) super.clone();
            clone.setMap(new ConcurrentSkipListMap<E,Object>(m));
            return clone;
} catch (CloneNotSupportedException e) {
throw new InternalError();
}
}

/* ---------------- Set operations -------------- */
//Synthetic comment -- @@ -291,8 +292,8 @@
public boolean removeAll(Collection<?> c) {
// Override AbstractSet version to avoid unnecessary call to size()
boolean modified = false;
        for (Object e : c)
            if (remove(e))
modified = true;
return modified;
}
//Synthetic comment -- @@ -437,20 +438,24 @@
* @return a reverse order view of this set
*/
public NavigableSet<E> descendingSet() {
        return new ConcurrentSkipListSet<E>(m.descendingMap());
}

// Support for resetting map in clone
    private void setMap(ConcurrentNavigableMap<E,Object> map) {
        UNSAFE.putObjectVolatile(this, mapOffset, map);
    }

    private static final sun.misc.Unsafe UNSAFE;
private static final long mapOffset;
static {
try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentSkipListSet.class;
            mapOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("m"));
        } catch (Exception e) {
            throw new Error(e);
        }
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CopyOnWriteArraySet.java b/luni/src/main/java/java/util/concurrent/CopyOnWriteArraySet.java
//Synthetic comment -- index 87419fc..1f37bc9 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -160,8 +160,7 @@
* The following code can be used to dump the set into a newly allocated
* array of <tt>String</tt>:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -358,6 +357,6 @@
* Test for equality, coping with nulls.
*/
private static boolean eq(Object o1, Object o2) {
        return (o1 == null) ? o2 == null : o1.equals(o2);
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CountDownLatch.java b/luni/src/main/java/java/util/concurrent/CountDownLatch.java
//Synthetic comment -- index 1888279..e90badf 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;

/**
* A synchronization aid that allows one or more threads to wait until
//Synthetic comment -- @@ -44,7 +43,7 @@
* until all workers have completed.
* </ul>
*
 *  <pre> {@code
* class Driver { // ...
*   void main() throws InterruptedException {
*     CountDownLatch startSignal = new CountDownLatch(1);
//Synthetic comment -- @@ -76,9 +75,7 @@
*   }
*
*   void doWork() { ... }
 * }}</pre>
*
* <p>Another typical usage would be to divide a problem into N parts,
* describe each part with a Runnable that executes that portion and
//Synthetic comment -- @@ -87,7 +84,7 @@
* will be able to pass through await. (When threads must repeatedly
* count down in this way, instead use a {@link CyclicBarrier}.)
*
 *  <pre> {@code
* class Driver2 { // ...
*   void main() throws InterruptedException {
*     CountDownLatch doneSignal = new CountDownLatch(N);
//Synthetic comment -- @@ -115,9 +112,7 @@
*   }
*
*   void doWork() { ... }
 * }}</pre>
*
* <p>Memory consistency effects: Until the count reaches
* zero, actions in a thread prior to calling








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CyclicBarrier.java b/luni/src/main/java/java/util/concurrent/CyclicBarrier.java
//Synthetic comment -- index d5738c5..cf0b46e 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,7 +23,8 @@
*
* <p><b>Sample usage:</b> Here is an example of
*  using a barrier in a parallel decomposition design:
 *
 *  <pre> {@code
* class Solver {
*   final int N;
*   final float[][] data;
//Synthetic comment -- @@ -61,8 +62,8 @@
*
*     waitUntilDone();
*   }
 * }}</pre>
 *
* Here, each worker thread processes a row of the matrix then waits at the
* barrier until all rows have been processed. When all rows are processed
* the supplied {@link Runnable} barrier action is executed and merges the
//Synthetic comment -- @@ -76,9 +77,10 @@
* {@link #await} returns the arrival index of that thread at the barrier.
* You can then choose which thread should execute the barrier action, for
* example:
 *  <pre> {@code
 * if (barrier.await() == 0) {
 *   // log the completion of this iteration
 * }}</pre>
*
* <p>The <tt>CyclicBarrier</tt> uses an all-or-none breakage model
* for failed synchronization attempts: If a thread leaves a barrier
//Synthetic comment -- @@ -175,21 +177,21 @@
throw new InterruptedException();
}

            int index = --count;
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        breakBarrier();
                }
            }

// loop until tripped, broken, interrupted, or timed out
for (;;) {
//Synthetic comment -- @@ -325,7 +327,7 @@
try {
return dowait(false, 0L);
} catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/DelayQueue.java b/luni/src/main/java/java/util/concurrent/DelayQueue.java
//Synthetic comment -- index 8c44e82..52028cb 100644

//Synthetic comment -- @@ -1,12 +1,14 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/


package java.util.concurrent;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

// BEGIN android-note
//Synthetic comment -- @@ -29,7 +31,9 @@
*
* <p>This class and its iterator implement all of the
* <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.  The Iterator provided in method {@link
 * #iterator()} is <em>not</em> guaranteed to traverse the elements of
 * the DelayQueue in any particular order.
*
* @since 1.5
* @author Doug Lea
//Synthetic comment -- @@ -154,7 +158,7 @@
lock.lock();
try {
E first = q.peek();
            if (first == null || first.getDelay(NANOSECONDS) > 0)
return null;
else
return q.poll();
//Synthetic comment -- @@ -179,7 +183,7 @@
if (first == null)
available.await();
else {
                    long delay = first.getDelay(NANOSECONDS);
if (delay <= 0)
return q.poll();
else if (leader != null)
//Synthetic comment -- @@ -226,7 +230,7 @@
else
nanos = available.awaitNanos(nanos);
} else {
                    long delay = first.getDelay(NANOSECONDS);
if (delay <= 0)
return q.poll();
if (nanos <= 0)
//Synthetic comment -- @@ -284,6 +288,17 @@
}

/**
     * Return first element only if it is expired.
     * Used only by drainTo.  Call only when holding lock.
     */
    private E peekExpired() {
        // assert lock.isHeldByCurrentThread();
        E first = q.peek();
        return (first == null || first.getDelay(NANOSECONDS) > 0) ?
            null : first;
    }

    /**
* @throws UnsupportedOperationException {@inheritDoc}
* @throws ClassCastException            {@inheritDoc}
* @throws NullPointerException          {@inheritDoc}
//Synthetic comment -- @@ -298,11 +313,9 @@
lock.lock();
try {
int n = 0;
            for (E e; (e = peekExpired()) != null;) {
                c.add(e);       // In this order, in case add() throws.
                q.poll();
++n;
}
return n;
//Synthetic comment -- @@ -328,11 +341,9 @@
lock.lock();
try {
int n = 0;
            for (E e; n < maxElements && (e = peekExpired()) != null;) {
                c.add(e);       // In this order, in case add() throws.
                q.poll();
++n;
}
return n;
//Synthetic comment -- @@ -411,8 +422,7 @@
* <p>The following code can be used to dump a delay queue into a newly
* allocated array of <tt>Delayed</tt>:
*
     * <pre> {@code Delayed[] a = q.toArray(new Delayed[0]);}</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -451,6 +461,24 @@
}

/**
     * Identity-based version for use in Itr.remove
     */
    void removeEQ(Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Iterator<E> it = q.iterator(); it.hasNext(); ) {
                if (o == it.next()) {
                    it.remove();
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
* Returns an iterator over all the elements (both expired and
* unexpired) in this queue. The iterator does not return the
* elements in any particular order.
//Synthetic comment -- @@ -473,7 +501,7 @@
*/
private class Itr implements Iterator<E> {
final Object[] array; // Array of all elements
        int cursor;           // index of next element to return
int lastRet;          // index of last element, or -1 if no such

Itr(Object[] array) {
//Synthetic comment -- @@ -496,21 +524,8 @@
public void remove() {
if (lastRet < 0)
throw new IllegalStateException();
            removeEQ(array[lastRet]);
lastRet = -1;
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Delayed.java b/luni/src/main/java/java/util/concurrent/Delayed.java
//Synthetic comment -- index b1ff4ee..39d927c 100644

//Synthetic comment -- @@ -1,13 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;

/**
* A mix-in style interface for marking objects that should be
* acted upon after a given delay.








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Exchanger.java b/luni/src/main/java/java/util/concurrent/Exchanger.java
//Synthetic comment -- index 3c230be..6069dce 100644

//Synthetic comment -- @@ -2,7 +2,7 @@
* Written by Doug Lea, Bill Scherer, and Michael Scott with
* assistance from members of JCP JSR-166 Expert Group and released to
* the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,7 +23,7 @@
* to swap buffers between threads so that the thread filling the
* buffer gets a freshly emptied one when it needs it, handing off the
* filled one to the thread emptying the buffer.
 *  <pre> {@code
* class FillAndEmpty {
*   Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
*   DataBuffer initialEmptyBuffer = ... a made-up type
//Synthetic comment -- @@ -59,8 +59,7 @@
*     new Thread(new FillingLoop()).start();
*     new Thread(new EmptyingLoop()).start();
*   }
 * }}</pre>
*
* <p>Memory consistency effects: For each pair of threads that
* successfully exchange objects via an {@code Exchanger}, actions
//Synthetic comment -- @@ -135,8 +134,8 @@
* races between two threads or thread pre-emptions occurring
* between reading and CASing.  Also, very transient peak
* contention can be much higher than the average sustainable
     * levels.  An attempt to decrease the max limit is usually made
     * when a non-slot-zero wait elapses without being fulfilled.
* Threads experiencing elapsed waits move closer to zero, so
* eventually find existing (or future) threads even if the table
* has been shrunk due to inactivity.  The chosen mechanics and
//Synthetic comment -- @@ -275,7 +274,9 @@
* extra space.
*/
private static final class Slot extends AtomicReference<Object> {
        // Improve likelihood of isolation on <= 128 byte cache lines.
        // We used to target 64 byte cache lines, but some x86s (including
        // i7 under some BIOSes) actually use 128 byte cache lines.
long q0, q1, q2, q3, q4, q5, q6, q7, q8, q9, qa, qb, qc, qd, qe;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutionException.java b/luni/src/main/java/java/util/concurrent/ExecutionException.java
//Synthetic comment -- index bc561e5..9bb8dee 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -50,11 +50,9 @@

/**
* Constructs an <tt>ExecutionException</tt> with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of <tt>cause</tt>).
*
* @param  cause the cause (which is saved for later retrieval by the
*         {@link #getCause()} method)








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Executor.java b/luni/src/main/java/java/util/concurrent/Executor.java
//Synthetic comment -- index fbc4e6f..831bf46 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -27,23 +27,23 @@
* executor can run the submitted task immediately in the caller's
* thread:
*
 *  <pre> {@code
* class DirectExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     r.run();
 *   }
 * }}</pre>
*
* More typically, tasks are executed in some thread other
* than the caller's thread.  The executor below spawns a new thread
* for each task.
*
 *  <pre> {@code
* class ThreadPerTaskExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     new Thread(r).start();
 *   }
 * }}</pre>
*
* Many <tt>Executor</tt> implementations impose some sort of
* limitation on how and when tasks are scheduled.  The executor below








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutorCompletionService.java b/luni/src/main/java/java/util/concurrent/ExecutorCompletionService.java
//Synthetic comment -- index b41955d..c0d6006 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutorService.java b/luni/src/main/java/java/util/concurrent/ExecutorService.java
//Synthetic comment -- index 89688e4..a33ceec 100644

//Synthetic comment -- @@ -1,14 +1,16 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.List;
import java.util.Collection;

// BEGIN android-note
// removed security manager docs
// END android-note

/**
* An {@link Executor} that provides methods to manage termination and
//Synthetic comment -- @@ -44,7 +46,7 @@
* pool service incoming requests. It uses the preconfigured {@link
* Executors#newFixedThreadPool} factory method:
*
 *  <pre> {@code
* class NetworkService implements Runnable {
*   private final ServerSocket serverSocket;
*   private final ExecutorService pool;
//Synthetic comment -- @@ -72,14 +74,13 @@
*   public void run() {
*     // read and service request on socket
*   }
 * }}</pre>
*
* The following method shuts down an <tt>ExecutorService</tt> in two phases,
* first by calling <tt>shutdown</tt> to reject incoming tasks, and then
* calling <tt>shutdownNow</tt>, if necessary, to cancel any lingering tasks:
*
 *  <pre> {@code
* void shutdownAndAwaitTermination(ExecutorService pool) {
*   pool.shutdown(); // Disable new tasks from being submitted
*   try {
//Synthetic comment -- @@ -96,8 +97,7 @@
*     // Preserve interrupt status
*     Thread.currentThread().interrupt();
*   }
 * }}</pre>
*
* <p>Memory consistency effects: Actions in a thread prior to the
* submission of a {@code Runnable} or {@code Callable} task to an








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Executors.java b/luni/src/main/java/java/util/concurrent/Executors.java
//Synthetic comment -- index e42b0cc..b4f03ba 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -12,9 +12,10 @@
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

// BEGIN android-note
// removed security manager docs
// END android-note
/**
* Factory and utility methods for {@link Executor}, {@link
* ExecutorService}, {@link ScheduledExecutorService}, {@link
//Synthetic comment -- @@ -271,10 +272,7 @@
/**
* Returns a default thread factory used to create new threads.
* This factory creates all new threads used by an Executor in the
     * same {@link ThreadGroup}. Each new
* thread is created as a non-daemon thread with priority set to
* the smaller of <tt>Thread.NORM_PRIORITY</tt> and the maximum
* priority permitted in the thread group.  New threads have names
//Synthetic comment -- @@ -289,36 +287,7 @@
}

/**
     * Legacy security code; do not use.
*/
public static ThreadFactory privilegedThreadFactory() {
return new PrivilegedThreadFactory();
//Synthetic comment -- @@ -383,18 +352,7 @@
}

/**
     * Legacy security code; do not use.
*/
public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
if (callable == null)
//Synthetic comment -- @@ -405,20 +363,10 @@
/**
* Returns a {@link Callable} object that will, when
* called, execute the given <tt>callable</tt> under the current
     * with the current context class loader as the context class loader.
*
* @return a callable object
* @throws NullPointerException if callable null
*/
public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
if (callable == null)
//Synthetic comment -- @@ -480,17 +428,19 @@
private final ClassLoader ccl;

PrivilegedCallableUsingCurrentClassLoader(Callable<T> task) {
            // BEGIN android-removed
            // SecurityManager sm = System.getSecurityManager();
            // if (sm != null) {
            //     // Calls to getContextClassLoader from this class
            //     // never trigger a security check, but we check
            //     // whether our callers have this permission anyways.
            //     sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            //
            //     // Whether setContextClassLoader turns out to be necessary
            //     // or not, we fail fast if permission is not available.
            //     sm.checkPermission(new RuntimePermission("setContextClassLoader"));
            // }
            // END android-removed
this.task = task;
this.acc = AccessController.getContext();
this.ccl = Thread.currentThread().getContextClassLoader();
//Synthetic comment -- @@ -561,16 +511,18 @@

PrivilegedThreadFactory() {
super();
            // BEGIN android-removed
            // SecurityManager sm = System.getSecurityManager();
            // if (sm != null) {
            //     // Calls to getContextClassLoader from this class
            //     // never trigger a security check, but we check
            //     // whether our callers have this permission anyways.
            //     sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            //
            //     // Fail fast
            //     sm.checkPermission(new RuntimePermission("setContextClassLoader"));
            // }
            // END android-removed
this.acc = AccessController.getContext();
this.ccl = Thread.currentThread().getContextClassLoader();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinPool.java b/luni/src/main/java/java/util/concurrent/ForkJoinPool.java
new file mode 100644
//Synthetic comment -- index 0000000..ee15ac8

//Synthetic comment -- @@ -0,0 +1,2127 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import libcore.util.SneakyThrow;

// BEGIN android-note
// removed security manager docs
// END android-note

/**
 * An {@link ExecutorService} for running {@link ForkJoinTask}s.
 * A {@code ForkJoinPool} provides the entry point for submissions
 * from non-{@code ForkJoinTask} clients, as well as management and
 * monitoring operations.
 *
 * <p>A {@code ForkJoinPool} differs from other kinds of {@link
 * ExecutorService} mainly by virtue of employing
 * <em>work-stealing</em>: all threads in the pool attempt to find and
 * execute subtasks created by other active tasks (eventually blocking
 * waiting for work if none exist). This enables efficient processing
 * when most tasks spawn other subtasks (as do most {@code
 * ForkJoinTask}s). When setting <em>asyncMode</em> to true in
 * constructors, {@code ForkJoinPool}s may also be appropriate for use
 * with event-style tasks that are never joined.
 *
 * <p>A {@code ForkJoinPool} is constructed with a given target
 * parallelism level; by default, equal to the number of available
 * processors. The pool attempts to maintain enough active (or
 * available) threads by dynamically adding, suspending, or resuming
 * internal worker threads, even if some tasks are stalled waiting to
 * join others. However, no such adjustments are guaranteed in the
 * face of blocked IO or other unmanaged synchronization. The nested
 * {@link ManagedBlocker} interface enables extension of the kinds of
 * synchronization accommodated.
 *
 * <p>In addition to execution and lifecycle control methods, this
 * class provides status check methods (for example
 * {@link #getStealCount}) that are intended to aid in developing,
 * tuning, and monitoring fork/join applications. Also, method
 * {@link #toString} returns indications of pool state in a
 * convenient form for informal monitoring.
 *
 * <p> As is the case with other ExecutorServices, there are three
 * main task execution methods summarized in the following
 * table. These are designed to be used by clients not already engaged
 * in fork/join computations in the current pool.  The main forms of
 * these methods accept instances of {@code ForkJoinTask}, but
 * overloaded forms also allow mixed execution of plain {@code
 * Runnable}- or {@code Callable}- based activities as well.  However,
 * tasks that are already executing in a pool should normally
 * <em>NOT</em> use these pool execution methods, but instead use the
 * within-computation forms listed in the table.
 *
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 *  <tr>
 *    <td></td>
 *    <td ALIGN=CENTER> <b>Call from non-fork/join clients</b></td>
 *    <td ALIGN=CENTER> <b>Call from within fork/join computations</b></td>
 *  </tr>
 *  <tr>
 *    <td> <b>Arrange async execution</td>
 *    <td> {@link #execute(ForkJoinTask)}</td>
 *    <td> {@link ForkJoinTask#fork}</td>
 *  </tr>
 *  <tr>
 *    <td> <b>Await and obtain result</td>
 *    <td> {@link #invoke(ForkJoinTask)}</td>
 *    <td> {@link ForkJoinTask#invoke}</td>
 *  </tr>
 *  <tr>
 *    <td> <b>Arrange exec and obtain Future</td>
 *    <td> {@link #submit(ForkJoinTask)}</td>
 *    <td> {@link ForkJoinTask#fork} (ForkJoinTasks <em>are</em> Futures)</td>
 *  </tr>
 * </table>
 *
 * <p><b>Sample Usage.</b> Normally a single {@code ForkJoinPool} is
 * used for all parallel task execution in a program or subsystem.
 * Otherwise, use would not usually outweigh the construction and
 * bookkeeping overhead of creating a large set of threads. For
 * example, a common pool could be used for the {@code SortTasks}
 * illustrated in {@link RecursiveAction}. Because {@code
 * ForkJoinPool} uses threads in {@linkplain java.lang.Thread#isDaemon
 * daemon} mode, there is typically no need to explicitly {@link
 * #shutdown} such a pool upon program exit.
 *
 *  <pre> {@code
 * static final ForkJoinPool mainPool = new ForkJoinPool();
 * ...
 * public void sort(long[] array) {
 *   mainPool.invoke(new SortTask(array, 0, array.length));
 * }}</pre>
 *
 * <p><b>Implementation notes</b>: This implementation restricts the
 * maximum number of running threads to 32767. Attempts to create
 * pools with greater than the maximum number result in
 * {@code IllegalArgumentException}.
 *
 * <p>This implementation rejects submitted tasks (that is, by throwing
 * {@link RejectedExecutionException}) only when the pool is shut down
 * or internal resources have been exhausted.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public class ForkJoinPool extends AbstractExecutorService {

    /*
     * Implementation Overview
     *
     * This class provides the central bookkeeping and control for a
     * set of worker threads: Submissions from non-FJ threads enter
     * into a submission queue. Workers take these tasks and typically
     * split them into subtasks that may be stolen by other workers.
     * Preference rules give first priority to processing tasks from
     * their own queues (LIFO or FIFO, depending on mode), then to
     * randomized FIFO steals of tasks in other worker queues, and
     * lastly to new submissions.
     *
     * The main throughput advantages of work-stealing stem from
     * decentralized control -- workers mostly take tasks from
     * themselves or each other. We cannot negate this in the
     * implementation of other management responsibilities. The main
     * tactic for avoiding bottlenecks is packing nearly all
     * essentially atomic control state into a single 64bit volatile
     * variable ("ctl"). This variable is read on the order of 10-100
     * times as often as it is modified (always via CAS). (There is
     * some additional control state, for example variable "shutdown"
     * for which we can cope with uncoordinated updates.)  This
     * streamlines synchronization and control at the expense of messy
     * constructions needed to repack status bits upon updates.
     * Updates tend not to contend with each other except during
     * bursts while submitted tasks begin or end.  In some cases when
     * they do contend, threads can instead do something else
     * (usually, scan for tasks) until contention subsides.
     *
     * To enable packing, we restrict maximum parallelism to (1<<15)-1
     * (which is far in excess of normal operating range) to allow
     * ids, counts, and their negations (used for thresholding) to fit
     * into 16bit fields.
     *
     * Recording Workers.  Workers are recorded in the "workers" array
     * that is created upon pool construction and expanded if (rarely)
     * necessary.  This is an array as opposed to some other data
     * structure to support index-based random steals by workers.
     * Updates to the array recording new workers and unrecording
     * terminated ones are protected from each other by a seqLock
     * (scanGuard) but the array is otherwise concurrently readable,
     * and accessed directly by workers. To simplify index-based
     * operations, the array size is always a power of two, and all
     * readers must tolerate null slots. To avoid flailing during
     * start-up, the array is presized to hold twice #parallelism
     * workers (which is unlikely to need further resizing during
     * execution). But to avoid dealing with so many null slots,
     * variable scanGuard includes a mask for the nearest power of two
     * that contains all current workers.  All worker thread creation
     * is on-demand, triggered by task submissions, replacement of
     * terminated workers, and/or compensation for blocked
     * workers. However, all other support code is set up to work with
     * other policies.  To ensure that we do not hold on to worker
     * references that would prevent GC, ALL accesses to workers are
     * via indices into the workers array (which is one source of some
     * of the messy code constructions here). In essence, the workers
     * array serves as a weak reference mechanism. Thus for example
     * the wait queue field of ctl stores worker indices, not worker
     * references.  Access to the workers in associated methods (for
     * example signalWork) must both index-check and null-check the
     * IDs. All such accesses ignore bad IDs by returning out early
     * from what they are doing, since this can only be associated
     * with termination, in which case it is OK to give up.
     *
     * All uses of the workers array, as well as queue arrays, check
     * that the array is non-null (even if previously non-null). This
     * allows nulling during termination, which is currently not
     * necessary, but remains an option for resource-revocation-based
     * shutdown schemes.
     *
     * Wait Queuing. Unlike HPC work-stealing frameworks, we cannot
     * let workers spin indefinitely scanning for tasks when none can
     * be found immediately, and we cannot start/resume workers unless
     * there appear to be tasks available.  On the other hand, we must
     * quickly prod them into action when new tasks are submitted or
     * generated.  We park/unpark workers after placing in an event
     * wait queue when they cannot find work. This "queue" is actually
     * a simple Treiber stack, headed by the "id" field of ctl, plus a
     * 15bit counter value to both wake up waiters (by advancing their
     * count) and avoid ABA effects. Successors are held in worker
     * field "nextWait".  Queuing deals with several intrinsic races,
     * mainly that a task-producing thread can miss seeing (and
     * signalling) another thread that gave up looking for work but
     * has not yet entered the wait queue. We solve this by requiring
     * a full sweep of all workers both before (in scan()) and after
     * (in tryAwaitWork()) a newly waiting worker is added to the wait
     * queue. During a rescan, the worker might release some other
     * queued worker rather than itself, which has the same net
     * effect. Because enqueued workers may actually be rescanning
     * rather than waiting, we set and clear the "parked" field of
     * ForkJoinWorkerThread to reduce unnecessary calls to unpark.
     * (Use of the parked field requires a secondary recheck to avoid
     * missed signals.)
     *
     * Signalling.  We create or wake up workers only when there
     * appears to be at least one task they might be able to find and
     * execute.  When a submission is added or another worker adds a
     * task to a queue that previously had two or fewer tasks, they
     * signal waiting workers (or trigger creation of new ones if
     * fewer than the given parallelism level -- see signalWork).
     * These primary signals are buttressed by signals during rescans
     * as well as those performed when a worker steals a task and
     * notices that there are more tasks too; together these cover the
     * signals needed in cases when more than two tasks are pushed
     * but untaken.
     *
     * Trimming workers. To release resources after periods of lack of
     * use, a worker starting to wait when the pool is quiescent will
     * time out and terminate if the pool has remained quiescent for
     * SHRINK_RATE nanosecs. This will slowly propagate, eventually
     * terminating all workers after long periods of non-use.
     *
     * Submissions. External submissions are maintained in an
     * array-based queue that is structured identically to
     * ForkJoinWorkerThread queues except for the use of
     * submissionLock in method addSubmission. Unlike the case for
     * worker queues, multiple external threads can add new
     * submissions, so adding requires a lock.
     *
     * Compensation. Beyond work-stealing support and lifecycle
     * control, the main responsibility of this framework is to take
     * actions when one worker is waiting to join a task stolen (or
     * always held by) another.  Because we are multiplexing many
     * tasks on to a pool of workers, we can't just let them block (as
     * in Thread.join).  We also cannot just reassign the joiner's
     * run-time stack with another and replace it later, which would
     * be a form of "continuation", that even if possible is not
     * necessarily a good idea since we sometimes need both an
     * unblocked task and its continuation to progress. Instead we
     * combine two tactics:
     *
     *   Helping: Arranging for the joiner to execute some task that it
     *      would be running if the steal had not occurred.  Method
     *      ForkJoinWorkerThread.joinTask tracks joining->stealing
     *      links to try to find such a task.
     *
     *   Compensating: Unless there are already enough live threads,
     *      method tryPreBlock() may create or re-activate a spare
     *      thread to compensate for blocked joiners until they
     *      unblock.
     *
     * The ManagedBlocker extension API can't use helping so relies
     * only on compensation in method awaitBlocker.
     *
     * It is impossible to keep exactly the target parallelism number
     * of threads running at any given time.  Determining the
     * existence of conservatively safe helping targets, the
     * availability of already-created spares, and the apparent need
     * to create new spares are all racy and require heuristic
     * guidance, so we rely on multiple retries of each.  Currently,
     * in keeping with on-demand signalling policy, we compensate only
     * if blocking would leave less than one active (non-waiting,
     * non-blocked) worker. Additionally, to avoid some false alarms
     * due to GC, lagging counters, system activity, etc, compensated
     * blocking for joins is only attempted after rechecks stabilize
     * (retries are interspersed with Thread.yield, for good
     * citizenship).  The variable blockedCount, incremented before
     * blocking and decremented after, is sometimes needed to
     * distinguish cases of waiting for work vs blocking on joins or
     * other managed sync. Both cases are equivalent for most pool
     * control, so we can update non-atomically. (Additionally,
     * contention on blockedCount alleviates some contention on ctl).
     *
     * Shutdown and Termination. A call to shutdownNow atomically sets
     * the ctl stop bit and then (non-atomically) sets each workers
     * "terminate" status, cancels all unprocessed tasks, and wakes up
     * all waiting workers.  Detecting whether termination should
     * commence after a non-abrupt shutdown() call requires more work
     * and bookkeeping. We need consensus about quiescence (i.e., that
     * there is no more work) which is reflected in active counts so
     * long as there are no current blockers, as well as possible
     * re-evaluations during independent changes in blocking or
     * quiescing workers.
     *
     * Style notes: There is a lot of representation-level coupling
     * among classes ForkJoinPool, ForkJoinWorkerThread, and
     * ForkJoinTask.  Most fields of ForkJoinWorkerThread maintain
     * data structures managed by ForkJoinPool, so are directly
     * accessed.  Conversely we allow access to "workers" array by
     * workers, and direct access to ForkJoinTask.status by both
     * ForkJoinPool and ForkJoinWorkerThread.  There is little point
     * trying to reduce this, since any associated future changes in
     * representations will need to be accompanied by algorithmic
     * changes anyway. All together, these low-level implementation
     * choices produce as much as a factor of 4 performance
     * improvement compared to naive implementations, and enable the
     * processing of billions of tasks per second, at the expense of
     * some ugliness.
     *
     * Methods signalWork() and scan() are the main bottlenecks so are
     * especially heavily micro-optimized/mangled.  There are lots of
     * inline assignments (of form "while ((local = field) != 0)")
     * which are usually the simplest way to ensure the required read
     * orderings (which are sometimes critical). This leads to a
     * "C"-like style of listing declarations of these locals at the
     * heads of methods or blocks.  There are several occurrences of
     * the unusual "do {} while (!cas...)"  which is the simplest way
     * to force an update of a CAS'ed variable. There are also other
     * coding oddities that help some methods perform reasonably even
     * when interpreted (not compiled).
     *
     * The order of declarations in this file is: (1) declarations of
     * statics (2) fields (along with constants used when unpacking
     * some of them), listed in an order that tends to reduce
     * contention among them a bit under most JVMs.  (3) internal
     * control methods (4) callbacks and other support for
     * ForkJoinTask and ForkJoinWorkerThread classes, (5) exported
     * methods (plus a few little helpers). (6) static block
     * initializing all statics in a minimally dependent order.
     */

    /**
     * Factory for creating new {@link ForkJoinWorkerThread}s.
     * A {@code ForkJoinWorkerThreadFactory} must be defined and used
     * for {@code ForkJoinWorkerThread} subclasses that extend base
     * functionality or initialize threads with different contexts.
     */
    public static interface ForkJoinWorkerThreadFactory {
        /**
         * Returns a new worker thread operating in the given pool.
         *
         * @param pool the pool this thread works in
         * @throws NullPointerException if the pool is null
         */
        public ForkJoinWorkerThread newThread(ForkJoinPool pool);
    }

    /**
     * Default ForkJoinWorkerThreadFactory implementation; creates a
     * new ForkJoinWorkerThread.
     */
    static class DefaultForkJoinWorkerThreadFactory
        implements ForkJoinWorkerThreadFactory {
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new ForkJoinWorkerThread(pool);
        }
    }

    /**
     * Creates a new ForkJoinWorkerThread. This factory is used unless
     * overridden in ForkJoinPool constructors.
     */
    public static final ForkJoinWorkerThreadFactory
        defaultForkJoinWorkerThreadFactory;

    /**
     * Permission required for callers of methods that may start or
     * kill threads.
     */
    private static final RuntimePermission modifyThreadPermission;

    /**
     * If there is a security manager, makes sure caller has
     * permission to modify threads.
     */
    private static void checkPermission() {
        SecurityManager security = System.getSecurityManager();
        if (security != null)
            security.checkPermission(modifyThreadPermission);
    }

    /**
     * Generator for assigning sequence numbers as pool names.
     */
    private static final AtomicInteger poolNumberGenerator;

    /**
     * Generator for initial random seeds for worker victim
     * selection. This is used only to create initial seeds. Random
     * steals use a cheaper xorshift generator per steal attempt. We
     * don't expect much contention on seedGenerator, so just use a
     * plain Random.
     */
    static final Random workerSeedGenerator;

    /**
     * Array holding all worker threads in the pool.  Initialized upon
     * construction. Array size must be a power of two.  Updates and
     * replacements are protected by scanGuard, but the array is
     * always kept in a consistent enough state to be randomly
     * accessed without locking by workers performing work-stealing,
     * as well as other traversal-based methods in this class, so long
     * as reads memory-acquire by first reading ctl. All readers must
     * tolerate that some array slots may be null.
     */
    ForkJoinWorkerThread[] workers;

    /**
     * Initial size for submission queue array. Must be a power of
     * two.  In many applications, these always stay small so we use a
     * small initial cap.
     */
    private static final int INITIAL_QUEUE_CAPACITY = 8;

    /**
     * Maximum size for submission queue array. Must be a power of two
     * less than or equal to 1 << (31 - width of array entry) to
     * ensure lack of index wraparound, but is capped at a lower
     * value to help users trap runaway computations.
     */
    private static final int MAXIMUM_QUEUE_CAPACITY = 1 << 24; // 16M

    /**
     * Array serving as submission queue. Initialized upon construction.
     */
    private ForkJoinTask<?>[] submissionQueue;

    /**
     * Lock protecting submissions array for addSubmission
     */
    private final ReentrantLock submissionLock;

    /**
     * Condition for awaitTermination, using submissionLock for
     * convenience.
     */
    private final Condition termination;

    /**
     * Creation factory for worker threads.
     */
    private final ForkJoinWorkerThreadFactory factory;

    /**
     * The uncaught exception handler used when any worker abruptly
     * terminates.
     */
    final Thread.UncaughtExceptionHandler ueh;

    /**
     * Prefix for assigning names to worker threads
     */
    private final String workerNamePrefix;

    /**
     * Sum of per-thread steal counts, updated only when threads are
     * idle or terminating.
     */
    private volatile long stealCount;

    /**
     * Main pool control -- a long packed with:
     * AC: Number of active running workers minus target parallelism (16 bits)
     * TC: Number of total workers minus target parallelism (16 bits)
     * ST: true if pool is terminating (1 bit)
     * EC: the wait count of top waiting thread (15 bits)
     * ID: ~poolIndex of top of Treiber stack of waiting threads (16 bits)
     *
     * When convenient, we can extract the upper 32 bits of counts and
     * the lower 32 bits of queue state, u = (int)(ctl >>> 32) and e =
     * (int)ctl.  The ec field is never accessed alone, but always
     * together with id and st. The offsets of counts by the target
     * parallelism and the positionings of fields makes it possible to
     * perform the most common checks via sign tests of fields: When
     * ac is negative, there are not enough active workers, when tc is
     * negative, there are not enough total workers, when id is
     * negative, there is at least one waiting worker, and when e is
     * negative, the pool is terminating.  To deal with these possibly
     * negative fields, we use casts in and out of "short" and/or
     * signed shifts to maintain signedness.
     */
    volatile long ctl;

    // bit positions/shifts for fields
    private static final int  AC_SHIFT   = 48;
    private static final int  TC_SHIFT   = 32;
    private static final int  ST_SHIFT   = 31;
    private static final int  EC_SHIFT   = 16;

    // bounds
    private static final int  MAX_ID     = 0x7fff;  // max poolIndex
    private static final int  SMASK      = 0xffff;  // mask short bits
    private static final int  SHORT_SIGN = 1 << 15;
    private static final int  INT_SIGN   = 1 << 31;

    // masks
    private static final long STOP_BIT   = 0x0001L << ST_SHIFT;
    private static final long AC_MASK    = ((long)SMASK) << AC_SHIFT;
    private static final long TC_MASK    = ((long)SMASK) << TC_SHIFT;

    // units for incrementing and decrementing
    private static final long TC_UNIT    = 1L << TC_SHIFT;
    private static final long AC_UNIT    = 1L << AC_SHIFT;

    // masks and units for dealing with u = (int)(ctl >>> 32)
    private static final int  UAC_SHIFT  = AC_SHIFT - 32;
    private static final int  UTC_SHIFT  = TC_SHIFT - 32;
    private static final int  UAC_MASK   = SMASK << UAC_SHIFT;
    private static final int  UTC_MASK   = SMASK << UTC_SHIFT;
    private static final int  UAC_UNIT   = 1 << UAC_SHIFT;
    private static final int  UTC_UNIT   = 1 << UTC_SHIFT;

    // masks and units for dealing with e = (int)ctl
    private static final int  E_MASK     = 0x7fffffff; // no STOP_BIT
    private static final int  EC_UNIT    = 1 << EC_SHIFT;

    /**
     * The target parallelism level.
     */
    final int parallelism;

    /**
     * Index (mod submission queue length) of next element to take
     * from submission queue. Usage is identical to that for
     * per-worker queues -- see ForkJoinWorkerThread internal
     * documentation.
     */
    volatile int queueBase;

    /**
     * Index (mod submission queue length) of next element to add
     * in submission queue. Usage is identical to that for
     * per-worker queues -- see ForkJoinWorkerThread internal
     * documentation.
     */
    int queueTop;

    /**
     * True when shutdown() has been called.
     */
    volatile boolean shutdown;

    /**
     * True if use local fifo, not default lifo, for local polling.
     * Read by, and replicated by ForkJoinWorkerThreads.
     */
    final boolean locallyFifo;

    /**
     * The number of threads in ForkJoinWorkerThreads.helpQuiescePool.
     * When non-zero, suppresses automatic shutdown when active
     * counts become zero.
     */
    volatile int quiescerCount;

    /**
     * The number of threads blocked in join.
     */
    volatile int blockedCount;

    /**
     * Counter for worker Thread names (unrelated to their poolIndex)
     */
    private volatile int nextWorkerNumber;

    /**
     * The index for the next created worker. Accessed under scanGuard.
     */
    private int nextWorkerIndex;

    /**
     * SeqLock and index masking for updates to workers array.  Locked
     * when SG_UNIT is set. Unlocking clears bit by adding
     * SG_UNIT. Staleness of read-only operations can be checked by
     * comparing scanGuard to value before the reads. The low 16 bits
     * (i.e, anding with SMASK) hold (the smallest power of two
     * covering all worker indices, minus one, and is used to avoid
     * dealing with large numbers of null slots when the workers array
     * is overallocated.
     */
    volatile int scanGuard;

    private static final int SG_UNIT = 1 << 16;

    /**
     * The wakeup interval (in nanoseconds) for a worker waiting for a
     * task when the pool is quiescent to instead try to shrink the
     * number of workers.  The exact value does not matter too
     * much. It must be short enough to release resources during
     * sustained periods of idleness, but not so short that threads
     * are continually re-created.
     */
    private static final long SHRINK_RATE =
        4L * 1000L * 1000L * 1000L; // 4 seconds

    /**
     * Top-level loop for worker threads: On each step: if the
     * previous step swept through all queues and found no tasks, or
     * there are excess threads, then possibly blocks. Otherwise,
     * scans for and, if found, executes a task. Returns when pool
     * and/or worker terminate.
     *
     * @param w the worker
     */
    final void work(ForkJoinWorkerThread w) {
        boolean swept = false;                // true on empty scans
        long c;
        while (!w.terminate && (int)(c = ctl) >= 0) {
            int a;                            // active count
            if (!swept && (a = (int)(c >> AC_SHIFT)) <= 0)
                swept = scan(w, a);
            else if (tryAwaitWork(w, c))
                swept = false;
        }
    }

    // Signalling

    /**
     * Wakes up or creates a worker.
     */
    final void signalWork() {
        /*
         * The while condition is true if: (there is are too few total
         * workers OR there is at least one waiter) AND (there are too
         * few active workers OR the pool is terminating).  The value
         * of e distinguishes the remaining cases: zero (no waiters)
         * for create, negative if terminating (in which case do
         * nothing), else release a waiter. The secondary checks for
         * release (non-null array etc) can fail if the pool begins
         * terminating after the test, and don't impose any added cost
         * because JVMs must perform null and bounds checks anyway.
         */
        long c; int e, u;
        while ((((e = (int)(c = ctl)) | (u = (int)(c >>> 32))) &
                (INT_SIGN|SHORT_SIGN)) == (INT_SIGN|SHORT_SIGN) && e >= 0) {
            if (e > 0) {                         // release a waiting worker
                int i; ForkJoinWorkerThread w; ForkJoinWorkerThread[] ws;
                if ((ws = workers) == null ||
                    (i = ~e & SMASK) >= ws.length ||
                    (w = ws[i]) == null)
                    break;
                long nc = (((long)(w.nextWait & E_MASK)) |
                           ((long)(u + UAC_UNIT) << 32));
                if (w.eventCount == e &&
                    UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc)) {
                    w.eventCount = (e + EC_UNIT) & E_MASK;
                    if (w.parked)
                        UNSAFE.unpark(w);
                    break;
                }
            }
            else if (UNSAFE.compareAndSwapLong
                     (this, ctlOffset, c,
                      (long)(((u + UTC_UNIT) & UTC_MASK) |
                             ((u + UAC_UNIT) & UAC_MASK)) << 32)) {
                addWorker();
                break;
            }
        }
    }

    /**
     * Variant of signalWork to help release waiters on rescans.
     * Tries once to release a waiter if active count < 0.
     *
     * @return false if failed due to contention, else true
     */
    private boolean tryReleaseWaiter() {
        long c; int e, i; ForkJoinWorkerThread w; ForkJoinWorkerThread[] ws;
        if ((e = (int)(c = ctl)) > 0 &&
            (int)(c >> AC_SHIFT) < 0 &&
            (ws = workers) != null &&
            (i = ~e & SMASK) < ws.length &&
            (w = ws[i]) != null) {
            long nc = ((long)(w.nextWait & E_MASK) |
                       ((c + AC_UNIT) & (AC_MASK|TC_MASK)));
            if (w.eventCount != e ||
                !UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc))
                return false;
            w.eventCount = (e + EC_UNIT) & E_MASK;
            if (w.parked)
                UNSAFE.unpark(w);
        }
        return true;
    }

    // Scanning for tasks

    /**
     * Scans for and, if found, executes one task. Scans start at a
     * random index of workers array, and randomly select the first
     * (2*#workers)-1 probes, and then, if all empty, resort to 2
     * circular sweeps, which is necessary to check quiescence. and
     * taking a submission only if no stealable tasks were found.  The
     * steal code inside the loop is a specialized form of
     * ForkJoinWorkerThread.deqTask, followed bookkeeping to support
     * helpJoinTask and signal propagation. The code for submission
     * queues is almost identical. On each steal, the worker completes
     * not only the task, but also all local tasks that this task may
     * have generated. On detecting staleness or contention when
     * trying to take a task, this method returns without finishing
     * sweep, which allows global state rechecks before retry.
     *
     * @param w the worker
     * @param a the number of active workers
     * @return true if swept all queues without finding a task
     */
    private boolean scan(ForkJoinWorkerThread w, int a) {
        int g = scanGuard; // mask 0 avoids useless scans if only one active
        int m = (parallelism == 1 - a && blockedCount == 0) ? 0 : g & SMASK;
        ForkJoinWorkerThread[] ws = workers;
        if (ws == null || ws.length <= m)         // staleness check
            return false;
        for (int r = w.seed, k = r, j = -(m + m); j <= m + m; ++j) {
            ForkJoinTask<?> t; ForkJoinTask<?>[] q; int b, i;
            ForkJoinWorkerThread v = ws[k & m];
            if (v != null && (b = v.queueBase) != v.queueTop &&
                (q = v.queue) != null && (i = (q.length - 1) & b) >= 0) {
                long u = (i << ASHIFT) + ABASE;
                if ((t = q[i]) != null && v.queueBase == b &&
                    UNSAFE.compareAndSwapObject(q, u, t, null)) {
                    int d = (v.queueBase = b + 1) - v.queueTop;
                    v.stealHint = w.poolIndex;
                    if (d != 0)
                        signalWork();             // propagate if nonempty
                    w.execTask(t);
                }
                r ^= r << 13; r ^= r >>> 17; w.seed = r ^ (r << 5);
                return false;                     // store next seed
            }
            else if (j < 0) {                     // xorshift
                r ^= r << 13; r ^= r >>> 17; k = r ^= r << 5;
            }
            else
                ++k;
        }
        if (scanGuard != g)                       // staleness check
            return false;
        else {                                    // try to take submission
            ForkJoinTask<?> t; ForkJoinTask<?>[] q; int b, i;
            if ((b = queueBase) != queueTop &&
                (q = submissionQueue) != null &&
                (i = (q.length - 1) & b) >= 0) {
                long u = (i << ASHIFT) + ABASE;
                if ((t = q[i]) != null && queueBase == b &&
                    UNSAFE.compareAndSwapObject(q, u, t, null)) {
                    queueBase = b + 1;
                    w.execTask(t);
                }
                return false;
            }
            return true;                         // all queues empty
        }
    }

    /**
     * Tries to enqueue worker w in wait queue and await change in
     * worker's eventCount.  If the pool is quiescent and there is
     * more than one worker, possibly terminates worker upon exit.
     * Otherwise, before blocking, rescans queues to avoid missed
     * signals.  Upon finding work, releases at least one worker
     * (which may be the current worker). Rescans restart upon
     * detected staleness or failure to release due to
     * contention. Note the unusual conventions about Thread.interrupt
     * here and elsewhere: Because interrupts are used solely to alert
     * threads to check termination, which is checked here anyway, we
     * clear status (using Thread.interrupted) before any call to
     * park, so that park does not immediately return due to status
     * being set via some other unrelated call to interrupt in user
     * code.
     *
     * @param w the calling worker
     * @param c the ctl value on entry
     * @return true if waited or another thread was released upon enq
     */
    private boolean tryAwaitWork(ForkJoinWorkerThread w, long c) {
        int v = w.eventCount;
        w.nextWait = (int)c;                      // w's successor record
        long nc = (long)(v & E_MASK) | ((c - AC_UNIT) & (AC_MASK|TC_MASK));
        if (ctl != c || !UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc)) {
            long d = ctl; // return true if lost to a deq, to force scan
            return (int)d != (int)c && (d & AC_MASK) >= (c & AC_MASK);
        }
        for (int sc = w.stealCount; sc != 0;) {   // accumulate stealCount
            long s = stealCount;
            if (UNSAFE.compareAndSwapLong(this, stealCountOffset, s, s + sc))
                sc = w.stealCount = 0;
            else if (w.eventCount != v)
                return true;                      // update next time
        }
        if ((!shutdown || !tryTerminate(false)) &&
            (int)c != 0 && parallelism + (int)(nc >> AC_SHIFT) == 0 &&
            blockedCount == 0 && quiescerCount == 0)
            idleAwaitWork(w, nc, c, v);           // quiescent
        for (boolean rescanned = false;;) {
            if (w.eventCount != v)
                return true;
            if (!rescanned) {
                int g = scanGuard, m = g & SMASK;
                ForkJoinWorkerThread[] ws = workers;
                if (ws != null && m < ws.length) {
                    rescanned = true;
                    for (int i = 0; i <= m; ++i) {
                        ForkJoinWorkerThread u = ws[i];
                        if (u != null) {
                            if (u.queueBase != u.queueTop &&
                                !tryReleaseWaiter())
                                rescanned = false; // contended
                            if (w.eventCount != v)
                                return true;
                        }
                    }
                }
                if (scanGuard != g ||              // stale
                    (queueBase != queueTop && !tryReleaseWaiter()))
                    rescanned = false;
                if (!rescanned)
                    Thread.yield();                // reduce contention
                else
                    Thread.interrupted();          // clear before park
            }
            else {
                w.parked = true;                   // must recheck
                if (w.eventCount != v) {
                    w.parked = false;
                    return true;
                }
                LockSupport.park(this);
                rescanned = w.parked = false;
            }
        }
    }

    /**
     * If inactivating worker w has caused pool to become
     * quiescent, check for pool termination, and wait for event
     * for up to SHRINK_RATE nanosecs (rescans are unnecessary in
     * this case because quiescence reflects consensus about lack
     * of work). On timeout, if ctl has not changed, terminate the
     * worker. Upon its termination (see deregisterWorker), it may
     * wake up another worker to possibly repeat this process.
     *
     * @param w the calling worker
     * @param currentCtl the ctl value after enqueuing w
     * @param prevCtl the ctl value if w terminated
     * @param v the eventCount w awaits change
     */
    private void idleAwaitWork(ForkJoinWorkerThread w, long currentCtl,
                               long prevCtl, int v) {
        if (w.eventCount == v) {
            if (shutdown)
                tryTerminate(false);
            ForkJoinTask.helpExpungeStaleExceptions(); // help clean weak refs
            while (ctl == currentCtl) {
                long startTime = System.nanoTime();
                w.parked = true;
                if (w.eventCount == v)             // must recheck
                    LockSupport.parkNanos(this, SHRINK_RATE);
                w.parked = false;
                if (w.eventCount != v)
                    break;
                else if (System.nanoTime() - startTime <
                         SHRINK_RATE - (SHRINK_RATE / 10)) // timing slop
                    Thread.interrupted();          // spurious wakeup
                else if (UNSAFE.compareAndSwapLong(this, ctlOffset,
                                                   currentCtl, prevCtl)) {
                    w.terminate = true;            // restore previous
                    w.eventCount = ((int)currentCtl + EC_UNIT) & E_MASK;
                    break;
                }
            }
        }
    }

    // Submissions

    /**
     * Enqueues the given task in the submissionQueue.  Same idea as
     * ForkJoinWorkerThread.pushTask except for use of submissionLock.
     *
     * @param t the task
     */
    private void addSubmission(ForkJoinTask<?> t) {
        final ReentrantLock lock = this.submissionLock;
        lock.lock();
        try {
            ForkJoinTask<?>[] q; int s, m;
            if ((q = submissionQueue) != null) {    // ignore if queue removed
                long u = (((s = queueTop) & (m = q.length-1)) << ASHIFT)+ABASE;
                UNSAFE.putOrderedObject(q, u, t);
                queueTop = s + 1;
                if (s - queueBase == m)
                    growSubmissionQueue();
            }
        } finally {
            lock.unlock();
        }
        signalWork();
    }

    //  (pollSubmission is defined below with exported methods)

    /**
     * Creates or doubles submissionQueue array.
     * Basically identical to ForkJoinWorkerThread version.
     */
    private void growSubmissionQueue() {
        ForkJoinTask<?>[] oldQ = submissionQueue;
        int size = oldQ != null ? oldQ.length << 1 : INITIAL_QUEUE_CAPACITY;
        if (size > MAXIMUM_QUEUE_CAPACITY)
            throw new RejectedExecutionException("Queue capacity exceeded");
        if (size < INITIAL_QUEUE_CAPACITY)
            size = INITIAL_QUEUE_CAPACITY;
        ForkJoinTask<?>[] q = submissionQueue = new ForkJoinTask<?>[size];
        int mask = size - 1;
        int top = queueTop;
        int oldMask;
        if (oldQ != null && (oldMask = oldQ.length - 1) >= 0) {
            for (int b = queueBase; b != top; ++b) {
                long u = ((b & oldMask) << ASHIFT) + ABASE;
                Object x = UNSAFE.getObjectVolatile(oldQ, u);
                if (x != null && UNSAFE.compareAndSwapObject(oldQ, u, x, null))
                    UNSAFE.putObjectVolatile
                        (q, ((b & mask) << ASHIFT) + ABASE, x);
            }
        }
    }

    // Blocking support

    /**
     * Tries to increment blockedCount, decrement active count
     * (sometimes implicitly) and possibly release or create a
     * compensating worker in preparation for blocking. Fails
     * on contention or termination.
     *
     * @return true if the caller can block, else should recheck and retry
     */
    private boolean tryPreBlock() {
        int b = blockedCount;
        if (UNSAFE.compareAndSwapInt(this, blockedCountOffset, b, b + 1)) {
            int pc = parallelism;
            do {
                ForkJoinWorkerThread[] ws; ForkJoinWorkerThread w;
                int e, ac, tc, i;
                long c = ctl;
                int u = (int)(c >>> 32);
                if ((e = (int)c) < 0) {
                                                 // skip -- terminating
                }
                else if ((ac = (u >> UAC_SHIFT)) <= 0 && e != 0 &&
                         (ws = workers) != null &&
                         (i = ~e & SMASK) < ws.length &&
                         (w = ws[i]) != null) {
                    long nc = ((long)(w.nextWait & E_MASK) |
                               (c & (AC_MASK|TC_MASK)));
                    if (w.eventCount == e &&
                        UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc)) {
                        w.eventCount = (e + EC_UNIT) & E_MASK;
                        if (w.parked)
                            UNSAFE.unpark(w);
                        return true;             // release an idle worker
                    }
                }
                else if ((tc = (short)(u >>> UTC_SHIFT)) >= 0 && ac + pc > 1) {
                    long nc = ((c - AC_UNIT) & AC_MASK) | (c & ~AC_MASK);
                    if (UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc))
                        return true;             // no compensation needed
                }
                else if (tc + pc < MAX_ID) {
                    long nc = ((c + TC_UNIT) & TC_MASK) | (c & ~TC_MASK);
                    if (UNSAFE.compareAndSwapLong(this, ctlOffset, c, nc)) {
                        addWorker();
                        return true;            // create a replacement
                    }
                }
                // try to back out on any failure and let caller retry
            } while (!UNSAFE.compareAndSwapInt(this, blockedCountOffset,
                                               b = blockedCount, b - 1));
        }
        return false;
    }

    /**
     * Decrements blockedCount and increments active count.
     */
    private void postBlock() {
        long c;
        do {} while (!UNSAFE.compareAndSwapLong(this, ctlOffset,  // no mask
                                                c = ctl, c + AC_UNIT));
        int b;
        do {} while (!UNSAFE.compareAndSwapInt(this, blockedCountOffset,
                                               b = blockedCount, b - 1));
    }

    /**
     * Possibly blocks waiting for the given task to complete, or
     * cancels the task if terminating.  Fails to wait if contended.
     *
     * @param joinMe the task
     */
    final void tryAwaitJoin(ForkJoinTask<?> joinMe) {
        Thread.interrupted(); // clear interrupts before checking termination
        if (joinMe.status >= 0) {
            if (tryPreBlock()) {
                joinMe.tryAwaitDone(0L);
                postBlock();
            }
            else if ((ctl & STOP_BIT) != 0L)
                joinMe.cancelIgnoringExceptions();
        }
    }

    /**
     * Possibly blocks the given worker waiting for joinMe to
     * complete or timeout.
     *
     * @param joinMe the task
     * @param nanos the wait time for underlying Object.wait
     */
    final void timedAwaitJoin(ForkJoinTask<?> joinMe, long nanos) {
        while (joinMe.status >= 0) {
            Thread.interrupted();
            if ((ctl & STOP_BIT) != 0L) {
                joinMe.cancelIgnoringExceptions();
                break;
            }
            if (tryPreBlock()) {
                long last = System.nanoTime();
                while (joinMe.status >= 0) {
                    long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
                    if (millis <= 0)
                        break;
                    joinMe.tryAwaitDone(millis);
                    if (joinMe.status < 0)
                        break;
                    if ((ctl & STOP_BIT) != 0L) {
                        joinMe.cancelIgnoringExceptions();
                        break;
                    }
                    long now = System.nanoTime();
                    nanos -= now - last;
                    last = now;
                }
                postBlock();
                break;
            }
        }
    }

    /**
     * If necessary, compensates for blocker, and blocks.
     */
    private void awaitBlocker(ManagedBlocker blocker)
        throws InterruptedException {
        while (!blocker.isReleasable()) {
            if (tryPreBlock()) {
                try {
                    do {} while (!blocker.isReleasable() && !blocker.block());
                } finally {
                    postBlock();
                }
                break;
            }
        }
    }

    // Creating, registering and deregistring workers

    /**
     * Tries to create and start a worker; minimally rolls back counts
     * on failure.
     */
    private void addWorker() {
        Throwable ex = null;
        ForkJoinWorkerThread t = null;
        try {
            t = factory.newThread(this);
        } catch (Throwable e) {
            ex = e;
        }
        if (t == null) {  // null or exceptional factory return
            long c;       // adjust counts
            do {} while (!UNSAFE.compareAndSwapLong
                         (this, ctlOffset, c = ctl,
                          (((c - AC_UNIT) & AC_MASK) |
                           ((c - TC_UNIT) & TC_MASK) |
                           (c & ~(AC_MASK|TC_MASK)))));
            // Propagate exception if originating from an external caller
            if (!tryTerminate(false) && ex != null &&
                !(Thread.currentThread() instanceof ForkJoinWorkerThread))
                SneakyThrow.sneakyThrow(ex); // android-changed
        }
        else
            t.start();
    }

    /**
     * Callback from ForkJoinWorkerThread constructor to assign a
     * public name
     */
    final String nextWorkerName() {
        for (int n;;) {
            if (UNSAFE.compareAndSwapInt(this, nextWorkerNumberOffset,
                                         n = nextWorkerNumber, ++n))
                return workerNamePrefix + n;
        }
    }

    /**
     * Callback from ForkJoinWorkerThread constructor to
     * determine its poolIndex and record in workers array.
     *
     * @param w the worker
     * @return the worker's pool index
     */
    final int registerWorker(ForkJoinWorkerThread w) {
        /*
         * In the typical case, a new worker acquires the lock, uses
         * next available index and returns quickly.  Since we should
         * not block callers (ultimately from signalWork or
         * tryPreBlock) waiting for the lock needed to do this, we
         * instead help release other workers while waiting for the
         * lock.
         */
        for (int g;;) {
            ForkJoinWorkerThread[] ws;
            if (((g = scanGuard) & SG_UNIT) == 0 &&
                UNSAFE.compareAndSwapInt(this, scanGuardOffset,
                                         g, g | SG_UNIT)) {
                int k = nextWorkerIndex;
                try {
                    if ((ws = workers) != null) { // ignore on shutdown
                        int n = ws.length;
                        if (k < 0 || k >= n || ws[k] != null) {
                            for (k = 0; k < n && ws[k] != null; ++k)
                                ;
                            if (k == n)
                                ws = workers = Arrays.copyOf(ws, n << 1);
                        }
                        ws[k] = w;
                        nextWorkerIndex = k + 1;
                        int m = g & SMASK;
                        g = (k > m) ? ((m << 1) + 1) & SMASK : g + (SG_UNIT<<1);
                    }
                } finally {
                    scanGuard = g;
                }
                return k;
            }
            else if ((ws = workers) != null) { // help release others
                for (ForkJoinWorkerThread u : ws) {
                    if (u != null && u.queueBase != u.queueTop) {
                        if (tryReleaseWaiter())
                            break;
                    }
                }
            }
        }
    }

    /**
     * Final callback from terminating worker.  Removes record of
     * worker from array, and adjusts counts. If pool is shutting
     * down, tries to complete termination.
     *
     * @param w the worker
     */
    final void deregisterWorker(ForkJoinWorkerThread w, Throwable ex) {
        int idx = w.poolIndex;
        int sc = w.stealCount;
        int steps = 0;
        // Remove from array, adjust worker counts and collect steal count.
        // We can intermix failed removes or adjusts with steal updates
        do {
            long s, c;
            int g;
            if (steps == 0 && ((g = scanGuard) & SG_UNIT) == 0 &&
                UNSAFE.compareAndSwapInt(this, scanGuardOffset,
                                         g, g |= SG_UNIT)) {
                ForkJoinWorkerThread[] ws = workers;
                if (ws != null && idx >= 0 &&
                    idx < ws.length && ws[idx] == w)
                    ws[idx] = null;    // verify
                nextWorkerIndex = idx;
                scanGuard = g + SG_UNIT;
                steps = 1;
            }
            if (steps == 1 &&
                UNSAFE.compareAndSwapLong(this, ctlOffset, c = ctl,
                                          (((c - AC_UNIT) & AC_MASK) |
                                           ((c - TC_UNIT) & TC_MASK) |
                                           (c & ~(AC_MASK|TC_MASK)))))
                steps = 2;
            if (sc != 0 &&
                UNSAFE.compareAndSwapLong(this, stealCountOffset,
                                          s = stealCount, s + sc))
                sc = 0;
        } while (steps != 2 || sc != 0);
        if (!tryTerminate(false)) {
            if (ex != null)   // possibly replace if died abnormally
                signalWork();
            else
                tryReleaseWaiter();
        }
    }

    // Shutdown and termination

    /**
     * Possibly initiates and/or completes termination.
     *
     * @param now if true, unconditionally terminate, else only
     * if shutdown and empty queue and no active workers
     * @return true if now terminating or terminated
     */
    private boolean tryTerminate(boolean now) {
        long c;
        while (((c = ctl) & STOP_BIT) == 0) {
            if (!now) {
                if ((int)(c >> AC_SHIFT) != -parallelism)
                    return false;
                if (!shutdown || blockedCount != 0 || quiescerCount != 0 ||
                    queueBase != queueTop) {
                    if (ctl == c) // staleness check
                        return false;
                    continue;
                }
            }
            if (UNSAFE.compareAndSwapLong(this, ctlOffset, c, c | STOP_BIT))
                startTerminating();
        }
        if ((short)(c >>> TC_SHIFT) == -parallelism) { // signal when 0 workers
            final ReentrantLock lock = this.submissionLock;
            lock.lock();
            try {
                termination.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    /**
     * Runs up to three passes through workers: (0) Setting
     * termination status for each worker, followed by wakeups up to
     * queued workers; (1) helping cancel tasks; (2) interrupting
     * lagging threads (likely in external tasks, but possibly also
     * blocked in joins).  Each pass repeats previous steps because of
     * potential lagging thread creation.
     */
    private void startTerminating() {
        cancelSubmissions();
        for (int pass = 0; pass < 3; ++pass) {
            ForkJoinWorkerThread[] ws = workers;
            if (ws != null) {
                for (ForkJoinWorkerThread w : ws) {
                    if (w != null) {
                        w.terminate = true;
                        if (pass > 0) {
                            w.cancelTasks();
                            if (pass > 1 && !w.isInterrupted()) {
                                try {
                                    w.interrupt();
                                } catch (SecurityException ignore) {
                                }
                            }
                        }
                    }
                }
                terminateWaiters();
            }
        }
    }

    /**
     * Polls and cancels all submissions. Called only during termination.
     */
    private void cancelSubmissions() {
        while (queueBase != queueTop) {
            ForkJoinTask<?> task = pollSubmission();
            if (task != null) {
                try {
                    task.cancel(false);
                } catch (Throwable ignore) {
                }
            }
        }
    }

    /**
     * Tries to set the termination status of waiting workers, and
     * then wakes them up (after which they will terminate).
     */
    private void terminateWaiters() {
        ForkJoinWorkerThread[] ws = workers;
        if (ws != null) {
            ForkJoinWorkerThread w; long c; int i, e;
            int n = ws.length;
            while ((i = ~(e = (int)(c = ctl)) & SMASK) < n &&
                   (w = ws[i]) != null && w.eventCount == (e & E_MASK)) {
                if (UNSAFE.compareAndSwapLong(this, ctlOffset, c,
                                              (long)(w.nextWait & E_MASK) |
                                              ((c + AC_UNIT) & AC_MASK) |
                                              (c & (TC_MASK|STOP_BIT)))) {
                    w.terminate = true;
                    w.eventCount = e + EC_UNIT;
                    if (w.parked)
                        UNSAFE.unpark(w);
                }
            }
        }
    }

    // misc ForkJoinWorkerThread support

    /**
     * Increments or decrements quiescerCount. Needed only to prevent
     * triggering shutdown if a worker is transiently inactive while
     * checking quiescence.
     *
     * @param delta 1 for increment, -1 for decrement
     */
    final void addQuiescerCount(int delta) {
        int c;
        do {} while (!UNSAFE.compareAndSwapInt(this, quiescerCountOffset,
                                               c = quiescerCount, c + delta));
    }

    /**
     * Directly increments or decrements active count without queuing.
     * This method is used to transiently assert inactivation while
     * checking quiescence.
     *
     * @param delta 1 for increment, -1 for decrement
     */
    final void addActiveCount(int delta) {
        long d = (long)delta << AC_SHIFT;
        long c;
        do {} while (!UNSAFE.compareAndSwapLong(this, ctlOffset,
                                                c = ctl, c + d));
    }

    /**
     * Returns the approximate (non-atomic) number of idle threads per
     * active thread.
     */
    final int idlePerActive() {
        // Approximate at powers of two for small values, saturate past 4
        int p = parallelism;
        int a = p + (int)(ctl >> AC_SHIFT);
        return (a > (p >>>= 1) ? 0 :
                a > (p >>>= 1) ? 1 :
                a > (p >>>= 1) ? 2 :
                a > (p >>>= 1) ? 4 :
                8);
    }

    // Exported methods

    // Constructors

    /**
     * Creates a {@code ForkJoinPool} with parallelism equal to {@link
     * java.lang.Runtime#availableProcessors}, using the {@linkplain
     * #defaultForkJoinWorkerThreadFactory default thread factory},
     * no UncaughtExceptionHandler, and non-async LIFO processing mode.
     */
    public ForkJoinPool() {
        this(Runtime.getRuntime().availableProcessors(),
             defaultForkJoinWorkerThreadFactory, null, false);
    }

    /**
     * Creates a {@code ForkJoinPool} with the indicated parallelism
     * level, the {@linkplain
     * #defaultForkJoinWorkerThreadFactory default thread factory},
     * no UncaughtExceptionHandler, and non-async LIFO processing mode.
     *
     * @param parallelism the parallelism level
     * @throws IllegalArgumentException if parallelism less than or
     *         equal to zero, or greater than implementation limit
     */
    public ForkJoinPool(int parallelism) {
        this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
    }

    /**
     * Creates a {@code ForkJoinPool} with the given parameters.
     *
     * @param parallelism the parallelism level. For default value,
     * use {@link java.lang.Runtime#availableProcessors}.
     * @param factory the factory for creating new threads. For default value,
     * use {@link #defaultForkJoinWorkerThreadFactory}.
     * @param handler the handler for internal worker threads that
     * terminate due to unrecoverable errors encountered while executing
     * tasks. For default value, use {@code null}.
     * @param asyncMode if true,
     * establishes local first-in-first-out scheduling mode for forked
     * tasks that are never joined. This mode may be more appropriate
     * than default locally stack-based mode in applications in which
     * worker threads only process event-style asynchronous tasks.
     * For default value, use {@code false}.
     * @throws IllegalArgumentException if parallelism less than or
     *         equal to zero, or greater than implementation limit
     * @throws NullPointerException if the factory is null
     */
    public ForkJoinPool(int parallelism,
                        ForkJoinWorkerThreadFactory factory,
                        Thread.UncaughtExceptionHandler handler,
                        boolean asyncMode) {
        checkPermission();
        if (factory == null)
            throw new NullPointerException();
        if (parallelism <= 0 || parallelism > MAX_ID)
            throw new IllegalArgumentException();
        this.parallelism = parallelism;
        this.factory = factory;
        this.ueh = handler;
        this.locallyFifo = asyncMode;
        long np = (long)(-parallelism); // offset ctl counts
        this.ctl = ((np << AC_SHIFT) & AC_MASK) | ((np << TC_SHIFT) & TC_MASK);
        this.submissionQueue = new ForkJoinTask<?>[INITIAL_QUEUE_CAPACITY];
        // initialize workers array with room for 2*parallelism if possible
        int n = parallelism << 1;
        if (n >= MAX_ID)
            n = MAX_ID;
        else { // See Hackers Delight, sec 3.2, where n < (1 << 16)
            n |= n >>> 1; n |= n >>> 2; n |= n >>> 4; n |= n >>> 8;
        }
        workers = new ForkJoinWorkerThread[n + 1];
        this.submissionLock = new ReentrantLock();
        this.termination = submissionLock.newCondition();
        StringBuilder sb = new StringBuilder("ForkJoinPool-");
        sb.append(poolNumberGenerator.incrementAndGet());
        sb.append("-worker-");
        this.workerNamePrefix = sb.toString();
    }

    // Execution methods

    /**
     * Performs the given task, returning its result upon completion.
     * If the computation encounters an unchecked Exception or Error,
     * it is rethrown as the outcome of this invocation.  Rethrown
     * exceptions behave in the same way as regular exceptions, but,
     * when possible, contain stack traces (as displayed for example
     * using {@code ex.printStackTrace()}) of both the current thread
     * as well as the thread actually encountering the exception;
     * minimally only the latter.
     *
     * @param task the task
     * @return the task's result
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public <T> T invoke(ForkJoinTask<T> task) {
        Thread t = Thread.currentThread();
        if (task == null)
            throw new NullPointerException();
        if (shutdown)
            throw new RejectedExecutionException();
        if ((t instanceof ForkJoinWorkerThread) &&
            ((ForkJoinWorkerThread)t).pool == this)
            return task.invoke();  // bypass submit if in same pool
        else {
            addSubmission(task);
            return task.join();
        }
    }

    /**
     * Unless terminating, forks task if within an ongoing FJ
     * computation in the current pool, else submits as external task.
     */
    private <T> void forkOrSubmit(ForkJoinTask<T> task) {
        ForkJoinWorkerThread w;
        Thread t = Thread.currentThread();
        if (shutdown)
            throw new RejectedExecutionException();
        if ((t instanceof ForkJoinWorkerThread) &&
            (w = (ForkJoinWorkerThread)t).pool == this)
            w.pushTask(task);
        else
            addSubmission(task);
    }

    /**
     * Arranges for (asynchronous) execution of the given task.
     *
     * @param task the task
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public void execute(ForkJoinTask<?> task) {
        if (task == null)
            throw new NullPointerException();
        forkOrSubmit(task);
    }

    // AbstractExecutorService methods

    /**
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public void execute(Runnable task) {
        if (task == null)
            throw new NullPointerException();
        ForkJoinTask<?> job;
        if (task instanceof ForkJoinTask<?>) // avoid re-wrap
            job = (ForkJoinTask<?>) task;
        else
            job = ForkJoinTask.adapt(task, null);
        forkOrSubmit(job);
    }

    /**
     * Submits a ForkJoinTask for execution.
     *
     * @param task the task to submit
     * @return the task
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        if (task == null)
            throw new NullPointerException();
        forkOrSubmit(task);
        return task;
    }

    /**
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public <T> ForkJoinTask<T> submit(Callable<T> task) {
        if (task == null)
            throw new NullPointerException();
        ForkJoinTask<T> job = ForkJoinTask.adapt(task);
        forkOrSubmit(job);
        return job;
    }

    /**
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public <T> ForkJoinTask<T> submit(Runnable task, T result) {
        if (task == null)
            throw new NullPointerException();
        ForkJoinTask<T> job = ForkJoinTask.adapt(task, result);
        forkOrSubmit(job);
        return job;
    }

    /**
     * @throws NullPointerException if the task is null
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     */
    public ForkJoinTask<?> submit(Runnable task) {
        if (task == null)
            throw new NullPointerException();
        ForkJoinTask<?> job;
        if (task instanceof ForkJoinTask<?>) // avoid re-wrap
            job = (ForkJoinTask<?>) task;
        else
            job = ForkJoinTask.adapt(task, null);
        forkOrSubmit(job);
        return job;
    }

    /**
     * @throws NullPointerException       {@inheritDoc}
     * @throws RejectedExecutionException {@inheritDoc}
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        ArrayList<ForkJoinTask<T>> forkJoinTasks =
            new ArrayList<ForkJoinTask<T>>(tasks.size());
        for (Callable<T> task : tasks)
            forkJoinTasks.add(ForkJoinTask.adapt(task));
        invoke(new InvokeAll<T>(forkJoinTasks));

        @SuppressWarnings({"unchecked", "rawtypes"})
            List<Future<T>> futures = (List<Future<T>>) (List) forkJoinTasks;
        return futures;
    }

    static final class InvokeAll<T> extends RecursiveAction {
        final ArrayList<ForkJoinTask<T>> tasks;
        InvokeAll(ArrayList<ForkJoinTask<T>> tasks) { this.tasks = tasks; }
        public void compute() {
            try { invokeAll(tasks); }
            catch (Exception ignore) {}
        }
        private static final long serialVersionUID = -7914297376763021607L;
    }

    /**
     * Returns the factory used for constructing new workers.
     *
     * @return the factory used for constructing new workers
     */
    public ForkJoinWorkerThreadFactory getFactory() {
        return factory;
    }

    /**
     * Returns the handler for internal worker threads that terminate
     * due to unrecoverable errors encountered while executing tasks.
     *
     * @return the handler, or {@code null} if none
     */
    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return ueh;
    }

    /**
     * Returns the targeted parallelism level of this pool.
     *
     * @return the targeted parallelism level of this pool
     */
    public int getParallelism() {
        return parallelism;
    }

    /**
     * Returns the number of worker threads that have started but not
     * yet terminated.  The result returned by this method may differ
     * from {@link #getParallelism} when threads are created to
     * maintain parallelism when others are cooperatively blocked.
     *
     * @return the number of worker threads
     */
    public int getPoolSize() {
        return parallelism + (short)(ctl >>> TC_SHIFT);
    }

    /**
     * Returns {@code true} if this pool uses local first-in-first-out
     * scheduling mode for forked tasks that are never joined.
     *
     * @return {@code true} if this pool uses async mode
     */
    public boolean getAsyncMode() {
        return locallyFifo;
    }

    /**
     * Returns an estimate of the number of worker threads that are
     * not blocked waiting to join tasks or for other managed
     * synchronization. This method may overestimate the
     * number of running threads.
     *
     * @return the number of worker threads
     */
    public int getRunningThreadCount() {
        int r = parallelism + (int)(ctl >> AC_SHIFT);
        return (r <= 0) ? 0 : r; // suppress momentarily negative values
    }

    /**
     * Returns an estimate of the number of threads that are currently
     * stealing or executing tasks. This method may overestimate the
     * number of active threads.
     *
     * @return the number of active threads
     */
    public int getActiveThreadCount() {
        int r = parallelism + (int)(ctl >> AC_SHIFT) + blockedCount;
        return (r <= 0) ? 0 : r; // suppress momentarily negative values
    }

    /**
     * Returns {@code true} if all worker threads are currently idle.
     * An idle worker is one that cannot obtain a task to execute
     * because none are available to steal from other threads, and
     * there are no pending submissions to the pool. This method is
     * conservative; it might not return {@code true} immediately upon
     * idleness of all threads, but will eventually become true if
     * threads remain inactive.
     *
     * @return {@code true} if all threads are currently idle
     */
    public boolean isQuiescent() {
        return parallelism + (int)(ctl >> AC_SHIFT) + blockedCount == 0;
    }

    /**
     * Returns an estimate of the total number of tasks stolen from
     * one thread's work queue by another. The reported value
     * underestimates the actual total number of steals when the pool
     * is not quiescent. This value may be useful for monitoring and
     * tuning fork/join programs: in general, steal counts should be
     * high enough to keep threads busy, but low enough to avoid
     * overhead and contention across threads.
     *
     * @return the number of steals
     */
    public long getStealCount() {
        return stealCount;
    }

    /**
     * Returns an estimate of the total number of tasks currently held
     * in queues by worker threads (but not including tasks submitted
     * to the pool that have not begun executing). This value is only
     * an approximation, obtained by iterating across all threads in
     * the pool. This method may be useful for tuning task
     * granularities.
     *
     * @return the number of queued tasks
     */
    public long getQueuedTaskCount() {
        long count = 0;
        ForkJoinWorkerThread[] ws;
        if ((short)(ctl >>> TC_SHIFT) > -parallelism &&
            (ws = workers) != null) {
            for (ForkJoinWorkerThread w : ws)
                if (w != null)
                    count -= w.queueBase - w.queueTop; // must read base first
        }
        return count;
    }

    /**
     * Returns an estimate of the number of tasks submitted to this
     * pool that have not yet begun executing.  This method may take
     * time proportional to the number of submissions.
     *
     * @return the number of queued submissions
     */
    public int getQueuedSubmissionCount() {
        return -queueBase + queueTop;
    }

    /**
     * Returns {@code true} if there are any tasks submitted to this
     * pool that have not yet begun executing.
     *
     * @return {@code true} if there are any queued submissions
     */
    public boolean hasQueuedSubmissions() {
        return queueBase != queueTop;
    }

    /**
     * Removes and returns the next unexecuted submission if one is
     * available.  This method may be useful in extensions to this
     * class that re-assign work in systems with multiple pools.
     *
     * @return the next submission, or {@code null} if none
     */
    protected ForkJoinTask<?> pollSubmission() {
        ForkJoinTask<?> t; ForkJoinTask<?>[] q; int b, i;
        while ((b = queueBase) != queueTop &&
               (q = submissionQueue) != null &&
               (i = (q.length - 1) & b) >= 0) {
            long u = (i << ASHIFT) + ABASE;
            if ((t = q[i]) != null &&
                queueBase == b &&
                UNSAFE.compareAndSwapObject(q, u, t, null)) {
                queueBase = b + 1;
                return t;
            }
        }
        return null;
    }

    /**
     * Removes all available unexecuted submitted and forked tasks
     * from scheduling queues and adds them to the given collection,
     * without altering their execution status. These may include
     * artificially generated or wrapped tasks. This method is
     * designed to be invoked only when the pool is known to be
     * quiescent. Invocations at other times may not remove all
     * tasks. A failure encountered while attempting to add elements
     * to collection {@code c} may result in elements being in
     * neither, either or both collections when the associated
     * exception is thrown.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.
     *
     * @param c the collection to transfer elements into
     * @return the number of elements transferred
     */
    protected int drainTasksTo(Collection<? super ForkJoinTask<?>> c) {
        int count = 0;
        while (queueBase != queueTop) {
            ForkJoinTask<?> t = pollSubmission();
            if (t != null) {
                c.add(t);
                ++count;
            }
        }
        ForkJoinWorkerThread[] ws;
        if ((short)(ctl >>> TC_SHIFT) > -parallelism &&
            (ws = workers) != null) {
            for (ForkJoinWorkerThread w : ws)
                if (w != null)
                    count += w.drainTasksTo(c);
        }
        return count;
    }

    /**
     * Returns a string identifying this pool, as well as its state,
     * including indications of run state, parallelism level, and
     * worker and task counts.
     *
     * @return a string identifying this pool, as well as its state
     */
    public String toString() {
        long st = getStealCount();
        long qt = getQueuedTaskCount();
        long qs = getQueuedSubmissionCount();
        int pc = parallelism;
        long c = ctl;
        int tc = pc + (short)(c >>> TC_SHIFT);
        int rc = pc + (int)(c >> AC_SHIFT);
        if (rc < 0) // ignore transient negative
            rc = 0;
        int ac = rc + blockedCount;
        String level;
        if ((c & STOP_BIT) != 0)
            level = (tc == 0) ? "Terminated" : "Terminating";
        else
            level = shutdown ? "Shutting down" : "Running";
        return super.toString() +
            "[" + level +
            ", parallelism = " + pc +
            ", size = " + tc +
            ", active = " + ac +
            ", running = " + rc +
            ", steals = " + st +
            ", tasks = " + qt +
            ", submissions = " + qs +
            "]";
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     * Tasks that are in the process of being submitted concurrently
     * during the course of this method may or may not be rejected.
     */
    public void shutdown() {
        checkPermission();
        shutdown = true;
        tryTerminate(false);
    }

    /**
     * Attempts to cancel and/or stop all tasks, and reject all
     * subsequently submitted tasks.  Tasks that are in the process of
     * being submitted or executed concurrently during the course of
     * this method may or may not be rejected. This method cancels
     * both existing and unexecuted tasks, in order to permit
     * termination in the presence of task dependencies. So the method
     * always returns an empty list (unlike the case for some other
     * Executors).
     *
     * @return an empty list
     */
    public List<Runnable> shutdownNow() {
        checkPermission();
        shutdown = true;
        tryTerminate(true);
        return Collections.emptyList();
    }

    /**
     * Returns {@code true} if all tasks have completed following shut down.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    public boolean isTerminated() {
        long c = ctl;
        return ((c & STOP_BIT) != 0L &&
                (short)(c >>> TC_SHIFT) == -parallelism);
    }

    /**
     * Returns {@code true} if the process of termination has
     * commenced but not yet completed.  This method may be useful for
     * debugging. A return of {@code true} reported a sufficient
     * period after shutdown may indicate that submitted tasks have
     * ignored or suppressed interruption, or are waiting for IO,
     * causing this executor not to properly terminate. (See the
     * advisory notes for class {@link ForkJoinTask} stating that
     * tasks should not normally entail blocking operations.  But if
     * they do, they must abort them on interrupt.)
     *
     * @return {@code true} if terminating but not yet terminated
     */
    public boolean isTerminating() {
        long c = ctl;
        return ((c & STOP_BIT) != 0L &&
                (short)(c >>> TC_SHIFT) != -parallelism);
    }

    /**
     * Returns true if terminating or terminated. Used by ForkJoinWorkerThread.
     */
    final boolean isAtLeastTerminating() {
        return (ctl & STOP_BIT) != 0L;
    }

    /**
     * Returns {@code true} if this pool has been shut down.
     *
     * @return {@code true} if this pool has been shut down
     */
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     */
    public boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock lock = this.submissionLock;
        lock.lock();
        try {
            for (;;) {
                if (isTerminated())
                    return true;
                if (nanos <= 0)
                    return false;
                nanos = termination.awaitNanos(nanos);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Interface for extending managed parallelism for tasks running
     * in {@link ForkJoinPool}s.
     *
     * <p>A {@code ManagedBlocker} provides two methods.  Method
     * {@code isReleasable} must return {@code true} if blocking is
     * not necessary. Method {@code block} blocks the current thread
     * if necessary (perhaps internally invoking {@code isReleasable}
     * before actually blocking). These actions are performed by any
     * thread invoking {@link ForkJoinPool#managedBlock}.  The
     * unusual methods in this API accommodate synchronizers that may,
     * but don't usually, block for long periods. Similarly, they
     * allow more efficient internal handling of cases in which
     * additional workers may be, but usually are not, needed to
     * ensure sufficient parallelism.  Toward this end,
     * implementations of method {@code isReleasable} must be amenable
     * to repeated invocation.
     *
     * <p>For example, here is a ManagedBlocker based on a
     * ReentrantLock:
     *  <pre> {@code
     * class ManagedLocker implements ManagedBlocker {
     *   final ReentrantLock lock;
     *   boolean hasLock = false;
     *   ManagedLocker(ReentrantLock lock) { this.lock = lock; }
     *   public boolean block() {
     *     if (!hasLock)
     *       lock.lock();
     *     return true;
     *   }
     *   public boolean isReleasable() {
     *     return hasLock || (hasLock = lock.tryLock());
     *   }
     * }}</pre>
     *
     * <p>Here is a class that possibly blocks waiting for an
     * item on a given queue:
     *  <pre> {@code
     * class QueueTaker<E> implements ManagedBlocker {
     *   final BlockingQueue<E> queue;
     *   volatile E item = null;
     *   QueueTaker(BlockingQueue<E> q) { this.queue = q; }
     *   public boolean block() throws InterruptedException {
     *     if (item == null)
     *       item = queue.take();
     *     return true;
     *   }
     *   public boolean isReleasable() {
     *     return item != null || (item = queue.poll()) != null;
     *   }
     *   public E getItem() { // call after pool.managedBlock completes
     *     return item;
     *   }
     * }}</pre>
     */
    public static interface ManagedBlocker {
        /**
         * Possibly blocks the current thread, for example waiting for
         * a lock or condition.
         *
         * @return {@code true} if no additional blocking is necessary
         * (i.e., if isReleasable would return true)
         * @throws InterruptedException if interrupted while waiting
         * (the method is not required to do so, but is allowed to)
         */
        boolean block() throws InterruptedException;

        /**
         * Returns {@code true} if blocking is unnecessary.
         */
        boolean isReleasable();
    }

    /**
     * Blocks in accord with the given blocker.  If the current thread
     * is a {@link ForkJoinWorkerThread}, this method possibly
     * arranges for a spare thread to be activated if necessary to
     * ensure sufficient parallelism while the current thread is blocked.
     *
     * <p>If the caller is not a {@link ForkJoinTask}, this method is
     * behaviorally equivalent to
     *  <pre> {@code
     * while (!blocker.isReleasable())
     *   if (blocker.block())
     *     return;
     * }</pre>
     *
     * If the caller is a {@code ForkJoinTask}, then the pool may
     * first be expanded to ensure parallelism, and later adjusted.
     *
     * @param blocker the blocker
     * @throws InterruptedException if blocker.block did so
     */
    public static void managedBlock(ManagedBlocker blocker)
        throws InterruptedException {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread w = (ForkJoinWorkerThread) t;
            w.pool.awaitBlocker(blocker);
        }
        else {
            do {} while (!blocker.isReleasable() && !blocker.block());
        }
    }

    // AbstractExecutorService overrides.  These rely on undocumented
    // fact that ForkJoinTask.adapt returns ForkJoinTasks that also
    // implement RunnableFuture.

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return (RunnableFuture<T>) ForkJoinTask.adapt(runnable, value);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return (RunnableFuture<T>) ForkJoinTask.adapt(callable);
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long ctlOffset;
    private static final long stealCountOffset;
    private static final long blockedCountOffset;
    private static final long quiescerCountOffset;
    private static final long scanGuardOffset;
    private static final long nextWorkerNumberOffset;
    private static final long ABASE;
    private static final int ASHIFT;

    static {
        poolNumberGenerator = new AtomicInteger();
        workerSeedGenerator = new Random();
        modifyThreadPermission = new RuntimePermission("modifyThread");
        defaultForkJoinWorkerThreadFactory =
            new DefaultForkJoinWorkerThreadFactory();
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ForkJoinPool.class;
            ctlOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("ctl"));
            stealCountOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("stealCount"));
            blockedCountOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("blockedCount"));
            quiescerCountOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("quiescerCount"));
            scanGuardOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("scanGuard"));
            nextWorkerNumberOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("nextWorkerNumber"));
        } catch (Exception e) {
            throw new Error(e);
        }
        Class<?> a = ForkJoinTask[].class;
        ABASE = UNSAFE.arrayBaseOffset(a);
        int s = UNSAFE.arrayIndexScale(a);
        if ((s & (s-1)) != 0)
            throw new Error("data type scale not a power of two");
        ASHIFT = 31 - Integer.numberOfLeadingZeros(s);
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinTask.java b/luni/src/main/java/java/util/concurrent/ForkJoinTask.java
new file mode 100644
//Synthetic comment -- index 0000000..86a29d7

//Synthetic comment -- @@ -0,0 +1,1357 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.reflect.Constructor;
import libcore.util.SneakyThrow;

/**
 * Abstract base class for tasks that run within a {@link ForkJoinPool}.
 * A {@code ForkJoinTask} is a thread-like entity that is much
 * lighter weight than a normal thread.  Huge numbers of tasks and
 * subtasks may be hosted by a small number of actual threads in a
 * ForkJoinPool, at the price of some usage limitations.
 *
 * <p>A "main" {@code ForkJoinTask} begins execution when submitted
 * to a {@link ForkJoinPool}.  Once started, it will usually in turn
 * start other subtasks.  As indicated by the name of this class,
 * many programs using {@code ForkJoinTask} employ only methods
 * {@link #fork} and {@link #join}, or derivatives such as {@link
 * #invokeAll(ForkJoinTask...) invokeAll}.  However, this class also
 * provides a number of other methods that can come into play in
 * advanced usages, as well as extension mechanics that allow
 * support of new forms of fork/join processing.
 *
 * <p>A {@code ForkJoinTask} is a lightweight form of {@link Future}.
 * The efficiency of {@code ForkJoinTask}s stems from a set of
 * restrictions (that are only partially statically enforceable)
 * reflecting their intended use as computational tasks calculating
 * pure functions or operating on purely isolated objects.  The
 * primary coordination mechanisms are {@link #fork}, that arranges
 * asynchronous execution, and {@link #join}, that doesn't proceed
 * until the task's result has been computed.  Computations should
 * avoid {@code synchronized} methods or blocks, and should minimize
 * other blocking synchronization apart from joining other tasks or
 * using synchronizers such as Phasers that are advertised to
 * cooperate with fork/join scheduling. Tasks should also not perform
 * blocking IO, and should ideally access variables that are
 * completely independent of those accessed by other running
 * tasks. Minor breaches of these restrictions, for example using
 * shared output streams, may be tolerable in practice, but frequent
 * use may result in poor performance, and the potential to
 * indefinitely stall if the number of threads not waiting for IO or
 * other external synchronization becomes exhausted. This usage
 * restriction is in part enforced by not permitting checked
 * exceptions such as {@code IOExceptions} to be thrown. However,
 * computations may still encounter unchecked exceptions, that are
 * rethrown to callers attempting to join them. These exceptions may
 * additionally include {@link RejectedExecutionException} stemming
 * from internal resource exhaustion, such as failure to allocate
 * internal task queues. Rethrown exceptions behave in the same way as
 * regular exceptions, but, when possible, contain stack traces (as
 * displayed for example using {@code ex.printStackTrace()}) of both
 * the thread that initiated the computation as well as the thread
 * actually encountering the exception; minimally only the latter.
 *
 * <p>The primary method for awaiting completion and extracting
 * results of a task is {@link #join}, but there are several variants:
 * The {@link Future#get} methods support interruptible and/or timed
 * waits for completion and report results using {@code Future}
 * conventions. Method {@link #invoke} is semantically
 * equivalent to {@code fork(); join()} but always attempts to begin
 * execution in the current thread. The "<em>quiet</em>" forms of
 * these methods do not extract results or report exceptions. These
 * may be useful when a set of tasks are being executed, and you need
 * to delay processing of results or exceptions until all complete.
 * Method {@code invokeAll} (available in multiple versions)
 * performs the most common form of parallel invocation: forking a set
 * of tasks and joining them all.
 *
 * <p>The execution status of tasks may be queried at several levels
 * of detail: {@link #isDone} is true if a task completed in any way
 * (including the case where a task was cancelled without executing);
 * {@link #isCompletedNormally} is true if a task completed without
 * cancellation or encountering an exception; {@link #isCancelled} is
 * true if the task was cancelled (in which case {@link #getException}
 * returns a {@link java.util.concurrent.CancellationException}); and
 * {@link #isCompletedAbnormally} is true if a task was either
 * cancelled or encountered an exception, in which case {@link
 * #getException} will return either the encountered exception or
 * {@link java.util.concurrent.CancellationException}.
 *
 * <p>The ForkJoinTask class is not usually directly subclassed.
 * Instead, you subclass one of the abstract classes that support a
 * particular style of fork/join processing, typically {@link
 * RecursiveAction} for computations that do not return results, or
 * {@link RecursiveTask} for those that do.  Normally, a concrete
 * ForkJoinTask subclass declares fields comprising its parameters,
 * established in a constructor, and then defines a {@code compute}
 * method that somehow uses the control methods supplied by this base
 * class. While these methods have {@code public} access (to allow
 * instances of different task subclasses to call each other's
 * methods), some of them may only be called from within other
 * ForkJoinTasks (as may be determined using method {@link
 * #inForkJoinPool}).  Attempts to invoke them in other contexts
 * result in exceptions or errors, possibly including
 * {@code ClassCastException}.
 *
 * <p>Method {@link #join} and its variants are appropriate for use
 * only when completion dependencies are acyclic; that is, the
 * parallel computation can be described as a directed acyclic graph
 * (DAG). Otherwise, executions may encounter a form of deadlock as
 * tasks cyclically wait for each other.  However, this framework
 * supports other methods and techniques (for example the use of
 * {@link Phaser}, {@link #helpQuiesce}, and {@link #complete}) that
 * may be of use in constructing custom subclasses for problems that
 * are not statically structured as DAGs.
 *
 * <p>Most base support methods are {@code final}, to prevent
 * overriding of implementations that are intrinsically tied to the
 * underlying lightweight task scheduling framework.  Developers
 * creating new basic styles of fork/join processing should minimally
 * implement {@code protected} methods {@link #exec}, {@link
 * #setRawResult}, and {@link #getRawResult}, while also introducing
 * an abstract computational method that can be implemented in its
 * subclasses, possibly relying on other {@code protected} methods
 * provided by this class.
 *
 * <p>ForkJoinTasks should perform relatively small amounts of
 * computation. Large tasks should be split into smaller subtasks,
 * usually via recursive decomposition. As a very rough rule of thumb,
 * a task should perform more than 100 and less than 10000 basic
 * computational steps, and should avoid indefinite looping. If tasks
 * are too big, then parallelism cannot improve throughput. If too
 * small, then memory and internal task maintenance overhead may
 * overwhelm processing.
 *
 * <p>This class provides {@code adapt} methods for {@link Runnable}
 * and {@link Callable}, that may be of use when mixing execution of
 * {@code ForkJoinTasks} with other kinds of tasks. When all tasks are
 * of this form, consider using a pool constructed in <em>asyncMode</em>.
 *
 * <p>ForkJoinTasks are {@code Serializable}, which enables them to be
 * used in extensions such as remote execution frameworks. It is
 * sensible to serialize tasks only before or after, but not during,
 * execution. Serialization is not relied on during execution itself.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public abstract class ForkJoinTask<V> implements Future<V>, Serializable {

    /*
     * See the internal documentation of class ForkJoinPool for a
     * general implementation overview.  ForkJoinTasks are mainly
     * responsible for maintaining their "status" field amidst relays
     * to methods in ForkJoinWorkerThread and ForkJoinPool.
     *
     * The methods of this class are more-or-less layered into
     * (1) basic status maintenance
     * (2) execution and awaiting completion
     * (3) user-level methods that additionally report results.
     * This is sometimes hard to see because this file orders exported
     * methods in a way that flows well in javadocs.
     */

    /*
     * The status field holds run control status bits packed into a
     * single int to minimize footprint and to ensure atomicity (via
     * CAS).  Status is initially zero, and takes on nonnegative
     * values until completed, upon which status holds value
     * NORMAL, CANCELLED, or EXCEPTIONAL. Tasks undergoing blocking
     * waits by other threads have the SIGNAL bit set.  Completion of
     * a stolen task with SIGNAL set awakens any waiters via
     * notifyAll. Even though suboptimal for some purposes, we use
     * basic builtin wait/notify to take advantage of "monitor
     * inflation" in JVMs that we would otherwise need to emulate to
     * avoid adding further per-task bookkeeping overhead.  We want
     * these monitors to be "fat", i.e., not use biasing or thin-lock
     * techniques, so use some odd coding idioms that tend to avoid
     * them.
     */

    /** The run status of this task */
    volatile int status; // accessed directly by pool and workers
    private static final int NORMAL      = -1;
    private static final int CANCELLED   = -2;
    private static final int EXCEPTIONAL = -3;
    private static final int SIGNAL      =  1;

    /**
     * Marks completion and wakes up threads waiting to join this task,
     * also clearing signal request bits.
     *
     * @param completion one of NORMAL, CANCELLED, EXCEPTIONAL
     * @return completion status on exit
     */
    private int setCompletion(int completion) {
        for (int s;;) {
            if ((s = status) < 0)
                return s;
            if (UNSAFE.compareAndSwapInt(this, statusOffset, s, completion)) {
                if (s != 0)
                    synchronized (this) { notifyAll(); }
                return completion;
            }
        }
    }

    /**
     * Tries to block a worker thread until completed or timed out.
     * Uses Object.wait time argument conventions.
     * May fail on contention or interrupt.
     *
     * @param millis if > 0, wait time.
     */
    final void tryAwaitDone(long millis) {
        int s;
        try {
            if (((s = status) > 0 ||
                 (s == 0 &&
                  UNSAFE.compareAndSwapInt(this, statusOffset, 0, SIGNAL))) &&
                status > 0) {
                synchronized (this) {
                    if (status > 0)
                        wait(millis);
                }
            }
        } catch (InterruptedException ie) {
            // caller must check termination
        }
    }

    /**
     * Blocks a non-worker-thread until completion.
     * @return status upon completion
     */
    private int externalAwaitDone() {
        int s;
        if ((s = status) >= 0) {
            boolean interrupted = false;
            synchronized (this) {
                while ((s = status) >= 0) {
                    if (s == 0)
                        UNSAFE.compareAndSwapInt(this, statusOffset,
                                                 0, SIGNAL);
                    else {
                        try {
                            wait();
                        } catch (InterruptedException ie) {
                            interrupted = true;
                        }
                    }
                }
            }
            if (interrupted)
                Thread.currentThread().interrupt();
        }
        return s;
    }

    /**
     * Blocks a non-worker-thread until completion or interruption or timeout.
     */
    private int externalInterruptibleAwaitDone(long millis)
        throws InterruptedException {
        int s;
        if (Thread.interrupted())
            throw new InterruptedException();
        if ((s = status) >= 0) {
            synchronized (this) {
                while ((s = status) >= 0) {
                    if (s == 0)
                        UNSAFE.compareAndSwapInt(this, statusOffset,
                                                 0, SIGNAL);
                    else {
                        wait(millis);
                        if (millis > 0L)
                            break;
                    }
                }
            }
        }
        return s;
    }

    /**
     * Primary execution method for stolen tasks. Unless done, calls
     * exec and records status if completed, but doesn't wait for
     * completion otherwise.
     */
    final void doExec() {
        if (status >= 0) {
            boolean completed;
            try {
                completed = exec();
            } catch (Throwable rex) {
                setExceptionalCompletion(rex);
                return;
            }
            if (completed)
                setCompletion(NORMAL); // must be outside try block
        }
    }

    /**
     * Primary mechanics for join, get, quietlyJoin.
     * @return status upon completion
     */
    private int doJoin() {
        Thread t; ForkJoinWorkerThread w; int s; boolean completed;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            if ((s = status) < 0)
                return s;
            if ((w = (ForkJoinWorkerThread)t).unpushTask(this)) {
                try {
                    completed = exec();
                } catch (Throwable rex) {
                    return setExceptionalCompletion(rex);
                }
                if (completed)
                    return setCompletion(NORMAL);
            }
            return w.joinTask(this);
        }
        else
            return externalAwaitDone();
    }

    /**
     * Primary mechanics for invoke, quietlyInvoke.
     * @return status upon completion
     */
    private int doInvoke() {
        int s; boolean completed;
        if ((s = status) < 0)
            return s;
        try {
            completed = exec();
        } catch (Throwable rex) {
            return setExceptionalCompletion(rex);
        }
        if (completed)
            return setCompletion(NORMAL);
        else
            return doJoin();
    }

    // Exception table support

    /**
     * Table of exceptions thrown by tasks, to enable reporting by
     * callers. Because exceptions are rare, we don't directly keep
     * them with task objects, but instead use a weak ref table.  Note
     * that cancellation exceptions don't appear in the table, but are
     * instead recorded as status values.
     *
     * Note: These statics are initialized below in static block.
     */
    private static final ExceptionNode[] exceptionTable;
    private static final ReentrantLock exceptionTableLock;
    private static final ReferenceQueue<Object> exceptionTableRefQueue;

    /**
     * Fixed capacity for exceptionTable.
     */
    private static final int EXCEPTION_MAP_CAPACITY = 32;

    /**
     * Key-value nodes for exception table.  The chained hash table
     * uses identity comparisons, full locking, and weak references
     * for keys. The table has a fixed capacity because it only
     * maintains task exceptions long enough for joiners to access
     * them, so should never become very large for sustained
     * periods. However, since we do not know when the last joiner
     * completes, we must use weak references and expunge them. We do
     * so on each operation (hence full locking). Also, some thread in
     * any ForkJoinPool will call helpExpungeStaleExceptions when its
     * pool becomes isQuiescent.
     */
    static final class ExceptionNode extends WeakReference<ForkJoinTask<?>>{
        final Throwable ex;
        ExceptionNode next;
        final long thrower;  // use id not ref to avoid weak cycles
        ExceptionNode(ForkJoinTask<?> task, Throwable ex, ExceptionNode next) {
            super(task, exceptionTableRefQueue);
            this.ex = ex;
            this.next = next;
            this.thrower = Thread.currentThread().getId();
        }
    }

    /**
     * Records exception and sets exceptional completion.
     *
     * @return status on exit
     */
    private int setExceptionalCompletion(Throwable ex) {
        int h = System.identityHashCode(this);
        final ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] t = exceptionTable;
            int i = h & (t.length - 1);
            for (ExceptionNode e = t[i]; ; e = e.next) {
                if (e == null) {
                    t[i] = new ExceptionNode(this, ex, t[i]);
                    break;
                }
                if (e.get() == this) // already present
                    break;
            }
        } finally {
            lock.unlock();
        }
        return setCompletion(EXCEPTIONAL);
    }

    /**
     * Removes exception node and clears status
     */
    private void clearExceptionalCompletion() {
        int h = System.identityHashCode(this);
        final ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            ExceptionNode[] t = exceptionTable;
            int i = h & (t.length - 1);
            ExceptionNode e = t[i];
            ExceptionNode pred = null;
            while (e != null) {
                ExceptionNode next = e.next;
                if (e.get() == this) {
                    if (pred == null)
                        t[i] = next;
                    else
                        pred.next = next;
                    break;
                }
                pred = e;
                e = next;
            }
            expungeStaleExceptions();
            status = 0;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns a rethrowable exception for the given task, if
     * available. To provide accurate stack traces, if the exception
     * was not thrown by the current thread, we try to create a new
     * exception of the same type as the one thrown, but with the
     * recorded exception as its cause. If there is no such
     * constructor, we instead try to use a no-arg constructor,
     * followed by initCause, to the same effect. If none of these
     * apply, or any fail due to other exceptions, we return the
     * recorded exception, which is still correct, although it may
     * contain a misleading stack trace.
     *
     * @return the exception, or null if none
     */
    private Throwable getThrowableException() {
        if (status != EXCEPTIONAL)
            return null;
        int h = System.identityHashCode(this);
        ExceptionNode e;
        final ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] t = exceptionTable;
            e = t[h & (t.length - 1)];
            while (e != null && e.get() != this)
                e = e.next;
        } finally {
            lock.unlock();
        }
        Throwable ex;
        if (e == null || (ex = e.ex) == null)
            return null;
        if (e.thrower != Thread.currentThread().getId()) {
            Class<? extends Throwable> ec = ex.getClass();
            try {
                Constructor<?> noArgCtor = null;
                Constructor<?>[] cs = ec.getConstructors();// public ctors only
                for (int i = 0; i < cs.length; ++i) {
                    Constructor<?> c = cs[i];
                    Class<?>[] ps = c.getParameterTypes();
                    if (ps.length == 0)
                        noArgCtor = c;
                    else if (ps.length == 1 && ps[0] == Throwable.class)
                        return (Throwable)(c.newInstance(ex));
                }
                if (noArgCtor != null) {
                    Throwable wx = (Throwable)(noArgCtor.newInstance());
                    wx.initCause(ex);
                    return wx;
                }
            } catch (Exception ignore) {
            }
        }
        return ex;
    }

    /**
     * Poll stale refs and remove them. Call only while holding lock.
     */
    private static void expungeStaleExceptions() {
        for (Object x; (x = exceptionTableRefQueue.poll()) != null;) {
            if (x instanceof ExceptionNode) {
                ForkJoinTask<?> key = ((ExceptionNode)x).get();
                ExceptionNode[] t = exceptionTable;
                int i = System.identityHashCode(key) & (t.length - 1);
                ExceptionNode e = t[i];
                ExceptionNode pred = null;
                while (e != null) {
                    ExceptionNode next = e.next;
                    if (e == x) {
                        if (pred == null)
                            t[i] = next;
                        else
                            pred.next = next;
                        break;
                    }
                    pred = e;
                    e = next;
                }
            }
        }
    }

    /**
     * If lock is available, poll stale refs and remove them.
     * Called from ForkJoinPool when pools become quiescent.
     */
    static final void helpExpungeStaleExceptions() {
        final ReentrantLock lock = exceptionTableLock;
        if (lock.tryLock()) {
            try {
                expungeStaleExceptions();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Report the result of invoke or join; called only upon
     * non-normal return of internal versions.
     */
    private V reportResult() {
        int s; Throwable ex;
        if ((s = status) == CANCELLED)
            throw new CancellationException();
        if (s == EXCEPTIONAL && (ex = getThrowableException()) != null)
            SneakyThrow.sneakyThrow(ex); // android-changed
        return getRawResult();
    }

    // public methods

    /**
     * Arranges to asynchronously execute this task.  While it is not
     * necessarily enforced, it is a usage error to fork a task more
     * than once unless it has completed and been reinitialized.
     * Subsequent modifications to the state of this task or any data
     * it operates on are not necessarily consistently observable by
     * any thread other than the one executing it unless preceded by a
     * call to {@link #join} or related methods, or a call to {@link
     * #isDone} returning {@code true}.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return {@code this}, to simplify usage
     */
    public final ForkJoinTask<V> fork() {
        ((ForkJoinWorkerThread) Thread.currentThread())
            .pushTask(this);
        return this;
    }

    /**
     * Returns the result of the computation when it {@link #isDone is
     * done}.  This method differs from {@link #get()} in that
     * abnormal completion results in {@code RuntimeException} or
     * {@code Error}, not {@code ExecutionException}, and that
     * interrupts of the calling thread do <em>not</em> cause the
     * method to abruptly return by throwing {@code
     * InterruptedException}.
     *
     * @return the computed result
     */
    public final V join() {
        if (doJoin() != NORMAL)
            return reportResult();
        else
            return getRawResult();
    }

    /**
     * Commences performing this task, awaits its completion if
     * necessary, and returns its result, or throws an (unchecked)
     * {@code RuntimeException} or {@code Error} if the underlying
     * computation did so.
     *
     * @return the computed result
     */
    public final V invoke() {
        if (doInvoke() != NORMAL)
            return reportResult();
        else
            return getRawResult();
    }

    /**
     * Forks the given tasks, returning when {@code isDone} holds for
     * each task or an (unchecked) exception is encountered, in which
     * case the exception is rethrown. If more than one task
     * encounters an exception, then this method throws any one of
     * these exceptions. If any task encounters an exception, the
     * other may be cancelled. However, the execution status of
     * individual tasks is not guaranteed upon exceptional return. The
     * status of each task may be obtained using {@link
     * #getException()} and related methods to check if they have been
     * cancelled, completed normally or exceptionally, or left
     * unprocessed.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @param t1 the first task
     * @param t2 the second task
     * @throws NullPointerException if any task is null
     */
    public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) {
        t2.fork();
        t1.invoke();
        t2.join();
    }

    /**
     * Forks the given tasks, returning when {@code isDone} holds for
     * each task or an (unchecked) exception is encountered, in which
     * case the exception is rethrown. If more than one task
     * encounters an exception, then this method throws any one of
     * these exceptions. If any task encounters an exception, others
     * may be cancelled. However, the execution status of individual
     * tasks is not guaranteed upon exceptional return. The status of
     * each task may be obtained using {@link #getException()} and
     * related methods to check if they have been cancelled, completed
     * normally or exceptionally, or left unprocessed.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @param tasks the tasks
     * @throws NullPointerException if any task is null
     */
    public static void invokeAll(ForkJoinTask<?>... tasks) {
        Throwable ex = null;
        int last = tasks.length - 1;
        for (int i = last; i >= 0; --i) {
            ForkJoinTask<?> t = tasks[i];
            if (t == null) {
                if (ex == null)
                    ex = new NullPointerException();
            }
            else if (i != 0)
                t.fork();
            else if (t.doInvoke() < NORMAL && ex == null)
                ex = t.getException();
        }
        for (int i = 1; i <= last; ++i) {
            ForkJoinTask<?> t = tasks[i];
            if (t != null) {
                if (ex != null)
                    t.cancel(false);
                else if (t.doJoin() < NORMAL)
                    ex = t.getException();
            }
        }
        if (ex != null)
            SneakyThrow.sneakyThrow(ex); // android-changed
    }

    /**
     * Forks all tasks in the specified collection, returning when
     * {@code isDone} holds for each task or an (unchecked) exception
     * is encountered, in which case the exception is rethrown. If
     * more than one task encounters an exception, then this method
     * throws any one of these exceptions. If any task encounters an
     * exception, others may be cancelled. However, the execution
     * status of individual tasks is not guaranteed upon exceptional
     * return. The status of each task may be obtained using {@link
     * #getException()} and related methods to check if they have been
     * cancelled, completed normally or exceptionally, or left
     * unprocessed.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @param tasks the collection of tasks
     * @return the tasks argument, to simplify usage
     * @throws NullPointerException if tasks or any element are null
     */
    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> tasks) {
        if (!(tasks instanceof RandomAccess) || !(tasks instanceof List<?>)) {
            invokeAll(tasks.toArray(new ForkJoinTask<?>[tasks.size()]));
            return tasks;
        }
        @SuppressWarnings("unchecked")
        List<? extends ForkJoinTask<?>> ts =
            (List<? extends ForkJoinTask<?>>) tasks;
        Throwable ex = null;
        int last = ts.size() - 1;
        for (int i = last; i >= 0; --i) {
            ForkJoinTask<?> t = ts.get(i);
            if (t == null) {
                if (ex == null)
                    ex = new NullPointerException();
            }
            else if (i != 0)
                t.fork();
            else if (t.doInvoke() < NORMAL && ex == null)
                ex = t.getException();
        }
        for (int i = 1; i <= last; ++i) {
            ForkJoinTask<?> t = ts.get(i);
            if (t != null) {
                if (ex != null)
                    t.cancel(false);
                else if (t.doJoin() < NORMAL)
                    ex = t.getException();
            }
        }
        if (ex != null)
            SneakyThrow.sneakyThrow(ex); // android-changed
        return tasks;
    }

    /**
     * Attempts to cancel execution of this task. This attempt will
     * fail if the task has already completed or could not be
     * cancelled for some other reason. If successful, and this task
     * has not started when {@code cancel} is called, execution of
     * this task is suppressed. After this method returns
     * successfully, unless there is an intervening call to {@link
     * #reinitialize}, subsequent calls to {@link #isCancelled},
     * {@link #isDone}, and {@code cancel} will return {@code true}
     * and calls to {@link #join} and related methods will result in
     * {@code CancellationException}.
     *
     * <p>This method may be overridden in subclasses, but if so, must
     * still ensure that these properties hold. In particular, the
     * {@code cancel} method itself must not throw exceptions.
     *
     * <p>This method is designed to be invoked by <em>other</em>
     * tasks. To terminate the current task, you can just return or
     * throw an unchecked exception from its computation method, or
     * invoke {@link #completeExceptionally}.
     *
     * @param mayInterruptIfRunning this value has no effect in the
     * default implementation because interrupts are not used to
     * control cancellation.
     *
     * @return {@code true} if this task is now cancelled
     */
    public boolean cancel(boolean mayInterruptIfRunning) {
        return setCompletion(CANCELLED) == CANCELLED;
    }

    /**
     * Cancels, ignoring any exceptions thrown by cancel. Used during
     * worker and pool shutdown. Cancel is spec'ed not to throw any
     * exceptions, but if it does anyway, we have no recourse during
     * shutdown, so guard against this case.
     */
    final void cancelIgnoringExceptions() {
        try {
            cancel(false);
        } catch (Throwable ignore) {
        }
    }

    public final boolean isDone() {
        return status < 0;
    }

    public final boolean isCancelled() {
        return status == CANCELLED;
    }

    /**
     * Returns {@code true} if this task threw an exception or was cancelled.
     *
     * @return {@code true} if this task threw an exception or was cancelled
     */
    public final boolean isCompletedAbnormally() {
        return status < NORMAL;
    }

    /**
     * Returns {@code true} if this task completed without throwing an
     * exception and was not cancelled.
     *
     * @return {@code true} if this task completed without throwing an
     * exception and was not cancelled
     */
    public final boolean isCompletedNormally() {
        return status == NORMAL;
    }

    /**
     * Returns the exception thrown by the base computation, or a
     * {@code CancellationException} if cancelled, or {@code null} if
     * none or if the method has not yet completed.
     *
     * @return the exception, or {@code null} if none
     */
    public final Throwable getException() {
        int s = status;
        return ((s >= NORMAL)    ? null :
                (s == CANCELLED) ? new CancellationException() :
                getThrowableException());
    }

    /**
     * Completes this task abnormally, and if not already aborted or
     * cancelled, causes it to throw the given exception upon
     * {@code join} and related operations. This method may be used
     * to induce exceptions in asynchronous tasks, or to force
     * completion of tasks that would not otherwise complete.  Its use
     * in other situations is discouraged.  This method is
     * overridable, but overridden versions must invoke {@code super}
     * implementation to maintain guarantees.
     *
     * @param ex the exception to throw. If this exception is not a
     * {@code RuntimeException} or {@code Error}, the actual exception
     * thrown will be a {@code RuntimeException} with cause {@code ex}.
     */
    public void completeExceptionally(Throwable ex) {
        setExceptionalCompletion((ex instanceof RuntimeException) ||
                                 (ex instanceof Error) ? ex :
                                 new RuntimeException(ex));
    }

    /**
     * Completes this task, and if not already aborted or cancelled,
     * returning the given value as the result of subsequent
     * invocations of {@code join} and related operations. This method
     * may be used to provide results for asynchronous tasks, or to
     * provide alternative handling for tasks that would not otherwise
     * complete normally. Its use in other situations is
     * discouraged. This method is overridable, but overridden
     * versions must invoke {@code super} implementation to maintain
     * guarantees.
     *
     * @param value the result value for this task
     */
    public void complete(V value) {
        try {
            setRawResult(value);
        } catch (Throwable rex) {
            setExceptionalCompletion(rex);
            return;
        }
        setCompletion(NORMAL);
    }

    /**
     * Waits if necessary for the computation to complete, and then
     * retrieves its result.
     *
     * @return the computed result
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread is not a
     * member of a ForkJoinPool and was interrupted while waiting
     */
    public final V get() throws InterruptedException, ExecutionException {
        int s = (Thread.currentThread() instanceof ForkJoinWorkerThread) ?
            doJoin() : externalInterruptibleAwaitDone(0L);
        Throwable ex;
        if (s == CANCELLED)
            throw new CancellationException();
        if (s == EXCEPTIONAL && (ex = getThrowableException()) != null)
            throw new ExecutionException(ex);
        return getRawResult();
    }

    /**
     * Waits if necessary for at most the given time for the computation
     * to complete, and then retrieves its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the computed result
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread is not a
     * member of a ForkJoinPool and was interrupted while waiting
     * @throws TimeoutException if the wait timed out
     */
    public final V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread w = (ForkJoinWorkerThread) t;
            long nanos = unit.toNanos(timeout);
            if (status >= 0) {
                boolean completed = false;
                if (w.unpushTask(this)) {
                    try {
                        completed = exec();
                    } catch (Throwable rex) {
                        setExceptionalCompletion(rex);
                    }
                }
                if (completed)
                    setCompletion(NORMAL);
                else if (status >= 0 && nanos > 0)
                    w.pool.timedAwaitJoin(this, nanos);
            }
        }
        else {
            long millis = unit.toMillis(timeout);
            if (millis > 0)
                externalInterruptibleAwaitDone(millis);
        }
        int s = status;
        if (s != NORMAL) {
            Throwable ex;
            if (s == CANCELLED)
                throw new CancellationException();
            if (s != EXCEPTIONAL)
                throw new TimeoutException();
            if ((ex = getThrowableException()) != null)
                throw new ExecutionException(ex);
        }
        return getRawResult();
    }

    /**
     * Joins this task, without returning its result or throwing its
     * exception. This method may be useful when processing
     * collections of tasks when some have been cancelled or otherwise
     * known to have aborted.
     */
    public final void quietlyJoin() {
        doJoin();
    }

    /**
     * Commences performing this task and awaits its completion if
     * necessary, without returning its result or throwing its
     * exception.
     */
    public final void quietlyInvoke() {
        doInvoke();
    }

    /**
     * Possibly executes tasks until the pool hosting the current task
     * {@link ForkJoinPool#isQuiescent is quiescent}. This method may
     * be of use in designs in which many tasks are forked, but none
     * are explicitly joined, instead executing them until all are
     * processed.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     */
    public static void helpQuiesce() {
        ((ForkJoinWorkerThread) Thread.currentThread())
            .helpQuiescePool();
    }

    /**
     * Resets the internal bookkeeping state of this task, allowing a
     * subsequent {@code fork}. This method allows repeated reuse of
     * this task, but only if reuse occurs when this task has either
     * never been forked, or has been forked, then completed and all
     * outstanding joins of this task have also completed. Effects
     * under any other usage conditions are not guaranteed.
     * This method may be useful when executing
     * pre-constructed trees of subtasks in loops.
     *
     * <p>Upon completion of this method, {@code isDone()} reports
     * {@code false}, and {@code getException()} reports {@code
     * null}. However, the value returned by {@code getRawResult} is
     * unaffected. To clear this value, you can invoke {@code
     * setRawResult(null)}.
     */
    public void reinitialize() {
        if (status == EXCEPTIONAL)
            clearExceptionalCompletion();
        else
            status = 0;
    }

    /**
     * Returns the pool hosting the current task execution, or null
     * if this task is executing outside of any ForkJoinPool.
     *
     * @see #inForkJoinPool
     * @return the pool, or {@code null} if none
     */
    public static ForkJoinPool getPool() {
        Thread t = Thread.currentThread();
        return (t instanceof ForkJoinWorkerThread) ?
            ((ForkJoinWorkerThread) t).pool : null;
    }

    /**
     * Returns {@code true} if the current thread is a {@link
     * ForkJoinWorkerThread} executing as a ForkJoinPool computation.
     *
     * @return {@code true} if the current thread is a {@link
     * ForkJoinWorkerThread} executing as a ForkJoinPool computation,
     * or {@code false} otherwise
     */
    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    /**
     * Tries to unschedule this task for execution. This method will
     * typically succeed if this task is the most recently forked task
     * by the current thread, and has not commenced executing in
     * another thread.  This method may be useful when arranging
     * alternative local processing of tasks that could have been, but
     * were not, stolen.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return {@code true} if unforked
     */
    public boolean tryUnfork() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .unpushTask(this);
    }

    /**
     * Returns an estimate of the number of tasks that have been
     * forked by the current worker thread but not yet executed. This
     * value may be useful for heuristic decisions about whether to
     * fork other tasks.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return the number of tasks
     */
    public static int getQueuedTaskCount() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .getQueueSize();
    }

    /**
     * Returns an estimate of how many more locally queued tasks are
     * held by the current worker thread than there are other worker
     * threads that might steal them.  This value may be useful for
     * heuristic decisions about whether to fork other tasks. In many
     * usages of ForkJoinTasks, at steady state, each worker should
     * aim to maintain a small constant surplus (for example, 3) of
     * tasks, and to process computations locally if this threshold is
     * exceeded.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return the surplus number of tasks, which may be negative
     */
    public static int getSurplusQueuedTaskCount() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .getEstimatedSurplusTaskCount();
    }

    // Extension methods

    /**
     * Returns the result that would be returned by {@link #join}, even
     * if this task completed abnormally, or {@code null} if this task
     * is not known to have been completed.  This method is designed
     * to aid debugging, as well as to support extensions. Its use in
     * any other context is discouraged.
     *
     * @return the result, or {@code null} if not completed
     */
    public abstract V getRawResult();

    /**
     * Forces the given value to be returned as a result.  This method
     * is designed to support extensions, and should not in general be
     * called otherwise.
     *
     * @param value the value
     */
    protected abstract void setRawResult(V value);

    /**
     * Immediately performs the base action of this task.  This method
     * is designed to support extensions, and should not in general be
     * called otherwise. The return value controls whether this task
     * is considered to be done normally. It may return false in
     * asynchronous actions that require explicit invocations of
     * {@link #complete} to become joinable. It may also throw an
     * (unchecked) exception to indicate abnormal exit.
     *
     * @return {@code true} if completed normally
     */
    protected abstract boolean exec();

    /**
     * Returns, but does not unschedule or execute, a task queued by
     * the current thread but not yet executed, if one is immediately
     * available. There is no guarantee that this task will actually
     * be polled or executed next. Conversely, this method may return
     * null even if a task exists but cannot be accessed without
     * contention with other threads.  This method is designed
     * primarily to support extensions, and is unlikely to be useful
     * otherwise.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return the next task, or {@code null} if none are available
     */
    protected static ForkJoinTask<?> peekNextLocalTask() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .peekTask();
    }

    /**
     * Unschedules and returns, without executing, the next task
     * queued by the current thread but not yet executed.  This method
     * is designed primarily to support extensions, and is unlikely to
     * be useful otherwise.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return the next task, or {@code null} if none are available
     */
    protected static ForkJoinTask<?> pollNextLocalTask() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .pollLocalTask();
    }

    /**
     * Unschedules and returns, without executing, the next task
     * queued by the current thread but not yet executed, if one is
     * available, or if not available, a task that was forked by some
     * other thread, if available. Availability may be transient, so a
     * {@code null} result does not necessarily imply quiescence
     * of the pool this task is operating in.  This method is designed
     * primarily to support extensions, and is unlikely to be useful
     * otherwise.
     *
     * <p>This method may be invoked only from within {@code
     * ForkJoinPool} computations (as may be determined using method
     * {@link #inForkJoinPool}).  Attempts to invoke in other contexts
     * result in exceptions or errors, possibly including {@code
     * ClassCastException}.
     *
     * @return a task, or {@code null} if none are available
     */
    protected static ForkJoinTask<?> pollTask() {
        return ((ForkJoinWorkerThread) Thread.currentThread())
            .pollTask();
    }

    /**
     * Adaptor for Runnables. This implements RunnableFuture
     * to be compliant with AbstractExecutorService constraints
     * when used in ForkJoinPool.
     */
    static final class AdaptedRunnable<T> extends ForkJoinTask<T>
        implements RunnableFuture<T> {
        final Runnable runnable;
        final T resultOnCompletion;
        T result;
        AdaptedRunnable(Runnable runnable, T result) {
            if (runnable == null) throw new NullPointerException();
            this.runnable = runnable;
            this.resultOnCompletion = result;
        }
        public T getRawResult() { return result; }
        public void setRawResult(T v) { result = v; }
        public boolean exec() {
            runnable.run();
            result = resultOnCompletion;
            return true;
        }
        public void run() { invoke(); }
        private static final long serialVersionUID = 5232453952276885070L;
    }

    /**
     * Adaptor for Callables
     */
    static final class AdaptedCallable<T> extends ForkJoinTask<T>
        implements RunnableFuture<T> {
        final Callable<? extends T> callable;
        T result;
        AdaptedCallable(Callable<? extends T> callable) {
            if (callable == null) throw new NullPointerException();
            this.callable = callable;
        }
        public T getRawResult() { return result; }
        public void setRawResult(T v) { result = v; }
        public boolean exec() {
            try {
                result = callable.call();
                return true;
            } catch (Error err) {
                throw err;
            } catch (RuntimeException rex) {
                throw rex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        public void run() { invoke(); }
        private static final long serialVersionUID = 2838392045355241008L;
    }

    /**
     * Returns a new {@code ForkJoinTask} that performs the {@code run}
     * method of the given {@code Runnable} as its action, and returns
     * a null result upon {@link #join}.
     *
     * @param runnable the runnable action
     * @return the task
     */
    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnable<Void>(runnable, null);
    }

    /**
     * Returns a new {@code ForkJoinTask} that performs the {@code run}
     * method of the given {@code Runnable} as its action, and returns
     * the given result upon {@link #join}.
     *
     * @param runnable the runnable action
     * @param result the result upon completion
     * @return the task
     */
    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T result) {
        return new AdaptedRunnable<T>(runnable, result);
    }

    /**
     * Returns a new {@code ForkJoinTask} that performs the {@code call}
     * method of the given {@code Callable} as its action, and returns
     * its result upon {@link #join}, translating any checked exceptions
     * encountered into {@code RuntimeException}.
     *
     * @param callable the callable action
     * @return the task
     */
    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable<T>(callable);
    }

    // Serialization support

    private static final long serialVersionUID = -7721805057305804111L;

    /**
     * Saves the state to a stream (that is, serializes it).
     *
     * @serialData the current run status and the exception thrown
     * during execution, or {@code null} if none
     * @param s the stream
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        s.defaultWriteObject();
        s.writeObject(getException());
    }

    /**
     * Reconstitutes the instance from a stream (that is, deserializes it).
     *
     * @param s the stream
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        Object ex = s.readObject();
        if (ex != null)
            setExceptionalCompletion((Throwable)ex);
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long statusOffset;
    static {
        exceptionTableLock = new ReentrantLock();
        exceptionTableRefQueue = new ReferenceQueue<Object>();
        exceptionTable = new ExceptionNode[EXCEPTION_MAP_CAPACITY];
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            statusOffset = UNSAFE.objectFieldOffset
                (ForkJoinTask.class.getDeclaredField("status"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinWorkerThread.java b/luni/src/main/java/java/util/concurrent/ForkJoinWorkerThread.java
new file mode 100644
//Synthetic comment -- index 0000000..d99ffe9

//Synthetic comment -- @@ -0,0 +1,970 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.Collection;
import java.util.concurrent.RejectedExecutionException;
import libcore.util.SneakyThrow;

/**
 * A thread managed by a {@link ForkJoinPool}, which executes
 * {@link ForkJoinTask}s.
 * This class is subclassable solely for the sake of adding
 * functionality -- there are no overridable methods dealing with
 * scheduling or execution.  However, you can override initialization
 * and termination methods surrounding the main task processing loop.
 * If you do create such a subclass, you will also need to supply a
 * custom {@link ForkJoinPool.ForkJoinWorkerThreadFactory} to use it
 * in a {@code ForkJoinPool}.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public class ForkJoinWorkerThread extends Thread {
    /*
     * Overview:
     *
     * ForkJoinWorkerThreads are managed by ForkJoinPools and perform
     * ForkJoinTasks. This class includes bookkeeping in support of
     * worker activation, suspension, and lifecycle control described
     * in more detail in the internal documentation of class
     * ForkJoinPool. And as described further below, this class also
     * includes special-cased support for some ForkJoinTask
     * methods. But the main mechanics involve work-stealing:
     *
     * Work-stealing queues are special forms of Deques that support
     * only three of the four possible end-operations -- push, pop,
     * and deq (aka steal), under the further constraints that push
     * and pop are called only from the owning thread, while deq may
     * be called from other threads.  (If you are unfamiliar with
     * them, you probably want to read Herlihy and Shavit's book "The
     * Art of Multiprocessor programming", chapter 16 describing these
     * in more detail before proceeding.)  The main work-stealing
     * queue design is roughly similar to those in the papers "Dynamic
     * Circular Work-Stealing Deque" by Chase and Lev, SPAA 2005
     * (http://research.sun.com/scalable/pubs/index.html) and
     * "Idempotent work stealing" by Michael, Saraswat, and Vechev,
     * PPoPP 2009 (http://portal.acm.org/citation.cfm?id=1504186).
     * The main differences ultimately stem from gc requirements that
     * we null out taken slots as soon as we can, to maintain as small
     * a footprint as possible even in programs generating huge
     * numbers of tasks. To accomplish this, we shift the CAS
     * arbitrating pop vs deq (steal) from being on the indices
     * ("queueBase" and "queueTop") to the slots themselves (mainly
     * via method "casSlotNull()"). So, both a successful pop and deq
     * mainly entail a CAS of a slot from non-null to null.  Because
     * we rely on CASes of references, we do not need tag bits on
     * queueBase or queueTop.  They are simple ints as used in any
     * circular array-based queue (see for example ArrayDeque).
     * Updates to the indices must still be ordered in a way that
     * guarantees that queueTop == queueBase means the queue is empty,
     * but otherwise may err on the side of possibly making the queue
     * appear nonempty when a push, pop, or deq have not fully
     * committed. Note that this means that the deq operation,
     * considered individually, is not wait-free. One thief cannot
     * successfully continue until another in-progress one (or, if
     * previously empty, a push) completes.  However, in the
     * aggregate, we ensure at least probabilistic non-blockingness.
     * If an attempted steal fails, a thief always chooses a different
     * random victim target to try next. So, in order for one thief to
     * progress, it suffices for any in-progress deq or new push on
     * any empty queue to complete.
     *
     * This approach also enables support for "async mode" where local
     * task processing is in FIFO, not LIFO order; simply by using a
     * version of deq rather than pop when locallyFifo is true (as set
     * by the ForkJoinPool).  This allows use in message-passing
     * frameworks in which tasks are never joined.  However neither
     * mode considers affinities, loads, cache localities, etc, so
     * rarely provide the best possible performance on a given
     * machine, but portably provide good throughput by averaging over
     * these factors.  (Further, even if we did try to use such
     * information, we do not usually have a basis for exploiting
     * it. For example, some sets of tasks profit from cache
     * affinities, but others are harmed by cache pollution effects.)
     *
     * When a worker would otherwise be blocked waiting to join a
     * task, it first tries a form of linear helping: Each worker
     * records (in field currentSteal) the most recent task it stole
     * from some other worker. Plus, it records (in field currentJoin)
     * the task it is currently actively joining. Method joinTask uses
     * these markers to try to find a worker to help (i.e., steal back
     * a task from and execute it) that could hasten completion of the
     * actively joined task. In essence, the joiner executes a task
     * that would be on its own local deque had the to-be-joined task
     * not been stolen. This may be seen as a conservative variant of
     * the approach in Wagner & Calder "Leapfrogging: a portable
     * technique for implementing efficient futures" SIGPLAN Notices,
     * 1993 (http://portal.acm.org/citation.cfm?id=155354). It differs
     * in that: (1) We only maintain dependency links across workers
     * upon steals, rather than use per-task bookkeeping.  This may
     * require a linear scan of workers array to locate stealers, but
     * usually doesn't because stealers leave hints (that may become
     * stale/wrong) of where to locate them. This isolates cost to
     * when it is needed, rather than adding to per-task overhead.
     * (2) It is "shallow", ignoring nesting and potentially cyclic
     * mutual steals.  (3) It is intentionally racy: field currentJoin
     * is updated only while actively joining, which means that we
     * miss links in the chain during long-lived tasks, GC stalls etc
     * (which is OK since blocking in such cases is usually a good
     * idea).  (4) We bound the number of attempts to find work (see
     * MAX_HELP) and fall back to suspending the worker and if
     * necessary replacing it with another.
     *
     * Efficient implementation of these algorithms currently relies
     * on an uncomfortable amount of "Unsafe" mechanics. To maintain
     * correct orderings, reads and writes of variable queueBase
     * require volatile ordering.  Variable queueTop need not be
     * volatile because non-local reads always follow those of
     * queueBase.  Similarly, because they are protected by volatile
     * queueBase reads, reads of the queue array and its slots by
     * other threads do not need volatile load semantics, but writes
     * (in push) require store order and CASes (in pop and deq)
     * require (volatile) CAS semantics.  (Michael, Saraswat, and
     * Vechev's algorithm has similar properties, but without support
     * for nulling slots.)  Since these combinations aren't supported
     * using ordinary volatiles, the only way to accomplish these
     * efficiently is to use direct Unsafe calls. (Using external
     * AtomicIntegers and AtomicReferenceArrays for the indices and
     * array is significantly slower because of memory locality and
     * indirection effects.)
     *
     * Further, performance on most platforms is very sensitive to
     * placement and sizing of the (resizable) queue array.  Even
     * though these queues don't usually become all that big, the
     * initial size must be large enough to counteract cache
     * contention effects across multiple queues (especially in the
     * presence of GC cardmarking). Also, to improve thread-locality,
     * queues are initialized after starting.
     */

    /**
     * Mask for pool indices encoded as shorts
     */
    private static final int  SMASK  = 0xffff;

    /**
     * Capacity of work-stealing queue array upon initialization.
     * Must be a power of two. Initial size must be at least 4, but is
     * padded to minimize cache effects.
     */
    private static final int INITIAL_QUEUE_CAPACITY = 1 << 13;

    /**
     * Maximum size for queue array. Must be a power of two
     * less than or equal to 1 << (31 - width of array entry) to
     * ensure lack of index wraparound, but is capped at a lower
     * value to help users trap runaway computations.
     */
    private static final int MAXIMUM_QUEUE_CAPACITY = 1 << 24; // 16M

    /**
     * The work-stealing queue array. Size must be a power of two.
     * Initialized when started (as opposed to when constructed), to
     * improve memory locality.
     */
    ForkJoinTask<?>[] queue;

    /**
     * The pool this thread works in. Accessed directly by ForkJoinTask.
     */
    final ForkJoinPool pool;

    /**
     * Index (mod queue.length) of next queue slot to push to or pop
     * from. It is written only by owner thread, and accessed by other
     * threads only after reading (volatile) queueBase.  Both queueTop
     * and queueBase are allowed to wrap around on overflow, but
     * (queueTop - queueBase) still estimates size.
     */
    int queueTop;

    /**
     * Index (mod queue.length) of least valid queue slot, which is
     * always the next position to steal from if nonempty.
     */
    volatile int queueBase;

    /**
     * The index of most recent stealer, used as a hint to avoid
     * traversal in method helpJoinTask. This is only a hint because a
     * worker might have had multiple steals and this only holds one
     * of them (usually the most current). Declared non-volatile,
     * relying on other prevailing sync to keep reasonably current.
     */
    int stealHint;

    /**
     * Index of this worker in pool array. Set once by pool before
     * running, and accessed directly by pool to locate this worker in
     * its workers array.
     */
    final int poolIndex;

    /**
     * Encoded record for pool task waits. Usages are always
     * surrounded by volatile reads/writes
     */
    int nextWait;

    /**
     * Complement of poolIndex, offset by count of entries of task
     * waits. Accessed by ForkJoinPool to manage event waiters.
     */
    volatile int eventCount;

    /**
     * Seed for random number generator for choosing steal victims.
     * Uses Marsaglia xorshift. Must be initialized as nonzero.
     */
    int seed;

    /**
     * Number of steals. Directly accessed (and reset) by pool when
     * idle.
     */
    int stealCount;

    /**
     * True if this worker should or did terminate
     */
    volatile boolean terminate;

    /**
     * Set to true before LockSupport.park; false on return
     */
    volatile boolean parked;

    /**
     * True if use local fifo, not default lifo, for local polling.
     * Shadows value from ForkJoinPool.
     */
    final boolean locallyFifo;

    /**
     * The task most recently stolen from another worker (or
     * submission queue).  All uses are surrounded by enough volatile
     * reads/writes to maintain as non-volatile.
     */
    ForkJoinTask<?> currentSteal;

    /**
     * The task currently being joined, set only when actively trying
     * to help other stealers in helpJoinTask. All uses are surrounded
     * by enough volatile reads/writes to maintain as non-volatile.
     */
    ForkJoinTask<?> currentJoin;

    /**
     * Creates a ForkJoinWorkerThread operating in the given pool.
     *
     * @param pool the pool this thread works in
     * @throws NullPointerException if pool is null
     */
    protected ForkJoinWorkerThread(ForkJoinPool pool) {
        super(pool.nextWorkerName());
        this.pool = pool;
        int k = pool.registerWorker(this);
        poolIndex = k;
        eventCount = ~k & SMASK; // clear wait count
        locallyFifo = pool.locallyFifo;
        Thread.UncaughtExceptionHandler ueh = pool.ueh;
        if (ueh != null)
            setUncaughtExceptionHandler(ueh);
        setDaemon(true);
    }

    // Public methods

    /**
     * Returns the pool hosting this thread.
     *
     * @return the pool
     */
    public ForkJoinPool getPool() {
        return pool;
    }

    /**
     * Returns the index number of this thread in its pool.  The
     * returned value ranges from zero to the maximum number of
     * threads (minus one) that have ever been created in the pool.
     * This method may be useful for applications that track status or
     * collect results per-worker rather than per-task.
     *
     * @return the index number
     */
    public int getPoolIndex() {
        return poolIndex;
    }

    // Randomization

    /**
     * Computes next value for random victim probes and backoffs.
     * Scans don't require a very high quality generator, but also not
     * a crummy one.  Marsaglia xor-shift is cheap and works well
     * enough.  Note: This is manually inlined in FJP.scan() to avoid
     * writes inside busy loops.
     */
    private int nextSeed() {
        int r = seed;
        r ^= r << 13;
        r ^= r >>> 17;
        r ^= r << 5;
        return seed = r;
    }

    // Run State management

    /**
     * Initializes internal state after construction but before
     * processing any tasks. If you override this method, you must
     * invoke {@code super.onStart()} at the beginning of the method.
     * Initialization requires care: Most fields must have legal
     * default values, to ensure that attempted accesses from other
     * threads work correctly even before this thread starts
     * processing tasks.
     */
    protected void onStart() {
        queue = new ForkJoinTask<?>[INITIAL_QUEUE_CAPACITY];
        int r = ForkJoinPool.workerSeedGenerator.nextInt();
        seed = (r == 0) ? 1 : r; //  must be nonzero
    }

    /**
     * Performs cleanup associated with termination of this worker
     * thread.  If you override this method, you must invoke
     * {@code super.onTermination} at the end of the overridden method.
     *
     * @param exception the exception causing this thread to abort due
     * to an unrecoverable error, or {@code null} if completed normally
     */
    protected void onTermination(Throwable exception) {
        try {
            terminate = true;
            cancelTasks();
            pool.deregisterWorker(this, exception);
        } catch (Throwable ex) {        // Shouldn't ever happen
            if (exception == null)      // but if so, at least rethrown
                exception = ex;
        } finally {
            if (exception != null)
                SneakyThrow.sneakyThrow(exception); // android-changed
        }
    }

    /**
     * This method is required to be public, but should never be
     * called explicitly. It performs the main run loop to execute
     * {@link ForkJoinTask}s.
     */
    public void run() {
        Throwable exception = null;
        try {
            onStart();
            pool.work(this);
        } catch (Throwable ex) {
            exception = ex;
        } finally {
            onTermination(exception);
        }
    }

    /*
     * Intrinsics-based atomic writes for queue slots. These are
     * basically the same as methods in AtomicReferenceArray, but
     * specialized for (1) ForkJoinTask elements (2) requirement that
     * nullness and bounds checks have already been performed by
     * callers and (3) effective offsets are known not to overflow
     * from int to long (because of MAXIMUM_QUEUE_CAPACITY). We don't
     * need corresponding version for reads: plain array reads are OK
     * because they are protected by other volatile reads and are
     * confirmed by CASes.
     *
     * Most uses don't actually call these methods, but instead
     * contain inlined forms that enable more predictable
     * optimization.  We don't define the version of write used in
     * pushTask at all, but instead inline there a store-fenced array
     * slot write.
     *
     * Also in most methods, as a performance (not correctness) issue,
     * we'd like to encourage compilers not to arbitrarily postpone
     * setting queueTop after writing slot.  Currently there is no
     * intrinsic for arranging this, but using Unsafe putOrderedInt
     * may be a preferable strategy on some compilers even though its
     * main effect is a pre-, not post- fence. To simplify possible
     * changes, the option is left in comments next to the associated
     * assignments.
     */

    /**
     * CASes slot i of array q from t to null. Caller must ensure q is
     * non-null and index is in range.
     */
    private static final boolean casSlotNull(ForkJoinTask<?>[] q, int i,
                                             ForkJoinTask<?> t) {
        return UNSAFE.compareAndSwapObject(q, (i << ASHIFT) + ABASE, t, null);
    }

    /**
     * Performs a volatile write of the given task at given slot of
     * array q.  Caller must ensure q is non-null and index is in
     * range. This method is used only during resets and backouts.
     */
    private static final void writeSlot(ForkJoinTask<?>[] q, int i,
                                        ForkJoinTask<?> t) {
        UNSAFE.putObjectVolatile(q, (i << ASHIFT) + ABASE, t);
    }

    // queue methods

    /**
     * Pushes a task. Call only from this thread.
     *
     * @param t the task. Caller must ensure non-null.
     */
    final void pushTask(ForkJoinTask<?> t) {
        ForkJoinTask<?>[] q; int s, m;
        if ((q = queue) != null) {    // ignore if queue removed
            long u = (((s = queueTop) & (m = q.length - 1)) << ASHIFT) + ABASE;
            UNSAFE.putOrderedObject(q, u, t);
            queueTop = s + 1;         // or use putOrderedInt
            if ((s -= queueBase) <= 2)
                pool.signalWork();
            else if (s == m)
                growQueue();
        }
    }

    /**
     * Creates or doubles queue array.  Transfers elements by
     * emulating steals (deqs) from old array and placing, oldest
     * first, into new array.
     */
    private void growQueue() {
        ForkJoinTask<?>[] oldQ = queue;
        int size = oldQ != null ? oldQ.length << 1 : INITIAL_QUEUE_CAPACITY;
        if (size > MAXIMUM_QUEUE_CAPACITY)
            throw new RejectedExecutionException("Queue capacity exceeded");
        if (size < INITIAL_QUEUE_CAPACITY)
            size = INITIAL_QUEUE_CAPACITY;
        ForkJoinTask<?>[] q = queue = new ForkJoinTask<?>[size];
        int mask = size - 1;
        int top = queueTop;
        int oldMask;
        if (oldQ != null && (oldMask = oldQ.length - 1) >= 0) {
            for (int b = queueBase; b != top; ++b) {
                long u = ((b & oldMask) << ASHIFT) + ABASE;
                Object x = UNSAFE.getObjectVolatile(oldQ, u);
                if (x != null && UNSAFE.compareAndSwapObject(oldQ, u, x, null))
                    UNSAFE.putObjectVolatile
                        (q, ((b & mask) << ASHIFT) + ABASE, x);
            }
        }
    }

    /**
     * Tries to take a task from the base of the queue, failing if
     * empty or contended. Note: Specializations of this code appear
     * in locallyDeqTask and elsewhere.
     *
     * @return a task, or null if none or contended
     */
    final ForkJoinTask<?> deqTask() {
        ForkJoinTask<?> t; ForkJoinTask<?>[] q; int b, i;
        if (queueTop != (b = queueBase) &&
            (q = queue) != null && // must read q after b
            (i = (q.length - 1) & b) >= 0 &&
            (t = q[i]) != null && queueBase == b &&
            UNSAFE.compareAndSwapObject(q, (i << ASHIFT) + ABASE, t, null)) {
            queueBase = b + 1;
            return t;
        }
        return null;
    }

    /**
     * Tries to take a task from the base of own queue.  Called only
     * by this thread.
     *
     * @return a task, or null if none
     */
    final ForkJoinTask<?> locallyDeqTask() {
        ForkJoinTask<?> t; int m, b, i;
        ForkJoinTask<?>[] q = queue;
        if (q != null && (m = q.length - 1) >= 0) {
            while (queueTop != (b = queueBase)) {
                if ((t = q[i = m & b]) != null &&
                    queueBase == b &&
                    UNSAFE.compareAndSwapObject(q, (i << ASHIFT) + ABASE,
                                                t, null)) {
                    queueBase = b + 1;
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Returns a popped task, or null if empty.
     * Called only by this thread.
     */
    private ForkJoinTask<?> popTask() {
        int m;
        ForkJoinTask<?>[] q = queue;
        if (q != null && (m = q.length - 1) >= 0) {
            for (int s; (s = queueTop) != queueBase;) {
                int i = m & --s;
                long u = (i << ASHIFT) + ABASE; // raw offset
                ForkJoinTask<?> t = q[i];
                if (t == null)   // lost to stealer
                    break;
                if (UNSAFE.compareAndSwapObject(q, u, t, null)) {
                    queueTop = s; // or putOrderedInt
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Specialized version of popTask to pop only if topmost element
     * is the given task. Called only by this thread.
     *
     * @param t the task. Caller must ensure non-null.
     */
    final boolean unpushTask(ForkJoinTask<?> t) {
        ForkJoinTask<?>[] q;
        int s;
        if ((q = queue) != null && (s = queueTop) != queueBase &&
            UNSAFE.compareAndSwapObject
            (q, (((q.length - 1) & --s) << ASHIFT) + ABASE, t, null)) {
            queueTop = s; // or putOrderedInt
            return true;
        }
        return false;
    }

    /**
     * Returns next task, or null if empty or contended.
     */
    final ForkJoinTask<?> peekTask() {
        int m;
        ForkJoinTask<?>[] q = queue;
        if (q == null || (m = q.length - 1) < 0)
            return null;
        int i = locallyFifo ? queueBase : (queueTop - 1);
        return q[i & m];
    }

    // Support methods for ForkJoinPool

    /**
     * Runs the given task, plus any local tasks until queue is empty
     */
    final void execTask(ForkJoinTask<?> t) {
        currentSteal = t;
        for (;;) {
            if (t != null)
                t.doExec();
            if (queueTop == queueBase)
                break;
            t = locallyFifo ? locallyDeqTask() : popTask();
        }
        ++stealCount;
        currentSteal = null;
    }

    /**
     * Removes and cancels all tasks in queue.  Can be called from any
     * thread.
     */
    final void cancelTasks() {
        ForkJoinTask<?> cj = currentJoin; // try to cancel ongoing tasks
        if (cj != null && cj.status >= 0)
            cj.cancelIgnoringExceptions();
        ForkJoinTask<?> cs = currentSteal;
        if (cs != null && cs.status >= 0)
            cs.cancelIgnoringExceptions();
        while (queueBase != queueTop) {
            ForkJoinTask<?> t = deqTask();
            if (t != null)
                t.cancelIgnoringExceptions();
        }
    }

    /**
     * Drains tasks to given collection c.
     *
     * @return the number of tasks drained
     */
    final int drainTasksTo(Collection<? super ForkJoinTask<?>> c) {
        int n = 0;
        while (queueBase != queueTop) {
            ForkJoinTask<?> t = deqTask();
            if (t != null) {
                c.add(t);
                ++n;
            }
        }
        return n;
    }

    // Support methods for ForkJoinTask

    /**
     * Returns an estimate of the number of tasks in the queue.
     */
    final int getQueueSize() {
        return queueTop - queueBase;
    }

    /**
     * Gets and removes a local task.
     *
     * @return a task, if available
     */
    final ForkJoinTask<?> pollLocalTask() {
        return locallyFifo ? locallyDeqTask() : popTask();
    }

    /**
     * Gets and removes a local or stolen task.
     *
     * @return a task, if available
     */
    final ForkJoinTask<?> pollTask() {
        ForkJoinWorkerThread[] ws;
        ForkJoinTask<?> t = pollLocalTask();
        if (t != null || (ws = pool.workers) == null)
            return t;
        int n = ws.length; // cheap version of FJP.scan
        int steps = n << 1;
        int r = nextSeed();
        int i = 0;
        while (i < steps) {
            ForkJoinWorkerThread w = ws[(i++ + r) & (n - 1)];
            if (w != null && w.queueBase != w.queueTop && w.queue != null) {
                if ((t = w.deqTask()) != null)
                    return t;
                i = 0;
            }
        }
        return null;
    }

    /**
     * The maximum stolen->joining link depth allowed in helpJoinTask,
     * as well as the maximum number of retries (allowing on average
     * one staleness retry per level) per attempt to instead try
     * compensation.  Depths for legitimate chains are unbounded, but
     * we use a fixed constant to avoid (otherwise unchecked) cycles
     * and bound staleness of traversal parameters at the expense of
     * sometimes blocking when we could be helping.
     */
    private static final int MAX_HELP = 16;

    /**
     * Possibly runs some tasks and/or blocks, until joinMe is done.
     *
     * @param joinMe the task to join
     * @return completion status on exit
     */
    final int joinTask(ForkJoinTask<?> joinMe) {
        ForkJoinTask<?> prevJoin = currentJoin;
        currentJoin = joinMe;
        for (int s, retries = MAX_HELP;;) {
            if ((s = joinMe.status) < 0) {
                currentJoin = prevJoin;
                return s;
            }
            if (retries > 0) {
                if (queueTop != queueBase) {
                    if (!localHelpJoinTask(joinMe))
                        retries = 0;           // cannot help
                }
                else if (retries == MAX_HELP >>> 1) {
                    --retries;                 // check uncommon case
                    if (tryDeqAndExec(joinMe) >= 0)
                        Thread.yield();        // for politeness
                }
                else
                    retries = helpJoinTask(joinMe) ? MAX_HELP : retries - 1;
            }
            else {
                retries = MAX_HELP;           // restart if not done
                pool.tryAwaitJoin(joinMe);
            }
        }
    }

    /**
     * If present, pops and executes the given task, or any other
     * cancelled task
     *
     * @return false if any other non-cancelled task exists in local queue
     */
    private boolean localHelpJoinTask(ForkJoinTask<?> joinMe) {
        int s, i; ForkJoinTask<?>[] q; ForkJoinTask<?> t;
        if ((s = queueTop) != queueBase && (q = queue) != null &&
            (i = (q.length - 1) & --s) >= 0 &&
            (t = q[i]) != null) {
            if (t != joinMe && t.status >= 0)
                return false;
            if (UNSAFE.compareAndSwapObject
                (q, (i << ASHIFT) + ABASE, t, null)) {
                queueTop = s;           // or putOrderedInt
                t.doExec();
            }
        }
        return true;
    }

    /**
     * Tries to locate and execute tasks for a stealer of the given
     * task, or in turn one of its stealers, Traces
     * currentSteal->currentJoin links looking for a thread working on
     * a descendant of the given task and with a non-empty queue to
     * steal back and execute tasks from.  The implementation is very
     * branchy to cope with potential inconsistencies or loops
     * encountering chains that are stale, unknown, or of length
     * greater than MAX_HELP links.  All of these cases are dealt with
     * by just retrying by caller.
     *
     * @param joinMe the task to join
     * @return true if ran a task
     */
    private boolean helpJoinTask(ForkJoinTask<?> joinMe) {
        boolean helped = false;
        int m = pool.scanGuard & SMASK;
        ForkJoinWorkerThread[] ws = pool.workers;
        if (ws != null && ws.length > m && joinMe.status >= 0) {
            int levels = MAX_HELP;              // remaining chain length
            ForkJoinTask<?> task = joinMe;      // base of chain
            outer:for (ForkJoinWorkerThread thread = this;;) {
                // Try to find v, the stealer of task, by first using hint
                ForkJoinWorkerThread v = ws[thread.stealHint & m];
                if (v == null || v.currentSteal != task) {
                    for (int j = 0; ;) {        // search array
                        if ((v = ws[j]) != null && v.currentSteal == task) {
                            thread.stealHint = j;
                            break;              // save hint for next time
                        }
                        if (++j > m)
                            break outer;        // can't find stealer
                    }
                }
                // Try to help v, using specialized form of deqTask
                for (;;) {
                    ForkJoinTask<?>[] q; int b, i;
                    if (joinMe.status < 0)
                        break outer;
                    if ((b = v.queueBase) == v.queueTop ||
                        (q = v.queue) == null ||
                        (i = (q.length-1) & b) < 0)
                        break;                  // empty
                    long u = (i << ASHIFT) + ABASE;
                    ForkJoinTask<?> t = q[i];
                    if (task.status < 0)
                        break outer;            // stale
                    if (t != null && v.queueBase == b &&
                        UNSAFE.compareAndSwapObject(q, u, t, null)) {
                        v.queueBase = b + 1;
                        v.stealHint = poolIndex;
                        ForkJoinTask<?> ps = currentSteal;
                        currentSteal = t;
                        t.doExec();
                        currentSteal = ps;
                        helped = true;
                    }
                }
                // Try to descend to find v's stealer
                ForkJoinTask<?> next = v.currentJoin;
                if (--levels > 0 && task.status >= 0 &&
                    next != null && next != task) {
                    task = next;
                    thread = v;
                }
                else
                    break;  // max levels, stale, dead-end, or cyclic
            }
        }
        return helped;
    }

    /**
     * Performs an uncommon case for joinTask: If task t is at base of
     * some workers queue, steals and executes it.
     *
     * @param t the task
     * @return t's status
     */
    private int tryDeqAndExec(ForkJoinTask<?> t) {
        int m = pool.scanGuard & SMASK;
        ForkJoinWorkerThread[] ws = pool.workers;
        if (ws != null && ws.length > m && t.status >= 0) {
            for (int j = 0; j <= m; ++j) {
                ForkJoinTask<?>[] q; int b, i;
                ForkJoinWorkerThread v = ws[j];
                if (v != null &&
                    (b = v.queueBase) != v.queueTop &&
                    (q = v.queue) != null &&
                    (i = (q.length - 1) & b) >= 0 &&
                    q[i] ==  t) {
                    long u = (i << ASHIFT) + ABASE;
                    if (v.queueBase == b &&
                        UNSAFE.compareAndSwapObject(q, u, t, null)) {
                        v.queueBase = b + 1;
                        v.stealHint = poolIndex;
                        ForkJoinTask<?> ps = currentSteal;
                        currentSteal = t;
                        t.doExec();
                        currentSteal = ps;
                    }
                    break;
                }
            }
        }
        return t.status;
    }

    /**
     * Implements ForkJoinTask.getSurplusQueuedTaskCount().  Returns
     * an estimate of the number of tasks, offset by a function of
     * number of idle workers.
     *
     * This method provides a cheap heuristic guide for task
     * partitioning when programmers, frameworks, tools, or languages
     * have little or no idea about task granularity.  In essence by
     * offering this method, we ask users only about tradeoffs in
     * overhead vs expected throughput and its variance, rather than
     * how finely to partition tasks.
     *
     * In a steady state strict (tree-structured) computation, each
     * thread makes available for stealing enough tasks for other
     * threads to remain active. Inductively, if all threads play by
     * the same rules, each thread should make available only a
     * constant number of tasks.
     *
     * The minimum useful constant is just 1. But using a value of 1
     * would require immediate replenishment upon each steal to
     * maintain enough tasks, which is infeasible.  Further,
     * partitionings/granularities of offered tasks should minimize
     * steal rates, which in general means that threads nearer the top
     * of computation tree should generate more than those nearer the
     * bottom. In perfect steady state, each thread is at
     * approximately the same level of computation tree. However,
     * producing extra tasks amortizes the uncertainty of progress and
     * diffusion assumptions.
     *
     * So, users will want to use values larger, but not much larger
     * than 1 to both smooth over transient shortages and hedge
     * against uneven progress; as traded off against the cost of
     * extra task overhead. We leave the user to pick a threshold
     * value to compare with the results of this call to guide
     * decisions, but recommend values such as 3.
     *
     * When all threads are active, it is on average OK to estimate
     * surplus strictly locally. In steady-state, if one thread is
     * maintaining say 2 surplus tasks, then so are others. So we can
     * just use estimated queue length (although note that (queueTop -
     * queueBase) can be an overestimate because of stealers lagging
     * increments of queueBase).  However, this strategy alone leads
     * to serious mis-estimates in some non-steady-state conditions
     * (ramp-up, ramp-down, other stalls). We can detect many of these
     * by further considering the number of "idle" threads, that are
     * known to have zero queued tasks, so compensate by a factor of
     * (#idle/#active) threads.
     */
    final int getEstimatedSurplusTaskCount() {
        return queueTop - queueBase - pool.idlePerActive();
    }

    /**
     * Runs tasks until {@code pool.isQuiescent()}. We piggyback on
     * pool's active count ctl maintenance, but rather than blocking
     * when tasks cannot be found, we rescan until all others cannot
     * find tasks either. The bracketing by pool quiescerCounts
     * updates suppresses pool auto-shutdown mechanics that could
     * otherwise prematurely terminate the pool because all threads
     * appear to be inactive.
     */
    final void helpQuiescePool() {
        boolean active = true;
        ForkJoinTask<?> ps = currentSteal; // to restore below
        ForkJoinPool p = pool;
        p.addQuiescerCount(1);
        for (;;) {
            ForkJoinWorkerThread[] ws = p.workers;
            ForkJoinWorkerThread v = null;
            int n;
            if (queueTop != queueBase)
                v = this;
            else if (ws != null && (n = ws.length) > 1) {
                ForkJoinWorkerThread w;
                int r = nextSeed(); // cheap version of FJP.scan
                int steps = n << 1;
                for (int i = 0; i < steps; ++i) {
                    if ((w = ws[(i + r) & (n - 1)]) != null &&
                        w.queueBase != w.queueTop) {
                        v = w;
                        break;
                    }
                }
            }
            if (v != null) {
                ForkJoinTask<?> t;
                if (!active) {
                    active = true;
                    p.addActiveCount(1);
                }
                if ((t = (v != this) ? v.deqTask() :
                     locallyFifo ? locallyDeqTask() : popTask()) != null) {
                    currentSteal = t;
                    t.doExec();
                    currentSteal = ps;
                }
            }
            else {
                if (active) {
                    active = false;
                    p.addActiveCount(-1);
                }
                if (p.isQuiescent()) {
                    p.addActiveCount(1);
                    p.addQuiescerCount(-1);
                    break;
                }
            }
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long ABASE;
    private static final int ASHIFT;

    static {
        int s;
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> a = ForkJoinTask[].class;
            ABASE = UNSAFE.arrayBaseOffset(a);
            s = UNSAFE.arrayIndexScale(a);
        } catch (Exception e) {
            throw new Error(e);
        }
        if ((s & (s-1)) != 0)
            throw new Error("data type scale not a power of two");
        ASHIFT = 31 - Integer.numberOfLeadingZeros(s);
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Future.java b/luni/src/main/java/java/util/concurrent/Future.java
//Synthetic comment -- index eb84796..6a37729 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/FutureTask.java b/luni/src/main/java/java/util/concurrent/FutureTask.java
//Synthetic comment -- index 2f2335e..d51881d 100644

//Synthetic comment -- @@ -1,55 +1,116 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.concurrent.locks.LockSupport;

/**
* A cancellable asynchronous computation.  This class provides a base
* implementation of {@link Future}, with methods to start and cancel
* a computation, query to see if the computation is complete, and
* retrieve the result of the computation.  The result can only be
 * retrieved when the computation has completed; the {@code get}
 * methods will block if the computation has not yet completed.  Once
* the computation has completed, the computation cannot be restarted
 * or cancelled (unless the computation is invoked using
 * {@link #runAndReset}).
*
 * <p>A {@code FutureTask} can be used to wrap a {@link Callable} or
 * {@link Runnable} object.  Because {@code FutureTask} implements
 * {@code Runnable}, a {@code FutureTask} can be submitted to an
 * {@link Executor} for execution.
*
* <p>In addition to serving as a standalone class, this class provides
 * {@code protected} functionality that may be useful when creating
* customized task classes.
*
* @since 1.5
* @author Doug Lea
 * @param <V> The result type returned by this FutureTask's {@code get} methods
*/
public class FutureTask<V> implements RunnableFuture<V> {
    /*
     * Revision notes: This differs from previous versions of this
     * class that relied on AbstractQueuedSynchronizer, mainly to
     * avoid surprising users about retaining interrupt status during
     * cancellation races. Sync control in the current design relies
     * on a "state" field updated via CAS to track completion, along
     * with a simple Treiber stack to hold waiting threads.
     *
     * Style note: As usual, we bypass overhead of using
     * AtomicXFieldUpdaters and instead directly use Unsafe intrinsics.
     */

/**
     * The run state of this task, initially NEW.  The run state
     * transitions to a terminal state only in methods set,
     * setException, and cancel.  During completion, state may take on
     * transient values of COMPLETING (while outcome is being set) or
     * INTERRUPTING (only while interrupting the runner to satisfy a
     * cancel(true)). Transitions from these intermediate to final
     * states use cheaper ordered/lazy writes because values are unique
     * and cannot be further modified.
     *
     * Possible state transitions:
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    private volatile int state;
    private static final int NEW          = 0;
    private static final int COMPLETING   = 1;
    private static final int NORMAL       = 2;
    private static final int EXCEPTIONAL  = 3;
    private static final int CANCELLED    = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED  = 6;

    /** The underlying callable; nulled out after running */
    private Callable<V> callable;
    /** The result to return or exception to throw from get() */
    private Object outcome; // non-volatile, protected by state reads/writes
    /** The thread running the callable; CASed during run() */
    private volatile Thread runner;
    /** Treiber stack of waiting threads */
    private volatile WaitNode waiters;

    /**
     * Returns result or throws exception for completed task.
     *
     * @param s completed state value
     */
    private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL) {
            @SuppressWarnings("unchecked") V v = (V)x;
            return v;
        }
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable)x);
    }

    /**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Callable}.
*
* @param  callable the callable task
     * @throws NullPointerException if the callable is null
*/
public FutureTask(Callable<V> callable) {
if (callable == null)
throw new NullPointerException();
        this.callable = callable;
        this.state = NEW;       // ensure visibility of callable
}

/**
     * Creates a {@code FutureTask} that will, upon running, execute the
     * given {@code Runnable}, and arrange that {@code get} will return the
* given result on successful completion.
*
* @param runnable the runnable task
//Synthetic comment -- @@ -57,29 +118,46 @@
* you don't need a particular result, consider using
* constructions of the form:
* {@code Future<?> f = new FutureTask<Void>(runnable, null)}
     * @throws NullPointerException if the runnable is null
*/
public FutureTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state = NEW;       // ensure visibility of callable
}

public boolean isCancelled() {
        return state >= CANCELLED;
}

public boolean isDone() {
        return state != NEW;
}

public boolean cancel(boolean mayInterruptIfRunning) {
        if (state != NEW)
            return false;
        if (mayInterruptIfRunning) {
            if (!UNSAFE.compareAndSwapInt(this, stateOffset, NEW, INTERRUPTING))
                return false;
            Thread t = runner;
            if (t != null)
                t.interrupt();
            UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED); // final state
        }
        else if (!UNSAFE.compareAndSwapInt(this, stateOffset, NEW, CANCELLED))
            return false;
        finishCompletion();
        return true;
}

/**
* @throws CancellationException {@inheritDoc}
*/
public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING)
            s = awaitDone(false, 0L);
        return report(s);
}

/**
//Synthetic comment -- @@ -87,12 +165,18 @@
*/
public V get(long timeout, TimeUnit unit)
throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null)
            throw new NullPointerException();
        int s = state;
        if (s <= COMPLETING &&
            (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING)
            throw new TimeoutException();
        return report(s);
}

/**
* Protected method invoked when this task transitions to state
     * {@code isDone} (whether normally or via cancellation). The
* default implementation does nothing.  Subclasses may override
* this method to invoke completion callbacks or perform
* bookkeeping. Note that you can query status inside the
//Synthetic comment -- @@ -102,230 +186,268 @@
protected void done() { }

/**
     * Sets the result of this future to the given value unless
* this future has already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
* upon successful completion of the computation.
     *
* @param v the value
*/
protected void set(V v) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = v;
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
            finishCompletion();
        }
}

/**
     * Causes this future to report an {@link ExecutionException}
     * with the given throwable as its cause, unless this future has
* already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
* upon failure of the computation.
     *
* @param t the cause of failure
*/
protected void setException(Throwable t) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = t;
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
            finishCompletion();
        }
}

public void run() {
        if (state != NEW ||
            !UNSAFE.compareAndSwapObject(this, runnerOffset,
                                         null, Thread.currentThread()))
            return;
        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    setException(ex);
                }
                if (ran)
                    set(result);
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            int s = state;
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
}

/**
* Executes the computation without setting its result, and then
     * resets this future to initial state, failing to do so if the
* computation encounters an exception or is cancelled.  This is
* designed for use with tasks that intrinsically execute more
* than once.
     *
* @return true if successfully run and reset
*/
protected boolean runAndReset() {
        if (state != NEW ||
            !UNSAFE.compareAndSwapObject(this, runnerOffset,
                                         null, Thread.currentThread()))
            return false;
        boolean ran = false;
        int s = state;
        try {
            Callable<V> c = callable;
            if (c != null && s == NEW) {
                try {
                    c.call(); // don't set result
                    ran = true;
                } catch (Throwable ex) {
                    setException(ex);
                }
            }
        } finally {
            // runner must be non-null until state is settled to
            // prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent
            // leaked interrupts
            s = state;
            if (s >= INTERRUPTING)
                handlePossibleCancellationInterrupt(s);
        }
        return ran && s == NEW;
}

/**
     * Ensures that any interrupt from a possible cancel(true) is only
     * delivered to a task while in run or runAndReset.
*/
    private void handlePossibleCancellationInterrupt(int s) {
        // It is possible for our interrupter to stall before getting a
        // chance to interrupt us.  Let's spin-wait patiently.
        if (s == INTERRUPTING)
            while (state == INTERRUPTING)
                Thread.yield(); // wait out pending interrupt

        // assert state == INTERRUPTED;

        // We want to clear any interrupt we may have received from
        // cancel(true).  However, it is permissible to use interrupts
        // as an independent mechanism for a task to communicate with
        // its caller, and there is no way to clear only the
        // cancellation interrupt.
        //
        // Thread.interrupted();
    }

    /**
     * Simple linked list nodes to record waiting threads in a Treiber
     * stack.  See other classes such as Phaser and SynchronousQueue
     * for more detailed explanation.
     */
    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;
        WaitNode() { thread = Thread.currentThread(); }
    }

    /**
     * Removes and signals all waiting threads, invokes done(), and
     * nulls out callable.
     */
    private void finishCompletion() {
        // assert state > COMPLETING;
        for (WaitNode q; (q = waiters) != null;) {
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (;;) {
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next = q.next;
                    if (next == null)
                        break;
                    q.next = null; // unlink to help gc
                    q = next;
}
                break;
}
}

        done();

        callable = null;        // to reduce footprint
    }

    /**
     * Awaits completion or aborts on interrupt or timeout.
     *
     * @param timed true if use timed waits
     * @param nanos time to wait, if timed
     * @return state upon completion
     */
    private int awaitDone(boolean timed, long nanos)
        throws InterruptedException {
        long last = timed ? System.nanoTime() : 0L;
        WaitNode q = null;
        boolean queued = false;
        for (;;) {
            if (Thread.interrupted()) {
                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            if (s > COMPLETING) {
                if (q != null)
                    q.thread = null;
                return s;
            }
            else if (q == null)
                q = new WaitNode();
            else if (!queued)
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset,
                                                     q.next = waiters, q);
            else if (timed) {
                long now = System.nanoTime();
                if ((nanos -= (now - last)) <= 0L) {
                    removeWaiter(q);
                    return state;
}
                last = now;
                LockSupport.parkNanos(this, nanos);
            }
            else
                LockSupport.park(this);
        }
    }

    /**
     * Tries to unlink a timed-out or interrupted wait node to avoid
     * accumulating garbage.  Internal nodes are simply unspliced
     * without CAS since it is harmless if they are traversed anyway
     * by releasers.  To avoid effects of unsplicing from already
     * removed nodes, the list is retraversed in case of an apparent
     * race.  This is slow when there are a lot of nodes, but we don't
     * expect lists to be long enough to outweigh higher-overhead
     * schemes.
     */
    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (;;) {          // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null)
                        pred = q;
                    else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) // check for race
                            continue retry;
                    }
                    else if (!UNSAFE.compareAndSwapObject(this, waitersOffset,
                                                          q, s))
                        continue retry;
}
                break;
}
}
}

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = FutureTask.class;
            stateOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedBlockingDeque.java b/luni/src/main/java/java/util/concurrent/LinkedBlockingDeque.java
//Synthetic comment -- index 8c01e71..6f32c47 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -13,6 +13,10 @@
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
* An optionally-bounded {@linkplain BlockingDeque blocking deque} based on
* linked nodes.
//Synthetic comment -- @@ -34,10 +38,6 @@
* <em>optional</em> methods of the {@link Collection} and {@link
* Iterator} interfaces.
*
* @since 1.6
* @author  Doug Lea
* @param <E> the type of elements held in this collection
//Synthetic comment -- @@ -590,7 +590,7 @@
/**
* Inserts the specified element at the end of this deque unless it would
* violate capacity restrictions.  When using a capacity-restricted deque,
     * it is generally preferable to use method {@link #offer(Object) offer}.
*
* <p>This method is equivalent to {@link #addLast}.
*
//Synthetic comment -- @@ -713,6 +713,8 @@
throw new NullPointerException();
if (c == this)
throw new IllegalArgumentException();
        if (maxElements <= 0)
            return 0;
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -891,8 +893,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -1014,7 +1015,7 @@
/**
* The next node to return in next()
*/
        Node<E> next;

/**
* nextItem holds on to item fields because once we claim that
//Synthetic comment -- @@ -1122,7 +1123,7 @@
}

/**
     * Saves the state of this deque to a stream (that is, serializes it).
*
* @serialData The capacity (int), followed by elements (each an
* {@code Object}) in the proper order, followed by a null
//Synthetic comment -- @@ -1146,8 +1147,8 @@
}

/**
     * Reconstitutes this deque from a stream (that is, deserializes it).
     *
* @param s the stream
*/
private void readObject(java.io.ObjectInputStream s)








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedBlockingQueue.java b/luni/src/main/java/java/util/concurrent/LinkedBlockingQueue.java
//Synthetic comment -- index d06c737..e8c9edb 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -106,13 +106,13 @@
private final int capacity;

/** Current number of elements */
    private final AtomicInteger count = new AtomicInteger();

/**
* Head of linked list.
* Invariant: head.item == null
*/
    transient Node<E> head;

/**
* Tail of linked list.
//Synthetic comment -- @@ -303,7 +303,7 @@
// Note: convention in all put/take/etc is to preset local var
// holding count negative to indicate failure unless set.
int c = -1;
        Node<E> node = new Node<E>(e);
final ReentrantLock putLock = this.putLock;
final AtomicInteger count = this.count;
putLock.lockInterruptibly();
//Synthetic comment -- @@ -383,7 +383,7 @@
if (count.get() == capacity)
return false;
int c = -1;
        Node<E> node = new Node<E>(e);
final ReentrantLock putLock = this.putLock;
putLock.lock();
try {
//Synthetic comment -- @@ -601,8 +601,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -699,6 +698,8 @@
throw new NullPointerException();
if (c == this)
throw new IllegalArgumentException();
        if (maxElements <= 0)
            return 0;
boolean signalNotFull = false;
final ReentrantLock takeLock = this.takeLock;
takeLock.lock();
//Synthetic comment -- @@ -746,7 +747,7 @@
* @return an iterator over the elements in this queue in proper sequence
*/
public Iterator<E> iterator() {
        return new Itr();
}

private class Itr implements Iterator<E> {
//Synthetic comment -- @@ -829,7 +830,7 @@
}

/**
     * Saves the state to a stream (that is, serializes it).
*
* @serialData The capacity is emitted (int), followed by all of
* its elements (each an {@code Object}) in the proper order,
//Synthetic comment -- @@ -856,8 +857,7 @@
}

/**
     * Reconstitutes this queue from a stream (that is, deserializes it).
*
* @param s the stream
*/








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedTransferQueue.java b/luni/src/main/java/java/util/concurrent/LinkedTransferQueue.java
new file mode 100644
//Synthetic comment -- index 0000000..2a3446e

//Synthetic comment -- @@ -0,0 +1,1323 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
 * An unbounded {@link TransferQueue} based on linked nodes.
 * This queue orders elements FIFO (first-in-first-out) with respect
 * to any given producer.  The <em>head</em> of the queue is that
 * element that has been on the queue the longest time for some
 * producer.  The <em>tail</em> of the queue is that element that has
 * been on the queue the shortest time for some producer.
 *
 * <p>Beware that, unlike in most collections, the {@code size} method
 * is <em>NOT</em> a constant-time operation. Because of the
 * asynchronous nature of these queues, determining the current number
 * of elements requires a traversal of the elements, and so may report
 * inaccurate results if this collection is modified during traversal.
 * Additionally, the bulk operations {@code addAll},
 * {@code removeAll}, {@code retainAll}, {@code containsAll},
 * {@code equals}, and {@code toArray} are <em>not</em> guaranteed
 * to be performed atomically. For example, an iterator operating
 * concurrently with an {@code addAll} operation might view only some
 * of the added elements.
 *
 * <p>This class and its iterator implement all of the
 * <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
 *
 * <p>Memory consistency effects: As with other concurrent
 * collections, actions in a thread prior to placing an object into a
 * {@code LinkedTransferQueue}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from
 * the {@code LinkedTransferQueue} in another thread.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
public class LinkedTransferQueue<E> extends AbstractQueue<E>
    implements TransferQueue<E>, java.io.Serializable {
    private static final long serialVersionUID = -3223113410248163686L;

    /*
     * *** Overview of Dual Queues with Slack ***
     *
     * Dual Queues, introduced by Scherer and Scott
     * (http://www.cs.rice.edu/~wns1/papers/2004-DISC-DDS.pdf) are
     * (linked) queues in which nodes may represent either data or
     * requests.  When a thread tries to enqueue a data node, but
     * encounters a request node, it instead "matches" and removes it;
     * and vice versa for enqueuing requests. Blocking Dual Queues
     * arrange that threads enqueuing unmatched requests block until
     * other threads provide the match. Dual Synchronous Queues (see
     * Scherer, Lea, & Scott
     * http://www.cs.rochester.edu/u/scott/papers/2009_Scherer_CACM_SSQ.pdf)
     * additionally arrange that threads enqueuing unmatched data also
     * block.  Dual Transfer Queues support all of these modes, as
     * dictated by callers.
     *
     * A FIFO dual queue may be implemented using a variation of the
     * Michael & Scott (M&S) lock-free queue algorithm
     * (http://www.cs.rochester.edu/u/scott/papers/1996_PODC_queues.pdf).
     * It maintains two pointer fields, "head", pointing to a
     * (matched) node that in turn points to the first actual
     * (unmatched) queue node (or null if empty); and "tail" that
     * points to the last node on the queue (or again null if
     * empty). For example, here is a possible queue with four data
     * elements:
     *
     *  head                tail
     *    |                   |
     *    v                   v
     *    M -> U -> U -> U -> U
     *
     * The M&S queue algorithm is known to be prone to scalability and
     * overhead limitations when maintaining (via CAS) these head and
     * tail pointers. This has led to the development of
     * contention-reducing variants such as elimination arrays (see
     * Moir et al http://portal.acm.org/citation.cfm?id=1074013) and
     * optimistic back pointers (see Ladan-Mozes & Shavit
     * http://people.csail.mit.edu/edya/publications/OptimisticFIFOQueue-journal.pdf).
     * However, the nature of dual queues enables a simpler tactic for
     * improving M&S-style implementations when dual-ness is needed.
     *
     * In a dual queue, each node must atomically maintain its match
     * status. While there are other possible variants, we implement
     * this here as: for a data-mode node, matching entails CASing an
     * "item" field from a non-null data value to null upon match, and
     * vice-versa for request nodes, CASing from null to a data
     * value. (Note that the linearization properties of this style of
     * queue are easy to verify -- elements are made available by
     * linking, and unavailable by matching.) Compared to plain M&S
     * queues, this property of dual queues requires one additional
     * successful atomic operation per enq/deq pair. But it also
     * enables lower cost variants of queue maintenance mechanics. (A
     * variation of this idea applies even for non-dual queues that
     * support deletion of interior elements, such as
     * j.u.c.ConcurrentLinkedQueue.)
     *
     * Once a node is matched, its match status can never again
     * change.  We may thus arrange that the linked list of them
     * contain a prefix of zero or more matched nodes, followed by a
     * suffix of zero or more unmatched nodes. (Note that we allow
     * both the prefix and suffix to be zero length, which in turn
     * means that we do not use a dummy header.)  If we were not
     * concerned with either time or space efficiency, we could
     * correctly perform enqueue and dequeue operations by traversing
     * from a pointer to the initial node; CASing the item of the
     * first unmatched node on match and CASing the next field of the
     * trailing node on appends. (Plus some special-casing when
     * initially empty).  While this would be a terrible idea in
     * itself, it does have the benefit of not requiring ANY atomic
     * updates on head/tail fields.
     *
     * We introduce here an approach that lies between the extremes of
     * never versus always updating queue (head and tail) pointers.
     * This offers a tradeoff between sometimes requiring extra
     * traversal steps to locate the first and/or last unmatched
     * nodes, versus the reduced overhead and contention of fewer
     * updates to queue pointers. For example, a possible snapshot of
     * a queue is:
     *
     *  head           tail
     *    |              |
     *    v              v
     *    M -> M -> U -> U -> U -> U
     *
     * The best value for this "slack" (the targeted maximum distance
     * between the value of "head" and the first unmatched node, and
     * similarly for "tail") is an empirical matter. We have found
     * that using very small constants in the range of 1-3 work best
     * over a range of platforms. Larger values introduce increasing
     * costs of cache misses and risks of long traversal chains, while
     * smaller values increase CAS contention and overhead.
     *
     * Dual queues with slack differ from plain M&S dual queues by
     * virtue of only sometimes updating head or tail pointers when
     * matching, appending, or even traversing nodes; in order to
     * maintain a targeted slack.  The idea of "sometimes" may be
     * operationalized in several ways. The simplest is to use a
     * per-operation counter incremented on each traversal step, and
     * to try (via CAS) to update the associated queue pointer
     * whenever the count exceeds a threshold. Another, that requires
     * more overhead, is to use random number generators to update
     * with a given probability per traversal step.
     *
     * In any strategy along these lines, because CASes updating
     * fields may fail, the actual slack may exceed targeted
     * slack. However, they may be retried at any time to maintain
     * targets.  Even when using very small slack values, this
     * approach works well for dual queues because it allows all
     * operations up to the point of matching or appending an item
     * (hence potentially allowing progress by another thread) to be
     * read-only, thus not introducing any further contention. As
     * described below, we implement this by performing slack
     * maintenance retries only after these points.
     *
     * As an accompaniment to such techniques, traversal overhead can
     * be further reduced without increasing contention of head
     * pointer updates: Threads may sometimes shortcut the "next" link
     * path from the current "head" node to be closer to the currently
     * known first unmatched node, and similarly for tail. Again, this
     * may be triggered with using thresholds or randomization.
     *
     * These ideas must be further extended to avoid unbounded amounts
     * of costly-to-reclaim garbage caused by the sequential "next"
     * links of nodes starting at old forgotten head nodes: As first
     * described in detail by Boehm
     * (http://portal.acm.org/citation.cfm?doid=503272.503282) if a GC
     * delays noticing that any arbitrarily old node has become
     * garbage, all newer dead nodes will also be unreclaimed.
     * (Similar issues arise in non-GC environments.)  To cope with
     * this in our implementation, upon CASing to advance the head
     * pointer, we set the "next" link of the previous head to point
     * only to itself; thus limiting the length of connected dead lists.
     * (We also take similar care to wipe out possibly garbage
     * retaining values held in other Node fields.)  However, doing so
     * adds some further complexity to traversal: If any "next"
     * pointer links to itself, it indicates that the current thread
     * has lagged behind a head-update, and so the traversal must
     * continue from the "head".  Traversals trying to find the
     * current tail starting from "tail" may also encounter
     * self-links, in which case they also continue at "head".
     *
     * It is tempting in slack-based scheme to not even use CAS for
     * updates (similarly to Ladan-Mozes & Shavit). However, this
     * cannot be done for head updates under the above link-forgetting
     * mechanics because an update may leave head at a detached node.
     * And while direct writes are possible for tail updates, they
     * increase the risk of long retraversals, and hence long garbage
     * chains, which can be much more costly than is worthwhile
     * considering that the cost difference of performing a CAS vs
     * write is smaller when they are not triggered on each operation
     * (especially considering that writes and CASes equally require
     * additional GC bookkeeping ("write barriers") that are sometimes
     * more costly than the writes themselves because of contention).
     *
     * *** Overview of implementation ***
     *
     * We use a threshold-based approach to updates, with a slack
     * threshold of two -- that is, we update head/tail when the
     * current pointer appears to be two or more steps away from the
     * first/last node. The slack value is hard-wired: a path greater
     * than one is naturally implemented by checking equality of
     * traversal pointers except when the list has only one element,
     * in which case we keep slack threshold at one. Avoiding tracking
     * explicit counts across method calls slightly simplifies an
     * already-messy implementation. Using randomization would
     * probably work better if there were a low-quality dirt-cheap
     * per-thread one available, but even ThreadLocalRandom is too
     * heavy for these purposes.
     *
     * With such a small slack threshold value, it is not worthwhile
     * to augment this with path short-circuiting (i.e., unsplicing
     * interior nodes) except in the case of cancellation/removal (see
     * below).
     *
     * We allow both the head and tail fields to be null before any
     * nodes are enqueued; initializing upon first append.  This
     * simplifies some other logic, as well as providing more
     * efficient explicit control paths instead of letting JVMs insert
     * implicit NullPointerExceptions when they are null.  While not
     * currently fully implemented, we also leave open the possibility
     * of re-nulling these fields when empty (which is complicated to
     * arrange, for little benefit.)
     *
     * All enqueue/dequeue operations are handled by the single method
     * "xfer" with parameters indicating whether to act as some form
     * of offer, put, poll, take, or transfer (each possibly with
     * timeout). The relative complexity of using one monolithic
     * method outweighs the code bulk and maintenance problems of
     * using separate methods for each case.
     *
     * Operation consists of up to three phases. The first is
     * implemented within method xfer, the second in tryAppend, and
     * the third in method awaitMatch.
     *
     * 1. Try to match an existing node
     *
     *    Starting at head, skip already-matched nodes until finding
     *    an unmatched node of opposite mode, if one exists, in which
     *    case matching it and returning, also if necessary updating
     *    head to one past the matched node (or the node itself if the
     *    list has no other unmatched nodes). If the CAS misses, then
     *    a loop retries advancing head by two steps until either
     *    success or the slack is at most two. By requiring that each
     *    attempt advances head by two (if applicable), we ensure that
     *    the slack does not grow without bound. Traversals also check
     *    if the initial head is now off-list, in which case they
     *    start at the new head.
     *
     *    If no candidates are found and the call was untimed
     *    poll/offer, (argument "how" is NOW) return.
     *
     * 2. Try to append a new node (method tryAppend)
     *
     *    Starting at current tail pointer, find the actual last node
     *    and try to append a new node (or if head was null, establish
     *    the first node). Nodes can be appended only if their
     *    predecessors are either already matched or are of the same
     *    mode. If we detect otherwise, then a new node with opposite
     *    mode must have been appended during traversal, so we must
     *    restart at phase 1. The traversal and update steps are
     *    otherwise similar to phase 1: Retrying upon CAS misses and
     *    checking for staleness.  In particular, if a self-link is
     *    encountered, then we can safely jump to a node on the list
     *    by continuing the traversal at current head.
     *
     *    On successful append, if the call was ASYNC, return.
     *
     * 3. Await match or cancellation (method awaitMatch)
     *
     *    Wait for another thread to match node; instead cancelling if
     *    the current thread was interrupted or the wait timed out. On
     *    multiprocessors, we use front-of-queue spinning: If a node
     *    appears to be the first unmatched node in the queue, it
     *    spins a bit before blocking. In either case, before blocking
     *    it tries to unsplice any nodes between the current "head"
     *    and the first unmatched node.
     *
     *    Front-of-queue spinning vastly improves performance of
     *    heavily contended queues. And so long as it is relatively
     *    brief and "quiet", spinning does not much impact performance
     *    of less-contended queues.  During spins threads check their
     *    interrupt status and generate a thread-local random number
     *    to decide to occasionally perform a Thread.yield. While
     *    yield has underdefined specs, we assume that it might help,
     *    and will not hurt, in limiting impact of spinning on busy
     *    systems.  We also use smaller (1/2) spins for nodes that are
     *    not known to be front but whose predecessors have not
     *    blocked -- these "chained" spins avoid artifacts of
     *    front-of-queue rules which otherwise lead to alternating
     *    nodes spinning vs blocking. Further, front threads that
     *    represent phase changes (from data to request node or vice
     *    versa) compared to their predecessors receive additional
     *    chained spins, reflecting longer paths typically required to
     *    unblock threads during phase changes.
     *
     *
     * ** Unlinking removed interior nodes **
     *
     * In addition to minimizing garbage retention via self-linking
     * described above, we also unlink removed interior nodes. These
     * may arise due to timed out or interrupted waits, or calls to
     * remove(x) or Iterator.remove.  Normally, given a node that was
     * at one time known to be the predecessor of some node s that is
     * to be removed, we can unsplice s by CASing the next field of
     * its predecessor if it still points to s (otherwise s must
     * already have been removed or is now offlist). But there are two
     * situations in which we cannot guarantee to make node s
     * unreachable in this way: (1) If s is the trailing node of list
     * (i.e., with null next), then it is pinned as the target node
     * for appends, so can only be removed later after other nodes are
     * appended. (2) We cannot necessarily unlink s given a
     * predecessor node that is matched (including the case of being
     * cancelled): the predecessor may already be unspliced, in which
     * case some previous reachable node may still point to s.
     * (For further explanation see Herlihy & Shavit "The Art of
     * Multiprocessor Programming" chapter 9).  Although, in both
     * cases, we can rule out the need for further action if either s
     * or its predecessor are (or can be made to be) at, or fall off
     * from, the head of list.
     *
     * Without taking these into account, it would be possible for an
     * unbounded number of supposedly removed nodes to remain
     * reachable.  Situations leading to such buildup are uncommon but
     * can occur in practice; for example when a series of short timed
     * calls to poll repeatedly time out but never otherwise fall off
     * the list because of an untimed call to take at the front of the
     * queue.
     *
     * When these cases arise, rather than always retraversing the
     * entire list to find an actual predecessor to unlink (which
     * won't help for case (1) anyway), we record a conservative
     * estimate of possible unsplice failures (in "sweepVotes").
     * We trigger a full sweep when the estimate exceeds a threshold
     * ("SWEEP_THRESHOLD") indicating the maximum number of estimated
     * removal failures to tolerate before sweeping through, unlinking
     * cancelled nodes that were not unlinked upon initial removal.
     * We perform sweeps by the thread hitting threshold (rather than
     * background threads or by spreading work to other threads)
     * because in the main contexts in which removal occurs, the
     * caller is already timed-out, cancelled, or performing a
     * potentially O(n) operation (e.g. remove(x)), none of which are
     * time-critical enough to warrant the overhead that alternatives
     * would impose on other threads.
     *
     * Because the sweepVotes estimate is conservative, and because
     * nodes become unlinked "naturally" as they fall off the head of
     * the queue, and because we allow votes to accumulate even while
     * sweeps are in progress, there are typically significantly fewer
     * such nodes than estimated.  Choice of a threshold value
     * balances the likelihood of wasted effort and contention, versus
     * providing a worst-case bound on retention of interior nodes in
     * quiescent queues. The value defined below was chosen
     * empirically to balance these under various timeout scenarios.
     *
     * Note that we cannot self-link unlinked interior nodes during
     * sweeps. However, the associated garbage chains terminate when
     * some successor ultimately falls off the head of the list and is
     * self-linked.
     */

    /** True if on multiprocessor */
    private static final boolean MP =
        Runtime.getRuntime().availableProcessors() > 1;

    /**
     * The number of times to spin (with randomly interspersed calls
     * to Thread.yield) on multiprocessor before blocking when a node
     * is apparently the first waiter in the queue.  See above for
     * explanation. Must be a power of two. The value is empirically
     * derived -- it works pretty well across a variety of processors,
     * numbers of CPUs, and OSes.
     */
    private static final int FRONT_SPINS   = 1 << 7;

    /**
     * The number of times to spin before blocking when a node is
     * preceded by another node that is apparently spinning.  Also
     * serves as an increment to FRONT_SPINS on phase changes, and as
     * base average frequency for yielding during spins. Must be a
     * power of two.
     */
    private static final int CHAINED_SPINS = FRONT_SPINS >>> 1;

    /**
     * The maximum number of estimated removal failures (sweepVotes)
     * to tolerate before sweeping through the queue unlinking
     * cancelled nodes that were not unlinked upon initial
     * removal. See above for explanation. The value must be at least
     * two to avoid useless sweeps when removing trailing nodes.
     */
    static final int SWEEP_THRESHOLD = 32;

    /**
     * Queue nodes. Uses Object, not E, for items to allow forgetting
     * them after use.  Relies heavily on Unsafe mechanics to minimize
     * unnecessary ordering constraints: Writes that are intrinsically
     * ordered wrt other accesses or CASes use simple relaxed forms.
     */
    static final class Node {
        final boolean isData;   // false if this is a request node
        volatile Object item;   // initially non-null if isData; CASed to match
        volatile Node next;
        volatile Thread waiter; // null until waiting

        // CAS methods for fields
        final boolean casNext(Node cmp, Node val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }

        final boolean casItem(Object cmp, Object val) {
            // assert cmp == null || cmp.getClass() != Node.class;
            return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
        }

        /**
         * Constructs a new node.  Uses relaxed write because item can
         * only be seen after publication via casNext.
         */
        Node(Object item, boolean isData) {
            UNSAFE.putObject(this, itemOffset, item); // relaxed write
            this.isData = isData;
        }

        /**
         * Links node to itself to avoid garbage retention.  Called
         * only after CASing head field, so uses relaxed write.
         */
        final void forgetNext() {
            UNSAFE.putObject(this, nextOffset, this);
        }

        /**
         * Sets item to self and waiter to null, to avoid garbage
         * retention after matching or cancelling. Uses relaxed writes
         * because order is already constrained in the only calling
         * contexts: item is forgotten only after volatile/atomic
         * mechanics that extract items.  Similarly, clearing waiter
         * follows either CAS or return from park (if ever parked;
         * else we don't care).
         */
        final void forgetContents() {
            UNSAFE.putObject(this, itemOffset, this);
            UNSAFE.putObject(this, waiterOffset, null);
        }

        /**
         * Returns true if this node has been matched, including the
         * case of artificial matches due to cancellation.
         */
        final boolean isMatched() {
            Object x = item;
            return (x == this) || ((x == null) == isData);
        }

        /**
         * Returns true if this is an unmatched request node.
         */
        final boolean isUnmatchedRequest() {
            return !isData && item == null;
        }

        /**
         * Returns true if a node with the given mode cannot be
         * appended to this node because this node is unmatched and
         * has opposite data mode.
         */
        final boolean cannotPrecede(boolean haveData) {
            boolean d = isData;
            Object x;
            return d != haveData && (x = item) != this && (x != null) == d;
        }

        /**
         * Tries to artificially match a data node -- used by remove.
         */
        final boolean tryMatchData() {
            // assert isData;
            Object x = item;
            if (x != null && x != this && casItem(x, null)) {
                LockSupport.unpark(waiter);
                return true;
            }
            return false;
        }

        private static final long serialVersionUID = -3375979862319811754L;

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;
        private static final long waiterOffset;
        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                itemOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("next"));
                waiterOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiter"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    /** head of the queue; null until first enqueue */
    transient volatile Node head;

    /** tail of the queue; null until first append */
    private transient volatile Node tail;

    /** The number of apparent failures to unsplice removed nodes */
    private transient volatile int sweepVotes;

    // CAS methods for fields
    private boolean casTail(Node cmp, Node val) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
    }

    private boolean casHead(Node cmp, Node val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }

    private boolean casSweepVotes(int cmp, int val) {
        return UNSAFE.compareAndSwapInt(this, sweepVotesOffset, cmp, val);
    }

    /*
     * Possible values for "how" argument in xfer method.
     */
    private static final int NOW   = 0; // for untimed poll, tryTransfer
    private static final int ASYNC = 1; // for offer, put, add
    private static final int SYNC  = 2; // for transfer, take
    private static final int TIMED = 3; // for timed poll, tryTransfer

    @SuppressWarnings("unchecked")
    static <E> E cast(Object item) {
        // assert item == null || item.getClass() != Node.class;
        return (E) item;
    }

    /**
     * Implements all queuing methods. See above for explanation.
     *
     * @param e the item or null for take
     * @param haveData true if this is a put, else a take
     * @param how NOW, ASYNC, SYNC, or TIMED
     * @param nanos timeout in nanosecs, used only if mode is TIMED
     * @return an item if matched, else e
     * @throws NullPointerException if haveData mode but e is null
     */
    private E xfer(E e, boolean haveData, int how, long nanos) {
        if (haveData && (e == null))
            throw new NullPointerException();
        Node s = null;                        // the node to append, if needed

        retry:
        for (;;) {                            // restart on append race

            for (Node h = head, p = h; p != null;) { // find & match first node
                boolean isData = p.isData;
                Object item = p.item;
                if (item != p && (item != null) == isData) { // unmatched
                    if (isData == haveData)   // can't match
                        break;
                    if (p.casItem(item, e)) { // match
                        for (Node q = p; q != h;) {
                            Node n = q.next;  // update by 2 unless singleton
                            if (head == h && casHead(h, n == null ? q : n)) {
                                h.forgetNext();
                                break;
                            }                 // advance and retry
                            if ((h = head)   == null ||
                                (q = h.next) == null || !q.isMatched())
                                break;        // unless slack < 2
                        }
                        LockSupport.unpark(p.waiter);
                        return LinkedTransferQueue.<E>cast(item);
                    }
                }
                Node n = p.next;
                p = (p != n) ? n : (h = head); // Use head if p offlist
            }

            if (how != NOW) {                 // No matches available
                if (s == null)
                    s = new Node(e, haveData);
                Node pred = tryAppend(s, haveData);
                if (pred == null)
                    continue retry;           // lost race vs opposite mode
                if (how != ASYNC)
                    return awaitMatch(s, pred, e, (how == TIMED), nanos);
            }
            return e; // not waiting
        }
    }

    /**
     * Tries to append node s as tail.
     *
     * @param s the node to append
     * @param haveData true if appending in data mode
     * @return null on failure due to losing race with append in
     * different mode, else s's predecessor, or s itself if no
     * predecessor
     */
    private Node tryAppend(Node s, boolean haveData) {
        for (Node t = tail, p = t;;) {        // move p to last node and append
            Node n, u;                        // temps for reads of next & tail
            if (p == null && (p = head) == null) {
                if (casHead(null, s))
                    return s;                 // initialize
            }
            else if (p.cannotPrecede(haveData))
                return null;                  // lost race vs opposite mode
            else if ((n = p.next) != null)    // not last; keep traversing
                p = p != t && t != (u = tail) ? (t = u) : // stale tail
                    (p != n) ? n : null;      // restart if off list
            else if (!p.casNext(null, s))
                p = p.next;                   // re-read on CAS failure
            else {
                if (p != t) {                 // update if slack now >= 2
                    while ((tail != t || !casTail(t, s)) &&
                           (t = tail)   != null &&
                           (s = t.next) != null && // advance and retry
                           (s = s.next) != null && s != t);
                }
                return p;
            }
        }
    }

    /**
     * Spins/yields/blocks until node s is matched or caller gives up.
     *
     * @param s the waiting node
     * @param pred the predecessor of s, or s itself if it has no
     * predecessor, or null if unknown (the null case does not occur
     * in any current calls but may in possible future extensions)
     * @param e the comparison value for checking match
     * @param timed if true, wait only until timeout elapses
     * @param nanos timeout in nanosecs, used only if timed is true
     * @return matched item, or e if unmatched on interrupt or timeout
     */
    private E awaitMatch(Node s, Node pred, E e, boolean timed, long nanos) {
        long lastTime = timed ? System.nanoTime() : 0L;
        Thread w = Thread.currentThread();
        int spins = -1; // initialized after first item and cancel checks
        ThreadLocalRandom randomYields = null; // bound if needed

        for (;;) {
            Object item = s.item;
            if (item != e) {                  // matched
                // assert item != s;
                s.forgetContents();           // avoid garbage
                return LinkedTransferQueue.<E>cast(item);
            }
            if ((w.isInterrupted() || (timed && nanos <= 0)) &&
                    s.casItem(e, s)) {        // cancel
                unsplice(pred, s);
                return e;
            }

            if (spins < 0) {                  // establish spins at/near front
                if ((spins = spinsFor(pred, s.isData)) > 0)
                    randomYields = ThreadLocalRandom.current();
            }
            else if (spins > 0) {             // spin
                --spins;
                if (randomYields.nextInt(CHAINED_SPINS) == 0)
                    Thread.yield();           // occasionally yield
            }
            else if (s.waiter == null) {
                s.waiter = w;                 // request unpark then recheck
            }
            else if (timed) {
                long now = System.nanoTime();
                if ((nanos -= now - lastTime) > 0)
                    LockSupport.parkNanos(this, nanos);
                lastTime = now;
            }
            else {
                LockSupport.park(this);
            }
        }
    }

    /**
     * Returns spin/yield value for a node with given predecessor and
     * data mode. See above for explanation.
     */
    private static int spinsFor(Node pred, boolean haveData) {
        if (MP && pred != null) {
            if (pred.isData != haveData)      // phase change
                return FRONT_SPINS + CHAINED_SPINS;
            if (pred.isMatched())             // probably at front
                return FRONT_SPINS;
            if (pred.waiter == null)          // pred apparently spinning
                return CHAINED_SPINS;
        }
        return 0;
    }

    /* -------------- Traversal methods -------------- */

    /**
     * Returns the successor of p, or the head node if p.next has been
     * linked to self, which will only be true if traversing with a
     * stale pointer that is now off the list.
     */
    final Node succ(Node p) {
        Node next = p.next;
        return (p == next) ? head : next;
    }

    /**
     * Returns the first unmatched node of the given mode, or null if
     * none.  Used by methods isEmpty, hasWaitingConsumer.
     */
    private Node firstOfMode(boolean isData) {
        for (Node p = head; p != null; p = succ(p)) {
            if (!p.isMatched())
                return (p.isData == isData) ? p : null;
        }
        return null;
    }

    /**
     * Returns the item in the first unmatched node with isData; or
     * null if none.  Used by peek.
     */
    private E firstDataItem() {
        for (Node p = head; p != null; p = succ(p)) {
            Object item = p.item;
            if (p.isData) {
                if (item != null && item != p)
                    return LinkedTransferQueue.<E>cast(item);
            }
            else if (item == null)
                return null;
        }
        return null;
    }

    /**
     * Traverses and counts unmatched nodes of the given mode.
     * Used by methods size and getWaitingConsumerCount.
     */
    private int countOfMode(boolean data) {
        int count = 0;
        for (Node p = head; p != null; ) {
            if (!p.isMatched()) {
                if (p.isData != data)
                    return 0;
                if (++count == Integer.MAX_VALUE) // saturated
                    break;
            }
            Node n = p.next;
            if (n != p)
                p = n;
            else {
                count = 0;
                p = head;
            }
        }
        return count;
    }

    final class Itr implements Iterator<E> {
        private Node nextNode;   // next node to return item for
        private E nextItem;      // the corresponding item
        private Node lastRet;    // last returned node, to support remove
        private Node lastPred;   // predecessor to unlink lastRet

        /**
         * Moves to next node after prev, or first node if prev null.
         */
        private void advance(Node prev) {
            /*
             * To track and avoid buildup of deleted nodes in the face
             * of calls to both Queue.remove and Itr.remove, we must
             * include variants of unsplice and sweep upon each
             * advance: Upon Itr.remove, we may need to catch up links
             * from lastPred, and upon other removes, we might need to
             * skip ahead from stale nodes and unsplice deleted ones
             * found while advancing.
             */

            Node r, b; // reset lastPred upon possible deletion of lastRet
            if ((r = lastRet) != null && !r.isMatched())
                lastPred = r;    // next lastPred is old lastRet
            else if ((b = lastPred) == null || b.isMatched())
                lastPred = null; // at start of list
            else {
                Node s, n;       // help with removal of lastPred.next
                while ((s = b.next) != null &&
                       s != b && s.isMatched() &&
                       (n = s.next) != null && n != s)
                    b.casNext(s, n);
            }

            this.lastRet = prev;

            for (Node p = prev, s, n;;) {
                s = (p == null) ? head : p.next;
                if (s == null)
                    break;
                else if (s == p) {
                    p = null;
                    continue;
                }
                Object item = s.item;
                if (s.isData) {
                    if (item != null && item != s) {
                        nextItem = LinkedTransferQueue.<E>cast(item);
                        nextNode = s;
                        return;
                    }
                }
                else if (item == null)
                    break;
                // assert s.isMatched();
                if (p == null)
                    p = s;
                else if ((n = s.next) == null)
                    break;
                else if (s == n)
                    p = null;
                else
                    p.casNext(s, n);
            }
            nextNode = null;
            nextItem = null;
        }

        Itr() {
            advance(null);
        }

        public final boolean hasNext() {
            return nextNode != null;
        }

        public final E next() {
            Node p = nextNode;
            if (p == null) throw new NoSuchElementException();
            E e = nextItem;
            advance(p);
            return e;
        }

        public final void remove() {
            final Node lastRet = this.lastRet;
            if (lastRet == null)
                throw new IllegalStateException();
            this.lastRet = null;
            if (lastRet.tryMatchData())
                unsplice(lastPred, lastRet);
        }
    }

    /* -------------- Removal methods -------------- */

    /**
     * Unsplices (now or later) the given deleted/cancelled node with
     * the given predecessor.
     *
     * @param pred a node that was at one time known to be the
     * predecessor of s, or null or s itself if s is/was at head
     * @param s the node to be unspliced
     */
    final void unsplice(Node pred, Node s) {
        s.forgetContents(); // forget unneeded fields
        /*
         * See above for rationale. Briefly: if pred still points to
         * s, try to unlink s.  If s cannot be unlinked, because it is
         * trailing node or pred might be unlinked, and neither pred
         * nor s are head or offlist, add to sweepVotes, and if enough
         * votes have accumulated, sweep.
         */
        if (pred != null && pred != s && pred.next == s) {
            Node n = s.next;
            if (n == null ||
                (n != s && pred.casNext(s, n) && pred.isMatched())) {
                for (;;) {               // check if at, or could be, head
                    Node h = head;
                    if (h == pred || h == s || h == null)
                        return;          // at head or list empty
                    if (!h.isMatched())
                        break;
                    Node hn = h.next;
                    if (hn == null)
                        return;          // now empty
                    if (hn != h && casHead(h, hn))
                        h.forgetNext();  // advance head
                }
                if (pred.next != pred && s.next != s) { // recheck if offlist
                    for (;;) {           // sweep now if enough votes
                        int v = sweepVotes;
                        if (v < SWEEP_THRESHOLD) {
                            if (casSweepVotes(v, v + 1))
                                break;
                        }
                        else if (casSweepVotes(v, 0)) {
                            sweep();
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Unlinks matched (typically cancelled) nodes encountered in a
     * traversal from head.
     */
    private void sweep() {
        for (Node p = head, s, n; p != null && (s = p.next) != null; ) {
            if (!s.isMatched())
                // Unmatched nodes are never self-linked
                p = s;
            else if ((n = s.next) == null) // trailing node is pinned
                break;
            else if (s == n)    // stale
                // No need to also check for p == s, since that implies s == n
                p = head;
            else
                p.casNext(s, n);
        }
    }

    /**
     * Main implementation of remove(Object)
     */
    private boolean findAndRemove(Object e) {
        if (e != null) {
            for (Node pred = null, p = head; p != null; ) {
                Object item = p.item;
                if (p.isData) {
                    if (item != null && item != p && e.equals(item) &&
                        p.tryMatchData()) {
                        unsplice(pred, p);
                        return true;
                    }
                }
                else if (item == null)
                    break;
                pred = p;
                if ((p = p.next) == pred) { // stale
                    pred = null;
                    p = head;
                }
            }
        }
        return false;
    }


    /**
     * Creates an initially empty {@code LinkedTransferQueue}.
     */
    public LinkedTransferQueue() {
    }

    /**
     * Creates a {@code LinkedTransferQueue}
     * initially containing the elements of the given collection,
     * added in traversal order of the collection's iterator.
     *
     * @param c the collection of elements to initially contain
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    public LinkedTransferQueue(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /**
     * Inserts the specified element at the tail of this queue.
     * As the queue is unbounded, this method will never block.
     *
     * @throws NullPointerException if the specified element is null
     */
    public void put(E e) {
        xfer(e, true, ASYNC, 0);
    }

    /**
     * Inserts the specified element at the tail of this queue.
     * As the queue is unbounded, this method will never block or
     * return {@code false}.
     *
     * @return {@code true} (as specified by
     *  {@link java.util.concurrent.BlockingQueue#offer(Object,long,TimeUnit)
     *  BlockingQueue.offer})
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e, long timeout, TimeUnit unit) {
        xfer(e, true, ASYNC, 0);
        return true;
    }

    /**
     * Inserts the specified element at the tail of this queue.
     * As the queue is unbounded, this method will never return {@code false}.
     *
     * @return {@code true} (as specified by {@link Queue#offer})
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        xfer(e, true, ASYNC, 0);
        return true;
    }

    /**
     * Inserts the specified element at the tail of this queue.
     * As the queue is unbounded, this method will never throw
     * {@link IllegalStateException} or return {@code false}.
     *
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        xfer(e, true, ASYNC, 0);
        return true;
    }

    /**
     * Transfers the element to a waiting consumer immediately, if possible.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * otherwise returning {@code false} without enqueuing the element.
     *
     * @throws NullPointerException if the specified element is null
     */
    public boolean tryTransfer(E e) {
        return xfer(e, true, NOW, 0) == null;
    }

    /**
     * Transfers the element to a consumer, waiting if necessary to do so.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else inserts the specified element at the tail of this queue
     * and waits until the element is received by a consumer.
     *
     * @throws NullPointerException if the specified element is null
     */
    public void transfer(E e) throws InterruptedException {
        if (xfer(e, true, SYNC, 0) != null) {
            Thread.interrupted(); // failure possible only due to interrupt
            throw new InterruptedException();
        }
    }

    /**
     * Transfers the element to a consumer if it is possible to do so
     * before the timeout elapses.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else inserts the specified element at the tail of this queue
     * and waits until the element is received by a consumer,
     * returning {@code false} if the specified wait time elapses
     * before the element can be transferred.
     *
     * @throws NullPointerException if the specified element is null
     */
    public boolean tryTransfer(E e, long timeout, TimeUnit unit)
        throws InterruptedException {
        if (xfer(e, true, TIMED, unit.toNanos(timeout)) == null)
            return true;
        if (!Thread.interrupted())
            return false;
        throw new InterruptedException();
    }

    public E take() throws InterruptedException {
        E e = xfer(null, false, SYNC, 0);
        if (e != null)
            return e;
        Thread.interrupted();
        throw new InterruptedException();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = xfer(null, false, TIMED, unit.toNanos(timeout));
        if (e != null || !Thread.interrupted())
            return e;
        throw new InterruptedException();
    }

    public E poll() {
        return xfer(null, false, NOW, 0);
    }

    /**
     * @throws NullPointerException     {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; (e = poll()) != null;) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /**
     * @throws NullPointerException     {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        int n = 0;
        for (E e; n < maxElements && (e = poll()) != null;) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /**
     * Returns an iterator over the elements in this queue in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * <p>The returned iterator is a "weakly consistent" iterator that
     * will never throw {@link java.util.ConcurrentModificationException
     * ConcurrentModificationException}, and guarantees to traverse
     * elements as they existed upon construction of the iterator, and
     * may (but is not guaranteed to) reflect any modifications
     * subsequent to construction.
     *
     * @return an iterator over the elements in this queue in proper sequence
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    public E peek() {
        return firstDataItem();
    }

    /**
     * Returns {@code true} if this queue contains no elements.
     *
     * @return {@code true} if this queue contains no elements
     */
    public boolean isEmpty() {
        for (Node p = head; p != null; p = succ(p)) {
            if (!p.isMatched())
                return !p.isData;
        }
        return true;
    }

    public boolean hasWaitingConsumer() {
        return firstOfMode(false) != null;
    }

    /**
     * Returns the number of elements in this queue.  If this queue
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * <p>Beware that, unlike in most collections, this method is
     * <em>NOT</em> a constant-time operation. Because of the
     * asynchronous nature of these queues, determining the current
     * number of elements requires an O(n) traversal.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return countOfMode(true);
    }

    public int getWaitingConsumerCount() {
        return countOfMode(false);
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.
     * Returns {@code true} if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue changed as a result of the call
     */
    public boolean remove(Object o) {
        return findAndRemove(o);
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return {@code true} if this queue contains the specified element
     */
    public boolean contains(Object o) {
        if (o == null) return false;
        for (Node p = head; p != null; p = succ(p)) {
            Object item = p.item;
            if (p.isData) {
                if (item != null && item != p && o.equals(item))
                    return true;
            }
            else if (item == null)
                break;
        }
        return false;
    }

    /**
     * Always returns {@code Integer.MAX_VALUE} because a
     * {@code LinkedTransferQueue} is not capacity constrained.
     *
     * @return {@code Integer.MAX_VALUE} (as specified by
     *         {@link java.util.concurrent.BlockingQueue#remainingCapacity()
     *         BlockingQueue.remainingCapacity})
     */
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    /**
     * Saves the state to a stream (that is, serializes it).
     *
     * @serialData All of the elements (each an {@code E}) in
     * the proper order, followed by a null
     * @param s the stream
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        s.defaultWriteObject();
        for (E e : this)
            s.writeObject(e);
        // Use trailing null as sentinel
        s.writeObject(null);
    }

    /**
     * Reconstitutes the Queue instance from a stream (that is,
     * deserializes it).
     *
     * @param s the stream
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        for (;;) {
            @SuppressWarnings("unchecked") E item = (E) s.readObject();
            if (item == null)
                break;
            else
                offer(item);
        }
    }

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long sweepVotesOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = LinkedTransferQueue.class;
            headOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("tail"));
            sweepVotesOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("sweepVotes"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Phaser.java b/luni/src/main/java/java/util/concurrent/Phaser.java
new file mode 100644
//Synthetic comment -- index 0000000..25ff743

//Synthetic comment -- @@ -0,0 +1,1135 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * A reusable synchronization barrier, similar in functionality to
 * {@link java.util.concurrent.CyclicBarrier CyclicBarrier} and
 * {@link java.util.concurrent.CountDownLatch CountDownLatch}
 * but supporting more flexible usage.
 *
 * <p> <b>Registration.</b> Unlike the case for other barriers, the
 * number of parties <em>registered</em> to synchronize on a phaser
 * may vary over time.  Tasks may be registered at any time (using
 * methods {@link #register}, {@link #bulkRegister}, or forms of
 * constructors establishing initial numbers of parties), and
 * optionally deregistered upon any arrival (using {@link
 * #arriveAndDeregister}).  As is the case with most basic
 * synchronization constructs, registration and deregistration affect
 * only internal counts; they do not establish any further internal
 * bookkeeping, so tasks cannot query whether they are registered.
 * (However, you can introduce such bookkeeping by subclassing this
 * class.)
 *
 * <p> <b>Synchronization.</b> Like a {@code CyclicBarrier}, a {@code
 * Phaser} may be repeatedly awaited.  Method {@link
 * #arriveAndAwaitAdvance} has effect analogous to {@link
 * java.util.concurrent.CyclicBarrier#await CyclicBarrier.await}. Each
 * generation of a phaser has an associated phase number. The phase
 * number starts at zero, and advances when all parties arrive at the
 * phaser, wrapping around to zero after reaching {@code
 * Integer.MAX_VALUE}. The use of phase numbers enables independent
 * control of actions upon arrival at a phaser and upon awaiting
 * others, via two kinds of methods that may be invoked by any
 * registered party:
 *
 * <ul>
 *
 *   <li> <b>Arrival.</b> Methods {@link #arrive} and
 *       {@link #arriveAndDeregister} record arrival.  These methods
 *       do not block, but return an associated <em>arrival phase
 *       number</em>; that is, the phase number of the phaser to which
 *       the arrival applied. When the final party for a given phase
 *       arrives, an optional action is performed and the phase
 *       advances.  These actions are performed by the party
 *       triggering a phase advance, and are arranged by overriding
 *       method {@link #onAdvance(int, int)}, which also controls
 *       termination. Overriding this method is similar to, but more
 *       flexible than, providing a barrier action to a {@code
 *       CyclicBarrier}.
 *
 *   <li> <b>Waiting.</b> Method {@link #awaitAdvance} requires an
 *       argument indicating an arrival phase number, and returns when
 *       the phaser advances to (or is already at) a different phase.
 *       Unlike similar constructions using {@code CyclicBarrier},
 *       method {@code awaitAdvance} continues to wait even if the
 *       waiting thread is interrupted. Interruptible and timeout
 *       versions are also available, but exceptions encountered while
 *       tasks wait interruptibly or with timeout do not change the
 *       state of the phaser. If necessary, you can perform any
 *       associated recovery within handlers of those exceptions,
 *       often after invoking {@code forceTermination}.  Phasers may
 *       also be used by tasks executing in a {@link ForkJoinPool},
 *       which will ensure sufficient parallelism to execute tasks
 *       when others are blocked waiting for a phase to advance.
 *
 * </ul>
 *
 * <p> <b>Termination.</b> A phaser may enter a <em>termination</em>
 * state, that may be checked using method {@link #isTerminated}. Upon
 * termination, all synchronization methods immediately return without
 * waiting for advance, as indicated by a negative return value.
 * Similarly, attempts to register upon termination have no effect.
 * Termination is triggered when an invocation of {@code onAdvance}
 * returns {@code true}. The default implementation returns {@code
 * true} if a deregistration has caused the number of registered
 * parties to become zero.  As illustrated below, when phasers control
 * actions with a fixed number of iterations, it is often convenient
 * to override this method to cause termination when the current phase
 * number reaches a threshold. Method {@link #forceTermination} is
 * also available to abruptly release waiting threads and allow them
 * to terminate.
 *
 * <p> <b>Tiering.</b> Phasers may be <em>tiered</em> (i.e.,
 * constructed in tree structures) to reduce contention. Phasers with
 * large numbers of parties that would otherwise experience heavy
 * synchronization contention costs may instead be set up so that
 * groups of sub-phasers share a common parent.  This may greatly
 * increase throughput even though it incurs greater per-operation
 * overhead.
 *
 * <p>In a tree of tiered phasers, registration and deregistration of
 * child phasers with their parent are managed automatically.
 * Whenever the number of registered parties of a child phaser becomes
 * non-zero (as established in the {@link #Phaser(Phaser,int)}
 * constructor, {@link #register}, or {@link #bulkRegister}), the
 * child phaser is registered with its parent.  Whenever the number of
 * registered parties becomes zero as the result of an invocation of
 * {@link #arriveAndDeregister}, the child phaser is deregistered
 * from its parent.
 *
 * <p><b>Monitoring.</b> While synchronization methods may be invoked
 * only by registered parties, the current state of a phaser may be
 * monitored by any caller.  At any given moment there are {@link
 * #getRegisteredParties} parties in total, of which {@link
 * #getArrivedParties} have arrived at the current phase ({@link
 * #getPhase}).  When the remaining ({@link #getUnarrivedParties})
 * parties arrive, the phase advances.  The values returned by these
 * methods may reflect transient states and so are not in general
 * useful for synchronization control.  Method {@link #toString}
 * returns snapshots of these state queries in a form convenient for
 * informal monitoring.
 *
 * <p><b>Sample usages:</b>
 *
 * <p>A {@code Phaser} may be used instead of a {@code CountDownLatch}
 * to control a one-shot action serving a variable number of parties.
 * The typical idiom is for the method setting this up to first
 * register, then start the actions, then deregister, as in:
 *
 *  <pre> {@code
 * void runTasks(List<Runnable> tasks) {
 *   final Phaser phaser = new Phaser(1); // "1" to register self
 *   // create and start threads
 *   for (final Runnable task : tasks) {
 *     phaser.register();
 *     new Thread() {
 *       public void run() {
 *         phaser.arriveAndAwaitAdvance(); // await all creation
 *         task.run();
 *       }
 *     }.start();
 *   }
 *
 *   // allow threads to start and deregister self
 *   phaser.arriveAndDeregister();
 * }}</pre>
 *
 * <p>One way to cause a set of threads to repeatedly perform actions
 * for a given number of iterations is to override {@code onAdvance}:
 *
 *  <pre> {@code
 * void startTasks(List<Runnable> tasks, final int iterations) {
 *   final Phaser phaser = new Phaser() {
 *     protected boolean onAdvance(int phase, int registeredParties) {
 *       return phase >= iterations || registeredParties == 0;
 *     }
 *   };
 *   phaser.register();
 *   for (final Runnable task : tasks) {
 *     phaser.register();
 *     new Thread() {
 *       public void run() {
 *         do {
 *           task.run();
 *           phaser.arriveAndAwaitAdvance();
 *         } while (!phaser.isTerminated());
 *       }
 *     }.start();
 *   }
 *   phaser.arriveAndDeregister(); // deregister self, don't wait
 * }}</pre>
 *
 * If the main task must later await termination, it
 * may re-register and then execute a similar loop:
 *  <pre> {@code
 *   // ...
 *   phaser.register();
 *   while (!phaser.isTerminated())
 *     phaser.arriveAndAwaitAdvance();}</pre>
 *
 * <p>Related constructions may be used to await particular phase numbers
 * in contexts where you are sure that the phase will never wrap around
 * {@code Integer.MAX_VALUE}. For example:
 *
 *  <pre> {@code
 * void awaitPhase(Phaser phaser, int phase) {
 *   int p = phaser.register(); // assumes caller not already registered
 *   while (p < phase) {
 *     if (phaser.isTerminated())
 *       // ... deal with unexpected termination
 *     else
 *       p = phaser.arriveAndAwaitAdvance();
 *   }
 *   phaser.arriveAndDeregister();
 * }}</pre>
 *
 *
 * <p>To create a set of {@code n} tasks using a tree of phasers, you
 * could use code of the following form, assuming a Task class with a
 * constructor accepting a {@code Phaser} that it registers with upon
 * construction. After invocation of {@code build(new Task[n], 0, n,
 * new Phaser())}, these tasks could then be started, for example by
 * submitting to a pool:
 *
 *  <pre> {@code
 * void build(Task[] tasks, int lo, int hi, Phaser ph) {
 *   if (hi - lo > TASKS_PER_PHASER) {
 *     for (int i = lo; i < hi; i += TASKS_PER_PHASER) {
 *       int j = Math.min(i + TASKS_PER_PHASER, hi);
 *       build(tasks, i, j, new Phaser(ph));
 *     }
 *   } else {
 *     for (int i = lo; i < hi; ++i)
 *       tasks[i] = new Task(ph);
 *       // assumes new Task(ph) performs ph.register()
 *   }
 * }}</pre>
 *
 * The best value of {@code TASKS_PER_PHASER} depends mainly on
 * expected synchronization rates. A value as low as four may
 * be appropriate for extremely small per-phase task bodies (thus
 * high rates), or up to hundreds for extremely large ones.
 *
 * <p><b>Implementation notes</b>: This implementation restricts the
 * maximum number of parties to 65535. Attempts to register additional
 * parties result in {@code IllegalStateException}. However, you can and
 * should create tiered phasers to accommodate arbitrarily large sets
 * of participants.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public class Phaser {
    /*
     * This class implements an extension of X10 "clocks".  Thanks to
     * Vijay Saraswat for the idea, and to Vivek Sarkar for
     * enhancements to extend functionality.
     */

    /**
     * Primary state representation, holding four bit-fields:
     *
     * unarrived  -- the number of parties yet to hit barrier (bits  0-15)
     * parties    -- the number of parties to wait            (bits 16-31)
     * phase      -- the generation of the barrier            (bits 32-62)
     * terminated -- set if barrier is terminated             (bit  63 / sign)
     *
     * Except that a phaser with no registered parties is
     * distinguished by the otherwise illegal state of having zero
     * parties and one unarrived parties (encoded as EMPTY below).
     *
     * To efficiently maintain atomicity, these values are packed into
     * a single (atomic) long. Good performance relies on keeping
     * state decoding and encoding simple, and keeping race windows
     * short.
     *
     * All state updates are performed via CAS except initial
     * registration of a sub-phaser (i.e., one with a non-null
     * parent).  In this (relatively rare) case, we use built-in
     * synchronization to lock while first registering with its
     * parent.
     *
     * The phase of a subphaser is allowed to lag that of its
     * ancestors until it is actually accessed -- see method
     * reconcileState.
     */
    private volatile long state;

    private static final int  MAX_PARTIES     = 0xffff;
    private static final int  MAX_PHASE       = Integer.MAX_VALUE;
    private static final int  PARTIES_SHIFT   = 16;
    private static final int  PHASE_SHIFT     = 32;
    private static final int  UNARRIVED_MASK  = 0xffff;      // to mask ints
    private static final long PARTIES_MASK    = 0xffff0000L; // to mask longs
    private static final long COUNTS_MASK     = 0xffffffffL;
    private static final long TERMINATION_BIT = 1L << 63;

    // some special values
    private static final int  ONE_ARRIVAL     = 1;
    private static final int  ONE_PARTY       = 1 << PARTIES_SHIFT;
    private static final int  ONE_DEREGISTER  = ONE_ARRIVAL|ONE_PARTY;
    private static final int  EMPTY           = 1;

    // The following unpacking methods are usually manually inlined

    private static int unarrivedOf(long s) {
        int counts = (int)s;
        return (counts == EMPTY) ? 0 : (counts & UNARRIVED_MASK);
    }

    private static int partiesOf(long s) {
        return (int)s >>> PARTIES_SHIFT;
    }

    private static int phaseOf(long s) {
        return (int)(s >>> PHASE_SHIFT);
    }

    private static int arrivedOf(long s) {
        int counts = (int)s;
        return (counts == EMPTY) ? 0 :
            (counts >>> PARTIES_SHIFT) - (counts & UNARRIVED_MASK);
    }

    /**
     * The parent of this phaser, or null if none
     */
    private final Phaser parent;

    /**
     * The root of phaser tree. Equals this if not in a tree.
     */
    private final Phaser root;

    /**
     * Heads of Treiber stacks for waiting threads. To eliminate
     * contention when releasing some threads while adding others, we
     * use two of them, alternating across even and odd phases.
     * Subphasers share queues with root to speed up releases.
     */
    private final AtomicReference<QNode> evenQ;
    private final AtomicReference<QNode> oddQ;

    private AtomicReference<QNode> queueFor(int phase) {
        return ((phase & 1) == 0) ? evenQ : oddQ;
    }

    /**
     * Returns message string for bounds exceptions on arrival.
     */
    private String badArrive(long s) {
        return "Attempted arrival of unregistered party for " +
            stateToString(s);
    }

    /**
     * Returns message string for bounds exceptions on registration.
     */
    private String badRegister(long s) {
        return "Attempt to register more than " +
            MAX_PARTIES + " parties for " + stateToString(s);
    }

    /**
     * Main implementation for methods arrive and arriveAndDeregister.
     * Manually tuned to speed up and minimize race windows for the
     * common case of just decrementing unarrived field.
     *
     * @param adjust value to subtract from state;
     *               ONE_ARRIVAL for arrive,
     *               ONE_DEREGISTER for arriveAndDeregister
     */
    private int doArrive(int adjust) {
        final Phaser root = this.root;
        for (;;) {
            long s = (root == this) ? state : reconcileState();
            int phase = (int)(s >>> PHASE_SHIFT);
            if (phase < 0)
                return phase;
            int counts = (int)s;
            int unarrived = (counts == EMPTY) ? 0 : (counts & UNARRIVED_MASK);
            if (unarrived <= 0)
                throw new IllegalStateException(badArrive(s));
            if (UNSAFE.compareAndSwapLong(this, stateOffset, s, s-=adjust)) {
                if (unarrived == 1) {
                    long n = s & PARTIES_MASK;  // base of next state
                    int nextUnarrived = (int)n >>> PARTIES_SHIFT;
                    if (root == this) {
                        if (onAdvance(phase, nextUnarrived))
                            n |= TERMINATION_BIT;
                        else if (nextUnarrived == 0)
                            n |= EMPTY;
                        else
                            n |= nextUnarrived;
                        int nextPhase = (phase + 1) & MAX_PHASE;
                        n |= (long)nextPhase << PHASE_SHIFT;
                        UNSAFE.compareAndSwapLong(this, stateOffset, s, n);
                        releaseWaiters(phase);
                    }
                    else if (nextUnarrived == 0) { // propagate deregistration
                        phase = parent.doArrive(ONE_DEREGISTER);
                        UNSAFE.compareAndSwapLong(this, stateOffset,
                                                  s, s | EMPTY);
                    }
                    else
                        phase = parent.doArrive(ONE_ARRIVAL);
                }
                return phase;
            }
        }
    }

    /**
     * Implementation of register, bulkRegister
     *
     * @param registrations number to add to both parties and
     * unarrived fields. Must be greater than zero.
     */
    private int doRegister(int registrations) {
        // adjustment to state
        long adjust = ((long)registrations << PARTIES_SHIFT) | registrations;
        final Phaser parent = this.parent;
        int phase;
        for (;;) {
            long s = (parent == null) ? state : reconcileState();
            int counts = (int)s;
            int parties = counts >>> PARTIES_SHIFT;
            int unarrived = counts & UNARRIVED_MASK;
            if (registrations > MAX_PARTIES - parties)
                throw new IllegalStateException(badRegister(s));
            phase = (int)(s >>> PHASE_SHIFT);
            if (phase < 0)
                break;
            if (counts != EMPTY) {                  // not 1st registration
                if (parent == null || reconcileState() == s) {
                    if (unarrived == 0)             // wait out advance
                        root.internalAwaitAdvance(phase, null);
                    else if (UNSAFE.compareAndSwapLong(this, stateOffset,
                                                       s, s + adjust))
                        break;
                }
            }
            else if (parent == null) {              // 1st root registration
                long next = ((long)phase << PHASE_SHIFT) | adjust;
                if (UNSAFE.compareAndSwapLong(this, stateOffset, s, next))
                    break;
            }
            else {
                synchronized (this) {               // 1st sub registration
                    if (state == s) {               // recheck under lock
                        phase = parent.doRegister(1);
                        if (phase < 0)
                            break;
                        // finish registration whenever parent registration
                        // succeeded, even when racing with termination,
                        // since these are part of the same "transaction".
                        while (!UNSAFE.compareAndSwapLong
                               (this, stateOffset, s,
                                ((long)phase << PHASE_SHIFT) | adjust)) {
                            s = state;
                            phase = (int)(root.state >>> PHASE_SHIFT);
                            // assert (int)s == EMPTY;
                        }
                        break;
                    }
                }
            }
        }
        return phase;
    }

    /**
     * Resolves lagged phase propagation from root if necessary.
     * Reconciliation normally occurs when root has advanced but
     * subphasers have not yet done so, in which case they must finish
     * their own advance by setting unarrived to parties (or if
     * parties is zero, resetting to unregistered EMPTY state).
     *
     * @return reconciled state
     */
    private long reconcileState() {
        final Phaser root = this.root;
        long s = state;
        if (root != this) {
            int phase, p;
            // CAS to root phase with current parties, tripping unarrived
            while ((phase = (int)(root.state >>> PHASE_SHIFT)) !=
                   (int)(s >>> PHASE_SHIFT) &&
                   !UNSAFE.compareAndSwapLong
                   (this, stateOffset, s,
                    s = (((long)phase << PHASE_SHIFT) |
                         ((phase < 0) ? (s & COUNTS_MASK) :
                          (((p = (int)s >>> PARTIES_SHIFT) == 0) ? EMPTY :
                           ((s & PARTIES_MASK) | p))))))
                s = state;
        }
        return s;
    }

    /**
     * Creates a new phaser with no initially registered parties, no
     * parent, and initial phase number 0. Any thread using this
     * phaser will need to first register for it.
     */
    public Phaser() {
        this(null, 0);
    }

    /**
     * Creates a new phaser with the given number of registered
     * unarrived parties, no parent, and initial phase number 0.
     *
     * @param parties the number of parties required to advance to the
     * next phase
     * @throws IllegalArgumentException if parties less than zero
     * or greater than the maximum number of parties supported
     */
    public Phaser(int parties) {
        this(null, parties);
    }

    /**
     * Equivalent to {@link #Phaser(Phaser, int) Phaser(parent, 0)}.
     *
     * @param parent the parent phaser
     */
    public Phaser(Phaser parent) {
        this(parent, 0);
    }

    /**
     * Creates a new phaser with the given parent and number of
     * registered unarrived parties.  When the given parent is non-null
     * and the given number of parties is greater than zero, this
     * child phaser is registered with its parent.
     *
     * @param parent the parent phaser
     * @param parties the number of parties required to advance to the
     * next phase
     * @throws IllegalArgumentException if parties less than zero
     * or greater than the maximum number of parties supported
     */
    public Phaser(Phaser parent, int parties) {
        if (parties >>> PARTIES_SHIFT != 0)
            throw new IllegalArgumentException("Illegal number of parties");
        int phase = 0;
        this.parent = parent;
        if (parent != null) {
            final Phaser root = parent.root;
            this.root = root;
            this.evenQ = root.evenQ;
            this.oddQ = root.oddQ;
            if (parties != 0)
                phase = parent.doRegister(1);
        }
        else {
            this.root = this;
            this.evenQ = new AtomicReference<QNode>();
            this.oddQ = new AtomicReference<QNode>();
        }
        this.state = (parties == 0) ? (long)EMPTY :
            ((long)phase << PHASE_SHIFT) |
            ((long)parties << PARTIES_SHIFT) |
            ((long)parties);
    }

    /**
     * Adds a new unarrived party to this phaser.  If an ongoing
     * invocation of {@link #onAdvance} is in progress, this method
     * may await its completion before returning.  If this phaser has
     * a parent, and this phaser previously had no registered parties,
     * this child phaser is also registered with its parent. If
     * this phaser is terminated, the attempt to register has
     * no effect, and a negative value is returned.
     *
     * @return the arrival phase number to which this registration
     * applied.  If this value is negative, then this phaser has
     * terminated, in which case registration has no effect.
     * @throws IllegalStateException if attempting to register more
     * than the maximum supported number of parties
     */
    public int register() {
        return doRegister(1);
    }

    /**
     * Adds the given number of new unarrived parties to this phaser.
     * If an ongoing invocation of {@link #onAdvance} is in progress,
     * this method may await its completion before returning.  If this
     * phaser has a parent, and the given number of parties is greater
     * than zero, and this phaser previously had no registered
     * parties, this child phaser is also registered with its parent.
     * If this phaser is terminated, the attempt to register has no
     * effect, and a negative value is returned.
     *
     * @param parties the number of additional parties required to
     * advance to the next phase
     * @return the arrival phase number to which this registration
     * applied.  If this value is negative, then this phaser has
     * terminated, in which case registration has no effect.
     * @throws IllegalStateException if attempting to register more
     * than the maximum supported number of parties
     * @throws IllegalArgumentException if {@code parties < 0}
     */
    public int bulkRegister(int parties) {
        if (parties < 0)
            throw new IllegalArgumentException();
        if (parties == 0)
            return getPhase();
        return doRegister(parties);
    }

    /**
     * Arrives at this phaser, without waiting for others to arrive.
     *
     * <p>It is a usage error for an unregistered party to invoke this
     * method.  However, this error may result in an {@code
     * IllegalStateException} only upon some subsequent operation on
     * this phaser, if ever.
     *
     * @return the arrival phase number, or a negative value if terminated
     * @throws IllegalStateException if not terminated and the number
     * of unarrived parties would become negative
     */
    public int arrive() {
        return doArrive(ONE_ARRIVAL);
    }

    /**
     * Arrives at this phaser and deregisters from it without waiting
     * for others to arrive. Deregistration reduces the number of
     * parties required to advance in future phases.  If this phaser
     * has a parent, and deregistration causes this phaser to have
     * zero parties, this phaser is also deregistered from its parent.
     *
     * <p>It is a usage error for an unregistered party to invoke this
     * method.  However, this error may result in an {@code
     * IllegalStateException} only upon some subsequent operation on
     * this phaser, if ever.
     *
     * @return the arrival phase number, or a negative value if terminated
     * @throws IllegalStateException if not terminated and the number
     * of registered or unarrived parties would become negative
     */
    public int arriveAndDeregister() {
        return doArrive(ONE_DEREGISTER);
    }

    /**
     * Arrives at this phaser and awaits others. Equivalent in effect
     * to {@code awaitAdvance(arrive())}.  If you need to await with
     * interruption or timeout, you can arrange this with an analogous
     * construction using one of the other forms of the {@code
     * awaitAdvance} method.  If instead you need to deregister upon
     * arrival, use {@code awaitAdvance(arriveAndDeregister())}.
     *
     * <p>It is a usage error for an unregistered party to invoke this
     * method.  However, this error may result in an {@code
     * IllegalStateException} only upon some subsequent operation on
     * this phaser, if ever.
     *
     * @return the arrival phase number, or the (negative)
     * {@linkplain #getPhase() current phase} if terminated
     * @throws IllegalStateException if not terminated and the number
     * of unarrived parties would become negative
     */
    public int arriveAndAwaitAdvance() {
        // Specialization of doArrive+awaitAdvance eliminating some reads/paths
        final Phaser root = this.root;
        for (;;) {
            long s = (root == this) ? state : reconcileState();
            int phase = (int)(s >>> PHASE_SHIFT);
            if (phase < 0)
                return phase;
            int counts = (int)s;
            int unarrived = (counts == EMPTY) ? 0 : (counts & UNARRIVED_MASK);
            if (unarrived <= 0)
                throw new IllegalStateException(badArrive(s));
            if (UNSAFE.compareAndSwapLong(this, stateOffset, s,
                                          s -= ONE_ARRIVAL)) {
                if (unarrived > 1)
                    return root.internalAwaitAdvance(phase, null);
                if (root != this)
                    return parent.arriveAndAwaitAdvance();
                long n = s & PARTIES_MASK;  // base of next state
                int nextUnarrived = (int)n >>> PARTIES_SHIFT;
                if (onAdvance(phase, nextUnarrived))
                    n |= TERMINATION_BIT;
                else if (nextUnarrived == 0)
                    n |= EMPTY;
                else
                    n |= nextUnarrived;
                int nextPhase = (phase + 1) & MAX_PHASE;
                n |= (long)nextPhase << PHASE_SHIFT;
                if (!UNSAFE.compareAndSwapLong(this, stateOffset, s, n))
                    return (int)(state >>> PHASE_SHIFT); // terminated
                releaseWaiters(phase);
                return nextPhase;
            }
        }
    }

    /**
     * Awaits the phase of this phaser to advance from the given phase
     * value, returning immediately if the current phase is not equal
     * to the given phase value or this phaser is terminated.
     *
     * @param phase an arrival phase number, or negative value if
     * terminated; this argument is normally the value returned by a
     * previous call to {@code arrive} or {@code arriveAndDeregister}.
     * @return the next arrival phase number, or the argument if it is
     * negative, or the (negative) {@linkplain #getPhase() current phase}
     * if terminated
     */
    public int awaitAdvance(int phase) {
        final Phaser root = this.root;
        long s = (root == this) ? state : reconcileState();
        int p = (int)(s >>> PHASE_SHIFT);
        if (phase < 0)
            return phase;
        if (p == phase)
            return root.internalAwaitAdvance(phase, null);
        return p;
    }

    /**
     * Awaits the phase of this phaser to advance from the given phase
     * value, throwing {@code InterruptedException} if interrupted
     * while waiting, or returning immediately if the current phase is
     * not equal to the given phase value or this phaser is
     * terminated.
     *
     * @param phase an arrival phase number, or negative value if
     * terminated; this argument is normally the value returned by a
     * previous call to {@code arrive} or {@code arriveAndDeregister}.
     * @return the next arrival phase number, or the argument if it is
     * negative, or the (negative) {@linkplain #getPhase() current phase}
     * if terminated
     * @throws InterruptedException if thread interrupted while waiting
     */
    public int awaitAdvanceInterruptibly(int phase)
        throws InterruptedException {
        final Phaser root = this.root;
        long s = (root == this) ? state : reconcileState();
        int p = (int)(s >>> PHASE_SHIFT);
        if (phase < 0)
            return phase;
        if (p == phase) {
            QNode node = new QNode(this, phase, true, false, 0L);
            p = root.internalAwaitAdvance(phase, node);
            if (node.wasInterrupted)
                throw new InterruptedException();
        }
        return p;
    }

    /**
     * Awaits the phase of this phaser to advance from the given phase
     * value or the given timeout to elapse, throwing {@code
     * InterruptedException} if interrupted while waiting, or
     * returning immediately if the current phase is not equal to the
     * given phase value or this phaser is terminated.
     *
     * @param phase an arrival phase number, or negative value if
     * terminated; this argument is normally the value returned by a
     * previous call to {@code arrive} or {@code arriveAndDeregister}.
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return the next arrival phase number, or the argument if it is
     * negative, or the (negative) {@linkplain #getPhase() current phase}
     * if terminated
     * @throws InterruptedException if thread interrupted while waiting
     * @throws TimeoutException if timed out while waiting
     */
    public int awaitAdvanceInterruptibly(int phase,
                                         long timeout, TimeUnit unit)
        throws InterruptedException, TimeoutException {
        long nanos = unit.toNanos(timeout);
        final Phaser root = this.root;
        long s = (root == this) ? state : reconcileState();
        int p = (int)(s >>> PHASE_SHIFT);
        if (phase < 0)
            return phase;
        if (p == phase) {
            QNode node = new QNode(this, phase, true, true, nanos);
            p = root.internalAwaitAdvance(phase, node);
            if (node.wasInterrupted)
                throw new InterruptedException();
            else if (p == phase)
                throw new TimeoutException();
        }
        return p;
    }

    /**
     * Forces this phaser to enter termination state.  Counts of
     * registered parties are unaffected.  If this phaser is a member
     * of a tiered set of phasers, then all of the phasers in the set
     * are terminated.  If this phaser is already terminated, this
     * method has no effect.  This method may be useful for
     * coordinating recovery after one or more tasks encounter
     * unexpected exceptions.
     */
    public void forceTermination() {
        // Only need to change root state
        final Phaser root = this.root;
        long s;
        while ((s = root.state) >= 0) {
            if (UNSAFE.compareAndSwapLong(root, stateOffset,
                                          s, s | TERMINATION_BIT)) {
                // signal all threads
                releaseWaiters(0); // Waiters on evenQ
                releaseWaiters(1); // Waiters on oddQ
                return;
            }
        }
    }

    /**
     * Returns the current phase number. The maximum phase number is
     * {@code Integer.MAX_VALUE}, after which it restarts at
     * zero. Upon termination, the phase number is negative,
     * in which case the prevailing phase prior to termination
     * may be obtained via {@code getPhase() + Integer.MIN_VALUE}.
     *
     * @return the phase number, or a negative value if terminated
     */
    public final int getPhase() {
        return (int)(root.state >>> PHASE_SHIFT);
    }

    /**
     * Returns the number of parties registered at this phaser.
     *
     * @return the number of parties
     */
    public int getRegisteredParties() {
        return partiesOf(state);
    }

    /**
     * Returns the number of registered parties that have arrived at
     * the current phase of this phaser. If this phaser has terminated,
     * the returned value is meaningless and arbitrary.
     *
     * @return the number of arrived parties
     */
    public int getArrivedParties() {
        return arrivedOf(reconcileState());
    }

    /**
     * Returns the number of registered parties that have not yet
     * arrived at the current phase of this phaser. If this phaser has
     * terminated, the returned value is meaningless and arbitrary.
     *
     * @return the number of unarrived parties
     */
    public int getUnarrivedParties() {
        return unarrivedOf(reconcileState());
    }

    /**
     * Returns the parent of this phaser, or {@code null} if none.
     *
     * @return the parent of this phaser, or {@code null} if none
     */
    public Phaser getParent() {
        return parent;
    }

    /**
     * Returns the root ancestor of this phaser, which is the same as
     * this phaser if it has no parent.
     *
     * @return the root ancestor of this phaser
     */
    public Phaser getRoot() {
        return root;
    }

    /**
     * Returns {@code true} if this phaser has been terminated.
     *
     * @return {@code true} if this phaser has been terminated
     */
    public boolean isTerminated() {
        return root.state < 0L;
    }

    /**
     * Overridable method to perform an action upon impending phase
     * advance, and to control termination. This method is invoked
     * upon arrival of the party advancing this phaser (when all other
     * waiting parties are dormant).  If this method returns {@code
     * true}, this phaser will be set to a final termination state
     * upon advance, and subsequent calls to {@link #isTerminated}
     * will return true. Any (unchecked) Exception or Error thrown by
     * an invocation of this method is propagated to the party
     * attempting to advance this phaser, in which case no advance
     * occurs.
     *
     * <p>The arguments to this method provide the state of the phaser
     * prevailing for the current transition.  The effects of invoking
     * arrival, registration, and waiting methods on this phaser from
     * within {@code onAdvance} are unspecified and should not be
     * relied on.
     *
     * <p>If this phaser is a member of a tiered set of phasers, then
     * {@code onAdvance} is invoked only for its root phaser on each
     * advance.
     *
     * <p>To support the most common use cases, the default
     * implementation of this method returns {@code true} when the
     * number of registered parties has become zero as the result of a
     * party invoking {@code arriveAndDeregister}.  You can disable
     * this behavior, thus enabling continuation upon future
     * registrations, by overriding this method to always return
     * {@code false}:
     *
     * <pre> {@code
     * Phaser phaser = new Phaser() {
     *   protected boolean onAdvance(int phase, int parties) { return false; }
     * }}</pre>
     *
     * @param phase the current phase number on entry to this method,
     * before this phaser is advanced
     * @param registeredParties the current number of registered parties
     * @return {@code true} if this phaser should terminate
     */
    protected boolean onAdvance(int phase, int registeredParties) {
        return registeredParties == 0;
    }

    /**
     * Returns a string identifying this phaser, as well as its
     * state.  The state, in brackets, includes the String {@code
     * "phase = "} followed by the phase number, {@code "parties = "}
     * followed by the number of registered parties, and {@code
     * "arrived = "} followed by the number of arrived parties.
     *
     * @return a string identifying this phaser, as well as its state
     */
    public String toString() {
        return stateToString(reconcileState());
    }

    /**
     * Implementation of toString and string-based error messages
     */
    private String stateToString(long s) {
        return super.toString() +
            "[phase = " + phaseOf(s) +
            " parties = " + partiesOf(s) +
            " arrived = " + arrivedOf(s) + "]";
    }

    // Waiting mechanics

    /**
     * Removes and signals threads from queue for phase.
     */
    private void releaseWaiters(int phase) {
        QNode q;   // first element of queue
        Thread t;  // its thread
        AtomicReference<QNode> head = (phase & 1) == 0 ? evenQ : oddQ;
        while ((q = head.get()) != null &&
               q.phase != (int)(root.state >>> PHASE_SHIFT)) {
            if (head.compareAndSet(q, q.next) &&
                (t = q.thread) != null) {
                q.thread = null;
                LockSupport.unpark(t);
            }
        }
    }

    /**
     * Variant of releaseWaiters that additionally tries to remove any
     * nodes no longer waiting for advance due to timeout or
     * interrupt. Currently, nodes are removed only if they are at
     * head of queue, which suffices to reduce memory footprint in
     * most usages.
     *
     * @return current phase on exit
     */
    private int abortWait(int phase) {
        AtomicReference<QNode> head = (phase & 1) == 0 ? evenQ : oddQ;
        for (;;) {
            Thread t;
            QNode q = head.get();
            int p = (int)(root.state >>> PHASE_SHIFT);
            if (q == null || ((t = q.thread) != null && q.phase == p))
                return p;
            if (head.compareAndSet(q, q.next) && t != null) {
                q.thread = null;
                LockSupport.unpark(t);
            }
        }
    }

    /** The number of CPUs, for spin control */
    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    /**
     * The number of times to spin before blocking while waiting for
     * advance, per arrival while waiting. On multiprocessors, fully
     * blocking and waking up a large number of threads all at once is
     * usually a very slow process, so we use rechargeable spins to
     * avoid it when threads regularly arrive: When a thread in
     * internalAwaitAdvance notices another arrival before blocking,
     * and there appear to be enough CPUs available, it spins
     * SPINS_PER_ARRIVAL more times before blocking. The value trades
     * off good-citizenship vs big unnecessary slowdowns.
     */
    static final int SPINS_PER_ARRIVAL = (NCPU < 2) ? 1 : 1 << 8;

    /**
     * Possibly blocks and waits for phase to advance unless aborted.
     * Call only on root phaser.
     *
     * @param phase current phase
     * @param node if non-null, the wait node to track interrupt and timeout;
     * if null, denotes noninterruptible wait
     * @return current phase
     */
    private int internalAwaitAdvance(int phase, QNode node) {
        // assert root == this;
        releaseWaiters(phase-1);          // ensure old queue clean
        boolean queued = false;           // true when node is enqueued
        int lastUnarrived = 0;            // to increase spins upon change
        int spins = SPINS_PER_ARRIVAL;
        long s;
        int p;
        while ((p = (int)((s = state) >>> PHASE_SHIFT)) == phase) {
            if (node == null) {           // spinning in noninterruptible mode
                int unarrived = (int)s & UNARRIVED_MASK;
                if (unarrived != lastUnarrived &&
                    (lastUnarrived = unarrived) < NCPU)
                    spins += SPINS_PER_ARRIVAL;
                boolean interrupted = Thread.interrupted();
                if (interrupted || --spins < 0) { // need node to record intr
                    node = new QNode(this, phase, false, false, 0L);
                    node.wasInterrupted = interrupted;
                }
            }
            else if (node.isReleasable()) // done or aborted
                break;
            else if (!queued) {           // push onto queue
                AtomicReference<QNode> head = (phase & 1) == 0 ? evenQ : oddQ;
                QNode q = node.next = head.get();
                if ((q == null || q.phase == phase) &&
                    (int)(state >>> PHASE_SHIFT) == phase) // avoid stale enq
                    queued = head.compareAndSet(q, node);
            }
            else {
                try {
                    ForkJoinPool.managedBlock(node);
                } catch (InterruptedException ie) {
                    node.wasInterrupted = true;
                }
            }
        }

        if (node != null) {
            if (node.thread != null)
                node.thread = null;       // avoid need for unpark()
            if (node.wasInterrupted && !node.interruptible)
                Thread.currentThread().interrupt();
            if (p == phase && (p = (int)(state >>> PHASE_SHIFT)) == phase)
                return abortWait(phase); // possibly clean up on abort
        }
        releaseWaiters(phase);
        return p;
    }

    /**
     * Wait nodes for Treiber stack representing wait queue
     */
    static final class QNode implements ForkJoinPool.ManagedBlocker {
        final Phaser phaser;
        final int phase;
        final boolean interruptible;
        final boolean timed;
        boolean wasInterrupted;
        long nanos;
        long lastTime;
        volatile Thread thread; // nulled to cancel wait
        QNode next;

        QNode(Phaser phaser, int phase, boolean interruptible,
              boolean timed, long nanos) {
            this.phaser = phaser;
            this.phase = phase;
            this.interruptible = interruptible;
            this.nanos = nanos;
            this.timed = timed;
            this.lastTime = timed ? System.nanoTime() : 0L;
            thread = Thread.currentThread();
        }

        public boolean isReleasable() {
            if (thread == null)
                return true;
            if (phaser.getPhase() != phase) {
                thread = null;
                return true;
            }
            if (Thread.interrupted())
                wasInterrupted = true;
            if (wasInterrupted && interruptible) {
                thread = null;
                return true;
            }
            if (timed) {
                if (nanos > 0L) {
                    long now = System.nanoTime();
                    nanos -= now - lastTime;
                    lastTime = now;
                }
                if (nanos <= 0L) {
                    thread = null;
                    return true;
                }
            }
            return false;
        }

        public boolean block() {
            if (isReleasable())
                return true;
            else if (!timed)
                LockSupport.park(this);
            else if (nanos > 0)
                LockSupport.parkNanos(this, nanos);
            return isReleasable();
        }
    }

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE;
    private static final long stateOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = Phaser.class;
            stateOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/PriorityBlockingQueue.java b/luni/src/main/java/java/util/concurrent/PriorityBlockingQueue.java
//Synthetic comment -- index cffbe64..26c72eb 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -81,7 +81,7 @@
* java.util.PriorityQueue operations within a lock, as was done
* in a previous version of this class. To maintain
* interoperability, a plain PriorityQueue is still used during
     * serialization, which maintains compatibility at the expense of
* transiently doubling overhead.
*/

//Synthetic comment -- @@ -139,7 +139,7 @@
* to maintain compatibility with previous versions
* of this class. Non-null only during serialization/deserialization.
*/
    private PriorityQueue<E> q;

/**
* Creates a {@code PriorityBlockingQueue} with the default
//Synthetic comment -- @@ -278,14 +278,13 @@
/**
* Mechanics for poll().  Call only while holding lock.
*/
    private E dequeue() {
int n = size - 1;
if (n < 0)
            return null;
else {
Object[] array = queue;
            E result = (E) array[0];
E x = (E) array[n];
array[n] = null;
Comparator<? super E> cmp = comparator;
//Synthetic comment -- @@ -294,8 +293,8 @@
else
siftDownUsingComparator(0, x, array, n, cmp);
size = n;
            return result;
}
}

/**
//Synthetic comment -- @@ -312,6 +311,7 @@
* @param k the position to fill
* @param x the item to insert
* @param array the heap array
     * @param n heap size
*/
private static <T> void siftUpComparable(int k, T x, Object[] array) {
Comparable<? super T> key = (Comparable<? super T>) x;
//Synthetic comment -- @@ -476,7 +476,7 @@
* @param timeout This parameter is ignored as the method never blocks
* @param unit This parameter is ignored as the method never blocks
* @return {@code true} (as specified by
     *  {@link BlockingQueue#offer(Object,long,TimeUnit) BlockingQueue.offer})
* @throws ClassCastException if the specified element cannot be compared
*         with elements currently in the priority queue according to the
*         priority queue's ordering
//Synthetic comment -- @@ -489,13 +489,11 @@
public E poll() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
            return dequeue();
} finally {
lock.unlock();
}
}

public E take() throws InterruptedException {
//Synthetic comment -- @@ -503,7 +501,7 @@
lock.lockInterruptibly();
E result;
try {
            while ( (result = dequeue()) == null)
notEmpty.await();
} finally {
lock.unlock();
//Synthetic comment -- @@ -517,7 +515,7 @@
lock.lockInterruptibly();
E result;
try {
            while ( (result = dequeue()) == null && nanos > 0)
nanos = notEmpty.awaitNanos(nanos);
} finally {
lock.unlock();
//Synthetic comment -- @@ -528,13 +526,11 @@
public E peek() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
            return (size == 0) ? null : (E) queue[0];
} finally {
lock.unlock();
}
}

/**
//Synthetic comment -- @@ -618,32 +614,28 @@
* @return {@code true} if this queue changed as a result of the call
*/
public boolean remove(Object o) {
final ReentrantLock lock = this.lock;
lock.lock();
try {
int i = indexOf(o);
            if (i == -1)
                return false;
            removeAt(i);
            return true;
} finally {
lock.unlock();
}
}

/**
* Identity-based version for use in Itr.remove
*/
    void removeEQ(Object o) {
final ReentrantLock lock = this.lock;
lock.lock();
try {
Object[] array = queue;
            for (int i = 0, n = size; i < n; i++) {
if (o == array[i]) {
removeAt(i);
break;
//Synthetic comment -- @@ -663,15 +655,13 @@
* @return {@code true} if this queue contains the specified element
*/
public boolean contains(Object o) {
final ReentrantLock lock = this.lock;
lock.lock();
try {
            return indexOf(o) != -1;
} finally {
lock.unlock();
}
}

/**
//Synthetic comment -- @@ -697,7 +687,6 @@
}
}

public String toString() {
final ReentrantLock lock = this.lock;
lock.lock();
//Synthetic comment -- @@ -708,7 +697,7 @@
StringBuilder sb = new StringBuilder();
sb.append('[');
for (int i = 0; i < n; ++i) {
                Object e = queue[i];
sb.append(e == this ? "(this Collection)" : e);
if (i != n - 1)
sb.append(',').append(' ');
//Synthetic comment -- @@ -726,23 +715,7 @@
* @throws IllegalArgumentException      {@inheritDoc}
*/
public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
}

/**
//Synthetic comment -- @@ -761,11 +734,10 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            int n = Math.min(size, maxElements);
            for (int i = 0; i < n; i++) {
                c.add((E) queue[0]); // In this order, in case add() throws.
                dequeue();
}
return n;
} finally {
//Synthetic comment -- @@ -813,8 +785,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -867,7 +838,7 @@
*/
final class Itr implements Iterator<E> {
final Object[] array; // Array of all elements
        int cursor;           // index of next element to return
int lastRet;          // index of last element, or -1 if no such

Itr(Object[] array) {
//Synthetic comment -- @@ -904,8 +875,8 @@
throws java.io.IOException {
lock.lock();
try {
            // avoid zero capacity argument
            q = new PriorityQueue<E>(Math.max(size, 1), comparator);
q.addAll(this);
s.defaultWriteObject();
} finally {
//Synthetic comment -- @@ -933,21 +904,16 @@
}

// Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long allocationSpinLockOffset;
    static {
try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = PriorityBlockingQueue.class;
            allocationSpinLockOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("allocationSpinLock"));
        } catch (Exception e) {
            throw new Error(e);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RecursiveAction.java b/luni/src/main/java/java/util/concurrent/RecursiveAction.java
new file mode 100644
//Synthetic comment -- index 0000000..48066c9

//Synthetic comment -- @@ -0,0 +1,165 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

/**
 * A recursive resultless {@link ForkJoinTask}.  This class
 * establishes conventions to parameterize resultless actions as
 * {@code Void} {@code ForkJoinTask}s. Because {@code null} is the
 * only valid value of type {@code Void}, methods such as {@code join}
 * always return {@code null} upon completion.
 *
 * <p><b>Sample Usages.</b> Here is a simple but complete ForkJoin
 * sort that sorts a given {@code long[]} array:
 *
 *  <pre> {@code
 * static class SortTask extends RecursiveAction {
 *   final long[] array; final int lo, hi;
 *   SortTask(long[] array, int lo, int hi) {
 *     this.array = array; this.lo = lo; this.hi = hi;
 *   }
 *   SortTask(long[] array) { this(array, 0, array.length); }
 *   protected void compute() {
 *     if (hi - lo < THRESHOLD)
 *       sortSequentially(lo, hi);
 *     else {
 *       int mid = (lo + hi) >>> 1;
 *       invokeAll(new SortTask(array, lo, mid),
 *                 new SortTask(array, mid, hi));
 *       merge(lo, mid, hi);
 *     }
 *   }
 *   // implementation details follow:
 *   final static int THRESHOLD = 1000;
 *   void sortSequentially(int lo, int hi) {
 *     Arrays.sort(array, lo, hi);
 *   }
 *   void merge(int lo, int mid, int hi) {
 *     long[] buf = Arrays.copyOfRange(array, lo, mid);
 *     for (int i = 0, j = lo, k = mid; i < buf.length; j++)
 *       array[j] = (k == hi || buf[i] < array[k]) ?
 *         buf[i++] : array[k++];
 *   }
 * }}</pre>
 *
 * You could then sort {@code anArray} by creating {@code new
 * SortTask(anArray)} and invoking it in a ForkJoinPool.  As a more
 * concrete simple example, the following task increments each element
 * of an array:
 *  <pre> {@code
 * class IncrementTask extends RecursiveAction {
 *   final long[] array; final int lo, hi;
 *   IncrementTask(long[] array, int lo, int hi) {
 *     this.array = array; this.lo = lo; this.hi = hi;
 *   }
 *   protected void compute() {
 *     if (hi - lo < THRESHOLD) {
 *       for (int i = lo; i < hi; ++i)
 *         array[i]++;
 *     }
 *     else {
 *       int mid = (lo + hi) >>> 1;
 *       invokeAll(new IncrementTask(array, lo, mid),
 *                 new IncrementTask(array, mid, hi));
 *     }
 *   }
 * }}</pre>
 *
 * <p>The following example illustrates some refinements and idioms
 * that may lead to better performance: RecursiveActions need not be
 * fully recursive, so long as they maintain the basic
 * divide-and-conquer approach. Here is a class that sums the squares
 * of each element of a double array, by subdividing out only the
 * right-hand-sides of repeated divisions by two, and keeping track of
 * them with a chain of {@code next} references. It uses a dynamic
 * threshold based on method {@code getSurplusQueuedTaskCount}, but
 * counterbalances potential excess partitioning by directly
 * performing leaf actions on unstolen tasks rather than further
 * subdividing.
 *
 *  <pre> {@code
 * double sumOfSquares(ForkJoinPool pool, double[] array) {
 *   int n = array.length;
 *   Applyer a = new Applyer(array, 0, n, null);
 *   pool.invoke(a);
 *   return a.result;
 * }
 *
 * class Applyer extends RecursiveAction {
 *   final double[] array;
 *   final int lo, hi;
 *   double result;
 *   Applyer next; // keeps track of right-hand-side tasks
 *   Applyer(double[] array, int lo, int hi, Applyer next) {
 *     this.array = array; this.lo = lo; this.hi = hi;
 *     this.next = next;
 *   }
 *
 *   double atLeaf(int l, int h) {
 *     double sum = 0;
 *     for (int i = l; i < h; ++i) // perform leftmost base step
 *       sum += array[i] * array[i];
 *     return sum;
 *   }
 *
 *   protected void compute() {
 *     int l = lo;
 *     int h = hi;
 *     Applyer right = null;
 *     while (h - l > 1 && getSurplusQueuedTaskCount() <= 3) {
 *        int mid = (l + h) >>> 1;
 *        right = new Applyer(array, mid, h, right);
 *        right.fork();
 *        h = mid;
 *     }
 *     double sum = atLeaf(l, h);
 *     while (right != null) {
 *        if (right.tryUnfork()) // directly calculate if not stolen
 *          sum += right.atLeaf(right.lo, right.hi);
 *       else {
 *          right.join();
 *          sum += right.result;
 *        }
 *        right = right.next;
 *      }
 *     result = sum;
 *   }
 * }}</pre>
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public abstract class RecursiveAction extends ForkJoinTask<Void> {
    private static final long serialVersionUID = 5232453952276485070L;

    /**
     * The main computation performed by this task.
     */
    protected abstract void compute();

    /**
     * Always returns {@code null}.
     *
     * @return {@code null} always
     */
    public final Void getRawResult() { return null; }

    /**
     * Requires null completion value.
     */
    protected final void setRawResult(Void mustBeNull) { }

    /**
     * Implements execution conventions for RecursiveActions.
     */
    protected final boolean exec() {
        compute();
        return true;
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RecursiveTask.java b/luni/src/main/java/java/util/concurrent/RecursiveTask.java
new file mode 100644
//Synthetic comment -- index 0000000..5e17280

//Synthetic comment -- @@ -0,0 +1,69 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

/**
 * A recursive result-bearing {@link ForkJoinTask}.
 *
 * <p>For a classic example, here is a task computing Fibonacci numbers:
 *
 *  <pre> {@code
 * class Fibonacci extends RecursiveTask<Integer> {
 *   final int n;
 *   Fibonacci(int n) { this.n = n; }
 *   Integer compute() {
 *     if (n <= 1)
 *        return n;
 *     Fibonacci f1 = new Fibonacci(n - 1);
 *     f1.fork();
 *     Fibonacci f2 = new Fibonacci(n - 2);
 *     return f2.compute() + f1.join();
 *   }
 * }}</pre>
 *
 * However, besides being a dumb way to compute Fibonacci functions
 * (there is a simple fast linear algorithm that you'd use in
 * practice), this is likely to perform poorly because the smallest
 * subtasks are too small to be worthwhile splitting up. Instead, as
 * is the case for nearly all fork/join applications, you'd pick some
 * minimum granularity size (for example 10 here) for which you always
 * sequentially solve rather than subdividing.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public abstract class RecursiveTask<V> extends ForkJoinTask<V> {
    private static final long serialVersionUID = 5232453952276485270L;

    /**
     * The result of the computation.
     */
    V result;

    /**
     * The main computation performed by this task.
     */
    protected abstract V compute();

    public final V getRawResult() {
        return result;
    }

    protected final void setRawResult(V value) {
        result = value;
    }

    /**
     * Implements execution conventions for RecursiveTask.
     */
    protected final boolean exec() {
        result = compute();
        return true;
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RejectedExecutionException.java b/luni/src/main/java/java/util/concurrent/RejectedExecutionException.java
//Synthetic comment -- index 30b043d..f0005d1 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -49,8 +49,8 @@

/**
* Constructs a <tt>RejectedExecutionException</tt> with the
     * specified cause.  The detail message is set to {@code (cause ==
     * null ? null : cause.toString())} (which typically contains
* the class and detail message of <tt>cause</tt>).
*
* @param  cause the cause (which is saved for later retrieval by the








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RejectedExecutionHandler.java b/luni/src/main/java/java/util/concurrent/RejectedExecutionHandler.java
//Synthetic comment -- index 417a27c..8c000ea 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RunnableFuture.java b/luni/src/main/java/java/util/concurrent/RunnableFuture.java
//Synthetic comment -- index d74211d..2d6d52c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RunnableScheduledFuture.java b/luni/src/main/java/java/util/concurrent/RunnableScheduledFuture.java
//Synthetic comment -- index 0e8cc32..fbb995c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledExecutorService.java b/luni/src/main/java/java/util/concurrent/ScheduledExecutorService.java
//Synthetic comment -- index 6cb4e27..71e57ed 100644

//Synthetic comment -- @@ -1,12 +1,10 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;

/**
* An {@link ExecutorService} that can schedule commands to run after a given








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledFuture.java b/luni/src/main/java/java/util/concurrent/ScheduledFuture.java
//Synthetic comment -- index 239d681..3745cb0 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledThreadPoolExecutor.java b/luni/src/main/java/java/util/concurrent/ScheduledThreadPoolExecutor.java
//Synthetic comment -- index c2eaedf..e41f0c3 100644

//Synthetic comment -- @@ -1,16 +1,19 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

// BEGIN android-note
// omit class-level docs on setRemoveOnCancelPolicy()
// removed security manager docs
// END android-note

/**
//Synthetic comment -- @@ -138,7 +141,7 @@
* Sequence number to break scheduling ties, and in turn to
* guarantee FIFO order among tied entries.
*/
    private static final AtomicLong sequencer = new AtomicLong();

/**
* Returns current nanosecond time.
//Synthetic comment -- @@ -203,11 +206,11 @@
}

public long getDelay(TimeUnit unit) {
            return unit.convert(time - now(), NANOSECONDS);
}

public int compareTo(Delayed other) {
            if (other == this) // compare zero if same object
return 0;
if (other instanceof ScheduledFutureTask) {
ScheduledFutureTask<?> x = (ScheduledFutureTask<?>)other;
//Synthetic comment -- @@ -221,9 +224,9 @@
else
return 1;
}
            long diff = (getDelay(NANOSECONDS) -
                         other.getDelay(NANOSECONDS));
            return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
}

/**
//Synthetic comment -- @@ -302,7 +305,7 @@
remove(task))
task.cancel(false);
else
                ensurePrestart();
}
}

//Synthetic comment -- @@ -318,7 +321,7 @@
if (!canRunInCurrentRunState(true) && remove(task))
task.cancel(false);
else
                ensurePrestart();
}
}

//Synthetic comment -- @@ -396,7 +399,7 @@
* @throws IllegalArgumentException if {@code corePoolSize < 0}
*/
public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
new DelayedWorkQueue());
}

//Synthetic comment -- @@ -413,7 +416,7 @@
*/
public ScheduledThreadPoolExecutor(int corePoolSize,
ThreadFactory threadFactory) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
new DelayedWorkQueue(), threadFactory);
}

//Synthetic comment -- @@ -430,7 +433,7 @@
*/
public ScheduledThreadPoolExecutor(int corePoolSize,
RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
new DelayedWorkQueue(), handler);
}

//Synthetic comment -- @@ -451,7 +454,7 @@
public ScheduledThreadPoolExecutor(int corePoolSize,
ThreadFactory threadFactory,
RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
new DelayedWorkQueue(), threadFactory, handler);
}

//Synthetic comment -- @@ -480,7 +483,7 @@
private long overflowFree(long delay) {
Delayed head = (Delayed) super.getQueue().peek();
if (head != null) {
            long headDelay = head.getDelay(NANOSECONDS);
if (headDelay < 0 && (delay - headDelay < 0))
delay = Long.MAX_VALUE + headDelay;
}
//Synthetic comment -- @@ -588,7 +591,7 @@
* @throws NullPointerException {@inheritDoc}
*/
public void execute(Runnable command) {
        schedule(command, 0, NANOSECONDS);
}

// Override AbstractExecutorService methods
//Synthetic comment -- @@ -598,7 +601,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public Future<?> submit(Runnable task) {
        return schedule(task, 0, NANOSECONDS);
}

/**
//Synthetic comment -- @@ -606,8 +609,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public <T> Future<T> submit(Runnable task, T result) {
        return schedule(Executors.callable(task, result), 0, NANOSECONDS);
}

/**
//Synthetic comment -- @@ -615,7 +617,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public <T> Future<T> submit(Callable<T> task) {
        return schedule(task, 0, NANOSECONDS);
}

/**
//Synthetic comment -- @@ -690,8 +692,9 @@
* @param value if {@code true}, remove on cancellation, else don't
* @see #getRemoveOnCancelPolicy
* @since 1.7
     * @hide
*/
    public void setRemoveOnCancelPolicy(boolean value) {
removeOnCancel = value;
}

//Synthetic comment -- @@ -704,8 +707,9 @@
*         from the queue
* @see #setRemoveOnCancelPolicy
* @since 1.7
     * @hide
*/
    public boolean getRemoveOnCancelPolicy() {
return removeOnCancel;
}

//Synthetic comment -- @@ -724,8 +728,6 @@
* ContinueExistingPeriodicTasksAfterShutdownPolicy} has been set
* {@code true}, future executions of existing periodic tasks will
* be cancelled.
*/
public void shutdown() {
super.shutdown();
//Synthetic comment -- @@ -750,7 +752,6 @@
*         including those tasks submitted using {@code execute},
*         which are for scheduling purposes used as the basis of a
*         zero-delay {@code ScheduledFuture}.
*/
public List<Runnable> shutdownNow() {
return super.shutdownNow();
//Synthetic comment -- @@ -803,8 +804,8 @@
*/

private static final int INITIAL_CAPACITY = 16;
        private RunnableScheduledFuture<?>[] queue =
            new RunnableScheduledFuture<?>[INITIAL_CAPACITY];
private final ReentrantLock lock = new ReentrantLock();
private int size = 0;

//Synthetic comment -- @@ -835,7 +836,7 @@
/**
* Set f's heapIndex if it is a ScheduledFutureTask.
*/
        private void setIndex(RunnableScheduledFuture<?> f, int idx) {
if (f instanceof ScheduledFutureTask)
((ScheduledFutureTask)f).heapIndex = idx;
}
//Synthetic comment -- @@ -844,10 +845,10 @@
* Sift element added at bottom up to its heap-ordered spot.
* Call only when holding lock.
*/
        private void siftUp(int k, RunnableScheduledFuture<?> key) {
while (k > 0) {
int parent = (k - 1) >>> 1;
                RunnableScheduledFuture<?> e = queue[parent];
if (key.compareTo(e) >= 0)
break;
queue[k] = e;
//Synthetic comment -- @@ -862,11 +863,11 @@
* Sift element added at top down to its heap-ordered spot.
* Call only when holding lock.
*/
        private void siftDown(int k, RunnableScheduledFuture<?> key) {
int half = size >>> 1;
while (k < half) {
int child = (k << 1) + 1;
                RunnableScheduledFuture<?> c = queue[child];
int right = child + 1;
if (right < size && c.compareTo(queue[right]) > 0)
c = queue[child = right];
//Synthetic comment -- @@ -931,7 +932,7 @@

setIndex(queue[i], -1);
int s = --size;
                RunnableScheduledFuture<?> replacement = queue[s];
queue[s] = null;
if (s != i) {
siftDown(i, replacement);
//Synthetic comment -- @@ -962,7 +963,7 @@
return Integer.MAX_VALUE;
}

        public RunnableScheduledFuture<?> peek() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -975,7 +976,7 @@
public boolean offer(Runnable x) {
if (x == null)
throw new NullPointerException();
            RunnableScheduledFuture<?> e = (RunnableScheduledFuture<?>)x;
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -1017,9 +1018,9 @@
* holding lock.
* @param f the task to remove and return
*/
        private RunnableScheduledFuture<?> finishPoll(RunnableScheduledFuture<?> f) {
int s = --size;
            RunnableScheduledFuture<?> x = queue[s];
queue[s] = null;
if (s != 0)
siftDown(0, x);
//Synthetic comment -- @@ -1027,12 +1028,12 @@
return f;
}

        public RunnableScheduledFuture<?> poll() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture<?> first = queue[0];
                if (first == null || first.getDelay(NANOSECONDS) > 0)
return null;
else
return finishPoll(first);
//Synthetic comment -- @@ -1041,16 +1042,16 @@
}
}

        public RunnableScheduledFuture<?> take() throws InterruptedException {
final ReentrantLock lock = this.lock;
lock.lockInterruptibly();
try {
for (;;) {
                    RunnableScheduledFuture<?> first = queue[0];
if (first == null)
available.await();
else {
                        long delay = first.getDelay(NANOSECONDS);
if (delay <= 0)
return finishPoll(first);
else if (leader != null)
//Synthetic comment -- @@ -1074,21 +1075,21 @@
}
}

        public RunnableScheduledFuture<?> poll(long timeout, TimeUnit unit)
throws InterruptedException {
long nanos = unit.toNanos(timeout);
final ReentrantLock lock = this.lock;
lock.lockInterruptibly();
try {
for (;;) {
                    RunnableScheduledFuture<?> first = queue[0];
if (first == null) {
if (nanos <= 0)
return null;
else
nanos = available.awaitNanos(nanos);
} else {
                        long delay = first.getDelay(NANOSECONDS);
if (delay <= 0)
return finishPoll(first);
if (nanos <= 0)
//Synthetic comment -- @@ -1120,7 +1121,7 @@
lock.lock();
try {
for (int i = 0; i < size; i++) {
                    RunnableScheduledFuture<?> t = queue[i];
if (t != null) {
queue[i] = null;
setIndex(t, -1);
//Synthetic comment -- @@ -1133,14 +1134,14 @@
}

/**
         * Return first element only if it is expired.
* Used only by drainTo.  Call only when holding lock.
*/
        private RunnableScheduledFuture<?> peekExpired() {
            // assert lock.isHeldByCurrentThread();
            RunnableScheduledFuture<?> first = queue[0];
            return (first == null || first.getDelay(NANOSECONDS) > 0) ?
                null : first;
}

public int drainTo(Collection<? super Runnable> c) {
//Synthetic comment -- @@ -1151,10 +1152,11 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture<?> first;
int n = 0;
                while ((first = peekExpired()) != null) {
                    c.add(first);   // In this order, in case add() throws.
                    finishPoll(first);
++n;
}
return n;
//Synthetic comment -- @@ -1173,10 +1175,11 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture<?> first;
int n = 0;
                while (n < maxElements && (first = peekExpired()) != null) {
                    c.add(first);   // In this order, in case add() throws.
                    finishPoll(first);
++n;
}
return n;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Semaphore.java b/luni/src/main/java/java/util/concurrent/Semaphore.java
//Synthetic comment -- index 62dea1a..bf2524c 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.*;
import java.util.concurrent.locks.*;

/**
* A counting semaphore.  Conceptually, a semaphore maintains a set of
//Synthetic comment -- @@ -20,7 +19,7 @@
* <p>Semaphores are often used to restrict the number of threads than can
* access some (physical or logical) resource. For example, here is
* a class that uses a semaphore to control access to a pool of items:
 *  <pre> {@code
* class Pool {
*   private static final int MAX_AVAILABLE = 100;
*   private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
//Synthetic comment -- @@ -62,9 +61,7 @@
*     }
*     return false;
*   }
 * }}</pre>
*
* <p>Before obtaining an item each thread must acquire a permit from
* the semaphore, guaranteeing that an item is available for use. When








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/SynchronousQueue.java b/luni/src/main/java/java/util/concurrent/SynchronousQueue.java
//Synthetic comment -- index 51d40c0..b05ae0a 100644

//Synthetic comment -- @@ -2,13 +2,12 @@
* Written by Doug Lea, Bill Scherer, and Michael Scott with
* assistance from members of JCP JSR-166 Expert Group and released to
* the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -250,12 +249,22 @@
}

// Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long matchOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = SNode.class;
                    matchOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("match"));
                    nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
}

/** The head (top) of the stack */
//Synthetic comment -- @@ -393,7 +402,6 @@
*/
long lastTime = timed ? System.nanoTime() : 0;
Thread w = Thread.currentThread();
int spins = (shouldSpin(s) ?
(timed ? maxTimedSpins : maxUntimedSpins) : 0);
for (;;) {
//Synthetic comment -- @@ -469,10 +477,18 @@
}

// Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;
        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferStack.class;
                headOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("head"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/** Dual Queue */
//Synthetic comment -- @@ -529,11 +545,22 @@
}

// Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE;
            private static final long itemOffset;
            private static final long nextOffset;

            static {
                try {
                    UNSAFE = sun.misc.Unsafe.getUnsafe();
                    Class<?> k = QNode.class;
                    itemOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("item"));
                    nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
}

/** Head of queue */
//Synthetic comment -- @@ -762,15 +789,24 @@
}
}

        private static final sun.misc.Unsafe UNSAFE;
        private static final long headOffset;
        private static final long tailOffset;
        private static final long cleanMeOffset;
        static {
            try {
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = TransferQueue.class;
                headOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("head"));
                tailOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("tail"));
                cleanMeOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("cleanMe"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
}

/**
//Synthetic comment -- @@ -998,8 +1034,19 @@
*
* @return an empty iterator
*/
    @SuppressWarnings("unchecked")
public Iterator<E> iterator() {
        return (Iterator<E>) EmptyIterator.EMPTY_ITERATOR;
    }

    // Replicated from a previous version of Collections
    private static class EmptyIterator<E> implements Iterator<E> {
        static final EmptyIterator<Object> EMPTY_ITERATOR
            = new EmptyIterator<Object>();

        public boolean hasNext() { return false; }
        public E next() { throw new NoSuchElementException(); }
        public void remove() { throw new IllegalStateException(); }
}

/**
//Synthetic comment -- @@ -1007,7 +1054,7 @@
* @return a zero-length array
*/
public Object[] toArray() {
        return new Object[0];
}

/**
//Synthetic comment -- @@ -1036,8 +1083,7 @@
if (c == this)
throw new IllegalArgumentException();
int n = 0;
        for (E e; (e = poll()) != null;) {
c.add(e);
++n;
}
//Synthetic comment -- @@ -1056,8 +1102,7 @@
if (c == this)
throw new IllegalArgumentException();
int n = 0;
        for (E e; n < maxElements && (e = poll()) != null;) {
c.add(e);
++n;
}
//Synthetic comment -- @@ -1084,7 +1129,7 @@
private WaitQueue waitingConsumers;

/**
     * Saves the state to a stream (that is, serializes it).
*
* @param s the stream
*/








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadFactory.java b/luni/src/main/java/java/util/concurrent/ThreadFactory.java
//Synthetic comment -- index 2f0fb1a..d1a4eb6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -13,13 +13,12 @@
*
* <p>
* The simplest implementation of this interface is just:
 *  <pre> {@code
* class SimpleThreadFactory implements ThreadFactory {
*   public Thread newThread(Runnable r) {
*     return new Thread(r);
*   }
 * }}</pre>
*
* The {@link Executors#defaultThreadFactory} method provides a more
* useful simple implementation, that sets the created thread context








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadLocalRandom.java b/luni/src/main/java/java/util/concurrent/ThreadLocalRandom.java
new file mode 100644
//Synthetic comment -- index 0000000..a559321

//Synthetic comment -- @@ -0,0 +1,198 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

import java.util.Random;

/**
 * A random number generator isolated to the current thread.  Like the
 * global {@link java.util.Random} generator used by the {@link
 * java.lang.Math} class, a {@code ThreadLocalRandom} is initialized
 * with an internally generated seed that may not otherwise be
 * modified. When applicable, use of {@code ThreadLocalRandom} rather
 * than shared {@code Random} objects in concurrent programs will
 * typically encounter much less overhead and contention.  Use of
 * {@code ThreadLocalRandom} is particularly appropriate when multiple
 * tasks (for example, each a {@link ForkJoinTask}) use random numbers
 * in parallel in thread pools.
 *
 * <p>Usages of this class should typically be of the form:
 * {@code ThreadLocalRandom.current().nextX(...)} (where
 * {@code X} is {@code Int}, {@code Long}, etc).
 * When all usages are of this form, it is never possible to
 * accidently share a {@code ThreadLocalRandom} across multiple threads.
 *
 * <p>This class also provides additional commonly used bounded random
 * generation methods.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public class ThreadLocalRandom extends Random {
    // same constants as Random, but must be redeclared because private
    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;

    /**
     * The random seed. We can't use super.seed.
     */
    private long rnd;

    /**
     * Initialization flag to permit calls to setSeed to succeed only
     * while executing the Random constructor.  We can't allow others
     * since it would cause setting seed in one part of a program to
     * unintentionally impact other usages by the thread.
     */
    boolean initialized;

    // Padding to help avoid memory contention among seed updates in
    // different TLRs in the common case that they are located near
    // each other.
    private long pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;

    /**
     * The actual ThreadLocal
     */
    private static final ThreadLocal<ThreadLocalRandom> localRandom =
        new ThreadLocal<ThreadLocalRandom>() {
            protected ThreadLocalRandom initialValue() {
                return new ThreadLocalRandom();
            }
    };


    /**
     * Constructor called only by localRandom.initialValue.
     */
    ThreadLocalRandom() {
        super();
        initialized = true;
    }

    /**
     * Returns the current thread's {@code ThreadLocalRandom}.
     *
     * @return the current thread's {@code ThreadLocalRandom}
     */
    public static ThreadLocalRandom current() {
        return localRandom.get();
    }

    /**
     * Throws {@code UnsupportedOperationException}.  Setting seeds in
     * this generator is not supported.
     *
     * @throws UnsupportedOperationException always
     */
    public void setSeed(long seed) {
        if (initialized)
            throw new UnsupportedOperationException();
        rnd = (seed ^ multiplier) & mask;
    }

    protected int next(int bits) {
        rnd = (rnd * multiplier + addend) & mask;
        return (int) (rnd >>> (48-bits));
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the
     * given least value (inclusive) and bound (exclusive).
     *
     * @param least the least value returned
     * @param bound the upper bound (exclusive)
     * @throws IllegalArgumentException if least greater than or equal
     * to bound
     * @return the next value
     */
    public int nextInt(int least, int bound) {
        if (least >= bound)
            throw new IllegalArgumentException();
        return nextInt(bound - least) + least;
    }

    /**
     * Returns a pseudorandom, uniformly distributed value
     * between 0 (inclusive) and the specified value (exclusive).
     *
     * @param n the bound on the random number to be returned.  Must be
     *        positive.
     * @return the next value
     * @throws IllegalArgumentException if n is not positive
     */
    public long nextLong(long n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        // Divide n by two until small enough for nextInt. On each
        // iteration (at most 31 of them but usually much less),
        // randomly choose both whether to include high bit in result
        // (offset) and whether to continue with the lower vs upper
        // half (which makes a difference only if odd).
        long offset = 0;
        while (n >= Integer.MAX_VALUE) {
            int bits = next(2);
            long half = n >>> 1;
            long nextn = ((bits & 2) == 0) ? half : n - half;
            if ((bits & 1) == 0)
                offset += n - nextn;
            n = nextn;
        }
        return offset + nextInt((int) n);
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the
     * given least value (inclusive) and bound (exclusive).
     *
     * @param least the least value returned
     * @param bound the upper bound (exclusive)
     * @return the next value
     * @throws IllegalArgumentException if least greater than or equal
     * to bound
     */
    public long nextLong(long least, long bound) {
        if (least >= bound)
            throw new IllegalArgumentException();
        return nextLong(bound - least) + least;
    }

    /**
     * Returns a pseudorandom, uniformly distributed {@code double} value
     * between 0 (inclusive) and the specified value (exclusive).
     *
     * @param n the bound on the random number to be returned.  Must be
     *        positive.
     * @return the next value
     * @throws IllegalArgumentException if n is not positive
     */
    public double nextDouble(double n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        return nextDouble() * n;
    }

    /**
     * Returns a pseudorandom, uniformly distributed value between the
     * given least value (inclusive) and bound (exclusive).
     *
     * @param least the least value returned
     * @param bound the upper bound (exclusive)
     * @return the next value
     * @throws IllegalArgumentException if least greater than or equal
     * to bound
     */
    public double nextDouble(double least, double bound) {
        if (least >= bound)
            throw new IllegalArgumentException();
        return nextDouble() * (bound - least) + least;
    }

    private static final long serialVersionUID = -5851777807851030925L;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadPoolExecutor.java b/luni/src/main/java/java/util/concurrent/ThreadPoolExecutor.java
//Synthetic comment -- index 6622af8..331e225 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -9,6 +9,10 @@
import java.util.concurrent.atomic.*;
import java.util.*;

// BEGIN android-note
// removed security manager docs
// END android-note

/**
* An {@link ExecutorService} that executes each submitted task using
* one of possibly several pooled threads, normally configured
//Synthetic comment -- @@ -1311,8 +1315,6 @@
* <p>This method does not wait for previously submitted tasks to
* complete execution.  Use {@link #awaitTermination awaitTermination}
* to do that.
*/
public void shutdown() {
final ReentrantLock mainLock = this.mainLock;
//Synthetic comment -- @@ -1342,8 +1344,6 @@
* processing actively executing tasks.  This implementation
* cancels tasks via {@link Thread#interrupt}, so any task that
* fails to respond to interrupts may never terminate.
*/
public List<Runnable> shutdownNow() {
List<Runnable> tasks;
//Synthetic comment -- @@ -1512,6 +1512,18 @@
}

/**
     * Same as prestartCoreThread except arranges that at least one
     * thread is started even if corePoolSize is 0.
     */
    void ensurePrestart() {
        int wc = workerCountOf(ctl.get());
        if (wc < corePoolSize)
            addWorker(null, true);
        else if (wc == 0)
            addWorker(null, false);
    }

    /**
* Starts all core threads, causing them to idly wait for work. This
* overrides the default policy of starting core threads only when
* new tasks are executed.








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/TimeUnit.java b/luni/src/main/java/java/util/concurrent/TimeUnit.java
//Synthetic comment -- index b2e3060..50f6ce0 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,14 +23,14 @@
* the following code will timeout in 50 milliseconds if the {@link
* java.util.concurrent.locks.Lock lock} is not available:
*
 *  <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.MILLISECONDS)) ...}</pre>
 *
* while this code will timeout in 50 seconds:
 *  <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.SECONDS)) ...}</pre>
*
* Note however, that there is no guarantee that a particular timeout
* implementation will be able to notice the passage of time at the








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/TimeoutException.java b/luni/src/main/java/java/util/concurrent/TimeoutException.java
//Synthetic comment -- index 8b84f28..83934f0 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/TransferQueue.java b/luni/src/main/java/java/util/concurrent/TransferQueue.java
new file mode 100644
//Synthetic comment -- index 0000000..9cd5773

//Synthetic comment -- @@ -0,0 +1,133 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

// BEGIN android-note
// removed link to collections framework docs
// END android-note

/**
 * A {@link BlockingQueue} in which producers may wait for consumers
 * to receive elements.  A {@code TransferQueue} may be useful for
 * example in message passing applications in which producers
 * sometimes (using method {@link #transfer}) await receipt of
 * elements by consumers invoking {@code take} or {@code poll}, while
 * at other times enqueue elements (via method {@code put}) without
 * waiting for receipt.
 * {@linkplain #tryTransfer(Object) Non-blocking} and
 * {@linkplain #tryTransfer(Object,long,TimeUnit) time-out} versions of
 * {@code tryTransfer} are also available.
 * A {@code TransferQueue} may also be queried, via {@link
 * #hasWaitingConsumer}, whether there are any threads waiting for
 * items, which is a converse analogy to a {@code peek} operation.
 *
 * <p>Like other blocking queues, a {@code TransferQueue} may be
 * capacity bounded.  If so, an attempted transfer operation may
 * initially block waiting for available space, and/or subsequently
 * block waiting for reception by a consumer.  Note that in a queue
 * with zero capacity, such as {@link SynchronousQueue}, {@code put}
 * and {@code transfer} are effectively synonymous.
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 */
public interface TransferQueue<E> extends BlockingQueue<E> {
    /**
     * Transfers the element to a waiting consumer immediately, if possible.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * otherwise returning {@code false} without enqueuing the element.
     *
     * @param e the element to transfer
     * @return {@code true} if the element was transferred, else
     *         {@code false}
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean tryTransfer(E e);

    /**
     * Transfers the element to a consumer, waiting if necessary to do so.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else waits until the element is received by a consumer.
     *
     * @param e the element to transfer
     * @throws InterruptedException if interrupted while waiting,
     *         in which case the element is not left enqueued
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    void transfer(E e) throws InterruptedException;

    /**
     * Transfers the element to a consumer if it is possible to do so
     * before the timeout elapses.
     *
     * <p>More precisely, transfers the specified element immediately
     * if there exists a consumer already waiting to receive it (in
     * {@link #take} or timed {@link #poll(long,TimeUnit) poll}),
     * else waits until the element is received by a consumer,
     * returning {@code false} if the specified wait time elapses
     * before the element can be transferred.
     *
     * @param e the element to transfer
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before completion,
     *         in which case the element is not left enqueued
     * @throws InterruptedException if interrupted while waiting,
     *         in which case the element is not left enqueued
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this queue
     */
    boolean tryTransfer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * Returns {@code true} if there is at least one consumer waiting
     * to receive an element via {@link #take} or
     * timed {@link #poll(long,TimeUnit) poll}.
     * The return value represents a momentary state of affairs.
     *
     * @return {@code true} if there is at least one waiting consumer
     */
    boolean hasWaitingConsumer();

    /**
     * Returns an estimate of the number of consumers waiting to
     * receive elements via {@link #take} or timed
     * {@link #poll(long,TimeUnit) poll}.  The return value is an
     * approximation of a momentary state of affairs, that may be
     * inaccurate if consumers have completed or given up waiting.
     * The value may be useful for monitoring and heuristics, but
     * not for synchronization control.  Implementations of this
     * method are likely to be noticeably slower than those for
     * {@link #hasWaitingConsumer}.
     *
     * @return the number of consumers waiting to receive elements
     */
    int getWaitingConsumerCount();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicBoolean.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicBoolean.java
//Synthetic comment -- index c774d21..d531f25 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -21,14 +21,14 @@
public class AtomicBoolean implements java.io.Serializable {
private static final long serialVersionUID = 4654671469794556979L;
// setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;

static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicBoolean.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
}

private volatile int value;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicInteger.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicInteger.java
//Synthetic comment -- index 16dd568..e0a0018 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -24,14 +24,14 @@
private static final long serialVersionUID = 6214790243416807050L;

// setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;

static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
}

private volatile int value;
//Synthetic comment -- @@ -217,18 +217,33 @@
}


    /**
     * Returns the value of this {@code AtomicInteger} as an {@code int}.
     */
public int intValue() {
return get();
}

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code long}
     * after a widening primitive conversion.
     */
public long longValue() {
return (long)get();
}

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code float}
     * after a widening primitive conversion.
     */
public float floatValue() {
return (float)get();
}

    /**
     * Returns the value of this {@code AtomicInteger} as a {@code double}
     * after a widening primitive conversion.
     */
public double doubleValue() {
return (double)get();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerArray.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerArray.java
//Synthetic comment -- index 6dcdfd0..804a51e 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
import sun.misc.Unsafe;

/**
* An {@code int} array in which elements may be updated atomically.
//Synthetic comment -- @@ -19,7 +18,7 @@
public class AtomicIntegerArray implements java.io.Serializable {
private static final long serialVersionUID = 2862133569453604235L;

    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final int base = unsafe.arrayBaseOffset(int[].class);
private static final int shift;
private final int[] array;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerFieldUpdater.java
//Synthetic comment -- index e8a0d57..c7ed158 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -236,24 +236,21 @@
* Standard hotspot implementation using intrinsics
*/
private static class AtomicIntegerFieldUpdaterImpl<T> extends AtomicIntegerFieldUpdater<T> {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
private final long offset;
private final Class<T> tclass;
        private final Class<?> cclass;

AtomicIntegerFieldUpdaterImpl(Class<T> tclass, String fieldName) {
Field field = null;
            Class<?> caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
                caller = VMStack.getStackClass2(); // android-changed
modifiers = field.getModifiers();

// BEGIN android-removed
// sun.reflect.misc.ReflectUtil.ensureMemberAccess(
//     caller, tclass, null, modifiers);
// sun.reflect.misc.ReflectUtil.checkPackageAccess(tclass);
//Synthetic comment -- @@ -262,7 +259,7 @@
throw new RuntimeException(ex);
}

            Class<?> fieldt = field.getType();
if (fieldt != int.class)
throw new IllegalArgumentException("Must be integer type");









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLong.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLong.java
//Synthetic comment -- index 12d6cd1..5e799f7 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -24,7 +24,7 @@
private static final long serialVersionUID = 1927816293512124184L;

// setup to use Unsafe.compareAndSwapLong for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;

/**
//Synthetic comment -- @@ -42,10 +42,10 @@
private static native boolean VMSupportsCS8();

static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicLong.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
}

private volatile long value;
//Synthetic comment -- @@ -231,18 +231,33 @@
}


    /**
     * Returns the value of this {@code AtomicLong} as an {@code int}
     * after a narrowing primitive conversion.
     */
public int intValue() {
return (int)get();
}

    /**
     * Returns the value of this {@code AtomicLong} as a {@code long}.
     */
public long longValue() {
return get();
}

    /**
     * Returns the value of this {@code AtomicLong} as a {@code float}
     * after a widening primitive conversion.
     */
public float floatValue() {
return (float)get();
}

    /**
     * Returns the value of this {@code AtomicLong} as a {@code double}
     * after a widening primitive conversion.
     */
public double doubleValue() {
return (double)get();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLongArray.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLongArray.java
//Synthetic comment -- index 9e2d25f..22edb3f 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
import sun.misc.Unsafe;

/**
* A {@code long} array in which elements may be updated atomically.
//Synthetic comment -- @@ -18,7 +17,7 @@
public class AtomicLongArray implements java.io.Serializable {
private static final long serialVersionUID = -2308431214976778248L;

    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final int base = unsafe.arrayBaseOffset(long[].class);
private static final int shift;
private final long[] array;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLongFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLongFieldUpdater.java
//Synthetic comment -- index 21ef748..748ae69 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -235,14 +235,14 @@
}

private static class CASUpdater<T> extends AtomicLongFieldUpdater<T> {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
private final long offset;
private final Class<T> tclass;
        private final Class<?> cclass;

CASUpdater(Class<T> tclass, String fieldName) {
Field field = null;
            Class<?> caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
//Synthetic comment -- @@ -257,7 +257,7 @@
throw new RuntimeException(ex);
}

            Class<?> fieldt = field.getType();
if (fieldt != long.class)
throw new IllegalArgumentException("Must be long type");

//Synthetic comment -- @@ -320,14 +320,14 @@


private static class LockedUpdater<T> extends AtomicLongFieldUpdater<T> {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
private final long offset;
private final Class<T> tclass;
        private final Class<?> cclass;

LockedUpdater(Class<T> tclass, String fieldName) {
Field field = null;
            Class<?> caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
//Synthetic comment -- @@ -342,7 +342,7 @@
throw new RuntimeException(ex);
}

            Class<?> fieldt = field.getType();
if (fieldt != long.class)
throw new IllegalArgumentException("Must be long type");









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicMarkableReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicMarkableReference.java
//Synthetic comment -- index 63f46d6f..eaf700c 100644

//Synthetic comment -- @@ -1,13 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;

/**
* An {@code AtomicMarkableReference} maintains an object reference
* along with a mark bit, that can be updated atomically.
//Synthetic comment -- @@ -163,7 +161,7 @@

// Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
private static final long pairOffset =
objectFieldOffset(UNSAFE, "pair", AtomicMarkableReference.class);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicReference.java
//Synthetic comment -- index f041bbd..b21e9b6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -18,14 +18,14 @@
public class AtomicReference<V>  implements java.io.Serializable {
private static final long serialVersionUID = -1848883965231344442L;

    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long valueOffset;

static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicReference.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
}

private volatile V value;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceArray.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceArray.java
//Synthetic comment -- index dbc5886..c47728d 100644

//Synthetic comment -- @@ -1,12 +1,14 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;

import java.util.Arrays;
import java.lang.reflect.Array;
import sun.misc.Unsafe;

/**
* An array of object references in which elements may be updated
//Synthetic comment -- @@ -20,13 +22,23 @@
public class AtomicReferenceArray<E> implements java.io.Serializable {
private static final long serialVersionUID = -6209656149925076980L;

    private static final Unsafe unsafe;
    private static final int base;
private static final int shift;
    private static final long arrayFieldOffset;
    private final Object[] array; // must have exact type Object[]

static {
        int scale;
        try {
            unsafe = Unsafe.getUnsafe();
            arrayFieldOffset = unsafe.objectFieldOffset
                (AtomicReferenceArray.class.getDeclaredField("array"));
            base = unsafe.arrayBaseOffset(Object[].class);
            scale = unsafe.arrayIndexScale(Object[].class);
        } catch (Exception e) {
            throw new Error(e);
        }
if ((scale & (scale - 1)) != 0)
throw new Error("data type scale not a power of two");
shift = 31 - Integer.numberOfLeadingZeros(scale);
//Synthetic comment -- @@ -45,7 +57,7 @@

/**
* Creates a new AtomicReferenceArray of the given length, with all
     * elements initially null.
*
* @param length the length of the array
*/
//Synthetic comment -- @@ -62,7 +74,7 @@
*/
public AtomicReferenceArray(E[] array) {
// Visibility guaranteed by final field guarantees
        this.array = Arrays.copyOf(array, array.length, Object[].class);
}

/**
//Synthetic comment -- @@ -121,7 +133,7 @@
public final E getAndSet(int i, E newValue) {
long offset = checkedByteOffset(i);
while (true) {
            E current = getRaw(offset);
if (compareAndSetRaw(offset, current, newValue))
return current;
}
//Synthetic comment -- @@ -167,7 +179,7 @@
* @return the String representation of the current values of array
*/
public String toString() {
        int iMax = array.length - 1;
if (iMax == -1)
return "[]";

//Synthetic comment -- @@ -181,4 +193,20 @@
}
}

    /**
     * Reconstitutes the instance from a stream (that is, deserializes it).
     * @param s the stream
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException,
        java.io.InvalidObjectException {
        // Note: This must be changed if any additional fields are defined
        Object a = s.readFields().get("array", null);
        if (a == null || !a.getClass().isArray())
            throw new java.io.InvalidObjectException("Not array type");
        if (a.getClass() != Object[].class)
            a = Arrays.copyOf((Object[])a, Array.getLength(a), Object[].class);
        unsafe.putObjectVolatile(this, arrayFieldOffset, a);
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceFieldUpdater.java
//Synthetic comment -- index 8b3da0b..d23d766 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
import dalvik.system.VMStack; // android-added
import sun.misc.Unsafe;
import java.lang.reflect.*;

//Synthetic comment -- @@ -155,7 +155,7 @@
private final long offset;
private final Class<T> tclass;
private final Class<V> vclass;
        private final Class<?> cclass;

/*
* Internal type checks within all update methods contain
//Synthetic comment -- @@ -173,8 +173,8 @@
Class<V> vclass,
String fieldName) {
Field field = null;
            Class<?> fieldClass = null;
            Class<?> caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicStampedReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicStampedReference.java
//Synthetic comment -- index 2e826f2..a0cb492 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -162,7 +162,7 @@

// Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
private static final long pairOffset =
objectFieldOffset(UNSAFE, "pair", AtomicStampedReference.class);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/Fences.java b/luni/src/main/java/java/util/concurrent/atomic/Fences.java
new file mode 100644
//Synthetic comment -- index 0000000..7ecf45a

//Synthetic comment -- @@ -0,0 +1,540 @@
/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.atomic;

/**
 * A set of methods providing fine-grained control over happens-before
 * and synchronization order relations among reads and/or writes.  The
 * methods of this class are designed for use in uncommon situations
 * where declaring variables {@code volatile} or {@code final}, using
 * instances of atomic classes, using {@code synchronized} blocks or
 * methods, or using other synchronization facilities are not possible
 * or do not provide the desired control.
 *
 * <p><b>Memory Ordering.</b> There are three methods for controlling
 * ordering relations among memory accesses (i.e., reads and
 * writes). Method {@code orderWrites} is typically used to enforce
 * order between two writes, and {@code orderAccesses} between a write
 * and a read.  Method {@code orderReads} is used to enforce order
 * between two reads with respect to other {@code orderWrites} and/or
 * {@code orderAccesses} invocations.  The formally specified
 * properties of these methods described below provide
 * platform-independent guarantees that are honored by all levels of a
 * platform (compilers, systems, processors).  The use of these
 * methods may result in the suppression of otherwise valid compiler
 * transformations and optimizations that could visibly violate the
 * specified orderings, and may or may not entail the use of
 * processor-level "memory barrier" instructions.
 *
 * <p>Each ordering method accepts a {@code ref} argument, and
 * controls ordering among accesses with respect to this reference.
 * Invocations must be placed <em>between</em> accesses performed in
 * expression evaluations and assignment statements to control the
 * orderings of prior versus subsequent accesses appearing in program
 * order. These methods also return their arguments to simplify
 * correct usage in these contexts.
 *
 * <p>Usages of ordering methods almost always take one of the forms
 * illustrated in the examples below.  These idioms arrange some of
 * the ordering properties associated with {@code volatile} and
 * related language-based constructions, but without other
 * compile-time and runtime benefits that make language-based
 * constructions far better choices when they are applicable.  Usages
 * should be restricted to the control of strictly internal
 * implementation matters inside a class or package, and must either
 * avoid or document any consequent violations of ordering or safety
 * properties expected by users of a class employing them.
 *
 * <p><b>Reachability.</b> Method {@code reachabilityFence}
 * establishes an ordering for strong reachability (as defined in the
 * {@link java.lang.ref} package specification) with respect to
 * garbage collection.  Method {@code reachabilityFence} differs from
 * the others in that it controls relations that are otherwise only
 * implicit in a program -- the reachability conditions triggering
 * garbage collection.  As illustrated in the sample usages below,
 * this method is applicable only when reclamation may have visible
 * effects, which is possible for objects with finalizers (see Section
 * 12.6 of the Java Language Specification) that are implemented in
 * ways that rely on ordering control for correctness.
 *
 * <p><b>Sample Usages</b>
 *
 * <p><b>Safe publication.</b> With care, method {@code orderWrites}
 * may be used to obtain the memory safety effects of {@code final}
 * for a field that cannot be declared as {@code final}, because its
 * primary initialization cannot be performed in a constructor, in
 * turn because it is used in a framework requiring that all classes
 * have a no-argument constructor; as in:
 *
 *  <pre> {@code
 * class WidgetHolder {
 *   private Widget widget;
 *   public WidgetHolder() {}
 *   public static WidgetHolder newWidgetHolder(Params params) {
 *     WidgetHolder h = new WidgetHolder();
 *     h.widget = new Widget(params);
 *     return Fences.orderWrites(h);
 *   }
 * }}</pre>
 *
 * Here, the invocation of {@code orderWrites} ensures that the
 * effects of the widget assignment are ordered before those of any
 * (unknown) subsequent stores of {@code h} in other variables that
 * make {@code h} available for use by other objects.  Initialization
 * sequences using {@code orderWrites} require more care than those
 * involving {@code final} fields.  When {@code final} is not used,
 * compilers cannot help you to ensure that the field is set correctly
 * across all usages.  You must fully initialize objects
 * <em>before</em> the {@code orderWrites} invocation that makes
 * references to them safe to assign to accessible variables. Further,
 * initialization sequences must not internally "leak" the reference
 * by using it as an argument to a callback method or adding it to a
 * static data structure.  If less constrained usages were required,
 * it may be possible to cope using more extensive sets of fences, or
 * as a normally better choice, using synchronization (locking).
 * Conversely, if it were possible to do so, the best option would be
 * to rewrite class {@code WidgetHolder} to use {@code final}.
 *
 * <p>An alternative approach is to place similar mechanics in the
 * (sole) method that makes such objects available for use by others.
 * Here is a stripped-down example illustrating the essentials. In
 * practice, among other changes, you would use access methods instead
 * of a public field.
 *
 *  <pre> {@code
 * class AnotherWidgetHolder {
 *   public Widget widget;
 *   void publish(Widget w) {
 *     this.widget = Fences.orderWrites(w);
 *   }
 *   // ...
 * }}</pre>
 *
 * In this case, the {@code orderWrites} invocation occurs before the
 * store making the object available. Correctness again relies on
 * ensuring that there are no leaks prior to invoking this method, and
 * that it really is the <em>only</em> means of accessing the
 * published object.  This approach is not often applicable --
 * normally you would publish objects using a thread-safe collection
 * that itself guarantees the expected ordering relations. However, it
 * may come into play in the construction of such classes themselves.
 *
 * <p><b>Safely updating fields.</b> Outside of the initialization
 * idioms illustrated above, Fence methods ordering writes must be
 * paired with those ordering reads. To illustrate, suppose class
 * {@code c} contains an accessible variable {@code data} that should
 * have been declared as {@code volatile} but wasn't:
 *
 *  <pre> {@code
 * class C {
 *    Object data;  // need volatile access but not volatile
 *    // ...
 * }
 *
 * class App {
 *   Object getData(C c) {
 *      return Fences.orderReads(c).data;
 *   }
 *
 *   void setData(C c) {
 *      Object newValue = ...;
 *      c.data = Fences.orderWrites(newValue);
 *      Fences.orderAccesses(c);
 *   }
 *   // ...
 * }}</pre>
 *
 * Method {@code getData} provides an emulation of {@code volatile}
 * reads of (non-long/double) fields by ensuring that the read of
 * {@code c} obtained as an argument is ordered before subsequent
 * reads using this reference, and then performs the read of its
 * field. Method {@code setData} provides an emulation of volatile
 * writes, ensuring that all other relevant writes have completed,
 * then performing the assignment, and then ensuring that the write is
 * ordered before any other access.  These techniques may apply even
 * when fields are not directly accessible, in which case calls to
 * fence methods would surround calls to methods such as {@code
 * c.getData()}.  However, these techniques cannot be applied to
 * {@code long} or {@code double} fields because reads and writes of
 * fields of these types are not guaranteed to be
 * atomic. Additionally, correctness may require that all accesses of
 * such data use these kinds of wrapper methods, which you would need
 * to manually ensure.
 *
 * <p>More generally, Fence methods can be used in this way to achieve
 * the safety properties of {@code volatile}. However their use does
 * not necessarily guarantee the full sequential consistency
 * properties specified in the Java Language Specification chapter 17
 * for programs using {@code volatile}. In particular, emulation using
 * Fence methods is not guaranteed to maintain the property that
 * {@code volatile} operations performed by different threads are
 * observed in the same order by all observer threads.
 *
 * <p><b>Acquire/Release management of threadsafe objects</b>. It may
 * be possible to use weaker conventions for volatile-like variables
 * when they are used to keep track of objects that fully manage their
 * own thread-safety and synchronization.  Here, an acquiring read
 * operation remains the same as a volatile-read, but a releasing
 * write differs by virtue of not itself ensuring an ordering of its
 * write with subsequent reads, because the required effects are
 * already ensured by the referenced objects.
 * For example:
 *
 *  <pre> {@code
 * class Item {
 *    synchronized f(); // ALL methods are synchronized
 *    // ...
 * }
 *
 * class ItemHolder {
 *   private Item item;
 *   Item acquireItem() {
 *      return Fences.orderReads(item);
 *   }
 *
 *   void releaseItem(Item x) {
 *      item = Fences.orderWrites(x);
 *   }
 *
 *   // ...
 * }}</pre>
 *
 * Because this construction avoids use of {@code orderAccesses},
 * which is typically more costly than the other fence methods, it may
 * result in better performance than using {@code volatile} or its
 * emulation. However, as is the case with most applications of fence
 * methods, correctness relies on the usage context -- here, the
 * thread safety of {@code Item}, as well as the lack of need for full
 * volatile semantics inside this class itself. However, the second
 * concern means that it can be difficult to extend the {@code
 * ItemHolder} class in this example to be more useful.
 *
 * <p><b>Avoiding premature finalization.</b> Finalization may occur
 * whenever a Java Virtual Machine detects that no reference to an
 * object will ever be stored in the heap: A garbage collector may
 * reclaim an object even if the fields of that object are still in
 * use, so long as the object has otherwise become unreachable. This
 * may have surprising and undesirable effects in cases such as the
 * following example in which the bookkeeping associated with a class
 * is managed through array indices. Here, method {@code action}
 * uses a {@code reachabilityFence} to ensure that the Resource
 * object is not reclaimed before bookkeeping on an associated
 * ExternalResource has been performed; in particular here, to ensure
 * that the array slot holding the ExternalResource is not nulled out
 * in method {@link Object#finalize}, which may otherwise run
 * concurrently.
 *
 *  <pre> {@code
 * class Resource {
 *   private static ExternalResource[] externalResourceArray = ...
 *
 *   int myIndex;
 *   Resource(...) {
 *     myIndex = ...
 *     externalResourceArray[myIndex] = ...;
 *     ...
 *   }
 *   protected void finalize() {
 *     externalResourceArray[myIndex] = null;
 *     ...
 *   }
 *   public void action() {
 *     try {
 *       // ...
 *       int i = myIndex;
 *       Resource.update(externalResourceArray[i]);
 *     } finally {
 *       Fences.reachabilityFence(this);
 *     }
 *   }
 *   private static void update(ExternalResource ext) {
 *     ext.status = ...;
 *   }
 * }}</pre>
 *
 * Here, the call to {@code reachabilityFence} is nonintuitively
 * placed <em>after</em> the call to {@code update}, to ensure that
 * the array slot is not nulled out by {@link Object#finalize} before
 * the update, even if the call to {@code action} was the last use of
 * this object. This might be the case if for example a usage in a
 * user program had the form {@code new Resource().action();} which
 * retains no other reference to this Resource.  While probably
 * overkill here, {@code reachabilityFence} is placed in a {@code
 * finally} block to ensure that it is invoked across all paths in the
 * method.  In a method with more complex control paths, you might
 * need further precautions to ensure that {@code reachabilityFence}
 * is encountered along all of them.
 *
 * <p>It is sometimes possible to better encapsulate use of
 * {@code reachabilityFence}. Continuing the above example, if it
 * were OK for the call to method update to proceed even if the
 * finalizer had already executed (nulling out slot), then you could
 * localize use of {@code reachabilityFence}:
 *
 *  <pre> {@code
 * public void action2() {
 *   // ...
 *   Resource.update(getExternalResource());
 * }
 * private ExternalResource getExternalResource() {
 *   ExternalResource ext = externalResourceArray[myIndex];
 *   Fences.reachabilityFence(this);
 *   return ext;
 * }}</pre>
 *
 * <p>Method {@code reachabilityFence} is not required in
 * constructions that themselves ensure reachability. For example,
 * because objects that are locked cannot in general be reclaimed, it
 * would suffice if all accesses of the object, in all methods of
 * class Resource (including {@code finalize}) were enclosed in {@code
 * synchronized (this)} blocks. (Further, such blocks must not include
 * infinite loops, or themselves be unreachable, which fall into the
 * corner case exceptions to the "in general" disclaimer.) However,
 * method {@code reachabilityFence} remains a better option in cases
 * where this approach is not as efficient, desirable, or possible;
 * for example because it would encounter deadlock.
 *
 * <p><b>Formal Properties.</b>
 *
 * <p>Using the terminology of The Java Language Specification chapter
 * 17, the rules governing the semantics of the methods of this class
 * are as follows:
 *
 * <p> The following is still under construction.
 *
 * <dl>
 *
 *   <dt><b>[Definitions]</b>
 *   <dd>
 *   <ul>
 *
 *     <li>Define <em>sequenced(a, b)</em> to be true if <em>a</em>
 *     occurs before <em>b</em> in <em>program order</em>.
 *
 *     <li>Define <em>accesses(a, p)</em> to be true if
 *     <em>a</em> is a read or write of a field (or if an array, an
 *     element) of the object referenced by <em>p</em>.
 *
 *     <li>Define <em>deeplyAccesses(a, p)</em> to be true if either
 *     <em>accesses(a, p)</em> or <em>deeplyAccesses(a, q)</em> where
 *     <em>q</em> is the value seen by some read <em>r</em>
 *     such that <em>accesses(r, p)</em>.
 *
 *   </ul>
 *   <p>
 *   <dt><b>[Matching]</b>
 *   <dd> Given:
 *
 *   <ul>
 *
 *     <li><em>p</em>, a reference to an object
 *
 *     <li><em>wf</em>, an invocation of {@code orderWrites(p)} or
 *       {@code orderAccesses(p)}
 *
 *     <li><em>w</em>, a write of value <em>p</em>
 *
 *     <li> <em>rf</em>, an invocation of {@code orderReads(p)} or
 *     {@code orderAccesses(p)}
 *
 *     <li> <em>r</em>, a read returning value <em>p</em>
 *
 *   </ul>
 *   If:
 *   <ul>
 *     <li>sequenced(wf, w)
 *     <li>read <em>r</em> sees write <em>w</em>
 *     <li>sequenced(r, rf)
 *   </ul>
 *   Then:
 *   <ul>
 *
 *     <li> <em>wf happens-before rf</em>
 *
 *     <li> <em>wf</em> precedes <em>rf</em> in the
 *          <em>synchronization order</em>
 *
 *     <li> If (<em>r1</em>, <em>w1</em>) and (<em>r2</em>,
 *     <em>w2</em>) are two pairs of reads and writes, both
 *     respectively satisfying the above conditions for <em>p</em>,
 *     and sequenced(r1, r2) then it is not the case that <em>w2
 *     happens-before w1</em>.
 *
 *   </ul>
 *   <p>
 *   <dt><b>[Initial Reads]</b>
 *   <dd> Given:
 *
 *   <ul>
 *
 *     <li><em>p</em>, a reference to an object
 *
 *     <li> <em>a</em>, an access where deeplyAccesses(a, p)
 *
 *     <li><em>wf</em>, an invocation of {@code orderWrites(p)} or
 *       {@code orderAccesses(p)}
 *
 *     <li><em>w</em>, a write of value <em>p</em>
 *
 *     <li> <em>r</em>, a read returning value <em>p</em>
 *
 *     <li> <em>b</em>, an access where accesses(b, p)
 *
 *   </ul>
 *   If:
 *   <ul>
 *     <li>sequenced(a, wf);
 *     <li>sequenced(wf, w)
 *     <li>read <em>r</em> sees write <em>w</em>, and
 *         <em>r</em> is the first read by some thread
 *         <em>t</em> that sees value <em>p</em>
 *     <li>sequenced(r, b)
 *   </ul>
 *   Then:
 *   <ul>
 *     <li> the effects of <em>b</em> are constrained
 *          by the relation <em>a happens-before b</em>.
 *   </ul>
 *  <p>
 *  <dt><b>[orderAccesses]</b>
 *  <dd> Given:
 *
 *   <ul>
 *     <li><em>p</em>, a reference to an object
 *     <li><em>f</em>, an invocation of {@code orderAccesses(p)}
 *   </ul>
 *   If:
 *   <ul>
 *     <li>sequenced(f, w)
 *   </ul>
 *
 *    Then:
 *
 *   <ul>
 *
 *     <li> <em>f</em> is an element of the <em>synchronization order</em>.
 *
 *   </ul>
 *   <p>
 *   <dt><b>[Reachability]</b>
 *   <dd> Given:
 *
 *   <ul>
 *
 *     <li><em>p</em>, a reference to an object
 *
 *     <li><em>f</em>, an invocation of {@code reachabilityFence(p)}
 *
 *     <li><em>a</em>, an access where accesses(a, p)
 *
 *     <li><em>b</em>, an action (by a garbage collector) taking
 *     the form of an invocation of {@code
 *     p.finalize()} or of enqueing any {@link
 *     java.lang.ref.Reference} constructed with argument <em>p</em>
 *
 *   </ul>
 *
 *   If:
 *   <ul>
 *     <li>sequenced(a, f)
 *   </ul>
 *
 *    Then:
 *
 *   <ul>
 *
 *     <li> <em>a happens-before b</em>.
 *
 *   </ul>
 *
 * </dl>
 *
 * @since 1.7
 * @hide
 * @author Doug Lea
 */
public class Fences {
    private Fences() {} // Non-instantiable

    /*
     * The methods of this class are intended to be intrinisified by a
     * JVM. However, we provide correct but inefficient Java-level
     * code that simply reads and writes a static volatile
     * variable. Without JVM support, the consistency effects are
     * stronger than necessary, and the memory contention effects can
     * be a serious performance issue.
     */
    private static volatile int theVolatile;

    /**
     * Informally: Ensures that a read of the given reference prior to
     * the invocation of this method occurs before a subsequent use of
     * the given reference with the effect of reading or writing a
     * field (or if an array, element) of the referenced object.  The
     * use of this method is sensible only when paired with other
     * invocations of {@link #orderWrites} and/or {@link
     * #orderAccesses} for the given reference. For details, see the
     * class documentation for this class.
     *
     * @param ref the reference. If null, this method has no effect.
     * @return the given ref, to simplify usage
     */
    public static <T> T orderReads(T ref) {
        int ignore = theVolatile;
        return ref;
    }

    /**
     * Informally: Ensures that a use of the given reference with the
     * effect of reading or writing a field (or if an array, element)
     * of the referenced object, prior to the invocation of this
     * method occur before a subsequent write of the reference. For
     * details, see the class documentation for this class.
     *
     * @param ref the reference. If null, this method has no effect.
     * @return the given ref, to simplify usage
     */
    public static <T> T orderWrites(T ref) {
        theVolatile = 0;
        return ref;
    }

    /**
     * Informally: Ensures that accesses (reads or writes) using the
     * given reference prior to the invocation of this method occur
     * before subsequent accesses.  For details, see the class
     * documentation for this class.
     *
     * @param ref the reference. If null, this method has no effect.
     * @return the given ref, to simplify usage
     */
    public static <T> T orderAccesses(T ref) {
        theVolatile = 0;
        return ref;
    }

    /**
     * Ensures that the object referenced by the given reference
     * remains <em>strongly reachable</em> (as defined in the {@link
     * java.lang.ref} package documentation), regardless of any prior
     * actions of the program that might otherwise cause the object to
     * become unreachable; thus, the referenced object is not
     * reclaimable by garbage collection at least until after the
     * invocation of this method. Invocation of this method does not
     * itself initiate garbage collection or finalization.
     *
     * <p>See the class-level documentation for further explanation
     * and usage examples.
     *
     * @param ref the reference. If null, this method has no effect.
     */
    public static void reachabilityFence(Object ref) {
        if (ref != null) {
            synchronized (ref) {}
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/UnsafeAccess.java b/luni/src/main/java/java/util/concurrent/atomic/UnsafeAccess.java
deleted file mode 100644
//Synthetic comment -- index 96fff17..0000000

//Synthetic comment -- @@ -1,34 +0,0 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/package-info.java b/luni/src/main/java/java/util/concurrent/atomic/package-info.java
//Synthetic comment -- index 4a4375d..efbb413 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

/**
//Synthetic comment -- @@ -11,9 +11,7 @@
* array elements to those that also provide an atomic conditional update
* operation of the form:
*
 *  <pre> {@code boolean compareAndSet(expectedValue, updateValue);}</pre>
*
* <p>This method (which varies in argument types across different
* classes) atomically sets a variable to the {@code updateValue} if it
//Synthetic comment -- @@ -40,15 +38,30 @@
* {@code AtomicInteger} provide atomic increment methods.  One
* application is to generate sequence numbers, as in:
*
 *  <pre> {@code
* class Sequencer {
*   private final AtomicLong sequenceNumber
*     = new AtomicLong(0);
*   public long next() {
*     return sequenceNumber.getAndIncrement();
*   }
 * }}</pre>
 *
 * <p>It is straightforward to define new utility functions that, like
 * {@code getAndIncrement}, apply a function to a value atomically.
 * For example, given some transformation
 * <pre> {@code long transform(long input)}</pre>
 *
 * write your utility method as follows:
 *  <pre> {@code
 * boolean getAndTransform(AtomicLong var) {
 *   while (true) {
 *     long current = var.get();
 *     long next = transform(current);
 *     if (var.compareAndSet(current, next))
 *         return current;
 *   }
 * }}</pre>
*
* <p>The memory effects for accesses and updates of atomics generally
* follow the rules for volatiles, as stated in
//Synthetic comment -- @@ -161,9 +174,9 @@
* {@code byte} values, and cast appropriately.
*
* You can also hold floats using
 * {@link java.lang.Float#floatToRawIntBits} and
* {@link java.lang.Float#intBitsToFloat} conversions, and doubles using
 * {@link java.lang.Double#doubleToRawLongBits} and
* {@link java.lang.Double#longBitsToDouble} conversions.
*
* @since 1.5








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractOwnableSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractOwnableSynchronizer.java
//Synthetic comment -- index f3780e5..4bec0cf 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedLongSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedLongSynchronizer.java
//Synthetic comment -- index 5c8111c..7b36460 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import sun.misc.Unsafe;

/**
//Synthetic comment -- @@ -569,7 +568,7 @@
/**
* Convenience method to interrupt current thread.
*/
    static void selfInterrupt() {
Thread.currentThread().interrupt();
}

//Synthetic comment -- @@ -1231,7 +1230,7 @@
* due to the queue being empty.
*
* <p>This method is designed to be used by a fair synchronizer to
     * avoid <a href="AbstractQueuedSynchronizer.html#barging">barging</a>.
* Such a synchronizer's {@link #tryAcquire} method should return
* {@code false}, and its {@link #tryAcquireShared} method should
* return a negative value, if this method returns {@code true}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedSynchronizer.java
//Synthetic comment -- index 065f130..42029f0 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import sun.misc.Unsafe;

// BEGIN android-note
//Synthetic comment -- @@ -173,7 +172,7 @@
* It also supports conditions and exposes
* one of the instrumentation methods:
*
 *  <pre> {@code
* class Mutex implements Lock, java.io.Serializable {
*
*   // Our internal helper class
//Synthetic comment -- @@ -229,15 +228,14 @@
*       throws InterruptedException {
*     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
*   }
 * }}</pre>
*
* <p>Here is a latch class that is like a {@link CountDownLatch}
* except that it only requires a single <tt>signal</tt> to
* fire. Because a latch is non-exclusive, it uses the <tt>shared</tt>
* acquire and release methods.
*
 *  <pre> {@code
* class BooleanLatch {
*
*   private static class Sync extends AbstractQueuedSynchronizer {
//Synthetic comment -- @@ -259,8 +257,7 @@
*   public void await() throws InterruptedException {
*     sync.acquireSharedInterruptibly(1);
*   }
 * }}</pre>
*
* @since 1.5
* @author Doug Lea
//Synthetic comment -- @@ -800,7 +797,7 @@
/**
* Convenience method to interrupt current thread.
*/
    static void selfInterrupt() {
Thread.currentThread().interrupt();
}

//Synthetic comment -- @@ -2251,9 +2248,7 @@
* are at it, we do the same for other CASable fields (which could
* otherwise be done with atomic field updaters).
*/
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long stateOffset;
private static final long headOffset;
private static final long tailOffset;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/Condition.java b/luni/src/main/java/java/util/concurrent/locks/Condition.java
//Synthetic comment -- index 8504e1f..7050df9 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
//Synthetic comment -- @@ -331,10 +331,9 @@
/**
* Causes the current thread to wait until it is signalled or interrupted,
* or the specified waiting time elapses. This method is behaviorally
     * equivalent to:
     *  <pre> {@code awaitNanos(unit.toNanos(time)) > 0}</pre>
     *
* @param time the maximum time to wait
* @param unit the time unit of the {@code time} argument
* @return {@code false} if the waiting time detectably elapsed








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/Lock.java b/luni/src/main/java/java/util/concurrent/locks/Lock.java
//Synthetic comment -- index 4b9abd6..d5c6294 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
//Synthetic comment -- @@ -48,14 +48,14 @@
* methods and statements. In most cases, the following idiom
* should be used:
*
 *  <pre> {@code
 * Lock l = ...;
 * l.lock();
 * try {
 *   // access the resource protected by this lock
 * } finally {
 *   l.unlock();
 * }}</pre>
*
* When locking and unlocking occur in different scopes, care must be
* taken to ensure that all code that is executed while the lock is
//Synthetic comment -- @@ -210,18 +210,18 @@
* immediately with the value {@code false}.
*
* <p>A typical usage idiom for this method would be:
     *  <pre> {@code
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     *
* This usage ensures that the lock is unlocked if it was acquired, and
* doesn't try to unlock if the lock was not acquired.
*








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/LockSupport.java b/luni/src/main/java/java/util/concurrent/locks/LockSupport.java
//Synthetic comment -- index 0c0f07d..422e428 100644

//Synthetic comment -- @@ -1,11 +1,10 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
import sun.misc.Unsafe;


//Synthetic comment -- @@ -49,7 +48,10 @@
* higher-level synchronization utilities, and are not in themselves
* useful for most concurrency control applications.  The {@code park}
* method is designed for use only in constructions of the form:
 *
 *  <pre> {@code
 * while (!canProceed()) { ... LockSupport.park(this); }}</pre>
 *
* where neither {@code canProceed} nor any other actions prior to the
* call to {@code park} entail locking or blocking.  Because only one
* permit is associated with each thread, any intermediary uses of
//Synthetic comment -- @@ -57,7 +59,7 @@
*
* <p><b>Sample Usage.</b> Here is a sketch of a first-in-first-out
* non-reentrant lock class:
 *  <pre> {@code
* class FIFOMutex {
*   private final AtomicBoolean locked = new AtomicBoolean(false);
*   private final Queue<Thread> waiters
//Synthetic comment -- @@ -92,7 +94,7 @@
private LockSupport() {} // Cannot be instantiated.

// Hotspot implementation via intrinsics API
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long parkBlockerOffset;

static {
//Synthetic comment -- @@ -246,10 +248,14 @@
* snapshot -- the thread may have since unblocked or blocked on a
* different blocker object.
*
     * @param t the thread
* @return the blocker
     * @throws NullPointerException if argument is null
* @since 1.6
*/
public static Object getBlocker(Thread t) {
        if (t == null)
            throw new NullPointerException();
return unsafe.getObjectVolatile(t, parkBlockerOffset);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReadWriteLock.java b/luni/src/main/java/java/util/concurrent/locks/ReadWriteLock.java
//Synthetic comment -- index 484f68d..bb7b388 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReentrantLock.java b/luni/src/main/java/java/util/concurrent/locks/ReentrantLock.java
//Synthetic comment -- index cf787ca..07baf41 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;

/**
* A reentrant mutual exclusion {@link Lock} with the same basic
//Synthetic comment -- @@ -44,7 +43,7 @@
* follow a call to {@code lock} with a {@code try} block, most
* typically in a before/after construction such as:
*
 *  <pre> {@code
* class X {
*   private final ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -57,8 +56,7 @@
*       lock.unlock()
*     }
*   }
 * }}</pre>
*
* <p>In addition to implementing the {@link Lock} interface, this
* class defines methods {@code isLocked} and
//Synthetic comment -- @@ -354,8 +352,11 @@
* method. If you want a timed {@code tryLock} that does permit barging on
* a fair lock then combine the timed and un-timed forms together:
*
     *  <pre> {@code
     * if (lock.tryLock() ||
     *     lock.tryLock(timeout, unit)) {
     *   ...
     * }}</pre>
*
* <p>If the current thread
* already holds this lock then the hold count is incremented by one and
//Synthetic comment -- @@ -485,7 +486,7 @@
* not be entered with the lock already held then we can assert that
* fact:
*
     *  <pre> {@code
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -498,8 +499,7 @@
*       lock.unlock();
*     }
*   }
     * }}</pre>
*
* @return the number of holds on this lock by the current thread,
*         or zero if this lock is not held by the current thread
//Synthetic comment -- @@ -516,7 +516,7 @@
* testing. For example, a method that should only be called while
* a lock is held can assert that this is the case:
*
     *  <pre> {@code
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -525,13 +525,12 @@
*       assert lock.isHeldByCurrentThread();
*       // ... method body
*   }
     * }}</pre>
*
* <p>It can also be used to ensure that a reentrant lock is used
* in a non-reentrant manner, for example:
*
     *  <pre> {@code
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -545,8 +544,7 @@
*           lock.unlock();
*       }
*   }
     * }}</pre>
*
* @return {@code true} if current thread holds this lock and
*         {@code false} otherwise








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReentrantReadWriteLock.java b/luni/src/main/java/java/util/concurrent/locks/ReentrantReadWriteLock.java
//Synthetic comment -- index b1a1a60..244a4a7 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

package java.util.concurrent.locks;
import java.util.concurrent.*;
import java.util.*;

/**
//Synthetic comment -- @@ -33,7 +32,7 @@
* <dt><b><i>Fair mode</i></b>
* <dd> When constructed as fair, threads contend for entry using an
* approximately arrival-order policy. When the currently held lock
 * is released, either the longest-waiting single writer thread will
* be assigned the write lock, or if there is a group of reader threads
* waiting longer than all waiting writer threads, that group will be
* assigned the read lock.
//Synthetic comment -- @@ -51,8 +50,8 @@
* will block unless both the read lock and write lock are free (which
* implies there are no waiting threads).  (Note that the non-blocking
* {@link ReadLock#tryLock()} and {@link WriteLock#tryLock()} methods
 * do not honor this fair setting and will immediately acquire the lock
 * if it is possible, regardless of waiting threads.)
* <p>
* </dl>
*
//Synthetic comment -- @@ -114,21 +113,21 @@
*   void processCachedData() {
*     rwl.readLock().lock();
*     if (!cacheValid) {
 *       // Must release read lock before acquiring write lock
 *       rwl.readLock().unlock();
 *       rwl.writeLock().lock();
 *       try {
 *         // Recheck state because another thread might have
 *         // acquired write lock and changed state before we did.
 *         if (!cacheValid) {
 *           data = ...
 *           cacheValid = true;
 *         }
 *         // Downgrade by acquiring read lock before releasing write lock
 *         rwl.readLock().lock();
 *       } finally {
 *         rwl.writeLock().unlock(); // Unlock write, still hold read
 *       }
*     }
*
*     try {
//Synthetic comment -- @@ -147,33 +146,33 @@
* is a class using a TreeMap that is expected to be large and
* concurrently accessed.
*
 *  <pre> {@code
* class RWDictionary {
 *   private final Map<String, Data> m = new TreeMap<String, Data>();
 *   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
 *   private final Lock r = rwl.readLock();
 *   private final Lock w = rwl.writeLock();
*
 *   public Data get(String key) {
 *     r.lock();
 *     try { return m.get(key); }
 *     finally { r.unlock(); }
 *   }
 *   public String[] allKeys() {
 *     r.lock();
 *     try { return m.keySet().toArray(); }
 *     finally { r.unlock(); }
 *   }
 *   public Data put(String key, Data value) {
 *     w.lock();
 *     try { return m.put(key, value); }
 *     finally { w.unlock(); }
 *   }
 *   public void clear() {
 *     w.lock();
 *     try { m.clear(); }
 *     finally { w.unlock(); }
 *   }
* }}</pre>
*
* <h3>Implementation Notes</h3>
//Synthetic comment -- @@ -625,7 +624,9 @@
}

/**
         * Reconstitutes this lock instance from a stream (that is,
         * deserializes it).
         *
* @param s the stream
*/
private void readObject(java.io.ObjectInputStream s)
//Synthetic comment -- @@ -790,8 +791,11 @@
* permit barging on a fair lock then combine the timed and
* un-timed forms together:
*
         *  <pre> {@code
         * if (lock.tryLock() ||
         *     lock.tryLock(timeout, unit)) {
         *   ...
         * }}</pre>
*
* <p>If the write lock is held by another thread then the
* current thread becomes disabled for thread scheduling
//Synthetic comment -- @@ -1020,8 +1024,11 @@
* that does permit barging on a fair lock then combine the
* timed and un-timed forms together:
*
         *  <pre> {@code
         * if (lock.tryLock() ||
         *     lock.tryLock(timeout, unit)) {
         *   ...
         * }}</pre>
*
* <p>If the current thread already holds this lock then the
* hold count is incremented by one and the method returns








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/UnsafeAccess.java b/luni/src/main/java/java/util/concurrent/locks/UnsafeAccess.java
deleted file mode 100644
//Synthetic comment -- index 07f64e4..0000000

//Synthetic comment -- @@ -1,34 +0,0 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/package-info.java b/luni/src/main/java/java/util/concurrent/locks/package-info.java
//Synthetic comment -- index 860acdd..433f869 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/package-info.java b/luni/src/main/java/java/util/concurrent/package-info.java
//Synthetic comment -- index 8509a41..259b845 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
*/

// BEGIN android-note
// omit links to ForkJoinPool, ForkJoinTask, LinkedTransferQueue, PHaser, TransferQueue
// END android-note

/**
//Synthetic comment -- @@ -146,7 +146,7 @@
* A {@code CopyOnWriteArrayList} is preferable to a synchronized
* {@code ArrayList} when the expected number of reads and traversals
* greatly outnumber the number of updates to a list.
 *
* <p>The "Concurrent" prefix used with some classes in this package
* is a shorthand indicating several differences from similar
* "synchronized" classes.  For example {@code java.util.Hashtable} and
//Synthetic comment -- @@ -243,7 +243,8 @@
*   in each thread <i>happen-before</i> those subsequent to the
*   corresponding {@code exchange()} in another thread.
*
 *   <li>Actions prior to calling {@code CyclicBarrier.await} and
 *   {@code Phaser.awaitAdvance} (as well as its variants)
*   <i>happen-before</i> actions performed by the barrier action, and
*   actions performed by the barrier action <i>happen-before</i> actions
*   subsequent to a successful return from the corresponding {@code await}







