/*Uploaded patch set 2.

Change-Id:I28a506f17fcd99c801a3faa17cca66fffb291596*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index a1fc4e0..b767778

//Synthetic comment -- @@ -161,6 +161,7 @@
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -4919,23 +4920,33 @@
final WindowList windows = win.getWindowList();
final int NCW = win.mChildWindows.size();
boolean added = false;
for (int j=0; j<NCW; j++) {
            WindowState cwin = win.mChildWindows.get(j);
            if (!added && cwin.mSubLayer >= 0) {
                if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding child window at "
                        + index + ": " + cwin);
win.mRebuilding = false;
windows.add(index, win);
index++;
added = true;
}
            if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding window at "
+ index + ": " + cwin);
cwin.mRebuilding = false;
windows.add(index, cwin);
index++;
}
if (!added) {
if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding window at "
+ index + ": " + win);
win.mRebuilding = false;
//Synthetic comment -- @@ -4946,6 +4957,22 @@
return index;
}

private final int reAddAppWindowsLocked(final DisplayContent displayContent, int index,
WindowToken token) {
final int NW = token.windows.size();







