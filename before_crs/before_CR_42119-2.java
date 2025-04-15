/*Fix starting window memory leak

If starting window is added to arrays like mViews in
WindowManagerImpl.java, but not accepted by WindowManagerService,
we leak starting windows. To avoid leaking, remove the view
from WindowManager.

Change-Id:I4d98b883e9dfaf5e71bdece385643ba1b59b2633*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index d9c07f8a..8cd9f08 100644

//Synthetic comment -- @@ -1521,6 +1521,9 @@
return null;
}

try {
Context context = mContext;
if (DEBUG_STARTING_WINDOW) Slog.d(TAG, "addStartingWindow " + packageName
//Synthetic comment -- @@ -1580,8 +1583,8 @@
params.privateFlags |= WindowManager.LayoutParams.PRIVATE_FLAG_SHOW_FOR_ALL_USERS;
params.setTitle("Starting " + packageName);

            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            View view = win.getDecorView();

if (win.isFloating()) {
// Whoops, there is no way to display an animation/preview
//Synthetic comment -- @@ -1611,6 +1614,11 @@
// failure loading resources because we are loading from an app
// on external storage that has been unmounted.
Log.w(TAG, appToken + " failed creating starting window", e);
}

return null;







