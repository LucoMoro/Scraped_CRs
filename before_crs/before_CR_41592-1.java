/*connectivity: fix delay observed in broadcasting connectivity action

Since changes to the routing table takes time to propagate,
a delay of 3seconds was introduced in broadcasting the
CONNECTIVITY_ACTION allowing applications such as GTkalk
to create sockets without entering a 3minute timeout.

Due to this, some delay has been observed in locating
the position using A-GPS.

LocationManagerService now listens to CONNECTIVITY_ACTION_IMMEDIATE
instead of CONNECTIVITY_ACTION, which was delayed. Both are fired
systematically on connection events.

Note: This patch is related to a change in platforms/frameworks/base

Change-Id:I427811bc1c7ff81acd50952d94b82e053675940dAuthor: Fabien Peix <fabien.peix@intel.com>
Signed-off-by: Fabien Peix <fabien.peix@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26697*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 2918dbc..2c0b6e6 100644

//Synthetic comment -- @@ -604,7 +604,7 @@

// Register for Network (Wifi or Mobile) updates
IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
// Register for Package Manager updates
intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
//Synthetic comment -- @@ -2052,7 +2052,7 @@
}
}
}
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
boolean noConnectivity =
intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
if (!noConnectivity) {







