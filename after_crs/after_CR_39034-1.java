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
	 * Rotates the given matrix in place by the given quaternion rotation
	 * 
	 * @param m The float array that holds matrix to rotate
	 * @param mOffset The offset into <code>m</code> where the matrix starts
	 * @param w The w-component of the quaternion
	 * @param x The x-component of the quaternion
	 * @param y The y-component of the quaternion
	 * @param z The z-component of the quaternion
	 */
	public static void rotateQuaternionM(float[] m, int mOffset, float w,
			float x, float y, float z) {
		synchronized (sTemp) {
			setRotateQuaternionM(sTemp, 0, w, x, y, z);
			multiplyMM(sTemp, 16, m, mOffset, sTemp, 0);
			System.arraycopy(sTemp, 16, m, mOffset, 16);
		}
	}
	
	/**
	 * Rotates the given matrix by the given quaternion rotation, putting the
	 * result in rm
	 * 
	 * @param rm The array to store the result
	 * @param rmOffset The offset into rm where the matrix should start.
	 * @param m The float array that holds matrix to rotate
	 * @param mOffset The offset into <code>m</code> where the matrix starts
	 * @param w The w-component of the quaternion
	 * @param x The x-component of the quaternion
	 * @param y The y-component of the quaternion
	 * @param z The z-component of the quaternion
	 */
	public static void rotateQuaternionM(float[] rm, int rmOffset, float[] m,
			int mOffset, float w, float x, float y, float z) {
		synchronized (sTemp) {
			setRotateQuaternionM(sTemp, 0, w, x, y, z);
			multiplyMM(rm, rmOffset, m, mOffset, sTemp, 0);
		}
	}
	
	/**
	 * Converts a quaternion (w, x, y, z) to a rotation matrix.
	 * 
	 * @param m The float array that holds matrix to rotate
	 * @param mOffset The offset into <code>m</code> where the matrix starts
	 * @param w The w-component of the quaternion
	 * @param x The x-component of the quaternion
	 * @param y The y-component of the quaternion
	 * @param z The z-component of the quaternion
	 */
	public static void setRotateQuaternionM(float[] m, int mOffset, float w,
			float x, float y, float z) {
		
		if (m.length < mOffset + 16) {
			throw new IllegalArgumentException("m.length < mOffset + 16");
		}
		
		final float xx = x*x, yy = y*y, zz = z*z;
		final float xy = x*y, yz = y*z, xz = x*z;
		final float xw = x*w, yw = y*w, zw = z*w;
		
		m[mOffset +  0] = 1 - 2*yy - 2*zz;
		m[mOffset +  1] = 2*xy + 2*zw;
		m[mOffset +  2] = 2*xz - 2*yw;
		m[mOffset +  3] = 0;
		
		m[mOffset +  4] = 2*xy - 2*zw;
		m[mOffset +  5] = 1 - 2*xx - 2*zz;
		m[mOffset +  6] = 2*yz + 2*xw;
		m[mOffset +  7] = 0;
		
		m[mOffset +  8] = 2*xz + 2*yw;
		m[mOffset +  9] = 2*yz - 2*xw;
		m[mOffset + 10] = 1 - 2*xx - 2*yy;
		m[mOffset + 11] = 0;
		
		m[mOffset + 12] = 0;
		m[mOffset + 13] = 0;
		m[mOffset + 14] = 0;
		m[mOffset + 15] = 1;
	}

/**
* Define a viewing transformation in terms of an eye point, a center of







