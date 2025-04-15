/*Secondary hash for IdentityHashMap and WeakHashMap.

Dalvik's identity hash code is a multiple of 8 meaning that it can cause
frequent collisions if a secondary hash isn't applied. This change adds
a secondary hash function to IdentityHashMap and WeakHashMap, as is done
in HashMap, HashTable and ConcurrentHashMap. There is some other minor clean-up
including updating the secondary hash function to match that in
ConcurrentHashMap.

Change-Id:Id2115b2622ff759bf2f67bd7572c2ecd8f0f0d67*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Collections.java b/luni/src/main/java/java/util/Collections.java
//Synthetic comment -- index 0fafcff..d62e392 100644

//Synthetic comment -- @@ -3399,4 +3399,50 @@
return sm.lastKey();
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/HashMap.java b/luni/src/main/java/java/util/HashMap.java
//Synthetic comment -- index 1b2438b..20a5522 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
} else if (capacity > MAXIMUM_CAPACITY) {
capacity = MAXIMUM_CAPACITY;
} else {
            capacity = roundUpToPowerOfTwo(capacity);
}
makeTable(capacity);
}
//Synthetic comment -- @@ -294,11 +294,7 @@
return e == null ? null : e.value;
}

        // Doug Lea's supplemental secondaryHash function (inlined)
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);

HashMapEntry<K, V>[] tab = table;
for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)];
e != null; e = e.next) {
//Synthetic comment -- @@ -323,11 +319,7 @@
return entryForNullKey != null;
}

        // Doug Lea's supplemental secondaryHash function (inlined)
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);

HashMapEntry<K, V>[] tab = table;
for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)];
e != null; e = e.next) {
//Synthetic comment -- @@ -387,7 +379,7 @@
return putValueForNullKey(value);
}

        int hash = secondaryHash(key.hashCode());
HashMapEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashMapEntry<K, V> e = tab[index]; e != null; e = e.next) {
//Synthetic comment -- @@ -450,7 +442,7 @@
return;
}

        int hash = secondaryHash(key.hashCode());
HashMapEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
HashMapEntry<K, V> first = tab[index];
//Synthetic comment -- @@ -519,7 +511,7 @@
*  <p>This method is called only by putAll.
*/
private void ensureCapacity(int numMappings) {
        int newCapacity = roundUpToPowerOfTwo(capacityForInitSize(numMappings));
HashMapEntry<K, V>[] oldTable = table;
int oldCapacity = oldTable.length;
if (newCapacity <= oldCapacity) {
//Synthetic comment -- @@ -618,7 +610,7 @@
if (key == null) {
return removeNullKey();
}
        int hash = secondaryHash(key.hashCode());
HashMapEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashMapEntry<K, V> e = tab[index], prev = null;
//Synthetic comment -- @@ -838,7 +830,7 @@
return e != null && Objects.equal(value, e.value);
}

        int hash = secondaryHash(key.hashCode());
HashMapEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashMapEntry<K, V> e = tab[index]; e != null; e = e.next) {
//Synthetic comment -- @@ -866,7 +858,7 @@
return true;
}

        int hash = secondaryHash(key.hashCode());
HashMapEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashMapEntry<K, V> e = tab[index], prev = null;
//Synthetic comment -- @@ -962,38 +954,6 @@
}
}

    /**
     * Applies a supplemental hash function to a given hashCode, which defends
     * against poor quality hash functions. This is critical because HashMap
     * uses power-of-two length hash tables, that otherwise encounter collisions
     * for hashCodes that do not differ in lower or upper bits.
     */
    private static int secondaryHash(int h) {
        // Doug Lea's supplemental hash function
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns the smallest power of two >= its argument, with several caveats:
     * If the argument is negative but not Integer.MIN_VALUE, the method returns
     * zero. If the argument is > 2^30 or equal to Integer.MIN_VALUE, the method
     * returns Integer.MIN_VALUE. If the argument is zero, the method returns
     * zero.
     */
    private static int roundUpToPowerOfTwo(int i) {
        i--; // If input is a power of two, shift its high-order bit right

        // "Smear" the high-order bit all the way to the right
        i |= i >>>  1;
        i |= i >>>  2;
        i |= i >>>  4;
        i |= i >>>  8;
        i |= i >>> 16;

        return i + 1;
    }

private static final long serialVersionUID = 362498820763181265L;

private static final ObjectStreamField[] serialPersistentFields = {
//Synthetic comment -- @@ -1026,7 +986,7 @@
} else if (capacity > MAXIMUM_CAPACITY) {
capacity = MAXIMUM_CAPACITY;
} else {
            capacity = roundUpToPowerOfTwo(capacity);
}
makeTable(capacity);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Hashtable.java b/luni/src/main/java/java/util/Hashtable.java
//Synthetic comment -- index a4e24bc..8bf9474 100644

//Synthetic comment -- @@ -132,7 +132,7 @@
} else if (capacity > MAXIMUM_CAPACITY) {
capacity = MAXIMUM_CAPACITY;
} else {
            capacity = roundUpToPowerOfTwo(capacity);
}
makeTable(capacity);
}
//Synthetic comment -- @@ -259,11 +259,7 @@
* @see #put
*/
public synchronized V get(Object key) {
        // Doug Lea's supplemental secondaryHash function (inlined)
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);

HashtableEntry<K, V>[] tab = table;
for (HashtableEntry<K, V> e = tab[hash & (tab.length - 1)];
e != null; e = e.next) {
//Synthetic comment -- @@ -287,11 +283,7 @@
* @see java.lang.Object#equals
*/
public synchronized boolean containsKey(Object key) {
        // Doug Lea's supplemental secondaryHash function (inlined)
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);

HashtableEntry<K, V>[] tab = table;
for (HashtableEntry<K, V> e = tab[hash & (tab.length - 1)];
e != null; e = e.next) {
//Synthetic comment -- @@ -366,7 +358,7 @@
} else if (value == null) {
throw new NullPointerException("value == null");
}
        int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
HashtableEntry<K, V> first = tab[index];
//Synthetic comment -- @@ -402,7 +394,7 @@
} else if (value == null) {
throw new NullPointerException("value == null");
}
        int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
HashtableEntry<K, V> first = tab[index];
//Synthetic comment -- @@ -442,7 +434,7 @@
*  <p>This method is called only by putAll.
*/
private void ensureCapacity(int numMappings) {
        int newCapacity = roundUpToPowerOfTwo(capacityForInitSize(numMappings));
HashtableEntry<K, V>[] oldTable = table;
int oldCapacity = oldTable.length;
if (newCapacity <= oldCapacity) {
//Synthetic comment -- @@ -555,7 +547,7 @@
* @see #put
*/
public synchronized V remove(Object key) {
        int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashtableEntry<K, V> e = tab[index], prev = null;
//Synthetic comment -- @@ -799,7 +791,7 @@
* Returns true if this map contains the specified mapping.
*/
private synchronized boolean containsMapping(Object key, Object value) {
        int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashtableEntry<K, V> e = tab[index]; e != null; e = e.next) {
//Synthetic comment -- @@ -815,7 +807,7 @@
* exists; otherwise, returns does nothing and returns false.
*/
private synchronized boolean removeMapping(Object key, Object value) {
        int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
int index = hash & (tab.length - 1);
for (HashtableEntry<K, V> e = tab[index], prev = null;
//Synthetic comment -- @@ -1062,38 +1054,6 @@
}
}

    /**
     * Applies a supplemental hash function to a given hashCode, which defends
     * against poor quality hash functions. This is critical because Hashtable
     * uses power-of-two length hash tables, that otherwise encounter collisions
     * for hashCodes that do not differ in lower or upper bits.
     */
    private static int secondaryHash(int h) {
        // Doug Lea's supplemental hash function
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns the smallest power of two >= its argument, with several caveats:
     * If the argument is negative but not Integer.MIN_VALUE, the method returns
     * zero. If the argument is > 2^30 or equal to Integer.MIN_VALUE, the method
     * returns Integer.MIN_VALUE. If the argument is zero, the method returns
     * zero.
     */
    private static int roundUpToPowerOfTwo(int i) {
        i--; // If input is a power of two, shift its high-order bit right

        // "Smear" the high-order bit all the way to the right
        i |= i >>>  1;
        i |= i >>>  2;
        i |= i >>>  4;
        i |= i >>>  8;
        i |= i >>> 16;

        return i + 1;
    }

private static final long serialVersionUID = 1421746759512286392L;

private static final ObjectStreamField[] serialPersistentFields = {
//Synthetic comment -- @@ -1129,7 +1089,7 @@
} else if (capacity > MAXIMUM_CAPACITY) {
capacity = MAXIMUM_CAPACITY;
} else {
            capacity = roundUpToPowerOfTwo(capacity);
}
makeTable(capacity);









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/IdentityHashMap.java b/luni/src/main/java/java/util/IdentityHashMap.java
//Synthetic comment -- index 904eed5..eda2d7b 100644

//Synthetic comment -- @@ -441,7 +441,7 @@
}

private int getModuloHash(Object key, int length) {
        return ((System.identityHashCode(key) & 0x7FFFFFFF) % (length / 2)) * 2;
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/LinkedHashMap.java b/luni/src/main/java/java/util/LinkedHashMap.java
//Synthetic comment -- index d8fc02d..d2c6b76 100644

//Synthetic comment -- @@ -247,11 +247,7 @@
return e.value;
}

        // Doug Lea's supplemental secondaryHash function (inlined)
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        hash ^= (hash >>> 7) ^ (hash >>> 4);

HashMapEntry<K, V>[] tab = table;
for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)];
e != null; e = e.next) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/WeakHashMap.java b/luni/src/main/java/java/util/WeakHashMap.java
//Synthetic comment -- index 0d9653a..2db8695 100644

//Synthetic comment -- @@ -55,7 +55,7 @@

private static final class Entry<K, V> extends WeakReference<K> implements
Map.Entry<K, V> {
        int hash;

boolean isNull;

//Synthetic comment -- @@ -70,7 +70,7 @@
Entry(K key, V object, ReferenceQueue<K> queue) {
super(key, queue);
isNull = key == null;
            hash = isNull ? 0 : key.hashCode();
value = object;
}

//Synthetic comment -- @@ -453,7 +453,7 @@
public V get(Object key) {
poll();
if (key != null) {
            int index = (key.hashCode() & 0x7FFFFFFF) % elementData.length;
Entry<K, V> entry = elementData[index];
while (entry != null) {
if (key.equals(entry.get())) {
//Synthetic comment -- @@ -476,7 +476,7 @@
Entry<K, V> getEntry(Object key) {
poll();
if (key != null) {
            int index = (key.hashCode() & 0x7FFFFFFF) % elementData.length;
Entry<K, V> entry = elementData[index];
while (entry != null) {
if (key.equals(entry.get())) {
//Synthetic comment -- @@ -590,7 +590,7 @@
int index = 0;
Entry<K, V> entry;
if (key != null) {
            index = (key.hashCode() & 0x7FFFFFFF) % elementData.length;
entry = elementData[index];
while (entry != null && !key.equals(entry.get())) {
entry = entry.next;
//Synthetic comment -- @@ -605,7 +605,7 @@
modCount++;
if (++elementCount > threshold) {
rehash();
                index = key == null ? 0 : (key.hashCode() & 0x7FFFFFFF)
% elementData.length;
}
entry = new Entry<K, V>(key, value, referenceQueue);
//Synthetic comment -- @@ -668,7 +668,7 @@
int index = 0;
Entry<K, V> entry, last = null;
if (key != null) {
            index = (key.hashCode() & 0x7FFFFFFF) % elementData.length;
entry = elementData[index];
while (entry != null && !key.equals(entry.get())) {
last = entry;







