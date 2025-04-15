/*Avoid the implicit NaN test in {float,double}To{Int,Long}Bits.

The possibility of ak being a NaN is eliminated by the preceding
compare to 0.0.  As such, a raw conversion will produce identical
results.

(cherry-pick of c9f45b2e4dd6c5f0bd96d8419370cdbbc7031ffa.)

Change-Id:Ic87cc137030d9d9e2f3c725ed15982b313323b5d*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/DualPivotQuicksort.java b/luni/src/main/java/java/util/DualPivotQuicksort.java
//Synthetic comment -- index 97797d1..5d2f77f 100644

//Synthetic comment -- @@ -1565,7 +1565,7 @@

for (int k = left; k <= n; k++) {
float ak = a[k];
            if (ak == 0.0f && NEGATIVE_ZERO == Float.floatToRawIntBits(ak)) {
a[k] = 0.0f;
numNegativeZeros++;
} else if (ak != ak) { // i.e., ak is NaN
//Synthetic comment -- @@ -1938,7 +1938,7 @@

for (int k = left; k <= n; k++) {
double ak = a[k];
            if (ak == 0.0d && NEGATIVE_ZERO == Double.doubleToRawLongBits(ak)) {
a[k] = 0.0d;
numNegativeZeros++;
} else if (ak != ak) { // i.e., ak is NaN







