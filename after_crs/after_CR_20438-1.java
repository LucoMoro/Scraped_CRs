/*Adjust mBiggerTouchSlopSquare to the suitable value

If the scaling factor is larger than 1.0 (i.e. 1.5),
then mTouchSlopSquare(576) is bigger than mBiggerTouchSlopSquare(400).
The double tap condition should be bigger than a single tap's one.
This causes the fail of the following CTS test cases in the device has
over 240 density.
- android.view.cts.GestureDetectorTest
  * testOnTouchEvent
- android.view.cts.GestureDetector_SimpleOnGestureListenerTest
  * testSimpleOnGestureListener

Change-Id:I0e61c13670e1300be1ccf45a89ef89410496fb48*/




//Synthetic comment -- diff --git a/core/java/android/view/GestureDetector.java b/core/java/android/view/GestureDetector.java
old mode 100644
new mode 100755
//Synthetic comment -- index c1e1049..9c4d1c4

//Synthetic comment -- @@ -193,10 +193,8 @@
}
}

private int mTouchSlopSquare;
    private int mBiggerTouchSlopSquare;
private int mDoubleTapSlopSquare;
private int mMinimumFlingVelocity;
private int mMaximumFlingVelocity;
//Synthetic comment -- @@ -400,6 +398,8 @@
mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
}
mTouchSlopSquare = touchSlop * touchSlop;
        // This should be a bit bigger than mTouchSlopSquare
        mBiggerTouchSlopSquare = (int)(mTouchSlopSquare * 1.2f);
mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
}








