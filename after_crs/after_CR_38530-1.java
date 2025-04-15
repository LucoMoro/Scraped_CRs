/*Camera: Fix control panel width

This patch fixes bug with preview overlapping on
control panel.

Change-Id:Ifd2908b3b102d3039be6799646964d0124fdec5fSigned-off-by: Andriy Chepurnyy <x0155536@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/camera/ui/ControlPanelLayout.java b/src/com/android/camera/ui/ControlPanelLayout.java
//Synthetic comment -- index 24efb8b..3d830e0 100644

//Synthetic comment -- @@ -66,6 +66,12 @@
Log.e(TAG, "layout_xxx of ControlPanelLayout should be wrap_content");
}

        // Make sure the width is bigger than the minimum width.
        int minWidth = getSuggestedMinimumWidth();
        if (minWidth > measuredSize) {
            measuredSize = minWidth;
        }

// The width cannot be bigger than the constraint.
if (mode == MeasureSpec.AT_MOST && measuredSize > specSize) {
measuredSize = specSize;







