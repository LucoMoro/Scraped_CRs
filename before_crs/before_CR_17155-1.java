/*Fix if ACTION_DOWN and lost focus time too close, it will become long press.

It will become long press because CheckForTap is still posted in background.
So remove the callback when lose focus, or it will become long press event.

Change-Id:I4f98a6fc077d256edbe555464095b2b81e75dd41*/
//Synthetic comment -- diff --git a/core/java/android/view/View.java b/core/java/android/view/View.java
//Synthetic comment -- index 11e5ad1..30aebc9 100644

//Synthetic comment -- @@ -3808,6 +3808,7 @@
imm.focusOut(this);
}
removeLongPressCallback();
onFocusLost();
} else if (imm != null && (mPrivateFlags & FOCUSED) != 0) {
imm.focusIn(this);







