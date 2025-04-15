/*Cleanup fixes for ScaleGestureDetector*/
//Synthetic comment -- diff --git a/core/java/android/view/ScaleGestureDetector.java b/core/java/android/view/ScaleGestureDetector.java
//Synthetic comment -- index f991df7..f4215a8 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
* Detects transformation gestures involving more than one pointer ("multitouch")
//Synthetic comment -- @@ -45,9 +44,9 @@
* 
* An application will receive events in the following order:
* <ul>
     *  <li>One {@link OnScaleGestureListener#onScaleBegin()}
     *  <li>Zero or more {@link OnScaleGestureListener#onScale()}
     *  <li>One {@link OnScaleGestureListener#onTransformEnd()}
* </ul>
*/
public interface OnScaleGestureListener {
//Synthetic comment -- @@ -82,8 +81,7 @@

/**
* Responds to the end of a scale gesture. Reported by existing
         * pointers going up. If the end of a gesture would result in a fling,
         * {@link onTransformFling()} is called instead.
* 
* Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
* and {@link ScaleGestureDetector#getFocusY()} will return the location
//Synthetic comment -- @@ -103,7 +101,7 @@
* {@link OnScaleGestureListener#onScaleBegin(ScaleGestureDetector)} return
* {@code true}. 
*/
    public class SimpleOnScaleGestureListener implements OnScaleGestureListener {

public boolean onScale(ScaleGestureDetector detector) {
return true;
//Synthetic comment -- @@ -373,7 +371,7 @@
* the two pointers forming the gesture.
* If a gesture is ending, the focal point is the location of the
* remaining pointer on the screen.
     * If {@link isInProgress()} would return false, the result of this
* function is undefined.
* 
* @return X coordinate of the focal point in pixels.
//Synthetic comment -- @@ -388,7 +386,7 @@
* the two pointers forming the gesture.
* If a gesture is ending, the focal point is the location of the
* remaining pointer on the screen.
     * If {@link isInProgress()} would return false, the result of this
* function is undefined.
* 
* @return Y coordinate of the focal point in pixels.
//Synthetic comment -- @@ -430,7 +428,7 @@
/**
* Return the scaling factor from the previous scale event to the current
* event. This value is defined as
     * ({@link getCurrentSpan()} / {@link getPreviousSpan()}).
* 
* @return The current scaling factor.
*/







