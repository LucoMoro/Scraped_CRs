/*Framework: Use ICS alert drawable

ICS updated the way one should point to the alert
drawable. The old way makes it point to an upscaled
GB drawable instead.

Change-Id:Ieb5b53aef55a8b5773a5c15af6e3440c9e18a3b4*/




//Synthetic comment -- diff --git a/packages/VpnDialogs/src/com/android/vpndialogs/ConfirmDialog.java b/packages/VpnDialogs/src/com/android/vpndialogs/ConfirmDialog.java
//Synthetic comment -- index c7b4a5f..9bb5aba 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
((CompoundButton) view.findViewById(R.id.check)).setOnCheckedChangeListener(this);

mDialog = new AlertDialog.Builder(this)
                    .setIconAttribute(android.R.attr.alertDialogIcon)
.setTitle(android.R.string.dialog_alert_title)
.setView(view)
.setPositiveButton(android.R.string.ok, this)







