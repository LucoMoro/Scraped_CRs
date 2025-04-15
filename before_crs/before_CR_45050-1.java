/*Settings: Fix the memory leak in Settings

Fixed the memory leak in both InstalledAppDetails

Signed-off-by: Zhijun Peng <pengzhj@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
old mode 100644
new mode 100755
//Synthetic comment -- index d85c341..aae5ccb

//Synthetic comment -- @@ -451,6 +451,14 @@
}

@Override
public void onAllSizesComputed() {
}








