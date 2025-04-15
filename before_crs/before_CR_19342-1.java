/*Fixed dialog handling in UsbStorageActivity

Dialogs in UsbStorageActivity were added in a non-UI thread
which caused crashes, e.g., when changing orientation. Dialogs are
now created in the UI thread.

Change-Id:I9dbeef33adc1b981319fb4c3f89d7678b6ff9ae0*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/UsbStorageActivity.java b/services/java/com/android/server/status/UsbStorageActivity.java
//Synthetic comment -- index e8631c53..67613a8 100644

//Synthetic comment -- @@ -198,9 +198,14 @@
return null;
}

    private void showDialogInner(int id) {
        removeDialog(id);
        showDialog(id);
}

private void switchUsbMassStorageAsync(boolean on) {







