/*Fix potential deadlock between LockScreen and WindowMaanagerService

If LockScreen is enhanced using SurfaceView/GLSurfaceView,
deadlock problem between LockScreen and WindowManagerService
can occur because of IWindow.resized() callback.
And it must lead to watchdog and reset.

IWindow.resized() callback is one-way function so calling resized()
callback of a remote IWindow object is never blocked.
However, calling resized() callback of a local IWindow object
(LockScreen is running on the same system_server process)
is always blocked until resized() callback returns.
Because resized() callback of SurfaceView/GLSurfaceView can lead to
WindowManagerService.relayoutWindow() call, deadlock can occur
between relayoutWindow() and performLayoutAndPlaceSurfacesLockedInner().
(Both functions need locking mWindowMap)

So this patch simulate one-way call when calling resized() callback
of a local IWindow object.

Change-Id:Iccdedf135994cc06ceb3706a2c3659cacc683424Signed-off-by: Sangkyu Lee <sk82.lee@lge.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index a1fc4e0..cb7d712 100644

//Synthetic comment -- @@ -9406,9 +9406,29 @@
if (DEBUG_ORIENTATION &&
winAnimator.mDrawState == WindowStateAnimator.DRAW_PENDING) Slog.i(
TAG, "Resizing " + win + " WITH DRAW PENDING");
                win.mClient.resized(win.mFrame, win.mLastContentInsets, win.mLastVisibleInsets,
                        winAnimator.mDrawState == WindowStateAnimator.DRAW_PENDING,
                        configChanged ? win.mConfiguration : null);
win.mContentInsetsChanged = false;
win.mVisibleInsetsChanged = false;
winAnimator.mSurfaceResized = false;







