/*Return error upon attempt to use non-existent APN

startUsingNetworkFeature will ignore errors from
reconnect - causing ConnectivityService to send
faulty information back to requester.

Change-Id:I4e0fcc1addd84da409cdc1eed1a95d25d925e020*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index ad1dfb2..c151177 100644

//Synthetic comment -- @@ -1183,8 +1183,9 @@
log("startUsingNetworkFeature reconnecting to " + networkType + ": " +
feature);
}
                    if (network.reconnect()) {
                        return PhoneConstants.APN_REQUEST_STARTED;
                    }
} else {
// need to remember this unsupported request so we respond appropriately on stop
synchronized(this) {







