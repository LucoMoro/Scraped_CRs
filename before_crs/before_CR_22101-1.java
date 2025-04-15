/*Select cuts surrogate pairs

Selection does not consider surrogate pairs when selecting text. This
can lead to selection edges cutting complex characters in half.
setSelection now look at the char at the selection edges and move the
edge to include any complex character that might get cut by the
selection.

Change-Id:I6e790dda6c452b47a743920c0ae7c664c73ad0fc*/
//Synthetic comment -- diff --git a/core/java/android/text/Selection.java b/core/java/android/text/Selection.java
//Synthetic comment -- index 13cb5e6..2e926eb 100644

//Synthetic comment -- @@ -71,6 +71,18 @@
int oend = getSelectionEnd(text);

if (ostart != start || oend != stop) {
text.setSpan(SELECTION_START, start, start,
Spanned.SPAN_POINT_POINT|Spanned.SPAN_INTERMEDIATE);
text.setSpan(SELECTION_END, stop, stop,
//Synthetic comment -- @@ -79,6 +91,30 @@
}

/**
* Move the cursor to offset <code>index</code>.
*/
public static final void setSelection(Spannable text, int index) {








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/text/SelectionTest.java b/core/tests/coretests/src/android/text/SelectionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..37574c0

//Synthetic comment -- @@ -0,0 +1,105 @@







