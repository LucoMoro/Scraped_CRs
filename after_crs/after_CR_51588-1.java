/*Add explicit null check in ArrayList

This will help developers debug their code when passing in null as the
collection they wish to copy from.

Change-Id:I6406880d3450037102911ae54ef44e8a03a79420*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayList.java b/luni/src/main/java/java/util/ArrayList.java
//Synthetic comment -- index d193eec..8a3218d 100644

//Synthetic comment -- @@ -90,6 +90,10 @@
*            the collection of elements to add.
*/
public ArrayList(Collection<? extends E> collection) {
        if (collection == null) {
            throw new NullPointerException("collection == null");
        }

Object[] a = collection.toArray();
if (a.getClass() != Object[].class) {
Object[] newArray = new Object[a.length];







