/*Revert "Gallery3D: Add depth component to icon animation"

This reverts commit f3a57d42104c5df77624d38813747b1279525d50.*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 1bf7ebc..74aff5d 100644

//Synthetic comment -- @@ -82,7 +82,6 @@

private final LayoutInterface mLayoutInterface;
private static final LayoutInterface sfullScreenLayoutInterface = new GridLayoutInterface(1);
    private static final float DEPTH_POSITION = 0.5f;

private MediaFeed mMediaFeed;
private boolean mInAlbum = false;
//Synthetic comment -- @@ -679,14 +678,6 @@
displayItem.set(position, j, false);
displayItem.commit();
} else {
                                        boolean isTouchPressed = mInputProcessor.touchPressed();
                                        boolean isBarDragged = mHud.getTimeBar().isDragged();
                                        if (mState == STATE_GRID_VIEW
                                                && !isTouchPressed
                                                && !isBarDragged) {
                                            displayItem.mAnimatedPosition.add(
                                                0.0f, 0.0f, i * DEPTH_POSITION);
                                        }
displayList.setPositionAndStackIndex(displayItem, position, j, true);
}
displayItems[baseIndex + j] = displayItem;







