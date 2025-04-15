/*Fix incorrect calculation of start and end indices of changed text

- The problem is taht the calculation of indices does not take the
  difference in original and modified text lengths into account.
- If reportExtractedText() is called from the onDraw() function,
  mInputMethodState is not reset.

The error can occur when the user is editing formatted text using a full screen editor.
One example of formatted text is phone numbers (e.g. dashes are added to North American
numbers by the PhoneNumberFormattingTextWatcher class).
If the two first digits are removed from the number 555-666-777, the new number becomes 566--777.
This is due incorrect calculation of start and end indices of the changed text. The error is in
the TextView class and the problem is that the calculation does not take the difference in original
and modified text lengths into account.

Change-Id:I83d5168de5cd74509df5aff64d2f672ffd162486*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 6418dad..dbfa370 100644

//Synthetic comment -- @@ -4512,6 +4512,9 @@
partialStartOffset = 0;
partialEndOffset = N;
} else {
                    // Now use the delta to determine the actual amount of text
                    // we need.
                    partialEndOffset += delta;
// Adjust offsets to ensure we contain full spans.
if (content instanceof Spanned) {
Spanned spanned = (Spanned)content;
//Synthetic comment -- @@ -4527,10 +4530,8 @@
}
}
outText.partialStartOffset = partialStartOffset;
                    outText.partialEndOffset = partialEndOffset - delta;

if (partialStartOffset > N) {
partialStartOffset = N;
} else if (partialStartOffset < 0) {
//Synthetic comment -- @@ -4590,6 +4591,10 @@
+ ": " + ims.mTmpExtracted.text);
imm.updateExtractedText(this, req.token,
mInputMethodState.mTmpExtracted);
                            ims.mChangedStart = EXTRACT_UNKNOWN;
                            ims.mChangedEnd = EXTRACT_UNKNOWN;
                            ims.mChangedDelta = 0;
                            ims.mContentChanged = false;
return true;
}
}
//Synthetic comment -- @@ -6165,8 +6170,8 @@
ims.mChangedStart = start;
ims.mChangedEnd = start+before;
} else {
                ims.mChangedStart = Math.min(ims.mChangedStart, start);
                ims.mChangedEnd = Math.max(ims.mChangedEnd, start + before - ims.mChangedDelta);
}
ims.mChangedDelta += after-before;
}







