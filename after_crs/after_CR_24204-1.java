/*Fix the issue that Airplane mode enabled banner was not show in GSM mode.

Without this fix, if in GSM/UMTS mode, enable airplane mode, then launch Dialer
and long press "1", the "No network available" banner was shown in stead of
"Airplane mode enabled" banner.

The issue was because CallManager::getServiceState can not get correct overall
service state via current logic. CallManager defines the service states with
a priority order of INSERVICE > NOSERVICE > EMERGENCYONLY > POWEROFF, and will
calculate overall state for all registered phone instances in accordance with
this order. Currently CallManager uses STATE_OUT_OF_SERVICE as an initial value,
it will get an incorrect result in the above case. If initialize it to the
lowest priority, which is STATE_POWER_OFF, the issue was fixed.

Change-Id:Ice94ff7ac461b3d4cda0c046ad903a3eb8e1a49e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallManager.java b/telephony/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b89058f..4f62471 100644

//Synthetic comment -- @@ -258,7 +258,8 @@
*/

public int getServiceState() {
        // initialize the service state to the lowest priority value
        int resultState = ServiceState.STATE_POWER_OFF;

for (Phone phone : mPhones) {
int serviceState = phone.getServiceState().getState();







