/*Gallery3D: Add depth component to icon animation

When the Gallery application positions the thumbnails
in a GridView, they are placed in the same z-plane.
When a configuration change occurs the icons present
a z-fighting visual artifact.

This patch adds a z component to the thumbnails when they are
animated as to mitigate this effect. This is specially
noticeable in a GPU architecture using deferred rendering.

Change-Id:Icc9bb782b420697f282ed2b10a699b36d09c7abfSigned-off-by: Rodrigo Obregon <robregon@ti.com>*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 74aff5d..84d5df0 100644

//Synthetic comment -- @@ -82,6 +82,7 @@

private final LayoutInterface mLayoutInterface;
private static final LayoutInterface sfullScreenLayoutInterface = new GridLayoutInterface(1);

private MediaFeed mMediaFeed;
private boolean mInAlbum = false;
//Synthetic comment -- @@ -116,6 +117,7 @@
private String mRequestFocusContentUri;
private int mFrameCount;
private boolean mRequestToEnterSelection;

// private ArrayList<Integer> mBreakSlots = new ArrayList<Integer>();
// private ArrayList<Integer> mOldBreakSlots;
//Synthetic comment -- @@ -678,6 +680,15 @@
displayItem.set(position, j, false);
displayItem.commit();
} else {
displayList.setPositionAndStackIndex(displayItem, position, j, true);
}
displayItems[baseIndex + j] = displayItem;
//Synthetic comment -- @@ -690,6 +701,7 @@
bestItems.clear();
}
}
if (mFeedChanged) {
mFeedChanged = false;
if (mInputProcessor != null && mState == STATE_FULL_SCREEN && mRequestFocusContentUri == null) {
//Synthetic comment -- @@ -872,6 +884,7 @@
deltaAnchorPosition.subtract(currentSlotPosition);
deltaAnchorPosition.y = 0;
deltaAnchorPosition.z = 0;
}
mDeltaAnchorPositionUncommited.set(deltaAnchorPosition);
} finally {







