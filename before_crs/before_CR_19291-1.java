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
switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
                // Make all touches hit either the textfield or the button,
                // depending on which side of the right edge of the textfield
                // they hit.
                if ((int) event.getX() > mTitleBg.getRight()) {
button.setPressed(true);
                } else {
mTitleBg.setPressed(true);
mHandler.sendMessageDelayed(mHandler.obtainMessage(
LONG_PRESS),
//Synthetic comment -- @@ -179,24 +183,13 @@
}
break;
case MotionEvent.ACTION_MOVE:
                int slop = ViewConfiguration.get(mBrowserActivity)
                        .getScaledTouchSlop();
                if ((int) event.getY() > getHeight() + slop) {
                    // We only trigger the actions in ACTION_UP if one or the
                    // other is pressed.  Since the user moved off the title
                    // bar, mark both as not pressed.
mTitleBg.setPressed(false);
                    button.setPressed(false);
mHandler.removeMessages(LONG_PRESS);
                    break;
}
                int x = (int) event.getX();
                int titleRight = mTitleBg.getRight();
                if (mTitleBg.isPressed() && x > titleRight + slop) {
                    mTitleBg.setPressed(false);
                    mHandler.removeMessages(LONG_PRESS);
                } else if (button.isPressed() && x < titleRight - slop) {
button.setPressed(false);
}
break;
case MotionEvent.ACTION_CANCEL:







