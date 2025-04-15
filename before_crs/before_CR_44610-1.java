/*Camera : Ignore keyevents when in gallery

When in gallery view (via swipe or thumbnail), ignore any keyevents.

Change-Id:Ie3f0ce3f26a13f5ee45b13927ee91d8fae991209*/
//Synthetic comment -- diff --git a/src/com/android/camera/Camera.java b/src/com/android/camera/Camera.java
//Synthetic comment -- index 44e9d7a..7b0f76a 100644

//Synthetic comment -- @@ -1833,6 +1833,9 @@

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_FOCUS:
if (mFirstTimeInitialized && event.getRepeatCount() == 0) {
//Synthetic comment -- @@ -1868,6 +1871,9 @@

@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_FOCUS:
if (mFirstTimeInitialized) {







