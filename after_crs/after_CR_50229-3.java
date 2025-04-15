/*Race condition in Toast.

1. mTN.mNextView set in toast.show() but can be invalided by posted
runnable mTN.mHide. If application call toast.cancel() right before
toast.show(), this problem happen.

2. mTN.hide() is called twice if toast.cancel() is called.

Change-Id:I554c5d44c59e18f5066f6175faec3e256b4e40d6*/




//Synthetic comment -- diff --git a/core/java/android/widget/Toast.java b/core/java/android/widget/Toast.java
//Synthetic comment -- index ab36139..ac9667d 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
//Synthetic comment -- @@ -58,7 +59,7 @@
*/ 
public class Toast {
static final String TAG = "Toast";
    static final boolean localLOGV = true;

/**
* Show the view or text notification for a short period of time.  This time
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
        tn.setNextView(mNextView);

try {
service.enqueueToast(pkg, tn, mDuration);
//Synthetic comment -- @@ -119,7 +120,7 @@
* after the appropriate duration.
*/
public void cancel() {
        mTN.cancel();

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
        private static final int MSG_HIDE = 0;
        private static final int MSG_SHOW = 1;
        private static final int MSG_SETNEXT = 2;
        private static final int MSG_CANCEL = 3;

private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case MSG_HIDE:
                    handleHide();
                    mView = null;
                    mNextView = null;
                    break;
                case MSG_SHOW:
                    if (mView != mNextView && mNextView != null) {
                        // remove the old view if necessary
                        handleHide();
                        mView = mNextView;
                        mNextView = null;
                        handleShow();
                    }
                    break;
                case MSG_SETNEXT:
                    mNextView = (View) msg.obj;
                    break;
                case MSG_CANCEL:
                    mNextView = null;
                    break;
                }
            }
        };

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
            mHandler.sendEmptyMessage(MSG_SHOW);
}

/**
//Synthetic comment -- @@ -364,23 +380,29 @@
@Override
public void hide() {
if (localLOGV) Log.v(TAG, "HIDE: " + this);
            mHandler.sendEmptyMessage(MSG_HIDE);
        }

        public void cancel() {
            mHandler.removeMessages(MSG_SHOW);
            mHandler.sendEmptyMessage(MSG_CANCEL);
        }

        public void setNextView(View nextView) {
            Message msg = mHandler.obtainMessage(MSG_SETNEXT, nextView);
            mHandler.sendMessage(msg);
}

public void handleShow() {
            if (localLOGV) Log.v(TAG, "HANDLE SHOW: " + this + " mView=" + mView);
            if (mView != null) {
Context context = mView.getContext();
if (context.getApplicationContext() != null) {
// Use application context, except when called from system
// service where there is no application context.
context = context.getApplicationContext();
}
                mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
}
}
}







