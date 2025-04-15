/*NPE fix in PhoneStatusBar.makeExpandedVisible

The crash occurs when you trying to expand the statusbar
and the TrackingView hasn't been attached to window yet.
The fix is to only allow this when the TrackingView
has been attached to window.

You can reproduce this easy by trying to expand
the status bar during boot
(try to expand the status bar as it was visible).

Change-Id:I20f4069c6a03bff7dde9d6d98c56fdcd6c36426a*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/phone/PhoneStatusBar.java b/packages/SystemUI/src/com/android/systemui/statusbar/phone/PhoneStatusBar.java
//Synthetic comment -- index 3c9d12c..5d55f43 100644

//Synthetic comment -- @@ -1461,6 +1461,10 @@
return false;
}

        if (!mTrackingView.mIsAttachedToWindow) {
            return false;
        }

final int action = event.getAction();
final int statusBarSize = mStatusBarView.getHeight();
final int hitSize = statusBarSize*2;








//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/phone/TrackingView.java b/packages/SystemUI/src/com/android/systemui/statusbar/phone/TrackingView.java
//Synthetic comment -- index cc23afc..955b93e 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
boolean mTracking;
int mStartX, mStartY;
Handler mHandler = new Handler();
    boolean mIsAttachedToWindow;

public TrackingView(Context context, AttributeSet attrs) {
super(context, attrs);
//Synthetic comment -- @@ -57,6 +58,13 @@
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

@Override







