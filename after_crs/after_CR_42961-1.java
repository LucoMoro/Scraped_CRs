/*Manually inline StrictMath.min(JJ) and StrictMath.max(JJ).

Also add new intrinsic tests.

Bug: 7146208
Change-Id:Ic454c8c5b218c51ce1c21f0c11163455392b4e72*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Math.java b/luni/src/main/java/java/lang/Math.java
//Synthetic comment -- index 68db4fa..22c89ce 100644

//Synthetic comment -- @@ -53,10 +53,6 @@
* <li>{@code abs(-infinity) = +infinity}</li>
* <li>{@code abs(NaN) = NaN}</li>
* </ul>
*/
public static native double abs(double d);

//Synthetic comment -- @@ -70,11 +66,6 @@
* <li>{@code abs(-infinity) = +infinity}</li>
* <li>{@code abs(NaN) = NaN}</li>
* </ul>
*/
public static native float abs(float f);

//Synthetic comment -- @@ -83,22 +74,12 @@
* <p>
* If the argument is {@code Integer.MIN_VALUE}, {@code Integer.MIN_VALUE}
* is returned.
*/
public static native int abs(int i);

/**
* Returns the absolute value of the argument. If the argument is {@code
* Long.MIN_VALUE}, {@code Long.MIN_VALUE} is returned.
*/
public static native long abs(long l);

//Synthetic comment -- @@ -467,12 +448,6 @@
* <li>{@code max(+0.0, -0.0) = +0.0}</li>
* <li>{@code max(-0.0, +0.0) = +0.0}</li>
* </ul>
*/
public static double max(double d1, double d2) {
if (d1 > d2) {
//Synthetic comment -- @@ -504,12 +479,6 @@
* <li>{@code max(+0.0, -0.0) = +0.0}</li>
* <li>{@code max(-0.0, +0.0) = +0.0}</li>
* </ul>
*/
public static float max(float f1, float f2) {
if (f1 > f2) {
//Synthetic comment -- @@ -533,24 +502,12 @@
/**
* Returns the most positive (closest to positive infinity) of the two
* arguments.
*/
public static native int max(int i1, int i2);

/**
* Returns the most positive (closest to positive infinity) of the two
* arguments.
*/
public static long max(long l1, long l2) {
return l1 > l2 ? l1 : l2;
//Synthetic comment -- @@ -567,12 +524,6 @@
* <li>{@code min(+0.0, -0.0) = -0.0}</li>
* <li>{@code min(-0.0, +0.0) = -0.0}</li>
* </ul>
*/
public static double min(double d1, double d2) {
if (d1 > d2) {
//Synthetic comment -- @@ -604,12 +555,6 @@
* <li>{@code min(+0.0, -0.0) = -0.0}</li>
* <li>{@code min(-0.0, +0.0) = -0.0}</li>
* </ul>
*/
public static float min(float f1, float f2) {
if (f1 > f2) {
//Synthetic comment -- @@ -633,24 +578,12 @@
/**
* Returns the most negative (closest to negative infinity) of the two
* arguments.
*/
public static native int min(int i1, int i2);

/**
* Returns the most negative (closest to negative infinity) of the two
* arguments.
*/
public static long min(long l1, long l2) {
return l1 < l2 ? l1 : l2;
//Synthetic comment -- @@ -884,10 +817,6 @@
* <li>{@code sqrt(+infinity) = +infinity}</li>
* <li>{@code sqrt(NaN) = NaN}</li>
* </ul>
*/
public static native double sqrt(double d);









//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/StrictMath.java b/luni/src/main/java/java/lang/StrictMath.java
//Synthetic comment -- index d21027b..6571b2d 100644

//Synthetic comment -- @@ -62,10 +62,6 @@
* <li>{@code abs(-infinity) = +infinity}</li>
* <li>{@code abs(NaN) = NaN}</li>
* </ul>
*/
public static double abs(double d) {
return Math.abs(d);
//Synthetic comment -- @@ -81,11 +77,6 @@
* <li>{@code abs(-infinity) = +infinity}</li>
* <li>{@code abs(NaN) = NaN}</li>
* </ul>
*/
public static float abs(float f) {
return Math.abs(f);
//Synthetic comment -- @@ -96,11 +87,6 @@
* <p>
* If the argument is {@code Integer.MIN_VALUE}, {@code Integer.MIN_VALUE}
* is returned.
*/
public static int abs(int i) {
return Math.abs(i);
//Synthetic comment -- @@ -111,11 +97,6 @@
* <p>
* If the argument is {@code Long.MIN_VALUE}, {@code Long.MIN_VALUE} is
* returned.
*/
public static long abs(long l) {
return Math.abs(l);
//Synthetic comment -- @@ -469,12 +450,6 @@
* <li>{@code max(+0.0, -0.0) = +0.0}</li>
* <li>{@code max(-0.0, +0.0) = +0.0}</li>
* </ul>
*/
public static double max(double d1, double d2) {
if (d1 > d2)
//Synthetic comment -- @@ -502,12 +477,6 @@
* <li>{@code max(+0.0, -0.0) = +0.0}</li>
* <li>{@code max(-0.0, +0.0) = +0.0}</li>
* </ul>
*/
public static float max(float f1, float f2) {
if (f1 > f2)
//Synthetic comment -- @@ -527,12 +496,6 @@
/**
* Returns the most positive (closest to positive infinity) of the two
* arguments.
*/
public static int max(int i1, int i2) {
return Math.max(i1, i2);
//Synthetic comment -- @@ -541,15 +504,9 @@
/**
* Returns the most positive (closest to positive infinity) of the two
* arguments.
*/
public static long max(long l1, long l2) {
        return l1 > l2 ? l1 : l2;
}

/**
//Synthetic comment -- @@ -563,12 +520,6 @@
* <li>{@code min(+0.0, -0.0) = -0.0}</li>
* <li>{@code min(-0.0, +0.0) = -0.0}</li>
* </ul>
*/
public static double min(double d1, double d2) {
if (d1 > d2)
//Synthetic comment -- @@ -596,12 +547,6 @@
* <li>{@code min(+0.0, -0.0) = -0.0}</li>
* <li>{@code min(-0.0, +0.0) = -0.0}</li>
* </ul>
*/
public static float min(float f1, float f2) {
if (f1 > f2)
//Synthetic comment -- @@ -621,12 +566,6 @@
/**
* Returns the most negative (closest to negative infinity) of the two
* arguments.
*/
public static int min(int i1, int i2) {
return Math.min(i1, i2);
//Synthetic comment -- @@ -635,15 +574,9 @@
/**
* Returns the most negative (closest to negative infinity) of the two
* arguments.
*/
public static long min(long l1, long l2) {
        return l1 < l2 ? l1 : l2;
}

/**
//Synthetic comment -- @@ -856,10 +789,6 @@
* <li>{@code sqrt(+infinity) = +infinity}</li>
* <li>{@code sqrt(NaN) = NaN}</li>
* </ul>
*/
public static native double sqrt(double d);









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/lang/IntrinsicTest.java b/luni/src/test/java/libcore/java/lang/IntrinsicTest.java
//Synthetic comment -- index fcf0522..75a4e42 100644

//Synthetic comment -- @@ -60,6 +60,32 @@
Math.class.getMethod("abs", double.class).invoke(null, 1.0);
}

    public void testStrictMath_abs() throws Exception {
        StrictMath.abs(1);
        StrictMath.class.getMethod("abs", int.class).invoke(null, 1);
        StrictMath.abs(1L);
        StrictMath.class.getMethod("abs", long.class).invoke(null, 1L);
        StrictMath.abs(1.0f);
        StrictMath.class.getMethod("abs", float.class).invoke(null, 1.0f);
        StrictMath.abs(1.0);
        StrictMath.class.getMethod("abs", double.class).invoke(null, 1.0);
    }

    public void testStrictMath_min() throws Exception {
        StrictMath.min(1, 2);
        StrictMath.class.getMethod("min", int.class, int.class).invoke(null, 1, 2);
    }

    public void testStrictMath_max() throws Exception {
        StrictMath.max(1, 2);
        StrictMath.class.getMethod("max", int.class, int.class).invoke(null, 1, 2);
    }

    public void testStrictMath_sqrt() throws Exception {
        StrictMath.sqrt(2.0);
        StrictMath.class.getMethod("sqrt", double.class).invoke(null, 2.0);
    }

public void testMath_min() throws Exception {
Math.min(1, 2);
Math.class.getMethod("min", int.class, int.class).invoke(null, 1, 2);







