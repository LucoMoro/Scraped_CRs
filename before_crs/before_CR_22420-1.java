/*Removing redundant null check. It is done already, as well as field is dereferenced above.

Change-Id:I33feb3b7d1d62938a6118554a7d6ab7a113079f8*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewRoot.java b/core/java/android/view/ViewRoot.java
//Synthetic comment -- index ccaef40..3c26525 100644

//Synthetic comment -- @@ -2579,7 +2579,7 @@

if (direction != 0) {

                        View focused = mView != null ? mView.findFocus() : null;
if (focused != null) {
View v = focused.focusSearch(direction);
boolean focusPassed = false;







