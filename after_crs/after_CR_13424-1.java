/*Fix Scroller for overshooting interpolators

The termination criteria was previously based on the final position. However, some
new Interpolators introduced in Android API 4 overshoot their final position.
As a result, the termination criteria must be done on the time elapsed during
the beginning of the animation only.*/




//Synthetic comment -- diff --git a/core/java/android/widget/Scroller.java b/core/java/android/widget/Scroller.java
//Synthetic comment -- index 11dab02..f1bf8c2 100644

//Synthetic comment -- @@ -203,9 +203,6 @@

mCurrX = mStartX + Math.round(x * mDeltaX);
mCurrY = mStartY + Math.round(x * mDeltaY);
break;
case FLING_MODE:
float timePassedSeconds = timePassed / 1000.0f;







