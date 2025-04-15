/*Add Watchdog timing information

Log timing information when the Watchdog's timeout
elapses so we can understand which monitor took
too long; this is especially usefull when mCurrentMonitor
is null but also when the monitors take too long but
return eventually.

Change-Id:I5e81f3f751502080fd9c45faef192c9bb83edb1dAuthor: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 66696*/




//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index 8bbf923..e82462e 100644

//Synthetic comment -- @@ -66,6 +66,8 @@
static final int REBOOT_DEFAULT_START_TIME = 3*60*60;                  // 3:00am
static final int REBOOT_DEFAULT_WINDOW = 60*60;                        // within 1 hour

    static final int NB_TIMINGS = 5;

static final String REBOOT_ACTION = "com.android.service.Watchdog.REBOOT";

static final String[] NATIVE_STACKS_OF_INTEREST = new String[] {
//Synthetic comment -- @@ -87,6 +89,8 @@
boolean mCompleted;
boolean mForceKillSystem;
Monitor mCurrentMonitor;
    long mTimings[][];
    int mCurrentTiming = 0;

int mPhonePid;

//Synthetic comment -- @@ -127,10 +131,16 @@
}

final int size = mMonitors.size();
                    long start, stop = SystemClock.uptimeMillis();
for (int i = 0 ; i < size ; i++) {
mCurrentMonitor = mMonitors.get(i);
                        start = stop;
mCurrentMonitor.monitor();
                        stop = SystemClock.uptimeMillis();
                        mTimings[i][mCurrentTiming] = (stop - start);
}
                    mCurrentTiming++;
                    if (mCurrentTiming >= NB_TIMINGS) mCurrentTiming = 0;

synchronized (Watchdog.this) {
mCompleted = true;
//Synthetic comment -- @@ -375,6 +385,7 @@
@Override
public void run() {
boolean waitedHalf = false;
        mTimings = new long[mMonitors.size()][NB_TIMINGS];
while (true) {
mCompleted = false;
mHandler.sendEmptyMessage(MONITOR);
//Synthetic comment -- @@ -422,6 +433,18 @@
mCurrentMonitor.getClass().getName() : "null";
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

            // Prints out the timings of the last NB_TIMINGS checks so that
            // it can help understanding which component took too much time
            // but finally returned
            for (int i = 0 ; i < mMonitors.size() ; i++) {
                Monitor monitor = mMonitors.get(i);
                String sMonitor = "Unkown monitor: ";
                if (monitor != null) sMonitor = monitor.getClass().getName() + ": ";
                for (int j = 0 ; j < NB_TIMINGS ; j++)
                    sMonitor += "" + mTimings[i][j] + "ms ";
                Log.i(TAG, sMonitor);
            }

ArrayList<Integer> pids = new ArrayList<Integer>();
pids.add(Process.myPid());
if (mPhonePid > 0) pids.add(mPhonePid);







