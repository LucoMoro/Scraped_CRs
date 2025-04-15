/*Fix incorrect calculation of start and end indices of changed text

- The problem is taht the calculation of indices does not take the difference in original and modified text lengths into account.
- If reportExtractedText() is called from the onDraw() function, mInputMethodState is not reset.

The error can occur when the user is editing formatted text using a full screen editor.
One example of formatted text is phone numbers (e.g. dashes are added to North American
numbers by the PhoneNumberFormattingTextWatcher class).
If the two first digits are removed from the number 555-666-777, the new number becomes 566--777.
This is due incorrect calculation of start and end indices of the changed text. The error is in
the TextView class and the problem is that the calculation does not take the difference in original
and modified text lengths into account.*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index f55ca3f..51f4b1f 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -4512,6 +4513,9 @@
partialStartOffset = 0;
partialEndOffset = N;
} else {
// Adjust offsets to ensure we contain full spans.
if (content instanceof Spanned) {
Spanned spanned = (Spanned)content;
//Synthetic comment -- @@ -4527,10 +4531,7 @@
}
}
outText.partialStartOffset = partialStartOffset;
                    outText.partialEndOffset = partialEndOffset;
                    // Now use the delta to determine the actual amount of text
                    // we need.
                    partialEndOffset += delta;
if (partialEndOffset > N) {
partialEndOffset = N;
} else if (partialEndOffset < 0) {
//Synthetic comment -- @@ -4585,6 +4586,10 @@
+ ": " + ims.mTmpExtracted.text);
imm.updateExtractedText(this, req.token,
mInputMethodState.mTmpExtracted);
return true;
}
}
//Synthetic comment -- @@ -6158,8 +6163,8 @@
ims.mChangedStart = start;
ims.mChangedEnd = start+before;
} else {
                if (ims.mChangedStart > start) ims.mChangedStart = start;
                if (ims.mChangedEnd < (start+before)) ims.mChangedEnd = start+before;
}
ims.mChangedDelta += after-before;
}







