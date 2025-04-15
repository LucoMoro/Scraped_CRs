/*Rebind all prefs after restored to default settings

Change-Id:I5caa7848dc4edfe13e41311b74c238a571008edaSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessagingPreferenceActivity.java b/src/com/android/mms/ui/MessagingPreferenceActivity.java
//Synthetic comment -- index 28c8ae9..b468489 100755

//Synthetic comment -- @@ -86,6 +86,14 @@
super.onCreate(icicle);
addPreferencesFromResource(R.xml.preferences);

        bindAllPrefs();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setMessagePreferences();
    }

    private void bindAllPrefs() {
mManageSimPref = findPreference("pref_key_manage_sim_messages");
mSmsLimitPref = findPreference("pref_key_sms_delete_limit");
mSmsDeliveryReportPref = findPreference("pref_key_sms_delivery_reports");
//Synthetic comment -- @@ -98,10 +106,6 @@

mVibrateEntries = getResources().getTextArray(R.array.prefEntries_vibrateWhen);
mVibrateValues = getResources().getTextArray(R.array.prefValues_vibrateWhen);
}

@Override
//Synthetic comment -- @@ -252,6 +256,9 @@
.edit().clear().apply();
setPreferenceScreen(null);
addPreferencesFromResource(R.xml.preferences);

        bindAllPrefs();

setMessagePreferences();
}








