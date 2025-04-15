/*Code refactoring in computeScrollOffset(). The computed time between the start of the scrolling and the current time is equivalent to the time the scrolling is performing (timePassed()).*/
//Synthetic comment -- diff --git a/core/java/android/widget/Scroller.java b/core/java/android/widget/Scroller.java
//Synthetic comment -- index c9ace0a..68b1143 100644

//Synthetic comment -- @@ -177,8 +177,8 @@
return false;
}

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    
if (timePassed < mDuration) {
switch (mMode) {
case SCROLL_MODE:







