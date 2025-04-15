/*Applying the previous rotation state when refreshing screen image.

Change-Id:I04e81b591701e096bb553ac31af3226fa6d6b3f6*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index f98dd13..8247363 100644

//Synthetic comment -- @@ -99,6 +99,8 @@

}

/*
* Create the screen capture dialog contents.
*/
//Synthetic comment -- @@ -119,6 +121,12 @@
@Override
public void widgetSelected(SelectionEvent e) {
updateDeviceImage(shell);
}
});

//Synthetic comment -- @@ -132,6 +140,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
if (mRawImage != null) {
mRawImage = mRawImage.getRotated();
updateImageDisplay(shell);
}







