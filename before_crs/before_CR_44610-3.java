/*Camera : Ignore keyevents when in gallery

When in gallery view (via swipe or thumbnail), ignore any keyevents.

Patchset 2 : Make return call appopriate super method
Patchset 3 : Ignore key events for VideoCamera as well

Change-Id:Ie3f0ce3f26a13f5ee45b13927ee91d8fae991209*/
//Synthetic comment -- diff --git a/src/com/android/camera/Camera.java b/src/com/android/camera/Camera.java
//Synthetic comment -- index 44e9d7a..263939b 100644

//Synthetic comment -- @@ -1833,6 +1833,11 @@

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_FOCUS:
if (mFirstTimeInitialized && event.getRepeatCount() == 0) {
//Synthetic comment -- @@ -1868,6 +1873,11 @@

@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_FOCUS:
if (mFirstTimeInitialized) {








//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index ae5176b..8894706 100755

//Synthetic comment -- @@ -1009,7 +1009,11 @@
if (mPaused) {
return true;
}

switch (keyCode) {
case KeyEvent.KEYCODE_CAMERA:
if (event.getRepeatCount() == 0) {
//Synthetic comment -- @@ -1033,6 +1037,11 @@

@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_CAMERA:
mShutterButton.setPressed(false);







