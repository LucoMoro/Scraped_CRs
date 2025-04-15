/*Auto hide virtual keyboard

Sometimes the virtual keyboard was not hidden when switching between
applications. An example of this was when launching the browser from
the Google Search widget:
1) Tap the Google Search widget and enter some text, e.g. "google"
2) Select one search items, e.g. "google maps"
3) Browser opens. Press back button.
4) Select an item again, e.g. "google maps" - Keyboard does not
   close.

When switching application, the virtual keyboard needs to find a new
Z position (window index) among the other windows. Normally it is
placed on top of the first window that is visible and can get focus
(canBeImeTarget()).

With a new application being launched, there is
an exception: a special "starting window" is placed on top of the
Activity window while the application is starting up. Since this
window should not get input, we need to look below that window.
When doing this, the previous implementation assumed that the
first window below always was focusable. If it wasn't, the
input method was placed above the "starting window", which
caused confusion that led to the keyboard not being closed
automatically.

In the case of the Browser, it sometimes has a "fake TitleBar"
window that can not get focus and that is placed above the
Activity window.

With this fix, we now keep looking through the windows below
the "starting window" until we find a window that can receive
input.

Change-Id:I1117846eb0f57603e64329bd955e28182f98f226*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 667b544..26cf55f 100644

//Synthetic comment -- @@ -929,6 +929,10 @@
&& w.mAttrs.type == WindowManager.LayoutParams.TYPE_APPLICATION_STARTING
&& i > 0) {
WindowState wb = localmWindows.get(i-1);
if (wb.mAppToken == w.mAppToken && canBeImeTarget(wb)) {
i--;
w = wb;







