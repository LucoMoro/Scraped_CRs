/*Phone: Allow showing Dialpad in Dialing state

With some service numbers, there will be a waiting message
played after the Dialing State. Some service numbers provide
the option for ignoring this waiting message. This is normally
done by sending DTMF tones. DialPad should be shown to the
user to make use of this option.

This fix will allow showing of dial pad during DIALING state.

Change-Id:Ie228666430966ad902be3b4f90f2613f6d28b119Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 21940*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 2cc22a0..bca27de 100755

//Synthetic comment -- @@ -3997,9 +3997,14 @@
// We can also dial while in ALERTING state because there are
// some connections that never update to an ACTIVE state (no
// indication from the network).

        // In-band tones are available even before the ALERTING state.
        // User should be provided with the option of skipping the
        // in-band tones. So, Dialpad should be shown even in the
        // DIALING state.
boolean canDial =
            (fgCallState == Call.State.DIALING || fgCallState == Call.State.ACTIVE
            || fgCallState == Call.State.ALERTING) && !hasRingingCall
&& (mApp.inCallUiState.inCallScreenMode != InCallScreenMode.MANAGE_CONFERENCE);

if (VDBG) log ("[okToDialDTMFTones] foreground state: " + fgCallState +







