/*Fixed dialog handling in UsbStorageActivity

Dialogs in UsbStorageActivity were added in a non-UI thread
which caused crashes, e.g., when changing orientation. Dialogs are
now created in the UI thread.

Change-Id:I8ba9896991ca1e36ce50051f166a03d8dd8b7a0e*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java b/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java
//Synthetic comment -- index 1383216..9cccf5f 100644

//Synthetic comment -- @@ -232,9 +232,15 @@
return null;
}

    private void showDialogInner(int id) {
        removeDialog(id);
        showDialog(id);
}

private void switchUsbMassStorage(final boolean on) {







