/*Phone: Allow showing Dialpad in Dialing state

With some service numbers, there will be a waiting message
played after the Dialing State. Some service numbers provide
the option for ignoring this waiting message. This is normally
done by sending DTMF tones. DialPad should be shown to the
user to make use of this option.

This fix will allow showing of dial pad during DIALING state.

Change-Id:I18ed7290a875241fa8f354424b2f658c2d3ded2bAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 21940*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index f8ba9c7..6777128 100755

//Synthetic comment -- @@ -3965,8 +3965,14 @@
// We can also dial while in ALERTING state because there are
// some connections that never update to an ACTIVE state (no
// indication from the network).

        // In-band tones are available even before the ALERTING state.
        // User should be provided with the option of skipping the
        // in-band tones. So, Dialpad should be shown even in the
        // DIALING state.
boolean canDial =
            (fgCallState == Call.State.DIALING || fgCallState == Call.State.ACTIVE
                || fgCallState == Call.State.ALERTING)
&& !hasRingingCall
&& (mApp.inCallUiState.inCallScreenMode != InCallScreenMode.MANAGE_CONFERENCE);








