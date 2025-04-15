/*Fix starting window memory leak

If starting window is added to arrays like mViews in
WindowManagerImpl.java, but not accepted by WindowManagerService,
we leak starting windows. To avoid leaking, remove the view
from WindowManager.

Change-Id:I4d98b883e9dfaf5e71bdece385643ba1b59b2633*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 25da642..dcbf76b 100755

//Synthetic comment -- @@ -1468,6 +1468,9 @@
return null;
}

        WindowManager wm = null;
        View view = null;

try {
Context context = mContext;
if (DEBUG_STARTING_WINDOW) Slog.d(TAG, "addStartingWindow " + packageName
//Synthetic comment -- @@ -1526,8 +1529,8 @@
WindowManager.LayoutParams.PRIVATE_FLAG_FAKE_HARDWARE_ACCELERATED;
params.setTitle("Starting " + packageName);

            wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            view = win.getDecorView();

if (win.isFloating()) {
// Whoops, there is no way to display an animation/preview
//Synthetic comment -- @@ -1557,6 +1560,11 @@
// failure loading resources because we are loading from an app
// on external storage that has been unmounted.
Log.w(TAG, appToken + " failed creating starting window", e);
        } finally {
            if (view != null && view.getParent() == null) {
                Log.w(TAG, "view not successfully added to wm, removing view");
                wm.removeViewImmediate(view);
            }
}

return null;







