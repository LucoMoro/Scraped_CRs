/*Phone: Fix some errors in logcat log

There are some errors in logcat log when change screen to landscape
with "Attention" window.

Change-Id:Ic29429f8283b80a19d3f03101cbb6ef9964db1d2Author: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: b129 <jianli.zhang@borqs.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 8966*/
//Synthetic comment -- diff --git a/src/com/android/phone/MobileNetworkSettings.java b/src/com/android/phone/MobileNetworkSettings.java
//Synthetic comment -- index a81d7c2..ff151cc 100644

//Synthetic comment -- @@ -98,6 +98,7 @@

private Preference mClickedPreference;


//This is a method implemented for DialogInterface.OnClickListener.
//  Used to dismiss the dialogs when they come up.
//Synthetic comment -- @@ -156,14 +157,15 @@
if (mButtonDataRoam.isChecked()) {
// First confirm with a warning dialog about charges
mOkClicked = false;
                new AlertDialog.Builder(this).setMessage(
getResources().getString(R.string.roaming_warning))
.setTitle(android.R.string.dialog_alert_title)
.setIconAttribute(android.R.attr.alertDialogIcon)
.setPositiveButton(android.R.string.yes, this)
.setNegativeButton(android.R.string.no, this)
                        .show()
                        .setOnDismissListener(this);
} else {
mPhone.setDataRoamingEnabled(false);
}
//Synthetic comment -- @@ -312,6 +314,14 @@
mDataUsageListener.pause();
}

/**
* Implemented to support onPreferenceChangeListener to look for preference
* changes specifically on CLIR.







