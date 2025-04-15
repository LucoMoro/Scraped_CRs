/*Applying the previous rotation state when refreshing screen image.

Change-Id:I04e81b591701e096bb553ac31af3226fa6d6b3f6*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index f98dd13..d0c8a2f 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
private RawImage mRawImage;
private Clipboard mClipboard;


/**
* Create with default style.
//Synthetic comment -- @@ -119,6 +121,14 @@
@Override
public void widgetSelected(SelectionEvent e) {
updateDeviceImage(shell);
}
});

//Synthetic comment -- @@ -132,6 +142,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
if (mRawImage != null) {
mRawImage = mRawImage.getRotated();
updateImageDisplay(shell);
}







