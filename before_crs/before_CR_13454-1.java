/*Default RotateDrawable's pivot set to (50%, 50%)

In order to define the pivot in an XML-instanciated RotateDrawable, android:pivotX
and android:pivotY had to be set in your XML. Forgetting to set those attributes
ended up in a NullPointerException (tv = null) that were caught by the Resources.getDrawable()
method (caught as an Exception). As a result a not-very-accurate message was logged:
"Resource not found ...". Defining a default pivot value seems like a great fix.
Some other fixes would be to modify the documentation or notify the user with a better
explanation than "Resource not found ...".*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/RotateDrawable.java b/graphics/java/android/graphics/drawable/RotateDrawable.java
//Synthetic comment -- index c4a7822..2083e05 100644

//Synthetic comment -- @@ -204,13 +204,27 @@
com.android.internal.R.styleable.RotateDrawable_visible);

TypedValue tv = a.peekValue(com.android.internal.R.styleable.RotateDrawable_pivotX);
        boolean pivotXRel = tv.type == TypedValue.TYPE_FRACTION;
        float pivotX = pivotXRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();

tv = a.peekValue(com.android.internal.R.styleable.RotateDrawable_pivotY);
        boolean pivotYRel = tv.type == TypedValue.TYPE_FRACTION;
        float pivotY = pivotYRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
        
float fromDegrees = a.getFloat(
com.android.internal.R.styleable.RotateDrawable_fromDegrees, 0.0f);
float toDegrees = a.getFloat(







