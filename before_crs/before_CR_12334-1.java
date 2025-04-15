/*Create a default calendar if there's none from GLS.*/
//Synthetic comment -- diff --git a/src/com/android/calendar/LaunchActivity.java b/src/com/android/calendar/LaunchActivity.java
//Synthetic comment -- index 1f053d7..3d61585 100644

//Synthetic comment -- @@ -20,13 +20,18 @@
import com.google.android.googlelogin.GoogleLoginServiceHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Gmail;

public class LaunchActivity extends Activity {
static final String KEY_DETAIL_VIEW = "DETAIL_VIEW";

//Synthetic comment -- @@ -94,16 +99,47 @@
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
super.onActivityResult(requestCode, resultCode, intent);
if (requestCode == GET_ACCOUNT_REQUEST) {
if (resultCode == RESULT_OK) {
if (intent != null) {
Bundle extras = intent.getExtras();
if (extras != null) {
                        final String account;
account = extras.getString(GoogleLoginServiceConstants.AUTH_ACCOUNT_KEY);
                        onAccountsLoaded(account);
}
}
} else {
finish();
}
}







