/*Monkey: Changes to release lock before reporting ANR and meminfo

Report ANR, dumpsys after releasing lock on Monkey.this.
This ensures the availability of the lock to Activity controller's
appNotResponding.

Reporting dumpsys while holding the lock on this causes a cyclic
deadlock, when twoANRs are reported (one after the other).

Monkey's ActivityController is registered to ActivityManagerService
for handling ANR.The first ANR caused by either service timeout or
broadcast timeout is reported byActivityManagerService to Monkey's
ActivityController via Binder. Meanwhile, the lock on
ActivityManagerService is held by serviceTimeout or broadcastTimeout.

appNotResponding corresponding to first ANR reports procrank and
acquires a lock onMonkey.this and sets few bool variables like
mRequestAnrTraces and mRequestDumpsysMemInfoand returns the control
to ActivityManagerService's service/broadcast timeout.
VM executing monkey process switches the control to main monkey
thread and it acquires thelock on Monkey.this and proceeds to report
ANR traces.

Meanwhile, a second ANR occurs and Activity Manager Service invokes
ActivityController's appNotResponding (via binder). appNotResponding
reports the procrank and waits to acquire the lock on Monkey.this
which is being held by Monkey's main thread(busy reporting first ANR).
This results in a blocking wait for ActivityManagerService's
appNotRespondingLocked() (corresponding to second ANR).

Meanwhile, the monkey's main thread (holding lock on Monkey.this)
tries to report the meminfo for first ANR, invokes
reportDumpsysMemInfo(), which in turn causes the android runtime to
launch dumpsys process. The dumpsys process queries service manager
to get a reference to meminfo service and invoke dump() on the same.
The meminfo service is created by ActivityManagerService's
setSystemProcess(). The dump() method tries to acquire a lock on
ActivityManagerService which is held by ActivityManagerService's
service/broadcasttimeout (awaiting the response from
ActivityController for the second ANR).

This cyclic deadlock continues for a minute after which WatchDog
thread of system_server kills system_server as it hasn't got the
response from ActivityManagerService's monitor(). The monitor()
of ActivityManagerService too tries to acquire lock on this and is
invoked once in every minute.

DEADLOCK:
--------

ActivityManager --> ActivityController  --> Monkey Main  --> MemInfo
--> ActivityManager

Change-Id:I7718eff332e5551b1950ab1c45395bf1ff4b1bda*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 4de86cf..8a19b5c 100644

//Synthetic comment -- @@ -857,6 +857,9 @@
int eventCounter = 0;
int cycleCounter = 0;

        boolean mLocalRequestAnrTraces = false;
        boolean mLocalRequestDumpsysMemInfo = false;
        boolean mLocalAbort = false;
boolean systemCrashed = false;

while (!systemCrashed && cycleCounter < mCount) {
//Synthetic comment -- @@ -866,12 +869,12 @@
mRequestProcRank = false;
}
if (mRequestAnrTraces) {
mRequestAnrTraces = false;
                    mLocalRequestAnrTraces = true;
}
if (mRequestDumpsysMemInfo) {
mRequestDumpsysMemInfo = false;
                    mLocalRequestDumpsysMemInfo = true;
}
if (mMonitorNativeCrashes) {
// first time through, when eventCounter == 0, just set up
//Synthetic comment -- @@ -882,12 +885,29 @@
}
}
if (mAbort) {
                    mLocalAbort = true;
}
}

            // Report ANR, dumpsys after releasing lock on this.
            // This ensures the availability of the lock to Activity controller's appNotResponding
            if (mLocalRequestAnrTraces) {
               mLocalRequestAnrTraces = false;
               reportAnrTraces();
            }

            if (mLocalRequestDumpsysMemInfo) {
               mLocalRequestDumpsysMemInfo = false;
               reportDumpsysMemInfo();
            }

            if (mLocalAbort) {
               mLocalAbort = false;
               System.out.println("** Monkey aborted due to error.");
               System.out.println("Events injected: " + eventCounter);
               return eventCounter;
            }

// In this debugging mode, we never send any events. This is
// primarily here so you can manually test the package or category
// limits, while manually exercising the system.







