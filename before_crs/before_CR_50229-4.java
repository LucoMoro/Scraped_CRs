/*Race condition in Toast.

1. mTN.mNextView set in toast.show() but can be invalided by posted
runnable mTN.mHide. If application call toast.cancel() right before
toast.show(), this problem happen.

2. mTN.hide() is called twice if toast.cancel() is called.

Change-Id:I554c5d44c59e18f5066f6175faec3e256b4e40d6*/
//Synthetic comment -- diff --git a/core/java/android/widget/Toast.java b/core/java/android/widget/Toast.java
//Synthetic comment -- index ab36139..da6d9c2 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
//Synthetic comment -- @@ -92,7 +93,7 @@
mTN.mY = context.getResources().getDimensionPixelSize(
com.android.internal.R.dimen.toast_y_offset);
}
    
/**
* Show the view for the specified duration.
*/
//Synthetic comment -- @@ -104,7 +105,7 @@
INotificationManager service = getService();
String pkg = mContext.getPackageName();
TN tn = mTN;
        tn.mNextView = mNextView;

try {
service.enqueueToast(pkg, tn, mDuration);
//Synthetic comment -- @@ -119,7 +120,7 @@
* after the appropriate duration.
*/
public void cancel() {
        mTN.hide();

try {
getService().cancelToast(mContext.getPackageName(), mTN);
//Synthetic comment -- @@ -127,7 +128,7 @@
// Empty
}
}
    
/**
* Set the view to show.
* @see #getView
//Synthetic comment -- @@ -160,7 +161,7 @@
public int getDuration() {
return mDuration;
}
    
/**
* Set the margins of the view.
*
//Synthetic comment -- @@ -216,7 +217,7 @@
public int getXOffset() {
return mTN.mX;
}
    
/**
* Return the Y offset in pixels to apply to the gravity's location.
*/
//Synthetic comment -- @@ -272,7 +273,7 @@
public void setText(int resId) {
setText(mContext.getText(resId));
}
    
/**
* Update the text in a Toast that was previously created using one of the makeText() methods.
* @param s The new text for the Toast.
//Synthetic comment -- @@ -304,31 +305,46 @@
}

private static class TN extends ITransientNotification.Stub {
        final Runnable mShow = new Runnable() {
            @Override
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            @Override
            public void run() {
                handleHide();
                // Don't do this in handleHide() because it is also invoked by handleShow()
                mNextView = null;
            }
        };

private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final Handler mHandler = new Handler();    

int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
int mX, mY;
float mHorizontalMargin;
float mVerticalMargin;

       
View mView;
View mNextView;

//Synthetic comment -- @@ -355,7 +371,7 @@
@Override
public void show() {
if (localLOGV) Log.v(TAG, "SHOW: " + this);
            mHandler.post(mShow);
}

/**
//Synthetic comment -- @@ -364,23 +380,29 @@
@Override
public void hide() {
if (localLOGV) Log.v(TAG, "HIDE: " + this);
            mHandler.post(mHide);
}

public void handleShow() {
            if (localLOGV) Log.v(TAG, "HANDLE SHOW: " + this + " mView=" + mView
                    + " mNextView=" + mNextView);
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
Context context = mView.getContext();
if (context.getApplicationContext() != null) {
// Use application context, except when called from system
// service where there is no application context.
context = context.getApplicationContext();
}
                mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
// We can resolve the Gravity here by using the Locale for getting
// the layout direction
final Configuration config = mView.getContext().getResources().getConfiguration();
//Synthetic comment -- @@ -420,7 +442,7 @@
event.setPackageName(mView.getContext().getPackageName());
mView.dispatchPopulateAccessibilityEvent(event);
accessibilityManager.sendAccessibilityEvent(event);
        }        

public void handleHide() {
if (localLOGV) Log.v(TAG, "HANDLE HIDE: " + this + " mView=" + mView);
//Synthetic comment -- @@ -432,8 +454,6 @@
if (localLOGV) Log.v(TAG, "REMOVE! " + mView + " in " + this);
mWM.removeView(mView);
}

                mView = null;
}
}
}







