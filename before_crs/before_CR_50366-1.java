/*Fix array index oob in Matrix3f.loadRotate().https://code.google.com/p/android/issues/detail?id=42860Change-Id:Idbf18576da3ad5b84b4209147dad34cc2f9044d2*/
//Synthetic comment -- diff --git a/graphics/java/android/renderscript/Matrix3f.java b/graphics/java/android/renderscript/Matrix3f.java
//Synthetic comment -- index 66f2c81..0bad7e0 100644

//Synthetic comment -- @@ -138,7 +138,7 @@
mMat[6] =  zx*nc + ys;
mMat[1] =  xy*nc + zs;
mMat[4] = y*y*nc +  c;
        mMat[9] =  yz*nc - xs;
mMat[2] =  zx*nc - ys;
mMat[6] =  yz*nc + xs;
mMat[8] = z*z*nc +  c;







