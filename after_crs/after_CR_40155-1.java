/*Improve detail messages for throwers of NegativeArraySizeException.

(cherry-pick of e1766a71541cbc592c6ceb6fe703258bebd9c15e.)

Change-Id:Ib208ac64d26f2969299667d3312eb0fd1fdda3d1*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/AbstractStringBuilder.java b/luni/src/main/java/java/lang/AbstractStringBuilder.java
//Synthetic comment -- index baab47d..6e46d26 100644

//Synthetic comment -- @@ -77,7 +77,7 @@

AbstractStringBuilder(int capacity) {
if (capacity < 0) {
            throw new NegativeArraySizeException(Integer.toString(capacity));
}
value = new char[capacity];
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Arrays.java b/luni/src/main/java/java/util/Arrays.java
//Synthetic comment -- index 9d0f4a4..588410f 100644

//Synthetic comment -- @@ -2459,7 +2459,7 @@
*/
public static boolean[] copyOf(boolean[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2478,7 +2478,7 @@
*/
public static byte[] copyOf(byte[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2497,7 +2497,7 @@
*/
public static char[] copyOf(char[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2516,7 +2516,7 @@
*/
public static double[] copyOf(double[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2535,7 +2535,7 @@
*/
public static float[] copyOf(float[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2554,7 +2554,7 @@
*/
public static int[] copyOf(int[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2573,7 +2573,7 @@
*/
public static long[] copyOf(long[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2592,7 +2592,7 @@
*/
public static short[] copyOf(short[] original, int newLength) {
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2614,7 +2614,7 @@
throw new NullPointerException();
}
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength);
}
//Synthetic comment -- @@ -2636,7 +2636,7 @@
public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
// We use the null pointer check in copyOfRange for exception priority compatibility.
if (newLength < 0) {
            throw new NegativeArraySizeException(Integer.toString(newLength));
}
return copyOfRange(original, 0, newLength, newType);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/BitSet.java b/luni/src/main/java/java/util/BitSet.java
//Synthetic comment -- index a4ee4c1..9dfe35e 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
*/
public BitSet(int bitCount) {
if (bitCount < 0) {
            throw new NegativeArraySizeException(Integer.toString(bitCount));
}
this.bits = arrayForBits(bitCount);
this.longCount = 0;







