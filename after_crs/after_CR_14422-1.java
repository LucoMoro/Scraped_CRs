/*Improved error handling when font loading fails.

Without this patch, an application could trigger a platform crash
by calling:

  Typeface typeface = Typeface.createFromFile("");
  boolean isbold = typeface.isBold();

The crash occurs since the Skia error that occurs when font loading
fails is not propagated.
With this patch, the Typeface constructor throws an
IllegalStateException when Skia fails to load the font (i.e. when
the native instance is null).

Change-Id:I1002b37b7b32840cb7eeef6819d2982cb18c9003*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/Typeface.java b/graphics/java/android/graphics/Typeface.java
//Synthetic comment -- index f0d5a6a..9a404f4 100644

//Synthetic comment -- @@ -143,6 +143,9 @@

// don't allow clients to call this directly
private Typeface(int ni) {
        if (ni == 0) {
            throw new IllegalStateException();
        }
native_instance = ni;
}








