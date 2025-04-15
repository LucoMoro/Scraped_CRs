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
//Synthetic comment -- index 196882f..7373f07 100644

//Synthetic comment -- @@ -79,6 +79,9 @@
private RadioButton mRadioButton;
private Intent mVoiceCheckData;

    // TODO: Remove and reference this from resources
    private static final float DISABLED_ALPHA = 0.4f;

private final CompoundButton.OnCheckedChangeListener mRadioChangeListener =
new CompoundButton.OnCheckedChangeListener() {
@Override
//Synthetic comment -- @@ -135,6 +138,9 @@
// Will be enabled only the engine has passed the voice check, and
// is currently enabled.
mSettingsIcon.setEnabled(isChecked && mVoiceCheckData != null);
        if (!isChecked) {
            mSettingsIcon.setAlpha(DISABLED_ALPHA);
        }
mSettingsIcon.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
//Synthetic comment -- @@ -168,7 +174,12 @@
// case mSettingsIcon && mRadioButton will be null. In this case
// getView will set the right values.
if (mSettingsIcon != null && mRadioButton != null) {
            if (mRadioButton.isChecked()) {
                mSettingsIcon.setEnabled(true);
            } else {
                mSettingsIcon.setEnabled(false);
                mSettingsIcon.setAlpha(DISABLED_ALPHA);
            }
}
}








