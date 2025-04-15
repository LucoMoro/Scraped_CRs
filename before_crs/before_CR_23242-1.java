/*Support a surrogate-pair for TextView

It is needed for handling Japanese emoji.

Change-Id:I5797f9cd073440ca82541595d2dc023c57364ee7*/
//Synthetic comment -- diff --git a/core/java/android/text/Selection.java b/core/java/android/text/Selection.java
//Synthetic comment -- index 13cb5e6..3b829ab 100644

//Synthetic comment -- @@ -370,7 +370,7 @@
if (line == layout.getLineCount() - 1)
return end;
else
                return end - 1;
}
}









//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 68600cf..e88664a 100644

//Synthetic comment -- @@ -8142,7 +8142,7 @@
// If the user "closes" the selection entirely they were probably trying to
// select a single character. Help them out.
if (offset == selectionEnd) {
                    offset = selectionEnd - 1;
}
selectionStart = offset;
} else {
//Synthetic comment -- @@ -8152,7 +8152,7 @@
// If the user "closes" the selection entirely they were probably trying to
// select a single character. Help them out.
if (offset == selectionStart) {
                    offset = selectionStart + 1;
}
selectionEnd = offset;
}







