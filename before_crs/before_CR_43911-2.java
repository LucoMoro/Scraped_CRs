/*Grey out settings icon when it is in disabled state

If engine is not active, its settings icon is disabled in
Settings > Language & input > Text-to-speech output screen.
Currently, settings icons for all TTS engines are shown at
the same opacity. This fix dims settings icons of not-selected
engines.
Make sure that more than one TTS engine are installed on the
target, for e.g., Google Text-to-speech Engine and Classic
Text To Speech Engine (SVOX Classic TTS).

Change-Id:Ifa7de79814a2f4a4aa021cd8621cbfab41655680Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/tts/TtsEnginePreference.java b/src/com/android/settings/tts/TtsEnginePreference.java
//Synthetic comment -- index 3d612f0..81010a3 100644

//Synthetic comment -- @@ -80,6 +80,9 @@
private RadioButton mRadioButton;
private Intent mVoiceCheckData;

private final CompoundButton.OnCheckedChangeListener mRadioChangeListener =
new CompoundButton.OnCheckedChangeListener() {
@Override
//Synthetic comment -- @@ -136,6 +139,9 @@
// Will be enabled only the engine has passed the voice check, and
// is currently enabled.
mSettingsIcon.setEnabled(isChecked && mVoiceCheckData != null);
mSettingsIcon.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
//Synthetic comment -- @@ -169,7 +175,12 @@
// case mSettingsIcon && mRadioButton will be null. In this case
// getView will set the right values.
if (mSettingsIcon != null && mRadioButton != null) {
            mSettingsIcon.setEnabled(mRadioButton.isChecked());
}
}








