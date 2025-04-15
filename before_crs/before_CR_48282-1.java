/*Settings: crashed ANR on 'com.android.settings'

Too much insert operations casue it and When updating user
dictionary preference, load words will also consume much time.
Asynchronous update user dictionary preference.

Change-Id:I3ae770462b674560f6d2516e460e4a32f2a67f98Author: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: b619 <b619@borqs.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 59662*/
//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/InputMethodAndLanguageSettings.java b/src/com/android/settings/inputmethod/InputMethodAndLanguageSettings.java
//Synthetic comment -- index c2ff0d9..7abfe22 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.hardware.input.KeyboardLayout;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
//Synthetic comment -- @@ -89,6 +90,7 @@
@SuppressWarnings("unused")
private SettingsObserver mSettingsObserver;
private Intent mIntentWaitingForResult;

@Override
public void onCreate(Bundle icicle) {
//Synthetic comment -- @@ -191,9 +193,7 @@
}
}

    private void updateUserDictionaryPreference(Preference userDictionaryPreference) {
        final Activity activity = getActivity();
        final TreeSet<String> localeList = UserDictionaryList.getUserDictionaryLocalesSet(activity);
if (null == localeList) {
// The locale list is null if and only if the user dictionary service is
// not present or disabled. In this case we need to remove the preference.
//Synthetic comment -- @@ -221,6 +221,29 @@
}
}

@Override
public void onResume() {
super.onResume();
//Synthetic comment -- @@ -251,7 +274,14 @@
}
}

            updateUserDictionaryPreference(findPreference(KEY_USER_DICTIONARY_SETTINGS));
if (SHOW_INPUT_METHOD_SWITCHER_SETTINGS) {
mShowInputMethodSelectorPref.setOnPreferenceChangeListener(this);
}
//Synthetic comment -- @@ -278,7 +308,10 @@
@Override
public void onPause() {
super.onPause();

mIm.unregisterInputDeviceListener(this);
mSettingsObserver.pause();








