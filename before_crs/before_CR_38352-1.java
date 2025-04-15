/*Make sure language is loaded by the PicoTTS engine

It seems like a TODO was forgotten, this causes the PicoTTS
not to work and a NPE is thrown when pressing
"Listen to an example" in the Settings app.

Change-Id:I4f123291de8be2dd9e96204753150e9e64053f18*/
//Synthetic comment -- diff --git a/pico/compat/src/com/android/tts/compat/CompatTtsService.java b/pico/compat/src/com/android/tts/compat/CompatTtsService.java
//Synthetic comment -- index af65ba4..a34cb8f 100755

//Synthetic comment -- @@ -101,7 +101,9 @@
protected int onLoadLanguage(String lang, String country, String variant) {
if (DBG) Log.d(TAG, "onLoadLanguage(" + lang + "," + country + "," + variant + ")");
int result = onIsLanguageAvailable(lang, country, variant);
        // TODO: actually load language
return result;
}








