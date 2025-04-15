/*avoid stopping app in case of accessing empty db

When accessing empty database, Setting App is stopped, sometimes.
To avoid stopping app, check null if occuring SQLException

Change-Id:I71ec067f502d12a9215a9abdbe9e23fc07af17bb*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnSettings.java b/src/com/android/settings/ApnSettings.java
//Synthetic comment -- index de1ce63..ddc44fa 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -160,47 +161,54 @@
+ android.os.SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "")
+ "\"";

        Cursor cursor = getContentResolver().query(Telephony.Carriers.CONTENT_URI, new String[] {
                "_id", "name", "apn", "type"}, where, null,
                Telephony.Carriers.DEFAULT_SORT_ORDER);

        PreferenceGroup apnList = (PreferenceGroup) findPreference("apn_list");
        apnList.removeAll();

        ArrayList<Preference> mmsApnList = new ArrayList<Preference>();

        mSelectedKey = getSelectedApnKey();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(NAME_INDEX);
            String apn = cursor.getString(APN_INDEX);
            String key = cursor.getString(ID_INDEX);
            String type = cursor.getString(TYPES_INDEX);

            ApnPreference pref = new ApnPreference(this);

            pref.setKey(key);
            pref.setTitle(name);
            pref.setSummary(apn);
            pref.setPersistent(false);
            pref.setOnPreferenceChangeListener(this);

            boolean selectable = ((type == null) || !type.equals("mms"));
            pref.setSelectable(selectable);
            if (selectable) {
                if ((mSelectedKey != null) && mSelectedKey.equals(key)) {
                    pref.setChecked();
                }
                apnList.addPreference(pref);
            } else {
                mmsApnList.add(pref);
            }
            cursor.moveToNext();
}
        cursor.close();

        for (Preference preference : mmsApnList) {
            apnList.addPreference(preference);
}
}

//Synthetic comment -- @@ -330,10 +338,15 @@
switch (msg.what) {
case EVENT_RESTORE_DEFAULTAPN_START:
ContentResolver resolver = getContentResolver();
                    resolver.delete(DEFAULTAPN_URI, null, null);                    
                    mRestoreApnUiHandler
                        .sendEmptyMessage(EVENT_RESTORE_DEFAULTAPN_COMPLETE);
                    break;
}
}
}







