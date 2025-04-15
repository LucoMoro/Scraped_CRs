/*Fix for a Launcher crash.

A forced close will occur in Launcher homescreen if you
are holding the edge portion of the touch screen and slide
close or open the touch screen. This is caused by the
display changing configurations but the touch events are
passed through the framework. So the coordinates will be
wrong which causes a out of bounds exception.

Added a bounds check for the pointer index on ACTION_UP.

Change-Id:I67d7e428f55694969d43948c37b6acda2a5c6eb6*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index c337c30..cba1a3b 100644

//Synthetic comment -- @@ -732,10 +732,12 @@
getLocationOnScreen(mTempCell);
// Send a tap to the wallpaper if the last down was on empty space
final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                        mWallpaperManager.sendWallpaperCommand(getWindowToken(), 
                                "android.wallpaper.tap",
                                mTempCell[0] + (int) ev.getX(pointerIndex),
                                mTempCell[1] + (int) ev.getY(pointerIndex), 0, null);
}
}








