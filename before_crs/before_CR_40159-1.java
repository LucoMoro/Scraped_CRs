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
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayDeque.java b/luni/src/main/java/java/util/ArrayDeque.java
//Synthetic comment -- index fafcdb4..c4d67b8 100644

//Synthetic comment -- @@ -1,6 +1,6 @@
/*
* Written by Josh Bloch of Google Inc. and released to the public domain,
 * as explained at http://creativecommons.org/licenses/publicdomain.
*/

package java.util;
//Synthetic comment -- @@ -9,8 +9,6 @@
// removed link to collections framework docs
// END android-note

import java.io.*;

/**
* Resizable-array implementation of the {@link Deque} interface.  Array
* deques have no capacity restrictions; they grow as necessary to support
//Synthetic comment -- @@ -53,7 +51,7 @@
* @param <E> the type of elements held in this collection
*/
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable
{
/**
* The array in which the elements of the deque are stored.
//Synthetic comment -- @@ -65,7 +63,7 @@
* other.  We also guarantee that all array cells not holding
* deque elements are always null.
*/
    private transient E[] elements;

/**
* The index of the element at the head of the deque (which is the
//Synthetic comment -- @@ -109,7 +107,7 @@
if (initialCapacity < 0)   // Too many elements, must back off
initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
}
        elements = (E[]) new Object[initialCapacity];
}

/**
//Synthetic comment -- @@ -127,7 +125,7 @@
Object[] a = new Object[newCapacity];
System.arraycopy(elements, p, a, 0, r);
System.arraycopy(elements, 0, a, r, p);
        elements = (E[])a;
head = 0;
tail = n;
}
//Synthetic comment -- @@ -155,7 +153,7 @@
* sufficient to hold 16 elements.
*/
public ArrayDeque() {
        elements = (E[]) new Object[16];
}

/**
//Synthetic comment -- @@ -263,7 +261,8 @@

public E pollFirst() {
int h = head;
        E result = elements[h]; // Element is null if deque empty
if (result == null)
return null;
elements[h] = null;     // Must null out slot
//Synthetic comment -- @@ -273,7 +272,7 @@

public E pollLast() {
int t = (tail - 1) & (elements.length - 1);
        E result = elements[t];
if (result == null)
return null;
elements[t] = null;
//Synthetic comment -- @@ -285,28 +284,33 @@
* @throws NoSuchElementException {@inheritDoc}
*/
public E getFirst() {
        E x = elements[head];
        if (x == null)
throw new NoSuchElementException();
        return x;
}

/**
* @throws NoSuchElementException {@inheritDoc}
*/
public E getLast() {
        E x = elements[(tail - 1) & (elements.length - 1)];
        if (x == null)
throw new NoSuchElementException();
        return x;
}

public E peekFirst() {
        return elements[head]; // elements[head] is null if deque empty
}

public E peekLast() {
        return elements[(tail - 1) & (elements.length - 1)];
}

/**
//Synthetic comment -- @@ -326,7 +330,7 @@
return false;
int mask = elements.length - 1;
int i = head;
        E x;
while ( (x = elements[i]) != null) {
if (o.equals(x)) {
delete(i);
//Synthetic comment -- @@ -354,7 +358,7 @@
return false;
int mask = elements.length - 1;
int i = (tail - 1) & mask;
        E x;
while ( (x = elements[i]) != null) {
if (o.equals(x)) {
delete(i);
//Synthetic comment -- @@ -499,7 +503,7 @@
*/
private boolean delete(int i) {
checkInvariants();
        final E[] elements = this.elements;
final int mask = elements.length - 1;
final int h = head;
final int t = tail;
//Synthetic comment -- @@ -597,7 +601,7 @@
public E next() {
if (cursor == fence)
throw new NoSuchElementException();
            E result = elements[cursor];
// This check doesn't catch all possible comodifications,
// but does catch the ones that corrupt traversal
if (tail != fence || result == null)
//Synthetic comment -- @@ -636,7 +640,7 @@
if (cursor == fence)
throw new NoSuchElementException();
cursor = (cursor - 1) & (elements.length - 1);
            E result = elements[cursor];
if (head != fence || result == null)
throw new ConcurrentModificationException();
lastRet = cursor;
//Synthetic comment -- @@ -667,7 +671,7 @@
return false;
int mask = elements.length - 1;
int i = head;
        E x;
while ( (x = elements[i]) != null) {
if (o.equals(x))
return true;
//Synthetic comment -- @@ -750,8 +754,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of <tt>String</tt>:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -765,6 +768,7 @@
*         this deque
* @throws NullPointerException if the specified array is null
*/
public <T> T[] toArray(T[] a) {
int size = size();
if (a.length < size)
//Synthetic comment -- @@ -785,6 +789,7 @@
*/
public ArrayDeque<E> clone() {
try {
ArrayDeque<E> result = (ArrayDeque<E>) super.clone();
result.elements = Arrays.copyOf(elements, elements.length);
return result;
//Synthetic comment -- @@ -806,7 +811,8 @@
* followed by all of its elements (each an object reference) in
* first-to-last order.
*/
    private void writeObject(ObjectOutputStream s) throws IOException {
s.defaultWriteObject();

// Write out size
//Synthetic comment -- @@ -821,8 +827,8 @@
/**
* Deserialize this deque.
*/
    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
s.defaultReadObject();

// Read in size and allocate array
//Synthetic comment -- @@ -833,6 +839,6 @@

// Read in all elements in the proper order.
for (int i = 0; i < size; i++)
            elements[i] = (E)s.readObject();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Deque.java b/luni/src/main/java/java/util/Deque.java
//Synthetic comment -- index cb6bd90..f74a6b4 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/licenses/publicdomain
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// changed {@link #offer(Object)} to {@link #offer} to satisfy DroidDoc
// END android-note

/**
//Synthetic comment -- @@ -356,7 +355,7 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer offer}.
*
* <p>This method is equivalent to {@link #addLast}.
*








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/NavigableMap.java b/luni/src/main/java/java/util/NavigableMap.java
//Synthetic comment -- index 29961c8..beeb651 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of JCP
* JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// changed {@link #subMap(Object)} to {@link #subMap} to satisfy DroidDoc
// END android-note

/**
//Synthetic comment -- @@ -48,9 +47,9 @@
* method {@code put}.
*
* <p>Methods
 * {@link #subMap subMap(K, K)},
 * {@link #headMap headMap(K)}, and
 * {@link #tailMap tailMap(K)}
* are specified to return {@code SortedMap} to allow existing
* implementations of {@code SortedMap} to be compatibly retrofitted to
* implement {@code NavigableMap}, but extensions and implementations








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/NavigableSet.java b/luni/src/main/java/java/util/NavigableSet.java
//Synthetic comment -- index cff0800..f410313 100644

//Synthetic comment -- @@ -1,14 +1,13 @@
/*
* Written by Doug Lea and Josh Bloch with assistance from members of JCP
* JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util;

// BEGIN android-note
// removed link to collections framework docs
// changed {@link #subSet(Object)} to {@link #subSet} to satisfy DroidDoc
// END android-note

/**
//Synthetic comment -- @@ -41,9 +40,9 @@
* Comparable} elements intrinsically do not permit {@code null}.)
*
* <p>Methods
 * {@link #subSet subSet(E, E)},
 * {@link #headSet headSet(E)}, and
 * {@link #tailSet tailSet(E)}
* are specified to return {@code SortedSet} to allow existing
* implementations of {@code SortedSet} to be compatibly retrofitted to
* implement {@code NavigableSet}, but extensions and implementations








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Queue.java b/luni/src/main/java/java/util/Queue.java
//Synthetic comment -- index 5aef944..8b465e6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/AbstractExecutorService.java b/luni/src/main/java/java/util/concurrent/AbstractExecutorService.java
//Synthetic comment -- index 36fcecc..a7f7745 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ArrayBlockingQueue.java b/luni/src/main/java/java/util/concurrent/ArrayBlockingQueue.java
//Synthetic comment -- index a622832..e30ab67 100644

//Synthetic comment -- @@ -1,12 +1,17 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -73,11 +78,20 @@

/** Main lock guarding all access */
final ReentrantLock lock;
/** Condition for waiting takes */
private final Condition notEmpty;
/** Condition for waiting puts */
private final Condition notFull;

// Internal helper methods

/**
//Synthetic comment -- @@ -94,16 +108,12 @@
return ((i == 0) ? items.length : i) - 1;
}

    @SuppressWarnings("unchecked")
    static <E> E cast(Object item) {
        return (E) item;
    }

/**
* Returns item at index i.
*/
final E itemAt(int i) {
        return this.<E>cast(items[i]);
}

/**
//Synthetic comment -- @@ -120,10 +130,12 @@
* Inserts element at current put position, advances, and signals.
* Call only when holding lock.
*/
    private void insert(E x) {
items[putIndex] = x;
putIndex = inc(putIndex);
        ++count;
notEmpty.signal();
}

//Synthetic comment -- @@ -131,42 +143,57 @@
* Extracts element at current take position, advances, and signals.
* Call only when holding lock.
*/
    private E extract() {
final Object[] items = this.items;
        E x = this.<E>cast(items[takeIndex]);
items[takeIndex] = null;
takeIndex = inc(takeIndex);
        --count;
notFull.signal();
return x;
}

/**
     * Deletes item at position i.
     * Utility for remove and iterator.remove.
* Call only when holding lock.
*/
    void removeAt(int i) {
final Object[] items = this.items;
        // if removing front item, just advance
        if (i == takeIndex) {
items[takeIndex] = null;
takeIndex = inc(takeIndex);
} else {
// slide over all others up through putIndex.
            for (;;) {
                int nexti = inc(i);
                if (nexti != putIndex) {
                    items[i] = items[nexti];
                    i = nexti;
} else {
items[i] = null;
                    putIndex = i;
break;
}
}
}
        --count;
notFull.signal();
}

//Synthetic comment -- @@ -271,7 +298,7 @@
if (count == items.length)
return false;
else {
                insert(e);
return true;
}
} finally {
//Synthetic comment -- @@ -293,7 +320,7 @@
try {
while (count == items.length)
notFull.await();
            insert(e);
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -320,7 +347,7 @@
return false;
nanos = notFull.awaitNanos(nanos);
}
            insert(e);
return true;
} finally {
lock.unlock();
//Synthetic comment -- @@ -331,7 +358,7 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            return (count == 0) ? null : extract();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -343,7 +370,7 @@
try {
while (count == 0)
notEmpty.await();
            return extract();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -359,7 +386,7 @@
return null;
nanos = notEmpty.awaitNanos(nanos);
}
            return extract();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -438,11 +465,15 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            for (int i = takeIndex, k = count; k > 0; i = inc(i), k--) {
                if (o.equals(items[i])) {
                    removeAt(i);
                    return true;
                }
}
return false;
} finally {
//Synthetic comment -- @@ -464,9 +495,14 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            for (int i = takeIndex, k = count; k > 0; i = inc(i), k--)
                if (o.equals(items[i]))
                    return true;
return false;
} finally {
lock.unlock();
//Synthetic comment -- @@ -522,8 +558,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -589,12 +624,20 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            for (int i = takeIndex, k = count; k > 0; i = inc(i), k--)
                items[i] = null;
            count = 0;
            putIndex = 0;
            takeIndex = 0;
            notFull.signalAll();
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -607,32 +650,7 @@
* @throws IllegalArgumentException      {@inheritDoc}
*/
public int drainTo(Collection<? super E> c) {
        checkNotNull(c);
        if (c == this)
            throw new IllegalArgumentException();
        final Object[] items = this.items;
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int i = takeIndex;
            int n = 0;
            int max = count;
            while (n < max) {
                c.add(this.<E>cast(items[i]));
                items[i] = null;
                i = inc(i);
                ++n;
            }
            if (n > 0) {
                count = 0;
                putIndex = 0;
                takeIndex = 0;
                notFull.signalAll();
            }
            return n;
        } finally {
            lock.unlock();
        }
}

/**
//Synthetic comment -- @@ -651,21 +669,33 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            int i = takeIndex;
            int n = 0;
            int max = (maxElements < count) ? maxElements : count;
            while (n < max) {
                c.add(this.<E>cast(items[i]));
                items[i] = null;
                i = inc(i);
                ++n;
}
            if (n > 0) {
                count -= n;
                takeIndex = i;
                notFull.signalAll();
            }
            return n;
} finally {
lock.unlock();
}
//Synthetic comment -- @@ -675,12 +705,12 @@
* Returns an iterator over the elements in this queue in proper sequence.
* The elements will be returned in order from first (head) to last (tail).
*
     * <p>The returned {@code Iterator} is a "weakly consistent" iterator that
* will never throw {@link java.util.ConcurrentModificationException
     * ConcurrentModificationException},
     * and guarantees to traverse elements as they existed upon
     * construction of the iterator, and may (but is not guaranteed to)
     * reflect any modifications subsequent to construction.
*
* @return an iterator over the elements in this queue in proper sequence
*/
//Synthetic comment -- @@ -689,88 +719,627 @@
}

/**
     * Iterator for ArrayBlockingQueue. To maintain weak consistency
     * with respect to puts and takes, we (1) read ahead one slot, so
     * as to not report hasNext true but then not have an element to
     * return -- however we later recheck this slot to use the most
     * current value; (2) ensure that each array slot is traversed at
     * most once (by tracking "remaining" elements); (3) skip over
     * null slots, which can occur if takes race ahead of iterators.
     * However, for circular array-based queues, we cannot rely on any
     * well established definition of what it means to be weakly
     * consistent with respect to interior removes since these may
     * require slot overwrites in the process of sliding elements to
     * cover gaps. So we settle for resiliency, operating on
     * established apparent nexts, which may miss some elements that
     * have moved between calls to next.
*/
    private class Itr implements Iterator<E> {
        private int remaining; // Number of elements yet to be returned
        private int nextIndex; // Index of element to be returned by next
        private E nextItem;    // Element to be returned by next call to next
        private E lastItem;    // Element returned by last call to next
        private int lastRet;   // Index of last element returned, or -1 if none

        Itr() {
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                lastRet = -1;
                if ((remaining = count) > 0)
                    nextItem = itemAt(nextIndex = takeIndex);
            } finally {
                lock.unlock();
}
}

        public boolean hasNext() {
            return remaining > 0;
}

        public E next() {
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                if (remaining <= 0)
                    throw new NoSuchElementException();
                lastRet = nextIndex;
                E x = itemAt(nextIndex);  // check for fresher value
                if (x == null) {
                    x = nextItem;         // we are forced to report old value
                    lastItem = null;      // but ensure remove fails
}
                else
                    lastItem = x;
                while (--remaining > 0 && // skip over nulls
                       (nextItem = itemAt(nextIndex = inc(nextIndex))) == null)
                    ;
                return x;
            } finally {
                lock.unlock();
}
}

        public void remove() {
            final ReentrantLock lock = ArrayBlockingQueue.this.lock;
            lock.lock();
            try {
                int i = lastRet;
                if (i == -1)
                    throw new IllegalStateException();
                lastRet = -1;
                E x = lastItem;
                lastItem = null;
                // only remove if item still at index
                if (x != null && x == items[i]) {
                    boolean removingHead = (i == takeIndex);
                    removeAt(i);
                    if (!removingHead)
                        nextIndex = dec(nextIndex);
}
            } finally {
                lock.unlock();
}
}
}

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BlockingDeque.java b/luni/src/main/java/java/util/concurrent/BlockingDeque.java
//Synthetic comment -- index 136df9c..34f103d 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -36,9 +36,9 @@
*  <tr>
*    <td><b>Insert</b></td>
*    <td>{@link #addFirst addFirst(e)}</td>
 *    <td>{@link #offerFirst offerFirst(e)}</td>
*    <td>{@link #putFirst putFirst(e)}</td>
 *    <td>{@link #offerFirst offerFirst(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -67,9 +67,9 @@
*  <tr>
*    <td><b>Insert</b></td>
*    <td>{@link #addLast addLast(e)}</td>
 *    <td>{@link #offerLast offerLast(e)}</td>
*    <td>{@link #putLast putLast(e)}</td>
 *    <td>{@link #offerLast offerLast(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -106,20 +106,20 @@
*    <td ALIGN=CENTER COLSPAN = 2> <b>Insert</b></td>
*  </tr>
*  <tr>
 *    <td>{@link #add add(e)}</td>
 *    <td>{@link #addLast addLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #offer offer(e)}</td>
 *    <td>{@link #offerLast offerLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #put put(e)}</td>
 *    <td>{@link #putLast putLast(e)}</td>
*  </tr>
*  <tr>
 *    <td>{@link #offer offer(e, time, unit)}</td>
 *    <td>{@link #offerLast offerLast(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td ALIGN=CENTER COLSPAN = 2> <b>Remove</b></td>
//Synthetic comment -- @@ -181,7 +181,7 @@
* possible to do so immediately without violating capacity restrictions,
* throwing an <tt>IllegalStateException</tt> if no space is currently
* available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerFirst offerFirst}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -196,7 +196,7 @@
* possible to do so immediately without violating capacity restrictions,
* throwing an <tt>IllegalStateException</tt> if no space is currently
* available.  When using a capacity-restricted deque, it is generally
     * preferable to use {@link #offerLast offerLast}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -212,7 +212,7 @@
* returning <tt>true</tt> upon success and <tt>false</tt> if no space is
* currently available.
* When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addFirst addFirst} method, which can
* fail to insert an element only by throwing an exception.
*
* @param e the element to add
//Synthetic comment -- @@ -228,7 +228,7 @@
* returning <tt>true</tt> upon success and <tt>false</tt> if no space is
* currently available.
* When using a capacity-restricted deque, this method is generally
     * preferable to the {@link #addLast addLast} method, which can
* fail to insert an element only by throwing an exception.
*
* @param e the element to add
//Synthetic comment -- @@ -371,8 +371,10 @@
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if an element was removed as a result of this call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
boolean removeFirstOccurrence(Object o);

//Synthetic comment -- @@ -387,8 +389,10 @@
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if an element was removed as a result of this call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
boolean removeLastOccurrence(Object o);

//Synthetic comment -- @@ -401,9 +405,9 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer offer}.
*
     * <p>This method is equivalent to {@link #addLast addLast}.
*
* @param e the element to add
* @throws IllegalStateException {@inheritDoc}
//Synthetic comment -- @@ -424,7 +428,7 @@
* generally preferable to the {@link #add} method, which can fail to
* insert an element only by throwing an exception.
*
     * <p>This method is equivalent to {@link #offerLast offerLast}.
*
* @param e the element to add
* @throws ClassCastException if the class of the specified element
//Synthetic comment -- @@ -440,7 +444,7 @@
* (in other words, at the tail of this deque), waiting if necessary for
* space to become available.
*
     * <p>This method is equivalent to {@link #putLast putLast}.
*
* @param e the element to add
* @throws InterruptedException {@inheritDoc}
//Synthetic comment -- @@ -458,7 +462,7 @@
* specified wait time if necessary for space to become available.
*
* <p>This method is equivalent to
     * {@link #offerLast offerLast}.
*
* @param e the element to add
* @return <tt>true</tt> if the element was added to this deque, else
//Synthetic comment -- @@ -557,13 +561,15 @@
* (or equivalently, if this deque changed as a result of the call).
*
* <p>This method is equivalent to
     * {@link #removeFirstOccurrence removeFirstOccurrence}.
*
* @param o element to be removed from this deque, if present
* @return <tt>true</tt> if this deque changed as a result of the call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
boolean remove(Object o);

//Synthetic comment -- @@ -575,8 +581,10 @@
* @param o object to be checked for containment in this deque
* @return <tt>true</tt> if this deque contains the specified element
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this deque (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
public boolean contains(Object o);

//Synthetic comment -- @@ -602,7 +610,7 @@
* words, inserts the element at the front of this deque unless it would
* violate capacity restrictions.
*
     * <p>This method is equivalent to {@link #addFirst addFirst}.
*
* @throws IllegalStateException {@inheritDoc}
* @throws ClassCastException {@inheritDoc}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BlockingQueue.java b/luni/src/main/java/java/util/concurrent/BlockingQueue.java
//Synthetic comment -- index d01c097..6cfe52b 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -42,7 +42,7 @@
*    <td>{@link #add add(e)}</td>
*    <td>{@link #offer offer(e)}</td>
*    <td>{@link #put put(e)}</td>
 *    <td>{@link #offer offer(e, time, unit)}</td>
*  </tr>
*  <tr>
*    <td><b>Remove</b></td>
//Synthetic comment -- @@ -102,7 +102,7 @@
* Usage example, based on a typical producer-consumer scenario.
* Note that a <tt>BlockingQueue</tt> can safely be used with multiple
* producers and multiple consumers.
 * <pre>
* class Producer implements Runnable {
*   private final BlockingQueue queue;
*   Producer(BlockingQueue q) { queue = q; }
//Synthetic comment -- @@ -135,8 +135,7 @@
*     new Thread(c1).start();
*     new Thread(c2).start();
*   }
 * }
 * </pre>
*
* <p>Memory consistency effects: As with other concurrent
* collections, actions in a thread prior to placing an object into a
//Synthetic comment -- @@ -156,7 +155,7 @@
* <tt>true</tt> upon success and throwing an
* <tt>IllegalStateException</tt> if no space is currently available.
* When using a capacity-restricted queue, it is generally preferable to
     * use {@link #offer offer}.
*
* @param e the element to add
* @return <tt>true</tt> (as specified by {@link Collection#add})
//Synthetic comment -- @@ -274,8 +273,10 @@
* @param o element to be removed from this queue, if present
* @return <tt>true</tt> if this queue changed as a result of the call
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
boolean remove(Object o);

//Synthetic comment -- @@ -287,8 +288,10 @@
* @param o object to be checked for containment in this queue
* @return <tt>true</tt> if this queue contains the specified element
* @throws ClassCastException if the class of the specified element
     *         is incompatible with this queue (optional)
     * @throws NullPointerException if the specified element is null (optional)
*/
public boolean contains(Object o);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/BrokenBarrierException.java b/luni/src/main/java/java/util/concurrent/BrokenBarrierException.java
//Synthetic comment -- index 3f93fbb..76fae13 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Callable.java b/luni/src/main/java/java/util/concurrent/Callable.java
//Synthetic comment -- index abc4d04..293544b 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CancellationException.java b/luni/src/main/java/java/util/concurrent/CancellationException.java
//Synthetic comment -- index 2c29544..dc452e4 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CompletionService.java b/luni/src/main/java/java/util/concurrent/CompletionService.java
//Synthetic comment -- index df9f719..7b0931c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentHashMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentHashMap.java
//Synthetic comment -- index c142e2a..c85a5cc 100644

//Synthetic comment -- @@ -1,16 +1,13 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -76,7 +73,25 @@

/*
* The basic strategy is to subdivide the table among Segments,
     * each of which itself is a concurrently readable hash table.
*/

/* ---------------- Constants -------------- */
//Synthetic comment -- @@ -108,8 +123,15 @@
static final int MAXIMUM_CAPACITY = 1 << 30;

/**
* The maximum number of segments to allow; used to bound
     * constructor arguments.
*/
static final int MAX_SEGMENTS = 1 << 16; // slightly conservative

//Synthetic comment -- @@ -135,7 +157,7 @@
final int segmentShift;

/**
     * The segments, each of which is a specialized hash table
*/
final Segment<K,V>[] segments;

//Synthetic comment -- @@ -143,7 +165,66 @@
transient Set<Map.Entry<K,V>> entrySet;
transient Collection<V> values;

    /* ---------------- Small Utilities -------------- */

/**
* Applies a supplemental hash function to a given hashCode, which
//Synthetic comment -- @@ -164,104 +245,67 @@
}

/**
     * Returns the segment that should be used for key with given hash
     * @param hash the hash code for the key
     * @return the segment
     */
    final Segment<K,V> segmentFor(int hash) {
        return segments[(hash >>> segmentShift) & segmentMask];
    }

    /* ---------------- Inner Classes -------------- */

    /**
     * ConcurrentHashMap list entry. Note that this is never exported
     * out as a user-visible Map.Entry.
     *
     * Because the value field is volatile, not final, it is legal wrt
     * the Java Memory Model for an unsynchronized reader to see null
     * instead of initial value when read via a data race.  Although a
     * reordering leading to this is not likely to ever actually
     * occur, the Segment.readValueUnderLock method is used as a
     * backup in case a null (pre-initialized) value is ever seen in
     * an unsynchronized access method.
     */
    static final class HashEntry<K,V> {
        final K key;
        final int hash;
        volatile V value;
        final HashEntry<K,V> next;

        HashEntry(K key, int hash, HashEntry<K,V> next, V value) {
            this.key = key;
            this.hash = hash;
            this.next = next;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        static final <K,V> HashEntry<K,V>[] newArray(int i) {
            return new HashEntry[i];
        }
    }

    /**
* Segments are specialized versions of hash tables.  This
* subclasses from ReentrantLock opportunistically, just to
* simplify some locking and avoid separate construction.
*/
static final class Segment<K,V> extends ReentrantLock implements Serializable {
/*
         * Segments maintain a table of entry lists that are ALWAYS
         * kept in a consistent state, so can be read without locking.
         * Next fields of nodes are immutable (final).  All list
         * additions are performed at the front of each bin. This
         * makes it easy to check changes, and also fast to traverse.
         * When nodes would otherwise be changed, new nodes are
         * created to replace them. This works well for hash tables
         * since the bin lists tend to be short. (The average length
         * is less than two for the default load factor threshold.)
*
         * Read operations can thus proceed without locking, but rely
         * on selected uses of volatiles to ensure that completed
         * write operations performed by other threads are
         * noticed. For most purposes, the "count" field, tracking the
         * number of elements, serves as that volatile variable
         * ensuring visibility.  This is convenient because this field
         * needs to be read in many read operations anyway:
         *
         *   - All (unsynchronized) read operations must first read the
         *     "count" field, and should not look at table entries if
         *     it is 0.
         *
         *   - All (synchronized) write operations should write to
         *     the "count" field after structurally changing any bin.
         *     The operations must not take any action that could even
         *     momentarily cause a concurrent read operation to see
         *     inconsistent data. This is made easier by the nature of
         *     the read operations in Map. For example, no operation
         *     can reveal that the table has grown but the threshold
         *     has not yet been updated, so there are no atomicity
         *     requirements for this with respect to reads.
         *
         * As a guide, all critical volatile reads and writes to the
         * count field are marked in code comments.
*/

private static final long serialVersionUID = 2249069246763182397L;

/**
         * The number of elements in this segment's region.
*/
        transient volatile int count;

/**
         * Number of updates that alter the size of the table. This is
         * used during bulk-read methods to make sure they see a
         * consistent snapshot: If modCounts change during a traversal
         * of segments computing size or checking containsValue, then
         * we might have an inconsistent view of state so (usually)
         * must retry.
*/
transient int modCount;

//Synthetic comment -- @@ -273,11 +317,6 @@
transient int threshold;

/**
         * The per-segment table.
         */
        transient volatile HashEntry<K,V>[] table;

        /**
* The load factor for the hash table.  Even though this value
* is same for all segments, it is replicated to avoid needing
* links to outer object.
//Synthetic comment -- @@ -285,202 +324,93 @@
*/
final float loadFactor;

        Segment(int initialCapacity, float lf) {
            loadFactor = lf;
            setTable(HashEntry.<K,V>newArray(initialCapacity));
}

@SuppressWarnings("unchecked")
        static final <K,V> Segment<K,V>[] newArray(int i) {
            return new Segment[i];
        }

        /**
         * Sets table to new HashEntry array.
         * Call only while holding lock or in constructor.
         */
        void setTable(HashEntry<K,V>[] newTable) {
            threshold = (int)(newTable.length * loadFactor);
            table = newTable;
        }

        /**
         * Returns properly casted first entry of bin for given hash.
         */
        HashEntry<K,V> getFirst(int hash) {
            HashEntry<K,V>[] tab = table;
            return tab[hash & (tab.length - 1)];
        }

        /**
         * Reads value field of an entry under lock. Called if value
         * field ever appears to be null. This is possible only if a
         * compiler happens to reorder a HashEntry initialization with
         * its table assignment, which is legal under memory model
         * but is not known to ever occur.
         */
        V readValueUnderLock(HashEntry<K,V> e) {
            lock();
            try {
                return e.value;
            } finally {
                unlock();
            }
        }

        /* Specialized implementations of map methods */

        V get(Object key, int hash) {
            if (count != 0) { // read-volatile
                HashEntry<K,V> e = getFirst(hash);
                while (e != null) {
                    if (e.hash == hash && key.equals(e.key)) {
                        V v = e.value;
                        if (v != null)
                            return v;
                        return readValueUnderLock(e); // recheck
                    }
                    e = e.next;
                }
            }
            return null;
        }

        boolean containsKey(Object key, int hash) {
            if (count != 0) { // read-volatile
                HashEntry<K,V> e = getFirst(hash);
                while (e != null) {
                    if (e.hash == hash && key.equals(e.key))
                        return true;
                    e = e.next;
                }
            }
            return false;
        }

        boolean containsValue(Object value) {
            if (count != 0) { // read-volatile
                HashEntry<K,V>[] tab = table;
                int len = tab.length;
                for (int i = 0 ; i < len; i++) {
                    for (HashEntry<K,V> e = tab[i]; e != null; e = e.next) {
                        V v = e.value;
                        if (v == null) // recheck
                            v = readValueUnderLock(e);
                        if (value.equals(v))
                            return true;
                    }
                }
            }
            return false;
        }

        boolean replace(K key, int hash, V oldValue, V newValue) {
            lock();
            try {
                HashEntry<K,V> e = getFirst(hash);
                while (e != null && (e.hash != hash || !key.equals(e.key)))
                    e = e.next;

                boolean replaced = false;
                if (e != null && oldValue.equals(e.value)) {
                    replaced = true;
                    e.value = newValue;
                }
                return replaced;
            } finally {
                unlock();
            }
        }

        V replace(K key, int hash, V newValue) {
            lock();
            try {
                HashEntry<K,V> e = getFirst(hash);
                while (e != null && (e.hash != hash || !key.equals(e.key)))
                    e = e.next;

                V oldValue = null;
                if (e != null) {
                    oldValue = e.value;
                    e.value = newValue;
                }
                return oldValue;
            } finally {
                unlock();
            }
        }


        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            lock();
            try {
                int c = count;
                if (c++ > threshold) // ensure capacity
                    rehash();
                HashEntry<K,V>[] tab = table;
                int index = hash & (tab.length - 1);
                HashEntry<K,V> first = tab[index];
                HashEntry<K,V> e = first;
                while (e != null && (e.hash != hash || !key.equals(e.key)))
                    e = e.next;

                V oldValue;
                if (e != null) {
                    oldValue = e.value;
                    if (!onlyIfAbsent)
                        e.value = value;
                }
                else {
                    oldValue = null;
                    ++modCount;
                    tab[index] = new HashEntry<K,V>(key, hash, first, value);
                    count = c; // write-volatile
                }
                return oldValue;
            } finally {
                unlock();
            }
        }

        void rehash() {
HashEntry<K,V>[] oldTable = table;
int oldCapacity = oldTable.length;
            if (oldCapacity >= MAXIMUM_CAPACITY)
                return;

            /*
             * Reclassify nodes in each list to new Map.  Because we are
             * using power-of-two expansion, the elements from each bin
             * must either stay at same index, or move with a power of two
             * offset. We eliminate unnecessary node creation by catching
             * cases where old nodes can be reused because their next
             * fields won't change. Statistically, at the default
             * threshold, only about one-sixth of them need cloning when
             * a table doubles. The nodes they replace will be garbage
             * collectable as soon as they are no longer referenced by any
             * reader thread that may be in the midst of traversing table
             * right now.
             */

            HashEntry<K,V>[] newTable = HashEntry.newArray(oldCapacity<<1);
            threshold = (int)(newTable.length * loadFactor);
            int sizeMask = newTable.length - 1;
for (int i = 0; i < oldCapacity ; i++) {
                // We need to guarantee that any existing reads of old Map can
                //  proceed. So we cannot yet null out each bin.
HashEntry<K,V> e = oldTable[i];

if (e != null) {
HashEntry<K,V> next = e.next;
int idx = e.hash & sizeMask;

                    //  Single node on list
                    if (next == null)
newTable[idx] = e;

                    else {
                        // Reuse trailing consecutive sequence at same slot
HashEntry<K,V> lastRun = e;
int lastIdx = idx;
for (HashEntry<K,V> last = next;
//Synthetic comment -- @@ -493,74 +423,263 @@
}
}
newTable[lastIdx] = lastRun;

                        // Clone all remaining nodes
for (HashEntry<K,V> p = e; p != lastRun; p = p.next) {
                            int k = p.hash & sizeMask;
HashEntry<K,V> n = newTable[k];
                            newTable[k] = new HashEntry<K,V>(p.key, p.hash,
                                                             n, p.value);
}
}
}
}
table = newTable;
}

/**
* Remove; match on key only if value null, else match both.
*/
        V remove(Object key, int hash, Object value) {
            lock();
try {
                int c = count - 1;
HashEntry<K,V>[] tab = table;
                int index = hash & (tab.length - 1);
                HashEntry<K,V> first = tab[index];
                HashEntry<K,V> e = first;
                while (e != null && (e.hash != hash || !key.equals(e.key)))
                    e = e.next;

                V oldValue = null;
                if (e != null) {
                    V v = e.value;
                    if (value == null || value.equals(v)) {
                        oldValue = v;
                        // All entries following removed node can stay
                        // in list, but all preceding ones need to be
                        // cloned.
                        ++modCount;
                        HashEntry<K,V> newFirst = e.next;
                        for (HashEntry<K,V> p = first; p != e; p = p.next)
                            newFirst = new HashEntry<K,V>(p.key, p.hash,
                                                          newFirst, p.value);
                        tab[index] = newFirst;
                        count = c; // write-volatile
}
}
                return oldValue;
} finally {
unlock();
}
}

        void clear() {
            if (count != 0) {
                lock();
                try {
                    HashEntry<K,V>[] tab = table;
                    for (int i = 0; i < tab.length ; i++)
                        tab[i] = null;
                    ++modCount;
                    count = 0; // write-volatile
                } finally {
                    unlock();
}
}
}
}



/* ---------------- Public operations -------------- */

//Synthetic comment -- @@ -580,14 +699,13 @@
* negative or the load factor or concurrencyLevel are
* nonpositive.
*/
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
        segmentShift = 32 - sshift;
        segmentMask = ssize - 1;
        this.segments = Segment.newArray(ssize);

if (initialCapacity > MAXIMUM_CAPACITY)
initialCapacity = MAXIMUM_CAPACITY;
int c = initialCapacity / ssize;
if (c * ssize < initialCapacity)
++c;
        int cap = 1;
while (cap < c)
cap <<= 1;

        for (int i = 0; i < this.segments.length; ++i)
            this.segments[i] = new Segment<K,V>(cap, loadFactor);
}

/**
//Synthetic comment -- @@ -672,34 +792,37 @@
* @return <tt>true</tt> if this map contains no key-value mappings
*/
public boolean isEmpty() {
        final Segment<K,V>[] segments = this.segments;
/*
         * We keep track of per-segment modCounts to avoid ABA
         * problems in which an element in one segment was added and
         * in another removed during traversal, in which case the
         * table was never actually empty at any point. Note the
         * similar use of modCounts in the size() and containsValue()
         * methods, which are the only other methods also susceptible
         * to ABA problems.
*/
        int[] mc = new int[segments.length];
        int mcsum = 0;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0)
                return false;
            else
                mcsum += mc[i] = segments[i].modCount;
        }
        // If mcsum happens to be zero, then we know we got a snapshot
        // before any modifications at all were made.  This is
        // probably common enough to bother tracking.
        if (mcsum != 0) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0 ||
                    mc[i] != segments[i].modCount)
return false;
}
}
return true;
}

//Synthetic comment -- @@ -711,45 +834,36 @@
* @return the number of key-value mappings in this map
*/
public int size() {
        final Segment<K,V>[] segments = this.segments;
        long sum = 0;
        long check = 0;
        int[] mc = new int[segments.length];
// Try a few times to get accurate count. On failure due to
// continuous async changes in table, resort to locking.
        for (int k = 0; k < RETRIES_BEFORE_LOCK; ++k) {
            check = 0;
            sum = 0;
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++i) {
                sum += segments[i].count;
                mcsum += mc[i] = segments[i].modCount;
            }
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++i) {
                    check += segments[i].count;
                    if (mc[i] != segments[i].modCount) {
                        check = -1; // force retry
                        break;
                    }
}
}
            if (check == sum)
                break;
}
        if (check != sum) { // Resort to locking all segments
            sum = 0;
            for (int i = 0; i < segments.length; ++i)
                segments[i].lock();
            for (int i = 0; i < segments.length; ++i)
                sum += segments[i].count;
            for (int i = 0; i < segments.length; ++i)
                segments[i].unlock();
}
        if (sum > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else
            return (int)sum;
}

/**
//Synthetic comment -- @@ -764,8 +878,21 @@
* @throws NullPointerException if the specified key is null
*/
public V get(Object key) {
        int hash = hash(key.hashCode());
        return segmentFor(hash).get(key, hash);
}

/**
//Synthetic comment -- @@ -777,9 +904,23 @@
*         <tt>equals</tt> method; <tt>false</tt> otherwise.
* @throws NullPointerException if the specified key is null
*/
public boolean containsKey(Object key) {
        int hash = hash(key.hashCode());
        return segmentFor(hash).containsKey(key, hash);
}

/**
//Synthetic comment -- @@ -794,53 +935,47 @@
* @throws NullPointerException if the specified value is null
*/
public boolean containsValue(Object value) {
if (value == null)
throw new NullPointerException();

        // See explanation of modCount use above

final Segment<K,V>[] segments = this.segments;
        int[] mc = new int[segments.length];

        // Try a few times without locking
        for (int k = 0; k < RETRIES_BEFORE_LOCK; ++k) {
            int sum = 0;
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++i) {
                int c = segments[i].count;
                mcsum += mc[i] = segments[i].modCount;
                if (segments[i].containsValue(value))
                    return true;
            }
            boolean cleanSweep = true;
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++i) {
                    int c = segments[i].count;
                    if (mc[i] != segments[i].modCount) {
                        cleanSweep = false;
                        break;
}
}
            }
            if (cleanSweep)
                return false;
        }
        // Resort to locking all segments
        for (int i = 0; i < segments.length; ++i)
            segments[i].lock();
        boolean found = false;
        try {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].containsValue(value)) {
                    found = true;
                    break;
                }
}
} finally {
            for (int i = 0; i < segments.length; ++i)
                segments[i].unlock();
}
        return found;
}

/**
//Synthetic comment -- @@ -850,7 +985,7 @@
* full compatibility with class {@link java.util.Hashtable},
* which supported this method prior to introduction of the
* Java Collections framework.

* @param  value a value to search for
* @return <tt>true</tt> if and only if some key maps to the
*         <tt>value</tt> argument in this table as
//Synthetic comment -- @@ -875,11 +1010,17 @@
*         <tt>null</tt> if there was no mapping for <tt>key</tt>
* @throws NullPointerException if the specified key or value is null
*/
public V put(K key, V value) {
if (value == null)
throw new NullPointerException();
int hash = hash(key.hashCode());
        return segmentFor(hash).put(key, hash, value, false);
}

/**
//Synthetic comment -- @@ -889,11 +1030,17 @@
*         or <tt>null</tt> if there was no mapping for the key
* @throws NullPointerException if the specified key or value is null
*/
public V putIfAbsent(K key, V value) {
if (value == null)
throw new NullPointerException();
int hash = hash(key.hashCode());
        return segmentFor(hash).put(key, hash, value, true);
}

/**
//Synthetic comment -- @@ -919,7 +1066,8 @@
*/
public V remove(Object key) {
int hash = hash(key.hashCode());
        return segmentFor(hash).remove(key, hash, null);
}

/**
//Synthetic comment -- @@ -929,9 +1077,9 @@
*/
public boolean remove(Object key, Object value) {
int hash = hash(key.hashCode());
        if (value == null)
            return false;
        return segmentFor(hash).remove(key, hash, value) != null;
}

/**
//Synthetic comment -- @@ -940,10 +1088,11 @@
* @throws NullPointerException if any of the arguments are null
*/
public boolean replace(K key, V oldValue, V newValue) {
if (oldValue == null || newValue == null)
throw new NullPointerException();
        int hash = hash(key.hashCode());
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
}

/**
//Synthetic comment -- @@ -954,18 +1103,23 @@
* @throws NullPointerException if the specified key or value is null
*/
public V replace(K key, V value) {
if (value == null)
throw new NullPointerException();
        int hash = hash(key.hashCode());
        return segmentFor(hash).replace(key, hash, value);
}

/**
* Removes all of the mappings from this map.
*/
public void clear() {
        for (int i = 0; i < segments.length; ++i)
            segments[i].clear();
}

/**
//Synthetic comment -- @@ -1066,42 +1220,41 @@
advance();
}

        public boolean hasMoreElements() { return hasNext(); }

final void advance() {
            if (nextEntry != null && (nextEntry = nextEntry.next) != null)
                return;

            while (nextTableIndex >= 0) {
                if ( (nextEntry = currentTable[nextTableIndex--]) != null)
                    return;
            }

            while (nextSegmentIndex >= 0) {
                Segment<K,V> seg = segments[nextSegmentIndex--];
                if (seg.count != 0) {
                    currentTable = seg.table;
                    for (int j = currentTable.length - 1; j >= 0; --j) {
                        if ( (nextEntry = currentTable[j]) != null) {
                            nextTableIndex = j - 1;
                            return;
                        }
                    }
}
}
}

        public boolean hasNext() { return nextEntry != null; }

        HashEntry<K,V> nextEntry() {
            if (nextEntry == null)
throw new NoSuchElementException();
            lastReturned = nextEntry;
            advance();
            return lastReturned;
}

        public void remove() {
if (lastReturned == null)
throw new IllegalStateException();
ConcurrentHashMap.this.remove(lastReturned.key);
//Synthetic comment -- @@ -1113,16 +1266,16 @@
extends HashIterator
implements Iterator<K>, Enumeration<K>
{
        public K next()        { return super.nextEntry().key; }
        public K nextElement() { return super.nextEntry().key; }
}

final class ValueIterator
extends HashIterator
implements Iterator<V>, Enumeration<V>
{
        public V next()        { return super.nextEntry().value; }
        public V nextElement() { return super.nextEntry().value; }
}

/**
//Synthetic comment -- @@ -1137,7 +1290,7 @@
}

/**
         * Set our entry's value and write through to the map. The
* value to return is somewhat arbitrary here. Since a
* WriteThroughEntry does not necessarily track asynchronous
* changes, the most recent "previous" value could be
//Synthetic comment -- @@ -1233,24 +1386,30 @@
/* ---------------- Serialization Support -------------- */

/**
     * Save the state of the <tt>ConcurrentHashMap</tt> instance to a
     * stream (i.e., serialize it).
* @param s the stream
* @serialData
* the key (Object) and value (Object)
* for each key-value mapping, followed by a null pair.
* The key-value mappings are emitted in no particular order.
*/
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
s.defaultWriteObject();

for (int k = 0; k < segments.length; ++k) {
            Segment<K,V> seg = segments[k];
seg.lock();
try {
HashEntry<K,V>[] tab = seg.table;
for (int i = 0; i < tab.length; ++i) {
                    for (HashEntry<K,V> e = tab[i]; e != null; e = e.next) {
s.writeObject(e.key);
s.writeObject(e.value);
}
//Synthetic comment -- @@ -1264,17 +1423,24 @@
}

/**
     * Reconstitute the <tt>ConcurrentHashMap</tt> instance from a
     * stream (i.e., deserialize it).
* @param s the stream
*/
private void readObject(java.io.ObjectInputStream s)
        throws IOException, ClassNotFoundException {
s.defaultReadObject();

        // Initialize each segment to be minimally sized, and let grow.
        for (int i = 0; i < segments.length; ++i) {
            segments[i].setTable(new HashEntry[1]);
}

// Read the keys and values, and put the mappings in the table
//Synthetic comment -- @@ -1286,4 +1452,31 @@
put(key, value);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentLinkedDeque.java b/luni/src/main/java/java/util/concurrent/ConcurrentLinkedDeque.java
//Synthetic comment -- index 72c2e08..30adb36 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea and Martin Buchholz with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -34,10 +34,17 @@
* ConcurrentModificationException}, and may proceed concurrently with
* other operations.
*
 * <p>Beware that, unlike in most collections, the {@code size}
 * method is <em>NOT</em> a constant-time operation. Because of the
* asynchronous nature of these deques, determining the current number
 * of elements requires a traversal of the elements.
*
* <p>This class and its iterator implement all of the <em>optional</em>
* methods of the {@link Deque} and {@link Iterator} interfaces.
//Synthetic comment -- @@ -245,13 +252,6 @@

private static final Node<Object> PREV_TERMINATOR, NEXT_TERMINATOR;

    static {
        PREV_TERMINATOR = new Node<Object>(null);
        PREV_TERMINATOR.next = PREV_TERMINATOR;
        NEXT_TERMINATOR = new Node<Object>(null);
        NEXT_TERMINATOR.prev = NEXT_TERMINATOR;
    }

@SuppressWarnings("unchecked")
Node<E> prevTerminator() {
return (Node<E>) PREV_TERMINATOR;
//Synthetic comment -- @@ -267,6 +267,9 @@
volatile E item;
volatile Node<E> next;

/**
* Constructs a new node.  Uses relaxed write because item can
* only be seen after publication via casNext or casPrev.
//Synthetic comment -- @@ -297,14 +300,25 @@

// Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE =
            sun.misc.Unsafe.getUnsafe();
        private static final long prevOffset =
            objectFieldOffset(UNSAFE, "prev", Node.class);
        private static final long itemOffset =
            objectFieldOffset(UNSAFE, "item", Node.class);
        private static final long nextOffset =
            objectFieldOffset(UNSAFE, "next", Node.class);
}

/**
//Synthetic comment -- @@ -1209,8 +1223,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -1395,14 +1408,6 @@
initHeadTail(h, t);
}

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE =
        sun.misc.Unsafe.getUnsafe();
    private static final long headOffset =
        objectFieldOffset(UNSAFE, "head", ConcurrentLinkedDeque.class);
    private static final long tailOffset =
        objectFieldOffset(UNSAFE, "tail", ConcurrentLinkedDeque.class);

private boolean casHead(Node<E> cmp, Node<E> val) {
return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
//Synthetic comment -- @@ -1412,15 +1417,25 @@
return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
}

    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentLinkedQueue.java b/luni/src/main/java/java/util/concurrent/ConcurrentLinkedQueue.java
//Synthetic comment -- index 3ed0a7c..a0a26fd 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea and Martin Buchholz with assistance from members of
* JCP JSR-166 Expert Group and released to the public domain, as explained
 * at http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -47,7 +47,14 @@
* <p>Beware that, unlike in most collections, the {@code size} method
* is <em>NOT</em> a constant-time operation. Because of the
* asynchronous nature of these queues, determining the current number
 * of elements requires a traversal of the elements.
*
* <p>This class and its iterator implement all of the <em>optional</em>
* methods of the {@link Queue} and {@link Iterator} interfaces.
//Synthetic comment -- @@ -165,12 +172,22 @@

// Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE =
            sun.misc.Unsafe.getUnsafe();
        private static final long nextOffset =
            objectFieldOffset(UNSAFE, "next", Node.class);
        private static final long itemOffset =
            objectFieldOffset(UNSAFE, "item", Node.class);
}

/**
//Synthetic comment -- @@ -563,8 +580,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -761,14 +777,6 @@
throw new NullPointerException();
}

    // Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
    private static final long headOffset =
        objectFieldOffset(UNSAFE, "head", ConcurrentLinkedQueue.class);
    private static final long tailOffset =
        objectFieldOffset(UNSAFE, "tail", ConcurrentLinkedQueue.class);

private boolean casTail(Node<E> cmp, Node<E> val) {
return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
}
//Synthetic comment -- @@ -777,15 +785,21 @@
return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
}

    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentMap.java
//Synthetic comment -- index 2daebc5..3405acf 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -32,11 +32,12 @@
* If the specified key is not already associated
* with a value, associate it with the given value.
* This is equivalent to
     * <pre>
     *   if (!map.containsKey(key))
     *       return map.put(key, value);
     *   else
     *       return map.get(key);</pre>
* except that the action is performed atomically.
*
* @param key key with which the specified value is to be associated
//Synthetic comment -- @@ -61,11 +62,13 @@
/**
* Removes the entry for a key only if currently mapped to a given value.
* This is equivalent to
     * <pre>
     *   if (map.containsKey(key) &amp;&amp; map.get(key).equals(value)) {
     *       map.remove(key);
     *       return true;
     *   } else return false;</pre>
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated
//Synthetic comment -- @@ -74,20 +77,24 @@
* @throws UnsupportedOperationException if the <tt>remove</tt> operation
*         is not supported by this map
* @throws ClassCastException if the key or value is of an inappropriate
     *         type for this map (optional)
* @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values (optional)
*/
boolean remove(Object key, Object value);

/**
* Replaces the entry for a key only if currently mapped to a given value.
* This is equivalent to
     * <pre>
     *   if (map.containsKey(key) &amp;&amp; map.get(key).equals(oldValue)) {
     *       map.put(key, newValue);
     *       return true;
     *   } else return false;</pre>
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated
//Synthetic comment -- @@ -108,10 +115,12 @@
/**
* Replaces the entry for a key only if currently mapped to some value.
* This is equivalent to
     * <pre>
     *   if (map.containsKey(key)) {
     *       return map.put(key, value);
     *   } else return null;</pre>
* except that the action is performed atomically.
*
* @param key key with which the specified value is associated








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentNavigableMap.java b/luni/src/main/java/java/util/concurrent/ConcurrentNavigableMap.java
//Synthetic comment -- index 7d86afb..ea99886 100644

//Synthetic comment -- @@ -1,20 +1,20 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.*;

/**
* A {@link ConcurrentMap} supporting {@link NavigableMap} operations,
* and recursively so for its navigable sub-maps.
*
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
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
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
* A scalable concurrent {@link ConcurrentNavigableMap} implementation.
//Synthetic comment -- @@ -15,8 +18,8 @@
* creation time, depending on which constructor is used.
*
* <p>This class implements a concurrent variant of <a
 * href="http://www.cs.umd.edu/~pugh/">SkipLists</a> providing
 * expected average <i>log(n)</i> time cost for the
* <tt>containsKey</tt>, <tt>get</tt>, <tt>put</tt> and
* <tt>remove</tt> operations and their variants.  Insertion, removal,
* update, and access operations safely execute concurrently by
//Synthetic comment -- @@ -37,12 +40,13 @@
* <p>Beware that, unlike in most collections, the <tt>size</tt>
* method is <em>not</em> a constant-time operation. Because of the
* asynchronous nature of these maps, determining the current number
 * of elements requires a traversal of the elements.  Additionally,
 * the bulk operations <tt>putAll</tt>, <tt>equals</tt>, and
 * <tt>clear</tt> are <em>not</em> guaranteed to be performed
 * atomically. For example, an iterator operating concurrently with a
 * <tt>putAll</tt> operation might view only some of the added
 * elements.
*
* <p>This class and its views and iterators implement all of the
* <em>optional</em> methods of the {@link Map} and {@link Iterator}
//Synthetic comment -- @@ -51,10 +55,6 @@
* null return values cannot be reliably distinguished from the absence of
* elements.
*
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
* @author Doug Lea
* @param <K> the type of keys maintained by this map
* @param <V> the type of mapped values
//Synthetic comment -- @@ -322,11 +322,11 @@
private transient int randomSeed;

/** Lazily initialized key set */
    private transient KeySet keySet;
/** Lazily initialized entry set */
    private transient EntrySet entrySet;
/** Lazily initialized values collection */
    private transient Values values;
/** Lazily initialized descending key set */
private transient ConcurrentNavigableMap<K,V> descendingMap;

//Synthetic comment -- @@ -478,13 +478,24 @@
return new AbstractMap.SimpleImmutableEntry<K,V>(key, v);
}

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
        private static final long valueOffset =
            objectFieldOffset(UNSAFE, "value", Node.class);
        private static final long nextOffset =
            objectFieldOffset(UNSAFE, "next", Node.class);

}

/* ---------------- Indexing -------------- */
//Synthetic comment -- @@ -551,10 +562,18 @@
}

// Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
        private static final long rightOffset =
            objectFieldOffset(UNSAFE, "right", Index.class);

}

/* ---------------- Head nodes -------------- */
//Synthetic comment -- @@ -884,7 +903,7 @@
* direction.
*/
level = max + 1;
            Index<K,V>[] idxs = (Index<K,V>[])new Index[level+1];
Index<K,V> idx = null;
for (int i = 1; i <= level; ++i)
idxs[i] = idx = new Index<K,V>(z, idx, null);
//Synthetic comment -- @@ -1387,16 +1406,16 @@
* @return a shallow copy of this map
*/
public ConcurrentSkipListMap<K,V> clone() {
        ConcurrentSkipListMap<K,V> clone = null;
try {
            clone = (ConcurrentSkipListMap<K,V>) super.clone();
} catch (CloneNotSupportedException e) {
throw new InternalError();
}

        clone.initialize();
        clone.buildFromSorted(this);
        return clone;
}

/**
//Synthetic comment -- @@ -1458,7 +1477,7 @@
/* ---------------- Serialization -------------- */

/**
     * Save the state of this map to a stream.
*
* @serialData The key (Object) and value (Object) for each
* key-value mapping represented by the map, followed by
//Synthetic comment -- @@ -1483,7 +1502,9 @@
}

/**
     * Reconstitute the map from a stream.
*/
private void readObject(final java.io.ObjectInputStream s)
throws java.io.IOException, ClassNotFoundException {
//Synthetic comment -- @@ -1613,7 +1634,9 @@
/**
* Returns <tt>true</tt> if this map maps one or more keys to the
* specified value.  This operation requires time linear in the
     * map size.
*
* @param value value whose presence in this map is to be tested
* @return <tt>true</tt> if a mapping to <tt>value</tt> exists;
//Synthetic comment -- @@ -1703,14 +1726,14 @@
*
* @return a navigable set view of the keys in this map
*/
     public NavigableSet<K> keySet() {
        KeySet ks = keySet;
        return (ks != null) ? ks : (keySet = new KeySet(this));
}

public NavigableSet<K> navigableKeySet() {
        KeySet ks = keySet;
        return (ks != null) ? ks : (keySet = new KeySet(this));
}

/**
//Synthetic comment -- @@ -1732,8 +1755,8 @@
* reflect any modifications subsequent to construction.
*/
public Collection<V> values() {
        Values vs = values;
        return (vs != null) ? vs : (values = new Values(this));
}

/**
//Synthetic comment -- @@ -1761,8 +1784,8 @@
*         sorted in ascending key order
*/
public Set<Map.Entry<K,V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet(this));
}

public ConcurrentNavigableMap<K,V> descendingMap() {
//Synthetic comment -- @@ -2253,8 +2276,8 @@

static final class KeySet<E>
extends AbstractSet<E> implements NavigableSet<E> {
        private final ConcurrentNavigableMap<E,Object> m;
        KeySet(ConcurrentNavigableMap<E,Object> map) { m = map; }
public int size() { return m.size(); }
public boolean isEmpty() { return m.isEmpty(); }
public boolean contains(Object o) { return m.containsKey(o); }
//Synthetic comment -- @@ -2268,11 +2291,11 @@
public E first() { return m.firstKey(); }
public E last() { return m.lastKey(); }
public E pollFirst() {
            Map.Entry<E,Object> e = m.pollFirstEntry();
return (e == null) ? null : e.getKey();
}
public E pollLast() {
            Map.Entry<E,Object> e = m.pollLastEntry();
return (e == null) ? null : e.getKey();
}
public Iterator<E> iterator() {
//Synthetic comment -- @@ -2323,20 +2346,20 @@
return tailSet(fromElement, true);
}
public NavigableSet<E> descendingSet() {
            return new KeySet(m.descendingMap());
}
}

static final class Values<E> extends AbstractCollection<E> {
        private final ConcurrentNavigableMap<Object, E> m;
        Values(ConcurrentNavigableMap<Object, E> map) {
m = map;
}
public Iterator<E> iterator() {
if (m instanceof ConcurrentSkipListMap)
                return ((ConcurrentSkipListMap<Object,E>)m).valueIterator();
else
                return ((SubMap<Object,E>)m).valueIterator();
}
public boolean isEmpty() {
return m.isEmpty();
//Synthetic comment -- @@ -2370,14 +2393,14 @@
public boolean contains(Object o) {
if (!(o instanceof Map.Entry))
return false;
            Map.Entry<K1,V1> e = (Map.Entry<K1,V1>)o;
V1 v = m.get(e.getKey());
return v != null && v.equals(e.getValue());
}
public boolean remove(Object o) {
if (!(o instanceof Map.Entry))
return false;
            Map.Entry<K1,V1> e = (Map.Entry<K1,V1>)o;
return m.remove(e.getKey(),
e.getValue());
}
//Synthetic comment -- @@ -2517,9 +2540,9 @@
if (lo == null)
return m.findFirst();
else if (loInclusive)
                return m.findNear(lo, m.GT|m.EQ);
else
                return m.findNear(lo, m.GT);
}

/**
//Synthetic comment -- @@ -2530,9 +2553,9 @@
if (hi == null)
return m.findLast();
else if (hiInclusive)
                return m.findNear(hi, m.LT|m.EQ);
else
                return m.findNear(hi, m.LT);
}

/**
//Synthetic comment -- @@ -2614,15 +2637,15 @@
*/
private Map.Entry<K,V> getNearEntry(K key, int rel) {
if (isDescending) { // adjust relation for direction
                if ((rel & m.LT) == 0)
                    rel |= m.LT;
else
                    rel &= ~m.LT;
}
if (tooLow(key))
                return ((rel & m.LT) != 0) ? null : lowestEntry();
if (tooHigh(key))
                return ((rel & m.LT) != 0) ? highestEntry() : null;
for (;;) {
Node<K,V> n = m.findNear(key, rel);
if (n == null || !inBounds(n.key))
//Synthetic comment -- @@ -2637,13 +2660,13 @@
// Almost the same as getNearEntry, except for keys
private K getNearKey(K key, int rel) {
if (isDescending) { // adjust relation for direction
                if ((rel & m.LT) == 0)
                    rel |= m.LT;
else
                    rel &= ~m.LT;
}
if (tooLow(key)) {
                if ((rel & m.LT) == 0) {
ConcurrentSkipListMap.Node<K,V> n = loNode();
if (isBeforeEnd(n))
return n.key;
//Synthetic comment -- @@ -2651,7 +2674,7 @@
return null;
}
if (tooHigh(key)) {
                if ((rel & m.LT) != 0) {
ConcurrentSkipListMap.Node<K,V> n = hiNode();
if (n != null) {
K last = n.key;
//Synthetic comment -- @@ -2683,7 +2706,7 @@
public V get(Object key) {
if (key == null) throw new NullPointerException();
K k = (K)key;
            return ((!inBounds(k)) ? null : m.get(k));
}

public V put(K key, V value) {
//Synthetic comment -- @@ -2850,35 +2873,35 @@
/* ----------------  Relational methods -------------- */

public Map.Entry<K,V> ceilingEntry(K key) {
            return getNearEntry(key, (m.GT|m.EQ));
}

public K ceilingKey(K key) {
            return getNearKey(key, (m.GT|m.EQ));
}

public Map.Entry<K,V> lowerEntry(K key) {
            return getNearEntry(key, (m.LT));
}

public K lowerKey(K key) {
            return getNearKey(key, (m.LT));
}

public Map.Entry<K,V> floorEntry(K key) {
            return getNearEntry(key, (m.LT|m.EQ));
}

public K floorKey(K key) {
            return getNearKey(key, (m.LT|m.EQ));
}

public Map.Entry<K,V> higherEntry(K key) {
            return getNearEntry(key, (m.GT));
}

public K higherKey(K key) {
            return getNearKey(key, (m.GT));
}

public K firstKey() {
//Synthetic comment -- @@ -2909,22 +2932,22 @@

public NavigableSet<K> keySet() {
KeySet<K> ks = keySetView;
            return (ks != null) ? ks : (keySetView = new KeySet(this));
}

public NavigableSet<K> navigableKeySet() {
KeySet<K> ks = keySetView;
            return (ks != null) ? ks : (keySetView = new KeySet(this));
}

public Collection<V> values() {
Collection<V> vs = valuesView;
            return (vs != null) ? vs : (valuesView = new Values(this));
}

public Set<Map.Entry<K,V>> entrySet() {
Set<Map.Entry<K,V>> es = entrySetView;
            return (es != null) ? es : (entrySetView = new EntrySet(this));
}

public NavigableSet<K> descendingKeySet() {
//Synthetic comment -- @@ -3053,20 +3076,16 @@
}

// Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
    private static final long headOffset =
        objectFieldOffset(UNSAFE, "head", ConcurrentSkipListMap.class);

    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
}
}

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ConcurrentSkipListSet.java b/luni/src/main/java/java/util/concurrent/ConcurrentSkipListSet.java
//Synthetic comment -- index d24876f..71431a9 100644

//Synthetic comment -- @@ -1,12 +1,15 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.*;
import sun.misc.Unsafe;

/**
* A scalable concurrent {@link NavigableSet} implementation based on
//Synthetic comment -- @@ -29,12 +32,14 @@
* <p>Beware that, unlike in most collections, the <tt>size</tt>
* method is <em>not</em> a constant-time operation. Because of the
* asynchronous nature of these sets, determining the current number
 * of elements requires a traversal of the elements. Additionally, the
 * bulk operations <tt>addAll</tt>, <tt>removeAll</tt>,
 * <tt>retainAll</tt>, and <tt>containsAll</tt> are <em>not</em>
 * guaranteed to be performed atomically. For example, an iterator
 * operating concurrently with an <tt>addAll</tt> operation might view
 * only some of the added elements.
*
* <p>This class and its iterators implement all of the
* <em>optional</em> methods of the {@link Set} and {@link Iterator}
//Synthetic comment -- @@ -43,10 +48,6 @@
* because <tt>null</tt> arguments and return values cannot be reliably
* distinguished from the absence of elements.
*
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
* @author Doug Lea
* @param <E> the type of elements maintained by this set
* @since 1.6
//Synthetic comment -- @@ -127,15 +128,15 @@
* @return a shallow copy of this set
*/
public ConcurrentSkipListSet<E> clone() {
        ConcurrentSkipListSet<E> clone = null;
try {
            clone = (ConcurrentSkipListSet<E>) super.clone();
            clone.setMap(new ConcurrentSkipListMap(m));
} catch (CloneNotSupportedException e) {
throw new InternalError();
}

        return clone;
}

/* ---------------- Set operations -------------- */
//Synthetic comment -- @@ -291,8 +292,8 @@
public boolean removeAll(Collection<?> c) {
// Override AbstractSet version to avoid unnecessary call to size()
boolean modified = false;
        for (Iterator<?> i = c.iterator(); i.hasNext(); )
            if (remove(i.next()))
modified = true;
return modified;
}
//Synthetic comment -- @@ -437,20 +438,24 @@
* @return a reverse order view of this set
*/
public NavigableSet<E> descendingSet() {
        return new ConcurrentSkipListSet(m.descendingMap());
}

// Support for resetting map in clone
    private static final Unsafe unsafe = Unsafe.getUnsafe();
private static final long mapOffset;
static {
try {
            mapOffset = unsafe.objectFieldOffset
                (ConcurrentSkipListSet.class.getDeclaredField("m"));
        } catch (Exception ex) { throw new Error(ex); }
}
    private void setMap(ConcurrentNavigableMap<E,Object> map) {
        unsafe.putObjectVolatile(this, mapOffset, map);
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CopyOnWriteArraySet.java b/luni/src/main/java/java/util/concurrent/CopyOnWriteArraySet.java
//Synthetic comment -- index 87419fc..1f37bc9 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -160,8 +160,7 @@
* The following code can be used to dump the set into a newly allocated
* array of <tt>String</tt>:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -358,6 +357,6 @@
* Test for equality, coping with nulls.
*/
private static boolean eq(Object o1, Object o2) {
        return (o1 == null ? o2 == null : o1.equals(o2));
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CountDownLatch.java b/luni/src/main/java/java/util/concurrent/CountDownLatch.java
//Synthetic comment -- index 1888279..e90badf 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

/**
* A synchronization aid that allows one or more threads to wait until
//Synthetic comment -- @@ -44,7 +43,7 @@
* until all workers have completed.
* </ul>
*
 * <pre>
* class Driver { // ...
*   void main() throws InterruptedException {
*     CountDownLatch startSignal = new CountDownLatch(1);
//Synthetic comment -- @@ -76,9 +75,7 @@
*   }
*
*   void doWork() { ... }
 * }
 *
 * </pre>
*
* <p>Another typical usage would be to divide a problem into N parts,
* describe each part with a Runnable that executes that portion and
//Synthetic comment -- @@ -87,7 +84,7 @@
* will be able to pass through await. (When threads must repeatedly
* count down in this way, instead use a {@link CyclicBarrier}.)
*
 * <pre>
* class Driver2 { // ...
*   void main() throws InterruptedException {
*     CountDownLatch doneSignal = new CountDownLatch(N);
//Synthetic comment -- @@ -115,9 +112,7 @@
*   }
*
*   void doWork() { ... }
 * }
 *
 * </pre>
*
* <p>Memory consistency effects: Until the count reaches
* zero, actions in a thread prior to calling








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/CyclicBarrier.java b/luni/src/main/java/java/util/concurrent/CyclicBarrier.java
//Synthetic comment -- index d5738c5..cf0b46e 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,7 +23,8 @@
*
* <p><b>Sample usage:</b> Here is an example of
*  using a barrier in a parallel decomposition design:
 * <pre>
* class Solver {
*   final int N;
*   final float[][] data;
//Synthetic comment -- @@ -61,8 +62,8 @@
*
*     waitUntilDone();
*   }
 * }
 * </pre>
* Here, each worker thread processes a row of the matrix then waits at the
* barrier until all rows have been processed. When all rows are processed
* the supplied {@link Runnable} barrier action is executed and merges the
//Synthetic comment -- @@ -76,9 +77,10 @@
* {@link #await} returns the arrival index of that thread at the barrier.
* You can then choose which thread should execute the barrier action, for
* example:
 * <pre>  if (barrier.await() == 0) {
 *     // log the completion of this iteration
 *   }</pre>
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
            throw new Error(toe); // cannot happen;
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/DelayQueue.java b/luni/src/main/java/java/util/concurrent/DelayQueue.java
//Synthetic comment -- index 8c44e82..52028cb 100644

//Synthetic comment -- @@ -1,12 +1,14 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/


package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;

// BEGIN android-note
//Synthetic comment -- @@ -29,7 +31,9 @@
*
* <p>This class and its iterator implement all of the
* <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
*
* @since 1.5
* @author Doug Lea
//Synthetic comment -- @@ -154,7 +158,7 @@
lock.lock();
try {
E first = q.peek();
            if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
return null;
else
return q.poll();
//Synthetic comment -- @@ -179,7 +183,7 @@
if (first == null)
available.await();
else {
                    long delay = first.getDelay(TimeUnit.NANOSECONDS);
if (delay <= 0)
return q.poll();
else if (leader != null)
//Synthetic comment -- @@ -226,7 +230,7 @@
else
nanos = available.awaitNanos(nanos);
} else {
                    long delay = first.getDelay(TimeUnit.NANOSECONDS);
if (delay <= 0)
return q.poll();
if (nanos <= 0)
//Synthetic comment -- @@ -284,6 +288,17 @@
}

/**
* @throws UnsupportedOperationException {@inheritDoc}
* @throws ClassCastException            {@inheritDoc}
* @throws NullPointerException          {@inheritDoc}
//Synthetic comment -- @@ -298,11 +313,9 @@
lock.lock();
try {
int n = 0;
            for (;;) {
                E first = q.peek();
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
                    break;
                c.add(q.poll());
++n;
}
return n;
//Synthetic comment -- @@ -328,11 +341,9 @@
lock.lock();
try {
int n = 0;
            while (n < maxElements) {
                E first = q.peek();
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
                    break;
                c.add(q.poll());
++n;
}
return n;
//Synthetic comment -- @@ -411,8 +422,7 @@
* <p>The following code can be used to dump a delay queue into a newly
* allocated array of <tt>Delayed</tt>:
*
     * <pre>
     *     Delayed[] a = q.toArray(new Delayed[0]);</pre>
*
* Note that <tt>toArray(new Object[0])</tt> is identical in function to
* <tt>toArray()</tt>.
//Synthetic comment -- @@ -451,6 +461,24 @@
}

/**
* Returns an iterator over all the elements (both expired and
* unexpired) in this queue. The iterator does not return the
* elements in any particular order.
//Synthetic comment -- @@ -473,7 +501,7 @@
*/
private class Itr implements Iterator<E> {
final Object[] array; // Array of all elements
        int cursor;           // index of next element to return;
int lastRet;          // index of last element, or -1 if no such

Itr(Object[] array) {
//Synthetic comment -- @@ -496,21 +524,8 @@
public void remove() {
if (lastRet < 0)
throw new IllegalStateException();
            Object x = array[lastRet];
lastRet = -1;
            // Traverse underlying queue to find == element,
            // not just a .equals element.
            lock.lock();
            try {
                for (Iterator it = q.iterator(); it.hasNext(); ) {
                    if (it.next() == x) {
                        it.remove();
                        return;
                    }
                }
            } finally {
                lock.unlock();
            }
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Delayed.java b/luni/src/main/java/java/util/concurrent/Delayed.java
//Synthetic comment -- index b1ff4ee..39d927c 100644

//Synthetic comment -- @@ -1,13 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;

import java.util.*;

/**
* A mix-in style interface for marking objects that should be
* acted upon after a given delay.








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Exchanger.java b/luni/src/main/java/java/util/concurrent/Exchanger.java
//Synthetic comment -- index 3c230be..6069dce 100644

//Synthetic comment -- @@ -2,7 +2,7 @@
* Written by Doug Lea, Bill Scherer, and Michael Scott with
* assistance from members of JCP JSR-166 Expert Group and released to
* the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,7 +23,7 @@
* to swap buffers between threads so that the thread filling the
* buffer gets a freshly emptied one when it needs it, handing off the
* filled one to the thread emptying the buffer.
 * <pre>{@code
* class FillAndEmpty {
*   Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
*   DataBuffer initialEmptyBuffer = ... a made-up type
//Synthetic comment -- @@ -59,8 +59,7 @@
*     new Thread(new FillingLoop()).start();
*     new Thread(new EmptyingLoop()).start();
*   }
 * }
 * }</pre>
*
* <p>Memory consistency effects: For each pair of threads that
* successfully exchange objects via an {@code Exchanger}, actions
//Synthetic comment -- @@ -135,8 +134,8 @@
* races between two threads or thread pre-emptions occurring
* between reading and CASing.  Also, very transient peak
* contention can be much higher than the average sustainable
     * levels.  The max limit is decreased on average 50% of the times
     * that a non-slot-zero wait elapses without being fulfilled.
* Threads experiencing elapsed waits move closer to zero, so
* eventually find existing (or future) threads even if the table
* has been shrunk due to inactivity.  The chosen mechanics and
//Synthetic comment -- @@ -275,7 +274,9 @@
* extra space.
*/
private static final class Slot extends AtomicReference<Object> {
        // Improve likelihood of isolation on <= 64 byte cache lines
long q0, q1, q2, q3, q4, q5, q6, q7, q8, q9, qa, qb, qc, qd, qe;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutionException.java b/luni/src/main/java/java/util/concurrent/ExecutionException.java
//Synthetic comment -- index bc561e5..9bb8dee 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -50,11 +50,9 @@

/**
* Constructs an <tt>ExecutionException</tt> with the specified cause.
     * The detail message is set to:
     * <pre>
     *  (cause == null ? null : cause.toString())</pre>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).
*
* @param  cause the cause (which is saved for later retrieval by the
*         {@link #getCause()} method)








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Executor.java b/luni/src/main/java/java/util/concurrent/Executor.java
//Synthetic comment -- index fbc4e6f..831bf46 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -27,23 +27,23 @@
* executor can run the submitted task immediately in the caller's
* thread:
*
 * <pre>
* class DirectExecutor implements Executor {
 *     public void execute(Runnable r) {
 *         r.run();
 *     }
 * }</pre>
*
* More typically, tasks are executed in some thread other
* than the caller's thread.  The executor below spawns a new thread
* for each task.
*
 * <pre>
* class ThreadPerTaskExecutor implements Executor {
 *     public void execute(Runnable r) {
 *         new Thread(r).start();
 *     }
 * }</pre>
*
* Many <tt>Executor</tt> implementations impose some sort of
* limitation on how and when tasks are scheduled.  The executor below








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutorCompletionService.java b/luni/src/main/java/java/util/concurrent/ExecutorCompletionService.java
//Synthetic comment -- index b41955d..c0d6006 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ExecutorService.java b/luni/src/main/java/java/util/concurrent/ExecutorService.java
//Synthetic comment -- index 89688e4..a33ceec 100644

//Synthetic comment -- @@ -1,14 +1,16 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.List;
import java.util.Collection;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
* An {@link Executor} that provides methods to manage termination and
//Synthetic comment -- @@ -44,7 +46,7 @@
* pool service incoming requests. It uses the preconfigured {@link
* Executors#newFixedThreadPool} factory method:
*
 * <pre>
* class NetworkService implements Runnable {
*   private final ServerSocket serverSocket;
*   private final ExecutorService pool;
//Synthetic comment -- @@ -72,14 +74,13 @@
*   public void run() {
*     // read and service request on socket
*   }
 * }
 * </pre>
*
* The following method shuts down an <tt>ExecutorService</tt> in two phases,
* first by calling <tt>shutdown</tt> to reject incoming tasks, and then
* calling <tt>shutdownNow</tt>, if necessary, to cancel any lingering tasks:
*
 * <pre>
* void shutdownAndAwaitTermination(ExecutorService pool) {
*   pool.shutdown(); // Disable new tasks from being submitted
*   try {
//Synthetic comment -- @@ -96,8 +97,7 @@
*     // Preserve interrupt status
*     Thread.currentThread().interrupt();
*   }
 * }
 * </pre>
*
* <p>Memory consistency effects: Actions in a thread prior to the
* submission of a {@code Runnable} or {@code Callable} task to an








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Executors.java b/luni/src/main/java/java/util/concurrent/Executors.java
//Synthetic comment -- index e42b0cc..b4f03ba 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -12,9 +12,10 @@
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.AccessControlException;
// import sun.security.util.SecurityConstants; // android-removed

/**
* Factory and utility methods for {@link Executor}, {@link
* ExecutorService}, {@link ScheduledExecutorService}, {@link
//Synthetic comment -- @@ -271,10 +272,7 @@
/**
* Returns a default thread factory used to create new threads.
* This factory creates all new threads used by an Executor in the
     * same {@link ThreadGroup}. If there is a {@link
     * java.lang.SecurityManager}, it uses the group of {@link
     * System#getSecurityManager}, else the group of the thread
     * invoking this <tt>defaultThreadFactory</tt> method. Each new
* thread is created as a non-daemon thread with priority set to
* the smaller of <tt>Thread.NORM_PRIORITY</tt> and the maximum
* priority permitted in the thread group.  New threads have names
//Synthetic comment -- @@ -289,36 +287,7 @@
}

/**
     * Returns a thread factory used to create new threads that
     * have the same permissions as the current thread.
     * This factory creates threads with the same settings as {@link
     * Executors#defaultThreadFactory}, additionally setting the
     * AccessControlContext and contextClassLoader of new threads to
     * be the same as the thread invoking this
     * <tt>privilegedThreadFactory</tt> method.  A new
     * <tt>privilegedThreadFactory</tt> can be created within an
     * {@link AccessController#doPrivileged} action setting the
     * current thread's access control context to create threads with
     * the selected permission settings holding within that action.
     *
     * <p> Note that while tasks running within such threads will have
     * the same access control and class loader settings as the
     * current thread, they need not have the same {@link
     * java.lang.ThreadLocal} or {@link
     * java.lang.InheritableThreadLocal} values. If necessary,
     * particular values of thread locals can be set or reset before
     * any task runs in {@link ThreadPoolExecutor} subclasses using
     * {@link ThreadPoolExecutor#beforeExecute}. Also, if it is
     * necessary to initialize worker threads to have the same
     * InheritableThreadLocal settings as some other designated
     * thread, you can create a custom ThreadFactory in which that
     * thread waits for and services requests to create others that
     * will inherit its values.
     *
     * @return a thread factory
     * @throws AccessControlException if the current access control
     * context does not have permission to both get and set context
     * class loader.
*/
public static ThreadFactory privilegedThreadFactory() {
return new PrivilegedThreadFactory();
//Synthetic comment -- @@ -383,18 +352,7 @@
}

/**
     * Returns a {@link Callable} object that will, when
     * called, execute the given <tt>callable</tt> under the current
     * access control context. This method should normally be
     * invoked within an {@link AccessController#doPrivileged} action
     * to create callables that will, if possible, execute under the
     * selected permission settings holding within that action; or if
     * not possible, throw an associated {@link
     * AccessControlException}.
     * @param callable the underlying task
     * @return a callable object
     * @throws NullPointerException if callable null
     *
*/
public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
if (callable == null)
//Synthetic comment -- @@ -405,20 +363,10 @@
/**
* Returns a {@link Callable} object that will, when
* called, execute the given <tt>callable</tt> under the current
     * access control context, with the current context class loader
     * as the context class loader. This method should normally be
     * invoked within an {@link AccessController#doPrivileged} action
     * to create callables that will, if possible, execute under the
     * selected permission settings holding within that action; or if
     * not possible, throw an associated {@link
     * AccessControlException}.
     * @param callable the underlying task
*
* @return a callable object
* @throws NullPointerException if callable null
     * @throws AccessControlException if the current access control
     * context does not have permission to both set and get context
     * class loader.
*/
public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
if (callable == null)
//Synthetic comment -- @@ -480,17 +428,19 @@
private final ClassLoader ccl;

PrivilegedCallableUsingCurrentClassLoader(Callable<T> task) {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                // Calls to getContextClassLoader from this class
                // never trigger a security check, but we check
                // whether our callers have this permission anyways.
                sm.checkPermission(new RuntimePermission("getContextClassLoader")); // android-changed

                // Whether setContextClassLoader turns out to be necessary
                // or not, we fail fast if permission is not available.
                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
            }
this.task = task;
this.acc = AccessController.getContext();
this.ccl = Thread.currentThread().getContextClassLoader();
//Synthetic comment -- @@ -561,16 +511,18 @@

PrivilegedThreadFactory() {
super();
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                // Calls to getContextClassLoader from this class
                // never trigger a security check, but we check
                // whether our callers have this permission anyways.
                sm.checkPermission(new RuntimePermission("getContextClassLoader")); // android-changed

                // Fail fast
                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
            }
this.acc = AccessController.getContext();
this.ccl = Thread.currentThread().getContextClassLoader();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinPool.java b/luni/src/main/java/java/util/concurrent/ForkJoinPool.java
new file mode 100644
//Synthetic comment -- index 0000000..ee15ac8

//Synthetic comment -- @@ -0,0 +1,2127 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinTask.java b/luni/src/main/java/java/util/concurrent/ForkJoinTask.java
new file mode 100644
//Synthetic comment -- index 0000000..86a29d7

//Synthetic comment -- @@ -0,0 +1,1357 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ForkJoinWorkerThread.java b/luni/src/main/java/java/util/concurrent/ForkJoinWorkerThread.java
new file mode 100644
//Synthetic comment -- index 0000000..d99ffe9

//Synthetic comment -- @@ -0,0 +1,970 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Future.java b/luni/src/main/java/java/util/concurrent/Future.java
//Synthetic comment -- index eb84796..6a37729 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/FutureTask.java b/luni/src/main/java/java/util/concurrent/FutureTask.java
//Synthetic comment -- index 2f2335e..d51881d 100644

//Synthetic comment -- @@ -1,55 +1,116 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;

/**
* A cancellable asynchronous computation.  This class provides a base
* implementation of {@link Future}, with methods to start and cancel
* a computation, query to see if the computation is complete, and
* retrieve the result of the computation.  The result can only be
 * retrieved when the computation has completed; the <tt>get</tt>
 * method will block if the computation has not yet completed.  Once
* the computation has completed, the computation cannot be restarted
 * or cancelled.
*
 * <p>A <tt>FutureTask</tt> can be used to wrap a {@link Callable} or
 * {@link java.lang.Runnable} object.  Because <tt>FutureTask</tt>
 * implements <tt>Runnable</tt>, a <tt>FutureTask</tt> can be
 * submitted to an {@link Executor} for execution.
*
* <p>In addition to serving as a standalone class, this class provides
 * <tt>protected</tt> functionality that may be useful when creating
* customized task classes.
*
* @since 1.5
* @author Doug Lea
 * @param <V> The result type returned by this FutureTask's <tt>get</tt> method
*/
public class FutureTask<V> implements RunnableFuture<V> {
    /** Synchronization control for FutureTask */
    private final Sync sync;

/**
     * Creates a <tt>FutureTask</tt> that will, upon running, execute the
     * given <tt>Callable</tt>.
*
* @param  callable the callable task
     * @throws NullPointerException if callable is null
*/
public FutureTask(Callable<V> callable) {
if (callable == null)
throw new NullPointerException();
        sync = new Sync(callable);
}

/**
     * Creates a <tt>FutureTask</tt> that will, upon running, execute the
     * given <tt>Runnable</tt>, and arrange that <tt>get</tt> will return the
* given result on successful completion.
*
* @param runnable the runnable task
//Synthetic comment -- @@ -57,29 +118,46 @@
* you don't need a particular result, consider using
* constructions of the form:
* {@code Future<?> f = new FutureTask<Void>(runnable, null)}
     * @throws NullPointerException if runnable is null
*/
public FutureTask(Runnable runnable, V result) {
        sync = new Sync(Executors.callable(runnable, result));
}

public boolean isCancelled() {
        return sync.innerIsCancelled();
}

public boolean isDone() {
        return sync.innerIsDone();
}

public boolean cancel(boolean mayInterruptIfRunning) {
        return sync.innerCancel(mayInterruptIfRunning);
}

/**
* @throws CancellationException {@inheritDoc}
*/
public V get() throws InterruptedException, ExecutionException {
        return sync.innerGet();
}

/**
//Synthetic comment -- @@ -87,12 +165,18 @@
*/
public V get(long timeout, TimeUnit unit)
throws InterruptedException, ExecutionException, TimeoutException {
        return sync.innerGet(unit.toNanos(timeout));
}

/**
* Protected method invoked when this task transitions to state
     * <tt>isDone</tt> (whether normally or via cancellation). The
* default implementation does nothing.  Subclasses may override
* this method to invoke completion callbacks or perform
* bookkeeping. Note that you can query status inside the
//Synthetic comment -- @@ -102,230 +186,268 @@
protected void done() { }

/**
     * Sets the result of this Future to the given value unless
* this future has already been set or has been cancelled.
     * This method is invoked internally by the <tt>run</tt> method
* upon successful completion of the computation.
* @param v the value
*/
protected void set(V v) {
        sync.innerSet(v);
}

/**
     * Causes this future to report an <tt>ExecutionException</tt>
     * with the given throwable as its cause, unless this Future has
* already been set or has been cancelled.
     * This method is invoked internally by the <tt>run</tt> method
* upon failure of the computation.
* @param t the cause of failure
*/
protected void setException(Throwable t) {
        sync.innerSetException(t);
}

    // The following (duplicated) doc comment can be removed once
    //
    // 6270645: Javadoc comments should be inherited from most derived
    //          superinterface or superclass
    // is fixed.
    /**
     * Sets this Future to the result of its computation
     * unless it has been cancelled.
     */
public void run() {
        sync.innerRun();
}

/**
* Executes the computation without setting its result, and then
     * resets this Future to initial state, failing to do so if the
* computation encounters an exception or is cancelled.  This is
* designed for use with tasks that intrinsically execute more
* than once.
* @return true if successfully run and reset
*/
protected boolean runAndReset() {
        return sync.innerRunAndReset();
}

/**
     * Synchronization control for FutureTask. Note that this must be
     * a non-static inner class in order to invoke the protected
     * <tt>done</tt> method. For clarity, all inner class support
     * methods are same as outer, prefixed with "inner".
     *
     * Uses AQS sync state to represent run status
*/
    private final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -7828117401763700385L;

        /** State value representing that task is ready to run */
        private static final int READY     = 0;
        /** State value representing that task is running */
        private static final int RUNNING   = 1;
        /** State value representing that task ran */
        private static final int RAN       = 2;
        /** State value representing that task was cancelled */
        private static final int CANCELLED = 4;

        /** The underlying callable */
        private final Callable<V> callable;
        /** The result to return from get() */
        private V result;
        /** The exception to throw from get() */
        private Throwable exception;

        /**
         * The thread running task. When nulled after set/cancel, this
         * indicates that the results are accessible.  Must be
         * volatile, to ensure visibility upon completion.
         */
        private volatile Thread runner;

        Sync(Callable<V> callable) {
            this.callable = callable;
        }

        private boolean ranOrCancelled(int state) {
            return (state & (RAN | CANCELLED)) != 0;
        }

        /**
         * Implements AQS base acquire to succeed if ran or cancelled
         */
        protected int tryAcquireShared(int ignore) {
            return innerIsDone() ? 1 : -1;
        }

        /**
         * Implements AQS base release to always signal after setting
         * final done status by nulling runner thread.
         */
        protected boolean tryReleaseShared(int ignore) {
            runner = null;
            return true;
        }

        boolean innerIsCancelled() {
            return getState() == CANCELLED;
        }

        boolean innerIsDone() {
            return ranOrCancelled(getState()) && runner == null;
        }

        V innerGet() throws InterruptedException, ExecutionException {
            acquireSharedInterruptibly(0);
            if (getState() == CANCELLED)
                throw new CancellationException();
            if (exception != null)
                throw new ExecutionException(exception);
            return result;
        }

        V innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException {
            if (!tryAcquireSharedNanos(0, nanosTimeout))
                throw new TimeoutException();
            if (getState() == CANCELLED)
                throw new CancellationException();
            if (exception != null)
                throw new ExecutionException(exception);
            return result;
        }

        void innerSet(V v) {
            for (;;) {
                int s = getState();
                if (s == RAN)
                    return;
                if (s == CANCELLED) {
                    // aggressively release to set runner to null,
                    // in case we are racing with a cancel request
                    // that will try to interrupt runner
                    releaseShared(0);
                    return;
}
                if (compareAndSetState(s, RAN)) {
                    result = v;
                    releaseShared(0);
                    done();
                    return;
                }
}
}

        void innerSetException(Throwable t) {
            for (;;) {
                int s = getState();
                if (s == RAN)
                    return;
                if (s == CANCELLED) {
                    // aggressively release to set runner to null,
                    // in case we are racing with a cancel request
                    // that will try to interrupt runner
                    releaseShared(0);
                    return;
}
                if (compareAndSetState(s, RAN)) {
                    exception = t;
                    releaseShared(0);
                    done();
                    return;
}
            }
        }

        boolean innerCancel(boolean mayInterruptIfRunning) {
            for (;;) {
                int s = getState();
                if (ranOrCancelled(s))
                    return false;
                if (compareAndSetState(s, CANCELLED))
                    break;
            }
            if (mayInterruptIfRunning) {
                Thread r = runner;
                if (r != null)
                    r.interrupt();
            }
            releaseShared(0);
            done();
            return true;
        }

        void innerRun() {
            if (!compareAndSetState(READY, RUNNING))
                return;

            runner = Thread.currentThread();
            if (getState() == RUNNING) { // recheck after setting thread
                V result;
                try {
                    result = callable.call();
                } catch (Throwable ex) {
                    setException(ex);
                    return;
                }
                set(result);
            } else {
                releaseShared(0); // cancel
            }
        }

        boolean innerRunAndReset() {
            if (!compareAndSetState(READY, RUNNING))
                return false;
            try {
                runner = Thread.currentThread();
                if (getState() == RUNNING)
                    callable.call(); // don't set result
                runner = null;
                return compareAndSetState(RUNNING, READY);
            } catch (Throwable ex) {
                setException(ex);
                return false;
}
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedBlockingDeque.java b/luni/src/main/java/java/util/concurrent/LinkedBlockingDeque.java
//Synthetic comment -- index 8c01e71..6f32c47 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -13,6 +13,10 @@
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
* An optionally-bounded {@linkplain BlockingDeque blocking deque} based on
* linked nodes.
//Synthetic comment -- @@ -34,10 +38,6 @@
* <em>optional</em> methods of the {@link Collection} and {@link
* Iterator} interfaces.
*
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
* @since 1.6
* @author  Doug Lea
* @param <E> the type of elements held in this collection
//Synthetic comment -- @@ -590,7 +590,7 @@
/**
* Inserts the specified element at the end of this deque unless it would
* violate capacity restrictions.  When using a capacity-restricted deque,
     * it is generally preferable to use method {@link #offer offer}.
*
* <p>This method is equivalent to {@link #addLast}.
*
//Synthetic comment -- @@ -713,6 +713,8 @@
throw new NullPointerException();
if (c == this)
throw new IllegalArgumentException();
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -891,8 +893,7 @@
* The following code can be used to dump the deque into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
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
     * Save the state of this deque to a stream (that is, serialize it).
*
* @serialData The capacity (int), followed by elements (each an
* {@code Object}) in the proper order, followed by a null
//Synthetic comment -- @@ -1146,8 +1147,8 @@
}

/**
     * Reconstitute this deque from a stream (that is,
     * deserialize it).
* @param s the stream
*/
private void readObject(java.io.ObjectInputStream s)








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedBlockingQueue.java b/luni/src/main/java/java/util/concurrent/LinkedBlockingQueue.java
//Synthetic comment -- index d06c737..e8c9edb 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -106,13 +106,13 @@
private final int capacity;

/** Current number of elements */
    private final AtomicInteger count = new AtomicInteger(0);

/**
* Head of linked list.
* Invariant: head.item == null
*/
    private transient Node<E> head;

/**
* Tail of linked list.
//Synthetic comment -- @@ -303,7 +303,7 @@
// Note: convention in all put/take/etc is to preset local var
// holding count negative to indicate failure unless set.
int c = -1;
        Node<E> node = new Node(e);
final ReentrantLock putLock = this.putLock;
final AtomicInteger count = this.count;
putLock.lockInterruptibly();
//Synthetic comment -- @@ -383,7 +383,7 @@
if (count.get() == capacity)
return false;
int c = -1;
        Node<E> node = new Node(e);
final ReentrantLock putLock = this.putLock;
putLock.lock();
try {
//Synthetic comment -- @@ -601,8 +601,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -699,6 +698,8 @@
throw new NullPointerException();
if (c == this)
throw new IllegalArgumentException();
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
     * Save the state to a stream (that is, serialize it).
*
* @serialData The capacity is emitted (int), followed by all of
* its elements (each an {@code Object}) in the proper order,
//Synthetic comment -- @@ -856,8 +857,7 @@
}

/**
     * Reconstitute this queue instance from a stream (that is,
     * deserialize it).
*
* @param s the stream
*/








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/LinkedTransferQueue.java b/luni/src/main/java/java/util/concurrent/LinkedTransferQueue.java
new file mode 100644
//Synthetic comment -- index 0000000..2a3446e

//Synthetic comment -- @@ -0,0 +1,1323 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Phaser.java b/luni/src/main/java/java/util/concurrent/Phaser.java
new file mode 100644
//Synthetic comment -- index 0000000..25ff743

//Synthetic comment -- @@ -0,0 +1,1135 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/PriorityBlockingQueue.java b/luni/src/main/java/java/util/concurrent/PriorityBlockingQueue.java
//Synthetic comment -- index cffbe64..26c72eb 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -81,7 +81,7 @@
* java.util.PriorityQueue operations within a lock, as was done
* in a previous version of this class. To maintain
* interoperability, a plain PriorityQueue is still used during
     * serialization, which maintains compatibility at the espense of
* transiently doubling overhead.
*/

//Synthetic comment -- @@ -139,7 +139,7 @@
* to maintain compatibility with previous versions
* of this class. Non-null only during serialization/deserialization.
*/
    private PriorityQueue q;

/**
* Creates a {@code PriorityBlockingQueue} with the default
//Synthetic comment -- @@ -278,14 +278,13 @@
/**
* Mechanics for poll().  Call only while holding lock.
*/
    private E extract() {
        E result;
int n = size - 1;
if (n < 0)
            result = null;
else {
Object[] array = queue;
            result = (E) array[0];
E x = (E) array[n];
array[n] = null;
Comparator<? super E> cmp = comparator;
//Synthetic comment -- @@ -294,8 +293,8 @@
else
siftDownUsingComparator(0, x, array, n, cmp);
size = n;
}
        return result;
}

/**
//Synthetic comment -- @@ -312,6 +311,7 @@
* @param k the position to fill
* @param x the item to insert
* @param array the heap array
*/
private static <T> void siftUpComparable(int k, T x, Object[] array) {
Comparable<? super T> key = (Comparable<? super T>) x;
//Synthetic comment -- @@ -476,7 +476,7 @@
* @param timeout This parameter is ignored as the method never blocks
* @param unit This parameter is ignored as the method never blocks
* @return {@code true} (as specified by
     *  {@link BlockingQueue#offer BlockingQueue.offer})
* @throws ClassCastException if the specified element cannot be compared
*         with elements currently in the priority queue according to the
*         priority queue's ordering
//Synthetic comment -- @@ -489,13 +489,11 @@
public E poll() {
final ReentrantLock lock = this.lock;
lock.lock();
        E result;
try {
            result = extract();
} finally {
lock.unlock();
}
        return result;
}

public E take() throws InterruptedException {
//Synthetic comment -- @@ -503,7 +501,7 @@
lock.lockInterruptibly();
E result;
try {
            while ( (result = extract()) == null)
notEmpty.await();
} finally {
lock.unlock();
//Synthetic comment -- @@ -517,7 +515,7 @@
lock.lockInterruptibly();
E result;
try {
            while ( (result = extract()) == null && nanos > 0)
nanos = notEmpty.awaitNanos(nanos);
} finally {
lock.unlock();
//Synthetic comment -- @@ -528,13 +526,11 @@
public E peek() {
final ReentrantLock lock = this.lock;
lock.lock();
        E result;
try {
            result = size > 0 ? (E) queue[0] : null;
} finally {
lock.unlock();
}
        return result;
}

/**
//Synthetic comment -- @@ -618,32 +614,28 @@
* @return {@code true} if this queue changed as a result of the call
*/
public boolean remove(Object o) {
        boolean removed = false;
final ReentrantLock lock = this.lock;
lock.lock();
try {
int i = indexOf(o);
            if (i != -1) {
                removeAt(i);
                removed = true;
            }
} finally {
lock.unlock();
}
        return removed;
}


/**
* Identity-based version for use in Itr.remove
*/
    private void removeEQ(Object o) {
final ReentrantLock lock = this.lock;
lock.lock();
try {
Object[] array = queue;
            int n = size;
            for (int i = 0; i < n; i++) {
if (o == array[i]) {
removeAt(i);
break;
//Synthetic comment -- @@ -663,15 +655,13 @@
* @return {@code true} if this queue contains the specified element
*/
public boolean contains(Object o) {
        int index;
final ReentrantLock lock = this.lock;
lock.lock();
try {
            index = indexOf(o);
} finally {
lock.unlock();
}
        return index != -1;
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
                E e = (E)queue[i];
sb.append(e == this ? "(this Collection)" : e);
if (i != n - 1)
sb.append(',').append(' ');
//Synthetic comment -- @@ -726,23 +715,7 @@
* @throws IllegalArgumentException      {@inheritDoc}
*/
public int drainTo(Collection<? super E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            E e;
            while ( (e = extract()) != null) {
                c.add(e);
                ++n;
            }
            return n;
        } finally {
            lock.unlock();
        }
}

/**
//Synthetic comment -- @@ -761,11 +734,10 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
            int n = 0;
            E e;
            while (n < maxElements && (e = extract()) != null) {
                c.add(e);
                ++n;
}
return n;
} finally {
//Synthetic comment -- @@ -813,8 +785,7 @@
* The following code can be used to dump the queue into a newly
* allocated array of {@code String}:
*
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
*
* Note that {@code toArray(new Object[0])} is identical in function to
* {@code toArray()}.
//Synthetic comment -- @@ -867,7 +838,7 @@
*/
final class Itr implements Iterator<E> {
final Object[] array; // Array of all elements
        int cursor;           // index of next element to return;
int lastRet;          // index of last element, or -1 if no such

Itr(Object[] array) {
//Synthetic comment -- @@ -904,8 +875,8 @@
throws java.io.IOException {
lock.lock();
try {
            int n = size; // avoid zero capacity argument
            q = new PriorityQueue<E>(n == 0 ? 1 : n, comparator);
q.addAll(this);
s.defaultWriteObject();
} finally {
//Synthetic comment -- @@ -933,21 +904,16 @@
}

// Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
    private static final long allocationSpinLockOffset =
        objectFieldOffset(UNSAFE, "allocationSpinLock",
                          PriorityBlockingQueue.class);

    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
}
}

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RecursiveAction.java b/luni/src/main/java/java/util/concurrent/RecursiveAction.java
new file mode 100644
//Synthetic comment -- index 0000000..48066c9

//Synthetic comment -- @@ -0,0 +1,165 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RecursiveTask.java b/luni/src/main/java/java/util/concurrent/RecursiveTask.java
new file mode 100644
//Synthetic comment -- index 0000000..5e17280

//Synthetic comment -- @@ -0,0 +1,69 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RejectedExecutionException.java b/luni/src/main/java/java/util/concurrent/RejectedExecutionException.java
//Synthetic comment -- index 30b043d..f0005d1 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -49,8 +49,8 @@

/**
* Constructs a <tt>RejectedExecutionException</tt> with the
     * specified cause.  The detail message is set to: <pre> (cause ==
     * null ? null : cause.toString())</pre> (which typically contains
* the class and detail message of <tt>cause</tt>).
*
* @param  cause the cause (which is saved for later retrieval by the








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RejectedExecutionHandler.java b/luni/src/main/java/java/util/concurrent/RejectedExecutionHandler.java
//Synthetic comment -- index 417a27c..8c000ea 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RunnableFuture.java b/luni/src/main/java/java/util/concurrent/RunnableFuture.java
//Synthetic comment -- index d74211d..2d6d52c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/RunnableScheduledFuture.java b/luni/src/main/java/java/util/concurrent/RunnableScheduledFuture.java
//Synthetic comment -- index 0e8cc32..fbb995c 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledExecutorService.java b/luni/src/main/java/java/util/concurrent/ScheduledExecutorService.java
//Synthetic comment -- index 6cb4e27..71e57ed 100644

//Synthetic comment -- @@ -1,12 +1,10 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
* An {@link ExecutorService} that can schedule commands to run after a given








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledFuture.java b/luni/src/main/java/java/util/concurrent/ScheduledFuture.java
//Synthetic comment -- index 239d681..3745cb0 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ScheduledThreadPoolExecutor.java b/luni/src/main/java/java/util/concurrent/ScheduledThreadPoolExecutor.java
//Synthetic comment -- index c2eaedf..e41f0c3 100644

//Synthetic comment -- @@ -1,16 +1,19 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.*;

// BEGIN android-note
// Omit class-level docs on setRemoveOnCancelPolicy()
// END android-note

/**
//Synthetic comment -- @@ -138,7 +141,7 @@
* Sequence number to break scheduling ties, and in turn to
* guarantee FIFO order among tied entries.
*/
    private static final AtomicLong sequencer = new AtomicLong(0);

/**
* Returns current nanosecond time.
//Synthetic comment -- @@ -203,11 +206,11 @@
}

public long getDelay(TimeUnit unit) {
            return unit.convert(time - now(), TimeUnit.NANOSECONDS);
}

public int compareTo(Delayed other) {
            if (other == this) // compare zero ONLY if same object
return 0;
if (other instanceof ScheduledFutureTask) {
ScheduledFutureTask<?> x = (ScheduledFutureTask<?>)other;
//Synthetic comment -- @@ -221,9 +224,9 @@
else
return 1;
}
            long d = (getDelay(TimeUnit.NANOSECONDS) -
                      other.getDelay(TimeUnit.NANOSECONDS));
            return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
}

/**
//Synthetic comment -- @@ -302,7 +305,7 @@
remove(task))
task.cancel(false);
else
                prestartCoreThread();
}
}

//Synthetic comment -- @@ -318,7 +321,7 @@
if (!canRunInCurrentRunState(true) && remove(task))
task.cancel(false);
else
                prestartCoreThread();
}
}

//Synthetic comment -- @@ -396,7 +399,7 @@
* @throws IllegalArgumentException if {@code corePoolSize < 0}
*/
public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
new DelayedWorkQueue());
}

//Synthetic comment -- @@ -413,7 +416,7 @@
*/
public ScheduledThreadPoolExecutor(int corePoolSize,
ThreadFactory threadFactory) {
        super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
new DelayedWorkQueue(), threadFactory);
}

//Synthetic comment -- @@ -430,7 +433,7 @@
*/
public ScheduledThreadPoolExecutor(int corePoolSize,
RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
new DelayedWorkQueue(), handler);
}

//Synthetic comment -- @@ -451,7 +454,7 @@
public ScheduledThreadPoolExecutor(int corePoolSize,
ThreadFactory threadFactory,
RejectedExecutionHandler handler) {
        super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
new DelayedWorkQueue(), threadFactory, handler);
}

//Synthetic comment -- @@ -480,7 +483,7 @@
private long overflowFree(long delay) {
Delayed head = (Delayed) super.getQueue().peek();
if (head != null) {
            long headDelay = head.getDelay(TimeUnit.NANOSECONDS);
if (headDelay < 0 && (delay - headDelay < 0))
delay = Long.MAX_VALUE + headDelay;
}
//Synthetic comment -- @@ -588,7 +591,7 @@
* @throws NullPointerException {@inheritDoc}
*/
public void execute(Runnable command) {
        schedule(command, 0, TimeUnit.NANOSECONDS);
}

// Override AbstractExecutorService methods
//Synthetic comment -- @@ -598,7 +601,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public Future<?> submit(Runnable task) {
        return schedule(task, 0, TimeUnit.NANOSECONDS);
}

/**
//Synthetic comment -- @@ -606,8 +609,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public <T> Future<T> submit(Runnable task, T result) {
        return schedule(Executors.callable(task, result),
                        0, TimeUnit.NANOSECONDS);
}

/**
//Synthetic comment -- @@ -615,7 +617,7 @@
* @throws NullPointerException       {@inheritDoc}
*/
public <T> Future<T> submit(Callable<T> task) {
        return schedule(task, 0, TimeUnit.NANOSECONDS);
}

/**
//Synthetic comment -- @@ -690,8 +692,9 @@
* @param value if {@code true}, remove on cancellation, else don't
* @see #getRemoveOnCancelPolicy
* @since 1.7
*/
    /*public*/ void setRemoveOnCancelPolicy(boolean value) { // android-changed
removeOnCancel = value;
}

//Synthetic comment -- @@ -704,8 +707,9 @@
*         from the queue
* @see #setRemoveOnCancelPolicy
* @since 1.7
*/
    /*public*/ boolean getRemoveOnCancelPolicy() { // android-changed
return removeOnCancel;
}

//Synthetic comment -- @@ -724,8 +728,6 @@
* ContinueExistingPeriodicTasksAfterShutdownPolicy} has been set
* {@code true}, future executions of existing periodic tasks will
* be cancelled.
     *
     * @throws SecurityException {@inheritDoc}
*/
public void shutdown() {
super.shutdown();
//Synthetic comment -- @@ -750,7 +752,6 @@
*         including those tasks submitted using {@code execute},
*         which are for scheduling purposes used as the basis of a
*         zero-delay {@code ScheduledFuture}.
     * @throws SecurityException {@inheritDoc}
*/
public List<Runnable> shutdownNow() {
return super.shutdownNow();
//Synthetic comment -- @@ -803,8 +804,8 @@
*/

private static final int INITIAL_CAPACITY = 16;
        private RunnableScheduledFuture[] queue =
            new RunnableScheduledFuture[INITIAL_CAPACITY];
private final ReentrantLock lock = new ReentrantLock();
private int size = 0;

//Synthetic comment -- @@ -835,7 +836,7 @@
/**
* Set f's heapIndex if it is a ScheduledFutureTask.
*/
        private void setIndex(RunnableScheduledFuture f, int idx) {
if (f instanceof ScheduledFutureTask)
((ScheduledFutureTask)f).heapIndex = idx;
}
//Synthetic comment -- @@ -844,10 +845,10 @@
* Sift element added at bottom up to its heap-ordered spot.
* Call only when holding lock.
*/
        private void siftUp(int k, RunnableScheduledFuture key) {
while (k > 0) {
int parent = (k - 1) >>> 1;
                RunnableScheduledFuture e = queue[parent];
if (key.compareTo(e) >= 0)
break;
queue[k] = e;
//Synthetic comment -- @@ -862,11 +863,11 @@
* Sift element added at top down to its heap-ordered spot.
* Call only when holding lock.
*/
        private void siftDown(int k, RunnableScheduledFuture key) {
int half = size >>> 1;
while (k < half) {
int child = (k << 1) + 1;
                RunnableScheduledFuture c = queue[child];
int right = child + 1;
if (right < size && c.compareTo(queue[right]) > 0)
c = queue[child = right];
//Synthetic comment -- @@ -931,7 +932,7 @@

setIndex(queue[i], -1);
int s = --size;
                RunnableScheduledFuture replacement = queue[s];
queue[s] = null;
if (s != i) {
siftDown(i, replacement);
//Synthetic comment -- @@ -962,7 +963,7 @@
return Integer.MAX_VALUE;
}

        public RunnableScheduledFuture peek() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -975,7 +976,7 @@
public boolean offer(Runnable x) {
if (x == null)
throw new NullPointerException();
            RunnableScheduledFuture e = (RunnableScheduledFuture)x;
final ReentrantLock lock = this.lock;
lock.lock();
try {
//Synthetic comment -- @@ -1017,9 +1018,9 @@
* holding lock.
* @param f the task to remove and return
*/
        private RunnableScheduledFuture finishPoll(RunnableScheduledFuture f) {
int s = --size;
            RunnableScheduledFuture x = queue[s];
queue[s] = null;
if (s != 0)
siftDown(0, x);
//Synthetic comment -- @@ -1027,12 +1028,12 @@
return f;
}

        public RunnableScheduledFuture poll() {
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture first = queue[0];
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
return null;
else
return finishPoll(first);
//Synthetic comment -- @@ -1041,16 +1042,16 @@
}
}

        public RunnableScheduledFuture take() throws InterruptedException {
final ReentrantLock lock = this.lock;
lock.lockInterruptibly();
try {
for (;;) {
                    RunnableScheduledFuture first = queue[0];
if (first == null)
available.await();
else {
                        long delay = first.getDelay(TimeUnit.NANOSECONDS);
if (delay <= 0)
return finishPoll(first);
else if (leader != null)
//Synthetic comment -- @@ -1074,21 +1075,21 @@
}
}

        public RunnableScheduledFuture poll(long timeout, TimeUnit unit)
throws InterruptedException {
long nanos = unit.toNanos(timeout);
final ReentrantLock lock = this.lock;
lock.lockInterruptibly();
try {
for (;;) {
                    RunnableScheduledFuture first = queue[0];
if (first == null) {
if (nanos <= 0)
return null;
else
nanos = available.awaitNanos(nanos);
} else {
                        long delay = first.getDelay(TimeUnit.NANOSECONDS);
if (delay <= 0)
return finishPoll(first);
if (nanos <= 0)
//Synthetic comment -- @@ -1120,7 +1121,7 @@
lock.lock();
try {
for (int i = 0; i < size; i++) {
                    RunnableScheduledFuture t = queue[i];
if (t != null) {
queue[i] = null;
setIndex(t, -1);
//Synthetic comment -- @@ -1133,14 +1134,14 @@
}

/**
         * Return and remove first element only if it is expired.
* Used only by drainTo.  Call only when holding lock.
*/
        private RunnableScheduledFuture pollExpired() {
            RunnableScheduledFuture first = queue[0];
            if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0)
                return null;
            return finishPoll(first);
}

public int drainTo(Collection<? super Runnable> c) {
//Synthetic comment -- @@ -1151,10 +1152,11 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture first;
int n = 0;
                while ((first = pollExpired()) != null) {
                    c.add(first);
++n;
}
return n;
//Synthetic comment -- @@ -1173,10 +1175,11 @@
final ReentrantLock lock = this.lock;
lock.lock();
try {
                RunnableScheduledFuture first;
int n = 0;
                while (n < maxElements && (first = pollExpired()) != null) {
                    c.add(first);
++n;
}
return n;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/Semaphore.java b/luni/src/main/java/java/util/concurrent/Semaphore.java
//Synthetic comment -- index 62dea1a..bf2524c 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

/**
* A counting semaphore.  Conceptually, a semaphore maintains a set of
//Synthetic comment -- @@ -20,7 +19,7 @@
* <p>Semaphores are often used to restrict the number of threads than can
* access some (physical or logical) resource. For example, here is
* a class that uses a semaphore to control access to a pool of items:
 * <pre>
* class Pool {
*   private static final int MAX_AVAILABLE = 100;
*   private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
//Synthetic comment -- @@ -62,9 +61,7 @@
*     }
*     return false;
*   }
 *
 * }
 * </pre>
*
* <p>Before obtaining an item each thread must acquire a permit from
* the semaphore, guaranteeing that an item is available for use. When








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/SynchronousQueue.java b/luni/src/main/java/java/util/concurrent/SynchronousQueue.java
//Synthetic comment -- index 51d40c0..b05ae0a 100644

//Synthetic comment -- @@ -2,13 +2,12 @@
* Written by Doug Lea, Bill Scherer, and Michael Scott with
* assistance from members of JCP JSR-166 Expert Group and released to
* the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
import java.util.concurrent.locks.*;
import java.util.*;
import libcore.util.EmptyArray;

// BEGIN android-note
// removed link to collections framework docs
//Synthetic comment -- @@ -250,12 +249,22 @@
}

// Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
            private static final long nextOffset =
                objectFieldOffset(UNSAFE, "next", SNode.class);
            private static final long matchOffset =
                objectFieldOffset(UNSAFE, "match", SNode.class);

}

/** The head (top) of the stack */
//Synthetic comment -- @@ -393,7 +402,6 @@
*/
long lastTime = timed ? System.nanoTime() : 0;
Thread w = Thread.currentThread();
            SNode h = head;
int spins = (shouldSpin(s) ?
(timed ? maxTimedSpins : maxUntimedSpins) : 0);
for (;;) {
//Synthetic comment -- @@ -469,10 +477,18 @@
}

// Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
        private static final long headOffset =
            objectFieldOffset(UNSAFE, "head", TransferStack.class);

}

/** Dual Queue */
//Synthetic comment -- @@ -529,11 +545,22 @@
}

// Unsafe mechanics
            private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
            private static final long nextOffset =
                objectFieldOffset(UNSAFE, "next", QNode.class);
            private static final long itemOffset =
                objectFieldOffset(UNSAFE, "item", QNode.class);
}

/** Head of queue */
//Synthetic comment -- @@ -762,15 +789,24 @@
}
}

        // unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE = sun.misc.Unsafe.getUnsafe();
        private static final long headOffset =
            objectFieldOffset(UNSAFE, "head", TransferQueue.class);
        private static final long tailOffset =
            objectFieldOffset(UNSAFE, "tail", TransferQueue.class);
        private static final long cleanMeOffset =
            objectFieldOffset(UNSAFE, "cleanMe", TransferQueue.class);

}

/**
//Synthetic comment -- @@ -998,8 +1034,19 @@
*
* @return an empty iterator
*/
public Iterator<E> iterator() {
        return Collections.<E>emptySet().iterator(); // android-changed
}

/**
//Synthetic comment -- @@ -1007,7 +1054,7 @@
* @return a zero-length array
*/
public Object[] toArray() {
        return EmptyArray.OBJECT; // android-changed
}

/**
//Synthetic comment -- @@ -1036,8 +1083,7 @@
if (c == this)
throw new IllegalArgumentException();
int n = 0;
        E e;
        while ( (e = poll()) != null) {
c.add(e);
++n;
}
//Synthetic comment -- @@ -1056,8 +1102,7 @@
if (c == this)
throw new IllegalArgumentException();
int n = 0;
        E e;
        while (n < maxElements && (e = poll()) != null) {
c.add(e);
++n;
}
//Synthetic comment -- @@ -1084,7 +1129,7 @@
private WaitQueue waitingConsumers;

/**
     * Save the state to a stream (that is, serialize it).
*
* @param s the stream
*/








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadFactory.java b/luni/src/main/java/java/util/concurrent/ThreadFactory.java
//Synthetic comment -- index 2f0fb1a..d1a4eb6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -13,13 +13,12 @@
*
* <p>
* The simplest implementation of this interface is just:
 * <pre>
* class SimpleThreadFactory implements ThreadFactory {
*   public Thread newThread(Runnable r) {
*     return new Thread(r);
*   }
 * }
 * </pre>
*
* The {@link Executors#defaultThreadFactory} method provides a more
* useful simple implementation, that sets the created thread context








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadLocalRandom.java b/luni/src/main/java/java/util/concurrent/ThreadLocalRandom.java
new file mode 100644
//Synthetic comment -- index 0000000..a559321

//Synthetic comment -- @@ -0,0 +1,198 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/ThreadPoolExecutor.java b/luni/src/main/java/java/util/concurrent/ThreadPoolExecutor.java
//Synthetic comment -- index 6622af8..331e225 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -9,6 +9,10 @@
import java.util.concurrent.atomic.*;
import java.util.*;

/**
* An {@link ExecutorService} that executes each submitted task using
* one of possibly several pooled threads, normally configured
//Synthetic comment -- @@ -1311,8 +1315,6 @@
* <p>This method does not wait for previously submitted tasks to
* complete execution.  Use {@link #awaitTermination awaitTermination}
* to do that.
     *
     * @throws SecurityException {@inheritDoc}
*/
public void shutdown() {
final ReentrantLock mainLock = this.mainLock;
//Synthetic comment -- @@ -1342,8 +1344,6 @@
* processing actively executing tasks.  This implementation
* cancels tasks via {@link Thread#interrupt}, so any task that
* fails to respond to interrupts may never terminate.
     *
     * @throws SecurityException {@inheritDoc}
*/
public List<Runnable> shutdownNow() {
List<Runnable> tasks;
//Synthetic comment -- @@ -1512,6 +1512,18 @@
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
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;
//Synthetic comment -- @@ -23,14 +23,14 @@
* the following code will timeout in 50 milliseconds if the {@link
* java.util.concurrent.locks.Lock lock} is not available:
*
 * <pre>  Lock lock = ...;
 *  if (lock.tryLock(50L, TimeUnit.MILLISECONDS)) ...
 * </pre>
* while this code will timeout in 50 seconds:
 * <pre>
 *  Lock lock = ...;
 *  if (lock.tryLock(50L, TimeUnit.SECONDS)) ...
 * </pre>
*
* Note however, that there is no guarantee that a particular timeout
* implementation will be able to notice the passage of time at the








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/TimeoutException.java b/luni/src/main/java/java/util/concurrent/TimeoutException.java
//Synthetic comment -- index 8b84f28..83934f0 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/TransferQueue.java b/luni/src/main/java/java/util/concurrent/TransferQueue.java
new file mode 100644
//Synthetic comment -- index 0000000..9cd5773

//Synthetic comment -- @@ -0,0 +1,133 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicBoolean.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicBoolean.java
//Synthetic comment -- index c774d21..d531f25 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -21,14 +21,14 @@
public class AtomicBoolean implements java.io.Serializable {
private static final long serialVersionUID = 4654671469794556979L;
// setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
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
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -24,14 +24,14 @@
private static final long serialVersionUID = 6214790243416807050L;

// setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
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


public int intValue() {
return get();
}

public long longValue() {
return (long)get();
}

public float floatValue() {
return (float)get();
}

public double doubleValue() {
return (double)get();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerArray.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerArray.java
//Synthetic comment -- index 6dcdfd0..804a51e 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
import sun.misc.Unsafe;
import java.util.*;

/**
* An {@code int} array in which elements may be updated atomically.
//Synthetic comment -- @@ -19,7 +18,7 @@
public class AtomicIntegerArray implements java.io.Serializable {
private static final long serialVersionUID = 2862133569453604235L;

    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private static final int base = unsafe.arrayBaseOffset(int[].class);
private static final int shift;
private final int[] array;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicIntegerFieldUpdater.java
//Synthetic comment -- index e8a0d57..c7ed158 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -236,24 +236,21 @@
* Standard hotspot implementation using intrinsics
*/
private static class AtomicIntegerFieldUpdaterImpl<T> extends AtomicIntegerFieldUpdater<T> {
        private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private final long offset;
private final Class<T> tclass;
        private final Class cclass;

AtomicIntegerFieldUpdaterImpl(Class<T> tclass, String fieldName) {
Field field = null;
            Class caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
                // BEGIN android-changed
                caller = VMStack.getStackClass2();
                // END android-changed
modifiers = field.getModifiers();

// BEGIN android-removed
                // modifiers = field.getModifiers();
// sun.reflect.misc.ReflectUtil.ensureMemberAccess(
//     caller, tclass, null, modifiers);
// sun.reflect.misc.ReflectUtil.checkPackageAccess(tclass);
//Synthetic comment -- @@ -262,7 +259,7 @@
throw new RuntimeException(ex);
}

            Class fieldt = field.getType();
if (fieldt != int.class)
throw new IllegalArgumentException("Must be integer type");









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLong.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLong.java
//Synthetic comment -- index 12d6cd1..5e799f7 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -24,7 +24,7 @@
private static final long serialVersionUID = 1927816293512124184L;

// setup to use Unsafe.compareAndSwapLong for updates
    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
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


public int intValue() {
return (int)get();
}

public long longValue() {
return get();
}

public float floatValue() {
return (float)get();
}

public double doubleValue() {
return (double)get();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLongArray.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLongArray.java
//Synthetic comment -- index 9e2d25f..22edb3f 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
import sun.misc.Unsafe;
import java.util.*;

/**
* A {@code long} array in which elements may be updated atomically.
//Synthetic comment -- @@ -18,7 +17,7 @@
public class AtomicLongArray implements java.io.Serializable {
private static final long serialVersionUID = -2308431214976778248L;

    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private static final int base = unsafe.arrayBaseOffset(long[].class);
private static final int shift;
private final long[] array;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicLongFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicLongFieldUpdater.java
//Synthetic comment -- index 21ef748..748ae69 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -235,14 +235,14 @@
}

private static class CASUpdater<T> extends AtomicLongFieldUpdater<T> {
        private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private final long offset;
private final Class<T> tclass;
        private final Class cclass;

CASUpdater(Class<T> tclass, String fieldName) {
Field field = null;
            Class caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
//Synthetic comment -- @@ -257,7 +257,7 @@
throw new RuntimeException(ex);
}

            Class fieldt = field.getType();
if (fieldt != long.class)
throw new IllegalArgumentException("Must be long type");

//Synthetic comment -- @@ -320,14 +320,14 @@


private static class LockedUpdater<T> extends AtomicLongFieldUpdater<T> {
        private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private final long offset;
private final Class<T> tclass;
        private final Class cclass;

LockedUpdater(Class<T> tclass, String fieldName) {
Field field = null;
            Class caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);
//Synthetic comment -- @@ -342,7 +342,7 @@
throw new RuntimeException(ex);
}

            Class fieldt = field.getType();
if (fieldt != long.class)
throw new IllegalArgumentException("Must be long type");









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicMarkableReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicMarkableReference.java
//Synthetic comment -- index 63f46d6f..eaf700c 100644

//Synthetic comment -- @@ -1,13 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;

import sun.misc.Unsafe;

/**
* An {@code AtomicMarkableReference} maintains an object reference
* along with a mark bit, that can be updated atomically.
//Synthetic comment -- @@ -163,7 +161,7 @@

// Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE = UnsafeAccess.THE_ONE; // android-changed
private static final long pairOffset =
objectFieldOffset(UNSAFE, "pair", AtomicMarkableReference.class);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicReference.java
//Synthetic comment -- index f041bbd..b21e9b6 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -18,14 +18,14 @@
public class AtomicReference<V>  implements java.io.Serializable {
private static final long serialVersionUID = -1848883965231344442L;

    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
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
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
import sun.misc.Unsafe;
import java.util.*;

/**
* An array of object references in which elements may be updated
//Synthetic comment -- @@ -20,13 +22,23 @@
public class AtomicReferenceArray<E> implements java.io.Serializable {
private static final long serialVersionUID = -6209656149925076980L;

    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
    private static final int base = unsafe.arrayBaseOffset(Object[].class);
private static final int shift;
    private final Object[] array;

static {
        int scale = unsafe.arrayIndexScale(Object[].class);
if ((scale & (scale - 1)) != 0)
throw new Error("data type scale not a power of two");
shift = 31 - Integer.numberOfLeadingZeros(scale);
//Synthetic comment -- @@ -45,7 +57,7 @@

/**
* Creates a new AtomicReferenceArray of the given length, with all
     * elements initially zero.
*
* @param length the length of the array
*/
//Synthetic comment -- @@ -62,7 +74,7 @@
*/
public AtomicReferenceArray(E[] array) {
// Visibility guaranteed by final field guarantees
        this.array = array.clone();
}

/**
//Synthetic comment -- @@ -121,7 +133,7 @@
public final E getAndSet(int i, E newValue) {
long offset = checkedByteOffset(i);
while (true) {
            E current = (E) getRaw(offset);
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

}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceFieldUpdater.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicReferenceFieldUpdater.java
//Synthetic comment -- index 8b3da0b..d23d766 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
import dalvik.system.VMStack;
import sun.misc.Unsafe;
import java.lang.reflect.*;

//Synthetic comment -- @@ -155,7 +155,7 @@
private final long offset;
private final Class<T> tclass;
private final Class<V> vclass;
        private final Class cclass;

/*
* Internal type checks within all update methods contain
//Synthetic comment -- @@ -173,8 +173,8 @@
Class<V> vclass,
String fieldName) {
Field field = null;
            Class fieldClass = null;
            Class caller = null;
int modifiers = 0;
try {
field = tclass.getDeclaredField(fieldName);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/AtomicStampedReference.java b/luni/src/main/java/java/util/concurrent/atomic/AtomicStampedReference.java
//Synthetic comment -- index 2e826f2..a0cb492 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.atomic;
//Synthetic comment -- @@ -162,7 +162,7 @@

// Unsafe mechanics

    private static final sun.misc.Unsafe UNSAFE = UnsafeAccess.THE_ONE; // android-changed
private static final long pairOffset =
objectFieldOffset(UNSAFE, "pair", AtomicStampedReference.class);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/Fences.java b/luni/src/main/java/java/util/concurrent/atomic/Fences.java
new file mode 100644
//Synthetic comment -- index 0000000..7ecf45a

//Synthetic comment -- @@ -0,0 +1,540 @@








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/UnsafeAccess.java b/luni/src/main/java/java/util/concurrent/atomic/UnsafeAccess.java
deleted file mode 100644
//Synthetic comment -- index 96fff17..0000000

//Synthetic comment -- @@ -1,34 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.util.concurrent.atomic;

import sun.misc.Unsafe;

/**
 * Easy access to {@link Unsafe} for the rest of this package.
 */
/*package*/ final class UnsafeAccess {
    /** non-null; unique instance of {@link Unsafe} */
    /*package*/ static final Unsafe THE_ONE = Unsafe.getUnsafe();

    /**
     * This class is uninstantiable.
     */
    private UnsafeAccess() {
        // This space intentionally left blank.
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/atomic/package-info.java b/luni/src/main/java/java/util/concurrent/atomic/package-info.java
//Synthetic comment -- index 4a4375d..efbb413 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

/**
//Synthetic comment -- @@ -11,9 +11,7 @@
* array elements to those that also provide an atomic conditional update
* operation of the form:
*
 * <pre>
 *   boolean compareAndSet(expectedValue, updateValue);
 * </pre>
*
* <p>This method (which varies in argument types across different
* classes) atomically sets a variable to the {@code updateValue} if it
//Synthetic comment -- @@ -40,15 +38,30 @@
* {@code AtomicInteger} provide atomic increment methods.  One
* application is to generate sequence numbers, as in:
*
 * <pre>
* class Sequencer {
*   private final AtomicLong sequenceNumber
*     = new AtomicLong(0);
*   public long next() {
*     return sequenceNumber.getAndIncrement();
*   }
 * }
 * </pre>
*
* <p>The memory effects for accesses and updates of atomics generally
* follow the rules for volatiles, as stated in
//Synthetic comment -- @@ -161,9 +174,9 @@
* {@code byte} values, and cast appropriately.
*
* You can also hold floats using
 * {@link java.lang.Float#floatToIntBits} and
* {@link java.lang.Float#intBitsToFloat} conversions, and doubles using
 * {@link java.lang.Double#doubleToLongBits} and
* {@link java.lang.Double#longBitsToDouble} conversions.
*
* @since 1.5








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractOwnableSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractOwnableSynchronizer.java
//Synthetic comment -- index f3780e5..4bec0cf 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedLongSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedLongSynchronizer.java
//Synthetic comment -- index 5c8111c..7b36460 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import sun.misc.Unsafe;

/**
//Synthetic comment -- @@ -569,7 +568,7 @@
/**
* Convenience method to interrupt current thread.
*/
    private static void selfInterrupt() {
Thread.currentThread().interrupt();
}

//Synthetic comment -- @@ -1231,7 +1230,7 @@
* due to the queue being empty.
*
* <p>This method is designed to be used by a fair synchronizer to
     * avoid <a href="AbstractQueuedSynchronizer#barging">barging</a>.
* Such a synchronizer's {@link #tryAcquire} method should return
* {@code false}, and its {@link #tryAcquireShared} method should
* return a negative value, if this method returns {@code true}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedSynchronizer.java b/luni/src/main/java/java/util/concurrent/locks/AbstractQueuedSynchronizer.java
//Synthetic comment -- index 065f130..42029f0 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import sun.misc.Unsafe;

// BEGIN android-note
//Synthetic comment -- @@ -173,7 +172,7 @@
* It also supports conditions and exposes
* one of the instrumentation methods:
*
 * <pre>
* class Mutex implements Lock, java.io.Serializable {
*
*   // Our internal helper class
//Synthetic comment -- @@ -229,15 +228,14 @@
*       throws InterruptedException {
*     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
*   }
 * }
 * </pre>
*
* <p>Here is a latch class that is like a {@link CountDownLatch}
* except that it only requires a single <tt>signal</tt> to
* fire. Because a latch is non-exclusive, it uses the <tt>shared</tt>
* acquire and release methods.
*
 * <pre>
* class BooleanLatch {
*
*   private static class Sync extends AbstractQueuedSynchronizer {
//Synthetic comment -- @@ -259,8 +257,7 @@
*   public void await() throws InterruptedException {
*     sync.acquireSharedInterruptibly(1);
*   }
 * }
 * </pre>
*
* @since 1.5
* @author Doug Lea
//Synthetic comment -- @@ -800,7 +797,7 @@
/**
* Convenience method to interrupt current thread.
*/
    private static void selfInterrupt() {
Thread.currentThread().interrupt();
}

//Synthetic comment -- @@ -2251,9 +2248,7 @@
* are at it, we do the same for other CASable fields (which could
* otherwise be done with atomic field updaters).
*/
    // BEGIN android-changed
    private static final Unsafe unsafe = UnsafeAccess.THE_ONE;
    // END android-changed
private static final long stateOffset;
private static final long headOffset;
private static final long tailOffset;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/Condition.java b/luni/src/main/java/java/util/concurrent/locks/Condition.java
//Synthetic comment -- index 8504e1f..7050df9 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
//Synthetic comment -- @@ -331,10 +331,9 @@
/**
* Causes the current thread to wait until it is signalled or interrupted,
* or the specified waiting time elapses. This method is behaviorally
     * equivalent to:<br>
     * <pre>
     *   awaitNanos(unit.toNanos(time)) &gt; 0
     * </pre>
* @param time the maximum time to wait
* @param unit the time unit of the {@code time} argument
* @return {@code false} if the waiting time detectably elapsed








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/Lock.java b/luni/src/main/java/java/util/concurrent/locks/Lock.java
//Synthetic comment -- index 4b9abd6..d5c6294 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
//Synthetic comment -- @@ -48,14 +48,14 @@
* methods and statements. In most cases, the following idiom
* should be used:
*
 * <pre><tt>     Lock l = ...;
 *     l.lock();
 *     try {
 *         // access the resource protected by this lock
 *     } finally {
 *         l.unlock();
 *     }
 * </tt></pre>
*
* When locking and unlocking occur in different scopes, care must be
* taken to ensure that all code that is executed while the lock is
//Synthetic comment -- @@ -210,18 +210,18 @@
* immediately with the value {@code false}.
*
* <p>A typical usage idiom for this method would be:
     * <pre>
     *      Lock lock = ...;
     *      if (lock.tryLock()) {
     *          try {
     *              // manipulate protected state
     *          } finally {
     *              lock.unlock();
     *          }
     *      } else {
     *          // perform alternative actions
     *      }
     * </pre>
* This usage ensures that the lock is unlocked if it was acquired, and
* doesn't try to unlock if the lock was not acquired.
*








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/LockSupport.java b/luni/src/main/java/java/util/concurrent/locks/LockSupport.java
//Synthetic comment -- index 0c0f07d..422e428 100644

//Synthetic comment -- @@ -1,11 +1,10 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
import java.util.concurrent.*;
import sun.misc.Unsafe;


//Synthetic comment -- @@ -49,7 +48,10 @@
* higher-level synchronization utilities, and are not in themselves
* useful for most concurrency control applications.  The {@code park}
* method is designed for use only in constructions of the form:
 * <pre>while (!canProceed()) { ... LockSupport.park(this); }</pre>
* where neither {@code canProceed} nor any other actions prior to the
* call to {@code park} entail locking or blocking.  Because only one
* permit is associated with each thread, any intermediary uses of
//Synthetic comment -- @@ -57,7 +59,7 @@
*
* <p><b>Sample Usage.</b> Here is a sketch of a first-in-first-out
* non-reentrant lock class:
 * <pre>{@code
* class FIFOMutex {
*   private final AtomicBoolean locked = new AtomicBoolean(false);
*   private final Queue<Thread> waiters
//Synthetic comment -- @@ -92,7 +94,7 @@
private LockSupport() {} // Cannot be instantiated.

// Hotspot implementation via intrinsics API
    private static final Unsafe unsafe = UnsafeAccess.THE_ONE; // android-changed
private static final long parkBlockerOffset;

static {
//Synthetic comment -- @@ -246,10 +248,14 @@
* snapshot -- the thread may have since unblocked or blocked on a
* different blocker object.
*
* @return the blocker
* @since 1.6
*/
public static Object getBlocker(Thread t) {
return unsafe.getObjectVolatile(t, parkBlockerOffset);
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReadWriteLock.java b/luni/src/main/java/java/util/concurrent/locks/ReadWriteLock.java
//Synthetic comment -- index 484f68d..bb7b388 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReentrantLock.java b/luni/src/main/java/java/util/concurrent/locks/ReentrantLock.java
//Synthetic comment -- index cf787ca..07baf41 100644

//Synthetic comment -- @@ -1,13 +1,12 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
* A reentrant mutual exclusion {@link Lock} with the same basic
//Synthetic comment -- @@ -44,7 +43,7 @@
* follow a call to {@code lock} with a {@code try} block, most
* typically in a before/after construction such as:
*
 * <pre>
* class X {
*   private final ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -57,8 +56,7 @@
*       lock.unlock()
*     }
*   }
 * }
 * </pre>
*
* <p>In addition to implementing the {@link Lock} interface, this
* class defines methods {@code isLocked} and
//Synthetic comment -- @@ -354,8 +352,11 @@
* method. If you want a timed {@code tryLock} that does permit barging on
* a fair lock then combine the timed and un-timed forms together:
*
     * <pre>if (lock.tryLock() || lock.tryLock(timeout, unit) ) { ... }
     * </pre>
*
* <p>If the current thread
* already holds this lock then the hold count is incremented by one and
//Synthetic comment -- @@ -485,7 +486,7 @@
* not be entered with the lock already held then we can assert that
* fact:
*
     * <pre>
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -498,8 +499,7 @@
*       lock.unlock();
*     }
*   }
     * }
     * </pre>
*
* @return the number of holds on this lock by the current thread,
*         or zero if this lock is not held by the current thread
//Synthetic comment -- @@ -516,7 +516,7 @@
* testing. For example, a method that should only be called while
* a lock is held can assert that this is the case:
*
     * <pre>
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -525,13 +525,12 @@
*       assert lock.isHeldByCurrentThread();
*       // ... method body
*   }
     * }
     * </pre>
*
* <p>It can also be used to ensure that a reentrant lock is used
* in a non-reentrant manner, for example:
*
     * <pre>
* class X {
*   ReentrantLock lock = new ReentrantLock();
*   // ...
//Synthetic comment -- @@ -545,8 +544,7 @@
*           lock.unlock();
*       }
*   }
     * }
     * </pre>
*
* @return {@code true} if current thread holds this lock and
*         {@code false} otherwise








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/ReentrantReadWriteLock.java b/luni/src/main/java/java/util/concurrent/locks/ReentrantReadWriteLock.java
//Synthetic comment -- index b1a1a60..244a4a7 100644

//Synthetic comment -- @@ -1,12 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

package java.util.concurrent.locks;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
//Synthetic comment -- @@ -33,7 +32,7 @@
* <dt><b><i>Fair mode</i></b>
* <dd> When constructed as fair, threads contend for entry using an
* approximately arrival-order policy. When the currently held lock
 * is released either the longest-waiting single writer thread will
* be assigned the write lock, or if there is a group of reader threads
* waiting longer than all waiting writer threads, that group will be
* assigned the read lock.
//Synthetic comment -- @@ -51,8 +50,8 @@
* will block unless both the read lock and write lock are free (which
* implies there are no waiting threads).  (Note that the non-blocking
* {@link ReadLock#tryLock()} and {@link WriteLock#tryLock()} methods
 * do not honor this fair setting and will acquire the lock if it is
 * possible, regardless of waiting threads.)
* <p>
* </dl>
*
//Synthetic comment -- @@ -114,21 +113,21 @@
*   void processCachedData() {
*     rwl.readLock().lock();
*     if (!cacheValid) {
 *        // Must release read lock before acquiring write lock
 *        rwl.readLock().unlock();
 *        rwl.writeLock().lock();
 *        try {
 *          // Recheck state because another thread might have
 *          // acquired write lock and changed state before we did.
 *          if (!cacheValid) {
 *            data = ...
 *            cacheValid = true;
 *          }
 *          // Downgrade by acquiring read lock before releasing write lock
 *          rwl.readLock().lock();
 *        } finally {
 *          rwl.writeLock().unlock(); // Unlock write, still hold read
 *        }
*     }
*
*     try {
//Synthetic comment -- @@ -147,33 +146,33 @@
* is a class using a TreeMap that is expected to be large and
* concurrently accessed.
*
 * <pre>{@code
* class RWDictionary {
 *    private final Map<String, Data> m = new TreeMap<String, Data>();
 *    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
 *    private final Lock r = rwl.readLock();
 *    private final Lock w = rwl.writeLock();
*
 *    public Data get(String key) {
 *        r.lock();
 *        try { return m.get(key); }
 *        finally { r.unlock(); }
 *    }
 *    public String[] allKeys() {
 *        r.lock();
 *        try { return m.keySet().toArray(); }
 *        finally { r.unlock(); }
 *    }
 *    public Data put(String key, Data value) {
 *        w.lock();
 *        try { return m.put(key, value); }
 *        finally { w.unlock(); }
 *    }
 *    public void clear() {
 *        w.lock();
 *        try { m.clear(); }
 *        finally { w.unlock(); }
 *    }
* }}</pre>
*
* <h3>Implementation Notes</h3>
//Synthetic comment -- @@ -625,7 +624,9 @@
}

/**
         * Reconstitute this lock instance from a stream
* @param s the stream
*/
private void readObject(java.io.ObjectInputStream s)
//Synthetic comment -- @@ -790,8 +791,11 @@
* permit barging on a fair lock then combine the timed and
* un-timed forms together:
*
         * <pre>if (lock.tryLock() || lock.tryLock(timeout, unit) ) { ... }
         * </pre>
*
* <p>If the write lock is held by another thread then the
* current thread becomes disabled for thread scheduling
//Synthetic comment -- @@ -1020,8 +1024,11 @@
* that does permit barging on a fair lock then combine the
* timed and un-timed forms together:
*
         * <pre>if (lock.tryLock() || lock.tryLock(timeout, unit) ) { ... }
         * </pre>
*
* <p>If the current thread already holds this lock then the
* hold count is incremented by one and the method returns








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/UnsafeAccess.java b/luni/src/main/java/java/util/concurrent/locks/UnsafeAccess.java
deleted file mode 100644
//Synthetic comment -- index 07f64e4..0000000

//Synthetic comment -- @@ -1,34 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.util.concurrent.locks;

import sun.misc.Unsafe;

/**
 * Easy access to {@link Unsafe} for the rest of this package.
 */
/*package*/ final class UnsafeAccess {
    /** non-null; unique instance of {@link Unsafe} */
    /*package*/ static final Unsafe THE_ONE = Unsafe.getUnsafe();

    /**
     * This class is uninstantiable.
     */
    private UnsafeAccess() {
        // This space intentionally left blank.
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/locks/package-info.java b/luni/src/main/java/java/util/concurrent/locks/package-info.java
//Synthetic comment -- index 860acdd..433f869 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/concurrent/package-info.java b/luni/src/main/java/java/util/concurrent/package-info.java
//Synthetic comment -- index 8509a41..259b845 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Written by Doug Lea with assistance from members of JCP JSR-166
* Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/licenses/publicdomain
*/

// BEGIN android-note
// Dropped references to unreleased APIs (ForkJoinPool, Phaser, etc.)
// END android-note

/**
//Synthetic comment -- @@ -146,7 +146,7 @@
* A {@code CopyOnWriteArrayList} is preferable to a synchronized
* {@code ArrayList} when the expected number of reads and traversals
* greatly outnumber the number of updates to a list.

* <p>The "Concurrent" prefix used with some classes in this package
* is a shorthand indicating several differences from similar
* "synchronized" classes.  For example {@code java.util.Hashtable} and
//Synthetic comment -- @@ -243,7 +243,8 @@
*   in each thread <i>happen-before</i> those subsequent to the
*   corresponding {@code exchange()} in another thread.
*
 *   <li>Actions prior to calling {@code CyclicBarrier.await}
*   <i>happen-before</i> actions performed by the barrier action, and
*   actions performed by the barrier action <i>happen-before</i> actions
*   subsequent to a successful return from the corresponding {@code await}







