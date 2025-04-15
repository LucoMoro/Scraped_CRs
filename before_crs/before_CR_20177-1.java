/*Allowing more than one index for cursor position

Many implementations of cursor handling for bi-directional text and
text containing complex glyphs need two indices to correctly store
a cursor position. The CTS should allow this.

Change-Id:I587b6ef1e0aef4c3417ece75cf932a630565cb88*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/LinkMovementMethodTest.java b/tests/tests/text/src/android/text/method/cts/LinkMovementMethodTest.java
//Synthetic comment -- index 57c6d33..cbfc327 100644

//Synthetic comment -- @@ -129,7 +129,7 @@
Selection.setSelection(spannable, 0, spannable.length());

assertSelection(spannable, 0, spannable.length());
        assertEquals(2, spannable.getSpans(0, spannable.length(), Object.class).length);
method.onTakeFocus(null, spannable, View.FOCUS_UP);
assertSelection(spannable, -1);
assertEquals(1, spannable.getSpans(0, spannable.length(), Object.class).length);
//Synthetic comment -- @@ -141,7 +141,7 @@
// focus forwards
Selection.setSelection(spannable, 0, spannable.length());
assertSelection(spannable, 0, spannable.length());
        assertEquals(3, spannable.getSpans(0, spannable.length(), Object.class).length);
method.onTakeFocus(null, spannable, View.FOCUS_RIGHT);
assertSelection(spannable, -1);
assertEquals(0, spannable.getSpans(0, spannable.length(), Object.class).length);
//Synthetic comment -- @@ -151,7 +151,7 @@
// param direction is unknown(0)
Selection.setSelection(spannable, 0, spannable.length());
assertSelection(spannable, 0, spannable.length());
        assertEquals(3, spannable.getSpans(0, spannable.length(), Object.class).length);
method.onTakeFocus(null, spannable, 0);
assertSelection(spannable, -1);
assertEquals(0, spannable.getSpans(0, spannable.length(), Object.class).length);
//Synthetic comment -- @@ -576,7 +576,7 @@
Selection.setSelection(spannable, 0, spannable.length());

assertSelection(spannable, 0, spannable.length());
        assertEquals(3, spannable.getSpans(0, spannable.length(), Object.class).length);
method.initialize(null, spannable);
assertSelection(spannable, -1);
assertEquals(0, spannable.getSpans(0, spannable.length(), Object.class).length);







