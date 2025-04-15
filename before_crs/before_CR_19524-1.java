/*Avoid sending two PDP deactivate at the same time

When an application is aggressive and calls startUsingNetworkFeature
two times in a row, the state machine in the framework gets confused.
This patch will not allow a disconnect when state is disconnecting.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 5f651e7..abc5098 100644

//Synthetic comment -- @@ -492,6 +492,11 @@
* @param reason reason for the clean up.
*/
private void cleanUpConnection(boolean tearDown, String reason) {
if (DBG) log("Clean up connection due to " + reason);

// Clear the reconnect alarm, if set.







