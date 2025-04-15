/*telephony: Increase the Send SMS retry delay timer

When the device is registered to the network,
framework will start resending the SMS. Number of
retry is 3 and the retry interval is 2seconds.
Since the retry interval is very less, some of
the SMS are never sent out. Failure may be
due to modem busy with SIM files caching or
service center busy etc.

This patch increases the SEND_RETRY_DELAY to
10seconds.

Change-Id:Id0016b7c3bc43955e5270887f49ed000e34a599cAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Author-tracking-BZ: 22238*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..a05fcc3 100644

//Synthetic comment -- @@ -137,7 +137,7 @@
/** Maximum number of times to retry sending a failed SMS. */
private static final int MAX_SEND_RETRIES = 3;
/** Delay before next send attempt on a failed SMS, in milliseconds. */
    private static final int SEND_RETRY_DELAY = 2000;
/** single part SMS */
private static final int SINGLE_PART_SMS = 1;
/** Message sending queue limit */







