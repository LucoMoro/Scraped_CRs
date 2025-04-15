/*Frameworks: Fix IndexOutOfBounds Exception in TextView

Change-Id:I6641e5510d2378689f6c1f11b449379b03cb06e4*/




//Synthetic comment -- diff --git a/core/java/android/widget/.TextView.java.swp b/core/java/android/widget/.TextView.java.swp
new file mode 100644
//Synthetic comment -- index 0000000..5db4f44
Binary files differ

//Synthetic comment -- @@ -4556,6 +4556,11 @@
} else if (partialEndOffset < 0) {
partialEndOffset = 0;
}
                    if (partialEndOffset < partialStartOffset) {
		        partialStartOffset ^= partialEndOffset;
		        partialEndOffset ^= partialStartOffset;
			partialStartOffset ^= partialEndOffset;
	             }
}
if ((request.flags&InputConnection.GET_TEXT_WITH_STYLES) != 0) {
outText.text = content.subSequence(partialStartOffset,







