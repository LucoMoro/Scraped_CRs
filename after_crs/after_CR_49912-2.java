/*Fix potential deadlock between LockScreen and WindowManagerService

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

Change-Id:I2a6a5c74ed22d8e6b7a3bea3424ff2879d227105Signed-off-by: Sangkyu Lee <sk82.lee@lge.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index a1fc4e0..cb7d712 100644

//Synthetic comment -- @@ -9406,9 +9406,29 @@
if (DEBUG_ORIENTATION &&
winAnimator.mDrawState == WindowStateAnimator.DRAW_PENDING) Slog.i(
TAG, "Resizing " + win + " WITH DRAW PENDING");
                if (win.mClient instanceof IWindow.Stub) {
                    // Simulate one-way call if win.mClient is a local object.
                    final IWindow client = win.mClient;
                    final Rect frame = win.mFrame;
                    final Rect contentInsets = win.mLastContentInsets;
                    final Rect visibleInsets = win.mLastVisibleInsets;
                    final boolean reportDraw = winAnimator.mDrawState == WindowStateAnimator.DRAW_PENDING;
                    final Configuration newConfig = configChanged ? win.mConfiguration : null;
                    mH.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.resized(frame, contentInsets, visibleInsets, reportDraw, newConfig);
                            } catch (RemoteException e) {
                                // Actually, it's not a remote call. RemoteException mustn't be raised.
                            }
                        }
                    });
                } else {
                    win.mClient.resized(win.mFrame, win.mLastContentInsets, win.mLastVisibleInsets,
                            winAnimator.mDrawState == WindowStateAnimator.DRAW_PENDING,
                            configChanged ? win.mConfiguration : null);
                }
win.mContentInsetsChanged = false;
win.mVisibleInsetsChanged = false;
winAnimator.mSurfaceResized = false;







