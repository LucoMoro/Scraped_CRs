/*Unregister ContentObserver in InputMethodAndLanguageSettings class

A content observer (SettingsObserver) is registered when
InputMethodAndLanguageSettings is created, but it is never unregistered.

Added resume() and pause() methods to SettingsObserver inner class
to register/unregister the content observer when InputMethodAndLanguageSettings
is resumed/paused.

Change-Id:I6d4cc2625a5fd8435967d930f46e8fe86eda4714Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/inputmethod/InputMethodAndLanguageSettings.java b/src/com/android/settings/inputmethod/InputMethodAndLanguageSettings.java
//Synthetic comment -- index 4454389..aa2bcf4 100644

//Synthetic comment -- @@ -172,6 +172,7 @@
@Override
public void onResume() {
super.onResume();
        mSettingsObserver.resume();
if (!mIsOnlyImeSettings) {
if (mLanguagePref != null) {
Configuration conf = getResources().getConfiguration();
//Synthetic comment -- @@ -207,6 +208,7 @@
@Override
public void onPause() {
super.onPause();
        mSettingsObserver.pause();
if (SHOW_INPUT_METHOD_SWITCHER_SETTINGS) {
mShowInputMethodSelectorPref.setOnPreferenceChangeListener(null);
}
//Synthetic comment -- @@ -359,17 +361,27 @@
}

private class SettingsObserver extends ContentObserver {
        private Context mContext;

public SettingsObserver(Handler handler, Context context) {
super(handler);
            mContext = context;
        }

        @Override public void onChange(boolean selfChange) {
            updateCurrentImeName();
        }

        public void resume() {
            final ContentResolver cr = mContext.getContentResolver();
cr.registerContentObserver(
Settings.Secure.getUriFor(Settings.Secure.DEFAULT_INPUT_METHOD), false, this);
cr.registerContentObserver(Settings.Secure.getUriFor(
Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE), false, this);
}

        public void pause() {
            mContext.getContentResolver().unregisterContentObserver(this);
}
}
}







