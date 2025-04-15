/*Disabled EditText now loses focus and closes onscreen keyboard. Fixes Issue 2771.

Change-Id:I44e3c0aff2a0ce1e6426818bfe16c1d19c7c18ac*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index e6ed70a..86368fc 100644

//Synthetic comment -- @@ -944,6 +944,25 @@
setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the







