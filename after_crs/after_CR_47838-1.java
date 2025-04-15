/*The order of child windows is switched

By calling WindowManagerService#reAddWindowLocked(), the window object
in DisplayContent.mWindows is rearranged. If this window has children,
the window objects belonging to WindowState.mChildWindows are also
rearranged together. At this time, while DisplayContent.mWindows is
arranged in ascending order of Z-Order, WindowState.mChildWindows is
arranged in descending order. Therefore, if children
(= WindowState.mChildWindows) are added to DisplayContent.mWindows
in the original order, the order of children is switched. I think
that children must be sorted before they are added.

Change-Id:I28a506f17fcd99c801a3faa17cca66fffb291596*/




//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index a1fc4e0..b927c55e

//Synthetic comment -- @@ -161,6 +161,7 @@
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -4919,22 +4920,31 @@
final WindowList windows = win.getWindowList();
final int NCW = win.mChildWindows.size();
boolean added = false;

        // Sort all child windows by ascending order of mSubLayer or mLayer.
        final WindowState[] children =
            (WindowState[]) win.mChildWindows.toArray(new WindowState[0]);
        Arrays.sort(children, msWindowZOrderComparator);

for (int j=0; j<NCW; j++) {
            WindowState cwin = children[j];
            // Find the position at which the parent window is.
            if (!added && cwin.mSubLayer > 0) {
                if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding window at "
                        + index + ": " + win);
win.mRebuilding = false;
windows.add(index, win);
index++;
added = true;
}
            // Children below parent should be added earlier than parent.
            if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding child window at "
+ index + ": " + cwin);
cwin.mRebuilding = false;
windows.add(index, cwin);
index++;
}
        // Parent has no children or all children are placed below parent.
if (!added) {
if (DEBUG_WINDOW_MOVEMENT) Slog.v(TAG, "Re-adding window at "
+ index + ": " + win);
//Synthetic comment -- @@ -4946,6 +4956,25 @@
return index;
}

    private static final java.util.Comparator<WindowState> msWindowZOrderComparator =
            new java.util.Comparator<WindowState>() {
        public int compare(final WindowState lhs, final WindowState rhs) {
            if (lhs.mSubLayer <  rhs.mSubLayer) {
                return -1;
            } else if (lhs.mSubLayer == rhs.mSubLayer) {
                if (lhs.mLayer == rhs.mLayer) {
                    return 0;
                } else if (lhs.mLayer == 0 || lhs.mLayer > rhs.mLayer) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return 1;
            }
        }
    };

private final int reAddAppWindowsLocked(final DisplayContent displayContent, int index,
WindowToken token) {
final int NW = token.windows.size();







