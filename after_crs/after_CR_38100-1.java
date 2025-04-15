/*Bluetooth: Use ICS alert drawable

Make it use the holo drawables instead of the GB
compability drawable.

Change-Id:Ib85d89f0f3a6248d357a612afa8c0158a0876b86*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java b/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java
//Synthetic comment -- index 87a695d..1e9004a 100644

//Synthetic comment -- @@ -458,7 +458,7 @@
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.download_succ_ok));
} else if (mWhichDialog == DIALOG_RECEIVE_COMPLETE_FAIL) {
            mAlert.setIconAttribute(android.R.attr.alertDialogIcon);
mAlert.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.download_fail_ok));
//Synthetic comment -- @@ -467,7 +467,7 @@
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.upload_succ_ok));
} else if (mWhichDialog == DIALOG_SEND_COMPLETE_FAIL) {
            mAlert.setIconAttribute(android.R.attr.alertDialogIcon);
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.upload_fail_ok));
mAlert.getButton(DialogInterface.BUTTON_NEGATIVE).setText(







