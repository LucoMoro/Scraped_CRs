/*[BatteryStatsImpl] Avoid memory corruption due to Parcel Recycling

While processing batterystatsfile, in present code there is a race where a parcel
might be recycled while it is being written to disk in another thread.

The write process is done in 2 steps:
- write into a Parcel (memory block)
- then write this Parcel to disk using Journaled API.
The Journaled API ensures robustness against file-system issues due to power cut.
The writing is done in batch using a thread running commitPendingDataToDisk().
This function writes the parcel to disk but this parcel is a global variable
initialized by writeLocked(). If the writing takes too long time and two write
threads are launched, the parcel is freed by the second writeLocked() call;
which could cause the memory corruption.

Hence, change global Parcel variable by a Parcel parameter to commitPendingDataToDisk()
and only this function will be allowed to recycle the parcel.

Change-Id:Ie86ee0a66434b7107264c79f68411196c7505a34Signed-off-by: Pavan Kumar S <pavan.kumar.s@intel.com>
Author: Pavan Kumar S <pavan.kumar.s@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47466 19426*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 94e7a06..edbefb9 100644

//Synthetic comment -- @@ -4845,8 +4845,6 @@
writeSyncLocked();
mShuttingDown = true;
}

    Parcel mPendingWrite = null;
final ReentrantLock mWriteLock = new ReentrantLock();

public void writeAsyncLocked() {
//Synthetic comment -- @@ -4867,41 +4865,31 @@
return;
}

        Parcel out = Parcel.obtain();
writeSummaryToParcel(out);
mLastWriteTime = SystemClock.elapsedRealtime();

        if (mPendingWrite != null) {
            mPendingWrite.recycle();
        }
        mPendingWrite = out;

if (sync) {
            commitPendingDataToDisk();
} else {
Thread thr = new Thread("BatteryStats-Write") {
@Override
public void run() {
Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    commitPendingDataToDisk();
}
};
thr.start();
}
}

    public void commitPendingDataToDisk() {
        final Parcel next;
        synchronized (this) {
            next = mPendingWrite;
            mPendingWrite = null;
            if (next == null) {
                return;
            }

            mWriteLock.lock();
}

try {
FileOutputStream stream = new FileOutputStream(mFile.chooseForWrite());
stream.write(next.marshall());







