/*Fix the layoutlib Paint.set(Paint) method.

It would change the text rendering info but not recompute
the Java Font objects. The effect is a broken font rendering
in the EditText (which use this method to copy some Paint
object before using the copy for the actual drawing)

Change-Id:I6d8d1bf86f0d77d088f60ad81c71dd3ebab727b0*/




//Synthetic comment -- diff --git a/tools/layoutlib/bridge/src/android/graphics/Paint.java b/tools/layoutlib/bridge/src/android/graphics/Paint.java
//Synthetic comment -- index 619ab30..d13b5fe 100644

//Synthetic comment -- @@ -283,6 +283,8 @@
mStyle = src.mStyle;
mFlags = src.mFlags;

            updateFontObject();

super.set(src);
}
}







