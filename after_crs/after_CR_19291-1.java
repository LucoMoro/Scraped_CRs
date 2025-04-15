/*Facilitate mirroring of the Browser TitleBar

The touch event handling code of the TitleBar
makes assumptions where the button is located
and where the input field is located. This makes
it hard to mirror the layout (implementing BIDI support)
without making changes to the java code.

This change makes it easier to implement mirroring
by checking if the touch event is inside the button
or input field making it independent of where they
are placed inside the TitleBar.

Change-Id:Ic78d7340ecdd16e838c4f169e48e8cb57bd825b0*/




//Synthetic comment -- diff --git a/src/com/android/browser/TitleBar.java b/src/com/android/browser/TitleBar.java
//Synthetic comment -- index dc4979b..0022057 100644

//Synthetic comment -- @@ -164,14 +164,18 @@
@Override
public boolean onTouchEvent(MotionEvent event) {
ImageView button = mInLoad ? mStopButton : mRtButton;
        int slop = ViewConfiguration.get(mBrowserActivity).getScaledTouchSlop();
        // Use the slop to make the touch area bigger for bookmarks and stop button
        Rect buttonRect = new Rect(button.getLeft() - slop, 0, button.getRight() + slop,
                button.getHeight());
        Rect titleRect = new Rect(mTitleBg.getLeft(), 0, mTitleBg.getRight(), mTitleBg.getHeight());
        int x = (int)event.getX();
        int y = (int)event.getY();
switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
                if (buttonRect.contains(x, y)) {
button.setPressed(true);
                } else if (titleRect.contains(x, y)) {
mTitleBg.setPressed(true);
mHandler.sendMessageDelayed(mHandler.obtainMessage(
LONG_PRESS),
//Synthetic comment -- @@ -179,24 +183,13 @@
}
break;
case MotionEvent.ACTION_MOVE:
                if (mTitleBg.isPressed() && !titleRect.contains(x, y)) {
mTitleBg.setPressed(false);
mHandler.removeMessages(LONG_PRESS);
}
                if (button.isPressed() && !buttonRect.contains(x, y)) {
button.setPressed(false);
                    mHandler.removeMessages(LONG_PRESS);
}
break;
case MotionEvent.ACTION_CANCEL:







