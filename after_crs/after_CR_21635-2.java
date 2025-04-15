/*Add initial and final MOVE events on drag.

Change-Id:I88dfc808f34a862941640ad8c9ddd49051e85f72*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index befec28..41c4d02 100644

//Synthetic comment -- @@ -516,6 +516,7 @@
public void start(Point point) {
try {
manager.touchDown(point.getX(), point.getY());
                    manager.touchMove(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag start event", e);
}
//Synthetic comment -- @@ -529,6 +530,7 @@

public void end(Point point) {
try {
                    manager.touchMove(point.getX(), point.getY());
manager.touchUp(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag end event", e);







