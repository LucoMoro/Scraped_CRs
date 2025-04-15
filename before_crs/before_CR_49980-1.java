/*Make transformation type constants 'final'.

Change-Id:I6661658dfc69ea9bd15e3ca0645b55d49a75f985*/
//Synthetic comment -- diff --git a/core/java/android/view/animation/Transformation.java b/core/java/android/view/animation/Transformation.java
//Synthetic comment -- index e8c1d23..890909b 100644

//Synthetic comment -- @@ -29,19 +29,19 @@
/**
* Indicates a transformation that has no effect (alpha = 1 and identity matrix.)
*/
    public static int TYPE_IDENTITY = 0x0;
/**
* Indicates a transformation that applies an alpha only (uses an identity matrix.)
*/
    public static int TYPE_ALPHA = 0x1;
/**
* Indicates a transformation that applies a matrix only (alpha = 1.)
*/
    public static int TYPE_MATRIX = 0x2;
/**
* Indicates a transformation that applies an alpha and a matrix.
*/
    public static int TYPE_BOTH = TYPE_ALPHA | TYPE_MATRIX;

protected Matrix mMatrix;
protected float mAlpha;







