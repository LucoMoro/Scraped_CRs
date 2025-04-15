/*TextView context menu has inconsistent behaviour

When selected or marked text is removed in a
TextView widget the selection mode needs to
be stopped in onTextChanged(). If not, the
context menu will not appear or show wrong options.

Change-Id:I67133293a9c187e240ca99458e2b1f6a52570953*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..9d71488 100644

//Synthetic comment -- @@ -6360,6 +6360,7 @@
// Hide the controller if the amount of content changed
if (before != after) {
hideControllers();
}
}








