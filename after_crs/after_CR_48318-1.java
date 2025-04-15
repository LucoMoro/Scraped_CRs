/*Mms: fix javacrash in nonBlockingUpdateNewMessageIndicator function

The size of TreeSet can be zero and need to return
immediately if it's the case.

Change-Id:I41fc1e4f4c90a1eb01a76c74a1c6860b63151ce3Author: Jun Xu <junx.wu@intel.com>
Signed-off-by: Jun Xu <junx.wu@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61225*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index 334707e..d8a626b 100644

//Synthetic comment -- @@ -815,6 +815,12 @@

// Figure out what we've got -- whether all sms's, mms's, or a mixture of both.
final int messageCount = notificationSet.size();
        if (messageCount == 0) {
            if (DEBUG) {
                Log.d(TAG, "updateNotification: there is no element in sNotificationSet");
            }
            return;
        }
NotificationInfo mostRecentNotification = notificationSet.first();

final Notification.Builder noti = new Notification.Builder(context)







