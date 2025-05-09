/*Fix for an infinite loop while scrolling lists.

When scrolling in a list it's possible to get stuck in a
loop where the screen is continuously redrawn. This happens
when you are at the end of a list and try to scroll further.
The problem is that you enter a switch statement with the
mState variable set to STATE_PULL_DECAY. This will trigger
code in the switch statement that does some adjustments and
calculations, but it does not change the value of mState to
STATE_IDLE or STATE_RECEDE after it's done. Hence it will
continue to return to the same place and not stop updating
since the update() method signals for completion only when
the mState variable is set to STATE_IDLE.

The fix is changing the value of mState after the
adjustments and calculations have been made.

Change-Id:I57bc84ec12d43ca87a1163d94cb5b206a376a24e*/
//Synthetic comment -- diff --git a/core/java/android/widget/EdgeGlow.java b/core/java/android/widget/EdgeGlow.java
//Synthetic comment -- index 416be86..a11de6f 100644

//Synthetic comment -- @@ -317,6 +317,7 @@
mEdgeScaleY = mEdgeScaleYStart +
(mEdgeScaleYFinish - mEdgeScaleYStart) *
interp * factor;
break;
case STATE_RECEDE:
mState = STATE_IDLE;







