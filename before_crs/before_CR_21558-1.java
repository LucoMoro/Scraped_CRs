/*Added empty check before removing first element in waiting APN list.

If the APN:s are removed during a pending connect, the waitingAPNs list will be cleared.
If the response returns with an exception the GsmDataConnectionTracker will remove the
first waiting APN without empty check. So an empty check has been added to avoid
IndexOutOfBoundsException.

Change-Id:I0c3c4e79d5b81d61a1e6f99c79b6cafc61597a44Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index e7d57bc..1ab5683 100644

//Synthetic comment -- @@ -1162,7 +1162,11 @@
return;
}

            waitingApns.remove(0);
if (waitingApns.isEmpty()) {
// No more to try, start delayed retry
startDelayedRetry(cause, reason);







