/*Removing dead store; it was likely a relict of debuging code.

Change-Id:I685c6df119212f48f90dd40d01a3638a33ce2800*/
//Synthetic comment -- diff --git a/core/java/android/os/BatteryStats.java b/core/java/android/os/BatteryStats.java
//Synthetic comment -- index 5de76ff..4ff3572 100644

//Synthetic comment -- @@ -1891,7 +1891,6 @@
final int NU = uidStats.size();
boolean didPid = false;
long nowRealtime = SystemClock.elapsedRealtime();
        StringBuilder sb = new StringBuilder(64);
for (int i=0; i<NU; i++) {
Uid uid = uidStats.valueAt(i);
SparseArray<? extends Uid.Pid> pids = uid.getPidStats();







