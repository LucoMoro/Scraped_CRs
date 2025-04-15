/*Bluetooth: Use proper holo alert drawable

Make it use the holo drawables instead of the GB
compability drawable.

Change-Id:Ib85d89f0f3a6248d357a612afa8c0158a0876b86*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppBtEnableActivity.java b/src/com/android/bluetooth/opp/BluetoothOppBtEnableActivity.java
//Synthetic comment -- index 029c4f8..d01c033 100644

//Synthetic comment -- @@ -56,7 +56,7 @@

// Set up the "dialog"
final AlertController.AlertParams p = mAlertParams;
        p.mIconAttrId = android.R.attr.alertDialogIcon;
p.mTitle = getString(R.string.bt_enable_title);
p.mView = createView();
p.mPositiveButtonText = getString(R.string.bt_enable_ok);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppBtErrorActivity.java b/src/com/android/bluetooth/opp/BluetoothOppBtErrorActivity.java
//Synthetic comment -- index 9fa6e12..d2380f6 100644

//Synthetic comment -- @@ -61,7 +61,7 @@

// Set up the "dialog"
final AlertController.AlertParams p = mAlertParams;
        p.mIconAttrId = android.R.attr.alertDialogIcon;
p.mTitle = mErrorTitle;
p.mView = createView();
p.mPositiveButtonText = getString(R.string.bt_error_btn_ok);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java b/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java
//Synthetic comment -- index 87a695d..23c9cb8 100644

//Synthetic comment -- @@ -223,14 +223,14 @@
mPara.mPositiveButtonText = getString(R.string.download_succ_ok);
mPara.mPositiveButtonListener = this;
} else if (mWhichDialog == DIALOG_RECEIVE_COMPLETE_FAIL) {
            mPara.mIconAttrId = android.R.attr.alertDialogIcon;
mPara.mPositiveButtonText = getString(R.string.download_fail_ok);
mPara.mPositiveButtonListener = this;
} else if (mWhichDialog == DIALOG_SEND_COMPLETE_SUCCESS) {
mPara.mPositiveButtonText = getString(R.string.upload_succ_ok);
mPara.mPositiveButtonListener = this;
} else if (mWhichDialog == DIALOG_SEND_COMPLETE_FAIL) {
            mPara.mIconAttrId = android.R.attr.alertDialogIcon;
mPara.mPositiveButtonText = getString(R.string.upload_fail_ok);
mPara.mPositiveButtonListener = this;
mPara.mNegativeButtonText = getString(R.string.upload_fail_cancel);
//Synthetic comment -- @@ -458,7 +458,7 @@
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.download_succ_ok));
} else if (mWhichDialog == DIALOG_RECEIVE_COMPLETE_FAIL) {
            mAlert.setIcon(mAlert.getIconAttributeResId(android.R.attr.alertDialogIcon));
mAlert.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.download_fail_ok));
//Synthetic comment -- @@ -467,7 +467,7 @@
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.upload_succ_ok));
} else if (mWhichDialog == DIALOG_SEND_COMPLETE_FAIL) {
            mAlert.setIcon(mAlert.getIconAttributeResId(android.R.attr.alertDialogIcon));
mAlert.getButton(DialogInterface.BUTTON_POSITIVE).setText(
getString(R.string.upload_fail_ok));
mAlert.getButton(DialogInterface.BUTTON_NEGATIVE).setText(







