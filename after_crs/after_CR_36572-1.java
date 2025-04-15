/*Only play sound effects if stream is not muted.

Calling playSoundEffect() currently results in audio data being sent to
audioflinger regardless of whether or not the system stream is muted.

Although this already produces no audio, when nothing else is using the
audio system it does have the less desirable effect of waking up the
hardware to play 3+ seconds of silence before audioflinger goes idle
again - probably a waste of power.

Change-Id:I81a6cb17efe4036285f4af0567c3f0a31dda1152Signed-off-by: David Overton <therealdave32@gmail.com>*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioService.java b/media/java/android/media/AudioService.java
//Synthetic comment -- index 37aacab..62eb857 100644

//Synthetic comment -- @@ -2275,7 +2275,9 @@
break;

case MSG_PLAY_SOUND_EFFECT:
                    if (!isStreamMute(AudioSystem.STREAM_SYSTEM)) {
                        playSoundEffect(msg.arg1, msg.arg2);
                    }
break;

case MSG_BTA2DP_DOCK_TIMEOUT:







