/*Fix reporting of window visibility in WindowManagerService.

WindowManagerService (WMS) can wrongly report windows visibility due
to wrong handling of "starting windows".
"Starting windows" are special temporary windows that are displayed
while the application is starting.
Sometimes "starting windows" are considered when checking visibility
what leads to not reported or wrongly reported visibility status.
If visibility is not reported correctly some internal flows are
not executed and WMS internal state can be wrong.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 24caf1f..d209cfa 100644

//Synthetic comment -- @@ -8520,7 +8520,8 @@
final int N = allAppWindows.size();
for (int i=0; i<N; i++) {
WindowState win = allAppWindows.get(i);
                if (win == startingWindow || win.mAppFreezing
                    || win.mAttrs.type == TYPE_APPLICATION_STARTING) {
continue;
}
if (DEBUG_VISIBILITY) {







