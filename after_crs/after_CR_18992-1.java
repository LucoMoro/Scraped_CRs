/*Gallery3D: Add depth component to icon animation

When the Gallery application positions the thumbnails
in a GridView, they are placed in the same z-plane.
When a configuration change occurs the icons present
a z-fighting visual artifact.

This patch adds a z component to the thumbnails when they are
animated as to mitigate this effect. This is specially
noticeable in a GPU architecture using deferred rendering.

Change-Id:If78232058b71d482cde0dbb55a038ded9fdbdf76Signed-off-by: Rodrigo Obregon <robregon@ti.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 74aff5d..9ea6459 100644

//Synthetic comment -- @@ -82,6 +82,7 @@

private final LayoutInterface mLayoutInterface;
private static final LayoutInterface sfullScreenLayoutInterface = new GridLayoutInterface(1);
	private static final float DEPTH_POSITION = 0.5f;

private MediaFeed mMediaFeed;
private boolean mInAlbum = false;
//Synthetic comment -- @@ -678,7 +679,12 @@
displayItem.set(position, j, false);
displayItem.commit();
} else {
						if (mState == STATE_GRID_VIEW && !mInputProcessor.touchPressed() && !mHud.getTimeBar().isDragged()) {
							displayItem.mAnimatedPosition.add(0.0f, 0.0f, i * DEPTH_POSITION);
							displayList.setPositionAndStackIndex(displayItem, position, j, true);
						} else {
							displayList.setPositionAndStackIndex(displayItem, position, j, true);
						}
}
displayItems[baseIndex + j] = displayItem;
}







