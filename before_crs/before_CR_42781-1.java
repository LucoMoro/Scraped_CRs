/*PicoTTS: mNativeSynth must be set previously to call super.onCreate()

The variable mNativeSynth must be set, previously to call super.onCreate()
on CompatTtsService#onCreate, because this method make use of mNativeSynth
when calls TextToSpeech#isLanguageAvailable.

Note: Seehttps://android-review.googlesource.com/#/c/42578/for the discussion about this
change

Change-Id:Ie211d54ee5a39eae721b7732eb3aaa01aed65a16*/
//Synthetic comment -- diff --git a/pico/compat/src/com/android/tts/compat/CompatTtsService.java b/pico/compat/src/com/android/tts/compat/CompatTtsService.java
//Synthetic comment -- index 475afaf..9c06849 100755

//Synthetic comment -- @@ -37,7 +37,6 @@
@Override
public void onCreate() {
if (DBG) Log.d(TAG, "onCreate()");
        super.onCreate();

String soFilename = getSoFilename();

//Synthetic comment -- @@ -71,6 +70,10 @@
c.close();
}
mNativeSynth = new SynthProxy(soFilename, engineConfig);
}

@Override







