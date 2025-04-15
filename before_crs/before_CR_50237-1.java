/*NetworkSettings: Cannot dismiss progress dialog sometimes.

When "Load Network List" dialog is shown, move
NetworkSettings activity to backgroud by pressing
HOME key. After several minutes, go to
NetworkSettings again. Network list is shown under
the progress dialog, but user cannot dismiss the
Progess dialog.

Steps to reproduce this issue:
1) Settings->wireless & networks->mobile networks;
2) Press "network operators";
3) "Searching" progress dialog is shown;
4) Press HOME key to return to home;
5) Wait several minutes;
6) Goto "network operators" activity again.

Expected result: "Searching" dialog is dismissed;
Actual result: "Searching" dialog is still shown.

Change-Id:Ib411a89be2bcf907e6cca4c1d5693f8445f2d681Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/NetworkSetting.java b/src/com/android/phone/NetworkSetting.java
//Synthetic comment -- index a11ebab..f50e510 100644

//Synthetic comment -- @@ -106,8 +106,15 @@
case EVENT_AUTO_SELECT_DONE:
if (DBG) log("hideProgressPanel");

                    if (mIsForeground) {
dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
}
getPreferenceScreen().setEnabled(true);

//Synthetic comment -- @@ -387,8 +394,16 @@
// update the state of the preferences.
if (DBG) log("hideProgressPanel");

        if (mIsForeground) {
dismissDialog(DIALOG_NETWORK_LIST_LOAD);
}

getPreferenceScreen().setEnabled(true);







