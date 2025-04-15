/*Added null pointer checking

Change-Id:I670df1bd2a284db85dd061bca34c34cf09749c58*/
//Synthetic comment -- diff --git a/core/java/android/speech/tts/BlockingAudioTrack.java b/core/java/android/speech/tts/BlockingAudioTrack.java
//Synthetic comment -- index fcadad7..a8506ca 100644

//Synthetic comment -- @@ -120,6 +120,9 @@
}

public void waitAndRelease() {
// For "small" audio tracks, we have to stop() them to make them mixable,
// else the audio subsystem will wait indefinitely for us to fill the buffer
// before rendering the track mixable.







