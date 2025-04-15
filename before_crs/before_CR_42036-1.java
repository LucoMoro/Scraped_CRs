/*Composing text is not cleared if you write too fast

In some scenarios when you write very fast, onUpdateSelection
is called with delay. This can lead to a incorrect state in
the input methods.
Current change notifies InputMethod as soon as a cursor position has
been changed.

Change-Id:I5064d52f576225f79544e06e3de2d2acef30c597*/
//Synthetic comment -- diff --git a/core/java/android/widget/Editor.java b/core/java/android/widget/Editor.java
//Synthetic comment -- index c29dd58..1a5669ad 100644

//Synthetic comment -- @@ -1186,14 +1186,13 @@
InputMethodManager imm = InputMethodManager.peekInstance();
if (imm != null) {
if (imm.isActive(mTextView)) {
                    boolean reported = false;
if (ims.mContentChanged || ims.mSelectionModeChanged) {
// We are in extract mode and the content has changed
// in some way... just report complete new text to the
// input method.
                        reported = reportExtractedText();
}
                    if (!reported && highlight != null) {
int candStart = -1;
int candEnd = -1;
if (mTextView.getText() instanceof Spannable) {







