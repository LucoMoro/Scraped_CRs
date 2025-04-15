/*Fixing toast remaining on screen and not go to sleep mode

Added code to dismiss toast when exiting out gallery.
Appears App.java is not using the main UI looper. One
is causing "sending message to a Handler on a dead thread"
exception. Also, it could cause the Toast not to hide. As
a result, the screen won't go to sleep mode. To pop up
toast from IntentService, we need to ensure toast is made
from main UI thread.

Change-Id:I456500410399def977392dcf7bee11071dbb8974*/
//Synthetic comment -- diff --git a/src/com/cooliris/app/App.java b/src/com/cooliris/app/App.java
//Synthetic comment -- index 5281670..717d7c9 100644

//Synthetic comment -- @@ -20,7 +20,9 @@
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.HashMap;
//Synthetic comment -- @@ -43,11 +45,11 @@
public static float PIXEL_DENSITY = 0.0f;

	private final Context mContext;
    private final HandlerThread mHandlerThread = new HandlerThread("AppHandlerThread");
private final Handler mHandler;	
private ReverseGeocoder mReverseGeocoder = null;

private boolean mPaused = false;

	public App(Context context) {
		// register
//Synthetic comment -- @@ -61,13 +63,13 @@
			PIXEL_DENSITY = metrics.density;
		}

        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());		
		
	    mReverseGeocoder = new ReverseGeocoder(mContext);					
	}
		
	public void shutdown() {
mReverseGeocoder.shutdown();

// unregister
//Synthetic comment -- @@ -118,11 +120,28 @@
//
//    public void onDestroy() {
//    }
    
    public void showToast(final String string, final int duration) {
mHandler.post(new Runnable() {
public void run() {
                Toast.makeText(mContext, string, duration).show();
}
});
}








//Synthetic comment -- diff --git a/src/com/cooliris/cache/CacheService.java b/src/com/cooliris/cache/CacheService.java
//Synthetic comment -- index c8a5fc0..58fb72d 100644

//Synthetic comment -- @@ -378,7 +378,8 @@
if (!isCacheReady(true)) {
// In this case, we should try to show a toast
if (context instanceof Gallery) {
                App.get(context).showToast(context.getResources().getString(Res.string.loading_new), Toast.LENGTH_LONG);
}
if (DEBUG)
Log.d(TAG, "Refreshing Cache for all items");








//Synthetic comment -- diff --git a/src/com/cooliris/media/Gallery.java b/src/com/cooliris/media/Gallery.java
//Synthetic comment -- index bfa2575..8514951 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
res = Res.string.no_usb_storage;
}

                mApp.showToast(getResources().getString(res), Toast.LENGTH_LONG);
}
handler.sendEmptyMessageDelayed(CHECK_STORAGE, 200);
} else {
//Synthetic comment -- @@ -260,7 +260,7 @@
public void onDestroy() {
// Force GLThread to exit.
setContentView(Res.layout.main);

// Remove any post messages.
handler.removeMessages(CHECK_STORAGE);
handler.removeMessages(HANDLE_INTENT);
//Synthetic comment -- @@ -281,7 +281,6 @@
mRenderView = null;
}
mGridLayer = null;
        mApp.shutdown();
super.onDestroy();
Log.i(TAG, "onDestroy");
}
//Synthetic comment -- @@ -397,7 +396,8 @@
}
mGridLayer.setPickIntent(true);
if (hasStorage) {
                    mApp.showToast(getResources().getString(Res.string.pick_prompt), Toast.LENGTH_LONG);
}
}
} else { // view intent for images and review intent for images and videos








//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaFeed.java b/src/com/cooliris/media/MediaFeed.java
//Synthetic comment -- index c190ef9..566fd70 100644

//Synthetic comment -- @@ -501,17 +501,7 @@

private void showToast(final String string, final int duration, final boolean centered) {
if (mContext != null && !App.get(mContext).isPaused()) {
            App.get(mContext).getHandler().post(new Runnable() {
                public void run() {
                    if (mContext != null) {
                        Toast toast = Toast.makeText(mContext, string, duration);
                        if (centered) {
                            toast.setGravity(Gravity.CENTER, 0, 0);
                        }
                        toast.show();
                    }
                }
            });
}
}








