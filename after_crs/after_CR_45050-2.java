/*Settings: Fix the memory leak in Settings

Fixed the memory leak in both InstalledAppDetails

Change-Id:I3520b3c1fe1bf98e3baec2ee989eb215164a7cf2Signed-off-by: Zhijun Peng <pengzhj@marvell.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
old mode 100644
new mode 100755
//Synthetic comment -- index d85c341..f57e1aa

//Synthetic comment -- @@ -451,6 +451,12 @@
}

@Override
    public void onDestroyView() {
        super.onDestroyView();
        mSession.release();
    }

    @Override
public void onAllSizesComputed() {
}








