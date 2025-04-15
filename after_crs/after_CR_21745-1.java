/*InCallScreen, fix setup call when not registered to network

Added possibility to place calls while not being registered
to a network, since a location update procedure should be
performed if there is a non-forbidden network available.

Change-Id:Ibf96133b18c0f23238a423afee6ce337e3b24f90Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 088f979..f7ac33f 100755

//Synthetic comment -- @@ -2758,7 +2758,9 @@

switch (state) {
case ServiceState.STATE_IN_SERVICE:
            case ServiceState.STATE_OUT_OF_SERVICE:
// Normal operation.  It's OK to make outgoing calls.
                // No network connection. It's OK to try to make outgoing calls.
return InCallInitStatus.SUCCESS;

case ServiceState.STATE_POWER_OFF:
//Synthetic comment -- @@ -2776,10 +2778,6 @@
// "incall_error_emergency_only" are totally unused.
return InCallInitStatus.EMERGENCY_ONLY;

default:
throw new IllegalStateException("Unexpected ServiceState: " + state);
}







