/*The bigger touch slop still has a problem

I had submitted the patch about this issue athttps://android-review.googlesource.com/#/c/20438/before. But it
has never been included in any version.

In the latest implementation, touchSlop is a configurable value
which is declared in config.xml for each device. First of all,
the problem is that BiggerTouchSlop is not scalable and variable
value according to a configured touchSlop.

I don't think that there should be a new api in ViewConfiguration
for this. Because the bigger touch slop is a local threshold value
in GestureDetector. The only thing to be satisfied is that the
value should be bigger than configured touch slop and should not
be over double touch slop.

Change-Id:I2c6662400fcffb4a7192ede4ac8da08559aa7948*/




//Synthetic comment -- diff --git a/core/java/android/view/GestureDetector.java b/core/java/android/view/GestureDetector.java
old mode 100644
new mode 100755
//Synthetic comment -- index a496a9e..5c8b236

//Synthetic comment -- @@ -193,8 +193,7 @@
}
}

    private int mBiggerTouchSlopSquare;

private int mTouchSlopSquare;
private int mDoubleTapSlopSquare;
//Synthetic comment -- @@ -408,6 +407,14 @@
}
mTouchSlopSquare = touchSlop * touchSlop;
mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;

        // The biggerTouchSlop should be a little bit bigger than touchSlop
        // and mBiggerTouchSlopSquare should not be over mDoubleTapSlopSquare.
        int biggerTouchSlop = (int)(touchSlop * 1.25f);
        mBiggerTouchSlopSquare = biggerTouchSlop * biggerTouchSlop;
        if (mBiggerTouchSlopSquare > mDoubleTapSlopSquare) {
            mBiggerTouchSlopSquare = mDoubleTapSlopSquare;
        }
}

/**







