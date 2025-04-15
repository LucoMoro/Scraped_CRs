/*NPE fix in StatusBarService.makeExpandedVisible

The crash occurs when you trying to expand the statusbar
and the TrackingView hasn't been attached to window yet.
The fix is to only allow this when the TrackingView
has been attached to window.

You can reproduce this easy by trying to expand
the status bar during boot
(try to expand the status bar as it was visible).

Change-Id:I20f4069c6a03bff7dde9d6d98c56fdcd6c36426a*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/StatusBarService.java b/packages/SystemUI/src/com/android/systemui/statusbar/StatusBarService.java
//Synthetic comment -- index de30ccd..612584f0 100644

//Synthetic comment -- @@ -942,6 +942,10 @@
return false;
}

        if (!mTrackingView.mIsAttachedToWindow) {
            return false;
        }

final int statusBarSize = mStatusBarView.getHeight();
final int hitSize = statusBarSize*2;
if (event.getAction() == MotionEvent.ACTION_DOWN) {








//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/TrackingView.java b/packages/SystemUI/src/com/android/systemui/statusbar/TrackingView.java
//Synthetic comment -- index 9108eee..7787f088 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
StatusBarService mService;
boolean mTracking;
int mStartX, mStartY;
    boolean mIsAttachedToWindow;

public TrackingView(Context context, AttributeSet attrs) {
super(context, attrs);
//Synthetic comment -- @@ -59,5 +60,12 @@
protected void onAttachedToWindow() {
super.onAttachedToWindow();
mService.onTrackingViewAttached();
        mIsAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsAttachedToWindow = false;
}
}







