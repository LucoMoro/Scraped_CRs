/*wlan: handle WEP authentication timeout

When user enters a wrong password for WEP authentication,
DUT never stops retrying to authenticate; the UI remains
in "connecting" state.
In such scenario, wpa_supplicant raises an "authentication
timeout" error message.
This patch makes framework handle such message. When
receiving it, it will process classical "autentication
problem" path.

Change-Id:Ib21d8031cc10b9c98d265fd4213318a9752be9d5Author: Jeremie GARCIA <jeremiex.garcia@intel.com>
Signed-off-by: Jeremie GARCIA <jeremiex.garcia@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 58279*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiMonitor.java b/wifi/java/android/net/wifi/WifiMonitor.java
//Synthetic comment -- index 0b0d7388e..aa09c02 100644

//Synthetic comment -- @@ -66,6 +66,8 @@
private static final String WPA_EVENT_PREFIX_STR = "WPA:";
private static final String PASSWORD_MAY_BE_INCORRECT_STR =
"pre-shared key may be incorrect";
    private static final String AUTH_TIMEOUT_STR =
       "Authentication timed out";

/* WPS events */
private static final String WPS_SUCCESS_STR = "WPS-SUCCESS";
//Synthetic comment -- @@ -385,6 +387,8 @@
if (eventStr.startsWith(WPA_EVENT_PREFIX_STR) &&
0 < eventStr.indexOf(PASSWORD_MAY_BE_INCORRECT_STR)) {
mStateMachine.sendMessage(AUTHENTICATION_FAILURE_EVENT);
                    } else if (0 < eventStr.indexOf(AUTH_TIMEOUT_STR)) {
                        mStateMachine.sendMessage(AUTHENTICATION_FAILURE_EVENT);
} else if (eventStr.startsWith(WPS_SUCCESS_STR)) {
mStateMachine.sendMessage(WPS_SUCCESS_EVENT);
} else if (eventStr.startsWith(WPS_FAIL_STR)) {







