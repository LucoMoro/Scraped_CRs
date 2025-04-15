/*CellBroadcastReceiver: Use ICS alert drawable

Still points to the GB compability drawable.

Patch Set 2: Rebased for JB

Change-Id:Ie0af5057113c5354537f098f3303e8e350883dadConflicts:

	src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java*/
//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java b/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java
//Synthetic comment -- index eb75a1e..20875b6 100644

//Synthetic comment -- @@ -237,7 +237,7 @@
public static void confirmDeleteThreadDialog(DeleteThreadListener listener,
boolean deleteAll, Context context) {
AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(android.R.drawable.ic_dialog_alert)
.setCancelable(true)
.setPositiveButton(R.string.button_delete, listener)
.setNegativeButton(R.string.button_cancel, null)







