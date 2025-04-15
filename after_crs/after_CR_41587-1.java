/*Phone: Play an out of service tone if SIM card is removed during call

In the audio framework, if no tone is played at call disonnection, the output is not
released correctly in accordance to the platform audio drivers. Thus the audio is not
reset gracefully and the Modem ALSA driver not closed. As a result it is not possible
to playback a stream after a call ended due to a SIM removal. Playing an out of service
tone solves the problem.

Change-Id:I23a46951b903f9c01d3096a27dd80ff2f318187eAuthor: Vincent Becker <vincentx.becker@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 11908*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index eb88c75..f3fc4da 100755

//Synthetic comment -- @@ -1119,7 +1119,8 @@
} else if (cause == Connection.DisconnectCause.CDMA_DROP) {
if (DBG) log("- need to play CDMA_DROP tone!");
toneToPlay = InCallTonePlayer.TONE_CDMA_DROP;
            } else if (cause == Connection.DisconnectCause.OUT_OF_SERVICE || cause == Connection.DisconnectCause.ICC_ERROR) {
                // ICC_ERROR is SIM removal during call
if (DBG) log("- need to play OUT OF SERVICE tone!");
toneToPlay = InCallTonePlayer.TONE_OUT_OF_SERVICE;
} else if (cause == Connection.DisconnectCause.UNOBTAINABLE_NUMBER) {







