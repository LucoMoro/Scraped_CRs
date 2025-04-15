/*Fix misuses of assert in OldClassTest and comment out other libcore asserts.

Change-Id:Ie9e5b785b3e87c91df02ffd93f7973fd7c99af0e*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayDeque.java b/luni/src/main/java/java/util/ArrayDeque.java
//Synthetic comment -- index 5ee3f81..c4fef89 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
* when head and tail have wrapped around to become equal.
*/
private void doubleCapacity() {
        // assert head == tail;
int p = head;
int n = elements.length;
int r = n - p; // number of elements to the right of p
//Synthetic comment -- @@ -484,11 +484,11 @@
}

private void checkInvariants() {
        // assert elements[tail] == null;
        // assert head == tail ? elements[head] == null :
        //     (elements[head] != null &&
        //      elements[(tail - 1) & (elements.length - 1)] != null);
        // assert elements[(head - 1) & (elements.length - 1)] == null;
}

/**
//Synthetic comment -- @@ -502,7 +502,7 @@
* @return true if elements moved backwards
*/
private boolean delete(int i) {
        //checkInvariants();
final Object[] elements = this.elements;
final int mask = elements.length - 1;
final int h = head;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Arrays.java b/luni/src/main/java/java/util/Arrays.java
//Synthetic comment -- index 4a149b7..c1b2727 100644

//Synthetic comment -- @@ -2400,7 +2400,7 @@
}
} else {
// element is an Object[], so we assert that
                        // assert elem instanceof Object[];
if (deepToStringImplContains(origArrays, elem)) {
sb.append("[...]");
} else {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TreeMap.java b/luni/src/main/java/java/util/TreeMap.java
//Synthetic comment -- index 9e19933..db37b9d 100644

//Synthetic comment -- @@ -441,7 +441,7 @@
if (parent.left == node) {
parent.left = replacement;
} else {
                // assert (parent.right == node);
parent.right = replacement;
}
} else {
//Synthetic comment -- @@ -474,7 +474,7 @@
if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
rotateLeft(node); // AVL right right
} else {
                    // assert (rightDelta == 1);
rotateRight(right); // AVL right left
rotateLeft(node);
}
//Synthetic comment -- @@ -492,7 +492,7 @@
if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
rotateRight(node); // AVL left left
} else {
                    // assert (leftDelta == -1);
rotateLeft(left); // AVL left right
rotateRight(node);
}
//Synthetic comment -- @@ -507,7 +507,7 @@
}

} else {
                // assert (delta == -1 || delta == 1);
node.height = Math.max(leftHeight, rightHeight) + 1;
if (!insert) {
break; // the height hasn't changed, so rebalancing is done!








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/UUID.java b/luni/src/main/java/java/util/UUID.java
//Synthetic comment -- index 8ac0a63..3594d87 100644

//Synthetic comment -- @@ -335,13 +335,13 @@
return this.mostSigBits < uuid.mostSigBits ? -1 : 1;
}

        // assert this.mostSigBits == uuid.mostSigBits;

if (this.leastSigBits != uuid.leastSigBits) {
return this.leastSigBits < uuid.leastSigBits ? -1 : 1;
}

        // assert this.leastSigBits == uuid.leastSigBits;

return 0;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Vector.java b/luni/src/main/java/java/util/Vector.java
//Synthetic comment -- index c236be0..f8a456d 100644

//Synthetic comment -- @@ -445,7 +445,7 @@
private void grow(int newCapacity) {
E[] newData = newElementArray(newCapacity);
// Assumes elementCount is <= newCapacity
        // assert elementCount <= newCapacity;
System.arraycopy(elementData, 0, newData, 0, elementCount);
elementData = newData;
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/OldClassTest.java b/luni/src/test/java/libcore/java/lang/OldClassTest.java
//Synthetic comment -- index c516467..dcea250 100644

//Synthetic comment -- @@ -989,12 +989,12 @@
Class clazz = getClass();

InputStream stream = clazz.getResourceAsStream("HelloWorld.txt");
        assertNotNull(stream);

byte[] buffer = new byte[20];
int length = stream.read(buffer);
String s = new String(buffer, 0, length);
        assertEquals("Hello, World.",  s);

stream.close();
}
//Synthetic comment -- @@ -1003,12 +1003,12 @@
Class clazz = getClass();

InputStream stream = clazz.getResourceAsStream("/libcore/java/lang/HelloWorld.txt");
        assertNotNull(stream);

byte[] buffer = new byte[20];
int length = stream.read(buffer);
String s = new String(buffer, 0, length);
        assertEquals("Hello, World.", s);

stream.close();








