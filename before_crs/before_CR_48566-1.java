/*CBS : Update the "Delete All" display.

In the CellBroadcastReceiver application, when the user
presses for the first time the menu touch, with one or
more SMS_CB received already, the "Delete All" is not
displayed.

This patch updates the menu display of the
CellBroadcastReceiver application. With this patch,
the "delete all" appears correctly in the settings
menu, if one or several SMS are present in the application.

Change-Id:I7bc6397ce4f024378cd964a44c6ec2a8f3bdbbf4Author: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 65376*/
//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java b/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java
//Synthetic comment -- index 777c24e..cb7fb8c 100644

//Synthetic comment -- @@ -148,6 +148,7 @@
// Swap the new cursor in.  (The framework will take care of closing the
// old cursor once we return.)
mAdapter.swapCursor(data);
}

@Override







