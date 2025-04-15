/*Fix OutOfMemoryError during tethering

Root cause:
When telephony responded a failure to dun request for some reason,
in current code, it would send another dun request immediately
after the failure. Then, it seems a dead loop was happened
there. It consumed lots of system resource, such as FeatureUser
in ConnectivityService, and caused the OutOfMemoryError eventually.

Solution:
Not to request dun immediately, delay it via
sendMessageDelayed(CMD_RETRY_UPSTREAM, UPSTREAM_SETTLE_TIME_MS);

Change-Id:I34c646b9af896ee95daa51ef4acc68255b204497*/
//Synthetic comment -- diff --git a/services/java/com/android/server/connectivity/Tethering.java b/services/java/com/android/server/connectivity/Tethering.java
//Synthetic comment -- index cc1df4f..e39c9d8 100644

//Synthetic comment -- @@ -1429,8 +1429,7 @@
}
break;
case CMD_UPSTREAM_CHANGED:
                        // need to try DUN immediately if Wifi goes down
                        mTryCell = !WAIT_FOR_NETWORK_TO_SETTLE;
chooseUpstreamType(mTryCell);
mTryCell = !mTryCell;
break;







