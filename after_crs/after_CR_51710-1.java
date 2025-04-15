/*avoid stopping app in case of accessing empty db

When accessing empty database, Setting App is stopped, sometimes.
To avoid stopping app, return if occuring SQLException

Change-Id:I71ec067f502d12a9215a9abdbe9e23fc07af17bb*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnSettings.java b/src/com/android/settings/ApnSettings.java
//Synthetic comment -- index de1ce63..088aead 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -160,9 +161,14 @@
+ android.os.SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "")
+ "\"";

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Telephony.Carriers.CONTENT_URI, new String[] {
                    "_id", "name", "apn", "type"}, where, null,
                    Telephony.Carriers.DEFAULT_SORT_ORDER);
        } catch (SQLException e) {
            return;
        }

PreferenceGroup apnList = (PreferenceGroup) findPreference("apn_list");
apnList.removeAll();







