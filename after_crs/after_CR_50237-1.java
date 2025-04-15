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

                    // Always try to dismiss the dialog because activity may
                    // be moved to background after dialog is shown.
                    try {
dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
                    } catch (IllegalArgumentException e) {
                        // "auto select" is always trigged in foreground, so "auto select" dialog
                        //  should be shown when "auto select" is trigged. Should NOT get
                        // this exception, and Log it.
                        Log.w(LOG_TAG, "[NetworksList] Fail to dismiss auto select dialog", e);
}
getPreferenceScreen().setEnabled(true);

//Synthetic comment -- @@ -387,8 +394,16 @@
// update the state of the preferences.
if (DBG) log("hideProgressPanel");


        // Always try to dismiss the dialog because activity may
        // be moved to background after dialog is shown.
        try {
dismissDialog(DIALOG_NETWORK_LIST_LOAD);
        } catch (IllegalArgumentException e) {
            // It's not a error in following scenario, we just ignore it.
            // "Load list" dialog will not show, if NetworkQueryService is
            // connected after this activity is moved to background.
            if (DBG) log("Fail to dismiss network load list dialog");
}

getPreferenceScreen().setEnabled(true);







