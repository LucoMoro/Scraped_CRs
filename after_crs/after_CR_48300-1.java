/*MMS: Stop previous TaskStack worker

Each time we Delete All Threads, a new TaskStack thread is started.
We need to stop the previous one to avoid over memory consumption.

Change-Id:I81b78d196d13576c8bb3cab65ada3796bd0f01f2Author: Emmanuel Berthier <emmanuel.berthier@intel.com>
Signed-off-by: Emmanuel Berthier <emmanuel.berthier@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 46592*/




//Synthetic comment -- diff --git a/src/com/android/mms/data/Contact.java b/src/com/android/mms/data/Contact.java
//Synthetic comment -- index c29b9bd..6fd51f1 100644

//Synthetic comment -- @@ -355,6 +355,9 @@
}

public static void init(final Context context) {
        if (sContactCache != null) { // Stop previous Runnable
            sContactCache.mTaskQueue.mWorkerThread.interrupt();
        }
sContactCache = new ContactsCache(context);

RecipientIdCache.init(context);
//Synthetic comment -- @@ -503,7 +506,7 @@
try {
mThingsToLoad.wait();
} catch (InterruptedException ex) {
                                        break;  // Exception sent by Contact.init() to stop Runnable
}
}
if (mThingsToLoad.size() > 0) {







