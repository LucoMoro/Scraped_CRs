/*TetheringSetting: Keep Tethering checkbox unchecked until tethering up

When click Tethering, the checkbox will be checked and then unchecked,
and finally move to checked states. This causes confusing to end user,
we need to keep Tethering checkbox unchecked before tethering is really
up.

Change-Id:If2314b7dd1369e619dead45af38fd53c7e89c34eAuthor: Li Wenji <wenji.li@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18281*/
//Synthetic comment -- diff --git a/src/com/android/settings/TetherSettings.java b/src/com/android/settings/TetherSettings.java
//Synthetic comment -- index 77a72a7..4a79eca 100644

//Synthetic comment -- @@ -493,8 +493,8 @@
private void setUsbTethering(boolean enabled) {
ConnectivityManager cm =
(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
if (cm.setUsbTethering(enabled) != ConnectivityManager.TETHER_ERROR_NO_ERROR) {
            mUsbTether.setChecked(false);
mUsbTether.setSummary(R.string.usb_tethering_errored_subtext);
return;
}







