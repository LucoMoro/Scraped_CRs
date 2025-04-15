/*Phone: Ignore touch during network operators search

When network search is in progress, the progress dialog is
dismissed if user taps the screen. This in-turn cancels
manual search and switches to the previous screen.
This behavior can be avoided if the dialog property is set
to ignore user touch events.
If required BACK key can be used to cancel search and move
to previous screen.

Change-Id:I00da1dfec78498d2f35f6bb5d49e42bb3ad43cc2CRs-Fixed: 367631*/
//Synthetic comment -- diff --git a/src/com/android/phone/NetworkSetting.java b/src/com/android/phone/NetworkSetting.java
//Synthetic comment -- index a11ebab..ed606c4 100644

//Synthetic comment -- @@ -287,7 +287,7 @@
default:
// reinstate the cancelablity of the dialog.
dialog.setMessage(getResources().getString(R.string.load_networks_progress));
                    dialog.setCancelable(true);
dialog.setOnCancelListener(this);
break;
}







