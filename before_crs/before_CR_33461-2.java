/*Adding the function which fill color if it clicks pressing the Control key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404c*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..6b936d9 100644

//Synthetic comment -- @@ -728,6 +728,9 @@
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
paint(event.getX(), event.getY(), currentButton);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -881,6 +884,72 @@
}
}

private boolean checkLockedRegion(int x, int y) {
int oldX = lastPositionX;
int oldY = lastPositionY;







