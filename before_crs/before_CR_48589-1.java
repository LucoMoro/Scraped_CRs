/*Shutdown aplog message

Correction of the aplog message while a gracefull shutdown is confirmed and
then cancelled.nPreviously the message was always like hereafter even when
the cancel soft button was selected : [SHTDWN] shutdown, confirm=YES

Change-Id:I0b486d70489c487907feb64e815313e9f3a0d828Author: David Castelain <davidx.castelain@intel.com>
Signed-off-by: David Castelain <davidx.castelain@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30177*/
//Synthetic comment -- diff --git a/services/java/com/android/server/power/ShutdownThread.java b/services/java/com/android/server/power/ShutdownThread.java
//Synthetic comment -- index c7f7390..3bd3b93 100644

//Synthetic comment -- @@ -137,9 +137,14 @@
.setPositiveButton(com.android.internal.R.string.yes, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
beginShutdownSequence(context);
}
})
                    .setNegativeButton(com.android.internal.R.string.no, null)
.create();
closer.dialog = sConfirmDialog;
sConfirmDialog.setOnDismissListener(closer);







