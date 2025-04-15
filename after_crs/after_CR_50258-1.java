/*Watchdog race condition when sending message out of synchronized

A race condition leading to false positive detections might occur
when the monitoring thread executes very fast and terminates before
the sending thread entered the synchronized section.

Change-Id:I6fe686f8f12393e11fa18326508af5b73738f9d7Author: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 81644*/




//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index 8bbf923..a7934da 100644

//Synthetic comment -- @@ -376,11 +376,10 @@
public void run() {
boolean waitedHalf = false;
while (true) {
synchronized (this) {
long timeout = TIME_TO_WAIT;
                mCompleted = false;
                mHandler.sendEmptyMessage(MONITOR);

// NOTE: We use uptimeMillis() here because we do not want to increment the time we
// wait while asleep. If the device is asleep then the thing that we are waiting







