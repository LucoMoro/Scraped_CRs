/*Gallery3D: Remove unnecessary variables

This patch removes the variables isTouchPressed
and isBarDragged introduced in the patch
"Gallery3D: Add depth component to icon animation".

This variables are no longer used in the current
version of the patch.

Change-Id:I5c35d1f7e39b45ea13cff4a430ca35e668cef53cSigned-off-by: Rodrigo Obregon <robregon@ti.com>*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index b8c2f75..119ddd9 100644

//Synthetic comment -- @@ -680,8 +680,6 @@
displayItem.set(position, j, false);
displayItem.commit();
} else {
                                        boolean isTouchPressed = mInputProcessor.touchPressed();
                                        boolean isBarDragged = mHud.getTimeBar().isDragged();
if (mState == STATE_GRID_VIEW
&& mLayoutChanged) {
displayItem.mAnimatedPosition.add(







