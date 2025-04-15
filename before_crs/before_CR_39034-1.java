/*Add quaternion methods to android.opengl.Matrix

Added methods for creating a matrix from a
rotation quaternion and for rotating a
matrix by a rotation quaternion.

Change-Id:I4ccebeb442f89a5e72c556ec0c3254f3daa1bbdb*/
//Synthetic comment -- diff --git a/opengl/java/android/opengl/Matrix.java b/opengl/java/android/opengl/Matrix.java
//Synthetic comment -- index 7c72ae4..c90e38c 100644

//Synthetic comment -- @@ -650,6 +650,88 @@
rm[rmOffset + 14] =  0.0f;
rm[rmOffset + 15] =  1.0f;
}

/**
* Define a viewing transformation in terms of an eye point, a center of







