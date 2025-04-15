/*Telephony: Add emergency call status intent and extra key

There are system services which needs to know the emergency
call status before taking any drastic actions.

With this patch introduces a new intent
android.intent.action.EMERGENCY_CALL_STATUS and Intent Extra
information key in telephony framework.

Note: This patch is related to a change in platform/packages/apps/Phone
		and platform/frameworks/opt/telephony

Change-Id:I2f3d27831cb44cddc2241f1c29a61440d3caa194Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 55480*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneConstants.java b/telephony/java/com/android/internal/telephony/PhoneConstants.java
//Synthetic comment -- index 16ea625..cab3703 100644

//Synthetic comment -- @@ -82,6 +82,7 @@
public static final String NETWORK_UNAVAILABLE_KEY = "networkUnvailable";
public static final String DATA_NETWORK_ROAMING_KEY = "networkRoaming";
public static final String PHONE_IN_ECM_STATE = "phoneinECMState";
    public static final String EMERGENCY_CALL_STATUS_KEY = "emergencyCallOngoing";

public static final String REASON_LINK_PROPERTIES_CHANGED = "linkPropertiesChanged";









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyIntents.java b/telephony/java/com/android/internal/telephony/TelephonyIntents.java
//Synthetic comment -- index 3cfd0bf..f74e1be 100644

//Synthetic comment -- @@ -26,6 +26,28 @@
public class TelephonyIntents {

/**
     * Broadcast Action: Emergency call status change(ongoing or ended)
     * This is a sticky broadcast.
     * The intent will have the following extra value:</p>
     *
     * <ul>
     *   <li><em>emergencyCallOngoing</em> - A boolean value indicating whether the phone
     *                                       is in emergency call or not.</li>
     * </ul>
     *
     * <p class="note">
     * Ongoing means that the call is in dialing, alerting, active or disconnecting state.
     *
     * <p class="note">
     * Requires the READ_PHONE_STATE permission.
     *
     * <p class="note">
     * This is a protected intent that can only be sent by the system.
     */
    public static final String ACTION_EMERGENCY_CALL_STATUS_CHANGED =
            "android.intent.action.EMERGENCY_CALL_STATUS";

    /**
* Broadcast Action: The phone service state has changed. The intent will have the following
* extra values:</p>
* <ul>







