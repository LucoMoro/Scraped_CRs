/*Change the watchdog monitor to avoid false positives

The critical section in the run's method might lead to
a race condition overwriting the mCompleted flag.
Generating a false positive detection.

Change-Id:I4460cbcca83199f9c81b4bcd6bfa04cec39271e1Author: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63690*/
//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index 8bbf923..cf62bd6 100644

//Synthetic comment -- @@ -376,7 +376,6 @@
public void run() {
boolean waitedHalf = false;
while (true) {
            mCompleted = false;
mHandler.sendEmptyMessage(MONITOR);

synchronized (this) {
//Synthetic comment -- @@ -399,6 +398,7 @@
if (mCompleted && !mForceKillSystem) {
// The monitors have returned.
waitedHalf = false;
continue;
}

//Synthetic comment -- @@ -464,6 +464,7 @@
}

waitedHalf = false;
}
}








