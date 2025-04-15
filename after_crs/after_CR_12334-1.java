/*Create a default calendar if there's none from GLS.*/




//Synthetic comment -- diff --git a/src/com/android/calendar/LaunchActivity.java b/src/com/android/calendar/LaunchActivity.java
//Synthetic comment -- index 1f053d7..3d61585 100644

//Synthetic comment -- @@ -20,13 +20,18 @@
import com.google.android.googlelogin.GoogleLoginServiceHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Calendar.Calendars;
import android.provider.Gmail;

import java.util.TimeZone;

public class LaunchActivity extends Activity {
static final String KEY_DETAIL_VIEW = "DETAIL_VIEW";

//Synthetic comment -- @@ -94,16 +99,47 @@
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
super.onActivityResult(requestCode, resultCode, intent);
if (requestCode == GET_ACCOUNT_REQUEST) {
            String account = null;
if (resultCode == RESULT_OK) {
                // if we got a response from the sub-activity, it's supposed to hold account data
if (intent != null) {
Bundle extras = intent.getExtras();
if (extras != null) {
account = extras.getString(GoogleLoginServiceConstants.AUTH_ACCOUNT_KEY);
}
}
} else {
                // otherwise, create a local calendar if there isn't one already
                Cursor cur = getContentResolver().query(Calendars.CONTENT_URI,
                        null, null, null, null);
                if (cur != null) {
                    if (cur.getCount() != 0) {
                        cur.moveToFirst();
                        try {
                            account = cur.getString(cur.getColumnIndexOrThrow(Calendars.NAME));
                        } catch(RuntimeException e) {
                            // ignore - this leaves account == null, which is fine
                        }
                    } else {
                        account = "nobody@localhost";
                        // inspired from CalendarProvider.onAccountsChanged
                        ContentValues vals = new ContentValues();
                        vals.put(Calendars.ACCESS_LEVEL, Integer.toString(Calendars.OWNER_ACCESS));
                        vals.put(Calendars.COLOR, -14069085);
                        vals.put(Calendars.DISPLAY_NAME, "Default");
                        vals.put(Calendars.HIDDEN, 0);
                        vals.put(Calendars.NAME, account);
                        vals.put(Calendars.SELECTED, 1);
                        vals.put(Calendars.SYNC_EVENTS, 1);
                        vals.put(Calendars.TIMEZONE, TimeZone.getDefault().getID());
                        getContentResolver().insert(Calendars.CONTENT_URI, vals);
                    }
                    cur.close();
                }
            }
            if (account != null) {
                onAccountsLoaded(account);
            } else {
finish();
}
}







