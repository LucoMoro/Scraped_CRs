/*Added null check on return value for getKeyDispatcherState()

The result from getKeyDispatcherState() was used without
checking if it returned null, which resulted in a NullPointerException.

Change-Id:I4b55ad44d5c08b7f729dbbdbcaed0e978a430258*/
//Synthetic comment -- diff --git a/core/java/android/widget/PopupWindow.java b/core/java/android/widget/PopupWindow.java
//Synthetic comment -- index 76755de..66524ab 100644

//Synthetic comment -- @@ -1421,6 +1421,10 @@
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
if (event.getAction() == KeyEvent.ACTION_DOWN
&& event.getRepeatCount() == 0) {
getKeyDispatcherState().startTracking(event, this);







