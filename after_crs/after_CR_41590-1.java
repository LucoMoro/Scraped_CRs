/*Connectivity: on suspended state broadcast only intent

Currently, AOSP handles the SUSPENDED and DISCONNECTED
states the same way. Upon SUSPENDED or DISCONNECTED
state, routing table entry is removed, other connections
will be tried and finally applications will be notified
of the suspended state. With this approach, Connection timed
out issue seen in some applications.

This patch notifies the applications about the SUSPENDED
state but doesn't update the routing table and also doesn't
try establishing other connections(WIFI)

Note: This will deviate from the existing android
behavior where upon SUSPENDED state, routing table
entry will be removed and other connections
(WIFI) will be tried.

Change-Id:Icd6bbf72377b2771ccd2ba85a3af6f919b8270edAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 42177 45421*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 230f07b..1a89941 100644

//Synthetic comment -- @@ -1579,6 +1579,40 @@
"ConnectivityService");
}


    /**
     * Handle a {@code SUSPENDED} event. Send out the broadcast
     * of connection state change.
     * @param info the {@code NetworkInfo} for the network
     */
    private void handleSuspended(NetworkInfo info) {
        if (info == null)
            return;

        int netType = info.getType();

        Intent intent = new Intent(ConnectivityManager.CONNECTIVITY_ACTION);
        if (intent != null) {
            intent.putExtra(ConnectivityManager.EXTRA_NETWORK_INFO, info);

            if (info.getReason() != null) {
                intent.putExtra(ConnectivityManager.EXTRA_REASON, info.getReason());
            }

            if (info.getExtraInfo() != null) {
                intent.putExtra(ConnectivityManager.EXTRA_EXTRA_INFO,
                        info.getExtraInfo());
            }
            sendStickyBroadcastDelayed(intent, getConnectivityChangeDelay());

            final Intent immediateIntent = new Intent(intent);
            if (immediateIntent != null) {
                immediateIntent.setAction(CONNECTIVITY_ACTION_IMMEDIATE);
                sendStickyBroadcast(immediateIntent);
            }
        }
    }

/**
* Handle a {@code DISCONNECTED} event. If this pertains to the non-active
* network, we ignore it. If it is for the active network, we send out a
//Synthetic comment -- @@ -2471,14 +2505,7 @@
} else if (state == NetworkInfo.State.DISCONNECTED) {
handleDisconnect(info);
} else if (state == NetworkInfo.State.SUSPENDED) {
                        handleSuspended(info);
} else if (state == NetworkInfo.State.CONNECTED) {
handleConnect(info);
}







