/*Launcher: Fix delay responding when dragging an icon with two fingers

This issue is because Launcher app do not catch ACTION_POINTER_DOWN and
ACTION_POINTER_UP events, so it does not move the DragView to the second
finger until there is a ACTION_MOVE event.

Add ACTION_POINTER_DOWN and ACTION_POINTER_UP event handling to OnTouchEvent
method will fix this issue.

Change-Id:Ibf755ca4ab128f949fb628758857a7181ed0f29cAuthor: Jian Li <jian.d.li@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18432*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/DragController.java b/src/com/android/launcher2/DragController.java
//Synthetic comment -- index 84f1515..39f865c 100644

//Synthetic comment -- @@ -575,6 +575,12 @@
mScrollState = SCROLL_OUTSIDE_ZONE;
}
break;
        case MotionEvent.ACTION_POINTER_UP:
            // Get the last pointer index
            int index = ev.getPointerCount() - 1;
            mDragObject.dragView.move((int)ev.getX(index), (int)ev.getY(index));
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
case MotionEvent.ACTION_MOVE:
handleMoveEvent(dragLayerX, dragLayerY);
break;







