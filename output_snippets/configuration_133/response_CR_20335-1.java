//<Beginning of snippet n. 0>


import dalvik.annotation.TestTargetNew;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.test.AndroidTestCase;

import java.util.List;
private static final String NORMAL_URL = "http://www.google.com/";
private static final String SECURE_URL = "https://www.google.com/";
private static final String TAG = "TelephonyTests";

/**
* Assert target intent can be handled by at least one Activity.
* @param intent - the Intent will be handled.
*/
private void assertCanBeHandled(Intent intent) {
    PackageManager packageManager = getContext().getPackageManager();
    List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
    if (resolveInfos.isEmpty()) {
        throw new IllegalStateException("Intent cannot be handled: " + intent.toString());
    }
    assertFalse("Intent cannot be handled: " + intent.toString(), resolveInfos.isEmpty());
}

public void testDialPhoneNumber() {
    if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        Uri uri = Uri.parse("tel:(212)5551212");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    } else {
        // Alternative actions defined
        Log.i(TAG, "Validating creation of dial intent for non-telephony device.");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        assertCanBeHandled(intent);
    }
}

public void testDialVoicemail() {
    if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        Uri uri = Uri.parse("voicemail:");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    } else {
        // Alternative actions defined
        Log.i(TAG, "Validating creation of dial intent for non-telephony device.");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("voicemail:"));
        assertCanBeHandled(intent);
    }
}

//<End of snippet n. 0>