/*Shutdown aplog message

Correction of the aplog message while a gracefull shutdown is confirmed and
then canceled. Previously the message was always like hereafter even when
the cancel soft button was selected : [SHTDWN] shutdown, confirm=YES

Change-Id:Ic17bd1a3034b7dc9b44054b23ccc38cd3985926cAuthor: Gabriel Touzeau <gabrielx.touzeau@intel.com>
Signed-off-by: Gabriel Touzeau <gabrielx.touzeau@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30177*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/ShutdownThread.java b/services/java/com/android/server/pm/ShutdownThread.java
//Synthetic comment -- index 3675d41..73878d4 100644

//Synthetic comment -- @@ -137,9 +137,15 @@
.setPositiveButton(com.android.internal.R.string.yes, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
beginShutdownSequence(context);
}
})
                    .setNegativeButton(com.android.internal.R.string.no, null)
.create();
closer.dialog = sConfirmDialog;
sConfirmDialog.setOnDismissListener(closer);







