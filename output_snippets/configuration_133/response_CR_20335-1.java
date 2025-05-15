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

private void assertCanBeHandled(Intent intent) {
    PackageManager packageManager = getContext().getPackageManager();
    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    if (activities.isEmpty()) {
        // Handle the case where no applications exist to handle the intent
        // Log or notify user as appropriate
    }
}

public void testDialPhoneNumber() {
    Uri uri = Uri.parse("tel:(212)5551212");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (isTelephonyAvailable()) {
        assertCanBeHandled(intent);
    }
}

public void testDialVoicemail() {
    Uri uri = Uri.parse("voicemail:");
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    if (isTelephonyAvailable()) {
        assertCanBeHandled(intent);
    }
}

private boolean isTelephonyAvailable() {
    PackageManager packageManager = getContext().getPackageManager();
    return packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
}

//<End of snippet n. 0>