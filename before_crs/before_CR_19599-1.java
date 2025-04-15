/*Improve performance of WindowState.toString()

This fix improves the performance by caching the string that should
be returned, and reuse it next time if possible.
This will make it faster to switch between activities, approximately
half the time to create the new view when changing from landscape to
portrait. Also, the time for starting a new application is be reduced
as WindowState.toString is being called thousands of times in this
case.

Change-Id:I2b8b9bc1e251d1af43b6c85f049c01452f2573a2*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 83d65fa..f605c11 100644

//Synthetic comment -- @@ -6005,6 +6005,11 @@
// Input channel
InputChannel mInputChannel;

WindowState(Session s, IWindow c, WindowToken token,
WindowState attachedWindow, WindowManager.LayoutParams a,
int viewVisibility) {
//Synthetic comment -- @@ -7262,9 +7267,14 @@

@Override
public String toString() {
            return "Window{"
                + Integer.toHexString(System.identityHashCode(this))
                + " " + mAttrs.getTitle() + " paused=" + mToken.paused + "}";
}
}








