//<Beginning of snippet n. 0>


import dalvik.annotation.TestTargetNew;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;

private static final String NORMAL_URL = "http://www.google.com/";
private static final String SECURE_URL = "https://www.google.com/";

private boolean isTelephonyEnabled() {
    PackageManager pm = getContext().getPackageManager();
    return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
}

/**
* Assert target intent can be handled by at least one Activity.
* @param intent - the Intent will be handled.
*/
private void assertCanBeHandled(Intent intent) {
    List<ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentActivities(intent, 0);
    assertFalse("No activity can handle intent: " + intent, resolveInfos.isEmpty());
}

public void testDialPhoneNumber() {
    if (isTelephonyEnabled()) {
        Uri uri = Uri.parse("tel:(212)5551212");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    } else {
        // Handle case for devices without telephony capability
        fail("Device does not support telephony for ACTION_DIAL");
    }
}

public void testDialVoicemail() {
    if (isTelephonyEnabled()) {
        Uri uri = Uri.parse("voicemail:");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        assertCanBeHandled(intent);
    } else {
        // Handle case for devices without telephony capability
        fail("Device does not support telephony for ACTION_DIAL");
    }
}

//<End of snippet n. 0>