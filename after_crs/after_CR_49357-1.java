/*Telephony: Support for stk user activity event

Stk User Activity Event is not supported in Android
framework.

This patch provides support for User Activity event.

User activity event is an STK Event Dowload commands.
When UICC registers to this event via a Setup Event List
command and next the terminal detects any user key-press
or screen-touch, the terminal informs UICC through an Stk
Envelope command. This is oneshot event as after sending
the envelope, User Activity event is removed from Event
List.

Change-Id:Iab08a2177d15f3f6201f00329d0814f4938cf12eAuthor: Andre Amegah <andrex.amegah@intel.com>
Signed-off-by: Andre Amegah <andrex.amegah@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61209*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/AppInterface.java b/src/java/com/android/internal/telephony/cat/AppInterface.java
//Synthetic comment -- index 01b86ef..02481df 100644

//Synthetic comment -- @@ -32,6 +32,14 @@
public static final String CAT_SESSION_END_ACTION =
"android.intent.action.stk.session_end";

    // Broadcast to be sent to Launcher to request user activity action
    public static final String CHECK_USER_ACTIVITY_ACTION =
            "android.intent.action.launcher.user_activity";

    // Broadcast to receive from Launcher when user activity occurs
    public static final String USER_ACTIVITY_AVAILABLE_ACTION =
            "android.intent.action.stk.user_activity_available";

/*
* Callback function from app to telephony to pass a result code and user's
* input back to the ICC.







