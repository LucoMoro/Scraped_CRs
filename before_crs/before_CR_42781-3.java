/*PicoTTS: Set mNativeSynth prior to TextToSpeechService#onCreate

The variable mNativeSynth is used by TextToSpeechService#onCreate so it must be set prior
to that call. Otherwise, the method TextToSpeech#onIsLanguageAvailable will return
TextToSpeech.ERROR because the synthesizer is not ready.

Patch 2: Better comments
Patch 3: Add a comment for explain the getContentResolver() status after this change

Note: Seehttps://android-review.googlesource.com/#/c/42578/for the discussion about this
change

Change-Id:Ie211d54ee5a39eae721b7732eb3aaa01aed65a16*/
//Synthetic comment -- diff --git a/pico/compat/src/com/android/tts/compat/CompatTtsService.java b/pico/compat/src/com/android/tts/compat/CompatTtsService.java
//Synthetic comment -- index 475afaf..dde706b 100755

//Synthetic comment -- @@ -37,7 +37,6 @@
@Override
public void onCreate() {
if (DBG) Log.d(TAG, "onCreate()");
        super.onCreate();

String soFilename = getSoFilename();

//Synthetic comment -- @@ -71,6 +70,13 @@
c.close();
}
mNativeSynth = new SynthProxy(soFilename, engineConfig);
}

@Override







