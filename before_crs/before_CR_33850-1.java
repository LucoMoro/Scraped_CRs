/*Avoiding horizontal keypad navigation trapping within gallery.

Non touchscreen devices such as Google TV require the keypad
navigation to properly move the focus among widgets.

The Gallery is misshandling the keypress event and avoiding
the focus from going to other widgets on it's sides upon
keypress even after the user has reached it's first and
last items.

Change-Id:If32ee57e8513cac692deb161e1941d6cc85e3188Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/Gallery.java b/core/java/android/widget/Gallery.java
//Synthetic comment -- index 5e37fa8..ea23a3a 100644

//Synthetic comment -- @@ -1187,15 +1187,15 @@
case KeyEvent.KEYCODE_DPAD_LEFT:
if (movePrevious()) {
playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT);
}
            return true;

case KeyEvent.KEYCODE_DPAD_RIGHT:
if (moveNext()) {
playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT);
}
            return true;

case KeyEvent.KEYCODE_DPAD_CENTER:
case KeyEvent.KEYCODE_ENTER:
mReceivedInvokeKeyDown = true;







