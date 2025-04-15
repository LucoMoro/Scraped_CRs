/*Correction of an error in the setRotate method of the ColorMatrix class.
The matrix rotation params around the 2nd axis was not set correctly:

Initial matrix for axis 2 (green):
 cos 0 sin 0 0
  0  1  0  0 0
-sin 0  0  0 0
  0  0 cos 1 0

After correction:
 cos 0 -sin 0 0
  0  1   0  0 0
 sin 0  cos 0 0
  0  0   0  1 0*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/ColorMatrix.java b/graphics/java/android/graphics/ColorMatrix.java
//Synthetic comment -- index 2478712e..c22cda1 100644

//Synthetic comment -- @@ -110,22 +110,31 @@
a[18] = aScale;
}

public void setRotate(int axis, float degrees) {
reset();
float radians = degrees * (float)Math.PI / 180;
float cosine = FloatMath.cos(radians);
float sine = FloatMath.sin(radians);
switch (axis) {
case 0:
mArray[6] = mArray[12] = cosine;
mArray[7] = sine;
mArray[11] = -sine;
break;
case 1:
            mArray[0] = mArray[17] = cosine;
            mArray[2] = sine;
            mArray[15] = -sine;
break;
case 2:
mArray[0] = mArray[6] = cosine;
mArray[1] = sine;







