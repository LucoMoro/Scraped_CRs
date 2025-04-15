/*Grey out settings icon when it is in disabled state

If engine is not active, its settings icon is disabled in
Settings > Language & input > Text-to-speech output screen.
Currently, settings icons for all TTS engines are shown at
the same opacity. This fix dims settings icons of not-selected
engines.
Make sure that more than one TTS engine are installed on the
target, for e.g., Google Text-to-speech Engine and Classic
Text To Speech Engine (SVOX Classic TTS).

Additionally, since setAlpha() is used in multiple places within
Settings package, moved DISABLED_ALPHA declaration to Utils.java
in order to have single point of reference.

Change-Id:Ifa7de79814a2f4a4aa021cd8621cbfab41655680Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/DreamSettings.java b/src/com/android/settings/DreamSettings.java
//Synthetic comment -- index 32328d9..23285c0 100644

//Synthetic comment -- @@ -316,7 +316,7 @@

ImageView settingsButton = (ImageView) row.findViewById(android.R.id.button2);
settingsButton.setVisibility(showSettings ? View.VISIBLE : View.INVISIBLE);
            settingsButton.setAlpha(dreamInfo.isActive ? 1f : 0.33f);
settingsButton.setEnabled(dreamInfo.isActive);
settingsButton.setOnClickListener(new OnClickListener(){
@Override








//Synthetic comment -- diff --git a/src/com/android/settings/Utils.java b/src/com/android/settings/Utils.java
//Synthetic comment -- index 6d76bed..4b0a753 100644

//Synthetic comment -- @@ -80,6 +80,11 @@
public static final int UPDATE_PREFERENCE_FLAG_SET_TITLE_TO_MATCHING_ACTIVITY = 1;

/**
* Name of the meta-data item that should be set in the AndroidManifest.xml
* to specify the icon that should be displayed for the preference.
*/








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/CheckBoxAndSettingsPreference.java b/src/com/android/settings/inputmethod/CheckBoxAndSettingsPreference.java
//Synthetic comment -- index 28b8616..f440bc8 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -29,7 +30,6 @@
import android.widget.TextView;

public class CheckBoxAndSettingsPreference extends CheckBoxPreference {
    private static final float DISABLED_ALPHA = 0.4f;

private SettingsPreferenceFragment mFragment;
private TextView mTitleText;
//Synthetic comment -- @@ -103,7 +103,7 @@
mSettingsButton.setClickable(checked);
mSettingsButton.setFocusable(checked);
if (!checked) {
                    mSettingsButton.setAlpha(DISABLED_ALPHA);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/InputMethodPreference.java b/src/com/android/settings/inputmethod/InputMethodPreference.java
//Synthetic comment -- index 103481e..f064c08 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.app.AlertDialog;
import android.app.Fragment;
//Synthetic comment -- @@ -47,7 +48,6 @@
public class InputMethodPreference extends CheckBoxPreference
implements Comparator<InputMethodPreference> {
private static final String TAG = InputMethodPreference.class.getSimpleName();
    private static final float DISABLED_ALPHA = 0.4f;
private final SettingsPreferenceFragment mFragment;
private final InputMethodInfo mImi;
private final InputMethodManager mImm;
//Synthetic comment -- @@ -172,7 +172,7 @@
mInputMethodSettingsButton.setClickable(checked);
mInputMethodSettingsButton.setFocusable(checked);
if (!checked) {
                mInputMethodSettingsButton.setAlpha(DISABLED_ALPHA);
}
}
if (mTitleText != null) {








//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/SingleSpellCheckerPreference.java b/src/com/android/settings/inputmethod/SingleSpellCheckerPreference.java
//Synthetic comment -- index 5b28142..5ea8bd7 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.settings.inputmethod;

import com.android.settings.R;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
//Synthetic comment -- @@ -37,7 +38,6 @@
import android.widget.Toast;

public class SingleSpellCheckerPreference extends Preference {
    private static final float DISABLED_ALPHA = 0.4f;
private static final String TAG = SingleSpellCheckerPreference.class.getSimpleName();
private static final boolean DBG = false;

//Synthetic comment -- @@ -198,7 +198,7 @@
mSettingsButton.setClickable(enabled);
mSettingsButton.setFocusable(enabled);
if (!enabled) {
                    mSettingsButton.setAlpha(DISABLED_ALPHA);
}
}
}
//Synthetic comment -- @@ -210,7 +210,7 @@
mSubtypeButton.setClickable(enabled);
mSubtypeButton.setFocusable(enabled);
if (!enabled) {
                    mSubtypeButton.setAlpha(DISABLED_ALPHA);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/settings/tts/TtsEnginePreference.java b/src/com/android/settings/tts/TtsEnginePreference.java
//Synthetic comment -- index 3d612f0..21ef81d 100644

//Synthetic comment -- @@ -30,6 +30,7 @@


import com.android.settings.R;


public class TtsEnginePreference extends Preference {
//Synthetic comment -- @@ -136,6 +137,9 @@
// Will be enabled only the engine has passed the voice check, and
// is currently enabled.
mSettingsIcon.setEnabled(isChecked && mVoiceCheckData != null);
mSettingsIcon.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
//Synthetic comment -- @@ -169,7 +173,12 @@
// case mSettingsIcon && mRadioButton will be null. In this case
// getView will set the right values.
if (mSettingsIcon != null && mRadioButton != null) {
            mSettingsIcon.setEnabled(mRadioButton.isChecked());
}
}








