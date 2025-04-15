/*Frameworks: Fix IndexOutOfBounds Exception in TextView

Change-Id:I6641e5510d2378689f6c1f11b449379b03cb06e4*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 05db266..08fafac 100644

//Synthetic comment -- @@ -4556,6 +4556,11 @@
} else if (partialEndOffset < 0) {
partialEndOffset = 0;
}
}
if ((request.flags&InputConnection.GET_TEXT_WITH_STYLES) != 0) {
outText.text = content.subSequence(partialStartOffset,







