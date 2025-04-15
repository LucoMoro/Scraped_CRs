/*Include the failed key when a Hashtable put fails.

This helped debugging a problem I was running into when
getting vogar to work when System.getenv() was returning
null; it might help other developers in similar situations.

(cherry-pick of e9f1bc2eb660107ef925b3424f472e38b74b92ac.)

Change-Id:If58c16397b70ef747736df8d5491da15fa3a450c*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Hashtable.java b/luni/src/main/java/java/util/Hashtable.java
//Synthetic comment -- index cea29da..4ea26fb 100644

//Synthetic comment -- @@ -361,8 +361,8 @@
* @see java.lang.Object#equals
*/
public synchronized V put(K key, V value) {
        if (value == null) {
            throw new NullPointerException();
}
int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
//Synthetic comment -- @@ -395,8 +395,8 @@
* ensure that capacity is sufficient, and does not increment modCount.
*/
private void constructorPut(K key, V value) {
        if (value == null) {
            throw new NullPointerException();
}
int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
//Synthetic comment -- @@ -680,7 +680,7 @@

public final V setValue(V value) {
if (value == null) {
                throw new NullPointerException();
}
V oldValue = this.value;
this.value = value;







