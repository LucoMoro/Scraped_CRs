/*Phone: IncomingCallWidget shows indefinitely sometimes.

This issue happens when following 3 conditions meet.
Usually cpu load is high at the moment.

1) Two ACTION_UP events on glow view is triggered;
2) Time from press the answer button to call state
   changed is more than 500ms;
3) Aniation of HideIncomingCallWidget did not start
   before handle 2nd event

Reset mIncomingCallWidgetIsFadingOut to false while
cancel HideIncomingCallWidget Animation, next time
HideIncomingCallWidget will be excuted when call
state is changed.

CQ00024038

Change-Id:I44ad728cdd624d2053397f463e44024527671840Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallTouchUi.java b/src/com/android/phone/InCallTouchUi.java
//Synthetic comment -- index 986765e..a36fc14 100644

//Synthetic comment -- @@ -1164,6 +1164,10 @@
ViewPropertyAnimator animator = mIncomingCallWidget.animate();
if (animator != null) {
animator.cancel();
}
mIncomingCallWidget.setAlpha(1.0f);








