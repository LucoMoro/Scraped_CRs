/*Add initial and final MOVE events on drag.

Change-Id:I88dfc808f34a862941640ad8c9ddd49051e85f72*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index e7e2e1c..203be04 100644

//Synthetic comment -- @@ -515,6 +515,7 @@
public void start(Point point) {
try {
manager.touchDown(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag start event", e);
}
//Synthetic comment -- @@ -528,6 +529,7 @@

public void end(Point point) {
try {
manager.touchUp(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag end event", e);







