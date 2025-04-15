/*Settings: Fix the memory leak in Settings

Fixed the memory leak in both InstalledAppDetails

Change-Id:Iafae29c6954f6cd9eab3b12b4c1e4dc2888f86a5Signed-off-by: Zhijun Peng <pengzhj@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/InstalledAppDetails.java b/src/com/android/settings/applications/InstalledAppDetails.java
old mode 100644
new mode 100755
//Synthetic comment -- index d85c341..f57e1aa

//Synthetic comment -- @@ -451,6 +451,12 @@
}

@Override
public void onAllSizesComputed() {
}








