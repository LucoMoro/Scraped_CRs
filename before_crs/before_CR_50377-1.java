/*One more fix for incorrect indices.

Change-Id:I283f2f09cd0a17eefed4a763b08df856bbae76b3*/
//Synthetic comment -- diff --git a/graphics/java/android/renderscript/Matrix3f.java b/graphics/java/android/renderscript/Matrix3f.java
//Synthetic comment -- index 0bad7e0..5e9a7ca 100644

//Synthetic comment -- @@ -140,7 +140,7 @@
mMat[4] = y*y*nc +  c;
mMat[7] =  yz*nc - xs;
mMat[2] =  zx*nc - ys;
        mMat[6] =  yz*nc + xs;
mMat[8] = z*z*nc +  c;
}








