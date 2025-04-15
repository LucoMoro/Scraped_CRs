/*Fixed errors javadoc tags

1. @result -> @return in ActivityManagerService
2. @linke -> link in WiFiManager*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 51ee41a..2e2cdb2 100644

//Synthetic comment -- @@ -3997,7 +3997,7 @@
* @param resultCode Result code, if any, from this Activity.
* @param resultData Result data (Intent), if any, from this Activity.
* 
     * @result Returns true if the activity successfully finished, or false if it is still running.
*/
public final boolean finishActivity(IBinder token, int resultCode, Intent resultData) {
// Refuse possible leaked file descriptors








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiManager.java b/wifi/java/android/net/wifi/WifiManager.java
//Synthetic comment -- index 74f4284..e862a7a 100644

//Synthetic comment -- @@ -959,7 +959,7 @@
*
* If this MulticastLock is not reference-counted, the first call to
* {@code release} (after the radio was multicast locked using
         * {@linke #acquire}) will unlock the multicast, and subsequent calls
* will be ignored.
*
* Note that if any other Wifi Multicast Locks are still outstanding







