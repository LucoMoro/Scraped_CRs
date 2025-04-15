/*CellBroadcastReceiver: Use ICS alert drawable

Still points to the GB compability drawable.

Change-Id:Ie0af5057113c5354537f098f3303e8e350883dad*/
//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java b/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java
//Synthetic comment -- index eafeec2..0893e2a 100644

//Synthetic comment -- @@ -219,7 +219,7 @@

AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle(R.string.confirm_dialog_title)
            .setIcon(android.R.drawable.ic_dialog_alert)
.setCancelable(true)
.setPositiveButton(R.string.button_delete, listener)
.setNegativeButton(R.string.button_cancel, null)







