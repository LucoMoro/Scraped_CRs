/*Fix the documentation for Math/StrictMath ceil/floor.

Bug:https://code.google.com/p/android/issues/detail?id=44080Change-Id:I426b723a7aa36a16aed7baf36fd907786799c91a*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Math.java b/luni/src/main/java/java/lang/Math.java
//Synthetic comment -- index 22c89ce..f8d22ed 100644

//Synthetic comment -- @@ -200,7 +200,7 @@

/**
* Returns the double conversion of the most negative (closest to negative
     * infinity) integer value which is greater than the argument.
* <p>
* Special cases:
* <ul>
//Synthetic comment -- @@ -211,10 +211,6 @@
* <li>{@code ceil(-infinity) = -infinity}</li>
* <li>{@code ceil(NaN) = NaN}</li>
* </ul>
     *
     * @param d
     *            the value whose closest integer value has to be computed.
     * @return the ceiling of the argument.
*/
public static native double ceil(double d);

//Synthetic comment -- @@ -301,7 +297,7 @@

/**
* Returns the double conversion of the most positive (closest to positive
     * infinity) integer value which is less than the argument.
* <p>
* Special cases:
* <ul>
//Synthetic comment -- @@ -311,10 +307,6 @@
* <li>{@code floor(-infinity) = -infinity}</li>
* <li>{@code floor(NaN) = NaN}</li>
* </ul>
     *
     * @param d
     *            the value whose closest integer value has to be computed.
     * @return the floor of the argument.
*/
public static native double floor(double d);









//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/StrictMath.java b/luni/src/main/java/java/lang/StrictMath.java
//Synthetic comment -- index 6571b2d..f409c06 100644

//Synthetic comment -- @@ -215,7 +215,7 @@

/**
* Returns the double conversion of the most negative (closest to negative
     * infinity) integer value which is greater than the argument.
* <p>
* Special cases:
* <ul>
//Synthetic comment -- @@ -226,10 +226,6 @@
* <li>{@code ceil(-infinity) = -infinity}</li>
* <li>{@code ceil(NaN) = NaN}</li>
* </ul>
     *
     * @param d
     *            the value whose closest integer value has to be computed.
     * @return the ceiling of the argument.
*/
public static native double ceil(double d);

//Synthetic comment -- @@ -309,7 +305,7 @@

/**
* Returns the double conversion of the most positive (closest to
     * positive infinity) integer value which is less than the argument.
* <p>
* Special cases:
* <ul>
//Synthetic comment -- @@ -319,9 +315,6 @@
* <li>{@code floor(-infinity) = -infinity}</li>
* <li>{@code floor(NaN) = NaN}</li>
* </ul>
     *
     * @param d the value whose closest integer value has to be computed.
     * @return the floor of the argument.
*/
public static native double floor(double d);








