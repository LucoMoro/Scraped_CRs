/*duplicate window type check in WindowManagerService.relayoutWindow()

There is no necessary check window type in WindowManagerService.relayoutWindow()
It has already checked in ViewRootImpl.relayoutWindow() as below.

        if (params != null && mOrigWindowType != params.type) {
            // For compatibility with old apps, don't crash here.
            if (mTargetSdkVersion < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                Slog.w(TAG, "Window type can not be changed after "
                        + "the window is added; ignoring change of " + mView);
                params.type = mOrigWindowType;
            }
        }

Change-Id:I215b2bc8f27a7f5eb06f930fb73559038148faedSigned-off-by: p13451 <cheoloh.park@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index 3f72dec..233b40d 100644

//Synthetic comment -- @@ -2558,10 +2558,6 @@
int attrChanges = 0;
int flagChanges = 0;
if (attrs != null) {
                if (win.mAttrs.type != attrs.type) {
                    throw new IllegalArgumentException(
                            "Window type can not be changed after the window is added.");
                }
flagChanges = win.mAttrs.flags ^= attrs.flags;
attrChanges = win.mAttrs.copyFrom(attrs);
if ((attrChanges&WindowManager.LayoutParams.LAYOUT_CHANGED) != 0) {







