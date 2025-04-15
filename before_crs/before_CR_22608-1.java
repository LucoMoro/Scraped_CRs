/*Reduced animation duration

Reduced animation duration for panel switching within Calculator from 500 to
200, as 500 made calculator seem slow and laggy

Change-Id:Id06d7c97b13b952655f04709cea7707aa55fb95c*/
//Synthetic comment -- diff --git a/src/com/android/calculator2/PanelSwitcher.java b/src/com/android/calculator2/PanelSwitcher.java
//Synthetic comment -- index cc62848..86eefc1 100644

//Synthetic comment -- @@ -26,7 +26,7 @@

class PanelSwitcher extends FrameLayout {
private static final int MAJOR_MOVE = 60;
    private static final int ANIM_DURATION = 400;

private GestureDetector mGestureDetector;
private int mCurrentView;







